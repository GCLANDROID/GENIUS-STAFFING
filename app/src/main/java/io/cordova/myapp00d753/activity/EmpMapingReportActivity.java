package io.cordova.myapp00d753.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.EmpMapingReportAdapter;
import io.cordova.myapp00d753.module.EmpMapiingReportModel;
import io.cordova.myapp00d753.module.SpinnerModel;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

public class EmpMapingReportActivity extends AppCompatActivity {
    Spinner spLocation;
    LinearLayout llMain, llRV, llLoader, llNoData;
    RecyclerView rvItem;
    ImageView imgBack, imgHome;
    ArrayList<SpinnerModel> mLocationList = new ArrayList<>();
    ArrayList<String> locationList = new ArrayList<>();
    Pref pref;
    ArrayList<EmpMapiingReportModel> empList = new ArrayList<>();
    LinearLayout llNoData1;
    String locationId = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_maping_report);

        initView();
        locationGet();
        onClick();
    }

    private void initView() {
        pref = new Pref(getApplicationContext());
        spLocation = (Spinner) findViewById(R.id.spLocation);
        llMain = (LinearLayout) findViewById(R.id.llMain);
        llRV = (LinearLayout) findViewById(R.id.llRV);
        llNoData = (LinearLayout) findViewById(R.id.llNoData);
        llLoader = (LinearLayout) findViewById(R.id.llLoader);
        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(EmpMapingReportActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);

        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        llNoData1 = (LinearLayout) findViewById(R.id.llNoData1);
    }

    private void locationGet() {
        String surl = AppData.url+"get_GeofenceConfiguration?SLongitude=0&SLatitude=0&SAddress=0&ELongitude=0&ELatitude=0&EAddress=0&EndPoint=0&LocationName=0&Operation=1&SecurityCode=" + pref.getSecurityCode();
        Log.d("configurl", surl);
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseconfig", response);

                        locationList.add("Please select Location");
                        mLocationList.add(new SpinnerModel("0", "0"));
                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("responseconfig", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {

                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String LocationName = obj.optString("LocationName");
                                    String GeoFenceId = obj.optString("GeoFenceId");
                                    SpinnerModel spModel = new SpinnerModel(LocationName, GeoFenceId);
                                    locationList.add(LocationName);
                                    mLocationList.add(spModel);

                                }

                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (EmpMapingReportActivity.this, android.R.layout.simple_spinner_item,
                                                locationList); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spLocation.setAdapter(spinnerArrayAdapter);
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNoData.setVisibility(View.GONE);



                            } else {
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNoData.setVisibility(View.VISIBLE);
                            }


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            //  Toast.makeText(EmployeeDashBoardActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Toast.makeText(EmployeeDashBoardActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());

            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(EmpMapingReportActivity.this);
        requestQueue.add(stringRequest);


    }

    private void onClick() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EmployeeDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        spLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    locationId = mLocationList.get(position).getItemId();
                    getEmpList();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getEmpList() {
        String surl = AppData.url+
                "get_EmployeeGeofenceConfigure?AEMConsultantID="+pref.getEmpConId()+"&AEMClientID="+pref.getEmpClintId()+"&AEMConsultantOffID=ACONOF0910000018&EmployeeId=0&GeoFenceId=" + locationId + "&Operation=1&SecurityCode=" + pref.getSecurityCode();
        Log.d("configurl", surl);
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading..");
        llRV.setVisibility(View.VISIBLE);
        llNoData1.setVisibility(View.GONE);
        pd.setCancelable(false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseconfig", response);
                        empList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("responseconfig", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {

                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String EmpName = obj.optString("Name");
                                    String LocationName=obj.optString("LocationName");

                                    EmpMapiingReportModel mModel = new EmpMapiingReportModel(EmpName,LocationName);
                                    empList.add(mModel);

                                }

                                pd.dismiss();
                                EmpMapingReportAdapter cAdapter = new EmpMapingReportAdapter(empList);
                                rvItem.setAdapter(cAdapter);


                            } else {
                                pd.dismiss();
                                llRV.setVisibility(View.GONE);
                                llNoData1.setVisibility(View.VISIBLE);
                            }


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            //  Toast.makeText(EmployeeDashBoardActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Toast.makeText(EmployeeDashBoardActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());

            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(EmpMapingReportActivity.this);
        requestQueue.add(stringRequest);


    }
}
