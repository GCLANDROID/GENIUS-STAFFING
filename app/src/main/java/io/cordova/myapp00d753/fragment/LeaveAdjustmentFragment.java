package io.cordova.myapp00d753.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import io.cordova.myapp00d753.adapter.MenuItemAdapter;
import io.cordova.myapp00d753.module.MenuItemModel;
import io.cordova.myapp00d753.module.SpineerItemModel;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;


public class LeaveAdjustmentFragment extends Fragment {
    LinearLayout lnAddApplication;
    View view;
    AlertDialog alerDialog1,successDialog;
    String next;
    Pref pref;
    ArrayList<String>otApplicationComponentList=new ArrayList<>();
    ArrayList<SpineerItemModel>otApplicationComponentModuleList=new ArrayList<>();
    String applicationComponentID="";
    String applicationComponent="";
    String effectiveDate;
    String startDate="";
    String endDate="";
    ArrayList<String>modelist=new ArrayList<>();
    ArrayList<SpineerItemModel>modelModulelist=new ArrayList<>();
    String mode="";
    String month;
    Spinner spMode;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_leave_adjustment, container, false);
        initView();
        onclick();
        return view;
    }

    private void initView(){
        pref=new Pref(getContext());
        lnAddApplication=(LinearLayout) view.findViewById(R.id.lnAddApplication);
        next = "<font color='#EE0000'>*</font>";
        modelist.add("Full Day");
        modelist.add("First Half");
        modelist.add("Second Half");

        modelModulelist.add(new SpineerItemModel("Full Day","2"));
        modelModulelist.add(new SpineerItemModel("First Half","1"));
        modelModulelist.add(new SpineerItemModel("Second Half","3"));


        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MMM-dd", Locale.getDefault());
        effectiveDate = df.format(c);
        Log.d("effectiveDate",effectiveDate);
        int m = Calendar.getInstance().get(Calendar.MONTH) + 1;
        month= String.valueOf(m);
        getOtherApplicationComponentItem();
    }

    private void onclick(){
        lnAddApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adjustPopUp();
            }
        });
    }


    public void getOtherApplicationComponentItem() {

        String surl = AppData.url+"gcl_ODAndOTOtherApplication?p_companyid="+pref.getEmpClintId()+"&p_mode=&SecurityCode="+pref.getSecurityCode();
        Log.d("inputLogin", surl);

        final ProgressDialog pd=new ProgressDialog(getContext());
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
                                   JSONArray responseData=job1.optJSONArray("responseData");
                                   for (int i=0;i<responseData.length();i++){
                                       JSONObject obj=responseData.optJSONObject(i);
                                       String AdjusmentName=obj.optString("AdjusmentName");
                                       String Aid=obj.optString("Aid");
                                       otApplicationComponentList.add(AdjusmentName);
                                       SpineerItemModel itemModule=new SpineerItemModel(AdjusmentName,Aid);
                                       otApplicationComponentModuleList.add(itemModule);
                                   }

                                }





                            // boolean _status = job1.getBoolean("status");


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
        TextView tvAdjustmentType=(TextView)dialogView.findViewById(R.id.tvAdjustmentType);
        TextView tvStartDateName=(TextView)dialogView.findViewById(R.id.tvStartDateName);
        TextView tvEndDateName=(TextView)dialogView.findViewById(R.id.tvEndDateName);
        TextView tvInTimeName=(TextView)dialogView.findViewById(R.id.tvInTimeName);
        TextView tvOutTimeName=(TextView)dialogView.findViewById(R.id.tvOutTimeName);
        TextView tvModeName=(TextView)dialogView.findViewById(R.id.tvModeName);
        TextView tvReason=(TextView)dialogView.findViewById(R.id.tvReason);
        TextView tvEffectiveDate=(TextView)dialogView.findViewById(R.id.tvEffectiveDate);
        TextView tvStrtDate=(TextView)dialogView.findViewById(R.id.tvStrtDate);
        TextView tvEndDate=(TextView)dialogView.findViewById(R.id.tvEndDate);
        TextView tvInTime=(TextView) dialogView.findViewById(R.id.tvInTime);
        TextView tvOutTime=(TextView) dialogView.findViewById(R.id.tvOutTime);

        LinearLayout llEffectiveDate=(LinearLayout)dialogView.findViewById(R.id.llEffectiveDate);
        LinearLayout llStartEndDate=(LinearLayout)dialogView.findViewById(R.id.llStartEndDate);
        LinearLayout llInOutTime=(LinearLayout)dialogView.findViewById(R.id.llInOutTime);
        LinearLayout llMode=(LinearLayout)dialogView.findViewById(R.id.llMode);
        LinearLayout lnEffectiveDate=(LinearLayout)dialogView.findViewById(R.id.lnEffectiveDate);
        LinearLayout llStartDate=(LinearLayout)dialogView.findViewById(R.id.llStartDate);
        LinearLayout llEndDate=(LinearLayout)dialogView.findViewById(R.id.llEndDate);
        LinearLayout llInTime=(LinearLayout)dialogView.findViewById(R.id.llInTime);
        LinearLayout llOutTime=(LinearLayout)dialogView.findViewById(R.id.llOutTime);

        tvAdjustmentType.setText(Html.fromHtml("Adjustment Type "+next));
        tvStartDateName.setText(Html.fromHtml("Start Date "+next));
        tvEndDateName.setText(Html.fromHtml("End Date "+next));
        tvInTimeName.setText(Html.fromHtml("In Time "+next));
        tvOutTimeName.setText(Html.fromHtml("Out Time "+next));
        tvModeName.setText(Html.fromHtml("Mode "+next));
        tvReason.setText(Html.fromHtml("Reason "+next));
        Spinner spAdjustment=(Spinner)dialogView.findViewById(R.id.spAdjustment);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_spinner_item,
                        otApplicationComponentList); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAdjustment.setAdapter(spinnerArrayAdapter);

         spMode=(Spinner)dialogView.findViewById(R.id.spMode);
        ArrayAdapter<String> spinnerModeArrayAdapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_spinner_item,
                        modelist); //selected item will look like a spinner set from XML
        spinnerModeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMode.setAdapter(spinnerModeArrayAdapter);

        spAdjustment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>0){
                    applicationComponent=otApplicationComponentModuleList.get(i).getItemName();
                    applicationComponentID=otApplicationComponentModuleList.get(i).getItemId();
                    if (applicationComponent.equalsIgnoreCase("On Duty")){
                         llStartEndDate.setVisibility(View.VISIBLE);
                         llEffectiveDate.setVisibility(View.GONE);
                         llMode.setVisibility(View.VISIBLE);
                        llInOutTime.setVisibility(View.GONE);
                    }else if (applicationComponent.equalsIgnoreCase("OT Application")){
                        llStartEndDate.setVisibility(View.VISIBLE);
                        llEffectiveDate.setVisibility(View.GONE);
                        llMode.setVisibility(View.GONE);
                        llInOutTime.setVisibility(View.VISIBLE);
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
                        tvInTime.setText( selectedHour + ":" + selectedMinute);
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
                        tvOutTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        spMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mode=modelModulelist.get(i).getItemId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        EditText etReason=(EditText)dialogView.findViewById(R.id.etReason);
        Button btnSave=(Button)dialogView.findViewById(R.id.btnSave);
        Button btnCancel=(Button)dialogView.findViewById(R.id.btnCancel);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (applicationComponent.equalsIgnoreCase("On Duty")){
                    postDataOD(etReason.getText().toString());
                }else {
                    postData(etReason.getText().toString(),tvInTime.getText().toString(),tvOutTime.getText().toString());
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
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {



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
                        if (startDate.equalsIgnoreCase(endDate)){
                            spMode.setEnabled(true);
                        }else {
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


    private void postData(String remarks,String intime,String outtime) {
        ProgressDialog pd=new ProgressDialog(getContext());
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.upload(AppData.url + "Post_EmployeeOTandODAdjustment")
                .addMultipartParameter("CompanyID", pref.getEmpClintId())
                .addMultipartParameter("EmployeeID", pref.getEmpId())
                .addMultipartParameter("YearId", "19")
                .addMultipartParameter("MonthId", month)
                .addMultipartParameter("GatePassDate", effectiveDate +" 00:00:00.000")
                .addMultipartParameter("EndDate", endDate+" 00:00:00.000")
                .addMultipartParameter("Remarks", remarks)
                .addMultipartParameter("StartTime", effectiveDate+" "+intime+":00.000")
                .addMultipartParameter("EndTime", effectiveDate+" "+outtime+":00.000")
                .addMultipartParameter("GatePassType", applicationComponentID)
                .addMultipartParameter("clinetname", "")
                .addMultipartParameter("clinetphn", "")
                .addMultipartParameter("CreatedBy", pref.getEmpId())
                .addMultipartParameter("AID", "0")
                .addMultipartParameter("Oddaytype", mode)
                .addMultipartParameter("OtMin", "0")
                .addMultipartParameter("refdate", effectiveDate+" 00:00:00.000")
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

    private void postDataOD(String remarks) {
        ProgressDialog pd=new ProgressDialog(getContext());
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.upload(AppData.url + "Post_EmployeeOTandODAdjustment")
                .addMultipartParameter("CompanyID", pref.getEmpClintId())
                .addMultipartParameter("EmployeeID", pref.getEmpId())
                .addMultipartParameter("YearId", "19")
                .addMultipartParameter("MonthId", month)
                .addMultipartParameter("GatePassDate", startDate +" 00:00:00.000")
                .addMultipartParameter("EndDate", endDate+" 00:00:00.000")
                .addMultipartParameter("Remarks", remarks)
                .addMultipartParameter("StartTime", startDate +" 00:00:00.000")
                .addMultipartParameter("EndTime", startDate +" 00:00:00.000")
                .addMultipartParameter("GatePassType", applicationComponentID)
                .addMultipartParameter("clinetname", "")
                .addMultipartParameter("clinetphn", "")
                .addMultipartParameter("CreatedBy", pref.getEmpId())
                .addMultipartParameter("AID", "0")
                .addMultipartParameter("Oddaytype", mode)
                .addMultipartParameter("OtMin", "0")
                .addMultipartParameter("refdate", effectiveDate+" 00:00:00.000")
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
                        }else {
                            Toast.makeText(getContext(),"False",Toast.LENGTH_LONG).show();
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

            }
        });

        successDialog = dialogBuilder.create();
        successDialog.setCancelable(true);
        Window window = successDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        successDialog.show();
    }
}