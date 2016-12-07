/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;

/**
 *
 * @author JAno
 */
public class FileParser {
    
    public static void transformToCsv(){
        File dir = new File("foursquare_datasets");
        File[] files = dir.listFiles();
        try{
            BufferedReader br;
            for(File file : files){
                System.out.println("Leyendo el archivo "+file.getName()+" ...");
                br = new BufferedReader(new FileReader(file));
                String line = br.readLine();
                line = br.readLine();
                line = br.readLine();
                int i = 0, numero_archivo = 1, id = 1;
                FileWriter writer = new FileWriter("dat/"+file.getName().split(".dat")[0]+"_"+numero_archivo+".dat");
                while(line != null){
                    if(i < 500000){
                        /*
                        line = line.replace("|", ",").replace(" ", "");
                        String[] parser = line.split(",");
                        if(parser.length == 3){
                            writer.write(parser[0]+","+parser[1]+","+parser[2]+"\n");
                        }
                        */
                        writer.write(id+" | "+line+"\n");
                        line = br.readLine();
                        id++;
                        i++;
                    }
                    else{
                        writer.flush();
                        numero_archivo++;
                        i=0;
                        writer = new FileWriter("dat/"+file.getName().split(".dat")[0]+"_"+numero_archivo+".dat");
                    }
                }
                br.close();
            }
        }
        catch(Exception e){
            e.printStackTrace(System.out);
        }
    }
    
    public int convertToTimestamp(String date_time){
        return Integer.parseInt(date_time.replace("-", "").replace(" ", "").replace(":", ""));
    }
    
}
