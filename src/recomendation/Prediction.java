/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recomendation;

import entities.Correlation;
import entities.Venue;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Query;
import util.SortItemtPredictionByRating;

/**
 *
 * @author JAno
 */
public class Prediction {
    private List<ItemPrediction> predictions;
    private Query queries;

    public Prediction(Query queries) {
        this.queries = queries;
        this.predictions = new ArrayList();
    }    

    public Query getQueries() {
        return queries;
    }

    public void setQueries(Query queries) {
        this.queries = queries;
    }

    public List<ItemPrediction> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<ItemPrediction> predictions) {
        this.predictions = predictions;
    }
    
    public List<ItemPrediction> getTopKItems(int k, long user_id){
        List<ItemPrediction> topK = new ArrayList();
        List<ItemPrediction> allPredictions = new ArrayList();
        
        try {
            double avgRatingUser = queries.getAverageRatingByUser(user_id);
            List<Correlation> neighborhood = queries.getNeighborhoodByUser(user_id, 10);
            double avgCorrelation = getCorrelationAverage(neighborhood);
            
            List<Venue> venues = queries.getAllVenues();
            System.out.println("CALCULANDO LAS PREDICCIONES...");
            for(Venue venue : venues){
            double sumaVecindario = 0;
            for(Correlation neighbor : neighborhood){
                double usersCorrelation;
                if(user_id != neighbor.getUser1_id()){
                    if((usersCorrelation = queries.getCorrelationByUsers(user_id, neighbor.getUser1_id())) == 2){
                        usersCorrelation = queries.getCorrelationByUsers(neighbor.getUser1_id(), user_id);
                    }
                    sumaVecindario += (queries.getRatingByUserAndVenue(neighbor.getUser1_id(), venue.getId()) - queries.getAverageRatingByUser(neighbor.getUser1_id())) * usersCorrelation;
                }
                else {
                    if((usersCorrelation = queries.getCorrelationByUsers(user_id, neighbor.getUser2_id())) == 2){
                        usersCorrelation = queries.getCorrelationByUsers(neighbor.getUser2_id(), user_id);
                    }
                    sumaVecindario += (queries.getRatingByUserAndVenue(neighbor.getUser2_id(), venue.getId()) - queries.getAverageRatingByUser(neighbor.getUser2_id())) * usersCorrelation;
                }
            }
            allPredictions.add(new ItemPrediction(venue, avgRatingUser + (sumaVecindario / avgCorrelation)));
        }
        } 
        catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        
        System.out.println("OBTENIENDO LAS TOP K PREDICCIONES");
        allPredictions.sort(new SortItemtPredictionByRating());
        for(int i=0; i<k; i++){
            topK.add(allPredictions.get(i));
        }
        return topK;
    }
    
    
    public double getCorrelationAverage(List<Correlation> neighborhood){
        double sum = 0;
        
        for(Correlation corr : neighborhood){
            sum += corr.getCorrelation_value();
        }
        
        return sum/neighborhood.size();
    }
}
