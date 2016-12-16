/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.geo;

import entities.Venue;

/**
 *
 * @author JAno
 */
public class ItemDistance {
    
    private Venue item;
    private double distance;
    
    public ItemDistance(Venue item, double distance){
        this.item = item;
        this.distance = distance;
    }

    public Venue getVenue() {
        return item;
    }

    public void setVenue(Venue item) {
        this.item = item;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
    
}
