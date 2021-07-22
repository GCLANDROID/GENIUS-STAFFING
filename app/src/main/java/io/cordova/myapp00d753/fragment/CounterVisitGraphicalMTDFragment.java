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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
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


public class CounterVisitGraphicalMTDFragment extends Fragment {

    View view;
    ArrayList<ChartModel> itemList = new ArrayList<>();

    BarChart chart, barTargetchart;
    ArrayList NoOfEmp = new ArrayList();
    ArrayList year = new ArrayList();
    ArrayList yVals = new ArrayList();


    int y;
    String cuyear, month;
    String financialYear;
    Pref pref;
    PieChart pieChart;
    TextView tvCount,tvLYCount;
    BarChart barChart;
    ArrayList<String>q=new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_counter_visit_graphical_y_t_d, container, false);
        initView();

        return view;
    }

    private void initView() {
        pref = new Pref(getContext());
        chart = view.findViewById(R.id.barchart);
        barTargetchart = view.findViewById(R.id.barTargetchart);

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
        pieChart = (PieChart) view.findViewById(R.id.pieChart);
        tvCount=(TextView)view.findViewById(R.id.tvCount);
        tvLYCount=(TextView)view.findViewById(R.id.tvLYCount);
        TextView tvTitle=(TextView)view.findViewById(R.id.tvTitle);
        tvTitle.setText("Monthly Visits (CY): ");
        TextView tvLYTitle=(TextView)view.findViewById(R.id.tvLYTitle);
        tvLYTitle.setText("Monthly Visits (LY): ");


        barChart=(BarChart)view.findViewById(R.id.barChart);
        barChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        barChart.setMaxVisibleValueCount(4);
        barChart.getXAxis().setDrawGridLines(false);
        // scaling can now only be done on x- and y-axis separately
        barChart.setPinchZoom(false);

        barChart.setDrawBarShadow(false);
        barChart.setDrawGridBackground(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawGridLines(false);

        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setEnabled(true);
        barChart.getXAxis().setDrawGridLines(false);
        // add a nice and smooth animation
        barChart.animateY(1500);


        barChart.getLegend().setEnabled(false);

        barChart.getAxisRight().setDrawLabels(false);
        barChart.getAxisLeft().setDrawLabels(true);
        barChart.setTouchEnabled(false);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.getXAxis().setEnabled(true);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.invalidate();

        if (pref.getXYZ().equals("emp")){
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
        String surl = AppData.url + "get_EmployeeVisitActivity?ClientID=" + pref.getEmpClintId() + "&UserID=" + d + "&FinancialYear=" + financialYear + "&Month=" + pref.getMonth() + "&RType=MTD&Operation=2&SecurityCode=" + pref.getSecurityCode();
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
                                String FinancialYear = obj.optString("Month");
                                Float TotalVisit = Float.valueOf(obj.optString("TotalVisit"));
                                Float Prev_TotalVisit = Float.valueOf(obj.optString("Prev_TotalVisit"));

                                String displayTotalVisit=obj.optString("TotalVisit");
                                String prevdisplayTotalVist=obj.optString("Prev_TotalVisit");

                                itemList.add(new ChartModel(TotalVisit,0,""));
                                itemList.add(new ChartModel(Prev_TotalVisit,0,""));
                                q.add("CY");
                                q.add("LY");

                                createBarChart(itemList);



                                yVals.add(new PieEntry(TotalVisit, "CY"));
                                yVals.add(new PieEntry(Prev_TotalVisit, "LY"));

                                /*BarDataSet barDataSet = new BarDataSet(NoOfEmp, "");
                                BarData barData = new BarData(barDataSet);
                                chart.setData(barData);
                                barDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                                barDataSet.setValueTextColor(Color.BLACK);
                                barDataSet.setValueTextSize(18f);*/

                                PieDataSet dataSet = new PieDataSet(yVals, "Tour Visit Count");
                                PieData pieData = new PieData(dataSet);
                                pieData.setValueFormatter(new LargeValueFormatter());
                                pieChart.setData(pieData);
                                dataSet.setColors(ColorTemplate.PASTEL_COLORS);
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
                                tvCount.setText(displayTotalVisit);
                                tvLYCount.setText(prevdisplayTotalVist);


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

        if (barChart.getData() != null &&
                barChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "Data Set");
            set1.setColors(ColorTemplate.MATERIAL_COLORS);
            set1.setDrawValues(true);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            barChart.setData(data);
            barChart.setVisibleXRange(1, 4);
            barChart.setFitBars(true);
            XAxis xAxis = barChart.getXAxis();
            xAxis.setGranularity(1f);
            xAxis.setGranularityEnabled(true);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(q));//setting String values in Xaxis
            for (IDataSet set : barChart.getData().getDataSets())
                set.setDrawValues(!set.isDrawValuesEnabled());

            barChart.invalidate();
        }
    }
}