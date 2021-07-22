package io.cordova.myapp00d753.activity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;


import androidx.appcompat.app.AppCompatActivity;

import io.cordova.myapp00d753.R;

public class SupNotificationActivity extends AppCompatActivity {
    LinearLayout llManage,llReport;
    ImageView imgBack,imgHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sup_notification);
        initialize();
        onClick();
    }

    private void initialize(){
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        llManage=(LinearLayout)findViewById(R.id.llManage);
        llReport=(LinearLayout)findViewById(R.id.llReport);

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
                Intent intent=new Intent(SupNotificationActivity.this,EmployeeDashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //finish();
            }
        });

        llManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             Intent intent=new Intent(SupNotificationActivity.this,NotificationManageActivity.class);
             startActivity(intent);
            }
        });

        llReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(SupNotificationActivity.this,NotificationActivity.class);
                startActivity(intent);
            }
        });
    }
}
