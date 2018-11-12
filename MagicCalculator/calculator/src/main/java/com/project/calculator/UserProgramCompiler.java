package com.project.calculator;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Stack;

public class UserProgramCompiler {

    UserProgramCompiler() {
        calculator = new Calculator();
        functions = new HashMap<>();
        actionsStack = new Stack<>();
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
        CompareToken,

        RangeToken,
        VarToken,
        NumToken,
        MathToken;

        private static String VarTokenRegEx = "[a-zA-Z_]([a-zA-Z0-9_])*";
        private static String NumberTokenRegEx = "(\\d+)|(\\d+\\.\\d+)";

        static public TokenType GetTypeOf(String word) {

            if(word.equals("for")) return ForToken;
            if(word.equals("if")) return IfToken;
            if(word.equals("in")) return InToken;
            if(word.equals("else")) return ElseToken;
            if(word.equals("new")) return NewToken;
            if(word.equals("=")) return EqualToken;
            if(word.equals("...")) return RangeToken;
            if(word.equals("<") || word.equals(">") || word.equals("=")) return CompareToken;
            if(word.equals("*") || word.equals("/") || word.equals("+") || word.equals("-") ||
                    word.equals("^")) return MathToken;

            if(word.matches(VarTokenRegEx)) return VarToken;
            if(word.matches(NumberTokenRegEx)) return NumToken;

            return null;
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

    public IFunction Compile(String program, int argNum) {

        actionsStack.clear();
        currentNestingLevel = 1;

        ActionWithBody main = new SimpleBodyAction();
        actionsStack.add(main);

        String[] lines = program.split("\n");
        for(int i = 0; i < lines.length; i++) {
            String line = lines[i];
            int level = GetNestingLevel(line);
            List<Token> tokenLine = TokenizeLine(line);

            line.substring(level - 1);

            IfAction lastIfAction = null;
            if(level > currentNestingLevel) {
                ActionWithBody action = new SimpleBodyAction();
                action.SetParent(actionsStack.peek());
                action.SetLineNumber(i + 1);
                actionsStack.add(action);
                currentNestingLevel++;
            }
            else {
                Log.d("MyTag", "Compile: current level is " + Integer.toString(level) + " " + line);
                while(level < currentNestingLevel) {
                    if(actionsStack.peek().getClass() == IfAction.class) {
                        lastIfAction = (IfAction) actionsStack.pop();
                    }
                    else {
                        lastIfAction = null;
                    }
                    currentNestingLevel--;
                }
            }

            CompileTokenLine(tokenLine, i + 1, lastIfAction);
        }

        return new Function(argNum, main);
    }

    private int GetNestingLevel(String line) {
        int level;
        line = line.replaceAll("  ", "\t");
        for(level = 0; level < line.length() && line.charAt(level) == '\t'; level++) {
        }
        return level + 1;
    }

    private IFunction GetCmp(Token t) {
        switch(t.GetWord()) {
            case ">": {
                return new IFunction() {
                    @Override
                    public double Calculate(double[] params) {
                        return (params[0] > params[1]) ? 1.0 : 0.0;
                    };
                    @Override
                    public int getNumberOfArgs() {
                        return 2;
                    }
                };
            }
            case "<": {
                return new IFunction() {
                    @Override
                    public double Calculate(double[] params) {
                        return (params[0] < params[1]) ? 1.0 : 0.0;
                    }
                    @Override
                    public int getNumberOfArgs() {
                        return 2;
                    }
                };
            }
            case "=": {
                return new IFunction() {
                    @Override
                    public double Calculate(double[] params) {
                        return (params[0] == params[1]) ? 1.0 : 0.0;
                    }
                    @Override
                    public int getNumberOfArgs() {
                        return 2;
                    }
                };
            }
            default: {
                throw new Error("Token is not compare function");
            }
        }
    }

    private List<Token> TokenizeLine(String line) {
        String[] symbolsForRegExp = new String[] { "\\+", "\\-", "\\*", "/", "\\(", "\\)", ">", "<", "=", "\\.\\.\\." };
        String[] specialSymbols = new String[] { "+", "-", "*", "/", "(", ")", ">", "<", "=", "..." };

        for (int i = 0; i < symbolsForRegExp.length; i++) {
            line = line.replaceAll(symbolsForRegExp[i], " " + specialSymbols[i] + " ");
        }
        Log.d("MyTag", line);
        String[] words = line.split("\\s");

        for(int i = 0; i < words.length; i++) {
            words[i] = words[i].replaceAll("\\s", "");
        }

        List<Token> tokenList = new ArrayList<>();

        for(String word : words) {
            if(word.isEmpty()) {
                continue;
            }
            TokenType type = TokenType.GetTypeOf(word);
            if(type == null) {
                Log.d("MyTag", "null type");
            }
            tokenList.add(new Token(type, word));
            Log.d("MyTag", "\"" + word + "\"");
        }
        return tokenList;
    }

    private Stack<ActionWithBody> actionsStack;

    private int currentNestingLevel = 1;

    private String CreateErrorMessage(String message, int lineNumber) {
        return "Line " + Integer.toString(lineNumber) + ": " + message;
    }

    private void CompileTokenLine(List<Token> line, int lineNum, IfAction lastIfAction)
        throws Error
    {
        ListIterator<Token> iter = line.listIterator();
        if(!iter.hasNext()) {
            return;
        }
        Token t = iter.next();

        if (t.GetType() == TokenType.ForToken) {
            // for <varName> in <varName/value>...<varName/value>
            if (line.size() != 6) {
                throw new Error(CreateErrorMessage("Bad number of tokens in for", lineNum));
            }
            Token varName = iter.next();
            if (varName.GetType() != TokenType.VarToken) {
                throw new Error(CreateErrorMessage("Bad for iterator name", lineNum));
            }
            if (iter.next().GetType() != TokenType.InToken) {
                throw new Error(CreateErrorMessage("Bad structure of for", lineNum));
            }
            Token left = iter.next();
            if (left.GetType() != TokenType.VarToken && left.GetType() != TokenType.NumToken) {
                throw new Error(CreateErrorMessage("bad structure of range", lineNum));
            }
            if (iter.next().GetType() != TokenType.RangeToken) {
                throw new Error(CreateErrorMessage("bad structure of range", lineNum));
            }
            Token right = iter.next();
            if (right.GetType() != TokenType.VarToken && right.GetType() != TokenType.NumToken) {
                throw new Error(CreateErrorMessage("bad structure of range", lineNum));
            }

            ForAction action = new ForAction(left.GetWord(), right.GetWord(), varName.GetWord());
            action.SetParent(actionsStack.peek());
            action.SetLineNumber(lineNum);
            actionsStack.peek().AddAction(action);
            actionsStack.add(action);
            currentNestingLevel++;
        }
        if (t.GetType() == TokenType.IfToken) {
            // if <varName/value> </>/= <varName/value>
            if (line.size() != 4) {
                throw new Error(CreateErrorMessage("Bad number of tokens in if", lineNum));
            }
            Token left = iter.next();
            if (left.GetType() != TokenType.VarToken && left.GetType() != TokenType.NumToken) {
                throw new Error(CreateErrorMessage("Expected number of var as left operand", lineNum));
            }
            Token operand = iter.next();
            if (operand.GetType() != TokenType.CompareToken && operand.GetType() != TokenType.EqualToken) {
                throw new Error(CreateErrorMessage("Expected compare token after left operand", lineNum));
            }
            Token right = iter.next();
            if (right.GetType() != TokenType.VarToken && right.GetType() != TokenType.NumToken) {
                throw new Error(CreateErrorMessage("Expected number of var as right operand", lineNum));
            }

            ActionWithBody ifAction = new IfAction(left.GetWord(), right.GetWord(), GetCmp(operand));
            ifAction.SetParent(actionsStack.peek());
            ifAction.SetLineNumber(lineNum);
            actionsStack.peek().AddAction(ifAction);

            actionsStack.add(ifAction);
            currentNestingLevel++;
        }

        if(t.GetType() == TokenType.NewToken) {
            if (line.size() < 2) {
                throw new Error(CreateErrorMessage("Expected var name", lineNum));
            }
            Token varName = iter.next();
            if (varName.GetType() != TokenType.VarToken) {
                throw new Error(CreateErrorMessage("Expected var name", lineNum));
            }
            NewAction action = new NewAction(varName.GetWord());
            action.SetLineNumber(lineNum);
            action.SetParent(actionsStack.peek());
            actionsStack.peek().AddAction(action);
        }
        if(t.GetType() == TokenType.VarToken) {
            // <varName> = <expression>
            if(line.size() < 3) {
                throw new Error(CreateErrorMessage("Assignment action is too short", lineNum));
            }
            Token variable = t;
            Token equalSign = iter.next();
            if(equalSign.GetType() != TokenType.EqualToken) {
                throw new Error(CreateErrorMessage("Expected equal sign, met " +
                        equalSign.GetWord() + " type: " + equalSign.GetType().toString(), lineNum));
            }
            String expression = "";
            while(iter.hasNext()) {
                Token currToken = iter.next();
                if(currToken.GetType() != TokenType.MathToken &&
                        currToken.GetType() != TokenType.NumToken &&
                        currToken.GetType() != TokenType.VarToken)
                {
                    throw new Error(CreateErrorMessage("Expected math operand or numbers or variable names", lineNum));
                }
                expression = expression + currToken.GetWord();
            }
            if(expression.isEmpty()) {
                throw new Error(CreateErrorMessage("Empty expression", lineNum));
            }
            Action action = new AssignmentAction(variable.GetWord(), expression, functions);
            action.SetParent(actionsStack.peek());
            action.SetLineNumber(lineNum);
            actionsStack.peek().AddAction(action);
        }
        if(t.GetType() == TokenType.ElseToken) {
            if(line.size() != 1) {
                throw new Error(CreateErrorMessage("Incorrect else", lineNum));
            }
            if(lastIfAction != null) {
                ActionWithBody elseAction = new SimpleBodyAction();
                elseAction.SetParent(lastIfAction.GetParent());
                elseAction.SetLineNumber(lineNum);
                lastIfAction.SetElseAction(elseAction);

                actionsStack.add(elseAction);

                currentNestingLevel++;
            }
            else {
                throw new Error(CreateErrorMessage("There was no if before else", lineNum));
            }
        }
    }
}















