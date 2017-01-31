 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Patterns;

import com.csvreader.CsvReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Daniel
 */
public class CreacionTabla extends CreacionTemplate{
    
    private String nombreTabla;
    private ArrayList<String> campos;
    private String campoClave;
    private int longitudCampos;

    @Override
    protected void IdentificarParametros(Object[] args) {
        this.nombreTabla = (String) args[0];
        this.campos = (ArrayList<String>) (List) args[1];
        this.campoClave = (String) args[2];
        this.longitudCampos = (int) args[3];
    }
    
    @Override
    protected void RealizarOperacion() {
        
        new File("filesBD\\").mkdirs();//Crea el directorio en caso de que no exista
        
        try{
            PrintWriter flujoSalida = new PrintWriter("filesBD" + "\\" + nombreTabla + ".bd"); 
            flujoSalida.println(this.campos.toString().replace("[", "").replace("]", "").replace(" ", ""));
            flujoSalida.close();
            
        }
        catch(Exception e ){
            throw new Error("Lo sentimos, algo salio mal con los archivos internos");
        }   
    }

    @Override
    protected void ActualizarMETA() {
        try {
            File file = new File(this.fileMETA);
            if(file.exists()){
                Scanner scan =  new Scanner(new File(this.fileMETA)).useDelimiter("\\A");
                String contenido =scan.next();
                scan.close();
                file.delete();
                PrintWriter wr = new PrintWriter(this.fileMETA);
                wr.print(contenido + "\n");
                wr.println(this.nombreTabla + "," + "0" + "," + this.campoClave + "," + this.longitudCampos
                + "," + this.campos.toString().replace("[", "").replace("]", "").replace(" ", ""));
                wr.close();
            }
            else{
                PrintWriter wr = new PrintWriter(this.fileMETA);
                wr.println(this.nombreTabla + "," + "0" + "," + this.campoClave + "," + this.longitudCampos
                + "," + this.campos.toString().replace("[", "").replace("]", "").replace(" ", ""));
                wr.close();
            }
        } catch (FileNotFoundException ex) {
            throw new Error("Lo sentimos, algo salio mal con los archivos internos");}
    }
}