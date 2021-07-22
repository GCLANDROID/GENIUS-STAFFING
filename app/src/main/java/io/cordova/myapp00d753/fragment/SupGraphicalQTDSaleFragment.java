package io.cordova.myapp00d753.fragment;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.BarChart;
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
import com.github.mikephil.charting.utils.ColorTemplate;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class SupGraphicalQTDSaleFragment extends Fragment {


    View view;
    ArrayList<ChartModel> itemList = new ArrayList<>();
    ArrayList<ChartModel> itemList1 = new ArrayList<>();

    int y;
    String cuyear,month;
    String financialYear;
    Pref pref;
    TextView tvMonth;

    CombinedChart comChart,comChart1;
    ArrayList<Entry>yVals=new ArrayList<>();
    ArrayList<BarEntry>yyVals=new ArrayList<>();
    ArrayList<String>xVals=new ArrayList<>();


    ArrayList<Entry>finVals=new ArrayList<>();
    ArrayList<BarEntry>preFinVals=new ArrayList<>();
    ArrayList xxVals = new ArrayList();

    ArrayList<Float>cytarget=new ArrayList<>();
    ArrayList<Float>cysold=new ArrayList<>();
    Float cytargetSum,cysoldSum;
    TextView tvCYTarget,tvCYAchieved;







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_graphical_sale_qtd, container, false);
        initView();
        getItemList();
        return view;
    }

    private void initView() {

        pref=new Pref(getContext());
        tvMonth=(TextView)view.findViewById(R.id.tvMonth);


        financialYear = pref.getFinacialYear();

        tvMonth=(TextView)view.findViewById(R.id.tvMonth);
        tvMonth.setText("Showing Quaterly Report: ");

        TextView tvText1=(TextView)view.findViewById(R.id.tvText1);
        tvText1.setText("Quarterly Target VS Achieved (Units)");
        TextView tvText=(TextView)view.findViewById(R.id.tvText);
        tvText.setText("CY VS LY Achievement ( % ) (Quarter to Quarter)");

        tvCYTarget=(TextView)view.findViewById(R.id.tvCYTarget);
        tvCYAchieved=(TextView)view.findViewById(R.id.tvCYAchieved);




    }

    private void getItemList() {
        Log.d("Arpan", "arpan");
        final ProgressDialog progressBar = new ProgressDialog(getContext());
        progressBar.setMessage("Loading..");
        progressBar.setCancelable(false);
        progressBar.show();
        String surl = AppData.url + "get_EmployeeSalesReport?ClientID="+pref.getEmpClintId()+"&UserID="+pref.getID()+"&FinancialYear="+financialYear+"&Month="+pref.getMonth()+"&RType=QTD&Operation=2&SecurityCode="+pref.getSecurityCode();
        Log.d("input", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        progressBar.dismiss();
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
                                    float Sold = Float.parseFloat(obj.optString("Sold"));
                                    float Target = Float.parseFloat(obj.optString("Target"));
                                    String QuaterName = obj.optString("QuaterName");
                                    ChartModel cModel=new ChartModel(Sold,i,QuaterName);
                                    cModel.setItem4(Target);
                                    itemList.add(cModel);
                                    cytarget.add(Target);
                                    cysold.add(Sold);
                                }



                                for (int i = 0; i < itemList.size(); i++) {

                                    yVals.add(new Entry(itemList.get(i).getItem2(),itemList.get(i).getItem1()));
                                    yyVals.add(new BarEntry(itemList.get(i).getItem2(),itemList.get(i).getItem4()));
                                    xVals.add(itemList.get(i).getItem3());


                                }

                                if (cytarget.size()==1){
                                    cytargetSum = cytarget.get(0);
                                }else if (cytarget.size()==2){
                                    cytargetSum = cytarget.get(0)+cytarget.get(1);
                                }else if (cytarget.size()==3){
                                    cytargetSum = cytarget.get(0)+cytarget.get(1)+cytarget.get(2);
                                }else if (cytarget.size()==4){
                                    cytargetSum = cytarget.get(0)+cytarget.get(1)+cytarget.get(2)+cytarget.get(3);
                                }

                                tvCYTarget.setText(String.valueOf(cytargetSum)+" Units");


                                if (cysold.size()==1){
                                    cysoldSum = cysold.get(0);
                                }else if (cysold.size()==2){
                                    cysoldSum = cysold.get(0)+cysold.get(1);
                                }else if (cysold.size()==3){
                                    cysoldSum = cysold.get(0)+cysold.get(1)+cysold.get(2);
                                }else if (cysold.size()==4){
                                    cysoldSum = cysold.get(0)+cysold.get(1)+cysold.get(2)+cysold.get(3);
                                }

                                tvCYAchieved.setText(String.valueOf(cysoldSum)+" Units");










                                /*BarDataSet bardataset = new BarDataSet(yVals, " Sold");
                                chart.animateY(1000);
                               *//* BarData data = new BarData(year, bardataset);
                                bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
                                chart.setData(data);*/

                                //combined chart

                                comChart=view.findViewById(R.id.comChart);
                                comChart.setTouchEnabled(false);

                                comChart.getDescription().setText("");
                                comChart.setDrawGridBackground(true);
                                comChart.setDrawBarShadow(true);
                                comChart.setHighlightFullBarEnabled(true);
                                comChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                                        CombinedChart.DrawOrder.BAR,  CombinedChart.DrawOrder.LINE
                                });

                                Legend l = comChart.getLegend();
                                l.setWordWrapEnabled(true);
                                l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                                l.setOrientation(Legend.LegendOrientation.HORIZONTAL);

                                YAxis rightAxis = comChart.getAxisRight();
                                rightAxis.setEnabled(false);
                                rightAxis.setDrawGridLines(false);
                                rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

                                YAxis leftAxis = comChart.getAxisLeft();
                                leftAxis.setDrawGridLines(false);
                                leftAxis.setAxisMinimum(0f);

                                XAxis xAxis = comChart.getXAxis();
                                xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                                xAxis.setAxisMinimum(0f);
                                xAxis.setGranularity(1f);
                                xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));

                                CombinedData data = new CombinedData();

                                data.setData( generateLineData());
                                data.setData(generateBarData());

                                xAxis.setAxisMaximum(data.getXMax() + 0.25f);
                                comChart.setData(data);
                                comChart.invalidate();
                                getItemList1();



                            } else {

                                Toast.makeText(getContext(), "No data found", Toast.LENGTH_LONG).show();

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
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
    }
    private void getItemList1() {
        Log.d("Arpan", "arpan");
        final ProgressDialog progressBar = new ProgressDialog(getContext());
        progressBar.setMessage("Loading..");
        progressBar.setCancelable(false);
        progressBar.show();
        String surl = AppData.url + "get_EmployeeSalesReport?ClientID=" + pref.getEmpClintId() + "&UserID=" + pref.getID() + "&FinancialYear=" + financialYear + "&Month=" + month + "&RType=QTD&Operation=2&SecurityCode=" + pref.getSecurityCode();
        Log.d("input", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        progressBar.dismiss();
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
                                    float Sold = Float.parseFloat(obj.optString("Sold"));
                                    float Target = Float.parseFloat(obj.optString("Target"));
                                    float AchvPer = Float.parseFloat(obj.optString("AchvPer"));
                                    float Prev_AchvPer = Float.parseFloat(obj.optString("Prev_AchvPer"));
                                    String Month = obj.optString("QuaterName");
                                    float GrowthPercentage = Float.parseFloat(obj.optString("GrowthPercentage"));
                                    ChartModel cModel=new ChartModel(AchvPer,i,Month);
                                    cModel.setItem4(Prev_AchvPer);
                                    itemList1.add(cModel);
                                }



                                for (int i = 0; i < itemList1.size(); i++) {

                                    finVals.add(new Entry(itemList1.get(i).getItem2(),itemList1.get(i).getItem1()));
                                    preFinVals.add(new BarEntry(itemList1.get(i).getItem2(),itemList1.get(i).getItem4()));
                                    xxVals.add(itemList1.get(i).getItem3());



                                }






                                /*BarDataSet bardataset = new BarDataSet(yVals, " Sold");
                                chart.animateY(1000);
                               *//* BarData data = new BarData(year, bardataset);
                                bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
                                chart.setData(data);*/

                                //combined chart

                                comChart1=view.findViewById(R.id.comChart1);
                                comChart1.setTouchEnabled(false);

                                comChart1.getDescription().setText("");
                                comChart1.setDrawGridBackground(true);
                                comChart1.setDrawBarShadow(true);
                                comChart1.setHighlightFullBarEnabled(true);
                                comChart1.setDrawOrder(new CombinedChart.DrawOrder[]{
                                        CombinedChart.DrawOrder.BAR,  CombinedChart.DrawOrder.LINE
                                });
                                comChart1.animateX(1000);

                                Legend l = comChart1.getLegend();
                                l.setWordWrapEnabled(true);
                                l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                                l.setOrientation(Legend.LegendOrientation.HORIZONTAL);

                                YAxis rightAxis = comChart1.getAxisRight();
                                rightAxis.setEnabled(false);
                                rightAxis.setDrawGridLines(false);
                                rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

                                YAxis leftAxis = comChart1.getAxisLeft();
                                leftAxis.setDrawGridLines(false);
                                leftAxis.setAxisMinimum(0f);

                                XAxis xAxis = comChart1.getXAxis();
                                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                                xAxis.setAxisMinimum(0f);
                                xAxis.setGranularity(1f);
                                xAxis.setValueFormatter(new IndexAxisValueFormatter(xxVals));



                                CombinedData data = new CombinedData();

                                data.setData( generateFinLineData());
                                data.setData(generatePreBarData());

                                xAxis.setAxisMaximum(data.getXMax() + 0.25f);
                                comChart1.setData(data);
                                comChart1.invalidate();




                            } else {

                                Toast.makeText(getContext(), "No data found", Toast.LENGTH_LONG).show();

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
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
    }


    private LineData generateLineData() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<Entry>();

        entries = yVals;

        LineDataSet set = new LineDataSet(entries, "CY Achieved Line Chart)");
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

    private BarData generateBarData() {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        entries = yyVals;

        BarDataSet set1 = new BarDataSet(entries, "CY Target (Bar Chart)");
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


    private LineData generateFinLineData() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<Entry>();

        entries = finVals;

        LineDataSet set = new LineDataSet(entries, "CY Achievement (%) (Line Chart)");
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

    private BarData generatePreBarData() {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        entries = preFinVals;

        BarDataSet set1 = new BarDataSet(entries, "LY Achievement (%) (Bar Chart)");
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

}
