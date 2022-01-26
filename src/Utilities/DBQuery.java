package Utilities;

import Main.Appointment;
import Main.Customer;
import Main.TimeConversion;
import Main.Validation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * This class holds methods that execute the program's Database Queries
 */
public class DBQuery {

    /**
     * This variable holds a prepared statement
     */
    private static PreparedStatement statement;
    /**
     * This variable holds the result of operations checking for associated appointments
     */
    private static boolean isAssociated;


    /**
     * This method sets the value of a prepared statement
     *
     * @param conn         database connection
     * @param sqlStatement prepared sql statement
     * @throws SQLException
     */
    public static void setPreparedStatement(Connection conn, String sqlStatement) throws SQLException {
        statement = conn.prepareStatement(sqlStatement);
    }

    /**
     * This method returns a prepared sql statement
     *
     * @return PreparedStatement SQL statement
     */
    public static PreparedStatement getPreparedStatement() {
        return statement;
    }


    /**
     * This method populates the Customer table
     *
     * @return list of customers
     * @throws SQLException
     */
    public static ObservableList<Customer> populateCustomerTable() throws SQLException {
        //set up connection
        Connection conn = JDBC.getConnection();
        //pull data for Customer Table
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
                ";";

        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();

        //create observable list to return
        ObservableList<Customer> customerTableData = FXCollections.observableArrayList();

        //create objects for list from pulled data
        while (rs.next()) {
            int customerID = rs.getInt("Customer_ID");
            String name = rs.getString("Customer_Name");
            String address = rs.getString("Address");
            String postalCode = rs.getString("Postal_Code");
            String division = rs.getString("Division");
            String country = rs.getString("Country");
            String phoneNumber = rs.getString("Phone");
            Customer c = new Customer(customerID, name, address, postalCode, division, country, phoneNumber);

            customerTableData.add(c);
        }
        return customerTableData;
    }

    /**
     * This method populates the Appointment table
     *
     * @return list of appointments
     * @throws SQLException
     */
    public static ObservableList<Appointment> populateAppointmentTable() throws SQLException {
        //set up connection
        Connection conn = JDBC.getConnection();
        //pull data for Customer Table
        String selectStatement = "SELECT * FROM client_schedule.appointments;";

        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();

        //create observable list to return
        ObservableList<Appointment> appointmentTableData = FXCollections.observableArrayList();

        //create objects for list from pulled data
        while (rs.next()) {
            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            int contactID = rs.getInt("Contact_ID");

            //grab timestamp values and convert to user time
            ZoneId utc = ZoneId.of("UTC");

            Timestamp start = (rs.getTimestamp("Start"));
            Timestamp end = rs.getTimestamp("End");
           // System.out.println("Start and End from Database: " + start + " " + end);
            Timestamp userStart = TimeConversion.ZonedDateTimeToTimeStamp(TimeConversion.timestampToUserZonedDateTime(start));
            Timestamp userEnd = TimeConversion.ZonedDateTimeToTimeStamp(TimeConversion.timestampToUserZonedDateTime(end));

            Appointment a = new Appointment(appointmentID, title, description, location, type, userStart, userEnd, customerID, userID, contactID);

            appointmentTableData.add(a);
        }

        return appointmentTableData;
    }

    /**
     * This method populates a list of appointments based on input contact ID
     *
     * @param inputID contact ID
     * @return list of appointments associated with input contact ID
     * @throws SQLException
     */
    public static ObservableList<Appointment> populateAppointmentsByContact(int inputID) throws SQLException {
        //set up connection
        Connection conn = JDBC.getConnection();
        //pull data for Customer Table
        String selectStatement = "SELECT * FROM client_schedule.appointments WHERE Contact_ID = " + inputID + ";";

        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();

        //create observable list to return
        ObservableList<Appointment> appointmentTableData = FXCollections.observableArrayList();

        //create objects for list from pulled data
        while (rs.next()) {
            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            int contactID = rs.getInt("Contact_ID");

            //grab timestamp values and convert to user time
            Timestamp start = rs.getTimestamp("Start");
            Timestamp end = rs.getTimestamp("End");
            Timestamp userStart = TimeConversion.ZonedDateTimeToTimeStamp(TimeConversion.timestampToUserZonedDateTime(start));
            Timestamp userEnd = TimeConversion.ZonedDateTimeToTimeStamp(TimeConversion.timestampToUserZonedDateTime(end));

            Appointment a = new Appointment(appointmentID, title, description, location, type, userStart, userEnd, customerID, userID, contactID);

            appointmentTableData.add(a);
        }

        return appointmentTableData;
    }

