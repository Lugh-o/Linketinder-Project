package com.acelerazg.dao

import com.acelerazg.dto.AnonymousCandidateDTO
import com.acelerazg.model.Address
import com.acelerazg.model.Candidate
import com.acelerazg.model.Competency
import com.acelerazg.persistence.DatabaseHandler
import groovy.transform.CompileStatic

import java.sql.*
import java.time.LocalDate

@CompileStatic
class CandidateDAO extends DAO {
    private final AddressDAO addressDAO
    private final PersonDAO personDAO
    private final CompetencyDAO competencyDAO

    CandidateDAO(AddressDAO addressDAO, PersonDAO personDAO, CompetencyDAO competencyDAO) {
        this.addressDAO = addressDAO
        this.personDAO = personDAO
        this.competencyDAO = competencyDAO
    }

    @SuppressWarnings()
    List<Candidate> getAll() {
        String sql = """            
            SELECT 
                c.id as id_candidate, c.first_name, c.last_name, c.cpf, c.birthday, c.graduation,
                p.id as id_person, p.email, p.description, p.id_address
            FROM candidate c
            INNER JOIN person p ON c.id_person = p.id
        """

        Connection connection = null
        PreparedStatement statement = null
        ResultSet response = null
        List<Candidate> candidates = []

        try {
            connection = DatabaseHandler.getConnection()
            statement = connection.prepareStatement(sql)
            response = statement.executeQuery()
            while (response.next()) {
                candidates.add(new Candidate(
                        response.getInt("id_person"),
                        response.getString("email"),
                        response.getString("description"),
                        response.getInt("id_address"),
                        response.getInt("id_candidate"),
                        response.getString("first_name"),
                        response.getString("last_name"),
                        response.getString("cpf"),
                        LocalDate.parse(response.getString("birthday")),
                        response.getString("graduation"),
                ))
            }
        } finally {
            DatabaseHandler.closeQuietly(response, statement, connection)
        }
        return candidates
    }

    private Candidate getCandidateGeneric(String sql, def parameter) {
        Connection connection = null
        PreparedStatement statement = null
        ResultSet response = null
        Candidate candidate = null
        try {
            connection = DatabaseHandler.getConnection()
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            statement.setObject(1, parameter)
            response = statement.executeQuery()
            if (response.next()) {
                candidate = new Candidate(
                        response.getInt("id_person"),
                        response.getString("email"),
                        response.getString("description"),
                        response.getString("passwd"),
                        response.getInt("id_address"),
                        response.getInt("id_candidate"),
                        response.getString("first_name"),
                        response.getString("last_name"),
                        response.getString("cpf"),
                        LocalDate.parse(response.getString("birthday")),
                        response.getString("graduation"),
                )
            }
        } finally {
            DatabaseHandler.closeQuietly(response, statement, connection)
        }
        return candidate
    }

    Candidate getById(int id) {
        String sql = """            
        SELECT 
            c.id as id_candidate, c.first_name, c.last_name, c.cpf, c.birthday, c.graduation,
            p.id as id_person, p.email, p.description, p.passwd, p.id_address,
            a.state, a.postal_code, a.country, a.street, a.city
        FROM candidate c
        INNER JOIN person p ON c.id_person = p.id
        INNER JOIN address a ON a.id = p.id_address            
        WHERE c.id = ?
        """
        return getCandidateGeneric(sql, id)
    }

    Candidate getByEmail(String email) {
        String sql = """
                SELECT 
                    c.id as id_candidate, c.first_name, c.last_name, c.cpf, c.birthday, c.graduation,
                    p.id as id_person, p.email, p.description, p.passwd, p.id_address,
                    a.state, a.postal_code, a.country, a.street, a.city
                FROM candidate c
                INNER JOIN person p ON c.id_person = p.id
                INNER JOIN address a ON a.id = p.id_address            
                WHERE p.email = ?
        """
        return getCandidateGeneric(sql, email)
    }

    List<AnonymousCandidateDTO> getAllCandidatesInterestedByJobId(int id) {
        String sql = """
            SELECT 
                cd.id AS candidate_id,
                cd.graduation AS candidate_graduation,
                p.description AS candidate_description,
                STRING_AGG(DISTINCT ct.name, ', ') AS candidate_competencies
            FROM candidate_like cl
            INNER JOIN job j ON cl.id_job = j.id
            INNER JOIN company cp ON j.id_company = cp.id
            INNER JOIN candidate cd ON cl.id_candidate = cd.id
            INNER JOIN candidate_competency cc ON cc.id_candidate = cd.id
            INNER JOIN competency ct ON cc.id_competency = ct.id
            INNER JOIN person p ON cd.id_person = p.id
            WHERE j.id = ?
            GROUP BY cd.id, cd.graduation, p.description;
        """
        Connection connection = null
        PreparedStatement statement = null
        ResultSet response = null
        List<AnonymousCandidateDTO> candidates = []

        try {
            connection = DatabaseHandler.getConnection()
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            statement.setObject(1, id)
            response = statement.executeQuery()
            while (response.next()) {
                candidates.add(new AnonymousCandidateDTO(
                        response.getInt("candidate_id"),
                        response.getString("candidate_graduation"),
                        response.getString("candidate_description"),
                        response.getString("candidate_competencies")
                                .split(',')
                                .collect { it.trim() }
                                .collect { new Competency(it) }
                ))
            }
        } finally {
            DatabaseHandler.closeQuietly(response, statement, connection)
        }
        return candidates
    }

