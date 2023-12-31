/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 
 */
package Analizador;
import Tokens.TablaSintactica;
import java.util.ArrayList;
/**
 *
 * @author branp
 */
public class ReportesSintacticos {

    private static ArrayList<TablaSintactica> reporteRecopilado = new ArrayList<>();
    private static ArrayList<TablaSintactica> errorRecopilado = new ArrayList<>();
    public ReportesSintacticos() {
    }

    public ArrayList<TablaSintactica> getreporteRecopilado() {
        return reporteRecopilado;
    }
    public ArrayList<TablaSintactica> getErrorRecopilado() {
        return errorRecopilado;
    }
    
    public void ResetArreglos(){
        reporteRecopilado = new ArrayList<>();
        errorRecopilado = new ArrayList<>();
    }
    ////////////////////////////////////////////////////////
    //ASIGNACIÓN
    //Metodos para reportes de error en asignación:
    public void reporteErrorAsignacion(int caso, int fila, int columna, int bloque, String nivel){
        TablaSintactica token;
        switch(caso){
            //asignación de enteros
            case 1:
                token = new TablaSintactica("","declaracion","","No se puede operar una cadena con otro valor",fila,columna,bloque,nivel);
                errorRecopilado.add(token);
                break;
            case 2:
                 token = new TablaSintactica("","declaracion","","Se recibió un token invalido al construir la operación",fila,columna,bloque,nivel);
                errorRecopilado.add(token);
                break;
            case 3:
                token = new TablaSintactica("","declaracion","","No se cerro la cadena correctamente",fila,columna,bloque,nivel);
                errorRecopilado.add(token);
                break;
            //asignación de otros
            case 4:
                token = new TablaSintactica("","declaracion","","No se cerro el arreglo",fila,columna,bloque,nivel);
                errorRecopilado.add(token);
                break;
                //Error en el formato más general
            case 5:
                token = new TablaSintactica("","declaracion","","hubo un error en el formato:",fila,columna,bloque,nivel);
                errorRecopilado.add(token);
                break;
            case 6:
                token = new TablaSintactica("","declaracion","","Se esperaba un signo =",fila,columna,bloque,nivel);
                errorRecopilado.add(token);
                break;
            case 7:
                token = new TablaSintactica("","declaracion","","Ocurrió un error con el formato al intentar la declaracion",fila,columna,bloque,nivel);
                errorRecopilado.add(token);
                break;
            default:
                System.out.println("wtf xd");
                break;
        }
    }
        //Metodos para reportes de easignación:
    public void reporteAsignacion(String Simbolo, String tipo, String valor, int fila, int columna,int bloque, String nivel){
            TablaSintactica token = new TablaSintactica(Simbolo,tipo,valor,"",fila,columna,bloque,nivel);
            reporteRecopilado.add(token);
    }
    //////////////////////////////////////////
    //Función
    //Metodos para reportes de error en función:
    public void reporteErrorFuncion(int caso, int fila, int columna, int bloque, String nivel){
        TablaSintactica token;
        switch(caso){
            //asignación de enteros
            case 1:
                token = new TablaSintactica("","Funcion","","No se cerró la función",fila,columna,bloque,nivel);
                errorRecopilado.add(token);
                break;
            case 2:
                token = new TablaSintactica("","Funcion","","Se esperaba un ID valido para el nombre de la función",fila,columna,bloque,nivel);
                errorRecopilado.add(token);
                break;    
            case 3:
                token = new TablaSintactica("","Funcion","","Se esperaba un ( para definir la función",fila,columna,bloque,nivel);
                errorRecopilado.add(token);
                break;
            case 4:
                token = new TablaSintactica("","Funcion","","Un token invalido en la declaración",fila,columna,bloque,nivel);
                errorRecopilado.add(token);
                break;
            case 5:
                token = new TablaSintactica("","Funcion","","Ocurrio un error en la sintaxis del parametro, revisar",fila,columna,bloque,nivel);
                errorRecopilado.add(token);
                break;    
            default:
                System.out.println("wtf xd");
                break;
        }
    }
        //Metodos para reportes de Funcion:
    public void reporteFuncion(String Simbolo, String tipo, String valor, int fila, int columna,int bloque, String nivel){
            TablaSintactica token = new TablaSintactica(Simbolo,tipo,valor,"",fila,columna,bloque,nivel);
            reporteRecopilado.add(token);
    }
    //////////////////////////////////////////
    //metodos para condicionales
    //Metodos para reportes de error en condicionales:
    public void reporteErrorCondicional(int caso, int fila, int columna, int bloque, String nivel){
        TablaSintactica token;
        switch(caso){
            //asignación de enteros
            case 1:
                token = new TablaSintactica("","Condicional","","No se esperaba algo después del :",fila,columna,bloque,nivel);
                errorRecopilado.add(token);
                break;
            case 2:
                token = new TablaSintactica("","Condicional","","Se esperaba un :",fila,columna,bloque,nivel);
                errorRecopilado.add(token);
                break;
            case 3:
                token = new TablaSintactica("","Condicional","","Se esperaba una constante Valida",fila,columna,bloque,nivel);
                errorRecopilado.add(token);
                break;
            case 4:
                token = new TablaSintactica("","Condicional","","Se esperaba una condicional",fila,columna,bloque,nivel);
                errorRecopilado.add(token);
                break;
            case 5:
                token = new TablaSintactica("","Condicional","","No se soporte el formato del if ingresado",fila,columna,bloque,nivel);
                errorRecopilado.add(token);
                break;
            default:
                System.out.println("wtf xd");
                break;
        }
    }
            //Metodos para reportes de condicional:
    public void reporteCondicional(String Simbolo, String tipo, String valor, int fila, int columna,int bloque, String nivel){
            TablaSintactica token = new TablaSintactica(Simbolo,tipo,valor,"",fila,columna,bloque,nivel);
            reporteRecopilado.add(token);
    }
     //////////////////////////////////////////
    //metodos para Ciclos
    //Metodos para reportes de error en Ciclos:
    public void reporteErrorCiclos(int caso, int fila, int columna, int bloque, String nivel){
        TablaSintactica token;
        switch(caso){
            //asignación de enteros
            case 1:
                token = new TablaSintactica("","Ciclos","","Para iterar a través de una secuencia con un ciclo for, debes utilizar una variable válida",fila,columna,bloque,nivel);
                errorRecopilado.add(token);
                break;
            case 2:
                token = new TablaSintactica("","Ciclos","","Se esperaba un in",fila,columna,bloque,nivel);
                errorRecopilado.add(token);
                break;
            case 3:
                token = new TablaSintactica("","Ciclos","","Se esperaba un Id o Metodo valido",fila,columna,bloque,nivel);
                errorRecopilado.add(token);
                break;
            case 4:
                token = new TablaSintactica("","Ciclos","","Se esperaba un : ",fila,columna,bloque,nivel);
                errorRecopilado.add(token);
                break;
            case 5:
                token = new TablaSintactica("","Ciclos","","No se escribió correctamente el parametro",fila,columna,bloque,nivel);
                errorRecopilado.add(token);
                break;
            case 6:
                token = new TablaSintactica("","Ciclos","","Se esperaba un )",fila,columna,bloque,nivel);
                errorRecopilado.add(token);
                break; 
            case 7:
                token = new TablaSintactica("","Ciclos","","Se esperaba un Id o Constante",fila,columna,bloque,nivel);
                errorRecopilado.add(token);
                break;
            case 8:
                token = new TablaSintactica("","Ciclos","","Se esperaba una comparación",fila,columna,bloque,nivel);
                errorRecopilado.add(token);
                break;      
            default:
                System.out.println("wtf xd");
                break;
        }
    }
            //Metodos para reportes de reporteCiclo:
    public void reporteCiclo(String Simbolo, String tipo, String valor, int fila, int columna,int bloque, String nivel){
            TablaSintactica token = new TablaSintactica(Simbolo,tipo,valor,"",fila,columna,bloque,nivel);
            reporteRecopilado.add(token);
    }
                //Metodos para reportes de return:
    public void reporteErrorReturn(int caso, int fila, int columna, int bloque, String nivel){
        TablaSintactica token;
        switch(caso){
            case 1:
                token = new TablaSintactica("","Return","","No se recibió un token valido para el Return",fila,columna,bloque,nivel);
                errorRecopilado.add(token);
                break;
            case 2:
                token = new TablaSintactica("","Return","","No se recibió un token valido después del valor",fila,columna,bloque,nivel);
                errorRecopilado.add(token);
                break;
            case 3:
                token = new TablaSintactica("","Return","","en el parametro se recibió un token invalido",fila,columna,bloque,nivel);
                errorRecopilado.add(token);
                break;
            case 4:
                token = new TablaSintactica("","Return","","se recibió un token invalido en el retorno",fila,columna,bloque,nivel);
                errorRecopilado.add(token);
                break;
            case 5:
                token = new TablaSintactica("","Return","","Se esperaba un ) que cerrara el parametro",fila,columna,bloque,nivel);
                errorRecopilado.add(token);
                break;
            case 6:
                token = new TablaSintactica("","Return","","Se esperaba un id o constante más para el parametro",fila,columna,bloque,nivel);
                errorRecopilado.add(token);
                break;    
            default:
                break;
        }
    }
    
                //Metodos para reportes generales:
    public void reporteErrorGeneral(int caso, int fila, int columna, int bloque, String nivel){
            TablaSintactica token = new TablaSintactica("","General","","No se ingresó un token valido",fila,columna,bloque,nivel);
            errorRecopilado.add(token);
    }
}