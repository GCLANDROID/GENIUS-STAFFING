package io.cordova.myapp00d753.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.AttendanceAdapter;
import io.cordova.myapp00d753.adapter.AttendanceCalenderAdapter;
import io.cordova.myapp00d753.module.AttendanceCalenderModel;
import io.cordova.myapp00d753.module.AttendanceModule;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

public class AttenDanceDashboardActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView rvItem;
    ArrayList<AttendanceCalenderModel> itemList = new ArrayList<>();
    Pref pref;
    DrawerLayout dlMain;
    boolean mslideState;
    ImageView imgMenu;
    LinearLayout llAttandanceManage, llAttendanceReport, llBackAttendance, llWeekly;
    AlertDialog alerDialog1;
    String month, year;
    int y ,m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atten_dance_dashboard);
        initView();
    }

    private void initView() {
        dlMain = (DrawerLayout) findViewById(R.id.dlMain);
        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        pref = new Pref(AttenDanceDashboardActivity.this);
        rvItem.setLayoutManager(new GridLayoutManager(this, 3));
        imgMenu = (ImageView) findViewById(R.id.imgMenu);
        imgMenu.setOnClickListener(this);
        dlMain.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {

            }

            @Override
            public void onDrawerOpened(@NonNull View view) {
                mslideState = true;

            }

            @Override
            public void onDrawerClosed(@NonNull View view) {
                mslideState = false;

            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });
        llAttandanceManage = (LinearLayout) findViewById(R.id.llAttandanceManage);
        llAttendanceReport = (LinearLayout) findViewById(R.id.llAttendanceReport);
        llBackAttendance = (LinearLayout) findViewById(R.id.llBackAttendance);
        llWeekly = (LinearLayout) findViewById(R.id.llWeekly);

        if (pref.getOnLeave().equals("1")) {

            llWeekly.setVisibility(View.VISIBLE);

        } else {

            llWeekly.setVisibility(View.GONE);


        }

        if (pref.getBackAttd().equals("1")) {
            llBackAttendance.setVisibility(View.VISIBLE);

        } else {
            llBackAttendance.setVisibility(View.GONE);

        }

        llAttandanceManage.setOnClickListener(this);
        llAttendanceReport.setOnClickListener(this);
        llWeekly.setOnClickListener(this);
        llBackAttendance.setOnClickListener(this);

        y = Calendar.getInstance().get(Calendar.YEAR);
        year = String.valueOf(y);
        Log.d("year", year);

         m = Calendar.getInstance().get(Calendar.MONTH) + 1;
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



    }


    private void getAttendanceList() {
        ProgressDialog pd = new ProgressDialog(AttenDanceDashboardActivity.this);
        pd.setMessage("Loading...");
        pd.show();
        pd.setCancelable(false);
        String surl = AppData.url + "gcl_Attendancecalender/Get?AemEmployeeid=" + pref.getEmpId() + "&AemClientid=" + pref.getEmpClintId() + "&Monthid="+m+"&yearid="+year+"&SecurityCode=" + pref.getSecurityCode();
        Log.d("input", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        pd.dismiss();
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
                                    String Date = obj.optString("Date");
                                    String PunchTiming = obj.optString("PunchTiming");
                                    String Day = obj.optString("Address");
                                    String Status = obj.optString("Status");

                                    AttendanceCalenderModel obj2 = new AttendanceCalenderModel();
                                    obj2.setDate(Date);
                                    obj2.setStatus(Status);
                                    obj2.setTime(PunchTiming);
                                    obj2.setDay(Day);
                                    itemList.add(obj2);


                                }

                                setAdapter();


                            } else {

                                //   Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();

                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
    }

    private void setAdapter() {
        AttendanceCalenderAdapter attendanceAdapter = new AttendanceCalenderAdapter(itemList);
        rvItem.setAdapter(attendanceAdapter);
    }

    @Override
    public void onClick(View view) {
        if (view == imgMenu) {
            dlMain.openDrawer(Gravity.LEFT);
        } else if (view == llAttandanceManage) {


            Intent intent = new Intent(AttenDanceDashboardActivity.this, AttendanceManageActivity.class);
            intent.putExtra("intt", "2");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }else if (view==llAttendanceReport){
            Intent intent = new Intent(AttenDanceDashboardActivity.this, AttendanceReportActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if (view==llBackAttendance){
            Intent intent = new Intent(AttenDanceDashboardActivity.this, BackDatedAttendanceActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if (view==llWeekly){
           weeklyfunction();
        }


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


                            }


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AttenDanceDashboardActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                Toast.makeText(AttenDanceDashboardActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");

    }

    private void successAlert(String text) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AttenDanceDashboardActivity.this, R.style.CustomDialogNew);
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
            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAttendanceList();
    }
}