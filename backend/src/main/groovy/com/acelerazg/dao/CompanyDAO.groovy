package com.acelerazg.dao

import com.acelerazg.exceptions.DataAccessException
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
                p.id as id_person, p.email, p.description, 
                a.id as id_address, a.state, a.postal_code, a.country, a.street, a.city
            FROM company c
            INNER JOIN person p ON c.id_person = p.id
            INNER JOIN address a ON p.id_address = a.id
        """
        List<Company> companies = []

        try (Connection connection = DatabaseHandler.getConnection()
             PreparedStatement statement = connection.prepareStatement(sql)
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Address address = Address.builder()
                        .state(resultSet.getString("state"))
                        .postalCode(resultSet.getString("postal_code"))
                        .country(resultSet.getString("country"))
                        .city(resultSet.getString("city"))
                        .street(resultSet.getString("street"))
                        .build()

                companies.add(new Company(resultSet.getString("description"),
                        resultSet.getString("email"),
                        resultSet.getString("name"),
                        resultSet.getString("cnpj"),
                        resultSet.getInt("id_person"),
                        address,
                        resultSet.getInt("id_address"),
                        resultSet.getInt("id_company")))
            }
            return companies
        } catch (Exception e) {
            throw new DataAccessException("Error fetching candidates", e)
        }
    }

    private Company getCompanyGeneric(String sql, Object param) {
        try (Connection connection = DatabaseHandler.getConnection()
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, param)
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Address address = Address.builder()
                            .state(resultSet.getString("state"))
                            .postalCode(resultSet.getString("postal_code"))
                            .country(resultSet.getString("country"))
                            .street(resultSet.getString("street"))
                            .city(resultSet.getString("city"))
                            .build()

                    return new Company(resultSet.getString("description"),
                            resultSet.getString("email"),
                            resultSet.getString("name"),
                            resultSet.getString("cnpj"),
                            resultSet.getInt("id_person"),
                            address,
                            resultSet.getInt("id_address"),
                            resultSet.getInt("id_company"))
                }
            }
        } catch (Exception e) {
            e.printStackTrace()
            throw new DataAccessException("Error fetching company", e)
        }
    }

    Company getById(int id) {
        String sql = """
            SELECT 
                c.id as id_company, c.name, c.cnpj, 
                p.id as id_person, p.email, p.description, p.passwd,
                a.id as id_address, a.state, a.postal_code, a.country, a.street, a.city
            FROM company c
            INNER JOIN person p ON c.id_person = p.id
            INNER JOIN address a ON a.id = p.id_address
            WHERE c.id = ?        
        """
        return getCompanyGeneric(sql, id)
    }

    Company getByEmail(String email) {
        String sql = """
            SELECT 
                c.id as id_company, c.name, c.cnpj, 
                p.id as id_person, p.email, p.description, p.passwd,
                a.id as id_address, a.state, a.postal_code, a.country, a.street, a.city
            FROM company c
            INNER JOIN person p ON c.id_person = p.id
            INNER JOIN address a ON a.id = p.id_address
            WHERE p.email = ?        
        """
        return getCompanyGeneric(sql, email)
    }

    Company create(Company company) {
        String sql = """
            INSERT INTO company (name, cnpj, id_person) VALUES
            (?, ?, ?)
        """

        Connection connection = DatabaseHandler.getConnection()
        connection.autoCommit = false

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            company.idAddress = addressDAO.create(connection, company.address)
            company.idPerson = personDAO.create(connection, company)

            statement.setString(1, company.name)
            statement.setString(2, company.cnpj)
            statement.setInt(3, company.idPerson)

            statement.executeUpdate()
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    company.idCompany = resultSet.getInt(1)
                }
            }
            connection.commit()
            return company
        } catch (Exception e) {
            if (connection != null) connection.rollback()
            throw e
        } finally {
            DatabaseHandler.closeQuietly(connection)
        }
    }

    Company update(int id, Company company) {
        String sql = """
            UPDATE company SET name = ?, cnpj = ?
            WHERE id = ?
        """

        Connection connection = DatabaseHandler.getConnection()
        connection.autoCommit = false

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            Company currentCompany = getById(id)
            if (currentCompany == null) {
                throw new DataAccessException("Candidate not found")
            }
            company.idCompany = currentCompany.idCompany
            company.idPerson = currentCompany.idPerson
            company.idAddress = currentCompany.idAddress
            company.email = company.email ?: currentCompany.email
            company.description = company.description ?: currentCompany.description
            company.passwd = company.passwd ?: currentCompany.passwd

            if (company.address != null) {
                addressDAO.update(connection, company.idAddress, company.address)
            }

            personDAO.update(connection, company)

            statement.setString(1, company.name)
            statement.setString(2, company.cnpj)
            statement.setInt(3, id)
            statement.executeUpdate()

            connection.commit()
            return getById(id)
        } catch (Exception e) {
            if (connection != null) connection.rollback()
            throw e
        } finally {
            DatabaseHandler.closeQuietly(connection)
        }
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