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
public class Pair<U, V> {
    /**
     * The first element of this <code>Pair</code>
     */
    private final U first;

    /**
     * The second element of this <code>Pair</code>
     */
    private final V second;

    /**
     * Constructs a new <code>Pair</code> with the given values.
     * 
     * @param first  the first element
     * @param second the second element
     */
    public Pair(U first, V second) {

        this.first = first;
        this.second = second;
    }
    
    public U getFirst(){
        return first;
    }
    
    public V getSecond(){
        return second;
    }
}

//getter for first and second