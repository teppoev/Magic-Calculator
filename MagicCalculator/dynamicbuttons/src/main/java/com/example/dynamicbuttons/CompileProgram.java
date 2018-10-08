package com.example.dynamicbuttons;

import java.util.Map;

public class CompileProgram implements IFunction{

    private Calculator calculator;
    private String program;
    private Map<String, IFunction> funcMap;
    private Map<String, Variable> varMap;

    private int paramsNum;

    public CompileProgram
            (String _prog,
             Calculator _calc,
             Map<String, IFunction> _func,
             int _paramsNum)
    {
        calculator = _calc;
        program = _prog;
        funcMap = _func;
        paramsNum = _paramsNum;

        for(int i = 0; i < paramsNum; ++i) {
            varMap.put("input" + Integer.toString(i + 1), new Variable(0.0));
        }

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















