package ru.spbu.twothreeone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
	    Calculator calc = new Calculator();
	    ArrayList<Token> tokens = new ArrayList<Token>();
	    Map<String, Variable> variables = new HashMap<String, Variable>();
	    Map<String, Function> functions = new HashMap<String, Function>();
	    {
	       variables.put("a", new Variable(5.));
	       variables.put("b", new Variable(1.));
	       variables.put("c", new Variable(.25));
	       functions.put("sin", new Function("sin"));
	       functions.put("cos", new Function("cos"));
	    }

        try {
            System.out.println(calc.calc("(a+b-c/sin(a))^cos(3)", variables, functions));
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
