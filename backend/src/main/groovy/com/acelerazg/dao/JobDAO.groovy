package com.acelerazg.dao

import com.acelerazg.model.Address
import com.acelerazg.model.Competency
import com.acelerazg.model.Job
import com.acelerazg.persistence.DatabaseHandler
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

    List<Job> getAllByCompanyId(int id) {
        String sql = """
            SELECT
                j.id, j.name, j.description, j.id_address,
                c.name as company_name, c.id as id_company,
                a.state, a.city
            FROM job j
            INNER JOIN company c on j.id_company = c.id
            INNER JOIN person p ON c.id_person = p.id
            INNER JOIN address a ON p.id_address = a.id
            WHERE id_company = ?
        """
        List<Job> jobs = []

        try (Connection connection = DatabaseHandler.getConnection()
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id)
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    jobs.add(new Job(resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getInt("id_address"),
                            resultSet.getInt("id_company")))
                }
            }
        }
        return jobs
    }

    Job getById(int id) {
        String sql = """
        SELECT
            j.id, j.name, j.description, j.id_address, j.id_company
            a.city, a.state,
            c.name as company_name
        FROM job j
        INNER JOIN address a ON j.id_address = a.id
        INNER JOIN company c ON j.id_company = c.id
        WHERE j.id = ?
        """
        Job job = null

        try (Connection connection = DatabaseHandler.getConnection()
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, id)
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    job = new Job(resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getInt("id_address"),
                            resultSet.getInt("id_company"))
                }
            }
        }
        return job
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

            competencies.forEach { createJobCompetency(connection, job.id, it) }

            connection.commit()
        } catch (Exception e) {
            if (connection != null) connection.rollback()
            throw e
        } finally {
            DatabaseHandler.closeQuietly(connection)
        }
        return job
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
            Job currentJob = getById(id)
            addressDAO.update(connection, currentJob.idAddress, address)
            updateJobCompetencies(connection, id, newCompetencies)

            statement.setString(1, job.name)
            statement.setString(2, job.description)
            statement.setInt(3, id)
            statement.executeUpdate()

            connection.commit()
        } catch (Exception e) {
            if (connection != null) connection.rollback()
            throw e
        } finally {
            DatabaseHandler.closeQuietly(connection)
        }
        return getById(id)
    }

    void updateJobCompetencies(Connection connection, int jobId, List<Competency> newCompetencies) {
        if (newCompetencies == null) return
        String deleteSql = "DELETE FROM job_competency WHERE id_job = ?"

        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
            deleteStatement.setInt(1, jobId)
            deleteStatement.executeUpdate()
        }
        newCompetencies.forEach { c -> createJobCompetency(connection, jobId, c)
        }
    }

    void delete(int id) {
        String sql = "DELETE FROM job WHERE id = ?"
        deleteGeneric(id, sql)
    }
}