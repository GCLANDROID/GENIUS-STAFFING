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
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.naishadhparmar.zcustomcalendar.CustomCalendar;
import org.naishadhparmar.zcustomcalendar.OnDateSelectedListener;
import org.naishadhparmar.zcustomcalendar.OnNavigationButtonClickedListener;
import org.naishadhparmar.zcustomcalendar.Property;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.AttendanceAdapter;
import io.cordova.myapp00d753.adapter.AttendanceCalenderAdapter;
import io.cordova.myapp00d753.adapter.DaywiseAttendanceCalenderAdapter;
import io.cordova.myapp00d753.module.AttendanceCalenderModel;
import io.cordova.myapp00d753.module.AttendanceModule;
import io.cordova.myapp00d753.module.SpineerItemModel;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.Util;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class AttenDanceDashboardActivity extends AppCompatActivity implements View.OnClickListener , OnNavigationButtonClickedListener {
    RecyclerView rvItem, rvSun, rvMon, rvTue, rvWed, rvThu, rvFri, rvSat;
    ArrayList<AttendanceCalenderModel> itemList = new ArrayList<>();

    Pref pref;
    DrawerLayout dlMain;
    boolean mslideState;
    ImageView imgMenu;
    LinearLayout llAttandanceManage, llAttendanceReport, llBackAttendance, llWeekly, llAttenRegularize, llBottom;
    AlertDialog alerDialog1;
    String month, year;
    int y, m;
    ImageView imgSearch;
    AlertDialog searchDialog;
    int futYear, pastYear;
    TextView tvCancel;
    String currentDate;
    Button btnMarkAttendance;
    LinearLayout llQR;
    CustomCalendar customCalendar;
    JSONArray attendanceArray;
    Button btnLeave;
    ArrayList<String>presentDays=new ArrayList<>();
    ArrayList<String>dateList=new ArrayList<>();

    TextView tvPresent,tvDetails,tvOK;
    LinearLayout lnStatus,llAdjustment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atten_dance_dashboard);
        initView();
    }

    private void initView() {
        btnLeave=(Button)findViewById(R.id.btnLeave);
        llQR = (LinearLayout) findViewById(R.id.llQR);
        llQR.setOnClickListener(this);
        btnMarkAttendance = (Button) findViewById(R.id.btnMarkAttendance);
        btnMarkAttendance.setOnClickListener(this);
        imgSearch = (ImageView) findViewById(R.id.imgSearch);
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
        llAdjustment=(LinearLayout)findViewById(R.id.llAdjustment);
        llAttandanceManage = (LinearLayout) findViewById(R.id.llAttandanceManage);
        llAttendanceReport = (LinearLayout) findViewById(R.id.llAttendanceReport);
        llBackAttendance = (LinearLayout) findViewById(R.id.llBackAttendance);
        llAttenRegularize = (LinearLayout) findViewById(R.id.llAttenRegularize);
        llWeekly = (LinearLayout) findViewById(R.id.llWeekly);
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        llBottom = (LinearLayout) findViewById(R.id.llBottom);
        if (pref.getEmpClintId().equals("AEMCLI0910000315")) {
            llQR.setVisibility(View.VISIBLE);
        } else {
            llQR.setVisibility(View.GONE);
        }

        if (pref.getOnLeave().equals("1")) {

            llWeekly.setVisibility(View.VISIBLE);

        } else {

            llWeekly.setVisibility(View.GONE);


        }

        if (pref.getBackAttd().equals("1")) {
            llBackAttendance.setVisibility(View.VISIBLE);
            llAttenRegularize.setVisibility(View.VISIBLE);

        } else {
            llBackAttendance.setVisibility(View.GONE);
            llAttenRegularize.setVisibility(View.GONE);
//
        }

        llAttandanceManage.setOnClickListener(this);
        llAttendanceReport.setOnClickListener(this);
        llWeekly.setOnClickListener(this);
        llBackAttendance.setOnClickListener(this);
        llAttenRegularize.setOnClickListener(this);
        llAdjustment.setOnClickListener(this);
        tvCancel.setOnClickListener(this);

        y = Calendar.getInstance().get(Calendar.YEAR);
        pastYear = y - 1;
        futYear = y + 1;
        year = String.valueOf(y);
        Log.d("year", year);

        m = Calendar.getInstance().get(Calendar.MONTH) + 1;
        imgSearch.setOnClickListener(this);

        final SimpleTooltip tooltip = new SimpleTooltip.Builder(AttenDanceDashboardActivity.this)
                .anchorView(imgMenu)
                .text("Mark & Regularize your Attendance from here")
                .gravity(Gravity.BOTTOM)
                .dismissOnOutsideTouch(true)
                .dismissOnInsideTouch(true)
                .modal(true)
                .animated(true)
                .animationDuration(2000)
                .contentView(R.layout.custom_tooltip, R.id.tv_text)
                .focusable(true)
                .build();





        tooltip.findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                if (tooltip.isShowing())
                    tooltip.dismiss();


            }
        });

        tooltip.show();

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        currentDate = month + "/" + day + "/" + year;

        customCalendar = findViewById(R.id.custom_calendar);

        // Initialize description hashmap
        HashMap<Object, Property> descHashMap = new HashMap<>();

        // Initialize default property
        Property defaultProperty = new Property();

        // Initialize default resource
        defaultProperty.layoutResource = R.layout.default_view;

        // Initialize and assign variable
        defaultProperty.dateTextViewResource = R.id.text_view;

        // Put object and property
        descHashMap.put("default", defaultProperty);

        // for current date
        Property currentProperty = new Property();
        currentProperty.layoutResource = R.layout.current_view;
        currentProperty.dateTextViewResource = R.id.text_view;
        descHashMap.put("wo", currentProperty);

        // for present date
        Property presentProperty = new Property();
        presentProperty.layoutResource = R.layout.present_view;
        presentProperty.dateTextViewResource = R.id.text_view;
        descHashMap.put("present", presentProperty);

        // For absent
        Property absentProperty = new Property();
        absentProperty.layoutResource = R.layout.absent_view;
        absentProperty.dateTextViewResource = R.id.text_view;
        descHashMap.put("absent", absentProperty);

        //holiday
        Property holidayProperty = new Property();
        holidayProperty.layoutResource = R.layout.holiday_view;
        holidayProperty.dateTextViewResource = R.id.text_view;
        descHashMap.put("h", holidayProperty);

        //leave
        Property leaveProperty = new Property();
        leaveProperty.layoutResource = R.layout.leave_view;
        leaveProperty.dateTextViewResource = R.id.text_view;
        descHashMap.put("leave", leaveProperty);
        tvPresent=(TextView)findViewById(R.id.tvPresent);

        Property hdlProperty = new Property();
        hdlProperty.layoutResource = R.layout.hdl_view;
        hdlProperty.dateTextViewResource = R.id.text_view;
        descHashMap.put("hdl", hdlProperty);




        // set desc hashmap on custom calendar
        customCalendar.setMapDescToProp(descHashMap);
        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.PREVIOUS, this);
        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.NEXT, this);
        btnLeave.setOnClickListener(this);

        lnStatus=(LinearLayout)findViewById(R.id.lnStatus);
        tvDetails=(TextView)findViewById(R.id.tvDetails);
        tvOK=(TextView)findViewById(R.id.tvOK);

        customCalendar.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(View view, Calendar selectedDate, Object desc) {
                String sDate=selectedDate.get(Calendar.DAY_OF_MONTH)
                        +"/" +(selectedDate.get(Calendar.MONTH)+1)
                        +"/" + selectedDate.get(Calendar.YEAR);


                String date=Util.changeAnyDateFormat(sDate,"dd/MM/yyyy","dd MMM yy");
                int pos =dateList.indexOf(date);
                Log.d("position", String.valueOf(pos));
                JSONObject object=attendanceArray.optJSONObject(pos);
                String PunchTiming = object.optString("PunchTiming");
                String Status = object.optString("Status").toUpperCase();


                if (Status.equalsIgnoreCase("DEFAULT")) {
                    lnStatus.setVisibility(View.GONE);
                }else if (Status.equalsIgnoreCase("PRESENT")){
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F224F400")));
                    tvDetails.setText(date + " : " + PunchTiming + " - " + "Present");
                    tvDetails.setTextColor(Color.parseColor("#000000"));
                    tvOK.setTextColor(Color.parseColor("#000000"));


                }else if (Status.equalsIgnoreCase("ABSENT")){
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F2FA0209")));
                    tvDetails.setText(date + " : " + PunchTiming + " - " + "Absent");

                }else if (Status.equalsIgnoreCase("LEAVE")){
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F2DD7C03")));
                    tvDetails.setText(date + " : " + PunchTiming + " - " + "On Leave");

                }else if (Status.equalsIgnoreCase("HDL")){
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#AD9951")));
                    tvDetails.setText(date + " : " + PunchTiming + " - " + "Half Day Leave");

                }else if (Status.equalsIgnoreCase("H")){
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFED45")));
                    tvDetails.setText(date + " : " + PunchTiming + " - " + "Holiday");
                    tvDetails.setTextColor(Color.parseColor("#000000"));
                    tvOK.setTextColor(Color.parseColor("#000000"));
                }else if (Status.equalsIgnoreCase("WO")){
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F20A75F6")));
                    tvDetails.setText(date + " : " + PunchTiming + "-" + "Weekly Off");

                }
            }
        });

        tvOK.setOnClickListener(this);
        if (pref.getEmpClintId().equals("AEMCLI0910000343") || pref.getEmpClintId().equals("AEMCLI0910000315")){
            llAdjustment.setVisibility(View.VISIBLE);
        }else {
            llAdjustment.setVisibility(View.GONE);
        }
        


    }


    private void getAttendanceList(int year, int month) {
        presentDays=new ArrayList<>();
        dateList=new ArrayList<>();
        HashMap<Integer, Object> dateHashmap = new HashMap<>();

        // initialize calendar
        Calendar calendar = Calendar.getInstance();


        ProgressDialog pd = new ProgressDialog(AttenDanceDashboardActivity.this);
        pd.setMessage("Loading...");
        pd.show();
        pd.setCancelable(false);
        String surl = AppData.url + "gcl_Attendancecalender/Get?AemEmployeeid=" + pref.getEmpId() + "&AemClientid=" + pref.getEmpClintId() + "&Monthid=" + month + "&yearid=" + year + "&SecurityCode=" + pref.getSecurityCode();
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
                                attendanceArray=responseData;
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String sDate = obj.optString("Date");
                                    String Date = Util.changeAnyDateFormat(obj.optString("Date"), "dd MMM yy", "dd");
                                    int date = Integer.parseInt(Date);
                                    String PunchTiming = obj.optString("PunchTiming");
                                    String Day = obj.optString("Address");
                                    String Status = obj.optString("Status");


                                    AttendanceCalenderModel obj2 = new AttendanceCalenderModel();
                                    obj2.setDate(Date);
                                    obj2.setStatus(Status);
                                    obj2.setTime(PunchTiming);
                                    obj2.setDay(Day);
                                    itemList.add(obj2);
                                    dateList.add(sDate);
                                    if (Status.equalsIgnoreCase("present")){
                                        presentDays.add(Day);
                                    }

                                    dateHashmap.put(date, Status);


                                }

                                customCalendar.setDate(calendar, dateHashmap);
                                tvPresent.setText(""+presentDays.size());
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

    private void getAttendanceListForNav(int year, int month,Calendar calendar) {
        HashMap<Integer, Object> dateHashmap = new HashMap<>();
        presentDays=new ArrayList<>();
        dateList=new ArrayList<>();
        // initialize calendar



        ProgressDialog pd = new ProgressDialog(AttenDanceDashboardActivity.this);
        pd.setMessage("Loading...");
        pd.show();
        pd.setCancelable(false);
        String surl = AppData.url + "gcl_Attendancecalender/Get?AemEmployeeid=" + pref.getEmpId() + "&AemClientid=" + pref.getEmpClintId() + "&Monthid=" + month + "&yearid=" + year + "&SecurityCode=" + pref.getSecurityCode();
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
                                attendanceArray=responseData;
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String sDate = obj.optString("Date");
                                    String Date = Util.changeAnyDateFormat(obj.optString("Date"), "dd MMM yy", "dd");
                                    int date = Integer.parseInt(Date);
                                    String PunchTiming = obj.optString("PunchTiming");
                                    String Day = obj.optString("Address");
                                    String Status = obj.optString("Status");


                                    AttendanceCalenderModel obj2 = new AttendanceCalenderModel();
                                    obj2.setDate(Date);
                                    obj2.setStatus(Status);
                                    obj2.setTime(PunchTiming);
                                    obj2.setDay(Day);
                                    itemList.add(obj2);
                                    dateList.add(sDate);
                                    if (Status.equalsIgnoreCase("present")){
                                        presentDays.add(Day);
                                    }
                                    dateHashmap.put(date, Status);


                                }

                                customCalendar.setDate(calendar, dateHashmap);
                                tvPresent.setText(""+presentDays.size());

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
            if (pref.getShiftFlag().equals("1")) {
                getShift();
            } else {
                if (pref.getEmpClintId().equals("AEMCLI2210001697") || pref.getEmpClintId().equals("AEMCLI2210001698")) {
                    Intent intent = new Intent(AttenDanceDashboardActivity.this, AttendanceManageWithoutLocActivity.class);
                    intent.putExtra("intt", "2");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(AttenDanceDashboardActivity.this, AttendanceManageActivity.class);
                    intent.putExtra("intt", "2");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }


        } else if (view == llAttendanceReport) {
            Intent intent = new Intent(AttenDanceDashboardActivity.this, AttendanceReportActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (view == llBackAttendance) {
            Intent intent = new Intent(AttenDanceDashboardActivity.this, BacklogAttendanceActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (view == llAttenRegularize) {
            Intent intent = new Intent(AttenDanceDashboardActivity.this, BacklogAttendanceActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (view == llWeekly) {
            if (pref.getEmpClintId().equals("AEMCLI2210001707") || pref.getEmpClintId().equals("AEMCLI2210001697") || pref.getEmpClintId().equals("AEMCLI2210001698")|| pref.getEmpClintId().equals("AEMCLI2310001805")) {
                Intent intent = new Intent(AttenDanceDashboardActivity.this, WeeklyOffAttendanceActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            } else {
                weeklyfunction();
            }

        } else if (view == imgSearch) {
            searchAlert();
        } else if (view == tvCancel) {
            llBottom.setVisibility(View.GONE);
        } else if (view == btnMarkAttendance) {
            if (pref.getEmpClintId().equals("AEMCLI2210001697") || pref.getEmpClintId().equals("AEMCLI2210001698")) {
                Intent intent = new Intent(AttenDanceDashboardActivity.this, AttendanceManageWithoutLocActivity.class);
                intent.putExtra("intt", "2");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Intent intent = new Intent(AttenDanceDashboardActivity.this, AttendanceManageActivity.class);
                intent.putExtra("intt", "2");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        } else if (view == llQR) {
            Intent intent = new Intent(AttenDanceDashboardActivity.this, QRCodeScannerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if (view == btnLeave) {
            Intent intent = new Intent(AttenDanceDashboardActivity.this, LeaveApplicationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if (view==tvOK){
            lnStatus.setVisibility(View.GONE);
        }else if (view==llAdjustment){
            Intent intent = new Intent(AttenDanceDashboardActivity.this, AdjustmentActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }


    }

    private void weeklyfunction() {
        String surl = AppData.url + "get_GCLSelfAttendanceWoLeave?AEMConsultantID=" + pref.getEmpConId() + "&AEMClientID=" + pref.getEmpClintId() + "&AEMClientOfficeID=" + pref.getEmpClintOffId() + "&AEMEmployeeID=" + pref.getEmpId() + "&CurrentPage=1&AID=0&ApproverStatus=4&YearVal=" + year + "&MonthName=" + month + "&WorkingStatus=1&SecurityCode=" + pref.getSecurityCode() + "&DbOperation=6&AttIds=0";
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
        TextView tvSuccess = (TextView) dialogView.findViewById(R.id.tvSuccess);

        tvSuccess.setText(text);

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

    private void searchAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AttenDanceDashboardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.search_dialog, null);
        dialogBuilder.setView(dialogView);
        Spinner spYear = (Spinner) dialogView.findViewById(R.id.spYear);
        Spinner spMonth = (Spinner) dialogView.findViewById(R.id.spMonth);
        ArrayList<Integer> yearList = new ArrayList<>();
        yearList.add(y);
        yearList.add(pastYear);
        yearList.add(futYear);

        ArrayList<SpineerItemModel> monthList = new ArrayList<>();
        monthList.add(new SpineerItemModel("January", "1"));
        monthList.add(new SpineerItemModel("February", "2"));
        monthList.add(new SpineerItemModel("March", "3"));
        monthList.add(new SpineerItemModel("April", "4"));
        monthList.add(new SpineerItemModel("May", "5"));
        monthList.add(new SpineerItemModel("June", "6"));
        monthList.add(new SpineerItemModel("July", "7"));
        monthList.add(new SpineerItemModel("August", "8"));
        monthList.add(new SpineerItemModel("September", "9"));
        monthList.add(new SpineerItemModel("October", "10"));
        monthList.add(new SpineerItemModel("November", "11"));
        monthList.add(new SpineerItemModel("December", "12"));

        ArrayList<String> monthItemList = new ArrayList<>();
        monthItemList.add("January");
        monthItemList.add("February");
        monthItemList.add("March");
        monthItemList.add("April");
        monthItemList.add("May");
        monthItemList.add("June");
        monthItemList.add("July");
        monthItemList.add("August");
        monthItemList.add("September");
        monthItemList.add("October");
        monthItemList.add("November");
        monthItemList.add("December");

        ArrayAdapter yearAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, yearList);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spYear.setAdapter(yearAdapter);

        ArrayAdapter monthAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, monthItemList);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMonth.setAdapter(monthAdapter);

        Button btnShow = (Button) dialogView.findViewById(R.id.btnShow);

        spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                y = yearList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                m = Integer.parseInt(monthList.get(i).getItemId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchDialog.dismiss();
                getAttendanceList(y, m);
            }
        });

        ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchDialog.dismiss();
            }
        });


        searchDialog = dialogBuilder.create();
        searchDialog.setCancelable(true);
        Window window = searchDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        searchDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAttendanceList(y, m);
    }

    private void getShift() {
        String surl = AppData.url + "post_attedanceRoster/Get_Shift?CompanyID=AEMCLI1410000807&EmployeeID=" + pref.getEmpId() + "&AttendanceDate=" + currentDate + "&SecurityCode=" + pref.getSecurityCode();
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
                            JSONArray shiftStatusArray = responseData.optJSONArray(0);
                            JSONObject shiftStatusOBJ = shiftStatusArray.optJSONObject(0);
                            String ShiftStatus = shiftStatusOBJ.optString("ShiftStatus");

                            JSONArray shiftArray = responseData.optJSONArray(1);

                            Intent intent = new Intent(AttenDanceDashboardActivity.this, AttenDanceManageWithShiftActivity.class);
                            intent.putExtra("intt", "2");
                            intent.putExtra("shiftStatus", ShiftStatus);
                            intent.putExtra("shiftArray", shiftArray.toString());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Intent intent = new Intent(AttenDanceDashboardActivity.this, EmployeeDashBoardActivity.class);
                            startActivity(intent);
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

    @Override
    public Map<Integer, Object>[] onNavigationButtonClicked(int whichButton, Calendar newMonth) {
        Map<Integer, Object>[] arr = new Map[2];
        arr[0] = new HashMap<>();
        switch(newMonth.get(Calendar.MONTH)) {
            case Calendar.JANUARY:
                Calendar calendar=Calendar.getInstance();
                calendar.set(y,0,1);

                getAttendanceListForNav(y,1,calendar);



                break;
            case Calendar.FEBRUARY:
                Calendar calendar1=Calendar.getInstance();
                calendar1.set(y,1,1);

                getAttendanceListForNav(y,2,calendar1);

                break;
            case Calendar.MARCH:
                Calendar calendar2=Calendar.getInstance();
                calendar2.set(y,2,1);

                getAttendanceListForNav(y,3,calendar2);

                break;
            case  Calendar.APRIL:
                Calendar calendar3=Calendar.getInstance();
                calendar3.set(y,3,1);

                getAttendanceListForNav(y,4,calendar3);
                break;
            case Calendar.MAY:
                Calendar calendar4=Calendar.getInstance();
                calendar4.set(y,4,1);

                getAttendanceListForNav(y,5,calendar4);
                break;
            case Calendar.JUNE:
                Calendar calendar5=Calendar.getInstance();
                calendar5.set(y,5,1);

                getAttendanceListForNav(y,6,calendar5);
                break;
            case Calendar.JULY:
                Calendar calendar6=Calendar.getInstance();
                calendar6.set(y,6,1);

                getAttendanceListForNav(y,7,calendar6);
                break;
            case Calendar.AUGUST:
                Calendar calendar7=Calendar.getInstance();
                calendar7.set(y,7,1);

                getAttendanceListForNav(y,8,calendar7);
                break;
            case Calendar.SEPTEMBER:
                Calendar calendar8=Calendar.getInstance();
                calendar8.set(y,8,1);

                getAttendanceListForNav(y,9,calendar8);
                break;
            case Calendar.OCTOBER:
                Calendar calendar9=Calendar.getInstance();
                calendar9.set(y,9,1);

                getAttendanceListForNav(y,10,calendar9);
                break;
            case Calendar.NOVEMBER:
                Calendar calendar10=Calendar.getInstance();
                calendar10.set(y,10,1);

                getAttendanceListForNav(y,11,calendar10);
                break;
            case Calendar.DECEMBER:
                Calendar calendar11=Calendar.getInstance();
                calendar11.set(y,11,1);

                getAttendanceListForNav(y,12,calendar11);
                break;
        }


        return arr;
    }
}