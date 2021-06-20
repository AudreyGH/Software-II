package model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * The FirstLeveLDivisions Class
 *
 * @author Audrey Hababag
 */
public class FirstLeveLDivisions
{
    private int divisionID;
    private String division;
    private LocalDateTime createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastupdatedBy;
    private int countryID;

    /**
     * FirstLeveLDivisions constructor
     *
     * @param divisionID the division ID
     * @param division the division
     * @param createDate the creation date
     * @param createdBy the username of creator
     * @param lastUpdate the date of last update
     * @param lastupdatedBy the username of lat updater
     * @param countryID the country ID
     */
    public FirstLeveLDivisions(int divisionID, String division, LocalDateTime createDate,
      String createdBy, Timestamp lastUpdate, String lastupdatedBy, int countryID)
    {
        setDivisionID(divisionID);
        setDivision(division);
        setCreateDate(createDate);
        setCreatedBy(createdBy);
        setLastUpdate(lastUpdate);
        setLastupdatedBy(lastupdatedBy);
        setCountryID(countryID);
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
     * Getter for division
     *
     * @return the division
     */
    public String getDivision()
    {
        return division;
    }

    /**
     * Setter for division
     *
     * @param division the division to set
     */
    public void setDivision(String division)
    {
        this.division = division;
    }

    /**
     * Getter for createDate
     *
     * @return the date of creation
     */
    public LocalDateTime getCreateDate()
    {
        return createDate;
    }

    /**
     * Setter for createDate
     *
     * @param createDate the date of creation to set
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
     * @param createdBy the creator username to set
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
     * @return the updater username
     */
    public String getLastupdatedBy()
    {
        return lastupdatedBy;
    }

    /**
     * Setter of lastupdatedBy
     *
     * @param lastupdatedBy updater username to set
     */
    public void setLastupdatedBy(String lastupdatedBy)
    {
        this.lastupdatedBy = lastupdatedBy;
    }

    /**
     * Getter for countryId
     *
     * @return the country Id
     */
    public int getCountryID()
    {
        return countryID;
    }

    /**
     * Setter for countryID
     *
     * @param countryID the country ID to set
     */
    public void setCountryID(int countryID)
    {
        this.countryID = countryID;
    }

}
