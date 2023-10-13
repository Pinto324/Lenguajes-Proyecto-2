/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizador;
import Tokens.Simbolos;
/**
 *
 * @author branp
 */
public class AnalizadorLexico {
    private int fila = 1;
    private int columna = 0;
    private int posicion = 0;
    private int inicio = 0;
    private static ArrayList<Simbolos> tokenRecopilado = new ArrayList<>();
    private static ArrayList<Simbolos> tokenErrores = new ArrayList<>();

    public AnalizadorLexico() {
    }
    
    public void analisisLexico(String texto){
        char temporal;
        tokenRecopilado = new ArrayList<>();
        tokenErrores = new ArrayList<>();
        while (posicion < texto.length()) {
            temporal = texto.charAt(posicion);
            String apoyoCadena = "";
            //if para validar si es comentario:
            if(EsComentario(temporal)){
                inicio = columna;
                while(temporal!='\n'||posicion < texto.length()){
                    apoyoCadena += temporal;
                    posicion++;
                    columna++;
                    temporal = texto.charAt(posicion);
                }
                reporteComentario(apoyoCadena);
                fila++;
                columna=0;
                //if para validar una cadena de comilla doble
            }else if(esComillasDobles(temporal)){
                boolean seCierra = true;
                inicio = columna;
                while(temporal!='"'||posicion < texto.length()){
                    //aqui se maneja si no se cerro la doble comilla
                    if(temporal=='\n'){
                        reporteErrorCadena(apoyoCadena);
                        fila++;
                        columna = 0;
                        posicion++;
                        temporal = '"';
                        seCierra = false;
                    }else{
                        apoyoCadena += temporal;
                        posicion++;
                        columna++;
                        temporal = texto.charAt(posicion);
                    }
                }
                if(seCierra){
                    reporteCadena(apoyoCadena);
                }       
            //if para validar una cadena de comilla simple
            }else if(esComillaSimple(temporal)){
                inicio = columna;
                boolean seCierra = true;
                while(temporal!='\''||posicion < texto.length()){
                    //aqui se maneja si no se cerro la comilla simple
                    if(temporal=='\n'){
                        reporteErrorCadena(apoyoCadena);
                        fila++;
                        columna = 0;
                        posicion++;
                        temporal = '"';
                        seCierra = false;
                    }else{
                        apoyoCadena += temporal;
                        posicion++;
                        columna++;
                        temporal = texto.charAt(posicion);
                    }
                }
                if(seCierra){
                    reporteCadena(apoyoCadena);
                }          
                //if encargado de manejar identificadores xd
            }else if ((temporal >= 'a' && temporal <= 'z')||(temporal >= 'A' && temporal <= 'Z')||temporal=='_'){
                Inicio = columna;
                boolean sinError = true; //variable que define que el identificador es legal 
                //ciclo el cual se encarga de acabar todo cuando encuentre un espacio
                while(temporal!=' '||posicion < texto.length()||temporal!='\n'||clasificadorCaracter(temporal)==-1){
                    //condicion la cual revisa que sea un caracter valido para identificador
                    if((temporal >= 'a' && temporal <= 'z')||(temporal >= 'A' && temporal <= 'Z')||temporal=='_'||(temporal >= '0' && temporal <= '9')){
                        apoyoCadena += temporal;
                        posicion++;
                        columna++;
                        temporal = texto.charAt(posicion);
                    //aqui se maneja si se encuentra un caracter invalido para un identificador:
                    }else{
                        reporteErrorIdentificadores(apoyoCadena);
                        //este ciclo while se encarga de llevar la lectura hasta el siguiente espacio o fin.
                        while(temporal!=' '||posicion < texto.length()){
                            if(temporal=='\n'){
                                fila++;
                                columna=0;
                                temporal = ' ';
                            }else{
                                columna++;
                                posicion++;
                                temporal = texto.charAt(posicion);
                            }     
                        }
                        sinError = false;  
                    }
                }//si no hay errores se crea el reporte correctamente, de lo contrario no
                if(sinError){
                    //aqui verificamos que el identificador no sea un operador logico:
                    if(esLogico(apoyoCadena)!=-1){
                        reporteOperadorLogico(apoyoCadena);
                        //aqui verificamos que la cadena no sea un boolean:
                    }else if(esBolean(apoyoCadena)){
                        reporteBooleans(apoyoCadena);
                        //aqui verificamos que la cadena no sea una palabra reservada:
                    }else if(esPalabraReservada(apoyoCadena)){
                        reportePalabraReservada(apoyoCadena);
                        //aqui se crea el reporte como identificador:
                    }else{
                        reporteIdentificadores(apoyoCadena);
                    }
                }
                //condición para los enteros y decimales:
            }else if(temporal >= '0' && temporal <= '9'){
                inicio = columna;
                boolean sinError = true; //variable que define que el entero o decimal es legal 
                boolean sinErrorDec = true;//variable para manejar error de no poner algo después del .
                boolean esDecimal = false; //variable para definir si es decimal o no
                //ciclo el cual se encarga de acabar todo cuando encuentre un espacio
                while(temporal!=' '||posicion < texto.length()||temporal!='\n'){
                    //aqui se mete si es un numero entero
                    if((temporal >= '0' && temporal <= '9')){
                        apoyoCadena += temporal;
                        if(esDecimal){
                            sinErrorDec = true;
                        }
                        posicion++;
                        columna++;
                        temporal = texto.charAt(posicion);
                    //aqui se mete si es un . lo cual lo vuelve un decimal, solo se puede una vez
                    }else if(temporal == '.' && !(esDecimal)){
                        esDecimal = true;
                        sinErrorDec = false;
                        apoyoCadena += temporal;
                        posicion++;
                        columna++;
                        temporal = texto.charAt(posicion);
                    }else{
                        //aqui va si hay un error en la cadena, lo cual genera un error dependiendo si es decimal o no y acaba el ciclo
                        sinError false;
                        if(esDecimal){
                            reporteErrorDecimales(apoyoCadena);
                        }else{
                            reporteErrorEntero(apoyoCadena);
                        }  
                       //este ciclo while se encarga de llevar la lectura hasta el siguiente espacio o fin.
                        while(temporal!=' '||posicion < texto.length()){
                            if(temporal=='\n'){
                                fila++;
                                columna=0;
                                temporal = ' ';
                            }else{
                                columna++;
                                posicion++;
                                temporal = texto.charAt(posicion);
                            } 
                    }
                }//si no hay errores se crea el reporte correctamente, de lo contrario n
                }
                if(sinError && sinErrorDec){
                        if(esDecimal){reporteDecimales(apoyoCadena);}else{reporteEnteros(apoyoCadena);}
                    }else if(!sinErrorDec){
                        reporteErrorDecimales(apoyoCadena);
                    }
            }else if(!(clasificadorCaracter(temporal)==-1)){
                inicio = columna;
                //if para validar si es caracter especial
                String Ayudador = "";
                boolean llave = false;
                try{
                    Ayudador = temporal + texto.charAt(posicion+1);
                    llave = EsCaracterDoble(Ayudador);
                }catch(StringIndexOutOfBoundsException e){       
                    System.out.println("No es doble");
                }
                //Validación si es caracter de 2 o 1 caracteres, dependiendo de esto incluye el caracter como una u otra cosa
                if(llave){//entra aqui cuando el caracter es doble
                    reporteCaracterDoble(Ayudador);
                    columna++;
                    columna++;
                    posicion++;
                    posicion++;
                }else{//entra aqui si el caracter es solo uno
                    reporteCaracter(temporal);
                    posicion++;
                    columna++;
                }
            //if para si hay salto de linea
            }else if(temporal == '\n'){
                columna = 0;
                fila++;
                posicion++;
            }else if(temporal == ' '){
                columna++;
                posicion++;
            }else{
                inicio = columna;
                reporteErrorNoIdentificado(temporal);
                columna++;
                posicion++;
            }
        }
    }

