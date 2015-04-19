/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import endpoints.StartupShutdownListener;
import java.util.List;
import java.util.ArrayList;
import com.opencsv.CSVReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.containers.CountDatapoint;
import java.util.Timer;

/**
 * A utility class for retrieving the persistent data of this application. 
 * Make sure to include an initialization call from ServletContextListener
 * @author Chaskin Saroff
 */
public class DatabaseUtility {
    
    private static final Timer timer = new Timer();
    private static String DB_URL;
    private static String USER;
    private static String PASS;
    
    //Like the java.lang.Math class, make this class unconstructable.
    private DatabaseUtility() {}
    
    /**
     * Retrieves the maximum number of people that have ever been in the gym at
     * a given time.
     * @param gym The gym to find the maximum count for.
     * @return the maximum number of people that have ever been in the gym at
     * the same time.
     * @throws java.sql.SQLException if the database was empty
     */
    public static int getMaxCount(Gym gym)throws SQLException{
        validateArgument(gym);
        String sql = "SELECT MAX(count) from sensorData WHERE gymName = ?;";
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
     * Retrieves the number of people who were in the given gym on the most
     * recent completed given day.
     * @param gym the gym.
     * @param day the most recent day that is completely over.
     * @return a list of CountDatapoints representing a days worth of entries of
     * the number of people in the gym at a given time.
     */
    public static List<CountDatapoint> getCountData(Gym gym, DayOfWeek day) {
        validateArgument(gym); validateArgument(day);
        String sql = "SELECT count, sensorTimeStamp "
                + "FROM sensorData "
                + "WHERE gymName = ? AND sensorTimeStamp "
                + "BETWEEN " + DayOfWeek.getStartOfDayForLast(day) + " "
                + "AND " + DayOfWeek.getEndOfDayForLast(day) + ";";
        List<CountDatapoint> datapointList = new ArrayList<>();
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)){
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, gym.name());
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                datapointList.add(new CountDatapoint(rs.getInt("count"), rs.getLong("sensorTimeStamp")));
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return datapointList;
    }
    
    /**
     * Retrieves the most recent number of gym rats in the given gym.
     * @param gym the gym to retrieve the current number of customers from.
     * @return the number of people currently in gymName.
     */
    public static int getCurrentCount(Gym gym){
        validateArgument(gym);
        return gym.getCurrentCount();
    }
    
    /**
     * Retrieves a list of GymClass objects associated with gymName on dayOfWeek.
     * This implementation reads in all GymClass data on server startup in order
     * to decrease latency time for data retrieval.
     * @param gym The gym to retrieve GymClasses from
     * @param day The day of the week to retrieve GymClasses for
     * @return a list of all GymClass objects associated with gymName and 
     * dayOfWeek.
     */
    public static List<GymClass> getGymClasses(Gym gym, DayOfWeek day) {
        validateArgument(gym); validateArgument(day);
        return gym.getClassSchedule().get(day);
    }

    /**
     * Updates the current count of gymName with count and saves it in the 
     * database.
     * @param gym The gym whose count will be updated.
     * @param timestamp The timestamp for the sensor that recorded the count.
     * @param count The number of people in the gym at time timestamp.
     * @param in true if newCount was recorded when someone was entering the gym
     * and false otherwise
     * @throws java.sql.SQLException 
     */
    public static void updateCurrentCount(Gym gym, java.util.Date timestamp, int count, boolean in) throws SQLException {
        gym.setCurrentCount(count, timestamp, in);
        validateArgument(gym);
        
        PreparedStatement pstmt;
        String tableName = "sensorData";
        String columns = "sensorTimeStamp, count, gymName";
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
     * Updates the average number of people who have come into the gym on day 
     * with inCount.  Note that this method should ONLY be called by an instance
     * of the CountAverager and should only average values for the current day.
     * @param gym the gym.
     * @param day the day.
     * @param inCount the number of people who entered the given gym on the 
     * given day.
     * @throws IllegalArgumentException if gym==null || day==null || 
     * day != today.
     * @throws SQLException if the database is empty
     */
    static void updateAverageInCount(Gym gym, DayOfWeek day, int inCount)throws SQLException{
        validateArgument(gym); validateArgument(day);
        if(!DayOfWeek.today().equals(day)) 
            throw new IllegalArgumentException("This method should only "
                    + "be called with the day argument equal to today");
        PreparedStatement pstmt;
        String tableName = "averageInTable";
        int inAverage;
        int numberOfWeeks;
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)){
            String sql = "SELECT " + "averageIns, numberOfWeeks" + " FROM "
                    + tableName + " WHERE " + "gymName = ? AND dayOfWeek = ? ;";
            System.out.println(sql);
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, gym.name());
            pstmt.setString(2, day.name());
            System.out.println(pstmt);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                inAverage = rs.getInt(1);
                numberOfWeeks = rs.getInt(2);
            }else{
                inAverage = 0;
                numberOfWeeks = 0;
            }
        }
        inAverage = (inAverage*numberOfWeeks + inCount)/(numberOfWeeks+1);
        numberOfWeeks++;
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)){
            String sql = 
                    "UPDATE " + tableName + " " 
                    + "SET averageIns = ?, numberOfWeeks= ? " 
                    + "WHERE gymName = ? AND dayOfWeek = ? ;";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, inAverage);
            pstmt.setInt(2, numberOfWeeks);
            pstmt.setString(3, gym.name());
            pstmt.setString(4, day.name());
            System.out.println(pstmt);
            pstmt.executeUpdate();            
        }
    }
    
    /**
     * Retrieves the average count of the number of people who came in to the
     * given Gym for the given DayOfWeek.
     * @param gym the gym
     * @param day the day
     * @return the average number of people who entered the given Gym for the
     * given DayOfWeek.
     * @throws SQLException if a database error occurred
     */
    public int getAverageInCount(Gym gym, DayOfWeek day)throws SQLException{
        validateArgument(gym); validateArgument(day);
        PreparedStatement pstmt;
        String tableName = "averageInTable";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)){
            String sql = "SELECT " + "averageIns" + " FROM " + tableName + 
                    "WHERE " + "gymName = ? AND dayOfWeek = ? ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, gym.name());
            pstmt.setString(2, day.name());
            
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }else{
                throw new SQLException("The database is empty");
            }
        }
    }
    
    /**
     * Retrieves the number of people who have walked into the given gym today.
     * @param gym the gym
     * @return the number of people who have walked into the given gym today.
     */
    public static int getInCountToday(Gym gym){
        validateArgument(gym);
        return gym.getInCountToday();
    }
    
    /**
     * Attempts to load the com.mysql.jdbc.Driver, throwing a 
     * ClassNotFoundException wrapped in a RuntimeException if the mysql Driver
     * is not in your classpath.
     * @throws RuntimeException if the JDBC driver class wasn't found in the 
     * classpath.
     */
    private static void loadJDBCDriver()throws RuntimeException{
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseUtility.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }
    
    /**
     * Checks to ensure that day is a valid method argument, throwing an 
     * IllegalArgumentException if it isn't.
     * @param day the day
     * @throws IllegalArgumentException if day==null.
     */
    private static void validateArgument(DayOfWeek day)throws IllegalArgumentException{
        if(day==null)
            throw new IllegalArgumentException("argument day must not be null");
    }
    
    /**
     * Checks to ensure that gym is a valid method argument, throwing an
     * IllegalArgumentException if it isn't.  This method is meant to be called
     * by other methods to ensure that their arguments are valid.
     * @param gym the gym
     * @throws IllegalArgumentException if gym==null.
     */
    private static void validateArgument(Gym gym)throws IllegalArgumentException{
        if(gym==null)
            throw new IllegalArgumentException("argument gym must not be null");
    }
    
    /**
     * Initialize the class information and gym hours, etc. This loads all the
     * class information into memory, at the start of the program, preventing
     * additional disk reads which are slow and expensive.
     * @throws java.io.FileNotFoundException
     */
    public static void init() throws IOException{
        //Map<String, String> classDescriptions = readClassDescriptions();
        loadJDBCDriver();
        Gym.COOPER.setClassSchedule(getGymClasses(Gym.COOPER));
        Gym.GLIMMERGLASS.setClassSchedule(getGymClasses(Gym.GLIMMERGLASS));
        Properties properties = readDBConfigFile(getDBConfigFilePath());
        DB_URL=properties.getProperty("DB_URL");
        USER=properties.getProperty("USER");
        PASS=properties.getProperty("PASS");
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("EST"), Locale.ENGLISH);
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 45);
        timer.scheduleAtFixedRate(new DailyTimerTask(), calendar.getTime(), 24l * 60l * 60l * 1000l);
    }
    
    /**
     * Retrieves the plaintext db configuration from the given path.
     * @param path the path to the database configuration file.
     * @return the database configuration properties.
     */
    private static Properties readDBConfigFile(String path){
        Properties properties = new Properties();
        try{
            properties.load(new InputStreamReader(
                StartupShutdownListener.getContext().getResourceAsStream(path)));
        }catch(IOException ioe){
            throw new RuntimeException(ioe);
        }
        return properties;
    }
    
    /**
     * Retrieves the plaintext db configuration path for production.config if 
     * the DEPLOYMENT environment variable is set to production.  If 
     * DEPLOYMENT=development, retrieve the db config file path from 
     * development.config instead.  production.config should only have read 
     * permission from the account that that is running the application server.
     * production.config should also be in .gitignore to ensure that the 
     * database password stays private. Try export DEPLOYMENT="prod" from the 
     * shell before running the app server in production.
     * @return the path to the database config file.
     */
    private static String getDBConfigFilePath(){
        final String devPath = "/WEB-INF/development.config";
        final String prodPath = "/WEB-INF/production.config";
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