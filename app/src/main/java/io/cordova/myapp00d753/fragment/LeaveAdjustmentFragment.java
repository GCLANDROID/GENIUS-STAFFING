package io.cordova.myapp00d753.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.AttendanceManageActivity;
import io.cordova.myapp00d753.activity.AttendanceReportActivity;
import io.cordova.myapp00d753.activity.EmployeeDashBoardActivity;
import io.cordova.myapp00d753.activity.VisitLocationActivity;
import io.cordova.myapp00d753.activity.metso.adapter.ApproverAutoCompleteAdapter;
import io.cordova.myapp00d753.activity.metso.model.ApproverModel;
import io.cordova.myapp00d753.adapter.CompOffViewAdapter;
import io.cordova.myapp00d753.adapter.MenuItemAdapter;
import io.cordova.myapp00d753.module.CompffViewModel;
import io.cordova.myapp00d753.module.MenuItemModel;
import io.cordova.myapp00d753.module.SpineerItemModel;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.ClientID;
import io.cordova.myapp00d753.utility.Pref;


public class LeaveAdjustmentFragment extends Fragment {
    private static final String TAG = "LeaveAdjustmentFragment";
    LinearLayout lnAddApplication;
    View view;
    AlertDialog alerDialog1, successDialog;
    String next;
    Pref pref;
    ArrayList<String> otApplicationComponentList = new ArrayList<>();
    ArrayList<SpineerItemModel> otApplicationComponentModuleList = new ArrayList<>();
    String applicationComponentID = "";
    String applicationComponent = "";
    String effectiveDate;
    String startDate = "";
    String endDate = "";
    ArrayList<String> modelist = new ArrayList<>();
    ArrayList<SpineerItemModel> modelModulelist = new ArrayList<>();
    String mode = "";
    String month;
    Spinner spMode;
    ArrayList<ApproverModel> approverList;
    ApproverAutoCompleteAdapter approverAutoCompleteAdapter;
    Dialog dialogLocationPopUp;
    long approverID = 0;
    Spinner spFinYear, spMonth;

