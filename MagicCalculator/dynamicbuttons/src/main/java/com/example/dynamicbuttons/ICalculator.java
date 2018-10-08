package com.example.dynamicbuttons;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public interface ICalculator {
    public double Calculate (String string, Map<String,Variable> variables, Map<String, Function> functions)
            throws IOException, Calculator.BraketsFunctionException, Calculator.BraketsCloseException, Calculator.BraketsOpenException, Calculator.ExtraCommaException, Calculator.IncorrectPostfixException;
    public double Calculate (ArrayList<Token> tokens, Map<String,Variable> variables, Map<String, Function> functions)
            throws IOException, Calculator.BraketsFunctionException, Calculator.BraketsCloseException, Calculator.BraketsOpenException, Calculator.ExtraCommaException, Calculator.IncorrectPostfixException;
}
