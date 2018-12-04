package ExpressionLanguage;

import com.project.calculator.Variable;

public class NewAction extends Action {
    String name;

    NewAction(String _name) {
        name = _name;
    }

    @Override
    public void Do() {
        parent.AddLocalVariable(name, new Variable(0.0));
    }
}
