package com.project.calculator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

public class CreateButtonWindow extends DialogFragment {

    private EditText text;
    private EditText body;

    private TextWatcher textWatcher;

    public interface CreateButtonWindowListener {
        public void onDialogPositiveClick(CreateButtonWindow dialog);
        public void onDialogNegativeClick(CreateButtonWindow dialog);
    }

    void TextCorrection(EditText textBox, Editable s, boolean isIncreased) {
        textBox.removeTextChangedListener(textWatcher);

        boolean isLineBegins = true;
        int lineBegin = 0;
        String text = s.toString();
        int spaceNum = 0;
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
                        }
                        else {
                            text = text.substring(0, i - 1) + text.substring(i, text.length());
                            --i;
                        }
                    }
                }
            }
            if(text.charAt(i) == '\n') {
                isLineBegins = true;
                lineBegin = i + 1;
                spaceNum = 0;
            }
        }
        if(spaceNum % 2 == 1) {
            if (isIncreased) {
                text = text.substring(0, lineBegin) + " " + text.substring(lineBegin, text.length());
            } else {
                text = text.substring(0, lineBegin - 1) + text.substring(lineBegin, text.length());
            }
        }
        s.clear();
        s.append(text.subSequence(0, text.length()));
        textBox.addTextChangedListener(textWatcher);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater().inflate(R.layout.user_program_editor, null);
        text = (EditText) view.findViewById(R.id.function_name);
        body = (EditText) view.findViewById(R.id.function_body);

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int pos =  body.getSelectionStart();
                TextCorrection(body, s, true);
                body.setSelection(pos);
            }
        };

        body.addTextChangedListener(textWatcher);

        builder.setView(view);

        builder.setMessage("Enter button name")
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CreateButtonWindowListener activity;
                try{
                    activity = (CreateButtonWindowListener) getActivity();
                    activity.onDialogPositiveClick(CreateButtonWindow.this);
                }
                catch(ClassCastException e) {
                    throw new ClassCastException(getActivity().toString()
                            + " must implement CreateButtonWindowListener");
                }
            }
        })
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

        return builder.create();
    }

    public String GetText() {
        return text.getText().toString();
    }

    public String GetBody() {
        return body.getText().toString();
    }

}

