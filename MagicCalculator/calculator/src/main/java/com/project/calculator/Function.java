package com.example.dynamicbuttons;

public class Function {
    private String code; //or file instead String
    private int numberOfVariables = 1;
    Function() {}
    Function(String _text, int _num) {
        code = _text;
        numberOfVariables = _num;
    }

    int getNumberOfVariables() {
        return numberOfVariables;
    }
    public void setNumberOfVariables(int _num) {
        numberOfVariables = _num;
    }

    double calc(double[] params) {
        double result = 1.;
        //There should execute code with variable giving result
        return result;
    }
}
