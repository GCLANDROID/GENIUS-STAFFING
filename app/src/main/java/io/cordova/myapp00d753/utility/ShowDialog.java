package io.cordova.myapp00d753.utility;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.attendance.MetsoAttendanceActivity;

public class ShowDialog {
    private static AlertDialog errorAlertDialog,successaAlertDialog;

    public static void showSuccessDialog(Context context, String text,ResultListener resultListener) {
        Dialog dialogView = new Dialog(context,R.style.CustomDialogNew2);
        dialogView.setContentView(R.layout.dialog_success);
        dialogView.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogView.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView tvSuccess = (TextView) dialogView.findViewById(R.id.tvSuccess);
        tvSuccess.setText(text);
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogView.cancel();
                resultListener.onSuccess();

            }
        });
        dialogView.show();
    }

    public static void showErrorDialog(Context context,String text) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.error_ayput, null);
        dialogBuilder.setView(dialogView);
        TextView tvError = (TextView) dialogView.findViewById(R.id.tvError);
        tvError.setText(text);
        ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorAlertDialog.dismiss();
            }
        });

        errorAlertDialog = dialogBuilder.create();
        errorAlertDialog.setCancelable(false);
        Window window = errorAlertDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        errorAlertDialog.show();
    }

    public static void showAlertDialog(Context context,String text,ResultListener resultListener) {
        Dialog dialog = new Dialog(context,R.style.CustomDialogNew2);
        dialog.setContentView(R.layout.alert_dialog_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView tvError = (TextView) dialog.findViewById(R.id.tvError);
        tvError.setText(text);
        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //errorAlertDialog.dismiss();
                dialog.cancel();
                resultListener.onSuccess();
            }
        });
        dialog.show();
    }




    public static void onDismiss(){
        if (successaAlertDialog != null  || successaAlertDialog.isShowing()){
            successaAlertDialog.dismiss();
        }
    }

    public interface ResultListener{
        void onSuccess();
    }

}
