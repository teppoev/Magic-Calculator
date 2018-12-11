package com.project.calculator;


/*import android.content.Intent;*/
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import CalculatorFiles.Calculator;
import ExpressionLanguage.IFunction;
import ExpressionLanguage.UserProgramCompiler;

public class MainActivity extends AppCompatActivity
        implements CreateButtonWindow.CreateButtonWindowListener{

    private TextView outView, grayOutView;
    private HorizontalScrollView myScroll, myGrayScroll;
    private int nob = 0; //number of brackets in outView; if ( then ++nub else if ) then --nub
    private boolean isStarted, isAnswered, isError, isContextMenu, isScroll, isGrayScroll;

    private Calculator calculator;

    private HashMap<String, IFunction> functionsMap = new HashMap<>();
    private HashMap<String, String> sourceCodeMap = new HashMap<>();

    private LinearLayout buttonShelf;
    private int buttonCounter = 0;
    private UserProgramCompiler compiler;

    private OnClickListener functionButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String name = ((Button)v).getText().toString();
            IFunction function = functionsMap.get(name);
            if(function != null) {
                if (isAnswered) {
                    grayOutView.setText(outView.getText());
                    myGrayScroll.scrollTo(grayOutView.getRight(), 0);
                    myGrayScroll.scrollBy(-1, 0);
                    isGrayScroll = true;
                    outView.setText("");
                    isAnswered = false;

                }
                if (isError) {
                    grayOutView.setText(outView.getText());
                    myGrayScroll.scrollTo(grayOutView.getRight(), 0);
                    myGrayScroll.scrollBy(-1, 0);
                    isGrayScroll = true;
                    outView.setText("");
                    isError = false;
                }
                char lastCh;
                if (outView.getText().length() != 0) {
                    lastCh = outView.getText().charAt(outView.getText().length() - 1);
                    if (lastCh == '.') {
                        outView.setText(outView.getText()
                                .subSequence(0, outView.getText().length() - 1));
                        isStarted = false;
                    }
                }
                outView.append(name + "(");
                ++nob;
                myScroll.scrollTo(outView.getRight(), 0);
                myScroll.scrollBy(-1, 0);
                isScroll = true;
            }
        }
    };

    private void DeleteButtonFromShelf(Button but) {
        String buttonName = but.getText().toString();

        functionsMap.remove(buttonName);
        sourceCodeMap.remove(buttonName);

        buttonShelf.removeView(but);
    }

    private void EditFunctionSourceCode(Button but) {

        String buttonName = but.getText().toString();
        String functionBody = sourceCodeMap.get(buttonName);
        int paramNum = functionsMap.get(buttonName).getNumberOfArgs();

        functionsMap.remove(buttonName);
        sourceCodeMap.remove(buttonName);

        CreateButtonWindow window = new CreateButtonWindow();

        Bundle args = new Bundle();

        args.putString("Mode", "Edit");
        args.putString("Body", functionBody);
        args.putString("Name", buttonName);
        args.putInt("ParamNum", paramNum - 1);

        window.setArguments(args);

        window.show(getFragmentManager(), "enterbuttonname");
    }

    private void showEditDeleteMenu(final View v) {
        PopupMenu menu = new PopupMenu(this, v);
        menu.inflate(R.menu.delete_edit_button_menu);

        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.edit_item:
                        EditFunctionSourceCode((Button) v);
                        return true;
                    case R.id.delete_item:
                        DeleteButtonFromShelf((Button) v);
                        return true;
                }
                return false;
            }
        });

        menu.show();
    }

    private void CreatingButton(String name, String body, int pNum) {

        if (!functionsMap.containsKey(name)) {

            final Button newButton = new Button(MainActivity.this);
            newButton.setText(name);
            newButton.setId(buttonCounter);
            TableRow.LayoutParams params =
                    new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT);

            IFunction function = compiler.Compile(body, pNum);
            newButton.setOnClickListener(functionButtonClickListener);

            newButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    showEditDeleteMenu(v);

                    return true;
                }
            });

            newButton.setLayoutParams(params);
            buttonCounter++;
            buttonShelf.addView(newButton);

            functionsMap.put(name, function);
            sourceCodeMap.put(name, body);
        }
        else {

            //...
        }
    }

    private void ChangeButtonParams(String newName, String oldName, String body, int pNum) {
        if(!functionsMap.containsKey(newName)) {
            int buttonCount = buttonShelf.getChildCount();

            Button button = null;

            for(int i = 0; i < buttonCount; ++i) {
                button = (Button)buttonShelf.getChildAt(i);
                if(button.getText().toString().equals(oldName)) {
                    break;
                }
            }

            if(button == null) {
                //...
            }

            button.setText(newName);

            IFunction function = compiler.Compile(body, pNum);

            functionsMap.put(newName, function);
            sourceCodeMap.put(newName, body);
        }
        else {
            //...
        }
    }

    private void ButtonsInit() {
        IFunction sinFunc = new IFunction() {
            @Override
            public double Calculate(double[] params) {
                return Math.sin(params[0]);
            }

            @Override
            public int getNumberOfArgs() {
                return 1;
            }
        };

        IFunction cosFunc = new IFunction() {
            @Override
            public double Calculate(double[] params) {
                return Math.cos(params[0]);
            }

            @Override
            public int getNumberOfArgs() {
                return 1;
            }
        };

        IFunction tanFunc = new IFunction() {
            @Override
            public double Calculate(double[] params) {
                return Math.tan(params[0]);
            }

            @Override
            public int getNumberOfArgs() {
                return 1;
            }
        };

        IFunction lnFunc = new IFunction() {
            @Override
            public double Calculate(double[] params) {
                return Math.log(params[0]);
            }

            @Override
            public int getNumberOfArgs() {
                return 1;
            }
        };

        IFunction asinFunc = new IFunction() {
            @Override
            public double Calculate(double[] params) {
                return Math.asin(params[0]);
            }

            @Override
            public int getNumberOfArgs() {
                return 1;
            }
        };

        IFunction acosFunc = new IFunction() {
            @Override
            public double Calculate(double[] params) {
                return Math.acos(params[0]);
            }

            @Override
            public int getNumberOfArgs() {
                return 1;
            }
        };

        IFunction atanFunc = new IFunction() {
            @Override
            public double Calculate(double[] params) {
                return Math.atan(params[0]);
            }

            @Override
            public int getNumberOfArgs() {
                return 1;
            }
        };

        functionsMap.put("sin", sinFunc);
        functionsMap.put("cos", cosFunc);
        functionsMap.put("tan", tanFunc);
        functionsMap.put("ln", lnFunc);
        functionsMap.put("asin", asinFunc);
        functionsMap.put("acos", acosFunc);
        functionsMap.put("atan", atanFunc);
    }

    private void BooleanInit() {
        isStarted = false;
        isAnswered = false;
        isError = false;
        isContextMenu = false;
        isScroll = false;
        isGrayScroll = false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myScroll = findViewById(R.id.scroll_main_out_view);
        myGrayScroll = findViewById(R.id.scroll_add_out_view);

        registerForContextMenu(findViewById(R.id.buttondot));
        registerForContextMenu(findViewById(R.id.buttonsin));
        registerForContextMenu(findViewById(R.id.buttoncos));
        registerForContextMenu(findViewById(R.id.buttontan));

        outView = findViewById(R.id.main_output_view);
        grayOutView = findViewById(R.id.additional_output_view);

        calculator = new Calculator();
        compiler = new UserProgramCompiler();
        outView = findViewById(R.id.main_output_view);
        grayOutView = findViewById(R.id.additional_output_view);
        Button magicButton = findViewById(R.id.new_button);
        buttonShelf = findViewById(R.id.button_shelf);

        ViewTreeObserver.OnScrollChangedListener scrollListener =
                new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (isScroll) {
                    myScroll.fullScroll(View.FOCUS_RIGHT);
                    isScroll = false;
                }
                if (isGrayScroll){
                    myGrayScroll.fullScroll(View.FOCUS_RIGHT);
                    isGrayScroll = false;
                }
            }
        };
        myScroll.getViewTreeObserver().addOnScrollChangedListener(scrollListener);
        myGrayScroll.getViewTreeObserver().addOnScrollChangedListener(scrollListener);

        View.OnClickListener clickHandler = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateButtonWindow window = new CreateButtonWindow();

                Bundle args = new Bundle();

                args.putString("Mode", "Create");

                window.setArguments(args);

                window.show(getFragmentManager(), "enterbuttonname");
            }
        };
        magicButton.setOnClickListener(clickHandler);

        ButtonsInit();
        BooleanInit();
    }


    @Override
    public void onDialogPositiveClick(CreateButtonWindow dialog) {

        switch(dialog.GetMode()) {
            case "Edit":
                ChangeButtonParams(dialog.GetName(), dialog.GetOldName(), dialog.GetBody(),
                        dialog.GetParamNum());
                break;
            case "Create":
                CreatingButton(dialog.GetName(), dialog.GetBody(), dialog.GetParamNum());
                break;
        }
    }

    @Override
    public void onDialogNegativeClick(CreateButtonWindow dialog) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        isContextMenu = true;
        switch (v.getId()) {
            case R.id.buttondot:
                if (isAnswered) {
                    grayOutView.setText(outView.getText());
                    myGrayScroll.scrollTo(grayOutView.getRight(), 0);
                    myGrayScroll.scrollBy(-1, 0);
                    isGrayScroll = true;
                    outView.setText("");
                    isAnswered = false;
                } else if (!isError) {
                    char lastCh;
                    if (outView.getText().toString().length() != 0) {
                        lastCh = outView.getText().charAt(outView.getText().length() - 1);
                        if (lastCh == ')' || lastCh >= 'a' && lastCh <= 'z' ||
                                lastCh >= 'A' && lastCh <= 'Z' ||
                                lastCh >= '0' && lastCh <= '9') {
                            outView.append(",");
                            isStarted = false;
                        } else if (lastCh == '.') {
                            outView.setText(outView.getText()
                                    .subSequence(0, outView.getText().length() - 1));
                            outView.append(",");
                            isStarted = false;
                        }
                    }
                    break;
                }
            case R.id.buttonsin:
                if (isAnswered || isError) {
                    grayOutView.setText(outView.getText());
                    myGrayScroll.scrollTo(grayOutView.getRight(), 0);
                    myGrayScroll.scrollBy(-1, 0);
                    isGrayScroll = true;
                    outView.setText("asin(");
                    ++nob;
                    if (isAnswered) {
                        isAnswered = false;
                    } else {
                        isError = false;
                    }
                } else {
                    char lastCh;
                    if (outView.getText().length() != 0) {
                        lastCh = outView.getText().charAt(outView.getText().length() - 1);
                        if (lastCh == '.') {
                            outView.setText(outView.getText()
                                    .subSequence(0, outView.getText().length() - 1));
                        }
                    }
                    isStarted = false;
                    outView.append("asin(");
                    ++nob;
                }
                break;
            case R.id.buttoncos:
                if (isAnswered || isError) {
                    grayOutView.setText(outView.getText());
                    myGrayScroll.scrollTo(grayOutView.getRight(), 0);
                    myGrayScroll.scrollBy(-1, 0);
                    isGrayScroll = true;
                    outView.setText("acos(");
                    ++nob;
                    if (isAnswered) {
                        isAnswered = false;
                    } else {
                        isError = false;
                    }
                } else {
                    char lastCh;
                    if (outView.getText().length() != 0) {
                        lastCh = outView.getText().charAt(outView.getText().length() - 1);
                        if (lastCh == '.') {
                            outView.setText(outView.getText()
                                    .subSequence(0, outView.getText().length() - 1));
                        }
                    }
                    isStarted = false;
                    outView.append("acos(");
                    ++nob;
                }
                break;
            case R.id.buttontan:
                if (isAnswered || isError) {
                    grayOutView.setText(outView.getText());
                    myGrayScroll.scrollTo(grayOutView.getRight(), 0);
                    myGrayScroll.scrollBy(-1, 0);
                    isGrayScroll = true;
                    outView.setText("atan(");
                    ++nob;
                    if (isAnswered) {
                        isAnswered = false;
                    } else {
                        isError = false;
                    }
                } else {
                    char lastCh;
                    if (outView.getText().length() != 0) {
                        lastCh = outView.getText().charAt(outView.getText().length() - 1);
                        if (lastCh == '.') {
                            outView.setText(outView.getText()
                                    .subSequence(0, outView.getText().length() - 1));
                        }
                    }
                    isStarted = false;
                    outView.append("atan(");
                    ++nob;
                }
                break;
            default:
                break;
        }
        myScroll.scrollTo(outView.getRight(), 0);
        myScroll.scrollBy(-1, 0);
        isScroll = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help:
                /*Intent intent = new Intent(this, HelpActivity.class);
                startActivity(intent);
                break;*/
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickStart(View v) {
        if (isAnswered) {
            if (v.getId() == R.id.buttonC || v.getId() == R.id.buttondelete) {
                grayOutView.setText(outView.getText());
                myGrayScroll.scrollTo(grayOutView.getRight(), 0);
                myGrayScroll.scrollBy(-1, 0);
                isGrayScroll = true;
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
                myGrayScroll.scrollTo(grayOutView.getRight(), 0);
                myGrayScroll.scrollBy(-1, 0);
                isGrayScroll = true;
                outView.setText("");
            }
            isAnswered = false;
        }

        if (isError) {
            grayOutView.setText(outView.getText());
            myGrayScroll.scrollTo(grayOutView.getRight(), 0);
            myGrayScroll.scrollBy(-1, 0);
            isGrayScroll = true;
            outView.setText("");
            nob = 0;
            isError = false;
            isStarted = false;
        }

        if (v.getId() == R.id.buttonC) {
            outView.setText("");
            nob = 0;
            isStarted = false;
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
                    lastCh >= 'a' && lastCh <= 'z' || lastCh == ',' || lastCh == '_' ||
                    lastCh >= 'A' && lastCh <= 'Z' ||
                    (lastCh >= '0' && lastCh <= '9' || lastCh == '.' || lastCh == 'π' ||
                            lastCh == 'е') && nob == 0)
            {
                outView.append("(");
                ++nob;
            } else if (lastCh >= '0' && lastCh <= '9' || lastCh == 'π' || lastCh == 'е')
            {
                isStarted = false;
                outView.append(")");
                --nob;
            } else if (lastCh == '.') {
                outView.setText(outView.getText().subSequence(0, outView.getText().length() - 1));
                isStarted = false;
                outView.append(")");
                --nob;
            } else if (lastCh == ')' && nob == 0) {
                outView.append("*(");
            } else if (lastCh == ')') {
                outView.append(")");
                --nob;
            }
        }
        else if (v.getId() == R.id.button7 || v.getId() == R.id.button8 ||
                v.getId() == R.id.button9 || v.getId() == R.id.button4 ||
                v.getId() == R.id.button5 || v.getId() == R.id.button6 ||
                v.getId() == R.id.button1 || v.getId() == R.id.button2 ||
                v.getId() == R.id.button3 || v.getId() == R.id.buttonπ ||
                v.getId() == R.id.buttonе || v.getId() == R.id.button0)
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
                if (lastCh == '%' || lastCh == '/' || lastCh == '*' || lastCh == '+' ||
                        lastCh == '^' || lastCh == '-' || lastCh == '.' ||
                        lastCh == ',' && v.getId() != R.id.buttonsub)
                {
                    outView.setText(outView.getText()
                            .subSequence(0, outView.getText().length() - 1));
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
            if (!isContextMenu) {
                char lastCh;
                if (outView.getText().length() != 0) {
                    lastCh = outView.getText().charAt(outView.getText().length() - 1);
                    if (lastCh >= '0' && lastCh <= '9'){
                        outView.append(".");
                    }
                    else if (lastCh == 'E') {} else {
                        outView.append("0.");
                    }
                } else {
                    outView.append("0.");
                }
                isStarted = true;
            }
            else {
                isContextMenu = false;
            }
        }
        else if (v.getId() == R.id.buttonsin || v.getId() == R.id.buttoncos ||
                v.getId() == R.id.buttontan || v.getId() == R.id.buttonln ||
                v.getId() == R.id.buttonsqrt ) {
            if (!isContextMenu || v.getId() == R.id.buttonln || v.getId() == R.id.buttonsqrt) {
                char lastCh;
                if (outView.getText().length() != 0) {
                    lastCh = outView.getText().charAt(outView.getText().length() - 1);
                    if (lastCh == '.') {
                        outView.setText(outView.getText()
                                .subSequence(0, outView.getText().length() - 1));
                    }
                }
                isStarted = false;
                outView.append(((Button) v).getText().toString() + "(");
                ++nob;
            } else {
                isContextMenu = false;
            }
        }
        else if (v.getId() == R.id.buttoncalc){
            try {
                double result;
                isAnswered = true;
                isStarted = false;
                if (outView.getText().length() != 0) {
                    char lastCh;
                    lastCh = outView.getText().charAt(outView.getText().length() - 1);
                    while (!(lastCh >= '0' && lastCh <= '9') && !(lastCh == '.') &&
                            !(lastCh >= 'a' && lastCh <= 'z') && !(lastCh == '_') &&
                            !(lastCh == ')') && !(lastCh == 'е') && !(lastCh == 'π'))
                    {
                        outView.setText(outView.getText()
                                .subSequence(0, outView.getText().length() - 1));
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
                        outView.setText(outView.getText()
                                .subSequence(0, outView.getText().length() - 1));
                    }
                    grayOutView.setText(outView.getText());
                    myGrayScroll.scrollTo(grayOutView.getRight(), 0);
                    myGrayScroll.scrollBy(-1, 0);
                    isGrayScroll = true;
                    if (outView.getText().length() != 0) {
                        result = calculator
                                .calc(outView.getText().toString(), null, functionsMap);
                        if (result - (int) result != 0.0 ||
                                !(result >= -32767.0 && result <= 32767.0))
                        {
                            outView.setText(String.valueOf(result));
                        } else {
                            outView.setText(String.valueOf((int) result));
                        }
                    }
                }
                else {
                    grayOutView.setText(outView.getText());
                    myGrayScroll.scrollTo(grayOutView.getRight(), 0);
                    myGrayScroll.scrollBy(-1, 0);
                    isGrayScroll = true;
                }
            }
            catch(Exception e) {
                outView.setText("Ошибка. ");
                isError = true;
                isAnswered = false;
            }
            catch (Error err) {
                outView.setText("Ошибка. ");
                isError = true;
                isAnswered = false;
            }
        }
        myScroll.scrollTo(outView.getRight(), 0);
        myScroll.scrollBy(-1, 0);
        isScroll = true;

    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("numberOfBrackets", nob);
        outState.putInt("buttonCounter", buttonCounter);
        outState.putBoolean("isStarted", isStarted);
        outState.putBoolean("isAnswered", isAnswered);
        outState.putBoolean("isError", isError);
        outState.putBoolean("isContextMenu", isContextMenu);
        outState.putBoolean("isScroll", isScroll);
        outState.putBoolean("isGrayScroll", isGrayScroll);
        outState.putSerializable("functionsMap", functionsMap);
        outState.putCharSequence("outView", outView.getText());
        outState.putCharSequence("grayOutView", grayOutView.getText());
        LinearLayout buttonShelf = findViewById(R.id.button_shelf);
        int count = buttonShelf.getChildCount();
        Button tmpButton;
        ArrayList<Button> buttons = new ArrayList<>();
        for (int i = count - 1; i > 0; --i){
            tmpButton = (Button) buttonShelf.getChildAt(i);
            buttons.add(tmpButton);
            buttonShelf.removeView(tmpButton);
        }
        outState.putSerializable("buttons", buttons);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        nob = savedInstanceState.getInt("numberOfBrackets");
        buttonCounter = savedInstanceState.getInt("buttonCounter");
        isStarted = savedInstanceState.getBoolean("isStarted");
        isAnswered = savedInstanceState.getBoolean("isAnswered");
        isError = savedInstanceState.getBoolean("isError");
        isContextMenu = savedInstanceState.getBoolean("isContextMenu");
        isScroll = savedInstanceState.getBoolean("isScroll");
        isGrayScroll = savedInstanceState.getBoolean("isGrayScroll");
        functionsMap = (HashMap<String, IFunction>) savedInstanceState
                .getSerializable("functionsMap");
        outView.setText(savedInstanceState.getCharSequence("outView"));
        grayOutView.setText(savedInstanceState.getCharSequence("grayOutView"));
        ArrayList<Button> buttons = (ArrayList<Button>) savedInstanceState.getSerializable("buttons");
        LinearLayout buttonShelf = findViewById(R.id.button_shelf);
        Button tmpButton;
        for (int i = buttons.size() - 1; i >= 0; --i) {
            tmpButton = buttons.get(i);
            buttonShelf.addView(tmpButton);
            buttonShelf.getChildAt(buttons.size() - i)
                    .setOnClickListener(functionButtonClickListener);
        }
    }
}
