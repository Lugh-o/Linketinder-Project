package com.acelerazg.common

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CorsHandler {
    static void applyCorsHeaders(HttpServletResponse response, HttpServletRequest request) {
        response.setHeader("Access-Control-Allow-Origin", "*")
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization")
        response.setHeader("Access-Control-Allow-Credentials", "true")
        response.setHeader("Access-Control-Max-Age", "3600")

        if ("OPTIONS".equalsIgnoreCase(request.method)) {
            response.status = 200
            response.writer.flush()
        }
    }
}