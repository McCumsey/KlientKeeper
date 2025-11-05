package Database;

import Model.Customer;
import Model.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**

 This class represents a database interface for managing customer information.
 */
public class CustomersDB {

    /**
     Retrieves a list of all customers from the database.
     @return An ObservableList of Customer objects representing the customers.
     @throws SQLException if an error occurs while accessing the database.
     */
    public static ObservableList<Customer> getCustomers() throws SQLException {
        ObservableList<Customer> customers = FXCollections.observableArrayList();

        String searchStatement = "SELECT * FROM customers AS c INNER JOIN first_level_divisions AS d ON c.Division_ID = d.Division_ID INNER JOIN countries AS co ON co.Country_ID=d.COUNTRY_ID;";

        Helper.setPreparedStatement(JDBC.getConnection(), searchStatement);
        PreparedStatement preparedStatement = Helper.getPreparedStatement();

        try {
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                Customer newCustomer = new Customer(
                        resultSet.getInt("Customer_ID"),
                        resultSet.getString("Customer_Name"),
                        resultSet.getString("Address"),
                        resultSet.getString("Postal_Code"),
                        resultSet.getString("Phone"),
                        resultSet.getString("Division"),
                        resultSet.getString("Country"),
                        resultSet.getInt("Division_ID")
                );

                customers.add(newCustomer);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());

        }

        return customers;
    }

    /**
     Retrieves a customer from the database based on the given customer name.

     @param customerName The name of the customer to retrieve.
     @return A Customer object representing the customer with the given name, or null if not found.
     @throws SQLException if an error occurs while accessing the database.
     */
    public static Customer getCustomerId(String customerName) throws SQLException {
        String queryStatement = "SELECT * FROM customers AS c INNER JOIN first_level_divisions AS d ON c.Division_ID = d.Division_ID INNER JOIN countries AS co ON co.Country_ID=d.COUNTRY_ID WHERE Customer_Name=?;";

        Helper.setPreparedStatement(JDBC.getConnection(), queryStatement);
        PreparedStatement preparedStatement = Helper.getPreparedStatement();

        preparedStatement.setString(1, customerName);

        try {
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();

            if (resultSet.next()) {
                Customer newCustomer = new Customer(
                        resultSet.getInt("Customer_ID"),
                        resultSet.getString("Customer_Name"),
                        resultSet.getString("Address"),
                        resultSet.getString("Postal_Code"),
                        resultSet.getString("Phone"),
                        resultSet.getString("Division"),
                        resultSet.getString("Country"),
                        resultSet.getInt("Division_ID")
                );

                return newCustomer;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return null;
    }

    /**
     Retrieves a customer from the database based on the given customer name.

     @param customerName The name of the customer to retrieve.
     @return A Customer object representing the customer with the given name, or null if not found.
     @throws SQLException if an error occurs while accessing the database.
     */
    public static Customer getCustomerName(String customerName) throws SQLException {
        String queryStatement = "SELECT * FROM customers AS c INNER JOIN first_level_divisions AS d ON c.Division_ID = d.Division_ID INNER JOIN countries AS co ON co.Country_ID=d.COUNTRY_ID WHERE Customer_Name=?;";

        Helper.setPreparedStatement(JDBC.getConnection(), queryStatement);
        PreparedStatement preparedStatement = Helper.getPreparedStatement();

        preparedStatement.setString(1, customerName);

        try {
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();

            if (resultSet.next()) {
                Customer newCustomer = new Customer(
                        resultSet.getInt("Customer_ID"),
                        resultSet.getString("Customer_Name"),
                        resultSet.getString("Address"),
                        resultSet.getString("Postal_Code"),
                        resultSet.getString("Phone"),
                        resultSet.getString("Division"),
                        resultSet.getString("Country"),
                        resultSet.getInt("Division_ID")
                );

                return newCustomer;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return null;
    }

    /**

     Creates a new customer in the database with the given details.

     @param name The name of the customer.
     @param address The address of the customer.
     @param postalCode The postal code of the customer.
     @param phone The phone number of the customer.
     @param division The division of the customer.
     @return true if the customer is created successfully, false otherwise.
     @throws SQLException if an error occurs while accessing the database.
     */
    public static boolean createCustomer(String name, String address, String postalCode, String phone, String division) throws SQLException {
        Division newDivision = DivisionDB.getDivisionId(division);

        String insertStatement = "INSERT INTO customers(Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES (?, ?, ?, ?, ?)";

        Helper.setPreparedStatement(JDBC.getConnection(), insertStatement);
        PreparedStatement preparedStatement = Helper.getPreparedStatement();

        preparedStatement.setString(1, name);
        preparedStatement.setString(2, address);
        preparedStatement.setString(3, postalCode);
        preparedStatement.setString(4, phone);
        preparedStatement.setInt(5, newDivision.getDivisionId());

        try {
            preparedStatement.execute();

            if (preparedStatement.getUpdateCount() > 0) {
                System.out.println("Rows affected: " + preparedStatement.getUpdateCount());
            } else {
                System.out.println("No change");
            }

            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());

            return false;
        }
    }

    /**

     Updates an existing customer in the database with the given details.

     @param customerId The ID of the customer to update.
     @param name The new name of the customer.
     @param address The new address of the customer.
     @param postalCode The new postal code of the customer.
     @param phone The new phone number of the customer.
     @param division The new division of the customer.
     @retun true if the customer is updated successfully, false otherwise.
     @throws SQLException if an error occurs while accessing the database.
     */
    public static boolean updateCustomer(int customerId, String name, String address, String postalCode, String phone, String division) throws SQLException {
        Division newDivision = DivisionDB.getDivisionId(division);

        String insertStatement = "UPDATE customers SET Customer_Name=?, Address=?, Postal_Code=?, Phone=?, Division_ID=? WHERE Customer_ID=?";

        Helper.setPreparedStatement(JDBC.getConnection(), insertStatement);
        PreparedStatement preparedStatement = Helper.getPreparedStatement();

        preparedStatement.setString(1, name);
        preparedStatement.setString(2, address);
        preparedStatement.setString(3, postalCode);
        preparedStatement.setString(4, phone);
        preparedStatement.setInt(5, newDivision.getDivisionId());
        preparedStatement.setInt(6, customerId);

        try {
            preparedStatement.execute();

            if (preparedStatement.getUpdateCount() > 0) {
                System.out.println("Rows affected: " + preparedStatement.getUpdateCount());
            } else {
                System.out.println("No change");
            }

            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());

            return false;
        }
    }

    /**

     Deletes a customer from the database with the given customer ID.

     @param customerId The ID of the customer to delete.

     @return true if the customer is deleted successfully, false otherwise.

     @throws SQLException if an error occurs while accessing the database.
     */
    public static boolean deleteCustomer(int customerId) throws SQLException {
        String insertStatement = "DELETE from customers WHERE Customer_Id=?";

        Helper.setPreparedStatement(JDBC.getConnection(), insertStatement);
        PreparedStatement preparedStatement = Helper.getPreparedStatement();

        preparedStatement.setInt(1, customerId);

        try {
            preparedStatement.execute();

            if (preparedStatement.getUpdateCount() > 0) {
                System.out.println("Rows affected: " + preparedStatement.getUpdateCount());
            } else {
                System.out.println("No change");
            }

            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());

            return false;
        }
    }

}