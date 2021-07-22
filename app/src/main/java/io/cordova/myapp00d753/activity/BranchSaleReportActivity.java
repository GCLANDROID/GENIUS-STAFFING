package io.cordova.myapp00d753.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.stream.Collectors;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.fragment.BranchDataFragment;
import io.cordova.myapp00d753.fragment.BranchGrpahicalFragment;
import io.cordova.myapp00d753.fragment.DataFragment;
import io.cordova.myapp00d753.fragment.GrpahicalFragment;
import io.cordova.myapp00d753.module.ChartModel;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

public class BranchSaleReportActivity extends AppCompatActivity {


    Pref pref;
    String month,financialYear;
    int y;
    String year;

    FloatingActionButton fab;
    TextView tvMonth,tvToolBar;
    ProgressDialog progressBar;

    String strNumber;
    ImageView imgBack,imgHome;
    LinearLayout llLoader;
    FrameLayout flMain;

    LinearLayout llNormal,llGraphical,llNormalD,llGraphicalD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zonewise_report);
        initView();
        onClick();

    }

    private void initView(){
        pref=new Pref(BranchSaleReportActivity.this);

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





        //tvMonth.setText("Synopsis For The Year Of " + financialYear );
        tvToolBar = (TextView) findViewById(R.id.tvToolBar);

        tvToolBar.setText("Branch Synopsis - 2020-21");

        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);

        llLoader=(LinearLayout)findViewById(R.id.llLoader);
        flMain=(FrameLayout) findViewById(R.id.flMain);

        llNormal=(LinearLayout)findViewById(R.id.llNormal);
        llGraphical=(LinearLayout)findViewById(R.id.llGraphical);

        llNormalD=(LinearLayout)findViewById(R.id.llNormalD);
        llGraphicalD=(LinearLayout)findViewById(R.id.llGraphicalD);



        loadGraphicalFragment();











       // getItemList();
    }




    public void loadNormalFragment() {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        BranchDataFragment pfragment=new BranchDataFragment();
        transaction.replace(R.id.frameLayout1, pfragment);
        transaction.commit();

        llNormalD.setVisibility(View.VISIBLE);
        llGraphicalD.setVisibility(View.GONE);




    }


    public void loadGraphicalFragment() {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        BranchGrpahicalFragment pfragment=new BranchGrpahicalFragment();
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
                Intent intent=new Intent(BranchSaleReportActivity.this,SuperVisiorDashBoardActivity.class);
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