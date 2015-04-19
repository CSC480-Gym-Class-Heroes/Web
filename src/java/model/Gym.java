/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Date;
/**
 * An enumeration representing a gym and it's associated state.
 * @author Chaskin Saroff
 */
public enum Gym {
    COOPER("Cooper.csv"), 
    GLIMMERGLASS("Glimmerglass.csv");
    private int currentCount = 0;
    private Date currentCountTimestamp = new Date(0);
    private int inCount = 0;
    private long healthTimestamp = 0l;
    public final String classScheduleFileName;
    private String[] hours = new String[7];
    private Map<DayOfWeek, List<GymClass>> classSchedule = new EnumMap<>(DayOfWeek.class);
    
    /**
     * Returns the Gym associated with the given gymName, ignoring case.
     * @param gymName the name of the gym.
     * @return the Gym associated with the given gymName, ignoring case.
     */
    public static Gym getGym(String gymName){
        return Gym.valueOf(gymName.toUpperCase(Locale.ENGLISH));
    }
    
    Gym(String classScheduleFileName){
        this.classScheduleFileName = classScheduleFileName;
    }
    
    /**
     * Sets currentCount to newCount if and only if newCount is actually newer 
     * than currentCount; ie newCountTimestamp.after(currentCountTimestamp).
     * If in==true, increment inCount irrespective newCount actually being 
     * newer.  
     * This should handle information being relayed out of order due to network
     * connectivity issues.
     * @param newCount the new current count of the number of gym users.
     * @param newCountTimestamp the timestamp for when this count was recorded
     * @param in true if newCount was recorded when someone was entering the gym
     * and false otherwise
     * @return true if newCount replaced currentCount and false otherwise.
     */
    public boolean setCurrentCount(int newCount, 
            Date newCountTimestamp, boolean in){
        if(in)
            inCount++;
        //Only reset the current number of gymrats if the new timestamp is
        //actually newer than currentTimeStamp.
        if(newCountTimestamp.after(this.currentCountTimestamp)){
            this.currentCount = newCount;
            this.currentCountTimestamp = newCountTimestamp;
            return true;
        }
        return false;
    }
    
    /**
     * @return gets the current count of the number of people in this gym.
     */
    public int getCurrentCount(){
        return this.currentCount;
    }
    
    /**
     * Resets the current count to zero.
     */
    public void resetCurrentCount(){
        currentCount=0;
    }
    
    /**
     * @return the total number of people who have come into the gym today.
     */
    public int getInCountToday(){
        return inCount;
    }
    
    /**
     * Increments the count of the number of people who can into this gym today.
     */
    public void incrementInCount(){
        inCount++;
    }
    
    /**
     * Resets the number of people who have entered the gym today to 0.
     */
    public void resetInCount(){
        inCount=0;
    }
    
    /**
     * @return the hours
     */
    public String[] getHours() {
        return hours;
    }

    /**
     * @param hours the hours to set
     */
    public void setHours(String[] hours) {
        this.hours = hours;
    }

    /**
     * @return the classSchedule
     */
    public Map<DayOfWeek, List<GymClass>> getClassSchedule() {
        return classSchedule;
    }

    /**
     * @param classSchedule the classSchedule to set
     */
    public void setClassSchedule(Map<DayOfWeek, List<GymClass>> classSchedule) {
        this.classSchedule = classSchedule;
    }
    
    /**
     * Returns a long value representing the last time the Raspberry Pi 
     * communicated a health message to the web server.
     * @return a timestamp representing the last time the raspberry pi
     * communicated a health message to the web server.
     */
    public long getHealthTimestamp() {
        return healthTimestamp;
    }
    
    /**
     * Sets this Gym's health timestamp to the current system time in milliseconds.
     */
    public void setHealthTimestamp(){
        healthTimestamp = System.currentTimeMillis();
    }
 
}
