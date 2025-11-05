package Controller;

import Database.CountryDB;
import Database.CustomersDB;
import Database.DivisionDB;
import Model.Country;
import Model.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


/**

 Represents a controller for adding a customer.
 */
public class AddCustomerController implements Initializable {

    // FXML components
    @FXML
    private Label NameLabel;
    @FXML
    private Label PhoneLabel;
    @FXML
    private Button HomeButton;
    @FXML
    private TextField PostalCodeTextField;
    @FXML
    private TextField AddressTextField;
    @FXML
    private Button CancelButton;
    @FXML
    private TextField NameTextField;
    @FXML
    private Button SaveButton;
    @FXML
    private Label Header;
    @FXML
    private Label IDLabel;
    @FXML
    private Label DivisionLabel;
    @FXML
    private ComboBox<String> CountryCombo;
    @FXML
    private Label PostalCodeLabel;
    @FXML
    private Label AddressLabel;
    @FXML
    private Label CountryLabel;
    @FXML
    private ComboBox<String> DivisionCombo;
    @FXML
    private TextField PhoneTextField;
    @FXML
    private TextField IDTextField;

    /**

     Initializes the controller.
     @param location The location used to resolve relative paths for the root object.
     @param resources The resources used to localize the root object.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDivisionCombo();
        setCountryCombo();
    }
    /**

     Handles the event when a country is selected from the country combo box.
     @param event The action event.
     */
    @FXML
    void SelectCountry(ActionEvent event) {
        ObservableList<String> divisionList = FXCollections.observableArrayList();
        ObservableList<Division> divisions = DivisionDB.getDivisionsByCountry(CountryCombo.getSelectionModel().getSelectedItem());
        if (divisions != null) {
            for (Division division: divisions) {
                divisionList.add(division.getDivision());
            }
        }
        DivisionCombo.setItems(divisionList);
    }
    /**

     Handles the event when a division is selected from the division combo box.
     @param event The action event.
     */
    @FXML
    void SelectDivision(ActionEvent event) {
    }

    /**

     Handles the event when the save button is clicked.

     @param event The action event.

     @throws SQLException if an SQL exception occurs.
     */
    @FXML
    void Save(ActionEvent event) throws SQLException {
        boolean valid = validateNotEmpty(
                NameTextField.getText(),
                AddressTextField.getText(),
                PostalCodeTextField.getText(),
                PhoneTextField.getText());

        if (valid) {
            try {
                boolean success = CustomersDB.createCustomer(
                        NameTextField.getText(),
                        AddressTextField.getText(),
                        PostalCodeTextField.getText(),
                        PhoneTextField.getText(),
                        DivisionCombo.getValue());

                if (success) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Successfully created new customer");
                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.isPresent() && (result.get() ==  ButtonType.OK)) {
                        try {
                            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                            Parent scene = FXMLLoader.load(getClass().getResource("/View/Customers.fxml"));
                            stage.setScene(new Scene(scene));
                            stage.centerOnScreen();
                            stage.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error Dialog");
                            alert.setContentText("Load Screen Error.");
                            alert.showAndWait();
                        }
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to save new customer");
                    Optional<ButtonType> result = alert.showAndWait();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**

     Validates that the required fields are not empty.
     @param name The name field value.
     @param address The address field value.
     @param postalCode The postal code field value.
     @param phone The phone number field value.
     @return true if all fields are not empty, false otherwise.
     */
    private boolean validateNotEmpty(String name, String address, String postalCode, String phone) {
        if (isEmptyField(name, "Name") ||
                isEmptyField(address, "Address") ||
                isEmptyField(postalCode, "Postal Code") ||
                isEmptyField(phone, "Phone Number") ||
                isEmptyCombo(DivisionCombo, "Division") ||
                isEmptyCombo(CountryCombo, "Country")) {
            return false;
        }
        return true;
    }
    /**

     Checks if a field is empty and shows an error alert if it is.
     @param field The field value.
     @param fieldName The field name.
     @return true if the field is empty, false otherwise.
     */
    private boolean isEmptyField(String field, String fieldName) {
        if (field.isEmpty()) {
            showErrorAlert(fieldName + " is required.");
            return true;
        }
        return false;
    }
    /**

     Checks if a combo box is empty and shows an error alert if it is.
     @param comboBox The combo box.
     @param fieldName The field name.
     @return true if the combo box is empty, false otherwise.
     */
    private boolean isEmptyCombo(ComboBox<?> comboBox, String fieldName) {
        if (comboBox.getSelectionModel().isEmpty()) {
            showErrorAlert(fieldName + " is required.");
            return true;
        }
        return false;
    }
    /**

     Shows an error alert with the given message.
     @param message The error message.
     */
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setContentText(message);
        alert.showAndWait();
    }
    /**
     * LAMBDA EXPRESSION

     Handles the event when the cancel button is pressed.
     @param event The action event.
     */
    @FXML
    void cancelButtonPressed(ActionEvent event) {
        showConfirmationDialog("Are you sure you want to navigate back to the Appointments screen?", () -> {
            navigateToAppointmentsScreen(event);
        });
    }
    /**

     Shows a confirmation dialog with the given message and executes the onConfirmation action if confirmed.

     @param message The confirmation message.

     @param onConfirmation The action to execute on confirmation.
     */
    private void showConfirmationDialog(String message, Runnable onConfirmation) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            onConfirmation.run();
        }
    }

    /**

     Navigates to the appointments screen.
     @param event The action event.
     */
    private void navigateToAppointmentsScreen(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Customers.fxml"));
            Parent scene = loader.load();
            CustomersController controller = loader.getController();
            Scene appointmentsScene = new Scene(scene);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(appointmentsScene);
            stage.centerOnScreen();
            stage.setTitle("Appointments");
        } catch (IOException e) {
            e.printStackTrace();
            showErrorDialog("Could not load the Appointments screen.");
        }
    }
    /**

     Shows an error dialog with the given message.
     @param message The error message.
     */
    private void showErrorDialog(String message) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error Dialog");
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }
    /**

     Sets the items of the division combo box.
     */
    private void setDivisionCombo() {
        try {
            ObservableList<Division> divisions = DivisionDB.getDivisions();
            if (divisions != null) {
                ObservableList<String> divisionList = divisions.stream()
                        .map(Division::getDivision)
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));

                DivisionCombo.setItems(divisionList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**

     Sets the items of the country combo box.
     */
    private void setCountryCombo() {
        ObservableList<String> countryList;
        ObservableList<Country> countries = CountryDB.getCountries();
        countryList = countries.stream()
                .map(Country::getCountry)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        CountryCombo.setItems(countryList);
    }
}






