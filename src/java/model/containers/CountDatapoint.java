/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.containers;

/**
 *
 * @author csaroff
 */
public class CountDatapoint {
    private final int count;
    private final long timestamp;
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
