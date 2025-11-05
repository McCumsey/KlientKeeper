package Controller;

import Database.AppointmentsDB;
import Database.ContactsDB;
import Database.CustomersDB;
import Database.UsersDB;
import Model.Appointment;
import Model.Contact;
import Model.Customer;
import Model.User;
import javafx.collections.FXCollections;
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
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/** Add Appointment Controller
 */
public class AddAppointmentController implements Initializable {

    private ZonedDateTime StartDateTimeConversion;
    private ZonedDateTime EndDateTimeConversion;

    private ZonedDateTime convertToTimeZone(LocalDateTime time, String zoneId) {
        return ZonedDateTime.of(time, ZoneId.of(zoneId));
    }

    private static Appointment selectedAppointment;

    @FXML
    private Label EndDateLabel;
    @FXML
    private DatePicker StartDateDatePicker;


    @FXML
    private Button HomeButton;

    @FXML
    private Button CancelButton;


    @FXML
    private Button SaveButton;

    @FXML
    private TextField DescriptionTextField;

    @FXML
    private Label StartTimeLabel;

    @FXML
    private TextField AppointmentIDTextField;

    @FXML
    private Label EndTimeLabel;

    @FXML
    private Label DescriptionLabel;

    @FXML
    private Label AppointmentIDLabel;

    @FXML
    private Label TitleLabel;

    @FXML
    private ComboBox<String> StartTimeCombo;

    @FXML
    private TextField TitleTextField;

    @FXML
    private Label TypeLabel;

    @FXML
    private Label LocationLabel;

    @FXML
    private DatePicker EndDateDatePicker;

    @FXML
    private ComboBox<String> EndTimeCombo;

    @FXML
    private TextField LocationTextField;

    @FXML
    private Label Header;

    @FXML
    private Label ContactLabel;

    @FXML
    private Label StartDateLabel;

    @FXML
    private Label CustomerNameLabel;

    @FXML
    private ComboBox<String> ContactCombo;

    @FXML
    private Label UserIDLabel;

    @FXML
    private ComboBox<String> CustomerNameCombo;
    @FXML
    private ComboBox<Integer> UserIDCombo;

    @FXML
    private ComboBox<String> TypeCombo;

