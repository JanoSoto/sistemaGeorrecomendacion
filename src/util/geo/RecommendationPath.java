/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.geo;

import entities.Venue;
import java.util.ArrayList;
import java.util.List;
import util.GeoPoint;

/**
 *
 * @author JAno
 */
public class RecommendationPath {
    private List<Venue> items;
    private GeoPoint initialPoint;
    private List<ItemDistance> itemDistance;
    
    public RecommendationPath(List<Venue> items, GeoPoint point){
        this.items = items;
        this.initialPoint = point;
        this.itemDistance = new ArrayList();
    }

    public List<Venue> getVenues() {
        return items;
    }

    public void setVenues(List<Venue> items) {
        this.items = items;
    }

    public GeoPoint getInitialPoint() {
        return initialPoint;
    }

    public void setInitialPoint(GeoPoint initialPoint) {
        this.initialPoint = initialPoint;
    }       

    public List<ItemDistance> getItemDistance() {
        return itemDistance;
    }

    public void setItemDistance(List<ItemDistance> itemDistance) {
        this.itemDistance = itemDistance;
    }

    public List<Venue> calculatePath() {
        List<Venue> route = new ArrayList();
        List<ItemDistance> distances = calculateDistances(this.initialPoint, this.items);
        while(!distances.isEmpty()){
            ItemDistance closest = getClosestVenue(distances);
            route.add(closest.getVenue());
            distances.remove(closest);
            distances = calculateDistances(new GeoPoint(closest.getVenue().getLatitude(), closest.getVenue().getLongitude()),
                                            getVenuesFromItemDistance(distances));
        }
        
        return route;
    }
    
    public List<ItemDistance> calculateDistances(GeoPoint inicialPoint, List<Venue> items){
        List<ItemDistance> distances = new ArrayList();
        
        for(Venue item : items){
            distances.add(new ItemDistance(item, 
                                    Distance.calculateDistance(inicialPoint, new GeoPoint(item.getLatitude(), item.getLongitude()))));
        }
        
        return distances;
    }
    
    public ItemDistance getClosestVenue(List<ItemDistance> items){
        ItemDistance closest = items.get(0);
        for(int i=1; i<items.size(); i++){
            if(items.get(i).getDistance() < closest.getDistance()){
                closest = items.get(i);
            }
        }
        
        return closest;
    }
    
    public List<Venue> getVenuesFromItemDistance(List<ItemDistance> distances){
        List<Venue> newVenues = new ArrayList();
        for(ItemDistance id : distances){
            newVenues.add(id.getVenue());
        }
        return newVenues;
    }
}
