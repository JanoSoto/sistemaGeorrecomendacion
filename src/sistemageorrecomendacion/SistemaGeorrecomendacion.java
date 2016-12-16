/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemageorrecomendacion;

import com.mysql.jdbc.Connection;
import entities.User;
import entities.Venue;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import recomendation.Prediction;
import util.CorrelationFunctons;
import util.GeoPoint;
import util.Query;

/**
 *
 * @author JAno
 */
public class SistemaGeorrecomendacion {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //FileParser.transformToCsv();
        
        try{  
            Class.forName("com.mysql.jdbc.Driver");  
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/georrecomendation","root","root");  
            System.out.println("Conexion con la base de datos exitosa");
            Statement stmt = con.createStatement();  
            Query queries = new Query(stmt);
            //CorrelationFunctons correlation = new CorrelationFunctons(queries);            
            //Zona norte de manhattan
            String [] northManhattanFrontier = {"40.743236 -73.971929",
                                    "40.773991 -73.943686",
                                    "40.781335 -73.944343",
                                    "40.783700 -73.943028",
                                    "40.786287 -73.938954",
                                    "40.795702 -73.929351",
                                    "40.801065 -73.929509",
                                    "40.811750 -73.954688",
                                    "40.818978 -73.961205",
                                    "40.762723 -74.002764",
                                    "40.743236 -73.971929"};
            
            //Zona sur de manhattan
            String [] southManhattanFrontier = {"40.704344 -74.016963",
                                        "40.701314 -74.014298",
                                        "40.702212 -74.009857",
                                        "40.708609 -73.998310",
                                        "40.711302 -73.977436",
                                        "40.729479 -73.971662",
                                        "40.731498 -73.973883",
                                        "40.736322 -73.975067",
                                        "40.742940 -73.971810",
                                        "40.757296 -74.005416",
                                        "40.750343 -74.009709",
                                        "40.705553 -74.018760",
                                        "40.704344 -74.016963"};
            
            String [] manhattanFrontier = {"40.701434 -74.015741",
                                            "40.702072 -74.009203",
                                            "40.708044 -73.999555",
                                            "40.711383 -73.978162",
                                            "40.729505 -73.971995",
                                            "40.731317 -73.973757",
                                            "40.736276 -73.974764",
                                            "40.775265 -73.942673",
                                            "40.783079 -73.944058",
                                            "40.787367 -73.938646",
                                            "40.792416 -73.934745",
                                            "40.795244 -73.929963",
                                            "40.803742 -73.930767",
                                            "40.809479 -73.935047",
                                            "40.828099 -73.934753",
                                            "40.834434 -73.949766",
                                            "40.750817 -74.008928",
                                            "40.704982 -74.017012",
                                            "40.701434 -74.015741"};
            
            List<GeoPoint> centroids = new ArrayList();
            centroids.add(new GeoPoint(40.707512, -74.011941));
            centroids.add(new GeoPoint(40.719209, -74.006779));
            centroids.add(new GeoPoint(40.718570, -73.988256));
            centroids.add(new GeoPoint(40.730460, -73.992928));
            centroids.add(new GeoPoint(40.743132, -73.984000));
            centroids.add(new GeoPoint(40.751833, -73.993307));
            centroids.add(new GeoPoint(40.759441, -73.981111));
            centroids.add(new GeoPoint(40.768617, -73.981244));
            centroids.add(new GeoPoint(40.781905, -73.972062));
            centroids.add(new GeoPoint(40.785857, -73.950950));
            centroids.add(new GeoPoint(40.807716, -73.964109));
            centroids.add(new GeoPoint(40.817120, -73.948240));
            centroids.add(new GeoPoint(40.805128, -73.939088));
            centroids.add(new GeoPoint(40.768140, -73.963862));
            
            Prediction prediction = new Prediction(queries);
            
            int items = 20;
            int neighborhood_size = 20;
            //Users para probar (cantidad de correlaciones): 4442(36), 485(27), 423(14), 2028(10)
            long user_id = 485;
            int r_inner = 1000;
            int r_outer = 10000;
            
            prediction.getTopKItems(items, neighborhood_size, user_id, manhattanFrontier);
            //prediction.getTopKItemsDistanceDecay(items, neighborhood_size, user_id, r_inner, r_outer);
            
        }
        catch(ClassNotFoundException | SQLException e){ 
            e.printStackTrace(System.out);
        }  
        
    }
    
}
