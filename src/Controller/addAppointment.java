package Controller;

import Main.*;
import Utilities.DBQuery;
import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

public class addAppointment implements Initializable {

    public TextField userIdTextBox;
    public TextField typeTextBox;
    public ComboBox customerIdComboBox;
    public ComboBox contactComboBox;
    public TextField descriptionTextBox;
    public TextField titleTextBox;
    public TextField appointmentIDTextBox;
    public ComboBox startHourComboBox;
    public ComboBox endHourComboBox;
    public DatePicker dateOfAppointmentPicker;
    public TextField locationTextBox;
    public ComboBox contactNameComboBox;

    /**
     * This method initializes the starting values for the Add Appointment screen
     * @param url url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //set values of combo boxes
        startHourComboBox.setPromptText(Location.timeZone);
        endHourComboBox.setPromptText(Location.timeZone);
        startHourComboBox.setItems(TimeConversion.populateTimeStampList());
        endHourComboBox.setItems(TimeConversion.populateTimeStampList());

        try
        {
            contactNameComboBox.setItems(DBQuery.populateContactNames());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try
        {
            contactComboBox.setItems(DBQuery.populateContactComboBox());
        } catch (SQLException throwables) {
            throwables.printStackTrace();

        }
        try
        {
            customerIdComboBox.setItems(DBQuery.populateCustomerComboBox());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     * This method saves the user's input and takes them back to the Appointment screen.
     * @param actionEvent actionEvent
     * @throws SQLException
     * @throws IOException
     */
    public void onClickConfirm(ActionEvent actionEvent) throws SQLException, IOException, NullPointerException {
        //try/catch for validating data in case of null or overloaded text field
        try {
            //collect values to store
            String title = titleTextBox.getText();
            String description = descriptionTextBox.getText();
            String location = locationTextBox.getText();
            String type = typeTextBox.getText();
            int contact = (Integer) contactComboBox.getValue();
            int customerID = (Integer) customerIdComboBox.getValue();

            LocalTime startTime = (LocalTime) startHourComboBox.getValue();
            LocalTime endTime = (LocalTime) endHourComboBox.getValue();

            //check correct end date
            LocalDate endDate;
            if(startTime.isAfter(endTime) && startHourComboBox.getSelectionModel().getSelectedIndex() < endHourComboBox.getSelectionModel().getSelectedIndex()) {
                endDate = dateOfAppointmentPicker.getValue().plusDays(1);
            }
            else {
                endDate = dateOfAppointmentPicker.getValue();
                //validate that end time is after start time
                if (startTime.isAfter(endTime)) {
                    Message.invalidStartEndTime();
                    return;
                }
            }
            //convert user date/time input to UTC
            ZonedDateTime startTimeUTC = TimeConversion.convertToUTC(TimeConversion.createUserZonedDateTime(dateOfAppointmentPicker.getValue(), startTime.getHour(), startTime.getMinute()));
            ZonedDateTime endTimeUTC = TimeConversion.convertToUTC(TimeConversion.createUserZonedDateTime(endDate, endTime.getHour(), endTime.getMinute()));
            //convert from ZonedDateTime to Time Stamp
            Timestamp startTimeStamp = TimeConversion.ZonedDateTimeToTimeStamp(startTimeUTC);
            Timestamp endTimeStamp = TimeConversion.ZonedDateTimeToTimeStamp(endTimeUTC);



            //validate that the appointment time input by user does not conflict with an already existing appointment
           if (DBQuery.checkForConflicts(0, startTimeStamp, endTimeStamp)) {
                Message.overLappingAppointments();
                return;
            }


            //collect input information
            Appointment a = new Appointment(0, title, description, location, type, startTimeStamp, endTimeStamp, customerID, Validation.userID, contact);
            //insert new appointment
            DBQuery.insertAppointment(a);
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

        Parent root = (Parent)FXMLLoader.load(this.getClass().getResource("/view/appointmentScreen.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 846.0D, 403.0D);
        stage.setTitle("Appointments");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method takes the user back to the Appointment screen without saving the user's input
     * @param actionEvent actionEvent
     * @throws IOException
     */
    public void onClickCancel(ActionEvent actionEvent) throws IOException {
        if(!Message.lossOfProgressWarning()) {
            return;
        }

        Parent root = (Parent)FXMLLoader.load(this.getClass().getResource("/view/appointmentScreen.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 950.0D, 403.0D);
        stage.setTitle("Appointments");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method notifies the program of a contact's ID based on the user's selection in a combo box
     * @param actionEvent actionEvent
     */
    public void onSelectContactName(ActionEvent actionEvent) {
        int contactIndex = contactNameComboBox.getSelectionModel().getSelectedIndex();
        contactComboBox.getSelectionModel().clearAndSelect(contactIndex);
    }
}
