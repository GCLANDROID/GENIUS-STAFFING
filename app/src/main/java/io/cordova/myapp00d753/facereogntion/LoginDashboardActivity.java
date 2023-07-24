package io.cordova.myapp00d753.facereogntion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.LoginActivity;
import io.cordova.myapp00d753.databinding.ActivityLoginDashboardBinding;


public class LoginDashboardActivity extends AppCompatActivity {
     ActivityLoginDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_login_dashboard);
        binding.lnCandidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginDashboardActivity.this,CandidateLoginActivity.class);
                intent.putExtra("LoginFlag",1);
                startActivity(intent);

            }
        });

        binding.lnInterviewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginDashboardActivity.this, InterviewrLoginActivity.class);
                startActivity(intent);

            }
        });


        binding.lnHR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginDashboardActivity.this,HRLoginActivity.class);
                startActivity(intent);

            }
        });

        binding.lnEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginDashboardActivity.this,CandidateLoginActivity.class);
                intent.putExtra("LoginFlag",2);
                startActivity(intent);
                
            }
        });
    }
}