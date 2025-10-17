package com.acelerazg.view.input

import groovy.transform.CompileStatic

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@CompileStatic
class InputValidator {
    static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    ValidationResult<String> hasValidLength(String input, int maxLength, int minLength) {
        input = input.trim()
        if (input.size() > maxLength || input.size() <= minLength) {
            return ValidationResult.failure("Invalid field length")
        }
        return ValidationResult.success(input)
    }

    ValidationResult<LocalDate> isValidDateInPast(String input) {
        input = input.trim()
        if (input.isEmpty()) {
            return ValidationResult.failure("This field is required")
        }
        try {
            LocalDate date = LocalDate.parse(input, DATE_FORMAT)
            if (date.isBefore(LocalDate.now())) return ValidationResult.success(date)
            return ValidationResult.failure("Date must be before today")
        } catch (DateTimeParseException ignored) {
            return ValidationResult.failure("Invalid date format")
        }
    }

    <T> ValidationResult<Integer> isValidId(String input, List<T> list, String idFieldName) {
        try {
            int id = input.toInteger()
            T object = list.stream()
                    .filter(c -> c[idFieldName] == id)
                    .findFirst()
                    .orElse(null)
            if (!object) {
                return ValidationResult.failure("Entity not found.")
            }
            return ValidationResult.success(id)
        } catch (Exception ignored) {
            return ValidationResult.failure("Invalid Id")
        }

    }
}