package com.example.asus1.teacherbyvideo.Util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.asus1.teacherbyvideo.R;

public class DialogBuilder {

    public interface DialogCallback{
        void certian();
        void cancle();
    }

    public static AlertDialog createMessageDialog(Context context,
                                           String title, String message,
                                           boolean nagative, final DialogCallback callback){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(context.getString(R.string.certain), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                callback.certian();
            }
        });
        if(nagative){
            builder.setNegativeButton(context.getString(R.string.cancle), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    callback.cancle();
                }
            });
        }

        return builder.create();
    }


    public static AlertDialog createSimpleDialog(Context context,
                                                  String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);

        return builder.create();
    }
}
