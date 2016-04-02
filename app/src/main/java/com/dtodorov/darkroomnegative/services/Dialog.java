package com.dtodorov.darkroomnegative.services;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.dtodorov.darkroomnegative.R;


/**
 * Created by ditodoro on 4/1/2016.
 */
public class Dialog extends DialogFragment {

    private String _explanation;
    private IDialogListener _listener;

    public void setup(String explanation, IDialogListener listener) {
        _explanation = explanation;
        _listener = listener;
    }

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(_explanation)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(_listener != null) {
                            _listener.onOk();
                        }
                    }
                })
                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(_listener != null) {
                            _listener.onCancel();
                        }
                    }
                });
        return builder.create();
    }
}
