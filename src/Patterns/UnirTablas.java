/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Patterns;

import SQLUC.TablaRegistrada;
import com.csvreader.CsvReader;
import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;

/**
 *
 * @author Daniel
 */
public class UnirTablas {
    private final String nombreTabla1;
    private final String nombreTabla2;
    private final String nombreCampo;

    public UnirTablas(Object[] args) {
        this.nombreTabla1 = (String) args[0];
        this.nombreTabla2 = (String) args[1];
        this.nombreCampo = (String) args[2];
    }
    
    private ArrayList<String> getCamposTabla(String nombreTabla, int posicionCampo){
        return null;
    }
    
    
    public void Visualizar(){
        
        ArrayList<ArrayList> tabla = new ArrayList<>();
        ArrayList<String> cabecera = new ArrayList<>();
        /*
            Ejemplo SQL
            https://www.tutorialspoint.com/sql/sql-using-joins.htm
        */
        try{
            
            int posicionCampo1 = -1;
            int posicionCampo2 = -1;
            
            //Posicion y longitud del primer campo
            
            CsvReader read = new CsvReader("filesBD\\" + nombreTabla1 + ".BD");
            read.readRecord();
            cabecera.add(nombreCampo);
            for(int i = 0; i<read.getColumnCount(); i++){
                if(read.get(i).equals(nombreCampo)){
                    posicionCampo1 = i;
                    break;
                }
            }
            for(int i = 0; i<read.getColumnCount(); i++){
                if(i!=posicionCampo1)
                    cabecera.add(read.get(i));
            }
            read.close();
            
            //Posicion y longitud de la segunda tabla
            read = new CsvReader("filesBD\\" + nombreTabla2 + ".BD");
            read.readRecord();
            for(int i = 0; i<read.getColumnCount(); i++){
                if(read.get(i).equals(nombreCampo)){
                    posicionCampo2 = i;
                    break;
                }
            }
            for(int i = 0; i<read.getColumnCount(); i++){
                if(i!=posicionCampo2)
                    cabecera.add(read.get(i));
            }
            read.close();
            
            read = new CsvReader("filesBD\\" + nombreTabla1 + ".BD");
            read.readRecord();
            while(read.readRecord()){
                //Registro de la nueva tabla
                ArrayList<String> registro = new ArrayList<>();
                //Primer campo que es el campo en comun
                registro.add(read.get(posicionCampo1));
                //Registrar el resto de campos de la primera tabla, archivo actual
                for(int i = 0; i<read.getColumnCount(); i++)
                    if(i!=posicionCampo1) registro.add(read.get(i));
                //Recuperar los campos de la segunda tabla
                //SEGUNDO RECORRIDO
                CsvReader interno = new CsvReader("filesBD\\" + nombreTabla2 + ".BD");
                interno.readRecord();
                while(interno.readRecord()){
                    if(interno.get(posicionCampo2).equals(read.get(posicionCampo1))){
                        ArrayList<String> otrosRegistros = (ArrayList<String>) registro.clone();
                        for(int i = 0; i<interno.getColumnCount(); i++)
                            if(i!=posicionCampo2) otrosRegistros.add(interno.get(i));
                        tabla.add(otrosRegistros);
                    }
                }
            }   
        }
        catch (FileNotFoundException ex) {} catch (IOException ex) {}
        
        String matriz[][] = new String[tabla.size()][cabecera.size()];
        System.out.println("Cabecera: " + cabecera);
        for(int i = 0; i<tabla.size(); i++){
            for(int j = 0; j<cabecera.size();j++)
                matriz[i][j] = (String) tabla.get(i).get(j);
        }
        System.out.println("Matriz: " + tabla);
        
        String cabez[] = new String[cabecera.size()];
        for(int i = 0; i<cabecera.size(); i++){
            cabez[i] = cabecera.get(i);
        }
        
        TablaRegistrada tablaVer = new TablaRegistrada();
        tablaVer.setTitle("UNION DE: " + nombreTabla1 + ".BD y " + nombreTabla2 + " por el CAMPO: " + nombreCampo);

        //Creacion de la tabla
        JTable table = new JTable(matriz, cabez); 
        table.setPreferredScrollableViewportSize(new Dimension(tabla.size(), cabecera.size()));

        tablaVer.jScrollPane1.setViewportView(new JTable(matriz, cabez));
        tablaVer.setVisible(true);
    }
}
