package com.project.calculator;

import java.io.IOException;
import java.util.*;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Calculator implements ICalculator {
    private Map<String, Variable> variables = new HashMap<String, Variable>();
    private Map<String, IFunction> functions = new HashMap<String, IFunction>();
    private ArrayList<Token> filter(String string) throws IOException {
        ArrayList<Token> tokens = new ArrayList<Token>();
        Token forAdd;
        String tmp = "";
        boolean isStarted = false;
        boolean isNotVariable = true;
        boolean isExp = false;
        int i = 0;
        char ch = string.charAt(i), lastCh = string.charAt(i);
        if(ch == '-'){
            tmp = "0";
            lastCh = '0';
        }
        while (i < string.length()) {
            if (ch >= '0' && ch <= '9')
            {
                if (lastCh >= '0' && lastCh <= '9' ||
                        lastCh == '+' ||
                        lastCh == '-' ||
                        lastCh == '*' ||
                        lastCh == '/' ||
                        lastCh == '^' ||
                        lastCh == '%' ||
                        lastCh == '(' ||
                        lastCh == '.' ||
                        lastCh == ',' ||
                        lastCh == '=' ||
                        lastCh >= 'a' && lastCh <= 'z' ||
                        lastCh >= 'A' && lastCh <= 'Z' ||
                        lastCh == '_')
                {
                    tmp += ch;
                    lastCh = ch;
                    ++i;
                    if(i < string.length()) {
                        ch = string.charAt(i);
                    }
                }
                else if (lastCh == '>' ||
                         lastCh == '<')
                {
                    tmp += lastCh;
                    forAdd = new BinaryToken(tmp);
                    tokens.add(forAdd);
                    tmp = "";
                    isStarted = false;
                    isExp = false;
                    tmp += ch;
                    lastCh = ch;
                    ++i;
                    if(i < string.length()) {
                        ch = string.charAt(i);
                    }
                }
                else if (lastCh == ')')
                {
                    forAdd = new OperationToken('*');
                    tokens.add(forAdd);
                    tmp += ch;
                    lastCh = ch;
                    ++i;
                    if(i < string.length()) {
                        ch = string.charAt(i);
                    }
                }
                else {
                    throw new IOException("Ошибка ввода 1 " + ch  + " " + tmp + " " + lastCh);
                }
            }
            else if (ch >= 'a' && ch <= 'z'|| ch >= 'A' && ch <= 'Z' && ch != 'E' ||
                    ch == '_')
            {
                if (lastCh >= '0' && lastCh <= '9' && isNotVariable ||
                        lastCh == '.' ||
                        lastCh == 'E')
                {
                    if (lastCh == 'E') {
                        tmp += "1";
                    }
                    forAdd = new NumberToken(Double.parseDouble(tmp));
                    tokens.add(forAdd);
                    tmp = "";
                    isStarted = false;
                    isExp = false;
                    forAdd = new OperationToken('*');
                    tokens.add(forAdd);
                    isNotVariable = false;
                    tmp += ch;
                    lastCh = ch;
                    ++i;
                    if(i < string.length()) {
                        ch = string.charAt(i);
                    }
                }
                else if (lastCh =='+' ||
                        lastCh == '-' ||
                        lastCh == '*' ||
                        lastCh == '/' ||
                        lastCh == '^' ||
                        lastCh == '%' ||
                        lastCh == '(' ||
                        lastCh == ',' ||
                        lastCh == '=' ||
                        lastCh >= 'a' && lastCh <= 'z' ||
                        lastCh >= 'A' && lastCh <= 'Z' && lastCh != 'E' ||
                        lastCh >= '0' && lastCh <= '9' ||
                        lastCh == '_')
                {
                    isNotVariable = false;
                    tmp += ch;
                    lastCh = ch;
                    ++i;
                    if(i < string.length()) {
                        ch = string.charAt(i);
                    }
                }
                else if (lastCh == '>' ||
                        lastCh == '<')
                {
                    tmp += lastCh;
                    forAdd = new BinaryToken(tmp);
                    tokens.add(forAdd);
                    tmp = "";
                    isStarted = false;
                    isExp = false;
                    isNotVariable = false;
                    tmp += ch;
                    lastCh = ch;
                    ++i;
                    if(i < string.length()) {
                        ch = string.charAt(i);
                    }
                }
                else if (lastCh == ')')
                {
                    forAdd = new OperationToken('*');
                    tokens.add(forAdd);
                    isNotVariable = false;
                    tmp += ch;
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
            else if (ch == 'E')
            {
                if (!isExp && (lastCh >= '0' && lastCh <= '9' && isNotVariable ||
                        lastCh == '.'))
                {
                    if (lastCh == '.') {
                        tmp += '0';
                    }
                    tmp += ch;
                    lastCh = ch;
                    ++i;
                    if(i < string.length()) {
                        ch = string.charAt(i);
                    }
                    isExp = true;
                }
                else {
                    throw new IOException("Ошибка ввода EXP " + ch + " " + tmp + " " + lastCh);
                }
            }
            else if (ch == '+' ||
                    ch == '-' ||
                    ch == '*' ||
                    ch == '/' ||
                    ch == '^' ||
                    ch == '%' ||
                    ch == ')' ||
                    ch == ',')
            {
                if (lastCh >= '0' && lastCh <= '9' && isNotVariable ||
                        lastCh == '.' ||
                        lastCh == 'E' && ch != '-')
                {
                    if (lastCh == 'E') {
                        tmp += "1";
                    }
                    forAdd = new NumberToken(Double.parseDouble(tmp));
                    tokens.add(forAdd);
                    tmp = "";
                    isStarted = false;
                    isExp = false;
                    forAdd = new OperationToken(ch);
                    tokens.add(forAdd);
                    lastCh = ch;
                    ++i;
                    if(i < string.length()) {
                        ch = string.charAt(i);
                    }
                }
                else if (lastCh == 'E')
                {
                    tmp += ch;
                    lastCh = ch;
                    ++i;
                    if(i < string.length()) {
                        ch = string.charAt(i);
                    }
                }
                else if (lastCh >= 'a' && lastCh <= 'z' ||
                        lastCh >= 'A' && lastCh <= 'Z' && lastCh != 'E' ||
                        lastCh == '_' ||
                        lastCh >= '0' && lastCh <= '9')
                {
                    if (variables.get(tmp) != null) {
                        forAdd = new VariableToken(tmp);
                    }
                    else if (functions.get(tmp) != null) {
                        forAdd = new FunctionToken(tmp);
                    }
                    else {
                        throw new IOException("Ошибка ввода 3 " + ch + " " + tmp + " " + lastCh);
                    }
                    isNotVariable = true;
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
                else if (lastCh == '+' ||
                        lastCh == '-' ||
                        lastCh == '*' ||
                        lastCh == '/' ||
                        lastCh == '^' ||
                        lastCh == '%' ||
                        lastCh == ',' ||
                        lastCh == '>' ||
                        lastCh == '<' ||
                        lastCh == '=' ||
                        lastCh == '√' ||
                        lastCh == '!')
                {
                    throw new IOException("Ошибка ввода 4 " + ch  + " " + tmp + " " + lastCh);
                }
                else if (lastCh == '(')
                {
                    if (ch == '-'){
                        tmp = "0";
                        forAdd = new NumberToken(Double.valueOf(tmp));
                        tokens.add(forAdd);
                        tmp = "";
                        forAdd = new OperationToken('-');
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
                    isNotVariable = true;
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
                else if (lastCh == ')')
                {
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
            else if (ch == '>' ||
                    ch == '<' ||
                    ch == '=' ||
                    ch == '!')
            {
                if (lastCh >= '0' && lastCh <= '9' && isNotVariable ||
                        lastCh == '.' ||
                        lastCh == 'E')
                {
                    if (lastCh == 'E') {
                        tmp += "1";
                    }
                    forAdd = new NumberToken(Double.parseDouble(tmp));
                    tokens.add(forAdd);
                    tmp = "";
                    isStarted = true;
                    lastCh = ch;
                    ++i;
                    if(i < string.length()) {
                        ch = string.charAt(i);
                    }
                }
                else if (lastCh >= 'a' && lastCh <= 'z' ||
                        lastCh >= 'A' && lastCh <= 'Z' && lastCh != 'E' ||
                        lastCh == '_' ||
                        lastCh >= '0' && lastCh <= '9')
                {
                    if (variables.get(tmp) != null) {
                        forAdd = new VariableToken(tmp);
                    }
                    else if (functions.get(tmp) != null) {
                        forAdd = new FunctionToken(tmp);
                    }
                    else {
                        throw new IOException("Ошибка ввода 7 " + ch + " " + tmp + " " + lastCh);
                    }
                    isNotVariable = true;
                    tokens.add(forAdd);
                    tmp = "";
                    isStarted = true;
                    lastCh = ch;
                    ++i;
                    if(i < string.length()) {
                        ch = string.charAt(i);
                    }
                }
                else if (lastCh == '+' ||
                        lastCh == '-' ||
                        lastCh == '*' ||
                        lastCh == '/' ||
                        lastCh == '^' ||
                        lastCh == '%' ||
                        lastCh == '(' ||
                        lastCh == ',')
                {
                    throw new IOException("Ошибка ввода 8 " + ch  + " " + tmp + " " + lastCh);
                }
                else if (lastCh == ')')
                {
                    forAdd = new OperationToken(ch);
                    tokens.add(forAdd);
                    isStarted = true;
                    lastCh = ch;
                    ++i;
                    if(i < string.length()) {
                        ch = string.charAt(i);
                    }
                }
                else if (lastCh == '>' ||
                        lastCh == '<' ||
                        lastCh == '=' ||
                        lastCh == '!')
                {
                    if (ch == '>' ||
                            ch == '<' ||
                            ch == '!')
                    {
                        throw new IOException("Ошибка ввода 9 " + ch + " " + tmp + " " + lastCh);
                    }
                    else {
                        isStarted = false;
                        tmp += lastCh; tmp += ch;
                        forAdd = new BinaryToken(tmp);
                        tokens.add(forAdd);
                        tmp = "";
                        lastCh = ch;
                        ++i;
                        if(i < string.length()) {
                            ch = string.charAt(i);
                        }
                    }
                }
                else {
                    throw new IOException("Ошибка ввода 10 " + ch  + " " + tmp + " " + lastCh);
                }
            }
            else if (ch == '(')
            {
                if (lastCh >= '0' && lastCh <= '9' && isNotVariable ||
                        lastCh == '.' ||
                        lastCh == 'E')
                {
                    if (lastCh == 'E') {
                        tmp += "1";
                    }
                    forAdd = new NumberToken(Double.parseDouble(tmp));
                    tokens.add(forAdd);
                    tmp = "";
                    isStarted = false;
                    isExp = false;
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
                else if (lastCh >= 'a' && lastCh <= 'z' ||
                        lastCh >= 'A' && lastCh <= 'Z' && lastCh != 'E' ||
                        lastCh >= '0' && lastCh <= '9' ||
                        lastCh == '_')
                {
                    if (variables.get(tmp) != null) {
                        forAdd = new VariableToken(tmp);
                    }
                    else if (functions.get(tmp) != null) {
                        forAdd = new FunctionToken(tmp);
                    }
                    else {
                        throw new IOException("Ошибка ввода 11 " + ch + " " + tmp + " " + lastCh);
                    }
                    isNotVariable = true;
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
                else if (lastCh == '+' ||
                        lastCh == '-' ||
                        lastCh == '*' ||
                        lastCh == '/' ||
                        lastCh == '^' ||
                        lastCh == '%' ||
                        lastCh == '(' ||
                        lastCh == ',' ||
                        lastCh == '=' ||
                        lastCh == '√')
                {
                    forAdd = new OperationToken(ch);
                    tokens.add(forAdd);
                    lastCh = ch;
                    ++i;
                    if(i < string.length()) {
                        ch = string.charAt(i);
                    }
                }
                else if (lastCh == '>' ||
                        lastCh == '<')
                {
                    tmp += lastCh;
                    forAdd = new BinaryToken(tmp);
                    tokens.add(forAdd);
                    tmp = "";
                    isStarted = false;
                    isExp = false;
                    forAdd = new OperationToken(ch);
                    tokens.add(forAdd);
                    lastCh = ch;
                    ++i;
                    if(i < string.length()) {
                        ch = string.charAt(i);
                    }
                }
                else if (lastCh == ')')
                {
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
                    throw new IOException("Ошибка ввода 12 " + ch  + " " + tmp + " " + lastCh);
                }
            }
            else if (ch == '.')
            {
                if (lastCh >= '0' && lastCh <= '9' && isNotVariable && !isStarted)
                {
                    tmp += ch;
                    lastCh = ch;
                    ++i;
                    if(i < string.length()) {
                        ch = string.charAt(i);
                    }
                    isStarted = true;
                }
                else if (lastCh >= '0' && lastCh <= '9' ||
                        lastCh >= 'a' && lastCh <= 'z' ||
                        lastCh >= 'A' && lastCh <= 'Z' && lastCh != 'E' ||
                        lastCh == '.' ||
                        lastCh == '_')
                {
                    throw new IOException("Ошибка ввода 13 " + ch  + " " + tmp + " " + lastCh);
                }
                else if (lastCh == '+' ||
                        lastCh == '-' ||
                        lastCh == '*' ||
                        lastCh == '/' ||
                        lastCh == '^' ||
                        lastCh == '%' ||
                        lastCh == '(' ||
                        lastCh == ',' ||
                        lastCh == '=')
                {
                    tmp += "0.";
                    isStarted = true;
                    lastCh = ch;
                    ++i;
                    if(i < string.length()) {
                        ch = string.charAt(i);
                    }
                }
                else if (lastCh == '>' ||
                        lastCh == '<')
                {
                    tmp += lastCh;
                    forAdd = new BinaryToken(tmp);
                    tokens.add(forAdd);
                    tmp = "0.";
                    lastCh = ch;
                    ++i;
                    if(i < string.length()) {
                        ch = string.charAt(i);
                    }
                }
                else if (lastCh == ')')
                {
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
                    throw new IOException("Ошибка ввода 14 " + ch  + " " + tmp + " " + lastCh);
                }
            }
            else if (ch == '√')
            {
                if (lastCh >= '0' && lastCh <= '9' && isNotVariable ||
                        lastCh == '.' ||
                        lastCh == 'E')
                {
                    if (lastCh == 'E') {
                        tmp += "1";
                    }
                    forAdd = new NumberToken(Double.parseDouble(tmp));
                    tokens.add(forAdd);
                    tmp = "";
                    isStarted = false;
                    isExp = false;
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
                else if (lastCh >= 'a' && lastCh <= 'z' ||
                        lastCh >= 'A' && lastCh <= 'Z' && lastCh != 'E' ||
                        lastCh >= '0' && lastCh <= '9' ||
                        lastCh == '_')
                {
                    if (variables.get(tmp) != null) {
                        forAdd = new VariableToken(tmp);
                    }
                    else if (functions.get(tmp) != null) {
                        forAdd = new FunctionToken(tmp);
                    }
                    else {
                        throw new IOException("Ошибка ввода 15 " + ch + " " + tmp + " " + lastCh);
                    }
                    isNotVariable = true;
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
                else if (lastCh == '+' ||
                        lastCh == '-' ||
                        lastCh == '*' ||
                        lastCh == '/' ||
                        lastCh == '^' ||
                        lastCh == '%' ||
                        lastCh == '(' ||
                        lastCh == ',' ||
                        lastCh == ')' ||
                        lastCh == '=' ||
                        lastCh == '√' && i == 0)
                {
                    forAdd = new OperationToken(ch);
                    tokens.add(forAdd);
                    lastCh = ch;
                    ++i;
                    if(i < string.length()) {
                        ch = string.charAt(i);
                    }
                }
                else if (lastCh == '>' ||
                        lastCh == '<')
                {
                    tmp += lastCh;
                    forAdd = new BinaryToken(tmp);
                    tokens.add(forAdd);
                    tmp = "";
                    isStarted = false;
                    isExp = false;
                    forAdd = new OperationToken(ch);
                    tokens.add(forAdd);
                    lastCh = ch;
                    ++i;
                    if(i < string.length()) {
                        ch = string.charAt(i);
                    }
                }
                else {
                    throw new IOException("Ошибка ввода 16 " + ch  + " " + tmp + " " + lastCh);
                }
            }
            else throw new IOException("Ошибка ввода Незнакомый символ" + ch + " " + tmp + " " + lastCh);
        }

        if (lastCh >= '0' && lastCh <= '9' && isNotVariable ||
                lastCh == '.' ||
                lastCh == 'E')
        {
            if (lastCh == 'E') {
                tmp += "1";
            }
            forAdd = new NumberToken(Double.parseDouble(tmp));
            tokens.add(forAdd);
        }
        else if (lastCh >= 'a' && lastCh <= 'z' ||
                lastCh >= 'A' && lastCh <= 'Z' && lastCh != 'E' ||
                lastCh >= '0' && lastCh <= '9' ||
                lastCh == '_')
        {
            if (variables.get(tmp) != null) {
                forAdd = new VariableToken(tmp);
            }
            else if (functions.get(tmp) != null) {
                forAdd = new FunctionToken(tmp);
            }
            else {
                throw new IOException("Ошибка ввода 17" + ch + " " + tmp + " " + lastCh);
            }
            tokens.add(forAdd);
        }
        else if (lastCh != ')') {
            //throw new IOException("Ошибка ввода 18 " + ch  + " " + tmp + " " + lastCh);
            tokens.remove(tokens.size() - 1);
        }

        tokens.add(new EmptyToken());
        return tokens;
    }

    private final char[][] ActionsTable = {
         // \0  +  -  *  /  (  )  P  ^  F  ,  %  √  <>
            {4, 2, 2, 2, 2, 2, 6, 1, 2, 2, 9, 2, 2, 2}, //\0
            {3, 3, 3, 2, 2, 2, 3, 1, 2, 2, 9, 2, 2, 3}, //+
            {3, 3, 3, 2, 2, 2, 3, 1, 2, 2, 9, 2, 2, 3}, //-
            {3, 3, 3, 3, 3, 2, 3, 1, 2, 2, 9, 3, 2, 3}, //*
            {3, 3, 3, 3, 3, 2, 3, 1, 2, 2, 9, 3, 2, 3}, ///
            {7, 2, 2, 2, 2, 2, 5, 1, 2, 2, 10,2, 2, 2}, //(
            {3, 3, 3, 3, 3, 2, 3, 1, 3, 2, 9, 3, 2, 3}, //^
            {3, 3, 3, 3, 3, 2, 3, 8, 3, 8, 9, 3, 8, 3}, //F
            {3, 3, 3, 3, 3, 2, 3, 1, 2, 2, 9, 3, 2, 3}, //%
            {3, 3, 3, 3, 3, 2, 3, 8, 3, 8, 9, 3, 8, 3}, //√
            {3, 2, 2, 2, 2, 2, 3, 1, 2, 2, 9, 2, 2, 3}, //<, >, =, !
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
            case '<': return 10;
            case '>': return 10;
            case '=': return 10;
            case '!': return 10;
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
            case '<': return 13;
            case '>': return 13;
            case '=': return 13;
            case '!': return 13;
        }
        switch (token.getType()){
            case 1: return 7;
            case 4: return 7;
            default: return 9;
        }
    }

    private ArrayList<Token> infixToPostfix(ArrayList<Token> intokens) throws BraketsOpenException,
            BraketsCloseException, BraketsFunctionException, ExtraCommaException{
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
                case 6: throw new BraketsOpenException(i);
                case 7: throw new BraketsCloseException(i);
                case 8: throw new BraketsFunctionException(i);
                case 9: throw new ExtraCommaException("There is an extra comma in tokens at position " + i);
                case 10: ++i;
            }
        } while (action != 4);
        return outtokens;
    }

    private double calcPostfix(ArrayList<Token> tokens) throws IncorrectPostfixException, ExtraVariableException{
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
                    IFunction foo;
                    if (functions.get(tmp.getValue()) != null) {
                        foo = functions.get(tmp.getValue());
                    } else throw new IncorrectPostfixException("Function was not found");
                    int numberOfVariables = foo.getNumberOfArgs();
                    tmps = new double[numberOfVariables];
                    try {
                        for (int j = 0; j < numberOfVariables; ++j) {
                            tmps[numberOfVariables - 1 - j] = Double.parseDouble(S.pop().getValue());
                        }
                    } catch (EmptyStackException e) {
                        throw new IncorrectPostfixException("There are not enough variables for function in stack");
                    }
                    S.push(new NumberToken(foo.Calculate(tmps)));
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
                    if (S.size() == 1){
                        return Double.parseDouble(S.peek().getValue());
                    }
                    else {
                        throw new ExtraVariableException("There are " + (S.size() - 1) + " extra arguments in Stack");
                    }
                case 6:
                    tmps = new double[2];
                    tmps[1] = Double.parseDouble(S.pop().getValue());
                    switch (tmp.getValue().charAt(0)) {
                        case '<':
                            if (tmp.getValue().length() == 1){
                                if (tmps[0] < tmps[1]){
                                    S.push(new NumberToken(1.0));
                                } else {
                                    S.push(new NumberToken(0.0));
                                }
                            } else {
                                if (tmps[0] <= tmps[1]){
                                    S.push(new NumberToken(1.0));
                                } else {
                                    S.push(new NumberToken(0.0));
                                }
                            }
                            break;
                        case '>':
                            if (tmp.getValue().length() == 1){
                                if (tmps[0] > tmps[1]){
                                    S.push(new NumberToken(1.0));
                                } else {
                                    S.push(new NumberToken(0.0));
                                }
                            } else {
                                if (tmps[0] >= tmps[1]){
                                    S.push(new NumberToken(1.0));
                                } else {
                                    S.push(new NumberToken(0.0));
                                }
                            }
                            break;
                        case '!':
                            if (tmps[0] != tmps[1]){
                                S.push(new NumberToken(1.0));
                            } else {
                                S.push(new NumberToken(0.0));
                            }
                            break;
                        case '=':
                            if (tmps[0] == tmps[1]){
                                S.push(new NumberToken(1.0));
                            } else {
                                S.push(new NumberToken(0.0));
                            }
                            break;
                    }
            }
        }
        throw new IncorrectPostfixException("Something wrong in 3th function");
    }

    class BraketsOpenException extends Exception{
        public BraketsOpenException(){}
        BraketsOpenException(int i) {
            super("There is no ( for ) in tokens at position " + i);
        }
    }

    class BraketsCloseException extends Exception{
        public BraketsCloseException(){}
        BraketsCloseException(int i) {
            super("There is no ) for ( in tokens at position " + i);
        }
    }

    class BraketsFunctionException extends Exception{
        public BraketsFunctionException(){}
        BraketsFunctionException(int i) {
            super("Error with brackets after function in tokens at position " + i);
        }
    }

    class ExtraCommaException extends Exception{
        public ExtraCommaException(){}
        ExtraCommaException(String S) {
            super(S);
        }
    }

    class ExtraVariableException extends Exception{
        public ExtraVariableException(){}
        ExtraVariableException(String S) {
            super(S);
        }
    }

    class IncorrectPostfixException extends Exception{
        public IncorrectPostfixException(){}
        IncorrectPostfixException(String S) {
            super(S);
        }
    }

    public double calc(String string, Map<String, Variable> _variables, Map<String, IFunction> _functions)
            throws IOException, BraketsFunctionException, BraketsOpenException, BraketsCloseException,
            ExtraCommaException, IncorrectPostfixException, ExtraVariableException {
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