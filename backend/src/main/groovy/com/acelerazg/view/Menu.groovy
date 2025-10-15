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
import com.acelerazg.model.*
import com.acelerazg.utils.InputReader
import groovy.transform.CompileStatic

import java.time.LocalDate

@CompileStatic
class Menu {
    private final CandidateController candidateController
    private final CompanyController companyController
    private final JobController jobController
    private final MatchController matchController
    private final InputReader inputReader
    private final Scanner appScanner

    Menu() {
        AddressDAO addressDao = new AddressDAO()
        PersonDAO personDao = new PersonDAO()
        CompetencyDAO competencyDao = new CompetencyDAO()
        CandidateDAO candidateDao = new CandidateDAO(addressDao, personDao, competencyDao)
        CompanyDAO companyDao = new CompanyDAO(addressDao, personDao)
        MatchEventDAO matchEventDAO = new MatchEventDAO()
        JobDAO jobDAO = new JobDAO(addressDao, competencyDao)

        candidateController = new CandidateController(candidateDao)
        companyController = new CompanyController(companyDao, candidateDao, matchEventDAO, jobDAO)
        jobController = new JobController(jobDAO)
        matchController = new MatchController(matchEventDAO)
        appScanner = new Scanner(System.in)
        inputReader = new InputReader(competencyDao, appScanner)
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

    private final Map<String, Runnable> commands = ["1": { candidateController.handleGetAll().forEach { println it } },
                                                    "2": { companyController.handleGetAll().forEach { println it } },
                                                    "3": this.&createCandidateCase,
                                                    "4": this.&createCompanyCase,
                                                    "5": this.&createJobCase,
                                                    "6": this.&likeJobAsCandidate,
                                                    "7": this.&likeCandidateAsCompany,
                                                    "8": this.&listAllMatchesOfJob,
                                                    "q": { println "Terminating application." }] as Map<String, Runnable>

    private void createCandidateCase() {
        String candidateDescription = inputReader.readNonEmpty("Description: ", "The description cannot be empty.")
        String candidatePassword = inputReader.readNonEmpty("Password: ", "The password cannot be empty.")
        String candidateEmail = inputReader.readNonEmpty("Email: ", "The email cannot be empty.")
        String candidateFirstName = inputReader.readNonEmpty("First Name: ", "The first name cannot be empty.")
        String candidateLastName = inputReader.readNonEmpty("Last Name: ", "The last name cannot be empty.")
        String candidateCpf = inputReader.readNonEmpty("CPF: ", "The CPF cannot be empty.", 11)
        LocalDate candidateBirthday = inputReader.readDate("Birthday: (yyyy-MM-dd) ", "Invalid date. Try again.")
        String candidateGraduation = inputReader.readNonEmpty("Graduation: ", "The graduation cannot be empty")
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
        String companyDescription = inputReader.readNonEmpty("Description: ", "The description cannot be empty.")
        String companyPassword = inputReader.readNonEmpty("Password: ", "The password cannot be empty.")
        String companyEmail = inputReader.readNonEmpty("Email: ", "The email cannot be empty.")
        String companyName = inputReader.readNonEmpty("Name:", "The name cannot be empty")
        String companyCnpj = inputReader.readNonEmpty("CNPJ:", "The cnpj cannot be empty")
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
        int idCompany = selectEntityFromCollection(companyController.handleGetAll(), "Company", "idCompany")

        String jobName = inputReader.readNonEmpty("Name:", "The name cannot be empty")
        String jobDescription = inputReader.readNonEmpty("Description:", "The description cannot be empty")
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
        int idCandidate = selectEntityFromCollection(candidateController.handleGetAll(), "Candidate", "idCandidate")
        int idCompany = selectEntityFromCollection(companyController.handleGetAll(), "Company", "idCompany")
        int idJob = selectEntityFromCollection(jobController.handleGetAllByCompanyId(idCompany), "Job")

        if (candidateController.handleLikeJob(idCandidate, idJob)) {
            println "Like added successfully!"
        } else {
            println "This job is already liked!"
        }
    }

    private void likeCandidateAsCompany() {
        int idCompany = selectEntityFromCollection(companyController.handleGetAll(), "Company", "idCompany")
        int idJob = selectEntityFromCollection(jobController.handleGetAllByCompanyId(idCompany), "Job")
        int idCandidate = selectEntityFromCollection(candidateController.handleGetAllInterestedInJob(idJob), "Candidate")

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
        int idCompany = selectEntityFromCollection(companyController.handleGetAll(), "Company", "idCompany")
        int idJob = selectEntityFromCollection(jobController.handleGetAllByCompanyId(idCompany), "Job")

        List<MatchDTO> allMatches = matchController.handleGetAllMatchesByJobId(idJob)
        if (isEmptyAndPrint(allMatches, "Matches")) return
        allMatches.forEach { println it }
    }

    private <T> int selectEntityFromCollection(List<T> items, String label, String idField = "id") {
        if (isEmptyAndPrint(items, label)) return -1
        items.forEach { println it }
        return inputReader.readId(items, "${label} Id: ", idField)
    }

    private boolean isEmptyAndPrint(List items, String label) {
        if (items.isEmpty()) {
            println "No records found for ${label.toLowerCase()}."
            return true
        }
        return false
    }
}