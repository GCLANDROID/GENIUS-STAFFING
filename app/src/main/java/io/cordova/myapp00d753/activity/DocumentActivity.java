package io.cordova.myapp00d753.activity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;


import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;
import io.cordova.myapp00d753.utility.Pref;

public class DocumentActivity extends AppCompatActivity {
    private static final String TAG = "DocumentActivity";
    ImageView imgBack,imgHome;
    LinearLayout llManage,llReport;
    NetworkConnectionCheck connectionCheck;

    Pref pref;

    String from="";

    ImageView imgManage,imgReport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        initialize();
        onClick();
    }

    private  void initialize(){
        if (getIntent().getExtras() != null){
            from = getIntent().getExtras().getString("from");
            Log.e(TAG, "from: "+from);
        } else {
            Log.e(TAG, "from: Null");
        }
        pref=new Pref(DocumentActivity.this);
        connectionCheck=new NetworkConnectionCheck(DocumentActivity.this);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        llManage=(LinearLayout)findViewById(R.id.llManage);
        llReport=(LinearLayout)findViewById(R.id.llReport);

        imgReport=findViewById(R.id.imgReport);
        imgManage=findViewById(R.id.imgManage);

        Glide.with(DocumentActivity.this)
                .asGif()
                .load(R.drawable.manage_giff)
                .into( imgManage);

        Glide.with(DocumentActivity.this)
                .asGif()
                .load(R.drawable.chart_giff)
                .into( imgReport);
    }

    private void onClick(){
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pref.getUserType().equals("1")) {
                    Intent intent = new Intent(DocumentActivity.this, EmployeeDashBoardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else if (pref.getUserType().equals("4")){
                    Intent intent = new Intent(DocumentActivity.this, TempDashBoardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
               // finish();
            }
        });

        llManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionCheck.isNetworkAvailable()) {
                    Intent intent = new Intent(DocumentActivity.this, DocumentNumberActivity.class);
                    if (from.equals(TempDashBoardActivity.TEMP_DASHBOARD)){
                        intent.putExtra("from",from);
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    connectionCheck.getNetworkActiveAlert().show();
                }
            }
        });

        llReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionCheck.isNetworkAvailable()) {
                    Intent intent = new Intent(DocumentActivity.this, DocumentReportActivity.class);
                    intent.putExtra("status","All");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    connectionCheck.getNetworkActiveAlert().show();
                }
            }
        });
    }



}