    /**
     * This method populates a list of appointments based on the user system's current set month
     *
     * @return list of appointments
     * @throws SQLException
     */
    public static ObservableList<Appointment> populateAppointmentByMonth() throws SQLException {
        //set up connection
        Connection conn = JDBC.getConnection();
        //pull data for Customer Table
        String selectStatement = "SELECT * FROM client_schedule.appointments;";

        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();

        //create observable list to return
        ObservableList<Appointment> appointmentTableData = FXCollections.observableArrayList();

        //create objects for list from pulled data
        while (rs.next()) {
            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            int contactID = rs.getInt("Contact_ID");

            //grab timestamp values and convert to user time
            Timestamp start = rs.getTimestamp("Start");
            Timestamp end = rs.getTimestamp("End");
            Timestamp userStart = TimeConversion.ZonedDateTimeToTimeStamp(TimeConversion.timestampToUserZonedDateTime(start));
            Timestamp userEnd = TimeConversion.ZonedDateTimeToTimeStamp(TimeConversion.timestampToUserZonedDateTime(end));

            Appointment a = new Appointment(appointmentID, title, description, location, type, userStart, userEnd, customerID, userID, contactID);

            //current month object
            Timestamp currentTimestamp = Timestamp.from(Instant.now());

            //check if appointment object matches current month
            if (currentTimestamp.toLocalDateTime().getYear() == a.getStart().toLocalDateTime().getYear() && currentTimestamp.toLocalDateTime().getMonth() == a.getStart().toLocalDateTime().getMonth()) {
                appointmentTableData.add(a);
            }
        }
        return appointmentTableData;
    }

    /**
     * This method populates a list of appointments based on the user system's current set week
     *
     * @return list of appointments
     * @throws SQLException
     */
    public static ObservableList<Appointment> populateAppointmentByWeek() throws SQLException {
        //set up connection
        Connection conn = JDBC.getConnection();
        //pull data for Customer Table
        String selectStatement = "SELECT * FROM client_schedule.appointments;";

        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();

        //create observable list to return
        ObservableList<Appointment> appointmentTableData = FXCollections.observableArrayList();

        //create objects for list from pulled data
        while (rs.next()) {
            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            int contactID = rs.getInt("Contact_ID");

            //grab timestamp values and convert to user time
            Timestamp start = rs.getTimestamp("Start");
            Timestamp end = rs.getTimestamp("End");
            Timestamp userStart = TimeConversion.ZonedDateTimeToTimeStamp(TimeConversion.timestampToUserZonedDateTime(start));
            Timestamp userEnd = TimeConversion.ZonedDateTimeToTimeStamp(TimeConversion.timestampToUserZonedDateTime(end));

            Appointment a = new Appointment(appointmentID, title, description, location, type, userStart, userEnd, customerID, userID, contactID);

            //current week object
            Timestamp currentTimestamp = Timestamp.from(Instant.now());

            //check if appointment object matches current month
            if (currentTimestamp.toLocalDateTime().getYear() == a.getStart().toLocalDateTime().getYear()) {
                //check if appointment object matches current week
                if (TimeConversion.identifyMonday(currentTimestamp).toLocalDateTime().getDayOfYear() <= a.getStart().toLocalDateTime().getDayOfYear() && TimeConversion.identifySunday(currentTimestamp).toLocalDateTime().getDayOfYear() >= a.getStart().toLocalDateTime().getDayOfYear()) {
                    appointmentTableData.add(a);
                }
            }
        }
        return appointmentTableData;
    }

