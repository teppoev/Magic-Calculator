package com.project.calculator;

import java.io.Serializable;

public class Variable implements Serializable{
    private double value;
    public Variable() {}
    public Variable(double _value) { value = _value; }
    public double getValue() {
        return value;
    }
    public void setValue(double _value) {
        value = _value;
    }
}
