package Database;

import Model.Country;
import Model.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DivisionDB {

    private static Connection connection = null;

    /**
     * Retrieves all divisions from the database.
     *
     * @return an ObservableList of Division objects.
     * @throws SQLException if an error occurs while accessing the database.
     */
    public static ObservableList<Division> getDivisions() throws SQLException {
        ObservableList<Division> divisions = FXCollections.observableArrayList();

        String queryStatement = "SELECT * FROM first_level_divisions;";

        try {
            if (connection == null || connection.isClosed()) {
                connection = JDBC.getConnection();
            }

            PreparedStatement preparedStatement = connection.prepareStatement(queryStatement);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Division newDivision = new Division(
                        resultSet.getInt("Division_ID"),
                        resultSet.getString("Division"),
                        resultSet.getInt("COUNTRY_ID")
                );

                divisions.add(newDivision);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return divisions;
    }

    /**
     * Retrieves a division by its name.
     *
     * @param division the name of the division.
     * @return the Division object if found, or null if not found.
     */
    public static Division getDivisionId(String division) {
        String queryStatement = "SELECT * FROM first_level_divisions WHERE Division=?";

        try {
            if (connection == null || connection.isClosed()) {
                connection = JDBC.getConnection();
            }

            PreparedStatement preparedStatement = connection.prepareStatement(queryStatement);
            preparedStatement.setString(1, division);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Division newDivision = new Division(
                            resultSet.getInt("Division_ID"),
                            resultSet.getString("Division"),
                            resultSet.getInt("COUNTRY_ID")
                    );

                    return newDivision;
                }
            }

            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return null;
    }

    /**
     * Retrieves divisions by country.
     *
     * @param country the name of the country.
     * @return an ObservableList of Division objects.
     */
    public static ObservableList<Division> getDivisionsByCountry(String country) {
        Country newCountry = CountryDB.getCountryId(country);

        ObservableList<Division> divisions = FXCollections.observableArrayList();

        String queryStatement = "SELECT * FROM first_level_divisions WHERE COUNTRY_ID=?;";

        try {
            if (connection == null || connection.isClosed()) {
                connection = JDBC.getConnection();
            }

            PreparedStatement preparedStatement = connection.prepareStatement(queryStatement);
            preparedStatement.setInt(1, newCountry.getCountryId());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Division newDivision = new Division(
                            resultSet.getInt("Division_ID"),
                            resultSet.getString("Division"),
                            resultSet.getInt("COUNTRY_ID")
                    );

                    divisions.add(newDivision);
                }
            }

            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return divisions;
    }
}