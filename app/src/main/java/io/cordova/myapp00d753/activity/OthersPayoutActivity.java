package io.cordova.myapp00d753.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.OthersPayableAdapter;
import io.cordova.myapp00d753.adapter.SalaryAdapter;
import io.cordova.myapp00d753.module.OthersPayableModel;
import io.cordova.myapp00d753.module.SalaryModule;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

public class OthersPayoutActivity extends AppCompatActivity {
    private static final String TAG = "OthersPayoutActivity";
    RecyclerView rvItem;
    Spinner spYear;
    Button btnView;
    LinearLayout lnMain,lnNoData;
    int y;
    ArrayList<String>yearList=new ArrayList<>();
    ArrayList<OthersPayableModel>itemList=new ArrayList<>();
    Pref pref;
    String finYear;
    ImageView imgBack,imgHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_payout);
        initView();
        onclick();
    }

    private void initView(){
        pref=new Pref(OthersPayoutActivity.this);
        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(OthersPayoutActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);

        spYear = (Spinner) findViewById(R.id.spYear);
        y = Calendar.getInstance().get(Calendar.YEAR);

        yearList.add("2022-2023");
        yearList.add("2023-2024");
        yearList.add("2024-2025");
        yearList.add("2025-2026");
        yearList.add("2021-2022");

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (OthersPayoutActivity.this, android.R.layout.simple_spinner_item,
                        yearList); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spYear.setAdapter(spinnerArrayAdapter);

        lnMain=(LinearLayout) findViewById(R.id.lnMain);
        lnNoData=(LinearLayout) findViewById(R.id.lnNoData);
        btnView=(Button) findViewById(R.id.btnView);
        imgHome=(ImageView) findViewById(R.id.imgHome);
        imgBack=(ImageView) findViewById(R.id.imgBack);

    }

    private void onclick(){
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(OthersPayoutActivity.this,EmployeeDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                finYear=yearList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getOthersPayoutItem();

                JSONObject obj=new JSONObject();
                try {
                    obj.put("AEMEmployeeID", pref.getEmpId());
                    obj.put("FinancialYear", finYear);
                    obj.put("SecurityCode",pref.getSecurityCode());
                    getOthersPayoutItem(obj);
                    //"gcl_EmployeeOtherDisbursedPayout?AEMEmployeeID="+pref.getEmpId()+"&FinancialYear="+finYear+"&SecurityCode="+pref.getSecurityCode();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void getOthersPayoutItem(JSONObject jsonObject) {
        Log.e(TAG, "getOthersPayoutItem: "+jsonObject);
        ProgressDialog progressDialog=new ProgressDialog(OthersPayoutActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        lnMain.setVisibility(View.GONE);
        lnNoData.setVisibility(View.GONE);
        AndroidNetworking.post(AppData.GET_EMPLOYEE_DISBURSED_PAYOUT)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            Log.e(TAG, "DISBURSED_PAYOUT_LIST: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(Response_Data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String BillType = obj.optString("BillType");
                                    String Amount = obj.optString("Amount");
                                    String SalMonth = obj.optString("SalMonth");
                                    String SalYear = obj.optString("SalYear");
                                    OthersPayableModel othersPayableModel=new OthersPayableModel();
                                    othersPayableModel.setBilltype(BillType);
                                    othersPayableModel.setYear(SalYear);
                                    othersPayableModel.setMonth(SalMonth);
                                    othersPayableModel.setAmt(Amount);
                                    itemList.add(othersPayableModel);
                                }

                                if (itemList.size() > 0) {

                                    lnMain.setVisibility(View.VISIBLE);
                                    lnNoData.setVisibility(View.GONE);

                                    setAdapter();
                                } else {
                                    lnMain.setVisibility(View.GONE);
                                    lnNoData.setVisibility(View.VISIBLE);
                                }
                            } else {
                                lnMain.setVisibility(View.GONE);
                                lnNoData.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(OthersPayoutActivity.this, "Something want to wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Log.e(TAG, "DISBURSED_PAYOUT_LIST_error: "+anError.getErrorBody());
                        lnMain.setVisibility(View.GONE);
                        lnNoData.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void getOthersPayoutItem() {
        String surl = AppData.url+"gcl_EmployeeOtherDisbursedPayout?AEMEmployeeID="+pref.getEmpId()+"&FinancialYear="+finYear+"&SecurityCode="+pref.getSecurityCode();
        Log.d("salaryinput",surl);
        ProgressDialog progressDialog=new ProgressDialog(OthersPayoutActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        lnMain.setVisibility(View.GONE);
        lnNoData.setVisibility(View.GONE);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                progressDialog.dismiss();
                                //      Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();
                                itemList.clear();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String BillType = obj.optString("BillType");
                                    String Amount = obj.optString("Amount");
                                    String SalMonth = obj.optString("SalMonth");
                                    String SalYear = obj.optString("SalYear");
                                    OthersPayableModel othersPayableModel=new OthersPayableModel();
                                    othersPayableModel.setBilltype(BillType);
                                    othersPayableModel.setYear(SalYear);
                                    othersPayableModel.setMonth(SalMonth);
                                    othersPayableModel.setAmt(Amount);
                                    itemList.add(othersPayableModel);

                                }

                                if (itemList.size() > 0) {

                                    lnMain.setVisibility(View.VISIBLE);
                                    lnNoData.setVisibility(View.GONE);

                                    setAdapter();
                                } else {
                                    lnMain.setVisibility(View.GONE);
                                    lnNoData.setVisibility(View.VISIBLE);
                                }
                            } else {
                                lnMain.setVisibility(View.GONE);
                                lnNoData.setVisibility(View.VISIBLE);
                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();

                            //Toast.makeText(SalaryActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                lnMain.setVisibility(View.GONE);
                lnNoData.setVisibility(View.VISIBLE);

                // Toast.makeText(SalaryActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(OthersPayoutActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                200000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void setAdapter() {
        OthersPayableAdapter payableAdapter = new OthersPayableAdapter(itemList);
        rvItem.setAdapter(payableAdapter);
    }

}