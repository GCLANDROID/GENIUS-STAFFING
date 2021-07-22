package io.cordova.myapp00d753.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;


public class CustomerReportDataFragment extends Fragment {

    View view;
    TextView tvCYCus,tvLYCus,tvCYCusRev,tvLYCusRev,tvCYCusSold,tvLYCusSold;
    Pref prefManager;

    String month,financialYear,year;
    int y;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_customer_report_data, container, false);
        initView();
        return view;
    }

    private void initView(){
        tvCYCus=(TextView)view.findViewById(R.id.tvCYCus);
        tvLYCus=(TextView)view.findViewById(R.id.tvLYCus);
        tvCYCusRev=(TextView)view.findViewById(R.id.tvCYCusRev);
        tvLYCusRev=(TextView)view.findViewById(R.id.tvLYCusRev);
        tvCYCusSold=(TextView)view.findViewById(R.id.tvCYCusSold);
        tvLYCusSold=(TextView)view.findViewById(R.id.tvLYCusSold);

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

                                //CYLYCUS
                                String CY_Customer= obj.optString("CY_Customer");
                                String LY_Customer= obj.optString("LY_Customer");
                                tvCYCus.setText("Customer: "+CY_Customer);
                                tvLYCus.setText("Customer: "+LY_Customer);


                                //CYLYRev

                                String CY_Customer_Revenu= obj.optString("CY_Customer_Revenu");
                                String LY_Customer_Revenu= obj.optString("LY_Customer_Revenu");
                                tvCYCusRev.setText("Revenue: "+CY_Customer_Revenu+" Lacs");
                                tvLYCusRev.setText("Revenue: "+LY_Customer_Revenu+" Lacs");




                                String CY_Customer_Sold= obj.optString("CY_Customer_Sold");
                                String LY_Customer_Sold= obj.optString("LY_Customer_Sold");
                                tvCYCusSold.setText("Products Sale: "+CY_Customer_Sold+" Units");
                                tvLYCusSold.setText("Products Sale: "+LY_Customer_Sold+" Units");



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
}