package com.mynew.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.mynew.R;

/**
 * Created by user on 2017/5/1.
 */
public class DialogUtil {

    public static Dialog createDialogProgress(Context context){
        View view= LayoutInflater.from(context).inflate(R.layout.progress_bar,null);
        RelativeLayout layout=(RelativeLayout)view.findViewById(R.id.layout);
        Dialog dialog=new Dialog(context,R.style.loading_dialog_tran);
        dialog.setContentView(layout,new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT));
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

}
