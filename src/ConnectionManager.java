import com.mysql.jdbc.Connection;
//import com.mysql.jdbc.ResultSet;
//import com.mysql.jdbc.Statement;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by alima on 5/5/17.
 */
public class ConnectionManager {

    private static final ConnectionManager _instance = new ConnectionManager();

    /**
     * Declaring constants variables
     * */
//    private static final String MySQL_DB = "LPA_eComms";
//    private static final String user      = "lpaecomms";
//    private static final String pass      = "lpaecomms";
//    private static final String url       = "jdbc:mysql://phpmyadmin.app/" + MySQL_DB;

    private static final String MySQL_DB = "LPA_eComms";
    private static final String user      = "lpaecomms";
    private static final String pass      = "lpaecomms";
    private static final String url       = "jdbc:mysql://localhost/" + MySQL_DB;

    private Connection conn;

    public void connect(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection(url, user, pass);
            //st = (Statement) conn.createStatement();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet executeQuery(String sql) throws Exception {
        try{
            Statement st = conn.createStatement();
            //st.closeOnCompletion();
            return st.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error on execute the query.", e);
        }
    }

    public void executeUpdate(String sql) throws Exception {
        try{
            Statement st = conn.createStatement();
            st.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error on execute the query.", e);
        }
    }

    public void closeResultSet(ResultSet rs){
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            //e.printStackTrace();
        }
    }

    public static ConnectionManager instance(){
        return _instance;
    }
}
