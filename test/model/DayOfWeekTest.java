/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Calendar;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author csaroff
 */
public class DayOfWeekTest {
    
    public DayOfWeekTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of values method, of class DayOfWeek.
     */
    @Test
    public void testValues() {
        System.out.println("values");
        DayOfWeek[] expResult = {
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY,
            DayOfWeek.SUNDAY};
        DayOfWeek[] result = DayOfWeek.values();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of valueOf method, of class DayOfWeek.
     */
    @Test
    public void testValueOf() {
        System.out.println("valueOf");

        assertEquals(DayOfWeek.MONDAY, DayOfWeek.valueOf("MONDAY"));
        try{
            DayOfWeek.valueOf(null);
            fail("Expected null pointer exception for null input.");
        }catch(NullPointerException npe){}
        try{
            DayOfWeek.valueOf("");
            fail("Expected IllegalArgumentException for empty string input");
        }catch(IllegalArgumentException iae){}
    }

    /**
     * Test of today method, of class DayOfWeek.
     */
    @Test
    public void testToday() {
        System.out.println("today");
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        DayOfWeek expResult;
        switch(day){
            case Calendar.MONDAY:
                expResult=DayOfWeek.MONDAY; break;
            case Calendar.TUESDAY:
                expResult=DayOfWeek.TUESDAY; break;
            case Calendar.WEDNESDAY:
                expResult=DayOfWeek.WEDNESDAY; break;
            case Calendar.THURSDAY:
                expResult=DayOfWeek.THURSDAY; break;
            case Calendar.FRIDAY:
                expResult=DayOfWeek.FRIDAY; break;
            case Calendar.SATURDAY:
                expResult=DayOfWeek.SATURDAY; break;
            case Calendar.SUNDAY:
                expResult=DayOfWeek.SUNDAY; break;
            default:
                fail("This test case is broken.");return;
        }
        DayOfWeek result = DayOfWeek.today();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDayOfWeek method, of class DayOfWeek.
     */
    @Test
    public void testGetDayOfWeek() {
        System.out.println("getDayOfWeek");
        String monday = "MondaY";
        String tuesday = "tUeSdAy";
        String wednesday = "WEDNESDAY";
        String thursday = "thursday";
        String friday = "FRiDAy";
        String saturday = "saturday";
        String sunday = "SUNDAY";
        assertEquals(DayOfWeek.MONDAY, DayOfWeek.getDayOfWeek(monday));
        assertEquals(DayOfWeek.TUESDAY, DayOfWeek.getDayOfWeek(tuesday));
        assertEquals(DayOfWeek.WEDNESDAY, DayOfWeek.getDayOfWeek(wednesday));
        assertEquals(DayOfWeek.THURSDAY, DayOfWeek.getDayOfWeek(thursday));
        assertEquals(DayOfWeek.FRIDAY, DayOfWeek.getDayOfWeek(friday));
        assertEquals(DayOfWeek.SATURDAY, DayOfWeek.getDayOfWeek(saturday));
        assertEquals(DayOfWeek.SUNDAY, DayOfWeek.getDayOfWeek(sunday));
        try{
            DayOfWeek.getDayOfWeek("");
            fail("Expected illegal argument exception");
        }catch(IllegalArgumentException iae){}
        try{
            DayOfWeek.getDayOfWeek(null);
            fail("Expected null pointer exception");
        }catch(NullPointerException npe){}
        try{
            DayOfWeek.getDayOfWeek("Monday ");
            fail("Expected illegal argument exception");
        }catch(IllegalArgumentException iae){}
    }

    /**
     * Test of getStartOfDayForLast method, of class DayOfWeek.
     */
    @Test
    public void testGetStartOfDayForLast() {
        System.out.println("getStartOfDayForLast");
        
        Calendar cal = Calendar.getInstance();
        // set to mid-night
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date expResult = new Date(cal.getTimeInMillis()-604800000);
        Date result = DayOfWeek.getStartOfDayForLast(DayOfWeek.today());
        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getEndOfDayForLast method, of class DayOfWeek.
     */
    @Test
    public void testGetEndOfDayForLast() {
        System.out.println("getEndOfDayForLast");
        Calendar cal = Calendar.getInstance();
        // set to mid-night
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date expResult = new Date(cal.getTimeInMillis()-518400001);
        Date result = DayOfWeek.getEndOfDayForLast(DayOfWeek.today());
        System.out.println("Expected: " + expResult);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);        
    }
    
}
