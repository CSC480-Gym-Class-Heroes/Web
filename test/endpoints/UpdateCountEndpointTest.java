/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;


import com.jayway.restassured.RestAssured;
import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.matcher.RestAssuredMatchers.*;
import java.util.Date;
import model.DayOfWeek;
import static org.hamcrest.Matchers.*;
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
public class UpdateCountEndpointTest {
    
    public UpdateCountEndpointTest() {
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
    
    @Test
    public void testLoadLastWeek(){
        loadLastWeek("cooper");
        loadLastWeek("glimmerglass");
    }
    
    /**
     * Populates the gym database with historical data associated with seven
     * days ago by calling the updatecount endpoint repeatedly.
     * @param gymName 
     */
    public void loadLastWeek(String gymName){
        RestAssured.baseURI = "http://localhost:9999/Gym_Rats";
        Date beginningOfDay = DayOfWeek.getStartOfDayForLast(DayOfWeek.today());
        int numberOfPeopleInGym = 0;
        for(int i=0; i<100; i++){
            System.out.println("numberOfPeopleInGym = " + numberOfPeopleInGym);
            boolean in = Math.random()>0.25;
            if(in) {numberOfPeopleInGym++;}
            else if(numberOfPeopleInGym==0){}
            else {numberOfPeopleInGym--;}
            given()
                //.log().all()
                .param("gym", gymName)
                .param("timestamp", "" + (beginningOfDay.getTime()+ 18000000 + ((long)(360000*i*Math.random()))))
                .param("count", "" + numberOfPeopleInGym)
                .param("in", "" + in)
            .expect()
                .statusCode(200)
            .when()
                .post("/updatecount");
        }
        int num = numberOfPeopleInGym;
        for(int i=100; i<100+num; i++){
            System.out.println("numberOfPeopleInGym = " + numberOfPeopleInGym);
            given()
                //.log().all()
                    .param("gym", gymName)
                .param("timestamp", "" + (beginningOfDay.getTime()+ 18000000 + ((long)(360000*i*Math.random()))))
                .param("count", "" + numberOfPeopleInGym--)
                .param("in", "false")
            .expect()
                .statusCode(200)
            .when()
                .post("/updatecount");
        }
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
