package com.example.dynamicbuttons;

public class SimpleBodyAction extends ActionWithBody {
    @Override
    public void Do() {
        for(IAction action: actions) {
            action.Do();
        }
    }
}
