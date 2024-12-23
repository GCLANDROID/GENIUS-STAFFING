package io.cordova.myapp00d753.activity;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.ShowDialog;

public class WeeklyOffAttendanceActivity extends AppCompatActivity {
    LinearLayout llDate;
    TextView tvDate;
    ImageView imgBack, imgHome;
    LinearLayout llSubmit;
    Pref pref;
    String newdate;
    private AlertDialog alertDialog, alerDialog1, al2;
    String responseText;
    NetworkConnectionCheck connectionCheck;
    TextView tvMan;
    String ApproverStatus;
    LinearLayout llTick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weeklyoff);
        initialize();
        onclick();
    }

    private void initialize() {
        pref = new Pref(getApplicationContext());
        connectionCheck = new NetworkConnectionCheck(this);
        llDate = (LinearLayout) findViewById(R.id.llDate);
        tvDate = (TextView) findViewById(R.id.tvDate);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        llSubmit = (LinearLayout) findViewById(R.id.llSubmit);
        tvMan = (TextView) findViewById(R.id.tvMan);


    }

    private void onclick() {
        llDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                c.add(Calendar.DAY_OF_MONTH, -7);


                final DatePickerDialog dialog = new DatePickerDialog(WeeklyOffAttendanceActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {


                        newdate = y + "-" + (m + 1) + "-" + d;

                        tvDate.setVisibility(View.VISIBLE);
                        tvDate.setText(newdate);

                    }
                }, year, month, day);
                dialog.getDatePicker();
                dialog.show();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeeklyOffAttendanceActivity.this, EmployeeDashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //finish();
            }
        });

        llSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newdate != null) {
                    if (connectionCheck.isNetworkAvailable()) {
                        attendance();
                        tvMan.setVisibility(View.GONE);
                    } else {
                        connectionCheck.getNetworkActiveAlert().show();
                    }

                } else {
                    Toast.makeText(WeeklyOffAttendanceActivity.this, "please select date", Toast.LENGTH_LONG).show();
                }


            }
        });


    }


    private void attendance() {
        ProgressDialog progressDialog = new ProgressDialog(WeeklyOffAttendanceActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        AndroidNetworking.upload(AppData.url + "get_GCLSelfAttendanceWoLeave/WOApplicationSave")
                .addMultipartParameter("Aid", "0")
                .addMultipartParameter("EmployeeId", pref.getEmpId())
                .addMultipartParameter("AttType", "WO")
                .addMultipartParameter("ApprovalStatus", "1")
                .addMultipartParameter("Operation", "7")
                .addMultipartParameter("Adate", newdate)
                .addMultipartParameter("SecurityCode","0000")
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

                        progressDialog.dismiss();
                        JSONObject job = response;
                        responseText = job.optString("responseText");
                        boolean responseStatus = job.optBoolean("responseStatus");
                        if (responseStatus){
                            successAlert(responseText);
                        } else {
                            ShowDialog.showErrorDialog(WeeklyOffAttendanceActivity.this, responseText);
                        }

                        // boolean _status = job1.getBoolean("status");
                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        progressDialog.dismiss();
                        Toast.makeText(WeeklyOffAttendanceActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();

                    }
                });
    }

    private void showAlertforFalse(String text) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(WeeklyOffAttendanceActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_invaliddate, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvInvalidDialog);
        if (responseText.equals("")) {
            tvInvalidDate.setText(text);
        } else {
            tvInvalidDate.setText(text);
        }
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(true);
        Window window = alertDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog.show();

    }


    private void successAlert(String text) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(WeeklyOffAttendanceActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvSuccess = (TextView) dialogView.findViewById(R.id.tvSuccess);
        tvSuccess.setText(text);
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(WeeklyOffAttendanceActivity.this,EmployeeDashBoardActivity.class);
                startActivity(intent);
                finish();
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





    private void showAlertforFalse1(String text) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(WeeklyOffAttendanceActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_invaliddate, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvInvalidDialog);
        tvInvalidDate.setText(text);

        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                al2.dismiss();
                llSubmit.setVisibility(View.GONE);
            }
        });

        al2 = dialogBuilder.create();
        al2.setCancelable(true);
        Window window = al2.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        al2.show();

    }


}
