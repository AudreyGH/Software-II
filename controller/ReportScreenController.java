package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.*;
import model.Appointments;
import model.Contacts;
import model.Users;
import model.reportTypeMonth;
import utils.DBConnection;
import static utils.DBConnection.closeConnection;

/**
 * FXML controller class for reports screen.
 *
 * @author Audrey Hababag
 */
public class ReportScreenController implements Initializable
{
    Stage stage;
    Parent scene;
    private static Connection conn = DBConnection.getConnection();
    ObservableList<reportTypeMonth> reportList = FXCollections.observableArrayList();
    ObservableList<Contacts> contactListReport = FXCollections.observableArrayList();
    ObservableList<Appointments> AppListReport = FXCollections.observableArrayList();
    Users userNow;

    @FXML
    private TableView<reportTypeMonth> typeMonthTable;
    @FXML
    private TableColumn<reportTypeMonth, String> reportTypeColumn;
    @FXML
    private TableColumn<reportTypeMonth, String> monthColumn;
    @FXML
    private TableColumn<reportTypeMonth, Integer> totalColumn;
    @FXML
    private ComboBox<String> contactComboBox;
    @FXML
    private TableView<Appointments> ContactTable;
    @FXML
    private TableColumn<Appointments, Integer> apptColumn;
    @FXML
    private TableColumn<Appointments, String> titleColumn;
    @FXML
    private TableColumn<Appointments, String> typeColumn;
    @FXML
    private TableColumn<Appointments, String> descColumn;
    @FXML
    private TableColumn<Appointments, LocalDateTime> startColumn;
    @FXML
    private TableColumn<Appointments, LocalDateTime> endColumn;
    @FXML
    private TableColumn<Appointments, Integer> custIdColumn;
    @FXML
    private TableColumn<Appointments, String> customerColumn;
    @FXML
    private PieChart customerPie;

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
     * Directs the user to the main menu.
     * <p>
     * The throws clause on the method header handles the IOException that is
     * highlighted by the editor at the FXMLLoader.load line.
     *
     * @param event when the user clicks on the Add button
     * @throws IOException if not caught or declared
     */
    @FXML
    void onActionMainReturn(ActionEvent event) throws IOException
    {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        /* The line below this comment becomes partially highlighed as an error
        if not declared */
        scene = FXMLLoader.load(getClass().getResource("/view/mainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * Initializes controller Class.
     *
     * @param url pointer to the resource
     * @param rb the resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // Type and month tab
        monthColumn.setCellValueFactory(new PropertyValueFactory<>("month"));
        reportTypeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

        // Contacts Schedule tab
        apptColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        custIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));

        populatetypeMonthTab();
        populateContactCombobox();
        populatecontactTab();
        populatePieTaB();
    }

    /**
     * Populates the first report tab table.
     * <p>
     * The table in the first tab is populated with columns month, type and
     * total. A prepared statement and SELECT query collect the column
     * information from the database. The "total"column is the total of each
     * type of appointments per month.
     * <p>
     * There are currently no planned updates or improvements for this method.
     * Perhaps an update will be considered if a more efficient way of writing
     * this method becomes available in the future.
     */
    private void populatetypeMonthTab()
    {
        try
        {
            PreparedStatement ps = conn.prepareStatement(
              "SELECT MONTHNAME(Start) AS Month, Type, "
              + "COUNT(Type) AS TOTAL "
              + "FROM appointments "
              + "GROUP BY Month, Type");

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                String month = rs.getString("Month");
                String type = rs.getString("Type");
                int total = rs.getInt("TOTAL");

                reportList.add(new reportTypeMonth(month, type, total));

            }
            typeMonthTable.setItems(reportList);
        }
        catch(SQLException e)

