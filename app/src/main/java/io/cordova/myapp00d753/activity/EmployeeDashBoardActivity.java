package io.cordova.myapp00d753.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

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
import android.widget.Toast;



import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.florent37.viewtooltip.ViewTooltip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kyanogen.signatureview.SignatureView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;
import io.cordova.myapp00d753.utility.Pref;

public class  EmployeeDashBoardActivity extends AppCompatActivity {
    LinearLayout llProfile, llAttandence, llPayroll, llPf, llFeedBack, llNotification, llSales, llLeaveApplication, llDocument, llLogout,llFace;
    Pref pref;
    TextView tvEmployeeName, tvGreeting, tvLoginDateTime;
    NetworkConnectionCheck connectionCheck;
    String s1, s2, s3, s4, s5, s6;
    LinearLayout llRem, llREMD, llREMD1;
    TextView tvNumber;
    LinearLayout llCall;
    LinearLayout llOther,llOtherD,llOtherD1;

    TextView tvEmpName;
    Toolbar toolbar;
    DrawerLayout dlMain;
    ImageView imgageView;
    boolean mslideState;
    ImageView imgProfile,imgAttenDance,imgPayroll,imgPf,imgDocument,imgRem;
    FloatingActionButton fab;
    LinearLayout llProfileD, llAttandenceD, llPayrollD,   llDocumentD ,llSale,llRemD;


    @ColorInt
    public static final int BLUE = 0xFF0FB8B3;

    @ColorInt
    public static final int GREEN = 0xFF5BBD76;

    LinearLayout llGeoFence,llDailyLog;
    String responseCode,DocLink,PFLink;
    AlertDialog alertDialog;

