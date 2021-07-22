package io.cordova.myapp00d753.activity;

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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.ClaimReportAdapter;
import io.cordova.myapp00d753.module.AttendanceModule;
import io.cordova.myapp00d753.module.ClaimModule;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

public class ClaimReportActivity extends AppCompatActivity {
    RecyclerView rvItem;
    ArrayList<ClaimModule> itemList = new ArrayList<>();
    LinearLayout llLoder,llMain,llNodata,llAgain,llSearch;
    String year;
    int y;
    TextView tvYear;
    String month;
    TextView tvMonth;
    AlertDialog alertDialog,alertDialog1,alertDialog2;
    Pref pref;
    ImageView imgBack;
    ImageView imgSearch,imgHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_report);
        initialize();
        getItemList();
        onClick();

    }

    private void initialize() {
        pref=new Pref(getApplicationContext());
        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(ClaimReportActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);
        llLoder=(LinearLayout)findViewById(R.id.llLoader);
        llMain=(LinearLayout)findViewById(R.id.llMain);
        llNodata=(LinearLayout)findViewById(R.id.llNodata);
        llAgain=(LinearLayout)findViewById(R.id.llAgain);
        llSearch=(LinearLayout)findViewById(R.id.llSearch);
        y= Calendar.getInstance().get(Calendar.YEAR);
        year=String.valueOf(y);
        Log.d("year", year);

        int m=Calendar.getInstance().get(Calendar.MONTH)+1;
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
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView) findViewById(R.id.imgHome);
        imgSearch=(ImageView)findViewById(R.id.imgSearch);

    }

    private void getItemList() {
        llLoder.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);
        llAgain.setVisibility(View.GONE);
        String surl = AppData.url+"gcl_ReimbursementClaim?AEMEmployeeID="+pref.getEmpId()+"&Year="+year+"&Month="+month+"&SecurityCode="+pref.getSecurityCode();
        Log.d("inputclaimreport",surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseclaimreport", response);

                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText=job1.optString("responseText");

                            boolean responseStatus=job1.optBoolean("responseStatus");
                            if (responseStatus){
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData=job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++){
                                    JSONObject job=responseData.getJSONObject(i);
                                    String ClaimedDate=job.optString("ClaimedDate");
                                    String Amount=job.optString("ActualClaimAmount");
                                    String ApprovedClaimAmount=job.optString("ApprovedClaimAmount");
                                    String ClaimID=job.optString("ClaimID");
                                    String ProcessStaus=job.optString("ProcessStaus");
                                    String Category=job.optString("CategoryComponent");
                                    ClaimModule obj2 = new ClaimModule(ClaimedDate,Amount,Category,ProcessStaus,ClaimID,ApprovedClaimAmount);
                                    itemList.add(obj2);


                                }

                                llLoder.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.GONE);
                                llAgain.setVisibility(View.GONE);
                                setAdapter();

                            }

                            else {
                                llLoder.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNodata.setVisibility(View.VISIBLE);
                                llAgain.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();

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

    private void setAdapter() {
        ClaimReportAdapter claimAdapter = new ClaimReportAdapter(itemList);
        rvItem.setAdapter(claimAdapter);
    }

    private void onClick(){
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ClaimReportActivity.this, R.style.CustomDialogNew);
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

                        itemList.clear();
                        getItemList();
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

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ClaimReportActivity.this,EmployeeDashBoardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void showYearDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ClaimReportActivity.this, R.style.CustomDialogNew);
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ClaimReportActivity.this, R.style.CustomDialogNew);
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
