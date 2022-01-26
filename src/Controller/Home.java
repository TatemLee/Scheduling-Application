package Controller;

import Main.Appointment;
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
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class Home implements Initializable {

    public Label upcomingAppointmentsLabel;

    /**
     * This method initializes the starting values for the Home page
     * @param url url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)  {
        try {
            checkForUpcomingAppointments();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     * This method checks for appointments within fifteen minutes of the user's login. This method compares the user's current time to the times of appointments.
     * (the appointments are converted to the users local time before comparison)
     * @throws SQLException
     */
    public void checkForUpcomingAppointments() throws SQLException {

        Timestamp userTimestamp = Timestamp.valueOf(LocalDateTime.now());
        System.out.println("User time stamp for home: " + userTimestamp);
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        allAppointments = DBQuery.populateAppointmentTable();
        for(int i = 0; i < allAppointments.size(); i++) {
            Appointment a = allAppointments.get(i);
            //get start time of comparison appointment
            Timestamp compStart = a.getStart();
            //create timestamp 15 minutes before  comparison appointment time (compStart)
            Timestamp fifteenMinutesAfter = Timestamp.valueOf(a.getStart().toLocalDateTime().minusMinutes(15));
            //compare times to identify if an appointment starts in the next 15 minutes
            if(userTimestamp.compareTo(compStart) <= 0 && userTimestamp.compareTo(fifteenMinutesAfter) >= 0){
                Message.upcomingAppointment(a.getAppointmentID(), compStart);
                //update user interface
                upcomingAppointmentsLabel.setText("Upcoming Appointment ID: " +  a.getAppointmentID() + " Time: " + compStart);
                
            }
        }
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
     * This method loads the appointments screen
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
     * This method loads the Reports screen
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
