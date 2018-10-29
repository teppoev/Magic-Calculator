package com.project.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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
    int nob = 0; //number of brackets in outView; if ( then ++nub else if ) then --nubZ
    boolean isStarted = false;
    boolean isAnswered = false;

    Calculator calculator = new Calculator();
    HashMap<String,Variable> variableMap = new HashMap<String, Variable>();
    HashMap<String, IFunction> functionMap = new HashMap<String, IFunction>();

    private LinearLayout buttonShelf;
    private int buttonCounter = 0;
    private UserProgramCompiler compiler;

    private void CreatingButton(String name, String body) {

        if (!functionMap.containsKey(name)) {

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
                    IFunction function = functionMap.get(name);
                    if(function != null) {
                        if (isAnswered) {
                            grayOutView.setText(outView.getText());
                            outView.setText("");
                            isAnswered = false;
                        }
                        char lastCh;
                        if (outView.getText().length() != 0) {
                            lastCh = outView.getText().charAt(outView.getText().length() - 1);
                            if (lastCh == '.') {
                                outView.setText(outView.getText().subSequence(0, outView.getText().length() - 1));
                                isStarted = false;
                            }
                        }
                        outView.append(name + "(");
                        ++nob;
                    }
                }
            });


            newButton.setLayoutParams(params);
            buttonCounter++;
            buttonShelf.addView(newButton);

            functionMap.put(name, function);
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

        outView = (TextView) findViewById(R.id.main_output_view);
        grayOutView = (TextView) findViewById(R.id.additional_output_view);

        grayOutView.setMovementMethod(new ScrollingMovementMethod());
        grayOutView = (TextView) findViewById(R.id.additional_output_view);
        buttonShelf = (LinearLayout)findViewById(R.id.button_shelf);
        compiler = new UserProgramCompiler();
        outView = (TextView) findViewById(R.id.main_output_view);
        Button magicButton = (Button)findViewById(R.id.new_button);
        View.OnClickListener clickHandler = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            nob = 0;
        }
        else if (v.getId() == R.id.buttondelete){
            char lastCh;
            if (outView.getText().toString().length() != 0) {
                lastCh = outView.getText().charAt(outView.getText().length() - 1);
                if (lastCh == '(') --nob;
                else if (lastCh == ')') ++nob;
                else if (lastCh == '.') isStarted = false;
                outView.setText(outView.getText().subSequence(0, outView.getText().length() - 1));
            } else {
                outView.setText("");
            }
        }
        else if (v.getId() == R.id.buttonbrakets){
            char lastCh;
            if (outView.getText().length() != 0) {
                lastCh = outView.getText().charAt(outView.getText().length() - 1);
            } else {
                lastCh = '(';
            }
            if (lastCh == '+' || lastCh == '-' || lastCh == '*' || lastCh == '/' ||
                    lastCh == '%' || lastCh == '(' || lastCh == '^' || lastCh == '√' ||
                    (lastCh >= '0' && lastCh <= '9' || lastCh == '.' || lastCh == 'π' || lastCh == 'е')
                    && nob == 0) {
                outView.setText((String) (outView.getText() + "("));
                ++nob;
            } else if (lastCh >= '0' && lastCh <= '9' || lastCh == 'π' || lastCh == 'е') {
                isStarted = false;
                outView.setText((String)(outView.getText() + ")"));
                --nob;
            } else if (lastCh == '.') {
                outView.setText(outView.getText().subSequence(0, outView.getText().length() - 1));
                isStarted = false;
                outView.setText((String)(outView.getText() + ")"));
                --nob;
            } else if (lastCh == ')' && nob == 0) {
                outView.setText((String) (outView.getText() + "*("));
            } else if (lastCh == ')') {
                outView.setText((String) (outView.getText() + ")"));
                --nob;
            }
        }
        else if (v.getId() == R.id.button7 || v.getId() == R.id.button8 || v.getId() == R.id.button9 ||
                v.getId() == R.id.button4 || v.getId() == R.id.button5 || v.getId() == R.id.button6 ||
                v.getId() == R.id.button1 || v.getId() == R.id.button2 || v.getId() == R.id.button3 ||
                v.getId() == R.id.buttonπ || v.getId() == R.id.buttonе || v.getId() == R.id.button0)
        {
            outView.setText((outView.getText().toString() + ((Button)v).getText().toString()));
        }
        else if (v.getId() == R.id.buttonmod || v.getId() == R.id.buttondiv ||
                v.getId() == R.id.buttonmul || v.getId() == R.id.buttonsub ||
                v.getId() == R.id.buttonadd || v.getId() == R.id.buttonpowy ||
                v.getId() == R.id.buttonpow2) {
            char lastCh;
            if (outView.getText().length() != 0) {
                isStarted = false;
                lastCh = outView.getText().charAt(outView.getText().length() - 1);
                if (lastCh == '%' || lastCh == '/' || lastCh == '*' || lastCh == '+' || lastCh == '^' || lastCh == '-'){
                    outView.setText(outView.getText().subSequence(0, outView.getText().length() - 1));
                }
                else if (lastCh == '.') {
                    outView.setText(outView.getText().subSequence(0, outView.getText().length() - 1));
                }
                if (outView.getText().length() != 0) {
                    if (v.getId() != R.id.buttonpowy && v.getId() != R.id.buttonpow2) {
                        outView.append(((Button) v).getText().toString());
                    } else {
                        outView.append("^");
                    }
                    if (v.getId() == R.id.buttonpow2) {
                        outView.append("2");
                    }
                }
            } else if (v.getId() == R.id.buttonsub) {
                outView.append("-");
            }
        }
        else if (v.getId() == R.id.buttondot && !isStarted) {
            char lastCh;
            if (outView.getText().length() != 0) {
                lastCh = outView.getText().charAt(outView.getText().length() - 1);
                if (lastCh >= '0' && lastCh <= '9'){
                    outView.append(".");
                }
                else {
                    outView.append("0.");
                }
            } else {
                outView.append("0.");
            }
            isStarted = true;
        }
        else if (v.getId() == R.id.buttonsin || v.getId() == R.id.buttoncos ||
                v.getId() == R.id.buttontan || v.getId() == R.id.buttonln ||
                v.getId() == R.id.buttonsqrt ) {
            char lastCh;
            if (outView.getText().length() != 0) {
                lastCh = outView.getText().charAt(outView.getText().length() - 1);
                if (lastCh == '.') {
                    outView.append("0");
                }
            }
            isStarted = false;
            outView.setText((String)((outView.getText().toString() + ((Button)v).getText().toString()) + "("));
            ++nob;
        }
        else if (v.getId() == R.id.buttoncalc){
            try {
                double result;
                isAnswered = true;
                isStarted = false;
                if (outView.getText().length() != 0) {
                    char lastCh;
                    lastCh = outView.getText().charAt(outView.getText().length() - 1);
                    while (!(lastCh >= '0' && lastCh <= '9') && !(lastCh == '.') && !(lastCh >= 'a' && lastCh <= 'z') && !(lastCh == '_') && !(lastCh == ')')){
                        outView.setText(outView.getText().subSequence(0, outView.getText().length() - 1));
                        if (lastCh == '(') --nob;
                        if (outView.getText().length() != 0) {
                            lastCh = outView.getText().charAt(outView.getText().length() - 1);
                        } else break;
                    }
                    while (nob > 0) {
                        outView.append(")");
                        --nob;
                    }
                    if (lastCh == '.') {
                        outView.setText(outView.getText().subSequence(0, outView.getText().length() - 1));
                    }
                    grayOutView.setText(outView.getText());
                    if (outView.getText().length() != 0) {
                        result = calculator.calc(outView.getText().toString(), variableMap, functionMap);
                        if (result - (int) result != 0.0) {
                            outView.setText(String.valueOf(result));
                        } else {
                            outView.setText(String.valueOf((int) result));
                        }
                    }
                }
                else {
                    grayOutView.setText(outView.getText());
                }
            } catch(Exception e) {
                outView.setText((String)("Ошибка: " + e.getMessage()));
            }
        }
    }
}
