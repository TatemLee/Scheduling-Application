package Main;

import java.sql.SQLException;

/**
 * This interface lambda expressions  used to validate userID/Password combos
 */
public interface validationInterface {
   /**
    *  LAMBDA JUSTIFICATION: This lambda expression is used to compare the values of the user input username/password with the database's values.
    *  This lambda expression improves the code by reducing the overall lines of code - because this is a single use block of code,
    *  writing it using a lambda expression uses less lines than creating the method in another Class and calling it here.
    *  Additionally, it improves clarity by including the validation code expression in the same place as the validation page code,
    *  making it easier to understand for any future programmer reading it.
    * @param username input username
    * @param password input password
    * @return Boolean login attempt result
    * @throws SQLException
    */
   boolean verifyLoginInformation(String username, String password) throws SQLException;
}

