package io.cordova.myapp00d753.fragment;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
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
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.ChartModel;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

/**
 * A simple {@link Fragment} subclass.
 */
public class GraphicalWTDSaleFragment extends Fragment {


    View view;
    ArrayList<ChartModel> itemList = new ArrayList<>();
    ArrayList<ChartModel> itemList1 = new ArrayList<>();
    BarChart barTargetchart;
    ArrayList NoOfEmp = new ArrayList();
    ArrayList year = new ArrayList();


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
    TextView tvText;
    LinearLayout llCYLY;
    TextView tvCYTarget,tvCYAchieved;
    ArrayList<Integer>cd=new ArrayList<>();
    ArrayList<Integer>dd=new ArrayList<>();
    Integer targetSum,soldSum;
    BarChart barChart;

    ArrayList<BarEntry> target = new ArrayList<>();
    ArrayList<BarEntry> sold = new ArrayList<>();

    float groupSpace = 0.08f;
    float barSpace = 0.03f; // x4 DataSet
    float barWidth = 0.2f;
    BarDataSet set1, set2;

    int groupCount = 6;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_graphical_sale_wtd, container, false);
        initView();

        return view;
    }



    private void initView() {

        pref=new Pref(getContext());

        barTargetchart = view.findViewById(R.id.barTargetchart);
        tvMonth=(TextView)view.findViewById(R.id.tvMonth);




        financialYear = pref.getFinacialYear();

        tvMonth=(TextView)view.findViewById(R.id.tvMonth);
        tvMonth.setText("Showing Quaterly Report: ");

        tvCYAchieved=(TextView)view.findViewById(R.id.tvCYAchieved);
        tvCYTarget=(TextView)view.findViewById(R.id.tvCYTarget);



        getItemList();









    }

    private void getItemList() {
        Log.d("Arpan", "arpan");
        final ProgressDialog progressBar = new ProgressDialog(getContext());
        progressBar.setMessage("Loading..");
        progressBar.setCancelable(false);
        progressBar.show();
        String surl = AppData.url + "get_EmployeeSalesReport?ClientID="+pref.getEmpClintId()+"&UserID="+pref.getEmpId()+"&FinancialYear="+financialYear+"&Month="+pref.getMonth()+"&RType=WTD&Operation=2&SecurityCode="+pref.getSecurityCode();
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
                                    String QuaterName = obj.optString("WeekName");
                                    ChartModel cModel=new ChartModel(Sold,i,QuaterName);
                                    cModel.setItem4(Target);
                                    itemList.add(cModel);
                                    int iSold=Integer.parseInt(obj.optString("Sold"));
                                    int iTarget=Integer.parseInt(obj.optString("Target"));

                                    cd.add(iSold);
                                    dd.add(iTarget);

                                }







                                for (int i = 0; i < itemList.size(); i++) {

                                    yVals.add(new Entry(itemList.get(i).getItem2(),itemList.get(i).getItem1()));
                                    yyVals.add(new BarEntry(itemList.get(i).getItem2(),itemList.get(i).getItem4()));
                                    xVals.add(itemList.get(i).getItem3());
                                    /*target.add(new BarEntry(itemList.get(i).getItem2(),itemList.get(i).getItem4()));
                                    sold.add(new BarEntry(itemList.get(i).getItem2(),itemList.get(i).getItem1()));
*/


                                }









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

                                targetSum=dd.get(0)+dd.get(1)+dd.get(2)+dd.get(3)+dd.get(4);
                                tvCYTarget.setText(String.valueOf(targetSum)+" Units");
                                soldSum=cd.get(0)+cd.get(1)+cd.get(2)+cd.get(3)+cd.get(4);
                                tvCYAchieved.setText(String.valueOf(soldSum)+" Units");





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

        LineDataSet set = new LineDataSet(entries, "Weekly Achievement (Line chart)");
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

        BarDataSet set1 = new BarDataSet(entries, "Weekly Target (Bar Chart)");
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

        LineDataSet set = new LineDataSet(entries, "Weekly Achievement (Line Chart)");
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

        BarDataSet set1 = new BarDataSet(entries, "Weekly Achievement (%) (Bar Chart)");
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



    private void createBarChart() {



        }
    }







