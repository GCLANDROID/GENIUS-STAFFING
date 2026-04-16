package io.cordova.myapp00d753.activity.murugappa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.databinding.ActivityAttendanceDashboardBinding;

public class AttendanceDashboardActivity extends AppCompatActivity {
    ActivityAttendanceDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil. setContentView(this,R.layout.activity_attendance_dashboard);
       initView();
    }

    private void initView(){
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.llCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AttendanceDashboardActivity.this, CheckInAttendanceActivity.class));
            }
        });

        binding.llCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AttendanceDashboardActivity.this, CheckOutAttendanceActivity.class));
            }
        });
    }
}