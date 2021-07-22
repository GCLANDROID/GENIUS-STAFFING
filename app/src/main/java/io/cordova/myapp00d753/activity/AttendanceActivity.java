package io.cordova.myapp00d753.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.DatabaseHelper;
import io.cordova.myapp00d753.utility.GPSTracker;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;
import io.cordova.myapp00d753.utility.Pref;

public class AttendanceActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    LinearLayout llAttandanceManage, llAttendanceReport, llBackAttendance, llOnLeave, llWeekly, llApproval, llWeekly1,llFCAttendance;
    ImageView imgBack, imgHome;
    AlertDialog alerDialog1;
    String month, year;
    int y;
    Pref pref;
    NetworkConnectionCheck connectionCheck;
    int flag;
    String formattedDate;
    String AttendanceType;
    String ApproverStatus;
    GoogleApiClient googleApiClient;
    GPSTracker gps;
    double latitude,longitude;
    private DatabaseHelper db;
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
    LinearLayout llDemo,llManageD,llReportD,llFaceD,llMain;
    AlertDialog alertDialog;
    String dateD;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        initialize();
        onClick();
        attendanceCheck();
    }

    private void initialize() {
        pref = new Pref(getApplicationContext());
        connectionCheck = new NetworkConnectionCheck(this);
        llAttandanceManage = (LinearLayout) findViewById(R.id.llAttandanceManage);
        llAttendanceReport = (LinearLayout) findViewById(R.id.llAttendanceReport);
        llBackAttendance = (LinearLayout) findViewById(R.id.llBackAttendance);
        llOnLeave = (LinearLayout) findViewById(R.id.llOnLeave);
        llWeekly = (LinearLayout) findViewById(R.id.llWeekly);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        llWeekly1 = (LinearLayout) findViewById(R.id.llWeekly1);
        llFCAttendance = (LinearLayout) findViewById(R.id.llFCAttendance);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        formattedDate = df.format(c);


        Date cd = Calendar.getInstance().getTime();
        SimpleDateFormat dfd = new SimpleDateFormat("dd/MM/yyyy");
        dateD = dfd.format(cd);


        y = Calendar.getInstance().get(Calendar.YEAR);
        year = String.valueOf(y);
        Log.d("year", year);

        int m = Calendar.getInstance().get(Calendar.MONTH) + 1;
        Log.d("month", String.valueOf(m));
        if (m == 1) {
            month = "January";
        } else if (m == 2) {
            month = "February";
        } else if (m == 3) {
            month = "March";
        } else if (m == 4) {
            month = "April";
        } else if (m == 5) {
            month = "May";
        } else if (m == 6) {
            month = "June";
        } else if (m == 7) {
            month = "July";
        } else if (m == 8) {
            month = "August";
        } else if (m == 9) {
            month = "September";
        } else if (m == 10) {
            month = "October";
        } else if (m == 11) {
            month = "November";
        } else if (m == 12) {
            month = "December";
        }

        if (pref.getWeeklyoff().equals("1")) {
            llWeekly.setVisibility(View.VISIBLE);

        } else {
            llWeekly.setVisibility(View.GONE);
            llWeekly1.setVisibility(View.GONE);
        }

        if (pref.getOnLeave().equals("1")) {
            llOnLeave.setVisibility(View.VISIBLE);
            llWeekly.setVisibility(View.VISIBLE);

        } else {
            llOnLeave.setVisibility(View.GONE);
            llWeekly.setVisibility(View.GONE);


        }

        if (pref.getBackAttd().equals("1")) {
            llBackAttendance.setVisibility(View.VISIBLE);

        } else {
            llBackAttendance.setVisibility(View.GONE);
            llWeekly.setVisibility(View.GONE);
        }
        llApproval = (LinearLayout) findViewById(R.id.llApproval);
        if (pref.getSup().equals("0")) {
            llApproval.setVisibility(View.GONE);
        } else {
            llApproval.setVisibility(View.VISIBLE);
        }

        gps = new GPSTracker(AttendanceActivity.this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            Log.d("saikatdas", String.valueOf(latitude));
            longitude = gps.getLongitude();
        } else {
// can't get location
// GPS or Network is not enabled
// Ask user to enable GPS/network in settings

        }

        llMain=(LinearLayout)findViewById(R.id.llMain);
        llDemo=(LinearLayout)findViewById(R.id.llDemo);
        llManageD=(LinearLayout)findViewById(R.id.llManageD);
        llReportD=(LinearLayout)findViewById(R.id.llReportD);
        llFaceD=(LinearLayout)findViewById(R.id.llFaceD);

        if (pref.getDemoFlag().equals("1")){
            llFCAttendance.setVisibility(View.VISIBLE);
            llMain.setVisibility(View.GONE);
            llDemo.setVisibility(View.VISIBLE);

        }else {
            llFCAttendance.setVisibility(View.GONE);
            llMain.setVisibility(View.VISIBLE);
            llDemo.setVisibility(View.GONE);
        }



    }

    private void onClick() {
        llAttandanceManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                       if (!ApproverStatus.equals("1")) {
                           if (!pref.getOffAttnFlag().equals("1")) {

                               Intent intent = new Intent(AttendanceActivity.this, AttendanceManageActivity.class);
                               intent.putExtra("intt","2");
                               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                               startActivity(intent);
                           } else {
                               Intent intent = new Intent(AttendanceActivity.this, AttendanceManageActivity.class);
                               intent.putExtra("intt","2");
                               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                               startActivity(intent);
                           }
                       } else {
                           atteAlert();

                       }


            }
        });


        llManageD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!ApproverStatus.equals("1")) {
                    if (!pref.getOffAttnFlag().equals("1")) {

                        Intent intent = new Intent(AttendanceActivity.this, AttendanceManageActivity.class);
                        intent.putExtra("intt","2");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(AttendanceActivity.this, AttendanceManageActivity.class);
                        intent.putExtra("intt","2");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                } else {
                    atteAlert();
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
                } else {
                    if (pref.getOffAttnFlag().equals("1")) {
                        Intent intent = new Intent(AttendanceActivity.this, OfflineAttenReportActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else {
                        connectionCheck.getNetworkActiveAlert().show();
                    }
                }
            }
        });


        llReportD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionCheck.isNetworkAvailable()) {

                    Intent intent = new Intent(AttendanceActivity.this, AttendanceReportActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    if (pref.getOffAttnFlag().equals("1")) {
                        Intent intent = new Intent(AttendanceActivity.this, OfflineAttenReportActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else {
                        connectionCheck.getNetworkActiveAlert().show();
                    }
                }
            }
        });
        llFCAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AttendanceActivity.this,FRDashboard.class);
                intent.putExtra("intt","1");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        llFaceD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AttendanceActivity.this,FRDashboard.class);
                intent.putExtra("intt","1");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        llBackAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionCheck.isNetworkAvailable()) {


                    Intent intent = new Intent(AttendanceActivity.this, BackDatedAttendanceActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    connectionCheck.getNetworkActiveAlert().show();
                }
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AttendanceActivity.this, EmployeeDashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //  finish();
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
                    flag = 1;

                    if (!ApproverStatus.equals("1")) {

                        if (!AttendanceType.equals("L")) {
                            if (!AttendanceType.equals("A")){
                                onLeavefunction();
                            }else {
                                showAlertforLeave();
                            }



                        } else {
                            Toast.makeText(AttendanceActivity.this, "You already applied", Toast.LENGTH_LONG).show();

                        }
                    } else {
                        atteAlert();                    }
                } else {
                    connectionCheck.getNetworkActiveAlert().show();
                }
            }
        });

        llWeekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionCheck.isNetworkAvailable()) {
                    flag = 2;

                    if (!ApproverStatus.equals("1")) {

                        if (!AttendanceType.equals("WO")) {
                            if (!AttendanceType.equals("A")){
                                weeklyfunction();
                            }else {
                                //show dialog
                                showAlertforWeekly();
                            }



                        } else {
                            Toast.makeText(AttendanceActivity.this, "You already applied", Toast.LENGTH_LONG).show();

                        }
                    } else {
                        atteAlert();
                    }
                } else {
                    connectionCheck.getNetworkActiveAlert().show();
                }
            }
        });

        llWeekly1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionCheck.isNetworkAvailable()) {
                    flag = 2;


                    if (!AttendanceType.equals("WO")) {
                        if (!ApproverStatus.equals("1")) {

                            weeklyfunction();
                        } else {
                            Toast.makeText(AttendanceActivity.this, "Your attendance approved by admin", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(AttendanceActivity.this, "You already applied", Toast.LENGTH_LONG).show();
                    }
                } else {
                    connectionCheck.getNetworkActiveAlert().show();
                }
            }
        });

        llApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AttendanceActivity.this, AttenApprovalActivity.class);
                startActivity(intent);
            }
        });

    }

    private void onLeavefunction() {
        String surl = AppData.url+"get_GCLSelfAttendanceWoLeave?AEMConsultantID=" + pref.getEmpConId() + "&AEMClientID=" + pref.getEmpClintId() + "&AEMClientOfficeID=" + pref.getEmpClintOffId() + "&AEMEmployeeID=" + pref.getEmpId() + "&CurrentPage=1&AID=0&ApproverStatus=4&YearVal=" + year + "&MonthName=" + month + "&WorkingStatus=1&SecurityCode=" + pref.getSecurityCode() + "&DbOperation=7&AttIds=0";
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
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                successAlert(responseText);
                                attendanceCheck();

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
                Toast.makeText(AttendanceActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");

    }

    private void weeklyfunction() {
        String surl = AppData.url+"get_GCLSelfAttendanceWoLeave?AEMConsultantID=" + pref.getEmpConId() + "&AEMClientID=" + pref.getEmpClintId() + "&AEMClientOfficeID=" + pref.getEmpClintOffId() + "&AEMEmployeeID=" + pref.getEmpId() + "&CurrentPage=1&AID=0&ApproverStatus=4&YearVal=" + year + "&MonthName=" + month + "&WorkingStatus=1&SecurityCode=" + pref.getSecurityCode() + "&DbOperation=6&AttIds=0";
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
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                successAlert(responseText);
                                attendanceCheck();

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
                Toast.makeText(AttendanceActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");

    }

    private void successAlert(String text) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AttendanceActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);
        if (flag == 1) {
            tvInvalidDate.setText(text);
        } else if (flag == 2) {
            tvInvalidDate.setText(text);
        }
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

    private void attendanceCheck() {
        String surl = AppData.url+"gcl_FetchEmployeeAttendanceByDate?AEMEmployeeID=" + pref.getEmpId() + "&Adate=" + formattedDate + "&CurrentPage=1&Operation=1&SecurityCode=" + pref.getSecurityCode();
        Log.d("attencinput", surl);
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responsecheck", response);
                        progressBar.dismiss();
                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);

                            boolean responseStatus = job1.optBoolean("responseStatus");

                                String toastText = job1.optString("responseText");
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    AttendanceType = obj.optString("AttendanceType");
                                    pref.saveAtteType(AttendanceType);
                                    ApproverStatus = obj.optString("ApproverStatus");


                                }





                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Intent intent=new Intent(AttendanceActivity.this,EmployeeDashBoardActivity.class);
                            startActivity(intent);
                            Toast.makeText(AttendanceActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                Toast.makeText(AttendanceActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");

    }

    private void turnGPSOn() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(AttendanceActivity.this).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            // **************************
            builder.setAlwaysShow(true); // this is the key ingredient
            // **************************

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                    .checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result
                            .getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            Intent intent = new Intent(AttendanceActivity.this, AttendanceReportActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                try {
                                    status.startResolutionForResult(AttendanceActivity.this, 1000);
                                } catch (IntentSender.SendIntentException e) {
                                    // Ignore the error.
                                }
                            } catch (Exception e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                            break;
                    }
                }
            });
        }
    }

    @Override

    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



    private void showAlertforLeave() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("If you mark Leave then your Attendance punch will be overrulled");
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        onLeavefunction();
                        arg0.dismiss();
                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.show();
        alertDialogBuilder.setCancelable(false);


    }


    private void showAlertforWeekly() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("If you mark WEEKLY OFF then your ATTENDANCE punch will be overrulled");
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        weeklyfunction();
                        arg0.dismiss();
                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.show();
        alertDialogBuilder.setCancelable(false);


    }

    private void atteAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AttendanceActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_attendate, null);
        dialogBuilder.setView(dialogView);
        TextView tvAttenDate = (TextView) dialogView.findViewById(R.id.tvAttenDate);
        tvAttenDate.setText("Your attendance has been approved on "+dateD);
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
}
