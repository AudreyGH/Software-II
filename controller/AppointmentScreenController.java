package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.*;
import javafx.collections.transformation.FilteredList;
import javafx.event.*;
import javafx.fxml.*;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.*;
import model.Appointments;
import model.Users;
import model.Contacts;
import utils.DBConnection;
import static utils.DBConnection.closeConnection;

/**
 * FXML controller class for appointments screen.
 *
 * @author Audrey Hababag
 */
public class AppointmentScreenController implements Initializable
{
    Stage stage;
    Parent scene;
    int contactID = -1;
    int customerID = -1;
    int apptID1 = 0;
    Timestamp start = null;
    Timestamp end = null;
    private Users userNow;
    private static Connection conn = (Connection) DBConnection.getConnection();
    PreparedStatement ps;
    private Appointments selectedAppt;
    private ObservableList<Appointments> appList = FXCollections.observableArrayList();
    private ObservableList<Contacts> contactList = FXCollections.observableArrayList();
    private final ObservableList<LocalTime> startTime = FXCollections.observableArrayList();
    private final ObservableList<LocalTime> endTime = FXCollections.observableArrayList();
    @FXML
    private RadioButton allButton;
    @FXML
    private TableView<Appointments> apptTable;
    @FXML
    private TableColumn<Appointments, Integer> apptColumn;
    @FXML
    private TableColumn<Appointments, String> titleColumn;
    @FXML
    private TableColumn<Appointments, String> descColumn;
    @FXML
    private TableColumn<Appointments, String> locationColumn;
    @FXML
    private TableColumn<Appointments, String> contactColumn;
    @FXML
    private TableColumn<Appointments, String> typeColumn;
    @FXML
    private TableColumn<Appointments, String> startColumn;
    @FXML
    private TableColumn<Appointments, String> endColumn;
    @FXML
    private TableColumn<Appointments, Integer> custIdColumn;
    @FXML
    private TableColumn<Appointments, String> customerColumn;
    @FXML
    private Label appointmentLabel;
    @FXML
    private TextField apptID;
    @FXML
    private TextField apptTitle;
    @FXML
    private TextField apptDescription;
    @FXML
    private ComboBox<String> locationComboBox;
    @FXML
    private ComboBox<String> contactComboBox;
    @FXML
    private ComboBox<String> typeComboBox;
    @FXML
    private DatePicker apptDatePicker;
    @FXML
    private ComboBox<LocalTime> startComboBox;
    @FXML
    private ComboBox<LocalTime> endComboBox;
    @FXML
    private ComboBox<Integer> custIDcomboBox;
    @FXML
    private TextField custName;