    //////////////////////////////////////////
    //Metodos usados para clasificar caracteres////////////////////

    //metodo que se encarga de crear el report por cada caracter:
    public void reporteCaracter(char Carac){
        switch(clasificadorCaracter(Carac)){
            case 1:
                Simbolos token = new Simbolos("Aritméticos","Suma",Carac,fila,columna,inicio);
                tokenRecopilado.add(token);
                break;
            case 2:
                Simbolos token = new Simbolos("Aritméticos","Resta",Carac,fila,columna,inicio);
                tokenRecopilado.add(token);
                break;
            case 3:
                Simbolos token = new Simbolos("Aritméticos","División",Carac,fila,columna,inicio);
                tokenRecopilado.add(token);
                break;
            case 4:
                Simbolos token = new Simbolos("Aritméticos","Módulo",Carac,fila,columna,inicio);
                tokenRecopilado.add(token);
                break;
            case 5:
                Simbolos token = new Simbolos("Aritméticos","Multiplicación",Carac,fila,columna,inicio);
                tokenRecopilado.add(token);
                break;
            case 6:
                Simbolos token = new Simbolos("Comparación","Mayor que ",Carac,fila,columna,inicio);
                tokenRecopilado.add(token);
                break;
            case 7:
                Simbolos token = new Simbolos("Comparación","Menor que ",Carac,fila,columna,inicio);
                tokenRecopilado.add(token);
                break;
            case 8:
                Simbolos token = new Simbolos("Asignación","Asignación",Carac,fila,columna,inicio);
                tokenRecopilado.add(token);
                break;
            case 9:
                Simbolos token = new Simbolos("Otros","Paréntesis",Carac,fila,columna,inicio);
                tokenRecopilado.add(token);
                break;
            case 10:
                Simbolos token = new Simbolos("Otros","Paréntesis",Carac,fila,columna,inicio);
                tokenRecopilado.add(token);
                break;
            case 11:
                Simbolos token = new Simbolos("Otros","Llaves",Carac,fila,columna,inicio);
                tokenRecopilado.add(token);
                break;
            case 12:
                Simbolos token = new Simbolos("Otros","Llaves",Carac,fila,columna,inicio);
                tokenRecopilado.add(token);
                break;
            case 13:
                Simbolos token = new Simbolos("Otros","Corchetes",Carac,fila,columna,inicio);
                tokenRecopilado.add(token);
                break;
            case 14:
                Simbolos token = new Simbolos("Otros","Corchetes",Carac,fila,columna,inicio);
                tokenRecopilado.add(token);
                break;
            case 15:
                Simbolos token = new Simbolos("Otros","Coma",Carac,fila,columna,inicio);
                tokenRecopilado.add(token);
                break;
            case 16:
                Simbolos token = new Simbolos("Otros","Punto y coma",Carac,fila,columna,inicio);
                tokenRecopilado.add(token);
                break;
            case 17:
                Simbolos token = new Simbolos("Otros","Dos puntos ",Carac,fila,columna,inicio);
                tokenRecopilado.add(token);
                break;                                        
            default:
                System.out.println("wtf xd");
                break;
        }

    public int clasificadorCaracter(char carac){
        if(carac == '+'){
            return 1;
        }else if(carac == '-'){
            return 2;
        }else if(carac == '/'){
            return 3;
        }else if(carac == '%'){
            return 4;
        }else if(carac == '*'){
            return 5;
        }else if(carac == '>'){
            return 6;
        }else if(carac == '<'){
            return 7;
        }else if(carac == '='){
            return 8;
        }else if(carac == '('){
            return 9;
        }else if(carac == ')'){
            return 10;
        }else if(carac == '{'){
            return 11;
        }else if(carac == '}'){
            return 12;
        }else if(carac == '['){
            return 13;
        }else if(carac == ']'){
            return 14;
        }else if(carac == ','){
            return 15;
        }else if(carac == ';'){
            return 16;
        }else if(carac == ':'){
            return 17;
        }else{
            return -1;
        }
    }
    }
    /////////////////////////////////////////////////////////////////////////////
    //Metodos usados para clasificar caracteres dobles
    //Metodo que se encarga de crear el reporte de un caracter si es doble:
    public void reporteCaracterDoble(String Carac){
        switch(clasificadorCaracterDoble(Carac.charAt(0))){
            case 1:
                Simbolos token = new Simbolos("Aritméticos","Exponente",Carac,fila,columna,inicio);
                tokenRecopilado.add(token);
                break;
            case 2:
                Simbolos token = new Simbolos("Aritméticos","División",Carac,fila,columna,inicio);
                tokenRecopilado.add(token);
                break;
            case 3:
                Simbolos token = new Simbolos("Comparación","Igual",Carac,fila,columna,inicio);
                tokenRecopilado.add(token);
                break;
            case 4:
                Simbolos token = new Simbolos("Comparación","Diferente",Carac,fila,columna,inicio);
                tokenRecopilado.add(token);
                break;
            case 5:
                Simbolos token = new Simbolos("Comparación","Mayor o igual que",Carac,fila,columna,inicio);
                tokenRecopilado.add(token);
                break;
            case 6:
                Simbolos token = new Simbolos("Comparación","Menor o igual que",Carac,fila,columna,inicio);
                tokenRecopilado.add(token);
                break;                      
            default:
                System.out.println("wtf xd");
                break;
        }

    }
    
