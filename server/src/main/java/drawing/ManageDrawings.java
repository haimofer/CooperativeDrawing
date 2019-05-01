package drawing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;

@Service
public class ManageDrawings {
    private static final Logger logger = LogManager.getLogger(ManageDrawings.class);
    private static final int MAX_CANVAS_SIZE = 21844;

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:~/test";

    //  Database credentials
    static final String USER = "sa";
    static final String PASS = "";


    public static void addNewDrawing(CanvasData data) throws Exception {
        validateData(data);

        Connection conn = null;
        Statement stmt = null;
        try {
            conn = openConnection();
            logger.info("Connected database successfully...");

            //Execute a query
            stmt = conn.createStatement();
            String sql;
            try {
                //insert new data:
                sql = "INSERT INTO DRAWINGS(IP, NAME, DRAWING)" +
                        " VALUES('" + data.getIp() + "','" + data.getName() + "','" + data.getCanvas() + "')";
                stmt.executeUpdate(sql);
                logger.info("Inserted a new record into the table...");

            } catch (Exception e) {
                logger.error(e);
                throw new Exception("Error in inserting data to the DB");
                /*//update an old value- optional
                sql = "UPDATE DRAWINGS\n" +
                        "SET DRAWING = '" + data.getCanvas() + "'\n" +
                        "WHERE IP = '" + data.getIp() + "' AND NAME = '" + data.getName()+"'";
                stmt.executeUpdate(sql);
                logger.info("Inserted an old record into the table...");*/
            }

            //Clean-up environment
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt);
        }
    }

    private static void validateData(CanvasData data) throws Exception {
        if (data.getCanvas() == null) {
            throw new Exception("Error: drawing is missing");
        }
        if (data.getCanvas().length() > MAX_CANVAS_SIZE) {
            throw new Exception("Error: drawing is too big");
        }
        if (data.getIp() == null) {
            data.setIp("DEFAULT");
        }
        if (data.getName() == null) {
            data.setName("anonymous");
        }
    }

    public static ArrayList<CanvasData> getAllResults() throws Exception {
        logger.info("Trying to get data from DB");
       ArrayList<CanvasData> res = null;

        //-------------------------------------------------------------------
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = openConnection();
            logger.info("Connected database successfully...");

            //Execute query
            stmt = conn.createStatement();
            String sql = "SELECT * FROM DRAWINGS";
            //TODO: select name only, get every drawing's data only when it is chosen with a separate query to the DB
            ResultSet rs = stmt.executeQuery(sql);

            res = extractDataFromResultSet(rs);

            //Clean-up environment
            rs.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            closeResources(conn, stmt);
        }

        return res;
    }

    private static ArrayList<CanvasData> extractDataFromResultSet(ResultSet rs) throws SQLException {
        ArrayList<CanvasData> results = new ArrayList<>();
        while (rs.next()) {
            results.add(new CanvasData(rs.getString("IP"), rs.getString("NAME"), rs.getString("DRAWING")));
        }
        return results;
    }

    private static Connection openConnection() throws ClassNotFoundException, SQLException {
        //Register JDBC driver
        Class.forName(JDBC_DRIVER);

        //Open a connection
        logger.info("Connecting to a selected database...");
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    private static void closeResources(Connection conn, Statement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException se2) {
            se2.printStackTrace();
        }

        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
}
