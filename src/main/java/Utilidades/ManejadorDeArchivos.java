/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilidades;

import InterfazGrafica.MenuInicial;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author branp
 */
public class ManejadorDeArchivos {
    
    private MenuInicial MenuNuevo;
    public static File archivoAProcesar;
    FileOutputStream salida;
    
    public ManejadorDeArchivos(File archivoAProcesar, MenuInicial Menu){
        this.archivoAProcesar = archivoAProcesar;
        this.MenuNuevo = Menu;
    }
    
    private void CargarArchivo() throws FileNotFoundException, IOException, ArrayIndexOutOfBoundsException{
        BufferedReader lector = new BufferedReader(new FileReader(this.archivoAProcesar));
        String auxiliar = lector.readLine();
        String temporal="";
        while(auxiliar != null){
            temporal=temporal+auxiliar;
            if(temporal!=""){
               MenuNuevo.getAreaDeTexto().append(temporal);
               temporal="";
            }
            auxiliar = lector.readLine();
            if(auxiliar!=null){
               MenuNuevo.getAreaDeTexto().append("\n");
               MenuNuevo.getAreaDeTexto().append("\n"); 
            }        
        }       
        JOptionPane.showMessageDialog(null, "Datos cargados Exitosamente.");
        lector.close();
    }   
    
      public void BuscarError(){
        try {
            CargarArchivo();
        } catch (FileNotFoundException e) {
            System.err.println(e);
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            System.err.println(e); 
        }
    }
      
    public String GuardarArchivo(File archivo, String texto) throws IOException{
        String Contenido=null;
        salida= new FileOutputStream(archivo);
        byte[] bytxt=texto.getBytes();
        salida.write(bytxt);
        Contenido="Archivo Guardado Exitosamente";
        return Contenido;
    }

    public void GuardarArchivoExistente(String texto, File archivo) throws IOException{
        try (FileWriter fw = new FileWriter(archivo)) {
            fw.write(texto);
        }
    }
}
