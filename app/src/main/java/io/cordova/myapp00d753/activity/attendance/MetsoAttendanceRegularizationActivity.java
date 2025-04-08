package io.cordova.myapp00d753.activity.attendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import io.cordova.myapp00d753.R;

import io.cordova.myapp00d753.activity.metso.adapter.ApproverAutoCompleteAdapter;
import io.cordova.myapp00d753.activity.metso.adapter.LocationSpinnerAdapter;
import io.cordova.myapp00d753.activity.metso.adapter.MetsoAttendanceAdapter;
import io.cordova.myapp00d753.activity.metso.adapter.ShiftSpinnerAdapter;
import io.cordova.myapp00d753.activity.metso.model.ApproverModel;
import io.cordova.myapp00d753.activity.metso.model.LocationSpinnerModel;
import io.cordova.myapp00d753.activity.metso.model.MetsoLocationModel;
import io.cordova.myapp00d753.activity.metso.model.MetsoShiftModel;
import io.cordova.myapp00d753.module.BackLogAttendanceModel;

import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.Util;

public class MetsoAttendanceRegularizationActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MetsoAttendanceRegulari";
    RecyclerView rvItem;
    LinearLayout btnSubmit;
    ArrayList<LocationSpinnerModel> locationList;
    ArrayList<ApproverModel> approverList;
    LocationSpinnerAdapter locationSpinnerAdapter;
    LocationSpinnerAdapter locationSpinnerAdapter1;
    ApproverAutoCompleteAdapter approverAutoCompleteAdapter;
    ProgressDialog progressDialog;
    ImageView imgBack;
    LinearLayout lnStartDate,lnEndDate,llMain,llLoader,llNodata;
    String startDate="",endDate="",ClientID="",MasterID="";
    TextView tvStartDate,tvEndDate;
    Button btnShow;
    Pref pref;
    ArrayList<MetsoShiftModel> metsoShiftList;
    ShiftSpinnerAdapter shiftSpinnerAdapter;
    ArrayList<BackLogAttendanceModel> blockLogList;
    ArrayList<MetsoLocationModel> metsoLocationArrayList;
    int siteID;
    long approverID;
    ArrayList<String> backLogItem = new ArrayList<>();
    ArrayList<String>item1=new ArrayList<>();
    int itemSelectCount=0;
    AlertDialog alerDialog1;
    Dialog dialogLocationPopUp;
    String prvMonth1stDate ="",currentDate="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metso_attendance_regularization);
        initView();
        getMetsoShiftData();
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


        btnSubmit.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        lnStartDate.setOnClickListener(this);
        lnEndDate.setOnClickListener(this);
        btnShow.setOnClickListener(this);

        pref = new Pref(MetsoAttendanceRegularizationActivity.this);
        ClientID = pref.getEmpClintId();
        MasterID = pref.getMasterId();

        rvItem.setLayoutManager(new LinearLayoutManager(MetsoAttendanceRegularizationActivity.this));

        locationList = new ArrayList<LocationSpinnerModel>();

        //locationSpinnerAdapter = new LocationSpinnerAdapter(MetsoAttendanceRegularizationActivity.this, locationList);

        progressDialog = new ProgressDialog(MetsoAttendanceRegularizationActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

    }

    private void getPreviousMonth1stDate() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH)+1;
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        Log.e(TAG, "getPreviousMonth1stDate: Day: "+mDay+" Month: "+mMonth+" Year: "+mYear);
        currentDate = mYear+"-"+mMonth+"-"+mDay;
        if (mMonth == 1){
            int prvYear = mYear-1;
            int prvMonth = 12;
            int prvDay = 1;
            prvMonth1stDate = prvYear+"-"+prvMonth+"-"+prvDay;
        } else {
            int prvYear = mYear;
            int prvMonth = mMonth-1;
            int prvDay = 1;
            prvMonth1stDate = prvYear+"-"+prvMonth+"-"+prvDay;
        }
        Log.e(TAG, "getPreviousMonth1stDate: "+ prvMonth1stDate);
        getAttendanceRegularizationData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSubmit:
                if (itemSelectCount>0){
                    openLocationSelectDialog();
                }else {
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
                        getAttendanceRegularizationData();
                    } else {
                        Toast.makeText(MetsoAttendanceRegularizationActivity.this,"Please Enter End Date",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(MetsoAttendanceRegularizationActivity.this,"Please Enter Start Date",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }



    private void openLocationSelectDialog() {
        dialogLocationPopUp = new Dialog(MetsoAttendanceRegularizationActivity.this);
        dialogLocationPopUp.setContentView(R.layout.metso_att_location_selection_dialog);
        dialogLocationPopUp.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogLocationPopUp.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ImageView imgCancel = dialogLocationPopUp.findViewById(R.id.imgCancel);
        TextView txtSelectLocation = dialogLocationPopUp.findViewById(R.id.txtSelectLocation);
        TextView txtErrorApprover = dialogLocationPopUp.findViewById(R.id.txtErrorApprover);
        TextView txtErrorLocation = dialogLocationPopUp.findViewById(R.id.txtErrorLocation);
        AutoCompleteTextView actApproverName = dialogLocationPopUp.findViewById(R.id.actApproverName);
        Spinner spLocation = dialogLocationPopUp.findViewById(R.id.spLocation);
        LinearLayout llApprover = dialogLocationPopUp.findViewById(R.id.llApprover);
        AppCompatButton btnSubmit = dialogLocationPopUp.findViewById(R.id.btnSubmit);

        spLocation.setAdapter(locationSpinnerAdapter);
        spLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MetsoLocationModel clickedItem = (MetsoLocationModel) adapterView.getItemAtPosition(i);
                if (!clickedItem.getSiteName().equals("Select Location")){
                    txtSelectLocation.setText(clickedItem.getSiteName());
                    siteID = clickedItem.getSiteid();
                    txtErrorLocation.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        actApproverName.setAdapter(approverAutoCompleteAdapter);
        actApproverName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ApproverModel selectedItem = (ApproverModel) adapterView.getItemAtPosition(i);
                actApproverName.setText(selectedItem.getApproverName());
                approverID = selectedItem.approverId;
                txtErrorApprover.setVisibility(View.GONE);
            }
        });

        txtSelectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spLocation.performClick();
            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogLocationPopUp.cancel();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtSelectLocation.getText().toString().trim().isEmpty()){
                    txtErrorLocation.setVisibility(View.VISIBLE);
                } else if (actApproverName.getText().toString().trim().isEmpty()){
                    txtErrorApprover.setVisibility(View.VISIBLE);
                } else {
                    submitOperation();
                }
            }
        });
        dialogLocationPopUp.show();
    }

    private void submitOperation() {
        progressDialog.show();
        String regularizationSubmitString="";
        for (int i = 0; i < blockLogList.size(); i++) {
            if (blockLogList.get(i).isSelected()){
                //Log.e(TAG, "submitOperation: "+i);
                if (regularizationSubmitString.isEmpty()){
                    if (blockLogList.get(i).getRemarks2().isEmpty())
                        regularizationSubmitString = pref.getEmpId()+"_"+pref.getEmpClintId()+"_"+blockLogList.get(i).getDate()+"_"+blockLogList.get(i).getInTime()+"_"+blockLogList.get(i).getOutTime()+"_"+blockLogList.get(i).getShiftID()+"_NA";
                    else
                        regularizationSubmitString = pref.getEmpId()+"_"+pref.getEmpClintId()+"_"+blockLogList.get(i).getDate()+"_"+blockLogList.get(i).getInTime()+"_"+blockLogList.get(i).getOutTime()+"_"+blockLogList.get(i).getShiftID()+"_"+blockLogList.get(i).getRemarks2();
                } else {
                    if (blockLogList.get(i).getRemarks2().isEmpty())
                        regularizationSubmitString += ","+pref.getEmpId()+"_"+pref.getEmpClintId()+"_"+blockLogList.get(i).getDate()+"_"+blockLogList.get(i).getInTime()+"_"+blockLogList.get(i).getOutTime()+"_"+blockLogList.get(i).getShiftID()+"_NA";
                    else
                        regularizationSubmitString += ","+pref.getEmpId()+"_"+pref.getEmpClintId()+"_"+blockLogList.get(i).getDate()+"_"+blockLogList.get(i).getInTime()+"_"+blockLogList.get(i).getOutTime()+"_"+blockLogList.get(i).getShiftID()+"_"+blockLogList.get(i).getRemarks2();
                }
            }
        }
        Log.e(TAG, "submitOperation: "+regularizationSubmitString);
        regularizationSaveApiCall(regularizationSubmitString);
    }

    private void regularizationSaveApiCall(String strDayBreakUp) {
        /*final ProgressDialog pg=new ProgressDialog(MetsoAttendanceRegularizationActivity.this);
        pg.setMessage("Loading..");
        pg.setCancelable(false);
        pg.show();*/
        Log.e(TAG, "regularizationSaveApiCall: strDayBreakUp: "+strDayBreakUp+"\nSiteid: "+siteID+"\nApproverid: "+approverID+"\nSecurityCode: "+pref.getSecurityCode());
        AndroidNetworking.upload( AppData.url+"gcl_AttendanceRegularization/SaveForMetso")
                .addMultipartParameter("StrDayBreakUp", strDayBreakUp)
                .addMultipartParameter("Siteid", String.valueOf(siteID))
                .addMultipartParameter("Approverid", String.valueOf(approverID))
                .addMultipartParameter("SetMsg","")
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())
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
                        Log.e(TAG, "Regularization_response: "+new Gson().toJson(response));
                        progressDialog.dismiss();
                        dialogLocationPopUp.cancel();
                        JSONObject job = response;
                        String responseText=job.optString("responseText");
                        boolean responseStatus = job.optBoolean("responseStatus");
                        if (responseStatus) {
                            successAlert();
                        } else {
                            Toast.makeText(MetsoAttendanceRegularizationActivity.this,responseText,Toast.LENGTH_LONG).show();
                        }
                        // boolean _status = job1.getBoolean("status");
                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e("Regularization_error",error.toString());
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void successAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MetsoAttendanceRegularizationActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);

        tvInvalidDate.setText("Backlog Attendance saved successfully");



        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*   if (imgLike.getVisibility()==View.VISIBLE){
                    imgLike.setVisibility(View.GONE);
                }*/
                alerDialog1.dismiss();
                Intent intent = new Intent(MetsoAttendanceRegularizationActivity.this, MetsoAttendanceReportActivity.class);
                startActivity(intent);
                finish();
                //backLogItem.clear();
                //backlogDetails="";
                //getAttendanceRegularizationData();
            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }

    private void showStartDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(MetsoAttendanceRegularizationActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        int month = (monthOfYear + 1);
                        startDate =  year +"-"+month+"-"+dayOfMonth;
                        tvStartDate.setText(Util.changeAnyDateFormat(startDate,"yyyy-MM-dd","dd MMM yyyy"));
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate((long) (System.currentTimeMillis() - 1000));
        datePickerDialog.show();
    }


    private void showEndDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(MetsoAttendanceRegularizationActivity.this,
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

        datePickerDialog.getDatePicker().setMaxDate((long) (System.currentTimeMillis() - 1000));
        datePickerDialog.show();
    }

    private void getAttendanceRegularizationData() {
        llLoader.setVisibility(View.VISIBLE);
        //progressDialog.show();
        //AppData.url + "gcl_AttendanceRegularization/Get?DbOperation=1&empid="+pref.getEmpId()+"&clientid="+pref.getEmpClintId()+"&fromdate="+strtDate+"&todate="+endDate+"&SecurityCode="+pref.getSecurityCode() ;
        Log.e(TAG, "getAttendanceRegularizationData: empid: "+pref.getEmpId()
                +"\nclientid: "+pref.getEmpClintId()
                +"\nstartDate: "+startDate
                +"\nendDate: "+endDate
                +"\nSecurityCode: "+pref.getSecurityCode());

        /*Call<JsonObject> call = RetrofitClient
                .getInstance()
                .getApi()
                .GetAttendanceRegularizationData("1",pref.getEmpId(),pref.getEmpClintId(),startDate,endDate,pref.getSecurityCode());

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "GET_ATTENDANCE_REGULATION_DATA: "+new Gson().toJson(response.body()));

                try {
                    JSONObject object = new JSONObject(String.valueOf(response));
                    if (object.getBoolean("responseStatus") == true){
                        JSONArray jsonArray = object.getJSONArray("responseData");
                        blockLogList = new ArrayList<>();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "GET_ATTENDANCE_REGULATION_DATA_onFailure: "+t.getMessage());
            }
        });*/

        AndroidNetworking.get(AppData.url+"gcl_AttendanceRegularization")
                    .addQueryParameter("DbOperation", "1")
                    .addQueryParameter("empid", pref.getEmpId())
                    .addQueryParameter("clientid",pref.getEmpClintId())
                    .addQueryParameter("fromdate", prvMonth1stDate)
                    //.addQueryParameter("fromdate", startDate)
                    .addQueryParameter("todate", currentDate)
                    //.addQueryParameter("todate", endDate)
                    .addQueryParameter("SecurityCode", pref.getSecurityCode())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the successful response
                        Log.e(TAG, "Metso_Attendance_Regularization_RESPONSE: "+ new Gson().toJson(response));
                        try {
                            JSONObject object = new JSONObject(String.valueOf(response));
                            if (object.getBoolean("responseStatus") == true){
                                JSONArray jsonArray = object.getJSONArray("responseData");
                                blockLogList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject objectResponse = jsonArray.getJSONObject(i);
                                    //blockLogList.add(new BackLogAttendanceModel());
                                    String Remarks = objectResponse.optString("Remarks");
                                    blockLogList.add(new BackLogAttendanceModel(objectResponse.getString("Dates"),
                                            objectResponse.getString("Intime"),
                                            objectResponse.getString("Outtime"),
                                            false));
                                }
                                MetsoAttendanceAdapter metsoAttendanceAdapter = new MetsoAttendanceAdapter(MetsoAttendanceRegularizationActivity.this,metsoShiftList,blockLogList);
                                rvItem.setAdapter(metsoAttendanceAdapter);
                                llMain.setVisibility(View.VISIBLE);
                                progressDialog.cancel();
                                llLoader.setVisibility(View.GONE);
                            } else {
                                llMain.setVisibility(View.GONE);
                                llLoader.setVisibility(View.GONE);
                                llNodata.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        llLoader.setVisibility(View.GONE);
                        //progressDialog.cancel();
                        // Handle the error response
                        Toast.makeText(MetsoAttendanceRegularizationActivity.this, "Timeout for network problem, please try again.", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "SHIFT_RESPONSE_error: "+error);
                    }
                });
    }

    private void getMetsoShiftData() {
        progressDialog.show();
        AndroidNetworking.get(AppData.url+"Leave/Get_MetsoAttendanceData")
                .addQueryParameter("Mode", "2")
                .addQueryParameter("CompanyID", ClientID)
                .addQueryParameter("SecurityCode", "0000")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the successful response
                        Log.e(TAG, "SHIFT_RESPONSE: "+ new Gson().toJson(response));
                        try {
                            JSONObject object = new JSONObject(String.valueOf(response));
                            if (object.getBoolean("responseStatus") == true){
                                JSONArray jsonArray = object.getJSONArray("responseData");
                                metsoShiftList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject objectResponse = jsonArray.getJSONObject(i);
                                    MetsoShiftModel metsoLocationModel = new MetsoShiftModel(objectResponse.getLong("WorkingShiftID"),
                                            objectResponse.getString("Column1"));
                                    metsoShiftList.add(metsoLocationModel);
                                }

                                Log.e(TAG, "onResponse: SHIFT_LIST_SIZE: "+metsoShiftList.size());

                                getMetsoLocationData();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // Handle the error response
                        Log.e(TAG, "SHIFT_RESPONSE_error: "+error);
                    }
                });
    }

    private void getMetsoLocationData() {
        AndroidNetworking.get(AppData.url+"Leave/Get_MetsoAttendanceData")
                .addQueryParameter("Mode", "1")
                .addQueryParameter("CompanyID", ClientID)
                .addQueryParameter("SecurityCode", "0000")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject object = new JSONObject(String.valueOf(response));
                            if (object.getBoolean("responseStatus") == true){
                                JSONArray jsonArray = object.getJSONArray("responseData");
                                metsoLocationArrayList = new ArrayList<>();
                                metsoLocationArrayList.add(new MetsoLocationModel(0,"Select Location"));
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject objectResponse = jsonArray.getJSONObject(i);
                                    MetsoLocationModel metsoLocationModel = new MetsoLocationModel(objectResponse.getInt("Siteid"),
                                            (String) objectResponse.getString("SiteName"));
                                    metsoLocationArrayList.add(metsoLocationModel);
                                }
                                Log.e(TAG, "onResponse: SIZE: "+metsoLocationArrayList.size());
                                locationSpinnerAdapter = new LocationSpinnerAdapter(MetsoAttendanceRegularizationActivity.this,metsoLocationArrayList);
                                getApproverList();
                                progressDialog.cancel();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // Handle the error response
                        Log.e(TAG, "LOCATION_RESPONSE_error: "+error);
                        Toast.makeText(MetsoAttendanceRegularizationActivity.this, "Getting Some Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getApproverList() {
        AndroidNetworking.get(AppData.url+"Leave/Get_MetsoAttendanceData")
                .addQueryParameter("Mode", "3")
                .addQueryParameter("CompanyID", ClientID)
                .addQueryParameter("SecurityCode", "0000")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject object = new JSONObject(String.valueOf(response));
                            if (object.getBoolean("responseStatus") == true){
                                JSONArray jsonArray = object.getJSONArray("responseData");
                                approverList = new ArrayList<>();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject objectResponse = jsonArray.getJSONObject(i);
                                    approverList.add(new ApproverModel(objectResponse.getInt("UserId"),
                                            objectResponse.getString("UserName")));
                                }
                                Log.e(TAG, "onResponse: SIZE: "+metsoLocationArrayList.size());
                                approverAutoCompleteAdapter = new ApproverAutoCompleteAdapter(MetsoAttendanceRegularizationActivity.this,approverList);
                                progressDialog.cancel();
                                getPreviousMonth1stDate();
                                //llMain.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        //Handle the error response
                        Log.e(TAG, "SHIFT_RESPONSE_error: "+error);
                        Toast.makeText(MetsoAttendanceRegularizationActivity.this, "Getting Some Error", Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                    }
                });
    }

    public void updateItemStatus(int position,boolean status) {
        if (status)
            itemSelectCount++;
        else
            itemSelectCount--;
    }
}