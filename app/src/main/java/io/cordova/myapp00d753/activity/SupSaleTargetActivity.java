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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.fragment.SupDailySaleFragment;
import io.cordova.myapp00d753.fragment.SupMTDSaleFragment;
import io.cordova.myapp00d753.fragment.SupQTDSaleFragment;
import io.cordova.myapp00d753.fragment.SupWTDSaleFragment;
import io.cordova.myapp00d753.fragment.SupYTDSaleFragment;
import io.cordova.myapp00d753.utility.Pref;


public class SupSaleTargetActivity extends AppCompatActivity {
    LinearLayout llYTD,llQTD,llMTD,llDailySale,llWTD,llWTDD;
    ImageView imgBack,imgHome,imgSearch;

    Pref pref;
    String empId;
    TextView tvEmpName;
    String empName;
    String month;
    AlertDialog alertDialog2;

    LinearLayout llDailySalesD,llYTDD,llQTDD,llMTDD;
    TextView tvToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_target);
        initView();
        loadYTDFragment();
        onClick();
    }


    private void initView(){
        pref=new Pref(getApplicationContext());
        empId=getIntent().getStringExtra("empId");
        pref.saveID(empId);

        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        imgSearch=(ImageView)findViewById(R.id.imgSearch);

        llYTD=(LinearLayout)findViewById(R.id.llYTD);
        llQTD=(LinearLayout)findViewById(R.id.llQTD);
        llMTD=(LinearLayout)findViewById(R.id.llMTD);
        llWTD=(LinearLayout)findViewById(R.id.llWTD);
        llWTDD=(LinearLayout)findViewById(R.id.llWTDD);
        llDailySale=(LinearLayout)findViewById(R.id.llDailySale);

        tvEmpName=(TextView)findViewById(R.id.tvEmpName);
        empName=getIntent().getStringExtra("empName");
        tvEmpName=(TextView)findViewById(R.id.tvEmpName);
        tvEmpName.setText(empName+"-"+pref.getFinacialYear());

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


        llYTDD=(LinearLayout)findViewById(R.id.llYTDD);
        llQTDD=(LinearLayout)findViewById(R.id.llQTDD);
        llMTDD=(LinearLayout)findViewById(R.id.llMTDD);

        tvToolBar=(TextView)findViewById(R.id.tvToolBar);




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
               showMonthDialog();
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
        SupYTDSaleFragment pfragment=new SupYTDSaleFragment();
        transaction.replace(R.id.frameLayout, pfragment);
        transaction.commit();

        llYTDD.setVisibility(View.VISIBLE);
        llQTDD.setVisibility(View.GONE);
        llMTDD.setVisibility(View.GONE);
        llWTDD.setVisibility(View.GONE);

        tvToolBar.setText("YTD Target Report");




    }

    public void loadQTEDFragment() {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SupQTDSaleFragment efr=new SupQTDSaleFragment();
        transaction.replace(R.id.frameLayout, efr);
        transaction.commit();

        llYTDD.setVisibility(View.GONE);
        llQTDD.setVisibility(View.VISIBLE);
        llMTDD.setVisibility(View.GONE);
        llWTDD.setVisibility(View.GONE);

        tvToolBar.setText("QTD Target Report");

        //tvHeader.setText("Personal");


    }


    public void loadMTDFragment() {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SupMTDSaleFragment htfragment=new SupMTDSaleFragment();
        transaction.replace(R.id.frameLayout, htfragment);
        transaction.commit();

        llYTDD.setVisibility(View.GONE);
        llQTDD.setVisibility(View.GONE);
        llMTDD.setVisibility(View.VISIBLE);
        llWTDD.setVisibility(View.GONE);

        tvToolBar.setText("MTD Target Report");

        //tvHeader.setText("Personal");


    }


    public void loadWTDFragment() {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SupWTDSaleFragment htfragment=new SupWTDSaleFragment();
        transaction.replace(R.id.frameLayout, htfragment);
        transaction.commit();

        llYTDD.setVisibility(View.GONE);
        llQTDD.setVisibility(View.GONE);
        llMTDD.setVisibility(View.GONE);
        llWTDD.setVisibility(View.VISIBLE);

        tvToolBar.setText("WTD Target Report");

        //tvHeader.setText("Personal");


    }

    public void loadDailySaleFragment() {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SupDailySaleFragment htfragment=new SupDailySaleFragment();
        transaction.replace(R.id.frameLayout, htfragment);
        transaction.commit();

        //tvHeader.setText("Personal");


    }

    private void showMonthDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SupSaleTargetActivity.this, R.style.CustomDialogNew);
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
                loadYTDFragment();
            }
        });
        llM2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvFeb.getText().toString();
                pref.saveMonth(month);
                alertDialog2.dismiss();
                loadYTDFragment();
            }
        });

        llM3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvMarch.getText().toString();
                pref.saveMonth(month);
                alertDialog2.dismiss();
                loadYTDFragment();
            }
        });
        llM4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvApril.getText().toString();
                pref.saveMonth(month);
                alertDialog2.dismiss();
                loadYTDFragment();
            }
        });
        llM5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvMay.getText().toString();
                pref.saveMonth(month);
                alertDialog2.dismiss();
                loadYTDFragment();
            }
        });

        llM6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvJune.getText().toString();
                pref.saveMonth(month);
                alertDialog2.dismiss();
                loadYTDFragment();
            }
        });
        llM7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvJuly.getText().toString();
                pref.saveMonth(month);
                alertDialog2.dismiss();
                loadYTDFragment();
            }
        });
        llM8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvAugust.getText().toString();
                alertDialog2.dismiss();
                pref.saveMonth(month);
                loadYTDFragment();
            }
        });
        llM9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvSept.getText().toString();
                alertDialog2.dismiss();
                pref.saveMonth(month);
                loadYTDFragment();
            }
        });
        llM10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvOct.getText().toString();
                alertDialog2.dismiss();
                pref.saveMonth(month);
                loadYTDFragment();
            }
        });
        llM11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvNov.getText().toString();
                alertDialog2.dismiss();
                pref.saveMonth(month);
                loadYTDFragment();
            }
        });
        llM112.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvDec.getText().toString();
                alertDialog2.dismiss();
                pref.saveMonth(month);
                loadYTDFragment();
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
