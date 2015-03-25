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
public class GymClass {
    private DayOfWeek dayOfWeek;
    private String name;
    private String time;
    private String instructor;
    private String description;
    public GymClass(DayOfWeek dayOfWeek, String name, String time, String instructor, String description){
        this.dayOfWeek=dayOfWeek;
        this.name=name;
        this.time=time;
        this.instructor=instructor;
        this.description=description;
    }
    public String toString(){
        return dayOfWeek + "\t" + name + "\t" + time + "\t" + instructor + "\t" + description;
    }
    public DayOfWeek getDayOfWeek(){
        return dayOfWeek;
    }
    public String getName(){
        return name;
    }
    public String getTime(){
        return time;
    }
    public String getInstructor(){
        return instructor;
    }
}
