package com.project.calculator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public interface ICalculator {
    public double calc (String string, Map<String,Variable> variables, Map<String, IFunction> functions)
            throws IOException, Calculator.BraketsFunctionException, Calculator.BraketsCloseException, Calculator.BraketsOpenException, Calculator.ExtraCommaException, Calculator.IncorrectPostfixException;
}
