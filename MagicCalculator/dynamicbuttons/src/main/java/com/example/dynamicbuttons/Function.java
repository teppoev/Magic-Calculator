package com.example.dynamicbuttons;

public class Function implements IFunction {
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

    public double Calculate (double[] params) {
        double result = 1.;
        //There should execute code with variable giving result
        return result;
    }
}
