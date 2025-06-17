package io.cordova.myapp00d753.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.attendance.AttenDanceDashboardActivity;
import io.cordova.myapp00d753.fragment.LeaveBalanceReportFragment;

public class ViewLeaveBalanceActivity extends AppCompatActivity {
    LinearLayout llApplication,llApproval,llDetails,llAdjustment,llLeaveBalanceReport;
    ImageView imgBack,imgHome;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_leave_balance);
        initView();

    }

    private void initView() {
        imgBack = findViewById(R.id.imgBack);
        imgHome = findViewById(R.id.imgHome);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewLeaveBalanceActivity.this, EmployeeDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        loadBalanceReport();
    }


    public void loadBalanceReport() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        LeaveBalanceReportFragment htfragment=new LeaveBalanceReportFragment();
        transaction.replace(R.id.frameLayout, htfragment);
        transaction.commit();
    }
}
