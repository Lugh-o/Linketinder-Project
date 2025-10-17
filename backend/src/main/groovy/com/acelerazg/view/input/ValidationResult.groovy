package com.acelerazg.view.input

import groovy.transform.CompileStatic

@CompileStatic
class ValidationResult<T> {
    final boolean isValid
    final String message
    final T value

    private ValidationResult(boolean isValid, String message, T value) {
        this.isValid = isValid
        this.message = message
        this.value = value
    }

    static <T> ValidationResult<T> success(T value) {
        return new ValidationResult<>(true, null, value)
    }

    static <T> ValidationResult<T> failure(String message) {
        return new ValidationResult<>(false, message, null)
    }
}