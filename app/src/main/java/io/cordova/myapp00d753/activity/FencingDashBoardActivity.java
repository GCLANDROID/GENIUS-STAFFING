package io.cordova.myapp00d753.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.utility.Pref;


public class FencingDashBoardActivity extends AppCompatActivity {
    LinearLayout llFenceConfig, llFence, llConfig, llReport, llEMap;
    ImageView imgBack, imgHome;
    Pref pref;
    ProgressDialog pd, pd1;
    LinearLayout llMapping;
    LinearLayout llGeoConfig;
    String point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fencing_dash_board);
        initView();
        onClick();


    }

    @Override
    protected void onPause() {
        super.onPause();
        pd.dismiss();
        pd1.dismiss();
    }

    private void onClick(){
        llFence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pd1.setMessage("Loading..");
                pd1.setCancelable(false);
                pd1.show();
                Intent intent = new Intent(FencingDashBoardActivity.this, GeoFenceManageDashBoardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("point",point);
                startActivity(intent);


            }
        });


        llFenceConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setMessage("Loading...");
                pd.show();
                pd.setCancelable(false);
                Intent intent = new Intent(FencingDashBoardActivity.this, FenceNumberActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("point",point);
                startActivity(intent);
            }
        });



        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        llReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FencingDashBoardActivity.this, ReportActivityActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EmployeeDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        llMapping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FencingDashBoardActivity.this, EmpMapiingDashBoardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("point",point);
                startActivity(intent);
            }
        });

    }
    private void initView(){
        pref = new Pref(getApplicationContext());
        pd = new ProgressDialog(FencingDashBoardActivity.this);
        pd1 = new ProgressDialog(FencingDashBoardActivity.this);
        llFenceConfig = (LinearLayout) findViewById(R.id.llFenceConfig);
        llFence = (LinearLayout) findViewById(R.id.llFence);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        llReport = (LinearLayout) findViewById(R.id.llReport);
        llGeoConfig = (LinearLayout) findViewById(R.id.llGeoConfig);
        llMapping = (LinearLayout) findViewById(R.id.llMapping);
        imgBack = (ImageView) findViewById(R.id.imgBack);

      /*  if (pref.getGeoFenceFlag().equals("1")) {
            llFence.setVisibility(View.VISIBLE);
        } else {
            llFence.setVisibility(View.GONE);
        }


        if (pref.getGeoFenceConfig().equals("1")) {
            llGeoConfig.setVisibility(View.VISIBLE);
        } else {
            llGeoConfig.setVisibility(View.GONE);
        }*/
        point=getIntent().getStringExtra("point");

    }
}