    //metodo para validar si es un char doble:!+ =+
    public boolean EsCaracterDoble(String Caracteres){
        if(Caracteres.equals("**")||Caracteres.equals("//")||Caracteres.equals("==")||Caracteres.equals("!=")||Caracteres.equals(">=")||Caracteres.equals("<=")){
            return true;
        }else{
            return false;
        }
    }
    //metodo para clasificar el tipo de caracter doble que es, este sirve para hacer el reporte:
    public int clasificadorCaracterDoble(char carac){
        if(carac == '*'){
            return 1;
        }else if(carac == '/'){
            return 2;
        }else if(carac == '='){
            return 3;
        }else if(carac == '!'){
            return 4;
        }else if(carac == '>'){
            return 5;
        }else if(carac == '<'){
            return 6;
        }else{
            return -1;
        }
    }
  //////////////////////////////////////////////////////////////////////////////////////////////  
  //Metodo para validar si es comentario:
  public boolean EsComentario(char Carac){
    if(Carac == '#'){
        return true;
    }else{
        return false;
    }
  }  
  public void reporteComentario(String comentario){
    Simbolos token = new Simbolos("Comentario","Comentario",comentario,fila,columna,inicio);
    tokenRecopilado.add(token);
  }
    //////////////////////////////////////////////////////
    //Identificadores:
  //reporte de identificadores:
    public void reporteIdentificadores(String Ide){
        Simbolos token = new Simbolos("ID","([\w]|_)+(\w|\d)*",Ide,fila,columna,inicio);
         tokenRecopilado.add(token);
    }
    //Reporte error de identificador:
    public void reporteErrorIdentificadores(String ide){
        Simbolos error = new Simbolos("ID","Caracter invalido",ide,fila,columna,inicio);
        tokenErrores.add(error);
    }
  /////////////////////////////////////////////////
  //Constantes:
  //metodo para validar si es una cadena:
  public boolean esComillasDobles(char carac){
    if(carac == '"'){
        return true;
    }else{
        return false;
    }
  }
    public void reporteCadena(String comentario){
        Simbolos token = new Simbolos("Constantes","Cadena",comentario,fila,columna,inicio);
        tokenRecopilado.add(token);
    }
    //metodo para errores de no cerrar una cadena
    public void reporteErrorCadena(String Cadena){
        Simbolos error = new Simbolos("Constantes","Cadena No se cerro las comillas",Cadena,fila,columna,inicio);
        tokenErrores.add(error);
    }
  public boolean esComillaSimple(char carac){
    if(carac == '\''){
        return true;
    }else{
        return false;
    }
  }
    //reporte para Enteros:
    public void reporteEnteros(String Ide){
        Simbolos token = new Simbolos("Constantes","Entero",Ide,fila,columna,inicio);
        tokenRecopilado.add(token);
    }
    //reporte para decimales:
    public void reporteDecimales(String Ide){
        Simbolos token = new Simbolos("Constantes","Decimal",Ide,fila,columna,inicio);
        tokenRecopilado.add(token);
    }
    //reporte para errores en enteros:
    public void reporteErrorEntero(String Cad){
        Simbolos error = new Simbolos("Constantes","Entero con caracter Invalido",Cad,fila,columna,inicio);
        tokenErrores.add(error);
    }
    //repoirte para errores en decimales:
    public void reporteErrorDecimales(String Cad){
        Simbolos error = new Simbolos("Constantes","Decimal con Caracter Invalido",Cad,fila,columna,inicio);
        tokenErrores.add(error);
    }
    //metodo para validar si es Boolean
    public boolean esBolean(String cadena){
        if(cadena.equals("True")||cadena.equals("False")){
            return true;
        }else{
            return false;
        }
    }
    //metodo para el reporte de boolean
    public void reporteBooleans(String Ide){
        Simbolos token = new Simbolos("Constantes","booleanas",Ide,fila,columna,inicio);
        tokenRecopilado.add(token);
    }
    ///////////////////////////////////////////////////////
    //Operadores logicos
     //metodo para crear el reporte de un operador logico:
        public void reporteOperadorLogico(String Carac){
        switch(esLogico(Carac)){
            case 1:
                Simbolos token = new Simbolos("Lógicos","y",Carac,fila,columna,inicio);
                tokenRecopilado.add(token);
                break;
            case 2:
                Simbolos token = new Simbolos("Lógicos","o",Carac,fila,columna,inicio);
                tokenRecopilado.add(token);
                break;
            case 3:
                Simbolos token = new Simbolos("Lógicos","Negación",Carac,fila,columna,inicio);
                tokenRecopilado.add(token);
                break;                    
            default:
                System.out.println("wtf xd");
                break;
        }

    }
    //metodo para saber que tipo de operador es:
    public int esLogico(String Cadena){
        if(Cadena.equals("and")){
            return 1;
        }else if(Cadena.equals("or")){
            return 2;
        }else if(Cadena.equals("not")){
            return 3;
        }else{return -1;}
    }
    //////////////////////////////////////////////////////
    //Palabras Clave
     //metodo para crear el reporte de Palabras clave:
        public void reportePalabraReservada(String Carac){
            Simbolos token = new Simbolos("Palabras clave","Palabra reservada",Carac,fila,columna,inicio);
            tokenRecopilado.add(token);
        }
    //metodo para saber si es palabra reservada :
    public boolean esPalabraReservada(String Cadena){
        if(Cadena.equals("as")||Cadena.equals("assert")||Cadena.equals("break")||Cadena.equals("class")||Cadena.equals("continue")){
            return true;
        }else if(Cadena.equals("def")||Cadena.equals("del")||Cadena.equals("elif")||Cadena.equals("else")||Cadena.equals("except")){
            return true;
        }else if(Cadena.equals("finally")||Cadena.equals("for")||Cadena.equals("from")||Cadena.equals("global")||Cadena.equals("if")){
            return true;
        }else if(Cadena.equals("import")||Cadena.equals("in")||Cadena.equals("is")||Cadena.equals("lambda")||Cadena.equals("None")){
            return true;
        }else if(Cadena.equals("nonlocal")||Cadena.equals("pass")||Cadena.equals("raise")||Cadena.equals("return")||Cadena.equals("try")){
            return true;
        }else if(Cadena.equals("while")||Cadena.equals("with")||Cadena.equals("yield")){
            return true;
        }else{return false;}
    }
    //////////////////////////////////////////////////////
    //reporte de error para token no identificado:
    public void reporteErrorNoIdentificado(char Cad){
        Simbolos error = new Simbolos("404","Caracter No identificado",Cad,fila,columna,inicio);
        tokenErrores.add(error);
    }
    ///////////////////////////////////////////////////
    //metodos get y set de mi arreglo de tokens:
    public ArrayList<Simbolos> gettokenRecopilado() {
        return tokenRecopilado;
    }

    public void settokenRecopilado(ArrayList<Simbolos> tokenRecopilado) {
        this.tokenRecopilado = tokenRecopilado;
    }
}
