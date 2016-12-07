/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import util.Query;

/**
 *
 * @author JAno
 */
public class Rating {
    private long user_id;
    private long venue_id;
    private int rating;
    
    public Rating(){
        this.user_id = -1;
        this.venue_id = -1;
        this.rating = -1;
    }
    
    public Rating(long user, long venue, int rating){
        this.user_id = user;
        this.venue_id = venue;
        this.rating = rating;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getVenue_id() {
        return venue_id;
    }

    public void setVenue_id(long venue_id) {
        this.venue_id = venue_id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
    
    /*
    public static List<Rating> getRatingsFromFile(List<User> users, List<Venue> venues){
        System.out.println("Cargando la lista de ratings...");
        List<Rating> ratings = new ArrayList();
        
        try {
            BufferedReader br = new BufferedReader(new FileReader("foursquare_datasets/ratings.dat"));
            br.readLine();
            br.readLine();
            String line = br.readLine();
            while(line != null){
                line = line.replace("|", ",").replace(" ", "");
                String[] parser = line.split(",");
                if(parser.length == 3){
                    ratings.add(new Rating(Query.findUserById(users, Long.parseLong(parser[0])), Query.findVenueById(venues, Long.parseLong(parser[1])), Integer.parseInt(parser[2])));
                }
                line = br.readLine();
            }
            
        } 
        catch (FileNotFoundException ex) {
            ex.printStackTrace(System.out);
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
        
        System.out.println("Largo de la lista de ratings: "+ratings.size());
        System.out.println("Ratings cargados con éxito");
        return ratings;
    }
    */
}
