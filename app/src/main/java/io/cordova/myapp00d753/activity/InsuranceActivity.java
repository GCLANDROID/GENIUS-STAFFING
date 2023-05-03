package io.cordova.myapp00d753.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
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
import io.cordova.myapp00d753.adapter.BackLogAdapter;
import io.cordova.myapp00d753.adapter.InsuranceAdapter;
import io.cordova.myapp00d753.module.BackLogAttendanceModel;
import io.cordova.myapp00d753.module.InsuranceModel;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

public class InsuranceActivity extends AppCompatActivity {
    RecyclerView  rvItem;
    ArrayList<InsuranceModel>itemList=new ArrayList<>();
    Pref pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance);
        initView();
    }

    private void initView(){
        pref=new Pref(InsuranceActivity.this);
        rvItem=(RecyclerView) findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(InsuranceActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);

        getInsuranceData();
    }


    private void getInsuranceData() {
        String surl = AppData.url + "gcl_EmployeeInsuranceReport?MasterID="+pref.getMasterId()+"&SecurityCode="+pref.getSecurityCode() ;
        Log.d("backlogURL",surl);
        ProgressDialog progressDialog=new ProgressDialog(InsuranceActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("blockActivityData", response);
                        progressDialog.dismiss();
                        itemList.clear();

                        try {
                            JSONArray job1 = new JSONArray(response);
                            Log.e("response12", "@@@@@@" + job1);



                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();


                                for (int i = 0; i < job1.length(); i++) {
                                    JSONObject obj = job1.getJSONObject(i);
                                    String Policy_Type = obj.optString("Policy_Type");
                                    String Policy_No = obj.optString("Policy_No");
                                    String UHID = obj.optString("UHID");
                                    String Password=obj.optString("Password");
                                    String Policy_URL=obj.optString("Policy_URL");
                                    String Doc_URL=obj.optString("Doc_URL");

                                    InsuranceModel insuranceModel = new InsuranceModel();
                                    insuranceModel.setPolicyName(Policy_Type);
                                    insuranceModel.setPolicyNumber(Policy_No);
                                    insuranceModel.setUHID(UHID);
                                    insuranceModel.setPassword(Password);
                                    insuranceModel.setCompanyURL(Policy_URL);
                                    insuranceModel.setTrainingURL(Doc_URL);
                                    itemList.add(insuranceModel);


                                }

                                InsuranceAdapter insuranceAdapter=new InsuranceAdapter(itemList,InsuranceActivity.this);
                                rvItem.setAdapter(insuranceAdapter);





                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            //  Toast.makeText(AboutUsActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               progressDialog.dismiss();
                Toast.makeText(InsuranceActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(InsuranceActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }
}