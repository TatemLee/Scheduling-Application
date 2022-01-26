package Controller;

import Main.*;
import Utilities.DBQuery;
import Utilities.JDBC;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.*;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ResourceBundle;

public class Login implements Initializable {
    public Button loginButton;
    public TextField passwordTextBox;
    public TextField userIdTextBox;
    public Label passwordLabel;
    public Label userIDLabel;
    public Label currentUserLocation;
    public Label loginHeader;
    public Label currentUserLocationLabel;

    /**
     * This method initializes the starting values for the login page
     * @param url url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDefaults();
    }

    /**
     * This method sets up default text for the login page.
     */
    public void setDefaults() {
        this.currentUserLocation.setText(Location.timeZone);
        //check language and translate
       if(Language.getLocalLanguageIsFrench()) {
            this.userIDLabel.setText("Nom d'utilisateur");
            this.passwordLabel.setText("Mot de passe");
            this.loginHeader.setText("Connexion");
            this.loginButton.setText("Connexion");
            this.currentUserLocationLabel.setText("Emplacement actuel de l'utilisateur:");
        }
    }



    //!!!!!!!!!!!!!!!!!!!!!!!!!! LAMBDA EXPRESSION 2 !!!!!!!!!!!!!!!!!!!!!!!

    /**
     This method activates the login sequence and validates user input.
     * @param actionEvent
     * @throws SQLException
     * @throws IOException
     */
    public void onLogin(ActionEvent actionEvent) throws SQLException, IOException {
        //retrieve login values entered by user
        Validation.userName = userIdTextBox.getText();
        Validation.password = passwordTextBox.getText();

        //lambda expression definition
        validationInterface validation = (username, password)-> {
            Connection conn = JDBC.getConnection();
            String selectStatement = "SELECT User_ID, User_Name, Password FROM users";
            DBQuery.setPreparedStatement(conn, selectStatement);
            PreparedStatement ps = DBQuery.getPreparedStatement();
            ps.execute();
            ResultSet rs = ps.getResultSet();

            while(rs.next()) {
                int userID = rs.getInt("User_ID");
                String compName = rs.getString("User_Name");
                String compPass = rs.getString("Password");

                if(compName.equals(Validation.userName) &&  compPass.equals(Validation.password)) {
                    Validation.userID = userID;
                    return true;
                }
            }
            return false;
        };

        //check input
      //if(DBQuery.verifyLoginInformation()) {
        if(validation.verifyLoginInformation(Validation.userName, Validation.password)) {
          FileWriter fileWriteSuccess = new FileWriter("login_activity.txt", true);
          PrintWriter logWriterSuccess = new PrintWriter(fileWriteSuccess);
          logWriterSuccess.println("User: " + Validation.userName + " - Successful login at: " + Timestamp.from(Instant.now()) + " " + ZoneId.systemDefault());
          logWriterSuccess.close();
          //loads next page
          Parent root = (Parent)FXMLLoader.load(this.getClass().getResource("/view/Home.fxml"));
          Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
          Scene scene = new Scene(root, 405.0D, 165.0D);
          stage.setTitle("Sunset Schedules - Home Screen");
          stage.setScene(scene);
          stage.show();
      }
      else {
          FileWriter fileWriteFailure = new FileWriter("login_activity.txt", true);
          PrintWriter logWriterFailure = new PrintWriter(fileWriteFailure);
          logWriterFailure.println("User: " + Validation.userName + " - Invalid login attempt at: " + Timestamp.from(Instant.now()) + " " + ZoneId.systemDefault());
          logWriterFailure.close();
          Message.invalidLogin();
      }
    }


}
