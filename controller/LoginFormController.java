package controller;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;
import model.Appointments;
import model.Users;
import utils.DBConnection;
import static utils.DBConnection.closeConnection;

/**
 * FXML controller class for LoginForm screen.
 *
 * @author Audrey Hababag
 */
public class LoginFormController implements Initializable
{
    Stage stage;
    Parent scene;
    Users userNow;
    private boolean loginAttempt = false;
    private static Connection conn = (Connection) DBConnection.getConnection();
    private ObservableList<Appointments> alertList = FXCollections.observableArrayList();
    DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("dd MMM'.' yyyy 'at' HH:mm'.'");
    DateTimeFormatter logFormat = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm");
    @FXML
    private Button signIn;
    @FXML
    private Button Exit;
    @FXML
    private Label welcomeLabel;
    @FXML
    private Label idLabel;
    @FXML
    private TextField loginID;
    @FXML
    private Label pwordLabel;
    @FXML
    private TextField loginPword;
    @FXML
    private Label countryLabel;
    @FXML
    private Label locationLabel;

    /**
     * Initializes controller Class.
     *
     * @param url pointer to the resource
     * @param rb the resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        //Remove forward slashes to fill login and password automatically
        loginID.setText("1");
        loginPword.setText("test");
        

        rb = ResourceBundle.getBundle("schedapp/loginForm", Locale.getDefault());
        welcomeLabel.setText(rb.getString("Title"));
        idLabel.setText(rb.getString("UserID"));
        loginID.setPromptText(rb.getString("UserID"));
        pwordLabel.setText(rb.getString("PassWord"));
        loginPword.setPromptText(rb.getString("PassWord"));
        countryLabel.setText(rb.getString("Country"));
        signIn.setText(rb.getString("SignIn"));
        Exit.setText(rb.getString("Exit"));
        locationLabel.setText(rb.getString("Locale"));
    }

    /**
     * Accepts a user ID and password and provides an appropriate error message.
     * <p>
     * The user ID and password is matched with the database using a prepared
     * statement and a SELECT query. An error message is provided if no match is
     * found. An alert is provided if the User ID is not an integer. An alert is
     * provided if any of the fields are left blank.
     * <p>
     * There are currently no planned updates or improvements for this method.
     * Perhaps an update will be considered if a more efficient way of writing
     * this method becomes available in the future.
     * <p>
     * The throws clause on the method header handles the IOException that is
     * highlighted by the editor at the FXMLLoader.load line.
     *
     * @param event when the user actuates Enter key or presses the "Sign In"
     * Button
     * @throws IOException highlights line if not caught or declared to be
     * thrown
     */
    @FXML
    void onActionSignIn(ActionEvent event) throws IOException
    {
        try
        {
            ResourceBundle rb = ResourceBundle.getBundle("schedapp/loginForm", Locale.getDefault());

            // Alert if any fields are empty
            if(loginID.getText().trim().isBlank() || loginPword.getText().trim().isBlank())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(rb.getString("setTitle1"));
                alert.setHeaderText(rb.getString("setHeader1"));
                alert.setContentText(rb.getString("setContext1"));
                alert.showAndWait();
            }

            // Alert if user ID is not an integer
            if(!loginID.getText().trim().isBlank() && !loginPword.getText().trim().isBlank())
            {
                if(!isInteger(loginID.getText().trim()))
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle(rb.getString("setTitle1"));
                    alert.setHeaderText(rb.getString("setHeader2"));
                    alert.showAndWait();
                    return;
                }
                
                if(isInteger(loginPword.getText().trim()))
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle(rb.getString("setTitle1"));
                    alert.setHeaderText(rb.getString("setHeader7"));
                    alert.showAndWait();
                    return;
                }
                
                int userID = Integer.parseInt(loginID.getText().trim());
                String passText = loginPword.getText().trim();

                //  Match user ID and Password with database
                PreparedStatement ps = conn.prepareStatement(
                  "SELECT * FROM users WHERE User_ID = ? AND Password = ?");

                ps.setInt(1, userID);
                ps.setString(2, passText);

