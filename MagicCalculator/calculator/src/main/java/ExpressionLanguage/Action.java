package ExpressionLanguage;

import java.io.Serializable;

public abstract class Action implements IAction, Serializable {
    protected ActionWithBody parent;
    private int lineNumber = 0;

    public void SetLineNumber(int lineNum) {
        lineNumber = lineNum;
    }

    public int GetLineNumber() {
        return lineNumber;
    }

    public void SetParent(ActionWithBody parent) {
        this.parent = parent;
    }

    ActionWithBody GetParent() {
        return parent;
    }
}

