package io.cordova.myapp00d753.AndroidXCamera;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.cordova.myapp00d753.R;

public class CameraPermissionDialog {
    public interface OnResponseListeners {
        void onResponse();
    }
    public static void showDialog(Context context, OnResponseListeners mOnResponseListeners){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.camera_permission_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        dialog.setCancelable(false);
        TextView txtRetry = dialog.findViewById(R.id.txtRetry);
        txtRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnResponseListeners.onResponse();
                dialog.dismiss();
            }
        });
    }

}
