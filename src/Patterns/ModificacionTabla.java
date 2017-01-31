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

/**
 *
 * @author Daniel
 */
public class ModificacionTabla extends ModificacionTemplate{
    
    String nombreTabla;
    String nombreCampo;
    String nuevoCampo;
    
    private int posCampo = -1;
    private ArrayList<String> valCampos = new ArrayList<>();

    @Override
    protected void IdentificarParametros(Object[] args) {
        this.nombreTabla = (String) args[0];
        this.nombreCampo = (String) args[1];
        this.nuevoCampo = (String) args[2];
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
                
                //Busqueda de la posicion del campo
                this.posCampo = -1;
                flujoEntrada.readRecord();
                
                for (int i = 0; i< flujoEntrada.getColumnCount();i++){
                    if(flujoEntrada.get(i).equals(this.nombreCampo)){
                        this.posCampo = i;
                        this.valCampos.add(this.nuevoCampo);
                    }
                    else
                        this.valCampos.add(flujoEntrada.get(i));
                } 
                
                if(this.posCampo == -1)
                    throw new Error("Lo sentimos, no se ha encontrado el campo a modificar");
                //Cambiar la cabecera del archivo
                
                PrintWriter flujoSalida = new PrintWriter("filesBD\\" + this.nombreTabla + ".BD");//Crear el archivo luego de superar la condicion
                flujoSalida.println(this.valCampos.toString().replace("[", "").replace("]", "").replace(" ",""));
                
                //El siguiente proceso es por si el archivo contiene registros, es decir una tabla con registros
                //Lectura y escritura
                while(flujoEntrada.readRecord()){
                    flujoSalida.println(flujoEntrada.getRawRecord());
                }
                    
                flujoEntrada.close();
                flujoSalida.close();
                
                //Borrar el archivo auxiliar
                new File("filesBD\\" + this.nombreTabla + "AuxTabla77.BD").delete();
            }
        } catch (FileNotFoundException ex) {
            throw new Error("Lo sentimos, algo salio mal con los archivos internos");
        } catch (IOException ex) {
            throw new Error("Error inesperado, algo salio mal con los archivos internos");
        }
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
                        for(int i = 0; i<4;i++){
                            flujoSalida.print(flujoEntrada.get(i) +",");
                        }
                        flujoSalida.println(this.valCampos.toString().replace("[", "").replace("]", "").replace(" ", ""));
                        
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
