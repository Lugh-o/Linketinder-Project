package com.acelerazg.dao

import com.acelerazg.model.Competency
import com.acelerazg.persistency.DatabaseHandler
import groovy.transform.CompileStatic

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

@CompileStatic
class CompetencyDAO extends DAO {
    List<Competency> getAll() {
        String sql = "SELECT * FROM competency"
        List<Competency> competencies = []

        try (Connection connection = DatabaseHandler.getConnection()
             PreparedStatement statement = connection.prepareStatement(sql)
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                competencies.add(new Competency(resultSet.getString("name"),
                        resultSet.getInt("id")))
            }
        }
        return competencies
    }

    Competency getByName(Connection connection, String name) {
        String sql = """
            SELECT * from competency c
            WHERE c.name = ?;
        """
        Competency competency = null

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name)
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    competency = new Competency(resultSet.getString("name"),
                            resultSet.getInt("id"))
                }
            }
        }
        return competency
    }

    Competency create(Competency competency) {
        String sql = "INSERT INTO competency (name) VALUES (?)"
        Connection connection = DatabaseHandler.getConnection()
        connection.autoCommit = false

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, competency.name)
            statement.executeUpdate()
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    competency.id = resultSet.getInt(1)
                }
            }

            connection.commit()
        } catch (Exception e) {
            if (connection != null) connection.rollback()
            throw e
        } finally {
            DatabaseHandler.closeQuietly(connection)
        }
        return competency
    }

    Competency createWithConnection(Connection connection, Competency competency) {
        String sql = "INSERT INTO competency (name) VALUES (?)"

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, competency.name)
            statement.executeUpdate()

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    competency.id = resultSet.getInt(1)
                }
            }

        } catch (Exception e) {
            if (connection != null) connection.rollback()
            throw e
        }
        return competency
    }

    void delete(int id) {
        String sql = "DELETE FROM competency WHERE id = ?"
        deleteGeneric(id, sql)
    }
}