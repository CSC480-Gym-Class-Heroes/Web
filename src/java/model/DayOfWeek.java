/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Locale;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * An enumeration representing a day of the week.
 * @author Chaskin Saroff
 */
public enum DayOfWeek {
    MONDAY(Calendar.MONDAY), 
    TUESDAY(Calendar.TUESDAY), 
    WEDNESDAY(Calendar.WEDNESDAY), 
    THURSDAY(Calendar.THURSDAY), 
    FRIDAY(Calendar.FRIDAY), 
    SATURDAY(Calendar.SATURDAY),
    SUNDAY(Calendar.SUNDAY);

    //Each day get's an integer value to comply with java.util.Calendar's 
    //DayOfWeek system.
    private final int value;

    /**
     * Constructs a new day of week with the given java.util.Calendar day of the
     * week value system.
     * @param value the java.util.Calendar's day of the week value.
     */
    private DayOfWeek(int value){
        this.value=value;
    }
    
    /**
     * Returns the DayOfWeek object associated with today's date.  i.e. If today
     * is tuesday, this method returns DayOfWeek.TUESDAY.
     * @return the DayOfWeek object assocated with today's date.
     */
    public static DayOfWeek today(){
        Calendar c = Calendar.getInstance();
        c.setTime(new java.util.Date());
        return getDayOfWeek(
                c.getDisplayName(
                        Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US));
    }
    
    /**
     * Returns the DayOfWeek with the given name, ignoring case.
     * @param dayName the name of the day.
     * @return the DayOfWeek with the given name, ignoring case.
     */
    public static DayOfWeek getDayOfWeek(String dayName){
        return DayOfWeek.valueOf(dayName.toUpperCase(Locale.ENGLISH));
    }
    
    /**
     * Returns the timestamp of the beginning of the day for the last time the
     * given dayOfWeek occurred.  If day==today(), this method returns the
     * timestamp of the beginning of the day for 7 days ago.  For example,
     * If today is Tuesday, January 13th, 2015 and the given day is MONDAY, this 
     * method returns the earliest timestamp on Monday, January 12th 2015.
     * If today is Tuesday, January 13th, 2015 and the given day is TUESDAY,
     * this method returns the earliest timestamp on Tuesday, January 6th, 2015.
     * @param day 
     * @return the timestamp of the beginning of the day of the most recent 
     * occurrence of dayOfWeek.
     */
    public static Date getStartOfDayForLast(DayOfWeek day){
        return getStartOfDay(getDateForLast(day));
    } 
    
    /**
     Returns the timestamp of the end of the day for the last time the
     * given dayOfWeek occurred.  If day==today(), this method returns the
     * timestamp of the end of the day for 7 days ago.  For example,
     * If today is Tuesday, January 13th, 2015 and the given day is MONDAY, this 
     * method returns the earliest timestamp on Monday, January 12th 2015.
     * If today is Tuesday, January 13th, 2015 and the given day is TUESDAY,
     * this method returns the earliest timestamp on Tuesday, January 6th, 2015.
     * @param day 
     * @return the timestamp of the end of the day of the most recent 
     * occurrence of dayOfWeek.
     */
    public static Date getEndOfDayForLast(DayOfWeek day){
        return getEndOfDay(getDateForLast(day));
    }
    
    private static Calendar getDateForLast(DayOfWeek day){
        Calendar calendar = Calendar.getInstance(
                TimeZone.getTimeZone("America/New_York"), Locale.US
        );
        calendar.add(Calendar.DATE, today().equals(day)?-7:(-1)*Math.abs(day.value-today().value));
        return calendar;
    }
    private static Date getStartOfDay(Calendar calendar) {
        //System.out.println("Start " + calendar.getTime());
        //System.out.println("Start " + calendar.getTimeInMillis());
        //System.out.println("getStartOfDay input = " + new Date(calendar.getTimeInMillis()));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        //System.out.println("getStartOfDay input = " + new Date(calendar.getTimeInMillis()));
        calendar.set(Calendar.MINUTE, 0);
        //System.out.println("getStartOfDay input = " + new Date(calendar.getTimeInMillis()));
        calendar.set(Calendar.SECOND, 0);
        //System.out.println("getStartOfDay input = " + new Date(calendar.getTimeInMillis()));
        calendar.set(Calendar.MILLISECOND, 0);
        //System.out.println("getStartOfDay input = " + new Date(calendar.getTimeInMillis()));
        //System.out.println("End " + calendar.getTime());
        //System.out.println("End " + calendar.getTimeInMillis());
        return calendar.getTime();
    }

    private static Date getEndOfDay(Calendar calendar) {
        //System.out.println("getEndOfDay input = " + new Date(calendar.getTimeInMillis()));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        //System.out.println("getEndOfDay input = " + new Date(calendar.getTimeInMillis()));
        return calendar.getTime();
    }
}

