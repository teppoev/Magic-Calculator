package com.example.godis.firstproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Calculator calc = new Calculator();
    private DoubleParser parser = new DoubleParser();

    public void OperationClick(View v) {
        Button butt = (Button) v;
        CharSequence text = butt.getText();
        char operation = text.charAt(0);

        TextView output = (TextView) findViewById(R.id.output);
        double number = parser.GetNumber();
        calc.PutNumber(number, operation);
        if(operation == '=') {
            parser.PutNumber(calc.GetResult());
        }
        else {
            parser.Erase();
        }
        output.setText(parser.GetString());
    }

    public void NumberClick(View v) {
        Button butt = (Button) v;
        CharSequence text = butt.getText();

        TextView output = (TextView) findViewById(R.id.output);
        parser.Put(text.charAt(0));
        output.setText(parser.GetString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
