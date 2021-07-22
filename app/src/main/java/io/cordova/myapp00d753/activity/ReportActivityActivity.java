package io.cordova.myapp00d753.activity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import io.cordova.myapp00d753.adapter.FenceReportAdapter;
import io.cordova.myapp00d753.module.ReportModel;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

public class ReportActivityActivity extends AppCompatActivity {
    RecyclerView rvReport;
    LinearLayout llLoader, llMain;
    ArrayList<ReportModel> itemList = new ArrayList<>();
    ImageView imgBack;
    String fentype;
    Pref pref;
    ImageView imgHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_activity);
        initialize();
        getItem();
    }

    private void initialize() {
        rvReport = (RecyclerView) findViewById(R.id.rvReport);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(ReportActivityActivity.this, LinearLayoutManager.VERTICAL, false);
        rvReport.setLayoutManager(layoutManager);
        llMain = (LinearLayout) findViewById(R.id.llMain);
        llLoader = (LinearLayout) findViewById(R.id.llLoader);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        pref=new Pref(ReportActivityActivity.this);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),EmployeeDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


    private void getItem() {
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        String surl = AppData.url+"get_EmployeewiseGeofence?EmployeeId="+pref.getEmpId()+"&Longitude=0&Latitude=0&Address=0&FenceType=0&Createdon=0&Operation=1&SecurityCode="+pref.getSecurityCode();
        Log.d("inputReport", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);

                        itemList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                //          Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String Address = obj.optString("Address");
                                    String FenceType = obj.optString("FenceType");
                                    String LoginTime = obj.optString("LoginTime");
                                    String LoginDate = obj.optString("LoginDate");

                                    ReportModel obj2 = new ReportModel(Address,FenceType,LoginTime,LoginDate);
                                    itemList.add(obj2);


                                }

                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);

                                setAdapter();
                                /*llNodata.setVisibility(View.GONE);
                                llAgain.setVisibility(View.GONE);*/

                            } else {
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);

                                Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ReportActivityActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.GONE);
                llMain.setVisibility(View.GONE);

                //Toast.makeText(SupAttenReportActivity.this, "volly slider2img"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(ReportActivityActivity.this);
        requestQueue.add(stringRequest);
    }

    private void setAdapter() {
        FenceReportAdapter aAdapter = new FenceReportAdapter(itemList);
        rvReport.setAdapter(aAdapter);

    }


}
