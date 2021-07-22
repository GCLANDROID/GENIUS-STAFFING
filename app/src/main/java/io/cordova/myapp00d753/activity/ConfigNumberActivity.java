package io.cordova.myapp00d753.activity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.utility.Pref;


public class ConfigNumberActivity extends AppCompatActivity {
    LinearLayout llMultiple,llSingle;
    Pref pref;
    String menu;
    String s1,s2;
    ImageView imgBack,imgHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_number);
        initView();
        onClick();
    }

    private void initView() {
        pref = new Pref(getApplicationContext());
        llMultiple = (LinearLayout) findViewById(R.id.llMultiple);
        llSingle = (LinearLayout) findViewById(R.id.llSingle);
        /*menu = pref.getFenceSubMenu();
        if (pref.getFenceSubMenu().equals("")) {
            llMultiple.setVisibility(View.VISIBLE);
            llSingle.setVisibility(View.VISIBLE);

        } else {
            String d = menu.replace("{", "").replace("}", "");
            Log.d("split", d);
            Log.d("menuu", menu);
            String[] separated = menu.split(",");
            if (separated.length == 2) {
                Log.d("arpan", "riku");
                s1 = separated[0];
                s2 = separated[1];
                if (s1.equals("1")) {
                    llSingle.setVisibility(View.GONE);
                } else if (s1.equals("2")) {
                    llMultiple.setVisibility(View.GONE);

                }


                if (s2.equals("1")) {
                    llSingle.setVisibility(View.GONE);
                } else if (s2.equals("2")) {
                    llMultiple.setVisibility(View.GONE);

                }
            }
        }*/
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
    }

    private void onClick(){
        llMultiple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ConfigNumberActivity.this,FencingDashBoardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("point","mul");
                startActivity(intent);
            }
        });

        llSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ConfigNumberActivity.this,FencingDashBoardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("point","sin");
                startActivity(intent);
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),EmployeeDashBoardActivity.class);
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
    }


}
