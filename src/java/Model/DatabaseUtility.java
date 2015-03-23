/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author csaroff
 */
import Endpoints.StartupShutdownListener;
import java.util.List;
import java.util.ArrayList;
import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.servlet.ServletContext;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A utility class for getting persistent data from the database. It is
 * important to be careful when editing this class. Static "initialization" may
 * not behave the way that you think. Make sure to include an initialization
 * call from ServletContextListener
 */
public class DatabaseUtility {

    //The array index is the day of the week 
    //as an integer with Sunday as 0, etc
    //Each day has a List of classes...
    private static final String DB_URL = "jdbc:mysql://127.0.0.1/csc480";
    private static final String USER = "root";
    private static final String PASS = "password";

    private static final String csvPath = "/WEB-INF/GymData/GymClasses/";
    private static final String cooperClassFileName = "Cooper.csv";
    private static final String glimmerglassClassFileName = "Glimmerglass.csv";
    
    //Should be using enumerations for the Gyms, but just discovered them.
    private static Map<Weekday, List<GymClass>> cooperClasses = new LinkedHashMap<Weekday, List<GymClass>>();
    private static Map<Weekday, List<GymClass>> glimmerglassClasses = new LinkedHashMap<Weekday, List<GymClass>>();
    private static String[] cooperGymHours = new String[7];
    private static String[] glimmerglassGymHours = new String[7];
    //private static int cooperCount = 0; 
    //private static int glimmerglassCount = 0;

    //Like the Math class, make this class unconstructable.
    private DatabaseUtility() {}
    
