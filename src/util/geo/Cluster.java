/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.geo;

import recomendation.ItemPrediction;
import java.util.ArrayList;
import java.util.List;
import util.GeoPoint;

/**
 *
 * @author JAno
 */
public class Cluster {
    private long id;
    private List<ItemPrediction> items;
    private GeoPoint centroid;
    
    public Cluster(long id){
        this.id = id;
        this.items = new ArrayList();
        this.centroid = null;
    }
    
    public Cluster(long id, GeoPoint centroid){
        this.id = id;
        this.items = new ArrayList();
        this.centroid = centroid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<ItemPrediction> getItemPredictions() {
        return items;
    }

    public void setItemPredictions(List<ItemPrediction> items) {
        this.items = items;
    }

    public GeoPoint getCentroid() {
        return centroid;
    }

    public void setCentroid(GeoPoint centroid) {
        this.centroid = centroid;
    }
    
    public void clear(){
        this.items.clear();
    }
    
    public void addItemPrediction(ItemPrediction item){
        this.items.add(item);
    }
    
    public double getAverageRating(){
        double avg = 0;
        for(ItemPrediction item : this.items){
            avg += item.getRating();
        }
        return avg / items.size();
    }
}
