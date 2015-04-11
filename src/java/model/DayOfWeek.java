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
    
    private final int value;

    private DayOfWeek(int value){
        this.value=value;
    }
    
    public static DayOfWeek today(){
        Calendar c = Calendar.getInstance();
        c.setTime(new java.util.Date());
        return getDayOfWeek(
                c.getDisplayName(
                        Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US));
    }
    
    public static DayOfWeek getDayOfWeek(String dayName){
        return DayOfWeek.valueOf(dayName.toUpperCase(Locale.ENGLISH));
    }
    
    public static Date getStartOfDayForLast(DayOfWeek day){
        return getStartOfDay(getDateForLast(day));
    } 
    
    public static Date getEndOfDayForLast(DayOfWeek day){
        return getEndOfDay(getDateForLast(day));
    }
    
    private static Calendar getDateForLast(DayOfWeek day){
        Calendar calendar = Calendar.getInstance(
                TimeZone.getTimeZone("EST"), Locale.US);
        calendar.add(Calendar.DATE, today().equals(day)?-7:(-1)*Math.abs(day.value-today().value));
        return calendar;
    }
    private static Date getStartOfDay(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 23, 59, 59);
        return calendar.getTime();
    }

    private static Date getEndOfDay(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 23, 59, 59);
        return calendar.getTime();
    }
}

