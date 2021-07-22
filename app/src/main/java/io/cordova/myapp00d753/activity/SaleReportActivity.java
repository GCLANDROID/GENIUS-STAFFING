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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.fragment.DailySaleFragment;
import io.cordova.myapp00d753.fragment.MTDSaleFragment;
import io.cordova.myapp00d753.fragment.QTDSaleFragment;
import io.cordova.myapp00d753.fragment.WTDSaleFragment;
import io.cordova.myapp00d753.fragment.YTDSaleFragment;
import io.cordova.myapp00d753.utility.Pref;


public class SaleReportActivity extends AppCompatActivity {
    LinearLayout llYTD,llQTD,llMTD,llDailySale,llWTD;
    ImageView imgBack,imgHome,imgSearch;

    Pref pref;
    String month;
    AlertDialog alertDialog2;
    LinearLayout llDailySalesD,llYTDD,llQTDD,llMTDD,llWTDD;
    TextView tvEmpName;
    TextView tvToolBar,tvToolBar1;

    AlertDialog alertDialog,alertDialog1;
    TextView tvYear,tvMonth;
    String year;
    int y;
    TextView tvLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_report);
        initView();
        loadDailySaleFragment();
        onClick();
    }


    private void initView(){
        pref=new Pref(getApplicationContext());

        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        imgSearch=(ImageView)findViewById(R.id.imgSearch);

        llYTD=(LinearLayout)findViewById(R.id.llYTD);
        llQTD=(LinearLayout)findViewById(R.id.llQTD);
        llMTD=(LinearLayout)findViewById(R.id.llMTD);
        llWTD=(LinearLayout)findViewById(R.id.llWTD);
        llDailySale=(LinearLayout)findViewById(R.id.llDailySale);

        llDailySalesD=(LinearLayout)findViewById(R.id.llDailySalesD);
        llYTDD=(LinearLayout)findViewById(R.id.llYTDD);
        llQTDD=(LinearLayout)findViewById(R.id.llQTDD);
        llMTDD=(LinearLayout)findViewById(R.id.llMTDD);
        llWTDD=(LinearLayout)findViewById(R.id.llWTDD);

        int m = Calendar.getInstance().get(Calendar.MONTH) + 1;
        Log.d("month", String.valueOf(m));

        tvEmpName=(TextView)findViewById(R.id.tvEmpName);
        tvEmpName.setText(pref.getEmpName()+" ( "+pref.getEmpId()+" )");

        tvToolBar=(TextView)findViewById(R.id.tvToolBar);
        tvToolBar1=(TextView)findViewById(R.id.tvToolBar1);


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
        tvLocation=(TextView)findViewById(R.id.tvLocation);
        tvLocation.setText(pref.getBranchNameForEmp()+" - "+pref.getZoneNameForEmp());
        pref.saveXYZ("1");





    }
    private void onClick(){

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), EmployeeDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        llYTD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadYTDFragment();
            }
        });

        llQTD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadQTEDFragment();
            }
        });

        llMTD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMTDFragment();
            }
        });
       llDailySale.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               loadDailySaleFragment();
           }
       });

       imgSearch.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               serachDialog();

           }
       });

       llWTD.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               loadWTDFragment();
           }
       });

    }


    public void loadYTDFragment() {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        YTDSaleFragment pfragment=new YTDSaleFragment();
        transaction.replace(R.id.frameLayout, pfragment);
        transaction.commit();

        llYTDD.setVisibility(View.VISIBLE);
        llQTDD.setVisibility(View.GONE);
        llMTDD.setVisibility(View.GONE);
        llDailySalesD.setVisibility(View.GONE);
        llWTDD.setVisibility(View.GONE);

        tvToolBar.setText("Yearly SPR");
        tvToolBar1.setVisibility(View.VISIBLE);
        tvToolBar1.setText(pref.getShowFinacialYear());




    }

    public void loadQTEDFragment() {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        QTDSaleFragment efr=new QTDSaleFragment();
        transaction.replace(R.id.frameLayout, efr);
        transaction.commit();

        llYTDD.setVisibility(View.GONE);
        llQTDD.setVisibility(View.VISIBLE);
        llMTDD.setVisibility(View.GONE);
        llDailySalesD.setVisibility(View.GONE);
        llWTDD.setVisibility(View.GONE);

        tvToolBar.setText("Quarterly  SPR");
        tvToolBar1.setVisibility(View.VISIBLE);
        tvToolBar1.setText(pref.getShowFinacialYear());

        //tvHeader.setText("Personal");


    }


    public void loadMTDFragment() {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        MTDSaleFragment htfragment=new MTDSaleFragment();
        transaction.replace(R.id.frameLayout, htfragment);
        transaction.commit();

        llYTDD.setVisibility(View.GONE);
        llQTDD.setVisibility(View.GONE);
        llMTDD.setVisibility(View.VISIBLE);
        llDailySalesD.setVisibility(View.GONE);
        llWTDD.setVisibility(View.GONE);

        tvToolBar.setText("Monthly SPR");
        tvToolBar1.setVisibility(View.VISIBLE);
        tvToolBar1.setText(pref.getMonth()+" "+pref.getShowFinacialYear());

        //tvHeader.setText("Personal");


    }

    public void loadDailySaleFragment() {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        DailySaleFragment htfragment=new DailySaleFragment();
        transaction.replace(R.id.frameLayout, htfragment);
        transaction.commit();


        llYTDD.setVisibility(View.GONE);
        llQTDD.setVisibility(View.GONE);
        llMTDD.setVisibility(View.GONE);
        llDailySalesD.setVisibility(View.VISIBLE);
        llWTDD.setVisibility(View.GONE);

        tvToolBar.setText("Daily Sales Report");
        tvToolBar1.setVisibility(View.VISIBLE);
        tvToolBar1.setVisibility(View.VISIBLE);
        tvToolBar1.setText(pref.getMonth()+" "+pref.getShowFinacialYear());

        //tvHeader.setText("Personal");


    }


    public void loadWTDFragment() {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        WTDSaleFragment htfragment=new WTDSaleFragment();
        transaction.replace(R.id.frameLayout, htfragment);
        transaction.commit();


        llYTDD.setVisibility(View.GONE);
        llQTDD.setVisibility(View.GONE);
        llMTDD.setVisibility(View.GONE);
        llDailySalesD.setVisibility(View.GONE);
        llWTDD.setVisibility(View.VISIBLE);

        tvToolBar.setText("Weekly SPR");
        tvToolBar1.setVisibility(View.VISIBLE);
        tvToolBar1.setVisibility(View.VISIBLE);
        tvToolBar1.setText(pref.getMonth()+" "+pref.getShowFinacialYear());

        //tvHeader.setText("Personal");


    }

    private void serachDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SaleReportActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.attendancereportsearch, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llYear=(LinearLayout)dialogView.findViewById(R.id.llYear);
        tvYear=(TextView) dialogView.findViewById(R.id.tvYear);
        tvMonth=(TextView)dialogView.findViewById(R.id.tvMonth);

        TextView tvSYear=(TextView)dialogView.findViewById(R.id.tvSYear);
        TextView tvSMonth=(TextView)dialogView.findViewById(R.id.tvSMonth);
        tvSYear.setText("Select Sales Year");
        tvSMonth.setText("Select Sales Month");

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
               loadDailySaleFragment();
                pref.saveShowFinacialYear("2020-21");
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SaleReportActivity.this, R.style.CustomDialogNew);
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
                pref.saveShowFinacialYear("2022-23");


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
                pref.saveShowFinacialYear("2020-21");
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SaleReportActivity.this, R.style.CustomDialogNew);
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



}
