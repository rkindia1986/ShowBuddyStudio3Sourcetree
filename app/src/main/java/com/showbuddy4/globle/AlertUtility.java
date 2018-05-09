package com.showbuddy4.globle;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by User on 27-04-2018.
 */

public class AlertUtility {

    public static void alertSingleChoise(Context context,String[]items,String title,String messaage,DialogInterface.OnClickListener listener){
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(messaage)
                .setSingleChoiceItems(items, 0, listener)

//                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        dialog.dismiss();
//                        int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
//                        // Do something useful withe the position of the selected radio button
//                    }
//                })
                .show();
    }
}
