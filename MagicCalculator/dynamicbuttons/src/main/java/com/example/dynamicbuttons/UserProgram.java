package com.example.dynamicbuttons;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserProgram implements IFunction{

    UserProgram(String program) {
        Compile(program);
    }

    private Calculator calculator;
    private Map<String, IFunction> functions;

    enum TokenType {

        ForToken,
        IfToken,
        InToken,
        ElseToken,
        NewToken,
        EqualToken,

        RangeToken,
        VarToken,
        NumToken,
        MathToken;

        private static String VarTokenRegEx = "([A-Za-z_])([\\w\\d])*";
        private static String NumberTokenRegEx = "((\\d)+)|((\\d)+\\.(\\d)+)";

        static public TokenType GetTypeOf(String word) {

            if(word == "for") return ForToken;
            if(word == "if") return IfToken;
            if(word == "in") return InToken;
            if(word == "else") return ElseToken;
            if(word == "new") return NewToken;
            if(word == "=") return EqualToken;
            if(word == "...") return RangeToken;

            if(word.matches(VarTokenRegEx)) return VarToken;
            if(word.matches(NumberTokenRegEx)) return NumToken;

            return null;
        }
    }


    private boolean IsInteger(double number) {
        return Math.ceil(number) == Math.floor(number);
    }


    private interface IAction {
        void Do();
        void AddAction(IAction action);
        BodyAction GetParent();
    }

    private abstract class Action implements IAction {

        protected BodyAction parent;

        @Override
        public BodyAction GetParent() {
            return parent;
        }

        public void setParent(BodyAction parent) {
            this.parent = parent;
        }
    }

    private class BodyAction extends Action {
        private List<IAction> actions;
        private Map<String, Variable> variables;

        BodyAction(BodyAction _parent) {
            parent = _parent;
            variables = parent.GetLocalVariables();
        }

        @Override
        public void Do() {
            for (IAction action : actions) {
                action.Do();
            }
        }

        @Override
        public void AddAction(IAction action) {
            actions.add(action);
        }

        public void AddVariable(String name, Variable _var) {
            if(!variables.containsKey(name)) {
                variables.put(name, _var);
            }
            else {
                throw new Error("The variable with the same name also exists: " + name);
            }
        }

        public void DeleteLocalVariables() {
            variables = parent.GetLocalVariables();
        }

        Map<String, Variable> GetLocalVariables() {
            return variables;
        }
    }

    private class ForAction extends Action {

        private BodyAction body;

        private String iteratorName;
        private Variable iterator;

        private Variable left;
        private Variable right;

        ForAction(String _itName, Variable _left, Variable _right, BodyAction _parent) {
            super.setParent(_parent);

            left = _left;
            right = _right;
            iteratorName = _itName;


            if(!IsInteger(left.getValue()) || !IsInteger(right.getValue())) {
                throw new InvalidParameterException("left or right border of range is not integer");
            }

            iterator = new Variable(left.getValue());
        }

        @Override
        public void Do() {

            for(iterator.getValue();
                iterator.getValue() <= right.getValue();
                iterator.setValue(iterator.getValue() + 1.0))
            {
                body.DeleteLocalVariables();
                body.AddVariable(iteratorName, iterator);
            }
        }

        @Override
        public void AddAction(IAction action) {
            body.AddAction(action);
        }
    }

    private class IfAction extends Action {

        boolean IsIfBodyEnds;

        private BodyAction ifBody;
        private BodyAction elseBody;
        private Variable left;
        private Variable right;
        private IFunction cmp;

        IfAction(Variable _left, Variable _right, IFunction _cmp, BodyAction _parent) {
            super.setParent(_parent);

            left = _left;
            right = _right;
            cmp = _cmp;

            ifBody = new BodyAction(parent);
        }

        @Override
        public void Do() {
            double[] params = new double[] {left.getValue(), right.getValue()};
            double cmpResult = cmp.Calculate(params);
            if(!IsInteger(cmpResult) || (cmpResult != 0.0 && cmpResult != 1.0)) {
                throw new Error("Cmp function returns value which is not belongs to {0, 1}");
            }
            if(cmpResult == 1.0) {
                ifBody.Do();
            }
            else {
                elseBody.Do();
            }
        }

        @Override
        public void AddAction(IAction action) {
            if(!IsIfBodyEnds) {
                ifBody.AddAction(action);
            }
            else {
                if(elseBody != null) {
                    elseBody.AddAction(action);
                }
            }
        }

        public void EndOfIfBody() {
            IsIfBodyEnds = false;
        }
    }

    private class NewAction extends Action {

        String name;

        NewAction(String newVarName, BodyAction _parent) {
            super.setParent(_parent);
        }

        @Override
        public void Do() {
            Variable newVar = new Variable(0.0);
            parent.AddVariable(name, newVar);
        }

        @Override
        public void AddAction(IAction action) {
            parent.AddAction(action);
        }
    }

    private class AssignmentAction extends Action {

        private Variable variable;
        private String expression;

        AssignmentAction(Variable _var, String _exp, BodyAction _parent) {
            super.setParent(_parent);

            variable = _var;
            expression = _exp;
        }

        @Override
        public void Do() {
            variable.setValue(calculator.calc(expression, parent.GetLocalVariables(), functions));
        }

        @Override
        public void AddAction(IAction action) {
            parent.AddAction(action);
        }
    }

    static private class Token {

        private TokenType type;

        private String word;

        public Token(TokenType _type, String _word) {
            type = _type;
            word = _word;
        }

        public TokenType GetType() {
            return type;
        }

        public String GetWord() {
            return word;
        }
    }

    private void Compile(String program) {
        String[] lines = program.split("\n");
        for(String line: lines) {
            int level = GetNestingLevel(line);
            List<Token> tokenLine = TokenizeLine(line);

            line.substring(level);

            CompileTokenLine(tokenLine, level);
        }
    }

    private int GetNestingLevel(String line) {
        int level;
        for(level = 0; line.charAt(level) == '\t'; level++) { }
        return level;
    }

    private List<Token> TokenizeLine(String line) {
        String[] specialSymbols = new String[] { "+", "-", "*", "/", "(", ")", ">", "<", "=", "..." };
        for(String smbl : specialSymbols) {
            line.replaceAll(smbl, " " + smbl + " ");
        }

        String[] words = line.split("\\s");
        List<Token> tokenList = new ArrayList<Token>();

        for(String word : words) {
            tokenList.add(new Token(TokenType.GetTypeOf(word), word));
        }
        return tokenList;
    }

    private void CompileTokenLine(List<Token> line, int level) {

        IFunction Sin = new IFunction() {
            @Override
            public double Calculate(double[] params) {
                return Math.sin(params[0]);
            }
        };

    }

    @Override
    public double Calculate(double[] params) {
        return 0.0;
    }
}















