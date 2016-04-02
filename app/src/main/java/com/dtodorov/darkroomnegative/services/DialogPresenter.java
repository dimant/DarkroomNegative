package com.dtodorov.darkroomnegative.services;

import android.app.FragmentManager;

/**
 * Created by ditodoro on 4/1/2016.
 */
public class DialogPresenter implements IDialogPresenter {
    private FragmentManager _fragmentManager;

    public DialogPresenter(FragmentManager fragmentManager) {
        _fragmentManager = fragmentManager;
    }

    @Override
    public void presentDialog(String explanation, IDialogListener listener) {
        Dialog dialog = new Dialog();
        dialog.setup(explanation, listener);
        dialog.show(_fragmentManager, explanation);
    }
}
