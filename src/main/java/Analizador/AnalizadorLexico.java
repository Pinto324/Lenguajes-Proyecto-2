/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizador;

/**
 *
 * @author branp
 */
public class AnalizadorLexico {
    private int fila = 1;
    private int columna = 0;
    private int posicion = 0;
    int matrizTransicion[][] = new int[9][2];
    { 
        /*
        [valor de Token][Aceptacion]
        Para Identificador -> 1
        para Entero -> 2
        Para Decimal -> 3
        and asd 9asd 7>7 43.
        */
        matrizTransicion[0][0] = 1; matrizTransicion[1][0] = 2; matrizTransicion[2][0] = 3;
        matrizTransicion[0][1] = -1; matrizTransicion[1][1] = -2; matrizTransicion[2][1] = -3;
    }
    public AnalizadorLexico() {
    }
    
    public void analisisLexico(String texto){
        char temporal;
        while (posicion < texto.length()) {
            temporal = texto.charAt(posicion);
            System.out.println(temporal);
            if (temporal == '\n') {
                System.out.println("Es un salto de línea");
            }else if (temporal == ' ') {
                System.out.println("Es un espacio en blanco");
            }
                posicion++;
        }
    }
    
    
}
