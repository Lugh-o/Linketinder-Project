package com.acelerazg.dao

import com.acelerazg.model.Address
import com.acelerazg.model.Company
import com.acelerazg.persistence.DatabaseHandler
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

        Connection connection = null
        PreparedStatement statement = null
        ResultSet response = null
        List<Company> companies = []

        try {
            connection = DatabaseHandler.getConnection()
            statement = connection.prepareStatement(sql)
            response = statement.executeQuery()
            while (response.next()) {
                companies.add(new Company(
                        response.getInt("id_person"),
                        response.getString("email"),
                        response.getString("description"),
                        response.getInt("id_address"),
                        response.getInt("id_company"),
                        response.getString("name"),
                        response.getString("cnpj")
                ))
            }
        } finally {
            DatabaseHandler.closeQuietly(response, statement, connection)
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

        Connection connection = null
        PreparedStatement statement = null
        ResultSet response = null
        Company company = null

        try {
            connection = DatabaseHandler.getConnection()
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            statement.setInt(1, id)
            response = statement.executeQuery()
            if (response.next()) {
                company = new Company(
                        response.getInt("id_person"),
                        response.getString("email"),
                        response.getString("description"),
                        response.getString("passwd"),
                        response.getInt("id_address"),
                        response.getInt("id_company"),
                        response.getString("name"),
                        response.getString("cnpj")
                )
            }
        } finally {
            DatabaseHandler.closeQuietly(response, statement, connection)
        }
        return company
    }

    Company create(Company company, Address address) {
        String sql = """
            INSERT INTO company (name, cnpj, id_person) VALUES
            (?, ?, ?)
        """

        Connection connection = null
        PreparedStatement statement = null
        ResultSet response = null

        try {
            connection = DatabaseHandler.getConnection()
            connection.autoCommit = false

            company.idAddress = addressDAO.create(connection, address)
            company.idPerson = personDAO.create(connection, company)

            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            statement.setString(1, company.name)
            statement.setString(2, company.cnpj)
            statement.setInt(3, company.idPerson)
            statement.executeUpdate()
            response = statement.getGeneratedKeys()

            if (response.next()) {
                company.idCompany = response.getInt(1)
            }
            connection.commit()
        } catch (Exception e) {
            if (connection != null) connection.rollback()
            throw e
        } finally {
            DatabaseHandler.closeQuietly(response, statement, connection)
        }

        return company

    }

    Company update(int id, Company company, Address address) {
        String sql = """
            UPDATE company SET name = ?, cnpj = ?
            WHERE id = ?
        """

        Connection connection = null
        PreparedStatement statement = null

        try {
            connection = DatabaseHandler.getConnection()
            connection.autoCommit = false

            Company currentCompany = getById(id)

            addressDAO.update(connection, currentCompany.idAddress, address)
            personDAO.update(connection, company)

            statement = connection.prepareStatement(sql)
            statement.setString(1, company.name)
            statement.setString(2, company.cnpj)
            statement.setInt(3, id)
            statement.executeUpdate()

            connection.commit()
        } catch (Exception e) {
            if (connection != null) connection.rollback()
            throw e
        } finally {
            DatabaseHandler.closeQuietly(statement, connection)
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

        Connection connection = null
        PreparedStatement statement = null
        ResultSet response = null

        try {
            connection = DatabaseHandler.getConnection()
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            statement.setInt(1, idCompany)
            statement.setInt(2, idCandidate)

            response = statement.executeQuery()
            if (response.next()) {
                return true
            }
        } finally {
            DatabaseHandler.closeQuietly(response, statement, connection)
        }
        return false
    }
}