                ResultSet rs = ps.executeQuery();
                if(!rs.next())
                {
                    loginAttempt = false;
                    loginAttempts(loginAttempt);

                    // Alert if user ID or password are incorrect.
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle(rb.getString("setTitle1"));
                    alert.setHeaderText(rb.getString("setHeader3"));
                    alert.showAndWait();
                }
                else
                {
                    do
                    {
                        {
                            userNow.setuserID(rs.getInt("User_ID"));
                            userNow.setPassword(rs.getString("Password"));
                            userNow.setuserName(rs.getString("User_Name"));

                            apptAlert();
                            loginAttempt = true;
                            loginAttempts(loginAttempt);

                            // Go to Main Menu
                            /* The line below this comment becomes partially highlighed as an error
                            if not declared */
                            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                            scene = FXMLLoader.load(getClass().getResource("/view/mainMenu.fxml"));
                            stage.setScene(new Scene(scene));
                            stage.centerOnScreen();
                            stage.show();
                        }
                    }
                    while(rs.next());
                }
            }
        }
        catch(SQLException e)
        {
            ResourceBundle rb = ResourceBundle.getBundle("schedapp/loginForm", Locale.getDefault());
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println(rb.getString("sqlException"));
        }
        catch(Exception e)
        {
            
            ResourceBundle rb = ResourceBundle.getBundle("schedapp/loginForm", Locale.getDefault());
            System.out.println(e.getMessage());
            System.out.println(rb.getString("exceptionMessage"));
             
            /*
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("setTitle1"));
            alert.setHeaderText(rb.getString("setHeader6"));
            alert.showAndWait();
            */
        }
    }

    /**
     * Provides an alert for upcoming appointments.
     * <p>
     * A SELECT query finds all appointments that are beginning within 15
     * minutes of user login. Appointments that have already begun are ignored.
     * An information dialogue consisting of the appointment ID and type is
     * provided for each appointment that exists within the time frame. An
     * information dialogue is provided if no appointments are found.
     * <p>
     * There are currently no planned updates or improvements for this method.
     * Perhaps an update will be considered if a more efficient way of writing
     * this method becomes available in the future.
     */
    private void apptAlert()
    {
        try
        {
            ResourceBundle rb = ResourceBundle.getBundle("schedapp/loginForm", Locale.getDefault());
            
            // Select appoinments within 15 minutes of current time
            PreparedStatement ps = conn.prepareStatement(
              "SELECT * FROM appointments WHERE Start BETWEEN NOW() AND "
                + "ADDDATE(NOW(), INTERVAL 15 MINUTE)");

            ResultSet rs = ps.executeQuery();

            if(!rs.next())
            {
                // Alert if no appoinments exist
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setTitle(rb.getString("setTitle2"));
                alert2.setHeaderText(rb.getString("setHeader4"));
                alert2.showAndWait();
            }
            else
            {
                do
                {  
                    // Alert for appointments found
                    int appointmentID = rs.getInt("Appointment_ID");
                    LocalDateTime alertDate = rs.getTimestamp("Start").toLocalDateTime();

                    alertList.add(new Appointments(appointmentID, alertDate));

                    System.out.println("Appointment ID: " + appointmentID);
                    System.out.println("Time: " + alertDate + "\n");

                    //LocalDateTime timeNow = LocalDateTime.now();
                    //LocalDateTime time15 = LocalDateTime.now().plusMinutes(15);
                    String formatted = alertDate.format(dtFormat);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle(rb.getString("setTitle2"));
                    alert.setHeaderText(rb.getString("setHeader5"));
                    alert.setContentText(rb.getString("setContext2") + appointmentID
                      + " " + rb.getString("setContext2_1") + " " + formatted);
                    alert.showAndWait();
                }
                while(rs.next());
            }
        }
        catch(SQLException e)
        {
            ResourceBundle rb = ResourceBundle.getBundle("schedapp/loginForm", Locale.getDefault());
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println(rb.getString("sqlException"));
        }
        catch(Exception e)
        {
            ResourceBundle rb = ResourceBundle.getBundle("schedapp/loginForm", Locale.getDefault());
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println(rb.getString("exceptionMessage"));
        }
    }

    /**
     * Exits the application.
     * <p>
     * The throws clause on the method header handles the SQLException that is
     * highlighted by the editor at the closeConnection() line .
     *
     * @param event when the user clicks on "Exit"
     * @throws SQLException if not called or declared
     */
    @FXML
    void onActionExit(ActionEvent event) throws SQLException
    {
        closeConnection();
        System.exit(0);
    }

    /**
     * Checks if the input is an integer.
     *
     * @param input the text being checked
     * @return returns true or false
     */
    private boolean isInteger(String input)
    {
        try
        {
            Integer.parseInt(input);
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    /**
     * Tracks user login activity. Login attempts are saved to
     * login_activity.txt. The text file keeps a record of timestamps, username,
     * and whether an attempt to log in was a success or a failure. All
     * successful logins are saved. Failed attempts that involve invalid formats
     * and blank fields are not logged. Data will only be saved when both the
     * User ID and Password fields are filled with correct formats--int for ID
     * and String for password.
     * <p>
     * There's a minor quirk in the saved text when the locale is set to french.
     * For some odd reason, the month on the timestamp prints in lowercase
     * followed by a period (-oct.-) instead of the intended format of
     * capitalized first letter, and ending wihtout a period (-Oct-).
     *
     * @param login the login ID
     * @throws IOException when the file cannot be found
     */
    private void loginAttempts(boolean login) throws IOException
    {
        try
        {
            ResourceBundle rb = ResourceBundle.getBundle("schedapp/loginForm", Locale.getDefault());

            // Create a file if it doesn't exists. Write into file it it exists
            FileWriter fileName = new FileWriter("login_activity.txt", true);
            BufferedWriter loginAttempt = new BufferedWriter(fileName);

            // Texts saved to file
            loginAttempt.append(LocalDateTime.now().format(logFormat) + " User: "
              + userNow.getuserName() + " Successful: " + login);
            loginAttempt.newLine();
            loginAttempt.close();
            System.out.println("\n" + rb.getString("println1"));
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }

    }
}
