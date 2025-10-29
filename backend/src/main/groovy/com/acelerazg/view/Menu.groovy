package com.acelerazg.view

import com.acelerazg.controller.CandidateController
import com.acelerazg.controller.CompanyController
import com.acelerazg.controller.JobController
import com.acelerazg.controller.MatchController
import com.acelerazg.dao.*
import com.acelerazg.dto.CreateCandidateDTO
import com.acelerazg.dto.CreateCompanyDTO
import com.acelerazg.dto.CreateJobDTO
import com.acelerazg.dto.MatchDTO
import com.acelerazg.exceptions.EmptyCollectionException
import com.acelerazg.model.*
import com.acelerazg.service.CandidateService
import com.acelerazg.service.CompanyService
import com.acelerazg.service.JobService
import com.acelerazg.service.MatchService
import com.acelerazg.view.input.InputReader
import com.acelerazg.view.input.InputValidator
import groovy.transform.CompileStatic

import java.time.LocalDate

@CompileStatic
class Menu {
    private final CandidateController candidateController
    private final CompanyController companyController
    private final JobController jobController
    private final MatchController matchController
    private final Scanner appScanner
    private final InputValidator inputValidator
    private final InputReader inputReader

    Menu() {
        AddressDAO addressDao = new AddressDAO()
        PersonDAO personDao = new PersonDAO()
        CompetencyDAO competencyDao = new CompetencyDAO()
        CandidateDAO candidateDao = new CandidateDAO(addressDao, personDao, competencyDao)
        CompanyDAO companyDao = new CompanyDAO(addressDao, personDao)
        MatchEventDAO matchEventDAO = new MatchEventDAO()
        JobDAO jobDAO = new JobDAO(addressDao, competencyDao)

        CandidateService candidateService = new CandidateService(candidateDao)
        candidateController = new CandidateController(candidateService)
        CompanyService companyService = new CompanyService(companyDao, candidateDao, matchEventDAO, jobDAO)
        companyController = new CompanyController(companyService)
        JobService jobService = new JobService(jobDAO)
        jobController = new JobController(jobService)
        MatchService matchService = new MatchService(matchEventDAO)
        matchController = new MatchController(matchService)

        appScanner = new Scanner(System.in)
        inputValidator = new InputValidator()
        inputReader = new InputReader(competencyDao, appScanner, inputValidator)
    }

    static void run() {
        Menu menu = new Menu()
        menu.showMenu()
    }

    void showMenu() {
        println "Welcome to Linketinder!"

        String appInput = ""
        while (appInput != "q") {
            println "Insert the desired operation:"
            println "1 - List all candidates"
            println "2 - List all companies"
            println "3 - Create a candidate"
            println "4 - Create a company"
            println "5 - Add a Job to a company"
            println "6 - Like a job as an candidate"
            println "7 - Like a candidate as a company"
            println "8 - List all matches of a job"
            println "q - Exit the application"
            appInput = appScanner.nextLine()

            Runnable command = commands.get(appInput)
            if (command) {
                command.run()
            } else {
                println "Invalid option."
            }
        }
        appScanner.close()
    }

    private final Map<String, Runnable> commands = ["1": { candidateController.handleGetAll().forEach { Candidate c -> println c } },
                                                    "2": { companyController.handleGetAllCompanies().forEach { Company co -> println co } },
                                                    "3": this.&createCandidateCase,
                                                    "4": this.&createCompanyCase,
                                                    "5": this.&createJobCase,
                                                    "6": this.&likeJobAsCandidate,
                                                    "7": this.&likeCandidateAsCompany,
                                                    "8": this.&listAllMatchesOfJob,
                                                    "q": { println "Terminating application." }] as Map<String, Runnable>

    private void createCandidateCase() {
        String candidateDescription = inputReader.readValidatedString("Description: ")
        String candidatePassword = inputReader.readValidatedString("Password: ")
        String candidateEmail = inputReader.readValidatedString("Email: ")
        String candidateFirstName = inputReader.readValidatedString("First Name: ")
        String candidateLastName = inputReader.readValidatedString("Last Name: ")
        String candidateCpf = inputReader.readValidatedString("CPF: ", 11)
        LocalDate candidateBirthday = inputReader.readDate("Birthday: (yyyy-MM-dd) ")
        String candidateGraduation = inputReader.readValidatedString("Graduation: ")
        Address candidateAddress = inputReader.readAddress()
        List<Competency> candidateCompetencies = inputReader.readCompetencies()

        CreateCandidateDTO createCandidateDTO = new CreateCandidateDTO(candidateDescription,
                candidatePassword,
                candidateEmail,
                candidateFirstName,
                candidateLastName,
                candidateCpf,
                candidateBirthday,
                candidateGraduation,
                candidateAddress,
                candidateCompetencies)

        Candidate candidate = candidateController.handleCreateCandidate(createCandidateDTO)

        println candidate ? "Candidate added successfully!" : "Something went wrong."
    }

