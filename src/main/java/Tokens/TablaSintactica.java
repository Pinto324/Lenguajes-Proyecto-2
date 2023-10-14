/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tokens;

import java.util.ArrayList;

/**
 *
 * @author branp
 */
public class TablaSintactica {    
    private String Simbolo;
    private String Tipo;
    private String Valor;
    private String Descripcion;
    private int fila;
    private int columna;
    private int bloque;
    private String nivel;

         /**
    *
    * Constructor de simbolos
     * @param Simbolo
     * @param Tipo
     * @param Valor
     * @param Descripcion
     * @param fila
     * @param bloque
     * @param columna
     * @param nivel
    */
    public TablaSintactica(String Simbolo, String Tipo ,String Valor, String Descripcion, int fila, int columna, int bloque, String nivel) {
        this.Simbolo = Simbolo;
        this.Tipo = Tipo;
        this.Valor = Valor;
        this.Descripcion = Descripcion;
        this.fila = fila;
        this.columna = columna;
        this.bloque = bloque;
        this.nivel = nivel;
    }
    //get y sets
    public String getSimbolo() {
        return Simbolo;
    }
    
    public void setSimbolo(String Simbolo) {
        this.Simbolo = Simbolo;
    }

    public String getTipo() {
        return Tipo;
    }
    
    public void setTipo(String Tipo) {
        this.Tipo = Tipo;
    }

    public String getValor() {
        return Valor;
    }
    
    public void setValor(String Valor) {
        this.Valor = Valor;
    }

    public String getDescripcion() {
        return Descripcion;
    }
    
    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }    
    
    public int getfila() {
        return fila;
    }
    
    public void setfila(int fila) {
        this.fila = fila;
    }

    public int getcolumna() {
        return columna;
    }
    
    public void setcolumna(int columna) {
        this.fila = columna;
    }

    public int getbloque() {
        return bloque;
    }
    
    public void setbloque(int bloque) {
        this.bloque = bloque;
    }
    public String getnivel() {
        return nivel;
    }
    
    public void setnivel(String nivel) {
        this.nivel = nivel;
    }
}