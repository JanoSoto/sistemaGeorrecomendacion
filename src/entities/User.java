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

/**
 *
 * @author JAno
 */
public class User {
    private long id;
    private double latitude;
    private double longitude;

    public User(){
        this.id = 0;
        this.latitude = 0;
        this.longitude = 0;
    }
    
    public User(long id, double latitude, double longitude){
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
    public static List<User> getUsersFromFile(){
        System.out.println("Cargando la lista de usuarios...");
        List<User> users = new ArrayList();
        
        try {
            BufferedReader br = new BufferedReader(new FileReader("foursquare_datasets/users.dat"));
            br.readLine();
            br.readLine();
            String line = br.readLine();
            while(line != null){
                line = line.replace("|", ",").replace(" ", "");
                String[] user = line.split(",");
                if(user.length == 3){
                    users.add(new User(Long.parseLong(user[0]), Double.parseDouble(user[1]), Double.parseDouble(user[2])));
                }
                line = br.readLine();
            }
            
        } 
        catch (FileNotFoundException ex) {
            ex.printStackTrace(System.out);
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
        
        System.out.println("Largo de la lista de usuarios: "+users.size());
        System.out.println("Usuarios cargados con éxito");
        return users;
    }
    
    
}
