package io.cordova.myapp00d753.activity;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import io.cordova.myapp00d753.R;

public class KYCDashBoardActivity extends AppCompatActivity {
    LinearLayout llManage,llUpdate,llReport,llManageD,llManageD1,llUpdateD,llUpdateD1,llReportD,llReportD1;
    ImageView imgBack,imgHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kycdash_board);
        initialize();
        onClick();
    }

    private void initialize(){
        llManage=(LinearLayout)findViewById(R.id.llManage);
        llManageD=(LinearLayout)findViewById(R.id.llManageD);
        llManageD1=(LinearLayout)findViewById(R.id.llManageD1);

        llUpdate=(LinearLayout)findViewById(R.id.llUpdate);
        llUpdateD=(LinearLayout)findViewById(R.id.llUpdateD);
        llUpdateD1=(LinearLayout)findViewById(R.id.llUpdateD1);

        llReport=(LinearLayout)findViewById(R.id.llReport);
        llReportD=(LinearLayout)findViewById(R.id.llReportD);
        llReportD1=(LinearLayout)findViewById(R.id.llReportD1);
    }

    private void onClick(){
        llManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llManageD.setVisibility(View.GONE);
                llManageD1.setVisibility(View.VISIBLE);

                llUpdateD.setVisibility(View.VISIBLE);
                llUpdateD1.setVisibility(View.GONE);

                llReportD.setVisibility(View.VISIBLE);
                llReportD1.setVisibility(View.GONE);
                Intent intent=new Intent(KYCDashBoardActivity.this,KYCManageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}
