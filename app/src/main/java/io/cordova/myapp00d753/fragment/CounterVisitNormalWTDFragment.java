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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.CounterMTDAdapter;
import io.cordova.myapp00d753.adapter.CounterWTDAdapter;
import io.cordova.myapp00d753.module.CounterVisitModel;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;


public class CounterVisitNormalWTDFragment extends Fragment {

   View view;
   RecyclerView rvItem;
   Pref pref;
   String financialYear,month;
   ArrayList<CounterVisitModel>itemList=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_counter_visit_normal_y_t_d, container, false);
        initView();
        getItemList();
        return view;
    }

    private void initView() {
        pref = new Pref(getContext());
        rvItem = (RecyclerView) view.findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);


        int m = Calendar.getInstance().get(Calendar.MONTH) + 1;
        month = pref.getMonth();
        financialYear = pref.getFinacialYear();






    }

    private void onClick() {

    }

    private void getItemList() {
        Log.d("Arpan", "arpan");
        final ProgressDialog progressBar = new ProgressDialog(getContext());
        progressBar.setMessage("Loading..");
        progressBar.setCancelable(false);
        progressBar.show();
        String surl = AppData.url + "get_EmployeeVisitActivity?ClientID=" + pref.getEmpClintId() + "&UserID=" + pref.getEmpId() + "&FinancialYear=" + financialYear + "&Month="+pref.getMonth()+"&RType=WTD&Operation=2&SecurityCode=" + pref.getSecurityCode();
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
                                for (int i = 0; i < responseData.length(); i++) {

                                    JSONObject obj = responseData.getJSONObject(i);
                                    String FinancialYear = obj.optString("WeekName");
                                    String TotalVisit = obj.optString("TotalVisit");
                                    String Month=obj.optString("Month");

                                    CounterVisitModel cModel = new CounterVisitModel(FinancialYear, TotalVisit);
                                    cModel.setMonth(Month);
                                    itemList.add(cModel);

                                }
                                setAdapter();


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

    private void setAdapter() {
        CounterWTDAdapter saleAdapter = new CounterWTDAdapter(itemList);
        rvItem.setAdapter(saleAdapter);
    }


}