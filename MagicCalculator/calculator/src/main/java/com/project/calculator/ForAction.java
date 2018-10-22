package com.project.calculator;


import android.util.Log;

public class ForAction extends ActionWithBody {

    private String leftName;

    private String rightName;

    private String iterName;

    ForAction(String _l, String _r, String _it) {
        super();
        leftName = _l;
        rightName = _r;
        iterName = _it;

        if(this.IsDefinedVariable(iterName)) {
            throw new Error("Defined iterator name");
        }
        variables.put(iterName, new Variable(0.0));
    }

    @Override
    public void Do() {
        Variable left;
        Variable right;

        if(UserProgramCompiler.TokenType.GetTypeOf(leftName) == UserProgramCompiler.TokenType.VarToken) {
            Log.d("MyTag", "Left border is var");
            left = this.GetLocalVariable(leftName);
        } else if (UserProgramCompiler.TokenType.GetTypeOf(leftName) == UserProgramCompiler.TokenType.NumToken) {
            Log.d("MyTag", "Left border is num");
            left = new Variable(Double.parseDouble(leftName));
        } else {
            throw new Error("ForAction:Do:left is not var or number");
        }

        if(UserProgramCompiler.TokenType.GetTypeOf(rightName) == UserProgramCompiler.TokenType.VarToken) {
            right = this.GetLocalVariable(rightName);
        } else if (UserProgramCompiler.TokenType.GetTypeOf(rightName) == UserProgramCompiler.TokenType.NumToken) {
            right = new Variable(Double.parseDouble(rightName));
        } else {
            throw new Error("ForAction:Do:right is not var or number");
        }

        Log.d("MyTag", "Left border of range: " + left.getValue());
        Log.d("MyTag", "Right border of range: " + right.getValue());

        Variable iter = this.GetLocalVariable(iterName);
        for(iter.setValue(left.getValue());
            iter.getValue() <= right.getValue();
            iter.setValue(iter.getValue() + 1.0))
        {

            Log.d("MyTag", "Do: 48 line in ForAction: go to DO");
            for(IAction action : actions) {
                action.Do();

            }
            variables.clear();
            variables.put(iterName, iter);
        }
    }
}
