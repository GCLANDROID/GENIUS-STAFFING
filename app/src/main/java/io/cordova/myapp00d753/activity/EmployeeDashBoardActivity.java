package io.cordova.myapp00d753.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;



import java.io.File;
import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;
import io.cordova.myapp00d753.utility.Pref;

public class EmployeeDashBoardActivity extends AppCompatActivity {
    LinearLayout llProfile, llAttandence, llPayroll, llPf, llFeedBack, llNotification, llSales, llLeaveApplication, llDocument, llLogout;
    Pref pref;
    TextView tvEmployeeName, tvGreeting, tvLoginDateTime;
    NetworkConnectionCheck connectionCheck;
    String s1,s2,s3,s4,s5,s6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_dash_board);
        initialize();
        onClick();
    }

    private void initialize() {
        pref = new Pref(EmployeeDashBoardActivity.this);
        connectionCheck=new NetworkConnectionCheck(EmployeeDashBoardActivity.this);
        llProfile = (LinearLayout) findViewById(R.id.llProfile);
        llAttandence = (LinearLayout) findViewById(R.id.llAttendance);
        llPayroll = (LinearLayout) findViewById(R.id.llPayroll);
        llPf = (LinearLayout) findViewById(R.id.llPf);
        llFeedBack = (LinearLayout) findViewById(R.id.llFeedBack);
        llNotification = (LinearLayout) findViewById(R.id.llNotification);
        llSales = (LinearLayout) findViewById(R.id.llSales);
        llLeaveApplication = (LinearLayout) findViewById(R.id.llleaveApplication);
        llDocument = (LinearLayout) findViewById(R.id.llDocument);
        llLogout=(LinearLayout)findViewById(R.id.llLogout);



        tvEmployeeName = (TextView) findViewById(R.id.tvEmployeeName);
        tvEmployeeName.setText(pref.getEmpName());

        tvGreeting = (TextView) findViewById(R.id.tvGreeting);
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            tvGreeting.setText("Good Morning");
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            tvGreeting.setText("Good Afternoon");
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            tvGreeting.setText("Good Evening");
        } else if (timeOfDay >= 21 && timeOfDay < 24) {

            tvGreeting.setText("Good Night");
        }
        tvLoginDateTime = (TextView) findViewById(R.id.tvLoginDateTime);
        tvLoginDateTime.setText(pref.getloginTime());
        String menu = pref.getMenu();
        String d=menu.replace("{","").replace("}","");
        Log.d("split",d);
        Log.d("menuu", menu);
        String[] separated = menu.split(",");
        /*String n = separated[0];
        String o = separated[1];
        String p = separated[2];
        Log.d("split", p);
        if (n.equals("1")) {
            llProfile.setVisibility(View.GONE);
        } else if (n.equals("2")) {
            llAttandence.setVisibility(View.GONE);
        } else if (n.equals("3")) {
            llSales.setVisibility(View.GONE);
        } else if (n.equals("4")) {
            llPayroll.setVisibility(View.GONE);
        } else if (n.equals("5")) {
            llPf.setVisibility(View.GONE);
        } else if (n.equals("6")) {
            llFeedBack.setVisibility(View.GONE);
        } else if (n.equals("7")) {
            llNotification.setVisibility(View.GONE);
        } else if (n.equals("8")) {
            llLeaveApplication.setVisibility(View.GONE);
        } else if (n.equals("9")) {
            llDocument.setVisibility(View.GONE);
        }


        if (o.equals("1")) {
            llProfile.setVisibility(View.GONE);
        } else if (o.equals("2")) {
            llAttandence.setVisibility(View.GONE);
        } else if (o.equals("3")) {
            llSales.setVisibility(View.GONE);
        } else if (o.equals("4")) {
            llPayroll.setVisibility(View.GONE);
        } else if (o.equals("5")) {
            llPf.setVisibility(View.GONE);
        } else if (o.equals("6")) {
            llFeedBack.setVisibility(View.GONE);
        } else if (o.equals("7")) {
            llNotification.setVisibility(View.GONE);
        } else if (o.equals("8")) {
            llLeaveApplication.setVisibility(View.GONE);
        } else if (o.equals("9")) {
            llDocument.setVisibility(View.GONE);
        }

        if (p.equals("1")) {
            llProfile.setVisibility(View.GONE);
        } else if (p.equals("2")) {
            llAttandence.setVisibility(View.GONE);
        } else if (p.equals("3")) {
            llSales.setVisibility(View.GONE);
        } else if (p.equals("4")) {
            llPayroll.setVisibility(View.GONE);
        } else if (p.equals("5")) {
            llPf.setVisibility(View.GONE);
        } else if (p.equals("6")) {
            llFeedBack.setVisibility(View.GONE);
        } else if (p.equals("7")) {
            llNotification.setVisibility(View.GONE);
        } else if (p.equals("8")) {
            llLeaveApplication.setVisibility(View.GONE);
        } else if (p.equals("9")) {
            llDocument.setVisibility(View.GONE);
        }*/
        if (separated.length==2){
            Log.d("arpan","riku");
            s1 = separated[0];
            s2= separated[1];
            if (s1.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s1.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s1.equals("3")) {
                llSales.setVisibility(View.GONE);
            } else if (s1.equals("4")) {
                llPayroll.setVisibility(View.GONE);
            } else if (s1.equals("5")) {
                llPf.setVisibility(View.GONE);
            } else if (s1.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s1.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s1.equals("8")) {
                llLeaveApplication.setVisibility(View.GONE);
            } else if (s1.equals("10")) {
                llDocument.setVisibility(View.GONE);
            }


            if (s2.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s2.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s2.equals("3")) {
                llSales.setVisibility(View.GONE);
            } else if (s2.equals("4")) {
                llPayroll.setVisibility(View.GONE);
            } else if (s2.equals("5")) {
                llPf.setVisibility(View.GONE);
            } else if (s2.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s2.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s2.equals("8")) {
                llLeaveApplication.setVisibility(View.GONE);
            } else if (s2.equals("10")) {
                llDocument.setVisibility(View.GONE);
            }


        } else if (separated.length==3) {
            Log.d("arpan","riku1");
            s1 = separated[0];
            s2= separated[1];
            s3=separated[2];
            Log.d("co",s3);
            if (s1.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s1.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s1.equals("3")) {
                llSales.setVisibility(View.GONE);
            } else if (s1.equals("4")) {
                llPayroll.setVisibility(View.GONE);
            } else if (s1.equals("5")) {
                llPf.setVisibility(View.GONE);
            } else if (s1.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s1.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s1.equals("8")) {
                llLeaveApplication.setVisibility(View.GONE);
            } else if (s1.equals("10")) {
                llDocument.setVisibility(View.GONE);
            }


            if (s2.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s2.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s2.equals("3")) {
                llSales.setVisibility(View.GONE);
            } else if (s2.equals("4")) {
                llPayroll.setVisibility(View.GONE);
            } else if (s2.equals("5")) {
                llPf.setVisibility(View.GONE);
            } else if (s2.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s2.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s2.equals("8")) {
                llLeaveApplication.setVisibility(View.GONE);
            } else if (s2.equals("10")) {
                llDocument.setVisibility(View.GONE);
            }

            if (s3.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s3.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s3.equals("3")) {
                llSales.setVisibility(View.GONE);
            } else if (s3.equals("4")) {
                llPayroll.setVisibility(View.GONE);
            } else if (s3.equals("5")) {
                llPf.setVisibility(View.GONE);
            } else if (s3.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s3.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s3.equals("8")) {
                llLeaveApplication.setVisibility(View.GONE);
            } else if (s3.equals("10")) {
                llDocument.setVisibility(View.GONE);
            }


        }else if (separated.length==4){
            Log.d("arpan","riku2");
            s1 = separated[0];
            s2= separated[1];
            s3=separated[2];
            s4=separated[3];
            if (s1.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s1.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s1.equals("3")) {
                llSales.setVisibility(View.GONE);
            } else if (s1.equals("4")) {
                llPayroll.setVisibility(View.GONE);
            } else if (s1.equals("5")) {
                llPf.setVisibility(View.GONE);
            } else if (s1.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s1.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s1.equals("8")) {
                llLeaveApplication.setVisibility(View.GONE);
            } else if (s1.equals("10")) {
                llDocument.setVisibility(View.GONE);
            }


            if (s2.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s2.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s2.equals("3")) {
                llSales.setVisibility(View.GONE);
            } else if (s2.equals("4")) {
                llPayroll.setVisibility(View.GONE);
            } else if (s2.equals("5")) {
                llPf.setVisibility(View.GONE);
            } else if (s2.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s2.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s2.equals("8")) {
                llLeaveApplication.setVisibility(View.GONE);
            } else if (s2.equals("10")) {
                llDocument.setVisibility(View.GONE);
            }

            if (s3.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s3.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s3.equals("3")) {
                llSales.setVisibility(View.GONE);
            } else if (s3.equals("4")) {
                llPayroll.setVisibility(View.GONE);
            } else if (s3.equals("5")) {
                llPf.setVisibility(View.GONE);
            } else if (s3.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s3.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s3.equals("8")) {
                llLeaveApplication.setVisibility(View.GONE);
            } else if (s3.equals("10")) {
                llDocument.setVisibility(View.GONE);
            }


            if (s4.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s4.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s4.equals("3")) {
                llSales.setVisibility(View.GONE);
            } else if (s4.equals("4")) {
                llPayroll.setVisibility(View.GONE);
            } else if (s4.equals("5")) {
                llPf.setVisibility(View.GONE);
            } else if (s4.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s4.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s4.equals("8")) {
                llLeaveApplication.setVisibility(View.GONE);
            } else if (s4.equals("10")) {
                llDocument.setVisibility(View.GONE);
            }


        }
        else if (separated.length==5){
            Log.d("arpan","riku2");
            s1 = separated[0];
            s2= separated[1];
            s3=separated[2];
            s4=separated[3];
            s5=separated[4];
            if (s1.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s1.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s1.equals("3")) {
                llSales.setVisibility(View.GONE);
            } else if (s1.equals("4")) {
                llPayroll.setVisibility(View.GONE);
            } else if (s1.equals("5")) {
                llPf.setVisibility(View.GONE);
            } else if (s1.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s1.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s1.equals("8")) {
                llLeaveApplication.setVisibility(View.GONE);
            } else if (s1.equals("10")) {
                llDocument.setVisibility(View.GONE);
            }


            if (s2.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s2.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s2.equals("3")) {
                llSales.setVisibility(View.GONE);
            } else if (s2.equals("4")) {
                llPayroll.setVisibility(View.GONE);
            } else if (s2.equals("5")) {
                llPf.setVisibility(View.GONE);
            } else if (s2.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s2.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s2.equals("8")) {
                llLeaveApplication.setVisibility(View.GONE);
            } else if (s2.equals("10")) {
                llDocument.setVisibility(View.GONE);
            }

            if (s3.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s3.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s3.equals("3")) {
                llSales.setVisibility(View.GONE);
            } else if (s3.equals("4")) {
                llPayroll.setVisibility(View.GONE);
            } else if (s3.equals("5")) {
                llPf.setVisibility(View.GONE);
            } else if (s3.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s3.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s3.equals("8")) {
                llLeaveApplication.setVisibility(View.GONE);
            } else if (s3.equals("10")) {
                llDocument.setVisibility(View.GONE);
            }


            if (s4.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s4.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s4.equals("3")) {
                llSales.setVisibility(View.GONE);
            } else if (s4.equals("4")) {
                llPayroll.setVisibility(View.GONE);
            } else if (s4.equals("5")) {
                llPf.setVisibility(View.GONE);
            } else if (s4.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s4.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s4.equals("8")) {
                llLeaveApplication.setVisibility(View.GONE);
            } else if (s4.equals("10")) {
                llDocument.setVisibility(View.GONE);
            }

            if (s5.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s5.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s5.equals("3")) {
                llSales.setVisibility(View.GONE);
            } else if (s5.equals("4")) {
                llPayroll.setVisibility(View.GONE);
            } else if (s5.equals("5")) {
                llPf.setVisibility(View.GONE);
            } else if (s5.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s5.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s5.equals("8")) {
                llLeaveApplication.setVisibility(View.GONE);
            } else if (s5.equals("10")) {
                llDocument.setVisibility(View.GONE);
            }

        }else if (separated.length==1){
            s1 = separated[0];
            if (s1.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s1.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s1.equals("3")) {
                llSales.setVisibility(View.GONE);
            } else if (s1.equals("4")) {
                llPayroll.setVisibility(View.GONE);
            } else if (s1.equals("5")) {
                llPf.setVisibility(View.GONE);
            } else if (s1.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s1.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s1.equals("8")) {
                llLeaveApplication.setVisibility(View.GONE);
            } else if (s1.equals("10")) {
                llDocument.setVisibility(View.GONE);
            }
        }else if (separated.length==6){
            s1 = separated[0];
            s2= separated[1];
            s3=separated[2];
            s4=separated[3];
            s5=separated[4];
            if (s1.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s1.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s1.equals("3")) {
                llSales.setVisibility(View.GONE);
            } else if (s1.equals("4")) {
                llPayroll.setVisibility(View.GONE);
            } else if (s1.equals("5")) {
                llPf.setVisibility(View.GONE);
            } else if (s1.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s1.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s1.equals("8")) {
                llLeaveApplication.setVisibility(View.GONE);
            } else if (s1.equals("10")) {
                llDocument.setVisibility(View.GONE);
            }


            if (s2.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s2.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s2.equals("3")) {
                llSales.setVisibility(View.GONE);
            } else if (s2.equals("4")) {
                llPayroll.setVisibility(View.GONE);
            } else if (s2.equals("5")) {
                llPf.setVisibility(View.GONE);
            } else if (s2.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s2.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s2.equals("8")) {
                llLeaveApplication.setVisibility(View.GONE);
            } else if (s2.equals("10")) {
                llDocument.setVisibility(View.GONE);
            }

            if (s3.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s3.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s3.equals("3")) {
                llSales.setVisibility(View.GONE);
            } else if (s3.equals("4")) {
                llPayroll.setVisibility(View.GONE);
            } else if (s3.equals("5")) {
                llPf.setVisibility(View.GONE);
            } else if (s3.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s3.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s3.equals("8")) {
                llLeaveApplication.setVisibility(View.GONE);
            } else if (s3.equals("10")) {
                llDocument.setVisibility(View.GONE);
            }


            if (s4.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s4.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s4.equals("3")) {
                llSales.setVisibility(View.GONE);
            } else if (s4.equals("4")) {
                llPayroll.setVisibility(View.GONE);
            } else if (s4.equals("5")) {
                llPf.setVisibility(View.GONE);
            } else if (s4.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s4.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s4.equals("8")) {
                llLeaveApplication.setVisibility(View.GONE);
            } else if (s4.equals("10")) {
                llDocument.setVisibility(View.GONE);
            }

            if (s5.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s5.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s5.equals("3")) {
                llSales.setVisibility(View.GONE);
            } else if (s5.equals("4")) {
                llPayroll.setVisibility(View.GONE);
            } else if (s5.equals("5")) {
                llPf.setVisibility(View.GONE);
            } else if (s5.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s5.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s5.equals("8")) {
                llLeaveApplication.setVisibility(View.GONE);
            } else if (s5.equals("10")) {
                llDocument.setVisibility(View.GONE);
            }

            if (s6.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s6.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s6.equals("3")) {
                llSales.setVisibility(View.GONE);
            } else if (s6.equals("4")) {
                llPayroll.setVisibility(View.GONE);
            } else if (s6.equals("5")) {
                llPf.setVisibility(View.GONE);
            } else if (s6.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s6.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s6.equals("8")) {
                llLeaveApplication.setVisibility(View.GONE);
            } else if (s6.equals("10")) {
                llDocument.setVisibility(View.GONE);
            }
        }







    }

    private void onClick() {
        llProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent = new Intent(EmployeeDashBoardActivity.this, ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

            }
        });
        llAttandence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent = new Intent(EmployeeDashBoardActivity.this, AttendanceActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

            }
        });

        llPayroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent = new Intent(EmployeeDashBoardActivity.this, PayrollActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

            }
        });

        llPf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionCheck.isNetworkAvailable()) {

                    Intent intent = new Intent(EmployeeDashBoardActivity.this, PFActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    connectionCheck.getNetworkActiveAlert().show();
                }

            }
        });

        llFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent = new Intent(EmployeeDashBoardActivity.this, FeedBackActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

            }
        });

        llNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionCheck.isNetworkAvailable()) {

                    Intent intent = new Intent(EmployeeDashBoardActivity.this, NotificationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    connectionCheck.getNetworkActiveAlert().show();
                }

            }
        });


        llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(EmployeeDashBoardActivity.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                pref.saveEmpId("");
                deleteCache(getApplicationContext());
            }
        });

        llDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent = new Intent(EmployeeDashBoardActivity.this, DocumentActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

            }
        });


        llLeaveApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBrowser();
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are  you want to exit ,"
                );
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                finish();
                            }
                        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    private void openBrowser(){
        Uri uri = Uri.parse(pref.getLeaveUrl()); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}




