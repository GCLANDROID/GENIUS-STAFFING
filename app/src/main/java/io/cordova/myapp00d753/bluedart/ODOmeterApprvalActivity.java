package io.cordova.myapp00d753.bluedart;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.AttendanceReportActivity;
import io.cordova.myapp00d753.activity.SuperVisiorDashBoardActivity;
import io.cordova.myapp00d753.adapter.ODOMeterApprovalAdapter;
import io.cordova.myapp00d753.adapter.ODOMeterReadingAdapter;
import io.cordova.myapp00d753.databinding.ActivityOdometerApprvalBinding;
import io.cordova.myapp00d753.module.ODOMeterApprovalModule;
import io.cordova.myapp00d753.module.ODOMeterReadingModule;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

public class ODOmeterApprvalActivity extends AppCompatActivity {
    ActivityOdometerApprvalBinding binding;
    ODOMeterApprovalAdapter detailsAdpater;
    ArrayList<ODOMeterApprovalModule>itemList=new ArrayList<>();
    String startDate="",endDate="";
    int allclick=0;
    ArrayList<Integer>item1=new ArrayList<>();
    String attId="";
    Pref pref;
    AlertDialog alerDialog1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_odometer_apprval);
        pref=new Pref(ODOmeterApprvalActivity.this);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(ODOmeterApprvalActivity.this, LinearLayoutManager.VERTICAL, false);
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

        binding.llClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.llSelect.getVisibility()==View.GONE){
                    binding.llSelect.setVisibility(View.VISIBLE);
                    allclick=1;
                    detailsAdpater.selectAll();
                }else {
                    binding.llSelect.setVisibility(View.GONE);
                    detailsAdpater.unselectall();
                    allclick=0;
                    item1.clear();
                }
            }
        });

        binding.btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!startDate.equals("")) {
                    if (!endDate.equals("")) {
                        getItem();
                    }else {
                        Toast.makeText(ODOmeterApprvalActivity.this,"Please select End Date",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(ODOmeterApprvalActivity.this,"please select Start Date",Toast.LENGTH_LONG).show();
                }
            }
        });

        binding.btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item1.size()>0){
                    odometerApproval("1");
                }else {
                    Toast.makeText(ODOmeterApprvalActivity.this,"Please select item",Toast.LENGTH_LONG).show();
                }
            }
        });

        binding.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item1.size()>0){
                    odometerApproval("-1");
                }else {
                    Toast.makeText(ODOmeterApprvalActivity.this,"Please select item",Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    private void showStrtDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(ODOmeterApprvalActivity.this,
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


        DatePickerDialog datePickerDialog = new DatePickerDialog(ODOmeterApprvalActivity.this,
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
        String surl = AppData.url +"Leave/Odometerapprovaldata?CompanyID=aaa&ApproverID="+pref.getEmpId()+"&Startdate="+startDate+"&Enddate="+endDate+"&SecurityCode=0000";
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
                                    String PUNCHINGDATE= obj.optString("PUNCHINGDATE");
                                    String ODOMETERREADING = obj.optString("ODOMETERREADING");
                                    String JOURNEYTYPE = obj.optString("JOURNEYTYPE");
                                    String IMAGENAME = obj.optString("IMAGENAME");
                                    String Code=obj.optString("Code");
                                    int AID = obj.optInt("AID");
                                    item1.add(AID);
                                    ODOMeterApprovalModule obj2 = new ODOMeterApprovalModule();
                                    obj2.setDate(PUNCHINGDATE);
                                    obj2.setImageData(IMAGENAME);
                                    obj2.setJrType(JOURNEYTYPE);
                                    obj2.setOdometereading(ODOMETERREADING);
                                    obj2.setAid(AID);
                                    obj2.setName(Code);

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

                Toast.makeText(ODOmeterApprvalActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(ODOmeterApprvalActivity.this);
        requestQueue.add(stringRequest);

    }
    private void setAdapter(){
         detailsAdpater=new ODOMeterApprovalAdapter(itemList, ODOmeterApprvalActivity.this);
        binding.rvItem.setAdapter(detailsAdpater);
    }


    public void updateAttendanceStatus(int position, boolean status) {
        item1.clear();
        itemList.get(position).setSelected(status);
        if (itemList.get(position).isSelected()==true) {
            item1.add(itemList.get(position).getAid());
        }else {
            item1.clear();
        }


        detailsAdpater.notifyDataSetChanged();
    }


    private void odometerApproval(String reading) {
        if (allclick==1){
            attId=item1.toString().replace("[","").replace("]","").replaceAll("\\s+", "");
        }else {
            attId = item1.toString().replace("[","").replace("]","").replaceAll("\\s+", "");
        }
        ProgressDialog pd = new ProgressDialog(ODOmeterApprvalActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.upload(AppData.url + "Leave/ApproveODOMeter")
                .addMultipartParameter("ApplicationMID", attId)
                .addMultipartParameter("ApproverID",pref.getEmpId() )
                .addMultipartParameter("Approvalstatus", reading)
                .addMultipartParameter("SecurityCode", "0000")
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
                        String responseText = job.optString("responseText");
                        boolean responseStatus = job.optBoolean("responseStatus");
                        if (responseStatus) {
                            if (reading.equals("1")){
                                successAlert("KM Reading has been successfully approved");
                            }else {
                                successAlert("KM Reading has been successfully rejected");
                            }

                        } else {
                            Toast.makeText(ODOmeterApprvalActivity.this, responseText, Toast.LENGTH_LONG).show();
                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        pd.dismiss();
                        Toast.makeText(ODOmeterApprvalActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();

                    }
                });
    }


    private void successAlert(String text) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ODOmeterApprvalActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);

        tvInvalidDate.setText(text);

        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();

                    Intent intent = new Intent(ODOmeterApprvalActivity.this, SuperVisiorDashBoardActivity.class);
                    startActivity(intent);
                    finish();


            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }
}