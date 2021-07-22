package io.cordova.myapp00d753.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.fragment.AssociateWisePerformerFragment;
import io.cordova.myapp00d753.fragment.ZoneWisePerformerFragment;
import io.cordova.myapp00d753.fragment.BranchWisePerformerFragment;
import io.cordova.myapp00d753.utility.Pref;


public class TopPerformerActivity extends AppCompatActivity {
    LinearLayout llZone,llBranch,llAssociate,llZoneD,llBranchD,llAssociateD;
    ImageView imgBack,imgHome,imgSearch;

    Pref pref;
    String month;
    AlertDialog alertDialog2;

    TextView tvToolBar,tvToolBarM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performer);
        initView();
        loadZoneFragment();
        onClick();
    }


    private void initView(){
        pref=new Pref(getApplicationContext());

        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        imgSearch=(ImageView)findViewById(R.id.imgSearch);


        tvToolBar=(TextView)findViewById(R.id.tvToolBar);
        tvToolBarM=(TextView)findViewById(R.id.tvToolBarM);
        llZone=(LinearLayout)findViewById(R.id.llZone);
        llBranch=(LinearLayout)findViewById(R.id.llBranch);
        llAssociate=(LinearLayout)findViewById(R.id.llAssociate);
        llZoneD=(LinearLayout)findViewById(R.id.llZoneD);
        llBranchD=(LinearLayout)findViewById(R.id.llBranchD);
        llAssociateD=(LinearLayout)findViewById(R.id.llAssociateD);

        tvToolBarM.setText(pref.getMonth()+" - "+pref.getShowFinacialYear());





    }
    private void onClick(){

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), SuperVisiorDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        llZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadZoneFragment();
            }
        });

        llBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadBranchFragment();
            }
        });


        llAssociate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadAssociateFragment();
            }
        });



    }


    public void loadZoneFragment() {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        ZoneWisePerformerFragment pfragment=new ZoneWisePerformerFragment();
        transaction.replace(R.id.frameLayout1, pfragment);
        transaction.commit();

        llZoneD.setVisibility(View.VISIBLE);
        llBranchD.setVisibility(View.GONE);
        llAssociateD.setVisibility(View.GONE);




        tvToolBar.setText("Zone Wise Performer");




    }

    public void loadBranchFragment() {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        BranchWisePerformerFragment efr=new BranchWisePerformerFragment();
        transaction.replace(R.id.frameLayout1, efr);
        transaction.commit();

        llZoneD.setVisibility(View.GONE);
        llBranchD.setVisibility(View.VISIBLE);
        llAssociateD.setVisibility(View.GONE);



        tvToolBar.setText("Branch Wise Performer");

        //tvHeader.setText("Personal");


    }

    public void loadAssociateFragment() {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        AssociateWisePerformerFragment efr=new AssociateWisePerformerFragment();
        transaction.replace(R.id.frameLayout1, efr);
        transaction.commit();

        llZoneD.setVisibility(View.GONE);
        llBranchD.setVisibility(View.GONE);
        llAssociateD.setVisibility(View.VISIBLE);



        tvToolBar.setText("Associate Wise Performer");

        //tvHeader.setText("Personal");


    }






}
