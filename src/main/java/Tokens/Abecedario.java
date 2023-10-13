package Tokens;

import java.util.ArrayList;

/**
 *
 * @author branp
 */
public class Abecedario {    
    private ArrayList<Character> Identificadores;
    private ArrayList<Character> Enteros;

    /* Metodos Get & Set de los distintos Tipos de abecedarios
     * @return 
    */
    public ArrayList<Character> getIdentificadores() {
        return Identificadores;
    }

    public void setIdentificadores(ArrayList<Character> signosAgrupacion) {
        this.Identificadores = signosAgrupacion;
    }

    private void LlenarIdentificadores() {
        Identificadores = new ArrayList<>();
        this.Logicos.add("a");
        this.Logicos.add("b");
        this.Logicos.add("c");
        this.Logicos.add("A");
        this.Logicos.add("B");
        this.Logicos.add("C");
    }
}