    /**
     * This method populates the update appointments form based on a given appointment ID
     *
     * @param appointmentID reference appointmentID
     * @throws SQLException
     */
    public static void populateUpdateAppointmentForm(int appointmentID) throws SQLException {
        //set up connection
        Connection conn = JDBC.getConnection();
        //pull data for Customer Table
        String selectStatement = "SELECT * FROM client_schedule.appointments WHERE Appointment_ID = " + appointmentID + " ;";

        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();

        //fill static variables with appointment's information
        while (rs.next()) {
            Appointment.staticAppointmentID = rs.getInt("Appointment_ID");
            Appointment.staticTitle = rs.getString("Title");
            Appointment.staticDescription = rs.getString("Description");
            Appointment.staticLocation = rs.getString("Location");
            Appointment.staticType = rs.getString("Type");
            Appointment.staticCustomerID = rs.getInt("Customer_ID");
            Appointment.staticUserID = rs.getInt("User_ID");
            Appointment.staticContactID = rs.getInt("Contact_ID");

            Appointment.staticStart = rs.getTimestamp("Start");
            Appointment.staticEnd = rs.getTimestamp("End");
            //convert UTC timestamp to LocalDateTime in user's timezone
            Appointment.staticStartLDT = TimeConversion.ZonedDateTimeToTimeStamp(TimeConversion.timestampToUserZonedDateTime(Appointment.staticStart)).toLocalDateTime();
            Appointment.staticEndLDT = TimeConversion.ZonedDateTimeToTimeStamp(TimeConversion.timestampToUserZonedDateTime(Appointment.staticEnd)).toLocalDateTime();

            System.out.println("UTC Start Time:  " + Appointment.staticStart);
            System.out.println("Local Start Time:  " + Appointment.staticStartLDT);
        }
    }


    /**
     * This method populates a country combo box
     *
     * @return list of countries
     * @throws SQLException
     */
    public static ObservableList<String> populateCountryComboBox() throws SQLException {
        ObservableList<String> countryComboBoxData = FXCollections.observableArrayList();

        Connection conn = JDBC.getConnection();
        String selectStatement = "SELECT Country FROM countries";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();

        while (rs.next()) {
            String country = rs.getString("Country");
            countryComboBoxData.add(country);
        }
        return countryComboBoxData;
    }

    /**
     * This method populates a first level combo box based on a country ID
     *
     * @param countryID input country ID
     * @return list of first level regions
     * @throws SQLException
     */
    public static ObservableList<String> populateFirstLevelComboBox(int countryID) throws SQLException {
        ObservableList<String> firstLevelComboBoxData = FXCollections.observableArrayList();

        Connection conn = JDBC.getConnection();
        String selectStatement = "SELECT Division FROM client_schedule.first_level_divisions WHERE Country_ID = " + countryID + ";";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();

        while (rs.next()) {
            String Division = rs.getString("Division");
            firstLevelComboBoxData.add(Division);
        }
        return firstLevelComboBoxData;
    }

    /**
     * This method populates a contact combo box
     *
     * @return list of contacts
     * @throws SQLException
     */
    public static ObservableList<Integer> populateContactComboBox() throws SQLException {
        ObservableList<Integer> allContacts = FXCollections.observableArrayList();

        Connection conn = JDBC.getConnection();
        String selectStatement = "SELECT Contact_ID FROM client_schedule.contacts;";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();

        while (rs.next()) {
            int contact = rs.getInt("Contact_ID");
            allContacts.add(contact);
        }
        return allContacts;
    }

    /**
     * This method populates a contact name combo box
     *
     * @return list of contact names
     * @throws SQLException
     */
    public static ObservableList<String> populateContactNames() throws SQLException {
        ObservableList<String> allContacts = FXCollections.observableArrayList();

        Connection conn = JDBC.getConnection();
        String selectStatement = "SELECT Contact_Name FROM client_schedule.contacts;";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();

        while (rs.next()) {
            String contact = rs.getString("Contact_Name");
            allContacts.add(contact);
        }
        return allContacts;
    }

