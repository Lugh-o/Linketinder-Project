package com.acelerazg.dao

import com.acelerazg.persistence.DatabaseHandler
import groovy.transform.CompileStatic

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.Statement

@CompileStatic
class DAO {
    static void deleteGeneric(int id, String sql) {
        Connection connection = null
        PreparedStatement statement = null

        try {
            connection = DatabaseHandler.getConnection()
            connection.autoCommit = false

            statement = connection.prepareStatement(sql)
            statement.setInt(1, id)
            statement.executeUpdate()

            connection.commit()
        } catch (Exception e) {
            if (connection != null) connection.rollback()
            throw e
        } finally {
            DatabaseHandler.closeQuietly(statement, connection)
        }
    }

    static void likeGeneric(int originId, int targetId, String sql) {
        Connection connection = null
        PreparedStatement statement = null

        try {
            connection = DatabaseHandler.getConnection()
            connection.autoCommit = false

            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            statement.setInt(1, originId)
            statement.setInt(2, targetId)
            statement.executeUpdate()

            connection.commit()
        } catch (Exception e) {
            if (connection != null) connection.rollback()
            throw e
        } finally {
            DatabaseHandler.closeQuietly(statement, connection)
        }
    }
}