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
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
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


public class GraphicalMTDFragment extends Fragment {


    View view;
    ArrayList<ChartModel> itemList = new ArrayList<>();
    ArrayList<ChartModel> itemList1 = new ArrayList<>();
    PieChart pieChart, pieChart1;

    // ArrayList yVals = new ArrayList();


    int y;
    String cuyear, month;
    String financialYear;
    Pref pref;
    TextView tvMonth;
    CombinedChart comChart;
    ArrayList<PieEntry> pieValues = new ArrayList<>();

    ArrayList<PieEntry> pieValues1 = new ArrayList<>();
    String d;
    TextView tvCYTarget,tvCYAchieved;
    ArrayList<Float>cytarget=new ArrayList<>();
    ArrayList<Float>cysold=new ArrayList<>();
    Float cytargetSum,cysoldSum,lytargetSum,lysoldSum;
    BarChart severityBarChart,severityBarChart1;

    ArrayList<BarEntry> values = new ArrayList<>();
    ArrayList<String>p=new ArrayList<>();
    ArrayList<String>q=new ArrayList<>();
    TextView tvCYAchv,tvLYAchv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_graphical_m_t_d, container, false);
        initView();

        return view;
    }

    private void initView() {
        pref = new Pref(getContext());

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
       // tvMonth = (TextView) view.findViewById(R.id.tvMonth);
//        tvMonth.setText("Showing All Month Report :");
        pieChart = (PieChart) view.findViewById(R.id.pieChart);
        pieChart1 = (PieChart) view.findViewById(R.id.pieChart1);
        if (pref.getXYZ().equals("1")){
            d=pref.getEmpId();
        }else {
            d=pref.getID();
        }

        tvCYAchieved=(TextView)view.findViewById(R.id.tvCYAchieved) ;
        tvCYTarget=(TextView)view.findViewById(R.id.tvCYTarget) ;

        severityBarChart=(BarChart)view.findViewById(R.id.severityBarChart);
        severityBarChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        severityBarChart.setMaxVisibleValueCount(4);
        severityBarChart.getXAxis().setDrawGridLines(false);
        // scaling can now only be done on x- and y-axis separately
        severityBarChart.setPinchZoom(false);

        severityBarChart.setDrawBarShadow(false);
        severityBarChart.setDrawGridBackground(false);

        XAxis xAxis = severityBarChart.getXAxis();
        xAxis.setDrawGridLines(false);

        severityBarChart.getAxisLeft().setDrawGridLines(false);
        severityBarChart.getAxisRight().setDrawGridLines(false);
        severityBarChart.getAxisRight().setEnabled(false);
        severityBarChart.getAxisLeft().setEnabled(true);
        severityBarChart.getXAxis().setDrawGridLines(false);
        // add a nice and smooth animation
        severityBarChart.animateY(1500);


        severityBarChart.getLegend().setEnabled(false);

        severityBarChart.getAxisRight().setDrawLabels(false);
        severityBarChart.getAxisLeft().setDrawLabels(true);
        severityBarChart.setTouchEnabled(false);
        severityBarChart.setDoubleTapToZoomEnabled(false);
        severityBarChart.getXAxis().setEnabled(true);
        severityBarChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        severityBarChart.invalidate();
        p.add("Target");
        p.add("Achievement");


        severityBarChart1=(BarChart)view.findViewById(R.id.severityBarChart1);
        severityBarChart1.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        severityBarChart1.setMaxVisibleValueCount(4);
        severityBarChart1.getXAxis().setDrawGridLines(false);
        // scaling can now only be done on x- and y-axis separately
        severityBarChart1.setPinchZoom(false);

        severityBarChart1.setDrawBarShadow(false);
        severityBarChart1.setDrawGridBackground(false);

        XAxis xxAxis = severityBarChart1.getXAxis();
        xxAxis.setDrawGridLines(false);

        severityBarChart1.getAxisLeft().setDrawGridLines(false);
        severityBarChart1.getAxisRight().setDrawGridLines(false);
        severityBarChart1.getAxisRight().setEnabled(false);
        severityBarChart1.getAxisLeft().setEnabled(true);
        severityBarChart1.getXAxis().setDrawGridLines(false);
        // add a nice and smooth animation
        severityBarChart1.animateY(1500);


        severityBarChart1.getLegend().setEnabled(false);

        severityBarChart1.getAxisRight().setDrawLabels(false);
        severityBarChart1.getAxisLeft().setDrawLabels(true);
        severityBarChart1.setTouchEnabled(false);
        severityBarChart1.setDoubleTapToZoomEnabled(false);
        severityBarChart1.getXAxis().setEnabled(true);
        severityBarChart1.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        severityBarChart1.invalidate();

        tvLYAchv=(TextView)view.findViewById(R.id.tvLYAchv);
        tvCYAchv=(TextView)view.findViewById(R.id.tvCYAchv);




        getItemList(d);



    }

    private void getItemList(String id) {
        Log.d("Arpan", "arpan");
        final ProgressDialog progressBar = new ProgressDialog(getContext());
        progressBar.setMessage("Loading..");
        progressBar.setCancelable(false);
        progressBar.show();
        String surl = AppData.url + "get_EmployeeSalesReport?ClientID=" + pref.getEmpClintId() + "&UserID=" + id + "&FinancialYear=" + financialYear + "&Month=" + pref.getMonth() + "&RType=MTD&Operation=2&SecurityCode=" + pref.getSecurityCode();
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


                                JSONObject obj = responseData.getJSONObject(0);
                                float Sold = Float.parseFloat(obj.optString("Sold"));
                                String d=obj.optString("Sold");
                                String e=obj.optString("Target");
                                float Target = Float.parseFloat(obj.optString("Target"));
                                float AchvPer =  Float.parseFloat(obj.optString("AchvPer"));
                                float Prev_AchvPer = Float.parseFloat( obj.optString("Prev_AchvPer"));
                                Float Prev_Sold=Float.parseFloat(obj.optString("Prev_Sold"));
                                String PrevFinanYear =  obj.optString("PrevFinanYear");
                                String FinancialYear =  obj.optString("FinancialYear");
                                q.add("CY");
                                q.add("LY");

                                cysold.add(Sold);
                                cytarget.add(Target);
                                itemList.add(new ChartModel(Target,0,"Sold"));
                                itemList.add(new ChartModel(Sold,1,"Target"));

                                itemList1.add(new ChartModel(Sold,0,"Sold"));
                                itemList1.add(new ChartModel(Prev_Sold,1,"Achievement"));

                                String target=obj.optString("Target");
                                String sold=obj.optString("Sold");
                                String prev_sold=obj.optString("Prev_Sold");




                                tvCYTarget.setText(target+" Units");
                                tvCYAchieved.setText(sold+" Units");

                                tvCYAchv.setText(sold+" Units");
                                tvLYAchv.setText(prev_sold+" Units");




                                createBarChart(itemList);
                                createBarChart1(itemList1);



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

    private void createBarChart(ArrayList<ChartModel> severityListServer) {
        ArrayList<BarEntry> values = new ArrayList<>();

        for (int i = 0; i < severityListServer.size(); i++) {
            ChartModel dataObject = severityListServer.get(i);
            values.add(new BarEntry(i, dataObject.getItem1()));
        }

        BarDataSet set1;

        if (severityBarChart.getData() != null &&
                severityBarChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) severityBarChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            severityBarChart.getData().notifyDataChanged();
            severityBarChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "Data Set");
            set1.setColors(ColorTemplate.MATERIAL_COLORS);
            set1.setDrawValues(true);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            severityBarChart.setData(data);
            severityBarChart.setVisibleXRange(1, 4);
            severityBarChart.setFitBars(true);
            XAxis xAxis = severityBarChart.getXAxis();
            xAxis.setGranularity(1f);
            xAxis.setGranularityEnabled(true);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(p));//setting String values in Xaxis
            for (IDataSet set : severityBarChart.getData().getDataSets())
                set.setDrawValues(!set.isDrawValuesEnabled());

            severityBarChart.invalidate();
        }
    }

    private void createBarChart1(ArrayList<ChartModel> severityListServer) {
        ArrayList<BarEntry> values = new ArrayList<>();

        for (int i = 0; i < severityListServer.size(); i++) {
            ChartModel dataObject = severityListServer.get(i);
            values.add(new BarEntry(i, dataObject.getItem1()));
        }

        BarDataSet set1;

        if (severityBarChart1.getData() != null &&
                severityBarChart1.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) severityBarChart1.getData().getDataSetByIndex(0);
            set1.setValues(values);
            severityBarChart1.getData().notifyDataChanged();
            severityBarChart1.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "Data Set");
            set1.setColors(ColorTemplate.MATERIAL_COLORS);
            set1.setDrawValues(true);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            severityBarChart1.setData(data);
            severityBarChart1.setVisibleXRange(1, 4);
            severityBarChart1.setFitBars(true);
            XAxis xAxis = severityBarChart1.getXAxis();
            xAxis.setGranularity(1f);
            xAxis.setGranularityEnabled(true);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(q));//setting String values in Xaxis
            for (IDataSet set : severityBarChart1.getData().getDataSets())
                set.setDrawValues(!set.isDrawValuesEnabled());

            severityBarChart1.invalidate();
        }
    }


}