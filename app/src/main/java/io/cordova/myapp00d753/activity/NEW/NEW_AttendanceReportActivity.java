package io.cordova.myapp00d753.activity.NEW;

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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.EmployeeDashBoardActivity;
import io.cordova.myapp00d753.activity.NEW.adapter.NEW_AttendanceAdapter;
import io.cordova.myapp00d753.activity.NEW.model.AttendanceReportModel;
import io.cordova.myapp00d753.adapter.AttendanceAdapter;
import io.cordova.myapp00d753.module.AttendanceModule;
import io.cordova.myapp00d753.module.SpModule;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.YearMonthUtil;

public class NEW_AttendanceReportActivity extends AppCompatActivity {
    private static final String TAG = "NEW_AttendanceReportAct";
    RecyclerView rvAttendanceReport;
    //ArrayList<AttendanceModule>attendabceInfiList=new ArrayList<>();
    ArrayList<AttendanceReportModel>attendanceReportList=new ArrayList<>();
    AttendanceModule attendanceModule;
    ImageView imgBack,imgHome;
    LinearLayout llSearch;
    private AlertDialog alertDialog,alertDialog1,alertDialog2;
    ArrayList<SpModule>spItem=new ArrayList<>();
    Spinner spYear;

    String[] spMonthList = {"-----select-----","January","February","March","April","May","June","July","March","May","June","July","August","September","October","November","December"};
    ProgressBar progressBar;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    public static int mPageCount = 0;
    boolean mIsEndReached = false;
    private boolean loading = false;
    LinearLayoutManager layoutManager;
    LinearLayout llLoder;
    NEW_AttendanceAdapter attendanceAdapter;
    String year;
    int y;
    TextView tvYear;
    LinearLayout llMain,llNodata;
    String month;
    TextView tvMonth;
    String AttendanceID;
    Pref pref;
    int monthNo=0;
    NetworkConnectionCheck connectionCheck;
    LinearLayout llAgain;
    ImageView imgAgain,imgSearch;
    ArrayList<String> financialYearList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_report);
        mPageCount=1;
        initialize();

        onClick();
        if (connectionCheck.isNetworkAvailable()) {
            //getAttendanceList();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("ConsultantID",pref.getEmpConId());
                jsonObject.put("clientid",pref.getEmpClintId());
                jsonObject.put("empid",pref.getEmpId());
                jsonObject.put("Year",year);
                jsonObject.put("Month",String.valueOf(monthNo));
                jsonObject.put("SecurityCode",pref.getSecurityCode());
                getAttendanceReport(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            connectionCheck.getNetworkActiveAlert().show();
        }
    }

    private void getAttendanceReport(JSONObject jsonObject) {
        try {
            Log.e(TAG, "getAttendanceReport: INPUT: "+jsonObject.toString(4));
        }catch (Exception e){
            e.printStackTrace();
        }

        llLoder.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);
        llAgain.setVisibility(View.GONE);

        AndroidNetworking.post(AppData.LAMS_DailyAttendanceReportEmp)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "ATTENDANCE_REPORT: "+response.toString(4));
                            JSONObject object = new JSONObject(String.valueOf(response));
                            String Response_Message = object.optString("Response_Message");
                            String Response_Code = object.optString("Response_Code");

                            if (Response_Code.equals("101")) {
                                JSONObject Response_Data = object.optJSONObject("Response_Data");
                                JSONArray Table = Response_Data.optJSONArray("Table");
                                if (Table.length()>0){
                                    for (int i = 0; i < Table.length(); i++){
                                        JSONObject obj=Table.getJSONObject(i);
                                        String SLNO = obj.optString("Sl. No.");
                                        String date = obj.optString("Date");
                                        String intime = obj.optString("Intime");
                                        String outTime = obj.optString("OutTime");
                                        String AttendanceStatus = obj.optString("Attendance Status");
                                        String firstHalfStatus = obj.optString("First Half Status");
                                        String secondHalfStatus = obj.optString("Second Half Status");
                                        String workingHours = obj.optString("Working Hours");
                                        String inAddress = obj.optString("In Address");
                                        String inPunchSource = obj.optString("In-Punch Source");
                                        String outAddress = obj.optString("Out Address");
                                        String outPunchSource = obj.optString("Out-Punch Source");
                                        String fName = obj.optString("FName");
                                        String inImage = obj.optString("InImage");
                                        String outImage = obj.optString("OutImage");
                                        String location = obj.optString("Location");
                                        String workingShift = obj.optString("WorkingShift");
                                        String selectedApprover = obj.optString("SelectedApprover");
                                        String regularisation = obj.optString("Regularisation");
                                        String approvalStatus = obj.optString("ApprovalStatus");
                                        String DayType = obj.optString("DayType");

                                        AttendanceReportModel attendanceReportModel = new AttendanceReportModel(SLNO,date,intime,outTime,AttendanceStatus,firstHalfStatus,secondHalfStatus,
                                                workingHours,inAddress,inPunchSource,outAddress,outPunchSource,fName,inImage,outImage,location,workingShift,selectedApprover,regularisation,approvalStatus,DayType);
                                        attendanceReportList.add(attendanceReportModel);
                                    }
                                    attendanceAdapter.notifyDataSetChanged();
                                    llLoder.setVisibility(View.GONE);
                                    llMain.setVisibility(View.VISIBLE);
                                    llNodata.setVisibility(View.GONE);
                                    llAgain.setVisibility(View.GONE);
                                } else {
                                    //attendanceAdapter.notifyDataSetChanged();
                                    llLoder.setVisibility(View.GONE);
                                    llMain.setVisibility(View.GONE);
                                    llNodata.setVisibility(View.VISIBLE);
                                    llAgain.setVisibility(View.GONE);
                                }

                            } else {
                                llLoder.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNodata.setVisibility(View.VISIBLE);
                                llAgain.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "ATTENDANCE_REPORT_error: "+anError.getErrorBody());
                        llLoder.setVisibility(View.GONE);
                        llMain.setVisibility(View.GONE);
                        llNodata.setVisibility(View.VISIBLE);
                        llAgain.setVisibility(View.GONE);
                    }
                });
    }

    private  void  initialize(){
        pref=new Pref(NEW_AttendanceReportActivity.this);
        connectionCheck=new NetworkConnectionCheck(NEW_AttendanceReportActivity.this);
        rvAttendanceReport=(RecyclerView)findViewById(R.id.rvAttendanceReport);
        layoutManager = new LinearLayoutManager(NEW_AttendanceReportActivity.this, LinearLayoutManager.VERTICAL, false);
        rvAttendanceReport.setLayoutManager(layoutManager);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        llSearch=(LinearLayout)findViewById(R.id.llSearch);
        llLoder=(LinearLayout)findViewById(R.id.llWLLoader) ;
        llMain=(LinearLayout)findViewById(R.id.llMain);
        llNodata=(LinearLayout)findViewById(R.id.llNodata);
        progressBar=(ProgressBar)findViewById(R.id.WLpagination_loader);
     /*   rvAttendanceReport.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                    if (!loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = true;

                            progressBar.setVisibility(View.VISIBLE);
                            if (!mIsEndReached) {
                                mPageCount=mPageCount+1;
                                getAttendanceList();
                            }

                        }
                    }
                }
            }
        });*/
        setAdapter();
        y= Calendar.getInstance().get(Calendar.YEAR);
        year=String.valueOf(y);
        Log.d("year", year);
        year = YearMonthUtil.getCurrentFinancialYear();
        financialYearList = YearMonthUtil.getFinancialYearList();

        int m=Calendar.getInstance().get(Calendar.MONTH)+1;
        monthNo = m;
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


        llAgain=(LinearLayout)findViewById(R.id.llAgain);
        imgAgain=(ImageView)findViewById(R.id.imgAgain);
        imgAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getAttendanceList();

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("ConsultantID",pref.getEmpConId());
                    jsonObject.put("clientid",pref.getEmpClintId());
                    jsonObject.put("empid",pref.getEmpId());
                    jsonObject.put("Year",year);
                    jsonObject.put("Month",String.valueOf(monthNo));
                    jsonObject.put("SecurityCode",pref.getSecurityCode());
                    getAttendanceReport(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        imgSearch=(ImageView)findViewById(R.id.imgSearch);

    }

    /*private void getAttendanceList(){
        Log.d("Arpan","arpan");
        llLoder.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);
        llAgain.setVisibility(View.GONE);
        String surl = AppData.url+"get_GCLSelfAttendanceWoLeave?AEMConsultantID="+pref.getEmpConId()+"&AEMClientID="+pref.getEmpClintId()+"&AEMClientOfficeID="+pref.getEmpClintOffId()+"&AEMEmployeeID="+pref.getEmpId()+"&CurrentPage=0&AID=0&ApproverStatus=4&YearVal="+year+"&MonthName="+month+"&WorkingStatus=1&SecurityCode="+pref.getSecurityCode()+"&DbOperation=8&AttIds=null";
        Log.d("input",surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);

                        attendanceReportList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText=job1.optString("responseText");

                            boolean responseStatus=job1.optBoolean("responseStatus");
                            if (responseStatus){
                               // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData=job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++){
                                    JSONObject obj=responseData.getJSONObject(i);
                                    String SLNO = obj.optString("Sl. No.");
                                    String date = obj.optString("Date");
                                    String intime = obj.optString("Intime");
                                    String outTime = obj.optString("OutTime");
                                    String AttendanceStatus = obj.optString("Attendance Status");
                                    String firstHalfStatus = obj.optString("First Half Status");
                                    String secondHalfStatus = obj.optString("Second Half Status");
                                    String workingHours = obj.optString("Working Hours");
                                    String inAddress = obj.optString("In Address");
                                    String inPunchSource = obj.optString("In-Punch Source");
                                    String outAddress = obj.optString("Out Address");
                                    String outPunchSource = obj.optString("Out-Punch Source");
                                    String fName = obj.optString("FName");
                                    String inImage = obj.optString("InImage");
                                    String outImage = obj.optString("OutImage");
                                    String location = obj.optString("Location");
                                    String workingShift = obj.optString("WorkingShift");
                                    String selectedApprover = obj.optString("SelectedApprover");
                                    String regularisation = obj.optString("Regularisation");
                                    String approvalStatus = obj.optString("ApprovalStatus");
                                    String DayType = obj.optString("DayType");

                                    AttendanceReportModel attendanceReportModel = new AttendanceReportModel(SLNO,date,intime,outTime,AttendanceStatus,firstHalfStatus,secondHalfStatus,
                                            workingHours,inAddress,inPunchSource,outAddress,outPunchSource,fName,inImage,outImage,location,workingShift,selectedApprover,regularisation,approvalStatus,);
                                    attendanceReportList.add(attendanceReportModel);
                                }
                                attendanceAdapter.notifyDataSetChanged();
                                llLoder.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.GONE);
                                llAgain.setVisibility(View.GONE);

                            }

                            else {
                                attendanceAdapter.notifyDataSetChanged();
                                llLoder.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.VISIBLE);
                                llAgain.setVisibility(View.GONE);
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
                llLoder.setVisibility(View.GONE);
                llMain.setVisibility(View.GONE);
                llNodata.setVisibility(View.GONE);
                llAgain.setVisibility(View.VISIBLE);

               // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert",error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
    }*/

    private  void setAdapter(){
         attendanceAdapter=new NEW_AttendanceAdapter(attendanceReportList);
        rvAttendanceReport.setAdapter(attendanceAdapter);
    }

    private void onClick(){
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(NEW_AttendanceReportActivity.this, EmployeeDashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
              //  finish();
            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NEW_AttendanceReportActivity.this, R.style.CustomDialogNew);
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.attendancereportsearch, null);
                dialogBuilder.setView(dialogView);
                LinearLayout llYear=(LinearLayout)dialogView.findViewById(R.id.llYear);
                tvYear=(TextView) dialogView.findViewById(R.id.tvYear);
                tvMonth=(TextView)dialogView.findViewById(R.id.tvMonth);
                ImageView imgCancel=(ImageView)dialogView.findViewById(R.id.imgCancel);

                llYear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       showYearDialog();
                    }
                });

                tvYear.setText(year);
                LinearLayout llMonth=(LinearLayout)dialogView.findViewById(R.id.llMonth);
                llMonth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showMonthDialog();

                    }
                });
                tvMonth.setText(month);

                Button btnSubmit=(Button)dialogView.findViewById(R.id.btnSubmit);
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPageCount=1;
                        attendanceReportList.clear();
                        //getAttendanceList();
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("ConsultantID",pref.getEmpConId());
                            jsonObject.put("clientid",pref.getEmpClintId());
                            jsonObject.put("empid",pref.getEmpId());
                            jsonObject.put("Year",year);
                            jsonObject.put("Month",String.valueOf(monthNo));
                            jsonObject.put("SecurityCode",pref.getSecurityCode());
                            getAttendanceReport(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        alertDialog.dismiss();
                    }
                });
                imgCancel.setOnClickListener(new View.OnClickListener() {
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
        });


    }


    private void showYearDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NEW_AttendanceReportActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_year, null);
        dialogBuilder.setView(dialogView);
        final TextView tvYear1=(TextView)dialogView.findViewById(R.id.tvYear1);
        final TextView tvYear2=(TextView)dialogView.findViewById(R.id.tvYear2);
        final TextView tvYear3=(TextView)dialogView.findViewById(R.id.tvYear3);
        LinearLayout llY1=(LinearLayout)dialogView.findViewById(R.id.llY1);
        LinearLayout llY2=(LinearLayout)dialogView.findViewById(R.id.llY2);
        LinearLayout llY3=(LinearLayout)dialogView.findViewById(R.id.llY3);

        /*int pastx1=y-2;
        String pasty1=String.valueOf(pastx1);
        tvYear1.setText(pasty1);*/
        tvYear1.setText(financialYearList.get(0));

        /*int pastx2=y-1;
        String pasty2=String.valueOf(pastx2);
        tvYear2.setText(pasty2);*/
        tvYear2.setText(financialYearList.get(1));

        //String pastx3=String.valueOf(y);
        //tvYear3.setText(pastx3);
        tvYear3.setText(financialYearList.get(2));

        ImageView imgCancel=(ImageView)dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog1.dismiss();


            }
        });


        llY3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year=tvYear3.getText().toString();
                Log.d("yrtrr",year);
                tvYear.setText(year);
                alertDialog1.dismiss();

            }
        });

        llY2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year=tvYear2.getText().toString();
                alertDialog1.dismiss();
                tvYear.setText(year);
                Log.d("ttt",year);
            }
        });

        llY1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year=tvYear1.getText().toString();
                alertDialog1.dismiss();
                tvYear.setText(year);
                Log.d("ttt",year);
            }
        });

        alertDialog1= dialogBuilder.create();
        alertDialog1.setCancelable(true);
        Window window = alertDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog1.show();
    }

    private void showMonthDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NEW_AttendanceReportActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_month, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llM1=(LinearLayout)dialogView.findViewById(R.id.llM1);
        LinearLayout llM2=(LinearLayout)dialogView.findViewById(R.id.llM2);
        LinearLayout llM3=(LinearLayout)dialogView.findViewById(R.id.llM3);
        LinearLayout llM4=(LinearLayout)dialogView.findViewById(R.id.llM4);
        LinearLayout llM5=(LinearLayout)dialogView.findViewById(R.id.llM5);
        LinearLayout llM6=(LinearLayout)dialogView.findViewById(R.id.llM6);
        LinearLayout llM7=(LinearLayout)dialogView.findViewById(R.id.llM7);
        LinearLayout llM8=(LinearLayout)dialogView.findViewById(R.id.llM8);
        LinearLayout llM9=(LinearLayout)dialogView.findViewById(R.id.llM9);
        LinearLayout llM10=(LinearLayout)dialogView.findViewById(R.id.llM10);
        LinearLayout llM11=(LinearLayout)dialogView.findViewById(R.id.llM111);
        LinearLayout llM112=(LinearLayout)dialogView.findViewById(R.id.llM12);

        final TextView tvJan=(TextView)dialogView.findViewById(R.id.tvJan);
        tvJan.setText("January");
        final TextView tvFeb=(TextView)dialogView.findViewById(R.id.tvFeb);
        final TextView tvMarch=(TextView)dialogView.findViewById(R.id.tvMarch);
        final TextView tvApril=(TextView)dialogView.findViewById(R.id.tvApril);
        final TextView tvMay=(TextView)dialogView.findViewById(R.id.tvMay);
        final TextView tvJune=(TextView)dialogView.findViewById(R.id.tvJune);
        final TextView tvJuly=(TextView)dialogView.findViewById(R.id.tvJuly);
        final TextView tvAugust=(TextView)dialogView.findViewById(R.id.tvAugust);
        final TextView tvSept=(TextView)dialogView.findViewById(R.id.tvSeptember);
        final TextView tvOct=(TextView)dialogView.findViewById(R.id.tvOct);
        final TextView tvNov=(TextView)dialogView.findViewById(R.id.tvNovember);
        final TextView tvDec=(TextView)dialogView.findViewById(R.id.tvDecember);

        llM1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month=tvJan.getText().toString();
                Log.d("monnn",month);
                tvMonth.setText(month);
                alertDialog2.dismiss();
                monthNo = 1;
            }
        });
        llM2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month=tvFeb.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
                monthNo = 2;
            }
        });

        llM3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month=tvMarch.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
                monthNo = 3;
            }
        });
        llM4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month=tvApril.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
                monthNo = 4;
            }
        });
        llM5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month=tvMay.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
                monthNo = 5;
            }
        });

        llM6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month=tvJune.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
                monthNo = 6;
            }
        });
        llM7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month=tvJuly.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
                monthNo = 7;
            }
        });
        llM8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month=tvAugust.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
                monthNo = 8;
            }
        });
        llM9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month=tvSept.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
                monthNo = 9;
            }
        });
        llM10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month=tvOct.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
                monthNo = 10;
            }
        });
        llM11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month=tvNov.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
                monthNo = 11;
            }
        });
        llM112.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month=tvDec.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
                monthNo = 12;
            }
        });
        ImageView imgCancel=(ImageView)dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog2.dismiss();
            }
        });


        alertDialog2 = dialogBuilder.create();
        alertDialog2.setCancelable(true);
        Window window = alertDialog2.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog2.show();

    }






}
