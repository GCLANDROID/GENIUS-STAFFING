package io.cordova.myapp00d753.activity;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.metso.MetsoNewReimbursementClaimActivity;
import io.cordova.myapp00d753.activity.metso.MetsoReimbursementDeleteActivity;
import io.cordova.myapp00d753.utility.ClientID;
import io.cordova.myapp00d753.utility.Pref;

public class RemManageDashBoardActivity extends AppCompatActivity {
    ImageView imgBack, imgHome;
    LinearLayout llManage, llDelete;
    Pref pref;
    ImageView imgManage,imgDelete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rem_manage_dash_board);
        initialize();
        onClick();
    }

    private void initialize() {
        pref = new Pref(getApplicationContext());
        llManage = (LinearLayout) findViewById(R.id.llManage);

        llDelete = (LinearLayout) findViewById(R.id.llDelete);

        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imhHome);

        imgManage = (ImageView) findViewById(R.id.imgManage);
        imgDelete = (ImageView) findViewById(R.id.imgDelete);


        Glide.with(RemManageDashBoardActivity.this)
                .asGif()
                .load(R.drawable.claim_manage_giff)
                .into( imgManage);

        Glide.with(RemManageDashBoardActivity.this)
                .asGif()
                .load(R.drawable.delete_giff)
                .into( imgDelete);


    }

    private void onClick() {
        llManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pref.getEmpClintId().equals("AEMCLI1110000501")) {
                    Intent intent = new Intent(RemManageDashBoardActivity.this, RecktitRemActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (pref.getEmpClintId().equals(ClientID.METSO)) {
                    Intent intent = new Intent(RemManageDashBoardActivity.this, MetsoNewReimbursementClaimActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (pref.getSecurityCode().equals("222")) {
                    Intent intent = new Intent(RemManageDashBoardActivity.this, FMSNewClaimActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(RemManageDashBoardActivity.this, NewClaimActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });


        llDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pref.getEmpClintId().equals(ClientID.METSO)) {
                    Intent intent = new Intent(RemManageDashBoardActivity.this, MetsoReimbursementDeleteActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(RemManageDashBoardActivity.this, ClaimDeletActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RemManageDashBoardActivity.this, EmployeeDashBoardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}
