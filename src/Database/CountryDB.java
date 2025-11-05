package Database;

import Model.Country;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The CountryDB class provides methods for accessing and manipulating country data in the database.
 */
public class CountryDB {

    private static Connection connection = null;

    /**
     * Retrieves a list of all countries from the database.
     *
     * @return An ObservableList of Country objects representing the countries.
     */
    public static ObservableList<Country> getCountries() {
        ObservableList<Country> countries = FXCollections.observableArrayList();

        String searchStatement = "SELECT * FROM countries;";

        try {
            if (connection == null || connection.isClosed()) {
                connection = JDBC.getConnection();
            }

            PreparedStatement preparedStatement = connection.prepareStatement(searchStatement);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Country newCountry = new Country(
                        resultSet.getInt("Country_ID"),
                        resultSet.getString("Country")
                );
                countries.add(newCountry);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return countries;
    }

    /**
     * Retrieves a Country object with the given country name.
     *
     * @param country The name of the country to search for.
     * @return A Country object representing the country, or null if not found.
     */
    public static Country getCountryId(String country) {
        String queryStatement = "SELECT * FROM countries WHERE Country=?";
        PreparedStatement preparedStatement = null;

        try {
            if (connection == null || connection.isClosed()) {
                connection = JDBC.getConnection();
            }

            preparedStatement = connection.prepareStatement(queryStatement);
            preparedStatement.setString(1, country);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Country newCountry = new Country(
                            resultSet.getInt("Country_ID"),
                            resultSet.getString("Country")
                    );
                    return newCountry;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing PreparedStatement: " + e.getMessage());
            }
        }
        return null;
    }
}
