package Controller;

import Main.Appointment;
import Main.Message;
import Main.TimeConversion;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

public class updateAppointment implements Initializable {


    public TextField appointmentIDTextBox;
    public TextField titleTextBox;
    public TextField descriptionTextBox;
    public TextField locationTextBox;
    public ComboBox contactComboBox;
    public ComboBox customerIdComboBox;
    public TextField typeTextBox;
    public TextField userIdTextBox;
    public DatePicker dateOfAppointmentPicker;
    public ComboBox startHourComboBox;
    public ComboBox endHourComboBox;
    public ComboBox contactNameComboBox;

    /**
     * This method initializes the starting values for the Update Appointment screen
     * @param url url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //set date picker and time boxes
       this.dateOfAppointmentPicker.setValue(TimeConversion.TimeStampToLocalDate(Appointment.staticStart));
       this.startHourComboBox.setItems(TimeConversion.populateTimeStampList());
       this.endHourComboBox.setItems(TimeConversion.populateTimeStampList());
       this.startHourComboBox.setValue(Appointment.staticStartLDT.toLocalTime());
       this.endHourComboBox.setValue(Appointment.staticEndLDT.toLocalTime());


        this.contactComboBox.setValue(Appointment.staticContactID);
        this.customerIdComboBox.setValue(Appointment.staticCustomerID);
        this.appointmentIDTextBox.setText(String.valueOf(Appointment.staticAppointmentID));
        this.titleTextBox.setText(Appointment.staticTitle);
        this.descriptionTextBox.setText(Appointment.staticDescription);
        this.typeTextBox.setText(Appointment.staticType);
        this.userIdTextBox.setText(String.valueOf(Appointment.staticUserID));
        this.locationTextBox.setText(Appointment.staticLocation);
        //set values of combo boxes
        try {
            contactNameComboBox.setItems(DBQuery.populateContactNames());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            this.contactComboBox.setItems(DBQuery.populateContactComboBox());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            this.customerIdComboBox.setItems(DBQuery.populateCustomerComboBox());
        } catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

    /**
     * This method confirms the user's input and takes them back to the Appointment screen
     * @param actionEvent actionEvent
     * @throws IOException
     * @throws SQLException
     */
    public void onClickConfirm(ActionEvent actionEvent) throws IOException, SQLException, NullPointerException {
        //try/catch in case of null values
        try {
            //collect values to store
            Appointment.staticTitle = titleTextBox.getText();
            Appointment.staticDescription = descriptionTextBox.getText();
            Appointment.staticLocation = locationTextBox.getText();
            Appointment.staticType = typeTextBox.getText();
            Appointment.staticContactID = (Integer) contactComboBox.getValue();
            Appointment.staticCustomerID = (Integer) customerIdComboBox.getValue();

            LocalTime startTime = (LocalTime) startHourComboBox.getValue();
            LocalTime endTime = (LocalTime) endHourComboBox.getValue();
            //create correct end date
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
            Appointment.staticStart = TimeConversion.ZonedDateTimeToTimeStamp(startTimeUTC);
            Appointment.staticEnd = TimeConversion.ZonedDateTimeToTimeStamp(endTimeUTC);



            if (DBQuery.checkForConflicts(Appointment.staticAppointmentID, Appointment.staticStart, Appointment.staticEnd)) {
                Message.overLappingAppointments();
                return;
            }

            DBQuery.updateAppointmentByID(Appointment.staticAppointmentID);
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
        Scene scene = new Scene(root, 950.0D, 403.0D);
        stage.setTitle("Appointments");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method returns the user to the Appointment screen without saving their progress.
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
     * This method notifies the program of a Contact's ID based on a user's selection
     * @param actionEvent
     */
    public void onSelectContactName(ActionEvent actionEvent) {
        int contactIndex = contactNameComboBox.getSelectionModel().getSelectedIndex();
        contactComboBox.getSelectionModel().clearAndSelect(contactIndex);
    }
}
