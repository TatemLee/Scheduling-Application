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

public class addCustomer implements Initializable {
    public ComboBox firstLevelComboBox;
    public ComboBox countryComboBox;
    public TextField addressTextBox;
    public TextField phoneNumberTextBox;
    public TextField nameTextBox;
    public TextField postalCodeTextBox;

    /**
     * This method initializes starting values for the Add Customer's page
     * @param url url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.countryComboBox.setItems(DBQuery.populateCountryComboBox());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * This method takes the user back to the Customers screen
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

    /**
     * This method saves the user's input and takes them back to the Customers screen
     * @param actionEvent
     * @throws SQLException
     * @throws IOException
     */
    public void onClickConfirm(ActionEvent actionEvent) throws SQLException, IOException {
        //try/catch in case of null value or overloaded text field
        try {
            String name = nameTextBox.getText();
            String phoneNumber = phoneNumberTextBox.getText();
            String postalCode = postalCodeTextBox.getText();
            String address = addressTextBox.getText();
            String country = countryComboBox.getValue().toString();
            String firstLevel = firstLevelComboBox.getValue().toString();
            int firstLevelInt = DBQuery.identifyDivisionID(firstLevel);
            //load customer object with values
            Customer c = new Customer();
            DBQuery.insertCustomer(c.createNewCustomer(1, name, address, postalCode, firstLevel, country, phoneNumber), firstLevelInt);
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
     * This method enables the First Level combo box when a selection has been made in the country combo box
     * @param actionEvent actionEvent
     * @throws SQLException
     */
    public void onCountrySelection(ActionEvent actionEvent) throws SQLException {
        firstLevelComboBox.setDisable(false);
        firstLevelComboBox.setItems(DBQuery.populateFirstLevelComboBox( DBQuery.identifyCountryID(countryComboBox.getValue().toString())));
    }
}
