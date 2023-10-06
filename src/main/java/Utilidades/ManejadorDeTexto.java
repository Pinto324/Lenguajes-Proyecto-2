
package Utilidades;

public class ManejadorDeTexto {
    private String Acccion;
    private String TextoToken;
    /**
     *
     * Constructor
     */
    public ManejadorDeTexto(String textoToken, String accion) {
        Acccion=accion;
        TextoToken = textoToken;        
    }
    /**
     *
     * Gets y sets 
     */
    public String getAcccion() {
        return Acccion;
    }

    public void setAcccion(String Acccion) {
        this.Acccion = Acccion;
    }

    public String getTextoToken() {
        return TextoToken;
    }

    public void setTextoToken(String TextoToken) {
        this.TextoToken = TextoToken;
    }
}
