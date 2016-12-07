/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemageorrecomendacion;

import com.mysql.jdbc.Connection;
import entities.Rating;
import entities.User;
import entities.Venue;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import util.CorrelationFunctons;
import util.FileParser;
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
            //here sonoo is database name, root is username and password  
            System.out.println("Conexion con la base de datos exitosa");
            Statement stmt = con.createStatement();  
            Query queries = new Query(stmt);
            CorrelationFunctons correlation = new CorrelationFunctons(queries);
            /*
            double [] array1 = new double[6];
            double [] array2 = new double[6];
            
            array1[0] = 5;
            array1[2] = 3;
            array1[3] = 4;
            array1[5] = 1;
            
            array2[0] = 2;
            array2[1] = 3;
            array2[3] = 3;
            array2[4] = 4;
            array2[5] = 2;
            
            System.out.println("CORRELACION: "+new PearsonsCorrelation().correlation(array1, array2));
            */
            
            /*
            double [][] matriz = new double[4][4];
            matriz[0][0] = 2;
            matriz[1][0] = 3;
            matriz[2][0] = 4;
            matriz[3][0] = 1;
            matriz[0][1] = 3;
            matriz[1][1] = 5;
            matriz[2][1] = 5;
            matriz[3][1] = 3;
            matriz[0][2] = 1;
            matriz[1][2] = 2;
            matriz[2][2] = 4;
            matriz[3][2] = 1;
            matriz[0][3] = 3;
            matriz[1][3] = 4;
            matriz[2][3] = 5;
            matriz[3][3] = 2;
            
            BlockRealMatrix dataMatrix = new BlockRealMatrix(matriz);
            BlockRealMatrix correlationMatrix = (BlockRealMatrix) new PearsonsCorrelation().computeCorrelationMatrix(dataMatrix.transpose());
            System.out.println("rows: "+correlationMatrix.getRowDimension());
            System.out.println("columns: "+correlationMatrix.getColumnDimension());
            System.out.println("row[0]: "+Arrays.toString(correlationMatrix.getRow(0)));
            System.out.println("row[1]: "+Arrays.toString(correlationMatrix.getRow(1)));
            System.out.println("row[2]: "+Arrays.toString(correlationMatrix.getRow(2)));
            System.out.println("row[3]: "+Arrays.toString(correlationMatrix.getRow(3)));
            
            System.out.println("---");
            
            System.out.println("Correlacion [2,3]: "+correlationMatrix.getEntry(2,3));
            
            System.out.println("---");
            
            System.out.println("CORRELACION (0,1): "+new PearsonsCorrelation().correlation(matriz[0], matriz[1]));
            System.out.println("CORRELACION (0,2): "+new PearsonsCorrelation().correlation(matriz[0], matriz[2]));
            System.out.println("CORRELACION (0,3): "+new PearsonsCorrelation().correlation(matriz[0], matriz[3]));
            */
            
            try{
                System.out.println("Calculando las correlaciones...");
                correlation.calculateCorrelation();
                /*
                System.out.println("Obteniendo la matriz de datos...");
                BlockRealMatrix dataMatrix = correlation.getMatrixUserItem();
                System.out.println("Matriz de datos obtenida.");
                System.out.println("Calculando las correlaciones");
                BlockRealMatrix correlationMatrix = (BlockRealMatrix) new PearsonsCorrelation().computeCorrelationMatrix(dataMatrix.transpose());
                System.out.println("CORRELACION ENTRE EL 0 Y 1; "+correlationMatrix.getEntry(0, 1));                
                */
            }
            catch(Exception e){
                e.printStackTrace(System.out);
            }
        }
        catch(ClassNotFoundException | SQLException e){ 
            e.printStackTrace(System.out);
        }  
        
    }
    
}
