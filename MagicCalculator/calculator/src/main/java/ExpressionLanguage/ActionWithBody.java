package ExpressionLanguage;

import com.project.calculator.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ActionWithBody extends Action {

    protected List<IAction> actions;
    protected Map<String, Variable> variables;

    ActionWithBody() {
        actions = new ArrayList<>();
        variables = new HashMap<>();
    }

    public void AddAction(IAction action) {
        actions.add(action);
    }

    public Variable GetLocalVariable(String name) {
        if(variables.containsKey(name)) {
            return variables.get(name);
        }
        else {
            if (parent != null) {
                return parent.GetLocalVariable(name);
            }
            else {
                return null;
            }
        }
    }

    public void GetAllLocalVariables(Map<String, Variable> container) {
        container.putAll(variables);
        if(parent != null) {
            parent.GetAllLocalVariables(container);
        }
    }

    public boolean IsDefinedVariable(String name) {
        if (variables.containsKey(name)) {
            return true;
        }
        if (parent != null) {
            if (parent.IsDefinedVariable(name)) {
                return true;
            }
        }
        return false;
    }

    public void AddLocalVariable(String name, Variable variable) {
        if(IsDefinedVariable(name)) {
            throw new Error("AddLocalVariable: this variable is already defined");
        }
        variables.put(name, variable);
    }
}
