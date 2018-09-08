package io.cordova.myapp00d753.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;



import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.utility.Pref;

public class SuperVisiorDashBoardActivity extends AppCompatActivity {
    LinearLayout llProfile, llAttandence, llPayroll, llPf, llFeedBack, llNotification, llSales, llLeaveApplication, llDocument, llLogout;    Pref pref;
    TextView tvEmployeeName,tvGreeting,tvLoginDateTime;
    String s1,s2,s3,s4,s5,s6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_visior_dash_board);
        initialize();
        onClick();
    }

    private  void  initialize(){
        pref=new Pref(getApplicationContext());
        tvEmployeeName=(TextView)findViewById(R.id.tvEmployeeName);

        tvEmployeeName.setText(pref.getEmpName());
        tvGreeting=(TextView)findViewById(R.id.tvGreeting);
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

        }else if (separated.length==6){
            s1 = separated[0];
            s2= separated[1];
            s3=separated[2];
            s4=separated[3];
            s5=separated[4];
            s6=separated[5];
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
        else if (separated.length==1){
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
        }


    }

    private void onClick(){
        llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref.saveEmpId("");
                Intent intent=new Intent(SuperVisiorDashBoardActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();


            }
        });

        llProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SuperVisiorDashBoardActivity.this,SupProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        llPayroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SuperVisiorDashBoardActivity.this,SupPyrollActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        llAttandence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SuperVisiorDashBoardActivity.this,SupAttendanceActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        llNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SuperVisiorDashBoardActivity.this,SupNotificationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        llFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SuperVisiorDashBoardActivity.this,SupFeedBackActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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


}