    /**
     * This method populates a customer combo box
     *
     * @return list of customers
     * @throws SQLException
     */
    public static ObservableList<Integer> populateCustomerComboBox() throws SQLException {
        ObservableList<Integer> allCustomers = FXCollections.observableArrayList();

        Connection conn = JDBC.getConnection();
        String selectStatement = "SELECT Customer_ID FROM client_schedule.customers;";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();

        while (rs.next()) {
            int customer = rs.getInt("Customer_ID");
            allCustomers.add(customer);
        }
        return allCustomers;
    }

    /**
     * This method populates a type combo box
     *
     * @return list of types
     * @throws SQLException
     */
    public static ObservableList<String> populateTypeComboBox() throws SQLException {
        ObservableList<String> allDistinctTypes = FXCollections.observableArrayList();
        Connection conn = JDBC.getConnection();
        String selectStatement = "SELECT DISTINCT Type FROM client_schedule.appointments;";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();

        while (rs.next()) {
            String type = rs.getString("Type");
            allDistinctTypes.add(type);

        }
        return allDistinctTypes;
    }

    /**
     * This method counts appointments by a given type/ month
     *
     * @param type  type to count by
     * @param month month to count by
     * @return int total count of matches
     * @throws SQLException
     */
    public static int countAppointmentsByTypeMonth(String type, int month) throws SQLException {
        int count = 0;
        Connection conn = JDBC.getConnection();
        String selectStatement = "SELECT Type, Start FROM client_schedule.appointments;";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();

        //increase the count for every result
        while (rs.next()) {
            String compType = rs.getString("Type");
            //get month value as int where January = 1
            int compMonth = rs.getTimestamp("Start").toLocalDateTime().getMonthValue();
            if (type.equals(compType) && month == compMonth)
                count++;
        }
        return count;
    }

    /**
     * This method counts appointments by Type
     *
     * @param type type to count by
     * @return int total count of matches
     * @throws SQLException
     */
    public static int countAppointmentsByType(String type) throws SQLException {
        int count = 0;
        Connection conn = JDBC.getConnection();
        String selectStatement = "SELECT Type FROM client_schedule.appointments;";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();

        //increase the count for every result
        while (rs.next()) {
            String compType = rs.getString("Type");
            if (type.equals(compType))
                count++;
        }
        return count;
    }

    /**
     * This method counts appointments by month
     *
     * @param month month to count by
     * @return int total count of matches
     * @throws SQLException
     */
    public static int countAppointmentsByMonth(int month) throws SQLException {
        int count = 0;
        Connection conn = JDBC.getConnection();
        String selectStatement = "SELECT Start FROM client_schedule.appointments;";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();

        //increase the count for every result
        while (rs.next()) {
            //get month value as int where January = 1
            int compMonth = rs.getTimestamp("Start").toLocalDateTime().getMonthValue();
            if (month == compMonth)
                count++;
        }
        return count;
    }

    /**
     * This method counts appointments by customer location
     *
     * @param divisionID customer location IO
     * @return int total count of matches
     * @throws SQLException
     */
    public static int countCustomerByLocation(int divisionID) throws SQLException {
        int count = 0;
        Connection conn = JDBC.getConnection();
        String selectStatement = "SELECT Customer_ID FROM client_schedule.customers WHERE Division_ID = " + divisionID + ";";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();

        //increase the count for every result
        while (rs.next()) {
            count++;
        }
        return count;
    }

    /**
     * This method identifies a country ID by name
     *
     * @param country String country name
     * @return country ID
     * @throws SQLException
     */
    public static int identifyCountryID(String country) throws SQLException {

        Connection conn = JDBC.getConnection();
        String selectStatement = "SELECT Country_ID FROM client_schedule.countries WHERE Country = '" + country + "';";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();

        //creating variable to hold ID. Note: the data base is set to auto increment and 0 will never be used.
        int countryID = 0;
        //catch ID from table
        while (rs.next()) {
            countryID = rs.getInt("Country_ID");
        }
        return countryID;
    }

