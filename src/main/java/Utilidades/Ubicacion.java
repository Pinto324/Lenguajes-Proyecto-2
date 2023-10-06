
package Utilidades;

public class Ubicacion {
    private final int Columna;
    private final int Fila;
    
    public Ubicacion(int Columna, int Fila) {
        this.Columna = Columna;
        this.Fila = Fila;
    }

    public int getCol() {
        return this.Columna;
    }

    public int getRow() {
        return this.Fila;
    }

    @Override
    public String toString() {
        return "Col: " + this.Columna + ", Fila: " + this.Fila;
    }
}
