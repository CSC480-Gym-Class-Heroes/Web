/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 * A container for gym schedule class information
 * @author Chaskin Saroff
 */
public class GymClass {
    private final String name;
    private final String description;
    private final DayOfWeek dayOfWeek;
    private final String time;
    private final String instructor;
    
    /**
     * Constructs a new GymClass with the following parameters.
     * @param dayOfWeek The day of this gym class.
     * @param name The name of this gym class.
     * @param time The time of this gym class.
     * @param instructor The instructor who is teaching this gym class.
     * @param description A description of this gym class.
     */
    public GymClass(String name, String description, DayOfWeek dayOfWeek, String time, String instructor){
        this.dayOfWeek=dayOfWeek;
        this.name=name;
        this.time=time;
        this.instructor=instructor;
        this.description=description;
    }
    
    /**
     * @return a String representation of this GymClass instance.
     */
    @Override
    public String toString(){
        return dayOfWeek + "\t" + name + "\t" + time + "\t" + instructor + "\t" + description;
    }
    
    /**
     * @return the name of this gym class as a String.
     */
    public String getName(){
        return name;
    }
    
    /**
     * @return a description of this gym class as a String.
     */
    public String getDescription(){
        return description;
    }
    
    /**
     * @return the day of the week that this gym class is scheduled for.
     */
    public DayOfWeek getDayOfWeek(){
        return dayOfWeek;
    }
    
    /**
     * @return the time that this gym class is scheduled for as a String.
     */
    public String getTime(){
        return time;
    }

    /**
     * @return the name of the instructor who is teaching this gym class.
     */
    public String getInstructor(){
        return instructor;
    }
}
