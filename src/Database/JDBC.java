package Database;

import java.sql.Connection;
import java.sql.DriverManager;

/**This class creates and closes the connection to a MySQL Database.*/
public class JDBC {

    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/"; // 127.0.0.1 */ /** PORT: 3306
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER";
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String userName = "sqlUser";
    private static final String password = "Passw0rd!";

    /** Connection the DAO classes use */
    public static Connection connection;

    public static Connection getConnection(){return connection;}

    /** Connects to the database. */
    public static void openConnection()
    {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(jdbcUrl, userName, password);
            System.out.println("Connection has been established!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
            e.printStackTrace();
        }
    }


    /** Terminate the connection */
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection has been destroyed!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
            e.printStackTrace();
        }
    }
}
