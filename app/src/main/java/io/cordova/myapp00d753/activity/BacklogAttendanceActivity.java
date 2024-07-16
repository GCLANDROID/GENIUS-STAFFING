package io.cordova.myapp00d753.activity;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.androidnetworking.interfaces.UploadProgressListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.BackLogAdapter;
import io.cordova.myapp00d753.module.BackLogAttendanceModel;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.Util;

public class BacklogAttendanceActivity extends AppCompatActivity {
    private static final String TAG = "BacklogAttendanceActivi";
    TextView tvToolBar;
    RecyclerView rvItem;
    public static ArrayList<BackLogAttendanceModel> newBacklogArray = new ArrayList<BackLogAttendanceModel>();
    LinearLayout llHeading, llLoader, llMain;
    Pref pref;
    ArrayList<BackLogAttendanceModel> blockLogList = new ArrayList<>();
    ArrayList<String> backLogItem = new ArrayList<>();
    BackLogAdapter backLogAdapter;
    ImageView imgHome,imgBack;
    ArrayList<String>backLogData=new ArrayList<>();
    LinearLayout btnSubmit;
    String backlogDetails;
    LinearLayout llNodata;
    EditText etFocus;
    AlertDialog alerDialog1;
    TextView tvRemark,tvDate,tvInTime,tvOutTime;
    String securityCode;
    Button btnShow;
    TextView tvStrtDate,tvEndDate;
    String strtDate="",endDate="";
    LinearLayout lnStartDate,lnEndDate;
    LinearLayout llTick;
    ImageView imgLike;
    ArrayList<String>item1=new ArrayList<>();
    int allclick;
    TextView tvCount;
    int itemSelectCount=0;
    public boolean isSelectedAll = false;
    ProgressDialog globleProgressDialog;
    int startYear,startMonth,startDay,endYear,endMonth,endDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backlog);
        initView();
        onClick();
    }

    private void initView() {
        pref = new Pref(getApplicationContext());
        securityCode=pref.getSecurityCode();
        tvToolBar = findViewById(R.id.tvToolBar);
        tvCount=(TextView)findViewById(R.id.tvCount);
        rvItem = findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(BacklogAttendanceActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);
        llHeading = findViewById(R.id.llHeading);
        llLoader = findViewById(R.id.llLoader);
        llMain = findViewById(R.id.llMain);
        llNodata = findViewById(R.id.llNodata);
        etFocus=(EditText)findViewById(R.id.etFocus);


        imgHome=(ImageView)findViewById(R.id.imgHome);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        btnSubmit=(LinearLayout) findViewById(R.id.btnSubmit);

        tvRemark=(TextView)findViewById(R.id.tvRemark);
        tvDate=(TextView)findViewById(R.id.tvDate);
        tvInTime=(TextView)findViewById(R.id.tvInTime);
        tvOutTime=(TextView)findViewById(R.id.tvOutTime);

        tvEndDate=(TextView) findViewById(R.id.tvEndDate);
        tvStrtDate=(TextView) findViewById(R.id.tvStrtDate);
        btnShow=(Button) findViewById(R.id.btnShow);
        lnEndDate=(LinearLayout) findViewById(R.id.lnEndDate);
        lnStartDate=(LinearLayout) findViewById(R.id.lnStartDate);
        llTick=(LinearLayout) findViewById(R.id.llTick);
        imgLike=(ImageView) findViewById(R.id.imgLike);

        final Calendar c = Calendar.getInstance();
        startYear = c.get(Calendar.YEAR);
        startMonth = c.get(Calendar.MONTH);
        startDay = c.get(Calendar.DAY_OF_MONTH);

        endYear = c.get(Calendar.YEAR);
        endMonth = c.get(Calendar.MONTH);
        endDay = c.get(Calendar.DAY_OF_MONTH);


    }

    private void onClick(){
        llTick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgLike.getVisibility()==View.GONE){
                    imgLike.setVisibility(View.VISIBLE);
                    isSelectedAll = true;
                    backLogAdapter.selectAll();
                    allclick=1;
                }else {
                    imgLike.setVisibility(View.GONE);
                    isSelectedAll = false;
                    backLogAdapter.unselectall();
                    allclick=0;
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemSelectCount > 0 ||allclick==1) {
                    submitOperation();
                }else {
                    Toast.makeText(getApplicationContext(),"Please Select Your Date(s)",Toast.LENGTH_LONG).show();
                }

            }
        });

        lnStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStrtDatePicker();
            }
        });

        lnEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showendDatePicker();
            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!strtDate.equals("")){
                    if (!endDate.equals("")){
                        //getBackLogData();
                        JSONObject obj=new JSONObject();
                        try {
                            obj.put("DbOperation","1");
                            obj.put("empid",pref.getEmpId());
                            obj.put("clientid",pref.getEmpClintId());
                            obj.put("fromdate",strtDate);
                            obj.put("todate",endDate);
                            obj.put("SecurityCode",pref.getSecurityCode());
                            getBackLogData(obj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(BacklogAttendanceActivity.this,"Please Enter End Date",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(BacklogAttendanceActivity.this,"Please Enter Start Date",Toast.LENGTH_LONG).show();
                }
            }
        });

        etFocus.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){

                }
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BacklogAttendanceActivity.this, EmployeeDashBoardActivity.class);
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

    private void submitOperation() {
        globleProgressDialog = new ProgressDialog(BacklogAttendanceActivity.this);
        globleProgressDialog.setMessage("Loading..");
        globleProgressDialog.setCancelable(false);
        globleProgressDialog.show();
        StringBuilder regularizationSubmitString= new StringBuilder();
        for (int i = 0; i < blockLogList.size(); i++) {
            if (isSelectedAll){
                if (regularizationSubmitString.length() == 0){
                    regularizationSubmitString = new StringBuilder(pref.getEmpId() + "_" + pref.getEmpClintId() + "_" + blockLogList.get(i).getDate() + "_" + blockLogList.get(i).getInTime() + "_" + blockLogList.get(i).getOutTime() + "_" + blockLogList.get(i).getRemarks());
                } else {
                    regularizationSubmitString.append(",").append(pref.getEmpId()).append("_").append(pref.getEmpClintId()).append("_").append(blockLogList.get(i).getDate()).append("_").append(blockLogList.get(i).getInTime()).append("_").append(blockLogList.get(i).getOutTime()).append("_").append(blockLogList.get(i).getRemarks());
                }
            } else {
                if (blockLogList.get(i).isSelected()){
                    if (regularizationSubmitString.length() == 0){
                        regularizationSubmitString = new StringBuilder(pref.getEmpId() + "_" + pref.getEmpClintId() + "_" + blockLogList.get(i).getDate() + "_" + blockLogList.get(i).getInTime() + "_" + blockLogList.get(i).getOutTime() + "_" + blockLogList.get(i).getRemarks());
                    } else {
                        regularizationSubmitString.append(",").append(pref.getEmpId()).append("_").append(pref.getEmpClintId()).append("_").append(blockLogList.get(i).getDate()).append("_").append(blockLogList.get(i).getInTime()).append("_").append(blockLogList.get(i).getOutTime()).append("_").append(blockLogList.get(i).getRemarks());
                    }
                }
            }
        }

        //backlogSave(String.valueOf(regularizationSubmitString));

        JSONObject obj=new JSONObject();
        try {
            obj.put("StrDayBreakUp", regularizationSubmitString);
            obj.put("SetMsg","");
            obj.put("SecurityCode", securityCode);
            backlogSave2(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getBackLogData(JSONObject jsonObject) {
        Log.e(TAG, "getBackLogData: INPUT"+jsonObject);
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);
        blockLogList.clear();
        AndroidNetworking.post(AppData.GET_ATTENDANCE_REGULARIZATION)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "GET_BACK_LOG_DATA: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(Response_Data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String AttDate = obj.optString("Dates");
                                    String InTime = obj.optString("Intime");
                                    String OutTime = obj.optString("Outtime");
                                    String Daytype=obj.optString("Daytype");
                                    String Remarks=obj.optString("Remarks");

                                    //Log.e(TAG, "Remarks: "+Remarks);

                                    BackLogAttendanceModel blockModule = new BackLogAttendanceModel(AttDate, InTime, OutTime);
                                    blockModule.setDayType(Daytype);
                                    blockModule.setRemarks(Remarks);
                                    blockLogList.add(blockModule);
                                    item1.add(pref.getEmpId()+"_"+pref.getEmpClintId()+"_"+AttDate + "_" + InTime + "_" + OutTime + "_"+Remarks);
                                }
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.GONE);
                                backLogAdapter = new BackLogAdapter(blockLogList, BacklogAttendanceActivity.this,item1);
                                rvItem.setAdapter(backLogAdapter);
                                tvCount.setText("Total Day(s) Count : "+jsonArray.length());
                            } else {
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNodata.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "GET_BACK_LOG_DATA_error: "+anError.getErrorBody());
                        llLoader.setVisibility(View.VISIBLE);
                        llMain.setVisibility(View.GONE);
                        llNodata.setVisibility(View.GONE);
                    }
                });
    }

    private void getBackLogData() {
        String surl = AppData.url + "gcl_AttendanceRegularization/Get?DbOperation=1&empid="+pref.getEmpId()+"&clientid="+pref.getEmpClintId()+"&fromdate="+strtDate+"&todate="+endDate+"&SecurityCode="+pref.getSecurityCode() ;
        Log.d("backlogURL",surl);
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);
        blockLogList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("blockActivityData", response);
                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                blockLogList.clear();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String AttDate = obj.optString("Dates");
                                    String InTime = obj.optString("Intime");
                                    String OutTime = obj.optString("Outtime");
                                    String Daytype=obj.optString("Daytype");
                                    String Remarks=obj.optString("Remarks");

                                    Log.e(TAG, "Remarks: "+Remarks);

                                    BackLogAttendanceModel blockModule = new BackLogAttendanceModel(AttDate, InTime, OutTime);
                                    blockModule.setDayType(Daytype);
                                    blockModule.setRemarks(Remarks);
                                    blockLogList.add(blockModule);
                                    item1.add(pref.getEmpId()+"_"+pref.getEmpClintId()+"_"+AttDate + "_" + InTime + "_" + OutTime + "_"+Remarks);
                                }
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.GONE);
                                backLogAdapter = new BackLogAdapter(blockLogList, BacklogAttendanceActivity.this,item1);
                                rvItem.setAdapter(backLogAdapter);
                                tvCount.setText("Total Day(s) Count : "+responseData.length());
                            } else {

                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNodata.setVisibility(View.VISIBLE);
                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            //  Toast.makeText(AboutUsActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                llNodata.setVisibility(View.GONE);
                Toast.makeText(BacklogAttendanceActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(BacklogAttendanceActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    /*public void updateItemStatus(int position,boolean status) {

        item1.clear();

        blockLogList.get(position).setSelected(status);
        if (blockLogList.get(position).isSelected()==true) {
            backLogItem.add(pref.getEmpId()+"_"+pref.getEmpClintId()+"_"+blockLogList.get(position).getDate() + "_" + blockLogList.get(position).getInTime() + "_" + blockLogList.get(position).getOutTime() + "_"+blockLogList.get(position).getRemarks());

        }else {
            backLogItem.clear();
        }


        backLogAdapter.notifyDataSetChanged();
    }*/

    public void removeItem(int position) {
       Log.e(TAG, "removeItem: "+item1.remove(position));
       Log.e(TAG, "LIST SIZE: "+item1.size());
       Log.e(TAG, "position: "+position);
       item1.remove(position);

    }

    public void backlogSave2(JSONObject jsonObject) {
        Log.e(TAG, "backlogSave2: "+jsonObject);
        AndroidNetworking.post(AppData.SAVE_ATTENDANCE_REGULARIZATION)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            globleProgressDialog.dismiss();
                            Log.e(TAG, "SAVE_REGULARIZATION: " + response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                successAlert();
                            } else {
                                Toast.makeText(BacklogAttendanceActivity.this,Response_Message,Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(BacklogAttendanceActivity.this, "Something went to wrong, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        globleProgressDialog.dismiss();
                        Log.e(TAG, "SAVE_REGULARIZATION_error: "+anError.getErrorBody());
                        Toast.makeText(BacklogAttendanceActivity.this, "Something went to wrong, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void backlogSave(String regularizationSubmitString) {
        etFocus.clearFocus();
       /* if (allclick==1){
            backlogDetails=item1.toString().replace("[","").replace("]","").replaceAll("\\s+", "");
        }else {
            backlogDetails=backLogItem.toString().replace("[","").replace("]","").replaceAll("\\s+", "");

        }*/
        //Log.e("backlogDetails",backlogDetails);
        Log.e("backlogDetails",regularizationSubmitString);

        /*final ProgressDialog pg=new ProgressDialog(BacklogAttendanceActivity.this);
        pg.setMessage("Loading..");
        pg.setCancelable(false);
        pg.show();*/
        Log.e(TAG, "\nStrDayBreakUp: "+regularizationSubmitString
                +"\nSetMsg: \nSecurityCode:"+securityCode);
        AndroidNetworking.upload( AppData.url+"gcl_AttendanceRegularization/Save")
                .addMultipartParameter("StrDayBreakUp", regularizationSubmitString)
                .addMultipartParameter("SetMsg","")
                .addMultipartParameter("SecurityCode", securityCode)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        //pg.show();

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //pg.dismiss();
                        globleProgressDialog.dismiss();
                        JSONObject job = response;
                        String responseText=job.optString("responseText");
                        boolean responseStatus = job.optBoolean("responseStatus");
                        if (responseStatus) {

                          successAlert();
                        } else {
                            Toast.makeText(BacklogAttendanceActivity.this,responseText,Toast.LENGTH_LONG).show();
                        }


                        // boolean _status = job1.getBoolean("status");
                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e("error",error.toString());
                        //pg.dismiss();
                        globleProgressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();

                    }
                });
    }


    private void successAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(BacklogAttendanceActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);

        tvInvalidDate.setText("Your backlog attendance was saved successfully");

        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgLike.getVisibility() == View.VISIBLE) {
                    imgLike.setVisibility(View.GONE);
                }
                alerDialog1.dismiss();

                backLogItem.clear();
                backlogDetails = "";
                //getBackLogData();

                JSONObject obj=new JSONObject();
                try {
                    obj.put("DbOperation","1");
                    obj.put("empid",pref.getEmpId());
                    obj.put("clientid",pref.getEmpClintId());
                    obj.put("fromdate",strtDate);
                    obj.put("todate",endDate);
                    obj.put("SecurityCode",pref.getSecurityCode());
                    getBackLogData(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }

    private void showStrtDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);




        DatePickerDialog datePickerDialog = new DatePickerDialog(BacklogAttendanceActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        startYear = year;
                        startMonth = monthOfYear;
                        startDay = dayOfMonth;

                        int month = (monthOfYear + 1);
                        strtDate =  year +"-"+month+"-"+dayOfMonth;
                        tvStrtDate.setText(Util.changeAnyDateFormat(strtDate,"yyyy-MM-dd","dd MMM yyyy"));

                    }
                }, startYear, startMonth, startDay);
        datePickerDialog.getDatePicker().setMaxDate((long) (System.currentTimeMillis() - 1000));
        datePickerDialog.show();

    }


    private void showendDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(BacklogAttendanceActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        endYear = year;
                        endMonth = monthOfYear;
                        endDay = dayOfMonth;

                        int enddate = dayOfMonth + monthOfYear + year;
                        int month = (monthOfYear + 1);
                        endDate = year +"-"+month+"-"+dayOfMonth;
                        tvEndDate.setText(Util.changeAnyDateFormat(endDate,"yyyy-MM-dd","dd MMM yyyy"));

                    }
                }, endYear, endMonth, endDay);

        datePickerDialog.getDatePicker().setMaxDate((long) (System.currentTimeMillis() - 1000));
        datePickerDialog.show();

    }

    public void updateItemStatus(int position,boolean status) {
        if (status)
            itemSelectCount++;
        else
            itemSelectCount--;
    }

}
