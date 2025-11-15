package com.acelerazg.dao

import com.acelerazg.dto.JobDTO
import com.acelerazg.exceptions.DataAccessException
import com.acelerazg.model.Address
import com.acelerazg.model.Competency
import com.acelerazg.model.Job
import com.acelerazg.persistency.DatabaseHandler
import groovy.transform.CompileStatic

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

@CompileStatic
class JobDAO extends DAO {
    private final AddressDAO addressDAO
    private final CompetencyDAO competencyDAO

    JobDAO(AddressDAO addressDAO, CompetencyDAO competencyDAO) {
        this.addressDAO = addressDAO
        this.competencyDAO = competencyDAO
    }

    List<JobDTO> getAllByCompanyId(int id) {
        String sql = """
            SELECT
                j.id as id_job, j.name, j.description,
                c.id as id_company, c.name as company_name, c.cnpj as company_cnpj, 
                a.id as id_address, a.state, a.postal_code, a.country, a.street, a.city,
                COALESCE(STRING_AGG(co.name, ', '), '') AS competencies
            FROM job j
            INNER JOIN company c on j.id_company = c.id
            INNER JOIN person p ON c.id_person = p.id
            INNER JOIN address a ON p.id_address = a.id
            INNER JOIN job_competency jc ON jc.id_job = j.id
            INNER JOIN competency co ON jc.id_competency = co.id
            WHERE id_company = ?
            GROUP BY j.id, c.id, a.id;
        """
        List<JobDTO> jobs = []

        try (Connection connection = DatabaseHandler.getConnection()
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id)
            try (ResultSet resultSet = statement.executeQuery()) {
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

                    jobs.add(new JobDTO(resultSet.getString("name"),
                            resultSet.getString("description"),
                            address,
                            competencyList,
                            resultSet.getInt("id_company"),
                            resultSet.getInt("id_address"),
                            resultSet.getInt("id_job"),))
                }
                return jobs
            }
        } catch (Exception e) {
            throw new DataAccessException("Error fetching jobs", e)
        }
    }

    JobDTO getById(int id) {
        String sql = """
            SELECT
                j.id as id_job, j.name, j.description,
                c.id as id_company, c.name as company_name, c.cnpj as company_cnpj, 
                a.id as id_address, a.state, a.postal_code, a.country, a.street, a.city,
                COALESCE(STRING_AGG(co.name, ', '), '') AS competencies
            FROM job j
            INNER JOIN company c on j.id_company = c.id
            INNER JOIN person p ON c.id_person = p.id
            INNER JOIN address a ON p.id_address = a.id
            INNER JOIN job_competency jc ON jc.id_job = j.id
            INNER JOIN competency co ON jc.id_competency = co.id
            WHERE j.id = ?
            GROUP BY j.id, c.id, a.id;
        """
        try (Connection connection = DatabaseHandler.getConnection()
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, id)
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

                    return new JobDTO(resultSet.getString("name"),
                            resultSet.getString("description"),
                            address,
                            competencyList,
                            resultSet.getInt("id_company"),
                            resultSet.getInt("id_address"),
                            resultSet.getInt("id_job"))
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Error fetching job", e)
        }
    }

    Job create(Job job, Address address, List<Competency> competencies) {
        String sql = """
            INSERT INTO job (name, description, id_company, id_address) VALUES
            (?, ?, ?, ?);
        """

        Connection connection = DatabaseHandler.getConnection()
        connection.autoCommit = false

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            job.idAddress = addressDAO.create(connection, address)

            statement.setString(1, job.name)
            statement.setString(2, job.description)
            statement.setInt(3, job.idCompany)
            statement.setInt(4, job.idAddress)
            statement.executeUpdate()
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) job.id = resultSet.getInt("id")
            }

            competencies.forEach { Competency c -> createJobCompetency(connection, job.id, c) }

            connection.commit()
            return job
        } catch (Exception e) {
            if (connection != null) connection.rollback()
            throw new DataAccessException("Error creating job", e)
        } finally {
            DatabaseHandler.closeQuietly(connection)
        }
    }

    void createJobCompetency(Connection connection, int jobId, Competency competency) {
        Competency existing = competencyDAO.getByName(connection, competency.name)
        if (!existing) {
            existing = competencyDAO.createWithConnection(connection, competency)
        }

        String sql = "INSERT INTO job_competency (id_job, id_competency) VALUES (?, ?);"
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, jobId)
            statement.setInt(2, existing.id)
            statement.executeUpdate()
        } catch (Exception e) {
            if (connection != null) {
                connection.rollback()
            }
            throw new DataAccessException("Error creating job competency", e)
        }
    }

    Job update(int id, Job job, Address address, List<Competency> newCompetencies) {
        String sql = """
            UPDATE job SET name = ?, description = ?
            WHERE id = ?
        """

        Connection connection = DatabaseHandler.getConnection()
        connection.autoCommit = false

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            Job currentJob = getById(id).toModel()
            if (currentJob == null) {
                throw new DataAccessException("Job not found")
            }

            job.name = job.name ?: currentJob.name
            job.description = job.description ?: currentJob.description
            job.idAddress = currentJob.idAddress
            job.idCompany = currentJob.idCompany

            if (address != null) {
                addressDAO.update(connection, job.idAddress, address)
            }

            if (newCompetencies != null) {
                updateJobCompetencies(connection, id, newCompetencies)
            }

            statement.setString(1, job.name)
            statement.setString(2, job.description)
            statement.setInt(3, id)
            statement.executeUpdate()

            connection.commit()
            return getById(id).toModel()
        } catch (Exception e) {
            if (connection != null) connection.rollback()
            throw e
        } finally {
            DatabaseHandler.closeQuietly(connection)
        }
    }

    void updateJobCompetencies(Connection connection, int jobId, List<Competency> newCompetencies) {
        if (newCompetencies == null) return
        String deleteSql = "DELETE FROM job_competency WHERE id_job = ?"

        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
            deleteStatement.setInt(1, jobId)
            deleteStatement.executeUpdate()
        }
        newCompetencies.forEach { Competency c -> createJobCompetency(connection, jobId, c)
        }
    }

    void delete(int id) {
        String sql = "DELETE FROM job WHERE id = ?"
        deleteGeneric(id, sql)
    }
}