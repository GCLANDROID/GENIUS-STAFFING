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
import io.cordova.myapp00d753.utility.Pref;


public class LeaveApplicationActivity extends AppCompatActivity {
    LinearLayout llApplication,llApproval,llDetails,llAdjustment;
    ImageView imgBack,imgHome;
    TextView tvApproval,tvDetails,tvApllication,tvToolBar,tvAdjustment;
    Pref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_application);
        initView();
        if (pref.getEmpClintId().equalsIgnoreCase("AEMCLI2110001671")) {
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
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);

        tvApllication=(TextView)findViewById(R.id.tvApllication);
        tvDetails=(TextView)findViewById(R.id.tvDetails);
        tvApproval=(TextView)findViewById(R.id.tvApproval);
        tvAdjustment=(TextView)findViewById(R.id.tvAdjustment);
        tvToolBar=(TextView)findViewById(R.id.tvToolBar);

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
                loadApplicationFragment();
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


        tvApllication.setTextColor(Color.parseColor("#FFFFFF"));
        tvDetails.setTextColor(Color.parseColor("#4f8888"));
        tvApproval.setTextColor(Color.parseColor("#4f8888"));
        tvAdjustment.setTextColor(Color.parseColor("#4f8888"));
        tvToolBar.setText("Leave Application");

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


        tvApllication.setTextColor(Color.parseColor("#FFFFFF"));
        tvDetails.setTextColor(Color.parseColor("#4f8888"));
        tvApproval.setTextColor(Color.parseColor("#4f8888"));
        tvAdjustment.setTextColor(Color.parseColor("#4f8888"));
        tvToolBar.setText("Leave Application");

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


        tvApllication.setTextColor(Color.parseColor("#4f8888"));
        tvDetails.setTextColor(Color.parseColor("#4f8888"));
        tvApproval.setTextColor(Color.parseColor("#FFFFFF"));
        tvAdjustment.setTextColor(Color.parseColor("#4f8888"));
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


        tvApllication.setTextColor(Color.parseColor("#4f8888"));
        tvDetails.setTextColor(Color.parseColor("#FFFFFF"));
        tvApproval.setTextColor(Color.parseColor("#4f8888"));
        tvAdjustment.setTextColor(Color.parseColor("#4f8888"));

        tvToolBar.setText("Leave Application Details");

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


        tvApllication.setTextColor(Color.parseColor("#4f8888"));
        tvDetails.setTextColor(Color.parseColor("#4f8888"));
        tvApproval.setTextColor(Color.parseColor("#4f8888"));
        tvAdjustment.setTextColor(Color.parseColor("#FFFFFF"));

        tvToolBar.setText("Leave Adjustment");

    }

    public void  approverVisibility(){
        llApproval.setVisibility(View.VISIBLE);
    }

    public void  approverHidden(){
        llApproval.setVisibility(View.GONE);
    }
}
