package com.example.dynamicbuttons;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Calculator implements ICalculator {
    private Map<String, Variable> variables = new HashMap<String, Variable>();
    private Map<String, Function> functions = new HashMap<String, Function>();
    private ArrayList<Token> filter(String string) throws IOException {
        ArrayList<Token> tokens = new ArrayList<Token>();
        Token forAdd;
        String tmp = "";
        boolean isStarted = false;
        int i = 0;
        boolean isFrac = false;
        char ch = string.charAt(i), lastCh = string.charAt(i);
        while (i < string.length()) {
            if (ch >= '0' && ch <= '9') {
                if (lastCh >= '0' && lastCh <= '9' || lastCh =='+' || lastCh == '-' || lastCh == '*' || lastCh == '/' || lastCh == '^' || lastCh == '%' || lastCh == '(' || lastCh == '.' || lastCh == ',') {
                    tmp += String.valueOf(ch);
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
                        throw new IOException("Ошибка ввода 0");
                    }
                }
                else {
                    throw new IOException("Ошибка ввода 1");
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
                else if (lastCh =='+' || lastCh == '-' || lastCh == '*' || lastCh == '/' || lastCh == '^' || lastCh == '%' || lastCh == '(' || lastCh == ',' || lastCh >= 'a' && lastCh <= 'z') {
                    tmp += String.valueOf(ch);
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
                    throw new IOException("Ошибка ввода 2");
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
                        throw new IOException("Ошибка ввода 3");
                    }
                }
                else if (lastCh == '+' || lastCh == '-' || lastCh == '*' || lastCh == '/' || lastCh == '^' || lastCh == '%' || lastCh == '(' || lastCh == ',') {
                    throw new IOException("Ошибка ввода 4");
                }
                else if (lastCh == ')'){
                    forAdd = new OperationToken(ch);
                    tokens.add(forAdd);
                    lastCh = ch;
                    ++i;
                    if(i < string.length()) {
                        ch = string.charAt(i);
                    }
                }
                else {
                    throw new IOException("Ошибка ввода 5");
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
                        throw new IOException("Ошибка ввода 6");
                    }
                }
                else if (lastCh == '+' || lastCh == '-' || lastCh == '*' || lastCh == '/' || lastCh == '^' || lastCh == '%' || lastCh == '(' || lastCh == ',') {
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
                    throw new IOException("Ошибка ввода 7");
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
                    throw new IOException("Ошибка ввода 8");
                }
                else if (lastCh == '+' || lastCh == '-' || lastCh == '*' || lastCh == '/' || lastCh == '^' || lastCh == '%' || lastCh == '(' || lastCh == ',') {
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
                    throw new IOException("Ошибка ввода 9");
                }
            }
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
                throw new IOException("Ошибка ввода 7");
            }
        }
        else if (lastCh >= '0' && lastCh <= '9') {
            forAdd = new NumberToken(Double.parseDouble(tmp));
            tokens.add(forAdd);
        }
        else if (lastCh != ')') {
            throw new IOException("Ошибка ввода 10");
        }

        tokens.add(new EmptyToken());
        return tokens;
    }

    private final char[][] ActionsTable = {
         // \0  +  -  *  /  (  )  P  ^  F  ,
            {4, 2, 2, 2, 2, 2, 6, 1, 2, 2, 9}, //\0
            {3, 3, 3, 2, 2, 2, 3, 1, 2, 2, 9}, //+
            {3, 3, 3, 2, 2, 2, 3, 1, 2, 2, 9}, //-
            {3, 3, 3, 3, 3, 2, 3, 1, 2, 2, 9}, //*
            {3, 3, 3, 3, 3, 2, 3, 1, 2, 2, 9}, ///
            {7, 2, 2, 2, 2, 2, 5, 1, 2, 2, 10}, //(
            {3, 3, 3, 3, 3, 2, 3, 1, 3, 2, 9}, //^
            {3, 3, 3, 3, 3, 2, 3, 8, 3, 8, 9}  //F
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
        }
        switch (token.getType()){
            case 1: return 7;
            case 4: return 7;
            default: return 9;
        }
    }

    public class BraketsOpenException extends Exception{
        public BraketsOpenException(){}
        public BraketsOpenException(String S) {
            super(S);
        }
    }

    public class BraketsCloseException extends Exception{
        BraketsCloseException(){}
        BraketsCloseException(String S) {
            super(S);
        }
    }

    public class BraketsFunctionException extends Exception{
        BraketsFunctionException(){}
        BraketsFunctionException(String S) {
            super(S);
        }
    }

    public class ExtraCommaException extends Exception{
        ExtraCommaException(){}
        ExtraCommaException(String S) {
            super(S);
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
                case 4: break;
                case 5: S.pop(); i++; break;
                case 6: throw new BraketsOpenException("There is no ( for ) in " + intokens + " at position " + i);
                case 7: throw new BraketsOpenException("There is no ) for ( in " + intokens + " at position " + i);
                case 8: throw new BraketsOpenException("There is no ( for ) in " + intokens + " at position " + i);
                case 9: throw new ExtraCommaException("There is an extra comma in " + intokens + " at position " + i);
                case 10: i++;
            }
        } while (action != 4);
        return outtokens;
    }

    public double calcPostfix(ArrayList<Token> tokens) {

        return 0.0;
    }

    public double calc(String string, Map<String, Variable> variables, Map<String, Function> functions)
            throws IOException, BraketsFunctionException, BraketsOpenException, BraketsCloseException, ExtraCommaException {
        this.variables = variables;
        this.functions = functions;
        ArrayList<Token> tokens = new ArrayList<Token>();
        return calcPostfix(infixToPostfix(this.filter(string)));
    }
}