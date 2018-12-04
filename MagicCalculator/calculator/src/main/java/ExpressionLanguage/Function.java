package ExpressionLanguage;

import android.util.Log;

import com.project.calculator.Variable;

import java.util.Map;

public class Function implements IFunction {

    private ActionWithBody main;
    private Variable[] variables;
    private Map<String, IFunction> functions;
    private Variable result;

    private int numberOfVariables;

    public Function(int _num, ActionWithBody _main) {
        numberOfVariables = _num;
        main = _main;

        variables = new Variable[numberOfVariables];
        for(int i = 0; i < numberOfVariables; i++) {
            variables[i] = new Variable(0.0);
            String name = "arg" + Integer.toString(i + 1);

            Log.d("MyTag", name);

            main.AddLocalVariable(name, variables[i]);
        }

        result = new Variable(0.0);
        main.AddLocalVariable("result", result);
    }

    @Override
    public int getNumberOfArgs() {
        return numberOfVariables;
    }
    public void setNumberOfVariables(int _num) {
        numberOfVariables = _num;
    }

    @Override
    public double Calculate(double[] params) {

        if(params.length != variables.length) {
            throw new Error("Incorrect number of params");
        }

        for(int i = 0; i < params.length; i++) {
            variables[i].setValue(params[i]);
        }

        main.Do();

        double answer = result.getValue();

        result.setValue(0.0);

        return answer;
    }
}