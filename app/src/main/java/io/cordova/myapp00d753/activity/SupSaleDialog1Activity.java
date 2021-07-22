package io.cordova.myapp00d753.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.ChartModel;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

public class SupSaleDialog1Activity extends AppCompatActivity {
    CombinedChart comCYTargetChart,comCYPYTargetChart,comCYRevenueChart,comCYPYRevenueChart,comCYPYProductGrowthChart,comCYPYRevenueGrowthChart;

    ArrayList<Entry> cyTargetYvals=new ArrayList<>();
    ArrayList<BarEntry>cyTargetYYvals=new ArrayList<>();
    ArrayList cyTargetxvals = new ArrayList();

    ArrayList<Entry> cypyAchvYvals=new ArrayList<>();
    ArrayList<BarEntry>cypyAchvYYvals=new ArrayList<>();
    ArrayList cypyAchvxvals = new ArrayList();

    ArrayList<Entry> cyRevenueYvals=new ArrayList<>();
    ArrayList<BarEntry>cyRevenueYYvals=new ArrayList<>();
    ArrayList cyRevenuexvals = new ArrayList();

    ArrayList<Entry> cypyRevenueYvals=new ArrayList<>();
    ArrayList<BarEntry>cypyRevenueYYvals=new ArrayList<>();
    ArrayList cypyRevenuexvals = new ArrayList();


    ArrayList<Entry> cypyProductGrowthYvals=new ArrayList<>();
    ArrayList<BarEntry>cypyProductGrowthYYvals=new ArrayList<>();
    ArrayList cypyProductGrowthxvals = new ArrayList();

    ArrayList<Entry> cypyRevenueGrowthYvals=new ArrayList<>();
    ArrayList<BarEntry>cypyRevenueGrowthYYvals=new ArrayList<>();
    ArrayList cypyRevenueGrowthxvals = new ArrayList();

