package Main;

import Utilities.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Locale;
import java.util.TimeZone;

/**
 * This is the main class, the program starts from here.
 * LAMBDA EXPRESSION 1: Controller.customerScreen lines 158, 191
 * LAMBDA EXPRESSION 2: Controller.Login lines 82,107
 */
public class Main extends Application {

    /**
     * This method loads the login page when the program launches
     * @param primaryStage primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("../View/Login.fxml"));
        primaryStage.setTitle("Welcome");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    /**
     * This method launches the program. This method also holds code to identify the user's current location and language settings
     * @param args args
     * @throws SQLException
     * @throws IOException
     */
    public static void main(String[] args) throws SQLException, IOException {
        JDBC.openConnection();

        //establish local language
        if(Locale.getDefault().getLanguage().equals("fr") ) {
            Language.setLocalLanguageIsFrench(true);
        }
        else {
            Language.setLocalLanguageIsFrench(false);
        }

        //determine user's location
        TimeZone timeZone = TimeZone.getDefault();
        Location.timeZone = timeZone.getID();


        launch(args);
        JDBC.closeConnection();
    }
}