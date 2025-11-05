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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

/** Controller to Update Appointment
 */
public class ModifyAppointmentController implements Initializable {

    private ZonedDateTime StartDateTimeConversion;
    private ZonedDateTime EndDateTimeConversion;

    /** Appointment selected from Appointment Controller View
     */
    public static Appointment selectedAppointment;

    @FXML
    private DatePicker StartDateDatePicker;

    @FXML
    private DatePicker EndDateDatePicker;

    @FXML
    private TextField LocationTextField;

    @FXML
    private Button HomeButton;

    @FXML
    private TextField DescriptionTextField;



    @FXML
    private Button CancelButton;

    @FXML
    private Label StartTimeLabel;

    @FXML
    private Button SaveButton;

    @FXML
    private TextField AppointmentIDTextField;

    @FXML
    private TextField CustomerNameTextField;

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
    private ComboBox<String> EndTimeCombo;

    @FXML
    private Label Header;

    @FXML
    private Label EndDateLabel;

    @FXML
    private Label ContactLabel;

    @FXML
    private Label StartDateLabel;

    @FXML
    private Label CustomerIDLabel;

    @FXML
    private Label UserIDLabel;

    @FXML
    private ComboBox<String> CustomerNameCombo;
    @FXML
    private ComboBox<String> ContactCombo;
    @FXML
    private ComboBox<Integer> UserIDCombo;

    @FXML
    private ComboBox<String> TypeCombo;

    /** Sets the fields with appointment data and initializes combo boxes.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateTimeComboBoxes();
        populateContactComboBox();
        populateCustomerNameComboBox();
        populateUserIDComboBox();
        populateTypeComboBox();

        ZonedDateTime zonedStartTime = convertToTimeZone(selectedAppointment.getStart(), String.valueOf(ZoneId.of(TimeZone.getDefault().getID())));
        ZonedDateTime zonedEndTime = convertToTimeZone(selectedAppointment.getEnd(), String.valueOf(ZoneId.of(TimeZone.getDefault().getID())));

        ContactCombo.getSelectionModel().select(selectedAppointment.getContactName());
        TitleTextField.setText(selectedAppointment.getTitle());
        DescriptionTextField.setText(selectedAppointment.getDescription());
        LocationTextField.setText(selectedAppointment.getLocation());
        TypeCombo.getSelectionModel().select(selectedAppointment.getType());
        UserIDCombo.getSelectionModel().select(Integer.valueOf(selectedAppointment.getUserId()));
        AppointmentIDTextField.setText(String.valueOf(selectedAppointment.getAppointmentId()));
        StartDateDatePicker.setValue(selectedAppointment.getStart().toLocalDate());
        StartTimeCombo.getSelectionModel().select(String.valueOf(zonedStartTime.toLocalTime()));
        EndDateDatePicker.setValue(selectedAppointment.getEnd().toLocalDate());
        EndTimeCombo.getSelectionModel().select(String.valueOf(zonedEndTime.toLocalTime()));
        CustomerNameCombo.getSelectionModel().select(selectedAppointment.getCustomerName());
    }

    /** Converts time to EST.
     * @param time LocalDateTime time to convert
     * @return ZonedDateTime Returns time converted to EST
     */
    private static ZonedDateTime convertToEST(LocalDateTime time) {
        return ZonedDateTime.of(time, ZoneId.of("America/New_York"));
    }

    private ZonedDateTime convertToTimeZone(LocalDateTime time, String zoneId) {
        return ZonedDateTime.of(time, ZoneId.of(zoneId));
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.setTitle("Error Dialog");
        alert.showAndWait();
    }

    @FXML
    void PickStartDate(ActionEvent event) {

    }

    @FXML
    void PickEndDate(ActionEvent event) {

    }

    /** Updates Appointment.
     * */

    @FXML
    void Save(ActionEvent event) {
        boolean valid = validateAppointment(
                TitleTextField.getText(),
                DescriptionTextField.getText(),
                LocationTextField.getText(),
                AppointmentIDTextField.getText()
        );

        if (valid) {
            try {
                boolean success = AppointmentsDB.modifyAppointment(
                        ContactCombo.getSelectionModel().getSelectedItem(),
                        TitleTextField.getText(),
                        DescriptionTextField.getText(),
                        LocationTextField.getText(),
                        TypeCombo.getSelectionModel().getSelectedItem(),
                        LocalDateTime.of(StartDateDatePicker.getValue(), LocalTime.parse(StartTimeCombo.getSelectionModel().getSelectedItem())),
                        LocalDateTime.of(EndDateDatePicker.getValue(), LocalTime.parse(EndTimeCombo.getSelectionModel().getSelectedItem())),
                        CustomerNameCombo.getSelectionModel().getSelectedItem(),
                        UserIDCombo.getSelectionModel().getSelectedItem(),
                        Integer.parseInt(AppointmentIDTextField.getText())
                );

                if (success) {
                    showConfirmationDialog("Successfully updated appointment");

                    navigateToAppointmentsScreen(event);
                } else {
                    showErrorDialog("Failed to update appointment");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showConfirmationDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message);
        alert.showAndWait();
    }

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.showAndWait();
    }

    private void navigateToAppointmentsScreen(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = FXMLLoader.load(getClass().getResource("/View/Appointments.fxml"));
            stage.setScene(new Scene(scene));
            stage.centerOnScreen();
            stage.setTitle("Main");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Load Screen Error.");
        }
    }


