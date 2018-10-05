package ru.spbu.twothreeone;

public interface Token {
    public int getType();
    public String getValue();
}

class NumberToken implements Token{
    private double value;
    private final int type = 1;
    public NumberToken(double _value) {
        value = _value;
    }
    public String getValue() {
        return String.valueOf(value);
    }
    public int getType() {
        return type;
    }
}

class OperationToken implements Token{
    private char value;
    private final int type = 2;
    public OperationToken(char _value) {
        value = _value;
    }
    public String getValue() {
        return String.valueOf(value);
    }
    public int getType() {
        return type;
    }
}

class FunctionToken implements Token{
    private String value;
    private final int type = 3;
    public FunctionToken(String _value) {
        value = _value;
    }
    public String getValue() {
        return value;
    }
    public int getType() {
        return type;
    }
}

class VariableToken implements Token{
    private String value;
    private final int type = 4;
    public VariableToken(String _value) {
        value = _value;
    }
    public String getValue() {
        return value;
    }
    public int getType() {
        return type;
    }
}

class EmptyToken implements Token{
    private final String value = "\0";
    private final int type = 5;
    public EmptyToken(){}
    public String getValue() {return value; }
    public int getType() { return type; }
}

class CommaToken implements  Token{
    private final String value = ",";
    private final int type = 6;
    public CommaToken() {}
    public String getValue() {return value; }
    public int getType() {return type; }
}