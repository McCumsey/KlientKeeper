package Controller;

import Database.AppointmentsDB;
import Database.CustomersDB;
import Model.Customer;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/** Controller for Viewing and Manipulating Customers
 */
public class CustomersController implements Initializable {

    @FXML
    private TableColumn<?, ?> NameColumn;

    @FXML
    private Button HomeButton;

    @FXML
    private TableView<Customer> CustomersTable;

    @FXML
    private TableColumn<?, ?> DivisionColumn;

    @FXML
    private Button CreateCustomerButton;

    /** List of Customers
     */
    static ObservableList<Customer> customers;
    @FXML
    private TableColumn<?, ?> CountryColumn;

    @FXML
    private TableColumn<?, ?> PhoneColumn;

    @FXML
    private Button DeleteCustomerButton;

    @FXML
    private Label Header;

    @FXML
    private TableColumn<?, ?> PostalCodeColumn;


    @FXML
    private TableColumn<?, ?> AddressColumn;

    @FXML
    private Button UpdateCustomerButton;

    @FXML
    private TableColumn<?, ?> CustomerIDColumn;


    /** This method initializes the table in the Customers View.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            customers = CustomersDB.getCustomers();
            CustomersTable.setItems(customers);
            CustomerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            NameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            AddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
            PostalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            PhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
            DivisionColumn.setCellValueFactory(new PropertyValueFactory<>("division"));
            CountryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Navigates to Create Customer View when clicked
     */
    @FXML
    void CreateCustomer(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = FXMLLoader.load(getClass().getResource("/View/AddCustomer.fxml"));
            stage.setScene(new Scene(scene));
            stage.setTitle("Create New Customer");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Load Screen Error.");
            alert.showAndWait();
        }
    }


    /** Validates customer is to be deleted
     */
    private boolean checkAppointments(Customer selectedCustomer) {
        try {
            ObservableList appointments = AppointmentsDB.getAppointmentsByCustomerName(selectedCustomer.getCustomerName());
            if (appointments != null && appointments.size() < 1) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    /** Deletes customer
     */
    @FXML
    void DeleteCustomer(ActionEvent event) {

        Customer selectedCustomer = CustomersTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("You must select a customer to delete.");
            alert.showAndWait();
        } else if (CustomersTable.getSelectionModel().getSelectedItem() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will delete the selected customer. Do you wish to continue?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && (result.get() == ButtonType.OK)) {
                try {

                    boolean valid = checkAppointments(selectedCustomer);
                    if (valid) {
                        boolean deleteSuccessful = CustomersDB.deleteCustomer(CustomersTable.getSelectionModel().getSelectedItem().getCustomerId());

                        if (deleteSuccessful) {
                            customers = CustomersDB.getCustomers();
                            CustomersTable.setItems(customers);
                            CustomersTable.refresh();
                        } else {
                            alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error Dialog");
                            alert.setContentText("Could not delete Customer.");
                            alert.showAndWait();
                        }
                    } else {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Dialog");
                        alert.setContentText("Cannot delete customer with existing appointments.");
                        alert.showAndWait();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /** Switches to Modify Customer View
     */
    @FXML
    void ModifyCustomer(ActionEvent event) {

        ModifyCustomerController.receiveSelectedCustomer(CustomersTable.getSelectionModel().getSelectedItem());

        if (CustomersTable.getSelectionModel().getSelectedItem() != null) {
            try {
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent scene = FXMLLoader.load(getClass().getResource("/View/Modify.fxml"));
                stage.setScene(new Scene(scene));
                stage.setTitle("Modify Existing Customer");
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
            alert.setContentText("You must select a customer to update.");
            alert.showAndWait();
        }
    }

    /** Navigates to appointments View
     */


    @FXML
    void backButtonPressed(ActionEvent event) {
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

}
