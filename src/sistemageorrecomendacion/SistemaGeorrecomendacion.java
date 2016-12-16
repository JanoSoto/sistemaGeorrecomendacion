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
import java.util.List;
import recomendation.Prediction;
import util.CorrelationFunctons;
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
            String [] frontier = {"40.743236 -73.971929",
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
            Prediction prediction = new Prediction(queries);
            
            prediction.getTopKItems(10, 10, 423, frontier);
            
        }
        catch(ClassNotFoundException | SQLException e){ 
            e.printStackTrace(System.out);
        }  
        
    }
    
}