    Pref pref;
    String month,financialYear;
    int y;
    String year;
    ArrayList<ChartModel>itemList=new ArrayList<>();
    ArrayList<ChartModel>itemList1=new ArrayList<>();
    ArrayList<ChartModel>itemList2=new ArrayList<>();
    ArrayList<ChartModel>itemList3=new ArrayList<>();
    ArrayList<ChartModel>itemList4=new ArrayList<>();
    ArrayList<ChartModel>itemList5=new ArrayList<>();
    LinearLayout ll1;
    FloatingActionButton fab;
    TextView tvMonth,tvToolBar;
    ProgressDialog progressBar;
    ImageView imgUpArrow,imgDownArrow;
    Float percen;
    ImageView imgBack,imgHome;
    LinearLayout llLoader;
    FrameLayout flMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sup_sale_dialog1);
        initView();
        onClick();
    }

    private void initView(){
        pref=new Pref(SupSaleDialog1Activity.this);
        comCYTargetChart=(CombinedChart)findViewById(R.id.comCYTargetChart);
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

        ll1=(LinearLayout)findViewById(R.id.ll1);
        ll1.setClickable(false);
        fab=(FloatingActionButton)findViewById(R.id.fab);
        tvMonth = (TextView) findViewById(R.id.tvMonth);
        tvToolBar = (TextView) findViewById(R.id.tvToolBar);
        tvToolBar.setText("YTD - "+ financialYear);

         progressBar = new ProgressDialog(SupSaleDialog1Activity.this);
        progressBar.setMessage("Loading..");
        progressBar.setCancelable(false);

        imgUpArrow=(ImageView)findViewById(R.id.imgUpArrow);
        imgDownArrow=(ImageView)findViewById(R.id.imgDownArrow);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        llLoader=(LinearLayout)findViewById(R.id.llLoader);
        flMain=(FrameLayout)findViewById(R.id.flMain);



        getItemList();
    }

    private void getItemList() {
        Log.d("Arpan", "arpan");

        llLoader.setVisibility(View.VISIBLE);
        flMain.setVisibility(View.GONE);
        String surl = AppData.url + "get_EmployeeSalesReport?ClientID=" + pref.getEmpClintId() + "&UserID=" + pref.getEmpId() + "&FinancialYear=" + financialYear + "&Month=" + month + "&RType=DM&Operation=7&SecurityCode=" + pref.getSecurityCode();
        Log.d("input", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        llLoader.setVisibility(View.VISIBLE);
                        flMain.setVisibility(View.GONE);
                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i=0;i<responseData.length();i++) {

                                    JSONObject obj = responseData.getJSONObject(i);
                                    float Sold = Float.parseFloat(obj.optString("Overall_Sold"));
                                    float Target = Float.parseFloat(obj.optString("Overall_Target"));
                                    String Month = obj.optString("Month");
                                    String GrowthPercentage = obj.optString("Overall_GrowthPercentage");
                                    tvMonth.setText(GrowthPercentage+" %");
                                    percen= Float.valueOf(GrowthPercentage);
                                    ChartModel cModel=new ChartModel(Sold,i,Month);
                                    cModel.setItem4(Target);
                                    itemList.add(cModel);
                                }

                                if (percen<0.0){
                                    imgDownArrow.setVisibility(View.VISIBLE);
                                    imgUpArrow.setVisibility(View.GONE);
                                    tvMonth.setTextColor(Color.RED);
                                }else if (percen>0.0){
                                    imgDownArrow.setVisibility(View.GONE);
                                    imgUpArrow.setVisibility(View.VISIBLE);
                                    tvMonth.setTextColor(Color.GREEN);
                                }else {
                                    imgDownArrow.setVisibility(View.GONE);
                                    imgUpArrow.setVisibility(View.GONE);
                                    tvMonth.setTextColor(Color.RED);

                                }




                                for (int i = 0; i < itemList.size(); i++) {

                                    cyTargetYvals.add(new Entry(itemList.get(i).getItem2(),itemList.get(i).getItem1()));
                                    cyTargetYYvals.add(new BarEntry(itemList.get(i).getItem2(),itemList.get(i).getItem4()));
                                    cyTargetxvals.add(itemList.get(i).getItem3());



                                }

                                //cy target and sold combined chart
                                comCYTargetChart=findViewById(R.id.comCYTargetChart);
                                comCYTargetChart.setTouchEnabled(false);
                                comCYTargetChart.setDrawValueAboveBar(true);


                                comCYTargetChart.getDescription().setText("");
                                comCYTargetChart.setDrawGridBackground(true);
                                comCYTargetChart.setDrawBarShadow(true);
                                comCYTargetChart.setHighlightFullBarEnabled(true);
                                comCYTargetChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                                        CombinedChart.DrawOrder.BAR,  CombinedChart.DrawOrder.LINE
                                });
                                comCYTargetChart.animateX(1000);


                                Legend l = comCYTargetChart.getLegend();
                                l.setWordWrapEnabled(true);
                                l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                                l.setOrientation(Legend.LegendOrientation.HORIZONTAL);

                                YAxis rightAxis = comCYTargetChart.getAxisRight();
                                rightAxis.setDrawGridLines(false);
                                rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

                                YAxis leftAxis = comCYTargetChart.getAxisLeft();
                                leftAxis.setDrawGridLines(false);
                                leftAxis.setAxisMinimum(0f);

                                XAxis xAxis = comCYTargetChart.getXAxis();
                                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                                xAxis.setAxisMinimum(0f);
                                xAxis.setGranularity(1f);
                                xAxis.setValueFormatter(new IndexAxisValueFormatter(cyTargetxvals));



                                CombinedData data = new CombinedData();

                                data.setData( generateBarDataForCYTarget());
                                data.setData(generateLineDataForCYSold());

                               // xAxis.setAxisMaximum(data.getXMax() + 0.25f);
                                comCYTargetChart.setData(data);
                                comCYTargetChart.invalidate();

                                getItemListForCYPYAchv();






                            } else {

                                Toast.makeText(SupSaleDialog1Activity.this, "No data found", Toast.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void getItemListForCYPYAchv() {
        Log.d("Arpan", "arpan");

        llLoader.setVisibility(View.VISIBLE);
        flMain.setVisibility(View.GONE);
        String surl = AppData.url + "get_EmployeeSalesReport?ClientID=" + pref.getEmpClintId() + "&UserID=" + pref.getEmpId() + "&FinancialYear=" + financialYear + "&Month=" + month + "&RType=DM&Operation=7&SecurityCode=" + pref.getSecurityCode();
        Log.d("input", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        llLoader.setVisibility(View.VISIBLE);
                        flMain.setVisibility(View.GONE);
                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i=0;i<responseData.length();i++) {

                                    JSONObject obj = responseData.getJSONObject(i);
                                    float AchvPer = Float.parseFloat(obj.optString("Overall_AchvPer"));
                                    float Prev_AchvPer = Float.parseFloat(obj.optString("Overall_Prev_AchvPer"));
                                    String Month = obj.optString("Month");
                                    String GrowthPercentage = obj.optString("GrowthPercentage");
                                    ChartModel cModel=new ChartModel(AchvPer,i,Month);
                                    cModel.setItem4(Prev_AchvPer);
                                    itemList1.add(cModel);
                                }



                                for (int i = 0; i < itemList1.size(); i++) {

                                    cypyAchvYvals.add(new Entry(itemList1.get(i).getItem2(),itemList1.get(i).getItem1()));
                                    cypyAchvYYvals.add(new BarEntry(itemList1.get(i).getItem2(),itemList1.get(i).getItem4()));
                                    cypyAchvxvals.add(itemList1.get(i).getItem3());



                                }

                                //cy target and sold combined chart
                                comCYPYTargetChart=findViewById(R.id.comCYPYTargetChart);
                                comCYPYTargetChart.setTouchEnabled(false);
                                comCYPYTargetChart.setDrawValueAboveBar(true);

                                comCYPYTargetChart.getDescription().setText("");
                                comCYPYTargetChart.setDrawGridBackground(true);
                                comCYPYTargetChart.setDrawBarShadow(true);
                                comCYPYTargetChart.setHighlightFullBarEnabled(true);
                                comCYTargetChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                                        CombinedChart.DrawOrder.BAR,  CombinedChart.DrawOrder.LINE
                                });
                                comCYPYTargetChart.animateX(1000);


                                Legend l = comCYPYTargetChart.getLegend();
                                l.setWordWrapEnabled(true);
                                l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                                l.setOrientation(Legend.LegendOrientation.HORIZONTAL);

                                YAxis rightAxis = comCYPYTargetChart.getAxisRight();
                                rightAxis.setDrawGridLines(false);
                                rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

                                YAxis leftAxis = comCYPYTargetChart.getAxisLeft();
                                leftAxis.setDrawGridLines(false);
                                leftAxis.setAxisMinimum(0f);

                                XAxis xAxis = comCYPYTargetChart.getXAxis();
                                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                              //  xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                                xAxis.setAxisMinimum(0f);
                                xAxis.setGranularity(1f);
                                xAxis.setValueFormatter(new IndexAxisValueFormatter(cypyAchvxvals));



                                CombinedData data = new CombinedData();

                                data.setData( generateLineDataForCYAchv());
                                data.setData(generateBarDataForPYAchv());

                              //  xAxis.setAxisMaximum(data.getXMax() + 0.25f);
                                comCYPYTargetChart.setData(data);
                                comCYPYTargetChart.invalidate();

                                getItemListForCYRevenue();






                            } else {

                                Toast.makeText(SupSaleDialog1Activity.this, "No data found", Toast.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void getItemListForCYRevenue() {
        Log.d("Arpan", "arpan");

        llLoader.setVisibility(View.VISIBLE);
        flMain.setVisibility(View.GONE);
        String surl = AppData.url + "get_EmployeeSalesReport?ClientID=" + pref.getEmpClintId() + "&UserID=" + pref.getEmpId() + "&FinancialYear=" + financialYear + "&Month=" + month + "&RType=DM&Operation=7&SecurityCode=" + pref.getSecurityCode();
        Log.d("input", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        llLoader.setVisibility(View.VISIBLE);
                        flMain.setVisibility(View.GONE);
                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i=0;i<responseData.length();i++) {

                                    JSONObject obj = responseData.getJSONObject(i);
                                    float Revenue_Sold = Float.parseFloat(obj.optString("Overall_Revenue_Sold"));
                                    float Revenue_Target = Float.parseFloat(obj.optString("Overall_Revenue_Target"));
                                    String Month = obj.optString("Month");
                                    String GrowthPercentage = obj.optString("GrowthPercentage");
                                    ChartModel cModel=new ChartModel(Revenue_Sold,i,Month);
                                    cModel.setItem4(Revenue_Target);
                                    itemList2.add(cModel);
                                }



                                for (int i = 0; i < itemList2.size(); i++) {

                                    cyRevenueYvals.add(new Entry(itemList2.get(i).getItem2(),itemList2.get(i).getItem1()));
                                    cyRevenueYYvals.add(new BarEntry(itemList2.get(i).getItem2(),itemList2.get(i).getItem4()));
                                    cyRevenuexvals.add(itemList2.get(i).getItem3());



                                }

                                //cy target and sold combined chart
                                comCYRevenueChart=findViewById(R.id.comCYRevenueChart);
                                comCYRevenueChart.setTouchEnabled(false);
                                comCYRevenueChart.setDrawValueAboveBar(true);

                                comCYRevenueChart.getDescription().setText("");
                                comCYRevenueChart.setDrawGridBackground(true);
                                comCYRevenueChart.setDrawBarShadow(true);
                                comCYRevenueChart.setHighlightFullBarEnabled(true);
                                comCYRevenueChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                                        CombinedChart.DrawOrder.BAR,  CombinedChart.DrawOrder.LINE
                                });
                                comCYRevenueChart.animateX(1000);


                                Legend l = comCYRevenueChart.getLegend();
                                l.setWordWrapEnabled(true);
                                l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                                l.setOrientation(Legend.LegendOrientation.HORIZONTAL);

                                YAxis rightAxis = comCYRevenueChart.getAxisRight();
                                rightAxis.setDrawGridLines(false);
                                rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

                                YAxis leftAxis = comCYRevenueChart.getAxisLeft();
                                leftAxis.setDrawGridLines(false);
                                leftAxis.setAxisMinimum(0f);

                                XAxis xAxis = comCYRevenueChart.getXAxis();
                                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                                //  xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                                xAxis.setAxisMinimum(0f);
                                xAxis.setGranularity(1f);
                                xAxis.setValueFormatter(new IndexAxisValueFormatter(cypyAchvxvals));



                                CombinedData data = new CombinedData();

                                data.setData( generateLineDataForCYRevenue());
                                data.setData(generateBarDataForCYRevenue());

                                //  xAxis.setAxisMaximum(data.getXMax() + 0.25f);
                                comCYRevenueChart.setData(data);
                                comCYRevenueChart.invalidate();

                                getItemListForCYPYRevenue();






                            } else {

                                Toast.makeText(SupSaleDialog1Activity.this, "No data found", Toast.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void getItemListForCYPYRevenue() {
        Log.d("Arpan", "arpan");

        llLoader.setVisibility(View.VISIBLE);
        flMain.setVisibility(View.GONE);
        String surl = AppData.url + "get_EmployeeSalesReport?ClientID=" + pref.getEmpClintId() + "&UserID=" + pref.getEmpId() + "&FinancialYear=" + financialYear + "&Month=" + month + "&RType=DM&Operation=7&SecurityCode=" + pref.getSecurityCode();
        Log.d("input", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        llLoader.setVisibility(View.VISIBLE);
                        flMain.setVisibility(View.GONE);
                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i=0;i<responseData.length();i++) {

                                    JSONObject obj = responseData.getJSONObject(i);
                                    float Revenue_AchvPer = Float.parseFloat(obj.optString("Overall_Revenue_AchvPer"));
                                    float Prev_Revenue_AchvPer = Float.parseFloat(obj.optString("Overall_Prev_Revenue_AchvPer"));
                                    String Month = obj.optString("Month");
                                    String GrowthPercentage = obj.optString("GrowthPercentage");
                                    ChartModel cModel=new ChartModel(Revenue_AchvPer,i,Month);
                                    cModel.setItem4(Prev_Revenue_AchvPer);
                                    itemList3.add(cModel);
                                }



                                for (int i = 0; i < itemList3.size(); i++) {

                                    cypyRevenueYvals.add(new Entry(itemList3.get(i).getItem2(),itemList3.get(i).getItem1()));
                                    cypyRevenueYYvals.add(new BarEntry(itemList3.get(i).getItem2(),itemList3.get(i).getItem4()));
                                    cypyRevenuexvals.add(itemList3.get(i).getItem3());



                                }

                                //cy target and sold combined chart
                                comCYPYRevenueChart=findViewById(R.id.comCYPYRevenueChart);
                                comCYPYRevenueChart.setTouchEnabled(false);

                                comCYPYRevenueChart.getDescription().setText("");
                                comCYPYRevenueChart.setDrawGridBackground(true);
                                comCYPYRevenueChart.setDrawBarShadow(true);
                                comCYPYRevenueChart.setHighlightFullBarEnabled(true);
                                comCYPYRevenueChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                                        CombinedChart.DrawOrder.BAR,  CombinedChart.DrawOrder.LINE
                                });
                                comCYPYRevenueChart.animateX(1000);


                                Legend l = comCYPYRevenueChart.getLegend();
                                l.setWordWrapEnabled(true);
                                l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                                l.setOrientation(Legend.LegendOrientation.HORIZONTAL);

                                YAxis rightAxis = comCYPYRevenueChart.getAxisRight();
                                rightAxis.setDrawGridLines(false);
                                rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

                                YAxis leftAxis = comCYPYRevenueChart.getAxisLeft();
                                leftAxis.setDrawGridLines(false);
                                leftAxis.setAxisMinimum(0f);

                                XAxis xAxis = comCYPYRevenueChart.getXAxis();
                                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                                //  xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                                xAxis.setAxisMinimum(0f);
                                xAxis.setGranularity(1f);
                                xAxis.setValueFormatter(new IndexAxisValueFormatter(cypyAchvxvals));



                                CombinedData data = new CombinedData();

                                data.setData( generateLineDataForCYRevenueAchv());
                                data.setData(generateBarDataForPYRevenueAchv());

                                //  xAxis.setAxisMaximum(data.getXMax() + 0.25f);
                                comCYPYRevenueChart.setData(data);
                                comCYPYRevenueChart.invalidate();

                                getItemListForCYPYProductGrowth();






                            } else {

                                Toast.makeText(SupSaleDialog1Activity.this, "No data found", Toast.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }


    private void getItemListForCYPYProductGrowth() {
        Log.d("Arpan", "arpan");

        llLoader.setVisibility(View.VISIBLE);
        flMain.setVisibility(View.GONE);
        String surl = AppData.url + "get_EmployeeSalesReport?ClientID=" + pref.getEmpClintId() + "&UserID=" + pref.getEmpId() + "&FinancialYear=" + financialYear + "&Month=" + month + "&RType=DM&Operation=7&SecurityCode=" + pref.getSecurityCode();
        Log.d("input", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        llLoader.setVisibility(View.VISIBLE);
                        flMain.setVisibility(View.GONE);
                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i=0;i<responseData.length();i++) {

                                    JSONObject obj = responseData.getJSONObject(i);
                                    float Overall_AchvPer = Float.parseFloat(obj.optString("Overall_AchvPer"));
                                    float Overall_Prev_AchvPer = Float.parseFloat(obj.optString("Overall_Prev_AchvPer"));
                                    String Month = obj.optString("Month");
                                    String GrowthPercentage = obj.optString("GrowthPercentage");
                                    ChartModel cModel=new ChartModel(Overall_AchvPer,i,Month);
                                    cModel.setItem4(Overall_Prev_AchvPer);
                                    itemList4.add(cModel);
                                }



                                for (int i = 0; i < itemList4.size(); i++) {

                                    cypyProductGrowthYvals.add(new Entry(itemList4.get(i).getItem2(),itemList4.get(i).getItem1()));
                                    cypyProductGrowthYYvals.add(new BarEntry(itemList4.get(i).getItem2(),itemList4.get(i).getItem4()));
                                    cypyProductGrowthxvals.add(itemList4.get(i).getItem3());



                                }

                                //cy target and sold combined chart
                                comCYPYProductGrowthChart=findViewById(R.id.comCYPYProductGrowthChart);
                                comCYPYProductGrowthChart.setTouchEnabled(false);

                                comCYPYProductGrowthChart.getDescription().setText("");
                                comCYPYProductGrowthChart.setDrawGridBackground(true);
                                comCYPYProductGrowthChart.setDrawBarShadow(true);
                                comCYPYProductGrowthChart.setHighlightFullBarEnabled(true);
                                comCYPYProductGrowthChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                                        CombinedChart.DrawOrder.BAR,  CombinedChart.DrawOrder.LINE
                                });
                                comCYPYProductGrowthChart.animateX(1000);


                                Legend l = comCYPYProductGrowthChart.getLegend();
                                l.setWordWrapEnabled(true);
                                l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                                l.setOrientation(Legend.LegendOrientation.HORIZONTAL);

                                YAxis rightAxis = comCYPYProductGrowthChart.getAxisRight();
                                rightAxis.setDrawGridLines(false);
                                rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

                                YAxis leftAxis = comCYPYProductGrowthChart.getAxisLeft();
                                leftAxis.setDrawGridLines(false);
                                leftAxis.setAxisMinimum(0f);

                                XAxis xAxis = comCYPYProductGrowthChart.getXAxis();
                                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                                //  xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                                xAxis.setAxisMinimum(0f);
                                xAxis.setGranularity(1f);
                                xAxis.setValueFormatter(new IndexAxisValueFormatter(cypyAchvxvals));



                                CombinedData data = new CombinedData();

                                data.setData( generateLineDataForCYProductGrowth());
                                data.setData(generateBarDataForPYProductGrowth());

                                //  xAxis.setAxisMaximum(data.getXMax() + 0.25f);
                                comCYPYProductGrowthChart.setData(data);
                                comCYPYProductGrowthChart.invalidate();

                                getItemListForCYPYRevenueGrowth();






                            } else {

                                Toast.makeText(SupSaleDialog1Activity.this, "No data found", Toast.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void getItemListForCYPYRevenueGrowth() {
        Log.d("Arpan", "arpan");
        llLoader.setVisibility(View.VISIBLE);
        flMain.setVisibility(View.GONE);
        String surl = AppData.url + "get_EmployeeSalesReport?ClientID=" + pref.getEmpClintId() + "&UserID=" + pref.getEmpId() + "&FinancialYear=" + financialYear + "&Month=" + month + "&RType=DM&Operation=7&SecurityCode=" + pref.getSecurityCode();
        Log.d("input", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        llLoader.setVisibility(View.GONE);
                        flMain.setVisibility(View.VISIBLE);
                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i=0;i<responseData.length();i++) {

                                    JSONObject obj = responseData.getJSONObject(i);
                                    float Overall_Revenue_AchvPer = Float.parseFloat(obj.optString("Overall_Revenue_AchvPer"));
                                    float Overall_Prev_Revenue_AchvPer = Float.parseFloat(obj.optString("Overall_Prev_Revenue_AchvPer"));
                                    String Month = obj.optString("Month");
                                    String GrowthPercentage = obj.optString("GrowthPercentage");
                                    ChartModel cModel=new ChartModel(Overall_Revenue_AchvPer,i,Month);
                                    cModel.setItem4(Overall_Prev_Revenue_AchvPer);
                                    itemList5.add(cModel);
                                }



                                for (int i = 0; i < itemList5.size(); i++) {

                                    cypyRevenueGrowthYvals.add(new Entry(itemList5.get(i).getItem2(),itemList5.get(i).getItem1()));
                                    cypyRevenueGrowthYYvals.add(new BarEntry(itemList5.get(i).getItem2(),itemList5.get(i).getItem4()));
                                    cypyRevenueGrowthxvals.add(itemList5.get(i).getItem3());



                                }

                                //cy target and sold combined chart
                                comCYPYRevenueGrowthChart=findViewById(R.id.comCYPYRevenueGrowthChart);
                                comCYPYRevenueGrowthChart.setTouchEnabled(false);

                                comCYPYRevenueGrowthChart.getDescription().setText("");
                                comCYPYRevenueGrowthChart.setDrawGridBackground(true);
                                comCYPYRevenueGrowthChart.setDrawBarShadow(true);
                                comCYPYRevenueGrowthChart.setHighlightFullBarEnabled(true);
                                comCYPYRevenueGrowthChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                                        CombinedChart.DrawOrder.BAR,  CombinedChart.DrawOrder.LINE
                                });
                                comCYPYRevenueGrowthChart.animateX(1000);


                                Legend l = comCYPYRevenueGrowthChart.getLegend();
                                l.setWordWrapEnabled(true);
                                l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                                l.setOrientation(Legend.LegendOrientation.HORIZONTAL);

                                YAxis rightAxis = comCYPYRevenueGrowthChart.getAxisRight();
                                rightAxis.setDrawGridLines(false);
                                rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

                                YAxis leftAxis = comCYPYRevenueGrowthChart.getAxisLeft();
                                leftAxis.setDrawGridLines(false);
                                leftAxis.setAxisMinimum(0f);

                                XAxis xAxis = comCYPYRevenueGrowthChart.getXAxis();
                                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                                //  xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                                xAxis.setAxisMinimum(0f);
                                xAxis.setGranularity(1f);
                                xAxis.setValueFormatter(new IndexAxisValueFormatter(cypyAchvxvals));



                                CombinedData data = new CombinedData();

                                data.setData( generateLineDataForCYRevenueGrowth());
                                data.setData(generateBarDataForPYRevenueGrowth());

                                //  xAxis.setAxisMaximum(data.getXMax() + 0.25f);
                                comCYPYRevenueGrowthChart.setData(data);
                                comCYPYRevenueGrowthChart.invalidate();






                            } else {

                                Toast.makeText(SupSaleDialog1Activity.this, "No data found", Toast.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private LineData generateLineDataForCYSold() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<Entry>();

        entries = cyTargetYvals;

        LineDataSet set = new LineDataSet(entries, "CY Achievement  (Line Chart)");
        //set.setColor(Color.rgb(240, 238, 70));
        set.setColors(Color.GRAY);
        set.setLineWidth(3.5f);

        set.setCircleColor(Color.BLACK);
        set.setCircleRadius(5f);
        set.setFillColor(Color.BLACK);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.BLACK);


        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return d;
    }

    private BarData generateBarDataForCYTarget() {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        entries = cyTargetYYvals;

        BarDataSet set1 = new BarDataSet(entries, "CY  Target  (Bar Chart)");
        //set1.setColor(Color.rgb(60, 220, 78));
        set1.setColors(Color.rgb(107,142,35));
        set1.setValueTextColor(Color.BLACK);
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        float barWidth = 0.45f; // x2 dataset

        BarData d = new BarData(set1);
        d.setBarWidth(barWidth);

        return d;
    }


    private LineData generateLineDataForCYAchv() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<Entry>();

        entries = cypyAchvYvals;

        LineDataSet set = new LineDataSet(entries, "CY  Achievement (%) (Line Chart)");
        //set.setColor(Color.rgb(240, 238, 70));
        set.setColors(Color.GRAY);
        set.setLineWidth(3.5f);

        set.setCircleColor(Color.BLACK);
        set.setCircleRadius(5f);
        set.setFillColor(Color.BLACK);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.BLACK);


        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return d;
    }

    private BarData generateBarDataForPYAchv() {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        entries = cypyAchvYYvals;

        BarDataSet set1 = new BarDataSet(entries, "LY  Achievement (%) (Bar Chart)");
        //set1.setColor(Color.rgb(60, 220, 78));
        set1.setColors(Color.rgb(107,142,35));
        set1.setValueTextColor(Color.BLACK);
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        float barWidth = 0.45f; // x2 dataset

        BarData d = new BarData(set1);
        d.setBarWidth(barWidth);

        return d;
    }

    private LineData generateLineDataForCYRevenue() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<Entry>();

        entries = cyRevenueYvals;

        LineDataSet set = new LineDataSet(entries, "CY  Achieved  (Line Chart)");
        //set.setColor(Color.rgb(240, 238, 70));
        set.setColors(Color.GRAY);
        set.setLineWidth(3.5f);

        set.setCircleColor(Color.BLACK);
        set.setCircleRadius(5f);
        set.setFillColor(Color.BLACK);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.BLACK);


        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return d;
    }

    private BarData generateBarDataForCYRevenue() {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        entries = cyRevenueYYvals;

        BarDataSet set1 = new BarDataSet(entries, "CY Revenue Target (Bar Chart)");
        //set1.setColor(Color.rgb(60, 220, 78));
        set1.setColors(Color.rgb(107,142,35));
        set1.setValueTextColor(Color.BLACK);
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        float barWidth = 0.45f; // x2 dataset

        BarData d = new BarData(set1);
        d.setBarWidth(barWidth);

        return d;
    }

    private LineData generateLineDataForCYRevenueAchv() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<Entry>();

        entries = cypyRevenueYvals;

        LineDataSet set = new LineDataSet(entries, "CY  Acheivement (%) (Line Chart)");
        //set.setColor(Color.rgb(240, 238, 70));
        set.setColors(Color.GRAY);
        set.setLineWidth(3.5f);

        set.setCircleColor(Color.BLACK);
        set.setCircleRadius(5f);
        set.setFillColor(Color.BLACK);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.BLACK);


        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return d;
    }

    private BarData generateBarDataForPYRevenueAchv() {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        entries = cypyRevenueYYvals;

        BarDataSet set1 = new BarDataSet(entries, "LY  Acheivement (%) (Bar Chart)");
        //set1.setColor(Color.rgb(60, 220, 78));
        set1.setColors(Color.rgb(107,142,35));
        set1.setValueTextColor(Color.BLACK);
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        float barWidth = 0.45f; // x2 dataset

        BarData d = new BarData(set1);
        d.setBarWidth(barWidth);

        return d;
    }

    private LineData generateLineDataForCYProductGrowth() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<Entry>();

        entries = cypyProductGrowthYvals;

        LineDataSet set = new LineDataSet(entries, "CY   Product Growth (%) (Line Chart)");
        //set.setColor(Color.rgb(240, 238, 70));
        set.setColors(Color.GRAY);
        set.setLineWidth(3.5f);

        set.setCircleColor(Color.BLACK);
        set.setCircleRadius(5f);
        set.setFillColor(Color.BLACK);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.BLACK);


        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return d;
    }

    private BarData generateBarDataForPYProductGrowth() {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        entries = cypyProductGrowthYYvals;

        BarDataSet set1 = new BarDataSet(entries, "LY  Product Growth (%) (Bar Chart)");
        //set1.setColor(Color.rgb(60, 220, 78));
        set1.setColors(Color.rgb(107,142,35));
        set1.setValueTextColor(Color.BLACK);
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        float barWidth = 0.45f; // x2 dataset

        BarData d = new BarData(set1);
        d.setBarWidth(barWidth);

        return d;
    }

    private LineData generateLineDataForCYRevenueGrowth() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<Entry>();

        entries = cypyRevenueGrowthYvals;

        LineDataSet set = new LineDataSet(entries, "CY   Revenue Growth (%) (Line Chart)");
        //set.setColor(Color.rgb(240, 238, 70));
        set.setColors(Color.GRAY);
        set.setLineWidth(3.5f);

        set.setCircleColor(Color.BLACK);
        set.setCircleRadius(5f);
        set.setFillColor(Color.BLACK);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.BLACK);


        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return d;
    }

    private BarData generateBarDataForPYRevenueGrowth() {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        entries = cypyRevenueGrowthYYvals;

        BarDataSet set1 = new BarDataSet(entries, "LY   Revenue Growth (%) (Bar Chart)");
        //set1.setColor(Color.rgb(60, 220, 78));
        set1.setColors(Color.rgb(107,142,35));
        set1.setValueTextColor(Color.BLACK);
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        float barWidth = 0.45f; // x2 dataset

        BarData d = new BarData(set1);
        d.setBarWidth(barWidth);

        return d;
    }


    private void onClick(){
        comCYTargetChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener()
        {
            @Override
            public void onValueSelected(Entry e, Highlight h)
            {

            }

            @Override
            public void onNothingSelected()
            {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SupSaleDialog1Activity.this,SupSalesManagementDashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SupSaleDialog1Activity.this,SuperVisiorDashBoardActivity.class);
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