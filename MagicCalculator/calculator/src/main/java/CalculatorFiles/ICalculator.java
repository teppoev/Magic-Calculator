package CalculatorFiles;

import com.project.calculator.Variable;

import java.io.IOException;
import java.util.Map;

import ExpressionLanguage.IFunction;

public interface ICalculator {
    double calc (String string, Map<String, Variable> variables, Map<String, IFunction> functions)
            throws IOException, Calculator.BraketsFunctionException,
            Calculator.BraketsCloseException, Calculator.BraketsOpenException,
            Calculator.ExtraCommaException, Calculator.IncorrectPostfixException,
            Calculator.ExtraVariableException;
}
