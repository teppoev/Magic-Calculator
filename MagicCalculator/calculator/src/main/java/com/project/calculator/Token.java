package com.example.dynamicbuttons;

public abstract class Token {
    abstract public int getType();
    abstract public String getValue();
}

class NumberToken extends Token{
    private double value;
    NumberToken(double _value) {
        value = _value;
    }
    public String getValue() {
        return String.valueOf(value);
    }
    public int getType() {
        return 1;
    }
}

class OperationToken extends Token{
    private char value;
    OperationToken(char _value) {
        value = _value;
    }
    public String getValue() {
        return String.valueOf(value);
    }
    public int getType() {
        return 2;
    }
}

class FunctionToken extends Token{
    private String value;
    FunctionToken(String _value) {
        value = _value;
    }
    public String getValue() {
        return value;
    }
    public int getType() {
        return 3;
    }
}

class VariableToken extends Token{
    private String value;
    VariableToken(String _value) {
        value = _value;
    }
    public String getValue() {
        return value;
    }
    public int getType() {
        return 4;
    }
}

class EmptyToken extends Token{
    EmptyToken(){}
    public String getValue() {
        return "\0"; }
    public int getType() {
        return 5; }
}

class CommaToken extends  Token{
    public CommaToken() {}
    public String getValue() {
        return ","; }
    public int getType() {
        return 6; }
}