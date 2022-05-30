package io.cordova.myapp00d753.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.util.Base64;
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


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

public class SuperVisiorDashBoardActivity extends AppCompatActivity {
    LinearLayout llProfile, llAttandence, llPayroll, llPf, llFeedBack, llNotification, llDocument, llLogout;
    Pref pref;
    TextView tvEmployeeName,tvGreeting,tvLoginDateTime;
    String s1,s2,s3,s4,s5,s6;
    TextView tvEmpName;
    Toolbar toolbar;
    DrawerLayout dlMain;
    ImageView imgageView;
    boolean mslideState;
    LinearLayout llCall;
    android.app.AlertDialog alerDialog1;
    LinearLayout llSale;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_visior_dash_board);
        initialize();
        onClick();
        loginFunction();
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

            tvGreeting.setText("Good Evening");
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

            } else if (s1.equals("4")) {
                llPayroll.setVisibility(View.GONE);


            } else if (s1.equals("5")) {
                llPf.setVisibility(View.GONE);
            } else if (s1.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s1.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s1.equals("8")) {

            } else if (s1.equals("10")) {
                llDocument.setVisibility(View.GONE);

            }


            if (s2.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s2.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s2.equals("3")) {

            } else if (s2.equals("4")) {
                llPayroll.setVisibility(View.GONE);


            } else if (s2.equals("5")) {
                llPf.setVisibility(View.GONE);




            } else if (s2.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s2.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s2.equals("8")) {



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

            } else if (s1.equals("4")) {
                llPayroll.setVisibility(View.GONE);


            } else if (s1.equals("5")) {
                llPf.setVisibility(View.GONE);



            } else if (s1.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s1.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s1.equals("8")) {



            } else if (s1.equals("10")) {
                llDocument.setVisibility(View.GONE);

            }


            if (s2.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s2.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s2.equals("3")) {

            } else if (s2.equals("4")) {
                llPayroll.setVisibility(View.GONE);


            } else if (s2.equals("5")) {
                llPf.setVisibility(View.GONE);


            } else if (s2.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s2.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s2.equals("8")) {



            } else if (s2.equals("10")) {
                llDocument.setVisibility(View.GONE);

            }


            if (s3.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s3.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s3.equals("3")) {

            } else if (s3.equals("4")) {
                llPayroll.setVisibility(View.GONE);


            } else if (s3.equals("5")) {
                llPf.setVisibility(View.GONE);




            } else if (s3.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s3.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s3.equals("8")) {



            } else if (s3.equals("10")) {
                llDocument.setVisibility(View.GONE);

            }




        }else if (separated.length==4){
            s1 = separated[0];
            s2= separated[1];
            s3=separated[2];
            s4=separated[3];
            if (s1.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s1.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s1.equals("3")) {

            } else if (s1.equals("4")) {
                llPayroll.setVisibility(View.GONE);


            } else if (s1.equals("5")) {
                llPf.setVisibility(View.GONE);



            } else if (s1.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s1.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s1.equals("8")) {



            } else if (s1.equals("10")) {
                llDocument.setVisibility(View.GONE);

            }


            if (s2.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s2.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s2.equals("3")) {

            } else if (s2.equals("4")) {
                llPayroll.setVisibility(View.GONE);


            } else if (s2.equals("5")) {
                llPf.setVisibility(View.GONE);


            } else if (s2.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s2.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s2.equals("8")) {



            } else if (s2.equals("10")) {
                llDocument.setVisibility(View.GONE);

            }


            if (s3.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s3.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s3.equals("3")) {

            } else if (s3.equals("4")) {
                llPayroll.setVisibility(View.GONE);


            } else if (s3.equals("5")) {
                llPf.setVisibility(View.GONE);




            } else if (s3.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s3.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s3.equals("8")) {



            } else if (s3.equals("10")) {
                llDocument.setVisibility(View.GONE);
            }


            if (s4.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s4.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s4.equals("3")) {

            } else if (s4.equals("4")) {
                llPayroll.setVisibility(View.GONE);


            } else if (s4.equals("5")) {
                llPf.setVisibility(View.GONE);




            } else if (s4.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s4.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s4.equals("8")) {



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

            } else if (s1.equals("4")) {
                llPayroll.setVisibility(View.GONE);


            } else if (s1.equals("5")) {
                llPf.setVisibility(View.GONE);



            } else if (s1.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s1.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s1.equals("8")) {



            } else if (s1.equals("10")) {
                llDocument.setVisibility(View.GONE);

            }


            if (s2.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s2.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s2.equals("3")) {

            } else if (s2.equals("4")) {
                llPayroll.setVisibility(View.GONE);


            } else if (s2.equals("5")) {
                llPf.setVisibility(View.GONE);


            } else if (s2.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s2.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s2.equals("8")) {

                llDocument.setVisibility(View.VISIBLE);
            } else if (s2.equals("10")) {
                llDocument.setVisibility(View.GONE);

            }


            if (s3.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s3.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s3.equals("3")) {

            } else if (s3.equals("4")) {
                llPayroll.setVisibility(View.GONE);


            } else if (s3.equals("5")) {
                llPf.setVisibility(View.GONE);




            } else if (s3.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s3.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s3.equals("8")) {



            } else if (s3.equals("10")) {
                llDocument.setVisibility(View.GONE);

            }


            if (s4.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s4.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s4.equals("3")) {

            } else if (s4.equals("4")) {
                llPayroll.setVisibility(View.GONE);


            } else if (s4.equals("5")) {
                llPf.setVisibility(View.GONE);




            } else if (s4.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s4.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s4.equals("8")) {



            } else if (s4.equals("10")) {
                llDocument.setVisibility(View.GONE);

            }


            if (s5.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s5.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s5.equals("3")) {

            } else if (s5.equals("4")) {
                llPayroll.setVisibility(View.GONE);


            } else if (s5.equals("5")) {
                llPf.setVisibility(View.GONE);




            } else if (s5.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s5.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s5.equals("8")) {



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

            } else if (s1.equals("4")) {
                llPayroll.setVisibility(View.GONE);


            } else if (s1.equals("5")) {
                llPf.setVisibility(View.GONE);



            } else if (s1.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s1.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s1.equals("8")) {



            } else if (s1.equals("10")) {
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

            } else if (s1.equals("4")) {
                llPayroll.setVisibility(View.GONE);
            } else if (s1.equals("5")) {
                llPf.setVisibility(View.GONE);
            } else if (s1.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s1.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s1.equals("8")) {

            } else if (s1.equals("10")) {
                llDocument.setVisibility(View.GONE);
            }


            if (s2.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s2.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s2.equals("3")) {

            } else if (s2.equals("4")) {
                llPayroll.setVisibility(View.GONE);
            } else if (s2.equals("5")) {
                llPf.setVisibility(View.GONE);
            } else if (s2.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s2.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s2.equals("8")) {

            } else if (s2.equals("10")) {
                llDocument.setVisibility(View.GONE);
            }

            if (s3.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s3.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s3.equals("3")) {

            } else if (s3.equals("4")) {
                llPayroll.setVisibility(View.GONE);
            } else if (s3.equals("5")) {
                llPf.setVisibility(View.GONE);
            } else if (s3.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s3.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s3.equals("8")) {

            } else if (s3.equals("10")) {
                llDocument.setVisibility(View.GONE);
            }


            if (s4.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s4.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s4.equals("3")) {

            } else if (s4.equals("4")) {
                llPayroll.setVisibility(View.GONE);
            } else if (s4.equals("5")) {
                llPf.setVisibility(View.GONE);
            } else if (s4.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s4.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s4.equals("8")) {

            } else if (s4.equals("10")) {
                llDocument.setVisibility(View.GONE);
            }

            if (s5.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s5.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s5.equals("3")) {

            } else if (s5.equals("4")) {
                llPayroll.setVisibility(View.GONE);
            } else if (s5.equals("5")) {
                llPf.setVisibility(View.GONE);
            } else if (s5.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s5.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s5.equals("8")) {

            } else if (s5.equals("10")) {
                llDocument.setVisibility(View.GONE);
            }

            if (s6.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s6.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s6.equals("3")) {

            } else if (s6.equals("4")) {
                llPayroll.setVisibility(View.GONE);
            } else if (s6.equals("5")) {
                llPf.setVisibility(View.GONE);
            } else if (s6.equals("6")) {
                llFeedBack.setVisibility(View.GONE);
            } else if (s6.equals("7")) {
                llNotification.setVisibility(View.GONE);
            } else if (s6.equals("8")) {

            } else if (s6.equals("10")) {
                llDocument.setVisibility(View.GONE);
            }
        }
        llCall=(LinearLayout)findViewById(R.id.llCall);



        tvEmpName=(TextView)findViewById(R.id.tvEmpName);
        tvEmpName.setText("Welcome "+pref.getEmpName());

        imgageView=(ImageView)findViewById(R.id.imageview);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        dlMain = (DrawerLayout) findViewById(R.id.dlMain);
        llSale=(LinearLayout)findViewById(R.id.llSale);

        if (pref.getDemoFlag().equals("1")){
            llSale.setVisibility(View.VISIBLE);
        }else {
            llSale.setVisibility(View.GONE);
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

        imgageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlMain.openDrawer(Gravity.LEFT);
            }
        });

        dlMain.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {

            }

            @Override
            public void onDrawerOpened(@NonNull View view) {
                mslideState = true;

            }

            @Override
            public void onDrawerClosed(@NonNull View view) {
                mslideState = false;

            }

            @Override
            public void onDrawerStateChanged(int i) {

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

        llSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(SuperVisiorDashBoardActivity.this, SupSalesManagementDashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        llPayroll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                Intent intent=new Intent(SuperVisiorDashBoardActivity.this,SupSalaryActivity.class);
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

        llCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:18008333555"));
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

    public void loginFunction() {
        byte[] data = new byte[0];
        try {
            data = pref.getPassword().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String base64 = Base64.encodeToString(data, Base64.DEFAULT).replaceAll("\\s+", "");;


        String surl = AppData.url+"get_GCLAuthenticateWithEncryption?MasterID=" + pref.getMasterId() + "&Password=" + base64 + "&IMEI=0000&Version=1.0&SecurityCode=" + pref.getSecurityCode() + "&DeviceID=azzzzzz&DeviceType=A";
        final ProgressDialog pd=new ProgressDialog(SuperVisiorDashBoardActivity.this);
        pd.setCancelable(false);
        pd.setMessage("Loading.....");
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                pd.dismiss();

                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String LoginDateTime = obj.optString("LoginDateTime");
                                    pref.saveloginTime(LoginDateTime);
                                    boolean AppRenameFlag=obj.optBoolean("AppRenameFlag");
                                    String AppRenameText=obj.optString("AppRenameText");

                                    pref.saveMsgAlertStatus(AppRenameFlag);
                                    pref.saveMsg(AppRenameText);





                                }

                                if (pref.getMsgAlertStatus()){
                                    msgAlert();
                                }else {

                                }
                            }else {
                                Intent intent=new Intent(SuperVisiorDashBoardActivity.this,LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                           // Toast.makeText(SuperVisiorDashBoardActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                //  Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
                Intent intent=new Intent(SuperVisiorDashBoardActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");

    }

    private void msgAlert() {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(SuperVisiorDashBoardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_message_alert, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);
        tvInvalidDate.setText(pref.getMsg());



        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();

            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }


}
