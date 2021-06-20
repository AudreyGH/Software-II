package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.*;
import model.Countries;
import model.Customers;
import model.FirstLeveLDivisions;
import model.Users;
import utils.DBConnection;
import static utils.DBConnection.closeConnection;

/**
 * FXML controller class for customer screen.
 *
 * @author Audrey Hababag
 */
public class CustomerScreenController implements Initializable
{
    Stage stage;
    Parent scene;
    int divisionID = -1;
    PreparedStatement ps;
    private Users userNow;
    private static Connection conn = DBConnection.getConnection();
    private Customers selectedCustomer;
    ObservableList<Customers> customerList = FXCollections.observableArrayList();
    ObservableList<Countries> countryList = FXCollections.observableArrayList();
    ObservableList<FirstLeveLDivisions> firstLevelList = FXCollections.observableArrayList();
    @FXML
    private TableView<Customers> customerTable;
    @FXML
    private TableColumn<Customers, Integer> custIdColumn;
    @FXML
    private TableColumn<Customers, String> nameColumn;
    @FXML
    private TableColumn<Customers, String> phoneColumn;
    @FXML
    private TableColumn<Customers, String> addressColumn;
    @FXML
    private TableColumn<Customers, String> divisionColumn;
    @FXML
    private TableColumn<Customers, String> zipColumn;
    @FXML
    private Label addCustomerLabel;
    @FXML
    private TextField custID;
    @FXML
    private TextField custName;
    @FXML
    private TextField custAddress;
    @FXML
    private ComboBox<String> stateComboBox;
    @FXML
    private ComboBox<String> countryComboBox;
    @FXML
    private TextField custZip;
    @FXML
    private TextField custPhone;

