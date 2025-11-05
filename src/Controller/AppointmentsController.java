package Controller;

import Database.AppointmentsDB;
import Model.Appointment;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/** Controller for Appointments main page table
 */
public class AppointmentsController implements Initializable {

    /** List of Appointments
     */
    public static ObservableList<Appointment> appointments;


    @FXML
    private TableColumn<?, ?> ContactColumn;

    @FXML
    private Button LogoutButton;

    @FXML
    private TableColumn<?, ?> DescriptionColumn;

    @FXML
    private Button DeleteAppointmentButton;

    @FXML
    private TableColumn<?, ?> LocationColumn;

    @FXML
    private TableView<Appointment> AppointmentsTable;

    @FXML
    private TableColumn<?, ?> TitleColumn;

    @FXML
    private RadioButton MonthRadioButton;

    @FXML
    private TableColumn<?, ?> AppointmentIDColumn;

    @FXML
    private Button CreateAppointmentButton;

    @FXML
    private RadioButton AllRadioButton;

    @FXML
    private RadioButton WeekRadioButton;

    @FXML
    private TableColumn<Appointment, ZonedDateTime> StartDateTimeColumn;

    @FXML
    private TableColumn<Appointment, ZonedDateTime> EndDateTimeColumn;

    @FXML
    private Label Header;

    @FXML
    private Button CustomersButton;

    @FXML
    private Button ReportsButton;

    @FXML
    private Button UpdateCustomerButton;


    @FXML
    private TableColumn<?, ?> TypeColumn;

    @FXML
    private TableColumn<?, ?> CustomerNameColumn;

    @FXML
    private TableColumn<?, ?> UserIDColumn;

    @FXML
    private ToggleGroup ToggleView;




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AllRadioButton.setToggleGroup(ToggleView);
        WeekRadioButton.setToggleGroup(ToggleView);
        MonthRadioButton.setToggleGroup(ToggleView);
        try {
            appointments = AppointmentsDB.getAppointments();

            AppointmentsTable.setItems(appointments);
            AppointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
            TitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            DescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            LocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
            ContactColumn.setCellValueFactory(new PropertyValueFactory<>("contactName"));
            TypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            StartDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
            EndDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
            CustomerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            UserIDColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /** Toggle View - All, Week, or Month.
     */

    @FXML
    void ViewToggle(ActionEvent event) {
        try {
            if (AllRadioButton.isSelected()) {
                appointments = AppointmentsDB.getAppointments();
            } else if (ToggleView.getSelectedToggle().equals(MonthRadioButton)) {
                appointments = AppointmentsDB.getAppointmentsMonth();
            } else if (ToggleView.getSelectedToggle().equals(WeekRadioButton)) {
                appointments = AppointmentsDB.getAppointmentsWeek();
            }
            AppointmentsTable.setItems(appointments);
            AppointmentsTable.refresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    /** Opens Customer Window.
     */
    @FXML
    void Customers(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = FXMLLoader.load(getClass().getResource("/View/Customers.fxml"));
            stage.setScene(new Scene(scene));
            stage.setTitle("Customers");
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Load Screen Error.");
            alert.showAndWait();
        }
    }

    /** Opens Reports Window.
     */
    @FXML
    void Reports(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = FXMLLoader.load(getClass().getResource("/View/Reports.fxml"));
            stage.setScene(new Scene(scene));
            stage.setTitle("Reports");
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Load Screen Error.");
            alert.showAndWait();
        }

    }

    /** Navigates to Create Appointment View when clicked
     */
    @FXML
    void CreateAppointment(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = FXMLLoader.load(getClass().getResource("/View/AddAppointment.fxml"));
            stage.setScene(new Scene(scene));
            stage.setTitle("Add New Appointment");
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Load Screen Error.");
            alert.showAndWait();
        }
    }

    /** Navigates to Update Appointment View when clicked.
     */


    @FXML
    void ModifyAppointment(ActionEvent event) {
        ModifyAppointmentController.receiveSelectedAppointment(AppointmentsTable.getSelectionModel().getSelectedItem());

        if (AppointmentsTable.getSelectionModel().getSelectedItem() != null) {
            try {
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent scene = FXMLLoader.load(getClass().getResource("/View/ModifyAppointment.fxml"));
                stage.setScene(new Scene(scene));
                stage.setTitle("Modify Existing Appointment");
                stage.centerOnScreen();
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Load Screen Error.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("You must select an appointment to modify.");
            alert.showAndWait();
        }
    }

    /**
     * LAMBDA EXPRESSION
     *
     * Deletes selected appointment.
     */

    @FXML
    void DeleteAppointment(ActionEvent event) {
        Appointment selectedAppointment = AppointmentsTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            showErrorAlert("You must select an appointment to delete.");
        } else {
            showConfirmationAlert("This will delete the selected appointment. Do you wish to continue?")
                    .ifPresent(confirmed -> handleDeleteConfirmation(confirmed, selectedAppointment));
        }
    }

    private void handleDeleteConfirmation(boolean confirmed, Appointment selectedAppointment) {
        if (confirmed) {
            try {
                boolean deleteSuccessful = AppointmentsDB.deleteAppointment(selectedAppointment.getAppointmentId());

                if (deleteSuccessful) {
                    showInfoAlert("Successfully deleted Appointment ID: " + selectedAppointment.getAppointmentId() + " Type: " + selectedAppointment.getType());
                    refreshAppointmentsTable();
                } else {
                    showErrorAlert("Could not delete appointment.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * LAMBDA EXPRESSION
     *
     * */
    private Optional<Boolean> showConfirmationAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText(message);

        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(okButton, cancelButton);

        return alert.showAndWait().map(result -> result == okButton);
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfoAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successful Delete");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void refreshAppointmentsTable() throws SQLException {
        appointments = AppointmentsDB.getAppointments();
        AppointmentsTable.setItems(appointments);
        AppointmentsTable.refresh();
    }



    @FXML
    void Logout(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Would you like to log out or exit completely?");
        alert.setTitle("Exit?");

        ButtonType logout = new ButtonType("Log Out");
        ButtonType exit = new ButtonType("Exit Program");
        ButtonType cancel = new ButtonType("Return to Program");
        alert.getButtonTypes().setAll(logout, exit, cancel);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent()) {
            if (result.get() == logout) {
                switchScene(AppointmentsTable, "/View/Login.fxml", "Login");
            } else if (result.get() == exit) {
                Platform.exit();
            } else if (result.get() == cancel) {
                alert.close();
            }
        }
    }

    private void switchScene(Node node, String fxmlFile, String title) throws IOException {
        Stage stage = (Stage) node.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.centerOnScreen();
        stage.show();
    }

}