    ArrayList<String> finyearList = new ArrayList<>();
    ArrayList<String> monthList = new ArrayList<>();
    ArrayList<SpineerItemModel> modulemonthList = new ArrayList<>();
    String monthID, finyr;
    RecyclerView rvItem;
    ArrayList<CompffViewModel> compList = new ArrayList<>();
    Button btnShow;
    TextView tvNoData;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_leave_adjustment, container, false);
        initView();
        onclick();
        return view;
    }

    private void initView() {
        pref = new Pref(getContext());
        getApproverList();
      /*  JSONObject obj1=new JSONObject();
        try {
            obj1.put("Mode", "3");
            obj1.put("CompanyID",pref.getEmpClintId());
            obj1.put("SecurityCode",pref.getSecurityCode());
            getApproverList(obj1);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        lnAddApplication = (LinearLayout) view.findViewById(R.id.lnAddApplication);
        next = "<font color='#EE0000'>*</font>";
        if (pref.getEmpClintId().equals("AEMCLI2110001671") || pref.getEmpClintId().equals(ClientID.SKF_CLIENT_ID)) {
            modelist.add("Full Day");
        } else {
            modelist.add("Full Day");
            modelist.add("First Half");
            modelist.add("Second Half");
        }

        if (pref.getEmpClintId().equals(ClientID.SKY_ROOT)){
            modelModulelist.add(new SpineerItemModel("Full Day", "2"));
            modelModulelist.add(new SpineerItemModel("First Half", "4"));
            modelModulelist.add(new SpineerItemModel("Second Half", "5"));
        } else {
            modelModulelist.add(new SpineerItemModel("Full Day", "2"));
            modelModulelist.add(new SpineerItemModel("First Half", "1"));
            modelModulelist.add(new SpineerItemModel("Second Half", "3"));
        }


        finyearList.add("2024-2025");
        finyearList.add("2025-2026");
        finyearList.add("2026-2027");

        monthList.add("January");
        monthList.add("February");
        monthList.add("March");
        monthList.add("April");
        monthList.add("May");
        monthList.add("June");
        monthList.add("July");
        monthList.add("August");
        monthList.add("September");
        monthList.add("October");
        monthList.add("November");
        monthList.add("December");


        modulemonthList.add(new SpineerItemModel("January", "1"));
        modulemonthList.add(new SpineerItemModel("February", "2"));
        modulemonthList.add(new SpineerItemModel("March", "3"));
        modulemonthList.add(new SpineerItemModel("April", "4"));
        modulemonthList.add(new SpineerItemModel("May", "5"));
        modulemonthList.add(new SpineerItemModel("June", "6"));
        modulemonthList.add(new SpineerItemModel("July", "7"));
        modulemonthList.add(new SpineerItemModel("August", "8"));
        modulemonthList.add(new SpineerItemModel("September", "9"));
        modulemonthList.add(new SpineerItemModel("October", "10"));
        modulemonthList.add(new SpineerItemModel("November", "11"));
        modulemonthList.add(new SpineerItemModel("December", "12"));

        spMonth = (Spinner) view.findViewById(R.id.spMonth);
        spFinYear = (Spinner) view.findViewById(R.id.spFinYear);
        rvItem = (RecyclerView) view.findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);

        btnShow = (Button) view.findViewById(R.id.btnShow);
        tvNoData = (TextView) view.findViewById(R.id.tvNoData);

        ArrayAdapter<String> finadapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_spinner_item,
                        finyearList); //selected item will look like a spinner set from XML
        finadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFinYear.setAdapter(finadapter);


        ArrayAdapter<String> monthadapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_spinner_item,
                        monthList); //selected item will look like a spinner set from XML
        monthadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMonth.setAdapter(monthadapter);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MMM-dd", Locale.getDefault());
        effectiveDate = df.format(c);
        Log.d("effectiveDate", effectiveDate);
        int monthNumber = Calendar.getInstance().get(Calendar.MONTH);
        int m = Calendar.getInstance().get(Calendar.MONTH) + 1;
        month = String.valueOf(m);
        getOtherApplicationComponentItem();
       /* JSONObject obj=new JSONObject();
        try {
            obj.put("p_companyid", pref.getEmpClintId());
            obj.put("p_mode","");
            obj.put("SecurityCode",pref.getSecurityCode());
            getOtherApplicationComponentItem(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        spMonth.setSelection(monthNumber);
    }

    private void onclick() {
        lnAddApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adjustPopUp();
            }
        });
        spFinYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                finyr = finyearList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                monthID = modulemonthList.get(i).getItemId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCompffData();
            }
        });
    }

    public void getOtherApplicationComponentItem(JSONObject jsonObject) {
        Log.e(TAG, "getOtherApplicationComponentItem: "+jsonObject);
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading.....");
        pd.show();
        AndroidNetworking.post(AppData.GET_OD_AND_OT_OTHER_APPLICATION)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            pd.dismiss();
                            Log.e(TAG, "OD_AND_OT_OTHER: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(Response_Data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.optJSONObject(i);
                                    String AdjusmentName = obj.optString("AdjusmentName");
                                    String Aid = obj.optString("Aid");
                                    otApplicationComponentList.add(AdjusmentName);
                                    SpineerItemModel itemModule = new SpineerItemModel(AdjusmentName, Aid);
                                    otApplicationComponentModuleList.add(itemModule);
                                }
                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Something went to wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        pd.dismiss();
                        Log.e(TAG, "OD_AND_OT_OTHER_error: "+anError.getErrorBody());
                    }
                });
    }


    public void getOtherApplicationComponentItem() {
        String surl = AppData.url + "gcl_ODAndOTOtherApplication?p_companyid=" + pref.getEmpClintId() + "&p_mode=&SecurityCode=" + pref.getSecurityCode();
        Log.d("inputLogin", surl);

        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading.....");
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        pd.dismiss();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");

                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.optJSONObject(i);
                                    String AdjusmentName = obj.optString("AdjusmentName");
                                    String Aid = obj.optString("Aid");
                                    otApplicationComponentList.add(AdjusmentName);
                                    SpineerItemModel itemModule = new SpineerItemModel(AdjusmentName, Aid);
                                    otApplicationComponentModuleList.add(itemModule);
                                }
                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                //  Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");

    }

    private void adjustPopUp() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_leave_adjustment, null);
        dialogBuilder.setView(dialogView);
        TextView tvAdjustmentType = (TextView) dialogView.findViewById(R.id.tvAdjustmentType);
        TextView tvStartDateName = (TextView) dialogView.findViewById(R.id.tvStartDateName);
        TextView tvEndDateName = (TextView) dialogView.findViewById(R.id.tvEndDateName);
        TextView tvInTimeName = (TextView) dialogView.findViewById(R.id.tvInTimeName);
        TextView tvOutTimeName = (TextView) dialogView.findViewById(R.id.tvOutTimeName);
        TextView tvModeName = (TextView) dialogView.findViewById(R.id.tvModeName);
        TextView tvReason = (TextView) dialogView.findViewById(R.id.tvReason);
        TextView tvEffectiveDate = (TextView) dialogView.findViewById(R.id.tvEffectiveDate);
        TextView tvStrtDate = (TextView) dialogView.findViewById(R.id.tvStrtDate);
        TextView tvEndDate = (TextView) dialogView.findViewById(R.id.tvEndDate);
        TextView tvInTime = (TextView) dialogView.findViewById(R.id.tvInTime);
        TextView tvOutTime = (TextView) dialogView.findViewById(R.id.tvOutTime);

        LinearLayout llEffectiveDate = (LinearLayout) dialogView.findViewById(R.id.llEffectiveDate);
        LinearLayout llStartEndDate = (LinearLayout) dialogView.findViewById(R.id.llStartEndDate);
        LinearLayout llInOutTime = (LinearLayout) dialogView.findViewById(R.id.llInOutTime);
        LinearLayout llMode = (LinearLayout) dialogView.findViewById(R.id.llMode);
        LinearLayout lnEffectiveDate = (LinearLayout) dialogView.findViewById(R.id.lnEffectiveDate);
        LinearLayout llStartDate = (LinearLayout) dialogView.findViewById(R.id.llStartDate);
        LinearLayout llEndDate = (LinearLayout) dialogView.findViewById(R.id.llEndDate);
        LinearLayout llInTime = (LinearLayout) dialogView.findViewById(R.id.llInTime);
        LinearLayout llOutTime = (LinearLayout) dialogView.findViewById(R.id.llOutTime);
        LinearLayout llEndTime = (LinearLayout) dialogView.findViewById(R.id.llEndTime);

        tvAdjustmentType.setText(Html.fromHtml("Adjustment Type " + next));
        tvStartDateName.setText(Html.fromHtml("Start Date " + next));
        tvEndDateName.setText(Html.fromHtml("End Date " + next));
        tvInTimeName.setText(Html.fromHtml("In Time " + next));
        tvOutTimeName.setText(Html.fromHtml("Out Time " + next));
        tvModeName.setText(Html.fromHtml("Mode " + next));
        tvReason.setText(Html.fromHtml("Reason " + next));
        Spinner spAdjustment = (Spinner) dialogView.findViewById(R.id.spAdjustment);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_spinner_item,
                        otApplicationComponentList); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAdjustment.setAdapter(spinnerArrayAdapter);

        spMode = (Spinner) dialogView.findViewById(R.id.spMode);
        ArrayAdapter<String> spinnerModeArrayAdapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_spinner_item,
                        modelist); //selected item will look like a spinner set from XML
        spinnerModeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMode.setAdapter(spinnerModeArrayAdapter);

        spAdjustment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    applicationComponent = otApplicationComponentModuleList.get(i).getItemName();
                    applicationComponentID = otApplicationComponentModuleList.get(i).getItemId();
                    if (applicationComponent.equalsIgnoreCase("On Duty")) {
                        llStartEndDate.setVisibility(View.VISIBLE);
                        llEffectiveDate.setVisibility(View.GONE);
                        llMode.setVisibility(View.VISIBLE);
                        llInOutTime.setVisibility(View.GONE);
                    } else if (applicationComponent.equalsIgnoreCase("OT Application")) {
                        llStartEndDate.setVisibility(View.VISIBLE);
                        llEffectiveDate.setVisibility(View.GONE);
                        llMode.setVisibility(View.GONE);
                        llInOutTime.setVisibility(View.VISIBLE);
                    } else if (applicationComponent.equalsIgnoreCase("Comp Off")) {
                        llStartEndDate.setVisibility(View.VISIBLE);
                        llEffectiveDate.setVisibility(View.VISIBLE);
                        llMode.setVisibility(View.VISIBLE);
                        llInOutTime.setVisibility(View.GONE);
                        llEndTime.setVisibility(View.GONE);
                        tvStartDateName.setText(Html.fromHtml("Referal Date " + next));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        lnEffectiveDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEffectiveDate(tvEffectiveDate);
            }
        });

        llStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStartDate(tvStrtDate);
            }
        });

        llEndDate.setOnClickListener(new View.OnClickListener() {
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
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
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
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        tvOutTime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        spMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mode = modelModulelist.get(i).getItemId();
                Log.e(TAG, "onItemSelected: "+mode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        EditText etReason = (EditText) dialogView.findViewById(R.id.etReason);
        Button btnSave = (Button) dialogView.findViewById(R.id.btnSave);
        Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (applicationComponent.equalsIgnoreCase("On Duty")) {
                    postDataOD(etReason.getText().toString());
                } else if (applicationComponent.equalsIgnoreCase("Comp Off")) {
                    if (pref.getEmpClintId().equalsIgnoreCase(ClientID.SKF_CLIENT_ID)) {
                        //TODO: Data save will be called
                        postDataCompOff(etReason.getText().toString(), "0");
                    } else if (pref.getEmpClintId().equalsIgnoreCase(ClientID.SKY_ROOT)){
                        postDataCompOff(etReason.getText().toString(), "0");
                    } else {
                        approverpopup(etReason.getText().toString());
                    }
                } else {
                    postData(etReason.getText().toString(), tvInTime.getText().toString(), tvOutTime.getText().toString());
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
            }
        });


        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }


    private void showEffectiveDate(TextView tv) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {


                        int month = (monthOfYear + 1);
                        effectiveDate = year + "-" + month + "-" + dayOfMonth;
                        tv.setText(effectiveDate);

                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.show();

    }

    private void showEndDate(TextView tv) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {


                        int month = (monthOfYear + 1);
                        endDate = year + "-" + month + "-" + dayOfMonth;
                        tv.setText(endDate);
                        if (startDate.equalsIgnoreCase(endDate)) {
                            spMode.setEnabled(true);
                        } else {
                            spMode.setEnabled(false);
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


        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {


                        int month = (monthOfYear + 1);
                        startDate = year + "-" + month + "-" + dayOfMonth;
                        tv.setText(startDate);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePickerDialog.show();

    }


    private void postData(String remarks, String intime, String outtime) {
        ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.upload(AppData.ATT_SAVE_EMPLOYEE_DIGITAL_DOCUMENT)
                .addMultipartParameter("CompanyID", pref.getEmpClintId())
                .addMultipartParameter("EmployeeID", pref.getEmpId())
                .addMultipartParameter("YearId", "19")
                .addMultipartParameter("MonthId", month)
                .addMultipartParameter("GatePassDate", effectiveDate + " 00:00:00.000")
                .addMultipartParameter("EndDate", endDate + " 00:00:00.000")
                .addMultipartParameter("Remarks", remarks)
                .addMultipartParameter("StartTime", effectiveDate + " " + intime + ":00.000")
                .addMultipartParameter("EndTime", effectiveDate + " " + outtime + ":00.000")
                .addMultipartParameter("GatePassType", applicationComponentID)
                .addMultipartParameter("clinetname", "")
                .addMultipartParameter("clinetphn", "")
                .addMultipartParameter("CreatedBy", pref.getEmpId())
                .addMultipartParameter("AID", "0")
                .addMultipartParameter("Oddaytype", mode)
                .addMultipartParameter("OtMin", "0")
                .addMultipartParameter("refdate", effectiveDate + " 00:00:00.000")
                .addMultipartParameter("LtMin", "0")
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {


                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pd.dismiss();


                        JSONObject job = response;
                        boolean responseStatus = job.optBoolean("responseStatus");
                        if (responseStatus) {

                            successAlert();
                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        pd.dismiss();
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();

                    }
                });

        /*AndroidNetworking.upload(AppData.url + "Post_EmployeeOTandODAdjustment")
                .addMultipartParameter("CompanyID", pref.getEmpClintId())
                .addMultipartParameter("EmployeeID", pref.getEmpId())
                .addMultipartParameter("YearId", "19")
                .addMultipartParameter("MonthId", month)
                .addMultipartParameter("GatePassDate", effectiveDate + " 00:00:00.000")
                .addMultipartParameter("EndDate", endDate + " 00:00:00.000")
                .addMultipartParameter("Remarks", remarks)
                .addMultipartParameter("StartTime", effectiveDate + " " + intime + ":00.000")
                .addMultipartParameter("EndTime", effectiveDate + " " + outtime + ":00.000")
                .addMultipartParameter("GatePassType", applicationComponentID)
                .addMultipartParameter("clinetname", "")
                .addMultipartParameter("clinetphn", "")
                .addMultipartParameter("CreatedBy", pref.getEmpId())
                .addMultipartParameter("AID", "0")
                .addMultipartParameter("Oddaytype", mode)
                .addMultipartParameter("OtMin", "0")
                .addMultipartParameter("refdate", effectiveDate + " 00:00:00.000")
                .addMultipartParameter("LtMin", "0")
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {


                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pd.dismiss();


                        JSONObject job = response;
                        boolean responseStatus = job.optBoolean("responseStatus");
                        if (responseStatus) {

                            successAlert();
                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        pd.dismiss();
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();

                    }
                });*/
    }

    private void postDataOD(String remarks) {
        ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        AndroidNetworking.upload(AppData.url + "Post_EmployeeOTandODAdjustment")
                .addMultipartParameter("CompanyID", pref.getEmpClintId())
                .addMultipartParameter("EmployeeID", pref.getEmpId())
                .addMultipartParameter("YearId", "19")
                .addMultipartParameter("MonthId", month)
                .addMultipartParameter("GatePassDate", startDate + " 00:00:00.000")
                .addMultipartParameter("EndDate", endDate + " 00:00:00.000")
                .addMultipartParameter("Remarks", remarks)
                .addMultipartParameter("StartTime", startDate + " 00:00:00.000")
                .addMultipartParameter("EndTime", startDate + " 00:00:00.000")
                .addMultipartParameter("GatePassType", applicationComponentID)
                .addMultipartParameter("clinetname", "")
                .addMultipartParameter("clinetphn", "")
                .addMultipartParameter("CreatedBy", pref.getEmpId())
                .addMultipartParameter("AID", "0")
                .addMultipartParameter("Oddaytype", mode)
                .addMultipartParameter("OtMin", "0")
                .addMultipartParameter("refdate", effectiveDate + " 00:00:00.000")
                .addMultipartParameter("LtMin", "0")
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {


                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pd.dismiss();

                        JSONObject job = response;
                        boolean responseStatus = job.optBoolean("responseStatus");
                        if (responseStatus) {
                            successAlert();
                        } else {
                            Toast.makeText(getContext(), "False", Toast.LENGTH_LONG).show();
                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        pd.dismiss();
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();

                    }
                });
    }


    private void postDataCompOff(String remarks, String approverID) {
        ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        Log.e(TAG, "postDataCompOff: \nCompanyID:"+pref.getEmpClintId()
                +"\nEmployeeID:"+pref.getEmpId()
                +"\nYearId:20"
                +"\nMonthId:"+month
                +"\nGatePassDate:"+effectiveDate + " 00:00:00.000"
                +"\nEndDate:"+effectiveDate + " 00:00:00.000"
                +"\nRemarks:"+remarks
                +"\nStartTime:"+effectiveDate + " 00:00:00.000"
                +"\nEndTime:"+effectiveDate + " 00:00:00.000"
                +"\nGatePassType:"+applicationComponentID
                +"\nclinetname:"
                +"\nclinetphn:"
                +"\nCreatedBy:"+pref.getEmpId()
                +"\nAID:0"
                +"\nOddaytype:"+mode
                +"\nOtMin:"+0
                +"\nApproverid:"+approverID
                +"\nrefdate:"+startDate + " 00:00:00.000"
                +"\nLtMin:0"
                +"\nSecurityCode:"+pref.getSecurityCode());

        AndroidNetworking.upload(AppData.url + "Post_EmployeeOTandODAdjustment")
                .addMultipartParameter("CompanyID", pref.getEmpClintId())
                .addMultipartParameter("EmployeeID", pref.getEmpId())
                .addMultipartParameter("YearId", "20")
                .addMultipartParameter("MonthId", month)
                .addMultipartParameter("GatePassDate", effectiveDate + " 00:00:00.000")
                .addMultipartParameter("EndDate", effectiveDate + " 00:00:00.000")
                .addMultipartParameter("Remarks", remarks)
                .addMultipartParameter("StartTime", effectiveDate + " 00:00:00.000")
                .addMultipartParameter("EndTime", effectiveDate + " 00:00:00.000")
                .addMultipartParameter("GatePassType", applicationComponentID)
                .addMultipartParameter("clinetname", "")
                .addMultipartParameter("clinetphn", "")
                .addMultipartParameter("CreatedBy", pref.getEmpId())
                .addMultipartParameter("AID", "0")
                .addMultipartParameter("Oddaytype", mode)
                .addMultipartParameter("OtMin", "0")
                .addMultipartParameter("Approverid", approverID)
                .addMultipartParameter("refdate", startDate + " 00:00:00.000")
                .addMultipartParameter("LtMin", "0")
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {


                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pd.dismiss();
                        Log.e(TAG, "postDataCompOff: "+response);
                        JSONObject job = response;
                        boolean responseStatus = job.optBoolean("responseStatus");
                        String responseText=job.optString("responseText");
                        if (responseStatus) {
                            successAlert();
                        } else {
                            Toast.makeText(getContext(), responseText, Toast.LENGTH_LONG).show();
                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        pd.dismiss();
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                });
    }


    private void successAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);

        tvInvalidDate.setText("Leave adjustment has been saved successfully");

        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                successDialog.dismiss();
                getCompffData();

            }
        });

        successDialog = dialogBuilder.create();
        successDialog.setCancelable(true);
        Window window = successDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        successDialog.show();
    }

    private void getApproverList(JSONObject jsonObject) {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        AndroidNetworking.post(AppData.GET_METSO_ATTENDANCE_DATA)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "APPROVER_LIST: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(Response_Data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject objectResponse = jsonArray.getJSONObject(i);
                                    approverList.add(new ApproverModel(objectResponse.getInt("UserId"),
                                            objectResponse.getString("UserName")));
                                }
                                approverAutoCompleteAdapter = new ApproverAutoCompleteAdapter(getContext(), approverList);
                                progressDialog.cancel();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "something went to wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "APPROVER_LIST_error: "+anError.getErrorBody());
                        progressDialog.cancel();
                    }
                });
    }

    private void getApproverList() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        AndroidNetworking.get(AppData.url + "Leave/Get_MetsoAttendanceData")
                .addQueryParameter("Mode", "3")
                .addQueryParameter("CompanyID", pref.getEmpClintId())
                .addQueryParameter("SecurityCode", "0000")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject object = new JSONObject(String.valueOf(response));
                            if (object.getBoolean("responseStatus") == true) {
                                JSONArray jsonArray = object.getJSONArray("responseData");
                                approverList = new ArrayList<>();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject objectResponse = jsonArray.getJSONObject(i);
                                    approverList.add(new ApproverModel(objectResponse.getInt("UserId"),
                                            objectResponse.getString("UserName")));
                                }

                                approverAutoCompleteAdapter = new ApproverAutoCompleteAdapter(getContext(), approverList);
                                progressDialog.cancel();


                                //llMain.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        //Handle the error response

                        Toast.makeText(getContext(), "Getting Some Error", Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                    }
                });
    }


    private void getCompffData() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        AndroidNetworking.get(AppData.url + "Leave/adjustmenview")
                .addQueryParameter("CompanyID", pref.getEmpClintId())
                .addQueryParameter("EmployeeID", pref.getEmpId())
                .addQueryParameter("YearID", "20")
                .addQueryParameter("MonthID", monthID)
                .addQueryParameter("FYear", finyr)
                .addQueryParameter("AppType", "0")
                .addQueryParameter("SecurityCode", "0000")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        compList.clear();
                        try {
                            JSONObject object = new JSONObject(String.valueOf(response));
                            boolean responseStatus = object.optBoolean("responseStatus");
                            if (responseStatus) {
                                JSONArray responseData = object.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.optJSONObject(i);
                                    String Com_ref_date = obj.optString("Com_ref_date");
                                    String ApprovalStatus = obj.optString("ApprovalStatus");
                                    String Remarks = obj.optString("Remarks");
                                    String adjday = obj.optString("adjday");
                                    String GatePassDate = obj.optString("GatePassDate");
                                    String ApplicationDate = obj.optString("ApplicationDate");
                                    String GatePassType=obj.optString("GatePassType");
                                    CompffViewModel viewModel = new CompffViewModel();
                                    viewModel.setAdjday(adjday);
                                    viewModel.setApplicationDate(ApplicationDate);
                                    viewModel.setCom_ref_date(Com_ref_date);
                                    viewModel.setApprovalStatus(ApprovalStatus);
                                    viewModel.setRemarks(Remarks);
                                    viewModel.setGatePassDate(GatePassDate);
                                    viewModel.setGatePassType(GatePassType);
                                    compList.add(viewModel);
                                }
                                tvNoData.setVisibility(View.GONE);
                                rvItem.setVisibility(View.VISIBLE);
                                CompOffViewAdapter viewAdapter = new CompOffViewAdapter(compList);
                                rvItem.setAdapter(viewAdapter);

                            }else{
                                tvNoData.setVisibility(View.VISIBLE);
                                rvItem.setVisibility(View.GONE);
                            }
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        //Handle the error response

                        Toast.makeText(getContext(), "Getting Some Error", Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                    }
                });
    }


    private void approverpopup(String remarks) {
        dialogLocationPopUp = new Dialog(getContext());
        dialogLocationPopUp.setContentView(R.layout.metso_att_location_selection_dialog);
        dialogLocationPopUp.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogLocationPopUp.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ImageView imgCancel = dialogLocationPopUp.findViewById(R.id.imgCancel);
        TextView txtSelectLocation = dialogLocationPopUp.findViewById(R.id.txtSelectLocation);
        TextView textView = dialogLocationPopUp.findViewById(R.id.textView);
        TextView tvLocationTitle = dialogLocationPopUp.findViewById(R.id.tvLocationTitle);
        tvLocationTitle.setVisibility(View.GONE);
        txtSelectLocation.setVisibility(View.GONE);
        TextView txtErrorApprover = dialogLocationPopUp.findViewById(R.id.txtErrorApprover);
        TextView txtErrorLocation = dialogLocationPopUp.findViewById(R.id.txtErrorLocation);
        txtErrorLocation.setVisibility(View.GONE);
        AutoCompleteTextView actApproverName = dialogLocationPopUp.findViewById(R.id.actApproverName);
        Spinner spLocation = dialogLocationPopUp.findViewById(R.id.spLocation);
        LinearLayout llApprover = dialogLocationPopUp.findViewById(R.id.llApprover);
        AppCompatButton btnSubmit = dialogLocationPopUp.findViewById(R.id.btnSubmit);

        textView.setText("Select Approver");
        actApproverName.setAdapter(approverAutoCompleteAdapter);
        actApproverName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ApproverModel selectedItem = (ApproverModel) adapterView.getItemAtPosition(i);
                actApproverName.setText(selectedItem.getApproverName());
                approverID = selectedItem.approverId;

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
                if (approverID != 0) {
                    postDataCompOff(remarks, String.valueOf(approverID));
                    dialogLocationPopUp.cancel();
                } else {
                    Toast.makeText(getContext(), "Please select Your approver", Toast.LENGTH_LONG).show();
                }
            }
        });
        dialogLocationPopUp.show();
    }
}