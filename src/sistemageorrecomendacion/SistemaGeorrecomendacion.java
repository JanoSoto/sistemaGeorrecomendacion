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
            centroids.add(new GeoPoint(40.7819788, -73.97201339999998));
            centroids.add(new GeoPoint(40.7852756, -73.9696032));
            centroids.add(new GeoPoint(40.7794839, -73.9558399));
            centroids.add(new GeoPoint(40.776215, -73.9762432));
            centroids.add(new GeoPoint(40.77350719999999, -73.95974269999999));
            centroids.add(new GeoPoint(40.788644, -73.97621800000002));
            centroids.add(new GeoPoint(40.783934, -73.979917));
            centroids.add(new GeoPoint(40.778453, -73.98196999999999));
            centroids.add(new GeoPoint(40.7858636, -73.95094990000001));
            centroids.add(new GeoPoint(40.7984831, -73.95261310000001));
            centroids.add(new GeoPoint(40.79998380000001, -73.95847989999999));
            centroids.add(new GeoPoint(40.8022138, -73.94974359999998));
            centroids.add(new GeoPoint(40.7960878, -73.96145260000003));
            centroids.add(new GeoPoint(40.8046406, -73.9555416));
            centroids.add(new GeoPoint(40.7948233, -73.94463150000001));
            centroids.add(new GeoPoint(40.79882449999999, -73.9416071));
            centroids.add(new GeoPoint(40.7901906, -73.94767919999998));
            centroids.add(new GeoPoint(40.791642, -73.964696));
            centroids.add(new GeoPoint(40.7991871, -73.96830090000003));
            centroids.add(new GeoPoint(40.8171269, -73.94824080000001));
            centroids.add(new GeoPoint(40.8214186, -73.9541375));
            centroids.add(new GeoPoint(40.81416349999999, -73.9406275));
            centroids.add(new GeoPoint(40.8107556, -73.95283259999997));
            centroids.add(new GeoPoint(40.8240445, -73.94444959999998));
            centroids.add(new GeoPoint(40.8155364, -73.95841209999998));
            centroids.add(new GeoPoint(40.82655099999999, -73.95035999999999));
            centroids.add(new GeoPoint(40.8076995, -73.94573579999997));
            centroids.add(new GeoPoint(40.82042190000001, -73.9362544));
            centroids.add(new GeoPoint(40.82405039999999, -73.9369385));
            centroids.add(new GeoPoint(40.7655704, -73.98043840000003));
            centroids.add(new GeoPoint(40.7629289, -73.98159140000001));
            centroids.add(new GeoPoint(40.7687006, -73.98157609999998));
            centroids.add(new GeoPoint(40.7687348, -73.9821078));
            centroids.add(new GeoPoint(40.76135439999999, -73.98372979999999));
            centroids.add(new GeoPoint(40.7604823, -73.9834902));
            centroids.add(new GeoPoint(40.7594853, -73.98118669999997));
            centroids.add(new GeoPoint(40.76266, -73.96725800000002));
            centroids.add(new GeoPoint(40.76298809999999, -73.96786609999998));
            centroids.add(new GeoPoint(40.7576981, -73.96907420000002));
            centroids.add(new GeoPoint(40.7648991, -73.96641929999998));
            centroids.add(new GeoPoint(40.7591386, -73.9532542));
            centroids.add(new GeoPoint(40.7571494, -73.97214980000001));
            centroids.add(new GeoPoint(40.768141, -73.96386999999999));
            centroids.add(new GeoPoint(40.7602095, -73.97553979999998));
            centroids.add(new GeoPoint(40.7652165, -73.9747228));
            centroids.add(new GeoPoint(40.763902, -73.97734249999996));
            centroids.add(new GeoPoint(40.7520124, -73.99333380000002));
            centroids.add(new GeoPoint(40.7549699, -74.00100580000003));
            centroids.add(new GeoPoint(40.750568, -73.99351899999999));
            centroids.add(new GeoPoint(40.7574294, -73.98993389999998));
            centroids.add(new GeoPoint(40.75630519999999, -73.98733149999998));
            centroids.add(new GeoPoint(40.75382099999999, -73.98196300000001));
            centroids.add(new GeoPoint(40.7496439, -73.9876706));
            centroids.add(new GeoPoint(40.752397, -73.97746919999997));
            centroids.add(new GeoPoint(40.75272619999999, -73.9772294));
            centroids.add(new GeoPoint(40.7485354, -73.9885466));
            centroids.add(new GeoPoint(40.75504189999999, -73.98419799999999));
            centroids.add(new GeoPoint(40.75433649999999, -73.98709550000001));
            centroids.add(new GeoPoint(40.7552225, -73.98740200000003));
            centroids.add(new GeoPoint(40.750766, -73.99033539999999));
            centroids.add(new GeoPoint(40.7556158, -73.98633469999999));
            centroids.add(new GeoPoint(40.7452995, -73.99815380000001));
            centroids.add(new GeoPoint(40.744081, -73.995657));
            centroids.add(new GeoPoint(40.7472202, -73.9931403));
            centroids.add(new GeoPoint(40.7402058, -73.98634700000002));
            centroids.add(new GeoPoint(40.7433087, -73.98386289999996));
            centroids.add(new GeoPoint(40.73143200000001, -73.98216689999998));
            centroids.add(new GeoPoint(40.732849, -73.98612200000002));
            centroids.add(new GeoPoint(40.7415465, -73.98899779999999));
            centroids.add(new GeoPoint(40.7466576, -73.98188729999998));
            centroids.add(new GeoPoint(40.74530110000001, -73.98855679999997));
            centroids.add(new GeoPoint(40.74273009999999, -73.9926213));
            centroids.add(new GeoPoint(40.7300946, -73.9908418));
            centroids.add(new GeoPoint(40.7430351, -73.99303320000001));
            centroids.add(new GeoPoint(40.7330459, -74.00705219999998));
            centroids.add(new GeoPoint(40.73346859999999, -74.00307499999997));
            centroids.add(new GeoPoint(40.73110409999999, -74.00122010000001));
            centroids.add(new GeoPoint(40.734161, -73.99879450000003));
            centroids.add(new GeoPoint(40.728251, -74.00536699999998));
            centroids.add(new GeoPoint(40.7399508, -74.00273900000002));
            centroids.add(new GeoPoint(40.7387388, -73.99975169999999));
            centroids.add(new GeoPoint(40.7306255, -73.99266979999999));
            centroids.add(new GeoPoint(40.7371991, -73.996692));
            centroids.add(new GeoPoint(40.722854, -74.00627700000001));
            centroids.add(new GeoPoint(40.726221, -74.00401299999999));
            centroids.add(new GeoPoint(40.7352838, -73.99105750000001));
            centroids.add(new GeoPoint(40.74104, -73.99787100000003));
            centroids.add(new GeoPoint(40.724107, -73.99766690000001));
            centroids.add(new GeoPoint(40.7250154, -73.99569559999998));
            centroids.add(new GeoPoint(40.7260522, -73.9947573));
            centroids.add(new GeoPoint(40.7184209, -73.98718459999998));
            centroids.add(new GeoPoint(40.7144029, -73.9899188));
            centroids.add(new GeoPoint(40.71880369999999, -73.98823920000001));
            centroids.add(new GeoPoint(40.7181922, -73.99358159999997));
            centroids.add(new GeoPoint(40.720302, -73.993423));
            centroids.add(new GeoPoint(40.723938, -73.99129169999998));
            centroids.add(new GeoPoint(40.7013507, -73.98660280000001));
            centroids.add(new GeoPoint(40.7220965, -73.99679859999998));
            centroids.add(new GeoPoint(40.7178324, -74.00026020000001));
            centroids.add(new GeoPoint(40.7186153, -74.00059909999999));
            centroids.add(new GeoPoint(40.7137446, -74.00904409999998));
            centroids.add(new GeoPoint(40.713469, -74.00673569999998));
            centroids.add(new GeoPoint(40.7127166, -74.00777540000001));
            centroids.add(new GeoPoint(40.7157399, -74.0093248));
            centroids.add(new GeoPoint(40.71224919999999, -74.01030420000001));
            centroids.add(new GeoPoint(40.7127901, -74.0049219));
            centroids.add(new GeoPoint(40.71271, -74.01193));
            centroids.add(new GeoPoint(40.71416840000001, -74.00318870000001));
            centroids.add(new GeoPoint(40.7101902, -74.00758810000002));
            centroids.add(new GeoPoint(40.7095114, -74.00608979999998));
            centroids.add(new GeoPoint(40.7108642, -74.01069789999997));
            centroids.add(new GeoPoint(40.7017448, -74.01297790000001));
            centroids.add(new GeoPoint(40.7017396, -74.01290990000001));
            centroids.add(new GeoPoint(40.70463580000001, -74.01354409999999));
            centroids.add(new GeoPoint(40.70756239999999, -74.01384740000003));
            centroids.add(new GeoPoint(40.70647599999999, -74.011056));
            centroids.add(new GeoPoint(40.7076161, -74.01297290000002));
            centroids.add(new GeoPoint(40.7076093, -74.01197530000002));
            centroids.add(new GeoPoint(40.7066642, -74.00864869999998));
            centroids.add(new GeoPoint(40.709373, -74.00832579999997));
            centroids.add(new GeoPoint(40.7104122, -74.00974789999998));
            
            Prediction prediction = new Prediction(queries);
            
            int items = 20;
            int neighborhood_size = 20;
            //Users para probar (cantidad de correlaciones): 4442(36), 485(27), 423(14), 2028(10)
            long user_id = 485;
            int r_inner = 1000;
            int r_outer = 10000;
            
            //prediction.getTopKItems(items, neighborhood_size, user_id, manhattanFrontier);
            //prediction.getTopKItemsDistanceDecay(items, neighborhood_size, user_id, r_inner, r_outer);
            prediction.getBestClusterPath(items, neighborhood_size, user_id, manhattanFrontier, centroids);
            
        }
        catch(ClassNotFoundException | SQLException e){ 
            e.printStackTrace(System.out);
        }  
        
    }
    
}
