/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.geo;

import entities.User;
import entities.Venue;
import util.GeoPoint;

/**
 *
 * @author JAno
 */
public class Distance {
    
    public static double distanceDecay(User user, Venue venue, int r_inner, int r_outer){
        double R_INNER = r_inner/1000;
        double R_OUTER = r_outer/1000;
        double lambda = -1*Math.log(0.1)/(R_OUTER - R_INNER);
        double distance = calculateDistance(user.getLatitude(), user.getLongitude(), 
                                            venue.getLatitude(), venue.getLongitude());
        if(distance < R_INNER){
            return 1;
        }
        else if (R_INNER <= distance && distance < R_OUTER){
            return Math.exp(-1*lambda*(distance-R_INNER));
        }
        else{
            return 0;
        }
    }
    
    /**
     * Cálculo de la distancia de Haversine
     * @param user_latitude
     * @param user_longitude
     * @param venue_latitude
     * @param venue_longitude
     * @return 
     */
    public static double calculateDistance(double user_latitude, double user_longitude, double venue_latitude, double venue_longitude) {
        double R = 6372.8; // In kilometers
        double dLat = Math.toRadians(venue_latitude - user_latitude);
        double dLon = Math.toRadians(venue_longitude - user_longitude);
 
        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(Math.toRadians(user_latitude)) * Math.cos(Math.toRadians(venue_latitude));
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }
    
    public static double calculateDistance(GeoPoint location1, GeoPoint location2) {
        double R = 6372.8; // In kilometers
        double dLat = Math.toRadians(location2.getLatitude() - location1.getLatitude());
        double dLon = Math.toRadians(location2.getLongitude() - location1.getLongitude());
 
        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(Math.toRadians(location1.getLatitude())) * Math.cos(Math.toRadians(location2.getLatitude()));
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }
}
