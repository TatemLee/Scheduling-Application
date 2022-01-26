package Utilities;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * This class handles the connection between the program and the database
 */
public abstract class JDBC {

    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = UTC"; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = "sqlUser"; // Username
    private static String password = "Passw0rd!"; // Password
    public static Connection connection;  // Connection Interface


    /**
     * This method connects the program to the database
     */
    public static void openConnection()
    {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(jdbcUrl, userName, password);

        }
        catch(Exception e)
        {
         System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * This method closes the program's database connection
     */
    public static void closeConnection() {
        try {
            connection.close();
        }
        catch(Exception e)
        {
         //ignore
        }
    }

    /**
     * This method references the database connection made with openConnection()
     * @return Connection
     */
    public static Connection getConnection() {
            return connection;
    }



}
