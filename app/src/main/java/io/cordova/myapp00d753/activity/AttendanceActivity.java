package io.cordova.myapp00d753.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;
import io.cordova.myapp00d753.utility.Pref;

public class AttendanceActivity extends AppCompatActivity {
    LinearLayout llAttandanceManage,llAttendanceReport,llBackAttendance,llOnLeave,llWeekly,llApproval;
    ImageView imgBack,imgHome;
    android.support.v7.app.AlertDialog alerDialog1;
    String month,year;
    int y;
    Pref pref;
    NetworkConnectionCheck connectionCheck;
    int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        initialize();
        onClick();
    }
    private void initialize(){
        pref=new Pref(getApplicationContext());
        connectionCheck = new NetworkConnectionCheck(this);
        llAttandanceManage=(LinearLayout)findViewById(R.id.llAttandanceManage);
        llAttendanceReport=(LinearLayout)findViewById(R.id.llAttendanceReport);
        llBackAttendance=(LinearLayout)findViewById(R.id.llBackAttendance);
        llOnLeave=(LinearLayout)findViewById(R.id.llOnLeave);
        llWeekly=(LinearLayout)findViewById(R.id.llWeekly);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        y= Calendar.getInstance().get(Calendar.YEAR);
        year=String.valueOf(y);
        Log.d("year", year);

        int m=Calendar.getInstance().get(Calendar.MONTH)+1;
        Log.d("month", String.valueOf(m));
        if (m==1){
            month="January";
        }else if (m==2){
            month="February";
        }else if (m==3){
            month="March";
        }
        else if (m==4){
            month="April";
        }else if (m==5){
            month="May";
        }else if (m==6){
            month="June";
        }else if (m==7){
            month="July";
        }
        else if (m==8){
            month="August";
        }else if (m==9){
            month="September";
        }else if (m==10){
            month="October";
        }else if (m==11){
            month="November";
        }
        else if (m==12){
            month="December";
        }

        if (pref.getWeeklyoff().equals("1")){
            llWeekly.setVisibility(View.VISIBLE);
        }else {
            llWeekly.setVisibility(View.GONE);
        }

        if (pref.getOnLeave().equals("1")){
            llOnLeave.setVisibility(View.VISIBLE);
        }else {
            llOnLeave.setVisibility(View.GONE);
        }

        if (pref.getBackAttd().equals("1")){
            llBackAttendance.setVisibility(View.VISIBLE);
        }else {
            llBackAttendance.setVisibility(View.GONE);
        }
        llApproval=(LinearLayout)findViewById(R.id.llApproval);
        if (pref.getSup().equals("0")){
            llApproval.setVisibility(View.GONE);
        }else {
            llApproval.setVisibility(View.VISIBLE);
        }
    }
    private void onClick(){
        llAttandanceManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionCheck.isNetworkAvailable()) {
                    Intent intent = new Intent(AttendanceActivity.this, AttendanceManageActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    connectionCheck.getNetworkActiveAlert().show();
                }
            }
        });

        llAttendanceReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionCheck.isNetworkAvailable()) {
                    Intent intent = new Intent(AttendanceActivity.this, AttendanceReportActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    connectionCheck.getNetworkActiveAlert().show();
                }
            }
        });

        llBackAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionCheck.isNetworkAvailable()) {
                    Intent intent = new Intent(AttendanceActivity.this, BackDatedAttendanceActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    connectionCheck.getNetworkActiveAlert().show();
                }
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AttendanceActivity.this,DashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
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

        llOnLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionCheck.isNetworkAvailable()) {
                    flag=1;

                   onLeavefunction();
                }else {
                    connectionCheck.getNetworkActiveAlert().show();
                }
            }
        });

        llWeekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionCheck.isNetworkAvailable()) {
                    flag=2;

                    weeklyfunction();
                }else {
                    connectionCheck.getNetworkActiveAlert().show();
                }
            }
        });

        llApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AttendanceActivity.this,AttenApprovalActivity.class);
                startActivity(intent);
            }
        });

    }

    private void onLeavefunction(){
        String surl ="http://111.93.182.174/GeniusiOSApi/api/get_GCLSelfAttendanceWoLeave?AEMConsultantID="+pref.getEmpConId()+"&AEMClientID="+pref.getEmpClintId()+"&AEMClientOfficeID="+pref.getEmpClintOffId()+"&AEMEmployeeID="+pref.getEmpId()+"&CurrentPage=1&AID=0&ApproverStatus=4&YearVal="+year+"&MonthName="+month+"&WorkingStatus=1&SecurityCode="+pref.getSecurityCode()+"&DbOperation=7&AttIds=0";
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Submating...");
        progressBar.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLeave", response);
                        progressBar.dismiss();
                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText=job1.optString("responseText");
                            boolean responseStatus=job1.optBoolean("responseStatus");
                            if (responseStatus){
                               // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                successAlert();

                            }


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AttendanceActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                Toast.makeText(AttendanceActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert",error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");

    }
    private void weeklyfunction(){
        String surl ="http://111.93.182.174/GeniusiOSApi/api/get_GCLSelfAttendanceWoLeave?AEMConsultantID="+pref.getEmpConId()+"&AEMClientID="+pref.getEmpClintId()+"&AEMClientOfficeID="+pref.getEmpClintOffId()+"&AEMEmployeeID="+pref.getEmpId()+"&CurrentPage=1&AID=0&ApproverStatus=4&YearVal="+year+"&MonthName="+month+"&WorkingStatus=1&SecurityCode="+pref.getSecurityCode()+"&DbOperation=6&AttIds=0";
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Submating...");
        progressBar.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLeave", response);
                        progressBar.dismiss();
                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText=job1.optString("responseText");
                            boolean responseStatus=job1.optBoolean("responseStatus");
                            if (responseStatus){
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                successAlert();

                            }


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AttendanceActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                Toast.makeText(AttendanceActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert",error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");

    }

    private void successAlert(){
        android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(AttendanceActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate=(TextView)dialogView.findViewById(R.id.tvSuccess);
        if (flag==1) {
            tvInvalidDate.setText("Thank you! successfully applied for Onleave");
        }
        else if (flag==2){
            tvInvalidDate.setText("Thank you! successfully applied for WeeklyOff");
        }
        Button btnOk=(Button)dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
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
}
