package com.example.dynamicbuttons;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentManager;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class TestDynamicButtons extends AppCompatActivity
                                implements CreateButtonWindow.CreateButtonWindowListener{

    private LinearLayout buttonShelf;
    private int buttonCounter = 0;
    private TextView answerView;

    private Map<String, IFunction> buttonsPrograms;

    private IFunction GenerateFunction(final String body) {
        return new IFunction() {
            @Override
            public double Calculate(double[] params) throws  NumberFormatException {
                try {
                    return Double.parseDouble(body);
                }
                catch(NumberFormatException e) {
                    throw new NumberFormatException(body.toString());
                }
            }
        };
    }

    private void CreatingButton(String name, String body) {

        if (!buttonsPrograms.containsKey(name)) {

            final Button newButton = new Button(TestDynamicButtons.this);
            newButton.setText(name);
            newButton.setId(buttonCounter);
            TableRow.LayoutParams params =
                    new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT);

            IFunction function = GenerateFunction(body);
            newButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = newButton.getText().toString();
                    IFunction function = buttonsPrograms.get(name);
                    if(function != null) {
                        double answer = function.Calculate(new double[1]);
                        answerView.setText(name + " " + Double.toString(answer));
                    }
                }
            });


            newButton.setLayoutParams(params);
            buttonCounter++;
            buttonShelf.addView(newButton);

            buttonsPrograms.put(name, function);

        }
        else {
            //...
        }
    }

    @Override
    public void onDialogPositiveClick(CreateButtonWindow dialog) {
        CreatingButton(dialog.GetText(), dialog.GetBody());
    }

    @Override
    public void onDialogNegativeClick(CreateButtonWindow dialog) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_dynamic_buttons);

        buttonsPrograms = new HashMap();

        buttonShelf = (LinearLayout)findViewById(R.id.shelf);

        answerView = (TextView) findViewById(R.id.answer_view);

        Button magicButton = (Button)findViewById(R.id.createbut);
        View.OnClickListener clickHandler = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name;
                CreateButtonWindow window = new CreateButtonWindow();
                window.show(getFragmentManager(), "enterbuttonname");
            }
        };
        magicButton.setOnClickListener(clickHandler);
    }
}
