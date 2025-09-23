package io.cordova.myapp00d753.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
import io.cordova.myapp00d753.adapter.SalaryAdapter;
import io.cordova.myapp00d753.module.SalaryModule;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.RecyclerItemClickListener;

public class SalaryActivity extends AppCompatActivity  {
    private static final String TAG = "SalaryActivity";
    RecyclerView rvSalary;
    ArrayList<SalaryModule> salaryList = new ArrayList<>();
    SalaryAdapter salaryAdapter;
    String[] spYearList = {"----select----", "2015", "2016", "2017", "2018", "2019"};
    ArrayList<String> splist = new ArrayList<>();
    Spinner spYear;
    ImageView imgBack, imgHome;
    String year="0";
    int y;
    AlertDialog alertDialog;
    TextView tvYear;
    LinearLayout llSearch;
    LinearLayout llMain, llLoader;
    Pref pref;
    String surl;
    NetworkConnectionCheck connectionCheck;
    LinearLayout llNodata;
    LinearLayout llAgain;
    ImageView imgAgain,imgSearch;
    TextView tvToolBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary);
        initialize();
        if (connectionCheck.isNetworkAvailable()) {
            //TODO: new api
            JSONObject obj=new JSONObject();
            try {

                obj.put("AEMEmployeeID",pref.getEmpId());
                obj.put("SalYear",year);
                obj.put("SecurityCode",pref.getSecurityCode());
                getSalaryList(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //getSalaryList();
        }else {

        }
        onClick();
    }

    private void initialize() {
        pref = new Pref(getApplicationContext());
        connectionCheck=new NetworkConnectionCheck(SalaryActivity.this);
        rvSalary = (RecyclerView) findViewById(R.id.rvSalary);
        LinearLayoutManager layoutManager = new LinearLayoutManager(SalaryActivity.this, LinearLayoutManager.VERTICAL, false);
        rvSalary.setLayoutManager(layoutManager);

        spYear = (Spinner) findViewById(R.id.spYear);
        //SpinnerAdapter spinnerAdapter = new SpinnerAdapter(SalaryActivity.this, spYearList);
        //spYear.setAdapter(spinnerAdapter);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        llSearch = (LinearLayout) findViewById(R.id.llSearch);
        y = Calendar.getInstance().get(Calendar.YEAR);
        year=String.valueOf(y);

        tvYear = (TextView) findViewById(R.id.tvYear);
        tvYear.setText(year);
        llLoader = (LinearLayout) findViewById(R.id.llLoader);
        llMain = (LinearLayout) findViewById(R.id.llMain);
        llNodata=(LinearLayout)findViewById(R.id.llNodata);
        llAgain=(LinearLayout)findViewById(R.id.llAgain);
        imgAgain=(ImageView)findViewById(R.id.imgAgain);
        imgAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO: new api
                JSONObject obj=new JSONObject();
                try {

                    obj.put("AEMEmployeeID",pref.getEmpId());
                    obj.put("SalYear",year);
                    obj.put("SecurityCode",pref.getSecurityCode());
                    getSalaryList(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        imgSearch=(ImageView)findViewById(R.id.imgSearch);
        tvToolBar=(TextView) findViewById(R.id.tvToolBar);
        tvToolBar.setText("Monthly Salary - \n"+year);
    }

    private void getSalaryList(JSONObject jsonObject) {
        Log.e(TAG, "getSalaryList: "+jsonObject);
        AndroidNetworking.post(AppData.GET_EMPLOYEE_SALARY)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "SALARY_LIST: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(Response_Data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String SalMonth = obj.optString("SalMonth");
                                    String SalYear = obj.optString("SalYear");
                                    String MonthlyNet = obj.optString("MonthlyNet");
                                    String url = obj.optString("url");
                                    SalaryModule salaryModule = new SalaryModule(SalYear, SalMonth,"Rs. "+ MonthlyNet, url);
                                    salaryModule.setIsPaidService(obj.optInt("IsPaidService"));
                                    salaryModule.setCharges(obj.optString("Charges"));
                                    salaryList.add(salaryModule);
                                }

                                if (salaryList.size() > 0) {
                                    llLoader.setVisibility(View.GONE);
                                    llMain.setVisibility(View.VISIBLE);
                                    llNodata.setVisibility(View.GONE);
                                    llAgain.setVisibility(View.GONE);
                                    setAdapter();
                                } else {
                                    llLoader.setVisibility(View.GONE);
                                    llMain.setVisibility(View.GONE);
                                    llNodata.setVisibility(View.GONE);
                                    llAgain.setVisibility(View.GONE);
                                }
                            } else {
                                //Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNodata.setVisibility(View.VISIBLE);
                                llAgain.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SalaryActivity.this, "Something went to wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "SALARY_LIST_error: "+anError.getErrorBody());
                        llLoader.setVisibility(View.GONE);
                        llMain.setVisibility(View.GONE);
                        llNodata.setVisibility(View.GONE);
                        llAgain.setVisibility(View.VISIBLE);
                    }
                });
    }

    public void paymentOption(int pos){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("AEMEmployeeId",pref.getEmpId());
            jsonObject.put("SalYear",year);
            jsonObject.put("SalMonth",salaryList.get(pos).getMonth());
            jsonObject.put("Charges",salaryList.get(pos).getCharges());
            jsonObject.put("SecurityCode",pref.getSecurityCode());
            getPaymentLink(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void getPaymentLink(JSONObject jsonObject) {
        ProgressDialog pd=new ProgressDialog(SalaryActivity.this);
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
        pd.show();
        Log.e(TAG, "getSalaryList: "+jsonObject);
        AndroidNetworking.post(AppData.PAYMENT_LINK)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "SALARY_LIST: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            pd.dismiss();
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                Uri uri = Uri.parse(Response_Data); // missing 'http://' will cause crashed
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);


                            } else {
                                //Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNodata.setVisibility(View.VISIBLE);
                                llAgain.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SalaryActivity.this, "Something went to wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "SALARY_LIST_error: "+anError.getErrorBody());
                        llLoader.setVisibility(View.GONE);
                        llMain.setVisibility(View.GONE);
                        llNodata.setVisibility(View.GONE);
                        llAgain.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void getSalaryList() {
        surl = AppData.url+"get_Salary?AEMConsultantID=0&AEMClientID=null&MasterID=" + pref.getMasterId() + "&AEMEmployeeID=" + pref.getEmpId() + "&SalYear=" + year + "&SalMonth=jan&WorkingStatus=3&CurrentPage=1&SecurityCode="+pref.getSecurityCode();
        Log.d("salaryinput",surl);
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);
        llAgain.setVisibility(View.GONE);

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
                          //      Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();
                                salaryList.clear();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String SalMonth = obj.optString("SalMonth");
                                    String SalYear = obj.optString("SalYear");
                                    String MonthlyNet = obj.optString("MonthlyNet");
                                    String url = obj.optString("url");
                                    SalaryModule salaryModule = new SalaryModule(SalYear, SalMonth,"Rs. "+ MonthlyNet, url);
                                    salaryList.add(salaryModule);
                                }

                                if (salaryList.size() > 0) {
                                    llLoader.setVisibility(View.GONE);
                                    llMain.setVisibility(View.VISIBLE);
                                    llNodata.setVisibility(View.GONE);
                                    llAgain.setVisibility(View.GONE);
                                    setAdapter();
                                } else {
                                    llLoader.setVisibility(View.GONE);
                                    llMain.setVisibility(View.GONE);
                                    llNodata.setVisibility(View.GONE);
                                    llAgain.setVisibility(View.GONE);
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNodata.setVisibility(View.VISIBLE);
                                llAgain.setVisibility(View.GONE);

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
                llLoader.setVisibility(View.GONE);
                llMain.setVisibility(View.GONE);
                llNodata.setVisibility(View.GONE);
                llAgain.setVisibility(View.VISIBLE);

               // Toast.makeText(SalaryActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
    }

    private void setAdapter() {
        salaryAdapter = new SalaryAdapter(salaryList,SalaryActivity.this);
        rvSalary.setAdapter(salaryAdapter);
    }

    private void onClick() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SalaryActivity.this, EmployeeDashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
              //  finish();
            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    showYearDialog();
            }
        });
    }


    private void showYearDialog() {
        year = String.valueOf(y);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SalaryActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_year, null);
        dialogBuilder.setView(dialogView);
        final TextView tvYear1 = (TextView) dialogView.findViewById(R.id.tvYear1);
        final TextView tvYear2 = (TextView) dialogView.findViewById(R.id.tvYear2);
        final TextView tvYear3 = (TextView) dialogView.findViewById(R.id.tvYear3);
        LinearLayout llY1 = (LinearLayout) dialogView.findViewById(R.id.llY1);
        LinearLayout llY2 = (LinearLayout) dialogView.findViewById(R.id.llY2);
        LinearLayout llY3 = (LinearLayout) dialogView.findViewById(R.id.llY3);

        int pastx1 = y - 2;
        String pasty1 = String.valueOf(pastx1);
        tvYear1.setText(pasty1);

        int pastx2 = y - 1;
        String pasty2 = String.valueOf(pastx2);
        tvYear2.setText(pasty2);

        String pastx3 = String.valueOf(y);
        tvYear3.setText(pastx3);

        ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });


        llY3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = tvYear3.getText().toString();
                Log.d("yrtrr", year);
                tvYear.setText(year);
                salaryList.clear();

                //TODO: new api
                JSONObject obj=new JSONObject();
                try {

                    obj.put("AEMEmployeeID",pref.getEmpId());
                    obj.put("SalYear",year);
                    obj.put("SecurityCode",pref.getSecurityCode());
                    getSalaryList(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                alertDialog.dismiss();
            }
        });

        llY2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = tvYear2.getText().toString();
                alertDialog.dismiss();
                tvYear.setText(year);
                salaryList.clear();

                //TODO: new api
                JSONObject obj=new JSONObject();
                try {

                    obj.put("AEMEmployeeID",pref.getEmpId());
                    obj.put("SalYear",year);
                    obj.put("SecurityCode",pref.getSecurityCode());
                    getSalaryList(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                alertDialog.dismiss();
                Log.d("ttt", year);
            }
        });

        llY1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = tvYear1.getText().toString();
                alertDialog.dismiss();
                tvYear.setText(year);
                salaryList.clear();

                //TODO: new api
                JSONObject obj=new JSONObject();
                try {

                    obj.put("AEMEmployeeID",pref.getEmpId());
                    obj.put("SalYear",year);
                    obj.put("SecurityCode",pref.getSecurityCode());
                    getSalaryList(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                alertDialog.dismiss();
                Log.d("ttt", year);
            }
        });

        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(true);
        Window window = alertDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog.show();
    }
}
