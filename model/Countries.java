package model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * The Countries Class
 *
 * @author Audrey Hababag
 */
public class Countries
{
    private int countryID;
    private String country;
    private LocalDateTime createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;

    /**
     * Countries constructor
     *
     * @param countryID the country ID
     * @param country the country
     * @param createDate the create date
     * @param createdBy the user responsible for creation
     * @param lastUpdate the date of last update
     * @param lastUpdatedBy the user responsible for last update
     */
    public Countries(int countryID, String country, LocalDateTime createDate,
      String createdBy, Timestamp lastUpdate, String lastUpdatedBy)
    {
        setCountryID(countryID);
        setCountry(country);
        setCreateDate(createDate);
        setCreatedBy(createdBy);
        setLastUpdate(lastUpdate);
        setLastUpdatedBy(lastUpdatedBy);
    }

    /**
     * Getter for countryID
     *
     * @return the country ID
     */
    public int getCountryID()
    {
        return countryID;
    }

    /**
     * Setter for country ID
     *
     * @param countryID the country ID to set
     */
    public void setCountryID(int countryID)
    {
        this.countryID = countryID;
    }

    /**
     * Getter for country
     *
     * @return the country
     */
    public String getCountry()
    {
        return country;
    }

    /**
     * Setter for country
     *
     * @param country the country to set
     */
    public void setCountry(String country)
    {
        this.country = country;
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
     * @return the user responsible for creating
     */
    public String getCreatedBy()
    {
        return createdBy;
    }

    /**
     * Setter for createdBy
     *
     * @param createdBy the user to set
     */
    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }

    /**
     * Getter for lastUpdate
     *
     * @return the timestamp of last update
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
     * @return the user responsible for last update
     */
    public String getLastUpdatedBy()
    {
        return lastUpdatedBy;
    }

    /**
     * Setter for lastUpdatedBy
     *
     * @param lastUpdatedBy the user to set
     */
    public void setLastUpdatedBy(String lastUpdatedBy)
    {
        this.lastUpdatedBy = lastUpdatedBy;
    }
}
