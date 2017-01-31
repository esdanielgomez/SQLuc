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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ProyectoFinal
 */
public class GestionBDProxy implements GestionBD {

    static int caso = 0;
    
    static final int CREARTABLA = 101;
    static final int MODIFICARTABLA = 102;
    static final int ELIMINARTABLA = 103;
    
    static final int CREARREGISTRO = 201;
    static final int MODIFICARREGISTRO = 202;
    static final int ELIMINARREGISTRO = 203;
    
    static final int SELECCIONARTABLAS = 301;
    static final int JOIN = 302;
    
    
    private boolean BuscarTabla(String nombreTabla){    
        if(!new File("filesBD\\META.bd").exists())
            return false;
        String fileMETA = "filesBD\\META.bd";
        try {
            CsvReader ar = new CsvReader(fileMETA);
            while(ar.readRecord()){
                if(ar.get(0).equals(nombreTabla)){
                    ar.close();
                    return true;
                }   
            }
            ar.close();
        } 
        catch (FileNotFoundException ex) {
            throw new Error("Error, algo salio mal con los archivos internos");
        } catch (IOException ex) {
            throw new Error("Error, algo salio mal con los archivos internos");
        }
        return false;
    }
    
    @Override
    public void Peticion(Object[] args) {
        //El argumento en la posicion cero es el comando
        String comando = (String) args[0];
        //Asignacion tipo
        if(comando.replace(" ", "").contains("CREARTABLA"))
            caso = CREARTABLA;
        else if(comando.replace(" ", "").contains("MODIFICARTABLA"))
            caso = MODIFICARTABLA;
        else if(comando.replace(" ", "").contains("ELIMINARTABLA"))
            caso = ELIMINARTABLA;
        else if(comando.replace(" ", "").contains("CREARREGISTRO"))
            caso = CREARREGISTRO;
        else if(comando.replace(" ", "").contains("MODIFICARREGISTRO"))
            caso = MODIFICARREGISTRO;
        else if(comando.replace(" ", "").contains("ELIMINARREGISTRO"))
            caso = ELIMINARREGISTRO;
        else if(comando.replace(" ", "").contains("SELECCIONARDE"))
            caso = SELECCIONARTABLAS;
        else if(comando.replace(" ", "").contains("UNIR"))
            caso = JOIN;
        else
            throw new SecurityException("Lo sentimos, no se ha entendido esa orden.");
        
        //SENTENCIAS DEL LENGUAJE: 
        switch (caso) {
            case CREARTABLA:
            {
                int i = "CREAR TABLA ".length();
                String nombreTabla = null;
                String campoClave = null;
                int longitudCampos = 0;
                List<String> campos = new ArrayList<>();
                for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;
                StringBuffer atributo = new StringBuffer();
                //Nombre de la tabla
                for(i=i+0;i<comando.length();i++)
                {
                    if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                    else break;
                }
                //Palabra campo
                nombreTabla = atributo.toString();
                atributo = new StringBuffer();
                for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;
                
                //Palabra reservada CAMPOS
                for(i=i+0;i<comando.length();i++)
                {
                    if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                    else break;
                }
                
                if(!atributo.toString().equals("CAMPOS"))
                    throw new SecurityException("Error en sintaxis, falta la palabra reservada CAMPOS");
                for (i=i+0;i<comando.length();i++) if(comando.charAt(i)!=' ')break;
                
                //Listado de Campos
                
                atributo = new StringBuffer();
                for(i = i+0; i<comando.length();i++) 
                    atributo.append(comando.charAt(i));
                
                if(!atributo.toString().contains("CLAVE"))
                    throw new SecurityException("Error en sintaxis, falta la palabra reservada CLAVE");
                
                //Listado de campos
                String comando2 = atributo.toString().replace(" ", "");
                atributo = new StringBuffer();
                for(i = 0; i<comando2.indexOf("CLAVE"); i++){
                    if(comando2.charAt(i)!=','){
                        atributo.append(comando2.charAt(i));
                    }
                    else{
                        campos.add(atributo.toString());
                        atributo = new StringBuffer();
                    }
                }
                campos.add(atributo.toString());
                //Campo clave
                atributo = new StringBuffer();
                for(i = i+0; i<comando2.length();i++) 
                    atributo.append(comando2.charAt(i));
                if(!atributo.toString().contains("LONGITUD"))
                    throw new SecurityException("Error en sintaxis, falta la palabra reservada LONGITUD");
                comando = atributo.toString().replace("CLAVE", ""); 
                atributo = new StringBuffer();
                for(i = 0; i<comando.indexOf("LONGITUD"); i++)
                    atributo.append(comando.charAt(i));
                
                campoClave = atributo.toString();
                comando = comando.replace(campoClave, "");
                //Longitud de los campos
                comando = comando.replace("LONGITUD", "");
                try{
                    longitudCampos = Integer.parseInt(comando);
                }
                catch (Exception e){
                    throw new SecurityException("Error, la longitud ingresada es incorrecta");
                }   
                if(longitudCampos<=0)
                    throw new SecurityException("Error, la longitud ingresada no puede ser negativa o cero");     
                //Validacion de tabla existente
                if(this.BuscarTabla(nombreTabla))
                    throw new SecurityException("Error, el nombre de la tabla que se desea crear ya existe");
                //Validacion longitud
                if(longitudCampos<=0)
                    throw new SecurityException("Error, la longitud del campo clave no puede ser menor o igual que cero");
                
                //Validacion campo clave existente
                if(campos.contains(campoClave) == false)
                    throw new SecurityException("Error, el campo clave no esta dentro de los campos especificados");
                
                /**
                * Solicitud aceptada correctamente
                **/
                
                Object[] arg = {"CREARTABLA",nombreTabla,campos,campoClave,longitudCampos};
                new GestionBDReal().Peticion(arg);
                
                break;
            }
            case MODIFICARTABLA://----------------------------------------------
                {
                    int i = "MODIFICAR TABLA ".length();
                    
                    String nombreTabla = null;
                    String nombreCampo = null;
                    String nuevoCampo = null;
                    
                    for (i=i+0;i<comando.length();i++) if(comando.charAt(i)!=' ')break;
                    
                    StringBuffer atributo = new StringBuffer();
                
                    //Nombre de la tabla
                    for(i=i+0;i<comando.length();i++)
                    {
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    nombreTabla = atributo.toString();
                    
                    if(nombreTabla.isEmpty())
                        throw new SecurityException("Error, no se ha especificado el nombre de la tabla");
                    
                    
                    
                    //Palabra reservada Campo
                    atributo = new StringBuffer();
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;
                    for(i=i+0;i<comando.length();i++){
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    if(!atributo.toString().equals("CAMPO"))
                        throw new SecurityException("Error en sintaxis, falta la palabra reservada CAMPO");
                    
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;

                    //Campo
                    atributo = new StringBuffer();
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;
                    for(i=i+0;i<comando.length();i++){
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    nombreCampo = atributo.toString();
                    if(nombreCampo.isEmpty())
                        throw new SecurityException("Error, no se ha especificado el nombre del campo a modificar");
                    
                    //Palabra reservada POR
                    atributo = new StringBuffer();
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;
                    for(i=i+0;i<comando.length();i++){
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    if(!atributo.toString().equals("POR"))
                        throw new SecurityException("Error en sintaxis, falta la palabra reservada POR");
                    
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;

                    //Nuevo valor del campo
                    atributo = new StringBuffer();
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;
                    for(i=i+0;i<comando.length();i++){
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    nuevoCampo = atributo.toString();
                    if(nuevoCampo.isEmpty())
                        throw new SecurityException("Error, no se ha especificado el nuevo valor del campo");
                    
                    
                    for(i=i+0;i<comando.length();i++){
                        if(comando.charAt(i)!=' ')
                            throw new SecurityException("Error, existen caracteres de mas en la sentencia");
                    }
                    
                    
                    //Validacion de tabla existente
                    if(!this.BuscarTabla(nombreTabla))
                        throw new SecurityException("Error, el nombre de la tabla especificada no existe");
                    
                    //Validacion campo existente
                    ArrayList<String> campos = new ArrayList<>();
                    String fileMETA = "filesBD\\META.bd";
                    boolean boolClave = false;
                    try {
                        CsvReader ar = new CsvReader(fileMETA);
                        while(ar.readRecord()){
                            if(ar.get(0).equals(nombreTabla)){
                                for (int j = 4; j < ar.getColumnCount();j++){
                                    campos.add(ar.get(j));
                                }
                                if(ar.get(2).equals(nombreCampo))
                                    boolClave = true;
                            }
                                                   
                        }
                        ar.close();
                    }
                    catch (FileNotFoundException ex) {
                        throw new Error("Error, algo salio mal con los archivos internos");
                    } catch (IOException ex) {
                        throw new Error("Error, algo salio mal con los archivos internos");
                    }
                    
                    if(!campos.contains(nombreCampo))
                        throw new SecurityException("Error, el nombre del campo especificado no existe");
                    
                    if(boolClave==true)
                        throw new SecurityException("Error, no se puede modificar el campo clave");
                    
                    /**
                    * Solicitud aceptada correctamente
                    **/
                    
                    Object[] arg = {"MODIFICARTABLA",nombreTabla,nombreCampo,nuevoCampo};
                    new GestionBDReal().Peticion(arg);
                    
                    break;
                }
            case ELIMINARTABLA: //----------------------------------------------
                {
                    String nombreTabla = null;
                    
                    int i = "ELIMINAR TABLA ".length();
                    for (i=i+0;i<comando.length();i++) if(comando.charAt(i)!=' ')break;
                    StringBuffer atributo = new StringBuffer();
                
                    //Nombre de la tabla
                    for(i=i+0;i<comando.length();i++){
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    nombreTabla = atributo.toString();
                    if(nombreTabla.isEmpty())
                        throw new SecurityException("Error, no se ha especificado el nombre de la tabla");
                    
                    
                    for(i=i+0;i<comando.length();i++){
                        if(comando.charAt(i)!=' ')
                            throw new SecurityException("Error, existen caracteres de mas en la sentencia");
                    }
                    
                    //Validacion de tabla existente
                    if(!this.BuscarTabla(nombreTabla))
                        throw new SecurityException("Error, el nombre de la tabla especificada no existe");
                    
                    /**
                    * Solicitud aceptada correctamente
                    **/
                    
                    Object[] arg = {"ELIMINARTABLA",nombreTabla};
                    new GestionBDReal().Peticion(arg);
                    
                    break;
                }
            case CREARREGISTRO: //----------------------------------------------
                {
                    int i = "CREAR REGISTRO ".length();
                    
                    String nombreTabla = null;
                    List<String> valoresCampos = new ArrayList<>();
                    
                    
                    for (i=i+0;i<comando.length();i++) if(comando.charAt(i)!=' ')break;
                    
                    StringBuffer atributo = new StringBuffer();
                
                    //Nombre de la tabla
                    for(i=i+0;i<comando.length();i++)
                    {
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    nombreTabla = atributo.toString();
                    
                    if(nombreTabla.isEmpty())
                        throw new SecurityException("Error, no se ha especificado el nombre de la tabla");
                    
                    atributo = new StringBuffer();
                    
                    //Palabra reservada CLAVE
                    
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;
                    for(i=i+0;i<comando.length();i++){
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    if(!atributo.toString().equals("VALOR"))
                        throw new SecurityException("Error en sintaxis, falta la palabra reservada VALOR");
                    
                    atributo = new StringBuffer();
                    for (i=i+0;i<comando.length();i++)
                        if(comando.charAt(i)!=' ')
                            atributo.append(comando.charAt(i));
                            
                    comando = atributo.toString();
                    //Valores de campos
                    
                    atributo = new StringBuffer();
                    for(i = 0; i<comando.length(); i++){
                        if(comando.charAt(i)!=',')
                            atributo.append(comando.charAt(i));
                        else
                        {
                            valoresCampos.add(atributo.toString());
                            atributo = new StringBuffer();
                        }
                    }
                    valoresCampos.add(atributo.toString());
                    
                    //Validacion valores null
                    
                    if(valoresCampos.contains(""))
                        throw new SecurityException("Error, uno de los valores de campos es nulo");
                    
                    //Validacion de tabla existente
                    if(!this.BuscarTabla(nombreTabla))
                        throw new SecurityException("Error, el nombre de la tabla especificada no existe");
                    
                    
                    //Validaciones longitud y numero de campos
                    int nroCampos = - 1;
                    int longitud = -1;
                    String campoClave = null;
                    String fileMETA = "filesBD\\META.bd";
                    try {
                        CsvReader ar = new CsvReader(fileMETA);
                        while(ar.readRecord()){
                            if(ar.get(0).equals(nombreTabla))
                            {
                                nroCampos = ar.getColumnCount()-4;
                                longitud = Integer.parseInt(ar.get(3));
                                campoClave = ar.get(2);
                            }    
                        }
                        ar.close();
                    }
                    catch(IOException | NumberFormatException e){throw new Error("Error, algo salio mal con los archivos internos");}
                    
                    if(nroCampos ==-1 || longitud ==-1 || campoClave == null)
                        throw new SecurityException("Error interno, no se han encontrado los datos de la tabla especificada");
                    
                    if(valoresCampos.size()!=nroCampos)
                        throw new SecurityException("Error, el numero total de valores ingresados no coincide con el numero de campos registrados");
                    
                    for(int k = 0; k<valoresCampos.size();k++){
                        if(valoresCampos.get(k).length()>longitud)
                            throw new SecurityException("Error, en uno de los campos excede la longitud maxima posible");
                    }
                    
                    //Validacion de que el campo clave no se repita
                    try {
                        
                        CsvReader ar = new CsvReader("filesBD\\"+nombreTabla+".BD");
                        int posicion = -1;
                        ar.readRecord();
                        for(int k = 0; k<ar.getColumnCount(); k++){
                            System.out.println("val: " + ar.get(k));
                            if(ar.get(k).equals(campoClave)){
                                posicion = k;
                            }
                        }
                        
                        if(posicion == -1)
                            throw new SecurityException("Error interno, no se han encontrado los datos de la tabla especificada");
                        while(ar.readRecord()){
                            if(ar.get(posicion).equals(valoresCampos.get(posicion)))
                                throw new SecurityException("Error, el valor correspondiente al campo clave ya existe");
                        }
                        ar.close();
                        
                    }catch (FileNotFoundException ex) {throw new Error("Error, algo salio mal con los archivos internos");
                    }catch (IOException ex) {throw new Error("Error inesperado, algo salio mal");}
                    
                    /**
                    * Solicitud aceptada correctamente
                    **/
                    
                    Object[] arg = {"CREARREGISTRO",nombreTabla,valoresCampos};
                    new GestionBDReal().Peticion(arg);
                    
                    break;
                }
            case MODIFICARREGISTRO: //------------------------------------------
                {
                    int i = "MODIFICAR REGISTRO ".length();
                    
                    String nombreTabla = null;
                    String valorCampoClave = null;
                    String nombreCampo = null;
                    String valorCampo = null;
                    
                    for (i=i+0;i<comando.length();i++) if(comando.charAt(i)!=' ')break;
                    
                    StringBuffer atributo = new StringBuffer();
                
                    //Nombre de la tabla
                    for(i=i+0;i<comando.length();i++)
                    {
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    nombreTabla = atributo.toString();
                    
                    if(nombreTabla.isEmpty())
                        throw new SecurityException("Error, no se ha especificado el nombre de la tabla");
                    
                    atributo = new StringBuffer();
                    
                    //Palabra reservada CLAVE
                    
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;
                    for(i=i+0;i<comando.length();i++){
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    if(!atributo.toString().equals("CLAVE"))
                        throw new SecurityException("Error en sintaxis, falta la palabra reservada CLAVE");
                    
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;
                    
                    //Campo clave 
                    atributo = new StringBuffer();
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;
                    for(i=i+0;i<comando.length();i++){
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    valorCampoClave = atributo.toString();
                    if(valorCampoClave.isEmpty())
                        throw new SecurityException("Error, no se ha especificado el campo clave");
                    
                    //Palabra reservada CAMPO
                    atributo = new StringBuffer();
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;
                    for(i=i+0;i<comando.length();i++){
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    if(!atributo.toString().equals("CAMPO"))
                        throw new SecurityException("Error en sintaxis, falta la palabra reservada CAMPO");
                    
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;

                    //Campo
                    atributo = new StringBuffer();
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;
                    for(i=i+0;i<comando.length();i++){
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    nombreCampo = atributo.toString();
                    if(nombreCampo.isEmpty())
                        throw new SecurityException("Error, no se ha especificado el nombre del campo a modificar");
                    
                    //Palabra reservada POR
                    atributo = new StringBuffer();
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;
                    for(i=i+0;i<comando.length();i++){
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    if(!atributo.toString().equals("POR"))
                        throw new SecurityException("Error en sintaxis, falta la palabra reservada POR");
                    
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;

                    //Nuevo valor del campo
                    atributo = new StringBuffer();
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;
                    for(i=i+0;i<comando.length();i++){
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    valorCampo = atributo.toString();
                    if(valorCampo.isEmpty())
                        throw new SecurityException("Error, no se ha especificado el nuevo valor del campo");
                    
                    
                    for(i=i+0;i<comando.length();i++){
                        if(comando.charAt(i)!=' ')
                            throw new SecurityException("Error, existen caracteres de mas en la sentencia");
                    }
                    
                    
                    //Validacion de tabla existente
                    if(!this.BuscarTabla(nombreTabla))
                        throw new SecurityException("Error, el nombre de la tabla especificada no existe");
                    
                    //Validacion tabla con registros
                    try {

                        CsvReader read = new CsvReader("filesBD\\"+nombreTabla+".bd");
                        int cont = 0;
                        while(read.readRecord()) cont++;
                        if(cont<=1) throw new SecurityException("Error, la tabla especificada no cuenta con registros");
                        read.close();
                    } 
                    catch (FileNotFoundException ex) {throw new Error("Error, algo salio mal con los archivos internos");}
                    catch (IOException ex) {throw new Error("Error inesperado, algo salio mal");}
                    
                    //Validaciones de existencia y longitud
                    String campoClaveReal = null;
                    String fileMETA = "filesBD\\META.bd";
                    int posicionCampo = -1;
                    try {
                        CsvReader ar = new CsvReader(fileMETA);
                        while(ar.readRecord()){
                            //Existencia del campo
                            if(ar.get(0).equals(nombreTabla))
                            {
                                boolean estadoEncontrado = false;
                                boolean estadoEncontradoClave = false;
                                campoClaveReal = ar.get(2);
                                for(int w = 4;w<ar.getColumnCount();w++){
                                    if(ar.get(w).equals(nombreCampo)){
                                        estadoEncontrado = true;
                                    }
                                    if(ar.get(w).equals(campoClaveReal)){
                                        estadoEncontradoClave = true;
                                        posicionCampo = w;
                                    }
                                }
                                posicionCampo = posicionCampo - 4;
                                
                                //Errores varios
                                if(estadoEncontrado == false)
                                    throw new SecurityException("Error, el nombre del campo especificado no existe");
                                if(estadoEncontradoClave == false)
                                    throw new SecurityException("Error, el nombre del campo clave especificado no existe");
                                if(ar.get(2).equals(nombreCampo))
                                    throw new SecurityException("Lo sentimos, no es posible modificar el valor del campo clave");
                                if(valorCampo.length()> Integer.parseInt(ar.get(3)))
                                    throw new SecurityException("Error, el nuevo valor del campo excede el maximo de longitud establecido");
                                
                                break;
                            }    
                        }
                        ar.close();
                        
                        
                        CsvReader read = new CsvReader("filesBD\\"+nombreTabla+".bd");
                        boolean estadoEncontrado = false;
                        read.readRecord();
                        while(read.readRecord()){
                            System.out.println("");
                            if(read.get(posicionCampo).equals(valorCampoClave)){
                                estadoEncontrado = true;
                                break;
                            }
                        }
                        read.close();
                        read.close();
                        if(estadoEncontrado==false)
                            throw new SecurityException("Error, no existe un registro con ese valor de campo clave");
                    }
                    catch(IOException | NumberFormatException e){throw new Error("Error, algo salio mal con los archivos internos");}
                    
                    if(campoClaveReal == null)
                        throw new SecurityException("Error interno, no se han encontrado los datos de la tabla especificada");
                    
                    /**
                    * Solicitud aceptada correctamente
                    **/
                    
                    Object[] arg = {"MODIFICARREGISTRO",nombreTabla,valorCampoClave,nombreCampo,valorCampo,posicionCampo};
                    new GestionBDReal().Peticion(arg);
                    
                    break;
                }
            case ELIMINARREGISTRO: //-------------------------------------------
                {
                    String nombreTabla = null;
                    String campoClave = null;
                    
                    int i = "ELIMINAR REGISTRO ".length();
                    
                    for (i=i+0;i<comando.length();i++) if(comando.charAt(i)!=' ')break;
                    
                    StringBuffer atributo = new StringBuffer();
                
                    //Nombre de la tabla
                    for(i=i+0;i<comando.length();i++)
                    {
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    nombreTabla = atributo.toString();
                    
                    if(nombreTabla.isEmpty())
                        throw new SecurityException("Error, no se ha especificado el nombre de la tabla");
                    
                    atributo = new StringBuffer();
                    
                    //Palabra reservada CLAVE
                    
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;
                    for(i=i+0;i<comando.length();i++){
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    if(!atributo.toString().equals("CLAVE"))
                        throw new SecurityException("Error en sintaxis, falta la palabra reservada CLAVE");
                    
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;
                    
                    //Campo clave 
                    atributo = new StringBuffer();
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;
                    for(i=i+0;i<comando.length();i++){
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    campoClave = atributo.toString();
                    if(campoClave.isEmpty())
                        throw new SecurityException("Error, no se ha especificado el campo clave");
                    
                    for(i=i+0;i<comando.length();i++){
                        if(comando.charAt(i)!=' ')
                            throw new SecurityException("Error, existen caracteres de mas en la sentencia");
                    }
                            
                    //Validacion de tabla existente
                    if(!this.BuscarTabla(nombreTabla))
                        throw new SecurityException("Error, el nombre de la tabla especificada no existe");
     
                    //Validacion tabla con registros
                    try {

                        CsvReader read = new CsvReader("filesBD\\"+nombreTabla+".bd");
                        int cont = 0;
                        while(read.readRecord()) cont++;
                        if(cont<=1) 
                            throw new SecurityException("Error, la tabla especificada no cuenta con registros");
                        read.close();
                        read.close();
                    } 
                    catch (FileNotFoundException ex) {throw new Error("Error, algo salio mal con los archivos internos");}
                    catch (IOException ex) {throw new Error("Error inesperado, algo salio mal");}
                    
                    //Validacion de valor de campo clave existente
                    String fileMETA = "filesBD\\META.bd";
                    int posicion = -1;
                    try {
                        String campoClaveReal = null;
                        
                        CsvReader ar = new CsvReader(fileMETA);
                        while(ar.readRecord()){
                            if(ar.get(0).equals(nombreTabla)){
                                campoClaveReal = ar.get(2);
                                for(int k = 4; k<ar.getColumnCount(); k++){
                                    if(ar.get(k).equals(campoClaveReal)){
                                        posicion = k;
                                        break;
                                    }
                                }
                                break;
                            }    
                        }
                        posicion = posicion - 4;
                        ar.close();
                        ar.close();
                        if(campoClaveReal == null || posicion == -1)
                            throw new SecurityException("Error interno, no se han encontrado los datos de la tabla especificada");
                        
                        CsvReader read = new CsvReader("filesBD\\"+nombreTabla+".bd");
                        boolean estadoEncontrado = false;
                        read.readRecord();
                        while(read.readRecord()){
                            if(read.get(posicion).equals(campoClave)){
                                estadoEncontrado = true;
                                break;
                            }
                        }
                        read.close();
                        read.close();
                        if(estadoEncontrado==false)
                            throw new SecurityException("Error, no existe un registro con ese valor de campo clave");
                    } 
                    catch (FileNotFoundException ex) {
                        throw new Error("Error, algo salio mal con los archivos internos");
                    } catch (IOException ex) {
                        throw new Error("Error, algo salio mal con los archivos internos");
                    }
                    
                    /**
                    * Solicitud aceptada correctamente
                    **/
                    
                    Object[] arg = {"ELIMINARREGISTRO",nombreTabla, campoClave, posicion};
                    new GestionBDReal().Peticion(arg);
                    
                    break;
                }
            case SELECCIONARTABLAS://-------------------------------------------
                {
                    int i = "SELECCIONAR DE ".length();
                    
                    String nombreTabla = null;
                    String nombreCampo = null;
                    String valorCampo = null;
                    
                    for (i=i+0;i<comando.length();i++) if(comando.charAt(i)!=' ')break;
                    
                    StringBuffer atributo = new StringBuffer();
                
                    //Nombre de la tabla
                    for(i=i+0;i<comando.length();i++)
                    {
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    nombreTabla = atributo.toString();
                    
                    if(nombreTabla.isEmpty()) throw new SecurityException("Error, no se ha especificado el nombre de la tabla");
                    
                    atributo = new StringBuffer();
                    
                    //Palabra reservada DONDE
                    
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;
                    for(i=i+0;i<comando.length();i++){
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    if(!atributo.toString().equals("DONDE")) throw new SecurityException("Error en sintaxis, falta la palabra reservada DONDE");
                    
                    atributo = new StringBuffer();
                    for(i = i +0; i<comando.length(); i++) atributo.append(comando.charAt(i));
                    
                    if(!atributo.toString().contains("=")) throw new SecurityException("Error en sintaxis, falta el operador =");
                    
                    comando = atributo.toString().replace(" ", "");
                    //Palabra del campo de atributo campo
                    atributo = new StringBuffer();
                    for(i = 0; i<comando.indexOf("=");i++) atributo.append(comando.charAt(i));
                    
                    nombreCampo = atributo.toString();
                    
                    if(!comando.contains("\"")) throw new SecurityException("Error en sintaxis, faltan las comillas \" para especificar el valor de busqueda");
                    if(comando.charAt(i+1)!='\"') throw new SecurityException("Error en sintaxis, faltan las comillas \" para especificar el valor de busqueda");
                    
                    
                    //Valor
                    atributo = new StringBuffer();
                    for(i = i +2; i<comando.length(); i++)atributo.append(comando.charAt(i));
                    comando = atributo.toString();
                    if(!comando.toString().contains("\"")) throw new SecurityException("Error en sintaxis, faltan las comillas \" para especificar el valor de busqueda");
                    
                    atributo = new StringBuffer();
                    for(i = 0; i<comando.indexOf("\""); i++) atributo.append(comando.charAt(i));
                    
                    valorCampo = atributo.toString();
                    if(nombreCampo.isEmpty()) throw new SecurityException("Error, no se ha especificado el nombre del campo");
                    
                    if(valorCampo.isEmpty()) throw new SecurityException("Error, no se ha especificado el valor del campo");
                    
                    
                    for(i = i+1; i<comando.length(); i++) 
                        if(comando.charAt(i)!=' ') throw new SecurityException("Error, existen caracteres de mas en la sentencia");
                    
                    //Validacion de tabla existente
                    if(!this.BuscarTabla(nombreTabla)) throw new SecurityException("Error, el nombre de la tabla especificada no existe");
                    
                    //Validacion de que el campo especificado exista...
                    
                    int posicion = -1;
                    String fileMETA = "filesBD\\META.bd";
                        
                    try {
                        CsvReader ar = new CsvReader(fileMETA);
                        
                        while(ar.readRecord()){
                            if(ar.get(0).equals(nombreTabla)){
                                for(int k = 4; k<ar.getColumnCount(); k++){
                                    if(ar.get(k).equals(nombreCampo)){
                                        posicion = k;
                                        break;
                                    }
                                }
                                break;
                            }    
                        }
                        if(posicion!=-1)
                            posicion = posicion - 4;
                        
                        ar.close();
                        
                    } catch (FileNotFoundException ex) {
                        throw new Error("Error, algo salio mal con los archivos internos");
                    } catch (IOException ex) {
                        throw new Error("Error, algo salio mal con los archivos internos");
                    }
                    if(posicion == -1) throw new SecurityException("Error interno, la tabla " + nombreTabla + " no posee ese campo");
                    
                    /**
                    * Solicitud aceptada correctamente
                    **/
                    
                    Object[] arg = {"SELECCIONAR",nombreTabla, nombreCampo, valorCampo, posicion};
                    new GestionBDReal().Peticion(arg);
                    
                    break;
                }
            case JOIN://--------------------------------------------------------
            {
                int i = "UNIR ".length();

                String nombreTabla1 = null;
                String nombreTabla2 = null;
                String nombreCampo = null;

                for (i=i+0;i<comando.length();i++) if(comando.charAt(i)!=' ')break;

                //Listado de Campos

                StringBuffer atributo = new StringBuffer();
                for(i = i+0; i<comando.length();i++) 
                    atributo.append(comando.charAt(i));

                //Listado de campos
                String comando2 = atributo.toString().replace(" ", "");
                atributo = new StringBuffer();
                
                if(!comando.contains("POR"))
                    throw new SecurityException("Error en sintaxis, falta la palabra reservada POR");

                if(!comando.contains(","))
                    throw new SecurityException("Error en sintaxis, falta especificar el nombre de las tablas");
                
                for(i = 0; i<comando2.indexOf("POR"); i++){
                    if(comando2.charAt(i)!=',')
                        atributo.append(comando2.charAt(i));
                    else
                    {
                        nombreTabla1 = atributo.toString();
                        atributo = new StringBuffer();
                        comando = comando.replace(nombreTabla1, "");
                    }
                }
                nombreTabla2 = atributo.toString();
                comando = comando.replace(nombreTabla2, "");
                if(nombreTabla1.isEmpty())
                    throw new SecurityException("Error, no se ha especificado el nombre de la primera tabla");

                if(nombreTabla2.isEmpty())
                    throw new SecurityException("Error, no se ha especificado el nombre de la segunda tabla");
                
                //Palabra reservada POR
                atributo = new StringBuffer();
                i = comando.indexOf("POR")+3;
                
                for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;

                //Nuevo valor del campo
                atributo = new StringBuffer();
                for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;
                for(i=i+0;i<comando.length();i++){
                    if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                    else break;
                }

                nombreCampo = atributo.toString();
                if(nombreCampo.isEmpty())
                    throw new SecurityException("Error, no se ha especificado el factor de union");


                for(i=i+0;i<comando.length();i++){
                    if(comando.charAt(i)!=' ')
                        throw new SecurityException("Error, existen caracteres de mas en la sentencia");
                }
                
                
                //Validacion de tabla1 existente
                if(!this.BuscarTabla(nombreTabla1))
                    throw new SecurityException("Error, la tabla "+ nombreTabla1 +" no existe");

                if(!this.BuscarTabla(nombreTabla2))
                    throw new SecurityException("Error, la tabla "+ nombreTabla2 +" no existe");
                
                //Validacion campo existentes en las tablas
                boolean estadoTabla1 = false;
                boolean estadoTabla2 = false;
                CsvReader read;
                CsvReader read2;
                try {

                    read = new CsvReader("filesBD\\" + nombreTabla1 + ".BD");
                    read.readRecord();
                    for(i = 0; i<read.getColumnCount();i++){
                        if(!read.getRawRecord().isEmpty())
                            if(read.get(i).equals(nombreCampo)){
                                estadoTabla1 = true;
                                break;
                            }
                    }
                    read.close();
                    read2 = new CsvReader("filesBD\\" + nombreTabla2 + ".BD");
                    read2.readRecord();
                    for(i = 0; i<read2.getColumnCount();i++){
                        if(!read2.getRawRecord().isEmpty())
                            if(read2.get(i).equals(nombreCampo)){
                                estadoTabla2 = true;
                                break;
                            }
                    }
                    read2.close();
                }
                catch (FileNotFoundException ex) {
                    throw new Error("Error, algo salio mal con los archivos internos");
                } catch (IOException ex) {
                    throw new Error("Error, algo salio mal con los archivos internos");
                }
                read2.close();
                read.close();
                
                if(estadoTabla1==false)
                    throw new SecurityException("Error, la tabla " + nombreTabla1 + " no posee el campo " + nombreCampo);
                
                if(estadoTabla2==false)
                    throw new SecurityException("Error, la tabla " + nombreTabla2 + " no posee el campo " + nombreCampo);
                
                /**
                * Solicitud aceptada correctamente
                **/
                
                Object[] arg = {"UNIR",nombreTabla1, nombreTabla2, nombreCampo};
                new GestionBDReal().Peticion(arg);
                
                break;
            }
            default:
                throw new SecurityException("Lo sentimos, no se ha entendido esa orden.");
        }
        
        //Solicitud aprobada - Ubicar las invocaciones en cada uno de los casos
        //new GestionBDReal().Peticion(arg);
    }    
}
