package Main;

import java.sql.SQLException;

/**
 * This interface holds lambda expressions related to updating the customer table
 */
public interface updateInterface {

    /**
     *  LAMBDA JUSTIFICATION: in this case, the lambda expression is used to populate variables and help complete the update customer process.
     *  This lambda expression improves the code by eliminating the need for a static input variable, making use of an anonymous variable.
     *   It also improves readability and clarity for anyone reading the code
     * @param customerID
     * @throws SQLException
     */
    void populateCustomerUpdateTable(int customerID) throws SQLException;

}
