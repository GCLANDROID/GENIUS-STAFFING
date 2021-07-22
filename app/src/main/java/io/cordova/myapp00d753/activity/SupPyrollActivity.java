package io.cordova.myapp00d753.activity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;


import androidx.appcompat.app.AppCompatActivity;

import io.cordova.myapp00d753.R;

public class SupPyrollActivity extends AppCompatActivity {
    ImageView imgback,imgHome;
    LinearLayout llSalary,llCTC;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sup_pyroll);
        initialize();
        onClick();
    }

    private void initialize(){
        llSalary=(LinearLayout)findViewById(R.id.llSalary);
        llCTC=(LinearLayout)findViewById(R.id.llCTC);
        imgback=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);

    }

    private void onClick(){
        llSalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(SupPyrollActivity.this,SupSalaryActivity.class);
                startActivity(intent);

            }
        });

        llCTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(SupPyrollActivity.this,SupCTCActivity.class);
                startActivity(intent);
            }
        });


       imgback.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               onBackPressed();
           }
       });

       imgHome.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent=new Intent(SupPyrollActivity.this,SuperVisiorDashBoardActivity.class);
               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
               startActivity(intent);
              // finish();
           }
       });
    }
}
