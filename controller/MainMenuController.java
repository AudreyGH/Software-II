package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;
import static utils.DBConnection.closeConnection;

/**
 * FXML controller class for MainMenu screen.
 *
 * @author Audrey Hababag
 */
public class MainMenuController implements Initializable
{
    Stage stage;
    Parent scene;

    /**
     * Directs the user to the Appointments Screen
     * <p>
     * The throws clause on the method header handles the IOException that is
     * highlighted by the editor at the FXMLLoader.load line.
     *
     * @param event when the user clicks the Appointments button
     * @throws IOException highlights line if not caught or declared to be
     * thrown
     */
    @FXML
    void onActionAppointments(ActionEvent event) throws IOException
    {
        try
        {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

            /* The line below this comment becomes partially highlighed as an error
            if not declared */
            scene = FXMLLoader.load(getClass().getResource("/view/appointmentScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.centerOnScreen();
            stage.show();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Directs the user to the Customer Screen
     * <p>
     * The throws clause on the method header handles the IOException that is
     * highlighted by the editor at the FXMLLoader.load line.
     *
     * @param event when the user clicks the Customer Records button
     * @throws IOException highlights line if not caught or declared to be
     * thrown
     */
    @FXML
    void onActionCustomer(ActionEvent event) throws IOException
    {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        /* The line below this comment becomes partially highlighed as an error
        if not declared */
        scene = FXMLLoader.load(getClass().getResource("/view/customerScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * Directs the user to the Report Screen
     * <p>
     * The throws clause on the method header handles the IOException that is
     * highlighted by the editor at the FXMLLoader.load line.
     *
     * @param event when the user clicks the Reports button
     * @throws IOException highlights line if not caught or declared to be
     * thrown
     */
    @FXML
    void onActionReports(ActionEvent event) throws IOException
    {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        /* The line below this comment becomes partially highlighed as an error
        if not declared */
        scene = FXMLLoader.load(getClass().getResource("/view/reportScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();

    }

    /**
     * Exits the application.
     * <p>
     * The throws clause on the method header handles the SQLException that is
     * highlighted by the editor at the closeConnection() line .
     *
     * @param event when the user clicks on "Exit Program"
     * @throws SQLException if not called or declared
     */
    @FXML
    void onActionExit(ActionEvent event) throws SQLException
    {
        closeConnection();
        System.exit(0);
    }

    /**
     * Initializes the main menu.
     *
     * @param url pointer to the resource
     * @param rb the resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }

}
