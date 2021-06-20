package model;

import java.time.LocalDate;

/**
 * The reportTypeMonth Class
 *
 * @author Audrey Hababag
 */
public class reportTypeMonth
{
    private String month;
    private String type;
    private int total;

    /**
     * The constructor for reportTypeMonth Class
     *
     * @param month the month
     * @param type the type
     * @param total the total
     */
    public reportTypeMonth(String month, String type, int total)
    {
        this.month = month;
        this.type = type;
        this.total = total;
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
     * Getter for month
     *
     * @return the month
     */
    public String getMonth()
    {
        return month;
    }

    /**
     * Setter for month
     *
     * @param month the month to set
     */
    public void setMonth(String month)
    {
        this.month = month;
    }

    /**
     * Getter for total
     *
     * @return the total
     */
    public int getTotal()
    {
        return total;
    }

    /**
     * Setter for total
     *
     * @param total the total to set
     */
    public void setTotal(int total)
    {
        this.total = total;
    }
}
