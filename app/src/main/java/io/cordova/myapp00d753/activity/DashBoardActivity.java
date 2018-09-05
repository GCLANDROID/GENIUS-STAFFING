package io.cordova.myapp00d753.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;



import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;
import io.cordova.myapp00d753.utility.Pref;

public class DashBoardActivity extends AppCompatActivity {
    Button btnSignIn;
    Pref pref;
    LinearLayout llAbout,llServices,llContact;
    NetworkConnectionCheck connectionCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        initialize();
        onClick();
    }

    private void initialize(){
        String   security="0000";
        String d=security.replace("\"", "");
        Log.d("de",security);
        pref=new Pref(DashBoardActivity.this);

        btnSignIn=(Button)findViewById(R.id.btnSignIn);
        llAbout=(LinearLayout)findViewById(R.id.llAbout);
        llServices=(LinearLayout)findViewById(R.id.llServices);
        llContact=(LinearLayout)findViewById(R.id.llContact);
        connectionCheck=new NetworkConnectionCheck(DashBoardActivity.this);
    }

    private void onClick(){
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (pref.getEmpId().equals("")) {
                    Intent intent = new Intent(DashBoardActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                }else {
                    if (pref.getUserType().equals("1")) {
                        Intent intent = new Intent(DashBoardActivity.this, EmployeeDashBoardActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }else  if (pref.getUserType().equals("2")){
                        Intent intent = new Intent(DashBoardActivity.this, SuperVisiorDashBoardActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }


            }
        });

        llAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionCheck.isNetworkAvailable()) {
                    Intent intent = new Intent(DashBoardActivity.this, AboutUsActivity.class);
                    startActivity(intent);
                }else {
                    connectionCheck.getNetworkActiveAlert().show();
                }
            }
        });

        llServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionCheck.isNetworkAvailable()) {
                    Intent intent = new Intent(DashBoardActivity.this, ServicesActivity.class);
                    startActivity(intent);
                }else {
                    connectionCheck.getNetworkActiveAlert().show();
                }
            }
        });

        llContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionCheck.isNetworkAvailable()) {
                    Intent intent = new Intent(DashBoardActivity.this, ContactUsActivity.class);
                    startActivity(intent);
                }else {
                    connectionCheck.getNetworkActiveAlert().show();
                }
            }
        });

    }


}
