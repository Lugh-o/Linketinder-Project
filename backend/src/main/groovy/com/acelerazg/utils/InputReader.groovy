package com.acelerazg.utils

import com.acelerazg.dao.CompetencyDAO
import com.acelerazg.model.Address
import com.acelerazg.model.Competency
import groovy.transform.CompileStatic

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@CompileStatic
class InputReader {
    private final CompetencyDAO competencyDAO
    private final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private final Scanner appScanner

    InputReader(CompetencyDAO competencyDAO, Scanner appScanner) {
        this.competencyDAO = competencyDAO
        this.appScanner = appScanner
    }

    String readNonEmpty(String prompt, String errorMessage, int maxLength = 255, int minLength = 0) {
        while (true) {
            println prompt
            String input = appScanner.nextLine().trim()
            if (input.size() > maxLength || input.size() < minLength) {
                println errorMessage
                continue
            }
            return input
        }
    }

    LocalDate readDate(String prompt, String errorMessage) {
        while (true) {
            println prompt
            String input = appScanner.nextLine().trim()
            if (input.isEmpty()) {
                System.out.println(errorMessage)
                continue
            }
            try {
                LocalDate date = LocalDate.parse(input, DATE_FORMAT)
                if (date.isBefore(LocalDate.now())) return date
                println errorMessage
            } catch (DateTimeParseException ignored) {
                println errorMessage
            }
        }
    }

    List<Competency> readCompetencies() {
        Set<Competency> competencies = new LinkedHashSet<>()
        List<Competency> allCompetencies = competencyDAO.getAll()
        while (true) {
            println "Add competencies: "
            allCompetencies.forEach { c -> println "${c.id}: ${c.name.replace("_", " ").capitalize()}"
            }
            println "q: End operation"

            String input = appScanner.nextLine()
            if (input == "q") {
                return new ArrayList<>(competencies)
            }
            try {
                competencies.add(allCompetencies.get(Integer.parseInt(input)))
            } catch (Exception ignored) {
                println "Invalid value. Try again."
            }
        }
    }

    <T> int readId(List<T> list, String message, String idFieldName = "id") {
        while (true) {
            println message
            try {
                int id = appScanner.nextLine().toInteger()
                T object = list.stream()
                        .filter(c -> c[idFieldName] == id)
                        .findFirst()
                        .orElse(null)
                if (!object) throw new Exception()
                return id
            } catch (Exception ignored) {
                println "Invalid Id. Try Again"
            }
        }
    }

    Address readAddress() {
        String state = readNonEmpty("State:", "Invalid Input. Try again.", 2, 2)
        String postalCode = readNonEmpty("Postal Code:", "The postal code cannot be empty", 16)
        String country = readNonEmpty("Country:", "The country cannot be empty")
        String city = readNonEmpty("City:", "The city cannot be empty")
        String street = readNonEmpty("Street:", "The street cannot be empty")

        return new Address(state, postalCode, country, city, street)
    }
}