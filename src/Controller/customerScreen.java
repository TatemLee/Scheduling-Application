package Controller;

import Main.Customer;
import Main.Message;
import Main.updateInterface;
import Utilities.DBQuery;
import Utilities.JDBC;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class customerScreen implements Initializable {

    public TableColumn customerIdCol;
    public TableColumn nameCol;
    public TableColumn addressCol;
    public TableColumn divisionCol;
    public TableColumn countryCol;
    public TableColumn postalCodeCol;
    public TableColumn phoneNumberCol;
    public TableView customerTable;


    /**
     * This method initializes the starting values for the Customer screen
     * @param url url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.customerTable.setItems(DBQuery.populateCustomerTable());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        this.customerIdCol.setCellValueFactory(new PropertyValueFactory("customerID"));
        this.nameCol.setCellValueFactory(new PropertyValueFactory("name"));
        this.addressCol.setCellValueFactory(new PropertyValueFactory("address"));
        this.postalCodeCol.setCellValueFactory(new PropertyValueFactory("postalCode"));
        this.divisionCol.setCellValueFactory(new PropertyValueFactory("division"));
        this.countryCol.setCellValueFactory(new PropertyValueFactory("country"));
        this.phoneNumberCol.setCellValueFactory(new PropertyValueFactory("phoneNumber"));
    }


    /**
     * This method refreshes the Customers screen
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
     * This method takes the user to the Add Customer screen
     * @param actionEvent actionEvent
     * @throws IOException
     */
    public void onClickAddCustomer(ActionEvent actionEvent) throws IOException {
        Parent root = (Parent) FXMLLoader.load(this.getClass().getResource("/view/addCustomer.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 365.0, 316.0);
        stage.setTitle("Add New Customer");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method deletes a Customer record selected by the user
     * @param actionEvent actionEvent
     * @throws SQLException
     */
    public void onClickDeleteCustomer(ActionEvent actionEvent) throws SQLException {
        Customer c = (Customer)this.customerTable.getSelectionModel().getSelectedItem();
        if(c == null) {
            Message.noSelection();
            return;
        }

        int comparisonID = c.getCustomerID();

        //check is the Customer can legally be deleted
        if(DBQuery.checkForAssociatedAppointments(comparisonID)) {
            Message.associatedAppointment();
            return;
       }
        if(!Message.deleteCustomerConfirmed()) {
            return;
        }

        int deleteCustomerByThisID = c.getCustomerID();
        DBQuery.deleteCustomerByID(deleteCustomerByThisID);

        //refresh customer table
        this.customerTable.setItems(DBQuery.populateCustomerTable());

        //inform user of successful delete
        Message.deleteCustomerSuccessful();
    }


    //!!!!!!!!!!!!!!!!!!!!!!!!! LAMBDA EXPRESSION 1 !!!!!!!!!!!!!!!!!!!!!!!

    /**
     * This method processes a Customer record selected by the user (based on an input customer ID) and takes the user to the Update Customer screen.
     * @param actionEvent actionEvent
     * @throws IOException
     * @throws SQLException
     */
    public void onClickUpdateCustomer(ActionEvent actionEvent) throws IOException, SQLException {
        Customer c = (Customer)this.customerTable.getSelectionModel().getSelectedItem();
        if(c == null) {
            Message.noSelection();
            return;
        }

        int customerID = c.getCustomerID();

        //lambda expression
        updateInterface update = cID-> {
            Connection conn = JDBC.getConnection();
            String selectStatement = "SELECT \n" +
                    "c.Customer_ID, \n" +
                    "c.Customer_Name, \n" +
                    "c.Address, \n" +
                    "c.Postal_Code, \n" +
                    "f.Division,\n" +
                    "o.Country, \n" +
                    "c.Phone\n" +
                    "FROM client_schedule.customers c\n" +
                    "JOIN client_schedule.first_level_divisions f\n" +
                    "ON f.Division_ID =  c.Division_ID\n" +
                    "JOIN client_schedule.countries o\n" +
                    "ON o.Country_ID = f.Country_ID\n" +
                    "WHERE c.Customer_ID = " + cID +
                    ";";
            DBQuery.setPreparedStatement(conn, selectStatement);
            PreparedStatement ps = DBQuery.getPreparedStatement();
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while(rs.next()) {
                Customer.staticCustomerID = Integer.parseInt(rs.getString("Customer_ID"));
                Customer.staticName = rs.getString("Customer_Name");
                Customer.staticPhoneNumber = rs.getString("Phone");
                Customer.staticAddress = rs.getString("Address");
                Customer.staticPostalCode = rs.getString("Postal_Code");
                Customer.staticCountry = rs.getString("Country");
                Customer.staticFirstLevel = rs.getString("Division");
            }
        };

        //execute lambda expression
        update.populateCustomerUpdateTable(customerID);


        Parent root = (Parent) FXMLLoader.load(this.getClass().getResource("/view/updateCustomer.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 365.0, 350.0);
        stage.setTitle("Add New Customer");
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
     * This method takes the user to the Reports screen
     * @param actionEvent
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
