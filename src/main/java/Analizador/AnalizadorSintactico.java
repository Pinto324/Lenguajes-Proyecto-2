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
        int Fila;
        while (posicion < Tokens.size()) {
            Simbolos elemento = Tokens.get(posicion);
            Fila = elemento.getFila();
            if(elemento.getInicio()==0){
                ArrayList<TablaSintactica> ListaDeReportes = Reportes.getreporteRecopilado();
                ArrayList<TablaSintactica> ElementosEnElBloque = new ArrayList<>();
                for (TablaSintactica objeto : ListaDeReportes) {
                    if (objeto.getbloque() == bloque) {
                        ElementosEnElBloque.add(objeto);
                    }
                //condición si el bloque tiene contenido adentro aqui se hace la distinción para saber que hay que hacer según su calsificación
                } 
                //Es nuevo:
                if(ElementosEnElBloque.isEmpty()){
                    analisisSintactico(Fila);
                //si ya existen elementos en ese bloque comprueba si un nuevo elemento pertenece y si no crea un nuevo bloque
                }else{
                    //comprobar si es if
                    if(ElementosEnElBloque.get(0).getSimbolo().equals("if")){
                        if(elemento.getLexema().equals("elif")||elemento.getLexema().equals("else")){
                            AumentarBloque = false;
                        }else{
                            AumentarBloque = true;
                        }   
                    }else if(ElementosEnElBloque.get(0).getSimbolo().equals("for")){
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
                    analisisSintactico(Fila);
                }
                //aqui va el comportamiento si es contenido de un bloque:
            }else{
                analisisSintactico(Fila);
            }
        }
    }

    public void analisisSintactico(int Fila){
            Simbolos elemento = Tokens.get(posicion);
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
                        boolean ContieneID = false;
                        boolean Asignacion = false;
                        boolean ExpresionCorrecta = false;
                        boolean esCadena = false;
                        boolean esOtro = false;
                        boolean huboError = false;
                        boolean seCerroLaCadena = false;
                        boolean esTernario = false;
                        boolean multiplesValores = false;
                        String Tipo = elemento.getPatron();
                        String Cad = "";
                        if(elemento.getPatron().equals("booleanas")){
                            huboError = true;
                            Reportes.reporteAsignacion(Simbolo,Tipo, elemento.getLexema(), elemento.getFila(),elemento.getColumna(),bloque,"Asignación");
                            posicion++;
                        }else if(elemento.getPatron().equals("Entero")||elemento.getPatron().equals("Decimal")||elemento.getTipoToken().equals("ID")){
                            Cad += elemento.getLexema();
                            if(elemento.getTipoToken().equals("ID")){
                                ContieneID = true;
                            }
                                posicion++;
                                elemento = Tokens.get(posicion);
                            if((elemento.getTipoToken().equals("Aritméticos")||elemento.getTipoToken().equals("Comparación"))&& Fila == elemento.getFila()){
                                while (posicion < Tokens.size() && Fila == elemento.getFila()) {
                                    elemento = Tokens.get(posicion);
                                    if(Fila != elemento.getFila()){}else{
                                        if(elemento.getPatron().equals("Entero")||elemento.getPatron().equals("Decimal")||elemento.getTipoToken().equals("ID")){
                                            if(elemento.getTipoToken().equals("ID")){ContieneID = true;}
                                            Cad += " "+elemento.getLexema();
                                            ExpresionCorrecta = true;
                                        }else if(elemento.getTipoToken().equals("Aritméticos")){
                                            Cad += " "+elemento.getLexema();
                                            ExpresionCorrecta = false;
                                        }else if(elemento.getTipoToken().equals("Comparación")){
                                            Asignacion = true;
                                            ExpresionCorrecta = false;
                                        }else{
                                            huboError = true;
                                            Reportes.reporteErrorAsignacion(2,elemento.getFila() , elemento.getColumna(),bloque,"Asignación");
                                            CambiarDeLinea(Fila);
                                            elemento = Tokens.get(posicion);
                                        }
                                    }
                                } 
                            }else{
                                huboError = true;
                                if(ContieneID){Reportes.reporteAsignacion(Simbolo,Tipo, "Undefined", Tokens.get(posicion-1).getFila(),Tokens.get(posicion-1).getColumna(),bloque,"Asignación");
                                }else{Reportes.reporteAsignacion(Simbolo,Tipo,Tokens.get(posicion-1).getLexema(), Tokens.get(posicion-1).getFila(),Tokens.get(posicion-1).getColumna(),bloque,"Asignación");}
                            }
                            if(huboError){}else{
                                if(ExpresionCorrecta){
                                    if(ContieneID){
                                        Reportes.reporteAsignacion(Simbolo,Tipo, "Undefined", elemento.getFila(),elemento.getColumna(),bloque,"Asignación");
                                    }else{
                                        if(Asignacion){
                                            Reportes.reporteAsignacion(Simbolo,Tipo, "Undefined", elemento.getFila(),elemento.getColumna(),bloque,"Asignación");
                                        }else{
                                            Reportes.reporteAsignacion(Simbolo,Tipo, Double.toString(calc.evaluarExpresion(Cad)), elemento.getFila(),elemento.getColumna(),bloque,"Asignación");
                                        }
                                    }
                                }else{Reportes.reporteErrorAsignacion(3,elemento.getFila(),elemento.getColumna(),bloque,"Asignación");} 
                            }
                        }else{
                            while (posicion < Tokens.size() && Fila == elemento.getFila()) {                           
                            elemento = Tokens.get(posicion);
                            if(Fila != elemento.getFila()){}else{
                                multiplesValores=true;
                                if(elemento.getPatron().equals("Cadena")){
                                    esCadena = true;
                                }else{
                                    esOtro = true;
                                }
                                if(esTernario){
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
                                    }
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
                            } 
                            }
                        } 
                        //condicion final, si hubo un error anterior no hace nada, si es error de tipo 3 y si no crea el reporte
                        if(huboError){

                        }else if(seCerroLaCadena){
                            elemento = Tokens.get(posicion-1);
                            if(esTernario){
                                Reportes.reporteAsignacion(Simbolo,Tipo, "Undefined", elemento.getFila(),elemento.getColumna(),bloque,"Ternario");
                            }else if(esCadena){
                                Reportes.reporteAsignacion(Simbolo,Tipo, Cadena, elemento.getFila(),elemento.getColumna(),bloque,"Asignación");
                            }else if(multiplesValores){
                                Reportes.reporteAsignacion(Simbolo,Tipo, Double.toString(calc.evaluarExpresion(Cadena)), elemento.getFila(),elemento.getColumna(),bloque,"Asignación");
                            }else{
                                Reportes.reporteAsignacion(Simbolo,Tipo, Cadena, elemento.getFila(),elemento.getColumna(),bloque,"Asignación");
                            }
                       
                        }else{
                            Reportes.reporteErrorAsignacion(3,elemento.getFila(),elemento.getColumna(),bloque,"Asignación");
                        }
                        //| ID realizar esto si sobre tiempo
                    }else if(false){

                        //|Otros(ID|Constantes|Otros)* 
                    }else if(elemento.getTipoToken().equals("Otros")){
                        String Abrio = elemento.getPatron();
                        posicion++;
                        boolean cerro = false;
                        while (posicion < Tokens.size() && Fila == elemento.getFila()) {
                            elemento = Tokens.get(posicion);
                            if(Fila != elemento.getFila()){
                                break;
                            }
                            Cadena += elemento.getLexema();
                            posicion++;
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
                }else if(elemento.getTipoToken().equals("Aritméticos")){ 
                    boolean cerrarCiclo = true;
                    posicion++;
                    elemento = Tokens.get(posicion);
                    if(elemento.getTipoToken().equals("Asignación")){
                        //bucle para seguir en la siguiente linea
                        posicion++;
                        while (posicion < Tokens.size() && cerrarCiclo &&!(elemento.getTipoToken().equals("Comentario"))) {   
                            elemento = Tokens.get(posicion);
                            if(Fila != elemento.getFila()){cerrarCiclo = false;}else{
                            posicion++; 
                            }  
                        }
                        elemento = Tokens.get(posicion-1);
                        Reportes.reporteAsignacion(Simbolo,"Asignación", "Undefined", elemento.getFila(),elemento.getColumna(),bloque,"operadores");
                    }else{
                        Reportes.reporteErrorAsignacion(6,elemento.getFila(),elemento.getColumna(),bloque,"Asignación");
                        CambiarDeLinea(Fila);
                    }
                    //Condición para ver las invocaciones de metodos
                }else if(elemento.getLexema().equals("(")){
                    String Cadena = "";
                    boolean cerro = false;
                    int PosCerrado = 0;
                    posicion++;
                        //bucle para seguir en la siguiente linea
                        while (posicion < Tokens.size() && Fila == elemento.getFila() && !cerro) { 
                            //condicional para evitar que se vaya una fila de más
                            elemento = Tokens.get(posicion);
                            if(Fila != elemento.getFila()){
                            }else{
                                if(elemento.getLexema().equals(")")){
                                    cerro = true;
                                    PosCerrado = posicion;
                                }else{
                                    Cadena += elemento.getLexema();
                                }
                                posicion++;
                            } 
                        }
                        elemento = Tokens.get(PosCerrado);
                        if(cerro){
                            Reportes.reporteFuncion(Simbolo,"Funciones", Cadena, elemento.getFila(),elemento.getColumna(),bloque,"Invocaciones");
                        }else{
                            elemento = Tokens.get(posicion-1);
                            Reportes.reporteErrorFuncion(1,elemento.getFila(),elemento.getColumna(),bloque,"Funciones");
                        }   
                    //condición para invocaciones multiples:
                }else if(elemento.getLexema().equals(",")){
                    posicion++;
                    boolean coma = false;
                    while (posicion < Tokens.size() && Fila == elemento.getFila()) {
                        elemento = Tokens.get(posicion);    
                        posicion++;
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
                //para palabras clave:
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
                        }else if(elemento.getTipoToken().equals("ID")||elemento.getTipoToken().equals("Constantes")){
                            boolean HayId = false;
                            //condición para saber si entro con un id
                            if(elemento.getTipoToken().equals("ID")){
                                HayId = true;
                            }
                            posicion++;
                            elemento = Tokens.get(posicion);
                            //condición para saber si es por medio de comparación el boolean
                            if(elemento.getTipoToken().equals("Comparación")||elemento.getTipoToken().equals("Palabras clave")){
                                posicion++;
                                elemento = Tokens.get(posicion);
                                //aqui se recibe el siguiente valor ya sea constante o ID
                                if(elemento.getTipoToken().equals("ID")||elemento.getTipoToken().equals("Constantes")){
                                    posicion++;
                                    elemento = Tokens.get(posicion);
                                    if(elemento.getTipoToken().equals("ID")){
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
                        }else if(elemento.getTipoToken().equals("Lógicos")){
                            posicion++;
                            elemento = Tokens.get(posicion);          
                                //aqui se recibe el siguiente valor ya sea constante o ID
                                if(elemento.getTipoToken().equals("ID")||elemento.getTipoToken().equals("Constantes")){
                                    posicion++;
                                    elemento = Tokens.get(posicion);
                                    //se verifica que se cierre la sentencia con :
                                    if(elemento.getLexema().equals(":")){
                                        Reportes.reporteCondicional(Simbolo,Tipo,"Undefined", elemento.getFila(),elemento.getColumna(),bloque,"Condicional");
                                        posicion++;
                                    }else{
                                        Reportes.reporteErrorCondicional(2,elemento.getFila(),elemento.getColumna(),bloque,"Condicional");
                                        CambiarDeLinea(Fila);
                                    }
                                }else{
                                    Reportes.reporteErrorCondicional(3,elemento.getFila(),elemento.getColumna(),bloque,"Condicional");
                                    CambiarDeLinea(Fila);
                                }
                        }else{
                            Reportes.reporteErrorCondicional(5,elemento.getFila(),elemento.getColumna(),bloque,"Condicional");
                            CambiarDeLinea(Fila);
                        }
                        break;
                    case "def":
                    //Métodos	|PALABRA_CLAVE ID OTROS ((ID|Constantes)(Otros)?)* OTROS OTROS def 4325(1): 
                        boolean EsComa = false;
                        boolean huboError = false;
                        boolean Finalizador = true;
                        String Cadena = "";
                        posicion++;
                        elemento = Tokens.get(posicion);
                        if(elemento.getTipoToken().equals("ID")){
                            Simbolo = elemento.getLexema();
                            posicion++;
                            elemento = Tokens.get(posicion);
                            if(elemento.getLexema().equals("(")){
                                posicion++;
                                while (posicion < Tokens.size() && Fila == Tokens.get(posicion).getFila() && Finalizador) {
                                    elemento = Tokens.get(posicion);
                                    if(elemento.getTipoToken().equals("ID")||elemento.getTipoToken().equals("Constantes")){
                                        EsComa = false;
                                        Cadena += elemento.getLexema();
                                        posicion++;
                                    }else if(elemento.getLexema().equals(",")){
                                        EsComa = true;
                                        Cadena += elemento.getLexema();
                                        posicion++;
                                    }else if(elemento.getTipoToken().equals("Aritméticos")){
                                        EsComa = true;
                                        Cadena += elemento.getLexema();
                                        posicion++;
                                    }else if(elemento.getLexema().equals(")")){
                                        posicion++;
                                    }else if (elemento.getLexema().equals(":")){
                                        Finalizador = false;
                                        posicion++;
                                    }else{
                                        huboError = true;
                                        //si viene algún tipo de token que no se esperaba:
                                         Reportes.reporteErrorFuncion(4,elemento.getFila(),elemento.getColumna(),bloque,"Funciones");
                                        //bucle para seguir en la siguiente linea
                                        CambiarDeLinea(Fila);
                                    }         
                                }
                                if(EsComa){
                                        //No se armo el parametro como se esperaba:
                                         Reportes.reporteErrorFuncion(5,elemento.getFila(),elemento.getColumna(),bloque,"Funciones");
                                }else if(!huboError){
                                    Reportes.reporteFuncion(Simbolo,"Funciones", Cadena, Tokens.get(posicion-1).getFila(),Tokens.get(posicion-1).getColumna(),bloque,"Funciones");
                                }
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
                                        Cadena = "";
                                        posicion++;
                                        //aqui va el funcionamiento si se usa metodo range
                                        while (posicion < Tokens.size()&& Fila == Tokens.get(posicion).getFila()) {
                                            elemento = Tokens.get(posicion);
                                            if(elemento.getLexema().equals(",")){
                                                Cadena += elemento.getLexema();
                                                errorParametro = true;
                                            }else if(elemento.getTipoToken().equals("ID")||elemento.getTipoToken().equals("Constantes")){
                                                Cadena += elemento.getLexema();
                                                errorParametro = false;
                                            }else if(elemento.getLexema().equals(")")){
                                                cerroFor = true;
                                            }else if(elemento.getLexema().equals(":")){
                                                DoblePuntos = true;
                                            }else{
                                            
                                            }
                                            posicion++;  
                                        }
                                        //ciclos que validan q no haya ocurrido ningún error
                                        if(!errorParametro){
                                            if(cerroFor){
                                                if(DoblePuntos){
                                                    Reportes.reporteCiclo(Simbolo,"Ciclos", Cadena, elemento.getFila(),elemento.getColumna(),bloque,"For");
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
                                        Reportes.reporteCiclo("else","else", "--", elemento.getFila(),elemento.getColumna(),bloque,"else");
                                        posicion++;
                                    }else{
                                        //error si no recibe :
                                        Reportes.reporteErrorCiclos(4,elemento.getFila(),elemento.getColumna(),bloque,"else");
                                        //bucle para seguir en la siguiente linea
                                        CambiarDeLinea(Fila);
                                    }
                        break;
                    case "return":
                        //Return ->	| Return Expresión
                       //Expresión ->	| (Id|Constante)[{((Arimetico)||(Comparación))( Id|Constante)}*]?
                        Cadena = "";
                        boolean TieneID = false;
                        boolean CerroParentesis = false;
                        boolean FinalConstante = false;
                        boolean TokenRaro = false;
                        posicion++;
                        elemento = Tokens.get(posicion);
                        if(elemento.getTipoToken().equals("ID")||elemento.getTipoToken().equals("Constantes")){
                            if(elemento.getTipoToken().equals("ID")){
                                TieneID = true;
                            }
                            Cadena += " "+elemento.getLexema();
                            posicion++;
                            elemento = Tokens.get(posicion);
                            if(elemento.getTipoToken().equals("Comentario")){
                                posicion++;
                                elemento = Tokens.get(posicion);
                            }
                            FinalConstante = true;
                            //Si el siguiente elemento de la lista sigue en la misma fila después de la constante se hace analisis
                            if(Fila == Tokens.get(posicion).getFila()){
                                switch (elemento.getTipoToken()) {
                                    case "Aritméticos":
                                    case "Comparación":
                                        FinalConstante = false;
                                        Cadena += " "+elemento.getLexema();
                                        posicion++;
                                            while (posicion < Tokens.size() && Fila == Tokens.get(posicion).getFila()) {
                                                elemento = Tokens.get(posicion);
                                                
                                                if(Fila != elemento.getFila()){
                                                }else{
                                                    switch (elemento.getTipoToken()) {
                                                    case "Aritméticos":
                                                    case "Comparación":
                                                        FinalConstante = false;
                                                        Cadena += " "+elemento.getLexema();
                                                        posicion++;
                                                        break;
                                                    case "ID":
                                                    case "Constantes":
                                                        FinalConstante = true;
                                                        if(elemento.getTipoToken().equals("ID")){
                                                            TieneID = true;
                                                        }
                                                        Cadena += " "+elemento.getLexema();
                                                        posicion++;
                                                        break;
                                                    case "Comentario":
                                                        posicion++;
                                                        break;
                                                    default:
                                                        TokenRaro = true;
                                                        Reportes.reporteErrorReturn(4,elemento.getFila(),elemento.getColumna(),bloque,"Return");
                                                        Fila--;
                                                        break;
                                                    }
                                                }
                                            }
                                        //condiciones para los reportes:
                        //si hubo algún error no hace nada
                        if(TokenRaro){
                        //si no hay error entra al siguiente:
                        }else{
                            //si construyó bien el parametro entra, si no reporte de error
                            if(FinalConstante){
                                if(TieneID){
                                    Reportes.reporteCiclo("return","Palabra reservada","Undefined", elemento.getFila(),elemento.getColumna(),bloque,"return");
                                }else{
                                    Reportes.reporteCiclo("return","Palabra reservada",Double.toString(Calculadora.evaluarExpresion(Cadena)), elemento.getFila(),elemento.getColumna(),bloque,"return");
                                }
                            }else{
                                Reportes.reporteErrorReturn(6,elemento.getFila(),elemento.getColumna(),bloque,"Return");
                            }
                        }    
                                        break;
                                    case "Otros":
                                        Cadena += " "+elemento.getLexema();
                                            while (posicion < Tokens.size()-1 && Fila == Tokens.get(posicion).getFila()) {
                                                elemento = Tokens.get(posicion);
                                                switch (elemento.getTipoToken()) {
                                                    case "Otros":
                                                        if(elemento.getLexema().equals(")")){
                                                            CerroParentesis = true;
                                                            FinalConstante = false;
                                                            Fila--;
                                                        }else{
                                                            FinalConstante = false;
                                                        }
                                                        Cadena += " "+elemento.getLexema();
                                                        break;
                                                    case "ID":
                                                    case "Constantes":
                                                        FinalConstante = true;
                                                        Cadena += " "+elemento.getLexema();
                                                        if(elemento.getTipoToken().equals("ID")){
                                                            TieneID = true;
                                                        }
                                                        break;   
                                                    default:
                                                        TokenRaro = true;
                                                        Reportes.reporteErrorReturn(3,elemento.getFila(),elemento.getColumna(),bloque,"Return");
                                                        Fila--;
                                                        break;
                                                }
                                                posicion++;
                                            }
                                             //condiciones para los reportes:
                        //si hubo algún error no hace nada
                        if(TokenRaro){
                        //si no hay error entra al siguiente:
                        }else{
                            //si se cerro el parentesis entra si no crea el reporte de error
                            if(CerroParentesis){
                                //si construyó bien el parametro entra, si no reporte de error
                                if(FinalConstante){
                                    if(TieneID){
                                        Reportes.reporteCiclo("return","Palabra reservada","Undefined", elemento.getFila(),elemento.getColumna(),bloque,"return");
                                    }else{
                                        Reportes.reporteCiclo("return","Palabra reservada",Double.toString(Calculadora.evaluarExpresion(Cadena)), elemento.getFila(),elemento.getColumna(),bloque,"return");
                                    }
                                }else{
                                    Reportes.reporteErrorReturn(6,elemento.getFila(),elemento.getColumna(),bloque,"Return");
                                }
                            }else{
                                Reportes.reporteErrorReturn(5,elemento.getFila(),elemento.getColumna(),bloque,"Return");
                            }
                        }
                                        break;
                                    default:
                                        Reportes.reporteErrorReturn(2,elemento.getFila(),elemento.getColumna(),bloque,"Return");
                                        break;
                                }
                            }else{
                                //si el siguiente elemento no está en la misma fila se crea el reporte 
                                Reportes.reporteCiclo("return","Palabra reservada",Cadena, elemento.getFila(),elemento.getColumna(),bloque,"return");
                            }              
                        }else{
                            //error si se recibe un token que no se esperaba
                            Reportes.reporteErrorReturn(1,elemento.getFila(),elemento.getColumna(),bloque,"Return");
                        }
                        break;
                    case "break":
                        CambiarDeLinea(Fila);
                        Reportes.reporteCiclo("break","Palabra reservada", "break", elemento.getFila(),elemento.getColumna(),bloque,"return");
                        break;    
                    default:
                        // Código por defecto si la opción no coincide con ningún caso
                        System.out.println("Opción no válida");
                        break;
                }
            }else if(elemento.getTipoToken().equals("Comentario")){
                posicion++;
            }else{
                Reportes.reporteErrorGeneral(4,elemento.getFila(),elemento.getColumna(),bloque,"General");
                CambiarDeLinea(Fila);
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
        Reportes = new ReportesSintacticos();
        calc = new Calculadora();
        bloque = 1;
        posicion = 0;
        Tokens = recopiladorLexico();
    }


}