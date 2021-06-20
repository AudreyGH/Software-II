package schedapp;

import java.sql.SQLException;
import java.util.Locale;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.DBConnection;

/**
 * The Main File
 *
 * @author Audrey Hababag
 */
public class SchedApp extends Application
{
    /**
     * Main
     * <p>
     * The throws clause on the method header handles the SQLException that is
     * highlighted by the editor at the closeConnection() line .
     *
     * @param args the command line arguments
     * @throws SQLException closeConnection() if not called or declared
     */
    public static void main(String[] args) throws SQLException
    {
        // Remove slashes to set French locale
        //Locale.setDefault(new Locale("fr"));
        
        DBConnection.startConnection();
        launch(args);
        DBConnection.closeConnection();
    }

    /**
     * Directs the user to the login screen.
     * <p>
     * The throws clause on the method header handles the IOException that is
     * highlighted by the editor at the FXMLLoader.load line.
     *
     * @param stage the stage
     * @throws Exception highlights line if not caught or declared to be thrown
     */
    @Override
    public void start(Stage stage) throws Exception
    {
        // Load the FXML file. 
        Parent root = FXMLLoader.load(getClass().getResource("/view/loginForm.fxml"));

        // Build the Scene and display 
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Main Menu");
        stage.show();

    }
}
