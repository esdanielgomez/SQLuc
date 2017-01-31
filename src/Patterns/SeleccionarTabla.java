/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Patterns;

import SQLUC.TablaRegistrada;
import com.csvreader.CsvReader;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JTable;

/**
 *
 * @author Daniel
 */
public class SeleccionarTabla {
    private final String nombreTabla;
    private final String nombreCampo;
    private final String valorCampo;
    private final int posicion;

    public SeleccionarTabla(Object[] args) {
        this.nombreTabla = (String) args[0];
        this.nombreCampo = (String) args[1];
        this.valorCampo = (String) args[2];
        this.posicion = (int) args[3];
    }
    
    public void Visualizar() throws IOException{
        try {           
            
            //Numero de filas y columnas
            File path = new File ("filesBD\\" + nombreTabla + ".bd");
            CsvReader reader = new CsvReader("filesBD\\" + nombreTabla + ".bd");
            int filas = 0;
            int columnas = 0;
            while(reader.readRecord()){
                if(reader.get(posicion).equals(valorCampo))
                    filas++;    
                columnas = reader.getColumnCount();
            }    
            reader.close();
            
            
            //Conseguir las cabeceras
            reader = new CsvReader("filesBD\\" + nombreTabla + ".bd");
            reader.readRecord();
            String matriz[][] = new String[filas][columnas];
            String cabecera[] = new String[columnas];
            for (int i = 0; i<columnas; i++){
                cabecera[i] = reader.get(i);
            
            }
            //Conseguir el cuerpo de la tabla
            int i = 0;
            while(reader.readRecord()){
                if(reader.get(posicion).equals(valorCampo)){
                    for (int j = 0; j<columnas; j++)
                        matriz[i][j] = reader.get(j);
                    i++;   
                }
            }
            reader.close();
            TablaRegistrada tabla = new TablaRegistrada();
            tabla.setTitle("SELECCION DE: " + nombreTabla + ".BD por el CAMPO: " + nombreCampo + " con el VALOR de: "+ valorCampo);
            
            //Creacion de la tabla
            JTable table = new JTable(matriz, cabecera); 
            table.setPreferredScrollableViewportSize(new Dimension(filas, columnas));
            
            tabla.jScrollPane1.setViewportView(new JTable(matriz, cabecera));
            tabla.setVisible(true);
        } catch (FileNotFoundException ex) {
            throw new Error("Lo sentimos, algo salio mal con los archivos internos");
        }
        catch (IOException ex) {
            throw new Error("Error inesperado, algo salio mal con los archivos internos");
        }
    }
}
