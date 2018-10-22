package com.project.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements CreateButtonWindow.CreateButtonWindowListener{

    TextView outView;
    TextView grayOutView;
    int nob = 0; //number of brakets in outView; if ( then ++nub else if ) then --nub
    boolean isAnswered = false;

    Calculator calculator = new Calculator();
    HashMap<String,Variable> variableMap = new HashMap<String, Variable>();
    HashMap<String, IFunction> functionMap = new HashMap<String, IFunction>();

    private LinearLayout buttonShelf;
    private int buttonCounter = 0;
    private UserProgramCompiler compiler;

    private Map<String, IFunction> buttonsPrograms;

    private void CreatingButton(String name, String body) {

        if (!buttonsPrograms.containsKey(name)) {

            final Button newButton = new Button(MainActivity.this);
            newButton.setText(name);
            newButton.setId(buttonCounter);
            TableRow.LayoutParams params =
                    new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT);


            IFunction function = compiler.Compile(body, 1);
            newButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = ((Button)v).getText().toString();
                    IFunction function = buttonsPrograms.get(name);
                    if(function != null) {
                        outView.append(name + "(");
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
        setContentView(R.layout.activity_main);

        buttonsPrograms = new HashMap();

        buttonShelf = (LinearLayout)findViewById(R.id.button_shelf);

        compiler = new UserProgramCompiler();

        outView = (TextView) findViewById(R.id.main_output_view);
        grayOutView = (TextView) findViewById(R.id.additional_output_view);
        outView.setText("");
        grayOutView.setText("");

        Button magicButton = (Button)findViewById(R.id.new_button);
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

        if (isAnswered) {
            grayOutView.setText(outView.getText());
            outView.setText("");
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
            if (lastCh == '+' || lastCh == '-' || lastCh == '*' || lastCh == '/' || lastCh == '%' ||
                    lastCh == '(' || lastCh == '^' || lastCh == '√' || (lastCh >= '0' && lastCh <=
                    '9' || lastCh == '.' || lastCh == 'π' || lastCh == 'е') && nob != 0) {
                outView.setText((String)(outView.getText() + "("));
            } else if (lastCh == ')'){
                outView.setText((String)(outView.getText() + "*("));
            } else if (lastCh >= '0' && lastCh <= '9' || lastCh == '.' || lastCh == 'π' || lastCh == 'е') {
                outView.setText((String)(outView.getText() + ")"));
            }
        }
        else if (v.getId() == R.id.buttonmod || v.getId() == R.id.buttondiv || v.getId() ==
                R.id.buttonsqrt || v.getId() == R.id.buttonmul || v.getId() == R.id.button7 ||
                v.getId() == R.id.button8 || v.getId() == R.id.button9 || v.getId() ==
                R.id.buttonsub || v.getId() == R.id.button4 || v.getId() == R.id.button5 ||
                v.getId() == R.id.button6 || v.getId() == R.id.buttonadd || v.getId() ==
                R.id.button1 || v.getId() == R.id.button2 || v.getId() == R.id.button3 ||
                v.getId() == R.id.buttonπ || v.getId() == R.id.buttonе || v.getId() == R.id.button0
                || v.getId() == R.id.buttdot) {
            outView.setText(outView.getText().toString() + ((Button)v).getText().toString());
        }
        else if (v.getId() == R.id.buttonsin || v.getId() == R.id.buttoncos || v.getId() == R.id.buttontan || v.getId() == R.id.buttonln) {
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
            isAnswered = true;
            try {
                double result = calculator.calc((String)outView.getText(), variableMap, functionMap);
                if (result - (int) result != 0.0) {
                    outView.setText(String.valueOf(result));
                }
                else {
                    outView.setText(String.valueOf((int) result));
                }
            } catch(Exception e) {
                outView.setText((String)("Ошибка: " + e.getMessage()));
            }
        }
    }
}
