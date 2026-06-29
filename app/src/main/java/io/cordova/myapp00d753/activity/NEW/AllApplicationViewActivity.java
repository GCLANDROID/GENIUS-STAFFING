package io.cordova.myapp00d753.activity.NEW;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import io.cordova.myapp00d753.activity.EmployeeDashBoardActivity;
import io.cordova.myapp00d753.activity.NEW.adapter.AllApplicationViewAdapter;
import io.cordova.myapp00d753.activity.NEW.model.AllApplicationViewModel;
import io.cordova.myapp00d753.activity.NewUserDashboardActivity;
import io.cordova.myapp00d753.adapter.CustomSpinnerAdapter;
import io.cordova.myapp00d753.module.SpinnerModel;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.ShowDialog;
import io.cordova.myapp00d753.utility.YearMonthUtil;

public class AllApplicationViewActivity extends AppCompatActivity {
    private static final String TAG = "AllApplicationViewActiv";
    ArrayList<SpinnerModel> dropDownList = new ArrayList<>();
    CustomSpinnerAdapter customSpinnerAdapter;
    Spinner spDropDown;
    RecyclerView rvItemView;
    TextView tvDropDown,tvMonth,tvYear;
    Pref pref;
    ProgressDialog progressDialog;
    ArrayList<String>yearList,monthList;
    Spinner spMonth,spYear;
    String selectedYear="",selectedMonth="",selectedAppliedType="";
    Button btnShow;
    ImageView imgHome,imgBack;
    LinearLayout llNoData,llLoading;
    ArrayList<AllApplicationViewModel> applicationViewList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_application_view);
        initView();
        onBtnClick();
    }

    private void initView() {
        pref = new Pref(this);
        spDropDown = findViewById(R.id.spDropDown);
        rvItemView = findViewById(R.id.rvItemView);
        tvDropDown = findViewById(R.id.tvDropDown);
        tvMonth = findViewById(R.id.tvMonth);
        tvYear = findViewById(R.id.tvYear);
        imgHome = findViewById(R.id.imgHome);
        imgBack = findViewById(R.id.imgBack);
        spMonth = findViewById(R.id.spMonth);
        spYear = findViewById(R.id.spYear);
        llNoData = findViewById(R.id.llNoData);
        llLoading = findViewById(R.id.llLoading);
        btnShow = findViewById(R.id.btnShow);
        setUpYearMonthSpinner();
        progressDialog = new ProgressDialog(AllApplicationViewActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ConsultantID", pref.getEmpConId());
            jsonObject.put("clientid", pref.getEmpClintId());
            jsonObject.put("empid", pref.getEmpId());
            jsonObject.put("Year", "");
            jsonObject.put("Month", "");
            jsonObject.put("Option", "1");
            jsonObject.put("SecurityCode", pref.getSecurityCode());
            getDropDownList(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setUpYearMonthSpinner() {
        // Month Spinner
        yearList = YearMonthUtil.getPreviousCurrentNextYearList();
        monthList = YearMonthUtil.getMonthNumberList();
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner_list,
                R.id.txtShiftTime,
                monthList
        );
        monthAdapter.setDropDownViewResource( R.layout.custom_spinner_list);
        spMonth.setDropDownWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        spMonth.setAdapter(monthAdapter);
        int currentMonthIndex = Calendar.getInstance().get(Calendar.MONTH);
        spMonth.setSelection(currentMonthIndex);
        // Year Spinner
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner_list,
                R.id.txtShiftTime,
                yearList
        );
        yearAdapter.setDropDownViewResource(R.layout.custom_spinner_list);
        spYear.setDropDownWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        spYear.setAdapter(yearAdapter);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int yearIndex = yearList.indexOf(String.valueOf(currentYear));
        if (yearIndex != -1) {
            spYear.setSelection(yearIndex);
        }
    }

    private void getDropDownList(JSONObject jsonObject) {
        progressDialog.show();
        AndroidNetworking.post(AppData.LAMS_DropDownData)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {
                            Log.e(TAG, "GET_DROP_DOWN: "+response.toString(4));
                            JSONObject object = new JSONObject(String.valueOf(response));
                            String Response_Message = object.optString("Response_Message");
                            String Response_Code = object.optString("Response_Code");
                            if (Response_Code.equals("101")) {
                                JSONObject Response_Data = object.getJSONObject("Response_Data");
                                JSONArray jsonArray = Response_Data.getJSONArray("Table");
                                Log.e(TAG, "onResponse: "+jsonArray);
                                dropDownList.add(new SpinnerModel("All","%"));
                                if (jsonArray.length() > 0){
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        String AdjType = obj.getString("AdjType");
                                        String AdjTypeName = obj.getString("AdjTypeName");
                                        dropDownList.add(new SpinnerModel(AdjTypeName,AdjType));
                                    }
                                    customSpinnerAdapter = new CustomSpinnerAdapter(AllApplicationViewActivity.this,dropDownList);
                                    spDropDown.setAdapter(customSpinnerAdapter);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Log.e(TAG, "GET_DROP_DOWN_error: "+anError.getErrorBody());
                    }
                });
    }

    private void onBtnClick() {
        tvDropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spDropDown.performClick();
            }
        });
        tvMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: m called");
                spMonth.performClick();
            }
        });
        tvYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: y called");
                spYear.performClick();
            }
        });
        spDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedAppliedType = dropDownList.get(i).getItemId();
                tvDropDown.setText(dropDownList.get(i).getItem());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedMonth = monthList.get(i);
                tvMonth.setText(monthList.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedYear = yearList.get(i);
                tvYear.setText(yearList.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AllApplicationViewActivity.this, NewUserDashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("ConsultantID", pref.getEmpConId());
                    jsonObject.put("clientid", pref.getEmpClintId());
                    jsonObject.put("empid", pref.getEmpId());
                    jsonObject.put("Year", selectedYear);
                    jsonObject.put("Month", selectedMonth);
                    jsonObject.put("AppliedType", selectedAppliedType);
                    jsonObject.put("Option", "1");
                    jsonObject.put("SecurityCode", pref.getSecurityCode());
                    getApplicationList(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getApplicationList(JSONObject jsonObject) {
        //progressDialog.show();
        llLoading.setVisibility(View.VISIBLE);
        rvItemView.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        AndroidNetworking.post(AppData.LAMS_AllApplicationView)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //progressDialog.dismiss();
                            Log.e(TAG, "ALL_APPLICATION_LIST: "+response.toString(4));
                            JSONObject object = new JSONObject(String.valueOf(response));
                            String Response_Message = object.optString("Response_Message");
                            String Response_Code = object.optString("Response_Code");
                            applicationViewList.clear();
                            if (Response_Code.equals("101")) {
                                JSONObject Response_Data = object.getJSONObject("Response_Data");
                                JSONArray jsonArray = Response_Data.getJSONArray("Table");
                                if (jsonArray.length()>0){
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        String AppliedType = obj.getString("Applied Type");
                                        String ApplicationDate = obj.getString("Application Date");
                                        String AppliedDate = obj.getString("Applied Date");
                                        String Intime = obj.getString("Intime");
                                        String Outtime = obj.getString("Outtime");
                                        String Reason = obj.getString("Reason");
                                        String RefDate = obj.getString("RefDate");
                                        String SelectedWorkPlace = obj.getString("Selected Work Place");
                                        String SelectedWorkingShift = obj.getString("Selected Working Shift");
                                        String SelectedApprover = obj.getString("Selected Approver");
                                        String CurrentApprovalStatus = obj.getString("Current Approval Status");
                                        String ApprovalDetails = obj.getString("Approval Details");
                                        String FinalApprovalStatus = obj.getString("Final Approval Status");
                                        int AllowDelete = obj.getInt("AllowDelete");
                                        String NotAllowDeleteReason = obj.getString("NotAllowDeleteReason");
                                        String AdjApplicationID = obj.getString("AdjApplicationID");
                                        String AdjType = obj.getString("AdjType");
                                        String RegApplicationMID = obj.getString("RegApplicationMID");
                                        String RegApplicationDID = obj.getString("RegApplicationDID");
                                        applicationViewList.add(new AllApplicationViewModel(
                                                AppliedType, ApplicationDate, AppliedDate, Intime, Outtime, Reason, RefDate,
                                                SelectedWorkPlace, SelectedWorkingShift, SelectedApprover, CurrentApprovalStatus, ApprovalDetails, FinalApprovalStatus,
                                                AllowDelete,NotAllowDeleteReason,AdjApplicationID,AdjType,RegApplicationMID,RegApplicationDID
                                        ));
                                    }
                                    rvItemView.setLayoutManager(new LinearLayoutManager(AllApplicationViewActivity.this));
                                    AllApplicationViewAdapter allApplicationAdapter = new AllApplicationViewAdapter(AllApplicationViewActivity.this,applicationViewList);
                                    rvItemView.setAdapter(allApplicationAdapter);
                                    llNoData.setVisibility(View.GONE);
                                    rvItemView.setVisibility(View.VISIBLE);
                                    llLoading.setVisibility(View.GONE);
                                }
                            } else {
                                llNoData.setVisibility(View.VISIBLE);
                                rvItemView.setVisibility(View.GONE);
                                llLoading.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        //progressDialog.dismiss();
                        Log.e(TAG, "ALL_APPLICATION_LIST_error: "+anError.getErrorBody());
                        llNoData.setVisibility(View.VISIBLE);
                        rvItemView.setVisibility(View.GONE);
                        llLoading.setVisibility(View.GONE);
                    }
                });
    }


    public void Delete_H_OH_WO(String AdjApplicationID){
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ConsultantID", pref.getEmpConId());
            jsonObject.put("ClientID", pref.getEmpClintId());
            jsonObject.put("EmployeeID", pref.getEmpId());
            jsonObject.put("AdjApplicationID", AdjApplicationID);
            jsonObject.put("SecurityCode", pref.getSecurityCode());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(AppData.LAMS_Delete_H_OH_WO)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "Delete_H_OH_WO: "+response.toString(4));
                            progressDialog.dismiss();
                            JSONObject job1 = response;
                            int Response_Code = job1.optInt("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code == 101) {
                                ShowDialog.showSuccessDialog(AllApplicationViewActivity.this, Response_Message, new ShowDialog.ResultListener() {
                                    @Override
                                    public void onSuccess() {
                                        ShowDialog.onDismiss();
                                        btnShow.performClick();
                                    }
                                });
                            } else {
                                ShowDialog.showErrorDialog(AllApplicationViewActivity.this,Response_Message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Log.e(TAG, "Delete_H_OH_WO_error: "+anError.getErrorBody());
                    }
                });
    }

    public void Delete_OD_CO_WFH(String AppliedType,String AdjApplicationID){
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("EmployeeID",pref.getEmpId());
            jsonObject.put("AppliedType",AppliedType);
            jsonObject.put("StartDate","");
            jsonObject.put("EndDate","");
            jsonObject.put("AdjApplicationID",AdjApplicationID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(AppData.LAMS_Delete_OD_CO_WFH)
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
                            Log.e(TAG, "Delete_OD_CO_WFH: "+response.toString(4) );
                            JSONObject job1 = response;
                            int Response_Code = job1.optInt("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code == 101) {
                                ShowDialog.showSuccessDialog(AllApplicationViewActivity.this, Response_Message, new ShowDialog.ResultListener() {
                                    @Override
                                    public void onSuccess() {
                                        ShowDialog.onDismiss();
                                        btnShow.performClick();
                                    }
                                });
                            } else {
                                ShowDialog.showErrorDialog(AllApplicationViewActivity.this,Response_Message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Log.e(TAG, "Delete_OD_CO_WFH_error: "+anError.getErrorBody());
                    }
                });
    }

    public void DeleteRegularisation(String AttendanceID,String RegAppDID,String RegAppMID){
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("EmployeeID", pref.getEmpId());
            jsonObject.put("ConsultantID", pref.getEmpConId());
            jsonObject.put("ClientID", pref.getEmpClintId());
            jsonObject.put("AttendanceID", AttendanceID);
            jsonObject.put("RegAppDID", RegAppDID);
            jsonObject.put("RegAppMID", RegAppMID);
            jsonObject.put("StartDate", "");
            jsonObject.put("EndDate", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(AppData.LAMS_Delete_Regularisation)
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
                            Log.e(TAG, "Delete_Regularisation: "+response.toString(4));
                            JSONObject job1 = response;
                            int Response_Code = job1.optInt("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code == 101) {
                                ShowDialog.showSuccessDialog(AllApplicationViewActivity.this, Response_Message, new ShowDialog.ResultListener() {
                                    @Override
                                    public void onSuccess() {
                                        ShowDialog.onDismiss();
                                        btnShow.performClick();
                                    }
                                });
                            } else {
                                ShowDialog.showErrorDialog(AllApplicationViewActivity.this,Response_Message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Log.e(TAG, "Delete_Regularisation_error: "+anError.getErrorBody());
                    }
                });
    }

}
