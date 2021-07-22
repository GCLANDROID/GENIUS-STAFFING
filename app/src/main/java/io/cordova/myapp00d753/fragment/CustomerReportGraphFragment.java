package io.cordova.myapp00d753.fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.CustomerReportActivity;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;


public class CustomerReportGraphFragment extends Fragment {

    int y;
    float cusGrowth,revGrowth,soldGrowth;

    ArrayList<BarEntry> cylyCusYYvals=new ArrayList<>();
    ArrayList<BarEntry>cylyRevYYvals=new ArrayList<>();
    ArrayList<BarEntry>cylySoldYYvals=new ArrayList<>();
    ArrayList cyCusxvals = new ArrayList();



    CombinedChart comCYPYCus,comCYPYRev,comCYPYSold;

    Pref prefManager;


    String month,financialYear,year;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_customer_report_graph, container, false);
        initialize();
        return view;
    }

    private void initialize(){
        prefManager=new Pref(getContext());

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
        targetGet();









    }

    public void targetGet() {
        final ProgressDialog pd=new ProgressDialog(getContext());
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

                                String GrowthPercentage_Revenu=obj.optString("GrowthPercentage_Revenu");
                                revGrowth= Float.parseFloat(GrowthPercentage_Revenu);


                                String GrowthPercentage_Sold=obj.optString("GrowthPercentage_Sold");
                                soldGrowth= Float.parseFloat(GrowthPercentage_Sold);


                                //CYLYCUS
                                Float CY_Customer= Float.valueOf(obj.optString("CY_Customer"));
                                Float LY_Customer= Float.valueOf(obj.optString("LY_Customer"));
                                cylyCusYYvals.add(new BarEntry(0,LY_Customer));
                                cylyCusYYvals.add(new BarEntry(1,CY_Customer));
                                cylyCusYYvals.add(new BarEntry(2,cusGrowth));
                                cyCusxvals.add("LY");
                                cyCusxvals.add("CY");
                                cyCusxvals.add("Growth (%)");


                                comCYPYCus=view.findViewById(R.id.comCYPYCus);
                                comCYPYCus.setTouchEnabled(false);
                                comCYPYCus.setDrawValueAboveBar(true);


                                comCYPYCus.getDescription().setText("");
                                comCYPYCus.setDrawGridBackground(false);
                                comCYPYCus.setDrawBarShadow(false);
                                comCYPYCus.setHighlightFullBarEnabled(false);
                                comCYPYCus.setDrawOrder(new CombinedChart.DrawOrder[]{
                                        CombinedChart.DrawOrder.BAR,  CombinedChart.DrawOrder.LINE
                                });
                                comCYPYCus.animateX(1000);


                                Legend l = comCYPYCus.getLegend();
                                l.setWordWrapEnabled(true);
                                l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                                l.setOrientation(Legend.LegendOrientation.HORIZONTAL);

                                YAxis rightAxis = comCYPYCus.getAxisRight();
                                rightAxis.setEnabled(false);
                                rightAxis.setDrawGridLines(false);
                                rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

                                YAxis leftAxis = comCYPYCus.getAxisLeft();
                                leftAxis.setDrawGridLines(false);
                                leftAxis.setAxisMinimum(0f);

                                XAxis xAxis = comCYPYCus.getXAxis();
                                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                                xAxis.setAxisMinimum(0f);
                                xAxis.setGranularity(1f);
                                xAxis.setValueFormatter(new IndexAxisValueFormatter(cyCusxvals));



                                CombinedData data = new CombinedData();

                                data.setData( generateBarDataForCYLYCus());

                                comCYPYCus.setData(data);
                                comCYPYCus.invalidate();




                                //CYLYRev

                                Float CY_Customer_Revenu= Float.valueOf(obj.optString("CY_Customer_Revenu"));
                                Float LY_Customer_Revenu= Float.valueOf(obj.optString("LY_Customer_Revenu"));


                                cylyRevYYvals.add(new BarEntry(0,LY_Customer_Revenu));
                                cylyRevYYvals.add(new BarEntry(1,CY_Customer_Revenu));
                                cylyRevYYvals.add(new BarEntry(2,revGrowth));


                                comCYPYRev=view.findViewById(R.id.comCYPYRev);
                                comCYPYRev.setTouchEnabled(false);
                                comCYPYRev.setDrawValueAboveBar(true);


                                comCYPYRev.getDescription().setText("");
                                comCYPYRev.setDrawGridBackground(false);
                                comCYPYRev.setDrawBarShadow(false);
                                comCYPYRev.setHighlightFullBarEnabled(false);
                                comCYPYRev.setDrawOrder(new CombinedChart.DrawOrder[]{
                                        CombinedChart.DrawOrder.BAR,  CombinedChart.DrawOrder.LINE
                                });
                                comCYPYRev.animateX(1000);


                                Legend l1 = comCYPYRev.getLegend();
                                l1.setWordWrapEnabled(true);
                                l1.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                                l1.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                                l1.setOrientation(Legend.LegendOrientation.HORIZONTAL);

                                YAxis rightAxis1 = comCYPYRev.getAxisRight();
                                rightAxis1.setEnabled(false);
                                rightAxis1.setDrawGridLines(false);
                                rightAxis1.setAxisMinimum(0f); // this replaces setStartAtZero(true)

                                YAxis leftAxis1 = comCYPYRev.getAxisLeft();
                                leftAxis1.setDrawGridLines(false);
                                leftAxis1.setAxisMinimum(0f);

                                XAxis xAxis1 = comCYPYRev.getXAxis();
                                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                                xAxis1.setAxisMinimum(0f);
                                xAxis1.setGranularity(1f);
                                xAxis1.setValueFormatter(new IndexAxisValueFormatter(cyCusxvals));



                                CombinedData data1 = new CombinedData();

                                data1.setData( generateBarDataForCYLYRev());

                                comCYPYRev.setData(data1);
                                comCYPYRev.invalidate();





                                //CYLYSold

                                Float CY_Customer_Sold= Float.valueOf(obj.optString("CY_Customer_Sold"));
                                Float LY_Customer_Sold= Float.valueOf(obj.optString("LY_Customer_Sold"));

                                cylySoldYYvals.add(new BarEntry(0,LY_Customer_Sold));
                                cylySoldYYvals.add(new BarEntry(1,CY_Customer_Sold));
                                cylySoldYYvals.add(new BarEntry(2,soldGrowth));


                                comCYPYSold=view.findViewById(R.id.comCYPYSold);
                                comCYPYSold.setTouchEnabled(false);
                                comCYPYSold.setDrawValueAboveBar(true);


                                comCYPYSold.getDescription().setText("");
                                comCYPYSold.setDrawGridBackground(false);
                                comCYPYSold.setDrawBarShadow(false);
                                comCYPYSold.setHighlightFullBarEnabled(false);
                                comCYPYSold.setDrawOrder(new CombinedChart.DrawOrder[]{
                                        CombinedChart.DrawOrder.BAR,  CombinedChart.DrawOrder.LINE
                                });
                                comCYPYSold.animateX(1000);


                                Legend l11 = comCYPYSold.getLegend();
                                l11.setWordWrapEnabled(true);
                                l11.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                                l11.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                                l11.setOrientation(Legend.LegendOrientation.HORIZONTAL);

                                YAxis rightAxis11 = comCYPYSold.getAxisRight();
                                rightAxis11.setEnabled(false);
                                rightAxis11.setDrawGridLines(false);
                                rightAxis11.setAxisMinimum(0f); // this replaces setStartAtZero(true)

                                YAxis leftAxis11 = comCYPYSold.getAxisLeft();
                                leftAxis11.setDrawGridLines(false);
                                leftAxis11.setAxisMinimum(0f);

                                XAxis xAxis11 = comCYPYSold.getXAxis();
                                //xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                                xAxis11.setAxisMinimum(0f);
                                xAxis11.setGranularity(1f);
                                xAxis11.setValueFormatter(new IndexAxisValueFormatter(cyCusxvals));



                                CombinedData data11 = new CombinedData();

                                data11.setData( generateBarDataForCYLYSold());

                                comCYPYSold.setData(data11);
                                comCYPYSold.invalidate();




                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }
    private BarData generateBarDataForCYLYCus() {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        entries = cylyCusYYvals;

        BarDataSet set1 = new BarDataSet(entries, "");
        //set1.setColor(Color.rgb(60, 220, 78));
        set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
        set1.setValueTextColor(Color.BLACK);
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        float barWidth = 0.45f; // x2 dataset

        BarData d = new BarData(set1);
        d.setBarWidth(barWidth);

        return d;
    }

    private BarData generateBarDataForCYLYRev() {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        entries = cylyRevYYvals;

        BarDataSet set1 = new BarDataSet(entries, "");
        //set1.setColor(Color.rgb(60, 220, 78));
        set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
        set1.setValueTextColor(Color.BLACK);
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        float barWidth = 0.45f; // x2 dataset

        BarData d = new BarData(set1);
        d.setBarWidth(barWidth);

        return d;
    }

    private BarData generateBarDataForCYLYSold() {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        entries = cylySoldYYvals;

        BarDataSet set1 = new BarDataSet(entries, "");
        //set1.setColor(Color.rgb(60, 220, 78));
        set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
        set1.setValueTextColor(Color.BLACK);
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        float barWidth = 0.45f; // x2 dataset

        BarData d = new BarData(set1);
        d.setBarWidth(barWidth);

        return d;
    }
}