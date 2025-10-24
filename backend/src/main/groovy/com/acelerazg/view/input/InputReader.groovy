package com.acelerazg.view.input

import com.acelerazg.dao.CompetencyDAO
import com.acelerazg.model.Address
import com.acelerazg.model.Competency
import groovy.transform.CompileStatic

import java.time.LocalDate

@CompileStatic
class InputReader {
    private final CompetencyDAO competencyDAO
    private final Scanner appScanner
    private final InputValidator inputValidator

    InputReader(CompetencyDAO competencyDAO, Scanner appScanner, InputValidator inputValidator) {
        this.competencyDAO = competencyDAO
        this.appScanner = appScanner
        this.inputValidator = inputValidator
    }

    String readValidatedString(String prompt, int maxLength = 255, int minLength = 0) {
        while (true) {
            println prompt
            String input = appScanner.nextLine().trim()
            ValidationResult<String> result = inputValidator.hasValidLength(input, maxLength, minLength)
            if (result.isValid) {
                return result.value
            }
            println result.message
        }
    }

    LocalDate readDate(String prompt) {
        while (true) {
            println prompt
            String input = appScanner.nextLine().trim()
            ValidationResult<LocalDate> result = inputValidator.isValidDateInPast(input)
            if (result.isValid) {
                return result.value
            }
            println result.message
        }
    }

    List<Competency> readCompetencies() {
        Set<Competency> competencies = new LinkedHashSet<>()
        List<Competency> allCompetencies = competencyDAO.getAll()
        while (true) {
            println "Add competencies: "
            allCompetencies.forEach { Competency c -> println "${c.id}: ${c.name.replace("_", " ").capitalize()}"
            }
            println "q: End operation"

            String input = appScanner.nextLine()
            if (input == "q") {
                return new ArrayList<>(competencies)
            }
            try {
                int parsedId = Integer.parseInt(input)
                competencies.add(allCompetencies.find { Competency c -> c.id == parsedId })
            } catch (Exception ignored) {
                println "Invalid value. Try again."
            }
        }
    }

    <T> int readId(List<T> list, String prompt, String idFieldName = "id") {
        while (true) {
            println prompt
            String input = appScanner.nextLine()
            ValidationResult<Integer> result = inputValidator.isValidId(input, list, idFieldName)
            if (result.isValid) {
                return result.value
            }
            println result.message
        }
    }

    Address readAddress() {
        String state = readValidatedString("State:", 2, 2)
        String postalCode = readValidatedString("Postal Code:", 16)
        String country = readValidatedString("Country:")
        String city = readValidatedString("City:")
        String street = readValidatedString("Street:")

        Address address = Address.builder()
                .state(state)
                .postalCode(postalCode)
                .country(country)
                .city(city)
                .street(street)
                .build()

        return address
    }
}