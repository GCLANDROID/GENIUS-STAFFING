package io.cordova.myapp00d753.activity.attendance;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
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
import org.naishadhparmar.zcustomcalendar.CustomCalendar;
import org.naishadhparmar.zcustomcalendar.OnDateSelectedListener;
import org.naishadhparmar.zcustomcalendar.OnNavigationButtonClickedListener;
import org.naishadhparmar.zcustomcalendar.Property;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.AdjustmentActivity;
import io.cordova.myapp00d753.activity.BacklogAttendanceActivity;
import io.cordova.myapp00d753.activity.DailyDashBoardActivity;
import io.cordova.myapp00d753.activity.EmployeeDashBoardActivity;
import io.cordova.myapp00d753.activity.HolidayMarkingActivity;
import io.cordova.myapp00d753.activity.ITViewActivity;
import io.cordova.myapp00d753.activity.LeaveApplicationActivity;
import io.cordova.myapp00d753.activity.NEW.AllApplicationViewActivity;
import io.cordova.myapp00d753.activity.NEW.NEW_AdjustmentActivity;
import io.cordova.myapp00d753.activity.NEW.NEW_AttendanceMarkingActivity;
import io.cordova.myapp00d753.activity.NEW.NEW_AttendanceRegularizationActivity;
import io.cordova.myapp00d753.activity.NEW.NEW_AttendanceReportActivity;
import io.cordova.myapp00d753.activity.NEW.NEW_HolidayMarkingActivity;
import io.cordova.myapp00d753.activity.NEW.NEW_HolidayViewActivity;
import io.cordova.myapp00d753.activity.NEW.NEW_WeeklyOffAttendanceActivity;
import io.cordova.myapp00d753.activity.PFDashBoardActivity;
import io.cordova.myapp00d753.activity.QRCodeScannerActivity;
import io.cordova.myapp00d753.activity.SKF.HolidayViewActivity;
import io.cordova.myapp00d753.activity.SKF.SKF_AttendanceRegularizationActivity;
import io.cordova.myapp00d753.activity.ViewLeaveBalanceActivity;
import io.cordova.myapp00d753.activity.WOHOHActivity;
import io.cordova.myapp00d753.activity.WeeklyOffAttendanceActivity;
import io.cordova.myapp00d753.activity.bosch.BoschAttendanceReportActivity;
import io.cordova.myapp00d753.activity.metso.MetsoPMSTargetAchivementActivity;
import io.cordova.myapp00d753.adapter.AttendanceCalenderAdapter;
import io.cordova.myapp00d753.module.AttendanceCalenderModel;
import io.cordova.myapp00d753.module.SpineerItemModel;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.BrunchId;
import io.cordova.myapp00d753.utility.ClientID;
import io.cordova.myapp00d753.utility.GPSTracker;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.Util;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class AttenDanceDashboardActivity extends AppCompatActivity implements View.OnClickListener, OnNavigationButtonClickedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "AttenDanceDashboard";
    RecyclerView rvItem, rvSun, rvMon, rvTue, rvWed, rvThu, rvFri, rvSat;
    ArrayList<AttendanceCalenderModel> itemList = new ArrayList<>();
    GoogleApiClient googleApiClient;
    Pref pref;
    DrawerLayout dlMain;
    boolean mslideState;
    ImageView imgMenu;
    LinearLayout llAttandanceManage, llAttendanceReport, llBackAttendance, llWeekly, llAttenRegularize, llBottom,llViewLeaveBalance;
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
    ArrayList<String> presentDays = new ArrayList<>();
    ArrayList<String> dateList = new ArrayList<>();

    TextView tvPresent, tvDetails, tvOK;
    LinearLayout lnStatus, llAdjustment,llHoliday,llHolidayView,llOptionalHoliday,llAllApplicationView;
    int date;
    GPSTracker gps;
    int leaveFlag;
    ImageView imgHome;
    String holidayFlag="0",OptionalHolidayFlag="0";
    String HolidayDetails_Array="";

    String MarkAttnFromMobileWithImage="", MarkAttnFromMobileWithGeoFencingLocation="",
            MarkAttnFromMobileWithWorkPlaceSelection="",MarkAttnFromMobileWithShiftSelection="",MarkAttnFromMobileWithHierarchySelection="",
            MarkAttnFromMobileWithPunchLocationSelection="";
    String AttendanceRegularisationWithDayTypeSelection="", AttendanceRegularisationWithWorkPlaceSelection="",
            AttendanceRegularisationWithWorkingShiftSelection="", AttendanceRegularisationWithHierarchySelection="";
    String OptionalHolidayMarkingWithHolidaySelection ="",OptionalHolidayMarkingWithHierarchySelection="",
            NormalHolidayMarkingWithHolidaySelection ="",NormalHolidayMarkingWithHierarchySelection="";
    String AttnAdjCOAppliocationWithMarkedHierarchy="",AttnAdjWFHAppliocationWithMarkedHierarchy="",AttnAdjODMarkHierarchyAtEntry="";
    String WOMarkingWithHierarchySelection="",isLiveStatus_WeeklyOff="";
    String isLiveStatus_MarkAttnFromMobile ="", isLiveStatus_HolidayMarking ="",
            isLiveStatus_OHolidayMarking="",isLiveStatus_AttReg="",isLiveStatus_HolidayView="",isLiveSatus_Adjustment="",isLiveStatus_DailyAttendanceView="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atten_dance_dashboard);
        initView();
    }

    private void initView() {
        leaveFlag=getIntent().getIntExtra("leaveFlag",1);
        btnLeave = (Button) findViewById(R.id.btnLeave);
        if (leaveFlag==1){
            btnLeave.setVisibility(View.VISIBLE);
        }else {
            btnLeave.setVisibility(View.GONE);
        }
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
        llAdjustment = (LinearLayout) findViewById(R.id.llAdjustment);
        llAttandanceManage = (LinearLayout) findViewById(R.id.llAttandanceManage);
        llAttendanceReport = (LinearLayout) findViewById(R.id.llAttendanceReport);
        llBackAttendance = (LinearLayout) findViewById(R.id.llBackAttendance);
        llAttenRegularize = (LinearLayout) findViewById(R.id.llAttenRegularize);
        llWeekly = (LinearLayout) findViewById(R.id.llWeekly);
        llHoliday = (LinearLayout) findViewById(R.id.llHoliday);
        llOptionalHoliday = (LinearLayout) findViewById(R.id.llOptionalHoliday);
        llHolidayView = (LinearLayout) findViewById(R.id.llHolidayView);
        llViewLeaveBalance = (LinearLayout) findViewById(R.id.llViewLeaveBalance);
        llAllApplicationView = (LinearLayout) findViewById(R.id.llAllApplicationView);
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

        if (pref.getEmpClintId().equals(ClientID.SKF_CLIENT_ID)
                || pref.getEmpClintId().equals(ClientID.SKF_ITS)
                || pref.getEmpClintId().equals(ClientID.SKF_MSP)
                || pref.getEmpClintId().equals(ClientID.SKF_ENGINEERING_LUB)
                || pref.getEmpClintId().equals(ClientID.SKF_INDUSTRIAL)
                ||pref.getEmpClintId().equals(ClientID.SVF)){
            llHoliday.setVisibility(View.VISIBLE);
        } else {
            llHoliday.setVisibility(View.GONE);
        }
        Log.e(TAG, "initView: "+pref.getBackAttd());
        if (pref.getBackAttd().equals("1")) {
            llBackAttendance.setVisibility(View.VISIBLE);
            llAttenRegularize.setVisibility(View.VISIBLE);
        } else {
            llBackAttendance.setVisibility(View.GONE);
            llAttenRegularize.setVisibility(View.GONE);
            /*llBackAttendance.setVisibility(View.VISIBLE);
            llAttenRegularize.setVisibility(View.VISIBLE);*/
        }

        if (pref.getEmpClintId().equals(ClientID.SKY_ROOT) || pref.getEmpClintId().equals(ClientID.WACKER) || pref.getEmpClintId().equals(ClientID.ABFRL)){
            llHolidayView.setVisibility(View.VISIBLE);
        } else {
            llHolidayView.setVisibility(View.GONE);
        }

        if (AppData.LEAVE_BALANCE_VIEW_FLAG.equals("1")){

            llViewLeaveBalance.setVisibility(View.VISIBLE);
        } else {
            llViewLeaveBalance.setVisibility(View.GONE);
        }

        if (pref.getEmpClintId().equals(ClientID.SHALIMAR_WIRES)){
            llAttandanceManage.setVisibility(View.GONE);
            llAttendanceReport.setVisibility(View.GONE);
            btnMarkAttendance.setVisibility(View.GONE);
        } else {
            llAttandanceManage.setVisibility(View.VISIBLE);
            llAttendanceReport.setVisibility(View.VISIBLE);
            btnMarkAttendance.setVisibility(View.VISIBLE);
        }

        llAttandanceManage.setOnClickListener(this);
        llAttendanceReport.setOnClickListener(this);
        llWeekly.setOnClickListener(this);
        llHoliday.setOnClickListener(this);
        llOptionalHoliday.setOnClickListener(this);
        llBackAttendance.setOnClickListener(this);
        llAttenRegularize.setOnClickListener(this);
        llAdjustment.setOnClickListener(this);
        llHolidayView.setOnClickListener(this);
        llViewLeaveBalance.setOnClickListener(this);
        llAllApplicationView.setOnClickListener(this);
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
        tvPresent = (TextView) findViewById(R.id.tvPresent);

        Property hdlProperty = new Property();
        hdlProperty.layoutResource = R.layout.hdl_view;
        hdlProperty.dateTextViewResource = R.id.text_view;
        descHashMap.put("hdl", hdlProperty);

        //TODO: Optional Holiday
        Property OH_Property = new Property();
        OH_Property.layoutResource = R.layout.oh_view;
        OH_Property.dateTextViewResource = R.id.text_view;
        descHashMap.put("oh", OH_Property);

        //TODO: Half day
        Property HD_Property = new Property();
        HD_Property.layoutResource = R.layout.hd_view;
        HD_Property.dateTextViewResource = R.id.text_view;
        descHashMap.put("hd", HD_Property);

        //TODO: Holiday work
        Property HW_Property = new Property();
        HW_Property.layoutResource = R.layout.hw_view;
        HW_Property.dateTextViewResource = R.id.text_view;
        descHashMap.put("hw", HW_Property);

        //TODO: weekly comp off
        Property WC_Property = new Property();
        WC_Property.layoutResource = R.layout.wc_view;
        WC_Property.dateTextViewResource = R.id.text_view;
        descHashMap.put("wc", WC_Property);

        // set desc hashmap on custom calendar
        customCalendar.setMapDescToProp(descHashMap);
        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.PREVIOUS, this);
        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.NEXT, this);
        btnLeave.setOnClickListener(this);

        lnStatus = (LinearLayout) findViewById(R.id.lnStatus);
        tvDetails = (TextView) findViewById(R.id.tvDetails);
        tvOK = (TextView) findViewById(R.id.tvOK);

        customCalendar.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(View view, Calendar selectedDate, Object desc) {
                String sDate = selectedDate.get(Calendar.DAY_OF_MONTH)
                        + "/" + (selectedDate.get(Calendar.MONTH) + 1)
                        + "/" + selectedDate.get(Calendar.YEAR);

                String date = Util.changeAnyDateFormat(sDate, "dd/MM/yyyy", "dd MMM yy");
                if (date.contains("Sept")){
                    date = date.replace("Sept","Sep");

                }
                int pos = dateList.indexOf(date);
                Log.d("position", String.valueOf(pos));
                JSONObject object = attendanceArray.optJSONObject(pos);
                String PunchTiming = object.optString("PunchTiming");
                String Status = object.optString("Status").toUpperCase();

                if (Status.equalsIgnoreCase("DEFAULT")) {
                    lnStatus.setVisibility(View.GONE);
                } else if (Status.equalsIgnoreCase("PRESENT")) {
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F224F400")));
                    tvDetails.setText(date + " : " + PunchTiming + " - " + "Present");
                    tvDetails.setTextColor(Color.parseColor("#000000"));
                    tvOK.setTextColor(Color.parseColor("#000000"));

                } else if (Status.equalsIgnoreCase("ABSENT")) {
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F2FA0209")));
                    tvDetails.setText(date + " : " + PunchTiming + " - " + "Absent");

                } else if (Status.equalsIgnoreCase("LEAVE")) {
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F2DD7C03")));
                    tvDetails.setText(date + " : " + PunchTiming + " - " + "On Leave");

                } else if (Status.equalsIgnoreCase("HDL")) {
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#AD9951")));
                    tvDetails.setText(date + " : " + PunchTiming + " - " + "Half Day Leave");

                } else if (Status.equalsIgnoreCase("H")) {
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFED45")));
                    tvDetails.setText(date + " : " + PunchTiming + " - " + "Holiday");
                    tvDetails.setTextColor(Color.parseColor("#000000"));
                    tvOK.setTextColor(Color.parseColor("#000000"));
                } else if (Status.equalsIgnoreCase("OH")) {

                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#daa920")));
                    tvDetails.setText(date + " : " + PunchTiming + " - " + "Optional Holiday");
                    tvDetails.setTextColor(Color.parseColor("#000000"));
                    tvOK.setTextColor(Color.parseColor("#000000"));

                } else if (Status.equalsIgnoreCase("HD")) {
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F87AA5")));
                    tvDetails.setText(date + " : " + PunchTiming + " - " + "Half Day");
                    tvDetails.setTextColor(Color.parseColor("#000000"));
                    tvOK.setTextColor(Color.parseColor("#000000"));

                } else if (Status.equalsIgnoreCase("WO")) {
                    lnStatus.setVisibility(View.VISIBLE);
                    lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F20A75F6")));
                    tvDetails.setText(date + " : " + PunchTiming + "-" + "Weekly Off");
                } else if (Status.equalsIgnoreCase("HW")) {
                    lnStatus.setVisibility(View.VISIBLE);
                    int color = ContextCompat.getColor(AttenDanceDashboardActivity.this, R.color.holiday_work);
                    //lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#86C73B")));
                    lnStatus.setBackgroundColor(color);
                    tvDetails.setText(date + " : " + PunchTiming + " - " + "Holiday Work");
                } else if (Status.equalsIgnoreCase("WC")) {
                    lnStatus.setVisibility(View.VISIBLE);
                    int color = ContextCompat.getColor(AttenDanceDashboardActivity.this, R.color.weekly_comp_off);
                    lnStatus.setBackgroundColor(color);
                    //lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F20A75F6")));
                    tvDetails.setText(date + " : " + PunchTiming + " - " + "Weekly Comp Off");
                }
            }
        });
        imgHome=(ImageView)findViewById(R.id.imgHome);
        tvOK.setOnClickListener(this);
        imgHome.setOnClickListener(this);
        if (pref.getAdjustmentStatus().equals("1")) {
            llAdjustment.setVisibility(View.VISIBLE);
        } else {
            llAdjustment.setVisibility(View.GONE);
        }
        AllApplicationViewConfiguration();
    }

    private void getAttendanceList(JSONObject jsonObject) {
        Log.e(TAG, "getAttendanceList: INPUT: "+jsonObject);
        presentDays = new ArrayList<>();
        dateList = new ArrayList<>();
        HashMap<Integer, Object> dateHashmap = new HashMap<>();
        // initialize calendar
        Calendar calendar = Calendar.getInstance();

        ProgressDialog pd = new ProgressDialog(AttenDanceDashboardActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();

        AndroidNetworking.post(AppData.GET_ATTENDANCE_CALENDER)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            pd.dismiss();
                            itemList.clear();
                            Log.e(TAG, "ATTENDANCE_CALENDER: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(Response_Data);
                                attendanceArray=jsonArray;
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String sDate = obj.optString("Date");
                                    String Date = Util.changeAnyDateFormat(obj.optString("SDate"), "dd/MM/yyyy", "dd");
                                    try {
                                        date = Integer.parseInt(Date);
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
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
                                    if (Status.equalsIgnoreCase("present")) {
                                        presentDays.add(Day);
                                    }
                                    dateHashmap.put(date, Status.toLowerCase());
                                }
                                customCalendar.setDate(calendar, dateHashmap);
                                tvPresent.setText("" + presentDays.size());
                                setAdapter();
                            } else {

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        pd.dismiss();
                        Log.e(TAG, "ATTENDANCE_CALENDER_error: "+anError.getErrorBody());
                    }
                });
    }


    private void getAttendanceList(int year, int month) {
        presentDays = new ArrayList<>();
        dateList = new ArrayList<>();
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
                                attendanceArray = responseData;
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String sDate = obj.optString("Date");
                                    String Date = Util.changeAnyDateFormat(obj.optString("Sdate"), "dd/MM/yyyy", "dd");
                                    try {
                                        date = Integer.parseInt(Date);
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
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
                                    if (Status.equalsIgnoreCase("present")) {
                                        presentDays.add(Day);
                                    }
                                    dateHashmap.put(date, Status.toLowerCase());
                                }
                                customCalendar.setDate(calendar, dateHashmap);
                                tvPresent.setText("" + presentDays.size());
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

    private void getAttendanceListForNav(JSONObject jsonObject, Calendar calendar) {
        Log.e(TAG, "getAttendanceListForNav: "+jsonObject);
        HashMap<Integer, Object> dateHashmap = new HashMap<>();
        presentDays = new ArrayList<>();
        dateList = new ArrayList<>();

        ProgressDialog pd = new ProgressDialog(AttenDanceDashboardActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();

        AndroidNetworking.post(AppData.GET_ATTENDANCE_CALENDER)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "ATTENDANCE_CALENDER_NAV: "+response.toString(4));
                            pd.dismiss();
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(Response_Data);
                                attendanceArray=jsonArray;
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String sDate = obj.optString("Date");
                                    String Date = Util.changeAnyDateFormat(obj.optString("SDate"), "dd/MM/yyyy", "dd");
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
                                    if (Status.equalsIgnoreCase("present")) {
                                        presentDays.add(Day);
                                    }
                                    dateHashmap.put(date, Status.toLowerCase());
                                }

                                customCalendar.setDate(calendar, dateHashmap);
                                tvPresent.setText("" + presentDays.size());
                                setAdapter();
                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        pd.dismiss();
                        Log.e(TAG, "ATTENDANCE_CALENDER_NAV: "+anError.getErrorBody());
                    }
                });
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
            turnGPSOn();
        } else if (view == llAttendanceReport) {
            if(isLiveStatus_DailyAttendanceView.equals("1")){
                Intent intent = new Intent(AttenDanceDashboardActivity.this, NEW_AttendanceReportActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else if (pref.getEmpClintId().equals(ClientID.METSO)
                    || pref.getEmpClintOffId().equals(BrunchId.CBRE_HARYANA_AMERICAN_EXPRESS)
                    || pref.getEmpClintOffId().equals(BrunchId.CBRE_KARNATAKA_AMERICAN_EXPRESS)
                    || pref.getEmpClintOffId().equals(BrunchId.CBRE_Tamil_Nadu_American_Express)
                    || pref.getEmpClintOffId().equals(BrunchId.CBRE_HARYANA_KEY_SITE)
                    || pref.getEmpClintOffId().equals(BrunchId.CBRE_DELHI_KEY_SITE)
                    || pref.getEmpClintOffId().equals(BrunchId.CBRE_KARNATAKA_KEY_SITE)
                    || pref.getEmpClintOffId().equals(BrunchId.CBRE_TELANGANA_KEY_SITE)
            ) {
                Intent intent = new Intent(AttenDanceDashboardActivity.this, MetsoAttendanceReportActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else if (pref.getEmpClintId().equals("AEMCLI1110000593")){
                Intent intent = new Intent(AttenDanceDashboardActivity.this, BoschAttendanceReportActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Intent intent = new Intent(AttenDanceDashboardActivity.this, AttendanceReportActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                /////
            }
        } else if (view == llBackAttendance) {
            Log.e(TAG, "onClick: llBackAttendance");
            if (isLiveStatus_AttReg.equals("1")){
                Intent intent = new Intent(AttenDanceDashboardActivity.this, NEW_AttendanceRegularizationActivity.class);
                intent.putExtra("AttendanceRegularisationWithDayTypeSelection",AttendanceRegularisationWithDayTypeSelection);
                intent.putExtra("AttendanceRegularisationWithHierarchySelection",AttendanceRegularisationWithHierarchySelection);
                intent.putExtra("AttendanceRegularisationWithWorkingShiftSelection",AttendanceRegularisationWithWorkingShiftSelection);
                intent.putExtra("AttendanceRegularisationWithWorkPlaceSelection",AttendanceRegularisationWithWorkPlaceSelection);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else if (pref.getEmpClintId().equals(ClientID.METSO)) {
                Intent intent = new Intent(AttenDanceDashboardActivity.this, MetsoAttendanceRegularizationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }  else if(pref.getEmpClintId().equals(ClientID.SKF_CLIENT_ID)
                    || pref.getEmpClintId().equals(ClientID.SKF_ITS)
                    || pref.getEmpClintId().equals(ClientID.SKF_MSP)
                    || pref.getEmpClintId().equals(ClientID.SKF_ENGINEERING_LUB)
                    || pref.getEmpClintId().equals(ClientID.SKF_INDUSTRIAL)
                    || pref.getEmpClintId().equals(ClientID.HONASA)
                    || pref.getEmpClintId().equals(ClientID.SVF)
                    || pref.getEmpClintId().equals(ClientID.FED_BANK)
                    || pref.getEmpClintId().equals(ClientID.PROTACTOR_GAMBLEID)
                    || pref.getEmpClintId().equals(ClientID.BROSE_INDIA_AUTOMOTIVE_SYSTEMS)
            ) {
                Intent intent = new Intent(AttenDanceDashboardActivity.this, SKF_AttendanceRegularizationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Intent intent = new Intent(AttenDanceDashboardActivity.this, BacklogAttendanceActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        } else if (view == llAttenRegularize) {
            if (isLiveStatus_AttReg.equals("1")){
                Intent intent = new Intent(AttenDanceDashboardActivity.this, NEW_AttendanceRegularizationActivity.class);
                intent.putExtra("AttendanceRegularisationWithDayTypeSelection",AttendanceRegularisationWithDayTypeSelection);
                intent.putExtra("AttendanceRegularisationWithHierarchySelection",AttendanceRegularisationWithHierarchySelection);
                intent.putExtra("AttendanceRegularisationWithWorkingShiftSelection",AttendanceRegularisationWithWorkingShiftSelection);
                intent.putExtra("AttendanceRegularisationWithWorkPlaceSelection",AttendanceRegularisationWithWorkPlaceSelection);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else if (pref.getEmpClintId().equals(ClientID.METSO)) {
                Intent intent = new Intent(AttenDanceDashboardActivity.this, MetsoAttendanceRegularizationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Intent intent = new Intent(AttenDanceDashboardActivity.this, BacklogAttendanceActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        } else if (view == llWeekly) {
            if (isLiveStatus_WeeklyOff.equals("1")){
                Intent intent = new Intent(AttenDanceDashboardActivity.this, NEW_WeeklyOffAttendanceActivity.class);
                intent.putExtra("WOMarkingWithHierarchySelection",WOMarkingWithHierarchySelection);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else if (pref.getEmpClintId().equals("AEMCLI2210001707") || pref.getEmpClintId().equals("AEMCLI2210001697") || pref.getEmpClintId().equals("AEMCLI2210001698") || pref.getEmpClintId().equals("AEMCLI2310001805")|| pref.getEmpClintId().equals(ClientID.METSO)|| pref.getEmpClintId().equals(ClientID.HONASA)|| pref.getEmpClintId().equals(ClientID.MAHINDRA_MAHINDRA)) {
                Intent intent = new Intent(AttenDanceDashboardActivity.this, WeeklyOffAttendanceActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else if( pref.getEmpClintId().equals(ClientID.SKF_CLIENT_ID)
                    || pref.getEmpClintId().equals(ClientID.SKF_ITS)
                    || pref.getEmpClintId().equals(ClientID.SKF_MSP)
                    || pref.getEmpClintId().equals(ClientID.SKF_ENGINEERING_LUB)
                    || pref.getEmpClintId().equals(ClientID.SKF_INDUSTRIAL)
                    || pref.getEmpClintId().equals(ClientID.SVF)
                    || pref.getEmpClintId().equals(ClientID.BROSE_INDIA_AUTOMOTIVE_SYSTEMS)
            ) {  //WO APplication with location ->SKF PUNE
                Intent intent = new Intent(AttenDanceDashboardActivity.this, WOHOHActivity.class);
                intent.putExtra("leaveFlag",leaveFlag);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else {
                Intent intent = new Intent(AttenDanceDashboardActivity.this, WeeklyOffAttendanceActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        } else if (view == llHoliday) {
            if (isLiveStatus_HolidayMarking.equals("1")){
                Intent intent = new Intent(AttenDanceDashboardActivity.this, NEW_HolidayMarkingActivity.class);
                intent.putExtra("holidayFlag","1");
                intent.putExtra("OptionalHolidayFlag","0");
                intent.putExtra("leaveFlag",leaveFlag);
                intent.putExtra("NormalHolidayMarkingWithHolidaySelection", NormalHolidayMarkingWithHolidaySelection);
                intent.putExtra("NormalHolidayMarkingWithHierarchySelection",NormalHolidayMarkingWithHierarchySelection);
                intent.putExtra("OptionalHolidayMarkingWithHierarchySelection","");
                intent.putExtra("OptionalHolidayMarkingWithHolidaySelection","");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Intent intent = new Intent(AttenDanceDashboardActivity.this, HolidayMarkingActivity.class);
                intent.putExtra("leaveFlag",leaveFlag);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }else if (view == llOptionalHoliday) {
            if (isLiveStatus_OHolidayMarking.equals("1")){
                Intent intent = new Intent(AttenDanceDashboardActivity.this, NEW_HolidayMarkingActivity.class);
                intent.putExtra("holidayFlag","0");
                intent.putExtra("OptionalHolidayFlag","1");
                intent.putExtra("leaveFlag",leaveFlag);
                intent.putExtra("NormalHolidayMarkingWithHolidaySelection","");
                intent.putExtra("NormalHolidayMarkingWithHierarchySelection","");
                intent.putExtra("OptionalHolidayMarkingWithHierarchySelection",OptionalHolidayMarkingWithHierarchySelection);
                intent.putExtra("OptionalHolidayMarkingWithHolidaySelection", OptionalHolidayMarkingWithHolidaySelection);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Intent intent = new Intent(AttenDanceDashboardActivity.this, HolidayMarkingActivity.class);
                intent.putExtra("leaveFlag",leaveFlag);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        } else if (view == imgSearch) {
            searchAlert();
        } else if (view == tvCancel) {
            llBottom.setVisibility(View.GONE);
        } else if (view == btnMarkAttendance) {
            turnGPSOn();
        } else if (view == llQR) {
            Intent intent = new Intent(AttenDanceDashboardActivity.this, QRCodeScannerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (view == btnLeave) {
//            Intent intent = new Intent(AttenDanceDashboardActivity.this, LeaveApplicationActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);

            Intent intent=new Intent(AttenDanceDashboardActivity.this, ITViewActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("url",pref.getLeaveApplicationURL());
            startActivity(intent);
        } else if (view == tvOK) {
            lnStatus.setVisibility(View.GONE);
        } else if (view == llAdjustment) {
           /* Intent intent = new Intent(AttenDanceDashboardActivity.this, AdjustmentActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);*/
            if (isLiveSatus_Adjustment.equals("1")){
                Intent intent = new Intent(AttenDanceDashboardActivity.this, NEW_AdjustmentActivity.class);
                intent.putExtra("AttnAdjCOAppliocationWithMarkedHierarchy",AttnAdjCOAppliocationWithMarkedHierarchy);
                intent.putExtra("AttnAdjWFHAppliocationWithMarkedHierarchy",AttnAdjWFHAppliocationWithMarkedHierarchy);
                intent.putExtra("AttnAdjODMarkHierarchyAtEntry",AttnAdjODMarkHierarchyAtEntry);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Intent intent = new Intent(AttenDanceDashboardActivity.this, AdjustmentActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        } else if (view == llHolidayView) {
            if (isLiveStatus_HolidayView.equals("1")) {
                Intent intent = new Intent(AttenDanceDashboardActivity.this, NEW_HolidayViewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Intent intent = new Intent(AttenDanceDashboardActivity.this, HolidayViewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        } else if(view == llViewLeaveBalance){
            Intent intent = new Intent(AttenDanceDashboardActivity.this, ViewLeaveBalanceActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if(view == llAllApplicationView){
            Intent intent = new Intent(AttenDanceDashboardActivity.this, AllApplicationViewActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (view == imgHome) {
            Intent intent = new Intent(AttenDanceDashboardActivity.this, EmployeeDashBoardActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void openLocationOnRequestPopup() {
        Dialog locationRequestDialog = new Dialog(AttenDanceDashboardActivity.this, R.style.CustomDialogNew2);
        locationRequestDialog.setContentView(R.layout.location_request_layout);
        locationRequestDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        locationRequestDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        TextView txtOkey = locationRequestDialog.findViewById(R.id.txtOkey);
        txtOkey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                locationRequestDialog.dismiss();
            }
        });

        locationRequestDialog.setCancelable(false);
        locationRequestDialog.show();
    }

    private void weeklyfunction(JSONObject jsonObject) {
        Log.e(TAG, "weeklyfunction: "+jsonObject);
        AndroidNetworking.post(AppData.SAVE_WEEKLY_OFF_APPLICATION)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "SAVE_WEEKLY_OFF_APPLICATION: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                String Response_Message = job1.optString("Response_Message");

                                successAlert(Response_Message);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "SAVE_WEEKLY_OFF_APPLICATION_error: "+anError.getErrorBody());
                    }
                });
    }

    private void weeklyfunction() {
        String surl = AppData.url + "get_GCLSelfAttendanceWoLeave?AEMConsultantID=" + pref.getEmpConId() + "&AEMClientID=" + pref.getEmpClintId() + "&AEMClientOfficeID=" + pref.getEmpClintOffId() + "&AEMEmployeeID=" + pref.getEmpId() + "&CurrentPage=1&AID=0&ApproverStatus=4&YearVal=" + year + "&MonthName=" + month + "&WorkingStatus=1&SecurityCode=" + pref.getSecurityCode() + "&DbOperation=6&AttIds=0";
        Log.e(TAG, "weeklyfunction: URL: "+surl);
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
//

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
                //getAttendanceList(y, m);

                JSONObject obj=new JSONObject();
                try {
                    obj.put("AemEmployeeid", pref.getEmpId());
                    obj.put("AemClientid",pref.getEmpClintId());
                    obj.put("Monthid",m);
                    obj.put("yearid",y);
                    obj.put("SecurityCode",pref.getSecurityCode());
                    getAttendanceList(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
       /* JSONObject objSubmenu=new JSONObject();
        try {
            objSubmenu.put("ConsultantID", pref.getEmpConId());
            objSubmenu.put("ClientID", pref.getEmpClintId());
            objSubmenu.put("EmployeeID", pref.getEmpId());
            objSubmenu.put("ModuleName", "Service Menu");
            objSubmenu.put("PunchDate", "");
            objSubmenu.put("SecurityCode", pref.getSecurityCode());
            Log.e(TAG, "objSubmenu: "+objSubmenu.toString(4));
            getSubmenu(objSubmenu);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        JSONObject obj=new JSONObject();
        try {
            obj.put("AemEmployeeid", pref.getEmpId());
            obj.put("AemClientid",pref.getEmpClintId());
            obj.put("Monthid",m);
            obj.put("yearid",y);
            obj.put("SecurityCode",pref.getSecurityCode());
            getAttendanceList(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getShift(JSONObject jsonObject) {
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();
        AndroidNetworking.post(AppData.GET_SHIFT)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "ATTENDANCE_GET_SHIFT: "+response.toString(4));
                            progressBar.dismiss();
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");

                                String toastText = job1.optString("responseText");
                                JSONArray responseData = job1.optJSONArray(Response_Data);
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
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressBar.dismiss();
                        Log.e(TAG, "ATTENDANCE_GET_SHIFT_error: "+anError.getErrorBody());
                    }
                });
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

                            if (pref.getEmpClintOffId().equals(BrunchId.CBRE_HARYANA_AMERICAN_EXPRESS)
                                    || pref.getEmpClintOffId().equals(BrunchId.CBRE_KARNATAKA_AMERICAN_EXPRESS)
                                    || pref.getEmpClintOffId().equals(BrunchId.CBRE_Tamil_Nadu_American_Express)
                                    || pref.getEmpClintOffId().equals(BrunchId.CBRE_HARYANA_KEY_SITE)
                                    || pref.getEmpClintOffId().equals(BrunchId.CBRE_DELHI_KEY_SITE)
                                    || pref.getEmpClintOffId().equals(BrunchId.CBRE_KARNATAKA_KEY_SITE)
                                    || pref.getEmpClintOffId().equals(BrunchId.CBRE_TELANGANA_KEY_SITE)
                            ){
                                Intent intent = new Intent(AttenDanceDashboardActivity.this, GeoFenceAttendanceWithShiftActivity.class);
                                //intent.putExtra("intt", "2");
                                //intent.putExtra("shiftStatus", ShiftStatus);
                                //intent.putExtra("shiftArray", shiftArray.toString());
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(AttenDanceDashboardActivity.this, AttenDanceManageWithShiftActivity.class);
                                intent.putExtra("intt", "2");
                                intent.putExtra("shiftStatus", ShiftStatus);
                                intent.putExtra("shiftArray", shiftArray.toString());
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }


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
        switch (newMonth.get(Calendar.MONTH)) {
            case Calendar.JANUARY:
                Calendar calendar = Calendar.getInstance();
                calendar.set(newMonth.get(Calendar.YEAR), 0, 1);
                //getAttendanceListForNav(y, 1, calendar);

                JSONObject obj1=new JSONObject();
                try {
                    obj1.put("AemEmployeeid", pref.getEmpId());
                    obj1.put("AemClientid",pref.getEmpClintId());
                    obj1.put("Monthid","1");
                    obj1.put("yearid",newMonth.get(Calendar.YEAR));
                    obj1.put("SecurityCode",pref.getSecurityCode());
                    getAttendanceListForNav(obj1,calendar);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Calendar.FEBRUARY:
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(newMonth.get(Calendar.YEAR), 1, 1);

                //getAttendanceListForNav(y, 2, calendar1);

                JSONObject obj2=new JSONObject();
                try {
                    obj2.put("AemEmployeeid", pref.getEmpId());
                    obj2.put("AemClientid",pref.getEmpClintId());
                    obj2.put("Monthid","2");
                    obj2.put("yearid",newMonth.get(Calendar.YEAR));
                    obj2.put("SecurityCode",pref.getSecurityCode());
                    getAttendanceListForNav(obj2,calendar1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case Calendar.MARCH:
                Calendar calendar2 = Calendar.getInstance();
                calendar2.set(newMonth.get(Calendar.YEAR), 2, 1);

                //getAttendanceListForNav(y, 3, calendar2);

                JSONObject obj3=new JSONObject();
                try {
                    obj3.put("AemEmployeeid", pref.getEmpId());
                    obj3.put("AemClientid",pref.getEmpClintId());
                    obj3.put("Monthid","3");
                    obj3.put("yearid",newMonth.get(Calendar.YEAR));
                    obj3.put("SecurityCode",pref.getSecurityCode());
                    getAttendanceListForNav(obj3,calendar2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case Calendar.APRIL:
                Calendar calendar3 = Calendar.getInstance();
                calendar3.set(newMonth.get(Calendar.YEAR), 3, 1);

                //getAttendanceListForNav(y, 4, calendar3);

                JSONObject obj4=new JSONObject();
                try {
                    obj4.put("AemEmployeeid", pref.getEmpId());
                    obj4.put("AemClientid",pref.getEmpClintId());
                    obj4.put("Monthid","4");
                    obj4.put("yearid",newMonth.get(Calendar.YEAR));
                    obj4.put("SecurityCode",pref.getSecurityCode());
                    getAttendanceListForNav(obj4,calendar3);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Calendar.MAY:
                Calendar calendar4 = Calendar.getInstance();
                calendar4.set(newMonth.get(Calendar.YEAR), 4, 1);

                //getAttendanceListForNav(y, 5, calendar4);

                JSONObject obj5=new JSONObject();
                try {
                    obj5.put("AemEmployeeid", pref.getEmpId());
                    obj5.put("AemClientid",pref.getEmpClintId());
                    obj5.put("Monthid","5");
                    obj5.put("yearid",newMonth.get(Calendar.YEAR));
                    obj5.put("SecurityCode",pref.getSecurityCode());
                    getAttendanceListForNav(obj5,calendar4);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Calendar.JUNE:
                Calendar calendar5 = Calendar.getInstance();
                calendar5.set(newMonth.get(Calendar.YEAR), 5, 1);

                //getAttendanceListForNav(y, 6, calendar5);

                JSONObject obj6=new JSONObject();
                try {
                    obj6.put("AemEmployeeid", pref.getEmpId());
                    obj6.put("AemClientid",pref.getEmpClintId());
                    obj6.put("Monthid","6");
                    obj6.put("yearid",newMonth.get(Calendar.YEAR));
                    obj6.put("SecurityCode",pref.getSecurityCode());
                    getAttendanceListForNav(obj6,calendar5);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Calendar.JULY:
                Calendar calendar6 = Calendar.getInstance();
                calendar6.set(newMonth.get(Calendar.YEAR), 6, 1);

                //getAttendanceListForNav(y, 7, calendar6);

                JSONObject obj7=new JSONObject();
                try {
                    obj7.put("AemEmployeeid", pref.getEmpId());
                    obj7.put("AemClientid",pref.getEmpClintId());
                    obj7.put("Monthid","7");
                    obj7.put("yearid",newMonth.get(Calendar.YEAR));
                    obj7.put("SecurityCode",pref.getSecurityCode());
                    getAttendanceListForNav(obj7,calendar6);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Calendar.AUGUST:
                Calendar calendar7 = Calendar.getInstance();
                calendar7.set(newMonth.get(Calendar.YEAR), 7, 1);

                //getAttendanceListForNav(y, 8, calendar7);

                JSONObject obj8=new JSONObject();
                try {
                    obj8.put("AemEmployeeid", pref.getEmpId());
                    obj8.put("AemClientid",pref.getEmpClintId());
                    obj8.put("Monthid","8");
                    obj8.put("yearid",newMonth.get(Calendar.YEAR));
                    obj8.put("SecurityCode",pref.getSecurityCode());
                    getAttendanceListForNav(obj8,calendar7);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Calendar.SEPTEMBER:
                Calendar calendar8 = Calendar.getInstance();
                calendar8.set(newMonth.get(Calendar.YEAR), 8, 1);

                //getAttendanceListForNav(y, 9, calendar8);

                JSONObject obj9=new JSONObject();
                try {
                    obj9.put("AemEmployeeid", pref.getEmpId());
                    obj9.put("AemClientid",pref.getEmpClintId());
                    obj9.put("Monthid","9");
                    obj9.put("yearid",newMonth.get(Calendar.YEAR));
                    obj9.put("SecurityCode",pref.getSecurityCode());
                    getAttendanceListForNav(obj9,calendar8);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Calendar.OCTOBER:
                Calendar calendar9 = Calendar.getInstance();
                calendar9.set(newMonth.get(Calendar.YEAR), 9, 1);

                //getAttendanceListForNav(y, 10, calendar9);

                JSONObject obj10=new JSONObject();
                try {
                    obj10.put("AemEmployeeid", pref.getEmpId());
                    obj10.put("AemClientid",pref.getEmpClintId());
                    obj10.put("Monthid","10");
                    obj10.put("yearid",newMonth.get(Calendar.YEAR));
                    obj10.put("SecurityCode",pref.getSecurityCode());
                    getAttendanceListForNav(obj10,calendar9);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Calendar.NOVEMBER:
                Calendar calendar10 = Calendar.getInstance();
                calendar10.set(newMonth.get(Calendar.YEAR), 10, 1);

                //getAttendanceListForNav(y, 11, calendar10);

                JSONObject obj11=new JSONObject();
                try {
                    obj11.put("AemEmployeeid", pref.getEmpId());
                    obj11.put("AemClientid",pref.getEmpClintId());
                    obj11.put("Monthid","11");
                    obj11.put("yearid",newMonth.get(Calendar.YEAR));
                    obj11.put("SecurityCode",pref.getSecurityCode());
                    getAttendanceListForNav(obj11,calendar10);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Calendar.DECEMBER:
                Calendar calendar11 = Calendar.getInstance();
                calendar11.set(newMonth.get(Calendar.YEAR), 11, 1);

                //getAttendanceListForNav(y, 12, calendar11);

                JSONObject obj12=new JSONObject();
                try {
                    obj12.put("AemEmployeeid", pref.getEmpId());
                    obj12.put("AemClientid",pref.getEmpClintId());
                    obj12.put("Monthid","12");
                    obj12.put("yearid",newMonth.get(Calendar.YEAR));
                    obj12.put("SecurityCode",pref.getSecurityCode());
                    getAttendanceListForNav(obj12,calendar11);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
        return arr;
    }

    private void turnGPSOn() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(AttenDanceDashboardActivity.this).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

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
                            Log.e("LOCATION", "onResult: location on");
                            if (pref.getShiftFlag().equals("1")) {
                                getShift();
                            } else {
                                openMarkAttendanceActivities();
                            }
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            Log.e("LOCATION", "onResult: log 2");
                            try {
                                try {
                                    status.startResolutionForResult(AttenDanceDashboardActivity.this, 1000);
                                } catch (IntentSender.SendIntentException e) {
                                    // Ignore the error.
                                }
                            } catch (Exception e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            Log.e("LOCATION", "onResult: log 3");
                            finish();
                            break;
                        case LocationSettingsStatusCodes.ERROR:
                            Log.e("LOCATION", "onResult: log 4");
                            finish();
                            break;
                    }
                }
            });
        } else {
            if (pref.getShiftFlag().equals("1")) {
                getShift();
            } else {
                openMarkAttendanceActivities();
            }
        }
    }

    private void openMarkAttendanceActivities() {
        if (isLiveStatus_MarkAttnFromMobile.equals("1")) {
            Intent intent = new Intent(AttenDanceDashboardActivity.this, NEW_AttendanceMarkingActivity.class);
            intent.putExtra("MarkAttnFromMobileWithImage", MarkAttnFromMobileWithImage);
            intent.putExtra("MarkAttnFromMobileWithGeoFencingLocation", MarkAttnFromMobileWithGeoFencingLocation);
            intent.putExtra("MarkAttnFromMobileWithWorkPlaceSelection", MarkAttnFromMobileWithWorkPlaceSelection);
            intent.putExtra("MarkAttnFromMobileWithShiftSelection", MarkAttnFromMobileWithShiftSelection);
            intent.putExtra("MarkAttnFromMobileWithHierarchySelection", MarkAttnFromMobileWithHierarchySelection);
            intent.putExtra("MarkAttnFromMobileWithPunchLocationSelection", MarkAttnFromMobileWithPunchLocationSelection);
            startActivity(intent);
        }else if (pref.getEmpClintId().equals("AEMCLI2210001697") || pref.getEmpClintId().equals("AEMCLI2210001698")) {
            Intent intent = new Intent(AttenDanceDashboardActivity.this, AttendanceManageWithoutLocActivity.class);
            intent.putExtra("intt", "2");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (pref.getEmpClintId().equals("AEMCLI1910001556")||pref.getEmpClintId().equals("AEMCLI2410001861")) {
            Intent intent = new Intent(AttenDanceDashboardActivity.this, DailyDashBoardActivity.class);
            intent.putExtra("intt", "2");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (pref.getEmpClintId().equals(ClientID.METSO)) {
            //AEMCLI2110001671
            Intent intent = new Intent(AttenDanceDashboardActivity.this, MetsoAttendanceActivity.class);
            intent.putExtra("intt", "2");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (pref.getEmpClintId().equals("AEMCLI1110000593")) {
            Intent intent = new Intent(AttenDanceDashboardActivity.this, BoschAttendanceActivity.class);
            intent.putExtra("intt", "2");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if (pref.getEmpClintId().equals(ClientID.SKF_CLIENT_ID)
                || pref.getEmpClintId().equals(ClientID.SKF_ITS)
                || pref.getEmpClintId().equals(ClientID.SKF_MSP)
                || pref.getEmpClintId().equals(ClientID.SKF_ENGINEERING_LUB)
                || pref.getEmpClintId().equals(ClientID.SKF_INDUSTRIAL)) {
            Intent intent = new Intent(AttenDanceDashboardActivity.this, ProtectorGambleAttendanceActivity.class);
            intent.putExtra("intt", "2");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if (pref.getEmpClintId().equals(ClientID.SVF)) {
            Intent intent = new Intent(AttenDanceDashboardActivity.this, AttendanceWithOnlyShiftActivity.class);
            intent.putExtra("intt", "2");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if (pref.getEmpClintId().equals(ClientID.PROTACTOR_GAMBLEID) || pref.getEmpClintId().equals(ClientID.WACKER)) {
            Intent intent = new Intent(AttenDanceDashboardActivity.this, GeoFenceAttendanceWithPunchTypeActivity.class);
            intent.putExtra("intt", "2");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (pref.getEmpClintId().equals(ClientID.SPCX) ||pref.getEmpClintId().equals(ClientID.ABFRL) ||pref.getEmpClintId().equals(ClientID.TATA_STEEL) || pref.getEmpClintId().equals(ClientID.APG_HOTEL)) {
            Intent intent = new Intent(AttenDanceDashboardActivity.this, GeoFenceAttendanceWithOutLocActivity.class);
            intent.putExtra("intt", "2");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (pref.getEmpClintOffId().equals(BrunchId.CBRE_HARYANA_KEY_SITE)
                        || pref.getEmpClintOffId().equals(BrunchId.CBRE_DELHI_KEY_SITE)
                        || pref.getEmpClintOffId().equals(BrunchId.CBRE_KARNATAKA_KEY_SITE)
                        || pref.getEmpClintOffId().equals(BrunchId.CBRE_TELANGANA_KEY_SITE)){
            Intent intent = new Intent(AttenDanceDashboardActivity.this, GeoFenceAttendanceWithShiftActivity.class);
            //intent.putExtra("intt", "2");
            //intent.putExtra("shiftStatus", ShiftStatus);
            //intent.putExtra("shiftArray", shiftArray.toString());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }else {
            Intent intent = new Intent(AttenDanceDashboardActivity.this, AttendanceManageActivity.class);
            intent.putExtra("intt", "2");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                openMarkAttendanceActivities();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                finish();
            }
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
        finish();
    }


    private void getPMSSubmitDetails() {
        String surl = AppData.url + "gcl_EmployeePMS_Metso?MasterID=" + pref.getMasterId() + "&SecurityCode=" + pref.getSecurityCode();
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
                            JSONArray job1 = new JSONArray(response);
                            JSONObject jsonObject = job1.optJSONObject(0);
                            String bFlag = jsonObject.optString("bFlag");
                            if (bFlag.equals("0")) {
                                Intent intent = new Intent(AttenDanceDashboardActivity.this, MetsoAttendanceActivity.class);
                                intent.putExtra("intt", "2");
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(AttenDanceDashboardActivity.this, MetsoPMSTargetAchivementActivity.class);
                                intent.putExtra("bFlag", bFlag);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();

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
    void getSubmenu(JSONObject objSubmenu){
        Log.e(TAG, "getSubmenu: "+objSubmenu);
       //AndroidNetworking.post("https://gsppi.geniusconsultant.com/GSPPI_API_V2/api/LAMS/GetSubServiceMenu")
       AndroidNetworking.post(AppData.LAMS_GetSubServiceMenu)
               .addJSONObjectBody(objSubmenu)
               //.addHeaders("SecurityKey", "gStbCQYjYBDCQ4fkGoQSUj7LYe8uVdZ1")
               .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
               .setTag("uploadTest")
               .setPriority(Priority.HIGH)
               .build()
               .getAsJSONObject(new JSONObjectRequestListener() {
                   @Override
                   public void onResponse(JSONObject response) {
                       try {
                           Log.e(TAG, "SUB_MENU: "+response.toString(4));
                           JSONObject job1 = response;
                           String Response_Code = job1.optString("Response_Code");
                           JSONObject Response_Data = job1.optJSONObject("Response_Data");
                           if(Response_Code.equals("101")){
                               Log.e(TAG, "Response_Data: "+Response_Data);
                               Log.e(TAG, "ServiceMenuAccessDetails_Array: "+Response_Data.optJSONArray("ServiceMenuAccessDetails"));
                               JSONArray ServiceMenuAccessDetails_Array = Response_Data.optJSONArray("ServiceMenuAccessDetails");
                               JSONObject ServiceMenuAccessDetails_OBJ = ServiceMenuAccessDetails_Array.getJSONObject(0);
                               String WorkPlaceDetails_Array = Response_Data.optString("WorkPlaceDetails");
                               pref.saveLocationList(WorkPlaceDetails_Array);
                               String WorkingShiftDetails_Array = Response_Data.optString("WorkingShiftDetails");
                               pref.saveShiftList(WorkingShiftDetails_Array);
                               String HierarchyDetails_Array = Response_Data.optString("HierarchyDetails");
                               pref.saveApproverList(HierarchyDetails_Array);
                               HolidayDetails_Array = Response_Data.optString("HolidayDetails");
                               pref.saveHolidayList(HolidayDetails_Array);
                               String OptionalHolidayDetails_Array = Response_Data.optString("OptionalHolidayDetails");
                               pref.saveOptionalHolidayList(OptionalHolidayDetails_Array);
                               String OtherLocation_Array = Response_Data.optString("OtherLocation");
                               pref.saveOtherLocation(OtherLocation_Array);
                                //TODO: Daily Attendance
                               if (ServiceMenuAccessDetails_OBJ.has("MarkAttnFromMobile")){
                                   String MarkAttnFromMobile = ServiceMenuAccessDetails_OBJ.optString("MarkAttnFromMobile");
                                   if (!MarkAttnFromMobile.equals("null") || !MarkAttnFromMobile.isEmpty()){
                                       String[] DailyAttendanceArr = MarkAttnFromMobile.split("_");
                                       String isRequired_MarkAttnFromMobile = DailyAttendanceArr[0];
                                       isLiveStatus_MarkAttnFromMobile = DailyAttendanceArr[1];
                                       if (isLiveStatus_MarkAttnFromMobile.equals("1")){
                                           llAttandanceManage.setVisibility(isRequired_MarkAttnFromMobile.equals("1")?View.VISIBLE:View.GONE);
                                           MarkAttnFromMobileWithImage =  ServiceMenuAccessDetails_OBJ.optString("MarkAttnFromMobileWithImage");
                                           MarkAttnFromMobileWithGeoFencingLocation =  ServiceMenuAccessDetails_OBJ.optString("MarkAttnFromMobileWithGeoFencingLocation");
                                           MarkAttnFromMobileWithWorkPlaceSelection =  ServiceMenuAccessDetails_OBJ.optString("MarkAttnFromMobileWithWorkPlaceSelection");
                                           MarkAttnFromMobileWithShiftSelection =  ServiceMenuAccessDetails_OBJ.optString("MarkAttnFromMobileWithShiftSelection");
                                           MarkAttnFromMobileWithHierarchySelection =  ServiceMenuAccessDetails_OBJ.optString("MarkAttnFromMobileWithHierarchySelection");
                                           MarkAttnFromMobileWithPunchLocationSelection =  ServiceMenuAccessDetails_OBJ.optString("MarkAttnFromMobileWithPunchLocationSelection");
                                       } else {
                                           previousConfiguration_Attendance_Manage();
                                       }
                                   } else {
                                       previousConfiguration_Attendance_Manage();
                                   }
                               } else {
                                   previousConfiguration_Attendance_Manage();
                               }

                               //TODO: Attendance Report
                               if (ServiceMenuAccessDetails_OBJ.has("DailyAttendanceView")){
                                   String DailyAttendanceView = ServiceMenuAccessDetails_OBJ.optString("DailyAttendanceView");
                                   if(!DailyAttendanceView.equals("null") || !DailyAttendanceView.isEmpty() ){
                                       String[] DailyAttendanceViewArr = DailyAttendanceView.split("_");
                                       String isRequired_DailyAttendanceView = DailyAttendanceViewArr[0];
                                       isLiveStatus_DailyAttendanceView = DailyAttendanceViewArr[1];
                                       if (isLiveStatus_DailyAttendanceView.equals("1")){
                                           llAttendanceReport.setVisibility(isRequired_DailyAttendanceView.equals("1")?View.VISIBLE:View.GONE);
                                       } else {
                                           previousConfiguration_AttendanceReport();
                                       }
                                   } else {
                                       previousConfiguration_AttendanceReport();
                                   }
                               } else {
                                   previousConfiguration_AttendanceReport();
                               }


                               //TODO: Attendance Regularisation
                               if (ServiceMenuAccessDetails_OBJ.has("AttendanceRegularisationAccessFromMobile")){
                                   String AttendanceRegularisation = ServiceMenuAccessDetails_OBJ.optString("AttendanceRegularisationAccessFromMobile");
                                   if(AttendanceRegularisation != null ||AttendanceRegularisation.equals("null")|| !AttendanceRegularisation.isEmpty()){
                                       String[] AttReg = AttendanceRegularisation.split("_");
                                       String isRequired_AttReg = AttReg[0];
                                       isLiveStatus_AttReg = AttReg[1];
                                       if (isLiveStatus_AttReg.equals("1")){
                                           if (isRequired_AttReg.equals("1")) {
                                               llBackAttendance.setVisibility(View.VISIBLE);
                                               llAttenRegularize.setVisibility(View.VISIBLE);
                                               AttendanceRegularisationWithDayTypeSelection = ServiceMenuAccessDetails_OBJ.optString("AttendanceRegularisationWithDayTypeSelection");
                                               AttendanceRegularisationWithWorkPlaceSelection = ServiceMenuAccessDetails_OBJ.optString("AttendanceRegularisationWithWorkPlaceSelection");
                                               AttendanceRegularisationWithWorkingShiftSelection = ServiceMenuAccessDetails_OBJ.optString("AttendanceRegularisationWithWorkingShiftSelection");
                                               AttendanceRegularisationWithHierarchySelection = ServiceMenuAccessDetails_OBJ.optString("AttendanceRegularisationWithHierarchySelection");
                                           } else {
                                               llBackAttendance.setVisibility(View.GONE);
                                               llAttenRegularize.setVisibility(View.GONE);
                                           }
                                       } else {
                                           previousConfiguration_AttenRegularize();
                                       }
                                   } else {
                                       previousConfiguration_AttenRegularize();
                                   }
                               } else {
                                   previousConfiguration_AttenRegularize();
                               }



                               //TODO: Weekly Off
                               if ( ServiceMenuAccessDetails_OBJ.has("WOMarkingFromMobileApp")){
                                   String WeeklyOff = ServiceMenuAccessDetails_OBJ.optString("WOMarkingFromMobileApp");
                                   if (!WeeklyOff.isEmpty() || !WeeklyOff.equals("null")){
                                       String[] WeeklyOffArr = WeeklyOff.split("_");
                                       String isRequired_WeeklyOff = WeeklyOffArr[0];
                                       isLiveStatus_WeeklyOff= WeeklyOffArr[1];
                                       if(isLiveStatus_WeeklyOff.equals("1")){
                                           WOMarkingWithHierarchySelection = ServiceMenuAccessDetails_OBJ.optString("WOMarkingWithHierarchySelection");
                                           llWeekly.setVisibility(isRequired_WeeklyOff.equals("1") ? View.VISIBLE : View.GONE);
                                       } else {
                                           previousConfiguration_WeeklyOFF();
                                       }
                                   }else {
                                       previousConfiguration_WeeklyOFF();
                                   }
                               } else {
                                   previousConfiguration_WeeklyOFF();
                               }


                               //TODO: Holiday Marking
                               if (ServiceMenuAccessDetails_OBJ.has("NormalHolidayMarkingFromMobileApp")){
                                   String HolidayMarking = ServiceMenuAccessDetails_OBJ.optString("NormalHolidayMarkingFromMobileApp");
                                   if (!HolidayMarking.isEmpty() || !HolidayMarking.equals("null")){
                                       String[] HolidayMarkingArr = HolidayMarking.split("_");
                                       String isRequired_HolidayMarking = HolidayMarkingArr[0];
                                       isLiveStatus_HolidayMarking= HolidayMarkingArr[1];
                                       if(isLiveStatus_HolidayMarking.equals("1")){
                                           //llHoliday.setVisibility(isRequired_HolidayMarking.equals("1") ? View.VISIBLE : View.GONE);
                                           if (isRequired_HolidayMarking.equals("1")){
                                               llHoliday.setVisibility(View.VISIBLE);
                                           } else {
                                               llHoliday.setVisibility(View.GONE);
                                           }
                                           holidayFlag="1";
                                           NormalHolidayMarkingWithHolidaySelection = ServiceMenuAccessDetails_OBJ.optString("NormalHolidayMarkingWithHolidaySelection");
                                           NormalHolidayMarkingWithHierarchySelection = ServiceMenuAccessDetails_OBJ.optString("NormalHolidayMarkingWithHierarchySelection");
                                       } else {
                                           previousConfiguration_HolidayMarking();
                                       }
                                   } else {
                                       previousConfiguration_HolidayMarking();
                                   }
                               } else {
                                   previousConfiguration_HolidayMarking();
                               }

                               //TODO: Optional Holiday Marking
                               if (ServiceMenuAccessDetails_OBJ.has("OptionalHolidayMarkingFromMobileApp")){
                                   String OptionalHolidayMarking = ServiceMenuAccessDetails_OBJ.optString("OptionalHolidayMarkingFromMobileApp");
                                   if (!OptionalHolidayMarking.isEmpty() || !OptionalHolidayMarking.equals("null")){
                                       String[] OptionalHolidayMarkingArr = OptionalHolidayMarking.split("_");
                                       String isRequired_OHolidayMarking = OptionalHolidayMarkingArr[0];
                                       isLiveStatus_OHolidayMarking = OptionalHolidayMarkingArr[1];
                                       if(isLiveStatus_OHolidayMarking.equals("1")){
                                           OptionalHolidayFlag="1";
                                           OptionalHolidayMarkingWithHolidaySelection = ServiceMenuAccessDetails_OBJ.optString("OptionalHolidayMarkingWithHolidaySelection");
                                           OptionalHolidayMarkingWithHierarchySelection = ServiceMenuAccessDetails_OBJ.optString("OptionalHolidayMarkingWithHierarchySelection");
                                           llOptionalHoliday.setVisibility(isRequired_OHolidayMarking.equals("1") ? View.VISIBLE : View.GONE);
                                       } else {
                                           //previousConfiguration_HolidayMarking();
                                       }
                                   } else {
                                       //previousConfiguration_HolidayMarking();
                                   }
                               } else {
                                   //previousConfiguration_HolidayMarking();
                               }

                               //TODO: Adjustment Application (Com-off,WFH,OD)
                               if (ServiceMenuAccessDetails_OBJ.has("AttnAdjCOApplicationAccessFromMobile")){
                                   AdjustmentStatusChecking(ServiceMenuAccessDetails_OBJ.optString("AttnAdjCOApplicationAccessFromMobile"));
                                   AttnAdjCOAppliocationWithMarkedHierarchy = ServiceMenuAccessDetails_OBJ.optString("AttnAdjCOAppliocationWithMarkedHierarchy");
                               } else if (ServiceMenuAccessDetails_OBJ.has("AttnAdjWFHApplicationAccessFromMobile")){
                                   AdjustmentStatusChecking(ServiceMenuAccessDetails_OBJ.optString("AttnAdjWFHApplicationAccessFromMobile"));
                                   AttnAdjWFHAppliocationWithMarkedHierarchy = ServiceMenuAccessDetails_OBJ.optString("AttnAdjWFHAppliocationWithMarkedHierarchy");
                               } else if (ServiceMenuAccessDetails_OBJ.has("AttnAdjODAccessFromMobileApp")){
                                   AdjustmentStatusChecking(ServiceMenuAccessDetails_OBJ.optString("AttnAdjODAccessFromMobileApp"));
                                   AttnAdjODMarkHierarchyAtEntry = ServiceMenuAccessDetails_OBJ.optString("AttnAdjODMarkHierarchyAtEntry");
                               } else {
                                   previousConfiguration_Adjustment();
                               }


                               //TODO: Holiday View
                               if (ServiceMenuAccessDetails_OBJ.has("NormalHolidayViewFromMobile")){
                                   String NormalHolidayViewFromMobile = ServiceMenuAccessDetails_OBJ.optString("NormalHolidayViewFromMobile");
                                   if(!NormalHolidayViewFromMobile.isEmpty()){
                                       HolidayViewStatusChecking(NormalHolidayViewFromMobile);
                                   } else {
                                       previousConfiguration_HolidayView();
                                   }
                               }
                               if(ServiceMenuAccessDetails_OBJ.has("OptionalHolidayViewFromMobile")){
                                   String OptionalHolidayViewFromMobile = ServiceMenuAccessDetails_OBJ.optString("OptionalHolidayViewFromMobile");
                                   if(!OptionalHolidayViewFromMobile.isEmpty()){
                                       HolidayViewStatusChecking(OptionalHolidayViewFromMobile);
                                   } else {
                                       previousConfiguration_HolidayView();
                                   }
                               }


                               //TODO: Leave Balance Management
                               if (ServiceMenuAccessDetails_OBJ.has("LeaveBalanceViewFromMobile")){
                                   String LeaveBalanceManagement = ServiceMenuAccessDetails_OBJ.optString("LeaveBalanceViewFromMobile");
                                   if (!LeaveBalanceManagement.isEmpty() || !LeaveBalanceManagement.equals("null")){
                                       String[] LeaveBalanceManagementArr = LeaveBalanceManagement.split("_");
                                       String isRequired_LeaveBalanceManagement= LeaveBalanceManagementArr[0];
                                       String isConfigure_LeaveBalanceManagement= LeaveBalanceManagementArr[1];
                                       if(isConfigure_LeaveBalanceManagement.equals("1")){
                                           llViewLeaveBalance.setVisibility(isRequired_LeaveBalanceManagement.equals("1") ? View.VISIBLE : View.GONE);
                                       } else {
                                           previousConfiguration_LeaveBalanceManagement();
                                       }
                                   } else {
                                       previousConfiguration_LeaveBalanceManagement();
                                   }
                               } else {
                                   previousConfiguration_LeaveBalanceManagement();
                               }

                               AllApplicationViewConfiguration();
                           }
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                   }

                   @Override
                   public void onError(ANError anError) {
                       Log.e(TAG, "SUB_MENU_error: "+anError.getErrorBody());
                   }
               });
   }

   void previousConfiguration_WeeklyOFF(){
       if (pref.getOnLeave().equals("1")) {
           llWeekly.setVisibility(View.VISIBLE);
       } else {
           llWeekly.setVisibility(View.GONE);
       }
   }
   void previousConfiguration_AttenRegularize(){
        if (pref.getBackAttd().equals("1")) {
            llBackAttendance.setVisibility(View.VISIBLE);
            llAttenRegularize.setVisibility(View.VISIBLE);
        } else {
            llBackAttendance.setVisibility(View.GONE);
            llAttenRegularize.setVisibility(View.GONE);
        }
    }
    void previousConfiguration_Attendance_Manage(){
        if (pref.getEmpClintId().equals(ClientID.SHALIMAR_WIRES)){
            llAttandanceManage.setVisibility(View.GONE);
            llAttendanceReport.setVisibility(View.GONE);
            btnMarkAttendance.setVisibility(View.GONE);
        } else {
            llAttandanceManage.setVisibility(View.VISIBLE);
            llAttendanceReport.setVisibility(View.VISIBLE);
            btnMarkAttendance.setVisibility(View.VISIBLE);
        }
    }
    void previousConfiguration_AttendanceReport(){
        if (pref.getEmpClintId().equals(ClientID.SHALIMAR_WIRES)){
            llAttendanceReport.setVisibility(View.GONE);
        } else {
            llAttendanceReport.setVisibility(View.VISIBLE);
        }
    }

    void previousConfiguration_HolidayMarking(){
        if (pref.getEmpClintId().equals(ClientID.SKF_CLIENT_ID)
                || pref.getEmpClintId().equals(ClientID.SKF_ITS)
                || pref.getEmpClintId().equals(ClientID.SKF_MSP)
                || pref.getEmpClintId().equals(ClientID.SKF_ENGINEERING_LUB)
                || pref.getEmpClintId().equals(ClientID.SKF_INDUSTRIAL)
                ||pref.getEmpClintId().equals(ClientID.SVF)
        ){
            llHoliday.setVisibility(View.VISIBLE);
        } else {
            llHoliday.setVisibility(View.GONE);
        }
    }

    void previousConfiguration_LeaveBalanceManagement(){
        if (AppData.LEAVE_BALANCE_VIEW_FLAG.equals("1")){
            llViewLeaveBalance.setVisibility(View.VISIBLE);
        } else {
            llViewLeaveBalance.setVisibility(View.GONE);
        }
    }

    void previousConfiguration_Adjustment(){
        if (pref.getAdjustmentStatus().equals("1")) {
            llAdjustment.setVisibility(View.VISIBLE);
        } else {
            llAdjustment.setVisibility(View.GONE);
        }
    }
    void previousConfiguration_HolidayView(){
        if (pref.getEmpClintId().equals(ClientID.SKY_ROOT) || pref.getEmpClintId().equals(ClientID.WACKER) || pref.getEmpClintId().equals(ClientID.ABFRL)){
            llHolidayView.setVisibility(View.VISIBLE);
        } else {
            llHolidayView.setVisibility(View.GONE);
        }
    }
    void AdjustmentStatusChecking(String status){
        if (!status.isEmpty() || !status.equals("null")){
            String[] AdjustmentApplicationOffArr = status.split("_");
            String isRequired_Adjustment= AdjustmentApplicationOffArr[0];
            isLiveSatus_Adjustment= AdjustmentApplicationOffArr[1];
            if(isLiveSatus_Adjustment.equals("1")){
                llAdjustment.setVisibility(isRequired_Adjustment.equals("1")?View.VISIBLE : View.GONE);
            } else {
                previousConfiguration_Adjustment();
            }
        } else {
            previousConfiguration_Adjustment();
        }
    }

    void HolidayViewStatusChecking(String statusHolidayView){
        if (!statusHolidayView.isEmpty() || !statusHolidayView.equals("null")){
            String[] HolidayViewArr = statusHolidayView.split("_");
            String isRequired_HolidayView= HolidayViewArr[0];
            isLiveStatus_HolidayView= HolidayViewArr[1];
            if(isLiveStatus_OHolidayMarking.equals("1")){
                llHolidayView.setVisibility(isRequired_HolidayView.equals("1")?View.VISIBLE:View.GONE);
            } else {
                previousConfiguration_HolidayView();
            }
        } else {
            previousConfiguration_HolidayView();
        }
    }

    void AllApplicationViewConfiguration(){
        if (isLiveStatus_HolidayMarking.equals("1") || isLiveStatus_OHolidayMarking.equals("1")
                || isLiveStatus_AttReg.equals("1")
                || isLiveStatus_WeeklyOff.equals("1")
                || isLiveSatus_Adjustment.equals("1")){
            llAllApplicationView.setVisibility(View.VISIBLE);
        } else {
            llAllApplicationView.setVisibility(View.GONE);
        }
    }
}