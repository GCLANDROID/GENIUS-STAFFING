package io.cordova.myapp00d753.activity;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
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

public class NotificationManageActivity extends AppCompatActivity {
    ImageView imgBack, imgHome;
    EditText etTitle, etBody;
    LinearLayout llSDate, llEDate;
    TextView tvSDate, tvEDate;
    String sdate;
    String edate;
    Button btnSend;
    Pref pref;
    android.support.v7.app.AlertDialog alerDialog1;
    int s, e;
    NetworkConnectionCheck connectionCheck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_manage);
        initialize();
        onClick();
    }

    private void initialize() {
        pref = new Pref(NotificationManageActivity.this);
        connectionCheck=new NetworkConnectionCheck(NotificationManageActivity.this);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        etTitle = (EditText) findViewById(R.id.etTitle);
        etBody = (EditText) findViewById(R.id.etBody);
        llSDate = (LinearLayout) findViewById(R.id.llSDate);
        llEDate = (LinearLayout) findViewById(R.id.llEDate);
        tvSDate = (TextView) findViewById(R.id.tvSDate);
        tvEDate = (TextView) findViewById(R.id.tvEDate);
        btnSend = (Button) findViewById(R.id.btnsend);
    }

    private void onClick() {
        llSDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int sday = c.get(Calendar.DAY_OF_MONTH);
                c.add(Calendar.DAY_OF_MONTH, -7);


                final DatePickerDialog dialog = new DatePickerDialog(NotificationManageActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {

                        sdate = (m + 1) + "/" + d + "/" + y;
                        s = (m + 1) + d + y;


                        tvSDate.setText(sdate);

                    }
                }, year, month, sday);
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
            }
        });

        llEDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                c.add(Calendar.DAY_OF_MONTH, -7);


                final DatePickerDialog dialog = new DatePickerDialog(NotificationManageActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {

                        edate = (m + 1) + "/" + d + "/" + y;
                        e = (m + 1) + d + y;

                        tvEDate.setText(edate);

                    }
                }, year, month, day);
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationManageActivity.this, DashBoardActivity.class);
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

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (s!=0) {
                    if (e!=0) {
                        if (s<=e) {
                            if (etTitle.getText().toString().length()>0){
                                if (etBody.getText().toString().length()>0){
                                    if (connectionCheck.isNetworkAvailable()){
                                        postNotification();
                                    }else {
                                        connectionCheck.getNetworkActiveAlert().show();
                                    }

                                }else {
                                    Toast.makeText(NotificationManageActivity.this, "please enter body of message", Toast.LENGTH_LONG).show();

                                }

                            }else {
                                Toast.makeText(NotificationManageActivity.this, "please enter title of message", Toast.LENGTH_LONG).show();

                            }


                        } else {
                            Toast.makeText(NotificationManageActivity.this, "start date should be less than end date", Toast.LENGTH_LONG).show();

                        }

                    } else {
                        Toast.makeText(NotificationManageActivity.this, "please select end date", Toast.LENGTH_LONG).show();

                    }

                } else {
                    Toast.makeText(NotificationManageActivity.this, "please select start date", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void postNotification() {
        String surl = "http://111.93.182.174/GeniusiOSApi/api/gcl_Notification?MsgMasterId=0&AEMClientID=" + pref.getEmpClintId() + "&AEMEmployeeID=" + pref.getEmpId() + "&StartDate=" + sdate + "&EndDate=" + edate + "&Tagline=" + etTitle.getText().toString().replaceAll("\\s+", "") + "&Description=" + etBody.getText().toString().replaceAll("\\s+", "") + "&CurrentPage=1&ApprovalStatus=0&Operation=3&SecurityCode="+pref.getSecurityCode();
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Submating...");
        progressBar.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLeave", response);
                        progressBar.dismiss();
                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {

                                successAlert();

                            } else {
                                Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();
                            }


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(NotificationManageActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                Toast.makeText(NotificationManageActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");

    }

    private void successAlert() {
        android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(NotificationManageActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);


        tvInvalidDate.setText("Notification sent successfully");

        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
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
