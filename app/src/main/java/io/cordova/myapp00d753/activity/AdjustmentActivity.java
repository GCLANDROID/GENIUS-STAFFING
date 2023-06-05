package io.cordova.myapp00d753.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.fragment.LeaveAdjustmentFragment;

public class AdjustmentActivity extends AppCompatActivity {
    ImageView imgBack,imgHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjustment);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdjustmentActivity.this,EmployeeDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        loadAdjustmentFragment();
    }


    public void loadAdjustmentFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        LeaveAdjustmentFragment htfragment=new LeaveAdjustmentFragment();
        transaction.replace(R.id.frameLayout, htfragment);
        transaction.commit();



    }
}