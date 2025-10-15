package com.acelerazg.dao

import com.acelerazg.exceptions.DataAccessException
import com.acelerazg.model.Address
import groovy.transform.CompileStatic

import java.sql.*

@CompileStatic
class AddressDAO {
    int create(Connection connection, Address address) {
        String sql = """
            INSERT INTO address (state, postal_code, country, city, street) VALUES
            (?, ?, ?, ?, ?);
        """
        int idAddress = 0

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, address.state)
            statement.setString(2, address.postalCode)
            statement.setString(3, address.country)
            statement.setString(4, address.city)
            statement.setString(5, address.street)
            statement.executeUpdate()
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) idAddress = resultSet.getInt(1)
            }

        } catch (SQLException e) {
            if (connection != null) connection.rollback()
            throw new DataAccessException("Error creating address", e)
        }
        return idAddress
    }

    void update(Connection connection, int id, Address address) {
        String sql = """
            UPDATE address SET state = ?, postal_code = ?, country = ?, city = ?, street = ?
            WHERE id = ?
        """

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, address.state)
            statement.setString(2, address.postalCode)
            statement.setString(3, address.country)
            statement.setString(4, address.city)
            statement.setString(5, address.street)
            statement.setInt(6, id)
            statement.executeUpdate()
        } catch (SQLException e) {
            if (connection != null) connection.rollback()
            throw new DataAccessException("Error updating address", e)
        }
    }
}