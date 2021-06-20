package model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * The Customers Class
 *
 * @author Audrey Hababag
 */
public class Customers
{
    private int customerID;
    private String customerName;
    private String address;
    private String postalCode;
    private String phone;
    private LocalDateTime createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;
    private int divisionID;
    private String divisionName;
    private String countryName;

    /**
     * Customers constructor
     *
     * @param customerID the customer ID
     * @param customerName the customer name
     * @param address the address
     * @param postalCode the postal code
     * @param phone the phone number
     * @param createDate the created date
     * @param createdBy the user responsible for creation
     * @param lastUpdate the date of last update
     * @param lastUpdatedBy the user responsible for last update
     * @param divisionID the division ID
     * @param divisionName the division name
     * @param countryName the country name
     */
    public Customers(int customerID, String customerName, String address,
      String postalCode, String phone, LocalDateTime createDate, String createdBy,
      Timestamp lastUpdate, String lastUpdatedBy, int divisionID, String divisionName, String countryName)
    {
        setCustomerID(customerID);
        setCustomerName(customerName);
        setAddress(address);
        setPostalCode(postalCode);
        setPhone(phone);
        setCreateDate(createDate);
        setCreatedBy(createdBy);
        setLastUpdate(lastUpdate);
        setLastUpdatedBy(lastUpdatedBy);
        setDivisionID(divisionID);
        setDivisionName(divisionName);
        setCountryName(countryName);
    }

    /**
     * Getter for customerID
     *
     * @return the customer id
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
     * Getter for customerName
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
     * Getter for address
     *
     * @return the address
     */
    public String getAddress()
    {
        return address;
    }

    /**
     * Setter for address
     *
     * @param address the address to set
     */
    public void setAddress(String address)
    {
        this.address = address;
    }

    /**
     * Getter for postalCode
     *
     * @return the postal code
     */
    public String getPostalCode()
    {
        return postalCode;
    }

    /**
     * Setter for postalCode
     *
     * @param postalCode the postal code to set
     */
    public void setPostalCode(String postalCode)
    {
        this.postalCode = postalCode;
    }

    /**
     * Getter for phone
     *
     * @return the phone nubmer
     */
    public String getPhone()
    {
        return phone;
    }

    /**
     * Setter for phone
     *
     * @param phone the phone number to set
     */
    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    /**
     * Getter for createDate
     *
     * @return the creation date
     */
    public LocalDateTime getCreateDate()
    {
        return createDate;
    }

    /**
     * Setter for createDate
     *
     * @param createDate the creation date to set
     */
    public void setCreateDate(LocalDateTime createDate)
    {
        this.createDate = createDate;
    }

    /**
     * Getter for createdBy
     *
     * @return the username of creator
     */
    public String getCreatedBy()
    {
        return createdBy;
    }

    /**
     * Setter for createdBy
     *
     * @param createdBy the username of creator to set
     */
    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }

    /**
     * Getter for lastUpdate
     *
     * @return the date of last update
     */
    public Timestamp getLastUpdate()
    {
        return lastUpdate;
    }

    /**
     * Setter for lastUpdate
     *
     * @param lastUpdate the date of last update to set
     */
    public void setLastUpdate(Timestamp lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    /**
     * Getter for lastUpdatedBy
     *
     * @return the date of last update
     */
    public String getLastUpdatedBy()
    {
        return lastUpdatedBy;
    }

    /**
     * Setter for lastUpdatedBy
     *
     * @param lastUpdatedBy the username of last updater
     */
    public void setLastUpdatedBy(String lastUpdatedBy)
    {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * Getter for divisionID
     *
     * @return the division ID
     */
    public int getDivisionID()
    {
        return divisionID;
    }

    /**
     * Setter for divisionID
     *
     * @param divisionID the division ID to set
     */
    public void setDivisionID(int divisionID)
    {
        this.divisionID = divisionID;
    }

    /**
     * Getter for divisionName
     *
     * @return the division name
     */
    public String getDivisionName()
    {
        return divisionName;
    }

    /**
     * Setter for divisionName
     *
     * @param division the division name to set
     */
    public void setDivisionName(String division)
    {
        this.divisionName = division;
    }

    /**
     * Getter for countryName
     *
     * @return the country name
     */
    public String getCountryName()
    {
        return countryName;
    }

    /**
     * Setter for countryName
     *
     * @param countryName the country name to set
     */
    public void setCountryName(String countryName)
    {
        this.countryName = countryName;
    }
}
