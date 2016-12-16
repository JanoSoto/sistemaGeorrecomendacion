/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recomendation;

import entities.Correlation;
import entities.Rating;
import entities.User;
import entities.Venue;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections4.map.MultiKeyMap;
import util.Query;
import util.SortItemPredictionByRating;
import static util.geo.Distance.distanceDecay;

/**
 *
 * @author JAno
 */
public class Prediction {
    private List<ItemPrediction> predictions;
    private Query query;

    public Prediction(Query query) {
        this.query = query;
        this.predictions = new ArrayList();
    }    

    public Query getQueries() {
        return query;
    }

    public void setQueries(Query query) {
        this.query = query;
    }

    public List<ItemPrediction> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<ItemPrediction> predictions) {
        this.predictions = predictions;
    }
    
    public void getTopKItems(int k, int neighborhood_size, long user_id, String [] frontier){
        this.predictions = new ArrayList();
        List<ItemPrediction> allPredictions = new ArrayList();
        
        try {
            double avgRatingUser = query.getAverageRatingByUser(user_id);
            System.out.println("OBTENIENDO EL VECINDARIO...");
            List<Correlation> neighborhood = query.getNeighborhoodByUser(user_id, neighborhood_size);
            System.out.println("VECINDARIO RECUPERADO CON ÉXITO.");
            double avgCorrelation = getCorrelationAverage(neighborhood);
            System.out.println("CALCULANDO EL RATING PROMEDIO DE CADA VECINO...");
            Map<Long, Double> avgVecindario = new HashMap<>();
            for(Correlation neighbor : neighborhood){
                if(avgVecindario.get(neighbor.getUser1_id()) == null){
                    avgVecindario.put(neighbor.getUser1_id(), query.getAverageRatingByUser(neighbor.getUser1_id()));
                }
                if(avgVecindario.get(neighbor.getUser2_id()) == null){
                    avgVecindario.put(neighbor.getUser2_id(), query.getAverageRatingByUser(neighbor.getUser2_id()));
                }
            }
            System.out.println("RATING PROMEDIO DE CADA VECINO REGISTRADO CON ÉXITO.");
            System.out.println("OBTENIENDO LOS LUGARES DENTRO DE LA FRONTERA...");
            List<Venue> venues = query.findVenuesByFrontier(frontier);
            System.out.println("SE RECUPERARON "+venues.size()+" LUGARES.");
            System.out.println("CARGANDO LOS RATINGS EN MEMORIA...");
            List<Rating> allRatings = query.getAllRatings();
            System.out.println("Cantidad de ratings: "+allRatings.size());
            MultiKeyMap ratings = new MultiKeyMap();
            for(Rating rating : allRatings){
                //System.out.println("Agregando: "+rating.getUser_id()+", "+rating.getVenue_id()+", "+rating.getRating());
                ratings.put((long) rating.getUser_id(), (long) rating.getVenue_id(), rating.getRating());
            }
            System.out.println("Cantidad de ratings: "+ratings.size());
            System.out.println("RATINGS CARGADOS CON ÉXITO.");            
            System.out.println("CALCULANDO LAS PREDICCIONES...");
            long startTime = System.currentTimeMillis();
            for(Venue venue : venues){
                //System.out.println("Predicción del lugar "+venue.getId());
                double sumaVecindario = 0;
                for(Correlation neighbor : neighborhood){
                    double usersCorrelation;
                    if(user_id != neighbor.getUser1_id()){
                        if((usersCorrelation = query.getCorrelationByUsers(user_id, neighbor.getUser1_id())) == 2){
                            usersCorrelation = query.getCorrelationByUsers(neighbor.getUser1_id(), user_id);
                        }
                        if(ratings.get(neighbor.getUser1_id(), venue.getId()) != null){
                            sumaVecindario += ((int) ratings.get(neighbor.getUser1_id(), venue.getId()) - avgVecindario.get(neighbor.getUser1_id())) * usersCorrelation;
                        }
                    }
                    else {
                        if((usersCorrelation = query.getCorrelationByUsers(user_id, neighbor.getUser2_id())) == 2){
                            usersCorrelation = query.getCorrelationByUsers(neighbor.getUser2_id(), user_id);
                        }
                        if(ratings.get(neighbor.getUser2_id(), venue.getId()) != null){
                            sumaVecindario += ((int) ratings.get(neighbor.getUser2_id(), venue.getId()) - avgVecindario.get(neighbor.getUser2_id())) * usersCorrelation;
                        }
                    }
                }
                allPredictions.add(new ItemPrediction(venue, avgRatingUser + (sumaVecindario / avgCorrelation)));
            }
            System.out.println("Tiempo total: "+(System.currentTimeMillis() - startTime)/1000);
        } 
        catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        
        System.out.println("OBTENIENDO LAS TOP K PREDICCIONES");
        allPredictions.sort(new SortItemPredictionByRating());
        for(int i=0; i<k; i++){
            this.predictions.add(allPredictions.get(i));
        }
        printPredictions(user_id);
    }
    
    public void getTopKItems(int k, int neighborhood_size, long user_id){
        this.predictions = new ArrayList();
        List<ItemPrediction> allPredictions = new ArrayList();
        
        try {
            double avgRatingUser = query.getAverageRatingByUser(user_id);
            List<Correlation> neighborhood = query.getNeighborhoodByUser(user_id, neighborhood_size);
            double avgCorrelation = getCorrelationAverage(neighborhood);
            Map<Long, Double> avgVecindario = new HashMap<>();
            for(Correlation neighbor : neighborhood){
                if(avgVecindario.get(neighbor.getUser1_id()) == null){
                    avgVecindario.put(neighbor.getUser1_id(), query.getAverageRatingByUser(neighbor.getUser1_id()));
                }
                if(avgVecindario.get(neighbor.getUser2_id()) == null){
                    avgVecindario.put(neighbor.getUser2_id(), query.getAverageRatingByUser(neighbor.getUser2_id()));
                }
            }
            List<Venue> venues = query.getAllVenues();
            System.out.println("CALCULANDO LAS PREDICCIONES...");
            for(Venue venue : venues){
                System.out.println("Predicción del lugar "+venue.getId());
                double sumaVecindario = 0;
                for(Correlation neighbor : neighborhood){
                    double usersCorrelation;
                    if(user_id != neighbor.getUser1_id()){
                        if((usersCorrelation = query.getCorrelationByUsers(user_id, neighbor.getUser1_id())) == 2){
                            usersCorrelation = query.getCorrelationByUsers(neighbor.getUser1_id(), user_id);
                        }
                        sumaVecindario += (query.getRatingByUserAndVenue(neighbor.getUser1_id(), venue.getId()) - query.getAverageRatingByUser(neighbor.getUser1_id())) * usersCorrelation;
                    }
                    else {
                        if((usersCorrelation = query.getCorrelationByUsers(user_id, neighbor.getUser2_id())) == 2){
                            usersCorrelation = query.getCorrelationByUsers(neighbor.getUser2_id(), user_id);
                        }
                        sumaVecindario += (query.getRatingByUserAndVenue(neighbor.getUser2_id(), venue.getId()) - query.getAverageRatingByUser(neighbor.getUser2_id())) * usersCorrelation;
                    }
                }
                allPredictions.add(new ItemPrediction(venue, avgRatingUser + (sumaVecindario / avgCorrelation)));
            }
        } 
        catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        
        System.out.println("OBTENIENDO LAS TOP K PREDICCIONES");
        allPredictions.sort(new SortItemPredictionByRating());
        for(int i=0; i<k; i++){
            this.predictions.add(allPredictions.get(i));
        }
        printPredictions(user_id);
    }
    
    public void getTopKItemsDistanceDecay(int k, int neighborhood_size, long user_id, int r_inner, int r_outer){
        this.predictions = new ArrayList();
        List<ItemPrediction> allPredictions = new ArrayList();
        
        try {
            double avgRatingUser = query.getAverageRatingByUser(user_id);
            System.out.println("OBTENIENDO EL VECINDARIO...");
            List<Correlation> neighborhood = query.getNeighborhoodByUser(user_id, neighborhood_size);
            System.out.println("VECINDARIO RECUPERADO CON ÉXITO.");
            double avgCorrelation = getCorrelationAverage(neighborhood);
            System.out.println("CALCULANDO EL RATING PROMEDIO DE CADA VECINO...");
            Map<Long, Double> avgVecindario = new HashMap<>();
            for(Correlation neighbor : neighborhood){
                if(avgVecindario.get(neighbor.getUser1_id()) == null){
                    avgVecindario.put(neighbor.getUser1_id(), query.getAverageRatingByUser(neighbor.getUser1_id()));
                }
                if(avgVecindario.get(neighbor.getUser2_id()) == null){
                    avgVecindario.put(neighbor.getUser2_id(), query.getAverageRatingByUser(neighbor.getUser2_id()));
                }
            }
            System.out.println("RATING PROMEDIO DE CADA VECINO REGISTRADO CON ÉXITO.");
            System.out.println("OBTENIENDO LOS LUGARES DENTRO DE LA FRONTERA...");
            List<Venue> venues = query.getNVenues(10000);
            System.out.println("SE RECUPERARON "+venues.size()+" LUGARES.");
            System.out.println("CARGANDO LOS RATINGS EN MEMORIA...");
            List<Rating> allRatings = query.getAllRatings();
            System.out.println("Cantidad de ratings: "+allRatings.size());
            MultiKeyMap ratings = new MultiKeyMap();
            for(Rating rating : allRatings){
                //System.out.println("Agregando: "+rating.getUser_id()+", "+rating.getVenue_id()+", "+rating.getRating());
                ratings.put((long) rating.getUser_id(), (long) rating.getVenue_id(), rating.getRating());
            }
            System.out.println("Cantidad de ratings: "+ratings.size());
            System.out.println("RATINGS CARGADOS CON ÉXITO.");            
            System.out.println("CALCULANDO LAS PREDICCIONES...");
            long startTime = System.currentTimeMillis();
            for(Venue venue : venues){
                //System.out.println("Predicción del lugar "+venue.getId());
                double sumaVecindario = 0;
                for(Correlation neighbor : neighborhood){
                    double usersCorrelation;
                    if(user_id != neighbor.getUser1_id()){
                        if((usersCorrelation = query.getCorrelationByUsers(user_id, neighbor.getUser1_id())) == 2){
                            usersCorrelation = query.getCorrelationByUsers(neighbor.getUser1_id(), user_id);
                        }
                        if(ratings.get(neighbor.getUser1_id(), venue.getId()) != null){
                            sumaVecindario += ((int) ratings.get(neighbor.getUser1_id(), venue.getId()) - avgVecindario.get(neighbor.getUser1_id())) * usersCorrelation;
                        }
                    }
                    else {
                        if((usersCorrelation = query.getCorrelationByUsers(user_id, neighbor.getUser2_id())) == 2){
                            usersCorrelation = query.getCorrelationByUsers(neighbor.getUser2_id(), user_id);
                        }
                        if(ratings.get(neighbor.getUser2_id(), venue.getId()) != null){
                            sumaVecindario += ((int) ratings.get(neighbor.getUser2_id(), venue.getId()) - avgVecindario.get(neighbor.getUser2_id())) * usersCorrelation;
                        }
                    }
                }
                allPredictions.add(new ItemPrediction(venue, avgRatingUser + (sumaVecindario / avgCorrelation)));
            }
            System.out.println("Tiempo total: "+(System.currentTimeMillis() - startTime)/1000);
        } 
        catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        
        System.out.println("OBTENIENDO LAS TOP K PREDICCIONES");
        allPredictions.sort(new SortItemPredictionByRating());
        for(int i=0; i<k; i++){
            this.predictions.add(allPredictions.get(i));
        }
        printPredictions(user_id);
        User user = null;
        try {
            user = query.getUserById(user_id);
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        
        if(user != null){
            this.predictions = reRankFromDistance(user, k, allPredictions, r_inner, r_outer);
            printPredictions(user_id);
        }
        else {
            System.out.println("ERROR: ID de usuario incorrecto.");
        }
    }
    
    public List<ItemPrediction> reRankFromDistance(User user, int k, List<ItemPrediction> predictions, int r_inner, int r_outer){
        System.out.println("Re-rankeando");
        List<ItemPrediction> topKPredictions = new ArrayList();
        List<ItemPrediction> allPredictions = new ArrayList();
        for(ItemPrediction item : predictions){
            allPredictions.add(new ItemPrediction(item.getVenue(), item.getRating() * distanceDecay(user, item.getVenue(), r_inner, r_outer)));            
        }
        allPredictions.sort(new SortItemPredictionByRating());
        
        for(int i=0; i<k; i++){
            topKPredictions.add(allPredictions.get(i));
        }
        
        return topKPredictions;
    }
    
    public double getCorrelationAverage(List<Correlation> neighborhood){
        double sum = 0;
        
        for(Correlation corr : neighborhood){
            sum += corr.getCorrelation_value();
        }
        
        return sum/neighborhood.size();
    }
    
    private void printPredictions(long user_id){
        System.out.println("ITEMS RECOMENDADOS PARA EL USUARIO "+user_id);
        for(int i = 0; i < predictions.size(); i++){
            System.out.println((i+1)+".- Venue: "+predictions.get(i).getVenue().getId()+", Rating: "+predictions.get(i).getRating());
        }
    }
}