    /** validates Appointment Fields, checks for blank fields
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


   /** private boolean validateAppointment(String title, String description, String location, String appointmentId) {
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
            showErrorAlert("End time can not come before Start time.");
            return false;
        }

        LocalDate startDate = StartDateDatePicker.getValue();
        LocalDate endDate = EndDateDatePicker.getValue();

        if (!startDate.equals(endDate)) {
            showErrorAlert("An Appointment must start and end on the same day.");
            return false;
        }

        LocalDateTime selectedStart = startDate.atTime(startTime);
        LocalDateTime selectedEnd = endDate.atTime(endTime);

        LocalDateTime proposedAppointmentStart;
        LocalDateTime proposedAppointmentEnd;

        try {
            ObservableList<Appointment> appointments = AppointmentsDB.getAppointmentsByCustomerName(CustomerNameCombo.getSelectionModel().getSelectedItem());
            for (Appointment appointment : appointments) {
                proposedAppointmentStart = appointment.getStart();
                proposedAppointmentEnd = appointment.getEnd();

                if ((proposedAppointmentStart.isAfter(selectedStart) && proposedAppointmentStart.isBefore(selectedEnd))
                        || (proposedAppointmentEnd.isAfter(selectedStart) && proposedAppointmentEnd.isBefore(selectedEnd))) {
                    showErrorAlert("Can not create overlapping appointments.");
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        StartDateTimeConversion = convertToEST(LocalDateTime.of(StartDateDatePicker.getValue(), LocalTime.parse(StartTimeCombo.getSelectionModel().getSelectedItem())));
        EndDateTimeConversion = convertToEST(LocalDateTime.of(EndDateDatePicker.getValue(), LocalTime.parse(EndTimeCombo.getSelectionModel().getSelectedItem())));

        if (StartDateTimeConversion.toLocalTime().isAfter(LocalTime.of(22, 0))
                || EndDateTimeConversion.toLocalTime().isAfter(LocalTime.of(22, 0))
                || StartDateTimeConversion.toLocalTime().isBefore(LocalTime.of(8, 0))
                || EndDateTimeConversion.toLocalTime().isBefore(LocalTime.of(8, 0))) {
            showErrorAlert("Appointment must be within business hours.");
            return false;
        }

        return true;
    }*/

    /** Method to receive selected appointment from Appointment View
     */
    public static void receiveSelectedAppointment(Appointment appointment) {
        selectedAppointment = appointment;
    }

    @FXML
    void Cancel(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Navigate back to Appointments?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && (result.get() ==  ButtonType.OK)) {
            try {
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent scene = FXMLLoader.load(getClass().getResource("/View/Appointments.fxml"));
                stage.setScene(new Scene(scene));
                stage.centerOnScreen();
                stage.setTitle("Appointments");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Load Screen Error.");
                alert.showAndWait();
            }
        }
    }


    /** Populates Start Time and End Time Combo Boxes in 15 minute increments
     */
    private void populateTimeComboBoxes() {
        LocalTime startTime = LocalTime.of(8, 0);
        LocalTime endTime = LocalTime.of(20, 0);

        List<String> timeList = new ArrayList<>();
        timeList.add(startTime.toString());

        while (startTime.isBefore(endTime)) {
            startTime = startTime.plusMinutes(15);
            timeList.add(startTime.toString());
        }

        ObservableList<String> time = FXCollections.observableArrayList(timeList);

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
        }
    }


    /** Populates User ID Combo Box with User ID List
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
        }
    }


    /** Populates Type Combo Box
     */
    private void populateTypeComboBox() {
        ObservableList<String> typeList = FXCollections.observableArrayList(
                "Planning Session", "De-Briefing", "Follow-up", "Pre-Briefing", "Open Session");

        TypeCombo.setItems(typeList);
    }

}