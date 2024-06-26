package io.cordova.myapp00d753.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;


public class SalesManagementDashboardActivity extends AppCompatActivity {
    LinearLayout llSale,llTarget,llVisit;

    ImageView imgBack,imgHome;
    String responseText,responseCode;
    AlertDialog alet1;
    boolean responseData;
    String flag;
    LinearLayout llLoader;
    LinearLayout llDW;
    AlertDialog alertDialog,alertDialog1,alertDialog2;
    TextView tvYear,tvMonth;
    String year,month;
    Pref pref;
    AlertDialog alertDialog3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesmanagement_dashboard);
        initialize();
        //checksale();
       // targetGet();
        targetGet();
        onClick();
    }

    private void initialize(){
        pref=new Pref(SalesManagementDashboardActivity.this);
        llSale=(LinearLayout)findViewById(R.id.llSale);
        llTarget=(LinearLayout)findViewById(R.id.llTarget);
        llVisit=(LinearLayout)findViewById(R.id.llVisit);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);


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





    }

    private void onClick(){
        llSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(SalesManagementDashboardActivity.this, SaleDashboardActivity.class);
                    startActivity(intent);

            }
        });





        llTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serachDialog();
            }
        });

        llVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SalesManagementDashboardActivity.this,CounterVisitDashboardActivity.class);
                startActivity(intent);
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
                Intent intent=new Intent(SalesManagementDashboardActivity.this,EmployeeDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });





    }

    private void serachDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SalesManagementDashboardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.attendancereportsearch, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llYear=(LinearLayout)dialogView.findViewById(R.id.llYear);
        tvYear=(TextView) dialogView.findViewById(R.id.tvYear);
        tvMonth=(TextView)dialogView.findViewById(R.id.tvMonth);

        TextView tvSYear=(TextView)dialogView.findViewById(R.id.tvSYear);
        TextView tvSMonth=(TextView)dialogView.findViewById(R.id.tvSMonth);
        tvSYear.setText("Select Target Year");
        tvSMonth.setText("Select Target Month");

        ImageView imgCancel=(ImageView)dialogView.findViewById(R.id.imgCancel);

        llYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showYearDialog();
            }
        });

        tvYear.setText("2020-2021");
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

                alertDialog.dismiss();
                Intent intent=new Intent(SalesManagementDashboardActivity.this,SaleTarget.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                pref.saveFinacialYear(tvYear.getText().toString());
                pref.saveMonth(tvMonth.getText().toString());



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

    private void showYearDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SalesManagementDashboardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_year, null);
        dialogBuilder.setView(dialogView);
        final TextView tvYear1=(TextView)dialogView.findViewById(R.id.tvYear1);
        final TextView tvYear2=(TextView)dialogView.findViewById(R.id.tvYear2);
        final TextView tvYear3=(TextView)dialogView.findViewById(R.id.tvYear3);
        LinearLayout llY1=(LinearLayout)dialogView.findViewById(R.id.llY1);
        LinearLayout llY2=(LinearLayout)dialogView.findViewById(R.id.llY2);
        LinearLayout llY3=(LinearLayout)dialogView.findViewById(R.id.llY3);


        tvYear1.setText("2020-2021");


        tvYear2.setText("2021-2022");


        tvYear3.setText("2022-2023");

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
                pref.saveFinacialYear(year);
                pref.saveShowFinacialYear("2020-21");


            }
        });

        llY2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year=tvYear2.getText().toString();
                alertDialog1.dismiss();
                tvYear.setText(year);
                Log.d("ttt",year);
                pref.saveFinacialYear(year);
                pref.saveShowFinacialYear("2021-22");
            }
        });

        llY1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year=tvYear1.getText().toString();
                alertDialog1.dismiss();
                tvYear.setText(year);
                Log.d("ttt",year);
                pref.saveFinacialYear(year);
                pref.saveShowFinacialYear("2022-23");
            }
        });

        alertDialog1= dialogBuilder.create();
        alertDialog1.setCancelable(true);
        Window window = alertDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog1.show();
    }

    private void showMonthDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SalesManagementDashboardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_month, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llM1 = (LinearLayout) dialogView.findViewById(R.id.llM1);
        LinearLayout llM2 = (LinearLayout) dialogView.findViewById(R.id.llM2);
        LinearLayout llM3 = (LinearLayout) dialogView.findViewById(R.id.llM3);
        LinearLayout llM4 = (LinearLayout) dialogView.findViewById(R.id.llM4);
        LinearLayout llM5 = (LinearLayout) dialogView.findViewById(R.id.llM5);
        LinearLayout llM6 = (LinearLayout) dialogView.findViewById(R.id.llM6);
        LinearLayout llM7 = (LinearLayout) dialogView.findViewById(R.id.llM7);
        LinearLayout llM8 = (LinearLayout) dialogView.findViewById(R.id.llM8);
        LinearLayout llM9 = (LinearLayout) dialogView.findViewById(R.id.llM9);
        LinearLayout llM10 = (LinearLayout) dialogView.findViewById(R.id.llM10);
        LinearLayout llM11 = (LinearLayout) dialogView.findViewById(R.id.llM111);
        LinearLayout llM112 = (LinearLayout) dialogView.findViewById(R.id.llM12);

        final TextView tvJan = (TextView) dialogView.findViewById(R.id.tvJan);
        tvJan.setText("January");
        final TextView tvFeb = (TextView) dialogView.findViewById(R.id.tvFeb);
        final TextView tvMarch = (TextView) dialogView.findViewById(R.id.tvMarch);
        final TextView tvApril = (TextView) dialogView.findViewById(R.id.tvApril);
        final TextView tvMay = (TextView) dialogView.findViewById(R.id.tvMay);
        final TextView tvJune = (TextView) dialogView.findViewById(R.id.tvJune);
        final TextView tvJuly = (TextView) dialogView.findViewById(R.id.tvJuly);
        final TextView tvAugust = (TextView) dialogView.findViewById(R.id.tvAugust);
        final TextView tvSept = (TextView) dialogView.findViewById(R.id.tvSeptember);
        final TextView tvOct = (TextView) dialogView.findViewById(R.id.tvOct);
        final TextView tvNov = (TextView) dialogView.findViewById(R.id.tvNovember);
        final TextView tvDec = (TextView) dialogView.findViewById(R.id.tvDecember);

        llM1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvJan.getText().toString();
                pref.saveMonth(month);
                alertDialog2.dismiss();
                tvMonth.setText(month);

            }
        });
        llM2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvFeb.getText().toString();
                pref.saveMonth(month);
                alertDialog2.dismiss();
                tvMonth.setText(month);

            }
        });

        llM3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvMarch.getText().toString();
                pref.saveMonth(month);
                alertDialog2.dismiss();
                tvMonth.setText(month);

            }
        });
        llM4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvApril.getText().toString();
                pref.saveMonth(month);
                alertDialog2.dismiss();
                tvMonth.setText(month);

            }
        });
        llM5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvMay.getText().toString();
                pref.saveMonth(month);
                alertDialog2.dismiss();
                tvMonth.setText(month);

            }
        });

        llM6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvJune.getText().toString();
                pref.saveMonth(month);
                alertDialog2.dismiss();
                tvMonth.setText(month);

            }
        });
        llM7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvJuly.getText().toString();
                pref.saveMonth(month);
                alertDialog2.dismiss();
                tvMonth.setText(month);

            }
        });
        llM8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvAugust.getText().toString();
                alertDialog2.dismiss();
                pref.saveMonth(month);
                tvMonth.setText(month);

            }
        });
        llM9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvSept.getText().toString();
                alertDialog2.dismiss();
                pref.saveMonth(month);
                tvMonth.setText(month);

            }
        });
        llM10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvOct.getText().toString();
                alertDialog2.dismiss();
                pref.saveMonth(month);
                tvMonth.setText(month);

            }
        });
        llM11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvNov.getText().toString();
                alertDialog2.dismiss();
                pref.saveMonth(month);
                tvMonth.setText(month);

            }
        });
        llM112.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvDec.getText().toString();
                alertDialog2.dismiss();
                pref.saveMonth(month);
                tvMonth.setText(month);

            }
        });
        ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);
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


    private void showPopupDialog(String target,String sold,String pending) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SalesManagementDashboardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_sale, null);
        dialogBuilder.setView(dialogView);
        TextView tvMonthlyTarget=(TextView)dialogView.findViewById(R.id.tvMonthlyTarget);
        TextView tvSold=(TextView)dialogView.findViewById(R.id.tvSold);
        TextView tvPending=(TextView)dialogView.findViewById(R.id.tvPending);

        tvMonthlyTarget.setText(target);
        tvSold.setText(sold);
        tvPending.setText(pending);

        Button btnExit=(Button)dialogView.findViewById(R.id.btnExit);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog3.dismiss();
            }
        });


        alertDialog3 = dialogBuilder.create();
        alertDialog3.setCancelable(true);
        Window window = alertDialog3.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog3.show();

    }

    public void targetGet() {
       final ProgressDialog pd=new ProgressDialog(SalesManagementDashboardActivity.this);
       pd.setMessage("Loading..");
       pd.setCancelable(false);
        pd.show();
        String surl = AppData.url+"get_EmployeeSalesAchvAlert?ClientID="+pref.getEmpClintId()+"&MasterID="+pref.getMasterId()+"&Operation=1&SecurityCode=0000";
        Log.d("inputCheck", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        pd.dismiss();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            boolean responseStatus=job1.optBoolean("responseStatus");
                            if (responseStatus){
                                JSONArray job=job1.optJSONArray("responseData");
                                JSONObject obj=job.optJSONObject(0);
                                String Caption=obj.optString("Caption");


                                String Zone=obj.optString("Zone");
                                pref.saveZoneNameForEmp(Zone);
                                String Branch=obj.optString("Branch");
                                pref.saveBranchNameForEmp(Branch);

                                // targetGet1();


                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SalesManagementDashboardActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(SalesManagementDashboardActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(SalesManagementDashboardActivity.this);
        requestQueue.add(stringRequest);

    }






}