    /**
     * This method identifies a division ID by name
     *
     * @param divisionID String division name
     * @return division ID
     * @throws SQLException
     */
    public static int identifyDivisionID(String divisionID) throws SQLException {

        Connection conn = JDBC.getConnection();
        String selectStatement = "SELECT Division_ID  FROM client_schedule.first_level_divisions WHERE Division = '" + divisionID + "';";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();

        //creating variable to hold ID. Note: the data base is set to auto increment and 0 will never be used.
        int division = 0;
        //catch ID from table
        while (rs.next()) {
            division = rs.getInt("Division_ID");
        }
        return division;
    }

    /**
     * This method inserts a customer into the database
     *
     * @param customer   Customer object
     * @param divisionID division identifier
     * @throws SQLException
     */
    public static void insertCustomer(Customer customer, int divisionID) throws SQLException {
        Connection conn = JDBC.getConnection();
        String selectStatement = "INSERT INTO customers(Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By,  Last_Update, Last_Updated_By, Division_ID) " +
                "VALUES(" +
                "'" + customer.getName() + "'," +
                "'" + customer.getAddress() + "'," +
                "'" + customer.getPostalCode() + "'," +
                "'" + customer.getPhoneNumber() + "'," +
                " now()," +
                " 'user'," +
                " now()," +
                "'" + Validation.userName + "'," +
                "'" + divisionID + "'" +
                ")";

        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
    }

    /**
     * This method inserts an appointment into the database
     *
     * @param appointment Appointment object
     * @throws SQLException
     */
    public static void insertAppointment(Appointment appointment) throws SQLException {
        Connection conn = JDBC.getConnection();
        String selectStatement = "INSERT INTO client_schedule.appointments" +
                "(Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) " +
                "VALUES(" +
                "'" + appointment.getTitle() + "', " +
                "'" + appointment.getDescription() + "', " +
                "'" + appointment.getLocation() + "', " +
                "'" + appointment.getType() + "', " +
                "'" + appointment.getStart() + "', " +
                "'" + appointment.getEnd() + "', " +
                " now(), " +
                "'" + Validation.userName + "', " +
                " now(), " +
                "'" + Validation.userName + "', " +
                "'" + appointment.getCustomerID() + "', " +
                "'" + appointment.getUserID() + "', " +
                "'" + appointment.getContactID() + "' " +
                ");";

        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
    }

    /**
     * This method deletes a Customer record by ID
     *
     * @param customerID customer identifier
     * @throws SQLException
     */
    public static void deleteCustomerByID(int customerID) throws SQLException {
        Connection conn = JDBC.getConnection();
        String selectStatement = "DELETE FROM client_schedule.customers WHERE Customer_ID = " + customerID;
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();

    }

    /**
     * This method deletes an Appointment record by ID
     *
     * @param appointmentID
     * @throws SQLException
     */
    public static void deleteAppointmentByID(int appointmentID) throws SQLException {
        Connection conn = JDBC.getConnection();
        String selectStatement = "DELETE FROM client_schedule.appointments WHERE Appointment_ID  = " + appointmentID;
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
    }


    /**
     * This method updates a Customer Record by ID
     *
     * @param customerID customer identifier
     * @param divisionID division identifier
     * @throws SQLException
     */
    public static void updateCustomerByID(int customerID, int divisionID) throws SQLException {
        Connection conn = JDBC.getConnection();
        String selectStatement = "UPDATE " +
                "client_schedule.customers " +
                "SET " +
                "Customer_Name = '" + Customer.staticName + "', " +
                "Address = '" + Customer.staticAddress + "', " +
                "Postal_Code = '" + Customer.staticPostalCode + "', " +
                "Phone = '" + Customer.staticPhoneNumber + "', " +
                "Last_Update = now(), " +
                "Last_Updated_By = '" + Validation.userName + "', " +
                "Division_ID = '" + divisionID + "' " +
                "WHERE Customer_ID = " + customerID + " ;";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
    }

