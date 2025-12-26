package io.cordova.myapp00d753.activity.NEW;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.EmployeeDashBoardActivity;
import io.cordova.myapp00d753.activity.NEW.fragment.NEW_LeaveAdjustmentFragment;
import io.cordova.myapp00d753.fragment.LeaveAdjustmentFragment;

public class NEW_AdjustmentActivity extends AppCompatActivity {
    ImageView imgBack,imgHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_adjustment);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(NEW_AdjustmentActivity.this, EmployeeDashBoardActivity.class);
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
        //loadAdjustmentFragment();
    }


    public void loadAdjustmentFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        NEW_LeaveAdjustmentFragment htfragment=new NEW_LeaveAdjustmentFragment();
        transaction.replace(R.id.frameLayout, htfragment);
        transaction.commit();
    }
}