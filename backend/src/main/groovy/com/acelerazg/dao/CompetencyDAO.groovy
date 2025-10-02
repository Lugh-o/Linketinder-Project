package com.acelerazg.dao

import com.acelerazg.model.Competency
import com.acelerazg.persistence.DatabaseHandler
import groovy.transform.CompileStatic

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

@CompileStatic
class CompetencyDAO extends DAO {
    static List<Competency> getAll() {
        String sql = "SELECT * FROM competency"

        Connection connection = null
        PreparedStatement statement = null
        ResultSet response = null
        List<Competency> competencies = []

        try {
            connection = DatabaseHandler.getConnection()
            statement = connection.prepareStatement(sql)
            response = statement.executeQuery()
            while (response.next()) {
                competencies.add(new Competency(
                        response.getString("name"),
                        response.getInt("id")
                ))
            }
        } finally {
            DatabaseHandler.closeQuietly(response, statement, connection)
        }
        return competencies
    }

    static Competency getByName(Connection connection, String name) {
        String sql = """
            SELECT * from competency c
            WHERE c.name = ?;
        """

        PreparedStatement statement = null
        ResultSet response = null
        Competency competency = null

        try {
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            statement.setString(1, name)
            response = statement.executeQuery()
            if (response.next()) {
                competency = new Competency(
                        response.getString("name"),
                        response.getInt("id")
                )
            }
        } finally {
            DatabaseHandler.closeQuietly(response, statement)
        }
        return competency
    }

    static Competency create(Competency competency) {
        String sql = "INSERT INTO competency (name) VALUES (?)"
        Connection connection = null
        PreparedStatement statement = null
        ResultSet response = null

        try {
            connection = DatabaseHandler.getConnection()
            connection.autoCommit = false

            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            statement.setString(1, competency.name)
            statement.executeUpdate()
            response = statement.getGeneratedKeys()

            if (response.next()) {
                competency.id = response.getInt(1)
            }
            connection.commit()
        } catch (Exception e) {
            if (connection != null) connection.rollback()
            throw e
        } finally {
            DatabaseHandler.closeQuietly(response, statement, connection)
        }
        return competency
    }

    static Competency createWithConnection(Connection connection, Competency competency) {
        String sql = "INSERT INTO competency (name) VALUES (?)"
        PreparedStatement statement = null
        ResultSet response = null

        try {
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            statement.setString(1, competency.name)
            statement.executeUpdate()
            response = statement.getGeneratedKeys()

            if (response.next()) {
                competency.id = response.getInt(1)
            }
        } catch (Exception e) {
            if (connection != null) connection.rollback()
            throw e
        } finally {
            DatabaseHandler.closeQuietly(response, statement)
        }
        return competency
    }

    static void delete(int id) {
        String sql = "DELETE FROM competency WHERE id = ?"
        deleteGeneric(id, sql)
    }
}