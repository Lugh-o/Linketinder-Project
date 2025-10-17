package com.acelerazg.exceptions

import groovy.transform.CompileStatic

@CompileStatic
class EmptyCollectionException extends RuntimeException {
    EmptyCollectionException(String message) {
        super(message)
    }
}