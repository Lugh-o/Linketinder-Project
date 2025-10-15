package com.acelerazg.dao

import com.acelerazg.dto.MatchDTO
import com.acelerazg.persistence.DatabaseHandler
import groovy.transform.CompileStatic

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

@CompileStatic
class MatchEventDAO extends DAO {
    List<MatchDTO> getAllMatchesByJobId(int idJob) {
        String sql = """
            SELECT 
                CONCAT(cd.first_name, ' ', cd.last_name) AS candidate_name,
                cd.graduation AS candidate_graduation
            FROM match_event me
            INNER JOIN candidate cd ON me.id_candidate = cd.id 
            INNER JOIN job j ON me.id_job = j.id
            WHERE j.id = ?;
        """
        List<MatchDTO> matches = []

        try (Connection connection = DatabaseHandler.getConnection()
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idJob)
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    matches.add(new MatchDTO(resultSet.getString("candidate_name"),
                            resultSet.getString("candidate_graduation")))
                }
            }
        }
        return matches
    }

    void create(int idJob, int idCandidate) {
        String sql = """
            INSERT INTO match_event (id_job, id_candidate) VALUES
            (?, ?);
        """
        Connection connection = DatabaseHandler.getConnection()
        connection.autoCommit = false

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, idJob)
            statement.setInt(2, idCandidate)
            statement.executeUpdate()

            connection.commit()
        } catch (Exception e) {
            if (connection != null) connection.rollback()
            throw e
        } finally {
            DatabaseHandler.closeQuietly(connection)
        }
    }
}