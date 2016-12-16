/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import entities.Correlation;
import entities.Rating;
import entities.User;
import entities.Venue;
import it.unimi.dsi.fastutil.longs.Long2IntArrayMap;
import it.unimi.dsi.fastutil.longs.LongArraySet;
import it.unimi.dsi.fastutil.longs.LongSet;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JAno
 */
public class Query {
    
    private final Statement stmt;
    
    public Query(Statement stmt){
        this.stmt = stmt;
    }
    
    public List<Rating> getRatingsByUser(long user_id) throws SQLException{
        List<Rating> ratings = new ArrayList();
        ResultSet rs = stmt.executeQuery("SELECT venue_id, rating FROM rating WHERE user_id = "+user_id);
        while(rs.next()){
            ratings.add(new Rating(user_id, rs.getInt(1), rs.getInt(2)));
        }
        return ratings;
    }

    public Long2IntArrayMap getRatingsByUserId(long user_id) throws SQLException{
        Long2IntArrayMap ratings = new Long2IntArrayMap();
        ResultSet rs = stmt.executeQuery("SELECT venue_id, rating FROM rating WHERE user_id = "+user_id);
        while(rs.next()){
            ratings.put(rs.getLong(1), rs.getInt(2));
        }
        return ratings;
    }    
    
    public List<User> getAllUsers() throws SQLException{
        List<User> users = new ArrayList();
        ResultSet rs = stmt.executeQuery("SELECT * FROM user LIMIT 10000");
        while(rs.next()){
            users.add(new User(rs.getLong(1), rs.getDouble(2), rs.getDouble(3)));
        }
        return users;
    }
    
    public List<User> findUsersByFrontier(String [] frontier) throws SQLException{
        String polygon = "\'POLYGON((";
        for(String gp : frontier){
            polygon = polygon.concat(gp+", ");
        }
        polygon = polygon.concat(frontier[0]+"))\'");
        
        List<User> users = new ArrayList();
        
        ResultSet rs = stmt.executeQuery("SELECT * FROM user WHERE ST_CONTAINS(ST_GEOMFROMTEXT("+polygon+"), POINT(user.latitude, user.longitude))");
        while(rs.next()){
            users.add(new User(rs.getLong(1), rs.getDouble(2), rs.getDouble(3)));
        }
        return users;
    }
    
    public List<Venue> findVenuesByFrontier(String [] frontier) throws SQLException {
        String polygon = "\'POLYGON((";
        for(String gp : frontier){
            polygon = polygon.concat(gp+", ");
        }
        polygon = polygon.concat(frontier[0]+"))\'");
        
        List<Venue> venues = new ArrayList();
        
        ResultSet rs = stmt.executeQuery("SELECT * FROM venue WHERE ST_CONTAINS(ST_GEOMFROMTEXT("+polygon+"), POINT(venue.latitude, venue.longitude))");
        while(rs.next()){
            venues.add(new Venue(rs.getLong(1), rs.getDouble(2), rs.getDouble(3)));
        }
        return venues;
    }
    
    public List<Long> getAllUsersIds() throws SQLException{
        List<Long> usersId = new ArrayList();
        ResultSet rs = stmt.executeQuery("SELECT id FROM user");
        while(rs.next()){
            usersId.add(rs.getLong(1));
        }
        return usersId;
    }
    
    public LongArraySet getUsersIds() throws SQLException{
        LongArraySet usersId = new LongArraySet();
        ResultSet rs = stmt.executeQuery("SELECT id FROM user LIMIT 10000");
        while(rs.next()){
            usersId.add(rs.getLong(1));
        }
        return usersId;
    }
    
    public void saveCorrelation(long user1_id, long user2_id, double corr) throws SQLException{
        /*stmt.execute("INSERT INTO correlation SELECT "+user1_id+","+user2_id+","+corr+
                " FROM DUAL WHERE NOT EXISTS ("+
                "SELECT * FROM correlation WHERE user1_id = "+user1_id+" AND user2_id = "+user2_id+" AND correlation_value = "+corr+")" +
                "LIMIT 1");
        */
        stmt.execute("INSERT INTO correlation (user1_id, user2_id, correlation_value)"
                + " VALUES ("+user1_id+","+user2_id+","+corr+")");
    }
    
    public boolean existsCorrelation(long user1_id, long user2_id) throws SQLException{
        ResultSet rs = stmt.executeQuery("SELECT * FROM correlation WHERE user1_id = "+user1_id+" AND user2_id = "+user2_id);
        return rs.next();
    }
    
    public int countVenues() throws SQLException{
        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM venue");
        if(rs.next()){
            return rs.getInt(1);
        }
        else {
            return 0;
        }
        
    }
    
    public double getAverageRatingByUser(long user_id) throws SQLException{
        ResultSet rs = stmt.executeQuery("SELECT AVG(rating) FROM `rating` WHERE user_id = "+user_id);
        if(rs.next()){
            return rs.getDouble(1);
        }
        else {
            return 0.0;
        }
    }
    
    public List<Correlation> getNeighborhoodByUser(long user_id, int k) {
        List<Correlation> neighborhood = new ArrayList();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM correlation WHERE user1_id = "+user_id+" OR user2_id = "+user_id+" ORDER BY correlation_value DESC LIMIT "+k);            
            while(rs.next()){
                neighborhood.add(new Correlation(rs.getLong(1), rs.getLong(2), rs.getDouble(3)));
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        return neighborhood;
    }
            
    public List<Venue> getAllVenues(){
        List<Venue> venues = new ArrayList();
        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM venue");
            while(rs.next()){
                venues.add(new Venue(rs.getLong(1), rs.getDouble(2), rs.getDouble(3)));
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        return venues;
    }
    
    public List<Venue> getNVenues(int n){
        List<Venue> venues = new ArrayList();
        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM venue LIMIT "+n);
            while(rs.next()){
                venues.add(new Venue(rs.getLong(1), rs.getDouble(2), rs.getDouble(3)));
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        return venues;
    }
    
    public double getCorrelationByUsers(long user1_id, long user2_id){
        try {
            ResultSet rs = stmt.executeQuery("SELECT correlation_value FROM correlation WHERE user1_id = "+user1_id+" AND user2_id = "+user2_id);
            if(rs.next()){
                return rs.getDouble(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        return 2;
    }
    
    public int getRatingByUserAndVenue(long user_id, long venue_id){
        try {
            ResultSet rs = stmt.executeQuery("SELECT rating FROM rating WHERE user_id = "+user_id+" AND venue_id = "+venue_id);
            if(rs.next()){
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        return 0;
    }
    
    public List<Rating> getAllRatings() throws SQLException{
        List<Rating> ratings = new ArrayList();
        ResultSet rs = stmt.executeQuery("SELECT * FROM rating");
        while(rs.next()){
            ratings.add(new Rating(rs.getLong(2), rs.getLong(3), rs.getInt(4)));
        }
        return ratings;
    }
    
    public User getUserById(long user_id) throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT * FROM user WHERE id = "+user_id);
        if(rs.next()){
            return new User(rs.getLong(1), rs.getDouble(2), rs.getDouble(3));
        }
        else {
            return null;
        }
    }
}
