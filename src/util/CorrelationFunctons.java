/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import entities.Rating;
import entities.User;
import it.unimi.dsi.fastutil.longs.LongArraySet;
import it.unimi.dsi.fastutil.longs.LongSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

/**
 *
 * @author JAno
 */
public class CorrelationFunctons {
    
    Query query;
    
    public CorrelationFunctons(Query query){
        this.query = query;
    }
    
    public void calculateCorrelation() throws SQLException{
        List<User> users = query.getAllUsers();
        for(int i = 0; i < users.size(); i++){
            for(int j = i+1; j < users.size(); j++){
                //if(!query.existsCorrelation(users.get(i).getId(), users.get(j).getId())){
                    double [][] array = getArraysToCalculateCorrelation(query.getRatingsByUser(users.get(i).getId()), query.getRatingsByUser(users.get(j).getId()));
                    if(array != null){
                        try{
                            double corr = new PearsonsCorrelation().correlation(array[0], array[1]);                        
                            System.out.println("Calculando la correlación de: "+users.get(i).getId()+" con "+users.get(j).getId()+" -> Resultado: "+corr);
                            if(!Double.isNaN(corr)){
                                query.saveCorrelation(users.get(i).getId(), users.get(j).getId(), corr);
                            }
                        }
                        catch(MathIllegalArgumentException e){
                        }
                    }       
                    else{
                        System.out.println(users.get(i).getId()+" no tiene elementos comunes con "+users.get(j).getId());
                    }
                //}                
            }
        }
    }
    
    private List<Rating> cleanList(List<Rating> ratings){
        List<Rating> aux = new ArrayList();
        for(Rating rt : ratings){
            if(!isInTheList(rt.getVenue_id(), aux)){
                aux.add(rt);
            }
        }
        
        return aux;
    }
    
    private boolean isInTheList(long venue_id, List<Rating> list){
        for(Rating rt : list){
            if(rt.getVenue_id() == venue_id){
                return true;
            }
        }
        return false;
    }
    
    private int countComunElements(List<Rating> list1, List<Rating> list2){        
        int counter = 0;
        
        for(Rating rt1 : list1){
            for(Rating rt2: list2){
                if(rt1.getVenue_id() == rt2.getVenue_id()){
                    counter++;
                    break;
                }
            }
        }
        return counter;
    }
    
    public double[][] getArraysToCalculateCorrelation(List<Rating> user1, List<Rating> user2){
        
        user1 = cleanList(user1);
        user2 = cleanList(user2);
        
        int count = countComunElements(user1, user2);        
        
        //System.out.println("Cantidad de elementos comunes: "+count);
        if(count == 0){
            return null;
        }
        
        /*
        double [][] user1Array = new double[2][user1.size()];
        for(int i = 0; i < user1.size(); i++){
            user1Array[0][i] = user1.get(i).getVenue_id();
            user1Array[1][i] = user1.get(i).getRating();
        }
        System.out.println("user1");
        System.out.println(Arrays.toString(user1Array[0]));
        System.out.println(Arrays.toString(user1Array[1]));
        
        double [][] user2Array = new double[2][user2.size()];
        for(int i = 0; i < user2.size(); i++){
            user2Array[0][i] = user2.get(i).getVenue_id();
            user2Array[1][i] = user2.get(i).getRating();
        }
        System.out.println("user2");
        System.out.println(Arrays.toString(user2Array[0]));
        System.out.println(Arrays.toString(user2Array[1]));
        */
        
        double[][] userArrays = new double[2][count];
        
        int i = 0;
        for(Rating rt1: user1){
            for(Rating rt2 : user2){
                if(rt1.getVenue_id() == rt2.getVenue_id()){
                    userArrays[0][i] = rt1.getRating();
                    userArrays[1][i] = rt2.getRating();
                    i++;
                    break;
                }
            }
        }        
        
        
        System.out.println(Arrays.toString(userArrays[0]));
        System.out.println(Arrays.toString(userArrays[1]));
        return userArrays;
        /*
        
        int i = 0;
        for(Rating rating : merge){
            boolean flag_u1 = false;
            boolean flag_u2 = false;
            for(Rating rt1 : user1){
                if(Objects.equals(rt1.getVenue_id(), rating.getVenue_id())){
                    flag_u1 = true;
                    //break;
                }
            }
            if(flag_u1){
                userArrays[0][i] = (double) rating.getRating();
            }
            else{
                userArrays[0][i] = 0.0;
            }
            
            for(Rating rt2 : user2){
                if(Objects.equals(rt2.getVenue_id(), rating.getVenue_id())){
                    flag_u2 = true;
                    //break;
                }
            }
            if(flag_u2){
                userArrays[1][i] = (double) rating.getRating();
            }
            else{
                userArrays[1][i] = 0.0;
            }
            i++;
        }
        */
        
    }    
    
    
    public List<Rating> mergeList(List<Rating> list1, List<Rating> list2){
        List<Rating> merge;
        merge = new ArrayList();
        
        for(Rating rating : list1){
            merge.add(rating);
        }
        
        for(Rating rt : list2){
            boolean flag = true;
            for(Rating rating : list1){
                if(Objects.equals(rt.getVenue_id(), rating.getVenue_id())){
                    flag = false;
                    break;
                }
            }
            if(flag){
                merge.add(rt);
            }
        }
        
        return merge;
    }
    
    public BlockRealMatrix getMatrixUserItem() throws SQLException{
        LongArraySet usersId = query.getUsersIds();
        System.out.println(usersId.size()+" Ids recuperaados desde la base de datos.");
        System.out.println("Creando la matriz vacía...");
        
        //double [][] matrix = new double[usersId.size()][query.countVenues()];
        BlockRealMatrix dataMatrix = new BlockRealMatrix(usersId.size(), query.countVenues());
        
        System.out.println("BlockRealmMatrix creada con exito.");
        
        for(Long id : usersId){
            System.out.println("Agregando los ratings del usuario "+id);
            for(Rating rt : query.getRatingsByUser(id)){
                //matrix[(int) (id-1)][(int) (rt.getVenue_id()-1)] = rt.getRating();
                dataMatrix.setEntry((int) (id-1), (int) (rt.getVenue_id()-1), rt.getRating());
            }
        }
        
        
        return dataMatrix;
    }
}