    /**
     * Initializes controller Class.
     *
     * @param url pointer to the resource
     * @param rb the resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        custIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        divisionColumn.setCellValueFactory(new PropertyValueFactory<>("divisionName"));
        zipColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));

        populateTable();
        populateCountryCombo();
        populateFirstLevelCombo();
    }

    /**
     * Populates the customer table.
     * <p>
     * Data is pulled from database to populate the table. A prepared statement
     * is used to pull data and populate the table. Data is set to an observable
     * list which is then used to populate the table view in Scene Builder.
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
            customerList.clear();
            PreparedStatement ps = conn.prepareStatement(
              "SELECT * FROM customers, first_level_divisions, countries "
              + "WHERE customers.Division_ID = first_level_divisions.Division_ID "
              + "AND first_level_divisions.COUNTRY_ID = countries.Country_ID "
              + "ORDER BY Customer_ID");

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                int customerID = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phone = rs.getString("Phone");
                LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                int divisionID = rs.getInt("Division_ID");
                String divisionName = rs.getString("Division");
                String countryName = rs.getString("Country");

                customerList.add(new Customers(customerID, customerName, address,
                  postalCode, phone, createDate, createdBy, lastUpdate,
                  lastUpdatedBy, divisionID, divisionName, countryName));
            }
            // Set all customers on the table
            customerTable.setItems(customerList);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("SQL Error!");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("Non-Sql error!");
        }
    }

    /**
     * Populates the State/Province combo box with all divisions.
     * <p>
     * Data is pulled from the database using a prepared statement and a SELECT
     * query. This is the default state of the combo box. The list changes when
     * a country is selected from the country combo box.
     * <p>
     * I ran into a problem where the combo boxes are not returning to its
     * default state. By default, the combo boxes should only contain the prompt
     * text. But once a selection is made, the combo box does not return to
     * default. I was able to solve this problem with the help of @James_D at:
     * https://stackoverflow.com/a/50569582.
     */
    @FXML
    private void populateFirstLevelCombo()
    {
        ObservableList<String> stateCombo = FXCollections.observableArrayList();
        try
        {
            PreparedStatement ps = conn.prepareStatement(
              "SELECT * FROM first_level_divisions");

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                int divisionID = rs.getInt("Division_ID");
                String division = rs.getString("Division");
                LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                int countryID = rs.getInt("COUNTRY_ID");

                stateCombo.add(division);

                firstLevelList.add(new FirstLeveLDivisions(divisionID, division,
                  createDate, createdBy, lastUpdate, lastUpdatedBy, countryID));
            }
            stateComboBox.setItems(stateCombo);

            /* Displays the prompt text when combobox is cleared. For some reason
            the prompt text does is not diplayed after clearing this combobox. Only
            two comboboxes have this problem */
            stateComboBox.setButtonCell(new ListCell<String>()
            {
                @Override
                protected void updateItem(String item, boolean empty)
                {
                    super.updateItem(item, empty);
                    if(empty || item == null)
                    {
                        setText("Select State/Province");
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
     * Filters the State/Province combo box by the country selected.
     * <p>
     * Data is pulled from the database using a prepared statement and a SELECT
     * query. The ID of the selected country is compared to the
     * first_level_divisions table and the divisions that match the same ID is
     * used to populate the State/Province combo box.
     * <p>
     * I ran into a problem where the combo boxes are not returning to its
     * default state. By default, the combo boxes should only contain the prompt
     * text. But once a selection is made, the combo box does not return to
     * default. I was able to solve this problem with the help of @James_D at:
     * https://stackoverflow.com/a/50569582.
     * <p>
     * This can be done without using the database. If filtering an observable
     * list is more efficient, it will be used in a future update.
     *
     * @param event when the user selects a country
     */
    @FXML
    void onActionCountry(ActionEvent event)
    {
        try
        {
            ObservableList<String> stateCombo = FXCollections.observableArrayList();

            if(countryComboBox.getValue() == null)
            {
                populateFirstLevelCombo();
            }
            if(countryComboBox.getValue().contentEquals("U.S"))
            {
                ps = conn.prepareStatement(
                  "SELECT * FROM first_level_divisions WHERE COUNTRY_ID = 1 "
                  + "ORDER BY Division");
            }
            if(countryComboBox.getValue().contentEquals("UK"))
            {
                ps = conn.prepareStatement(
                  "SELECT * FROM first_level_divisions WHERE COUNTRY_ID = 2 "
                  + "ORDER BY Division");
            }
            if(countryComboBox.getValue().contentEquals("Canada"))
            {
                ps = conn.prepareStatement(
                  "SELECT * FROM first_level_divisions WHERE COUNTRY_ID = 3 "
                  + "ORDER BY Division");
            }

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                int divisionID = rs.getInt("Division_ID");
                String division = rs.getString("Division");
                LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                int countryID = rs.getInt("COUNTRY_ID");

                stateCombo.add(division);

                firstLevelList.add(new FirstLeveLDivisions(divisionID, division,
                  createDate, createdBy, lastUpdate, lastUpdatedBy, countryID));
            }
            stateComboBox.setItems(stateCombo);

            /* Displays the prompt text when combobox is cleared. For some reason
            the prompt text does is not diplayed after clearing this combobox. Only
            two comboboxes have this problem */
            stateComboBox.setButtonCell(new ListCell<String>()
            {
                @Override
                protected void updateItem(String item, boolean empty)
                {
                    super.updateItem(item, empty);
                    if(empty || item == null)
                    {
                        setText("Select State/Province");
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
     * Populates the State/Province combo box.
     * <p>
     * Data is pulled from the database using a prepared statement and a SELECT
     * query.
     */
    @FXML
    private void populateCountryCombo()
    {
        ObservableList<String> countryCombo = FXCollections.observableArrayList();
        try
        {
            PreparedStatement ps = conn.prepareStatement(
              "SELECT * FROM countries");

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                int countryID = rs.getInt("Country_ID");
                String country = rs.getString("Country");
                LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                String lastUpdatedBy = rs.getString("Last_Updated_By");

                countryCombo.add(country);

                countryList.add(new Countries(countryID, country, createDate,
                  createdBy, lastUpdate, lastUpdatedBy));
            }
            countryComboBox.setItems(countryCombo);

            /* Displays the prompt text when combobox is cleared. For some reason
            the prompt text does is not diplayed after clearing this combobox. Only
            two comboboxes have this problem */
            countryComboBox.setButtonCell(new ListCell<String>()
            {
                @Override
                protected void updateItem(String item, boolean empty)
                {
                    super.updateItem(item, empty);
                    if(empty || item == null)
                    {
                        setText("Select Country");
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
     * Prepares fields for customer add.
     * <p>
     * Sets the customer label to "Add Customer" and calls onAcitonClear()
     * method to clear the fields.
     *
     * @param event when the "Add" button is pressed
     */
    @FXML
    void onActionAdd(ActionEvent event)
    {
        addCustomerLabel.setText("Add Customer");
        onActionClear(event);
    }

    /**
     * Prepares fields for customer update.
     * <p>
     * Sets the customer label to "Update Customer" and calls onAcitonClear()
     * method to clear the fields. The method also checks if the user has
     * selected a customer from the table and sends an alert if this is false.
     *
     * @param event when the "Update" button is pressed
     */
    @FXML
    void onActionUpdate(ActionEvent event)
    {
        if(customerTable.getSelectionModel().getSelectedItem() != null)
        {
            onActionClear(event);
            addCustomerLabel.setText("Update Customer");
            selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

            // populate field with values from selected customer
            custID.setText(Integer.toString(selectedCustomer.getCustomerID()));
            custName.setText(selectedCustomer.getCustomerName());
            custAddress.setText(selectedCustomer.getAddress());
            stateComboBox.setValue(selectedCustomer.getDivisionName());
            countryComboBox.setValue(selectedCustomer.getCountryName());
            custZip.setText(selectedCustomer.getPostalCode());
            custPhone.setText(selectedCustomer.getPhone());
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Select an customerfrom the table");
            alert.setContentText("Then click the update button again");
            alert.showAndWait();
        }

    }

    /**
     * Saves the customer being added or updated to the database.
     * <p>
     * This method pulls all the data from the fields and combo boxes and
     * inserts the data into the database using a prepared statement. All text
     * fields are checked for correct formatting accepted by the database. An
     * alert is sent to inform the user if a field's format is invalid. The user
     * receives an alert if a combo box is left to its default state, containing
     * a null value when no item is selected.
     * <p>
     * If I were to make any changes, I would try to separate the Update and Add
     * prepared statements instead of having them in one method. This made
     * everything more problematic than necessary. I would also try to validate
     * the field in its own method for cleaner formatting.
     *
     * @param event when the "Save" button is pressed.
     */
    @FXML
    void onActionSave(ActionEvent event)
    {
        try
        {
            String customerName = custName.getText();
            String address = custAddress.getText();
            String postalCode = custZip.getText();
            String phoneNumber = custPhone.getText();
            String lastUpdatedBy = userNow.getuserName();

            /* Stops at the line when an error is found so an alert can be sent.
               Errors will occur on the following lines that are reliant on the 
                field if it is empty or null.  */
            if(stateComboBox.getValue() != null)
            {

                divisionID = getDivisionIDfromList(stateComboBox.getValue());
            }

            // Alert if fields are empty or null
            if(customerName.isBlank())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Name field is empty");
                alert.showAndWait();
                return;
            }
            if(address.isBlank())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Address field is empty");
                alert.showAndWait();
                return;
            }
            if(stateComboBox.getValue() == null)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Select a State/Province");
                alert.showAndWait();
                return;
            }
            if(countryComboBox.getValue() == null)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Select a Country");
                alert.showAndWait();
                return;
            }
            if(postalCode.isBlank())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Postal Code field is empty");
                alert.showAndWait();
                return;
            }
            if(phoneNumber.isBlank())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Phone Number field is empty");
                alert.showAndWait();
                return;
            }

            // If Add Customer button is clicked
            if(addCustomerLabel.getText().contains("Add Customer"))
            {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO customers"
                  + "(Customer_Name, Address, Postal_Code, Phone, Create_Date, "
                  + "Created_By, Last_Update, Last_Updated_By, Division_ID) "
                  + "VALUES(?, ?, ?, ?, NOW(), ?, NOW(), ?, ?)",
                  Statement.RETURN_GENERATED_KEYS);

                ps.setString(1, customerName);
                ps.setString(2, address);
                ps.setString(3, postalCode);
                ps.setString(4, phoneNumber);
                ps.setString(5, lastUpdatedBy);
                ps.setString(6, lastUpdatedBy);
                ps.setInt(7, divisionID);

                // Check if INSERT is successful
                int result = ps.executeUpdate();
                if(result > 0)
                {
                    System.out.println("\n" + customerName + " added!\n");
                }
                else
                {
                    System.out.println("\nNo save? Check DB.\n");
                }

                // Generate new cutomer ID and print
                ResultSet rs = ps.getGeneratedKeys();
                if(rs.next())
                {

                    int autoKey = rs.getInt(1);
                    System.out.println("Generated Customer ID: " + autoKey);
                }

                populateTable();
                clearALL();
            }

            // If Update Customer button is clicked
            if(addCustomerLabel.getText().contains("Update Customer"))
            {
                PreparedStatement ps = conn.prepareStatement("UPDATE customers "
                  + "Set Customer_Name = ?, Address = ?, Postal_Code = ?, "
                  + "Phone = ?, Last_Update = NOW(), Last_Updated_By = ?, "
                  + "Division_ID =? "
                  + "WHERE Customer_ID = ?");

                ps.setString(1, customerName);
                ps.setString(2, address);
                ps.setString(3, postalCode);
                ps.setString(4, phoneNumber);
                ps.setString(5, lastUpdatedBy);
                ps.setInt(6, divisionID);
                ps.setInt(7, Integer.parseInt(custID.getText()));

                int result = ps.executeUpdate();
                if(result == 1)
                {
                    System.out.println("\n" + customerName + " updated.\n");
                }
                else
                {
                    System.out.println("\nSave error\n");
                }

                populateTable();
                clearALL();
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
     * This method also sets the customer id prompt text back to "Generated
     * Automatically."
     */
    void clearALL()
    {
        custID.clear();
        custID.setPromptText("Generated Automatically");
        custName.clear();
        custAddress.clear();
        custZip.clear();
        custPhone.clear();
    }

    /**
     * Deletes a customer from the database.
     * <p>
     * Checks if a customer is selected and sends an alert if this is false. A
     * prepared statement is used to delete data from the database. The user
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
        selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

        if(selectedCustomer != null)
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Delete Customer " + selectedCustomer.getCustomerName());
            alert.setContentText("This will also delete all associated appointments.");
            Optional<ButtonType> result = alert.showAndWait();

            if(result.get() == ButtonType.OK)
            {
                try
                {
                    PreparedStatement ps = conn.prepareStatement(
                      "DELETE appointments.* FROM appointments "
                      + "WHERE Customer_ID =?");
                    PreparedStatement ps2 = conn.prepareStatement(
                      "DELETE customers.* FROM customers "
                      + "WHERE Customer_ID =?");

                    ps.setInt(1, selectedCustomer.getCustomerID());
                    ps2.setInt(1, selectedCustomer.getCustomerID());
                    int rs = ps.executeUpdate();
                    int rs2 = ps2.executeUpdate();

                    if(rs2 > 0)
                    {
                        System.out.println("\nCustomer " + selectedCustomer.getCustomerName()
                          + " and associated appointments deleted\n");
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
                    System.out.println(e.getMessage());
                    System.out.println("Non-SQL error!");
                }
            }
            else
            {
                return;
            }
        }
        if(selectedCustomer == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Select a customer to delete");
            alert.showAndWait();
        }
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
     * Finds the division ID matching the selected State/Province.
     * <p>
     * A simple search is done to find the division ID of the currently selected
     * division from the State/Province combo box
     *
     * @param temp selected division
     * @return division id or -1 if no match is found
     */
    private int getDivisionIDfromList(String temp)
    {
        for(FirstLeveLDivisions look : firstLevelList)
        {
            if(look.getDivision().trim().toLowerCase().contains(
              temp.trim().toLowerCase()))
            {
                return look.getDivisionID();
            }
        }
        return -1;
    }
}
