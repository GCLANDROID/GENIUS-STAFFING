package io.cordova.myapp00d753.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import io.cordova.myapp00d753.R;

public class KYCMisActivity extends AppCompatActivity {
    LinearLayout llPrevious;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kycmis);
        initialize();
        onClick();
    }
    private void initialize(){
        llPrevious=(LinearLayout)findViewById(R.id.llPrevious);
    }

    private void onClick(){
        llPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
