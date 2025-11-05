package Controller;

import Database.CountryDB;
import Database.CustomersDB;
import Database.DivisionDB;
import Model.Country;
import Model.Customer;
import Model.Division;
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
import java.util.Optional;
import java.util.ResourceBundle;

/** Controller to Update Customer
 */
public class ModifyCustomerController implements Initializable {

    /** Customer selected from Customer Controller View
     */
    private static Customer selectedCustomer;

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

    /** Initializes combo boxes and sets text fields.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDivisionCombo();
        setCountryCombo();

        IDTextField.setText(Integer.toString(selectedCustomer.getCustomerId()));
        NameTextField.setText(selectedCustomer.getCustomerName());
        PostalCodeTextField.setText(selectedCustomer.getPostalCode());
        AddressTextField.setText(selectedCustomer.getAddress());
        PhoneTextField.setText(selectedCustomer.getPhoneNumber());
        CountryCombo.getSelectionModel().select(selectedCustomer.getCountry());
        DivisionCombo.getSelectionModel().select(selectedCustomer.getDivision());
    }

    /** Populates Division Combo Box with List of Divisions based on selected Country
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

    /** Saves modified Customer
     */
    @FXML
    void Save(ActionEvent event) {
        boolean valid = validateNotEmpty(
                NameTextField.getText(),
                AddressTextField.getText(),
                PostalCodeTextField.getText(),
                PhoneTextField.getText());

        if (valid) {
            try {
                boolean success = CustomersDB.updateCustomer(
                        Integer.parseInt(IDTextField.getText()),
                        NameTextField.getText(),
                        AddressTextField.getText(),
                        PostalCodeTextField.getText(),
                        PhoneTextField.getText(),
                        DivisionCombo.getValue());

                if (success) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Successfully updated customer");
                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.isPresent() && (result.get() ==  ButtonType.OK)) {
                        try {
                            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                            Parent scene = FXMLLoader.load(getClass().getResource("/View/Customers.fxml"));
                            stage.setScene(new Scene(scene));
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

    /** Validate Customer Fields are selected and not empty
     */

    private boolean validateNotEmpty(String name, String address, String postalCode, String phone) {
        if (isEmpty(name)) {
            showErrorAlert("Name is required.");
            return false;
        }

        if (isEmpty(address)) {
            showErrorAlert("Address is required.");
            return false;
        }

        if (isEmpty(postalCode)) {
            showErrorAlert("Postal Code is required.");
            return false;
        }

        if (isEmpty(phone)) {
            showErrorAlert("Phone Number is required.");
            return false;
        }

        if (DivisionCombo.getSelectionModel().isEmpty()) {
            showErrorAlert("Division is required.");
            return false;
        }

        if (CountryCombo.getSelectionModel().isEmpty()) {
            showErrorAlert("Country is required.");
            return false;
        }

        return true;
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setContentText(message);
        alert.showAndWait();
    }


    /** Method to receive selected customer from Customer View
     */
    public static void receiveSelectedCustomer(Customer customer) {
        selectedCustomer = customer;
    }

    /** Cancels customer input and navigates back to Customer View
     */
    @FXML
    void Cancel(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Navigate back to Customers?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && (result.get() ==  ButtonType.OK)) {
            try {
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent scene = FXMLLoader.load(getClass().getResource("/View/Customers.fxml"));
                stage.setTitle("Customers");
                stage.setScene(new Scene(scene));
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

    /** Populates Division Combo Box
     */
    private void setDivisionCombo(){
        ObservableList<String> divisionList = FXCollections.observableArrayList();

        try {
            ObservableList<Division> divisions = DivisionDB.getDivisions();;
            if (divisions != null) {
                for (Division division: divisions) {
                    divisionList.add(division.getDivision());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DivisionCombo.setItems(divisionList);
    }


    /** Populates Country Combo Box
     */
    private void setCountryCombo(){
        ObservableList<String> countryList = FXCollections.observableArrayList();

        ObservableList<Country> countries = CountryDB.getCountries();;
        if (countries != null) {
            for (Country country: countries) {
                countryList.add(country.getCountry());
            }
        }

        CountryCombo.setItems(countryList);
    }

}
