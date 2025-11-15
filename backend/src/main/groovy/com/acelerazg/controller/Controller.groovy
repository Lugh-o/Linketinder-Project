package com.acelerazg.controller

import com.acelerazg.common.Response

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest

class Controller extends HttpServlet {
    protected Response<?> attachPath(Response<?> r, HttpServletRequest req) {
        r.path = req.requestURI
        return r
    }
}
