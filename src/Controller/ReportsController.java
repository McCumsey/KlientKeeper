package Controller;

import Database.*;
import Model.Appointment;
import Model.Contact;
import Model.Customer;
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
import java.sql.*;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ResourceBundle;
import java.util.StringJoiner;

public class ReportsController implements Initializable {


    @FXML
    private RadioButton TypeRadioButton;

    @FXML
    private Button HomeButton;

    @FXML
    private Label Header;

    @FXML
    private TextArea reportTextArea;

    @FXML
    private ComboBox<String> ContactCombo;

    @FXML
    private ComboBox<String> CustomerCombo;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateCustomerNameComboBox();
        populateContactNameComboBox();

    }


    @FXML
    void onToggleFilterByMonthType(ActionEvent event) {
        String reportString = ReportsController.appointmentsByTypeMonth();
        reportTextArea.setText(reportString);
    }

    @FXML
    void onToggleFilterByCustomerName(ActionEvent event) throws SQLException {
        String selectedCustomerName = CustomerCombo.getSelectionModel().getSelectedItem();
        String reportString = ReportsController.getAppointmentsByCustomerName(selectedCustomerName);
        System.out.println(reportString);
        reportTextArea.setText(reportString);
    }
    @FXML
    void onToggleFilterByContactName(ActionEvent event) throws SQLException {
        String selectedContactName = ContactCombo.getSelectionModel().getSelectedItem();
        String reportString = ReportsController.getAppointmentsByContactName(selectedContactName);
        System.out.println(reportString);
        reportTextArea.setText(reportString);
    }

    public static String appointmentsByTypeMonth() {
        String sql = "SELECT Type, month(Start) AS month, count(*) AS count FROM appointments " +
                "GROUP BY Type, month(Start) ORDER BY month";

        StringJoiner reportString = new StringJoiner("\n");
        reportString.add("Total Appt BY Type per Month");

        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            int monthCounter = 0;

            while (rs.next()) {
                if (rs.getInt("month") > monthCounter) {
                    reportString.add("\n" + Month.of(rs.getInt("month")));
                    monthCounter = rs.getInt("month");
                }
                reportString.add(rs.getString("Type") + " - Total: " + rs.getInt("count"));
            }
        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }

        return reportString.toString();
    }

    public static String getAppointmentsByContactName(String selectedContactName) throws SQLException {
        String sql = "SELECT a.Appointment_ID, cu.Customer_ID, cu.Customer_Name, a.Title, a.Location, a.Type, " +
                "a.Description,  a.Start, a.End, a.User_ID, co.Contact_Name, co.Contact_ID FROM customers cu " +
                "JOIN appointments a ON a.Customer_ID = cu.Customer_ID JOIN contacts co ON a.Contact_ID = co.Contact_ID WHERE Contact_Name = ?";
        StringBuilder reportString = new StringBuilder("APPOINTMENTS FOR " + selectedContactName + "\n");
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, selectedContactName);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int appointmentID = rs.getInt("Appointment_ID");
                int custID = rs.getInt("Customer_ID");
                String cusName = rs.getString("Customer_Name");
                String title = rs.getString("Title");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                String description = rs.getString("Description");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int userID = rs.getInt("User_ID");
                String conName = rs.getString("Contact_Name");

                appointments.add(new Appointment(appointmentID, custID, cusName, title, location, type,
                        description, start, end, conName, userID));
            }

            for (Appointment appointment : appointments) {
                reportString.append(appointment);
                reportString.append("\n");
            }

        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return reportString.toString();

    }

    public static String getAppointmentsByCustomerName(String selectedCustomerName) throws SQLException {
        String sql = "SELECT a.Appointment_ID, cu.Customer_ID, cu.Customer_Name, a.Title, a.Location, a.Type, " +
                "a.Description,  a.Start, a.End, a.User_ID, co.Contact_Name, co.Contact_ID FROM customers cu " +
                "JOIN appointments a ON a.Customer_ID = cu.Customer_ID JOIN contacts co ON a.Contact_ID = co.Contact_ID WHERE Customer_Name = ?";
        StringBuilder reportString = new StringBuilder("APPOINTMENTS FOR " + selectedCustomerName + "\n");
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, selectedCustomerName);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int appointmentID = rs.getInt("Appointment_ID");
                int custID = rs.getInt("Customer_ID");
                String cusName = rs.getString("Customer_Name");
                String title = rs.getString("Title");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                String description = rs.getString("Description");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int userID = rs.getInt("User_ID");
                String conName = rs.getString("Contact_Name");

                appointments.add(new Appointment(appointmentID, custID, cusName, title, location, type,
                        description, start, end, conName, userID));
            }

            for (Appointment appointment : appointments) {
                reportString.append(appointment);
                reportString.append("\n");
            }

        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return reportString.toString();

    }
    @FXML
    void Main (ActionEvent event){
        try {
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent scene = FXMLLoader.load(getClass().getResource("/View/Appointments.fxml"));
                stage.setScene(new Scene(scene));
                stage.centerOnScreen();
                stage.setTitle("Appointments");
                stage.show();
        }catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Load Screen Error.");
                alert.showAndWait();
        }
    }


    private void populateContactNameComboBox () {
            ObservableList<String> contactNameComboList = FXCollections.observableArrayList();

            try {
                ObservableList<Contact> contacts = ContactsDB.getContacts();
                if (contacts != null) {
                    for (Contact contact : contacts) {
                        if (!contactNameComboList.contains(contact.getContactName())) {
                            contactNameComboList.add(contact.getContactName());
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            ContactCombo.setItems(contactNameComboList);
        }

        private void populateCustomerNameComboBox () {
            ObservableList<String> customerNameComboList = FXCollections.observableArrayList();

            try {
                ObservableList<Customer> customers = CustomersDB.getCustomers();
                if (customers != null) {
                    for (Customer customer : customers) {
                        customerNameComboList.add(customer.getCustomerName());
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            CustomerCombo.setItems(customerNameComboList);
    }


}