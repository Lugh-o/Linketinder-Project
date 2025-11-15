package com.acelerazg.common

import groovy.transform.CompileStatic
import groovy.transform.ToString

import java.time.LocalDateTime

@CompileStatic
@ToString(includeSuperProperties = true, includePackage = false, includeNames = true)
class Response<T> {
    int statusCode
    String message
    String error
    String details
    String path
    String timestamp
    T data

    Response(int statusCode) {
        this.statusCode = statusCode
        this.timestamp = LocalDateTime.now().toString()
    }

    Response(int statusCode, T data) {
        this.statusCode = statusCode
        this.data = data
        this.timestamp = LocalDateTime.now().toString()
    }

    Response(int statusCode, String message) {
        this.statusCode = statusCode
        this.message = message
        this.timestamp = LocalDateTime.now().toString()
    }

    static <T> Response<T> success(int statusCode, T data) {
        return new Response<>(statusCode, data)
    }

    static <T> Response<T> success(int statusCode) {
        return new Response<>(statusCode)
    }

    static <T> Response<T> error(int statusCode, String message) {
        Response<T> r = new Response<>(statusCode, message)
        r.error = httpStatusName(statusCode)
        return r
    }

    static <T> Response<T> error(int statusCode, String message, String details, String path) {
        Response<T> r = new Response<>(statusCode, message)
        r.error = httpStatusName(statusCode)
        r.details = details
        r.path = path
        return r
    }

    private static String httpStatusName(int code) {
        switch (code) {
            case 400: return "Bad Request"
            case 401: return "Unauthorized"
            case 403: return "Forbidden"
            case 404: return "Not Found"
            case 409: return "Conflict"
            case 422: return "Unprocessable Entity"
            case 500: return "Internal Server Error"
            default: return "Error"
        }
    }
}