    /**
     * This method updates an Appointment record by ID
     *
     * @param appointmentID appointment identifier
     * @throws SQLException
     */
    public static void updateAppointmentByID(int appointmentID) throws SQLException {
        Connection conn = JDBC.getConnection();
        String selectStatement = "UPDATE " +
                "client_schedule.appointments " +
                "SET " +
                "Title = '" + Appointment.staticTitle + "', " +
                "Description = '" + Appointment.staticDescription + "', " +
                "Location = '" + Appointment.staticLocation + "', " +
                "Type = '" + Appointment.staticType + "', " +
                "Start = '" + Appointment.staticStart + "', " +
                "End = '" + Appointment.staticEnd + "', " +
                "Last_Update = now(), " +
                "Last_Updated_By = '" + Validation.userName + "', " +
                "Customer_ID = " + Appointment.staticCustomerID + ", " +
                "User_ID = " + Appointment.staticUserID + ", " +
                "Contact_ID = " + Appointment.staticContactID + " " +
                "WHERE Appointment_ID = " + appointmentID + " ;";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
    }

    /**
     * This method updates an Appointment's time by appointment ID
     *
     * @param appointmentID appointment identifier
     * @throws SQLException
     */
    public static void updateAppointmentTime(int appointmentID) throws SQLException {
        Connection conn = JDBC.getConnection();
        String selectStatement = "UPDATE client_schedule.appointments SET Start = '" + Appointment.staticStart + "', End = '" + Appointment.staticEnd + "' WHERE Appointment_ID = " + appointmentID + ";";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
    }


    /**
     * This method checks for appointments associated with input customer ID
     *
     * @param customerID customer identifier
     * @return Boolean true if appointments are associated
     * @throws SQLException
     */
    public static boolean checkForAssociatedAppointments(int customerID) throws SQLException {
        Connection conn = JDBC.getConnection();
        String selectStatement = "SELECT Customer_ID FROM client_schedule.appointments;";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();
        while (rs.next()) {
            //var ID holds the database ID for comparison
            int comparisonID = rs.getInt("Customer_ID");
            if (customerID == comparisonID) {
                isAssociated = true;
                return isAssociated;
            } else {
                isAssociated = false;
            }
        }
        return isAssociated;
    }


    /**
     * This variable identifies if two appointments overlap. It is used by checkForConflicts() method
     */
    public static boolean isOverlap;



    /**
     * This method identifies if there any conflicting appointment times. If it returns true, there is an overlapping appointment
     *
     * @return if there is an overlapping appointment
     */
    public static boolean checkForConflicts(int appointmentID, Timestamp startTime, Timestamp endTime) throws SQLException {
        Connection conn = JDBC.getConnection();
        String selectStatement = "SELECT Appointment_ID, Start, End  FROM client_schedule.appointments;";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();
        while (rs.next()) {
            isOverlap = false;
            LocalDateTime compStart = TimeConversion.ZonedDateTimeToTimeStamp(TimeConversion.convertToUTC(TimeConversion.timestampToUserZonedDateTime(rs.getTimestamp("Start")))).toLocalDateTime();
            LocalDateTime compEnd = TimeConversion.ZonedDateTimeToTimeStamp(TimeConversion.convertToUTC(TimeConversion.timestampToUserZonedDateTime(rs.getTimestamp("End")))).toLocalDateTime();
            int compID = rs.getInt("Appointment_ID");

            LocalDateTime start = startTime.toLocalDateTime();
            LocalDateTime end = endTime.toLocalDateTime();


            //check that you aren't comparing an appointment to itself
            if (compID == appointmentID) {
                isOverlap = false;
            }
            //both start at same time
            else if (start.isEqual(compStart)){
                isOverlap = true;
                return isOverlap;
            }
            //if new appointment time is after comparison time
            else if(start.isAfter(compStart) && (compEnd.isBefore(start) || compEnd.isEqual(start))) {
                isOverlap = false;
            }
            //if new appointment is before comparison time
            else if(start.isBefore(compStart) && (end.isBefore(compStart) || end.isEqual(compStart))) {
                isOverlap = false;
            }
            else{
                isOverlap = true;
                return isOverlap;
            }

        }
        return isOverlap;

    }
}