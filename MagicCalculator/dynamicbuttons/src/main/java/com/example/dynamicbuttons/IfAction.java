package com.example.dynamicbuttons;

public class IfAction extends ActionWithBody {

    private ActionWithBody elseAction;

    private IFunction cmp;

    private String leftName;

    private String rightName;

    IfAction(String _l, String _r, IFunction _cmp) {
        leftName = _l;
        rightName = _r;
        cmp = _cmp;
        elseAction = null;
    }

    @Override
    public void Do() {
        Variable left = this.GetLocalVariable(leftName);
        Variable right = this.GetLocalVariable(rightName);

        double conditionValue = cmp.Calculate(new double[] {left.getValue(), right.getValue()});
        if(conditionValue == 1.0) {
            for(IAction action: actions) {
                action.Do();
            }
        }
        else if (conditionValue == 0.0) {
            for(IAction action: actions) {
                elseAction.Do();
            }
        }
        else {
            throw new Error("IfAction:Do:cmp returns incorrect value");
        }
        variables.clear();
    }

    public void SetElseAction(ActionWithBody _elseAction) {
        elseAction = _elseAction;
    }
}
