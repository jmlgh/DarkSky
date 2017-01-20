package com.example.javi.darksky;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

public class AlertDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Algo ha ido mal! :(")
                .setMessage("Vuelve a intentarlo")
                .setPositiveButton("Ok", null);
        AlertDialog alerta = builder.create();
        return alerta;
    }
}
