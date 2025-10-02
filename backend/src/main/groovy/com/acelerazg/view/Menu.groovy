package com.acelerazg.view

import com.acelerazg.controller.CandidateController
import com.acelerazg.controller.CompanyController
import com.acelerazg.controller.JobController
import com.acelerazg.controller.MatchController
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
    static void openMenu() {
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
                        CandidateController.handleGetAll().forEach { println it }
                        break
                    case "2":
                        CompanyController.handleGetAll().forEach { println it }
                        break
                    case "3":
                        String candidateDescription = InputReader.readNonEmpty(appScanner,
                                "Candidate Description: ", "The candidate description cannot be empty.")
                        String candidatePassword = InputReader.readNonEmpty(appScanner,
                                "Candidate Password: ", "The candidate password cannot be empty.")
                        String candidateEmail = InputReader.readNonEmpty(appScanner,
                                "Candidate Email: ", "The candidate email cannot be empty.")
                        String candidateFirstName = InputReader.readNonEmpty(appScanner,
                                "Candidate First Name: ", "The candidate first name cannot be empty.")
                        String candidateLastName = InputReader.readNonEmpty(appScanner,
                                "Candidate Last Name: ", "The candidate last name cannot be empty.")
                        String candidateCpf = InputReader.readNonEmpty(appScanner,
                                "Candidate CPF: ", "The candidate CPF cannot be empty.", 11)
                        LocalDate candidateBirthday = InputReader.readDate(appScanner,
                                "Candidate birthday: (yyyy-MM-dd) ", "Invalid date. Try again.")
                        String candidateGraduation = InputReader.readNonEmpty(appScanner,
                                "Candidate Graduation: ", "The candidate graduation cannot be empty")

                        String candidateState = InputReader.readNonEmpty(appScanner,
                                "Candidate State:", "Invalid Input. Try again.", 2, 2)
                        String candidatePostalCode = InputReader.readNonEmpty(appScanner,
                                "Candidate Postal Code:", "The candidate postal code cannot be empty", 16)
                        String candidateCountry = InputReader.readNonEmpty(appScanner,
                                "Candidate Country:", "The candidate country cannot be empty")
                        String candidateCity = InputReader.readNonEmpty(appScanner,
                                "Candidate City:", "The candidate city cannot be empty")
                        String candidateStreet = InputReader.readNonEmpty(appScanner,
                                "Candidate Street:", "The candidate street cannot be empty")

                        List<Competency> candidateCompetencies = InputReader.readCompetencies(appScanner)

                        Candidate candidate = CandidateController.handleCreateCandidate(
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
                        String companyDescription = InputReader.readNonEmpty(appScanner,
                                "Company Description: ", "The company description cannot be empty.")
                        String companyPassword = InputReader.readNonEmpty(appScanner,
                                "Company Password: ", "The company password cannot be empty.")
                        String companyEmail = InputReader.readNonEmpty(appScanner,
                                "Company Email: ", "The company email cannot be empty.")
                        String companyName = InputReader.readNonEmpty(appScanner,
                                "Company Name:", "The company name cannot be empty")
                        String companyCnpj = InputReader.readNonEmpty(appScanner,
                                "Company CNPJ:", "The company cnpj cannot be empty")

                        String companyState = InputReader.readNonEmpty(appScanner,
                                "Candidate State:", "Invalid Input. Try again.", 2, 2)
                        String companyPostalCode = InputReader.readNonEmpty(appScanner,
                                "Company Postal Code:", "The company postal code cannot be empty", 16)
                        String companyCountry = InputReader.readNonEmpty(appScanner,
                                "Company Country:", "The company country cannot be empty")
                        String companyCity = InputReader.readNonEmpty(appScanner,
                                "Company City:", "The company city cannot be empty")
                        String companyStreet = InputReader.readNonEmpty(appScanner,
                                "Company Street:", "The company street cannot be empty")

                        Company company = CompanyController.createCompany(
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
                        List<Company> allCompanies = CompanyController.handleGetAll()
                        int idCompany = InputReader.readId(appScanner, allCompanies, "Company Id: ", "idCompany")

                        String jobName = InputReader.readNonEmpty(appScanner,
                                "Job Name:", "The job name cannot be empty")
                        String jobDescription = InputReader.readNonEmpty(appScanner,
                                "Job Description:", "The job description cannot be empty")

                        String jobState = InputReader.readNonEmpty(appScanner,
                                "Job State:", "Invalid Input. Try again.", 2, 2)
                        String jobPostalCode = InputReader.readNonEmpty(appScanner,
                                "Job Postal Code:", "The job postal code cannot be empty", 16)
                        String jobCountry = InputReader.readNonEmpty(appScanner,
                                "Job Country:", "The job country cannot be empty")
                        String jobCity = InputReader.readNonEmpty(appScanner,
                                "Job City:", "The job city cannot be empty")
                        String jobStreet = InputReader.readNonEmpty(appScanner,
                                "Job Street:", "The job street cannot be empty")

                        List<Competency> jobCompetencies = InputReader.readCompetencies(appScanner)

                        Job job = JobController.handleCreateJob(
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
                        List<Candidate> allCandidates = CandidateController.handleGetAll()
                        allCandidates.forEach { println it }
                        int idCandidate = InputReader.readId(appScanner, allCandidates, "Candidate Id:", "idCandidate")

                        List<Company> allCompanies = CompanyController.handleGetAll()
                        allCompanies.forEach { println it }
                        int idCompany = InputReader.readId(appScanner, allCompanies, "Company Id: ", "idCompany")

                        List<Job> allJobs = JobController.handleGetAllByCompanyId(idCompany)
                        if (allJobs.isEmpty()) {
                            println "This company doesn't have any jobs."
                            break
                        }
                        allJobs.forEach { println it }
                        int idJob = InputReader.readId(appScanner, allJobs, "Job Id: ")

                        CandidateController.handleLikeJob(idCandidate, idJob)
                        println "Like added successfully!"
                        break
                    case "7":
                        List<Company> allCompanies = CompanyController.handleGetAll()
                        allCompanies.forEach { println it }
                        int idCompany = InputReader.readId(appScanner, allCompanies, "Company Id: ", "idCompany")

                        List<Job> allJobs = JobController.handleGetAllByCompanyId(idCompany)
                        if (allJobs.isEmpty()) {
                            println "This company doesn't have any jobs."
                            break
                        }
                        allJobs.forEach { println it }
                        int idJob = InputReader.readId(appScanner, allJobs, "Job Id: ")

                        List<AnonymousCandidateDTO> anonymousCandidates = CandidateController.handleGetAllInterestedInJob(idJob)
                        if (anonymousCandidates.isEmpty()) {
                            println "This job doesn't have any likes."
                            break
                        }
                        anonymousCandidates.forEach { println it }
                        int idCandidate = InputReader.readId(appScanner, anonymousCandidates, "Chose a candidate:")

                        CompanyController.handleLikeCandidate(idCompany, idCandidate)
                        println "Like added successfully!"
                        break
                    case "8":
                        List<Company> allCompanies = CompanyController.handleGetAll()
                        allCompanies.forEach { println it }
                        int idCompany = InputReader.readId(appScanner, allCompanies, "Company Id: ", "idCompany")

                        List<Job> allJobs = JobController.handleGetAllByCompanyId(idCompany)
                        if (allJobs.isEmpty()) {
                            println "This company doesn't have any jobs."
                            break
                        }
                        allJobs.forEach { println it }
                        int idJob = InputReader.readId(appScanner, allJobs, "Job Id: ")

                        List<MatchDTO> allMatches = MatchController.handleGetAllMatchesByJobId(idJob)
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