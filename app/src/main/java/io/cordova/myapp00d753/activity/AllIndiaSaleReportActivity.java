package io.cordova.myapp00d753.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.fragment.AllIndiaDataFragment;
import io.cordova.myapp00d753.fragment.AllIndiaGrpahicalFragment;
import io.cordova.myapp00d753.fragment.DataFragment;
import io.cordova.myapp00d753.fragment.GrpahicalFragment;
import io.cordova.myapp00d753.utility.Pref;

public class AllIndiaSaleReportActivity extends AppCompatActivity {

    Pref pref;
    String month,financialYear;
    int y;
    String year;

    LinearLayout ll1;
    FloatingActionButton fab;
    TextView tvMonth,tvToolBar;
    ProgressDialog progressBar;

    String strNumber;
    ImageView imgBack,imgHome;
    LinearLayout llLoader;
    FrameLayout flMain;
    LinearLayout llNormal,llGraphical,llNormalD,llGraphicalD;
    LinearLayout llL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zonewise_report);
        initView();
        loadGraphicalFragment();
        onClick();

    }

    private void initView(){
        pref=new Pref(AllIndiaSaleReportActivity.this);

        y = Calendar.getInstance().get(Calendar.YEAR);
        year = String.valueOf(y);
        Log.d("year", year);
        int m = Calendar.getInstance().get(Calendar.MONTH) + 1;
        Log.d("month", String.valueOf(m));
        if (m == 1) {
            month = "January";
        } else if (m == 2) {
            month = "February";
        } else if (m == 3) {
            month = "March";
        } else if (m == 4) {
            month = "April";
        } else if (m == 5) {
            month = "May";
        } else if (m == 6) {
            month = "June";
        } else if (m == 7) {
            month = "July";
        } else if (m == 8) {
            month = "August";
        } else if (m == 9) {
            month = "September";
        } else if (m == 10) {
            month = "October";
        } else if (m == 11) {
            month = "November";
        } else if (m == 12) {
            month = "December";
        }

        if(month.equals("January")){
            int futureyear = y - 1;
            financialYear = futureyear+"-"+year;
        }else if (month.equals("February")){
            int futureyear = y - 1;
            financialYear = futureyear+"-"+year;
        }else if (month.equals("March")){
            int futureyear = y - 1;
            financialYear = futureyear+"-"+year;
        }else {
            int futureyear = y + 1;
            financialYear = year+"-"+futureyear;
        }
        tvToolBar=(TextView)findViewById(R.id.tvToolBar);
        tvToolBar.setText("All India Synopsis - 2020-21");




        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);

        llNormal=(LinearLayout)findViewById(R.id.llNormal);
        llGraphical=(LinearLayout)findViewById(R.id.llGraphical);

        llNormalD=(LinearLayout)findViewById(R.id.llNormalD);
        llGraphicalD=(LinearLayout)findViewById(R.id.llGraphicalD);
        llL=(LinearLayout)findViewById(R.id.llL);
        llL.setVisibility(View.GONE);

       // getItemList();











       // getItemList();
    }

    public void loadNormalFragment() {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        AllIndiaDataFragment pfragment=new AllIndiaDataFragment();
        transaction.replace(R.id.frameLayout1, pfragment);
        transaction.commit();

        llNormalD.setVisibility(View.VISIBLE);
        llGraphicalD.setVisibility(View.GONE);




    }


    public void loadGraphicalFragment() {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        AllIndiaGrpahicalFragment pfragment=new AllIndiaGrpahicalFragment();
        transaction.replace(R.id.frameLayout1, pfragment);
        transaction.commit();

        llNormalD.setVisibility(View.GONE);
        llGraphicalD.setVisibility(View.VISIBLE);




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
                Intent intent=new Intent(AllIndiaSaleReportActivity.this,SuperVisiorDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        llNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadNormalFragment();
            }
        });

        llGraphical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadGraphicalFragment();
            }
        });
    }

}