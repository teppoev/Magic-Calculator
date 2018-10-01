package com.example.godis.firstproject;

public class DoubleParser {

    private String str = "0";
    boolean isDotPressed = false;

    public void Put(char c) {
        if(c == '.') {
            if(!isDotPressed) {
                str += '.';
                isDotPressed = true;
            }
        }
        else {
            if(str.length() == 1 && str.charAt(0) == '0') {
                str = Character.toString(c);
            }
            else {
                str += c;
            }
        }
    }

    public void PutNumber(double num) {
        if((num - (int)num) > 1e-15) {
            str = Double.toString(num);
            isDotPressed = true;
        }
        else {
            str = Integer.toString((int)num);
            isDotPressed = false;
        }
    }

    public void Erase() {
        str = "0";
        isDotPressed = false;
    }


    public String GetString() {
        return str;
    }

    public double GetNumber() {
        return Double.parseDouble(str);
    }

    public void EraseLast() {
        if(str.length() != 1) {
            if(str.charAt(str.length() - 1) == '.') {
                isDotPressed = false;
            }
        str = str.substring(0, str.length() - 1);
        }
        else {
            str = "0";
        }
    }
}
