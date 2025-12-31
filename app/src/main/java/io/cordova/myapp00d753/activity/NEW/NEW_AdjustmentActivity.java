package io.cordova.myapp00d753.activity.NEW;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
import io.cordova.myapp00d753.activity.attendance.AttenDanceDashboardActivity;
import io.cordova.myapp00d753.activity.metso.MetsoNewReimbursementClaimActivity;
import io.cordova.myapp00d753.activity.metso.adapter.SupervisorFilterAdapter;
import io.cordova.myapp00d753.adapter.CustomSpinnerAdapter;
import io.cordova.myapp00d753.module.SpineerItemModel;
import io.cordova.myapp00d753.module.SpinnerModel;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.RequiredListClass;
import io.cordova.myapp00d753.utility.ShowDialog;
import io.cordova.myapp00d753.utility.Util;

public class NEW_AdjustmentActivity extends AppCompatActivity {
    private static final String TAG = "NEW_AdjustmentActivity";
    ImageView imgBack,imgHome;
    Spinner spAdjustment,spMode;
    ProgressDialog progressDialog;
    ArrayList<SpinnerModel> dropDownList = new ArrayList<>();
    CustomSpinnerAdapter customSpinnerAdapter;
    Pref pref;
    TextView tvDropDown;
    LinearLayout llStartEndDate,llEffectiveDate,llMode,llInOutTime, llEndDate,llEndTime,llInTime,llOutTime,llApprover,llLabelApprover;
    TextView tvStartDateName,tvEffectiveDate,tvEndDate,tvStrtDate,tvInTime,tvOutTime,tvDayMode,tvApprover;
    String effectiveDate="",startDate = "",endDate = "",dayType="",dayTypeId="",intime="",outtime="";
    ArrayList<SpinnerModel> dayModeList = new ArrayList<>();
    Button btnSave;
    String applicationComponent = "";
    String applicationComponentID = "";
    EditText etReason;
    String isRequiredApproverCO ="", isRequiredApproverWFH ="", isRequiredApproverOD ="";
    Dialog searchWbsCodeDialog;
    ArrayList<SpineerItemModel> supervisorList = new ArrayList<>();
    String SupervisorID = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_adjustment);
        initView();
        btnClick();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ConsultantID", pref.getEmpConId());
            jsonObject.put("clientid", pref.getEmpClintId());
            jsonObject.put("empid", pref.getEmpId());
            jsonObject.put("Year", "");
            jsonObject.put("Month", "");
            jsonObject.put("Option", "2");
            jsonObject.put("SecurityCode", pref.getSecurityCode());
            getDropDownList(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //loadAdjustmentFragment();
    }

    private void initView() {
        pref = new Pref(this);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        tvDropDown=(TextView) findViewById(R.id.tvDropDown);
        spAdjustment = (Spinner)findViewById(R.id.spAdjustment);
        spMode = (Spinner)findViewById(R.id.spMode);
        llEffectiveDate = (LinearLayout) findViewById(R.id.llEffectiveDate);
        llStartEndDate = (LinearLayout) findViewById(R.id.llStartEndDate);
        llMode = (LinearLayout) findViewById(R.id.llMode);
        llEndDate = (LinearLayout) findViewById(R.id.llEndDate);
        llEndTime = (LinearLayout) findViewById(R.id.llEndTime);
        llInOutTime = (LinearLayout) findViewById(R.id.llInOutTime);
        llApprover = (LinearLayout) findViewById(R.id.llApprover);
        llLabelApprover = (LinearLayout) findViewById(R.id.llLabelApprover);
        tvStartDateName = (TextView)findViewById(R.id.tvStartDateName);
        tvEffectiveDate = (TextView)findViewById(R.id.tvEffectiveDate);
        tvEndDate = (TextView)findViewById(R.id.tvEndDate);
        tvStrtDate = (TextView)findViewById(R.id.tvStrtDate);
        tvInTime = (TextView)findViewById(R.id.tvInTime);
        tvOutTime = (TextView)findViewById(R.id.tvOutTime);
        tvDayMode = (TextView)findViewById(R.id.tvDayMode);
        llInTime = (LinearLayout) findViewById(R.id.llInTime);
        llOutTime = (LinearLayout) findViewById(R.id.llOutTime);
        tvApprover = (TextView) findViewById(R.id.tvApprover);
        etReason = findViewById(R.id.etReason);
        btnSave = findViewById(R.id.btnSave);
        getRequiredStatus();
        progressDialog = new ProgressDialog(NEW_AdjustmentActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        dayModeList.add(new SpinnerModel("Full Day","FD"));
        dayModeList.add(new SpinnerModel("First Half Day","FHD"));
        dayModeList.add(new SpinnerModel("Second Half Day","SHD"));
        /*ArrayAdapter<String> spinnerModeArrayAdapter = new ArrayAdapter<String>
                (NEW_AdjustmentActivity.this, R.layout.custom_spinner_list,
                        R.id.txtShiftTime,
                        dayModeList);*/ //selected item will look like a spinner set from XML
        //spinnerModeArrayAdapter.setDropDownViewResource(R.layout.custom_spinner_list);
        CustomSpinnerAdapter spinnerModeArrayAdapter = new CustomSpinnerAdapter(this,dayModeList);
        spMode.setAdapter(spinnerModeArrayAdapter);
        try {
            getRequiredList();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void getRequiredList() throws JSONException {
        progressDialog.show();
        supervisorList = RequiredListClass.getApproverData(pref.getApproverList());
        progressDialog.dismiss();
    }

    private void getRequiredStatus() {
        String AttnAdjCOAppliocationWithMarkedHierarchy = getIntent().getStringExtra("AttnAdjCOAppliocationWithMarkedHierarchy");
        String[] AttnAdjCOHierarchy = AttnAdjCOAppliocationWithMarkedHierarchy.split("_");
        isRequiredApproverCO = AttnAdjCOHierarchy[0];
        String AttnAdjWFHAppliocationWithMarkedHierarchy = getIntent().getStringExtra("AttnAdjWFHAppliocationWithMarkedHierarchy");
        String[] AttnAdjWFHHierarchy = AttnAdjWFHAppliocationWithMarkedHierarchy.split("_");
        isRequiredApproverWFH = AttnAdjWFHHierarchy[0];
        String AttnAdjODMarkHierarchyAtEntry = getIntent().getStringExtra("AttnAdjODMarkHierarchyAtEntry");
        String[] AttnAdjODMarkHierarchy = AttnAdjODMarkHierarchyAtEntry.split("_");
        isRequiredApproverOD = AttnAdjODMarkHierarchy[0];

        if (isRequiredApproverCO.equals("1") || isRequiredApproverOD.equals("1") || isRequiredApproverWFH.equals("1")){
            llLabelApprover.setVisibility(View.VISIBLE);
        } else {
            llLabelApprover.setVisibility(View.GONE);
        }
    }

    private void btnClick() {
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(NEW_AdjustmentActivity.this, EmployeeDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tvDropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spAdjustment.performClick();
            }
        });
        tvEffectiveDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEffectiveDate(tvEffectiveDate);
            }
        });
        tvStrtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStartDate(tvStrtDate);
            }
        });
        tvEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEndDate(tvEndDate);
            }
        });
        llInTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(NEW_AdjustmentActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        tvInTime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        llOutTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(NEW_AdjustmentActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        tvOutTime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        tvDayMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spMode.performClick();
            }
        });
        tvApprover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSupervisorPopUp(tvApprover);
            }
        });
        spAdjustment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                applicationComponent = dropDownList.get(i).getItem();
                applicationComponentID = dropDownList.get(i).getItemId();
                Log.e(TAG, "onItemSelected: "+applicationComponentID);
                tvDropDown.setText(applicationComponent);
                if (applicationComponentID.equalsIgnoreCase("OD")) {
                    tvStartDateName.setText("Start Date");
                    llStartEndDate.setVisibility(View.VISIBLE);
                    llEndTime.setVisibility(View.GONE);
                    llEffectiveDate.setVisibility(View.GONE);
                    llMode.setVisibility(View.GONE);
                    llInOutTime.setVisibility(View.VISIBLE);
                } else if (applicationComponentID.equalsIgnoreCase("WFH")) {
                    llStartEndDate.setVisibility(View.VISIBLE);
                    llEndTime.setVisibility(View.GONE);
                    llEffectiveDate.setVisibility(View.GONE);
                    llMode.setVisibility(View.GONE);
                    llInOutTime.setVisibility(View.VISIBLE);
                    /*llStartEndDate.setVisibility(View.VISIBLE);
                    llEffectiveDate.setVisibility(View.GONE);
                    llMode.setVisibility(View.GONE);
                    llInOutTime.setVisibility(View.VISIBLE);*/
                } else if (applicationComponentID.equalsIgnoreCase("CO")) {
                    tvStartDateName.setText("Referral date");
                    llStartEndDate.setVisibility(View.VISIBLE);
                    llEffectiveDate.setVisibility(View.VISIBLE);
                    llMode.setVisibility(View.VISIBLE);
                    llInOutTime.setVisibility(View.GONE);
                    llEndTime.setVisibility(View.GONE);
                    //tvStartDateName.setText(Html.fromHtml("Referal Date " + next));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dayType = dayModeList.get(i).getItem();
                dayTypeId = dayModeList.get(i).getItemId();
                tvDayMode.setText(dayModeList.get(i).getItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llLabelApprover.getVisibility() == View.VISIBLE && SupervisorID.isEmpty()){
                    //openSupervisorPopUp(tvApprover);
                    Toast.makeText(NEW_AdjustmentActivity.this, "Please select approver", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (applicationComponentID.equalsIgnoreCase("OD")) {
                    intime = tvInTime.getText().toString().trim();
                    outtime = tvOutTime.getText().toString();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("EmployeeID", pref.getEmpId());
                        jsonObject.put("AppliedType", applicationComponentID);
                        jsonObject.put("AppliedDate", startDate);
                        jsonObject.put("EndDate", startDate);
                        jsonObject.put("Remarks", etReason.getText().toString().trim());
                        jsonObject.put("StartTime", intime);
                        jsonObject.put("EndTime", intime);
                        jsonObject.put("clinetname", "");
                        jsonObject.put("clinetphn", "");
                        jsonObject.put("Oddaytype", "");
                        jsonObject.put("OtMin", "");
                        jsonObject.put("LtMin", "");
                        jsonObject.put("refdate", "");
                        jsonObject.put("Approverid", SupervisorID);
                        jsonObject.put("SecurityCode", pref.getSecurityCode());
                        saveAdjustmentApplication(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (applicationComponentID.equalsIgnoreCase("CO")) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("EmployeeID", pref.getEmpId());
                        jsonObject.put("AppliedType", applicationComponentID);
                        jsonObject.put("AppliedDate", effectiveDate);
                        jsonObject.put("EndDate", effectiveDate);
                        jsonObject.put("Remarks", etReason.getText().toString().trim());
                        jsonObject.put("StartTime", "");
                        jsonObject.put("EndTime", "");
                        jsonObject.put("clinetname", "");
                        jsonObject.put("clinetphn", "");
                        jsonObject.put("Oddaytype", "");
                        jsonObject.put("OtMin", "");
                        jsonObject.put("LtMin", "");
                        jsonObject.put("refdate", startDate);
                        jsonObject.put("Approverid", SupervisorID);
                        jsonObject.put("SecurityCode", pref.getSecurityCode());
                        saveAdjustmentApplication(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (applicationComponentID.equalsIgnoreCase("WFH")){
                    intime = tvInTime.getText().toString().trim();
                    outtime = tvOutTime.getText().toString();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("EmployeeID", pref.getEmpId());
                        jsonObject.put("AppliedType", applicationComponentID);
                        jsonObject.put("AppliedDate", startDate);
                        jsonObject.put("EndDate", startDate);
                        jsonObject.put("Remarks", etReason.getText().toString().trim());
                        jsonObject.put("StartTime", intime);
                        jsonObject.put("EndTime", outtime);
                        jsonObject.put("clinetname", "");
                        jsonObject.put("clinetphn", "");
                        jsonObject.put("Oddaytype", "");
                        jsonObject.put("OtMin", "");
                        jsonObject.put("LtMin", "");
                        jsonObject.put("refdate", "");
                        jsonObject.put("Approverid", SupervisorID);
                        jsonObject.put("SecurityCode", pref.getSecurityCode());
                        saveAdjustmentApplication(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    private void saveAdjustmentApplication(JSONObject jsonObject) {
        progressDialog.show();
        AndroidNetworking.post(AppData.LAMS_Save_OD_CO_WFH)
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
                            Log.e(TAG, "SAVE_ADJUSTMENT_APPLICATION: "+response.toString(4));
                            JSONObject object = new JSONObject(String.valueOf(response));
                            String Response_Message = object.optString("Response_Message");
                            String Response_Code = object.optString("Response_Code");
                            if (Response_Code.equals("101")) {
                                ShowDialog.showSuccessDialog(NEW_AdjustmentActivity.this, Response_Message, new ShowDialog.ResultListener() {
                                    @Override
                                    public void onSuccess() {
                                        Intent intent = new Intent(NEW_AdjustmentActivity.this, AttenDanceDashboardActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            } else {
                                Toast.makeText(NEW_AdjustmentActivity.this, Response_Message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Log.e(TAG, "SAVE_ADJUSTMENT_APPLICATION_error: "+anError.getErrorBody());
                    }
                });
    }




    /*public void loadAdjustmentFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        NEW_LeaveAdjustmentFragment htfragment=new NEW_LeaveAdjustmentFragment();
        transaction.replace(R.id.frameLayout, htfragment);
        transaction.commit();
    }*/

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
                                dropDownList.add(new SpinnerModel("Select","%"));
                                if (jsonArray.length() > 0){
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        String AdjType = obj.getString("AdjType");
                                        String AdjTypeName = obj.getString("AdjTypeName");
                                        dropDownList.add(new SpinnerModel(AdjTypeName,AdjType));
                                    }
                                    customSpinnerAdapter = new CustomSpinnerAdapter(NEW_AdjustmentActivity.this,dropDownList);
                                    spAdjustment.setAdapter(customSpinnerAdapter);
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

    private void showEffectiveDate(TextView tv) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(NEW_AdjustmentActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


                        int month = (monthOfYear + 1);
                        effectiveDate = year + "-" + month + "-" + dayOfMonth;
                        tv.setText(Util.changeAnyDateFormat(effectiveDate,"yyyy-MM-dd","d-MMMM-yyyy"));

                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.show();

    }
    private void showEndDate(TextView tv) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(NEW_AdjustmentActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {


                        int month = (monthOfYear + 1);
                        endDate = year + "-" + month + "-" + dayOfMonth;
                        tv.setText(Util.changeAnyDateFormat(endDate,"yyyy-MM-dd","d-MMMM-yyyy"));
                        if (startDate.equalsIgnoreCase(endDate)) {
                            spAdjustment.setEnabled(true);
                        } else {
                            spAdjustment.setEnabled(false);
                        }

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePickerDialog.show();

    }

    private void showStartDate(TextView tv) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(NEW_AdjustmentActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {


                        int month = (monthOfYear + 1);
                        startDate = year + "-" + month + "-" + dayOfMonth;
                        tv.setText(Util.changeAnyDateFormat(startDate,"yyyy-MM-dd","d-MMMM-yyyy"));

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePickerDialog.show();

    }

    private void openSupervisorPopUp(TextView txtSupervisor) {
        searchWbsCodeDialog = new Dialog(NEW_AdjustmentActivity.this, R.style.CustomDialogNew2);
        searchWbsCodeDialog.setContentView(R.layout.wbs_code_search_layout);
        searchWbsCodeDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        searchWbsCodeDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        searchWbsCodeDialog.setCancelable(true);

        TextView txtPopupHeadline = searchWbsCodeDialog.findViewById(R.id.txtPopupHeadline);
        SearchView wbsCodeSearchView = (SearchView) searchWbsCodeDialog.findViewById(R.id.wbsCodeSearchView);
        ImageView imgCancel = searchWbsCodeDialog.findViewById(R.id.imgCancel);
        RecyclerView rvWbsCode = searchWbsCodeDialog.findViewById(R.id.rvWbsCode);

        wbsCodeSearchView.setQueryHint("Search Approver");
        txtPopupHeadline.setText("Select Approver");
        rvWbsCode.setLayoutManager(new LinearLayoutManager(NEW_AdjustmentActivity.this));
        ArrayList<SpineerItemModel> supervisorListCopy = new ArrayList<>();
        supervisorListCopy = (ArrayList<SpineerItemModel>) supervisorList.clone();
        SupervisorFilterAdapter supervisorFilterAdapter = new SupervisorFilterAdapter(NEW_AdjustmentActivity.this, supervisorListCopy);
        rvWbsCode.setAdapter(supervisorFilterAdapter);

        wbsCodeSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                supervisorFilterAdapter.getFilter().filter(s);
                return false;
            }
        });

        supervisorFilterAdapter.setSupervisorSelectListener(new MetsoNewReimbursementClaimActivity.SupervisorSelectListener() {
            @Override
            public void onClick(String supervisor_id, String supervisor) {
                SupervisorID = supervisor_id;
                txtSupervisor.setText(supervisor);
                searchWbsCodeDialog.dismiss();
            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchWbsCodeDialog.dismiss();
            }
        });
        searchWbsCodeDialog.show();
    }
}