package com.example.godis.firstproject;

public class Calculator {
    private double lastNumber = 0.0;
    private char operation = '\0';
    private boolean hasOperation = false;

    public void PutNumber(double num, char oper) {
        switch(oper) {
            case '=':
                if(hasOperation) {
                    lastNumber = Calculate(num);
                    hasOperation = false;
                }
                else {
                    hasOperation = false;
                }
                break;
            default:
                lastNumber = num;
                operation = oper;
                hasOperation = true;
                break;
        }
    }

    public double GetResult() {
        return lastNumber;
    }

    private double Calculate(double num) {
        double temp;
        switch(operation) {
            case '+': {
                temp = num + lastNumber;
            break;
            }
            case '-': {
                temp = lastNumber - num;
            break;
            }
            case '/': {
                temp = lastNumber / num;
            break;
            }
            case '*': {
                temp = lastNumber * num;
            break;
            }
            default: {
                temp = -7.15;
            break;
            }
        }
        return temp;
    }
}
