package io.cordova.myapp00d753.bluedart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

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
import io.cordova.myapp00d753.adapter.LeaveDetailsAdapter;
import io.cordova.myapp00d753.adapter.ODOMeterReadingAdapter;
import io.cordova.myapp00d753.databinding.ActivityOdometerReadingReportBinding;
import io.cordova.myapp00d753.fragment.DetailsFragment;
import io.cordova.myapp00d753.module.LeaveDetailsModel;
import io.cordova.myapp00d753.module.ODOMeterReadingModule;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

public class ODOMeterReadingReportActivity extends AppCompatActivity {
    ActivityOdometerReadingReportBinding binding;
    String startDate="",endDate="";
    ArrayList<ODOMeterReadingModule>itemList=new ArrayList<>();
    Pref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_odometer_reading_report);
        initView();
    }

    private void initView(){
        pref=new Pref(ODOMeterReadingReportActivity.this);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(ODOMeterReadingReportActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvItem.setLayoutManager(layoutManager);

        binding.llStrtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStrtDatePicker();
            }
        });
        binding.llEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndDatePicker();
            }
        });

        binding.btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!startDate.equals("")) {
                    if (!endDate.equals("")) {
                        getItem();
                    }else {
                        Toast.makeText(ODOMeterReadingReportActivity.this,"Please select End Date",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(ODOMeterReadingReportActivity.this,"please select Start Date",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void showStrtDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(ODOMeterReadingReportActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {



                        int month = (monthOfYear + 1);
                        startDate =year + "/" + month + "/" + dayOfMonth;
                        binding.tvStrtDate.setText(startDate);

                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.show();

    }


    private void showEndDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(ODOMeterReadingReportActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {



                        int month = (monthOfYear + 1);
                        endDate = year + "/" + month + "/" + dayOfMonth;
                        binding.tvEndDate.setText(endDate);

                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.show();

    }


    private void getItem(){
        binding.llLoader.setVisibility(View.VISIBLE);
        binding.llMain.setVisibility(View.GONE);
        binding.llNoData.setVisibility(View.GONE);
        String surl = AppData.url +"Leave/Odometerapprovaldata?CompanyID="+pref.getEmpClintId()+"&ApproverID="+pref.getEmpId()+"&Startdate="+startDate+"&Enddate="+endDate+"&SecurityCode=0000";
        Log.d("inputLeaveBlanace", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        itemList.clear();

                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String PUNCHINGDATE = obj.optString("PUNCHINGDATE");
                                    String Journey_Start_Reading = obj.optString("Journey_Start_Reading");
                                    String Journey_Start_Image = obj.optString("Journey_Start_Image");
                                    String Journey_End_Reading = obj.optString("Journey_End_Reading");
                                    String Journey_End_Image = obj.optString("Journey_End_Image");
                                    String Total_Distance = obj.optString("Total_Distance");
                                    String Approval_Status = obj.optString("Approval_Status");


                                    ODOMeterReadingModule obj2 = new ODOMeterReadingModule();
                                    obj2.setDate(PUNCHINGDATE);
                                    obj2.setDistance(Total_Distance);
                                    obj2.setJrstrtreading(Journey_Start_Reading);
                                    obj2.setJrendreading(Journey_End_Reading);
                                    obj2.setJrendimage(Journey_End_Image);
                                    obj2.setJrstrtimage(Journey_Start_Image);
                                    obj2.setApprovalstatus(Approval_Status);
                                    itemList.add(obj2);


                                }
                                setAdapter();
                                binding.llLoader.setVisibility(View.GONE);
                                binding.llMain.setVisibility(View.VISIBLE);
                                binding.llNoData.setVisibility(View.GONE);

                            } else {
                                binding.llLoader.setVisibility(View.GONE);
                                binding.llMain.setVisibility(View.GONE);
                                binding.llNoData.setVisibility(View.VISIBLE);
                                //Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                binding.llLoader.setVisibility(View.VISIBLE);
                binding.llMain.setVisibility(View.GONE);
                binding.llNoData.setVisibility(View.GONE);

                Toast.makeText(ODOMeterReadingReportActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(ODOMeterReadingReportActivity.this);
        requestQueue.add(stringRequest);

    }
    private void setAdapter(){
        ODOMeterReadingAdapter detailsAdpater=new ODOMeterReadingAdapter(itemList, ODOMeterReadingReportActivity.this);
        binding.rvItem.setAdapter(detailsAdpater);
    }
}