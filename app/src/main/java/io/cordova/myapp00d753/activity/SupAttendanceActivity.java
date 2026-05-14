package io.cordova.myapp00d753.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.murugappa.AttendanceDashboardActivity;
import io.cordova.myapp00d753.adapter.ODOMeterApprovalAdapter;
import io.cordova.myapp00d753.adapter.SupervisorMenuAdapter;
import io.cordova.myapp00d753.bluedart.ODOmeterApprvalActivity;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

public class SupAttendanceActivity extends AppCompatActivity {
    private static final String TAG = "SupAttendanceActivity";
    LinearLayout llAttandanceManage,llAttendanceReport,llApproval,llQR,llODOmeter,llAdjApproval,llAdjReport,llLeaveBalaceReport;
    ImageView imgBack,imgHome;
    Pref pref;
    String NewLMSAccess="";
    RecyclerView rvItem;
    LinearLayout llOldLms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sup_attendance);
        initialize();
        onClick();
    }

    private void initialize(){
        pref=new Pref(SupAttendanceActivity.this);
        llOldLms=findViewById(R.id.llOldLms);
        llAttandanceManage=(LinearLayout)findViewById(R.id.llAttandanceManage);
        llAdjApproval=findViewById(R.id.llAdjApproval);
        llAttendanceReport=(LinearLayout)findViewById(R.id.llAttendanceReport);
        llODOmeter=(LinearLayout)findViewById(R.id.llODOmeter);
        llApproval=(LinearLayout)findViewById(R.id.llApproval);
        llQR=(LinearLayout)findViewById(R.id.llQR);
        llAdjReport=(LinearLayout) findViewById(R.id.llAdjReport);
        llLeaveBalaceReport=(LinearLayout) findViewById(R.id.llLeaveBalaceReport);
        if (pref.getEmpClintId().equals("AEMCLI0910000315")){
            llQR.setVisibility(View.VISIBLE);
        }else {
            llQR.setVisibility(View.GONE);
        }

        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        rvItem=findViewById(R.id.rvItem);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvItem.setLayoutManager(gridLayoutManager);
        JSONObject objSubmenu=new JSONObject();
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
        }



    }

    private void onClick(){
        if (pref.getEmpClintId().equals("AEMCLI1810001410")){
            llODOmeter.setVisibility(View.VISIBLE);
        } else {
            llODOmeter.setVisibility(View.GONE);
        }
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SupAttendanceActivity.this,SuperVisiorDashBoardActivity.class);
                startActivity(intent);
                 finish();
            }
        });
        llAttandanceManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (pref.getEmpClintId().equalsIgnoreCase("AEMCLI2410001867") || pref.getEmpClintId().equalsIgnoreCase("AEMCLI1310000782")){
                    Intent intent=new Intent(SupAttendanceActivity.this, AttendanceDashboardActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent=new Intent(SupAttendanceActivity.this,SupAttenManageActivity.class);
                    startActivity(intent);
                }


            }
        });

        llAttendanceReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SupAttendanceActivity.this, ITViewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("url", pref.getSupDailyAttenReportUrl());
                startActivity(intent);
            }
        });

        llLeaveBalaceReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SupAttendanceActivity.this, ITViewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("url", pref.getSupLeaveBalanceYTDReportUrl());
                startActivity(intent);
            }
        });

        llAdjReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SupAttendanceActivity.this, ITViewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("url", pref.getSupAdjAndRegReportUrl());
                startActivity(intent);
            }
        });

        llQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(SupAttendanceActivity.this,QRGeneratorActivity.class);
                startActivity(intent);
            }
        });

        llODOmeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(SupAttendanceActivity.this, ODOmeterApprvalActivity.class);
                startActivity(intent);
            }
        });

        llApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent = new Intent(SupAttendanceActivity.this, ITViewActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("url", pref.getRegApprovalURL());
                    startActivity(intent);

            }
        });


        llAdjApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent = new Intent(SupAttendanceActivity.this, ITViewActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("url", pref.getAdjApprovalURL());
                    startActivity(intent);

            }
        });
    }

    void getSubmenu(JSONObject objSubmenu){
        Log.e(TAG, "getSubmenu: "+objSubmenu);
        ProgressDialog pd = new ProgressDialog(SupAttendanceActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        //AndroidNetworking.post("https://gsppi.geniusconsultant.com/GSPPI_API_V2/api/LAMS/GetSubServiceMenu")
        AndroidNetworking.post(AppData.LAMS_GetSubServiceMenu)
                //AndroidNetworking.post("http://171.16.1.10/GSPPI_API_V2/api/LAMS/GetSubServiceMenu")
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
                            pd.dismiss();
                            Log.e(TAG, "SUB_MENU: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            JSONObject Response_Data = job1.optJSONObject("Response_Data");
                            if(Response_Code.equals("101")){
                                Log.e(TAG, "Response_Data: "+Response_Data);
                                Log.e(TAG, "ServiceMenuAccessDetails_Array: "+Response_Data.optJSONArray("ServiceMenuAccessDetails"));



                                JSONArray supervisorMenu = Response_Data.getJSONArray("SupervisorMenu");
                                JSONObject menuObj = supervisorMenu.getJSONObject(0);
                                int NewLMSAccess = menuObj.optInt("NewLMSAccess");
                                if (NewLMSAccess==1){
                                    llOldLms.setVisibility(View.GONE);
                                    rvItem.setVisibility(View.VISIBLE);
                                }else {
                                    llOldLms.setVisibility(View.VISIBLE);
                                    rvItem.setVisibility(View.GONE);
                                }
                                ArrayList<HashMap<String, String>> menuList = new ArrayList<>();



                                HashMap<String, String> nameMap = new HashMap<>();
                                //nameMap.put("NewLMSAccess", "New LMS");
                               // nameMap.put("AttendanceAccess", "Attendance");
                                //nameMap.put("LeaveAccess", "Leave");
                                nameMap.put("AttnMarkAccess", "Attendance Mark");
                                nameMap.put("DailyAttnReportAccess", "Attendance Report");
                                //nameMap.put("LeaveApprovalAccess", "Leave Approval");
                                nameMap.put("AttnRegApprovalAccess", "Attendance Approval");
                                nameMap.put("AttnAdjustmentReportAccess", "Adjustment Report");
                                nameMap.put("AdjApprovalAccess", "Adjustment Approval");

                                //nameMap.put("LeaveApplicationReportAccess", "Leave Application Report");
                                nameMap.put("LeaveBalanceReportAccess", "Leave Balance Report");

                                //nameMap.put("AttnRegReportAccess", "Attendance Regularization Report");
                               // nameMap.put("GeoFenchApprovalAccess", "Geo Fence Approval");

                                Iterator<String> keys = menuObj.keys();

                                while (keys.hasNext()) {
                                    String key = keys.next();

                                    if (menuObj.getInt(key) == 1 && nameMap.containsKey(key)) {

                                        HashMap<String, String> map = new HashMap<>();
                                        map.put("id", key);                  // API key as id
                                        map.put("name", nameMap.get(key));  // Custom display name

                                        menuList.add(map);
                                    }
                                }

                                if (pref.getEmpClintId().equals("AEMCLI1810001410")){
                                    HashMap<String, String> teamReport = new HashMap<>();
                                    teamReport.put("id", "ODOMeter");
                                    teamReport.put("name", "ODO Meter Approval");

                                    menuList.add(teamReport);
                                }




                                SupervisorMenuAdapter adapter =
                                        new SupervisorMenuAdapter(SupAttendanceActivity.this, menuList);

                                rvItem.setAdapter(adapter);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        pd.dismiss();
                        Log.e(TAG, "SUB_MENU_error: "+anError.getErrorBody());
                    }
                });
    }
}
