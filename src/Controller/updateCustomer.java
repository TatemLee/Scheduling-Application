package Controller;

import Main.Customer;
import Main.Message;
import Utilities.DBQuery;
import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class updateCustomer implements Initializable {
    public TextField nameTextBox;
    public TextField phoneNumberTextBox;
    public TextField addressTextBox;
    public TextField postalCodeTextBox;
    public ComboBox countryComboBox;
    public ComboBox firstLevelComboBox;
    public TextField customerIDTextBox;

    /**
     * This method initializes the starting values for the Update Customer screen
     * @param url url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setDefaults();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     * This method sets the default values for the Customer screen
     * @throws SQLException
     */
    public void setDefaults() throws SQLException {
        this.customerIDTextBox.setText(String.valueOf(Customer.staticCustomerID));
        this.nameTextBox.setText(Customer.staticName);
        this.addressTextBox.setText(Customer.staticAddress);
        this.phoneNumberTextBox.setText(Customer.staticPhoneNumber);
        this.postalCodeTextBox.setText(Customer.staticPostalCode);
        this.countryComboBox.setItems(DBQuery.populateCountryComboBox());
        countryComboBox.setValue(Customer.staticCountry);
        firstLevelComboBox.setItems(DBQuery.populateFirstLevelComboBox( DBQuery.identifyCountryID(countryComboBox.getValue().toString())));
        firstLevelComboBox.setValue(Customer.staticFirstLevel);

    }

    /**
     * This method enables the First Level combo box once a selection has been made in the Country combo box
     * @param actionEvent actionEvent
     * @throws SQLException
     */
    public void onCountrySelection(ActionEvent actionEvent) throws SQLException {
        firstLevelComboBox.setItems(DBQuery.populateFirstLevelComboBox( DBQuery.identifyCountryID(countryComboBox.getValue().toString())));
    }

    /**
     * This method saves user input and takes them back to the Customers screen
     * @param actionEvent actionEvent
     * @throws SQLException
     * @throws IOException
     */
    public void onClickConfirm(ActionEvent actionEvent) throws SQLException, IOException {
        //try/catch in case of null value or overloaded text field
        try {
            Customer.staticName = nameTextBox.getText();
            Customer.staticAddress = addressTextBox.getText();
            Customer.staticPhoneNumber = phoneNumberTextBox.getText();
            Customer.staticPostalCode = postalCodeTextBox.getText();
            Customer.staticCountry = countryComboBox.getValue().toString();
            Customer.staticFirstLevel = firstLevelComboBox.getValue().toString();

            DBQuery.updateCustomerByID(Customer.staticCustomerID, DBQuery.identifyDivisionID(Customer.staticFirstLevel));
            System.out.println("Customer with ID " + Customer.staticCustomerID + " has been updated");
        }
        catch (NullPointerException e)
        {
            Message.incompleteFields();
            return;
        }
        catch (MysqlDataTruncation m)
        {
            Message.overloadedField();
            return;
        }

        Parent root = (Parent) FXMLLoader.load(this.getClass().getResource("/view/customerScreen.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 640.0, 469.0);
        stage.setTitle("Customer Records");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method takes the user back to the Customer screen
     * @param actionEvent actionEvent
     * @throws IOException
     */
    public void onClickCancel(ActionEvent actionEvent) throws IOException {
        if(!Message.lossOfProgressWarning()) {
            return;
        }

        Parent root = (Parent) FXMLLoader.load(this.getClass().getResource("/view/customerScreen.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 640.0, 469.0);
        stage.setTitle("Customer Records");
        stage.setScene(scene);
        stage.show();
    }


}
