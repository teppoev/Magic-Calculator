package com.project.calculator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

// this class is dialog for creating a program-button
public class CreateButtonWindow extends DialogFragment {

    private EditText functionNameView;
    private EditText bodyTextView;
    private TextView paramNumView;
    private View currActivityView;
    private TextWatcher textWatcher;

    private int paramNum = 1;

    private String positiveButtonText = "Save";
    private String negativeButtonText = "Cancel";

    private String mode;

    private String oldName = "";

    public interface CreateButtonWindowListener {
        void onDialogPositiveClick(CreateButtonWindow dialog);
        void onDialogNegativeClick(CreateButtonWindow dialog);
    }

    private void ParamNumberViewInit() {

        Button increaseParamNum;
        Button decreaseParamNum;

        paramNumView = currActivityView.findViewById(R.id.func_param_num);
        increaseParamNum = currActivityView.findViewById(R.id.plus_arg_button);
        decreaseParamNum = currActivityView.findViewById(R.id.minus_arg_button);

        increaseParamNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paramNum++;
                paramNumView.append(Integer.toString(paramNum));
            }
        });

        decreaseParamNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(paramNum > 1) {
                    paramNum--;
                    paramNumView.append(Integer.toString(paramNum));
                }
            }
        });

    }

    private void FunctionNameViewInit() {
        functionNameView = currActivityView.findViewById(R.id.function_name);

        switch(mode) {
            case "Edit":
                functionNameView.setText(getArguments().getString("Name"));
                break;
            case "Create":
                break;
        }
    }

    private void BodyTextViewInit() {
        bodyTextView = currActivityView.findViewById(R.id.function_body);

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
                int pos =  bodyTextView.getSelectionStart();
                int shift =  TextCorrection(bodyTextView, s, currentSize > lastSize);

                int newPos = (pos + shift < 0) ? 0 : pos + shift;

                bodyTextView.setSelection(newPos);
            }
        };

        bodyTextView.addTextChangedListener(textWatcher);

        switch(mode) {
            case "Edit":
                bodyTextView.setText(getArguments().getString("Body"));
                break;
            case "Create":
                break;
        }
    }

    private boolean CheckFunctionNameNotEmpty() {
        return functionNameView.length() != 0;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mode = getArguments().getString("Mode");

        if(mode.equals("Edit")) {
            oldName = getArguments().getString("Name");
        }

        // base initialization
        currActivityView = getActivity().getLayoutInflater().inflate(R.layout.user_program_editor, null);

        ParamNumberViewInit();
        FunctionNameViewInit();
        BodyTextViewInit();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);

        builder.setView(currActivityView);
        builder.setMessage("Creating new function")
                .setPositiveButton(positiveButtonText, null)
                .setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        CreateButtonWindowListener activity;
                        try {
                            activity = (CreateButtonWindowListener) getActivity();
                            activity.onDialogNegativeClick(CreateButtonWindow.this);
                        } catch (ClassCastException e) {
                            throw new ClassCastException(getActivity().toString()
                                    + " must implement CreateButtonWindowListener");
                        }
                    }
                });

        Dialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                Button positiveButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);

                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!CheckFunctionNameNotEmpty()) {
                            ShowErrorMessage("Enter function name");
                            return;
                        }

                        CreateButtonWindowListener activity;

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

    public String GetName() {
        return functionNameView.getText().toString();
    }

    public String GetOldName() {
        return oldName;
    }

    public String GetBody() {
        return bodyTextView.getText().toString();
    }

    public int GetParamNum() {
        int pNum = Integer.parseInt(paramNumView.getText().toString());
        return pNum;
    }

    public String GetMode() {
        return mode;
    }

}

