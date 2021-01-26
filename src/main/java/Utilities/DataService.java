package Utilities;

/**
 * @author Raghvendra Gupta
 * @author Last updated by Raghvendra Gupta: $
 * @version : $
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataService {

    private static Connection con;
    private static final String Driver = "oracle.jdbc.driver.OracleDriver";
    private static String ConnectionString = "";
    private static String user = "";
    private static String pwd = "";
    private static ResultSet rs1;

    /**
     * create Database object
     */
    public DataService(String connectionString,String user, String pwd) {
        this.ConnectionString=connectionString;
        this.user=user;
        this.pwd=pwd;
    }

    /**
     * to load the database base driver
     * @return a database connection
     * @throws SQLException throws an exception if an error occurs
     */
    public static Connection loadDriver() throws SQLException {
        try {
            Class.forName(Driver);
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        con = DriverManager.getConnection(ConnectionString, user, pwd);
        return con;
    }

    /**
     * to get a result set of a query
     * @param query custom query
     * @return a result set of custom query
     * @throws SQLException throws an exception if an error occurs
     */
    public static ResultSet getResultSet(String query) throws SQLException {
        Connection con = loadDriver();
        ResultSet rs;
        PreparedStatement st = con.prepareStatement(query);
        rs = st.executeQuery();

        return rs;
    }

    /**
     * to run an update query such as update, delete
     * @param query custom query
     * @throws SQLException throws an exception if an error occurs
     */
    public static void runQuery(String query) throws SQLException {
        Connection con = loadDriver();
        ResultSet rs;
        PreparedStatement st = con.prepareStatement(query);
        st.executeUpdate();
    }

    public static void main(String[] args) throws SQLException {
        DataService dataService=new DataService("jdbc:oracle:thin:@MD1NPDLNXDB08:1521/PM12R2A","WWFMGR2019","wwfmgr2019");
        rs1=getResultSet("select pool_name,host_name,pool_state from sre_node_pool where host_name=\'MD1NPDVPMPEP01\'");
        while(rs1.next())
        {
            String pool_name = rs1.getString("pool_name");
            String host_name = rs1.getString(2);
            int pool_state = rs1.getInt(3);
            System.out.println(pool_name + " " + host_name + " " + pool_state);
            System.out.println("---------------");
        }
    }
}
