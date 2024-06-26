package io.cordova.myapp00d753.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
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
import io.cordova.myapp00d753.module.ChartModel;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;



public class SaleDialogActivity extends AppCompatActivity {
    TextView tvCap1,tvCap2,tvCap3,tvTarget,tvSold;
    Pref prefManager;
    Button btnNext;
    PieChart pie_chart ;
    String Caption,Cap1;
    FloatingActionButton fab;



    ArrayList<ChartModel> itemList = new ArrayList<>();
    ArrayList<ChartModel> itemList1 = new ArrayList<>();

    ArrayList<String> xVals = new ArrayList<String>();
    ArrayList<Entry> yVals = new ArrayList<Entry>();

    ArrayList<String> xVals1 = new ArrayList<String>();
    ArrayList<Entry> yVals1 = new ArrayList<Entry>();

    BarChart barchart,barchart1;

    ArrayList NoOfEmp = new ArrayList();
    ArrayList year = new ArrayList();

    ArrayList NoOfEmp1 = new ArrayList();
    ArrayList year1 = new ArrayList();

    List<ChartData> conatactdata;

    ProgressDialog pd;

    ImageView imgBack,imgHome;

    private LineChart mChart,mChrart1;

    PieChart pieChart;

    ArrayList<PieEntry> pieValues = new ArrayList<PieEntry>();
    TextView tvMonth;
    String month,showMonth;
    float diff;
    TextView tvToolBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sale_dialog);

        initialize();
        targetGet();

    }

    private void initialize(){
        prefManager=new Pref(SaleDialogActivity.this);

        pd=new ProgressDialog(SaleDialogActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);

        tvCap1=(TextView)findViewById(R.id.tvCap1);
        tvCap2=(TextView)findViewById(R.id.tvCap2);
        tvCap3=(TextView)findViewById(R.id.tvCap3);
        tvToolBar=(TextView)findViewById(R.id.tvToolBar);

        tvTarget=(TextView)findViewById(R.id.tvTarget);
        tvSold=(TextView)findViewById(R.id.tvSold);

//        pie_chart=(PieChart)findViewById(R.id.pie_chart);
        barchart1=(BarChart)findViewById(R.id.barchart1);
        barchart=(BarChart)findViewById(R.id.barchart);




        btnNext=(Button)findViewById(R.id.btnNext);
        fab=(FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SaleDialogActivity.this,SalesManagementDashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imgHome=(ImageView)findViewById(R.id.imgHome);
        imgBack=(ImageView)findViewById(R.id.imgBack);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        pieChart = (PieChart) findViewById(R.id.pieChart);

        tvMonth=(TextView)findViewById(R.id.tvMonth);

        int m = Calendar.getInstance().get(Calendar.MONTH) + 1;
        Log.d("month", String.valueOf(m));
        if (m == 1) {
            month = "January";
            showMonth="Jan";
        } else if (m == 2) {
            month = "February";
            showMonth="Feb";
        } else if (m == 3) {
            month = "March";
            showMonth="Mar";
        } else if (m == 4) {
            month = "April";
            showMonth="Apr";
        } else if (m == 5) {
            month = "May";
            showMonth="May";
        } else if (m == 6) {
            month = "Jun";
            showMonth="Jan";
        } else if (m == 7) {
            month = "July";
            showMonth="Jul";
        } else if (m == 8) {
            month = "Aug";
            showMonth="Aug";
        } else if (m == 9) {
            month = "Sep";
            showMonth="Sep";
        } else if (m == 10) {
            month = "Oct";
            showMonth="Jan";
        } else if (m == 11) {
            month = "Nov";
            showMonth="Nov";
        } else if (m == 12) {
            month = "December";
            showMonth="Dec";
        }

        tvToolBar.setText("Synopsis Report "+showMonth+"'20");



    }








    public void targetGet() {

        pd.show();
        String surl = AppData.url+"get_EmployeeSalesAchvAlert?ClientID="+prefManager.getEmpClintId()+"&MasterID="+prefManager.getMasterId()+"&Operation=1&SecurityCode=0000";
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
                                String Caption=obj.optString("Caption");
                                tvCap1.setText("-"+Caption);
                                String Percentage= obj.optString("Percentage").replace(".00","");
                                int p= Integer.parseInt(Percentage);
                                int deff=100-p;
                                Float defff= Float.valueOf(deff);
                                if (deff<0.00){
                                    diff=0;

                                }else {
                                    diff=defff;
                                }
                                float per= Float.parseFloat(Percentage);
                                String Target=obj.optString("Target");
                                String Sold=obj.optString("Sold");
                                tvTarget.setText(Target+" Units");
                                tvSold.setText(Sold+" Units");


                                pieValues.add(new PieEntry(per, "Achieved(%)"));
                                pieValues.add(new PieEntry(diff,"Pending(%)"));

                                PieDataSet dataSet = new PieDataSet(pieValues, "");

                                ArrayList<String> xVals = new ArrayList<String>();

                                xVals.add("Achieved");
                                xVals.add("Pending");


                                PieData pieData = new PieData(dataSet);
                                pieData.setValueFormatter(new PercentFormatter());
                                pieChart.setData(pieData);
                                dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                                dataSet.setSliceSpace(2f);
                                dataSet.setValueTextColor(Color.WHITE);
                                dataSet.setValueTextSize(10f);
                                dataSet.setSliceSpace(5f);

                                Legend l = pieChart.getLegend();
                                l.setEnabled(false);


                                pieChart.setDrawHoleEnabled(true);
                                pieChart.setTransparentCircleRadius(25f);
                                pieChart.setHoleRadius(25f);



                                pieChart.animateXY(1000, 1000);

                                String Zone=obj.optString("Zone");
                                prefManager.saveZoneNameForEmp(Zone);
                                String Branch=obj.optString("Branch");
                                prefManager.saveBranchNameForEmp(Branch);

                               // targetGet1();


                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SaleDialogActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(SaleDialogActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(SaleDialogActivity.this);
        requestQueue.add(stringRequest);

    }

    public void targetGet1() {

        pd.show();
        String surl = AppData.url+"get_EmployeeSalesAchvAlert?ClientID="+prefManager.getEmpClintId()+"&MasterID="+prefManager.getMasterId()+"&Operation=2&SecurityCode=0000";
        Log.d("inputCheck", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        pd.show();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            boolean responseStatus=job1.optBoolean("responseStatus");
                            if (responseStatus){
                                JSONArray job=job1.optJSONArray("responseData");
                                for (int i=0;i<job.length();i++) {
                                    JSONObject obj = job.optJSONObject(i);
                                    Caption = obj.optString("Caption");
                                    String Month = obj.optString("Month");
                                    Float Sold = Float.valueOf(obj.optString("Sold"));
                                    ChartModel cModel=new ChartModel(Sold,i,Month);
                                    itemList.add(cModel);
                                    tvCap2.setText(Caption);

                                }

                                for (int i = 0; i < itemList.size(); i++) {
                                    NoOfEmp.add(new BarEntry(itemList.get(i).getItem1(), itemList.get(i).getItem2()));
                                    year.add(itemList.get(i).getItem3());
                                    //for line chart
                                    yVals.add(new Entry(itemList.get(i).getItem1(), itemList.get(i).getItem2()));
                                    xVals.add(itemList.get(i).getItem3());



                                }
                                BarDataSet bardataset = new BarDataSet(NoOfEmp, "Sold Value");
                                barchart.animateY(1000);
                                //BarData data = new BarData(year, bardataset);
                                bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
                                //barchart.setData(data);

                                mChart = (LineChart) findViewById(R.id.linechart);
                                YAxis leftAxis = mChart.getAxisLeft();
                                leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
                                leftAxis.setAxisMaxValue(700f);
                                leftAxis.setAxisMinValue(-50f);
                                //leftAxis.setYOffset(20f);
                                leftAxis.enableGridDashedLine(10f, 10f, 0f);
                                leftAxis.setDrawZeroLine(false);


                                leftAxis.setDrawLimitLinesBehindData(true);

                                mChart.getAxisRight().setEnabled(false);
                               // mChart.animateX(1000, Easing.EasingOption.EaseInOutQuart);

                                mChart.invalidate();

                                LineDataSet set1;
                                set1 = new LineDataSet(yVals, "                         "+Caption);

                                set1.setFillAlpha(110);
                                set1.setColor(Color.BLACK);
                                set1.setCircleColor(Color.BLACK);
                                set1.setLineWidth(1f);
                                set1.setCircleRadius(3f);
                                set1.setDrawCircleHole(false);
                                set1.setValueTextSize(9f);
                                set1.setDrawFilled(true);

                                ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                                dataSets.add(set1); // add the datasets

                                // create a data object with the datasets
                               // LineData data1 = new LineData(xVals, dataSets);

                                // set data
                               // mChart.setData(data1);

                                Legend l = mChart.getLegend();

                                // modify the legend ...
                                // l.setPosition(LegendPosition.LEFT_OF_CHART);
                                l.setForm(Legend.LegendForm.LINE);


                               /* mChart.setDescription("Line Chart");
                                mChart.setNoDataTextDescription("You need to provide data for the chart.");*/

                                mChart.setTouchEnabled(true);
                                mChart.setDragEnabled(true);
                                mChart.setScaleEnabled(true);


                                targetGet2();










                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SaleDialogActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(SaleDialogActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(SaleDialogActivity.this);
        requestQueue.add(stringRequest);

    }

    public void targetGet2() {

        pd.show();
        String surl = AppData.url+"get_EmployeeSalesAchvAlert?ClientID="+prefManager.getEmpClintId()+"&MasterID="+prefManager.getMasterId()+"&Operation=3&SecurityCode=0000";
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
                                for (int i=0;i<job.length();i++) {
                                    JSONObject obj = job.optJSONObject(i);
                                    Cap1 = obj.optString("Caption");
                                    String VisitDay = obj.optString("VisitDay");
                                    Float Visit = Float.valueOf(obj.optString("Visit"));
                                    ChartModel cModel=new ChartModel(Visit,i,VisitDay);
                                    itemList1.add(cModel);
                                    tvCap3.setText(Caption);

                                }

                                for (int i = 0; i < itemList1.size(); i++) {
                                    NoOfEmp1.add(new BarEntry(itemList1.get(i).getItem1(), itemList1.get(i).getItem2()));
                                    year1.add(itemList1.get(i).getItem3());

                                    yVals1.add(new Entry(itemList1.get(i).getItem1(), itemList1.get(i).getItem2()));
                                    xVals1.add(itemList1.get(i).getItem3());
                                }
                                BarDataSet bardataset = new BarDataSet(NoOfEmp1, "No. Of Visit");
                                barchart1.animateY(1000);
                                //BarData data = new BarData(year1, bardataset);
                                bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
                               // barchart1.setData(data);

                                //linechart
                                mChrart1 = (LineChart) findViewById(R.id.linechart1);
                                YAxis leftAxis = mChrart1.getAxisLeft();
                                leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
                                leftAxis.setAxisMaxValue(220f);
                                leftAxis.setAxisMinValue(-50f);
                                //leftAxis.setYOffset(20f);
                                leftAxis.enableGridDashedLine(10f, 10f, 0f);
                                leftAxis.setDrawZeroLine(false);
                                leftAxis.setDrawLimitLinesBehindData(true);
                                mChrart1.getAxisRight().setEnabled(false);
                               // mChrart1.animateX(1000, Easing.EasingOption.EaseInOutQuart);
                                mChrart1.invalidate();
                                LineDataSet set1;
                                // create a dataset and give it a type
                                set1 = new LineDataSet(yVals1, "                         "+Cap1);
                                set1.setFillAlpha(110);
                                set1.setColor(Color.BLACK);
                                set1.setCircleColor(Color.BLACK);
                                set1.setLineWidth(1f);
                                set1.setCircleRadius(3f);
                                set1.setDrawCircleHole(false);
                                set1.setValueTextSize(9f);
                                set1.setDrawFilled(true);
                                ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                                dataSets.add(set1); // add the datasets
                                // create a data object with the datasets
                               // LineData data1 = new LineData(xVals1, dataSets);
                                // set data
                               // mChrart1.setData(data1);
                                Legend l = mChrart1.getLegend();
                                // modify the legend ...
                                // l.setPosition(LegendPosition.LEFT_OF_CHART);
                                l.setForm(Legend.LegendForm.LINE);
                               // mChrart1.setDescription(" Line Chart");
                               // mChrart1.setNoDataTextDescription("You need to provide data for the chart.");
                                mChrart1.setTouchEnabled(true);
                                // enable scaling and dragging
                                mChrart1.setDragEnabled(true);
                                mChrart1.setScaleEnabled(true);











                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SaleDialogActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(SaleDialogActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(SaleDialogActivity.this);
        requestQueue.add(stringRequest);

    }




}
