package io.cordova.myapp00d753.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
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
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.intrusoft.scatter.ChartData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.fragment.CustomerReportDataFragment;
import io.cordova.myapp00d753.fragment.CustomerReportGraphFragment;
import io.cordova.myapp00d753.fragment.DataFragment;
import io.cordova.myapp00d753.fragment.GrpahicalFragment;
import io.cordova.myapp00d753.module.ChartModel;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;


public class CustomerReportActivity extends AppCompatActivity {

    Pref prefManager;


    String month,financialYear,year;
    ImageView imgCusUp,imgCusDown,imgRevUp,imgRevDown,imgSoldUp,imgSoldDown;
    TextView tvSoldGrowth,tvRevGrowth,tvCusGrowth;
    int y;
    float cusGrowth,revGrowth,soldGrowth;
    LinearLayout llNormal,llGraphical,llNormalD,llGraphicalD;








    ArrayList<BarEntry>cylyCusYYvals=new ArrayList<>();
    ArrayList<BarEntry>cylyRevYYvals=new ArrayList<>();
    ArrayList<BarEntry>cylySoldYYvals=new ArrayList<>();
    ArrayList cyCusxvals = new ArrayList();


    TextView tvToolBar;
    ImageView imgBack,imgHome;
    BarChart barCYLYCus;
    CombinedChart comCYPYCus,comCYPYRev,comCYPYSold;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_customer_report);

        initialize();
        onClick();
        targetGet();

    }

    private void initialize(){
        prefManager=new Pref(CustomerReportActivity.this);
        imgCusUp=(ImageView)findViewById(R.id.imgCusUp);
        imgCusDown=(ImageView)findViewById(R.id.imgCusDown);
        imgRevUp=(ImageView)findViewById(R.id.imgRevUp);
        imgRevDown=(ImageView)findViewById(R.id.imgRevDown);
        imgSoldUp=(ImageView)findViewById(R.id.imgSoldUp);
        imgSoldDown=(ImageView)findViewById(R.id.imgSoldDown);

        tvSoldGrowth=(TextView)findViewById(R.id.tvSoldGrowth);
        tvRevGrowth=(TextView)findViewById(R.id.tvRevGrowth);
        tvCusGrowth=(TextView)findViewById(R.id.tvCusGrowth);

        tvToolBar=(TextView)findViewById(R.id.tvToolBar);


        y = Calendar.getInstance().get(Calendar.YEAR);
        year = String.valueOf(y);









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




        tvToolBar.setText("Customer Growth - 2020-21 ");

        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);

        llNormal=(LinearLayout)findViewById(R.id.llNormal);
        llGraphical=(LinearLayout)findViewById(R.id.llGraphical);

        llNormalD=(LinearLayout)findViewById(R.id.llNormalD);
        llGraphicalD=(LinearLayout)findViewById(R.id.llGraphicalD);




    }

    private void onClick(){
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CustomerReportActivity.this,SuperVisiorDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        llNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNormalFragment();
            }
        });
        llGraphical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadGraphicalFragment();
            }
        });
    }








    public void targetGet() {
        final ProgressDialog pd=new ProgressDialog(CustomerReportActivity.this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        String surl = AppData.url + "get_EmployeeSalesReport?ClientID=" + prefManager.getEmpClintId() + "&UserID=" + prefManager.getEmpId() + "&FinancialYear=" + financialYear + "&Month=" + month + "&RType=CMY&Operation=8&SecurityCode=" + prefManager.getSecurityCode();
        Log.d("inputCheck", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        pd.dismiss();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            boolean responseStatus=job1.optBoolean("responseStatus");
                            if (responseStatus){
                                JSONArray job=job1.optJSONArray("responseData");
                                JSONObject obj=job.optJSONObject(0);
                                String GrowthPercentage_Customer=obj.optString("GrowthPercentage_Customer");
                                cusGrowth= Float.parseFloat(GrowthPercentage_Customer);
                                if (cusGrowth<0.0){
                                    imgCusUp.setVisibility(View.GONE);
                                    imgCusDown.setVisibility(View.VISIBLE);
                                }else if (cusGrowth>0.0){
                                    imgCusUp.setVisibility(View.VISIBLE);
                                    imgCusDown.setVisibility(View.GONE);
                                }else {
                                    imgCusUp.setVisibility(View.GONE);
                                    imgCusDown.setVisibility(View.GONE);
                                }
                                tvCusGrowth.setText(GrowthPercentage_Customer+" %");
                                String GrowthPercentage_Revenu=obj.optString("GrowthPercentage_Revenu");
                                revGrowth= Float.parseFloat(GrowthPercentage_Revenu);

                                if (revGrowth<0.0){
                                    imgRevUp.setVisibility(View.GONE);
                                    imgRevDown.setVisibility(View.VISIBLE);
                                }else if (revGrowth>0.0){
                                    imgRevUp.setVisibility(View.VISIBLE);
                                    imgRevDown.setVisibility(View.GONE);
                                }else {
                                    imgRevUp.setVisibility(View.GONE);
                                    imgRevDown.setVisibility(View.GONE);
                                }

                                tvRevGrowth.setText(GrowthPercentage_Revenu+" %");

                                String GrowthPercentage_Sold=obj.optString("GrowthPercentage_Sold");
                                soldGrowth= Float.parseFloat(GrowthPercentage_Sold);

                                if (soldGrowth<0.0){
                                    imgSoldUp.setVisibility(View.GONE);
                                    imgSoldDown.setVisibility(View.VISIBLE);
                                }else if (soldGrowth>0.0){
                                    imgSoldUp.setVisibility(View.VISIBLE);
                                    imgSoldDown.setVisibility(View.GONE);
                                }else {
                                    imgSoldUp.setVisibility(View.GONE);
                                    imgSoldDown.setVisibility(View.GONE);
                                }

                                tvSoldGrowth.setText(GrowthPercentage_Sold+" %");
                                loadGraphicalFragment();
                                //CYLYCUS




                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CustomerReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(CustomerReportActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(CustomerReportActivity.this);
        requestQueue.add(stringRequest);

    }

    public void loadNormalFragment() {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        CustomerReportDataFragment pfragment1=new CustomerReportDataFragment();
        transaction.replace(R.id.frameLayout1, pfragment1);
        transaction.commit();

        llNormalD.setVisibility(View.VISIBLE);
        llGraphicalD.setVisibility(View.GONE);




    }


    public void loadGraphicalFragment() {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        CustomerReportGraphFragment pfragment=new CustomerReportGraphFragment();
        transaction.replace(R.id.frameLayout1, pfragment);
        transaction.commit();

        llNormalD.setVisibility(View.GONE);
        llGraphicalD.setVisibility(View.VISIBLE);




    }





}
