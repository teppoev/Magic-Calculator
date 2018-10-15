package com.example.dynamicbuttons;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

public class UserProgramCompiler {

    UserProgramCompiler(String program) {
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
        CompareToken,

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
            if(word == "<" || word == ">" || word == "=") return CompareToken;
            if(word == "*" || word == "/" || word == "+" || word == "-" ||
                    word == "^") return MathToken;


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

    private void Compile(String program) {
        String[] lines = program.split("\n");
        for(String line: lines) {
            int level = GetNestingLevel(line);
            List<Token> tokenLine = TokenizeLine(line);

            line.substring(level);

            if(level > currentNestingLevel) {
                ActionWithBody action = new SimpleBodyAction();
                action.SetParent(actionsStack.peek());
                actionsStack.add(action);
                currentNestingLevel++;
            }
            else {
                while(level < currentNestingLevel) {
                    actionsStack.pop();
                }
            }

            CompileTokenLine(tokenLine);
        }
    }

    private int GetNestingLevel(String line) {
        int level;
        for(level = 0; line.charAt(level) == '\t'; level++) { }
        return level;
    }

    private IFunction GetCmp(Token t) {
        switch(t.GetWord()) {
            case ">": {
                return new IFunction() {
                    @Override
                    public double Calculate(double[] params) {
                        return (params[0] > params[1]) ? 1.0 : 0.0;
                    };
                };
            }
            case "<": {
                return new IFunction() {
                    @Override
                    public double Calculate(double[] params) {
                        return (params[0] < params[1]) ? 1.0 : 0.0;
                    }
                };
            }
            case "=": {
                return new IFunction() {
                    @Override
                    public double Calculate(double[] params) {
                        return (params[0] == params[1]) ? 1.0 : 0.0;
                    }
                };
            }
            default: {
                throw new Error("Token is not compare function");
            }
        }
    }

    private List<Token> TokenizeLine(String line) {
        String[] specialSymbols = new String[] { "+", "-", "*", "/", "(", ")", ">", "<", "=", ".." };
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

    private Stack<ActionWithBody> actionsStack;

    private int currentNestingLevel = 1;

    private void CompileTokenLine(List<Token> line) {
        ListIterator<Token> iter = line.listIterator();
        Token t = iter.next();

        if (t.GetType() == TokenType.ForToken) {
            // for <varName> in <varName/value>...<varName/value>
            if (line.size() != 6) {
                throw new Error("Bad number of tokens in for");
            }
            Token varName = iter.next();
            if (varName.GetType() != TokenType.VarToken) {
                throw new Error("Bad for iterator name");
            }
            if (iter.next().GetType() != TokenType.InToken) {
                throw new Error("Bad structure of for");
            }
            Token left = iter.next();
            if (left.GetType() != TokenType.VarToken && left.GetType() != TokenType.NumToken) {
                throw new Error("bad structure of range");
            }
            if (iter.next().GetType() != TokenType.RangeToken) {
                throw new Error("bad structure of range");
            }
            Token right = iter.next();
            if (right.GetType() != TokenType.VarToken && right.GetType() != TokenType.NumToken) {
                throw new Error("bad structure of range");
            }

            ForAction action = new ForAction(left.GetWord(), right.GetWord(), varName.GetWord());
            action.SetParent(actionsStack.peek());
            actionsStack.add(action);
            currentNestingLevel++;
        }
        if (t.GetType() == TokenType.IfToken) {
            // if <varName/value> </>/= <varName/value>
            if (line.size() != 4) {
                throw new Error("Bad number of tokens in if");
            }
            Token left = iter.next();
            if (left.GetType() != TokenType.VarToken && left.GetType() != TokenType.NumToken) {
                throw new Error("Expected number of var as left operand");
            }
            Token operand = iter.next();
            if (operand.GetType() != TokenType.CompareToken) {
                throw new Error("Expected compare token after left operand");
            }
            Token right = iter.next();
            if (right.GetType() != TokenType.VarToken && right.GetType() != TokenType.NumToken) {
                throw new Error("Expected number of var as right operand");
            }

            IfAction action = new IfAction(left.GetWord(), right.GetWord(), GetCmp(operand));
            action.SetParent(actionsStack.peek());
            actionsStack.add(action);
            currentNestingLevel++;
        }
        if(t.GetType() == TokenType.VarToken) {
            // <varName> = <expression>
            if(line.size() < 3) {
                throw new Error("Assignment action is too short");
            }
            Token variable = t;
            Token equalSign = iter.next();
            if(equalSign.GetType() != TokenType.EqualToken) {
                throw new Error("Expected equal sign");
            }
            String expression = "";
            while(iter.hasNext()) {
                Token currToken = iter.next();
                if(currToken.GetType() != TokenType.MathToken ||
                        currToken.GetType() != TokenType.NumToken ||
                        currToken.GetType() != TokenType.VarToken)
                {
                    throw new Error("Expected math operand or numbers or variable names");
                }
                expression.concat(currToken.GetWord());
            }
            if(expression.isEmpty()) {
                throw new Error("Empty expression");
            }
            Action action = new AssignmentAction(variable.GetWord(), expression, functions);
            action.SetParent(actionsStack.peek());
            actionsStack.peek().AddAction(action);
        }
        if(t.GetType() == TokenType.NewToken) {
            if(line.size() != 2) {
                throw new Error("Incorrect assignment action");
            }
            Token varName = iter.next();
            if(varName.GetType() != TokenType.VarToken) {
                throw new Error("Expected new variable name");
            }
        }
        if(t.GetType() == TokenType.ElseToken) {
            if(line.size() != 1) {
                throw new Error("Incorrect else");
            }
            try {
                IfAction ifAction = (IfAction)actionsStack.pop();

                ActionWithBody elseAction = new SimpleBodyAction();
                elseAction.SetParent(ifAction.GetParent());

                ifAction.SetElseAction(elseAction);

                actionsStack.add(elseAction);

                currentNestingLevel++;
            }
            catch (ClassCastException e) {
                throw new Error("There was no if before else");
            }
        }
    }
}















