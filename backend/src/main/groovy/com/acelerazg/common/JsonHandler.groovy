package com.acelerazg.common


import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovy.transform.CompileStatic

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@CompileStatic
class JsonHandler {
    static Map parseJsonBody(HttpServletRequest req) {
        String rawBody = req.reader.text
        if (rawBody == null || rawBody.trim().isEmpty()) {
            return [:]
        }
        return (Map) new JsonSlurper().parseText(rawBody)
    }

    static void write(HttpServletResponse resp, Response<?> response) {
        resp.status = response.statusCode

        Map payload = [statusCode: response.statusCode,
                       message   : response.message,
                       error     : response.error,
                       details   : response.details,
                       path      : response.path,
                       timestamp : response.timestamp,
                       data      : response.data]

        resp.writer.write(JsonOutput.toJson(payload))
    }

    static void writeError(HttpServletResponse resp, int status, String msg) {
        resp.status = status

        Map payload = [statusCode: status,
                       message   : msg,
                       error     : statusToName(status),
                       timestamp : new Date().toString()]

        resp.writer.write(JsonOutput.toJson(payload))
    }

    private static String statusToName(int code) {
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