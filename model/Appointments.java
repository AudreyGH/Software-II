package model;

import java.time.LocalDateTime;
import java.sql.Timestamp;

/**
 * The Appointments Class
 *
 * @author Audrey Hababag
 */
public class Appointments
{
    private int appointmentID;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private LocalDateTime createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;
    private int customerID;
    private int userID;
    private int contactID;
    private String contactName;
    private String customerName;

    /**
     * Appointments constructor
     *
     * @param appointmentID the appointment id
     * @param title the appointment title
     * @param description the appointment description
     * @param location the location
     * @param type the appointment type
     * @param start the start date and time
     * @param end the end date and time
     * @param createDate the create date
     * @param createdBy the creator
     * @param lastUpdate the date of last update
     * @param lastUpdatedBy the user who updated
     * @param customerID the customer ID
     * @param userID the user ID
     * @param contactID the contact ID
     * @param contactName the contact name
     * @param customerName the customer name
     */
    public Appointments(int appointmentID, String title, String description,
      String location, String type, LocalDateTime start, LocalDateTime end, LocalDateTime createDate,
      String createdBy, Timestamp lastUpdate, String lastUpdatedBy, int customerID,
      int userID, int contactID, String contactName, String customerName)
    {
        setAppointmentID(appointmentID);
        setTitle(title);
        setDescription(description);
        setLocation(location);
        setType(type);
        setStart(start);
        setEnd(end);
        setCreateDate(createDate);
        setCreatedBy(createdBy);
        setLastUpdate(lastUpdate);
        setLastUpdatedBy(lastUpdatedBy);
        setCustomerID(customerID);
        setUserID(userID);
        setContactID(contactID);
        setContactName(contactName);
        setCustomerName(customerName);
    }

    /**
     * Appointments constructor used for login screen appointment alerts
     *
     * @param appointmentID the appointment ID
     * @param start the start date and time
     */
    public Appointments(int appointmentID, LocalDateTime start)
    {
        setAppointmentID(appointmentID);
        setStart(start);
    }

    /**
     * Setter for getContactName
     *
     * @return the contact name
     */
    public String getContactName()
    {
        return contactName;
    }

    /**
     * Getter for ContactName
     *
     * @param contactName the contact name to set
     */
    public void setContactName(String contactName)
    {
        this.contactName = contactName;
    }

    /**
     * Getter for CustomerName
     *
     * @return the customer name
     */
    public String getCustomerName()
    {
        return customerName;
    }

    /**
     * Setter for customerName
     *
     * @param customerName the customer name to set
     */
    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }

    /**
     * Getter for appointmentID
     *
     * @return the appointment ID
     */
    public int getAppointmentID()
    {
        return appointmentID;
    }

    /**
     * Setter for appointment ID
     *
     * @param appointmentID the appointment ID to set
     */
    public void setAppointmentID(int appointmentID)
    {
        this.appointmentID = appointmentID;
    }

    /**
     * Getter for title
     *
     * @return the title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Setter for Title
     *
     * @param title the title to set
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * Getter for description
     *
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Setter for description
     *
     * @param description the description to set
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Getter for location
     *
     * @return the location
     */
    public String getLocation()
    {
        return location;
    }

    /**
     * Setter for location
     *
     * @param location the location to set
     */
    public void setLocation(String location)
    {
        this.location = location;
    }

    /**
     * Getter for type
     *
     * @return the type
     */
    public String getType()
    {
        return type;
    }

    /**
     * Setter for type
     *
     * @param type the type to set
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * Getter for start
     *
     * @return the start date and time
     */
    public LocalDateTime getStart()
    {
        return start;
    }

    /**
     * Setter for start
     *
     * @param start the start date and time to set
     */
    public void setStart(LocalDateTime start)
    {
        this.start = start;
    }

    /**
     * Getter for end
     *
     * @return the end date and time
     */
    public LocalDateTime getEnd()
    {
        return end;
    }

    /**
     * Setter for end
     *
     * @param end the end date and time to set
     */
    public void setEnd(LocalDateTime end)
    {
        this.end = end;
    }

    /**
     * Getter for createDate
     *
     * @return the create date
     */
    public LocalDateTime getCreateDate()
    {
        return createDate;
    }

    /**
     * Setter for createDate
     *
     * @param createDate the create date to set
     */
    public void setCreateDate(LocalDateTime createDate)
    {
        this.createDate = createDate;
    }

    /**
     * Getter for createdBy
     *
     * @return the user who created
     */
    public String getCreatedBy()
    {
        return createdBy;
    }

    /**
     * Setter for createdBy
     *
     * @param createdBy the creator to set
     */
    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }

    /**
     * Getter for lastUpdate
     *
     * @return timestamp of last update
     */
    public Timestamp getLastUpdate()
    {
        return lastUpdate;
    }

    /**
     * Setter for lastUpdate
     *
     * @param lastUpdate timestamp of last update to set
     */
    public void setLastUpdate(Timestamp lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    /**
     * Getter for lastUpdatedBy
     *
     * @return the user responsible for the last update
     */
    public String getLastUpdatedBy()
    {
        return lastUpdatedBy;
    }

    /**
     * Setter for lastUpdatedBy
     *
     * @param lastUpdatedBy the user responsible for the last update
     */
    public void setLastUpdatedBy(String lastUpdatedBy)
    {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * Getter for customerID
     *
     * @return the customer ID
     */
    public int getCustomerID()
    {
        return customerID;
    }

    /**
     * Setter for customerID
     *
     * @param customerID the customer ID to set
     */
    public void setCustomerID(int customerID)
    {
        this.customerID = customerID;
    }

    /**
     * Getter for userID
     *
     * @return the user ID
     */
    public int getUserID()
    {
        return userID;
    }

    /**
     * Setter for userID
     *
     * @param userID the user ID to set
     */
    public void setUserID(int userID)
    {
        this.userID = userID;
    }

    /**
     * Getter for contactID
     *
     * @return the contact ID to set
     */
    public int getContactID()
    {
        return contactID;
    }

    /**
     * Setter for contactID
     *
     * @param contactID the contact ID to set
     */
    public void setContactID(int contactID)
    {
        this.contactID = contactID;
    }
}
