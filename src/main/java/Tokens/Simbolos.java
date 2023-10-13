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
public class Simbolos {    
    private String Lexema;
    private String TipoDeToken;
    private String patron;
    private String descripcion;
    private ArrayList<Character> SignosAgrupacion;
    private ArrayList<Character> Aritmeticos;
    private ArrayList<Character> Comparacion;
    private ArrayList<String> Logicos;
    private final char Asignacion = '=';
    private ArrayList<String> PalabrasClaves;
    private ArrayList<Character> Constantes;    
    private final char Comentario = '#';
    private ArrayList<Character> Otros;
    private int fila;
    private int columna;
    private int inicio;

    public Simbolos() {
        this.agrupacionTokens();
    }
    /**
    *
    * Metodos Get & Set de los distintos Tipos de Tokens
     * @return 
    */
    public ArrayList<Character> getSignosAgrupacion() {
        return SignosAgrupacion;
    }

    public void setSignosAgrupacion(ArrayList<Character> signosAgrupacion) {
        this.SignosAgrupacion = signosAgrupacion;
    }
    public ArrayList<Character> getOpeAritmeticos() {
        return Aritmeticos;
    }

    public ArrayList<Character> getOperadoresMatematicos() {
        return Aritmeticos;
    }

    public void setOperadoresMatematicos(ArrayList<Character> operadoresMatematicos) {
        this.Aritmeticos = operadoresMatematicos;
    }

    public ArrayList<Character> getCaracteresEspeciales() {
        return Otros;
    }

    public void setCaracteresEspeciales(ArrayList<Character> caracteresEspeciales) {
        this.Otros = caracteresEspeciales;
    }

    public String getLexema() {
        return Lexema;
    }

    public String getTipoToken() {
        return TipoDeToken;
    }
    
    public void setTipoToken(String tipoToken) {
        this.TipoDeToken = tipoToken;
    }
    public int getFila() {
        return fila;
    }

   public int getColumna() {
        return columna;
    }
    public int getInicio() {
        return inicio;
    }
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
     public String getPatron() {
        return patron;
    }

    public void setPatron(String patron) {
        this.patron = patron;
    }
    
     /**
    *
    * Constructor de simbolos
    */
      public Simbolos(String tipoToken, String patron ,String lexema, int fila, int columna, int inicio) {
        this.TipoDeToken = tipoToken;
        this.patron = patron;
        this.Lexema = lexema;
        this.fila = fila;
        this.columna = columna;
        this.inicio = inicio;
    }
    
    private void agrupacionTokens() {
        tiposOperadoresArimeticos();
        OperadoresOtros();
        OperadoresComparacion();
        OperadoresLogicos();
        OperadoresPalabrasClave();
    }
     
     /**
    *
    * Metodos de Tokens
    */
    private void tiposOperadoresArimeticos() {
        Aritmeticos = new ArrayList<>();
        this.Aritmeticos.add('*');
        this.Aritmeticos.add('%');
        this.Aritmeticos.add('+');
        this.Aritmeticos.add('-');
        this.Aritmeticos.add('/');
    }
    private void OperadoresComparacion() {
        Comparacion = new ArrayList<>();
        this.Comparacion.add('=');
        this.Comparacion.add('!');
        this.Comparacion.add('>');
        this.Comparacion.add('<');
    }
    private void OperadoresLogicos() {
        Logicos = new ArrayList<>();
        this.Logicos.add("and");
        this.Logicos.add("or");
        this.Logicos.add("not");
    }
    private void OperadoresPalabrasClave() {
        PalabrasClaves = new ArrayList<>();
        PalabrasClaves.add("and");
        PalabrasClaves.add("as");
        PalabrasClaves.add("assert");
        PalabrasClaves.add("break");
        PalabrasClaves.add("class");
        PalabrasClaves.add("continue");
        PalabrasClaves.add("def");
        PalabrasClaves.add("del");
        PalabrasClaves.add("elif");
        PalabrasClaves.add("else");
        PalabrasClaves.add("except");
        PalabrasClaves.add("False");
        PalabrasClaves.add("finally");
        PalabrasClaves.add("for");
        PalabrasClaves.add("from");
        PalabrasClaves.add("global");
        PalabrasClaves.add("if");
        PalabrasClaves.add("import");
        PalabrasClaves.add("in");
        PalabrasClaves.add("is");
        PalabrasClaves.add("lambda");
        PalabrasClaves.add("None");
        PalabrasClaves.add("nonlocal");
        PalabrasClaves.add("not");
        PalabrasClaves.add("or");
        PalabrasClaves.add("pass");
        PalabrasClaves.add("raise");
        PalabrasClaves.add("return");
        PalabrasClaves.add("True");
        PalabrasClaves.add("try");
        PalabrasClaves.add("while");
        PalabrasClaves.add("with");
        PalabrasClaves.add("yield");
    }

    private void OperadoresOtros() {
        Otros = new ArrayList<>();
        this.Otros.add(')');
        this.Otros.add('(');
        this.Otros.add('{');
        this.Otros.add('}');
        this.Otros.add(']');
        this.Otros.add('[');
        this.Otros.add(':');
        this.Otros.add(';');
        this.Otros.add(',');
    }
   
}
