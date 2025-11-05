package Database;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Helper {

    private static PreparedStatement statement;

    /**
     * Sets the PreparedStatement object with the given SQL statement and connection.
     *
     * @param connection    the database connection.
     * @param sqlStatement  the SQL statement.
     * @throws SQLException if an error occurs while preparing the statement.
     */
    public static void setPreparedStatement(Connection connection, String sqlStatement) throws SQLException {
        statement = connection.prepareStatement(sqlStatement);
    }

    /**
     * Retrieves the current PreparedStatement object.
     *
     * @return the PreparedStatement object.
     */
    public static PreparedStatement getPreparedStatement() {
        return statement;
    }
}