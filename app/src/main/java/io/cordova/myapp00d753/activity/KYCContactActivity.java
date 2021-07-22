package io.cordova.myapp00d753.activity;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import io.cordova.myapp00d753.R;

public class KYCContactActivity extends AppCompatActivity {
    LinearLayout llNext,llPrevious;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kyccontact);
        initialize();
        onClick();
    }
    private void initialize(){
        llNext=(LinearLayout)findViewById(R.id.llNext);
        llPrevious=(LinearLayout)findViewById(R.id.llPrevious);
    }

    private void onClick(){
        llNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(KYCContactActivity.this,KYCFamilyActivity.class);
                startActivity(intent);
            }
        });

        llPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
