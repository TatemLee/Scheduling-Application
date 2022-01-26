package Main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * This class  manages appointments.
 */
public class Appointment {

    public ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

    /**
     * Constructor for the Appointment class. It holds the values necessary to populate appointment information
     * @param appointmentID Appointment Identifier
     * @param title Appointment Title
     * @param description Appointment Description
     * @param location Appointment Location
     * @param type Appointment Type
     * @param start Appointment Start Date/Time
     * @param end Appointment End Date/Time
     * @param customer_ID Associated Customer ID
     * @param user_ID Associated User ID
     * @param contact_ID Associated Customer ID
     */
    public Appointment(int appointmentID, String title, String description, String location, String type, Timestamp start, Timestamp end, int customer_ID, int user_ID, int contact_ID) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        customerID = customer_ID;
        userID = user_ID;
        contactID = contact_ID;

    }

    /**
     * This variable holds an Appointment Identifier
     */
    private int appointmentID;
    /**
     * This variable holds an Appointment Title
     */
    private String title;
    /**
     * This variable holds an Appointment Description
     */
    private String description;
    /**
     * This variable holds an Appointment Location
     */
    private String location;
    /**
     * This variable holds an Appointment Type
     */
    private String type;
    /**
     * This variable holds an Appointment Start Date/Time
     */
    private java.sql.Timestamp start;
    /**
     * This variable holds an Appointment End Date/Time
     */
    private java.sql.Timestamp end;
    /**
     * This variable holds an associated customer ID
     */
    private int customerID;
    /**
     * This variable holds an associated user ID
     */
    private int userID;
    /**
     * This variable holds an associated contact ID
     */
    private int contactID;

    /**
     * This variable holds a static Appointment Identifier for holding information between screens
     */
    public static int staticAppointmentID;
    /**
     * This variable holds a static Appointment Title for holding information between screens
     */
    public static String staticTitle;
    /**
     * This variable holds a static Appointment Description for holding information between screens
     */
    public static String staticDescription;
    /**
     * This variable holds a static Appointment Location for holding information between screens
     */
    public static String staticLocation;
    /**
     * This variable holds a static Appointment Type for holding information between screens
     */
    public static String staticType;
    /**
     * This variable holds a static Appointment Start Date/Time for holding information between screens
     */
    public static java.sql.Timestamp staticStart;
    /**
     * This variable holds a static Appointment End Date/Time for holding information between screens
     */
    public static java.sql.Timestamp staticEnd;
    /**
     * This variable holds a static Appointment Start Date/Time in LocalDateTime format for holding information between screens
     */
    public static LocalDateTime staticStartLDT;
    /**
     * This variable holds a static Appointment End Date/Time in LocalDateTime  for holding information between screens
     */
    public static LocalDateTime staticEndLDT;
    /**
     * This variable holds a static associated customer ID for holding information between screens
     */
    public static int staticCustomerID;
    /**
     * This variable holds a static associated user ID for holding information between screens
     */
    public static int staticUserID;
    /**
     * This variable holds a static associated contact ID for holding information between screens
     */
    public static int staticContactID;

    /**
     * this method returns an Appointment Identifier
     * @return int appointment ID
     */
    public int  getAppointmentID() {
        return appointmentID;
    }

    /**
     * this method sets an Appointment Identifier
     * @param appointmentID int appointment ID
     */
    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }
    /**
     * this method returns an Appointment Title
     * @return String appointment title
     */
    public String getTitle() {
        return title;
    }

    /**
     * this method sets  an Appointment Title
     * @param title String appointment title
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * this method returns an Appointment Description
     * @return String appointment description
     */
    public String getDescription() {
        return description;
    }

    /**
     *  this method sets  an Appointment Description
     * @param description String appointment description
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * this method returns an Appointment Location
     * @return String appointment Location
     */
    public String getLocation() {
        return location;
    }

    /**
     * this method sets an Appointment Location
     * @param location String appointment location
     */
    public void setLocation(String location) {
        this.location = location;
    }
    /**
     * this method returns an Appointment Type
     * @return String appointment Type
     */
    public String getType() {
        return type;
    }

    /**
     * this method sets an Appointment Type
     * @param type String appointment type
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * this method returns an Appointment Start Date/Time
     * @return Timestamp appointment time
     */
    public Timestamp getStart() {
        return start;
    }

    /**
     * this method sets an Appointment Start Date/Time
     * @param start Timestamp appointment start
     */
    public void setStart(Timestamp start) {
        this.start = start;
    }
    /**
     * this method returns an Appointment End Date/Time
     * @return Timestamp appointment time
     */
    public Timestamp getEnd() {
        return end;
    }

    /**
     *  this method sets an Appointment End Date/Time
     * @param end Timestamp appointment end
     */
    public void setEnd(Timestamp end) {
        this.end = end;
    }
    /**
     * this method returns an associated Customer ID
     * @return int customer ID
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * this method sets  an associated Customer ID
     * @param customerID int customer ID
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }
    /**
     * this method returns an associated user ID
     * @return int user ID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * this method sets an associated user ID
     * @param userID int user ID
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }
    /**
     * this method returns an associated contact ID
     * @return int contact ID
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * this method sets an associated contact ID
     * @param contactID int contact ID
     */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }





}
