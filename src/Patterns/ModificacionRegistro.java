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

/**
 *
 * @author Daniel
 */
public class ModificacionRegistro extends ModificacionTemplate{
    
    String nombreTabla;
    String campoClave;
    String nombreCampo;
    String valorCampo;
    private int posicionCampoClave;

    @Override
    protected void IdentificarParametros(Object[] args) {
        this.nombreTabla = (String) args[0];
        this.campoClave = (String) args[1];
        this.nombreCampo = (String) args[2];
        this.valorCampo = (String) args[3];
        this.posicionCampoClave = (int) args[4];
        
    }
    
    @Override
    protected void RealizarOperacion() {
        try{
            
            File mod = new File("filesBD\\" + this.nombreTabla + ".BD");
            if(mod.exists())
            {
                
                //Renombrar el archivo
                mod.renameTo(new File("filesBD\\" + this.nombreTabla + "AuxTabla77.BD"));
                CsvReader flujoEntrada = new CsvReader("filesBD\\" + this.nombreTabla + "AuxTabla77.BD");
                PrintWriter flujoSalida = new PrintWriter("filesBD\\" + this.nombreTabla + ".BD");
                
                System.out.println("Antes...");
                
                //Lectura y escritura
                flujoEntrada.readRecord();
                flujoSalida.println(flujoEntrada.getRawRecord());
                int posicionCampoCambio = -1;
                System.out.println("Com: " + flujoEntrada.getRawRecord());
                for(int i = 0; i<flujoEntrada.getColumnCount();i++){
                    if(flujoEntrada.get(i).equals(this.nombreCampo)){
                        posicionCampoCambio = i;
                        break;
                    }
                }
                
                if(posicionCampoCambio==-1)
                    throw new Error("Error inesperado, no se pudo acceder a los datos de la tabla");

                while(flujoEntrada.readRecord()){
                    if(!flujoEntrada.get(this.posicionCampoClave).equals(this.campoClave))
                        flujoSalida.println(flujoEntrada.getRawRecord());
                    else{
                        for(int i = 0; i<flujoEntrada.getColumnCount(); i++){
                            if(i!=posicionCampoCambio)
                                flujoSalida.print(flujoEntrada.get(i));
                            else
                                flujoSalida.print(this.valorCampo);
                            if(i+1 == flujoEntrada.getColumnCount())
                                flujoSalida.print("\n");
                            else
                                flujoSalida.print(",");
                        }
                    }
                }
                flujoEntrada.close();
                flujoSalida.close();
                
                //Borrar el archivo auxiliar
                new File("filesBD\\" + this.nombreTabla + "AuxTabla77.BD").delete();
                flujoEntrada.close();
                flujoSalida.close();
            }
        } catch (FileNotFoundException ex) {
            throw new Error("Lo sentimos, algo salio mal con los archivos internos");
        } catch (IOException ex) {
            throw new Error("Error inesperado, algo salio mal con los archivos internos");
        }
    }

    @Override
    protected void ActualizarMETA() {
        //Actualizaciones AQUI
    }
}
