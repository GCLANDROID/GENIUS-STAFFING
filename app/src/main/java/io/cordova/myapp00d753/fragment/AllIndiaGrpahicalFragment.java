package io.cordova.myapp00d753.fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.stream.Collectors;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.SupSaleDialog1Activity;
import io.cordova.myapp00d753.module.ChartModel;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

public class AllIndiaGrpahicalFragment extends Fragment {
    View view;
    CombinedChart comCYTargetChart, comCYPYTargetChart, comCYRevenueChart, comCYPYRevenueChart, comCYPYProductGrowthChart, comCYPYRevenueGrowthChart;

    ArrayList<Entry> cyTargetYvals = new ArrayList<>();
    ArrayList<BarEntry> cyTargetYYvals = new ArrayList<>();
    ArrayList cyTargetxvals = new ArrayList();

    ArrayList<Entry> cypyAchvYvals = new ArrayList<>();
    ArrayList<BarEntry> cypyAchvYYvals = new ArrayList<>();
    ArrayList cypyAchvxvals = new ArrayList();

    ArrayList<Entry> cyRevenueYvals = new ArrayList<>();
    ArrayList<BarEntry> cyRevenueYYvals = new ArrayList<>();
    ArrayList cyRevenuexvals = new ArrayList();

    ArrayList<Entry> cypyRevenueYvals = new ArrayList<>();
    ArrayList<BarEntry> cypyRevenueYYvals = new ArrayList<>();
    ArrayList cypyRevenuexvals = new ArrayList();


    ArrayList<Entry> cypyProductGrowthYvals = new ArrayList<>();
    ArrayList<BarEntry> cypyProductGrowthYYvals = new ArrayList<>();
    ArrayList cypyProductGrowthxvals = new ArrayList();

    ArrayList<Entry> cypyRevenueGrowthYvals = new ArrayList<>();
    ArrayList<BarEntry> cypyRevenueGrowthYYvals = new ArrayList<>();
    ArrayList cypyRevenueGrowthxvals = new ArrayList();

