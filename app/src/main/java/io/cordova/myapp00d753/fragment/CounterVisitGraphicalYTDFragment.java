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


public class CounterVisitGraphicalYTDFragment extends Fragment {

    View view;
    ArrayList<ChartModel> itemList = new ArrayList<>();
    CombinedChart comChart;
    ArrayList<Entry>yVals=new ArrayList<>();
    ArrayList<BarEntry>yyVals=new ArrayList<>();
    ArrayList<String>xVals=new ArrayList<>();


    BarChart chart,barTargetchart;
    ArrayList NoOfEmp = new ArrayList();
    ArrayList year = new ArrayList();


    int y;
    String cuyear,month;
    String financialYear;
    Pref pref;



    PieChart pieChart;
    TextView tvTitle,tvCount,tvLYTitle,tvLYCount;

    ArrayList<Integer>cd=new ArrayList<>();
    ArrayList<Integer>od=new ArrayList<>();
    Integer sum,preSum;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.counter_graph, container, false);
        initView();

        return view;
    }

    private void initView() {
        pref=new Pref(getContext());



        y = Calendar.getInstance().get(Calendar.YEAR);
        cuyear = String.valueOf(y);
        Log.d("year", cuyear);

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
        financialYear = pref.getFinacialYear();

        tvTitle=(TextView)view.findViewById(R.id.tvTitle);
        tvCount=(TextView)view.findViewById(R.id.tvCount);
        tvLYTitle=(TextView)view.findViewById(R.id.tvLYTitle);
        tvLYCount=(TextView)view.findViewById(R.id.tvLYCount);

        tvTitle.setText("CY Visits: ");
        tvLYTitle.setText("LY Visits: ");

        if (pref.getXYZ().equals("emp")){
            getItemList(pref.getEmpId());
        }else {
            getItemList(pref.getID());
        }



        //String FinancialYear = obj.optString("FinancialYear");



    }

    private void getItemList(String d) {
        Log.d("Arpan", "arpan");
        final ProgressDialog progressBar = new ProgressDialog(getContext());
        progressBar.setMessage("Loading..");
        progressBar.setCancelable(false);
        progressBar.show();
        String surl = AppData.url + "get_EmployeeVisitActivity?ClientID=" + pref.getEmpClintId() + "&UserID=" + d + "&FinancialYear=" + financialYear + "&Month="+pref.getMonth()+"&RType=YTD&Operation=2&SecurityCode=" + pref.getSecurityCode();
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
                                    String Month = obj.optString("Month");
                                    Float TotalVisit = Float.valueOf(obj.optString("TotalVisit"));
                                    Float Prev_TotalVisit = Float.valueOf(obj.optString("Prev_TotalVisit"));
                                    ChartModel cModel=new ChartModel(TotalVisit,i,Month);
                                    cModel.setItem4(Prev_TotalVisit);
                                    itemList.add(cModel);

                                    Integer d=Integer.parseInt(obj.optString("TotalVisit"));
                                    Integer e=Integer.parseInt(obj.optString("Prev_TotalVisit"));
                                    cd.add(d);
                                    od.add(e);
                                }

                                for (int i = 0; i < itemList.size(); i++) {

                                    yVals.add(new Entry(itemList.get(i).getItem2(),itemList.get(i).getItem1()));
                                    yyVals.add(new BarEntry(itemList.get(i).getItem2(),itemList.get(i).getItem4()));
                                    xVals.add(itemList.get(i).getItem3());



                                }

                                comChart=view.findViewById(R.id.comChart);
                                comChart.setTouchEnabled(false);

                                comChart.getDescription().setText("");
                                comChart.setDrawGridBackground(false);
                                comChart.setDrawBarShadow(false);
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
                                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                                xAxis.setAxisMinimum(0f);
                                xAxis.setGranularity(1f);
                                xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));

                                CombinedData data = new CombinedData();

                                data.setData( generateLineData());
                                data.setData(generateBarData());

                                xAxis.setAxisMaximum(data.getXMax() + 0.25f);
                                comChart.setData(data);
                                comChart.invalidate();
                                if (cd.size()==1){
                                    sum=cd.get(0);
                                }else if (cd.size()==2){
                                    sum=cd.get(0)+cd.get(1);
                                }else if (cd.size()==3){
                                    sum=cd.get(0)+cd.get(1)+cd.get(2);
                                }else if (cd.size()==4){
                                    sum=cd.get(0)+cd.get(1)+cd.get(2)+cd.get(3);
                                }else if (cd.size()==5){
                                    sum=cd.get(0)+cd.get(1)+cd.get(2)+cd.get(3)+cd.get(4);
                                }else if (cd.size()==6){
                                    sum=cd.get(0)+cd.get(1)+cd.get(2)+cd.get(3)+cd.get(4)+cd.get(5);
                                }else if (cd.size()==7){
                                    sum=cd.get(0)+cd.get(1)+cd.get(2)+cd.get(3)+cd.get(4)+cd.get(5)+cd.get(6);
                                }else if (cd.size()==8){
                                    sum=cd.get(0)+cd.get(1)+cd.get(2)+cd.get(3)+cd.get(4)+cd.get(5)+cd.get(6)+cd.get(7);
                                }else if (cd.size()==9){
                                    sum=cd.get(0)+cd.get(1)+cd.get(2)+cd.get(3)+cd.get(4)+cd.get(5)+cd.get(6)+cd.get(7)+cd.get(8);
                                }else if (cd.size()==10){
                                    sum=cd.get(0)+cd.get(1)+cd.get(2)+cd.get(3)+cd.get(4)+cd.get(5)+cd.get(6)+cd.get(7)+cd.get(8)+cd.get(9);
                                }else if (cd.size()==11){
                                    sum=cd.get(0)+cd.get(1)+cd.get(2)+cd.get(3)+cd.get(4)+cd.get(5)+cd.get(6)+cd.get(7)+cd.get(8)+cd.get(9)+cd.get(10);
                                }else if (cd.size()==12){
                                    sum=cd.get(0)+cd.get(1)+cd.get(2)+cd.get(3)+cd.get(4)+cd.get(5)+cd.get(6)+cd.get(7)+cd.get(8)+cd.get(9)+cd.get(10)+cd.get(11);
                                }



                                tvCount.setText(String.valueOf(sum));


                                if (od.size()==1){
                                    preSum=od.get(0);
                                }else if (od.size()==2){
                                    preSum=od.get(0)+od.get(1);
                                }else if (od.size()==3){
                                    preSum=od.get(0)+od.get(1)+od.get(2);
                                }else if (od.size()==4){
                                    preSum=od.get(0)+od.get(1)+od.get(2)+od.get(3);
                                }else if (od.size()==5){
                                    preSum=od.get(0)+od.get(1)+od.get(2)+od.get(3)+od.get(4);
                                }else if (od.size()==6){
                                    preSum=od.get(0)+od.get(1)+od.get(2)+od.get(3)+od.get(4)+od.get(5);
                                }else if (od.size()==7){
                                    preSum=od.get(0)+od.get(1)+od.get(2)+od.get(3)+od.get(4)+od.get(5)+od.get(6);
                                }else if (od.size()==8){
                                    preSum=od.get(0)+od.get(1)+od.get(2)+od.get(3)+od.get(4)+od.get(5)+od.get(6)+od.get(7);
                                }else if (od.size()==9){
                                    preSum=od.get(0)+od.get(1)+od.get(2)+od.get(3)+od.get(4)+od.get(5)+od.get(6)+od.get(7)+od.get(8);
                                }else if (od.size()==10){
                                    preSum=od.get(0)+od.get(1)+od.get(2)+od.get(3)+od.get(4)+od.get(5)+od.get(6)+od.get(7)+od.get(8)+od.get(9);
                                }else if (od.size()==11){
                                    preSum=od.get(0)+od.get(1)+od.get(2)+od.get(3)+od.get(4)+od.get(5)+od.get(6)+od.get(7)+od.get(8)+od.get(9)+od.get(10);
                                }else if (od.size()==12){
                                    preSum=od.get(0)+od.get(1)+od.get(2)+od.get(3)+od.get(4)+od.get(5)+od.get(6)+od.get(7)+od.get(8)+od.get(9)+od.get(10)+od.get(11);
                                }

                                tvLYCount.setText(String.valueOf(preSum));










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

        LineDataSet set = new LineDataSet(entries, "CY Visits (Line chart)");
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

        BarDataSet set1 = new BarDataSet(entries, "LY Visits (Bar Chart)");
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