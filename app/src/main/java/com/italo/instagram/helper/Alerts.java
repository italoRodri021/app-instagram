package com.italo.instagram.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.italo.instagram.R;

public class Alerts {

    public static void showAlertDialog(Activity a, String title, String message) {

        AlertDialog.Builder alert = new AlertDialog.Builder(a);
        alert.setTitle(title)
                .setMessage(message)
                .setPositiveButton("CONTINUAR", null)
                .setCancelable(false)
                .create().show();

    }

    public static void showToast(Activity a, String message) {

        Toast.makeText(a, message, Toast.LENGTH_SHORT).show();

    }

    public static void showSnackBar(View v, String message) {

        Snackbar.make(v, message, BaseTransientBottomBar.LENGTH_LONG)
                .setBackgroundTint(v.getResources().getColor(R.color.white))
                .setTextColor(v.getResources().getColor(R.color.colorText))
                .show();

    }

}
