package io.cordova.myapp00d753.activity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.NumberTourAdapter;
import io.cordova.myapp00d753.module.NumberTourModel;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

public class NumberTourActivity extends AppCompatActivity {
    RecyclerView rvItem;
    LinearLayout llLoader,llMain,llNodata;
    ArrayList<NumberTourModel>itemList=new ArrayList<>();
    Pref pref;
    ImageView imgBack,imgHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numer_tour);
        initView();
        getItem();
        onClick();
    }
    private void initView(){
        pref=new Pref(getApplicationContext());
        rvItem=(RecyclerView)findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(NumberTourActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);
        llNodata=(LinearLayout)findViewById(R.id.llNodata);
        llMain=(LinearLayout)findViewById(R.id.llMain);
        llLoader=(LinearLayout)findViewById(R.id.llLoader);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
    }

   private void onClick(){
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),EmployeeDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
   }

    private void getItem(){
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);
        String surl = AppData.url+"get_OfflineDailyLogActivity?AEMEmployeeID=" + pref.getEmpId() + "&Year=0&Month=0&SecurityCode=" + pref.getSecurityCode()+"&AttendanceDate=0&Operation=2";
        Log.d("inputactivity", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);

                        // attendabceInfiList.clear();

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

                                    String PunchIn = obj.optString("PunchIn");
                                    String TotalActivity = obj.optString("TotalActivity");
                                    String Min_LatitudeIN=obj.optString("Min_LatitudeIN");
                                    String Min_LongitudeIN=obj.optString("Min_LongitudeIN");
                                    String Max_LatitudeOUT=obj.optString("Max_LatitudeOUT");
                                    String Max_LongitudeOUT=obj.optString("Max_LongitudeOUT");

                                    NumberTourModel obj2 = new NumberTourModel(PunchIn,TotalActivity,Min_LatitudeIN,Min_LongitudeIN,Max_LatitudeOUT,Max_LongitudeOUT);
                                    itemList.add(obj2);


                                }
                                setAdapter();
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.GONE);

                            } else {

                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNodata.setVisibility(View.VISIBLE);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                llNodata.setVisibility(View.GONE);


                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(NumberTourActivity.this);
        requestQueue.add(stringRequest);
    }
    private void setAdapter(){
        NumberTourAdapter vAdapter=new NumberTourAdapter(itemList,NumberTourActivity.this);
        rvItem.setAdapter(vAdapter);
    }

    public  double distance(int i){
        LatLng src=new LatLng(Double.parseDouble(itemList.get(i).getMinlat()),Double.parseDouble(itemList.get(i).getMinlong()));
        LatLng des=new LatLng(Double.parseDouble(itemList.get(i).getMaxlat()),Double.parseDouble(itemList.get(i).getMaxlong()));
        double distance=CalculationByDistance(src,des);


        return  distance;

    }

    public  static double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        //  Toast.makeText(getApplicationContext(),"Radious Value:  "+valueResult+"  KM: "+kmInDec+" Meter: "+meterInDec,Toast.LENGTH_LONG).show();
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);
        return Radius * c;
    }






}