    boolean hasLikedJob(int idCandidate, int idJob) {
        String sql = """
            SELECT * from candidate_like
            WHERE id_candidate = ? AND id_job = ?
        """
        Connection connection = null
        PreparedStatement statement = null
        ResultSet response = null

        try {
            connection = DatabaseHandler.getConnection()
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            statement.setInt(1, idCandidate)
            statement.setInt(2, idJob)
            response = statement.executeQuery()
            if (response.next()) {
                return true
            }
        } finally {
            DatabaseHandler.closeQuietly(response, statement, connection)
        }
        return false
    }

    Candidate create(Candidate candidate, Address address, List<Competency> competencies) {
        String sql = """
            INSERT INTO candidate (first_name, last_name, id_person, cpf, birthday, graduation) VALUES
            (?, ?, ?, ?, ?, ?);
        """

        Connection connection = null
        PreparedStatement statement = null
        ResultSet response = null

        try {
            connection = DatabaseHandler.getConnection()
            connection.autoCommit = false

            candidate.idAddress = addressDAO.create(connection, address)
            candidate.idPerson = personDAO.create(connection, candidate)

            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            statement.setString(1, candidate.firstName)
            statement.setString(2, candidate.lastName)
            statement.setInt(3, candidate.idPerson)
            statement.setString(4, candidate.cpf)
            statement.setDate(5, Date.valueOf(candidate.birthday))
            statement.setString(6, candidate.graduation)
            statement.executeUpdate()
            response = statement.getGeneratedKeys()

            if (response.next()) {
                candidate.idCandidate = response.getInt("id")
            }

            competencies.forEach { createCandidateCompetency(connection, candidate.idCandidate, it) }

            connection.commit()
        } catch (Exception e) {
            if (connection != null) connection.rollback()
            throw e
        } finally {
            DatabaseHandler.closeQuietly(response, statement, connection)
        }

        return candidate
    }

    void createCandidateCompetency(Connection connection, int idCandidate, Competency competency) {
        Competency existing = competencyDAO.getByName(connection, competency.name)
        if (!existing) {
            existing = competencyDAO.createWithConnection(connection, competency)
        }

        String sql = "INSERT INTO candidate_competency (id_candidate, id_competency) VALUES (?, ?);"
        PreparedStatement statement = null
        try {
            statement = connection.prepareStatement(sql)
            statement.setInt(1, idCandidate)
            statement.setInt(2, existing.id)
            statement.executeUpdate()
        } finally {
            DatabaseHandler.closeQuietly(statement)
        }
    }

    Candidate update(int id, Candidate candidate, Address address, List<Competency> newCompetencies) {
        String sql = """
            UPDATE candidate SET first_name = ?, last_name = ?, cpf = ?, date_of_birth = ?, graduation = ?
            WHERE id = ?
        """

        Connection connection = null
        PreparedStatement statement = null

        try {
            connection = DatabaseHandler.getConnection()
            connection.autoCommit = false

            Candidate currentCandidate = getById(id)

            addressDAO.update(connection, currentCandidate.idAddress, address)
            personDAO.update(connection, candidate)
            updateCandidateCompetencies(connection, id, newCompetencies)

            statement = connection.prepareStatement(sql)
            statement.setString(1, candidate.firstName)
            statement.setString(2, candidate.lastName)
            statement.setString(3, candidate.cpf)
            statement.setDate(4, Date.valueOf(candidate.birthday))
            statement.setString(5, candidate.graduation)
            statement.setInt(6, id)
            statement.executeUpdate()

            connection.commit()
        } catch (Exception e) {
            if (connection != null) connection.rollback()
            throw e
        } finally {
            DatabaseHandler.closeQuietly(statement, connection)
        }

        return getById(id)
    }

    void updateCandidateCompetencies(Connection connection, int idCandidate, List<Competency> newCompetencies) {
        if (newCompetencies == null) return
        String deleteSql = "DELETE FROM candidate_competency WHERE id_candidate = ?"
        PreparedStatement deleteStatement = null
        try {
            deleteStatement = connection.prepareStatement(deleteSql)
            deleteStatement.setInt(1, idCandidate)
            deleteStatement.executeUpdate()
        } finally {
            DatabaseHandler.closeQuietly(deleteStatement)
        }
        newCompetencies.forEach { c ->
            createCandidateCompetency(connection, idCandidate, c)
        }
    }

    void delete(int id) {
        String sql = "DELETE FROM candidate WHERE id = ?"
        deleteGeneric(id, sql)
    }

    void likeJob(int idCandidate, int jobId) {
        String sql = """
            INSERT INTO candidate_like (id_candidate, id_job) VALUES (?, ?)
        """
        likeGeneric(idCandidate, jobId, sql)
    }

    boolean isJobAlreadyLiked(int idCandidate, int idJob) {
        String sql = """
            SELECT * FROM candidate_like
            WHERE id_candidate = ? AND id_job = ?;
        """

        Connection connection = null
        PreparedStatement statement = null
        ResultSet response = null

        try {
            connection = DatabaseHandler.getConnection()
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            statement.setInt(1, idCandidate)
            statement.setInt(2, idJob)

            response = statement.executeQuery()
            if (response.next()) {
                return true
            }
        } finally {
            DatabaseHandler.closeQuietly(response, statement, connection)
        }
        return false
    }
}