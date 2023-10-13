import java.util.Stack;

public class Calculadora {
    public static double evaluarExpresion(String expresion) {
        Stack<Double> operandos = new Stack<>();
        Stack<Character> operadores = new Stack<>();

        String[] tokens = expresion.split(" ");

        for (String token : tokens) {
            if (esNumero(token)) {
                operandos.push(Double.parseDouble(token));
            } else if (esOperador(token)) {
                char operador = token.charAt(0);
                while (!operadores.isEmpty() && tienePrecedencia(operador, operadores.peek())) {
                    calcular(operandos, operadores);
                }
                operadores.push(operador);
            }
        }

        while (!operadores.isEmpty()) {
            calcular(operandos, operadores);
        }

        return operandos.pop();
    }

    private static boolean esNumero(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean esOperador(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/")
                || token.equals("%") || token.equals("**");
    }

    private static boolean tienePrecedencia(char op1, char op2) {
        int precedenciaOp1 = obtenerPrecedencia(op1);
        int precedenciaOp2 = obtenerPrecedencia(op2);

        if (precedenciaOp1 == precedenciaOp2) {
            return op1 != '^';
        }

        return precedenciaOp1 > precedenciaOp2;
    }

    private static int obtenerPrecedencia(char operador) {
        switch (operador) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
            case '%':
                return 2;
            case '^':
            case '**':
                return 3;
            default:
                return 0;
        }
    }

    private static void calcular(Stack<Double> operandos, Stack<Character> operadores) {
        char operador = operadores.pop();
        double operand2 = operandos.pop();
        double operand1 = operandos.pop();

        if (operador == '+') {
            operandos.push(operand1 + operand2);
        } else if (operador == '-') {
            operandos.push(operand1 - operand2);
        } else if (operador == '*') {
            operandos.push(operand1 * operand2);
        } else if (operador == '/') {
            operandos.push(operand1 / operand2);
        } else if (operador == '%') {
            operandos.push(operand1 % operand2);
        } else if (operador == '^' || operador == '**') {
            operandos.push(Math.pow(operand1, operand2));
        }
    }
}





