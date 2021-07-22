package io.cordova.myapp00d753.activity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import io.cordova.myapp00d753.R;


public class EmpMapiingDashBoardActivity extends AppCompatActivity {
    LinearLayout llManage,llReport;
    ImageView imgBack,imgHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_mapiing_dash_board);
        initView();
        onClick();
    }

    private void initView(){
        llManage=(LinearLayout)findViewById(R.id.llManage);
        llReport=(LinearLayout)findViewById(R.id.llReport);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
    }
    private void onClick(){
        llManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EmpMapiingDashBoardActivity.this,EmpMappingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        llReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EmpMapiingDashBoardActivity.this,EmpMapingReportActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EmpMapiingDashBoardActivity.this,EmployeeDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
