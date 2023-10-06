/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import InterfazGrafica.MenuInicial;
import InterfazGrafica.Principal;

/**
 *
 * @author branp
 */
public class Main {
        /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Desde acá ejecutamos la parte gráfica
       MenuInicial ventana = new MenuInicial();
       ventana.setVisible(true);
    }
}
