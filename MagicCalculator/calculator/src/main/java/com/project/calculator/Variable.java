package com.project.calculator;

public class Variable {
    private double value;
    public Variable() {}
    Variable(double _value) { value = _value; }
    double getValue() {
        return value;
    }
    public void setValue(double _value) {
        value = _value;
    }
}
