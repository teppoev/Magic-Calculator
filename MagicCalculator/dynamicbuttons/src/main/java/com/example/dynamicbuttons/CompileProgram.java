package com.example.dynamicbuttons;

import java.util.Map;

public class CompileProgram implements IFunction{

    private Calculator calculator;
    private String program;
    private Map<String, IFunction> functions;
    private Map<String, Variable> variables;

    private int paramsNum;

    public CompileProgram(String _prog, Calculator _calc, Map<String, IFunction> _func)
    {
        calculator = _calc;
        program = _prog;
        functions = _func;
        Compile();
    }

    private void Compile() {
        
    }

    private interface IExpression {
        IExpression Next();
    };

    private class OperationExpression implements IExpression{

        private String expression;

        @Override
        public IExpression Next() {
            return null;
        }
    };

    @Override
    public double Calculate(double[] params) {
        return 0.0;
    }
}















