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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
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
    private static String DB_URL;
    private static final String USER = "gym";
    private static String PASS;
    
    //Like the Math class, make this class unconstructable.
    private DatabaseUtility() {}
    
    /**
     * Retrieves the maximum number of people that have ever been in the gym at
     * a given time.
     * @param gym The gym to find the maximum count for.
     * @return the maximum number of people that have ever been in the gym at
     * the same time.
     * @throws java.sql.SQLException if the database was empty, or an error
     * occurred while reading from the database.
     */
    public static int getMaxCount(Gym gym)throws SQLException{
        String sql = "SELECT MAX(count) from sensorData WHERE gymId = ?;";
        if (gym == null) {
            throw new IllegalArgumentException("Gym Name can't be null");
        }
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseUtility.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)){
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, gym.name());
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }else{
                throw new SQLException("The database is empty");
            }
        }
    }   
    
    /**
     * Retrieves the most recent number of gym rats in the given gym.
     * @param gym the gym to retrieve the current number of customers for.
     * @return the number of people currently in gymName.
     * @throws java.sql.SQLException if the database was empty, or an error occured.
     */
    public static int getCount(Gym gym)throws SQLException{
        String tableName = "sensorData";
        String sql = "SELECT count FROM sensorData WHERE sensorTimeStamp IN "
                + "(SELECT MAX(sensorTimeStamp) FROM sensorData where gymID = ? );";
        
        if (gym == null) {
            throw new IllegalArgumentException("Gym Name can't be null");
        }
        try {         
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseUtility.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)){
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, gym.name());
            System.out.println(stmt);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }else{
                throw new SQLException("The database is empty");
            }
        }
    }
    
    /**
     * Retrieves a list of GymClass objects associated with gymName on dayOfWeek.
     * This implementation reads in all GymClass data on server startup in order
     * to decrease latency time for data retrieval.
     * @param gym The gym to retrieve GymClasses for
     * @param dayOfWeek The day of the week to retrieve GymClasses for
     * @return a list of all GymClass objects associated with gymName and 
     * dayOfWeek.
     */
    public static List<GymClass> getGymClassesFor(Gym gym, DayOfWeek dayOfWeek) {
        if (gym == null) {
            throw new IllegalArgumentException("Gym can't be null");
        }        
        return gym.getClassSchedule().get(dayOfWeek);
    }

    /**
     * Updates the current count of gymName with count and saves it in the 
     * database.
     * @param gym The gym whose count will be updated.
     * @param timestamp The timestamp for the sensor that recorded the count.
     * @param count The number of people in the gym at time timestamp.
     * @throws java.sql.SQLException 
     */
    public static void updateCount(Gym gym, java.util.Date timestamp, int count) throws SQLException {
        if (gym == null) {
            throw new IllegalArgumentException("Gym Name can't be null");
        }
        PreparedStatement pstmt;
        String tableName = "sensorData";
        String columns = "sensorTimeStamp, count, gymID";
        boolean error = false;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseUtility.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
        try (
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)){
            String sql = "INSERT INTO " + tableName + " ("
                    + columns + ") VALUES(" + "?, ?, ?" + ");";
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setLong(1, timestamp.getTime());
            pstmt.setInt(2, count);
            pstmt.setString(3, gym.name());
            pstmt.executeUpdate();
        }
    }

    /**
     * Initialize the class information and gym hours, etc. This loads all the
     * class information into memory, at the start of the program, preventing
     * additional disk reads which are slow and expensive.
     * @throws java.io.FileNotFoundException
     */
    public static void init() throws IOException, FileNotFoundException {
        //Map<String, String> classDescriptions = readClassDescriptions();
        Gym.COOPER.setClassSchedule(getGymClasses(Gym.COOPER));
        Gym.GLIMMERGLASS.setClassSchedule(getGymClasses(Gym.GLIMMERGLASS));
        DB_URL=getDBUrl();
        PASS=getDBPassword();
    }
    
    /**
     * Retrieves the database url based on the DEPLOYMENT environment variable.
     * Options are DEPLOYMENT="dev" or DEPLOYMENT="prod".  
     * Try export DEPLOYMENT="prod" from the shell before running the app server
     * in production.
     * @return the url associated with the jdbc database connection for this 
     * deployment environment.
     */
    private static String getDBUrl(){
        final String devPath="jdbc:mysql://localhost/gym";
        final String prodPath="jdbc:mysql://moxie.cs.oswego.edu/gym";
        if(System.getenv("DEPLOYMENT")==null){
            System.out.println("The DEPLOYMENT environment variable is not set."
                    + "defaulting to DEPLOYMENT=dev");
            return devPath;
        }else if(System.getenv("DEPLOYMENT").equals("dev")||
                 System.getenv("DEPLOYMENT").equals("development")){
            return devPath;
        }else if(System.getenv("DEPLOYMENT").equals("prod")||
                 System.getenv("DEPLOYMENT").equals("production")){
            return prodPath; 
        }else{
            System.out.println("The DEPLOYMENT env variable is currently set to "
                    + System.getenv("DEPLOYMENT") + ".");
            System.out.println("Acceptable DEPLOYMENT "
                    + "values are dev, prod, development or production");
            System.out.println("Defaulting to DEPLOYMENT=dev");
            return devPath;
        }
    }
    
    /**
     * Retrieves the plaintext db password from the password.db file.  This 
     * configuration file should only have read permission from the account that
     * that is running the application server.  password.db should also be in
     * .gitignore to ensure that the database password stays private.
     * @return the plaintext database password.
     * @throws FileNotFoundException
     */
    private static String getDBPassword() throws FileNotFoundException{
        final String path = "/WEB-INF/password.db";
        Scanner sc = new Scanner(new InputStreamReader(
                StartupShutdownListener.getContext().getResourceAsStream(path)));
        if(sc.hasNext()){
            return sc.next();
        }else{
            throw new FileNotFoundException("Missing the password.db file "
                    + "located at " + path  + "\npassword.db is a text file "
                    + "containing only the production database password "
                    + "without any meta information");
        }
    }
    
    /**
     * Retrieves the class descriptions from a csv file.
     * @return a map from class name to class description.
     * @throws IOException if an error occurred while reading the csv file.
     */
    private static Map<String, String> readClassDescriptions()throws IOException{
        System.out.println("readClassDescriptions()");
        final String path = "/WEB-INF/GymData/ClassDescriptions/ClassDescriptions.csv";
        Map<String, String> classDescriptions = new HashMap<>(); 
        CSVReader reader = new CSVReader(
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
     * slow.  Avoid excessive calls to this method by caching the result.
     * @param gym The gym to retrieve classes for.
     * @return A map from the day of the week to the classes offered on that day.
     * @throws IOException 
     */
    private static Map<DayOfWeek, List<GymClass>> getGymClasses(Gym gym) throws IOException {
        String path;
        final String csvPath = "/WEB-INF/GymData/GymClasses/";
        Map<String, String> classDescriptions = readClassDescriptions();
        Map<DayOfWeek, List<GymClass>> gymClassesOnDay = new HashMap<>();
        CSVReader reader;
        if (gym == null) {
            throw new IllegalArgumentException("Gym can't be null");
        }
        path = csvPath + gym.classScheduleFileName;
        reader = new CSVReader(
                //Requires StartupShutdownListener run it's startup method first.
                new InputStreamReader(StartupShutdownListener.getContext()
                        .getResourceAsStream(path)));
        for (DayOfWeek weekday : DayOfWeek.values()) {
            gymClassesOnDay.put(weekday, new ArrayList<>());
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
