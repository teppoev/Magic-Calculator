package com.example.dynamicbuttons;

import java.util.HashMap;
import java.util.Map;

public class AssignmentAction extends Action {

    private String varName;
    private String expression;

    private Calculator calculator;

    private Map<String, IFunction> functions;

    AssignmentAction(String _var, String _exp, Map<String, IFunction> _func) {
        varName = _var;
        expression = _exp;
        functions = _func;

        calculator = new Calculator();
    }

    @Override
    public void Do() {
        Variable variable = parent.GetLocalVariable(varName);

        if(variable != null) {
            Map<String, Variable> localVariables = new HashMap<>();
            parent.GetAllLocalVariables(localVariables);
            variable.setValue(calculator.calc(expression, localVariables, functions));
        }
        else {
            throw new Error("AssignmentAction:Do:Indefined variable");
        }
    }
}
