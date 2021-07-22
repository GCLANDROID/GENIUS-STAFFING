
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
import io.cordova.myapp00d753.fragment.GraphicalMTDFragment;
import io.cordova.myapp00d753.fragment.NormalMTDFragment;
import io.cordova.myapp00d753.fragment.SaleApprovalFragment;
import io.cordova.myapp00d753.fragment.SaleRejectFragment;
import io.cordova.myapp00d753.utility.Pref;

public class SaleArpprovalReportActivity extends AppCompatActivity {
    LinearLayout llApprove,llReject,llApproveD,llRejectD;
    ImageView imgBack,imgHome;
    String zoneId,branchId,zoneName,xy;
    Pref pref;
    TextView tvToolBar;
    TextView tvToolBar1;
    ImageView imgSearch;
    String finacialyear="2020-2021";
    AlertDialog alertDialog1;
    AlertDialog alertDialog;
    String month;
    int m;
    TextView tvYear,tvMonth;
    AlertDialog alertDialog2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_arpproval_report);
        initView();

        onClick();
    }

    private void initView(){
        pref=new Pref(SaleArpprovalReportActivity.this);
        llApprove=(LinearLayout)findViewById(R.id.llApprove);
        llReject=(LinearLayout)findViewById(R.id.llReject);

        llApproveD=(LinearLayout)findViewById(R.id.llApproveD);
        llRejectD=(LinearLayout)findViewById(R.id.llRejectD);

        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        zoneId=getIntent().getStringExtra("zoneId");
        pref.saveZoneId(zoneId);
        branchId=getIntent().getStringExtra("branchId");
        pref.saveBranchId(branchId);
        zoneName=getIntent().getStringExtra("zoneName");
        xy=getIntent().getStringExtra("xy");
        tvToolBar1=(TextView)findViewById(R.id.tvToolBar1);
        if (xy.equals("1")){
            loadApprovalFragment();
        }else if (xy.equals("2")){
            loadRejectFragment();
        }else {
            loadApprovalFragment();
        }

        tvToolBar=(TextView)findViewById(R.id.tvToolBar);
        tvToolBar.setText(zoneName);
        imgSearch=(ImageView)findViewById(R.id.imgSearch);
        m = Calendar.getInstance().get(Calendar.MONTH) + 1;

    }

    public void loadApprovalFragment() {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SaleApprovalFragment pfragment=new SaleApprovalFragment();
        transaction.replace(R.id.frameLayout, pfragment);
        transaction.commit();

        llApproveD.setVisibility(View.VISIBLE);
        llRejectD.setVisibility(View.GONE);
        tvToolBar1.setText("Approved Report");




    }


    public void loadRejectFragment() {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SaleRejectFragment pfragment=new SaleRejectFragment();
        transaction.replace(R.id.frameLayout, pfragment);
        transaction.commit();

        llApproveD.setVisibility(View.GONE);
        llRejectD.setVisibility(View.VISIBLE);
        tvToolBar1.setText("Rejected Report");




    }

    private void onClick(){
        llApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadApprovalFragment();
            }
        });
        llReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadRejectFragment();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SaleArpprovalReportActivity.this,SuperVisiorDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                finacialyear="2020-2021";
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SaleArpprovalReportActivity.this, R.style.CustomDialogNew);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.attendancereportsearch, null);
                dialogBuilder.setView(dialogView);
                LinearLayout llYear = (LinearLayout) dialogView.findViewById(R.id.llYear);
                tvYear = (TextView) dialogView.findViewById(R.id.tvYear);
                tvMonth = (TextView) dialogView.findViewById(R.id.tvMonth);
                ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);

                llYear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showYearDialog();
                    }
                });

                tvYear.setText(finacialyear);
                LinearLayout llMonth = (LinearLayout) dialogView.findViewById(R.id.llMonth);
                llMonth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showMonthDialog();

                    }
                });
                tvMonth.setText(month);

                Button btnSubmit = (Button) dialogView.findViewById(R.id.btnSubmit);
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       loadApprovalFragment();
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

    private void showYearDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SaleArpprovalReportActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_year, null);
        dialogBuilder.setView(dialogView);
        final TextView tvYear1 = (TextView) dialogView.findViewById(R.id.tvYear1);
        final TextView tvYear2 = (TextView) dialogView.findViewById(R.id.tvYear2);
        final TextView tvYear3 = (TextView) dialogView.findViewById(R.id.tvYear3);
        LinearLayout llY1 = (LinearLayout) dialogView.findViewById(R.id.llY1);
        LinearLayout llY2 = (LinearLayout) dialogView.findViewById(R.id.llY2);
        LinearLayout llY3 = (LinearLayout) dialogView.findViewById(R.id.llY3);


        tvYear1.setText("2020-2021");


        tvYear2.setText("2021-2022");


        tvYear3.setText("2022-2023");

        ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog1.dismiss();


            }
        });


        llY3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finacialyear = tvYear3.getText().toString();
                Log.d("yrtrr", finacialyear);
                tvYear.setText(finacialyear);
                alertDialog1.dismiss();

            }
        });

        llY2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finacialyear = tvYear2.getText().toString();
                alertDialog1.dismiss();
                tvYear.setText(finacialyear);
                Log.d("ttt", finacialyear);
            }
        });

        llY1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finacialyear = tvYear1.getText().toString();
                alertDialog1.dismiss();
                tvYear.setText(finacialyear);
                Log.d("ttt", finacialyear);
            }
        });

        alertDialog1 = dialogBuilder.create();
        alertDialog1.setCancelable(true);
        Window window = alertDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog1.show();
    }

    private void showMonthDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SaleArpprovalReportActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                Log.d("monnn", month);
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvFeb.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });

        llM3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvMarch.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvApril.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvMay.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });

        llM6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvJune.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvJuly.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvAugust.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvSept.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvOct.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvNov.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM112.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvDec.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
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