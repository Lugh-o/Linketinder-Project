//// CandidateControllerTest.groovy
//package com.acelerazg.controller
//
//import com.acelerazg.dto.AnonymousCandidateDTO
//import com.acelerazg.model.CandidateDTO
//import com.acelerazg.model.Address
//import com.acelerazg.model.Candidate
//import com.acelerazg.model.Competency
//import com.acelerazg.service.CandidateService
//import groovy.json.JsonSlurper
//import spock.lang.Specification
//
//import javax.servlet.http.HttpServletRequest
//import javax.servlet.http.HttpServletResponse
//import java.time.LocalDate
//
//class CandidateControllerTest extends Specification {
//
//    CandidateController controller
//    CandidateService candidateService
//    HttpServletRequest request
//    HttpServletResponse response
//
//    def setup() {
//        candidateService = Mock(CandidateService)
//        controller = new CandidateController()
//        controller.candidateService = candidateService
//
//        request = Mock()
//        response = Mock()
//    }
//
//    def "GET /api/v1/candidates - deve retornar lista de candidatos sem campos nulos e sem passwd"() {
//        given:
//        def addr = Address.builder().state("SP").postalCode("000").country("BR").city("SP").street("Rua").build()
//        def comps = [Competency.builder().name("Java").build()]
//        def dto = new CandidateDTO("desc", "a@b.com", "Ana", "Silva", "123", LocalDate.now(), "CS", addr, comps, 1, 1, 1)
//
//        candidateService.getAllCandidates() >> Response.success(200, [dto])
//
//        request.method = "GET"
//        request.pathInfo = "/"
//
//        when:
//        controller.service(request, response)
//        def json = new JsonSlurper().parseText(response.contentAsString)
//        def data = json.data[0]
//
//        then:
//        response.status == 200
//        json.statusCode == 200
//        data.firstName == "Ana"
//        data.competencies*.name == ["Java"]
//        !data.containsKey("passwd")
//        !data.containsKey("idPerson")
//        !data.containsKey("idAddress")
//        data.address.state == "SP"
//    }
//
//    def "GET /api/v1/candidates/{id} - deve retornar candidato com toMap()"() {
//        given:
//        def addr = Address.builder().state("RJ").build()
//        def dto = new CandidateDTO("dev", "b@c.com", "Bia", "Luz", "456", LocalDate.now(), "Eng", addr, [], 2, 2, 2)
//
//        candidateService.getById(2) >> Response.success(200, dto)
//
//        request.method = "GET"
//        request.pathInfo = "/2"
//
//        when:
//        controller.service(request, response)
//        def json = new JsonSlurper().parseText(response.contentAsString)
//        def data = json.data
//
//        then:
//        data.idCandidate == 2
//        data.firstName == "Bia"
//        !data.containsKey("passwd")
//        data.competencies == []
//    }
//
//    def "POST /api/v1/candidates - deve criar candidato"() {
//        given:
//        def body = [email       : "novo@email.com",
//                    firstName   : "Novo",
//                    lastName    : "User",
//                    address     : [state: "MG", postalCode: "30000-000", country: "Brasil", city: "BH", street: "Av Central"],
//                    competencies: ["SQL", "Docker"]]
//
//        def created = Candidate.builder().email("novo@email.com").firstName("Novo").build()
//        candidateService.createCandidate(_ as CandidateDTO) >> Response.success(201, created)
//
//        request.method = "POST"
//        request.pathInfo = "/"
//        request.content = JsonOutput.toJson(body).bytes
//        request.contentType = "application/json"
//
//        when:
//        controller.service(request, response)
//
//        then:
//        response.status == 201
//        def json = new JsonSlurper().parseText(response.contentAsString)
//        json.data.email == "novo@email.com"
//    }
//
//    def "POST /api/v1/candidates/{id}/like - deve dar like"() {
//        given:
//        candidateService.likeJob(1, 42) >> Response.success(201, [idCandidate: 1, idJob: 42])
//
//        request.method = "POST"
//        request.pathInfo = "/1/like"
//        request.content = JsonOutput.toJson([jobId: 42]).bytes
//        request.contentType = "application/json"
//
//        when:
//        controller.service(request, response)
//
//        then:
//        response.status == 201
//        def json = new JsonSlurper().parseText(response.contentAsString)
//        json.data.idJob == 42
//    }
//
//    def "PUT /api/v1/candidates/{id} - deve atualizar apenas campos enviados"() {
//        given:
//        def body = [email: "atualizado@email.com"]
//        def updatedDTO = new CandidateDTO("desc", "atualizado@email.com", "Ana", "Silva", "123", LocalDate.now(), "CS", null, [], 1, 1, 1)
//
//        candidateService.updateCandidate(1, _ as CandidateDTO) >> Response.success(200, updatedDTO)
//
//        request.method = "PUT"
//        request.pathInfo = "/1"
//        request.content = JsonOutput.toJson(body).bytes
//        request.contentType = "application/json"
//
//        when:
//        controller.service(request, response)
//        def json = new JsonSlurper().parseText(response.contentAsString)
//
//        then:
//        json.data.email == "atualizado@email.com"
//        !json.data.containsKey("address")  // não foi enviado
//    }
//
//    def "DELETE /api/v1/candidates/{id} - deve deletar"() {
//        given:
//        candidateService.deleteCandidate(1) >> Response.success(204)
//
//        request.method = "DELETE"
//        request.pathInfo = "/1"
//
//        when:
//        controller.service(request, response)
//
//        then:
//        response.status == 204
//        response.contentAsString.empty
//    }
//
//    def "GET /api/v1/candidates/interested/{jobId} - deve retornar anônimos"() {
//        given:
//        def anon = new AnonymousCandidateDTO(1, "CS", "desc", [Competency.builder().name("Java").build()])
//        candidateService.getAllInterestedInJob(42) >> Response.success(200, [anon])
//
//        request.method = "GET"
//        request.pathInfo = "/interested/42"
//
//        when:
//        controller.service(request, response)
//        def json = new JsonSlurper().parseText(response.contentAsString)
//
//        then:
//        json.data[0].competencies[0].name == "Java"
//    }
//}