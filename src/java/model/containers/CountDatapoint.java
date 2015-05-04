/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.containers;

/**
 * A representation of a moment in time of someone coming into the gym.  
 * The representation includes the timestamp representing that moment in time
 * and a count of the number of people who were in the gym at that time.  
 * @author csaroff
 */
public class CountDatapoint {
    private final int count;
    private final long timestamp;
    /**
     * Constructs a new datapoint with the given count and timestamp.
     * @param count the number of people who were in the gym at the given
     * timestamp
     * @param timestamp the milliseconds since the epoch for this moment in
     * time.
     */
    public CountDatapoint(int count, long timestamp){
        this.count=count;
        this.timestamp=timestamp;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }
}
