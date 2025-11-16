package com.acelerazg.dao

import com.acelerazg.dto.AnonymousCandidateDTO
import com.acelerazg.exceptions.DataAccessException
import com.acelerazg.model.Address
import com.acelerazg.model.Candidate
import com.acelerazg.model.Competency
import com.acelerazg.persistency.DatabaseHandler
import groovy.transform.CompileStatic

import java.sql.*

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

    List<Candidate> getAll() {
        String sql = """            
            SELECT 
                c.id as id_candidate, c.first_name, c.last_name, c.cpf, c.birthday, c.graduation,
                p.id as id_person, p.email, p.description, 
                a.id as id_address, a.state, a.postal_code, a.country, a.street, a.city,
                COALESCE(STRING_AGG(co.name, ', '), '') AS competencies
            FROM candidate c
            INNER JOIN person p ON c.id_person = p.id
            INNER JOIN address a ON p.id_address = a.id
            INNER JOIN candidate_competency cc ON cc.id_candidate = c.id
            INNER JOIN competency co ON cc.id_competency = co.id
            GROUP BY c.id, p.id, a.id
        """

        List<Candidate> candidates = []

        try (Connection connection = DatabaseHandler.getConnection()
             PreparedStatement statement = connection.prepareStatement(sql)
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String competencyString = resultSet.getString("competencies") ?: ""
                List<Competency> competencyList = competencyString ? competencyString.split(', ').collect { String name -> Competency.builder().name(name.trim()).build() } : []

                Address address = Address.builder()
                        .state(resultSet.getString("state"))
                        .postalCode(resultSet.getString("postal_code"))
                        .country(resultSet.getString("country"))
                        .street(resultSet.getString("street"))
                        .city(resultSet.getString("city"))
                        .build()

                candidates.add(new Candidate(resultSet.getString("description"),
                        resultSet.getString("email"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("cpf"),
                        resultSet.getDate("birthday")?.toLocalDate(),
                        resultSet.getString("graduation"),
                        address,
                        competencyList,
                        resultSet.getInt("id_person"),
                        resultSet.getInt("id_address"),
                        resultSet.getInt("id_candidate")))
            }
            return candidates
        } catch (Exception e) {
            throw new DataAccessException("Error fetching candidates", e)
        }
    }

    private Candidate getCandidateGeneric(String sql, Object param) {
        try (Connection connection = DatabaseHandler.getConnection()
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, param)
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String competencyString = resultSet.getString("competencies") ?: ""
                    List<Competency> competencyList = competencyString ? competencyString.split(', ').collect { String name -> Competency.builder().name(name.trim()).build() } : []

                    Address address = Address.builder()
                            .state(resultSet.getString("state"))
                            .postalCode(resultSet.getString("postal_code"))
                            .country(resultSet.getString("country"))
                            .street(resultSet.getString("street"))
                            .city(resultSet.getString("city"))
                            .build()

                    return new Candidate(resultSet.getString("description"),
                            resultSet.getString("email"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getString("cpf"),
                            resultSet.getDate("birthday").toLocalDate(),
                            resultSet.getString("graduation"),
                            address,
                            competencyList,
                            resultSet.getInt("id_person"),
                            resultSet.getInt("id_address"),
                            resultSet.getInt("id_candidate"))
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Error fetching candidate", e)
        }
    }

    Candidate getById(int id) {
        String sql = """            
        SELECT 
            c.id as id_candidate, c.first_name, c.last_name, c.cpf, c.birthday, c.graduation,
            p.id as id_person, p.email, p.description, 
            a.id as id_address, a.state, a.postal_code, a.country, a.street, a.city,
            COALESCE(STRING_AGG(co.name, ', '), '') AS competencies
        FROM candidate c
        INNER JOIN person p ON c.id_person = p.id
        INNER JOIN address a ON a.id = p.id_address
        INNER JOIN candidate_competency cc ON cc.id_candidate = c.id
        INNER JOIN competency co ON cc.id_competency = co.id   
        WHERE c.id = ?
        GROUP BY c.id, p.id, a.id
        """
        return getCandidateGeneric(sql, id)
    }

    Candidate getByEmail(String email) {
        String sql = """
                SELECT 
                    c.id as id_candidate, c.first_name, c.last_name, c.cpf, c.birthday, c.graduation,
                    p.id as id_person, p.email, p.description, 
                    a.id as id_address, a.state, a.postal_code, a.country, a.street, a.city,
                    COALESCE(STRING_AGG(co.name, ', '), '') AS competencies
                FROM candidate c
                INNER JOIN person p ON c.id_person = p.id
                INNER JOIN address a ON a.id = p.id_address     
                INNER JOIN candidate_competency cc ON cc.id_candidate = c.id
                INNER JOIN competency co ON cc.id_competency = co.id   
                WHERE p.email = ?
                GROUP BY c.id, p.id, a.id
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

        List<AnonymousCandidateDTO> candidates = []

        try (Connection connection = DatabaseHandler.getConnection()
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setObject(1, id)
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    candidates.add(new AnonymousCandidateDTO(resultSet.getInt("candidate_id"),
                            resultSet.getString("candidate_graduation"),
                            resultSet.getString("candidate_description"),
                            resultSet.getString("candidate_competencies")
                                    .split(',')
                                    .collect { it.trim() }
                                    .collect { Competency.builder().name(it).build() }))
                }
            }

        } catch (Exception e) {
            throw new DataAccessException("Error fetching candidates", e)
        }
        return candidates
    }

    boolean hasLikedJob(int idCandidate, int idJob) {
        String sql = """
            SELECT * from candidate_like
            WHERE id_candidate = ? AND id_job = ?
        """

        try (Connection connection = DatabaseHandler.getConnection()
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, idCandidate)
            statement.setInt(2, idJob)
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) return true
            }
            return false
        } catch (Exception e) {
            throw new DataAccessException("Error fetching likes", e)
        }
    }

    Candidate create(Candidate candidate) {
        String sql = """
            INSERT INTO candidate (first_name, last_name, id_person, cpf, birthday, graduation) VALUES
            (?, ?, ?, ?, ?, ?);
        """

        Connection connection = DatabaseHandler.getConnection()
        connection.autoCommit = false

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            candidate.idAddress = addressDAO.create(connection, candidate.address)
            candidate.idPerson = personDAO.create(connection, candidate)

            statement.setString(1, candidate.firstName)
            statement.setString(2, candidate.lastName)
            statement.setInt(3, candidate.idPerson)
            statement.setString(4, candidate.cpf)
            statement.setDate(5, Date.valueOf(candidate.birthday))
            statement.setString(6, candidate.graduation)
            statement.executeUpdate()
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    candidate.idCandidate = resultSet.getInt("id")
                }
            }
            candidate.competencies.forEach { Competency c -> createCandidateCompetency(connection, candidate.idCandidate, c) }

            connection.commit()
            return candidate
        } catch (Exception e) {
            if (connection != null) connection.rollback()
            throw new DataAccessException("Error creating candidate", e)
        } finally {
            DatabaseHandler.closeQuietly(connection)
        }
    }

    void createCandidateCompetency(Connection connection, int idCandidate, Competency competency) {
        Competency existing = competencyDAO.getByName(connection, competency.name)
        if (!existing) {
            existing = competencyDAO.createWithConnection(connection, competency)
        }
        String sql = "INSERT INTO candidate_competency (id_candidate, id_competency) VALUES (?, ?);"
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idCandidate)
            statement.setInt(2, existing.id)
            statement.executeUpdate()
        } catch (Exception e) {
            if (connection != null) {
                connection.rollback()
            }
            throw new DataAccessException("Error creating candidate competency", e)
        }
    }

    Candidate update(int id, Candidate candidate) {
        String sql = """
            UPDATE candidate SET first_name = ?, last_name = ?, cpf = ?, birthday = ?, graduation = ?
            WHERE id = ?
        """

        Connection connection = DatabaseHandler.getConnection()
        connection.autoCommit = false

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            Candidate currentCandidate = getById(id)
            if (currentCandidate == null) {
                throw new DataAccessException("Candidate not found")
            }

            candidate.idCandidate = currentCandidate.idCandidate
            candidate.idPerson = currentCandidate.idPerson
            candidate.idAddress = currentCandidate.idAddress
            candidate.email = candidate.email ?: currentCandidate.email
            candidate.description = candidate.description ?: currentCandidate.description
            candidate.passwd = candidate.passwd ?: currentCandidate.passwd

            if (candidate.address != null) {
                addressDAO.update(connection, candidate.idAddress, candidate.address)
            }

            personDAO.update(connection, candidate)

            if (candidate.competencies != null) {
                updateCandidateCompetencies(connection, id, candidate.competencies)
            }

            statement.setString(1, candidate.firstName)
            statement.setString(2, candidate.lastName)
            statement.setString(3, candidate.cpf)
            statement.setDate(4, Date.valueOf(candidate.birthday))
            statement.setString(5, candidate.graduation)
            statement.setInt(6, id)
            statement.executeUpdate()

            connection.commit()
            return getById(id)
        } catch (Exception e) {
            if (connection != null) connection.rollback()
            throw new DataAccessException("Error updating candidate", e)
        } finally {
            DatabaseHandler.closeQuietly(connection)
        }
    }

    void updateCandidateCompetencies(Connection connection, int idCandidate, List<Competency> newCompetencies) {
        if (newCompetencies == null) return
        String deleteSql = "DELETE FROM candidate_competency WHERE id_candidate = ?"
        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
            deleteStatement.setInt(1, idCandidate)
            deleteStatement.executeUpdate()
        } catch (Exception e) {
            throw new DataAccessException("Error updating candidate competencies", e)
        }
        newCompetencies.forEach { Competency c -> createCandidateCompetency(connection, idCandidate, c)
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

        try (Connection connection = DatabaseHandler.getConnection()
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, idCandidate)
            statement.setInt(2, idJob)

            try (ResultSet response = statement.executeQuery()) {
                if (response.next()) return true
            }

        } catch (Exception e) {
            throw new DataAccessException("Error fetching like", e)
        }
        return false
    }
}