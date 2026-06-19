package io.cordova.myapp00d753.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.metso.fragment.MetsoLeaveApplicationFragment;
import io.cordova.myapp00d753.fragment.ApplicationFragment;
import io.cordova.myapp00d753.fragment.ApproverFragment;
import io.cordova.myapp00d753.fragment.DetailsFragment;
import io.cordova.myapp00d753.fragment.LeaveAdjustmentFragment;
import io.cordova.myapp00d753.fragment.LeaveBalanceReportFragment;
import io.cordova.myapp00d753.utility.ClientID;
import io.cordova.myapp00d753.utility.Pref;


public class LeaveApplicationActivity extends AppCompatActivity {
    LinearLayout llApplication,llApproval,llDetails,llAdjustment,llLeaveBalanceReport;
    ImageView imgBack,imgHome;
    TextView tvApproval,tvDetails,tvApllication,tvToolBar,tvAdjustment,tvLeaveBalance;
    Pref pref;
    View view1,view2,view3,view4,view5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_application);
        initView();
        if (pref.getEmpClintId().equalsIgnoreCase(ClientID.METSO)) {
            loadMetsoApplicationFragment();
        }else {
            loadApplicationFragment();
        }
        onClick();
    }


    private void initView(){
        pref=new Pref(getApplicationContext());
        llApplication=(LinearLayout)findViewById(R.id.llApplication);
        llApproval=(LinearLayout)findViewById(R.id.llApproval);
        llDetails=(LinearLayout)findViewById(R.id.llDetails);
        llAdjustment=(LinearLayout)findViewById(R.id.llAdjustment);
        llLeaveBalanceReport=(LinearLayout)findViewById(R.id.llLeaveBalanceReport);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);

        tvApllication=(TextView)findViewById(R.id.tvApllication);
        tvDetails=(TextView)findViewById(R.id.tvDetails);
        tvApproval=(TextView)findViewById(R.id.tvApproval);
        tvAdjustment=(TextView)findViewById(R.id.tvAdjustment);
        tvLeaveBalance=(TextView)findViewById(R.id.tvLeaveBalance);
        tvToolBar=(TextView)findViewById(R.id.tvToolBar);

        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        view3 = findViewById(R.id.view3);
        view4 = findViewById(R.id.view4);
        view5 = findViewById(R.id.view5);

        if (pref.getEmpClintId().equals("AEMCLI0910000343") || pref.getEmpClintId().equals("AEMCLI0910000315")){
            llAdjustment.setVisibility(View.GONE);
        }else {
            llAdjustment.setVisibility(View.GONE);
        }


    }
    private void onClick(){
        llApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pref.getEmpClintId().equalsIgnoreCase(ClientID.METSO)) {
                    loadMetsoApplicationFragment();
                }else {
                    loadApplicationFragment();
                }
            }
        });
        llApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadApproverFragment();
            }
        });
        llDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDetailsFragment();
            }
        });

        llAdjustment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAdjustmentFragment();
            }
        });
        llLeaveBalanceReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadBalanceReport();
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
                Intent intent=new Intent(getApplicationContext(), EmployeeDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    public void loadApplicationFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        ApplicationFragment pfragment = new ApplicationFragment(); // for Metso Only
        //ApplicationFragment pfragment=new ApplicationFragment();
        transaction.replace(R.id.frameLayout, pfragment);
        transaction.commit();

        llApplication.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.colorPrimaryDark));
        llDetails.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.white));
        llApproval.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.white));
        llAdjustment.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.white));
        llLeaveBalanceReport.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.white));

        tvApllication.setTextColor(Color.parseColor("#FFFFFF"));
        tvDetails.setTextColor(Color.parseColor("#FFFFFF"));
        tvApproval.setTextColor(Color.parseColor("#FFFFFF"));
        tvAdjustment.setTextColor(Color.parseColor("#FFFFFF"));
        tvLeaveBalance.setTextColor(Color.parseColor("#FFFFFF"));
        tvToolBar.setText("Leave Application");
        menuLineVisibleInVisible(1);
   }

    public void loadMetsoApplicationFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        MetsoLeaveApplicationFragment pfragment = new MetsoLeaveApplicationFragment(); // for Metso Only
        //ApplicationFragment pfragment=new ApplicationFragment();
        transaction.replace(R.id.frameLayout, pfragment);
        transaction.commit();

        llApplication.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.colorPrimaryDark));
        llDetails.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.white));
        llApproval.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.white));
        llAdjustment.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.white));
        llLeaveBalanceReport.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.white));

        tvApllication.setTextColor(Color.parseColor("#FFFFFF"));
        tvDetails.setTextColor(Color.parseColor("#FFFFFF"));
        tvApproval.setTextColor(Color.parseColor("#FFFFFF"));
        tvAdjustment.setTextColor(Color.parseColor("#FFFFFF"));
        tvLeaveBalance.setTextColor(Color.parseColor("#FFFFFF"));
        tvToolBar.setText("Leave Application");
        menuLineVisibleInVisible(1);
    }

    public void loadApproverFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        ApproverFragment efr=new ApproverFragment();
        transaction.replace(R.id.frameLayout, efr);
        transaction.commit();

        llApplication.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.white));
        llDetails.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.white));
        llApproval.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.colorPrimaryDark));
        llAdjustment.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.white));
        llLeaveBalanceReport.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.white));


        tvApllication.setTextColor(Color.parseColor("#FFFFFF"));
        tvDetails.setTextColor(Color.parseColor("#FFFFFF"));
        tvApproval.setTextColor(Color.parseColor("#FFFFFF"));
        tvAdjustment.setTextColor(Color.parseColor("#FFFFFF"));
        tvLeaveBalance.setTextColor(Color.parseColor("#FFFFFF"));
        menuLineVisibleInVisible(3);
    }


    public void loadDetailsFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        DetailsFragment htfragment=new DetailsFragment();
        transaction.replace(R.id.frameLayout, htfragment);
        transaction.commit();

        llApplication.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.white));
        llDetails.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.colorPrimaryDark));
        llApproval.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.white));
        llAdjustment.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.white));
        llLeaveBalanceReport.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.white));


        tvApllication.setTextColor(Color.parseColor("#FFFFFF"));
        tvDetails.setTextColor(Color.parseColor("#FFFFFF"));
        tvApproval.setTextColor(Color.parseColor("#FFFFFF"));
        tvAdjustment.setTextColor(Color.parseColor("#FFFFFF"));
        tvLeaveBalance.setTextColor(Color.parseColor("#FFFFFF"));

        tvToolBar.setText("Leave Application Details");
        menuLineVisibleInVisible(2);
    }

    public void loadAdjustmentFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        LeaveAdjustmentFragment htfragment=new LeaveAdjustmentFragment();
        transaction.replace(R.id.frameLayout, htfragment);
        transaction.commit();

        llApplication.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.white));
        llDetails.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.white));
        llApproval.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.white));
        llAdjustment.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.colorPrimaryDark));
        llLeaveBalanceReport.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.white));


        tvApllication.setTextColor(Color.parseColor("#FFFFFF"));
        tvDetails.setTextColor(Color.parseColor("#FFFFFF"));
        tvApproval.setTextColor(Color.parseColor("#FFFFFF"));
        tvAdjustment.setTextColor(Color.parseColor("#FFFFFF"));
        tvLeaveBalance.setTextColor(Color.parseColor("#FFFFFF"));

        tvToolBar.setText("Leave Adjustment");
        menuLineVisibleInVisible(4);
    }

    public void loadBalanceReport() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        LeaveBalanceReportFragment htfragment=new LeaveBalanceReportFragment();
        transaction.replace(R.id.frameLayout, htfragment);
        transaction.commit();

        llApplication.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.white));
        llDetails.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.white));
        llApproval.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.white));
        llAdjustment.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.white));
        llLeaveBalanceReport.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.colorPrimaryDark));


        tvApllication.setTextColor(Color.parseColor("#FFFFFF"));
        tvDetails.setTextColor(Color.parseColor("#FFFFFF"));
        tvApproval.setTextColor(Color.parseColor("#FFFFFF"));
        tvAdjustment.setTextColor(Color.parseColor("#FFFFFF"));
        tvLeaveBalance.setTextColor(Color.parseColor("#FFFFFF"));

        tvToolBar.setText("Leave Balance Report");
        menuLineVisibleInVisible(5);
    }

    public void  approverVisibility(){
        llApproval.setVisibility(View.VISIBLE);
    }

    public void  approverHidden(){
        llApproval.setVisibility(View.GONE);
    }
    void menuLineVisibleInVisible(int isVisible){
        if (isVisible == 1){
            view1.setVisibility(View.VISIBLE);
            view2.setVisibility(View.GONE);
            view3.setVisibility(View.GONE);
            view4.setVisibility(View.GONE);
            view5.setVisibility(View.GONE);
        } else if(isVisible == 2){
            view1.setVisibility(View.GONE);
            view2.setVisibility(View.VISIBLE);
            view3.setVisibility(View.GONE);
            view4.setVisibility(View.GONE);
            view5.setVisibility(View.GONE);
        } else if(isVisible == 3){
            view1.setVisibility(View.GONE);
            view2.setVisibility(View.GONE);
            view3.setVisibility(View.VISIBLE);
            view4.setVisibility(View.GONE);
            view5.setVisibility(View.GONE);
        } else if (isVisible == 4){
            view1.setVisibility(View.GONE);
            view2.setVisibility(View.GONE);
            view3.setVisibility(View.GONE);
            view4.setVisibility(View.VISIBLE);
            view5.setVisibility(View.GONE);
        } else {
            view1.setVisibility(View.GONE);
            view2.setVisibility(View.GONE);
            view3.setVisibility(View.GONE);
            view4.setVisibility(View.GONE);
            view5.setVisibility(View.VISIBLE);
        }
    }
}
