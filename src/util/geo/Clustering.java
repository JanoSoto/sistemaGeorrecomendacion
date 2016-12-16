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
 * Implementación de K-Means
 */
public class Clustering {
    private List<Venue> items;
    private List<Cluster> clusters;
    
    public Clustering(List<Venue> items){        
        this.items = items;
        this.clusters = new ArrayList();
    }

    public List<Venue> getVenues() {
        return items;
    }

    public void setVenues(List<Venue> items) {
        this.items = items;
    }

    public List<Cluster> getClusters() {
        return clusters;
    }

    public void setClusters(List<Cluster> clusters) {
        this.clusters = clusters;
    }    
    
    public void setCentroids(List<GeoPoint> centroids){
        for(int i=0; i<centroids.size(); i++){
            this.clusters.add(new Cluster(i, centroids.get(i)));
        }
    }
    
    private void assignCluster() {
        double min; 
        int clusterNum = 0;                 
        double distance; 
        
        for(Venue item : items) {
            min = Double.MAX_VALUE;
            for(int i = 0; i < clusters.size(); i++) {
            	Cluster cluster = clusters.get(i);
                distance = Distance.calculateDistance(new GeoPoint(item.getLatitude(), item.getLongitude()), 
                                                            cluster.getCentroid());
                if(distance < min){
                    min = distance;
                    clusterNum = i;
                }
            }
            clusters.get(clusterNum).addVenue(item);
        }
    }
    
    private void calculateCentroids() {
        for(Cluster cluster : clusters) {
            double sumX = 0;
            double sumY = 0;
            List<Venue> clusterVenues = cluster.getVenues();
            int n_points = clusterVenues.size();
            
            for(Venue item : clusterVenues) {
            	sumX += item.getLatitude();
                sumY += item.getLongitude();
            }
            
            if(n_points > 0) {                
                cluster.getCentroid().setLatitude(sumX / n_points);
                cluster.getCentroid().setLongitude(sumY / n_points);
            }
        }
    }
    
    private List<GeoPoint> getCentroids(){
        List<GeoPoint> centroids = new ArrayList();
        for(Cluster cluster : clusters){
            GeoPoint centroid = cluster.getCentroid();
            centroids.add(new GeoPoint(centroid.getLatitude(), centroid.getLongitude()));
        }
        return centroids;
    }
    
    private void clearClusters() {
    	for(Cluster cluster : clusters) {
    		cluster.clear();
    	}
    }
    
    public void calculate(){
        boolean finish = false;
        int iteration = 0;
        
        if (clusters.isEmpty()){
            return;
        }
        
        while(!finish) {
            clearClusters();            

            List<GeoPoint> lastCentroids = getCentroids();
            
            
            assignCluster();
            
            calculateCentroids();

            iteration++;
            
            List<GeoPoint> currentCentroids = getCentroids();            
            
            double distance = 0;
            for(int i = 0; i < lastCentroids.size(); i++) {
                distance += Distance.calculateDistance(lastCentroids.get(i),currentCentroids.get(i));
            }
            
            System.out.println("#################");
            System.out.println("Iteration: " + iteration);
            System.out.println("Centroid distances: " + distance);
            
            for(Cluster cluster : clusters){
                System.out.println("[Cluster: " + cluster.getId()+"]");
		System.out.println("[Centroid: [" + cluster.getCentroid().getLatitude() + ", " + cluster.getCentroid().getLongitude() + "] ]");
		System.out.println("[Points: \n");
		for(Venue item : cluster.getVenues()) {
			System.out.println("[ " + item.getLatitude() + ", " + item.getLongitude() + " ]");
		}
		System.out.println("]");
            }
            
            System.out.println("]");
            
            if(distance == 0) {
                    finish = true;
            }
        }
    }
}