    /**
     * Calculate the most people that have ever been in the gym at a given time.
     * @param gymName The name of the gym to find the maximum count for.
     * @return the maximum number of people that have ever been in the gym at
     * the same time, or -1 if an error occured.
     */
    public static int getMaxCount(String gymName){
        String sql = "SELECT MAX(count) from sensorData WHERE gymId = ?;";
        if (gymName == null) {
            throw new IllegalArgumentException("Gym Name can't be null");
        }else if(!(gymName.equalsIgnoreCase("cooper") || gymName.equalsIgnoreCase("glimmerglass"))) {
            throw new IllegalArgumentException("Gym Name must be either cooper or glimmerglass");
        }
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)){
            PreparedStatement stmt = null;
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to a selected database...");
            
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, gymName);
            System.out.println(stmt);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }else{
                System.out.println("The database is empty");
            }
        }catch(SQLException sqle){
            sqle.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
        return -1;
    }   
    
    /**
     * Retrieves the most recent number of gymRats in the given gym.
     * @param gymName the name of the gym 
     * @return the number of people currently in gymName or -1 if an error occured.
     */
    public static int getCount(String gymName){
        String tableName = "sensorData";
        String sql = "SELECT count FROM sensorData WHERE sensorTimeStamp IN "
                + "(SELECT MAX(sensorTimeStamp) FROM sensorData where gymID = ? );";
        
        if (gymName == null) {
            throw new IllegalArgumentException("Gym Name can't be null");
        }else if(!(gymName.equalsIgnoreCase("cooper") || gymName.equalsIgnoreCase("glimmerglass"))) {
            throw new IllegalArgumentException("Gym Name must be either cooper or glimmerglass");
        }
        
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)){
            PreparedStatement stmt = null;
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to a selected database...");
            
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, gymName);
            System.out.println(stmt);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }else{
                System.out.println("The database is empty");
            }
        }catch(SQLException sqle){
            sqle.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
        return -1;
    }
    
    /**
     * Retrieves a list of GymClass objects associated with gymName on dayOfWeek.
     * This implementation reads in all GymClass data on server startup in order
     * to decrease latency time for data retrieval.
     * @param gymName The name of the gym to retrieve GymClasses for
     * @param dayOfWeek The day of the week to retrieve GymClasses for
     * @return a list of all GymClass objects associated with gymName and dayOfWeek.
     */
    public List<GymClass> getGymClassesFor(String gymName, Weekday dayOfWeek) {
        if (gymName == null) {
            throw new IllegalArgumentException("Gym Name can't be null");
        }else if (!(gymName.equalsIgnoreCase("cooper") || gymName.equalsIgnoreCase("glimmerglass"))) {
            throw new IllegalArgumentException("Gym Name must be either cooper or glimmerglass");
        }

        if (gymName.equalsIgnoreCase("cooper")) {
            return cooperClasses.get(dayOfWeek);
        } else if (gymName.equalsIgnoreCase("glimmerglass")) {
            return glimmerglassClasses.get(dayOfWeek);
        }

        //How could this happen?
        assert false;
        return null;
    }

    /**
     * Updates the current count of gymName with count and saves it in the 
     * database.
     * @param gymName The name of the gym to update.
     * @param date The timestamp for that the sensor recorded the count.
     * @param count The number of people currently in the gym.
     * @return true if the database and model both updated successfully and
     * false otherwise.
     */
    public static boolean updateCount(String gymName, java.util.Date date, int count) {
        if (gymName == null) {
            throw new IllegalArgumentException("Gym Name can't be null");
        } else if (!(gymName.equalsIgnoreCase("cooper") || gymName.equalsIgnoreCase("glimmerglass"))) {
            throw new IllegalArgumentException("Gym Name must be either cooper or glimmerglass");
        }
        
        PreparedStatement pstmt = null;
        String tableName = "sensorData";
        String columns = "sensorTimeStamp, count, gymID";
        boolean error = false;
        try (Connection conn){
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            
            //STEP 3: Open a connection
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected database successfully...");

            
            String sql = "INSERT INTO " + tableName + " ("
                    + columns + ") VALUES(" + "?, ?, ?" + ");";
            
            System.out.println(sql);
            pstmt = conn.prepareStatement(sql);
            
            
            System.out.println("Inserting records into the table...");
            pstmt.setTimestamp(1, new java.sql.Timestamp(date.getTime()));
            pstmt.setInt(2, count);
            pstmt.setString(3, gymName.toLowerCase());

            //STEP 4: Execute a query
            pstmt.executeUpdate();
            System.out.println("Inserted records into the table...");

        } catch (Exception e) {
            e.printStackTrace();
            error=true;
        }
        return error;
    }

    /**
     * Initialize the class information and gym hours, etc. This loads all the
     * class information into memory, at the start of the program, preventing
     * additional disk reads which are slow and expensive.
     */
    public static void init() throws FileNotFoundException, IOException {
        initGymClasses("cooper");
        initGymClasses("glimmerglass");
    }

    private static void initGymClasses(String gymName) throws FileNotFoundException, IOException {
        String path;
        Map<Weekday, List<GymClass>> gymClassesOnDay;
        CSVReader reader = null;
        if (gymName == null) {
            throw new IllegalArgumentException("Gym Name can't be null");
        }

        if (gymName.trim().equalsIgnoreCase("cooper")) {
            path = csvPath + cooperClassFileName;
            gymClassesOnDay = cooperClasses;
        } else if (gymName.trim().equalsIgnoreCase("glimmerglass")) {
            path = csvPath + glimmerglassClassFileName;
            gymClassesOnDay = glimmerglassClasses;
        } else {
            throw new IllegalArgumentException("Gym Name must be either cooper or glimmerglass");
        }
            //System.out.println("input =  " + path);
        //System.out.println("crrctvsn " +"/WEB-INF/GymData/GymClasses/Cooper.csv");
        reader = new CSVReader(
                //Requires StartupShutdownListener run it's startup method first.
                new InputStreamReader(StartupShutdownListener.getContext()
                        .getResourceAsStream(path)));
        for (Weekday weekday : Weekday.values()) {
            gymClassesOnDay.put(weekday, new ArrayList<GymClass>());
        }
        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            Weekday weekday = Weekday.valueOf(nextLine[0].trim().toUpperCase(Locale.ENGLISH));
            gymClassesOnDay.get(weekday).add(new GymClass(weekday, nextLine[1], nextLine[2], nextLine[3]));
        }
    }
}
