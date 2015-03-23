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
    private Weekday dayOfWeek;
    private String name;
    private String time;
    private String instructor;
    public GymClass(Weekday dayOfWeek, String name, String time, String instructor){
        this.dayOfWeek=dayOfWeek;
        this.name=name;
        this.time=time;
        this.instructor=instructor;
    }
    public String toString(){
        return dayOfWeek + "\t" + name + "\t" + time + "\t" + instructor;
    }
    public Weekday getDayOfWeek(){
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
