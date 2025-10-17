package com.acelerazg.dao

import com.acelerazg.model.Address
import com.acelerazg.model.Company
import com.acelerazg.persistency.DatabaseHandler
import groovy.transform.CompileStatic

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

@CompileStatic
class CompanyDAO extends DAO {
    private final AddressDAO addressDAO
    private final PersonDAO personDAO

    CompanyDAO(AddressDAO addressDAO, PersonDAO personDAO) {
        this.addressDAO = addressDAO
        this.personDAO = personDAO
    }

    List<Company> getAll() {
        String sql = """
        SELECT 
            c.id as id_company, c.name, c.cnpj,
            c.id_person, p.email, p.description, p.id_address
        FROM company c
        INNER JOIN person p ON c.id_person = p.id
        """
        List<Company> companies = []

        try (Connection connection = DatabaseHandler.getConnection()
             PreparedStatement statement = connection.prepareStatement(sql)
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                companies.add(new Company(resultSet.getInt("id_person"),
                        resultSet.getString("email"),
                        resultSet.getString("description"),
                        resultSet.getInt("id_address"),
                        resultSet.getInt("id_company"),
                        resultSet.getString("name"),
                        resultSet.getString("cnpj")))
            }
        }
        return companies
    }

    Company getById(int id) {
        String sql = """
            SELECT 
                c.id as id_company, c.name, c.cnpj, c.id_person, 
                p.email, p.description, p.passwd
                a.state, a.postal_code, a.country, a.street, a.city
            FROM company c
            INNER JOIN person p ON c.id_person = p.id
            INNER JOIN address a ON a.id = p.id_address
            WHERE c.id = ?        
        """
        Company company = null

        try (Connection connection = DatabaseHandler.getConnection()
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, id)
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    company = new Company(resultSet.getInt("id_person"),
                            resultSet.getString("email"),
                            resultSet.getString("description"),
                            resultSet.getString("passwd"),
                            resultSet.getInt("id_address"),
                            resultSet.getInt("id_company"),
                            resultSet.getString("name"),
                            resultSet.getString("cnpj"))
                }
            }
        }
        return company
    }

    Company create(Company company, Address address) {
        String sql = """
            INSERT INTO company (name, cnpj, id_person) VALUES
            (?, ?, ?)
        """

        Connection connection = DatabaseHandler.getConnection()
        connection.autoCommit = false

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            company.idAddress = addressDAO.create(connection, address)
            company.idPerson = personDAO.create(connection, company)

            statement.setString(2, company.cnpj)
            statement.setInt(3, company.idPerson)

            statement.executeUpdate()
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) company.idCompany = resultSet.getInt(1)
            }
            connection.commit()
        } catch (Exception e) {
            if (connection != null) connection.rollback()
            throw e
        } finally {
            DatabaseHandler.closeQuietly(connection)
        }
        return company
    }

    Company update(int id, Company company, Address address) {
        String sql = """
            UPDATE company SET name = ?, cnpj = ?
            WHERE id = ?
        """

        Connection connection = DatabaseHandler.getConnection()
        connection.autoCommit = false

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            Company currentCompany = getById(id)

            addressDAO.update(connection, currentCompany.idAddress, address)
            personDAO.update(connection, company)

            statement.setString(1, company.name)
            statement.setString(2, company.cnpj)
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

    void delete(int id) {
        String sql = "DELETE FROM company WHERE id = ?"
        deleteGeneric(id, sql)
    }

    void likeCandidate(int idCompany, int idCandidate) {
        String sql = """
            INSERT INTO company_like (id_company, id_candidate) VALUES (?, ?)
        """
        likeGeneric(idCompany, idCandidate, sql)
    }

    boolean isCandidateAlreadyLiked(int idCompany, int idCandidate) {
        String sql = """
            SELECT * FROM company_like
            WHERE id_company = ? AND id_candidate = ?;        
        """

        try (Connection connection = DatabaseHandler.getConnection()
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, idCompany)
            statement.setInt(2, idCandidate)
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) return true
            }
        }
        return false
    }
}