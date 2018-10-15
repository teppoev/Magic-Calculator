package com.example.dynamicbuttons;


public class ForAction extends ActionWithBody {

    private String leftName;

    private String rightName;

    private String iterName;

    ForAction(String _l, String _r, String _it) {
        leftName = _l;
        rightName = _r;
        iterName = _it;
    }

    @Override
    public void Do() {
        Variable left;
        Variable right;

        if(UserProgramCompiler.TokenType.GetTypeOf(leftName) == UserProgramCompiler.TokenType.VarToken) {
            left = this.GetLocalVariable(leftName);
        } else if (UserProgramCompiler.TokenType.GetTypeOf(leftName) == UserProgramCompiler.TokenType.NumToken) {
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

        for(Variable iter = this.GetLocalVariable(iterName);
            iter.getValue() < right.getValue();
            iter.setValue(iter.getValue() + 1.0))
        {
            for(IAction action : actions) {
                action.Do();
            }
            variables.clear();
            variables.put(iterName, iter);
        }
    }
}