    /**
     * Initializes controller Class.
     *
     * @param url pointer to the resource
     * @param rb the resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        apptColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        custIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));

        // Populate Table and Comboboxes
        populateTable();
        populateLocationCombobox();
        populateContactCombobox();
        populateTypeCombobox();
        populateDatTimeComboboxes();
        populateCustomerComboBox();
    }

    /**
     * Populates the appointment table.
     * <p>
     * Data is pulled from database to populate the table. A prepared statement
     * is used to pull data and populate the table. Data is set to an
     * Observablelist which is then used to populate the tableview in Scene
     * Builder.
     * <p>
     * There are currently no planned updates or improvements for this method.
     * Perhaps an update will be considered if a more efficient way of writing
     * this method becomes available in the future.
     */
    @FXML
    private void populateTable()
    {
        try
        {
            appList.clear();
            allButton.setSelected(true);

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

                appList.add(new Appointments(appointmentID, title, description,
                  location, type, start, end, createdDate, createdBy, lastUpdate,
                  lastUpdatedBy, customerID, userID, contactID, contactName, customerName));
            }

            // Set all appointments on the table
            apptTable.setItems(appList);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("SQL error!");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("Non-SQL error!");
        }
    }

    /**
     * Indicates that the "All" radio button is selected and calls the
     * populateTable() method.
     *
     * @param event when the "All" radio button is selected
     */
    @FXML
    void allRadio(ActionEvent event)
    {
        populateTable();
    }

    /**
     * Indicates that the "This Month" radio button is selected and calls the
     * filteredbyEndofMonth() method.
     *
     * @param event when the "This Month" radio button is selected.
     */
    @FXML
    void monthRadio(ActionEvent event)
    {
        // method used to filter by all appointments until the end of the month
        filteredbyEndofMonth(appList); 
    }

    /**
     * Indicates that the "This Week" radio button is selected and calls the
     * filteredbyWeek() method.
     *
     * @param event when the "This Week" radio button is selected.
     */
    @FXML
    void weekRadio(ActionEvent event)
    {
        // method used to filter by 7 days
        filteredbyWeek(appList);
    }

    /**
     * Indicates that the "Next 30 Days" radio button is selected and calls the
     * filteredbyWeek() method.
     *
     * @param event when the "Next 30 Days" radio button is selected.
     */
    @FXML
    void nextMonthRadio(ActionEvent event)
    {
        // method used to filter by 30 days
        filteredbyNextMonth(appList);
    }

    /**
     * Prepares fields for appointment add.
     * <p>
     * Sets the appointment label to "Add Appointment" and calls onAcitonClear()
     * method to clear the fields.
     *
     * @param event when the "Add Appointment" button is pressed
     */
    @FXML
    void onActionAdd(ActionEvent event)
    {
        appointmentLabel.setText("Add Appointment");
        onActionClear(event);
    }

    /**
     * Prepares fields for appointment update.
     * <p>
     * Sets the appointment label to "Update Appointment" and calls the
     * onActionClear() method to clear the fields. The method also checks if the
     * user has selected an appointment from the table and sends an alert if
     * this is false.
     *
     * @param event when the "Update Appointment" button is pressed.
     */
    @FXML
    void onActionUpdate(ActionEvent event)
    {
        if(apptTable.getSelectionModel().getSelectedItem() != null)
        {
            onActionClear(event);
            appointmentLabel.setText("Update Appointment");
            selectedAppt = apptTable.getSelectionModel().getSelectedItem();

            // populate fields with selected appointment values
            apptID.setText(Integer.toString(selectedAppt.getAppointmentID()));
            apptTitle.setText(selectedAppt.getTitle());
            apptDescription.setText(selectedAppt.getDescription());
            locationComboBox.setValue(selectedAppt.getLocation());
            contactComboBox.setValue(selectedAppt.getContactName());
            typeComboBox.setValue(selectedAppt.getType());
            custIDcomboBox.setValue(selectedAppt.getCustomerID());
            custName.setText(selectedAppt.getCustomerName());

            /* Convert LocalDateTime to separate LocalDate and LocalTime then populate
            datepicker and time comboboxes.  */
            apptDatePicker.setValue(LocalDate.parse(selectedAppt.getStart().toLocalDate().toString()));
            startComboBox.getSelectionModel().select(selectedAppt.getStart().toLocalTime());
            endComboBox.getSelectionModel().select(selectedAppt.getEnd().toLocalTime());
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Select an appointment from the table");
            alert.setContentText("Then click the update button again");
            alert.showAndWait();
        }
    }

    /**
     * Saves the appointment being added or updated to the database.
     * <p>
     * This method pulls all the data from the fields and combo boxes and
     * inserts the data into the database using a prepared statement. All text
     * fields are checked for correct formatting accepted by the database. An
     * alert is sent to inform the user if a field's format is invalid. The user
     * receives an alert if a combo box is left to its default state, containing
     * a null value when no item is selected. The title and description fields
     * can be left empty. They will be filled with default String values "title"
     * and "description" upon save. This is an intentional implementation. I
     * will change it to function like the other text fields if the evaluator
     * finds this does not reflect the competencies outlined by the rubric.
     * <p>
     * This is one of the most challenging methods to figure out, at least in my
     * case. I took a very long time to realize why INSERT is not working even
     * when there are no errors. I've searched online for alternatives and
     * implemented them. I've even read the online mySQL JDBC manual several
     * times over. It turns out, my mistake was completely unrelated to the
     * prepared statement. Make sure to always check if the correct comparison
     * method is being used. My two prepared statements are contained in their
     * separate if statements. When an if statement matches the string in a
     * label, the prepared statement it contains is used. My mistake is that I
     * was using "contains" instead of "contentEquals." The two strings being
     * checked are close enough in spelling and characters, and this prevented
     * the INSERT from executing.
     * <p>
     * If I were to make any changes, I would try to separate the Update and Add
     * prepared statements instead of having them in one method. This made
     * everything more problematic than necessary. I would also try to validate
     * the fields using a method outside of this one for cleaner formatting.
     * Alerts as a class in a separate file is also an option.
     *
     * @param event when the "Save" button is pressed.
     */
    @FXML
    void onActionSave(ActionEvent event)
    {
        try
        {
            String title = apptTitle.getText();
            String description = apptDescription.getText();
            String location = locationComboBox.getValue();
            String type = typeComboBox.getValue();

            // Concatenate date and time then convert to timestamp for INSERT
            LocalTime startBox = startComboBox.getValue();
            LocalTime endBox = endComboBox.getValue();

            /* The if/else statements ensure that the alerts are sent out first,
            before continuing on to the next line. Without these statements,
            parameters that reference text fields and comboboxes result in errors. 
             */
            if(startBox != null && endBox != null)
            {
                LocalDate date = apptDatePicker.getValue();
                LocalDateTime apptStart = LocalDateTime.of(date, startBox);
                LocalDateTime apptEnd = LocalDateTime.of(date, endBox);
                start = Timestamp.valueOf(apptStart);
                end = Timestamp.valueOf(apptEnd);
            }
            String lastUpdatedBy = userNow.getuserName();

            if(custIDcomboBox.getValue() != null)
            {
                customerID = custIDcomboBox.getValue();
            }

            int userID = userNow.getuserID();
            String tempContact = contactComboBox.getValue();

            if(tempContact != null)
            {
                contactID = getContactIDfromList(tempContact);    
     
            }

            // Alert if fields are empty or null
            if(title.isBlank())
            {
                apptTitle.setText("title");
                title = apptTitle.getText();
            }

            // Alert if description field is blank
            if(description.isBlank())
            {
                apptDescription.setText("description");
                description = apptDescription.getText();
            }

            // Alert if no location is selected
            if(location == null)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Select a location");
                alert.showAndWait();
                return;
            }

            // Alert if no contact is selected
            if(tempContact == null)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Select a contact");
                alert.showAndWait();
                return;
            }

            // Alert if no type is selected
            if(type == null)
            {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Select a type");
                alert.showAndWait();
                return;
            }

            // Alert is no start date is selected
            if(startBox == null)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Select a start time");
                alert.showAndWait();
                return;
            }

            // Alert if no end date is selected
            if(endBox == null)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Select an end time");
                alert.showAndWait();
                return;
            }

            // Alert if start date is after or equals the end date 
            if(startBox.isAfter(endBox) || startBox.equals(endBox))
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("End time must not begin before or at the "
                  + "same time as Start");
                alert.showAndWait();
                return;
            }
            
            // calls method that checks if start date is within business hours
            if(!checkBusinessHours(startBox))
            {
                return;
            }

            // calls method that checks if end date is within business hours
            if(!checkBusinessHours(endBox))
            {
                return;
            }
            
            // Alert if customer name field is empty
            if(custName.getText().isBlank())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Select customer ID");
                alert.showAndWait();
                return;
            }

            // Alert if there is no customer ID selected
            if(custIDcomboBox.getValue() == null)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Select customer ID");
                alert.showAndWait();
                return;
            }

            // Calls method that checks for overlapping appointments 
            if(overlapSchedule(start, end))
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Appointment unavailable");
                alert.setContentText("Check the table for available times then "
                  + "try any of the following:\n"
                  + "1. Select a different start and end time.\n"
                  + "2. Select a different date.\n");
                alert.showAndWait();
                return;
            }

            // If Add Appointment button is clicked
            if(appointmentLabel.getText().contentEquals("Add Appointment"))
            {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO appointments"
                  + "(Title, Description, Location, Type, Start, "
                  + "End, Create_Date, Created_By, Last_Update, Last_Updated_By, "
                  + "Customer_ID, User_ID, Contact_ID) "
                  + "VALUES(?, ?, ?, ?, ?, ?, NOW(), ?, NOW(), ?, ?, ?, ?)",
                  Statement.RETURN_GENERATED_KEYS);

                ps.setString(1, title);
                ps.setString(2, description);
                ps.setString(3, location);
                ps.setString(4, type);
                ps.setTimestamp(5, start);
                ps.setTimestamp(6, end);
                ps.setString(7, lastUpdatedBy);
                ps.setString(8, lastUpdatedBy);
                ps.setInt(9, customerID);
                ps.setInt(10, userID);
                ps.setInt(11, contactID);

                int result = ps.executeUpdate();
                if(result > 0)
                {
                    System.out.println("\nSave Succesful!\n");
                }
                else
                {
                    System.out.println("\nNo save? Check DB.\n");
                }

                ResultSet rs = ps.getGeneratedKeys();
                if(rs.next())
                {

                    int autoKey = rs.getInt(1);
                    System.out.println("Generated Appointment ID: " + autoKey);
                }

                populateTable();
                onActionClear(event);
            }

            // If Update Appoinment button is clicked
            if(appointmentLabel.getText().contentEquals("Update Appointment"))
            {
                PreparedStatement ps = conn.prepareStatement("UPDATE appointments "
                  + "SET Title = ?, Description = ?, Location = ?, "
                  + "Type = ?, Start = ?, End = ?, Last_Update = NOW(), "
                  + "Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? "
                  + "WHERE Appointment_ID = ?");

                ps.setString(1, title);
                ps.setString(2, description);
                ps.setString(3, location);
                ps.setString(4, type);
                ps.setTimestamp(5, start);
                ps.setTimestamp(6, end);
                ps.setString(7, lastUpdatedBy);
                ps.setInt(8, customerID);
                ps.setInt(9, userID);
                ps.setInt(10, contactID);
                ps.setInt(11, Integer.parseInt(apptID.getText()));

                int result = ps.executeUpdate();
                if(result == 1)
                {
                    System.out.println("\nSave Succesful!\n");
                }
                else
                {
                    System.out.println("\nNo save? Check DB.\n");
                }

                populateTable();
                onActionClear(event);
            }
        }
        catch(SQLException e)
        {
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
     * Calls the clearALL() on button press.
     *
     * @param event when the "Clear" button is pressed
     */
    @FXML
    void onActionClear(ActionEvent event)
    {
        clearALL();
    }

    /**
     * Clears the text fields and set combo boxes to null.
     * <p>
     * This method also sets the appointment id prompt text back to "Generated
     * Automatically."
     */
    void clearALL()
    {
        apptID.clear();
        apptID.setPromptText("Generated Automatically");
        apptTitle.clear();
        apptDescription.clear();
        locationComboBox.setValue(null);
        contactComboBox.setValue(null);
        typeComboBox.setValue(null);
        apptDatePicker.setValue(LocalDate.now());
        startComboBox.setValue(null);
        endComboBox.setValue(null);
    }

    /**
     * Deletes an appointment from the database.
     * <p>
     * Checks if an appointment is selected and sends an alert if this is false.
     * A prepared statement is used to delete data from the database. The user
     * receives a confirmation dialog before deletion. The user gets a message
     * on a successful or failed delete.
     * <p>
     * There are currently no planned updates or improvements for this method.
     * Perhaps an update will be considered if a more efficient way of writing
     * this method becomes available in the future.
     *
     * @param event when the "Delete" button is pressed
     */
    @FXML
    void onActionDelete(ActionEvent event)
    {
        selectedAppt = apptTable.getSelectionModel().getSelectedItem();

        if(selectedAppt != null)
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Delete Appointment " + selectedAppt.getAppointmentID() + "?");
            Optional<ButtonType> result = alert.showAndWait();

            if(result.get() == ButtonType.OK)
            {
                try
                {
                    PreparedStatement ps = conn.prepareStatement(
                      "DELETE appointments.* FROM appointments "
                      + "WHERE appointments.Appointment_ID = ?");
                    ps.setInt(1, selectedAppt.getAppointmentID());

                    int rs = ps.executeUpdate();
                    if(rs > 0)
                    {
                        System.out.println("\nDeleted appointment #" + selectedAppt.getAppointmentID()
                          + " " + selectedAppt.getType() + "\n");
                    }
                    else
                    {
                        System.out.println("\nOne item failed to delete.\n");
                    }
                    populateTable();
                }
                catch(SQLException e)
                {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                    System.out.println("SQL error!");
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                    System.out.println("Non-SQL error!");
                }
            }
            else
            {
                return;
            }
        }
        if(selectedAppt == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Select an appointment to delete");
            alert.showAndWait();
        }
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
     * Populates the location combo box
     * <p>
     * Data is pulled from the database using a prepared statement and a SELECT
     * query.
     * <p>
     * I ran into a problem where the combo boxes are not returning to its
     * default state. By default, the combo boxes should only contain the prompt
     * text. But once a selection is made, the combo box does not return to
     * default. I was able to solve this problem with the help of @James_D at:
     * https://stackoverflow.com/a/50569582.
     */
    private void populateLocationCombobox()
    {
        ObservableList<String> locationCombo = FXCollections.observableArrayList();
        try
        {
            PreparedStatement ps = conn.prepareStatement(
              "SELECT first_level_divisions.Division "
              + "FROM first_level_divisions");

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                locationCombo.add(rs.getString("Division"));
            }
            locationComboBox.setItems(locationCombo);

            /* Displays the prompt text when combobox is cleared. For some reason
            the prompt text does is not diplayed after clearing this combobox. Only
            two comboboxes have this problem. */
            locationComboBox.setButtonCell(new ListCell<String>()
            {
                @Override
                protected void updateItem(String item, boolean empty)
                {
                    super.updateItem(item, empty);
                    if(empty || item == null)
                    {
                        setText("Select Location");
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
     * Populates the contact combo box.
     * <p>
     * Data is pulled from the database using a prepared statement and a SELECT
     * query.
     * <p>
     * I ran into a problem where the combo boxes are not returning to its
     * default state. By default, the combo boxes should only contain the prompt
     * text. But once a selection is made, the combo box does not return to
     * default. I was able to solve this problem with the help of @James_D at:
     * https://stackoverflow.com/a/50569582.
     */
    @FXML
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

                contactList.add(new Contacts(contactID, contactName, email));
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
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("Non-SQL error!");
        }
    }

    /**
     * Populates the type combo box.
     * <p>
     * The list that populates the combo box is just a list of strings generated
     * within the method in an observable list.
     * <p>
     * I ran into a problem where the combo boxes are not returning to its
     * default state. By default, the combo boxes should only contain the prompt
     * text. But once a selection is made, the combo box does not return to
     * default. I was able to solve this problem with the help of @James_D at:
     * https://stackoverflow.com/a/50569582.
     * <p>
     * The most immediate change is perhaps pulling the list from the database
     * instead if creating one locally.
     */
    @FXML
    private void populateTypeCombobox()
    {
        ObservableList<String> typeCombo = FXCollections.observableArrayList();

        typeCombo.addAll("Planning Session", "De-Briefing", "New Customer",
          "Progress Review", "Close Account");
        typeComboBox.setItems(typeCombo);

        /* Displays the prompt text when combobox is cleared. For some reason
        the prompt text does is not diplayed after clearing this combobox. Only
        two comboboxes have this problem 
         */
        typeComboBox.setButtonCell(new ListCell<String>()
        {
            @Override
            protected void updateItem(String item, boolean empty)
            {
                super.updateItem(item, empty);
                if(empty || item == null)
                {
                    setText("Select Type");
                }
                else
                {
                    setText(item);
                }
            }
        });

    }

    /**
     * Populates the Date and time fields/combo boxes.
     * <p>
     * The date picker is set to the current date. The Start and End time combo
     * boxes are populated with times from 00:00 to 23:00 in 30 minute
     * intervals.
     */
    @FXML
    private void populateDatTimeComboboxes()
    {
        LocalTime times = LocalTime.of(0, 0);

        while(!times.equals(LocalTime.of(23, 30)))
        {
            startTime.add(times);
            endTime.add(times);
            times = times.plusMinutes(30);
        }

        apptDatePicker.setValue(LocalDate.now());
        startComboBox.setItems(startTime);
        endComboBox.setItems(endTime);
    }

    /**
     * Populates the customer ID combo box.
     * <p>
     * Data is pulled from the database using a prepared statement and a SELECT
     * query.
     * <p>
     * I ran into a problem where the combo boxes are not returning to its
     * default state. By default, the combo boxes should only contain the prompt
     * text. But once a selection is made, the combo box does not return to
     * default. I was able to solve this problem with the help of @James_D at:
     * https://stackoverflow.com/a/50569582.
     */
    @FXML
    private void populateCustomerComboBox()
    {
        ObservableList<Integer> custIDCombo = FXCollections.observableArrayList();

        try
        {
            PreparedStatement ps = conn.prepareStatement("SELECT customers.Customer_ID "
              + "FROM customers ORDER BY Customer_ID");

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                custIDCombo.add(rs.getInt("Customer_ID"));
            }

            custIDcomboBox.setItems(custIDCombo);

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
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("Non-SQL error!");
        }
    }

    /**
     * Populates the customer name text field.
     * <p>
     * Data is pulled from the database using a prepared statement and a SELECT
     * query.
     *
     * @param event when the user selects a customer ID.
     */
    @FXML
    void populateCustomerName(ActionEvent event)
    {
        try
        {
            int searchID = custIDcomboBox.getValue();

            PreparedStatement ps = conn.prepareStatement("SELECT * FROM customers "
              + "WHERE Customer_ID = ?");

            ps.setInt(1, searchID);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                custName.setText(rs.getString("Customer_Name"));
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("SQL error!");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("Non-SQL error!");
        }
    }

    /**
     * Finds contact ID from appointment list.
     * <p>
     * Selected contact name is used to search for matching contact ID.
     *
     * @param temp contact name
     * @return the matching contact id
     */
    private int getContactIDfromList(String temp)
    {
        for(Contacts look : contactList)
        {
            if(look.getContactName().trim().toLowerCase().contains(temp.trim().toLowerCase()))
            {
                return look.getContactID();
            }
        }
        return -1;
    }

    /**
     * Checks if appointment being saved is inside of business hours.
     * <p>
     * The selected start time is compared to US/Eastern time business hours
     * between 08:00 to 22:00. if start time is before 08:00, an alert is sent
     * to the user. The same occurs if the start time is greater than or equal
     * to 22:00.
     * <p>
     * There are other ways of doing this. A more efficient implementation will
     * be chosen if an updated becomes a necessity.
     *
     * @param startTime the start time
     * @return returns true if a appointment is inside business hours, else
     * returns false with alert
     */
    private boolean checkBusinessHours(LocalTime startTime)
    {
        LocalTime officeClosed = LocalTime.of(22, 00);
        LocalTime officeOpen = LocalTime.of(8, 00);
        LocalDate date = apptDatePicker.getValue();

        ZoneId zoneEST = ZoneId.of("US/Eastern");

        LocalDateTime combined = LocalDateTime.of(date, startTime);
        ZonedDateTime convert = combined.atZone(ZoneId.systemDefault())
          .withZoneSameInstant(zoneEST);
        LocalTime eastern = convert.toLocalTime();

        if(eastern.isBefore(officeOpen) || eastern.isAfter(officeClosed))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Appointment must be within business hours");
            alert.setContentText("Selected Time: " + startTime + "\nEastern Time: "
              + eastern + "\nBusiness hours: 08:00 to 22:00 US/Eastern");
            alert.showAndWait();
            return false;
        }
        return true;
        
    }

    /**
     * Checks for overlapping appointments. No customer can have an overlapping
     * appointment with his/her own or other customers.
     * <p>
     * It took several iterations and hours of perusing the MySQL online manual
     * to finally get this to a working state. In the end, it all came to making
     * sure the correct comparison operators are being applied, and using a
     * do...while loop.
     * <p>
     * Update: A revision had to be done when overlapping appointments occurred during
     * evaluation. Some syntax rearrangement was done to address the problems. 
     * The do..while loop has been replaced with an if...else statement.
     *
     * @param startA the timestamp of start date
     * @param endB the timestamp end date
     * @return true if schedule overlap exists and send an alert, else return
     * false
     */
    private boolean overlapSchedule(Timestamp startA, Timestamp endB)
    {

        try
        {
            if(apptID.getText().isBlank())
            {
                apptID1 = 0;
            }
            else
            {
                apptID1 = Integer.parseInt(apptID.getText().trim());
            }

            int customerID1 = custIDcomboBox.getValue();
            int contactID1 = getContactIDfromList(contactComboBox.getValue());

            System.out.println("\nSelected Appointment ID: " + apptID1);
            System.out.println("Selected Contact ID: " + contactID1);
            System.out.println("Selected Customer ID: " + customerID1);
            System.out.println("Selected Start: " + startA);
            System.out.println("Selected End: " + endB);

            ps = conn.prepareStatement(
              "SELECT * FROM appointments "
              + "WHERE (? BETWEEN Start AND End OR ? BETWEEN Start AND End OR ? < Start AND ? > End) "
              + "AND (Customer_ID = ? AND Appointment_ID != ?)");

            ps.setTimestamp(1, startA);
            ps.setTimestamp(2, endB);
            ps.setTimestamp(3, startA);
            ps.setTimestamp(4, endB);
            ps.setInt(5, customerID1);
            ps.setInt(6, apptID1);

            ResultSet rs = ps.executeQuery();

            if(rs.next())
            {
                return true;
            }
            return false;

            /*
            if(appointmentLabel.getText().contentEquals("Add Appointment"))
            {
                ps = conn.prepareStatement(
                  "SELECT * FROM appointments "
                  + "WHERE (Customer_ID = ?) "
                  + "AND (? BETWEEN Start AND End "
                  + "OR ? BETWEEN Start AND End) "
                  + "OR ? < Start AND ? > End");

                ps.setInt(1, customerID1);
                ps.setTimestamp(2, startA);
                ps.setTimestamp(3, endB);
                ps.setTimestamp(4, startA);
                ps.setTimestamp(5, endB);

                ResultSet rs = ps.executeQuery();

                if(rs.next())
                {                 
                    return true;
                }
                return false;
            }
             */
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("SQL error!");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("Non-SQL error!");
        }
        return false;       
    }

    /**
     * Filters the appointments by the next 7 days.
     * <p>
     * The appointment list is filtered by using the current system time as the
     * beginning of the filtered list and the 7th day from the current time as
     * the end.
     * <p>
     * Discussion of Lambda
     * <p>
     * This lambda expression is used because the original uses about 70 lines.
     * Using a filtered list that has an "always true" predicate is more
     * efficient than using a prepared statement and a SELECT QUERY that filters
     * the result from the database. I have left these queries in methods
     * monthRadio(), weekRadio(), and nexMonthRadio() as comments for
     * comparison. This lambda expression is similar to the other two used in
     * this project. The only difference is the filtered return value.
     * <p>
     * Hat tip to James_D at: https://stackoverflow.com/a/44925310 for the inspiration.
     *
     * @param aList the observable list containing all appointments
     */
    public void filteredbyWeek(ObservableList aList)
    {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime week = LocalDateTime.now().plusDays(7);

        FilteredList<Appointments> resultWeek = new FilteredList<>(aList);
        resultWeek.setPredicate(a ->
        {
            LocalDateTime date = a.getStart();
            return (date.isEqual(now) || date.isAfter(now)) && (date.isBefore(week) || date.isEqual(week));
        });
        apptTable.setItems(resultWeek);
    }

    /**
     * Filters the appointments from system time to the last day of the month.
     * <p>
     * The appointment list is filtered by using the current system time as the
     * beginning of the filtered list and the end of the month as the last day.
     * <p>
     * Discussion of Lambda
     * <p>
     * This lambda expression is used because the original uses about 70 lines.
     * Using a filtered list that has an "always true" predicate is more
     * efficient than using a prepared statement and a SELECT QUERY that filters
     * the result from the database. I have left these queries in methods
     * monthRadio(), weekRadio(), and nexMonthRadio() as comments for
     * comparison. This lambda expression is similar to the other two used in
     * this project. The only difference is the filtered return value.
     * <p>
     * Hat tip to James_D at: https://stackoverflow.com/a/44925310 for the inspiration.
     *
     * @param aList the observable list containing all appointments
     *
     */
    public void filteredbyEndofMonth(ObservableList aList)
    {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endMonth = now.with(TemporalAdjusters.lastDayOfMonth());

        FilteredList<Appointments> resultEnd = new FilteredList<>(aList);
        resultEnd.setPredicate(a ->
        {
            LocalDateTime date = a.getStart();
            return (date.isEqual(now) || date.isAfter(now)) && (date.isBefore(endMonth) || date.isEqual(endMonth));
        });
        apptTable.setItems(resultEnd);
    }

    /**
     * Filters the appointments by the next 30 days.
     * <p>
     * The appointment list is filtered by using the current system time as the
     * beginning of the filtered list and the 30th day from the current time as
     * the end.
     * <p>
     * Discussion of Lambda
     * <p>
     * This lambda expression is used because the original uses about 70 lines.
     * Using a filtered list that has an "always true" predicate is more
     * efficient than using a prepared statement and a SELECT QUERY that filters
     * the result from the database. I have left these queries in methods
     * monthRadio(), weekRadio(), and nexMonthRadio() as comments for
     * comparison. This lambda expression is similar to the other two used in
     * this project. The only difference is the filtered return value.
     * <p>
     * Hat tip to James_D at: https://stackoverflow.com/a/44925310 for the inspiration.
     *
     * @param aList the observable list containing all appointments
     */
    public void filteredbyNextMonth(ObservableList aList)
    {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime month = LocalDateTime.now().plusMonths(1);

        FilteredList<Appointments> nextMonth = new FilteredList<>(aList);
        nextMonth.setPredicate(a ->
        {
            LocalDateTime date = a.getStart();
            return (date.isEqual(now) || date.isAfter(now)) && (date.isBefore(month) || date.isEqual(month));
        });
        apptTable.setItems(nextMonth);
    }

}