    /** initialize combo boxes.
     * */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateTimeComboBoxes();
        populateContactComboBox();
        populateCustomerNameComboBox();
        populateUserIDComboBox();
        populateTypeComboBox();

    }

    /** Converts time to EST.
     */
    private static ZonedDateTime convertToEST(LocalDateTime time) {
        return ZonedDateTime.of(time,ZoneId.of("America/New_York"));
    }

    /** Creates Appointment
     */

    @FXML
    void Save(ActionEvent event) {
        boolean valid = validateAppointment(
                TitleTextField.getText(),
                DescriptionTextField.getText(),
                LocationTextField.getText(),
                AppointmentIDTextField.getText()
        );

        if (valid) {
            boolean success = saveNewAppointment();

            if (success) {
                showSuccessAlert("Successfully created new appointment", event);
            } else {
                showErrorAlert("Failed to save new appointment");
            }
        }
    }

    /**
     * Attempts to save a new appointment.
     *
     * @return {@code true} if the appointment is successfully saved, {@code false} otherwise.
     */
    private boolean saveNewAppointment() {
        try {
            return AppointmentsDB.createAppointment(
                    ContactCombo.getSelectionModel().getSelectedItem(),
                    TitleTextField.getText(),
                    DescriptionTextField.getText(),
                    LocationTextField.getText(),
                    TypeCombo.getSelectionModel().getSelectedItem(),
                    LocalDateTime.of(StartDateDatePicker.getValue(), LocalTime.parse(StartTimeCombo.getSelectionModel().getSelectedItem())),
                    LocalDateTime.of(EndDateDatePicker.getValue(), LocalTime.parse(EndTimeCombo.getSelectionModel().getSelectedItem())),
                    CustomerNameCombo.getSelectionModel().getSelectedItem(),
                    UserIDCombo.getSelectionModel().getSelectedItem()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Shows a success alert message and performs additional actions if the OK button is clicked.
     *
     * @param message The message to display in the success alert.
     * @param event   The ActionEvent that triggered the method.
     */
    private void showSuccessAlert(String message, ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && (result.get() == ButtonType.OK)) {
            try {
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent scene = FXMLLoader.load(getClass().getResource("/View/Appointments.fxml"));
                stage.setScene(new Scene(scene));
                Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
                stage.setX((primaryScreenBounds.getWidth() - stage.getWidth()) / 2);
                stage.setY((primaryScreenBounds.getHeight() - stage.getHeight()) / 2);

                stage.setTitle("Main");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
                showErrorAlert("Load Screen Error.");
            }
        }
    }

    /**
     * Shows an error alert message.
     *
     * @param message The message to display in the error alert.
     */
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.setTitle("Error Dialog");
        alert.showAndWait();
    }


    /**
     * Validates the appointment details to ensure all required fields are filled and the appointment is valid.
     *
     * @param title        the title of the appointment
     * @param description  the description of the appointment
     * @param location     the location of the appointment
     * @param appointmentId the ID of the appointment
     */

    private boolean validateAppointment(String title, String description, String location, String appointmentId) {
        if (ContactCombo.getSelectionModel().isEmpty()
                || title.isEmpty()
                || description.isEmpty()
                || location.isEmpty()
                || appointmentId.isEmpty()
                || TypeCombo.getSelectionModel().isEmpty()
                || StartDateDatePicker.getValue() == null
                || StartTimeCombo.getSelectionModel().isEmpty()
                || EndDateDatePicker.getValue() == null
                || EndTimeCombo.getSelectionModel().isEmpty()
                || CustomerNameCombo.getSelectionModel().isEmpty()
                || UserIDCombo.getSelectionModel().isEmpty()) {
            showErrorAlert("All fields must be filled.");
            return false;
        }

        LocalTime startTime = LocalTime.parse(StartTimeCombo.getSelectionModel().getSelectedItem());
        LocalTime endTime = LocalTime.parse(EndTimeCombo.getSelectionModel().getSelectedItem());

        if (endTime.isBefore(startTime)) {
            showErrorAlert("End time cannot come before Start time.");
            return false;
        }

        LocalDate startDate = StartDateDatePicker.getValue();
        LocalDate endDate = EndDateDatePicker.getValue();

        if (!startDate.equals(endDate)) {
            showErrorAlert("An appointment must start and end on the same day.");
            return false;
        }

        LocalDateTime selectedStart = startDate.atTime(startTime);
        LocalDateTime selectedEnd = endDate.atTime(endTime);

        ZonedDateTime startOne = convertToEST(LocalDateTime.of(startDate, startTime));
        ZonedDateTime endOne = convertToEST(LocalDateTime.of(endDate, endTime));

        try {
            if (!checkCustomerOverlap(startOne, endOne, CustomerNameCombo.getSelectionModel().getSelectedItem())) {
                showErrorAlert("Overlapping appointments cannot be created.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (startOne.toLocalTime().isAfter(LocalTime.of(22, 0))
                || endOne.toLocalTime().isAfter(LocalTime.of(22, 0))
                || startOne.toLocalTime().isBefore(LocalTime.of(8, 0))
                || endOne.toLocalTime().isBefore(LocalTime.of(8, 0))) {
            return false;
        }

        return true;
    }

    /**
     * Checks if there is any overlap between the specified time interval and existing appointments for a given customer.
     *
     * @param startOne     the start time of the interval to check
     * @param endOne       the end time of the interval to check
     * @param customerName the name of the customer to check for overlapping appointments
     * @throws SQLException if a database access error occurs
     */
    public static boolean checkCustomerOverlap(ZonedDateTime startOne, ZonedDateTime endOne, String customerName) throws SQLException {
        ObservableList<Appointment> appointments = AppointmentsDB.getAppointmentsByCustomerName(customerName);
        for (Appointment appointment : appointments) {
            ZonedDateTime startTwo = convertToEST(appointment.getStart());
            ZonedDateTime endTwo = convertToEST(appointment.getEnd());

            if ((startOne.isBefore(startTwo) && endOne.isAfter(startTwo))
                    || (startOne.isAfter(startTwo) && startOne.isBefore(endTwo))
                    || (endOne.isAfter(startTwo) && endOne.isBefore(endTwo))
                    || (startOne.isEqual(startTwo))) {
                return false;
            }
        }
        return true;
    }

    @FXML
    void PickStartDate(ActionEvent event) {

    }


    @FXML
    void PickEndDate(ActionEvent event) {

    }

    @FXML
    void SelectStartTime(ActionEvent event) {

    }

    @FXML
    void SelectEndTime(ActionEvent event) {

    }

    @FXML
    void SelectType(ActionEvent event) {

    }

    /** Cancels Appointment created and navigates back to Appointments View
     */

    @FXML
    void cancelButtonPressed(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to navigate back to the Appointments screen?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Appointments.fxml"));
                Parent scene = loader.load();
                AppointmentsController controller = loader.getController();
                Scene appointmentsScene = new Scene(scene);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(appointmentsScene);
                stage.centerOnScreen();
                stage.setTitle("Appointments");
            } catch (IOException e) {
                e.printStackTrace();
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error Dialog");
                errorAlert.setContentText("Could not load the Appointments screen.");
                errorAlert.showAndWait();
            }
        }
    }


    /** Populates Start Time and End Time Combo Boxes in 15 minute increments
     */
    private void populateTimeComboBoxes() {
        ObservableList<String> time = FXCollections.observableArrayList();
        LocalTime startTime = LocalTime.of(8, 0);
        LocalTime endTime = LocalTime.of(22, 0);

        time.add(startTime.toString());
        while (startTime.isBefore(endTime)) {
            startTime = startTime.plusMinutes(15);
            time.add(startTime.toString());
        }

        StartTimeCombo.setItems(time);
        EndTimeCombo.setItems(time);
    }

    /** Populates Contact Combo Box with Contacts List
     */
    private void populateContactComboBox() {
        try {
            ObservableList<Contact> contacts = ContactsDB.getContacts();
            if (contacts != null) {
                ObservableList<String> contactComboList = contacts.stream()
                        .map(Contact::getContactName)
                        .distinct()
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
                ContactCombo.setItems(contactComboList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Failed to populate Contact ComboBox");
        }
    }

    /** Populates Customer ID Combo Box with Customer ID List
     */
    private void populateCustomerNameComboBox() {
        try {
            ObservableList<Customer> customers = CustomersDB.getCustomers();
            if (customers != null) {
                ObservableList<String> customerNameComboList = customers.stream()
                        .map(Customer::getCustomerName)
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
                CustomerNameCombo.setItems(customerNameComboList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Failed to populate Customer Name ComboBox");
        }
    }


    /**
     * Populates the User ID ComboBox with data from the UsersDB.
     * Retrieves a list of users and sets it as the items for the UserIDCombo.
     * If an SQLException occurs, it prints the exception stack trace and shows an error alert.
     */
    private void populateUserIDComboBox() {
        try {
            ObservableList<User> users = UsersDB.getUsers();
            if (users != null) {
                ObservableList<Integer> userIDComboList = users.stream()
                        .map(User::getUserId)
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
                UserIDCombo.setItems(userIDComboList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Failed to populate User ID ComboBox");
        }
    }

    /**
     * Populates the Type ComboBox with predefined values.
     * Adds the predefined types to the typeList and sets it as the items for the TypeCombo.
     */
    private void populateTypeComboBox() {
        ObservableList<String> typeList = FXCollections.observableArrayList();

        typeList.addAll("Planning Session", "Performance Report", "Follow-up", "Conversion", "Open Session");

        TypeCombo.setItems(typeList);
    }



}


