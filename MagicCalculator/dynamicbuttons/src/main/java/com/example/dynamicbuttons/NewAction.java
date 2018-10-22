package com.example.dynamicbuttons;

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
