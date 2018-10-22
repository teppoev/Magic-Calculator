package com.project.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity{

    TextView outView;
    TextView grayOutView;
    int nob = 0; //number of brakets in outView; if ( then ++nub else if ) then --nubZ
    boolean isAnswered = false;

    Calculator calculator = new Calculator();
    HashMap<String,Variable> variableMap = new HashMap<String, Variable>();
    HashMap<String, Function> functionMap = new HashMap<String, Function>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        grayOutView = (TextView) findViewById(R.id.additional_output_view);
        grayOutView.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                //some code
                break;
            case R.id.help:
                //some code
                break;
            case R.id.progButtons:
                //some code
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickStart(View v) {
        //Toast.makeText(this, ((Button)v).getText(), Toast.LENGTH_SHORT).show();

        outView = (TextView) findViewById(R.id.main_output_view);
        grayOutView = (TextView) findViewById(R.id.additional_output_view);


        if (isAnswered) {
            if (v.getId() == R.id.buttonC) {
                grayOutView.setText(outView.getText());
            }
            else if (v.getId() == R.id.buttondelete) {
                outView.setText("");
            }
            else if (v.getId() == R.id.buttonbrakets || v.getId() == R.id.button7 ||
                    v.getId() == R.id.button8 || v.getId() == R.id.button9 ||
                    v.getId() == R.id.button4 || v.getId() == R.id.button5 ||
                    v.getId() == R.id.button6 || v.getId() == R.id.button1 ||
                    v.getId() == R.id.button2 || v.getId() == R.id.button3 ||
                    v.getId() == R.id.buttonπ || v.getId() == R.id.buttonе ||
                    v.getId() == R.id.button0 || v.getId() == R.id.buttondot ||
                    v.getId() == R.id.buttonsqrt || v.getId() == R.id.buttonsin ||
                    v.getId() == R.id.buttoncos || v.getId() == R.id.buttontan ||
                    v.getId() == R.id.buttonln) {
                grayOutView.setText(outView.getText());
                outView.setText("");
            }
            isAnswered = false;
        }

        if (v.getId() == R.id.buttonC) {
            outView.setText("");
        }
        else if (v.getId() == R.id.buttondelete){
            if (!((String) outView.getText()).isEmpty()) {
                outView.setText(outView.getText().subSequence(0, outView.getText().length() - 1));
            } else {
                outView.setText("");
            }
        }
        else if (v.getId() == R.id.buttonbrakets){
            char lastCh = outView.getText().charAt(outView.getText().length() - 1);
            if (lastCh == '+' || lastCh == '-' || lastCh == '*' || lastCh == '/' ||
                    lastCh == '%' || lastCh == '(' || lastCh == '^' || lastCh == '√' ||
                    (lastCh >= '0' && lastCh <= '9' || lastCh == '.' || lastCh == 'π' || lastCh == 'е')
                    && nob != 0) {
                outView.setText((String)(outView.getText() + "("));
            } else if (lastCh == ')'){
                outView.setText((String)(outView.getText() + "*("));
            } else if (lastCh >= '0' && lastCh <= '9' || lastCh == '.' ||
                    lastCh == 'π' || lastCh == 'е') {
                outView.setText((String)(outView.getText() + ")"));
            }
        }
        else if (v.getId() == R.id.buttonmod || v.getId() == R.id.buttondiv ||
                v.getId() == R.id.buttonmul || v.getId() == R.id.button7 ||
                v.getId() == R.id.button8 || v.getId() == R.id.button9 ||
                v.getId() == R.id.buttonsub || v.getId() == R.id.button4 ||
                v.getId() == R.id.button5 || v.getId() == R.id.button6 ||
                v.getId() == R.id.buttonadd || v.getId() == R.id.button1 ||
                v.getId() == R.id.button2 || v.getId() == R.id.button3 ||
                v.getId() == R.id.buttonπ || v.getId() == R.id.buttonе ||
                v.getId() == R.id.button0 || v.getId() == R.id.buttondot) {
            outView.setText((String)((String)(outView.getText()) + (String)(((Button)v).getText())));
        }
        else if (v.getId() == R.id.buttonsin || v.getId() == R.id.buttoncos ||
                v.getId() == R.id.buttontan || v.getId() == R.id.buttonln ||
                v.getId() == R.id.buttonsqrt ) {
            outView.setText((String)((String)(outView.getText()) + (String)(((Button)v).getText()) + "("));
        }
        else if (v.getId() == R.id.buttonpow2) {
            outView.setText((String)(outView.getText() + "^2"));
        }
        else if (v.getId() == R.id.buttonpowy) {
            outView.setText((String)(outView.getText() + "^"));
        }
        else if (v.getId() == R.id.buttoncalc){
            grayOutView.setText(outView.getText());
            try {
                double result;
                isAnswered = true;
                if (!((String) outView.getText()).isEmpty()) {
                    result = calculator.calc((String) outView.getText(), variableMap, functionMap);
                    if (result - (int) result != 0.0) {
                        outView.setText(String.valueOf(result));
                    }
                    else {
                        outView.setText(String.valueOf((int) result));
                    }
                } else {
                    grayOutView.setText(outView.getText());
                }
            } catch(Exception e) {
                outView.setText((String)("Ошибка: " + e.getMessage()));
            }
        }
    }
}
