package com.acelerazg.dao

import com.acelerazg.model.Person
import groovy.transform.CompileStatic

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

@CompileStatic
class PersonDAO {
    int create(Connection connection, Person person) {
        String sql = """
            INSERT INTO person (email, description, passwd, id_address) VALUES
            (?, ?, ?, ?);
        """
        int idPerson = 0

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, person.email)
            statement.setString(2, person.description)
            statement.setString(3, person.passwd)
            statement.setInt(4, person.idAddress)
            statement.executeUpdate()
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    idPerson = resultSet.getInt(1)
                }
            }
        } catch (Exception e) {
            if (connection != null) connection.rollback()
            throw e
        }
        return idPerson
    }

    void update(Connection connection, Person person) {
        String sql = """
            UPDATE person SET email = ?, description = ?, passwd = ?
            WHERE id = ?
        """

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, person.email)
            statement.setString(2, person.description)
            statement.setString(3, person.passwd)
            statement.setInt(4, person.idPerson)
            statement.executeUpdate()

            connection.commit()
        } catch (Exception e) {
            if (connection != null) connection.rollback()
            throw e
        }
    }
}