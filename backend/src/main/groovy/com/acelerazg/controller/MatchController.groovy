package com.acelerazg.controller

import com.acelerazg.common.Response
import com.acelerazg.dao.MatchEventDAO
import com.acelerazg.dto.MatchDTO
import com.acelerazg.service.MatchService
import com.acelerazg.utils.JsonHandler
import groovy.transform.CompileStatic

import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@CompileStatic
@WebServlet(name = "MatchController", urlPatterns = ["/api/v1/matches/*"])
class MatchController extends Controller {
    MatchService matchService

    MatchController() {}

    @Override
    void init() {
        MatchEventDAO matchEventDAO = new MatchEventDAO()
        this.matchService = new MatchService(matchEventDAO)
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        String method = req.method
        String path = req.pathInfo ?: "/"

        resp.contentType = "application/json"
        resp.characterEncoding = "UTF-8"

        try {
            // GET /api/v1/matches/{id}
            if (method == "GET" && path.matches("/\\d+")) {
                int id = (path.substring(1) as int)
                JsonHandler.write(resp, attachPath(handleGetAllMatchesByJobId(id), req))
                return
            }

            JsonHandler.writeError(resp, 404, "Endpoint not found")

        } catch (Exception e) {
            e.printStackTrace()
            JsonHandler.writeError(resp, 500, e.message)
        }
    }

    Response<List<MatchDTO>> handleGetAllMatchesByJobId(int idJob) {
        return matchService.getAllMatchesByJobId(idJob)
    }
}