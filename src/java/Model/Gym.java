/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
/**
 * An enumeration representing a gym and it's associated state.
 * @author Chaskin Saroff
 */
public enum Gym {
    COOPER("Cooper.csv"), 
    GLIMMERGLASS("Glimmerglass.csv");
    public final String classScheduleFileName;
    private String[] hours = new String[7];
    private Map<DayOfWeek, List<GymClass>> classSchedule = new EnumMap<>(DayOfWeek.class);
    private long healthTimestamp = 0l;
    public static Gym getGym(String gymName){
        return Gym.valueOf(gymName.toUpperCase(Locale.ENGLISH));
    }
    
    Gym(String classScheduleFileName){
        this.classScheduleFileName = classScheduleFileName;
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