    Bitmap bitmap;
    String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/GENIUSSURVEY/";
    String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    android.app.AlertDialog alerDialog1;
    AlertDialog alert2;
    android.app.AlertDialog alert3;
    SignatureView canvasLL;
    File f;
    private static final String IMAGE_DIRECTORY = "/signdemo";
    LinearLayout llMain,llDemo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_dash_board);
        initialize();
        loginFunction();
        onClick();
    }

    private void initialize() {
        pref = new Pref(EmployeeDashBoardActivity.this);
        connectionCheck = new NetworkConnectionCheck(EmployeeDashBoardActivity.this);
        llProfile = (LinearLayout) findViewById(R.id.llProfile);
        llAttandence = (LinearLayout) findViewById(R.id.llAttendance);
        llPayroll = (LinearLayout) findViewById(R.id.llPayroll);
        llPf = (LinearLayout) findViewById(R.id.llPf);
        llFeedBack = (LinearLayout) findViewById(R.id.llFeedBack);
        llNotification = (LinearLayout) findViewById(R.id.llNotification);
        //llSales = (LinearLayout) findViewById(R.id.llSales);
        llLeaveApplication = (LinearLayout) findViewById(R.id.llleaveApplication);

        llDocument = (LinearLayout) findViewById(R.id.llDocument);

        llLogout = (LinearLayout) findViewById(R.id.llLogout);

        tvEmpName=(TextView)findViewById(R.id.tvEmpName);

        imgageView=(ImageView)findViewById(R.id.imageview);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        dlMain = (DrawerLayout) findViewById(R.id.dlMain);
        imgProfile=(ImageView)findViewById(R.id.imgProfile);
        imgAttenDance=(ImageView)findViewById(R.id.imgAttenDance);
        imgPayroll=(ImageView)findViewById(R.id.imgPayroll);
        imgPf=(ImageView)findViewById(R.id.imgPf);
        imgDocument=(ImageView)findViewById(R.id.imgDocument);
        imgRem=(ImageView)findViewById(R.id.imgRem);
        fab=(FloatingActionButton)findViewById(R.id.fab);




        llRem = (LinearLayout) findViewById(R.id.llRem);
        llFace = (LinearLayout) findViewById(R.id.llFace);
        llSale = (LinearLayout) findViewById(R.id.llSale);

        llProfileD = (LinearLayout) findViewById(R.id.llProfileD);
        llAttandenceD = (LinearLayout) findViewById(R.id.llAttendanceD);
        llPayrollD = (LinearLayout) findViewById(R.id.llPayrollD);
        llDocumentD = (LinearLayout) findViewById(R.id.llDocumentD);
        llRemD = (LinearLayout) findViewById(R.id.llRemD);


        tvEmployeeName = (TextView) findViewById(R.id.tvEmployeeName);
        tvEmployeeName.setText(pref.getEmpName());
        tvEmpName.setText("Welcome "+pref.getEmpName());



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

            tvGreeting.setText("Good Evening");
        }
        tvLoginDateTime = (TextView) findViewById(R.id.tvLoginDateTime);
        tvLoginDateTime.setText(pref.getloginTime());

        llCall=(LinearLayout)findViewById(R.id.llCall);
        String menu = pref.getMenu();
        String d = menu.replace("{", "").replace("}", "");
        Log.d("split", d);
        Log.d("menuu", menu);
        String[] separated = menu.split(",");

        if (separated.length == 2) {
            Log.d("arpan", "riku");
            s1 = separated[0];
            s2 = separated[1];
            if (s1.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s1.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s1.equals("3")) {
                llRem.setVisibility(View.GONE);
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

                llDocument.setVisibility(View.VISIBLE);
            } else if (s1.equals("10")) {
                llDocument.setVisibility(View.GONE);

            }


            if (s2.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s2.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s2.equals("3")) {
                llRem.setVisibility(View.GONE);
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


        } else if (separated.length == 3) {
            Log.d("arpan", "riku1");
            s1 = separated[0];
            s2 = separated[1];
            s3 = separated[2];
            Log.d("co", s3);
            if (s1.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s1.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s1.equals("3")) {
                llRem.setVisibility(View.GONE);
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
                llRem.setVisibility(View.GONE);
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
                llRem.setVisibility(View.GONE);
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


        } else if (separated.length == 4) {
            s1 = separated[0];
            s2 = separated[1];
            s3 = separated[2];
            s4 = separated[3];
            if (s1.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s1.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s1.equals("3")) {
                llRem.setVisibility(View.GONE);
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
                llRem.setVisibility(View.GONE);
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
                llRem.setVisibility(View.GONE);
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
                llRem.setVisibility(View.GONE);
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

        } else if (separated.length == 5) {
            Log.d("arpan", "riku2");
            s1 = separated[0];
            s2 = separated[1];
            s3 = separated[2];
            s4 = separated[3];
            s5 = separated[4];
            if (s1.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s1.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s1.equals("3")) {
                llRem.setVisibility(View.GONE);
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
                llRem.setVisibility(View.GONE);
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
                llRem.setVisibility(View.GONE);
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
                llRem.setVisibility(View.GONE);
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
                llRem.setVisibility(View.GONE);
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

        } else if (separated.length == 1) {
            s1 = separated[0];
            if (s1.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s1.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s1.equals("3")) {
                llRem.setVisibility(View.GONE);
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
        } else if (separated.length == 6) {
            s1 = separated[0];
            s2 = separated[1];
            s3 = separated[2];
            s4 = separated[3];
            s5 = separated[4];
            s6=separated[5];
            if (s1.equals("1")) {
                llProfile.setVisibility(View.GONE);
            } else if (s1.equals("2")) {
                llAttandence.setVisibility(View.GONE);
            } else if (s1.equals("3")) {
                llRem.setVisibility(View.GONE);
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
                llRem.setVisibility(View.GONE);
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
                llRem.setVisibility(View.GONE);
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
                llRem.setVisibility(View.GONE);
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
                llRem.setVisibility(View.GONE);
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
                llRem.setVisibility(View.GONE);
            } else if (s6.equals("4")) {
                llPayroll.setVisibility(View.GONE);
            } else if (s6.equals("5")) {
                llPf.setVisibility(View.GONE);
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

        llDailyLog=(LinearLayout)findViewById(R.id.llDailyLog);



        llOther=(LinearLayout)findViewById(R.id.llOther);
        llMain=(LinearLayout)findViewById(R.id.llMain);
        llDemo=(LinearLayout)findViewById(R.id.llDemo);

        if (pref.getMasterId().equals("AEMP000075000185")){
            llOther.setVisibility(View.VISIBLE);
        }else {
            llOther.setVisibility(View.GONE);
        }
       // tooltipsView();

        llGeoFence=(LinearLayout)findViewById(R.id.llGeoFence);
        if (pref.getFenceMenuFlag().equals("1")){
            llGeoFence.setVisibility(View.VISIBLE);
        }else {
            llGeoFence.setVisibility(View.GONE);
        }

        if (pref.getDemoFlag().equals("1")){
            llDailyLog.setVisibility(View.VISIBLE);
            llFace.setVisibility(View.GONE);
            llSale.setVisibility(View.VISIBLE);
            llMain.setVisibility(View.GONE);
            llDemo.setVisibility(View.VISIBLE);
        }else {
            llDailyLog.setVisibility(View.GONE);
            llFace.setVisibility(View.GONE);
            llSale.setVisibility(View.GONE);
            llMain.setVisibility(View.VISIBLE);
            llDemo.setVisibility(View.GONE);
        }



    }

    private void onClick() {
        llProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (pref.getSecurityCode().equals("0000")||pref.getSecurityCode().equals("888")||pref.getSecurityCode().equals("222")) {
                    Intent intent = new Intent(EmployeeDashBoardActivity.this, ProfileDashBoardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(EmployeeDashBoardActivity.this, ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }
        });

        llProfileD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (pref.getSecurityCode().equals("0000")||pref.getSecurityCode().equals("888")||pref.getSecurityCode().equals("222")) {
                    Intent intent = new Intent(EmployeeDashBoardActivity.this, ProfileDashBoardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(EmployeeDashBoardActivity.this, ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }
        });
        llAttandence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionCheck.isNetworkAvailable()) {
                    if (connectionCheck.isGPSEnabled()) {


                        Intent intent = new Intent(EmployeeDashBoardActivity.this, AttendanceActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else {
                        Toast.makeText(EmployeeDashBoardActivity.this,"Sorry!Please check your GPS connection",Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(EmployeeDashBoardActivity.this, "no internet connection", Toast.LENGTH_LONG).show();
                }

            }
        });

        llAttandenceD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionCheck.isNetworkAvailable()) {
                    if (connectionCheck.isGPSEnabled()) {


                        Intent intent = new Intent(EmployeeDashBoardActivity.this, AttendanceActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else {
                        Toast.makeText(EmployeeDashBoardActivity.this,"Sorry!Please check your GPS connection",Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(EmployeeDashBoardActivity.this, "no internet connection", Toast.LENGTH_LONG).show();
                }

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
        llPayrollD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(EmployeeDashBoardActivity.this, PayrollActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        llSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(EmployeeDashBoardActivity.this, SalesManagementDashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        llPf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionCheck.isNetworkAvailable()) {




                    Uri uri = Uri.parse(PFLink); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else {
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
        llDailyLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EmployeeDashBoardActivity.this,DailyDashBoardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        llFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EmployeeDashBoardActivity.this,FRDashboard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
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
                } else {
                    connectionCheck.getNetworkActiveAlert().show();
                }

            }
        });


        llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(EmployeeDashBoardActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                pref.saveEmpId("");
                deleteCache(getApplicationContext());
                finish();
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

        llDocumentD.setOnClickListener(new View.OnClickListener() {
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


        llRem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                Intent intent=new Intent(EmployeeDashBoardActivity.this,RemDashBoardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        llRemD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                Intent intent=new Intent(EmployeeDashBoardActivity.this,RemDashBoardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        llOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(getApplicationContext(),"com.geniusdemo.demo");
            }
        });



        llCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeeDashBoardActivity.this,ChangePasswordActivity.class);
                startActivity(intent);
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: 9836655124"));
                startActivity(intent);
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

        llGeoFence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(EmployeeDashBoardActivity.this,ConfigNumberActivity.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
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

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    private void openBrowser() {
        Uri uri = Uri.parse(pref.getLeaveUrl()); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void startNewActivity(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            // We found the activity now start the activity
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            // Bring user to the market or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + packageName));
            context.startActivity(intent);
        }
    }

    private Paint createPaint() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setShader(new LinearGradient(0, 0, 0, 600, BLUE, GREEN, Shader.TileMode.CLAMP));
        paint.setStyle(Paint.Style.FILL);
        return paint;
    }

    private void tooltipsView(){
        ViewTooltip
                .on(imgProfile)
                // .customView(customView)
                .position(ViewTooltip.Position.RIGHT)
                .arrowSourceMargin(0)
                .arrowTargetMargin(0)
                .text("Profile View")
                .clickToHide(true)
                .autoHide(true, 2000)
                .color(createPaint())
                .animation(new ViewTooltip.FadeTooltipAnimation(500))
                .onDisplay(new ViewTooltip.ListenerDisplay() {
                    @Override
                    public void onDisplay(View view) {
                        Log.d("ViewTooltip", "onDisplay");
                    }
                })
                .onHide(new ViewTooltip.ListenerHide() {
                    @Override
                    public void onHide(View view) {
                        Log.d("ViewTooltip", "onHide");
                    }
                })
                .show();




        ViewTooltip
                .on(imgAttenDance)
                // .customView(customView)
                .position(ViewTooltip.Position.BOTTOM)
                .arrowSourceMargin(0)
                .arrowTargetMargin(0)
                .text("Attendance View")
                .clickToHide(true)
                .autoHide(true, 2000)
                .color(createPaint())
                .animation(new ViewTooltip.FadeTooltipAnimation(500))
                .onDisplay(new ViewTooltip.ListenerDisplay() {
                    @Override
                    public void onDisplay(View view) {
                        Log.d("ViewTooltip", "onDisplay");
                    }
                })
                .onHide(new ViewTooltip.ListenerHide() {
                    @Override
                    public void onHide(View view) {
                        Log.d("ViewTooltip", "onHide");
                    }
                })
                .show();



        ViewTooltip
                .on(imgPayroll)
                // .customView(customView)
                .position(ViewTooltip.Position.BOTTOM)
                .arrowSourceMargin(0)
                .arrowTargetMargin(0)
                .text("Payroll View")
                .clickToHide(true)
                .autoHide(true, 2000)
                .color(createPaint())
                .animation(new ViewTooltip.FadeTooltipAnimation(500))
                .onDisplay(new ViewTooltip.ListenerDisplay() {
                    @Override
                    public void onDisplay(View view) {
                        Log.d("ViewTooltip", "onDisplay");
                    }
                })
                .onHide(new ViewTooltip.ListenerHide() {
                    @Override
                    public void onHide(View view) {
                        Log.d("ViewTooltip", "onHide");
                    }
                })
                .show();



        ViewTooltip
                .on(imgPf)
                // .customView(customView)
                .position(ViewTooltip.Position.LEFT)
                .arrowSourceMargin(0)
                .arrowTargetMargin(0)
                .text("PF View")
                .clickToHide(true)
                .autoHide(true, 2000)
                .color(createPaint())
                .animation(new ViewTooltip.FadeTooltipAnimation(500))
                .onDisplay(new ViewTooltip.ListenerDisplay() {
                    @Override
                    public void onDisplay(View view) {
                        Log.d("ViewTooltip", "onDisplay");
                    }
                })
                .onHide(new ViewTooltip.ListenerHide() {
                    @Override
                    public void onHide(View view) {
                        Log.d("ViewTooltip", "onHide");
                    }
                })
                .show();




        ViewTooltip
                .on(imgDocument)
                // .customView(customView)
                .position(ViewTooltip.Position.BOTTOM)
                .arrowSourceMargin(0)
                .arrowTargetMargin(0)
                .text("Document View")
                .clickToHide(true)
                .autoHide(true, 2000)
                .color(createPaint())
                .animation(new ViewTooltip.FadeTooltipAnimation(500))
                .onDisplay(new ViewTooltip.ListenerDisplay() {
                    @Override
                    public void onDisplay(View view) {
                        Log.d("ViewTooltip", "onDisplay");
                    }
                })
                .onHide(new ViewTooltip.ListenerHide() {
                    @Override
                    public void onHide(View view) {
                        Log.d("ViewTooltip", "onHide");
                    }
                })
                .show();


        ViewTooltip
                .on(imgRem)
                // .customView(customView)
                .position(ViewTooltip.Position.LEFT)
                .arrowSourceMargin(0)
                .arrowTargetMargin(0)
                .text("Reimbursement View")
                .clickToHide(true)
                .autoHide(true, 2000)
                .color(createPaint())
                .animation(new ViewTooltip.FadeTooltipAnimation(500))
                .onDisplay(new ViewTooltip.ListenerDisplay() {
                    @Override
                    public void onDisplay(View view) {
                        Log.d("ViewTooltip", "onDisplay");
                    }
                })
                .onHide(new ViewTooltip.ListenerHide() {
                    @Override
                    public void onHide(View view) {
                        Log.d("ViewTooltip", "onHide");
                    }
                })
                .show();



        ViewTooltip
                .on(imgageView)
                // .customView(customView)
                .position(ViewTooltip.Position.RIGHT)
                .arrowSourceMargin(0)
                .arrowTargetMargin(0)
                .text("Others View")
                .clickToHide(true)
                .autoHide(true, 2000)
                .color(createPaint())
                .animation(new ViewTooltip.FadeTooltipAnimation(500))
                .onDisplay(new ViewTooltip.ListenerDisplay() {
                    @Override
                    public void onDisplay(View view) {
                        Log.d("ViewTooltip", "onDisplay");
                    }
                })
                .onHide(new ViewTooltip.ListenerHide() {
                    @Override
                    public void onHide(View view) {
                        Log.d("ViewTooltip", "onHide");
                    }
                })
                .show();







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
        Log.d("inputLogin", surl);

        final ProgressDialog pd=new ProgressDialog(EmployeeDashBoardActivity.this);
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
                                    String AEMClientID=obj.optString("AEMClientID");
                                    pref.saveEmpClintId(AEMClientID);

                                    pref.saveMsgAlertStatus(AppRenameFlag);
                                    pref.saveMsg(AppRenameText);

                                    String PFConsolidateURL=obj.optString("PFConsolidateURL");
                                    pref.savePFURL(PFConsolidateURL);




                                }

                                if (pref.getMsgAlertStatus()){
                                    msgAlert();
                                }else {
                                    acceptance();
                                }
                            }else {
                                Intent intent=new Intent(EmployeeDashBoardActivity.this,LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EmployeeDashBoardActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                //  Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                showAlert();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");

    }

    public void acceptance() {

        String surl = AppData.url+"gcl_EmployeeAppointLetterAcceptance?AEMConsultantId=" + pref.getEmpConId() + "&MasterId=" + pref.getMasterId() + "&SecurityCode=" + pref.getSecurityCode();
        Log.d("inputLogin", surl);

        final ProgressDialog pd=new ProgressDialog(EmployeeDashBoardActivity.this);
        pd.setMessage("Loading.....");
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        pd.dismiss();
                        getPFURL();
                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            responseCode=job1.optString("responseCode");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();


                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    DocLink = obj.optString("DocLink");




                                }

                                if (responseCode.equals("1")){
                                    shoeDialog(DocLink);
                                }


                            }


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EmployeeDashBoardActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                //  Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                showAlert();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");

    }

    public void getPFURL() {

        String surl = AppData.url+"get_PFManagementTripleA?MasterID="+pref.getMasterId()+"&SecurityCode="+pref.getSecurityCode();
        Log.d("inputLogin", surl);

        final ProgressDialog pd=new ProgressDialog(EmployeeDashBoardActivity.this);
        pd.setMessage("Loading.....");
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        pd.dismiss();
                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            responseCode=job1.optString("responseCode");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();


                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    PFLink = obj.optString("url");





                                }




                            }


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EmployeeDashBoardActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                //  Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                showAlert();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");

    }

    private void showAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("something went wrong");
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                    }
                });


    }


    private void shoeDialog(final String docLink) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EmployeeDashBoardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.acceptance_dialog, null);
        dialogBuilder.setView(dialogView);
        TextView tvClick=(TextView)dialogView.findViewById(R.id.tvClick);
        tvClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(docLink));
                startActivity(browserIntent);
            }
        });
        ImageView imgCancel=(ImageView)dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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




    public String saveImage(Bitmap myBitmap) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY /*iDyme folder*/);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
            Log.d("hhhhh", wallpaperDirectory.toString());
        }

        try {
            f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");


            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileOutputStream fo = null;
            try {
                fo = new FileOutputStream(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getApplicationContext(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";

    }


    private void msgAlert() {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(EmployeeDashBoardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_message_alert, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);
        tvInvalidDate.setText(pref.getMsg());



        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert3.dismiss();
                acceptance();

            }
        });

        alert3 = dialogBuilder.create();
        alert3.setCancelable(true);
        Window window = alert3.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alert3.show();
    }
}




