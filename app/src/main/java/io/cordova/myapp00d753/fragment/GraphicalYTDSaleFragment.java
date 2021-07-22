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

/**
 * A simple {@link Fragment} subclass.
 */
public class GraphicalYTDSaleFragment extends Fragment {


    View view;
    ArrayList<ChartModel> itemList = new ArrayList<>();
    ArrayList<ChartModel> itemList1 = new ArrayList<>();
    BarChart chart;
    ArrayList NoOfEmp = new ArrayList();
    ArrayList xVals = new ArrayList();

    int y;
    String cuyear, month;
    String financialYear;
    Pref pref;
    TextView tvMonth;
    CombinedChart comChart,comChart1;

    ArrayList<Entry>yVals=new ArrayList<>();
    ArrayList<BarEntry>yyVals=new ArrayList<>();

    ArrayList<Entry>finVals=new ArrayList<>();
    ArrayList<BarEntry>preFinVals=new ArrayList<>();
    ArrayList xxVals = new ArrayList();

    ArrayList<Integer>cytarget=new ArrayList<>();
    ArrayList<Integer>cysold=new ArrayList<>();
    ArrayList<Integer>cyAchv=new ArrayList<>();
    ArrayList<Integer>lyAchv=new ArrayList<>();
    Integer cytargetSum,cysoldSum,cyAchvSum,lyAchvSum;
    TextView tvCYTarget,tvCYAchieved,tvCYAchv,tvLYAchv;
    TextView tvTitle,tvTitle1,tvCYTitle,tvCYTitle1;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_graphical_sale_qtd, container, false);
        initView();


        return view;
    }

    private void initView() {
        pref = new Pref(getContext());
        chart = view.findViewById(R.id.barchart);

        y = Calendar.getInstance().get(Calendar.YEAR);
        cuyear = String.valueOf(y);
        Log.d("year", cuyear);

        int m = Calendar.getInstance().get(Calendar.MONTH) + 1;
        Log.d("month", String.valueOf(m));
        month = pref.getMonth();
        financialYear = pref.getFinacialYear();
        tvMonth = (TextView) view.findViewById(R.id.tvMonth);
        tvMonth.setText("Showing  Yearly Report:");

        comChart=(CombinedChart)view.findViewById(R.id.comChart);
        comChart1=(CombinedChart)view.findViewById(R.id.comChart1);

        tvTitle=(TextView)view.findViewById(R.id.tvTitle);
        tvTitle1=(TextView)view.findViewById(R.id.tvTitle1);

        tvTitle.setText("CY Target: ");
        tvTitle1.setText("CY Achievement: ");

        tvCYTitle=(TextView)view.findViewById(R.id.tvCYTitle);
        tvCYTitle1=(TextView)view.findViewById(R.id.tvCYTitle1);

        tvCYTitle.setText("CY Achievement: ");
        tvCYTitle1.setText("LY Achievement: ");

        tvCYTarget=(TextView)view.findViewById(R.id.tvCYTarget);
        tvCYAchieved=(TextView)view.findViewById(R.id.tvCYAchieved);

        tvCYAchv=(TextView)view.findViewById(R.id.tvCYAchv);
        tvLYAchv=(TextView)view.findViewById(R.id.tvLYAchv);


        if (pref.getXYZ().equals("1")){
            getItemList(pref.getEmpId());
        }else {
            getItemList(pref.getID());
        }







    }

    private void getItemList(String d) {
        Log.d("Arpan", "arpan");
        final ProgressDialog progressBar = new ProgressDialog(getContext());
        progressBar.setMessage("Loading..");
        progressBar.setCancelable(false);
        progressBar.show();
        String surl = AppData.url + "get_EmployeeSalesReport?ClientID=" + pref.getEmpClintId() + "&UserID=" + d + "&FinancialYear=" + financialYear + "&Month=" + month + "&RType=YTD&Operation=2&SecurityCode=" + pref.getSecurityCode();
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
                                    String Month = obj.optString("Month");
                                    String GrowthPercentage = obj.optString("GrowthPercentage");
                                    ChartModel cModel=new ChartModel(Sold,i,Month);
                                    cModel.setItem4(Target);
                                    itemList.add(cModel);

                                    Integer iSold=Integer.parseInt(obj.optString("Sold"));
                                    Integer iTarget=Integer.parseInt(obj.optString("Target"));
                                    cysold.add(iSold);
                                    cytarget.add(iTarget);
                                }



                                for (int i = 0; i < itemList.size(); i++) {

                                    yVals.add(new Entry(itemList.get(i).getItem2(),itemList.get(i).getItem1()));
                                    yyVals.add(new BarEntry(itemList.get(i).getItem2(),itemList.get(i).getItem4()));
                                    xVals.add(itemList.get(i).getItem3());



                                }

                                if (cysold.size()==1){
                                    cysoldSum=cysold.get(0);
                                }else if (cysold.size()==2){
                                    cysoldSum=cysold.get(0)+cysold.get(1);
                                }else if (cysold.size()==3){
                                    cysoldSum=cysold.get(0)+cysold.get(1)+cysold.get(2);
                                }else if (cysold.size()==4){
                                    cysoldSum=cysold.get(0)+cysold.get(1)+cysold.get(2)+cysold.get(3);
                                }else if (cysold.size()==5){
                                    cysoldSum=cysold.get(0)+cysold.get(1)+cysold.get(2)+cysold.get(3)+cysold.get(4);
                                }else if (cysold.size()==6){
                                    cysoldSum=cysold.get(0)+cysold.get(1)+cysold.get(2)+cysold.get(3)+cysold.get(4)+cysold.get(5);
                                }else if (cysold.size()==7){
                                    cysoldSum=cysold.get(0)+cysold.get(1)+cysold.get(2)+cysold.get(3)+cysold.get(4)+cysold.get(5)+cysold.get(6);
                                }else if (cysold.size()==8){
                                    cysoldSum=cysold.get(0)+cysold.get(1)+cysold.get(2)+cysold.get(3)+cysold.get(4)+cysold.get(5)+cysold.get(6)+cysold.get(7);
                                }else if (cysold.size()==9){
                                    cysoldSum=cysold.get(0)+cysold.get(1)+cysold.get(2)+cysold.get(3)+cysold.get(4)+cysold.get(5)+cysold.get(6)+cysold.get(7)+cysold.get(8);
                                }else if (cysold.size()==10){
                                    cysoldSum=cysold.get(0)+cysold.get(1)+cysold.get(2)+cysold.get(3)+cysold.get(4)+cysold.get(5)+cysold.get(6)+cysold.get(7)+cysold.get(8)+cysold.get(9);
                                }else if (cysold.size()==11){
                                    cysoldSum=cysold.get(0)+cysold.get(1)+cysold.get(2)+cysold.get(3)+cysold.get(4)+cysold.get(5)+cysold.get(6)+cysold.get(7)+cysold.get(8)+cysold.get(9)+cysold.get(10);
                                }else if (cysold.size()==12){
                                    cysoldSum=cysold.get(0)+cysold.get(1)+cysold.get(2)+cysold.get(3)+cysold.get(4)+cysold.get(5)+cysold.get(6)+cysold.get(7)+cysold.get(8)+cysold.get(9)+cysold.get(10)+cysold.get(11);
                                }



                                tvCYAchieved.setText(String.valueOf(cysoldSum)+" Units");


                                if (cytarget.size()==1){
                                    cytargetSum=cytarget.get(0);
                                }else if (cytarget.size()==2){
                                    cytargetSum=cytarget.get(0)+cytarget.get(1);
                                }else if (cytarget.size()==3){
                                    cytargetSum=cytarget.get(0)+cytarget.get(1)+cytarget.get(2);
                                }else if (cytarget.size()==4){
                                    cytargetSum=cytarget.get(0)+cytarget.get(1)+cytarget.get(2)+cytarget.get(3);
                                }else if (cytarget.size()==5){
                                    cytargetSum=cytarget.get(0)+cytarget.get(1)+cytarget.get(2)+cytarget.get(3)+cytarget.get(4);
                                }else if (cytarget.size()==6){
                                    cytargetSum=cytarget.get(0)+cytarget.get(1)+cytarget.get(2)+cytarget.get(3)+cytarget.get(4)+cytarget.get(5);
                                }else if (cytarget.size()==7){
                                    cytargetSum=cytarget.get(0)+cytarget.get(1)+cytarget.get(2)+cytarget.get(3)+cytarget.get(4)+cytarget.get(5)+cytarget.get(6);
                                }else if (cytarget.size()==8){
                                    cytargetSum=cytarget.get(0)+cytarget.get(1)+cytarget.get(2)+cytarget.get(3)+cytarget.get(4)+cytarget.get(5)+cytarget.get(6)+cytarget.get(7);
                                }else if (cytarget.size()==9){
                                    cytargetSum=cytarget.get(0)+cytarget.get(1)+cytarget.get(2)+cytarget.get(3)+cytarget.get(4)+cytarget.get(5)+cytarget.get(6)+cytarget.get(7)+cytarget.get(8);
                                }else if (cytarget.size()==10){
                                    cytargetSum=cytarget.get(0)+cytarget.get(1)+cytarget.get(2)+cytarget.get(3)+cytarget.get(4)+cytarget.get(5)+cytarget.get(6)+cytarget.get(7)+cytarget.get(8)+cytarget.get(9);
                                }else if (cytarget.size()==11){
                                    cytargetSum=cytarget.get(0)+cytarget.get(1)+cytarget.get(2)+cytarget.get(3)+cytarget.get(4)+cytarget.get(5)+cytarget.get(6)+cytarget.get(7)+cytarget.get(8)+cytarget.get(9)+cytarget.get(10);
                                }else if (cytarget.size()==12){
                                    cytargetSum=cytarget.get(0)+cytarget.get(1)+cytarget.get(2)+cytarget.get(3)+cytarget.get(4)+cytarget.get(5)+cytarget.get(6)+cytarget.get(7)+cytarget.get(8)+cytarget.get(9)+cytarget.get(10)+cytarget.get(11);
                                }

                                tvCYTarget.setText(String.valueOf(cytargetSum)+" Units");












                                /*BarDataSet bardataset = new BarDataSet(yVals, " Sold");
                                chart.animateY(1000);
                               *//* BarData data = new BarData(year, bardataset);
                                bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
                                chart.setData(data);*/

                                //combined chart

                                comChart=view.findViewById(R.id.comChart);
                                comChart.setTouchEnabled(false);

                                comChart.getDescription().setText("");
                                comChart.setDrawGridBackground(false);
                                comChart.setDrawBarShadow(false);
                                comChart.setHighlightFullBarEnabled(false);
                                comChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                                        CombinedChart.DrawOrder.BAR,  CombinedChart.DrawOrder.LINE
                                });
                                comChart.animateX(1000);


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
                                data.setData(generateBarData());
                                data.setData( generateLineData());


                                xAxis.setAxisMaximum(data.getXMax() + 0.25f);
                                comChart.setData(data);
                                comChart.invalidate();

                                if (pref.getXYZ().equals("1")){
                                    getItemList1(pref.getEmpId());
                                }else {
                                    getItemList1(pref.getID());
                                }




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

    private void getItemList1(String d) {
        Log.d("Arpan", "arpan");
        final ProgressDialog progressBar = new ProgressDialog(getContext());
        progressBar.setMessage("Loading..");
        progressBar.setCancelable(false);
        progressBar.show();
        String surl = AppData.url + "get_EmployeeSalesReport?ClientID=" + pref.getEmpClintId() + "&UserID=" + d + "&FinancialYear=" + financialYear + "&Month=" + month + "&RType=YTD&Operation=2&SecurityCode=" + pref.getSecurityCode();
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
                                    Float Prev_Sold=Float.parseFloat(obj.optString("Prev_Sold"));
                                    float Target = Float.parseFloat(obj.optString("Target"));
                                    float AchvPer = Float.parseFloat(obj.optString("AchvPer"));
                                    float Prev_AchvPer = Float.parseFloat(obj.optString("Prev_AchvPer"));
                                    String Month = obj.optString("Month");
                                    float GrowthPercentage = Float.parseFloat(obj.optString("GrowthPercentage"));
                                    ChartModel cModel=new ChartModel(Sold,i,Month);
                                    cModel.setItem4(Prev_Sold);
                                    itemList1.add(cModel);
                                    Integer iSold=Integer.parseInt(obj.optString("Sold"));
                                    Integer iPrevSold=Integer.parseInt(obj.optString("Prev_Sold"));
                                    cyAchv.add(iSold);
                                    lyAchv.add(iPrevSold);
                                }


                                if (cyAchv.size()==1){
                                    cyAchvSum=cyAchv.get(0);
                                }else if (cyAchv.size()==2){
                                    cyAchvSum=cyAchv.get(0)+cyAchv.get(1);
                                }else if (cyAchv.size()==3){
                                    cyAchvSum=cyAchv.get(0)+cyAchv.get(1)+cyAchv.get(2);
                                }else if (cyAchv.size()==4){
                                    cyAchvSum=cyAchv.get(0)+cyAchv.get(1)+cyAchv.get(2)+cyAchv.get(3);
                                }else if (cyAchv.size()==5){
                                    cyAchvSum=cyAchv.get(0)+cyAchv.get(1)+cyAchv.get(2)+cyAchv.get(3)+cyAchv.get(4);
                                }else if (cyAchv.size()==6){
                                    cyAchvSum=cyAchv.get(0)+cyAchv.get(1)+cyAchv.get(2)+cyAchv.get(3)+cyAchv.get(4)+cyAchv.get(5);
                                }else if (cyAchv.size()==7){
                                    cyAchvSum=cyAchv.get(0)+cyAchv.get(1)+cyAchv.get(2)+cyAchv.get(3)+cyAchv.get(4)+cyAchv.get(5)+cyAchv.get(6);
                                }else if (cyAchv.size()==8){
                                    cyAchvSum=cyAchv.get(0)+cyAchv.get(1)+cyAchv.get(2)+cyAchv.get(3)+cyAchv.get(4)+cyAchv.get(5)+cyAchv.get(6)+cyAchv.get(7);
                                }else if (cyAchv.size()==9){
                                    cyAchvSum=cyAchv.get(0)+cyAchv.get(1)+cyAchv.get(2)+cyAchv.get(3)+cyAchv.get(4)+cyAchv.get(5)+cyAchv.get(6)+cyAchv.get(7)+cyAchv.get(8);
                                }else if (cyAchv.size()==10){
                                    cyAchvSum=cyAchv.get(0)+cyAchv.get(1)+cyAchv.get(2)+cyAchv.get(3)+cyAchv.get(4)+cyAchv.get(5)+cyAchv.get(6)+cyAchv.get(7)+cyAchv.get(8)+cyAchv.get(9);
                                }else if (cyAchv.size()==11){
                                    cyAchvSum=cyAchv.get(0)+cyAchv.get(1)+cyAchv.get(2)+cyAchv.get(3)+cyAchv.get(4)+cyAchv.get(5)+cyAchv.get(6)+cyAchv.get(7)+cyAchv.get(8)+cyAchv.get(9)+cyAchv.get(10);
                                }else if (cysold.size()==12){
                                    cyAchvSum=cyAchv.get(0)+cyAchv.get(1)+cyAchv.get(2)+cyAchv.get(3)+cyAchv.get(4)+cyAchv.get(5)+cyAchv.get(6)+cyAchv.get(7)+cyAchv.get(8)+cyAchv.get(9)+cyAchv.get(10)+cyAchv.get(11);
                                }



                                tvCYAchv.setText(String.valueOf(cyAchvSum)+" Units");


                                if (lyAchv.size()==1){
                                    lyAchvSum=lyAchv.get(0);
                                }else if (lyAchv.size()==2){
                                    lyAchvSum=lyAchv.get(0)+lyAchv.get(1);
                                }else if (lyAchv.size()==3){
                                    lyAchvSum=lyAchv.get(0)+lyAchv.get(1)+lyAchv.get(2);
                                }else if (lyAchv.size()==4){
                                    lyAchvSum=lyAchv.get(0)+lyAchv.get(1)+lyAchv.get(2)+lyAchv.get(3);
                                }else if (lyAchv.size()==5){
                                    lyAchvSum=lyAchv.get(0)+lyAchv.get(1)+lyAchv.get(2)+lyAchv.get(3)+lyAchv.get(4);
                                }else if (lyAchv.size()==6){
                                    lyAchvSum=lyAchv.get(0)+lyAchv.get(1)+lyAchv.get(2)+lyAchv.get(3)+lyAchv.get(4)+lyAchv.get(5);
                                }else if (lyAchv.size()==7){
                                    lyAchvSum=lyAchv.get(0)+lyAchv.get(1)+lyAchv.get(2)+lyAchv.get(3)+lyAchv.get(4)+lyAchv.get(5)+lyAchv.get(6);
                                }else if (lyAchv.size()==8){
                                    lyAchvSum=lyAchv.get(0)+lyAchv.get(1)+lyAchv.get(2)+lyAchv.get(3)+lyAchv.get(4)+lyAchv.get(5)+lyAchv.get(6)+lyAchv.get(7);
                                }else if (lyAchv.size()==9){
                                    lyAchvSum=lyAchv.get(0)+lyAchv.get(1)+lyAchv.get(2)+lyAchv.get(3)+lyAchv.get(4)+lyAchv.get(5)+lyAchv.get(6)+lyAchv.get(7)+lyAchv.get(8);
                                }else if (lyAchv.size()==10){
                                    lyAchvSum=lyAchv.get(0)+lyAchv.get(1)+lyAchv.get(2)+lyAchv.get(3)+lyAchv.get(4)+lyAchv.get(5)+lyAchv.get(6)+lyAchv.get(7)+lyAchv.get(8)+lyAchv.get(9);
                                }else if (lyAchv.size()==11){
                                    lyAchvSum=lyAchv.get(0)+lyAchv.get(1)+lyAchv.get(2)+lyAchv.get(3)+lyAchv.get(4)+lyAchv.get(5)+lyAchv.get(6)+lyAchv.get(7)+lyAchv.get(8)+lyAchv.get(9)+lyAchv.get(10);
                                }else if (lyAchv.size()==12){
                                    lyAchvSum=lyAchv.get(0)+lyAchv.get(1)+lyAchv.get(2)+lyAchv.get(3)+lyAchv.get(4)+lyAchv.get(5)+lyAchv.get(6)+lyAchv.get(7)+lyAchv.get(8)+lyAchv.get(9)+lyAchv.get(10)+lyAchv.get(11);
                                }

                                tvLYAchv.setText(String.valueOf(lyAchvSum)+" Units");






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
                                comChart1.setDrawGridBackground(false);
                                comChart1.setDrawBarShadow(false);
                                comChart1.setHighlightFullBarEnabled(false);
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

        LineDataSet set = new LineDataSet(entries, "CY Achievement (Line Chart)");
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

        LineDataSet set = new LineDataSet(entries, "CY Achievement (Line Chart)");
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

        BarDataSet set1 = new BarDataSet(entries, "LY Achievement  (Bar Chart)");
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
