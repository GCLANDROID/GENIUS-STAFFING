package io.cordova.myapp00d753.activity;

import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.utility.Pref;

public class PFDashBoardActivity extends AppCompatActivity {
    LinearLayout llConsolidated,llMonth;
    Pref pref;
    ImageView imgBack,imgHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_f_dash_board);
        initView();
        onClick();
    }

    private void initView(){
        pref=new Pref(PFDashBoardActivity.this);
        llConsolidated=(LinearLayout)findViewById(R.id.llConsolidated);
        llMonth=(LinearLayout)findViewById(R.id.llMonth);

        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        if (!pref.getPFURL().equals("")){
            llConsolidated.setVisibility(View.VISIBLE);
        }else {
            llConsolidated.setVisibility(View.GONE);

        }
    }

    private void onClick(){
        llMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PFDashBoardActivity.this,PFActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        llConsolidated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBrowser();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PFDashBoardActivity.this,EmployeeDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void openBrowser(){
        Uri uri = Uri.parse(pref.getPFURL()); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
