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
public class CreacionRegistro extends CreacionTemplate {
    
    String nombreTabla;
    List<String> valoresCampos;

    @Override
    protected void IdentificarParametros(Object[] args) {
        this.nombreTabla = (String) args[0];
        this.valoresCampos = (List)(ArrayList<String>) args[1];
    }
    
    @Override
    protected void RealizarOperacion() {
        
        try {
            File file = new File("filesBD\\" + this.nombreTabla + ".BD");
            if(file.exists()){
                Scanner db = new Scanner(new File("filesBD\\" + this.nombreTabla + ".BD")).useDelimiter("\\A");
                String contenido = db.next();
                db.close();
                file.delete();
                PrintWriter wr = new PrintWriter("filesBD\\" + this.nombreTabla + ".BD");
                String nuevoRegistro = contenido + "\n" + this.valoresCampos.toString().replace("[", "").replace("]", "").replace(" ", "");
                wr.println(nuevoRegistro);
                wr.close();
            }
            else{
                
            }
            
            
        } catch (FileNotFoundException ex) {}
    }

    @Override
    protected void ActualizarMETA() {
        try{
        //Si el archivo META existe, leer y escribir su contenido
            if(new File(this.fileMETA).exists())
            {
                File file = new File(this.fileMETA);
                //Renombrar el archivo
                file.renameTo(new File(file.getParent() + "\\" + "fileAuxMeta77.BD"));
                CsvReader flujoEntrada = new CsvReader(file.getParent() + "\\" + "fileAuxMeta77.BD");
                
                //Lectura y escritura
                PrintWriter flujoSalida = new PrintWriter(this.fileMETA);//Crear el archivo luego de superar la condicion
                while(flujoEntrada.readRecord()){
                    if(!this.nombreTabla.equals(flujoEntrada.get(0)))
                        flujoSalida.println(flujoEntrada.getRawRecord());
                    else{
                        flujoSalida.print(flujoEntrada.get(0) + ",");
                        flujoSalida.print((Integer.parseInt(flujoEntrada.get(1))+1) + ",");
                        for(int i = 2; i<flujoEntrada.getColumnCount(); i++){
                            flujoSalida.print(flujoEntrada.get(i));
                            if(i+1!=flujoEntrada.getColumnCount())
                                flujoSalida.print(",");
                        }
                        flujoSalida.println();
                    }
                }
                    
                flujoEntrada.close();
                flujoSalida.close();
                
                //Borrar el archivo auxiliar
                new File(file.getParent() + "\\" + "fileAuxMeta77.BD").delete();
            }
        } catch (FileNotFoundException ex) {
            throw new Error("Lo sentimos, algo salio mal con los archivos internos");
        } catch (IOException ex) {
            throw new Error("Lo sentimos, algo salio mal con los archivos internos");
        }
    }
}
