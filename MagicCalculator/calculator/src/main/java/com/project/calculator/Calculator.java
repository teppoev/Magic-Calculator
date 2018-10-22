package com.project.calculator;

import java.io.IOException;
import java.util.*;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Calculator implements ICalculator {
    private Map<String, Variable> variables = new HashMap<String, Variable>();
    private Map<String, Function> functions = new HashMap<String, Function>();
    private ArrayList<Token> filter(String string) throws IOException {
        ArrayList<Token> tokens = new ArrayList<Token>();
        Token forAdd;
        String tmp = "";
        boolean isStarted = false;
        boolean isVariable = false;
        int i = 0;
        char ch = string.charAt(i), lastCh = string.charAt(i);
        while (i < string.length()) {
            if (ch >= '0' && ch <= '9') {
                if (lastCh >= '0' && lastCh <= '9' || lastCh =='+' || lastCh == '-' || lastCh == '*' || lastCh == '/' || lastCh == '^' || lastCh == '%' || lastCh == '(' || lastCh == '.' || lastCh == ',' || lastCh == '√') {
                    tmp += ch;
                    lastCh = ch;
                    ++i;
                    if(i < string.length()) {
                        ch = string.charAt(i);
                    }
                }
                else if (lastCh == ')') {
                    forAdd = new OperationToken('*');
                    tokens.add(forAdd);
                    tmp += String.valueOf(ch);
                    lastCh = ch;
                    ++i;
                    if(i < string.length()) {
                        ch = string.charAt(i);
                    }
                }
                else if (lastCh >= 'a' && lastCh <= 'z') {
                    if (variables.get(tmp) != null) {
                        forAdd = new VariableToken(tmp);
                        tokens.add(forAdd);
                        tmp = "";
                        forAdd = new OperationToken(ch);
                        tokens.add(forAdd);
                        tmp += String.valueOf(ch);
                        lastCh = ch;
                        ++i;
                        if(i < string.length()) {
                            ch = string.charAt(i);
                        }
                    }
                    else if (functions.get(tmp) != null) {
                        forAdd = new FunctionToken(tmp);
                        tokens.add(forAdd);
                        tmp = "";
                        forAdd = new OperationToken(ch);
                        tokens.add(forAdd);
                        tmp += String.valueOf(ch);
                        lastCh = ch;
                        ++i;
                        if(i < string.length()) {
                            ch = string.charAt(i);
                        }
                    }
                    else {
                        throw new IOException("Ошибка ввода 0 " + ch  + " " + tmp + " " + lastCh);
                    }
                }
                else {
                    throw new IOException("Ошибка ввода 1 " + ch  + " " + tmp + " " + lastCh);
                }
            }
            else if (ch >= 'a' && ch <= 'z') {
                if (lastCh >= '0' && lastCh <= '9' || lastCh == '.') {
                    forAdd = new NumberToken(Double.parseDouble(tmp));
                    tokens.add(forAdd);
                    tmp = "";
                    isStarted = false;
                    forAdd = new OperationToken('*');
                    tokens.add(forAdd);
                    tmp += String.valueOf(ch);
                    lastCh = ch;
                    ++i;
                    if(i < string.length()) {
                        ch = string.charAt(i);
                    }
                }
                else if (lastCh =='+' || lastCh == '-' || lastCh == '*' || lastCh == '/' || lastCh == '^' || lastCh == '%' || lastCh == '(' || lastCh == ',' || lastCh == '√' || lastCh >= 'a' && lastCh <= 'z') {
                    tmp += ch;
                    lastCh = ch;
                    ++i;
                    if(i < string.length()) {
                        ch = string.charAt(i);
                    }
                }
                else if  (lastCh == ')') {
                    forAdd = new OperationToken('*');
                    tokens.add(forAdd);
                    tmp += String.valueOf(ch);
                    lastCh = ch;
                    ++i;
                    if (i < string.length()) {
                        ch = string.charAt(i);
                    }
                }
                else {
                    throw new IOException("Ошибка ввода 2 " + ch + " " + tmp + " " + lastCh);
                }
            }
            else if (ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^' || ch == '%' || ch == ')' || ch == ',') {
                if (lastCh >= '0' && lastCh <= '9' || lastCh == '.') {
                    forAdd = new NumberToken(Double.parseDouble(tmp));
                    tokens.add(forAdd);
                    tmp = "";
                    isStarted = false;
                    forAdd = new OperationToken(ch);
                    tokens.add(forAdd);
                    lastCh = ch;
                    ++i;
                    if(i < string.length()) {
                        ch = string.charAt(i);
                    }
                }
                else if (lastCh >= 'a' && lastCh <= 'z'){
                    if (variables.get(tmp) != null) {
                        forAdd = new VariableToken(tmp);
                        tokens.add(forAdd);
                        tmp = "";
                        forAdd = new OperationToken(ch);
                        tokens.add(forAdd);
                        lastCh = ch;
                        ++i;
                        if(i < string.length()) {
                            ch = string.charAt(i);
                        }
                    }
                    else if (functions.get(tmp) != null) {
                        forAdd = new FunctionToken(tmp);
                        tokens.add(forAdd);
                        tmp = "";
                        forAdd = new OperationToken(ch);
                        tokens.add(forAdd);
                        lastCh = ch;
                        ++i;
                        if(i < string.length()) {
                            ch = string.charAt(i);
                        }
                    }
                    else {
                        throw new IOException("Ошибка ввода 3 " + ch + " " + tmp + " " + lastCh);
                    }
                }
                else if (lastCh == '+' || lastCh == '-' || lastCh == '*' || lastCh == '/' || lastCh == '^' || lastCh == '%' || lastCh == '(' || lastCh == ',') {
                    throw new IOException("Ошибка ввода 4 " + ch  + " " + tmp + " " + lastCh);
                }
                else if (lastCh == ')' || lastCh == '√'){
                    forAdd = new OperationToken(ch);
                    tokens.add(forAdd);
                    lastCh = ch;
                    ++i;
                    if(i < string.length()) {
                        ch = string.charAt(i);
                    }
                }
                else {
                    throw new IOException("Ошибка ввода 5 " + ch  + " " + tmp + " " + lastCh);
                }
            }
            else if (ch == '(') {
                if (lastCh >= '0' && lastCh <= '9' || lastCh == '.') {
                    forAdd = new NumberToken(Double.parseDouble(tmp));
                    tokens.add(forAdd);
                    tmp = "";
                    isStarted = false;
                    forAdd = new OperationToken('*');
                    tokens.add(forAdd);
                    forAdd = new OperationToken(ch);
                    tokens.add(forAdd);
                    lastCh = ch;
                    ++i;
                    if(i < string.length()) {
                        ch = string.charAt(i);
                    }
                }
                else if (lastCh >= 'a' && lastCh <= 'z') {
                    if (variables.get(tmp) != null) {
                        forAdd = new VariableToken(tmp);
                        tokens.add(forAdd);
                        tmp = "";
                        forAdd = new OperationToken('*');
                        tokens.add(forAdd);
                        forAdd = new OperationToken(ch);
                        tokens.add(forAdd);
                        lastCh = ch;
                        ++i;
                        if(i < string.length()) {
                            ch = string.charAt(i);
                        }
                    }
                    else if (functions.get(tmp) != null) {
                        forAdd = new FunctionToken(tmp);
                        tokens.add(forAdd);
                        tmp = "";
                        forAdd = new OperationToken(ch);
                        tokens.add(forAdd);
                        lastCh = ch;
                        ++i;
                        if(i < string.length()) {
                            ch = string.charAt(i);
                        }
                    }
                    else {
                        throw new IOException("Ошибка ввода 6 " + ch  + " " + tmp + " " + lastCh);
                    }
                }
                else if (lastCh == '+' || lastCh == '-' || lastCh == '*' || lastCh == '/' || lastCh == '^' || lastCh == '%' || lastCh == '(' || lastCh == ',' || lastCh == '√') {
                    forAdd = new OperationToken(ch);
                    tokens.add(forAdd);
                    lastCh = ch;
                    ++i;
                    if(i < string.length()) {
                        ch = string.charAt(i);
                    }
                }
                else if (lastCh == ')') {
                    forAdd = new OperationToken('*');
                    tokens.add(forAdd);
                    forAdd = new OperationToken(ch);
                    tokens.add(forAdd);
                    lastCh = ch;
                    ++i;
                    if(i < string.length()) {
                        ch = string.charAt(i);
                    }
                }
                else {
                    throw new IOException("Ошибка ввода 7 " + ch  + " " + tmp + " " + lastCh);
                }
            }
            else if (ch == '.') {
                if (lastCh >= '0' && lastCh <= '9' && !isStarted) {
                    tmp += ch;
                    lastCh = ch;
                    ++i;
                    if(i < string.length()) {
                        ch = string.charAt(i);
                    }
                    isStarted = true;
                }
                else if (lastCh >= '0' && lastCh <= '9' || lastCh >= 'a' && lastCh <= 'z' || lastCh == '.') {
                    throw new IOException("Ошибка ввода 8 " + ch  + " " + tmp + " " + lastCh);
                }
                else if (lastCh == '+' || lastCh == '-' || lastCh == '*' || lastCh == '/' || lastCh == '^' || lastCh == '%' || lastCh == '(' || lastCh == ',' || lastCh == '√') {
                    tmp += "0.";
                    isStarted = true;
                    lastCh = ch;
                    ++i;
                    if(i < string.length()) {
                        ch = string.charAt(i);
                    }
                }
                else if (lastCh == ')') {
                    forAdd = new OperationToken('*');
                    tokens.add(forAdd);
                    tmp += "0.";
                    isStarted = true;
                    lastCh = ch;
                    ++i;
                    if(i < string.length()) {
                        ch = string.charAt(i);
                    }
                }
                else {
                    throw new IOException("Ошибка ввода 9 " + ch  + " " + tmp + " " + lastCh);
                }
            } else if (ch == '√'){
                if (lastCh >= '0' && lastCh <= '9' || lastCh == '.') {
                    forAdd = new NumberToken(Double.parseDouble(tmp));
                    tokens.add(forAdd);
                    tmp = "";
                    isStarted = false;
                    forAdd = new OperationToken('*');
                    tokens.add(forAdd);
                    forAdd = new OperationToken(ch);
                    tokens.add(forAdd);
                    lastCh = ch;
                    ++i;
                    if(i < string.length()) {
                        ch = string.charAt(i);
                    }
                }
                else if (lastCh >= 'a' && lastCh <= 'z'){
                    if (variables.get(tmp) != null) {
                        forAdd = new VariableToken(tmp);
                        tokens.add(forAdd);
                        tmp = "";
                        forAdd = new OperationToken('*');
                        tokens.add(forAdd);
                        forAdd = new OperationToken(ch);
                        tokens.add(forAdd);
                        lastCh = ch;
                        ++i;
                        if(i < string.length()) {
                            ch = string.charAt(i);
                        }
                    }
                    else if (functions.get(tmp) != null) {
                        forAdd = new FunctionToken(tmp);
                        tokens.add(forAdd);
                        tmp = "";
                        forAdd = new OperationToken('*');
                        tokens.add(forAdd);
                        forAdd = new OperationToken(ch);
                        tokens.add(forAdd);
                        lastCh = ch;
                        ++i;
                        if(i < string.length()) {
                            ch = string.charAt(i);
                        }
                    }
                    else {
                        throw new IOException("Ошибка ввода 10 " + ch + " " + tmp + " " + lastCh);
                    }
                }
                else if (lastCh == '+' || lastCh == '-' || lastCh == '*' || lastCh == '/' || lastCh == '^' || lastCh == '%' || lastCh == '(' || lastCh == ',' || lastCh == ')' || lastCh == '√') {
                    forAdd = new OperationToken(ch);
                    tokens.add(forAdd);
                    lastCh = ch;
                    ++i;
                    if(i < string.length()) {
                        ch = string.charAt(i);
                    }
                }
                else {
                    throw new IOException("Ошибка ввода 11 " + ch  + " " + tmp + " " + lastCh);
                }
            }
            else throw new IOException("Ошибка ввода Незнакомый символ" + ch + " " + tmp + " " + lastCh);
        }

        if (lastCh >= 'a' && lastCh <= 'z') {
            if (variables.get(tmp) != null) {
                forAdd = new VariableToken(tmp);
                tokens.add(forAdd);
            }
            else if (functions.get(tmp) != null) {
                forAdd = new FunctionToken(tmp);
                tokens.add(forAdd);
            }
            else {
                throw new IOException("Ошибка ввода 12" + ch + " " + tmp + " " + lastCh);
            }
        }
        else if (lastCh >= '0' && lastCh <= '9') {
            forAdd = new NumberToken(Double.parseDouble(tmp));
            tokens.add(forAdd);
        }
        else if (lastCh != ')') {
            throw new IOException("Ошибка ввода 13 " + ch  + " " + tmp + " " + lastCh);
        }

        tokens.add(new EmptyToken());
        return tokens;
    }

    private final char[][] ActionsTable = {
         // \0  +  -  *  /  (  )  P  ^  F  ,  %  √
            {4, 2, 2, 2, 2, 2, 6, 1, 2, 2, 9, 2, 2}, //\0
            {3, 3, 3, 2, 2, 2, 3, 1, 2, 2, 9, 2, 2}, //+
            {3, 3, 3, 2, 2, 2, 3, 1, 2, 2, 9, 2, 2}, //-
            {3, 3, 3, 3, 3, 2, 3, 1, 2, 2, 9, 3, 2}, //*
            {3, 3, 3, 3, 3, 2, 3, 1, 2, 2, 9, 3, 2}, ///
            {7, 2, 2, 2, 2, 2, 5, 1, 2, 2, 10,2, 2}, //(
            {3, 3, 3, 3, 3, 2, 3, 1, 3, 2, 9, 3, 2}, //^
            {3, 3, 3, 3, 3, 2, 3, 8, 3, 8, 9, 3, 8}, //F
            {3, 3, 3, 3, 3, 2, 3, 1, 2, 2, 9, 3, 2}, //%
            {3, 3, 3, 3, 3, 2, 3, 8, 3, 8, 9, 3, 8}, //√
    };

    private int ActionsRowNumber(Token token) {
        switch (token.getValue().charAt(0)) {
            case 0: return 0;
            case '+': return 1;
            case '-': return 2;
            case '*': return 3;
            case '/': return 4;
            case '(': return 5;
            case '^': return 6;
            case '%': return 8;
            case '√': return 9;
            default: return 7;
        }
    }

    private int ActionsColNumber(Token token) {
        switch (token.getValue().charAt(0)) {
            case 0: return 0;
            case '+': return 1;
            case '-': return 2;
            case '*': return 3;
            case '/': return 4;
            case '(': return 5;
            case ')': return 6;
            case '^': return 8;
            case ',': return 10;
            case '%': return 11;
            case '√': return 12;
        }
        switch (token.getType()){
            case 1: return 7;
            case 4: return 7;
            default: return 9;
        }
    }

    private ArrayList<Token> infixToPostfix(ArrayList<Token> intokens) throws BraketsOpenException, BraketsCloseException, BraketsFunctionException, ExtraCommaException{
        int i = 0; // i is index intokens
        int row, col;
        char action;
        ArrayList<Token> outtokens = new ArrayList<Token>();
        Stack<Token> S = new Stack<Token>();
        do {
            col = ActionsColNumber(intokens.get(i));
            row = S.isEmpty() ? 0 : ActionsRowNumber(S.peek());
            action = ActionsTable[row][col];
            switch (action) {
                case 1: outtokens.add(intokens.get(i++));break;
                case 2: S.push(intokens.get(i++)); break;
                case 3: outtokens.add(S.peek()); S.pop(); break;
                case 4: outtokens.add(new EmptyToken()); break;
                case 5: S.pop(); ++i; break;
                case 6: throw new BraketsOpenException("There is no ( for ) in " + intokens + " at position " + i);
                case 7: throw new BraketsOpenException("There is no ) for ( in " + intokens + " at position " + i);
                case 8: throw new BraketsOpenException("There is no ( for ) in " + intokens + " at position " + i);
                case 9: throw new ExtraCommaException("There is an extra comma in " + intokens + " at position " + i);
                case 10: ++i;
            }
        } while (action != 4);
        return outtokens;
    }

    private double calcPostfix(ArrayList<Token> tokens) throws IncorrectPostfixException{
        Stack<Token> S = new Stack<Token>();
        Token tmp;
        double[] tmps;
        for (Token token : tokens) {
            tmp = token;
            switch (tmp.getType()) {
                case 1:
                    S.push(tmp);
                    break;
                case 2:
                    tmps = new double[2];
                    tmps[1] = Double.parseDouble(S.pop().getValue());
                    if (tmp.getValue().charAt(0) != '√') {
                        tmps[0] = Double.parseDouble(S.pop().getValue());
                    }
                    switch (tmp.getValue().charAt(0)) {
                        case '+':
                            S.push(new NumberToken(tmps[0] + tmps[1]));
                            break;
                        case '-':
                            S.push(new NumberToken(tmps[0] - tmps[1]));
                            break;
                        case '*':
                            S.push(new NumberToken(tmps[0] * tmps[1]));
                            break;
                        case '/':
                            S.push(new NumberToken(tmps[0] / tmps[1]));
                            break;
                        case '^':
                            S.push(new NumberToken(pow(tmps[0], tmps[1])));
                            break;
                        case '%':
                            S.push(new NumberToken(tmps[0] % tmps[1]));
                            break;
                        case '√':
                            S.push(new NumberToken(sqrt(tmps[1])));
                            break;
                    }
                    break;
                case 3: {
                    Function foo = new Function();
                    if (functions.get(tmp.getValue()) != null) {
                        foo = functions.get(tmp.getValue());
                    } else throw new IncorrectPostfixException("Function was not found");
                    int numberOfVariables = foo.getNumberOfVariables();
                    tmps = new double[numberOfVariables];
                    try {
                        for (int j = 0; j < numberOfVariables; ++j) {
                            tmps[numberOfVariables - 1 - j] = Double.parseDouble(S.pop().getValue());
                        }
                    } catch (EmptyStackException e) {
                        throw new IncorrectPostfixException("There are not enough variables for function in stack");
                    }
                    S.push(new NumberToken(foo.calc(tmps)));
                    break;
                }
                case 4:
                    double number = 0;
                    if (variables.get(tmp.getValue()) != null) {
                        number = variables.get(tmp.getValue()).getValue();
                    } else throw new IncorrectPostfixException("Variable was not found");
                    S.push(new NumberToken(number));
                    break;
                case 5:
                    return Double.parseDouble(S.peek().getValue());
                case 6:
                    throw new IncorrectPostfixException("There was found a comma in Postfix");
            }
        }
        throw new IncorrectPostfixException("Something wrong in 3th function");
    }

    class BraketsOpenException extends Exception{
        public BraketsOpenException(){}
        BraketsOpenException(String S) {
            super(S);
        }
    }

    class BraketsCloseException extends Exception{
        public BraketsCloseException(){}
        BraketsCloseException(String S) {
            super(S);
        }
    }

    class BraketsFunctionException extends Exception{
        public BraketsFunctionException(){}
        BraketsFunctionException(String S) {
            super(S);
        }
    }

    class ExtraCommaException extends Exception{
        public ExtraCommaException(){}
        ExtraCommaException(String S) {
            super(S);
        }
    }

    class IncorrectPostfixException extends Exception{
        public IncorrectPostfixException(){}
        IncorrectPostfixException(String S) {
            super(S);
        }
    }

    public double calc(String string, Map<String, Variable> _variables, Map<String, Function> _functions)
            throws IOException, BraketsFunctionException, BraketsOpenException, BraketsCloseException, ExtraCommaException, IncorrectPostfixException {
        variables = _variables;
        functions = _functions;
        ArrayList<Token> tokens = new ArrayList<Token>();
        tokens = filter(string);
        for (Token token : tokens) {
            System.out.print(token.getValue() + " ");
        }
        System.out.println();
        tokens = infixToPostfix(tokens);
        for (Token token : tokens) {
            System.out.print(token.getValue() + " ");
        }
        System.out.println();
        return calcPostfix(tokens);
    }
}