package Controller;

import Database.AppointmentsDB;
import Database.UsersDB;
import Model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import Helper.Logging;


public class LoginController implements Initializable {


    private ResourceBundle resourceBundle;

    @FXML
    private Label Description;

    @FXML
    private Label Header;

    @FXML
    private Label Title;

    @FXML
    private Button CancelButton;

    @FXML
    private Label LocationField;

    @FXML
    private TextField UsernameTextField;

    @FXML
    private TextField PasswordTextField;

    @FXML
    private Label UsernameLabel;

    @FXML
    private Label TimeZoneField;

    @FXML
    private Label TimeZoneLabel;

    @FXML
    private Label LocationLabel;

    @FXML
    private Label PasswordLabel;

    @FXML
    private Button LoginButton;


    /** This method initializes the Login View
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resourceBundle = ResourceBundle.getBundle("Language/language", Locale.getDefault());

        if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")){
            /**Header.setText(resourceBundle.getString("header"));*/
            Title.setText(resourceBundle.getString("title"));
            UsernameLabel.setText(resourceBundle.getString("username"));
            PasswordLabel.setText(resourceBundle.getString("password"));
            LocationLabel.setText(resourceBundle.getString("location"));
            LocationField.setText(resourceBundle.getString("country"));
            TimeZoneLabel.setText(resourceBundle.getString("timezone"));
            TimeZoneField.setText(String.valueOf(ZoneId.of(TimeZone.getDefault().getID())));
            LoginButton.setText(resourceBundle.getString("login"));
            CancelButton.setText(resourceBundle.getString("cancel"));
        }
    }


    /** Allows the user to login
     */


    @FXML
    void Login(ActionEvent event) {


        validateUsernameNotEmpty(UsernameTextField.getText());
        validatePasswordNotEmpty(PasswordTextField.getText());

        String uname = UsernameTextField.getText();
        String password = PasswordTextField.getText();



        try {
            boolean valid = UsersDB.checkUsernamePassword(UsernameTextField.getText(), PasswordTextField.getText());
            if (valid) {
                appointmentInFifteen();
                Logging.logLogIn(true, uname);



                try {
                    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    Parent scene = FXMLLoader.load(getClass().getResource("/View/Appointments.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.setTitle("Main");
                    stage.centerOnScreen();
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();



                    if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle(resourceBundle.getString("errorDialog"));
                        alert.setContentText(resourceBundle.getString("loadScreenError"));
                        alert.showAndWait();
                    }
                }
            } else {

                if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {
                    Logging.logLogIn(false, uname);
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle(resourceBundle.getString("errorDialog"));
                    alert.setContentText(resourceBundle.getString("incorrectUsernamePassword"));
                    alert.showAndWait();
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    /** Validates Username not empty
     */

    private void validateUsernameNotEmpty(String username) {
        if (username.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(resourceBundle.getString("errorDialog"));
            alert.setContentText(resourceBundle.getString("usernameRequired"));
            alert.showAndWait();
        }
    }

    /** Validates Password not empty
     */
    private void validatePasswordNotEmpty(String password){
        if (password.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(resourceBundle.getString("errorDialog"));
                alert.setContentText(resourceBundle.getString("passwordRequired"));
                alert.showAndWait();

        }
    }

    /** Alerts the user if there is an upcoming appointment in 15 minutes or otherwise
     */

    private void appointmentInFifteen() {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime localDateTimePlus15 = localDateTime.plusMinutes(15);

        try {
            ObservableList<Appointment> appointments = AppointmentsDB.getAppointments();
            ObservableList<Appointment> upcomingAppointments = findUpcomingAppointments(appointments, localDateTime, localDateTimePlus15);

            if (upcomingAppointments.isEmpty()) {
                showNoUpcomingAppointmentsAlert();
            } else {
                showUpcomingAppointmentsAlerts(upcomingAppointments);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ObservableList<Appointment> findUpcomingAppointments(ObservableList<Appointment> appointments, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        ObservableList<Appointment> upcomingAppointments = FXCollections.observableArrayList();

        if (appointments != null) {
            for (Appointment appointment : appointments) {
                if (appointment.getStart().isAfter(startDateTime) && appointment.getStart().isBefore(endDateTime)) {
                    upcomingAppointments.add(appointment);
                }
            }
        }

        return upcomingAppointments;
    }

    private void showUpcomingAppointmentsAlerts(ObservableList<Appointment> upcomingAppointments) {
        if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {
            for (Appointment appointment : upcomingAppointments) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(resourceBundle.getString("appointmentAlert"));
                alert.setContentText(
                        resourceBundle.getString("lessThanFifteenMin") +
                                "\n" +
                                resourceBundle.getString("appointmentId") +
                                " " +
                                appointment.getAppointmentId() +
                                "\n" +
                                resourceBundle.getString("date") +
                                " " +
                                appointment.getStart() +
                                "\n" +
                                resourceBundle.getString("time") +
                                " " +
                                appointment.getStart().toLocalTime());
                alert.setResizable(true);
                alert.showAndWait();
            }
        }
    }

    private void showNoUpcomingAppointmentsAlert() {
        if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(resourceBundle.getString("appointmentAlert"));
            alert.setContentText(resourceBundle.getString("noUpcomingAppointments"));
            alert.setResizable(true);
            alert.showAndWait();
        }
    }



    /** Prompts the user to close the application when clicked
     */
    @FXML
    void Cancel(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText(resourceBundle.getString("cancelError"));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }



}
