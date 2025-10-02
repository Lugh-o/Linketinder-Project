package com.acelerazg.dao

import com.acelerazg.model.Address
import com.acelerazg.persistence.DatabaseHandler
import groovy.transform.CompileStatic

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

@CompileStatic
class AddressDAO {
    static int create(Connection connection, Address address) {
        String sql = """
            INSERT INTO address (state, postal_code, country, city, street) VALUES
            (?, ?, ?, ?, ?);
        """
        PreparedStatement statement = null
        ResultSet response = null
        int idAddress = 0

        try {
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            statement.setString(1, address.state)
            statement.setString(2, address.postalCode)
            statement.setString(3, address.country)
            statement.setString(4, address.city)
            statement.setString(5, address.street)
            statement.executeUpdate()
            response = statement.getGeneratedKeys()
            if (response.next()) {
                idAddress = response.getInt(1)
            }

        } catch (Exception e) {
            if (connection != null) connection.rollback()
            throw e
        } finally {
            DatabaseHandler.closeQuietly(statement, response)
        }
        return idAddress
    }

    static void update(Connection connection, int id, Address address) {
        String sql = """
            UPDATE address SET state = ?, postal_code = ?, country = ?, city = ?, street = ?
            WHERE id = ?
        """

        PreparedStatement statement = null

        try {
            statement = connection.prepareStatement(sql)
            statement.setString(1, address.state)
            statement.setString(2, address.postalCode)
            statement.setString(3, address.country)
            statement.setString(4, address.city)
            statement.setString(5, address.street)
            statement.setInt(6, id)
            statement.executeUpdate()
        } catch (Exception e) {
            if (connection != null) connection.rollback()
            throw e
        } finally {
            DatabaseHandler.closeQuietly(statement)
        }
    }
}
