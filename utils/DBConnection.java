package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The Database Connection File
 *
 * @author Audrey Hababag
 */
public class DBConnection
{

    // JDBC URL parts
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String serverName = "//wgudb.ucertify.com/";
    private static final String database = "WJ07V15";

    // JDBC URL
    private static final String jdbcURL = protocol + vendorName + serverName + database;

    // Driver Interface Reference
    private static final String mysqlJDBCdriver = "com.mysql.cj.jdbc.Driver";
    private static Connection conn = null;

    // Username and password
    private static final String userName = "U07V15";
    private static final String password = "53689139269";

    public static Connection startConnection()
    {
        ResourceBundle rb = ResourceBundle.getBundle("schedapp/loginForm", Locale.getDefault());

        try
        {
            Class.forName(mysqlJDBCdriver);
            conn = DriverManager.getConnection(jdbcURL, userName, password);
            System.out.println("\n" + rb.getString("connTrue"));
        }
        catch(ClassNotFoundException ex)
        {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            
        }
        return conn;
    }

    public static void closeConnection() throws SQLException
    {
        ResourceBundle rb = ResourceBundle.getBundle("schedapp/loginForm", Locale.getDefault());

        try
        {
            conn.close();
            System.out.println("\n" + rb.getString("connFalse"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public static Connection getConnection()
    {
        return conn;
    }

}
