package Controller;

import Main.Message;
import Utilities.DBQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Reports implements Initializable {

    public Label appointmentTotal;
    public ComboBox monthComboBox;
    public ComboBox typeComboBox;
    public ComboBox countryComboBox;
    public Label customerTotal;
    public ComboBox regionComboBox;
    public ComboBox contactComboBox;
    public ComboBox contactNameComboBox;
    public TableView appointmentTable;
    public TableColumn appointmentIDCol;
    public TableColumn titleCol;
    public TableColumn descriptionCol;
    public TableColumn locationCol;
    public TableColumn contactCol;
    public TableColumn typeCol;
    public TableColumn endDateTimeCol;
    public TableColumn customerIDCol;
    public TableColumn userIDCol;
    public TableColumn startDateTimeCol;

    /**
     * This method initializes the starting values for the Reports screen
     * @param url url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try
        {
            typeComboBox.setItems(DBQuery.populateTypeComboBox());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        monthComboBox.setItems(populateMonthComboBox());
        try
        {
            countryComboBox.setItems(DBQuery.populateCountryComboBox());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try
        {
            contactNameComboBox.setItems(DBQuery.populateContactNames());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try
        {
            contactComboBox.setItems(DBQuery.populateContactComboBox());
        } catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
      
        this.appointmentIDCol.setCellValueFactory(new PropertyValueFactory("appointmentID"));
        this.titleCol.setCellValueFactory(new PropertyValueFactory("title"));
        this.descriptionCol.setCellValueFactory(new PropertyValueFactory("description"));
        this.locationCol.setCellValueFactory(new PropertyValueFactory("location"));
        this.contactCol.setCellValueFactory(new PropertyValueFactory("contactID"));
        this.typeCol.setCellValueFactory(new PropertyValueFactory("type"));
        this.startDateTimeCol.setCellValueFactory(new PropertyValueFactory("start"));
        this.endDateTimeCol.setCellValueFactory(new PropertyValueFactory("end"));
        this.customerIDCol.setCellValueFactory(new PropertyValueFactory("customerID"));
        this.userIDCol.setCellValueFactory(new PropertyValueFactory("userID"));
        }

    /**
     * This method populates the values for the Month combo box
      * @return list of months in the year
     */
    public static ObservableList<String> populateMonthComboBox()  {
        ObservableList<String> monthsOfYear = FXCollections.observableArrayList();
        monthsOfYear.add("January");
        monthsOfYear.add("February");
        monthsOfYear.add("March");
        monthsOfYear.add("April");
        monthsOfYear.add("May");
        monthsOfYear.add("June");
        monthsOfYear.add("July");
        monthsOfYear.add("August");
        monthsOfYear.add("September");
        monthsOfYear.add("October");
        monthsOfYear.add("November");
        monthsOfYear.add("December");
        return monthsOfYear;
    }

    /**
     * This method translates an input Month name string to an int. The output is used for comparing monthy values
     * @param stringMonth Month name
     * @return Month as int value
     */
    public static int monthNameToInt(String stringMonth) {
        int outputMonth = 0;
        switch (stringMonth) {
            case "January":
                outputMonth = 1;
                break;
            case "February":
                outputMonth = 2;
                break;
            case "March":
                outputMonth = 3;
                break;
            case "April":
                outputMonth = 4;
                break;
            case "May":
                outputMonth = 5;
                break;
            case "June":
                outputMonth = 6;
                break;
            case "July":
                outputMonth = 7;
                break;
            case "August":
                outputMonth = 8;
                break;
            case "September":
                outputMonth = 9;
                break;
            case "October":
                outputMonth = 10;
                break;
            case "November":
                outputMonth = 11;
                break;
            case "December":
                outputMonth = 12;
                break;
            default:
                outputMonth = 12;
                break;
        }
        return outputMonth;
    }

    /**
     * This method loads the Customers screen
     * @param actionEvent actionEvent
     * @throws IOException
     */
    public void onClickCustomers(ActionEvent actionEvent) throws IOException {
        Parent root = (Parent) FXMLLoader.load(this.getClass().getResource("/view/customerScreen.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 640.0, 469.0);
        stage.setTitle("Customer Records");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method takes the user to the Appointments screen
     * @param actionEvent actionEvent
     * @throws IOException
     */
    public void onClickAppointments(ActionEvent actionEvent) throws IOException {
        Parent root = (Parent)FXMLLoader.load(this.getClass().getResource("/view/appointmentScreen.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 950.0D, 403.0D);
        stage.setTitle("Appointments");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method takes the user back to the Login screen
     * @param actionEvent actionEvent
     * @throws IOException
     */
    public void onClickLogout(ActionEvent actionEvent) throws IOException {
        if(!Message.logoutWarning()) {
            return;
        }

        Parent root = (Parent) FXMLLoader.load(this.getClass().getResource("/view/Login.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 300.0, 275.0);
        stage.setTitle("Welcome");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method generates the value for the Appointment Total label
     * @param actionEvent actionEvent
     * @throws SQLException
     */
    public void onSelectType(ActionEvent actionEvent) throws SQLException {
        try
        {
            appointmentTotal.setText(String.valueOf(DBQuery.countAppointmentsByTypeMonth(typeComboBox.getValue().toString(), monthNameToInt(monthComboBox.getValue().toString()))));
        }
        catch (NullPointerException e)
        {
            appointmentTotal.setText(String.valueOf(DBQuery.countAppointmentsByType(typeComboBox.getValue().toString())));
        }

    }
    /**
     * This method generates the value for the Appointment Total label
     * @param actionEvent actionEvent
     * @throws SQLException
     */
    public void onSelectMonth(ActionEvent actionEvent) throws SQLException {

        //display appointment total
       try
       {
           appointmentTotal.setText(String.valueOf(DBQuery.countAppointmentsByTypeMonth(typeComboBox.getValue().toString(), monthNameToInt(monthComboBox.getValue().toString()))));
       }
       catch (NullPointerException e)
       {
           appointmentTotal.setText(String.valueOf(DBQuery.countAppointmentsByMonth(monthNameToInt(monthComboBox.getValue().toString()))));
       }
    }

    /**
     * This method generates the values for the Region combo box
     * @param actionEvent actionEvent
     * @throws SQLException
     */
    public void onSelectCountry(ActionEvent actionEvent) throws SQLException {
        regionComboBox.setDisable(false);
        regionComboBox.setValue("Region");
        regionComboBox.setItems(DBQuery.populateFirstLevelComboBox( DBQuery.identifyCountryID(countryComboBox.getValue().toString())));

    }

    /**
     * This method generates the value for the Customer Total label
     * @param actionEvent actionEvent
     * @throws SQLException
     */
    public void onSelectRegion(ActionEvent actionEvent) throws SQLException {
        int divisionID = DBQuery.identifyDivisionID(regionComboBox.getValue().toString());
        customerTotal.setText(String.valueOf(DBQuery.countCustomerByLocation(divisionID)));
    }

    /**
     * This method loads the values for the Appointments table
     * @param actionEvent actionEvent
     * @throws SQLException
     */
    public void onSelectContactName(ActionEvent actionEvent) throws SQLException {
        int contactIndex = contactNameComboBox.getSelectionModel().getSelectedIndex();
        contactComboBox.getSelectionModel().clearAndSelect(contactIndex);
        appointmentTable.setItems(DBQuery.populateAppointmentsByContact((Integer)contactComboBox.getValue()));

    }

    /**
     * This method takes the user to the Reports screen
     * @param actionEvent actionEvent
     * @throws IOException
     */
    public void onClickReports(ActionEvent actionEvent) throws IOException {
        Parent root = (Parent)FXMLLoader.load(this.getClass().getResource("/view/Reports.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 950.0D, 448.0D);
        stage.setTitle("Reports");
        stage.setScene(scene);
        stage.show();
    }
}
