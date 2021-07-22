package io.cordova.myapp00d753.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.ConSaleApprovalAdapter;
import io.cordova.myapp00d753.adapter.TeamSaleAdapter;
import io.cordova.myapp00d753.module.ConSaleApproval;
import io.cordova.myapp00d753.module.TeamSaleModule;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

public class ConApprovalActivity extends AppCompatActivity {
    LinearLayout llZone, llBranch;
    ImageView imgZone, imgBranch;
    RecyclerView rvItem;
    ArrayList<ConSaleApproval> itemListForZone = new ArrayList<>();
    ArrayList<ConSaleApproval> itemListForBranch = new ArrayList<>();

    Pref pref;
    String month,financialYear;
    int y;
    String year;
    LinearLayout llViewAll;
    LinearLayout llPAN;
    ImageView imgPAN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_con_approval);
        initView();
        getItemListForPAN();
        onClick();
    }

    private void initView() {
        llZone = (LinearLayout) findViewById(R.id.llZone);
        llBranch = (LinearLayout) findViewById(R.id.llBranch);
        imgZone = (ImageView) findViewById(R.id.imgZone);
        imgBranch = (ImageView) findViewById(R.id.imgBranch);
        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        rvItem.setLayoutManager(new GridLayoutManager(this, 2));


        pref=new Pref(ConApprovalActivity.this);

        y = Calendar.getInstance().get(Calendar.YEAR);
        year = String.valueOf(y);
        Log.d("year", year);
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
        llViewAll=(LinearLayout)findViewById(R.id.llViewAll);
        llPAN=(LinearLayout)findViewById(R.id.llPAN);
        imgPAN=(ImageView)findViewById(R.id.imgPAN);
    }

    private void onClick() {
        llZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgZone.getVisibility() == View.GONE) {
                    imgZone.setVisibility(View.VISIBLE);
                    imgBranch.setVisibility(View.GONE);
                    imgPAN.setVisibility(View.GONE);
                    getItemListForZone();
                } else {
                    imgZone.setVisibility(View.GONE);
                    imgBranch.setVisibility(View.GONE);
                    imgPAN.setVisibility(View.GONE);
                }
            }
        });

        llBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgBranch.getVisibility() == View.GONE) {
                    imgZone.setVisibility(View.GONE);
                    imgBranch.setVisibility(View.VISIBLE);
                    imgPAN.setVisibility(View.GONE);
                    getItemListForBranch();
                } else {
                    imgZone.setVisibility(View.GONE);
                    imgBranch.setVisibility(View.GONE);
                    imgPAN.setVisibility(View.GONE);
                }
            }
        });
        llViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ConApprovalActivity.this, SaleApprovalActivity.class);
                intent.putExtra("zoneId","0");
                intent.putExtra("branchId","0");
                intent.putExtra("zoneName","All");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
       llPAN.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (imgPAN.getVisibility() == View.GONE) {
                   imgZone.setVisibility(View.GONE);
                   imgBranch.setVisibility(View.GONE);
                   imgPAN.setVisibility(View.VISIBLE);
                   getItemListForPAN();

               } else {
                   imgZone.setVisibility(View.GONE);
                   imgBranch.setVisibility(View.GONE);
                   imgPAN.setVisibility(View.GONE);
               }
           }
       });
    }

    private void getItemListForZone() {
        final ProgressDialog pd=new ProgressDialog(ConApprovalActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        String surl = AppData.url + "get_EmployeeSalesReport?ClientID="+pref.getEmpClintId()+"&UserID="+pref.getEmpId()+"&FinancialYear=0&Month=0&RType=Z&Operation=11&SecurityCode="+pref.getSecurityCode()+"&ZoneID=%&BranchID=%";
        Log.d("input", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        pd.dismiss();
                        // attendabceInfiList.clear();
                        itemListForZone.clear();
                        itemListForBranch.clear();

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
                                    String ZoneID = obj.optString("ZoneID");
                                    String Zonename = obj.optString("Zonename");
                                    String Total_Sales = obj.optString("Total_Sales");
                                    String Total_App_Sales = obj.optString("Total_App_Sales");
                                    String Total_Rej_Sales = obj.optString("Total_Rej_Sales");
                                    String Total_Pend_Sales = obj.optString("Total_Pend_Sales");
                                    String BranchID = "0";

                                    ConSaleApproval cModel=new ConSaleApproval(Zonename,Total_Sales,Total_Pend_Sales,Total_App_Sales,Total_Rej_Sales,BranchID,ZoneID);
                                    cModel.setBranchId(BranchID);
                                    itemListForZone.add(cModel);

                                }






                                setAdapter();











                            } else {

                                Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();


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
        RequestQueue requestQueue = Volley.newRequestQueue(ConApprovalActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void getItemListForPAN() {
        final ProgressDialog pd=new ProgressDialog(ConApprovalActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        String surl = AppData.url + "get_EmployeeSalesReport?ClientID="+pref.getEmpClintId()+"&UserID="+pref.getEmpId()+"&FinancialYear=0&Month=0&RType=N&Operation=11&SecurityCode="+pref.getSecurityCode()+"&ZoneID=%&BranchID=%";
        Log.d("input", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        pd.dismiss();
                        // attendabceInfiList.clear();
                        itemListForZone.clear();
                        itemListForBranch.clear();

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
                                    String ZoneID = "0";
                                    String Zonename = "All India";
                                    String Total_Sales = obj.optString("Total_Sales");
                                    String Total_App_Sales = obj.optString("Total_App_Sales");
                                    String Total_Rej_Sales = obj.optString("Total_Rej_Sales");
                                    String Total_Pend_Sales = obj.optString("Total_Pend_Sales");
                                    String BranchID = "0";

                                    ConSaleApproval cModel=new ConSaleApproval(Zonename,Total_Sales,Total_Pend_Sales,Total_App_Sales,Total_Rej_Sales,BranchID,ZoneID);
                                    cModel.setBranchId(BranchID);
                                    itemListForZone.add(cModel);

                                }






                                setAdapter();











                            } else {

                                Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();


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
        RequestQueue requestQueue = Volley.newRequestQueue(ConApprovalActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void getItemListForBranch() {
        final ProgressDialog pd=new ProgressDialog(ConApprovalActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        String surl = AppData.url + "get_EmployeeSalesReport?ClientID="+pref.getEmpClintId()+"&UserID="+pref.getEmpId()+"&FinancialYear=0&Month=0&RType=B&Operation=11&SecurityCode="+pref.getSecurityCode()+"&ZoneID=%&BranchID=%";
        Log.d("input", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        pd.dismiss();
                        // attendabceInfiList.clear();
                        itemListForZone.clear();
                        itemListForBranch.clear();

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
                                    //String BranchID = obj.optString("BranchID");
                                    String BranchName = obj.optString("BranchName");
                                    String ZoneID = obj.optString("ZoneID");
                                    String Zonename = obj.optString("Zonename");
                                    String Total_Sales = obj.optString("Total_Sales");
                                    String Total_App_Sales = obj.optString("Total_App_Sales");
                                    String Total_Rej_Sales = obj.optString("Total_Rej_Sales");
                                    String Total_Pend_Sales = obj.optString("Total_Pend_Sales");
                                    String BranchID = obj.optString("BranchID");

                                    ConSaleApproval cModel=new ConSaleApproval(BranchName+" - "+Zonename,Total_Sales,Total_Pend_Sales,Total_App_Sales,Total_Rej_Sales,BranchID,ZoneID);
                                    cModel.setBranchId(BranchID);
                                    itemListForZone.add(cModel);

                                }






                                setAdapter();











                            } else {

                                Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();

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
        RequestQueue requestQueue = Volley.newRequestQueue(ConApprovalActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void setAdapter(){
        ConSaleApprovalAdapter saleAdapter=new ConSaleApprovalAdapter(itemListForZone,ConApprovalActivity.this);
        rvItem.setAdapter(saleAdapter);
    }
}