package com.acelerazg.exceptions

import groovy.transform.CompileStatic

@CompileStatic
class DataAccessException extends RuntimeException {
    DataAccessException(String message, Throwable cause) {
        super(message, cause)
    }

    DataAccessException(String s) {}
}