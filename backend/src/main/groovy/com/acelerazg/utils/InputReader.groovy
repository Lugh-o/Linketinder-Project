package com.acelerazg.utils

import com.acelerazg.dao.CompetencyDAO
import com.acelerazg.model.Competency
import groovy.transform.CompileStatic

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@CompileStatic
class InputReader {
    static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    static String readNonEmpty(Scanner scanner, String prompt, String errorMessage, int maxLength = 255, int minLength = 0) {
        while (true) {
            println prompt
            String input = scanner.nextLine().trim()
            if (input.size() > maxLength || input.size() < minLength) {
                println errorMessage
                continue
            }
            return input
        }
    }

    static LocalDate readDate(Scanner scanner, String prompt, String errorMessage) {
        while (true) {
            println prompt
            String input = scanner.nextLine().trim()
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

    static List<Competency> readCompetencies(Scanner appScanner) {
        Set<Competency> competencies = new LinkedHashSet<>()
        List<Competency> allCompetencies = CompetencyDAO.getAll()
        while (true) {
            println "Add competencies: "
            allCompetencies.forEach { comp ->
                println "${comp.id}: ${comp.name.replace("_", " ").capitalize()}"
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

    static <T> int readId(Scanner scanner, List<T> list, String message, String idFieldName = "id") {
        while (true) {
            println message
            try {
                int id = scanner.nextLine().toInteger()
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
}