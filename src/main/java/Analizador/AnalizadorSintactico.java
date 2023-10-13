/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 
 */
package Analizador;
import Tokens.Simbolos;
import Tokens.TablaSintactica;
import Utilidades.Calculadora;
import java.util.ArrayList;
/**
 *
 * @author branp
 */
public class AnalizadorSintactico {

    private ReportesSintacticos Reportes = new ReportesSintacticos();
    private Calculadora calc = new Calculadora();
    private int bloque = 1;
    private int posicion = 0;
    private ArrayList<Simbolos> Tokens = recopiladorLexico();
    public AnalizadorSintactico() {
    }
    public void bloques(){
        boolean AumentarBloque = false;
        while (posicion < Tokens.size()) {
            Simbolos elemento = Tokens.get(posicion);
            int Fila = elemento.getFila();
            if(elemento.getInicio()==0){
                ArrayList<TablaSintactica> ListaDeReportes = Reportes.getreporteRecopilado();
                for (TablaSintactica objeto : ListaDeReportes) {
                    if (objeto.getbloque() == bloque) {
                        ListaDeReportes.add(objeto);
                    }
                //condición si el bloque tiene contenido adentro aqui se hace la distinción para saber que hay que hacer según su calsificación
                } 
                //Es nuevo:
                if(ListaDeReportes.isEmpty()){
                    analisisSintactico();
                //si ya existen elementos en ese bloque comprueba si un nuevo elemento pertenece y si no crea un nuevo bloque
                }else{
                    //comprobar si es if
                    if(ListaDeReportes.get(0).getSimbolo().equals("if")){
                        if(elemento.getLexema().equals("elif")||elemento.getLexema().equals("else")){
                            AumentarBloque = false;
                        }else{
                            AumentarBloque = true;
                        }   
                    }else if(ListaDeReportes.get(0).getSimbolo().equals("for")){
                        if(elemento.getLexema().equals("else")){
                            AumentarBloque = false;
                        }else{
                            AumentarBloque = true;
                        }   
                    }else{
                        AumentarBloque = true;
                    }
                    //if encargado de cerrar el bloque 
                    if(AumentarBloque){
                        bloque++;
                    }
                    analisisSintactico();
                }
                //aqui va el comportamiento si es contenido de un bloque:
            }else{
                analisisSintactico();
            }
        }
    }

