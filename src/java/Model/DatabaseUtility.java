/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A utility class for retrieving the persistent data of this application. It is
 * important to be careful when editing this class. Static "initialization" may
 * not behave the way that you think. Make sure to include an initialization
 * call from ServletContextListener
 * @author Chaskin Saroff
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
    private static Map<DayOfWeek, List<GymClass>> cooperClasses = new LinkedHashMap<DayOfWeek, List<GymClass>>();
    private static Map<DayOfWeek, List<GymClass>> glimmerglassClasses = new LinkedHashMap<DayOfWeek, List<GymClass>>();
    private static String[] cooperGymHours = new String[7];
    private static String[] glimmerglassGymHours = new String[7];
    //private static int cooperCount = 0; 
    //private static int glimmerglassCount = 0;

    //Like the Math class, make this class unconstructable.
    private DatabaseUtility() {}
    
    /**
     * Retrieves the maximum number of people that have ever been in the gym at
     * a given time.
     * @param gymName The name of the gym to find the maximum count for.
     * @return the maximum number of people that have ever been in the gym at
     * the same time, or -1 if an error occurred.
     */
    public static int getMaxCount(String gymName){
        //This Method should really throw an error if an error occurs, instead
        //of returning -1.
        String sql = "SELECT MAX(count) from sensorData WHERE gymId = ?;";
        if (gymName == null) {
            throw new IllegalArgumentException("Gym Name can't be null");
        }else if(!(gymName.equalsIgnoreCase("cooper") || gymName.equalsIgnoreCase("glimmerglass"))) {
            throw new IllegalArgumentException("Gym Name must be either cooper or glimmerglass");
        }
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseUtility.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return -1;
        }
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)){
            PreparedStatement stmt = null;
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
     * @return the number of people currently in gymName or -1 if an error occurred.
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
        
        try {         
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseUtility.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return -1;
        }
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)){
            PreparedStatement stmt = null;
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
     * @return a list of all GymClass objects associated with gymName and 
     * dayOfWeek or null if an error occurs.
     */
    public List<GymClass> getGymClassesFor(String gymName, DayOfWeek dayOfWeek) {
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
        return null;
    }

    /**
     * Updates the current count of gymName with count and saves it in the 
     * database.
     * @param gymName The name of the gym whose count will be updated.
     * @param timestamp The timestamp for the sensor that recorded the count.
     * @param count The number of people in the gym at time timestamp.
     * @return true if the database and model both updated successfully and
     * false otherwise.
     */
    public static boolean updateCount(String gymName, java.util.Date timestamp, int count) {
        if (gymName == null) {
            throw new IllegalArgumentException("Gym Name can't be null");
        } else if (!(gymName.equalsIgnoreCase("cooper") || gymName.equalsIgnoreCase("glimmerglass"))) {
            throw new IllegalArgumentException("Gym Name must be either cooper or glimmerglass");
        }
        
        PreparedStatement pstmt = null;
        String tableName = "sensorData";
        String columns = "sensorTimeStamp, count, gymID";
        boolean error = false;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseUtility.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return false;
        }
        try (
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)){
            String sql = "INSERT INTO " + tableName + " ("
                    + columns + ") VALUES(" + "?, ?, ?" + ");";
            
            System.out.println(sql);
            pstmt = conn.prepareStatement(sql);
            
            
            System.out.println("Inserting records into the table...");
            pstmt.setTimestamp(1, new java.sql.Timestamp(timestamp.getTime()));
            pstmt.setInt(2, count);
            pstmt.setString(3, gymName.toLowerCase());

            //STEP 4: Execute a query
            pstmt.executeUpdate();
            System.out.println("Inserted records into the table...");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Initialize the class information and gym hours, etc. This loads all the
     * class information into memory, at the start of the program, preventing
     * additional disk reads which are slow and expensive.
     */
    public static void init() throws IOException {
        //Map<String, String> classDescriptions = readClassDescriptions();
        cooperClasses = getGymClasses("cooper");
        glimmerglassClasses = getGymClasses("glimmerglass");
        for(DayOfWeek className : cooperClasses.keySet()){
            System.out.println(className.toString() + cooperClasses.get(className));
        }
        for(DayOfWeek className : glimmerglassClasses.keySet()){
            System.out.println(className.toString() + cooperClasses.get(className));
        }    
    }
    
    /**
     * Retrieves the class descriptions from a csv file.
     * @return a map from class name to class description.
     * @throws IOException 
     */
    private static Map<String, String> readClassDescriptions()throws IOException{
        System.out.println("readClassDescriptions()");
        String path = "/WEB-INF/GymData/ClassDescriptions/ClassDescriptions.csv";
        Map<String, String> classDescriptions = new HashMap<String, String>(); 
        CSVReader reader = null;
        reader = new CSVReader(
                //Requires StartupShutdownListener run it's startup method first.
                new InputStreamReader(StartupShutdownListener.getContext()
                        .getResourceAsStream(path)));
        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            classDescriptions.put(nextLine[0], nextLine[1]);
        }
        return classDescriptions;
    }
    
    /**
     * Retrieves a collection of all of the gym classes for the given gymName.
     * This implementation reads a csv file from disk, and so will be generally
     * slow.  Avoid excessive calls to this method by cacheing the result.
     * @param gymName The name of the gym to retrieve classes for.
     * @return A map from the day of the week to the classes offered on that day.
     * @throws IOException 
     */
    private static Map<DayOfWeek, List<GymClass>> getGymClasses(String gymName) throws IOException {
        String path;
        Map<String, String> classDescriptions = readClassDescriptions();
        Map<DayOfWeek, List<GymClass>> gymClassesOnDay = new HashMap<>();
        CSVReader reader = null;
        if (gymName == null) {
            throw new IllegalArgumentException("Gym Name can't be null");
        }
        if (gymName.trim().equalsIgnoreCase("cooper")) {
            path = csvPath + cooperClassFileName;
            //gymClassesOnDay = cooperClasses;
        } else if (gymName.trim().equalsIgnoreCase("glimmerglass")) {
            path = csvPath + glimmerglassClassFileName;
            //gymClassesOnDay = glimmerglassClasses;
        } else {
            throw new IllegalArgumentException("Gym Name must be either cooper or glimmerglass");
        }
            //System.out.println("input =  " + path);
        //System.out.println("crrctvsn " +"/WEB-INF/GymData/GymClasses/Cooper.csv");
        reader = new CSVReader(
                //Requires StartupShutdownListener run it's startup method first.
                new InputStreamReader(StartupShutdownListener.getContext()
                        .getResourceAsStream(path)));
        for (DayOfWeek weekday : DayOfWeek.values()) {
            gymClassesOnDay.put(weekday, new ArrayList<GymClass>());
        }
        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            String description = classDescriptions.get(nextLine[2]);
            if(description==null){description = "";}
            DayOfWeek weekday = DayOfWeek.valueOf(nextLine[0].trim().toUpperCase(Locale.ENGLISH));
            gymClassesOnDay.get(weekday).add(new GymClass(nextLine[2], description, weekday, nextLine[1], nextLine[3]));
        }
        return gymClassesOnDay;
    }
}