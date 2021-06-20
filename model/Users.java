package model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * The Users Class
 *
 * @author Audrey Hababag
 */
public class Users
{
    private static int userID;
    private static String userName;
    private static String password;
    private static LocalDateTime createDate;
    private static String createdBy;
    private static Timestamp lastUpdate;
    private static String lastUpdatedBy;
    private static int countryID;

    /**
     * The Users Constructor
     *
     * @param userID the user ID
     * @param userName the user name
     * @param password the password
     * @param createDate the creation date
     * @param createdBy the creator user name
     * @param lastUpdate the date of last update
     * @param lastUpdatedBy the updater username
     * @param countryID the country ID
     */
    public Users(int userID, String userName, String password, LocalDateTime createDate,
      String createdBy, Timestamp lastUpdate, String lastUpdatedBy, int countryID)
    {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.countryID = countryID;
    }

    public Users(int userID, String userName, String password)
    {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
    }

    /**
     * Setter for userID
     *
     * @param userID the user ID to set
     */
    public static void setuserID(int userID)
    {
        Users.userID = userID;
    }

    /**
     * Setter for userName
     *
     * @param userName the username to set
     */
    public static void setuserName(String userName)
    {
        Users.userName = userName;
    }

    /**
     * Setter for password
     *
     * @param password the password to set
     */
    public static void setPassword(String password)
    {
        Users.password = password;
    }

    /**
     * Setter for createDate
     *
     * @param createDate the date of creation to set
     */
    public static void setcreateDate(LocalDateTime createDate)
    {
        Users.createDate = createDate;
    }

    /**
     * Setter for createdBy
     *
     * @param createdBy the username of creator
     */
    public static void setcreatedBy(String createdBy)
    {
        Users.createdBy = createdBy;
    }

    /**
     * Setter for lastUpdate
     *
     * @param lastUpdate the date of last update to set
     */
    public static void setlastUpdate(Timestamp lastUpdate)
    {

        Users.lastUpdate = lastUpdate;
    }

    /**
     * Setter for lastUpdatedBy
     *
     * @param lastUpdatedBy the last updater username
     */
    public static void setlastUpdatedBy(String lastUpdatedBy)
    {
        Users.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * Setter for countryID
     *
     * @param countryID the country ID to set
     */
    public static void setcountryID(int countryID)
    {
        Users.countryID = countryID;
    }

    /**
     * Getter for userID
     *
     * @return the user Id to set
     */
    public static int getuserID()
    {
        return userID;
    }

    /**
     * Getter for userName
     *
     * @return the username
     */
    public static String getuserName()
    {
        return userName;
    }

    /**
     * Getter for password
     *
     * @return the password
     */
    public static String getPassword()
    {
        return password;
    }

    /**
     * Getter for createDate
     *
     * @return the creation date
     */
    public static LocalDateTime getcreateDate()
    {
        return createDate;
    }

    /**
     * Getter for createdBy
     *
     * @return the creator username
     */
    public static String getcreatedBy()
    {
        return createdBy;
    }

    /**
     * Getter for lastUpdate
     *
     * @return the date of last update
     */
    public static Timestamp getlastUpdate()
    {
        return lastUpdate;
    }

    /**
     * Getter for lasupdatedBy
     *
     * @return the last updater username
     */
    public static String getlastUpdatedBy()
    {
        return lastUpdatedBy;
    }

    /**
     * Getter for countryID
     *
     * @return the country ID
     */
    public static int getcountryID()
    {
        return countryID;
    }

}