    Pref pref;
    String month, financialYear;
    int y;
    String year;
    ArrayList<ChartModel> itemList = new ArrayList<>();
    ArrayList<ChartModel> itemList1 = new ArrayList<>();
    ArrayList<ChartModel> itemList2 = new ArrayList<>();
    ArrayList<ChartModel> itemList3 = new ArrayList<>();
    ArrayList<ChartModel> itemList4 = new ArrayList<>();
    ArrayList<ChartModel> itemList5 = new ArrayList<>();
    LinearLayout ll1;
    FloatingActionButton fab;
    TextView tvMonth, tvToolBar;
    ProgressDialog progressBar;
    ImageView imgUpArrow, imgDownArrow;
    Float percen;
    ImageView imgBack, imgHome;
    LinearLayout llLoader;
    FrameLayout flMain;
    LinearLayout llUnit, llBranch;
    ImageView imgUnit, imgBranch;
    BarChart cytargetvsachvBar;
    ArrayList<String> targetachv = new ArrayList<>();
    TextView tvCYUnitTarget, tvCYUnitSold;
    BarChart cylyachvunits;
    ArrayList<String>cylyachv=new ArrayList<>();
    TextView tvCYAchv,tvLYAchv;
    BarChart cyrevtargetachv;
    TextView tvcyrevtarget,tvcyrevAchv;
    BarChart cylvRev;
    TextView tvcyrev,tvlyrev;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_allindia_graphical, container, false);
        initView();
        return view;
    }

    private void initView() {
        pref = new Pref(getContext());

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

        if (month.equals("January")) {
            int futureyear = y - 1;
            financialYear = futureyear + "-" + year;
        } else if (month.equals("February")) {
            int futureyear = y - 1;
            financialYear = futureyear + "-" + year;
        } else if (month.equals("March")) {
            int futureyear = y - 1;
            financialYear = futureyear + "-" + year;
        } else {
            int futureyear = y + 1;
            financialYear = year + "-" + futureyear;
        }


        imgBack = (ImageView) view.findViewById(R.id.imgBack);
        imgHome = (ImageView) view.findViewById(R.id.imgHome);

        llLoader = (LinearLayout) view.findViewById(R.id.llLoader);
        flMain = (FrameLayout) view.findViewById(R.id.flMain);


        cytargetvsachvBar = (BarChart) view.findViewById(R.id.cytargetvsachvBar);
        cytargetvsachvBar.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        cytargetvsachvBar.setMaxVisibleValueCount(4);
        cytargetvsachvBar.getXAxis().setDrawGridLines(false);
        // scaling can now only be done on x- and y-axis separately
        cytargetvsachvBar.setPinchZoom(false);

        cytargetvsachvBar.setDrawBarShadow(false);
        cytargetvsachvBar.setDrawGridBackground(false);

        XAxis xAxis = cytargetvsachvBar.getXAxis();
        xAxis.setDrawGridLines(false);

        cytargetvsachvBar.getAxisLeft().setDrawGridLines(false);
        cytargetvsachvBar.getAxisRight().setDrawGridLines(false);
        cytargetvsachvBar.getAxisRight().setEnabled(false);
        cytargetvsachvBar.getAxisLeft().setEnabled(true);
        cytargetvsachvBar.getXAxis().setDrawGridLines(false);
        // add a nice and smooth animation
        cytargetvsachvBar.animateY(1500);


        cytargetvsachvBar.getLegend().setEnabled(false);

        cytargetvsachvBar.getAxisRight().setDrawLabels(false);
        cytargetvsachvBar.getAxisLeft().setDrawLabels(true);
        cytargetvsachvBar.setTouchEnabled(false);
        cytargetvsachvBar.setDoubleTapToZoomEnabled(false);
        cytargetvsachvBar.getXAxis().setEnabled(true);
        cytargetvsachvBar.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        cytargetvsachvBar.invalidate();
        targetachv.add("Target");
        targetachv.add("Achievement");

        tvCYUnitSold = (TextView) view.findViewById(R.id.tvCYUnitSold);
        tvCYUnitTarget = (TextView) view.findViewById(R.id.tvCYUnitTarget);



        //2nd

        cylyachvunits = (BarChart) view.findViewById(R.id.cylyachvunits);
        cylyachvunits.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        cylyachvunits.setMaxVisibleValueCount(4);
        cylyachvunits.getXAxis().setDrawGridLines(false);
        // scaling can now only be done on x- and y-axis separately
        cylyachvunits.setPinchZoom(false);

        cylyachvunits.setDrawBarShadow(false);
        cylyachvunits.setDrawGridBackground(false);

        XAxis x2Axis = cytargetvsachvBar.getXAxis();
        x2Axis.setDrawGridLines(false);

        cylyachvunits.getAxisLeft().setDrawGridLines(false);
        cylyachvunits.getAxisRight().setDrawGridLines(false);
        cylyachvunits.getAxisRight().setEnabled(false);
        cylyachvunits.getAxisLeft().setEnabled(true);
        cylyachvunits.getXAxis().setDrawGridLines(false);
        // add a nice and smooth animation
        cylyachvunits.animateY(1500);


        cylyachvunits.getLegend().setEnabled(false);

        cylyachvunits.getAxisRight().setDrawLabels(false);
        cylyachvunits.getAxisLeft().setDrawLabels(true);
        cylyachvunits.setTouchEnabled(false);
        cylyachvunits.setDoubleTapToZoomEnabled(false);
        cylyachvunits.getXAxis().setEnabled(true);
        cylyachvunits.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        cylyachvunits.invalidate();
        cylyachv.add("CY");
        cylyachv.add("LY");

        tvCYAchv = (TextView) view.findViewById(R.id.tvCYAchv);
        tvLYAchv = (TextView) view.findViewById(R.id.tvLYAchv);

        //3rd

        cyrevtargetachv = (BarChart) view.findViewById(R.id.cyrevtargetachv);
        cyrevtargetachv.getDescription().setEnabled(false);


        cyrevtargetachv.setMaxVisibleValueCount(4);
        cyrevtargetachv.getXAxis().setDrawGridLines(false);
        // scaling can now only be done on x- and y-axis separately
        cyrevtargetachv.setPinchZoom(false);

        cyrevtargetachv.setDrawBarShadow(false);
        cyrevtargetachv.setDrawGridBackground(false);

        XAxis x3Axis = cyrevtargetachv.getXAxis();
        x3Axis.setDrawGridLines(false);

        cyrevtargetachv.getAxisLeft().setDrawGridLines(false);
        cyrevtargetachv.getAxisRight().setDrawGridLines(false);
        cyrevtargetachv.getAxisRight().setEnabled(false);
        cyrevtargetachv.getAxisLeft().setEnabled(true);
        cyrevtargetachv.getXAxis().setDrawGridLines(false);
        // add a nice and smooth animation
        cyrevtargetachv.animateY(1500);


        cyrevtargetachv.getLegend().setEnabled(false);

        cyrevtargetachv.getAxisRight().setDrawLabels(false);
        cyrevtargetachv.getAxisLeft().setDrawLabels(true);
        cyrevtargetachv.setTouchEnabled(false);
        cyrevtargetachv.setDoubleTapToZoomEnabled(false);
        cyrevtargetachv.getXAxis().setEnabled(true);
        cyrevtargetachv.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        cyrevtargetachv.invalidate();

        tvcyrevtarget = (TextView) view.findViewById(R.id.tvcyrevtarget);
        tvcyrevAchv = (TextView) view.findViewById(R.id.tvcyrevAchv);

        //4th

        cylvRev = (BarChart) view.findViewById(R.id.cylvRev);
        cylvRev.getDescription().setEnabled(false);


        cylvRev.setMaxVisibleValueCount(4);
        cylvRev.getXAxis().setDrawGridLines(false);
        // scaling can now only be done on x- and y-axis separately
        cylvRev.setPinchZoom(false);

        cylvRev.setDrawBarShadow(false);
        cylvRev.setDrawGridBackground(false);

        XAxis x4Axis = cylvRev.getXAxis();
        x4Axis.setDrawGridLines(false);

        cylvRev.getAxisLeft().setDrawGridLines(false);
        cylvRev.getAxisRight().setDrawGridLines(false);
        cylvRev.getAxisRight().setEnabled(false);
        cylvRev.getAxisLeft().setEnabled(true);
        cylvRev.getXAxis().setDrawGridLines(false);
        // add a nice and smooth animation
        cylvRev.animateY(1500);


        cylvRev.getLegend().setEnabled(false);

        cylvRev.getAxisRight().setDrawLabels(false);
        cylvRev.getAxisLeft().setDrawLabels(true);
        cylvRev.setTouchEnabled(false);
        cylvRev.setDoubleTapToZoomEnabled(false);
        cylvRev.getXAxis().setEnabled(true);
        cylvRev.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        cylvRev.invalidate();

        tvcyrev=(TextView)view.findViewById(R.id.tvcyrev);
        tvlyrev=(TextView)view.findViewById(R.id.tvlyrev);







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


                                JSONObject obj = responseData.getJSONObject(0);
                                float Sold = Float.parseFloat(obj.optString("Overall_Sold"));
                                float Target = Float.parseFloat(obj.optString("Overall_Target"));

                                itemList.add(new ChartModel(Target, 0, "Sold"));
                                itemList.add(new ChartModel(Sold, 1, "Target"));
                                String dispLaySold=obj.optString("Overall_Sold");
                                tvCYUnitSold.setText(dispLaySold+" Units ");
                                String displayTarget=obj.optString("Overall_Target");
                                tvCYUnitTarget.setText(displayTarget+" Units");


                                createBarChartForCYTargetAchv(itemList);


                                Float prev_sold=Float.parseFloat(obj.optString("Overall_Prev_Sold"));
                                String display_prevSold=obj.optString("Overall_Prev_Sold");

                                itemList1.add(new ChartModel(Sold,0,""));
                                itemList1.add(new ChartModel(prev_sold,1,""));

                                createBarChartForCYLYAchv(itemList1);
                                tvCYAchv.setText(dispLaySold+" Units ");
                                tvLYAchv.setText(display_prevSold+" Units ");

                                float Revenue_Sold = Float.parseFloat(obj.optString("Overall_Revenue_Sold"));
                                float Revenue_Target = Float.parseFloat(obj.optString("Overall_Revenue_Target"));
                                itemList2.add(new ChartModel(Revenue_Target,0,""));
                                itemList2.add(new ChartModel(Revenue_Sold,0,""));
                                 String displayRevSold=obj.optString("Overall_Revenue_Sold");
                                 String displayrevTarget=obj.optString("Overall_Revenue_Target");
                                createBarChartRevCYTArgetAchv(itemList2);
                                tvcyrevAchv.setText(displayRevSold+" Lacs");
                                tvcyrevtarget.setText(displayrevTarget+" Lacs");

                                float Overall_Prev_Revenue_Sold=Float.parseFloat(obj.optString("Overall_Prev_Revenue_Sold"));
                                String prevRevDiplay=obj.optString("Overall_Prev_Revenue_Sold");
                                itemList3.add(new ChartModel(Revenue_Sold,0,""));
                                itemList3.add(new ChartModel(Overall_Prev_Revenue_Sold,1,""));

                                createBarChartcylyRev(itemList3);

                                tvcyrev.setText(displayRevSold+" Lacs");
                                tvlyrev.setText(prevRevDiplay+" Lacs");









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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }



    private void createBarChartForCYTargetAchv(ArrayList<ChartModel> severityListServer) {
        ArrayList<BarEntry> values = new ArrayList<>();

        for (int i = 0; i < severityListServer.size(); i++) {
            ChartModel dataObject = severityListServer.get(i);
            values.add(new BarEntry(i, dataObject.getItem1()));
        }

        BarDataSet set1;

        if (cytargetvsachvBar.getData() != null &&
                cytargetvsachvBar.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) cytargetvsachvBar.getData().getDataSetByIndex(0);
            set1.setValues(values);
            cytargetvsachvBar.getData().notifyDataChanged();
            cytargetvsachvBar.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "Data Set");
            set1.setColors(ColorTemplate.MATERIAL_COLORS);
            set1.setDrawValues(true);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            cytargetvsachvBar.setData(data);
            cytargetvsachvBar.setVisibleXRange(1, 4);
            cytargetvsachvBar.setFitBars(true);
            XAxis xAxis = cytargetvsachvBar.getXAxis();
            xAxis.setGranularity(1f);
            xAxis.setGranularityEnabled(true);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(targetachv));//setting String values in Xaxis
            for (IDataSet set : cytargetvsachvBar.getData().getDataSets())
                set.setDrawValues(!set.isDrawValuesEnabled());

            cytargetvsachvBar.invalidate();
        }
    }

    private void createBarChartForCYLYAchv(ArrayList<ChartModel> severityListServer) {
        ArrayList<BarEntry> values = new ArrayList<>();

        for (int i = 0; i < severityListServer.size(); i++) {
            ChartModel dataObject = severityListServer.get(i);
            values.add(new BarEntry(i, dataObject.getItem1()));
        }

        BarDataSet set1;

        if (cylyachvunits.getData() != null &&
                cylyachvunits.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) cylyachvunits.getData().getDataSetByIndex(0);
            set1.setValues(values);
            cylyachvunits.getData().notifyDataChanged();
            cylyachvunits.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "Data Set");
            set1.setColors(ColorTemplate.MATERIAL_COLORS);
            set1.setDrawValues(true);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            cylyachvunits.setData(data);
            cylyachvunits.setVisibleXRange(1, 4);
            cylyachvunits.setFitBars(true);
            XAxis xAxis = cylyachvunits.getXAxis();
            xAxis.setGranularity(1f);
            xAxis.setGranularityEnabled(true);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(cylyachv));//setting String values in Xaxis
            for (IDataSet set : cylyachvunits.getData().getDataSets())
                set.setDrawValues(!set.isDrawValuesEnabled());

            cylyachvunits.invalidate();
        }
    }

    private void createBarChartRevCYTArgetAchv(ArrayList<ChartModel> severityListServer) {
        ArrayList<BarEntry> values = new ArrayList<>();

        for (int i = 0; i < severityListServer.size(); i++) {
            ChartModel dataObject = severityListServer.get(i);
            values.add(new BarEntry(i, dataObject.getItem1()));
        }

        BarDataSet set1;

        if (cyrevtargetachv.getData() != null &&
                cyrevtargetachv.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) cyrevtargetachv.getData().getDataSetByIndex(0);
            set1.setValues(values);
            cyrevtargetachv.getData().notifyDataChanged();
            cyrevtargetachv.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "Data Set");
            set1.setColors(ColorTemplate.MATERIAL_COLORS);
            set1.setDrawValues(true);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            cyrevtargetachv.setData(data);
            cyrevtargetachv.setVisibleXRange(1, 4);
            cyrevtargetachv.setFitBars(true);
            XAxis xAxis = cyrevtargetachv.getXAxis();
            xAxis.setGranularity(1f);
            xAxis.setGranularityEnabled(true);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(targetachv));//setting String values in Xaxis
            for (IDataSet set : cyrevtargetachv.getData().getDataSets())
                set.setDrawValues(!set.isDrawValuesEnabled());

            cyrevtargetachv.invalidate();
        }
    }

    private void createBarChartcylyRev(ArrayList<ChartModel> severityListServer) {
        ArrayList<BarEntry> values = new ArrayList<>();

        for (int i = 0; i < severityListServer.size(); i++) {
            ChartModel dataObject = severityListServer.get(i);
            values.add(new BarEntry(i, dataObject.getItem1()));
        }

        BarDataSet set1;

        if (cylvRev.getData() != null &&
                cylvRev.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) cylvRev.getData().getDataSetByIndex(0);
            set1.setValues(values);
            cylvRev.getData().notifyDataChanged();
            cylvRev.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "Data Set");
            set1.setColors(ColorTemplate.MATERIAL_COLORS);
            set1.setDrawValues(true);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            cylvRev.setData(data);
            cylvRev.setVisibleXRange(1, 4);
            cylvRev.setFitBars(true);
            XAxis xAxis = cylvRev.getXAxis();
            xAxis.setGranularity(1f);
            xAxis.setGranularityEnabled(true);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(cylyachv));//setting String values in Xaxis
            for (IDataSet set : cylvRev.getData().getDataSets())
                set.setDrawValues(!set.isDrawValuesEnabled());

            cylvRev.invalidate();
        }
    }

}