package Main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class manages customer records.
 */
public class Customer{
    ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    ObservableList<String> allCountries = FXCollections.observableArrayList();
    ObservableList<String> allFirstLevel = FXCollections.observableArrayList();


    /**
     * Customer default constructor
     */
    public Customer() {
    }

    /**
     * Loaded constructor for Customer class
     * @param customerID customer identifier
     * @param name customer name
     * @param address customer address
     * @param postalCode customer postal code
     * @param division customer division
     * @param country customer country
     * @param phoneNumber customer phone number
     */
    public Customer(int customerID, String name, String address, String postalCode, String  division, String country, String phoneNumber) {
        this.customerID = customerID;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.division = division;
        this.country = country;
        this.phoneNumber = phoneNumber;
    }

   //private variables for loading table information
    /**
     * This variable holds a customer identifier
     */
    private int customerID;
    /**
     * This variable holds a customer name
     */
    private String name;
    /**
     * This variable holds a customer address
     */
    private String address;
    /**
     * This variable holds a customer postal code
     */
    private String postalCode;
    /**
     * This variable holds a customer division
     */
    private String division;
    /**
     * This variable holds a customer country
     */
    private String country;
    /**
     * This variable holds a customer phone number
     */
    private String phoneNumber;

    /**
     * This variable holds a for customer identifier transferring between screens
     */
    public static int  staticCustomerID;
    /**
     * This variable holds a customer name  for transferring between screens
     */
    public static String staticName;
    /**
     * This variable holds a customer phone number for transferring between screens
     */
    public static String staticPhoneNumber;
    /**
     * This variable holds a customer address for transferring between screens
     */
    public static String staticAddress;
    /**
     * This variable holds a customer postal code for transferring between screens
     */
    public static String staticPostalCode;
    /**
     * This variable holds a customer country for transferring between screens
     */
    public static String staticCountry;
    /**
     * This variable holds a customer first level for transferring between screens
     */
    public static String staticFirstLevel;



    // getters and setters for Customer private variables

    /**
     * This method returns a customer identifier
     * @return int customer identifier
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * This method sets a customer identifier
     * @param customerID int customer identifier
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }
    /**
     * This method returns a customer name
     * @return String customer name
     */
    public String getName() {
        return name;
    }

    /**
     * This method sets a customer name
     * @param name String customer name
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * This method returns a customer address
     * @return String customer address
     */
    public String getAddress() {
        return address;
    }

    /**
     * This method sets a customer address
     * @param address String customer address
     */
    public void setAddress(String address) {
        this.address = address;
    }
    /**
     * This method returns a customer postal code
     * @return String customer postal code
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * This method sets a customer postal code
     * @param postalCode String customer postal code
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    /**
     * This method returns a customer division
     * @return String customer division
     */
    public String getDivision() {
        return division;
    }

    /**
     * This method sets a customer division
     * @param division String customer division
     */
    public void setDivision(String division) {
        this.division = division;
    }
    /**
     * This method returns a customer country
     * @return String customer country
     */
    public String getCountry() {
        return country;
    }

    /**
     * This method sets a customer country
     * @param country String customer country
     */
    public void setCountry(String country) {
        this.country = country;
    }
    /**
     * This method returns a customer phone number
     * @return String customer phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * This method sets a customer phone number
     * @param phoneNumber String customer phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * This method creates a new customer object
     * @param customerID customer identifier
     * @param name customer name
     * @param address customer address
     * @param postalCode customer postal code
     * @param division customer division
     * @param country customer country
     * @param phoneNumber customer phone number
     * @return Customer object
     */
    public Customer createNewCustomer(int customerID, String name, String address, String postalCode, String division, String country, String phoneNumber) {
        Customer c = new Customer(customerID, name, address, postalCode, division, country, phoneNumber);
        return c;
    }

}

