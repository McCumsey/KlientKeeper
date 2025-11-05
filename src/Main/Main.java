package Main;

import Database.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class Main extends Application {

    /**
     * The entry point for the application.
     *
     * @param primaryStage The primary stage for the application.
     * @throws Exception If an error occurs during application startup.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/View/Login.fxml"));
            primaryStage.setTitle("C195 Scheduler");
            primaryStage.setScene(new Scene(root, 600, 400));
            primaryStage.centerOnScreen();
            primaryStage.show();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Load Screen Error.");
            alert.showAndWait();
        }
    }

    /**
     * The main method to launch the JavaFX application.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        JDBC.openConnection();
        launch(args);
        JDBC.closeConnection();
    }
}