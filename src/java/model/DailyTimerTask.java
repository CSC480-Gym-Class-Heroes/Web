/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;
import java.sql.SQLException;
import java.util.TimerTask;

/**
 *
 * @author csaroff
 */
public class DailyTimerTask extends TimerTask{

    @Override
    public void run() {
        for(Gym gym : Gym.values()){
            try{
                DatabaseUtility.
                    updateAverageInCount(gym, DayOfWeek.today(), gym.getInCountToday());
            }catch(SQLException e){
                throw new RuntimeException(e);
            }
            gym.resetCurrentCount();
            gym.resetInCount();
        }
    }
    
}
