package io.cordova.myapp00d753.activity;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.SaleApprovalReportAdapter;

public class SaleApprovalDashboardActivity extends AppCompatActivity {
    LinearLayout llManage,llReport;
    ImageView imgBack,imgHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_approval_dashboard);
        initview();
    }

    private void initview(){
        llManage=(LinearLayout)findViewById(R.id.llManage);
        llReport=(LinearLayout)findViewById(R.id.llReport);

        llManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SaleApprovalDashboardActivity.this,ConApprovalActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        llReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SaleApprovalDashboardActivity.this, SaleArpprovalReportActivity.class);
                intent.putExtra("zoneId","0");
                intent.putExtra("branchId","0");
                intent.putExtra("zoneName","All");
                intent.putExtra("xy","0");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SaleApprovalDashboardActivity.this, SuperVisiorDashBoardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }
}