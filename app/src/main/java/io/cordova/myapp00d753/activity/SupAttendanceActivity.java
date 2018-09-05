package io.cordova.myapp00d753.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;



import io.cordova.myapp00d753.R;

public class SupAttendanceActivity extends AppCompatActivity {
    LinearLayout llAttandanceManage,llAttendanceReport,llApproval,llConso;
    ImageView imgBack,imgHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sup_attendance);
        initialize();
        onClick();
    }

    private void initialize(){
        llAttandanceManage=(LinearLayout)findViewById(R.id.llAttandanceManage);
        llAttendanceReport=(LinearLayout)findViewById(R.id.llAttendanceReport);
        llApproval=(LinearLayout)findViewById(R.id.llApproval);
        llConso=(LinearLayout)findViewById(R.id.llConso);

        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
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
                Intent intent=new Intent(SupAttendanceActivity.this,DashBoardActivity.class);
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

        llApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SupAttendanceActivity.this,AttenApprovalActivity.class);
                startActivity(intent);
            }
        });



    }
}