        {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("SQL Error!");
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Non-Sql error!");
        }
    }

    /**
     * Populates the second report tab table.
     * <p>
     * The table in the second tab is populated with columns that are identical
     * to the appointments table except for the contact name column. The contact
     * name column is contained within a combo box. The user can choose between
     * contacts, and the table will be populated with appointments associated
     * with the contact selected.
     * <p>
     * There are currently no planned updates or improvements for this method.
     * Perhaps an update will be considered if a more efficient way of writing
     * this method becomes available in the future.
     */
    private void populatecontactTab()
    {
        try
        {
            AppListReport.clear();
            // Get data from DB
            PreparedStatement ps = conn.prepareStatement(
              "SELECT * FROM appointments, customers, users, contacts "
              + "WHERE appointments.User_ID = users.User_ID "
              + "AND appointments.Contact_ID = contacts.Contact_ID "
              + "AND appointments.Customer_ID = customers.Customer_ID "
              + "ORDER BY Start");

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                // assign data from DB to variables that can be inserted to an observablelist
                int appointmentID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int customerID = rs.getInt("Customer_ID");
                String contactName = rs.getString("Contact_Name");
                String customerName = rs.getString("Customer_Name");

                // Other data not needed for table 
                LocalDateTime createdDate = rs.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                int contactID = rs.getInt("Contact_ID");
                String email = rs.getString("Email");

                int userID = userNow.getuserID();

                AppListReport.add(new Appointments(appointmentID, title, description,
                  location, type, start, end, createdDate, createdBy, lastUpdate,
                  lastUpdatedBy, customerID, userID, contactID, contactName, customerName));
            }

            // Set all appointments on the table
            ContactTable.setItems(AppListReport);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("SQL error!");
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Non-SQL error!");
        }

    }

    /**
     * Populates the contact combo box
     * <p>
     * Data is pulled from the database using a SELECT query.
     * <p>
     * I ran into a problem where the combo boxes are not returning to its
     * default state. By default, the combo boxes should only contain the prompt
     * text. But once a selection is made, the combo box does not return to
     * default. I was able to solve this problem with the help of @James_D at:
     * https://stackoverflow.com/a/50569582.
     */
    private void populateContactCombobox()
    {
        ObservableList<String> contactCombo = FXCollections.observableArrayList();
        try
        {
            PreparedStatement ps = conn.prepareStatement("SELECT * "
              + "FROM contacts");

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                int contactID = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String email = rs.getString("Email");

                contactCombo.add(rs.getString("Contact_Name"));

                contactListReport.add(new Contacts(contactID, contactName, email));
            }
            contactComboBox.setItems(contactCombo);

            /* Displays the prompt text when combobox is cleared. For some reason
            the prompt text does is not diplayed after clearing this combobox. Only
            two comboboxes have this problem */
            contactComboBox.setButtonCell(new ListCell<String>()
            {
                @Override
                protected void updateItem(String item, boolean empty)
                {
                    super.updateItem(item, empty);
                    if(empty || item == null)
                    {
                        setText("Select Contact");
                    }
                    else
                    {
                        setText(item);
                    }
                }
            });
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("SQL error!");
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Non-SQL error!");
        }

    }

    /**
     * Populates the second tab table filtered by contact name
     * <p>
     * Performs a SELECT query to filter appointments according to the contact
     * name and populates the table with the filtered list. The table is
     * populated when the user selects a contact from the combo box, and is
     * repopulated when the selection changes.
     * <p>
     * There are currently no planned updates or improvements for this method.
     * Perhaps an update will be considered if a more efficient way of writing
     * this method becomes available in the future.
     *
     * @param event when the user selects a contact from the combo box
     */
    @FXML
    void onActionContact(ActionEvent event)
    {
        try
        {
            AppListReport.clear();
            String selectedContact = contactComboBox.getValue();
            // Get data from DB
            PreparedStatement ps = conn.prepareStatement(
              "SELECT * FROM appointments, customers, users, contacts "
              + "WHERE appointments.User_ID = users.User_ID "
              + "AND appointments.Contact_ID = contacts.Contact_ID "
              + "AND appointments.Customer_ID = customers.Customer_ID AND "
              + "Contact_Name = ? "
              + "ORDER BY Start");

            ps.setString(1, selectedContact);
            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                // assign data from DB to variables that can be inserted to an observablelist
                int appointmentID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int customerID = rs.getInt("Customer_ID");
                String contactName = rs.getString("Contact_Name");
                String customerName = rs.getString("Customer_Name");

                // Other data not needed for table 
                LocalDateTime createdDate = rs.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                int contactID = rs.getInt("Contact_ID");
                String email = rs.getString("Email");

                int userID = userNow.getuserID();

                AppListReport.add(new Appointments(appointmentID, title, description,
                  location, type, start, end, createdDate, createdBy, lastUpdate,
                  lastUpdatedBy, customerID, userID, contactID, contactName, customerName));
            }

            // Set all appointments on the table
            ContactTable.setItems(AppListReport);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("SQL error!");
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Non-SQL error!");
        }
    }

    /**
     * Populates the third tab pie chart
     * <p>
     * The pie chart represents the total number of customers per day. The
     * numbers are not included. The chart only serves as a visual
     * representation of the raw data. The chart serves to give the user an idea
     * of the busiest and the slowest days.
     * <p>
     * I had wanted to include the number of customers in the chart, but I could
     * not figure out how to do so. This is definitely a candidate for future
     * patches.
     */
    private void populatePieTaB()
    {
        try
        {
            ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
            ArrayList<String> day = new ArrayList<String>();
            ArrayList<Integer> total = new ArrayList<Integer>();

            PreparedStatement ps = conn.prepareStatement(
              "SELECT DAYNAME(Start) AS Days, COUNT(*) AS Total "
              + "FROM appointments "
              + "GROUP BY Days ");

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                pieData.add(new PieChart.Data(rs.getString("Days"),
                  rs.getInt("Total")));

                // pie chart data
                day.add(rs.getString("Days"));
                total.add(rs.getInt("Total"));

            }

            customerPie.setData(pieData);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("SQL Error!");
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Non-Sql error!");
        }
    }
}
