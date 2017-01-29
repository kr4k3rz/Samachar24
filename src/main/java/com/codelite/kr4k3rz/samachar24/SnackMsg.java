package com.codelite.kr4k3rz.samachar24;

import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;


public class SnackMsg {


    static public void showMsgShort(View rootView, String msg) {
        Snackbar snackbar = Snackbar.make(rootView, "" + msg,
                Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(rootView.getContext(), R.color.accent));
        snackbar.show();

    }

    static public void showMsgLong(View rootView, String msg) {
        Snackbar snackbar = Snackbar.make(rootView, "" + msg,
                Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(rootView.getContext(), R.color.accent));
        snackbar.show();
    }
}
