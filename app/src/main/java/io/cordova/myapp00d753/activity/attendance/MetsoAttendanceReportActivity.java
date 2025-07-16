package io.cordova.myapp00d753.activity.attendance;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.TextView;

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
import io.cordova.myapp00d753.activity.EmployeeDashBoardActivity;
import io.cordova.myapp00d753.activity.metso.adapter.MetsoAttendanceReportAdapter;

import io.cordova.myapp00d753.module.AttendanceModule;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

public class MetsoAttendanceReportActivity extends AppCompatActivity {
    private static final String TAG = "MetsoAttendanceReportAc";

    Pref pref;
    String sYear,month;
    LinearLayout llLoder,llMain,llNodata,llAgain,llSearch;
    ImageView imgBack,imgHome,imgAgain,imgSearch;
    ProgressBar progressBar;
    ArrayList<AttendanceModule> attendabceInfiList;
    MetsoAttendanceReportAdapter metsoAttendanceReportAdapter;
    RecyclerView rvAttendanceReport;
    TextView tvYear,tvMonth;
    private AlertDialog alertDialog,alertDialog1,alertDialog2;
    String year;
    public static int mPageCount = 0;
    int y;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metso_attendance_report);
        initView();
        getMetsoAttandanceReport();
    }

    private void initView() {
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        imgSearch=(ImageView)findViewById(R.id.imgSearch);
        llSearch=(LinearLayout)findViewById(R.id.llSearch);
        llLoder=(LinearLayout)findViewById(R.id.llWLLoader) ;
        llMain=(LinearLayout)findViewById(R.id.llMain);
        llNodata=(LinearLayout)findViewById(R.id.llNodata);
        llAgain=(LinearLayout)findViewById(R.id.llAgain);
        imgAgain= findViewById(R.id.imgAgain);
        progressBar=(ProgressBar)findViewById(R.id.WLpagination_loader);
        rvAttendanceReport = findViewById(R.id.rvAttendanceReport);
        rvAttendanceReport.setLayoutManager(new LinearLayoutManager(MetsoAttendanceReportActivity.this));

        pref = new Pref(MetsoAttendanceReportActivity.this);
        y = Calendar.getInstance().get(Calendar.YEAR);
        sYear=String.valueOf(y);
        int m=Calendar.getInstance().get(Calendar.MONTH)+1;
        if (m==1){
            month="January";
        }else if (m==2){
            month="February";
        }else if (m==3){
            month="March";
        } else if (m==4){
            month="April";
        }else if (m==5){
            month="May";
        }else if (m==6){
            month="June";
        }else if (m==7){
            month="July";
        } else if (m==8){
            month="August";
        }else if (m==9){
            month="September";
        }else if (m==10){
            month="October";
        }else if (m==11){
            month="November";
        } else if (m==12){
            month="December";
        }

        imgAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMetsoAttandanceReport();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MetsoAttendanceReportActivity.this, EmployeeDashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMonthListPopup();
            }
        });
    }

    private void openMonthListPopup() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MetsoAttendanceReportActivity.this, R.style.CustomDialogNew);
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

        tvYear.setText(sYear);
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
                attendabceInfiList.clear();
                getMetsoAttandanceReport();
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

    private void getMetsoAttandanceReport() {
        /*String surl = AppData.url+"get_GCLSelfAttendanceWoLeave?AEMConsultantID="+pref.getEmpConId()+"&AEMClientID="+pref.getEmpClintId()+"&AEMClientOfficeID="+pref.getEmpClintOffId()+"&AEMEmployeeID="+pref.getEmpId()+"&CurrentPage=0&AID=0&ApproverStatus=4&YearVal="+year+"&MonthName="+month+"&WorkingStatus=1&SecurityCode="+pref.getSecurityCode()+"&DbOperation=8&AttIds=null";

        Log.d("input",surl);*/
        llLoder.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);
        llAgain.setVisibility(View.GONE);
        Log.e(TAG, "getMetsoAttandanceReport: AEMConsultantID: "+pref.getEmpConId()
                +"\nAEMClientID: "+pref.getEmpClintId()
                +"\nAEMClientOfficeID: "+pref.getEmpClintOffId()
                +"\nAEMEmployeeID: "+pref.getEmpId()
                +"\nYearVal: "+sYear
                +"\nMonthName: "+month
                +"\nSecurityCode:"+pref.getSecurityCode()
                +"\n");

        /*AndroidNetworking.get(AppData.url+"get_GCLSelfAttendanceWoLeave")
                .addQueryParameter("AEMEmployeeID",pref.getEmpConId())
                .addQueryParameter("AEMClientID",pref.getEmpClintId())
                .addQueryParameter("AEMClientOfficeID",pref.getEmpClintOffId())
                .addQueryParameter("AEMEmployeeID",pref.getEmpId())
                .addQueryParameter("CurrentPage","0")
                .addQueryParameter("AID","0")
                .addQueryParameter("ApproverStatus","4")
                .addQueryParameter("YearVal",sYear)
                .addQueryParameter("MonthName",month)
                .addQueryParameter("WorkingStatus","1")
                .addQueryParameter("SecurityCode","0000")
                .addQueryParameter("DbOperation","8")
                .addQueryParameter("AttIds","null")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "METSO_ATTENDANCE_REPORT: "+new Gson().toJson(response));
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e(TAG, "METSO_ATTENDANCE_REPORT_onError: "+error);
                    }
                });*/


        String surl = AppData.url+"get_GCLSelfAttendanceWoLeave?AEMConsultantID="+pref.getEmpConId()+"&AEMClientID="+pref.getEmpClintId()+"&AEMClientOfficeID="+pref.getEmpClintOffId()+"&AEMEmployeeID="+pref.getEmpId()+"&CurrentPage=0&AID=0&ApproverStatus=4&YearVal="+sYear+"&MonthName="+month+"&WorkingStatus=1&SecurityCode="+pref.getSecurityCode()+"&DbOperation=8&AttIds=null";
        Log.d("input",surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);

                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText=job1.optString("responseText");

                            boolean responseStatus=job1.optBoolean("responseStatus");
                            if (responseStatus){
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData=job1.optJSONArray("responseData");
                                attendabceInfiList = new ArrayList<>();
                                for (int i = 0; i < responseData.length(); i++){
                                    JSONObject obj=responseData.getJSONObject(i);
                                    String AttendanceDate=obj.optString("AttendanceDate");
                                    String AttendanceInDateTime=obj.optString("AttendanceInTime");
                                    String AttendanceOutDateTime=obj.optString("AttendanceOutTime");
                                    String Address=obj.optString("Address");
                                    //AttendanceID=obj.optString("AttendanceID");
                                    String ApproverStatus=obj.optString("ApproverStatus");
                                    String AttendanceType=obj.optString("AttendanceType");
                                    String shift=obj.optString("Longitude");
                                    String location=obj.optString("Latitude");
                                    AttendanceModule obj2 = new AttendanceModule(AttendanceDate,AttendanceInDateTime,Address,AttendanceOutDateTime,ApproverStatus,AttendanceType,shift,location);
                                    attendabceInfiList.add(obj2);
                                }
                                //attendanceAdapter.notifyDataSetChanged();
                                metsoAttendanceReportAdapter = new MetsoAttendanceReportAdapter(MetsoAttendanceReportActivity.this,attendabceInfiList);
                                rvAttendanceReport.setAdapter(metsoAttendanceReportAdapter);
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
                                //Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
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
    }

    private void showYearDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MetsoAttendanceReportActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_year, null);
        dialogBuilder.setView(dialogView);
        final TextView tvYear1=(TextView)dialogView.findViewById(R.id.tvYear1);
        final TextView tvYear2=(TextView)dialogView.findViewById(R.id.tvYear2);
        final TextView tvYear3=(TextView)dialogView.findViewById(R.id.tvYear3);
        LinearLayout llY1=(LinearLayout)dialogView.findViewById(R.id.llY1);
        LinearLayout llY2=(LinearLayout)dialogView.findViewById(R.id.llY2);
        LinearLayout llY3=(LinearLayout)dialogView.findViewById(R.id.llY3);

        int pastx1=y-2;
        String pasty1=String.valueOf(pastx1);
        tvYear1.setText(pasty1);

        int pastx2=y-1;
        String pasty2=String.valueOf(pastx2);
        tvYear2.setText(pasty2);

        String pastx3=String.valueOf(y);
        tvYear3.setText(pastx3);

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
                //year=tvYear3.getText().toString();
                sYear=tvYear3.getText().toString();
                Log.d("yrtrr",sYear);
                tvYear.setText(sYear);
                alertDialog1.dismiss();

            }
        });

        llY2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //year=tvYear2.getText().toString();
                sYear=tvYear2.getText().toString();
                alertDialog1.dismiss();
                tvYear.setText(sYear);
                Log.d("ttt",sYear);
            }
        });

        llY1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //year=tvYear1.getText().toString();
                sYear=tvYear1.getText().toString();
                alertDialog1.dismiss();
                tvYear.setText(sYear);
                Log.d("ttt",sYear);
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MetsoAttendanceReportActivity.this, R.style.CustomDialogNew);
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
            }
        });
        llM2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month=tvFeb.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });

        llM3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month=tvMarch.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month=tvApril.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month=tvMay.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });

        llM6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month=tvJune.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month=tvJuly.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month=tvAugust.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month=tvSept.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month=tvOct.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month=tvNov.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM112.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month=tvDec.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
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