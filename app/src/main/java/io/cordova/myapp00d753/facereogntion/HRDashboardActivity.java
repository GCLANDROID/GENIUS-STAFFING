package io.cordova.myapp00d753.facereogntion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.databinding.ActivityHrdashboardBinding;


public class HRDashboardActivity extends AppCompatActivity {
    ActivityHrdashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_hrdashboard);
        binding.lnInterview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.lnRegistration.setVisibility(View.VISIBLE);
                binding.lnInterview.setVisibility(View.GONE);
            }
        });

        binding.lnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HRDashboardActivity.this,HRActivity.class);
                startActivity(intent);

            }
        });
    }
}