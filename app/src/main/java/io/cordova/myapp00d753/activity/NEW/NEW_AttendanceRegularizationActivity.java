package io.cordova.myapp00d753.activity.NEW;

import static io.cordova.myapp00d753.activity.attendance.ProtectorGambleAttendanceActivity.SKF_PUNE_CLIENT_OFFICE_ID;

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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.Retrofit.RetrofitClient;
import io.cordova.myapp00d753.activity.EmployeeDashBoardActivity;
import io.cordova.myapp00d753.activity.NEW.adapter.NEW_BacklogAdapter;
import io.cordova.myapp00d753.activity.NEW.model.LocationModel;
import io.cordova.myapp00d753.activity.NEW.model.NEW_BackLogAttendanceModel;
import io.cordova.myapp00d753.activity.SKF.adapter.SKF_BacklogAdapter;
import io.cordova.myapp00d753.activity.metso.adapter.LocationSpinnerAdapter;
import io.cordova.myapp00d753.activity.metso.adapter.ShiftSpinnerAdapter;
import io.cordova.myapp00d753.activity.metso.model.LocationSpinnerModel;
import io.cordova.myapp00d753.activity.metso.model.MetsoLocationModel;
import io.cordova.myapp00d753.activity.metso.model.MetsoShiftModel;
import io.cordova.myapp00d753.module.BackLogAttendanceModel;
import io.cordova.myapp00d753.module.SpineerItemModel;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.RequiredListClass;
import io.cordova.myapp00d753.utility.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NEW_AttendanceRegularizationActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SKF_AttendanceRegulariz";
    RecyclerView rvItem;
    LinearLayout btnSubmit;
    ImageView imgBack,imgLike,imgHome;
    LinearLayout lnStartDate,lnEndDate,llMain,llLoader,llNodata,llWarning;
    TextView tvStartDate,tvEndDate;
    Button btnShow;
    Pref pref;
    ArrayList<LocationModel> locationArrayList;
    LocationSpinnerAdapter locationSpinnerAdapter;
    String startDate="",endDate="",ClientID="",MasterID="";
    String prvMonth1stDate ="",currentDate="";
    ArrayList<NEW_BackLogAttendanceModel> blockLogList;
    LinearLayout llTick;
    public boolean isSelectedAll = false;
    NEW_BacklogAdapter skfBacklogAdapter;
    int itemSelectCount=0;
    private Dialog shiftAndLocationDialog;
    String Siteid = "";
    ProgressDialog progressDialog;
    AlertDialog alerDialog1;
    AlertDialog al1;
    ArrayList<String> dayTypeArray;
    TextView txtRegularisationCount;
    LinearLayout llRegularisationCount;
    String isDayTypeSelectionRequired="",isShiftSelectionRequired="",isLocationSelectionRequired="",isApproverRequired="";
    ArrayList<MetsoShiftModel> metsoShiftList;
    ArrayList<SpineerItemModel> supervisorList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skf_attendance_regularization);
        initView();
    }

    private void initView() {
        getRequiredFlag();
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
        imgHome = findViewById(R.id.imgHome);
        llRegularisationCount = findViewById(R.id.llRegularisationCount);
        imgLike = findViewById(R.id.imgLike);
        txtRegularisationCount = findViewById(R.id.txtRegularisationCount);
        btnSubmit.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        lnStartDate.setOnClickListener(this);
        lnEndDate.setOnClickListener(this);
        btnShow.setOnClickListener(this);
        llTick.setOnClickListener(this);
        imgHome.setOnClickListener(this);
        rvItem.setLayoutManager(new LinearLayoutManager(NEW_AttendanceRegularizationActivity.this));
        pref = new Pref(NEW_AttendanceRegularizationActivity.this);
        progressDialog = new ProgressDialog(NEW_AttendanceRegularizationActivity.this);
        progressDialog.setCancelable(false);
        if (pref.getEmpClintId().equals(io.cordova.myapp00d753.utility.ClientID.SKF_CLIENT_ID)
                || pref.getEmpClintId().equals(io.cordova.myapp00d753.utility.ClientID.SKF_ITS)
                || pref.getEmpClintId().equals(io.cordova.myapp00d753.utility.ClientID.SKF_MSP)
                || pref.getEmpClintId().equals(io.cordova.myapp00d753.utility.ClientID.SKF_ENGINEERING_LUB)
                || pref.getEmpClintId().equals(io.cordova.myapp00d753.utility.ClientID.SKF_INDUSTRIAL)){
            llRegularisationCount.setVisibility(View.VISIBLE);
        } else {
            llRegularisationCount.setVisibility(View.GONE);
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("clientid",pref.getEmpClintId());
            jsonObject.put("BranchID","0");
            jsonObject.put("SecurityCode", pref.getSecurityCode());
            getSkfDayTypeList(jsonObject);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        getRequiredList();
    }

    private void getRequiredFlag() {
        String WithDayTypeSelection = getIntent().getStringExtra("AttendanceRegularisationWithDayTypeSelection");
        String WorkPlaceSelection = getIntent().getStringExtra("AttendanceRegularisationWithWorkPlaceSelection");
        String WorkingShiftSelection = getIntent().getStringExtra("AttendanceRegularisationWithWorkingShiftSelection");
        String HierarchySelection = getIntent().getStringExtra("AttendanceRegularisationWithHierarchySelection");
        Log.e(TAG, "getRequiredFlag: \nWithDayTypeSelection: "+WithDayTypeSelection
                +"\nWorkPlaceSelection: "+WorkPlaceSelection
                +"\nWorkingShiftSelection: "+WorkingShiftSelection
                +"\nHierarchySelection: "+HierarchySelection);
        String[] WithDayTypeSelectionArr = WithDayTypeSelection.split("_");
        String[] WorkPlaceSelectionArr = WorkPlaceSelection.split("_");
        String[] WorkingShiftSelectionArr = WorkingShiftSelection.split("_");
        String[] HierarchySelectionArr = HierarchySelection.split("_");
        isDayTypeSelectionRequired = WithDayTypeSelectionArr[0];
        isShiftSelectionRequired = WorkingShiftSelectionArr[0];
        //isShiftSelectionRequired = "1";
        isLocationSelectionRequired = WorkPlaceSelectionArr[0];
        isApproverRequired = HierarchySelectionArr[0];
        //isApproverRequired = "1";
        Log.e(TAG, "Flag: \nisDayTypeSelectionRequired: "+isDayTypeSelectionRequired
                +"\nisShiftSelectionRequired: "+isShiftSelectionRequired
                +"\nisLocationSelectionRequired: "+isLocationSelectionRequired
                +"\nisApproverRequired: "+isApproverRequired);
    }

    private void getRequiredList() {
        progressDialog.show();
        try {
            metsoShiftList = RequiredListClass.getShiftData(pref.getShiftList());
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            locationArrayList = RequiredListClass.getLocationData(pref.getLocationList());
        } catch (Exception e){
            e.printStackTrace();
        }
        try {
            supervisorList = RequiredListClass.getApproverData(pref.getApproverList());
        } catch (Exception e){
            e.printStackTrace();
        }
        progressDialog.dismiss();
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
                            /*obj.put("Option","1");
                            obj.put("ConsultantID",pref.getEmpConId());
                            obj.put("empid",pref.getEmpId());
                            obj.put("clientid",pref.getEmpClintId());
                            obj.put("fromdate",startDate);
                            obj.put("todate",endDate);
                            obj.put("SecurityCode",pref.getSecurityCode());*/

                            obj.put("ConsultantID",pref.getEmpConId());
                            obj.put("clientid", pref.getEmpClintId());
                            obj.put("empid",pref.getEmpId());
                            obj.put("fromdate",startDate);
                            obj.put("todate",endDate);
                            obj.put("dt","");
                            obj.put("SecurityCode",pref.getSecurityCode());
                            getBackLogData(obj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(NEW_AttendanceRegularizationActivity.this,"Please Enter End Date",Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(NEW_AttendanceRegularizationActivity.this,"Please Enter Start Date",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.llTick:
                selectAllOperation();
                break;
            case R.id.imgHome:
                Intent intent = new Intent(NEW_AttendanceRegularizationActivity.this, EmployeeDashBoardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
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
        JSONArray regularizationDayArray = new JSONArray();
        try{

            for (int i = 0; i < blockLogList.size(); i++) {
                Log.e(TAG, "submitOperation: Remarks: "+blockLogList.get(i).getRemarks());
               /* if (blockLogList.get(i).isSelected()){
                    //Log.e(TAG, "submitOperation: "+i);
                    if (regularizationSubmitString.isEmpty()){
                        regularizationSubmitString = pref.getEmpId()+"_"+pref.getEmpClintId()+"_"+blockLogList.get(i).getDate()+"_"+blockLogList.get(i).getInTime()+"_"+blockLogList.get(i).getOutTime()+"_"+blockLogList.get(i).getRemarks()+"_"+blockLogList.get(i).getLocationID()+"_"+blockLogList.get(i).getShiftID()+"_"+blockLogList.get(i).getApproverID()+"_"+blockLogList.get(i).getDayType();
                    } else {
                        regularizationSubmitString += ","+pref.getEmpId()+"_"+pref.getEmpClintId()+"_"+blockLogList.get(i).getDate()+"_"+blockLogList.get(i).getInTime()+"_"+blockLogList.get(i).getOutTime()+"_"+blockLogList.get(i).getRemarks()+"_"+blockLogList.get(i).getLocationID()+"_"+blockLogList.get(i).getShift()+"_"+blockLogList.get(i).getApproverID()+"_"+blockLogList.get(i).getDayType();
                    }
                }*/
                if (blockLogList.get(i).isSelected()){
                    JSONObject jsonObject = new JSONObject();

                    jsonObject.put("Date", blockLogList.get(i).getDate());
                    jsonObject.put("Intime", blockLogList.get(i).getInTime());
                    jsonObject.put("Outtime", blockLogList.get(i).getOutTime());
                    jsonObject.put("Daytype", blockLogList.get(i).getDayType());
                    jsonObject.put("Remarks", blockLogList.get(i).getRemarks2());
                    jsonObject.put("LocationId", blockLogList.get(i).getLocationID());
                    jsonObject.put("ShiftId", blockLogList.get(i).getShiftID());
                    jsonObject.put("SupervisorId", blockLogList.get(i).getApproverID());

                    regularizationDayArray.put(jsonObject);
                }

            }
        } catch (JSONException e){
            e.printStackTrace();
        }



        Log.e(TAG, "submitOperation: "+regularizationSubmitString);

        JSONObject obj=new JSONObject();
        try {
            //obj.put("AttnDate", regularizationSubmitString);
            obj.put("AttnDate", regularizationDayArray);
            obj.put("ConsultantID",pref.getEmpConId());
            obj.put("empid",pref.getEmpId());
            obj.put("clientid",pref.getEmpClintId());
            obj.put("fromdate",startDate);
            obj.put("todate",endDate);
            obj.put("SecurityCode", pref.getSecurityCode());
            Log.e(TAG, "BACKLOG_SAVE_INPUT: "+obj);
            regularizationSave_New_ApiCall(obj);
            /*if (pref.getEmpClintId().equals(io.cordova.myapp00d753.utility.ClientID.SKF_CLIENT_ID)
                    || pref.getEmpClintId().equals(io.cordova.myapp00d753.utility.ClientID.SKF_ITS)
                    || pref.getEmpClintId().equals(io.cordova.myapp00d753.utility.ClientID.SKF_MSP)
                    || pref.getEmpClintId().equals(io.cordova.myapp00d753.utility.ClientID.SKF_ENGINEERING_LUB)
                    || pref.getEmpClintId().equals(io.cordova.myapp00d753.utility.ClientID.SKF_INDUSTRIAL)){
                regularizationSaveApiCall(obj);
            } else {
                regularizationSave_New_ApiCall(obj);
            }*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void regularizationSave_New_ApiCall(JSONObject jsonObject) {
        AndroidNetworking.post(AppData.LAMS_SaveAttendanceRegularization)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "NEW_BACKLOG_SAVE: "+response.toString(4));
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


                                        NEW_BackLogAttendanceModel blockModule = new NEW_BackLogAttendanceModel(AttDate, InTime, OutTime);
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
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Log.e(TAG, "NEW_BACKLOG_SAVE_error: "+anError.getErrorBody());
                    }
                });
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


                                        NEW_BackLogAttendanceModel blockModule = new NEW_BackLogAttendanceModel(AttDate, InTime, OutTime);
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
        Log.e(TAG, "getBackLogData: INPUT" + jsonObject);
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);
        //blockLogList.clear();
        //AndroidNetworking.post(AppData.GET_ATTENDANCE_REGULARIZATION_LOCAL_IP)
        AndroidNetworking.post(AppData.LAMS_AttnRegularisationDate)
                .addJSONObjectBody(jsonObject)
                //.addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "NEW_BACK_LOG_DATA: " + response.toString(4));
                            blockLogList = new ArrayList<>();

                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                btnSubmit.setVisibility(View.VISIBLE);
                                JSONObject Response_Data = job1.optJSONObject("Response_Data");
                                JSONArray Table4 = Response_Data.optJSONArray("Table4"); //TODO: DATE LIST
                                Log.e(TAG, "Table4: " + Table4);
                                JSONArray Table5 = Response_Data.optJSONArray("Table5");
                                Log.e(TAG, "Table5: " + Table5);

                                JSONObject objectRequestCount = Table5.getJSONObject(0);
                                txtRegularisationCount.setText(String.valueOf(objectRequestCount.getInt("RegularisationRequestCount")));

                                if (objectRequestCount.getInt("RegularisationRequestCount") < 4) {
                                    if (Table4.length() > 0) {
                                        for (int i = 0; i < Table4.length(); i++) {
                                            JSONObject obj = Table4.getJSONObject(i);
                                            String AttDate = obj.optString("DATES");
                                            String InTime = obj.optString("Intime");
                                            String OutTime = obj.optString("Outtime");
                                            String Daytype = obj.optString("Daytype");
                                            //String Remarks=obj.optString("Remark");
                                            //Log.e(TAG, "Remarks: "+obj.optString("Remark") );

                                            //Log.e(TAG, "Remarks: "+Remarks);

                                            NEW_BackLogAttendanceModel blockModule = new NEW_BackLogAttendanceModel(AttDate, InTime, OutTime);
                                            blockModule.setDayType(Daytype);
                                            //blockModule.setRemarks(Remarks);
                                            blockLogList.add(blockModule);
                                            //item1.add(pref.getEmpId()+"_"+pref.getEmpClintId()+"_"+AttDate + "_" + InTime + "_" + OutTime + "_"+Remarks);
                                        }
                                        llLoader.setVisibility(View.GONE);
                                        llMain.setVisibility(View.VISIBLE);
                                        llNodata.setVisibility(View.GONE);
                                        llWarning.setVisibility(View.GONE);
                                        skfBacklogAdapter = new NEW_BacklogAdapter(blockLogList, dayTypeArray, NEW_AttendanceRegularizationActivity.this,
                                                metsoShiftList, locationArrayList, supervisorList);
                                        skfBacklogAdapter.setIsApproverRequired(isApproverRequired);
                                        skfBacklogAdapter.setIsDayTypeSelectionRequired(isDayTypeSelectionRequired);
                                        skfBacklogAdapter.setIsShiftSelectionRequired(isShiftSelectionRequired);
                                        skfBacklogAdapter.setIsLocationSelectionRequired(isLocationSelectionRequired);
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
                        Log.e(TAG, "SKF_BACK_LOG_DATA_error: " + anError.getErrorBody());
                        llLoader.setVisibility(View.VISIBLE);
                        llMain.setVisibility(View.GONE);
                        llNodata.setVisibility(View.GONE);
                    }
                });
    }
    /*private void getBackLogData(JSONObject jsonObject) {
        Log.e(TAG, "getBackLogData: INPUT"+jsonObject);
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);
        //blockLogList.clear();
        //AndroidNetworking.post(AppData.GET_ATTENDANCE_REGULARIZATION_LOCAL_IP)
        AndroidNetworking.post(AppData.LAMS_AttnRegularisationDate)
                .addJSONObjectBody(jsonObject)
                //.addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "NEW_BACK_LOG_DATA: "+response.toString(4));
                            blockLogList = new ArrayList<>();

                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                btnSubmit.setVisibility(View.VISIBLE);
                                JSONArray Response_Data = job1.optJSONArray("Response_Data");
                                JSONObject Response_Data_obj = new JSONObject(Response_Data);
                                String Table = Response_Data_obj.optString("Table");
                                Log.e(TAG, "Table: "+Table);
                                String Table1 = Response_Data_obj.optString("Table1");
                                Log.e(TAG, "Table1: "+Table1);
                               *//* JSONArray Table1Array = new JSONArray(Table1);
                                JSONObject table1Obj = Table1Array.getJSONObject(0);
                                txtRegularisationCount.setText(String.valueOf(table1Obj.getInt("RegularisationRequestCount")));
                                if(table1Obj.getInt("RegularisationRequestCount") < 4) {*//*
                                    JSONArray jsonArray = new JSONArray(Table);
                                    //if (jsonArray.length() > 0){
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject obj = jsonArray.getJSONObject(i);
                                            String AttDate = obj.optString("Dates");
                                            String InTime = obj.optString("Intime");
                                            String OutTime = obj.optString("Outtime");
                                            String Daytype=obj.optString("Daytype");
                                            String Remarks=obj.optString("Remark");
                                            //Log.e(TAG, "Remarks: "+obj.optString("Remark") );

                                            //Log.e(TAG, "Remarks: "+Remarks);

                                            NEW_BackLogAttendanceModel blockModule = new NEW_BackLogAttendanceModel(AttDate, InTime, OutTime);
                                            blockModule.setDayType(Daytype);
                                            blockModule.setRemarks(Remarks);
                                            blockLogList.add(blockModule);
                                            //item1.add(pref.getEmpId()+"_"+pref.getEmpClintId()+"_"+AttDate + "_" + InTime + "_" + OutTime + "_"+Remarks);
                                        }
                                        llLoader.setVisibility(View.GONE);
                                        llMain.setVisibility(View.VISIBLE);
                                        llNodata.setVisibility(View.GONE);
                                        llWarning.setVisibility(View.GONE);
                                        skfBacklogAdapter = new NEW_BacklogAdapter(blockLogList,dayTypeArray, NEW_AttendanceRegularizationActivity.this,
                                                metsoShiftList,locationArrayList,supervisorList);
                                        skfBacklogAdapter.setIsApproverRequired(isApproverRequired);
                                        skfBacklogAdapter.setIsDayTypeSelectionRequired(isDayTypeSelectionRequired);
                                        skfBacklogAdapter.setIsShiftSelectionRequired(isShiftSelectionRequired);
                                        skfBacklogAdapter.setIsLocationSelectionRequired(isLocationSelectionRequired);
                                        rvItem.setAdapter(skfBacklogAdapter);
                                    *//*} else {
                                        llLoader.setVisibility(View.GONE);
                                        llMain.setVisibility(View.GONE);
                                        llNodata.setVisibility(View.VISIBLE);
                                        llWarning.setVisibility(View.GONE);
                                    }*//*
                               *//* } else {
                                    //TODO: Exceed Request Limit message;
                                    llLoader.setVisibility(View.GONE);
                                    llMain.setVisibility(View.GONE);
                                    llNodata.setVisibility(View.GONE);
                                    llWarning.setVisibility(View.VISIBLE);
                                }*//*
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
    }*/





    private void showStartDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(NEW_AttendanceRegularizationActivity.this,
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

        DatePickerDialog datePickerDialog = new DatePickerDialog(NEW_AttendanceRegularizationActivity.this,
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
        shiftAndLocationDialog = new Dialog(NEW_AttendanceRegularizationActivity.this);
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
        //TextView textView = shiftAndLocationDialog.findViewById(R.id.textView);
        //textView.setText("Select location");

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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NEW_AttendanceRegularizationActivity.this, R.style.CustomDialogNew);
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
                    obj.put("Option","1");
                    obj.put("ConsultantID",pref.getEmpConId());
                    obj.put("UserID",pref.getEmpId());
                    obj.put("ClientID",pref.getEmpClintId());
                    obj.put("FromDate",startDate);
                    obj.put("ToDate",endDate);
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NEW_AttendanceRegularizationActivity.this, R.style.CustomDialogNew);
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