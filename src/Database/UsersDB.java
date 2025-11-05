package Database;

import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UsersDB {

    /**
     * Checks if the provided username and password match a user in the database.
     *
     * @param username the username to check.
     * @param password the password to check.
     * @return true if the username and password match a user in the database, false otherwise.
     * @throws SQLException if an error occurs while accessing the database.
     */
    public static boolean checkUsernamePassword(String username, String password) throws SQLException {
        String searchStatement = "SELECT * FROM users WHERE User_Name=? AND Password=?";

        Connection connection = JDBC.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(searchStatement);

        try {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            boolean result = preparedStatement.executeQuery().next();
            return result;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves a list of all users from the database.
     *
     * @return the list of users, or null if an error occurs.
     * @throws SQLException if an error occurs while accessing the database.
     */
    public static ObservableList<User> getUsers() throws SQLException {
        ObservableList<User> users = FXCollections.observableArrayList();

        String searchStatement = "SELECT * FROM users";

        Connection connection = JDBC.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(searchStatement);

        try {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User newUser = new User(
                        resultSet.getInt("User_ID"),
                        resultSet.getString("User_Name"),
                        resultSet.getString("Password")
                );

                users.add(newUser);
            }

            return users;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}