package io.cordova.myapp00d753.utility;

import android.app.Activity;
import android.app.Dialog;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.cordova.myapp00d753.R;

public class MyChromeClient extends WebChromeClient {
    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog,
                                  boolean isUserGesture, Message resultMsg) {

        Dialog popupDialog = new Dialog(view.getContext(),
                android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        // Inflate custom layout
        View dialogView = LayoutInflater.from(view.getContext())
                .inflate(R.layout.popup_layout, null);

        popupDialog.setContentView(dialogView);

        WebView popupWebView = dialogView.findViewById(R.id.popupWebView);
        LinearLayout btnBack = dialogView.findViewById(R.id.btnBack);
        TextView titleText = dialogView.findViewById(R.id.titleText);

        WebSettings popupSettings = popupWebView.getSettings();
        popupSettings.setJavaScriptEnabled(true);
        popupSettings.setDomStorageEnabled(true);
        popupSettings.setLoadWithOverviewMode(true);
        popupSettings.setUseWideViewPort(true);

        popupWebView.setWebViewClient(new WebViewClient());
        popupWebView.setWebChromeClient(new WebChromeClient());

        // Back button closes popup
        btnBack.setOnClickListener(v -> {
            Activity activity = (Activity) view.getContext();
            activity.finish();   // closes current activity
        });

        popupDialog.show();

        // Return popup WebView to system
        WebView.WebViewTransport transport =
                (WebView.WebViewTransport) resultMsg.obj;
        transport.setWebView(popupWebView);
        resultMsg.sendToTarget();

        return true;
    }
}
