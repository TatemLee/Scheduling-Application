package Controller;

import Main.Appointment;
import Main.Message;
import Main.TimeConversion;
import Utilities.DBQuery;
import com.sun.javafx.scene.shape.MeshHelper;
import javafx.event.ActionEvent;
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
import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

public class appointmentScreen implements Initializable {

    public TableColumn userIDCol;
    public TableColumn customerIDCol;
    public TableColumn endDateTimeCol;
    public TableColumn startDateTimeCol;
    public TableColumn typeCol;
    public TableColumn contactCol;
    public TableColumn locationCol;
    public TableColumn descriptionCol;
    public TableColumn titleCol;
    public TableColumn appointmentIDCol;
    public TableView appointmentTable;
    public ComboBox timeEndChangeComboBox;
    public ComboBox timeStartChangeComboBox;
    public Button changeAppointmentTimeButton;
    public Button deleteAppointmentButton;
    public Button updateAppointmentButton;
    public Button addAppointmentButton;
    public RadioButton allAppointmentsRadio;
    public RadioButton weekRadio;
    public Button logoutButton;
    public Button confirmButton;
    public Button cancelButton;
    public RadioButton monthRadio;
    public Label filterAppointmentsLabel;
    public ToggleGroup filterRadio;

    /**
     * This method initializes the starting values for the Appointment screen
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.appointmentTable.setItems(DBQuery.populateAppointmentTable());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        this.timeStartChangeComboBox.setItems(TimeConversion.populateTimeStampList());
        this.timeEndChangeComboBox.setItems(TimeConversion.populateTimeStampList());
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
     * This method takes the user to the Customer Screen
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
     * This method refreshes the Appointments screen
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
     * This method takes the user to the Add Appointments screen
     * @param actionEvent actionEvent
     * @throws IOException
     */
    public void onClickAddAppointment(ActionEvent actionEvent) throws IOException {
        Parent root = (Parent)FXMLLoader.load(this.getClass().getResource("/view/addAppointment.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 370.0D, 493.0D);
        stage.setTitle("Add Appointment");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method deletes an appointment as selected by the user
     * @param actionEvent actionEvent
     * @throws SQLException
     */
    public void onClickDeleteAppointment(ActionEvent actionEvent) throws SQLException {
        Appointment a = (Appointment) this.appointmentTable.getSelectionModel().getSelectedItem();
        if(a == null) {
            Message.noSelection();
            return;
        }

        if (!Message.deleteAppointmentConfirmed()) {
            return;
        }

        int deleteAppointmentByThisID = a.getAppointmentID();
        DBQuery.deleteAppointmentByID(deleteAppointmentByThisID);

        //refresh customer table
        allAppointmentsRadio.setSelected(true);
        this.appointmentTable.setItems(DBQuery.populateAppointmentTable());

        //inform user of successful delete
        Message.deleteAppointmentSuccessful(a.getAppointmentID(), a.getType());

        allAppointmentsRadio.isSelected();
    }

    /**
     * This method takes the user to the Update Appointment screen. The user must select an appointment for this method to run in full
     * @param actionEvent actionEvent
     * @throws IOException
     * @throws SQLException
     */
    public void onClickUpdateAppointment(ActionEvent actionEvent) throws IOException, SQLException {
        Appointment a = (Appointment)this.appointmentTable.getSelectionModel().getSelectedItem();
        if(a == null) {
            Message.noSelection();
            return;
        }

        int appointmentID = a.getAppointmentID();
        DBQuery.populateUpdateAppointmentForm(appointmentID);

        Parent root = (Parent)FXMLLoader.load(this.getClass().getResource("/view/updateAppointment.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 370.0D, 493.0D);
        stage.setTitle("Update Appointment");

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
     * This method adjusts the view of the Appointments screen and enables time edit options for the user
     * @param actionEvent actionEvent
     * @throws SQLException
     */
    public void onClickChangeAppointmentTime(ActionEvent actionEvent) throws SQLException {
        Appointment a = (Appointment) this.appointmentTable.getSelectionModel().getSelectedItem();
        if(a == null) {
            Message.noSelection();
            return;
        }
        Appointment.staticAppointmentID = a.getAppointmentID();
        Appointment.staticStart = a.getStart();

        //reveal combo boxes and lock page
        
        timeStartChangeComboBox.setOpacity(0.99);
        timeEndChangeComboBox.setOpacity(0.99);
        timeStartChangeComboBox.setDisable(false);
        timeEndChangeComboBox.setDisable(false);
        confirmButton.setOpacity(0.99);
        cancelButton.setOpacity(0.99);
        confirmButton.setDisable(false);
        cancelButton.setDisable(false);
        addAppointmentButton.setDisable(true);
        updateAppointmentButton.setDisable(true);
        deleteAppointmentButton.setDisable(true);
        changeAppointmentTimeButton.setDisable(true);
        allAppointmentsRadio.setDisable(true);
        weekRadio.setDisable(true);
        monthRadio.setDisable(true);
        logoutButton.setDisable(true);
        appointmentTable.setDisable(true);
        filterAppointmentsLabel.setDisable(true);





    }

    /**
     * This method confirms user input and adjusts the appointment record accordingly. This method also returns the screen to its original state
     * @param actionEvent actionEvent
     * @throws SQLException
     */
    public void onClickConfirm(ActionEvent actionEvent) throws SQLException {
        if(timeStartChangeComboBox.getValue() == null || timeEndChangeComboBox.getValue() == null) {
            Message.incompleteFields();
            return;
        }

        LocalTime startTime = (LocalTime) timeStartChangeComboBox.getValue();
        LocalTime endTime = (LocalTime) timeEndChangeComboBox.getValue();
        ZonedDateTime startTimeUTC = TimeConversion.convertToUTC(TimeConversion.createUserZonedDateTime(Appointment.staticStart.toLocalDateTime().toLocalDate(), startTime.getHour(), startTime.getMinute()));
        ZonedDateTime endTimeUTC = TimeConversion.convertToUTC(TimeConversion.createUserZonedDateTime(Appointment.staticStart.toLocalDateTime().toLocalDate(), endTime.getHour(), endTime.getMinute()));
        //convert from ZonedDateTime to Time Stamp
        Appointment.staticStart = TimeConversion.ZonedDateTimeToTimeStamp(startTimeUTC);
        Appointment.staticEnd = TimeConversion.ZonedDateTimeToTimeStamp(endTimeUTC);


        //check for correct end date and adjust accordingly
        if(Appointment.staticStart.toLocalDateTime().isAfter(Appointment.staticEnd.toLocalDateTime())) {
            Appointment.staticEnd = Timestamp.valueOf(Appointment.staticEnd.toLocalDateTime().plusDays(1));
        }
        //validate that end time is after start time
        else if(Appointment.staticStart.compareTo(Appointment.staticEnd) >= 0) {
            Message.invalidStartEndTime();
            return;
        }




        //validate that there are no overlapping appointments
        if(DBQuery.checkForConflicts(Appointment.staticAppointmentID, Appointment.staticStart, Appointment.staticEnd)) {
            Message.overLappingAppointments();
            return;
        }


        DBQuery.updateAppointmentTime(Appointment.staticAppointmentID);

        //reset page
        timeStartChangeComboBox.setOpacity(0.0);
        timeEndChangeComboBox.setOpacity(0.0);
        timeStartChangeComboBox.setDisable(true);
        timeEndChangeComboBox.setDisable(true);
        confirmButton.setOpacity(0.0);
        cancelButton.setOpacity(0.0);
        confirmButton.setDisable(true);
        cancelButton.setDisable(true);
        addAppointmentButton.setDisable(false);
        updateAppointmentButton.setDisable(false);
        deleteAppointmentButton.setDisable(false);
        changeAppointmentTimeButton.setDisable(false);
        allAppointmentsRadio.setDisable(false);
        weekRadio.setDisable(false);
        monthRadio.setDisable(false);
        logoutButton.setDisable(false);
        appointmentTable.setDisable(false);
        filterAppointmentsLabel.setDisable(false);
        appointmentTable.setItems(DBQuery.populateAppointmentTable());
        allAppointmentsRadio.isSelected();

        Message.successfulTimeChange();
    }

    /**
     * This method returns the screen to its original state without saving user input
     * @param actionEvent actionEvent
     */
    public void onClickCancel(ActionEvent actionEvent) {
        if(!Message.lossOfProgressWarning()) {
            return;
        }
        //reset page
        timeStartChangeComboBox.setOpacity(0.0);
        timeEndChangeComboBox.setOpacity(0.0);
        timeStartChangeComboBox.setDisable(true);
        timeEndChangeComboBox.setDisable(true);
        confirmButton.setOpacity(0.0);
        cancelButton.setOpacity(0.0);
        confirmButton.setDisable(true);
        cancelButton.setDisable(true);
        addAppointmentButton.setDisable(false);
        updateAppointmentButton.setDisable(false);
        deleteAppointmentButton.setDisable(false);
        changeAppointmentTimeButton.setDisable(false);
        allAppointmentsRadio.setDisable(false);
        weekRadio.setDisable(false);
        monthRadio.setDisable(false);
        logoutButton.setDisable(false);
        filterAppointmentsLabel.setDisable(false);
        appointmentTable.setDisable(false);
    }

    /**
     * This method populates the Appointment table with all appointments
     * @param actionEvent actionEvent
     * @throws SQLException
     */
    public void toggleAll(ActionEvent actionEvent) throws SQLException {
        appointmentTable.setItems(DBQuery.populateAppointmentTable());
    }

    /**
     * This method populates the Appointment table by current week
     * @param actionEvent actionEvent
     * @throws SQLException
     */
    public void toggleWeek(ActionEvent actionEvent) throws SQLException {
        appointmentTable.setItems(DBQuery.populateAppointmentByWeek());

    }
    /**
     * This method populates the Appointment table by current month
     * @param actionEvent actionEvent
     * @throws SQLException
     */
    public void toggleMonth(ActionEvent actionEvent) throws SQLException {
        appointmentTable.setItems(DBQuery.populateAppointmentByMonth());

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