    public void analisisSintactico(){
            Simbolos elemento = Tokens.get(posicion);
            int Fila = elemento.getFila();
            String Simbolo = elemento.getLexema();
            //condición si el elemento es ID: Declaración	|ID ASIGNACION EXPRESION | ID ARIMETICO/ASIGNACION EXPRESION
            if(elemento.getTipoToken().equals("ID")){    
                posicion++;
                elemento = Tokens.get(posicion);
                //Si es asignación de una
                if(elemento.getPatron().equals("Asignación")){
                    posicion++;
                    String Cadena = "";
                    elemento = Tokens.get(posicion);
                    //If donde se define el comportamiento de la expresión
                    //EXPRESION ->	|Constante(Operador Y Constante)*;
                    if(elemento.getTipoToken().equals("Constantes")){
                        boolean esCadena = false;
                        boolean esOtro = false;
                        boolean huboError = false;
                        boolean seCerroLaCadena = false;
                        boolean esTernario = false;
                        String Tipo = elemento.getPatron();
                        while (posicion < Tokens.size() && Fila == elemento.getFila()) { 
                            if(elemento.getPatron().equals("Cadena")){
                                esCadena = true;
                            }else{
                                esOtro = true;
                            }
                            if(esTernario){
                                Cadena += " "+elemento.getLexema();                             
                                //se entra a esta condición si se combinan tipos cadena con cualquier otro, lo cual haría un error de sintaxis
                            }else if((esCadena && esOtro)||(!esCadena && !esOtro)){
                                    Reportes.reporteErrorAsignacion(1,elemento.getFila() , elemento.getColumna(),bloque,"Asignación");
                                    huboError = true;
                                    //bucle para seguir en la siguiente linea
                                    CambiarDeLinea(Fila);
                                    break;
                            }else{
                                if(elemento.getLexema().equals("**")){
                                    Cadena += " ^";
                                }else{
                                    Cadena += " "+elemento.getLexema();
                                }
                                if(elemento.getTipoToken().equals("Aritméticos")){
                                    seCerroLaCadena = false;
                                }else if(elemento.getTipoToken().equals("Constantes")){
                                    seCerroLaCadena = true;
                                }else if(elemento.getTipoToken().equals("Palabras clave")||elemento.getTipoToken().equals("Comparación")){
                                    esTernario = true;
                                    seCerroLaCadena = false;
                                }else{
                                    if(!esTernario){
                                        Reportes.reporteErrorAsignacion(2,elemento.getFila() , elemento.getColumna(),bloque,"Asignación");
                                        huboError = true;
                                    //bucle para seguir en la siguiente linea
                                        CambiarDeLinea(Fila);
                                        break;
                                    }else{

                                    }
                                }
                            }
                            posicion++;
                            elemento = Tokens.get(posicion);
                        }
                        //condicion final, si hubo un error anterior no hace nada, si es error de tipo 3 y si no crea el reporte
                        if(huboError){

                        }else if(seCerroLaCadena){
                            if(esTernario){
                                Reportes.reporteAsignacion(Simbolo,Tipo, "Undefined", elemento.getFila(),elemento.getColumna(),bloque,"Ternario");
                            }else if(esCadena){
                                Reportes.reporteAsignacion(Simbolo,Tipo, Cadena, elemento.getFila(),elemento.getColumna(),bloque,"Asignación");
                            }else{
                                Reportes.reporteAsignacion(Simbolo,Tipo, Double.toString(calc.evaluarExpresion(Cadena)), elemento.getFila(),elemento.getColumna(),bloque,"Asignación");
                            }
                       
                        }else{
                            Reportes.reporteErrorAsignacion(3,elemento.getFila(),elemento.getColumna(),bloque,"Asignación");
                        }
                        //| ID realizar esto si sobre tiempo
                    }else if(false){

                        //|Otros(ID|Constantes|Otros)* 
                    }else if(elemento.getTipoToken().equals("Otros")){
                        String Abrio = elemento.getPatron();
                        boolean cerro = false;
                        while (posicion < Tokens.size() && Fila == elemento.getFila()) {
                            Cadena += elemento.getLexema();
                            posicion++;
                            elemento = Tokens.get(posicion);
                            if(elemento.getPatron().equals(Abrio)){
                                cerro = true;
                            }
                        }
                        if(cerro){
                            elemento = Tokens.get(posicion-1);
                            Reportes.reporteAsignacion(Simbolo,"Arreglo", "Undefined", elemento.getFila(),elemento.getColumna(),bloque,"Asignación");
                        }else{
                            Reportes.reporteErrorAsignacion(4,elemento.getFila(),elemento.getColumna(),bloque,"Asignación");
                        }
                        //Error con el formato de la asignación
                    }else{
                        Reportes.reporteErrorAsignacion(5,elemento.getFila(),elemento.getColumna(),bloque,"Asignación");
                        CambiarDeLinea(Fila);
                    }
                //si tiene un token arimetico antes:
                }else if(elemento.getLexema().equals("Aritméticos")){
                    posicion++;
                    String Cadena = "";
                    elemento = Tokens.get(posicion);
                    if(elemento.getTipoToken().equals("Asignación")){
                        //bucle para seguir en la siguiente linea
                        while (posicion < Tokens.size() && Fila == elemento.getFila()&&!(elemento.getTipoToken().equals("Comentario"))) {   
                            posicion++;
                            elemento = Tokens.get(posicion);
                        }
                        elemento = Tokens.get(posicion-1);
                        Reportes.reporteAsignacion(Simbolo,"Asignación", "Undefined", elemento.getFila(),elemento.getColumna(),bloque,"Asignación");
                    }else{
                        Reportes.reporteErrorAsignacion(6,elemento.getFila(),elemento.getColumna(),bloque,"Asignación");
                        CambiarDeLinea(Fila);
                    }
                    //Condición para ver las invocaciones de metodos
                }else if(elemento.getLexema().equals("(")){
                    boolean cerro = false;
                    int PosCerrado = 0;
                        //bucle para seguir en la siguiente linea
                        while (posicion < Tokens.size() && Fila == elemento.getFila()) {   
                            posicion++;
                            elemento = Tokens.get(posicion);
                            if(elemento.getLexema().equals(")")){
                                cerro = true;
                                PosCerrado = posicion;
                            }
                        }
                        elemento = Tokens.get(PosCerrado);
                        if(cerro){
                            Reportes.reporteFuncion(Simbolo,"Funciones", "Undefined", elemento.getFila(),elemento.getColumna(),bloque,"Invocaciones");
                        }else{
                            Reportes.reporteErrorFuncion(1,elemento.getFila(),elemento.getColumna(),bloque,"Funciones");
                        }   
                    //condición para invocaciones multiples:
                }else if(elemento.getLexema().equals(",")){
                    boolean coma = false;
                    while (posicion < Tokens.size() && Fila == elemento.getFila()) {
                            posicion++;
                            elemento = Tokens.get(posicion);
                            if(elemento.getLexema().equals(",")){
                                coma = false;
                            }else{
                                coma = true;
                            }
                    }
                    elemento = Tokens.get(posicion-1);
                    if(coma){
                        Reportes.reporteAsignacion(Simbolo,"Asignación", "Undefined", elemento.getFila(),elemento.getColumna(),bloque,"Asignación");
                    }
                    //si no cumple con ninguno de los requisitos genera error sintactico:
                }else{
                    Reportes.reporteErrorAsignacion(7,elemento.getFila(),elemento.getColumna(),bloque,"Asignación");
                }
                //para Bloques IF y elif
            }else if(elemento.getTipoToken().equals("Palabras clave")){
                String palabra = elemento.getLexema();
                //Switch para el manejo de palabras claves:
                switch (palabra) {
                    //para bloques IF y elif
                    case "if":
                    case "elif":
                        String Tipo = "boolean";
                        posicion++;
                        elemento = Tokens.get(posicion);
                        //EXPRESION ->	| boolean
                        if(elemento.getPatron().equals("booleanas")){
                            String Valor = elemento.getLexema();
                            posicion++;
                            elemento = Tokens.get(posicion);
                            if(elemento.getLexema().equals(":")){
                                posicion++;
                                elemento = Tokens.get(posicion);
                                if(Fila == elemento.getFila()){
                                    //si existe un elemento en la misma linea ve si es un comentario si lo es no pasa nada
                                    if(elemento.getTipoToken().equals("Comentario")){
                                        posicion++;
                                    }else{
                                        Reportes.reporteErrorCondicional(1,elemento.getFila(),elemento.getColumna(),bloque,"Condicional");
                                        CambiarDeLinea(Fila);
                                    }
                                }else{
                                    Reportes.reporteCondicional(Simbolo,Tipo, Valor, elemento.getFila(),elemento.getColumna(),bloque,"Condicional");
                                }
                            }else{
                                //error si no tiene : acabando la condicional
                                Reportes.reporteErrorCondicional(2,elemento.getFila(),elemento.getColumna(),bloque,"Condicional");
                                CambiarDeLinea(Fila);
                            }
                        //| (Entero|Double|ID) Comparación (Entero|Double|ID) || ID
                        }else if(elemento.getTipoToken().equals("ID")||elemento.getPatron().equals("Constantes")){
                            boolean HayId = false;
                            //condición para saber si entro con un id
                            if(elemento.getTipoToken().equals("ID")){
                                HayId = true;
                            }
                            posicion++;
                            elemento = Tokens.get(posicion);
                            //condición para saber si es por medio de comparación el boolean
                            if(elemento.getPatron().equals("Comparación")||elemento.getTipoToken().equals("Palabras clave")){
                                posicion++;
                                elemento = Tokens.get(posicion);
                                //aqui se recibe el siguiente valor ya sea constante o ID
                                if(elemento.getPatron().equals("ID")||elemento.getPatron().equals("Constantes")){
                                    posicion++;
                                    elemento = Tokens.get(posicion);
                                    if(elemento.getPatron().equals("ID")){
                                            HayId = true;
                                    }
                                    //se verifica que se cierre la sentencia con :
                                    if(elemento.getLexema().equals(":")){
                                        if(HayId){
                                                Reportes.reporteCondicional(Simbolo,Tipo,"Undefined", elemento.getFila(),elemento.getColumna(),bloque,"Condicional");
                                            }else{
                                                Reportes.reporteCondicional(Simbolo,Tipo,"Undefined", elemento.getFila(),elemento.getColumna(),bloque,"Condicional");
                                            } 
                                        posicion++;
                                        elemento = Tokens.get(posicion);
                                        //se verifica que el siguiente elemento no esté en la misma linea
                                        if(Fila == elemento.getFila()){
                                            //si existe un elemento en la misma linea ve si es un comentario si lo es no pasa nada
                                            if(elemento.getTipoToken().equals("Comentario")){
                                                posicion++;
                                            }else{
                                                //si el elemento no era un comentario tira un error
                                                Reportes.reporteErrorCondicional(1,elemento.getFila(),elemento.getColumna(),bloque,"Condicional");
                                                CambiarDeLinea(Fila);
                                            }   
                                        }
                                        //error si no tiene : acabando la condicional
                                    }else{
                                        Reportes.reporteErrorCondicional(2,elemento.getFila(),elemento.getColumna(),bloque,"Condicional");
                                        CambiarDeLinea(Fila);
                                    }
                                    //error si recibe un elemento que no sea Id o constante
                                }else{
                                    Reportes.reporteErrorCondicional(3,elemento.getFila(),elemento.getColumna(),bloque,"Condicional");
                                    CambiarDeLinea(Fila);
                                }
                                // si no recibe un signo de comparación prueba con la otra forma que sería recibir : si no lo recibe tira error
                            }else if(elemento.getLexema().equals(":")){
                                Reportes.reporteCondicional(Simbolo,Tipo,"Undefined", elemento.getFila(),elemento.getColumna(),bloque,"Condicional");
                                CambiarDeLinea(Fila);
                            }else{
                                Reportes.reporteErrorCondicional(4,elemento.getFila(),elemento.getColumna(),bloque,"Condicional");
                                CambiarDeLinea(Fila);
                            }
                        }
                        break;
                    case "def":
                    //Métodos	|PALABRA_CLAVE ID OTROS ((ID|Constantes)(Otros)?)* OTROS OTROS def 4325(1): 
                        boolean EsComa = false;
                        posicion++;
                        elemento = Tokens.get(posicion);
                        if(elemento.getTipoToken().equals("ID")){
                            posicion++;
                            elemento = Tokens.get(posicion);
                            if(elemento.getLexema().equals("(")){
                                while (posicion < Tokens.size() && Fila == Tokens.get(posicion).getFila()) {
                                    if(elemento.getTipoToken().equals("ID")||elemento.getTipoToken().equals("Constantes")){
                                        EsComa = false;
                                    }else if(elemento.getLexema().equals(",")){
                                        EsComa = true;
                                    }else if(elemento.getTipoToken().equals("Aritméticos")){
                                        EsComa = true;
                                    }else if(elemento.getLexema().equals(":")||elemento.getLexema().equals(")")){

                                    }else{
                                        //si viene algún tipo de token que no se esperaba:
                                         Reportes.reporteErrorFuncion(4,elemento.getFila(),elemento.getColumna(),bloque,"Funciones");
                                        //bucle para seguir en la siguiente linea
                                        CambiarDeLinea(Fila);
                                    }   
                                    posicion++;
                                }
                                if(EsComa){
                                        //No se armo el parametro como se esperaba:
                                         Reportes.reporteErrorFuncion(5,elemento.getFila(),elemento.getColumna(),bloque,"Funciones");
                                }else{
                                    Reportes.reporteFuncion(Simbolo,"Funciones", "Undefined", elemento.getFila(),elemento.getColumna(),bloque,"Funciones");
                                }
                                posicion++;
                                elemento = Tokens.get(posicion);
                            }else{
                                //error si no viene un parentesis lo cual define la función:
                                Reportes.reporteErrorFuncion(3,elemento.getFila(),elemento.getColumna(),bloque,"Funciones");
                                //bucle para seguir en la siguiente linea
                                CambiarDeLinea(Fila);
                            }
                        }else{
                            Reportes.reporteErrorFuncion(2,elemento.getFila(),elemento.getColumna(),bloque,"Funciones");
                            //bucle para seguir en la siguiente linea
                            CambiarDeLinea(Fila);
                        }
                        break;
                        //para Bloques For
                    case "for":
                    //For ->	| For Expresión                       
                        //Expresion - >	| ID in (ID|Range ((ID|Constante)(Otros)?)*)OTROS
                        posicion++;
                        elemento = Tokens.get(posicion);
                            if(elemento.getTipoToken().equals("ID")){
                                posicion++;
                                elemento = Tokens.get(posicion);
                                if(elemento.getLexema().equals("in")){
                                    posicion++;
                                    elemento = Tokens.get(posicion);
                                    if(elemento.getLexema().equals("range")){
                                        boolean cerroFor = false;
                                        boolean errorParametro = false;
                                        boolean DoblePuntos = false;
                                        //aqui va el funcionamiento si se usa metodo range
                                        while (posicion < Tokens.size() && Fila == Tokens.get(posicion).getFila()) {
                                            posicion++;
                                            elemento = Tokens.get(posicion);
                                            if(elemento.getLexema().equals(",")){
                                                errorParametro = true;
                                            }else if(elemento.getTipoToken().equals("ID")||elemento.getTipoToken().equals("Constantes")){
                                                errorParametro = false;
                                            }else if(elemento.getLexema().equals(")")){
                                                cerroFor = true;
                                                break;
                                            }else if(elemento.getLexema().equals(":")){
                                                DoblePuntos = true;
                                                break;
                                            }
                                        }
                                        //ciclos que validan q no haya ocurrido ningún error
                                        if(errorParametro){
                                            if(cerroFor){
                                                if(DoblePuntos){
                                                    Reportes.reporteCiclo(Simbolo,"Ciclos", "Undefined", elemento.getFila(),elemento.getColumna(),bloque,"For");
                                                }else{
                                                    //entra aqui si no viene :
                                                Reportes.reporteErrorCiclos(4,elemento.getFila(),elemento.getColumna(),bloque,"For");
                                                CambiarDeLinea(Fila);
                                                }
                                            }else{  
                                                //entra aqui si no se cierra )
                                                Reportes.reporteErrorCiclos(6,elemento.getFila(),elemento.getColumna(),bloque,"For");
                                                CambiarDeLinea(Fila);
                                            }
                                        }else{
                                            //entra aqui si no se genero correctamente el parametro
                                            Reportes.reporteErrorCiclos(5,elemento.getFila(),elemento.getColumna(),bloque,"For");
                                        }
                                    }else if(elemento.getTipoToken().equals("ID")){
                                    //aqui va el funcionamiento si es sobre otro ID
                                        posicion++;
                                        elemento = Tokens.get(posicion);
                                        if(elemento.getLexema().equals(":")){
                                            Reportes.reporteCiclo(Simbolo,"Ciclos", "Undefined", elemento.getFila(),elemento.getColumna(),bloque,"For");
                                            CambiarDeLinea(Fila);
                                        }else{
                                            //entra aqui si no se cierra la expresión correctamente
                                            Reportes.reporteErrorCiclos(4,elemento.getFila(),elemento.getColumna(),bloque,"For");
                                            //bucle para seguir en la siguiente linea
                                            CambiarDeLinea(Fila);
                                        }
                                    }else{
                                        //entra aqui si no recibe el Id o range esperado
                                        Reportes.reporteErrorCiclos(3,elemento.getFila(),elemento.getColumna(),bloque,"For");
                                        //bucle para seguir en la siguiente linea
                                        CambiarDeLinea(Fila);
                                    }
                                }else{
                                    //entra aqui si no recibe el in esperado
                                    Reportes.reporteErrorCiclos(2,elemento.getFila(),elemento.getColumna(),bloque,"For");
                                    //bucle para seguir en la siguiente linea
                                    CambiarDeLinea(Fila);
                                }
                            }else{
                            //error si no recibe un ID en el cual guardar ciclo for
                            Reportes.reporteErrorCiclos(1,elemento.getFila(),elemento.getColumna(),bloque,"For");
                            //bucle para seguir en la siguiente linea
                            CambiarDeLinea(Fila);
                            }
                        break;
                    case "while":
                        //Expresión ->	| (ID|Constante) Condicional (ID|Constante) OTROS
                        posicion++;
                        elemento = Tokens.get(posicion);
                        //condicional al que entra si es un ID O Constante
                            if(elemento.getTipoToken().equals("ID")||elemento.getTipoToken().equals("Constantes")){
                                posicion++;
                                elemento = Tokens.get(posicion);
                                if(elemento.getTipoToken().equals("Comparación")){
                                    posicion++;
                                    elemento = Tokens.get(posicion);
                                    if(elemento.getTipoToken().equals("ID")||elemento.getPatron().equals("Entero")||elemento.getPatron().equals("Decimal")){
                                        posicion++;
                                        elemento = Tokens.get(posicion);
                                        if(elemento.getLexema().equals(":")){
                                            Reportes.reporteCiclo(Simbolo,"Ciclos", "Undefined", elemento.getFila(),elemento.getColumna(),bloque,"While");
                                            posicion++;
                                        }else{
                                            //error si no recibe un :
                                            Reportes.reporteErrorCiclos(4,elemento.getFila(),elemento.getColumna(),bloque,"While");
                                            //bucle para seguir en la siguiente linea
                                            CambiarDeLinea(Fila);
                                        }
                                    }else{
                                        //error si no recibe un id o constante
                                        Reportes.reporteErrorCiclos(7,elemento.getFila(),elemento.getColumna(),bloque,"While");
                                        //bucle para seguir en la siguiente linea
                                        CambiarDeLinea(Fila);
                                    }
                                }else{
                                    //error si no recibe un id o constante
                                    Reportes.reporteErrorCiclos(8,elemento.getFila(),elemento.getColumna(),bloque,"While");
                                    //bucle para seguir en la siguiente linea
                                    CambiarDeLinea(Fila);
                                }
                            }else if(elemento.getPatron().equals("booleanas")){
                                posicion++;
                                elemento = Tokens.get(posicion);
                                    if(elemento.getLexema().equals(":")){
                                        Reportes.reporteCiclo(Simbolo,"Ciclos", "Undefined", elemento.getFila(),elemento.getColumna(),bloque,"While");
                                        posicion++;
                                    }else{
                                        //error si no recibe :
                                        Reportes.reporteErrorCiclos(4,elemento.getFila(),elemento.getColumna(),bloque,"While");
                                        //bucle para seguir en la siguiente linea
                                        CambiarDeLinea(Fila);
                                    }
                            }else{
                                //error si no recibe un id o constante
                                Reportes.reporteErrorCiclos(7,elemento.getFila(),elemento.getColumna(),bloque,"While");
                                //bucle para seguir en la siguiente linea
                                CambiarDeLinea(Fila);
                            }
                        break;
                    case "else":
                            posicion++;
                                elemento = Tokens.get(posicion);
                                    if(elemento.getLexema().equals(":")){
                                        Reportes.reporteCiclo("else","else", "Undefined", elemento.getFila(),elemento.getColumna(),bloque,"else");
                                        posicion++;
                                    }else{
                                        //error si no recibe :
                                        Reportes.reporteErrorCiclos(4,elemento.getFila(),elemento.getColumna(),bloque,"else");
                                        //bucle para seguir en la siguiente linea
                                        CambiarDeLinea(Fila);
                                    }
                        break;
                    case "return":
                        CambiarDeLinea(Fila);
                        Reportes.reporteCiclo("return","Palabra reservada", "Undefined", elemento.getFila(),elemento.getColumna(),bloque,"return");
                        break;    
                    default:
                        // Código por defecto si la opción no coincide con ningún caso
                        System.out.println("Opción no válida");
                        break;
                }
            }else{
                Reportes.reporteErrorGeneral(4,elemento.getFila(),elemento.getColumna(),bloque,"General");
            }
    }

    public ArrayList<Simbolos> recopiladorLexico(){
        AnalizadorLexico Reco = new AnalizadorLexico();
        return Reco.gettokenRecopilado();
    }

    public void CambiarDeLinea(int Linea){
        while (posicion < Tokens.size() && Linea == Tokens.get(posicion).getFila()) {   
            posicion++;
        }
    }

    public void ResetBloques(){
        bloque = 1;
    }


}