    private void createCompanyCase() {
        String companyDescription = inputReader.readValidatedString("Description: ")
        String companyPassword = inputReader.readValidatedString("Password: ")
        String companyEmail = inputReader.readValidatedString("Email: ")
        String companyName = inputReader.readValidatedString("Name:")
        String companyCnpj = inputReader.readValidatedString("CNPJ:")
        Address companyAddress = inputReader.readAddress()

        CreateCompanyDTO createCompanyDTO = new CreateCompanyDTO(companyDescription,
                companyPassword,
                companyEmail,
                companyName,
                companyCnpj,
                companyAddress)

        Company company = companyController.handleCreateCompany(createCompanyDTO)

        println company ? "Company added successfully!" : "Something went wrong."
    }

    private void createJobCase() {
        int idCompany

        try {
            idCompany = selectEntityFromCollection(companyController.handleGetAllCompanies(), "Company", "idCompany")
        } catch (EmptyCollectionException e) {
            println e.message
            return
        }

        String jobName = inputReader.readValidatedString("Name:")
        String jobDescription = inputReader.readValidatedString("Description:")
        Address jobAddress = inputReader.readAddress()
        List<Competency> jobCompetencies = inputReader.readCompetencies()

        CreateJobDTO createJobDTO = new CreateJobDTO(jobName,
                jobDescription,
                idCompany,
                jobAddress,
                jobCompetencies)

        Job job = jobController.handleCreateJob(createJobDTO)

        println job ? "Job added successfully!" : "Something went wrong."
    }

    private void likeJobAsCandidate() {
        int idCandidate
        int idCompany
        int idJob

        try {
            idCandidate = selectEntityFromCollection(candidateController.handleGetAll(), "Candidate", "idCandidate")
            idCompany = selectEntityFromCollection(companyController.handleGetAllCompanies(), "Company", "idCompany")
            idJob = selectEntityFromCollection(jobController.handleGetAllByCompanyId(idCompany), "Job")
        } catch (EmptyCollectionException e) {
            println e.message
            return
        }

        if (candidateController.handleLikeJob(idCandidate, idJob)) {
            println "Like added successfully!"
        } else {
            println "This job is already liked!"
        }
    }

    private void likeCandidateAsCompany() {
        int idCandidate
        int idCompany
        int idJob

        try {
            idCompany = selectEntityFromCollection(companyController.handleGetAllCompanies(), "Company", "idCompany")
            idJob = selectEntityFromCollection(jobController.handleGetAllByCompanyId(idCompany), "Job")
            idCandidate = selectEntityFromCollection(candidateController.handleGetAllInterestedInJob(idJob), "Candidate")
        } catch (EmptyCollectionException e) {
            println e.message
            return
        }

        LikeResult response = companyController.handleLikeCandidate(idCompany, idCandidate)
        switch (response) {
            case LikeResult.ALREADY_LIKED:
                println "This candidate is already matched!"
                break
            case LikeResult.SUCCESS:
                println "Like added successfully!"
                break
            case LikeResult.MATCH_FOUND:
                println "You Matched successfully with this candidate!"
                break
        }
    }

    private void listAllMatchesOfJob() {
        int idCompany
        int idJob

        try {
            idCompany = selectEntityFromCollection(companyController.handleGetAllCompanies(), "Company", "idCompany")
            idJob = selectEntityFromCollection(jobController.handleGetAllByCompanyId(idCompany), "Job")
        } catch (EmptyCollectionException e) {
            println e.message
            return
        }

        List<MatchDTO> allMatches = matchController.handleGetAllMatchesByJobId(idJob)
        if (allMatches.isEmpty()) return
        allMatches.forEach { MatchDTO m -> println m }
    }

    private <T> int selectEntityFromCollection(List<T> items, String label, String idField = "id") {
        if (items.isEmpty()) {
            throw new EmptyCollectionException("No records found for ${label.toLowerCase()}.")
        }
        items.forEach { T it -> println it }
        return inputReader.readId(items, "${label} Id: ", idField)
    }
}