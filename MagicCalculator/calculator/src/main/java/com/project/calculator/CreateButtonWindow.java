package com.project.calculator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;

// this class is dialog for creating a program-button
public class CreateButtonWindow extends DialogFragment {

    private EditText text;
    private EditText body;
    private Spinner paramNum;
    private UserProgramCompiler compiler;

    private TextWatcher textWatcher;

    public interface CreateButtonWindowListener {
        void onDialogPositiveClick(CreateButtonWindow dialog);
        void onDialogNegativeClick(CreateButtonWindow dialog);
    }

    private int TextCorrection(EditText textBox, Editable s, boolean isIncreased) {
        textBox.removeTextChangedListener(textWatcher);

        boolean isLineBegins = true;
        int lineBegin = 0;
        String text = s.toString();
        int spaceNum = 0;
        int shift = 0;
        for(int i = 0; i < text.length(); ++i) {
            if(isLineBegins) {
                if (text.charAt(i) == ' ' || text.charAt(i) == '\t') {
                    spaceNum++;
                }
                else {
                    isLineBegins = false;
                    if(spaceNum % 2 == 1) {
                        if(isIncreased) {
                            text = text.substring(0, i) + " " + text.substring(i, text.length());
                            ++i;
                            ++shift;
                        }
                        else {
                            text = text.substring(0, i - 1) + text.substring(i, text.length());
                            --i;
                            --shift;
                        }
                    }
                }
            }
            if(text.charAt(i) == '\n') {
                isLineBegins = true;
                lineBegin = i + 1;
                spaceNum = 0;
            }
            continue;
        }
        if(spaceNum % 2 == 1) {
            if (isIncreased) {
                text = text.substring(0, lineBegin) + " " + text.substring(lineBegin, text.length());
                ++shift;
            } else {
                String tempText;
                if (lineBegin != 0) {
                    tempText = text.substring(0, lineBegin - 1);

                    if (lineBegin < text.length()) {
                        tempText += text.substring(lineBegin, text.length());
                    }
                } else {
                    tempText = text.substring(1, text.length());
                }

                text = tempText;
                --shift;
            }
        }
        s.clear();
        s.append(text.subSequence(0, text.length()));
        textBox.addTextChangedListener(textWatcher);

        return shift;
    }

    private void ShowErrorMessage(String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Error")
                .setMessage(text)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    private void InitParamNumberSpinner(int pNum) {
        String[] data = new String[pNum];
        for(int i = 0; i < pNum; ++i) {
            data[i] = Integer.toString(i + 1) + " parameter" + ((i != 0) ? "s" : "");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.param_number_text, data);
        paramNum.setAdapter(adapter);
        paramNum.setPrompt("Func param num");
        paramNum.setSelection(paramNum.getFirstVisiblePosition());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater().inflate(R.layout.user_program_editor, null);
        text = view.findViewById(R.id.function_name);
        body = view.findViewById(R.id.function_body);
        paramNum = view.findViewById(R.id.func_param_num);

        InitParamNumberSpinner(5);

        paramNum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Button butCalc = getActivity().findViewById(R.id.buttoncalc);
                ColorDrawable butColor = (ColorDrawable)butCalc.getBackground();
                view.setBackgroundColor(butColor.getColor());

                ((TextView)view).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        textWatcher = new TextWatcher() {
            private int lastSize = 0;
            private int currentSize = 0;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                currentSize = after;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lastSize = before;
            }

            @Override
            public void afterTextChanged(Editable s) {
                int pos =  body.getSelectionStart();
                int shift =  TextCorrection(body, s, currentSize > lastSize);

                int newPos = (pos + shift < 0) ? 0 : pos + shift;

                body.setSelection(newPos);
            }
        };

        body.addTextChangedListener(textWatcher);

        builder.setView(view);

        builder.setMessage("Enter button name")
                // we'll add the pos click listener below
                // now it's null
        .setPositiveButton("OK", null)
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CreateButtonWindowListener activity;
                try{
                    activity = (CreateButtonWindowListener) getActivity();
                    activity.onDialogNegativeClick(CreateButtonWindow.this);
                }
                catch(ClassCastException e) {
                    throw new ClassCastException(getActivity().toString()
                            + " must implement CreateButtonWindowListener");
                }
            }
        });

        Dialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button posButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                final EditText editText = ((AlertDialog) dialog).findViewById(R.id.function_name);

                //
                posButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        CreateButtonWindowListener activity;
                        if (editText.getText().length() == 0) {
                            ShowErrorMessage("Enter function name");
                            return;
                        }

                        try {
                            activity = (CreateButtonWindowListener) getActivity();

                            // checking if the code compiles
                            // if it does, then close the window
                            // else - put the error message in new window (in catch)
                            activity.onDialogPositiveClick(CreateButtonWindow.this);
                            getDialog().dismiss();

                        } catch (ClassCastException e) {
                            throw new ClassCastException(getActivity().toString()
                                    + " must implement CreateButtonWindowListener");
                        } catch (Error err) {
                            ShowErrorMessage(err.getMessage());
                        } catch (android.net.ParseException e) {
                            ShowErrorMessage("Incorrect number of parameters");
                        }
                    }
                });
            }
        });

        return dialog;
    }

    public String GetText() {
        return text.getText().toString();
    }


    public String GetBody() {
        return body.getText().toString();
    }

    public int GetParamNum() {
        int pNum = paramNum.getSelectedItemPosition() + 1;
        return pNum;
    }

}

