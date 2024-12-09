package io.cordova.myapp00d753.activity.SKF;

import static io.cordova.myapp00d753.activity.protectorgamble.ProtectorGambleAttendanceActivity.SKF_PUNE_CLIENT_OFFICE_ID;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.Retrofit.RetrofitClient;
import io.cordova.myapp00d753.activity.BacklogAttendanceActivity;
import io.cordova.myapp00d753.activity.HolidayMarkingActivity;
import io.cordova.myapp00d753.activity.SKF.adapter.SKF_BacklogAdapter;
import io.cordova.myapp00d753.activity.metso.MetsoAttendanceRegularizationActivity;
import io.cordova.myapp00d753.activity.metso.adapter.LocationSpinnerAdapter;
import io.cordova.myapp00d753.activity.metso.adapter.MetsoAttendanceAdapter;
import io.cordova.myapp00d753.activity.metso.model.MetsoLocationModel;
import io.cordova.myapp00d753.adapter.BackLogAdapter;
import io.cordova.myapp00d753.module.BackLogAttendanceModel;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SKF_AttendanceRegularizationActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SKF_AttendanceRegulariz";
    RecyclerView rvItem;
    LinearLayout btnSubmit;
    ImageView imgBack,imgLike;
    LinearLayout lnStartDate,lnEndDate,llMain,llLoader,llNodata,llWarning;
    TextView tvStartDate,tvEndDate;
    Button btnShow;
    Pref pref;
    ArrayList<MetsoLocationModel> metsoLocationArrayList;
    LocationSpinnerAdapter locationSpinnerAdapter;
    String startDate="",endDate="",ClientID="",MasterID="";
    String prvMonth1stDate ="",currentDate="";
    ArrayList<BackLogAttendanceModel> blockLogList;
    LinearLayout llTick;
    public boolean isSelectedAll = false;
    SKF_BacklogAdapter skfBacklogAdapter;
    int itemSelectCount=0;
    private Dialog shiftAndLocationDialog;
    String Siteid = "";
    ProgressDialog progressDialog;
    AlertDialog alerDialog1;
    android.app.AlertDialog al1;
    ArrayList<String> dayTypeArray;
    TextView txtRegularisationCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skf_attendance_regularization);
        
        initView();
    }

    private void initView() {
        rvItem = findViewById(R.id.rvItem);
        btnSubmit = findViewById(R.id.btnSubmit);
        imgBack = findViewById(R.id.imgBack);
        lnStartDate = findViewById(R.id.lnStartDate);
        lnEndDate = findViewById(R.id.lnEndDate);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        btnShow = findViewById(R.id.btnShow);
        llMain = findViewById(R.id.llMain);
        llLoader = findViewById(R.id.llLoader);
        llNodata = findViewById(R.id.llNodata);
        llWarning = findViewById(R.id.llWarning);
        llTick = findViewById(R.id.llTick);
        imgLike = findViewById(R.id.imgLike);
        txtRegularisationCount = findViewById(R.id.txtRegularisationCount);
        btnSubmit.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        lnStartDate.setOnClickListener(this);
        lnEndDate.setOnClickListener(this);
        btnShow.setOnClickListener(this);
        llTick.setOnClickListener(this);
        rvItem.setLayoutManager(new LinearLayoutManager(SKF_AttendanceRegularizationActivity.this));
        pref = new Pref(SKF_AttendanceRegularizationActivity.this);
        progressDialog = new ProgressDialog(SKF_AttendanceRegularizationActivity.this);
        progressDialog.setCancelable(false);
        getLocationData();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("clientid","AEMCLI1110000502");
            jsonObject.put("BranchID","AEMCLO1110001277");
            getSkfDayTypeList(jsonObject);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSubmit:
                if (itemSelectCount>0){
                    if (SKF_PUNE_CLIENT_OFFICE_ID.equals(pref.getEmpClintOffId())){
                        openShiftAndLocationPopup();
                    } else {
                        submitOperation();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"Please Select Your Date(s)",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.imgBack:
                finish();
                break;
            case R.id.lnStartDate:
                showStartDatePicker();
                break;
            case R.id.lnEndDate:
                showEndDatePicker();
                break;
            case R.id.btnShow:
                if (!startDate.equals("")){
                    if (!endDate.equals("")){
                        //getAttendanceRegularizationData();
                        JSONObject obj=new JSONObject();
                        try {
                            obj.put("DbOperation","1");
                            obj.put("empid",pref.getEmpId());
                            obj.put("clientid",pref.getEmpClintId());
                            obj.put("fromdate",startDate);
                            obj.put("todate",endDate);
                            obj.put("SecurityCode",pref.getSecurityCode());
                            getBackLogData(obj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(SKF_AttendanceRegularizationActivity.this,"Please Enter End Date",Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(SKF_AttendanceRegularizationActivity.this,"Please Enter Start Date",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.llTick:
                selectAllOperation();
                break;
        }
    }

    private void getSkfDayTypeList(JSONObject jsonObject) {
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        AndroidNetworking.post(AppData.SKF_DAY_TYPE_LIST)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Log.e(TAG, "SKF_DAY_TYPE_LIST: "+response.toString(4));
                            progressDialog.dismiss();
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                dayTypeArray = new ArrayList<>();
                                JSONArray jsonArray = new JSONArray(Response_Data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    dayTypeArray.add(jsonObject.optString("DayType"));
                                }

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Log.e(TAG, "SKF_DAY_TYPE_LIST_error: "+anError.getErrorBody());
                    }
                });
    }

    private void selectAllOperation() {
        if (imgLike.getVisibility() == View.GONE){
            imgLike.setVisibility(View.VISIBLE);
            isSelectedAll = true;
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    setAllSelectOrUnselect(isSelectedAll);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            skfBacklogAdapter.selectAll();
                            progressDialog.dismiss();
                        }
                    });
                }
            }).start();
        } else {
            imgLike.setVisibility(View.GONE);
            isSelectedAll = false;
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    setAllSelectOrUnselect(isSelectedAll);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            skfBacklogAdapter.selectAll();
                            progressDialog.dismiss();
                        }
                    });
                }
            }).start();
        }
    }

    private void submitOperation() {
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        String regularizationSubmitString="";
        for (int i = 0; i < blockLogList.size(); i++) {
            Log.e(TAG, "submitOperation: Remarks: "+blockLogList.get(i).getRemarks());
            if (blockLogList.get(i).isSelected()){
                //Log.e(TAG, "submitOperation: "+i);
                if (regularizationSubmitString.isEmpty()){
                    if (SKF_PUNE_CLIENT_OFFICE_ID.equals(pref.getEmpClintOffId()))
                        regularizationSubmitString = pref.getEmpId()+"_"+pref.getEmpClintId()+"_"+blockLogList.get(i).getDate()+"_"+blockLogList.get(i).getInTime()+"_"+blockLogList.get(i).getOutTime()+"_"+blockLogList.get(i).getRemarks()+"_"+Siteid+"_0_"+blockLogList.get(i).getDayType();
                    else
                        regularizationSubmitString = pref.getEmpId()+"_"+pref.getEmpClintId()+"_"+blockLogList.get(i).getDate()+"_"+blockLogList.get(i).getInTime()+"_"+blockLogList.get(i).getOutTime()+"_"+blockLogList.get(i).getRemarks()+"_0_0_"+blockLogList.get(i).getDayType();
                } else {
                    if (SKF_PUNE_CLIENT_OFFICE_ID.equals(pref.getEmpClintOffId()))
                        regularizationSubmitString += ","+pref.getEmpId()+"_"+pref.getEmpClintId()+"_"+blockLogList.get(i).getDate()+"_"+blockLogList.get(i).getInTime()+"_"+blockLogList.get(i).getOutTime()+"_"+blockLogList.get(i).getRemarks()+"_"+Siteid+"_0_"+blockLogList.get(i).getDayType();
                    else
                        regularizationSubmitString += ","+pref.getEmpId()+"_"+pref.getEmpClintId()+"_"+blockLogList.get(i).getDate()+"_"+blockLogList.get(i).getInTime()+"_"+blockLogList.get(i).getOutTime()+"_"+blockLogList.get(i).getRemarks()+"_0_0_"+blockLogList.get(i).getDayType();
                }
            }
        }
        Log.e(TAG, "submitOperation: "+regularizationSubmitString);

        JSONObject obj=new JSONObject();
        try {
            obj.put("StrDayBreakUp", regularizationSubmitString);
            obj.put("SetMsg","");
            obj.put("SecurityCode", pref.getSecurityCode());
            //Log.e(TAG, "BACKLOG_SAVE_INPUT: "+obj);
            regularizationSaveApiCall(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void regularizationSaveApiCall(JSONObject jsonObject) {
        Log.e(TAG, "BACKLOG_SAVE_INPUT: "+jsonObject);
        AndroidNetworking.post(AppData.SKF_SAVE_ATTENDANCE_REGULARIZATION)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "SKF_BACKLOG_SAVE: "+response.toString(4));
                            progressDialog.dismiss();
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                //String Response_Data = job1.optString("Response_Data");
                                //successAlert();
                                JSONObject json_Response_Data = job1.optJSONObject("Response_Data");
                                String Table = json_Response_Data.optString("Table");
                                JSONArray TableArray = new JSONArray(Table);
                                Log.e(TAG, "TableArray: "+TableArray);
                                String Table1 = json_Response_Data.optString("Table1");
                                JSONArray Table1Array = new JSONArray(Table1);
                                Log.e(TAG, "TableArray: "+Table1Array);
                                JSONObject FinalStatusObj = Table1Array.optJSONObject(0);
                                Log.e(TAG, "FinalStatusObj: "+FinalStatusObj);
                                if (FinalStatusObj.getInt("FinalStatus") == 1){
                                    //TODO: Success
                                    int FinalStatus = FinalStatusObj.getInt("FinalStatus");
                                    int AlreadyRequestCount = FinalStatusObj.getInt("AlreadyRequestCount");
                                    int ExceedRequestLimit = FinalStatusObj.getInt("ExceedRequestLimit");
                                    txtRegularisationCount.setText(String.valueOf(AlreadyRequestCount));
                                    Log.e(TAG, "FinalStatus: "+FinalStatus);
                                    Log.e(TAG, "AlreadyRequestCount: "+AlreadyRequestCount);
                                    Log.e(TAG, "ExceedRequestLimit: "+ExceedRequestLimit);
                                    successAlert();
                                } else {
                                    blockLogList.clear();
                                    btnSubmit.setVisibility(View.GONE);
                                    //TODO: Failure
                                    for (int i = 0; i < TableArray.length(); i++) {
                                        Log.e(TAG, "Table: called");
                                        JSONObject obj = TableArray.getJSONObject(i);
                                        String AttDate = obj.optString("Dates");
                                        String InTime = obj.optString("Intime");
                                        String OutTime = obj.optString("Outtime");
                                        String Daytype = obj.optString("DayType");
                                        String Remarks = obj.optString("Remarks");
                                        String RemarksCode = String.valueOf(obj.optInt("RemarksCode"));
                                        String SLNo = String.valueOf(obj.optInt("SLNo"));


                                        BackLogAttendanceModel blockModule = new BackLogAttendanceModel(AttDate, InTime, OutTime);
                                        blockModule.setDayType(Daytype);
                                        blockModule.setRemarks(Remarks);
                                        blockModule.setRemarksCode(RemarksCode);
                                        blockModule.setSLNo(SLNo);
                                        blockLogList.add(blockModule);
                                    }
                                    skfBacklogAdapter.notifyDataSetChanged();
                                }
                            } else {
                                showErrorDialog(Response_Message);
                                //Toast.makeText(SKF_AttendanceRegularizationActivity.this,Response_Message,Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Log.e(TAG, "SKF_BACKLOG_SAVE_error: "+anError.getErrorBody());
                    }
                });
    }


    private void getBackLogData(JSONObject jsonObject) {
        Log.e(TAG, "getBackLogData: INPUT"+jsonObject);
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);
        //blockLogList.clear();
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
                            Log.e(TAG, "SKF_BACK_LOG_DATA: "+response.toString(4));
                            blockLogList = new ArrayList<>();
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                JSONObject Response_Data_obj = new JSONObject(Response_Data);
                                String Table = Response_Data_obj.optString("Table");
                                Log.e(TAG, "Table: "+Table);
                                String Table1 = Response_Data_obj.optString("Table1");
                                Log.e(TAG, "Table1: "+Table1);
                                JSONArray Table1Array = new JSONArray(Table1);
                                JSONObject table1Obj = Table1Array.getJSONObject(0);
                                txtRegularisationCount.setText(String.valueOf(table1Obj.getInt("RegularisationRequestCount")));
                                if(table1Obj.getInt("RegularisationRequestCount") < 4) {
                                    JSONArray jsonArray = new JSONArray(Table);
                                    if (jsonArray.length() > 0){
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject obj = jsonArray.getJSONObject(i);
                                            String AttDate = obj.optString("Dates");
                                            String InTime = obj.optString("Intime");
                                            String OutTime = obj.optString("Outtime");
                                            String Daytype=obj.optString("Daytype");
                                            String Remarks=obj.optString("Remark");
                                            //Log.e(TAG, "Remarks: "+obj.optString("Remark") );

                                            //Log.e(TAG, "Remarks: "+Remarks);

                                            BackLogAttendanceModel blockModule = new BackLogAttendanceModel(AttDate, InTime, OutTime);
                                            blockModule.setDayType(Daytype);
                                            blockModule.setRemarks(Remarks);
                                            blockLogList.add(blockModule);
                                            //item1.add(pref.getEmpId()+"_"+pref.getEmpClintId()+"_"+AttDate + "_" + InTime + "_" + OutTime + "_"+Remarks);
                                        }
                                        llLoader.setVisibility(View.GONE);
                                        llMain.setVisibility(View.VISIBLE);
                                        llNodata.setVisibility(View.GONE);
                                        llWarning.setVisibility(View.GONE);
                                        skfBacklogAdapter = new SKF_BacklogAdapter(blockLogList,dayTypeArray,SKF_AttendanceRegularizationActivity.this);
                                        rvItem.setAdapter(skfBacklogAdapter);
                                    } else {
                                        llLoader.setVisibility(View.GONE);
                                        llMain.setVisibility(View.GONE);
                                        llNodata.setVisibility(View.VISIBLE);
                                        llWarning.setVisibility(View.GONE);
                                    }
                                } else {
                                    //TODO: Exceed Request Limit message;
                                    llLoader.setVisibility(View.GONE);
                                    llMain.setVisibility(View.GONE);
                                    llNodata.setVisibility(View.GONE);
                                    llWarning.setVisibility(View.VISIBLE);
                                }
                                //tvCount.setText("Total Day(s) Count : "+jsonArray.length());
                            } else {
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNodata.setVisibility(View.VISIBLE);
                                llWarning.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "SKF_BACK_LOG_DATA_error: "+anError.getErrorBody());
                        llLoader.setVisibility(View.VISIBLE);
                        llMain.setVisibility(View.GONE);
                        llNodata.setVisibility(View.GONE);
                    }
                });
    }


    private void getLocationData() {
        progressDialog.setMessage("Loading...");
        Call<JsonObject> call = RetrofitClient
                .getInstance()
                .getApi()
                .GetMetsoAttendanceData("1", pref.getEmpClintId(), "0000");

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(String.valueOf(response.body()));
                    if (object.getBoolean("responseStatus") == true) {
                        JSONArray jsonArray = object.getJSONArray("responseData");
                        metsoLocationArrayList = new ArrayList<>();
                        metsoLocationArrayList.add(new MetsoLocationModel(0, "Select Location"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject objectResponse = jsonArray.getJSONObject(i);
                            MetsoLocationModel metsoLocationModel = new MetsoLocationModel(objectResponse.getInt("Siteid"),
                                    (String) objectResponse.getString("SiteName"));
                            metsoLocationArrayList.add(metsoLocationModel);
                        }

                        locationSpinnerAdapter = new LocationSpinnerAdapter(SKF_AttendanceRegularizationActivity.this, metsoLocationArrayList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, "onFailure: "+call);
            }
        });
    }



    private void showStartDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(SKF_AttendanceRegularizationActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        int month = (monthOfYear + 1);
                        startDate =  year +"-"+month+"-"+dayOfMonth;
                        tvStartDate.setText(Util.changeAnyDateFormat(startDate,"yyyy-MM-dd","dd MMM yyyy"));
                    }
                }, mYear, mMonth, mDay);
        //datePickerDialog.getDatePicker().setMaxDate((long) (System.currentTimeMillis() - 1000));
        datePickerDialog.show();
    }

    private void showEndDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(SKF_AttendanceRegularizationActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        int enddate = dayOfMonth + monthOfYear + year;
                        int month = (monthOfYear + 1);
                        endDate = year +"-"+month+"-"+dayOfMonth;
                        tvEndDate.setText(Util.changeAnyDateFormat(endDate,"yyyy-MM-dd","dd MMM yyyy"));
                    }
                }, mYear, mMonth, mDay);

        //datePickerDialog.getDatePicker().setMaxDate((long) (System.currentTimeMillis() - 1000));
        datePickerDialog.show();
    }

    void setAllSelectOrUnselect(boolean isSelectedAll){
        for (int i = 0; i < blockLogList.size(); i++) {
            blockLogList.get(i).setSelected(isSelectedAll);
        }
    }

    public void updateItemStatus(int position,boolean status) {
        if (status){
            itemSelectCount++;
            if (itemSelectCount == blockLogList.size())
                imgLike.setVisibility(View.VISIBLE);
        } else {
            itemSelectCount--;
            if (itemSelectCount < 0){
                itemSelectCount = 0;
            }

            if (itemSelectCount != blockLogList.size())
                imgLike.setVisibility(View.GONE);
        }

        Log.e(TAG, "updateItemStatus: "+itemSelectCount);
    }

    private void openShiftAndLocationPopup() {
        shiftAndLocationDialog = new Dialog(SKF_AttendanceRegularizationActivity.this);
        shiftAndLocationDialog.setContentView(R.layout.shift_location_popup);
        shiftAndLocationDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        shiftAndLocationDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        LinearLayout llShift = shiftAndLocationDialog.findViewById(R.id.llShift);
        llShift.setVisibility(View.GONE);
        LinearLayout lnCancel = shiftAndLocationDialog.findViewById(R.id.lnCancel);
        TextView txtSelectShift = shiftAndLocationDialog.findViewById(R.id.txtSelectShift);
        TextView txtSelectLocation = shiftAndLocationDialog.findViewById(R.id.txtSelectLocation);
        TextView txtErrorShift = shiftAndLocationDialog.findViewById(R.id.txtErrorShift);
        TextView txtErrorLocation = shiftAndLocationDialog.findViewById(R.id.txtErrorLocation);
        Spinner spShift = shiftAndLocationDialog.findViewById(R.id.spShift);
        Spinner spLocation = shiftAndLocationDialog.findViewById(R.id.spLocation);
        AppCompatButton btnMarkedYourAttendance = shiftAndLocationDialog.findViewById(R.id.btnMarkedYourAttendance);
        TextView textView = shiftAndLocationDialog.findViewById(R.id.textView);
        textView.setText("Select location");

        spLocation.setAdapter(locationSpinnerAdapter);
        spLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MetsoLocationModel clickedItem = (MetsoLocationModel) adapterView.getItemAtPosition(i);
                if (!clickedItem.getSiteName().equals("Select Location")) {
                    txtSelectLocation.setText(clickedItem.getSiteName());
                    Siteid = String.valueOf(clickedItem.getSiteid());
                    txtErrorLocation.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        txtSelectShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spShift.performClick();
            }
        });

        txtSelectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spLocation.performClick();
            }
        });

        lnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shiftAndLocationDialog.cancel();
            }
        });

        btnMarkedYourAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtSelectLocation.getText().toString().trim().isEmpty()) {
                    txtErrorLocation.setVisibility(View.VISIBLE);
                } else {
                    txtErrorLocation.setVisibility(View.GONE);
                    shiftAndLocationDialog.cancel();

                    submitOperation();
                }
            }
        });

        Window window = shiftAndLocationDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.NO_GRAVITY;
        shiftAndLocationDialog.setCancelable(false);
        shiftAndLocationDialog.show();
    }

    private void successAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SKF_AttendanceRegularizationActivity.this, R.style.CustomDialogNew);
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


                //getBackLogData();

                JSONObject obj=new JSONObject();
                try {
                    obj.put("DbOperation","1");
                    obj.put("empid",pref.getEmpId());
                    obj.put("clientid",pref.getEmpClintId());
                    obj.put("fromdate",startDate);
                    obj.put("todate",endDate);
                    obj.put("SecurityCode",pref.getSecurityCode());
                    getBackLogData(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(false);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }

    private void showErrorDialog(String text) {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(SKF_AttendanceRegularizationActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.error_ayput, null);
        dialogBuilder.setView(dialogView);
        TextView tvError = (TextView) dialogView.findViewById(R.id.tvError);
        tvError.setText(text);
        ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                al1.dismiss();
            }
        });

        al1 = dialogBuilder.create();
        al1.setCancelable(false);
        Window window = al1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        al1.show();
    }
}