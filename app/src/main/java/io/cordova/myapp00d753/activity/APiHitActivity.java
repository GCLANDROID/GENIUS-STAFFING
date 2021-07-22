package io.cordova.myapp00d753.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

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
import io.cordova.myapp00d753.module.VisitingLocationModel;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

public class APiHitActivity extends AppCompatActivity {
    Pref pref;
    ArrayList<VisitingLocationModel>addressList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_hit);
        pref=new Pref(getApplicationContext());
        String formattedDate = getIntent().getStringExtra("attdate").replaceAll("\\s+", "-");
        loadNames(formattedDate);
    }


    private void loadNames(String date) {
        //names.clear();
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        String surl = AppData.url+"get_OfflineDailyLogActivity?AEMEmployeeID=" + pref.getEmpId() + "&Year=0&Month=0&SecurityCode=" + pref.getSecurityCode()+"&AttendanceDate="+date+"&Operation=1";
        Log.d("mapactivity", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);

                        // attendabceInfiList.clear();
                        pd.dismiss();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i <responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);

                                    String PunchInTime = obj.optString("PunchInTime");
                                    String AddressIN = obj.optString("AddressIN");
                                    String LongitudeIN=obj.optString("LongitudeIN");
                                    String LatitudeIN=obj.optString("LatitudeIN");

                                    VisitingLocationModel obj2 = new VisitingLocationModel(AddressIN,PunchInTime,LatitudeIN,LongitudeIN);
                                    addressList.add(obj2);


                                }
                                ArrayList<String>myList=new ArrayList<>();
                                Intent intent=new Intent(APiHitActivity.this,MapReportActivity.class);
                                intent.putExtra("myList",addressList);
                                startActivity(intent);
                                finish();


                            } else {

                                pd.show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.show();


                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(APiHitActivity.this);
        requestQueue.add(stringRequest);


    }
}
