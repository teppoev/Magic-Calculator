package com.example.dynamicbuttons;

public abstract class Action implements IAction {
    protected ActionWithBody parent;

    public void SetParent(ActionWithBody parent) {
        this.parent = parent;
    }

    ActionWithBody GetParent() {
        return parent;
    }
}

