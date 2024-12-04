package io.cordova.myapp00d753.activity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;


import androidx.appcompat.app.AppCompatActivity;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.ODOMeterApprovalAdapter;
import io.cordova.myapp00d753.bluedart.ODOmeterApprvalActivity;
import io.cordova.myapp00d753.utility.Pref;

public class SupAttendanceActivity extends AppCompatActivity {
    LinearLayout llAttandanceManage,llAttendanceReport,llApproval,llQR,llODOmeter;
    ImageView imgBack,imgHome;
    Pref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sup_attendance);
        initialize();
        onClick();
    }

    private void initialize(){
        pref=new Pref(SupAttendanceActivity.this);
        llAttandanceManage=(LinearLayout)findViewById(R.id.llAttandanceManage);
        llAttendanceReport=(LinearLayout)findViewById(R.id.llAttendanceReport);
        llODOmeter=(LinearLayout)findViewById(R.id.llODOmeter);
        llApproval=(LinearLayout)findViewById(R.id.llApproval);
        llQR=(LinearLayout)findViewById(R.id.llQR);
        if (pref.getEmpClintId().equals("AEMCLI0910000315")){
            llQR.setVisibility(View.VISIBLE);
        }else {
            llQR.setVisibility(View.GONE);
        }

        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);

    }

    private void onClick(){
        if (pref.getEmpClintId().equals("AEMCLI1810001410")){
            llODOmeter.setVisibility(View.VISIBLE);
        } else {
            llODOmeter.setVisibility(View.GONE);
        }
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SupAttendanceActivity.this,SuperVisiorDashBoardActivity.class);
                startActivity(intent);
                 finish();
            }
        });
        llAttandanceManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(SupAttendanceActivity.this,SupAttenManageActivity.class);
                startActivity(intent);
            }
        });

        llAttendanceReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(SupAttendanceActivity.this,SupAttenReportActivity.class);
                startActivity(intent);
            }
        });

        llQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(SupAttendanceActivity.this,QRGeneratorActivity.class);
                startActivity(intent);
            }
        });

        llODOmeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(SupAttendanceActivity.this, ODOmeterApprvalActivity.class);
                startActivity(intent);
            }
        });

        llApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(SupAttendanceActivity.this,AttenApprovalActivity.class);
                startActivity(intent);

            }
        });



    }
}
