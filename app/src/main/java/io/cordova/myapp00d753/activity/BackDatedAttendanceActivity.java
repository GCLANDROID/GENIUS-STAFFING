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


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;
import io.cordova.myapp00d753.utility.Pref;

public class BackDatedAttendanceActivity extends AppCompatActivity {
    LinearLayout llDate;
    TextView tvDate;
    ImageView imgBack, imgHome;
    LinearLayout llSubmit;
    Pref pref;
    String newdate;
    private AlertDialog alertDialog, alerDialog1,al2;
    String responseText;
    NetworkConnectionCheck connectionCheck;
    TextView tvMan;
    String ApproverStatus;
    LinearLayout llTick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_dated);
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


                final DatePickerDialog dialog = new DatePickerDialog(BackDatedAttendanceActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {

                        newdate = (m + 1) + "/" + d + "/" + y;

                        tvDate.setVisibility(View.VISIBLE);
                        tvDate.setText(newdate);

                    }
                }, year, month, day);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
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
                Intent intent = new Intent(BackDatedAttendanceActivity.this, EmployeeDashBoardActivity.class);
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
                        attendanceCheck();
                        tvMan.setVisibility(View.GONE);
                    } else {
                        connectionCheck.getNetworkActiveAlert().show();
                    }

                } else {
                    Toast.makeText(BackDatedAttendanceActivity.this, "please select date", Toast.LENGTH_LONG).show();
                }


            }
        });


    }


    public void backDatedAttendance() {
        String surl = AppData.url+"get_GCLSelfAttendanceWoLeave?AEMEmployeeID=" + pref.getEmpId() + "&Adate=" + newdate + "&DbOperation=5&SecurityCode=" + pref.getSecurityCode();
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();
        Log.d("backattendanceurl",surl);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        progressBar.dismiss();
                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                               // Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();
                                successAlert(responseText);


                            } else {
                                showAlertforFalse(responseText);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(BackDatedAttendanceActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
              //  Toast.makeText(BackDatedAttendanceActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");

    }

    private void showAlertforFalse(String text) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(BackDatedAttendanceActivity.this, R.style.CustomDialogNew);
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(BackDatedAttendanceActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvSuccess = (TextView) dialogView.findViewById(R.id.tvSuccess);
        tvSuccess.setText(text);
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attendanceCheck();
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


    private void attendanceCheck() {
        String surl = AppData.url+"gcl_FetchEmployeeAttendanceByDate?AEMEmployeeID=" + pref.getEmpId() + "&Adate=" + newdate + "&CurrentPage=1&Operation=1&SecurityCode=" + pref.getSecurityCode();
        Log.d("attencinput", surl);
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
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

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                String toastText = job1.optString("responseText");
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    ApproverStatus = obj.optString("ApproverStatus");
                                    if (!ApproverStatus.equals("1")){
                                        backDatedAttendance();
                                    }else {
                                        showAlertforFalse1(toastText);
                                    }



                                }


                            } else {
                                backDatedAttendance();

                            }


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(BackDatedAttendanceActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                Toast.makeText(BackDatedAttendanceActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");

    }


    private void showAlertforFalse1(String text) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(BackDatedAttendanceActivity.this, R.style.CustomDialogNew);
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
