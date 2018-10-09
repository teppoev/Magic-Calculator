package com.example.dynamicbuttons;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class CreateButtonWindow extends DialogFragment {

    private EditText text;
    private EditText body;

    public interface CreateButtonWindowListener {
        public void onDialogPositiveClick(CreateButtonWindow dialog);
        public void onDialogNegativeClick(CreateButtonWindow dialog);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater().inflate(R.layout.layout, null);
        text = (EditText) view.findViewById(R.id.function_name);
        body = (EditText) view.findViewById(R.id.function_body);

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

