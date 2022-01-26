package Main;


import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.Timestamp;
import java.util.Optional;

/**
 * This class holds alert messages to display for the user.
 */
public class Message {


    /**
     * Notifies the user in either English or French of an invalid login input
     */
    public static void invalidLogin() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        //displays text based on language
        if(Language.getLocalLanguageIsFrench()) {
            alert.setTitle("Combinaison nom d'utilisateur/mot de passe incorrecte");
            alert.setHeaderText("Le nom d'utilisateur et/ou le mot de passe que vous avez saisis sont incorrects");
            alert.setContentText("Veuillez réessayer ou contacter l'administrateur");
        }
        else {
            alert.setTitle("Incorrect Username/Password Combination");
            alert.setHeaderText("The Username and/or Password you have entered is incorrect");
            alert.setContentText("Please try again, or contact the administrator");
        }
        alert.showAndWait();
    }

    /**
     * Notifies the user that they cannot delete a customer with associated appointments
     */
    public static void associatedAppointment() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Delete Operation Failure");
        alert.setHeaderText("This cannot be deleted at this time");
        alert.setContentText("To delete this Customer please first delete all of the selected Customer's appointments and try again");
        alert.showAndWait();
    }

    /**
     * This method asks the user to confirm the delete action
     * @return Boolean delete confirmation
     */
    public static boolean deleteCustomerConfirmed() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("This action cannot be reversed");
        alert.setContentText("Press 'OK' to Confirm deletion of selected customer. Press 'Cancel' to abort the process");

        boolean confirmed = false;
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.CANCEL)
            confirmed = false;
        if(result.isPresent() && result.get() == ButtonType.OK)
            confirmed = true;

        return confirmed;

    }

    /**
     * This method asks the user to confirm the delete action
     * @return Boolean delete confirmation
     */
    public static boolean deleteAppointmentConfirmed() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("This action cannot be reversed");
        alert.setContentText("Press 'OK' to Confirm deletion of selected appointment. Press 'Cancel' to abort the process");

        boolean confirmed = false;
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.CANCEL)
            confirmed = false;
        if(result.isPresent() && result.get() == ButtonType.OK)
            confirmed = true;

        return confirmed;
    }

    /**
     * Notifies the user of a successful customer deletion
     */
    public static void deleteCustomerSuccessful() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Delete Operation Successful");
        alert.setHeaderText("The selected customer has been permanently deleted");
        alert.setContentText("Please press 'OK' to continue");
        alert.showAndWait();
    }

    /**
     * Notifies the user of a successful appointment deletion
     */
    public static void deleteAppointmentSuccessful(int appointmentID, String type) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Delete Operation Successful");
        alert.setHeaderText("The following appointment has been permanently deleted:");
        alert.setContentText("Appointment ID: " + appointmentID + ". \nAppointment Type: " + type + ".");
        alert.showAndWait();
    }

    /**
     * Warns user of loss of progress if they confirm
     * @return Boolean user confirmation
     */
    public static boolean lossOfProgressWarning() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning!");
        alert.setHeaderText("Your changes have not be saved");
        alert.setContentText("Press 'OK' to abandon changes. Press 'Cancel' to continue editing");

        boolean confirmed = false;
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.CANCEL)
            confirmed = false;
        if(result.isPresent() && result.get() == ButtonType.OK)
            confirmed = true;

        return confirmed;

    }

    /**
     * This method asks the user to confirm logout operation
     * @return Boolean logout confirmation
     */
    public static boolean logoutWarning() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.setContentText("Press 'OK' to logout. Press 'Cancel' to continue use of this application");

        boolean confirmed = false;
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.CANCEL)
            confirmed = false;
        if(result.isPresent() && result.get() == ButtonType.OK)
            confirmed = true;

        return confirmed;

    }

    /**
     * Notifies the user of overlapping appointment times
     */
    public static void overLappingAppointments() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Overlapping Appointments");
        alert.setHeaderText("The time you have selected overlaps with another appointment");
        alert.setContentText("Please input a time that does not conflict with another appointment" );
        alert.showAndWait();
    }

    /**
     * Notifies user of upcoming appointment
     * @param appointmentID int appointment identifier
     * @param timestamp Timestamp  appointment start
     */
    public static void upcomingAppointment(int appointmentID, Timestamp timestamp) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Upcoming Appointment");
        alert.setHeaderText("There is an appointment starting within the next fifteen minutes");
        alert.setContentText("Appointment ID:  " + appointmentID + "\n" + "Appointment Date and Time: " + timestamp);
        alert.showAndWait();
    }

    /**
     * Notifies the user that no selection has been made
     */
    public static void noSelection() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("No Selection");
        alert.setContentText("Please make a selection first to continue this operation");
        alert.showAndWait();
    }

    /**
     * Notifies user of successful time change operation
     */
    public static void successfulTimeChange() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Successful Operation");
        alert.setHeaderText("Time Changed");
        alert.setContentText("The time change operation has been completed successfully");
        alert.showAndWait();
    }

    /**
     * Notifies user of invalid time input
     */
    public static void invalidStartEndTime() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Input");
        alert.setHeaderText("Start Time must be earlier than End Time");
        alert.setContentText("Please enter a valid time to continue");
        alert.showAndWait();
    }

    /**
     * Notifies user of incomplete form fields
     */
    public static void incompleteFields() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Incomplete Fields");
        alert.setContentText("You must complete all fields to perform this action. Please complete all fields and try again");
        alert.showAndWait();
    }

    /**
     * Notifies user of overloaded form fields
     */
    public static void overloadedField() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Overloaded Fields");
        alert.setContentText("All fields are limited to 50 characters");
        alert.showAndWait();
    }



}
