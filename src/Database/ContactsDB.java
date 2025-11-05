package Database;
import Model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContactsDB {

    /**
     * Retrieves a list of all contacts from the database.
     *
     * @return ObservableList of Contact objects representing the contacts in the database.
     * @throws SQLException if a database access error occurs.
     */
    public static ObservableList<Contact> getContacts() throws SQLException {
        ObservableList<Contact> contacts = FXCollections.observableArrayList();

        String queryStatement = "SELECT * FROM contacts;";

        Helper.setPreparedStatement(JDBC.getConnection(), queryStatement);
        PreparedStatement preparedStatement = Helper.getPreparedStatement();

        try {
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                Contact newContact = new Contact(
                        resultSet.getInt("Contact_ID"),
                        resultSet.getString("Contact_Name"),
                        resultSet.getString("Email")
                );

                contacts.add(newContact);
            }
            return contacts;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Retrieves a contact from the database based on the given contact name.
     *
     * @param contactName the name of the contact to retrieve.
     * @return Contact object representing the retrieved contact, or null if not found.
     * @throws SQLException if a database access error occurs.
     */
    public static Contact getContactName(String contactName) throws SQLException {
        String queryStatement = "SELECT * FROM contacts WHERE Contact_Name=?;";

        Helper.setPreparedStatement(JDBC.getConnection(), queryStatement);
        PreparedStatement preparedStatement = Helper.getPreparedStatement();

        preparedStatement.setString(1, contactName);

        try {
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();

            if (resultSet.next()) {
                Contact newContact = new Contact(
                        resultSet.getInt("Contact_ID"),
                        resultSet.getString("Contact_Name"),
                        resultSet.getString("Email")
                );

                return newContact;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves a contact from the database based on the given contact name.
     *
     * @param contactName the name of the contact to retrieve.
     * @return Contact object representing the retrieved contact, or null if not found.
     * @throws SQLException if a database access error occurs.
     */
    public static Contact getContactId(String contactName) throws SQLException {
        String queryStatement = "SELECT * FROM contacts WHERE Contact_Name=?;";

        Helper.setPreparedStatement(JDBC.getConnection(), queryStatement);
        PreparedStatement preparedStatement = Helper.getPreparedStatement();

        preparedStatement.setString(1, contactName);

        try {
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();

            if (resultSet.next()) {
                Contact newContact = new Contact(
                        resultSet.getInt("Contact_ID"),
                        resultSet.getString("Contact_Name"),
                        resultSet.getString("Email")
                );

                return newContact;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }
}
