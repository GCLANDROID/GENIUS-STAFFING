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
import io.cordova.myapp00d753.fragment.ApplicationFragment;
import io.cordova.myapp00d753.fragment.ApproverFragment;
import io.cordova.myapp00d753.fragment.DetailsFragment;
import io.cordova.myapp00d753.utility.Pref;


public class LeaveApplicationActivity extends AppCompatActivity {
    LinearLayout llApplication,llApproval,llDetails;
    ImageView imgBack,imgHome;
    TextView tvApproval,tvDetails,tvApllication,tvToolBar;
    Pref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_application);
        initView();
        loadApplicationFragment();
        onClick();
    }


    private void initView(){
        pref=new Pref(getApplicationContext());
        llApplication=(LinearLayout)findViewById(R.id.llApplication);
        llApproval=(LinearLayout)findViewById(R.id.llApproval);
        llDetails=(LinearLayout)findViewById(R.id.llDetails);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);

        tvApllication=(TextView)findViewById(R.id.tvApllication);
        tvDetails=(TextView)findViewById(R.id.tvDetails);
        tvApproval=(TextView)findViewById(R.id.tvApproval);
        tvToolBar=(TextView)findViewById(R.id.tvToolBar);

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
        ApplicationFragment pfragment=new ApplicationFragment();
        transaction.replace(R.id.frameLayout, pfragment);
        transaction.commit();

        tvApllication.setTextColor(Color.parseColor("#075994"));
        tvDetails.setTextColor(Color.parseColor("#4f8888"));
        tvApproval.setTextColor(Color.parseColor("#4f8888"));
        tvToolBar.setText("Leave Application");

   }

    public void loadApproverFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        ApproverFragment efr=new ApproverFragment();
        transaction.replace(R.id.frameLayout, efr);
        transaction.commit();

        tvDetails.setTextColor(Color.parseColor("#4f8888"));
        tvApllication.setTextColor(Color.parseColor("#4f8888"));
        tvApproval.setTextColor(Color.parseColor("#075994"));
    }


    public void loadDetailsFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        DetailsFragment htfragment=new DetailsFragment();
        transaction.replace(R.id.frameLayout, htfragment);
        transaction.commit();

        tvDetails.setTextColor(Color.parseColor("#075994"));
        tvApllication.setTextColor(Color.parseColor("#4f8888"));
        tvApproval.setTextColor(Color.parseColor("#4f8888"));

        tvToolBar.setText("Leave Details");

    }

    public void  approverVisibility(){
        llApproval.setVisibility(View.VISIBLE);
    }

    public void  approverHidden(){
        llApproval.setVisibility(View.GONE);
    }
}
