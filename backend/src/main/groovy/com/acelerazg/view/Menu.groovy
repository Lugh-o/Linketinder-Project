package com.acelerazg.view

import com.acelerazg.controller.CandidateController
import com.acelerazg.controller.CompanyController
import com.acelerazg.controller.JobController
import com.acelerazg.controller.MatchController
import com.acelerazg.dao.*
import com.acelerazg.dto.AnonymousCandidateDTO
import com.acelerazg.dto.MatchDTO
import com.acelerazg.model.Candidate
import com.acelerazg.model.Company
import com.acelerazg.model.Competency
import com.acelerazg.model.Job
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
        inputReader = new InputReader(competencyDao)
    }

    static void openMenu() {
        Menu menu = new Menu()
        menu.showOptions()
    }

    void showOptions() {
        println "Welcome to Linketinder!"

        String appInput = ""
        try (Scanner appScanner = new Scanner(System.in)) {
            while (appInput != "q") {
                println "Insert the desired operation:\n" +
                        "1 - List all candidates\n" +
                        "2 - List all companies\n" +
                        "3 - Create a candidate\n" +
                        "4 - Create a company\n" +
                        "5 - Add a Job to a company\n" +
                        "6 - Like a job as an candidate\n" +
                        "7 - Like a candidate as a company\n" +
                        "8 - List all matches of a job\n" +
                        "q - Exit the application\n"
                appInput = appScanner.nextLine()
                switch (appInput) {
                    case "1":
                        candidateController.handleGetAll().forEach { println it }
                        break
                    case "2":
                        companyController.handleGetAll().forEach { println it }
                        break
                    case "3":
                        String candidateDescription = inputReader.readNonEmpty(appScanner,
                                "Candidate Description: ", "The candidate description cannot be empty.")
                        String candidatePassword = inputReader.readNonEmpty(appScanner,
                                "Candidate Password: ", "The candidate password cannot be empty.")
                        String candidateEmail = inputReader.readNonEmpty(appScanner,
                                "Candidate Email: ", "The candidate email cannot be empty.")
                        String candidateFirstName = inputReader.readNonEmpty(appScanner,
                                "Candidate First Name: ", "The candidate first name cannot be empty.")
                        String candidateLastName = inputReader.readNonEmpty(appScanner,
                                "Candidate Last Name: ", "The candidate last name cannot be empty.")
                        String candidateCpf = inputReader.readNonEmpty(appScanner,
                                "Candidate CPF: ", "The candidate CPF cannot be empty.", 11)
                        LocalDate candidateBirthday = inputReader.readDate(appScanner,
                                "Candidate birthday: (yyyy-MM-dd) ", "Invalid date. Try again.")
                        String candidateGraduation = inputReader.readNonEmpty(appScanner,
                                "Candidate Graduation: ", "The candidate graduation cannot be empty")

                        String candidateState = inputReader.readNonEmpty(appScanner,
                                "Candidate State:", "Invalid Input. Try again.", 2, 2)
                        String candidatePostalCode = inputReader.readNonEmpty(appScanner,
                                "Candidate Postal Code:", "The candidate postal code cannot be empty", 16)
                        String candidateCountry = inputReader.readNonEmpty(appScanner,
                                "Candidate Country:", "The candidate country cannot be empty")
                        String candidateCity = inputReader.readNonEmpty(appScanner,
                                "Candidate City:", "The candidate city cannot be empty")
                        String candidateStreet = inputReader.readNonEmpty(appScanner,
                                "Candidate Street:", "The candidate street cannot be empty")

                        List<Competency> candidateCompetencies = inputReader.readCompetencies(appScanner)

                        Candidate candidate = candidateController.handleCreateCandidate(
                                candidateDescription,
                                candidatePassword,
                                candidateEmail,
                                candidateFirstName,
                                candidateLastName,
                                candidateCpf,
                                candidateBirthday,
                                candidateGraduation,
                                candidateState,
                                candidatePostalCode,
                                candidateCountry,
                                candidateCity,
                                candidateStreet,
                                candidateCompetencies
                        )
                        if (candidate) {
                            println "Candidate added successfully!"
                        } else {
                            println "Something went wrong."
                        }
                        break
                    case "4":
                        String companyDescription = inputReader.readNonEmpty(appScanner,
                                "Company Description: ", "The company description cannot be empty.")
                        String companyPassword = inputReader.readNonEmpty(appScanner,
                                "Company Password: ", "The company password cannot be empty.")
                        String companyEmail = inputReader.readNonEmpty(appScanner,
                                "Company Email: ", "The company email cannot be empty.")
                        String companyName = inputReader.readNonEmpty(appScanner,
                                "Company Name:", "The company name cannot be empty")
                        String companyCnpj = inputReader.readNonEmpty(appScanner,
                                "Company CNPJ:", "The company cnpj cannot be empty")

                        String companyState = inputReader.readNonEmpty(appScanner,
                                "Candidate State:", "Invalid Input. Try again.", 2, 2)
                        String companyPostalCode = inputReader.readNonEmpty(appScanner,
                                "Company Postal Code:", "The company postal code cannot be empty", 16)
                        String companyCountry = inputReader.readNonEmpty(appScanner,
                                "Company Country:", "The company country cannot be empty")
                        String companyCity = inputReader.readNonEmpty(appScanner,
                                "Company City:", "The company city cannot be empty")
                        String companyStreet = inputReader.readNonEmpty(appScanner,
                                "Company Street:", "The company street cannot be empty")

                        Company company = companyController.handleCreateCompany(
                                companyDescription,
                                companyPassword,
                                companyEmail,
                                companyName,
                                companyCnpj,
                                companyState,
                                companyPostalCode,
                                companyCountry,
                                companyCity,
                                companyStreet
                        )
                        if (company) {
                            println "Company added successfully!"
                        } else {
                            println "Something went wrong."
                        }
                        break
                    case "5":
                        List<Company> allCompanies = companyController.handleGetAll()
                        int idCompany = inputReader.readId(appScanner, allCompanies, "Company Id: ", "idCompany")

                        String jobName = inputReader.readNonEmpty(appScanner,
                                "Job Name:", "The job name cannot be empty")
                        String jobDescription = inputReader.readNonEmpty(appScanner,
                                "Job Description:", "The job description cannot be empty")

                        String jobState = inputReader.readNonEmpty(appScanner,
                                "Job State:", "Invalid Input. Try again.", 2, 2)
                        String jobPostalCode = inputReader.readNonEmpty(appScanner,
                                "Job Postal Code:", "The job postal code cannot be empty", 16)
                        String jobCountry = inputReader.readNonEmpty(appScanner,
                                "Job Country:", "The job country cannot be empty")
                        String jobCity = inputReader.readNonEmpty(appScanner,
                                "Job City:", "The job city cannot be empty")
                        String jobStreet = inputReader.readNonEmpty(appScanner,
                                "Job Street:", "The job street cannot be empty")

                        List<Competency> jobCompetencies = inputReader.readCompetencies(appScanner)

                        Job job = jobController.handleCreateJob(
                                jobName,
                                jobDescription,
                                idCompany,
                                jobState,
                                jobPostalCode,
                                jobCountry,
                                jobCity,
                                jobStreet,
                                jobCompetencies
                        )

                        if (job) {
                            println "Job added successfully!"
                        } else {
                            println "Something went wrong."
                        }
                        break
                    case "6":
                        List<Candidate> allCandidates = candidateController.handleGetAll()
                        allCandidates.forEach { println it }
                        int idCandidate = inputReader.readId(appScanner, allCandidates, "Candidate Id:", "idCandidate")

                        List<Company> allCompanies = companyController.handleGetAll()
                        allCompanies.forEach { println it }
                        int idCompany = inputReader.readId(appScanner, allCompanies, "Company Id: ", "idCompany")

                        List<Job> allJobs = jobController.handleGetAllByCompanyId(idCompany)
                        if (allJobs.isEmpty()) {
                            println "This company doesn't have any jobs."
                            break
                        }
                        allJobs.forEach { println it }
                        int idJob = inputReader.readId(appScanner, allJobs, "Job Id: ")

                        if (candidateController.handleLikeJob(idCandidate, idJob)) {
                            println "Like added successfully!"
                        } else {
                            println "This job is already liked!"
                        }
                        break
                    case "7":
                        List<Company> allCompanies = companyController.handleGetAll()
                        allCompanies.forEach { println it }
                        int idCompany = inputReader.readId(appScanner, allCompanies, "Company Id: ", "idCompany")

                        List<Job> allJobs = jobController.handleGetAllByCompanyId(idCompany)
                        if (allJobs.isEmpty()) {
                            println "This company doesn't have any jobs."
                            break
                        }
                        allJobs.forEach { println it }
                        int idJob = inputReader.readId(appScanner, allJobs, "Job Id: ")

                        List<AnonymousCandidateDTO> anonymousCandidates = candidateController.handleGetAllInterestedInJob(idJob)
                        if (anonymousCandidates.isEmpty()) {
                            println "This job doesn't have any likes."
                            break
                        }
                        anonymousCandidates.forEach { println it }
                        int idCandidate = inputReader.readId(appScanner, anonymousCandidates, "Chose a candidate:")
                        int response = companyController.handleLikeCandidate(idCompany, idCandidate)
                        switch (response) {
                            case 0:
                                println "This candidate is already matched!"
                                break
                            case 1:
                                println "Like added successfully!"
                                break
                            case 2:
                                println "You Matched successfully with this candidate!"
                        }

                        if (companyController.handleLikeCandidate(idCompany, idCandidate)) {
                            println "Like added successfully!"
                        } else {
                            println "This candidate is already matched!"
                        }
                        break
                    case "8":
                        List<Company> allCompanies = companyController.handleGetAll()
                        allCompanies.forEach { println it }
                        int idCompany = inputReader.readId(appScanner, allCompanies, "Company Id: ", "idCompany")

                        List<Job> allJobs = jobController.handleGetAllByCompanyId(idCompany)
                        if (allJobs.isEmpty()) {
                            println "This company doesn't have any jobs."
                            break
                        }
                        allJobs.forEach { println it }
                        int idJob = inputReader.readId(appScanner, allJobs, "Job Id: ")

                        List<MatchDTO> allMatches = matchController.handleGetAllMatchesByJobId(idJob)
                        if (allMatches.isEmpty()) {
                            println "This job doesn't have any matches."
                            break
                        }
                        allMatches.forEach { println it }
                        break
                }
            }
        }
    }
}