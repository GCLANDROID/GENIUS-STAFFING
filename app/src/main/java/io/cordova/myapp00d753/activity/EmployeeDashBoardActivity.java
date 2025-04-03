package io.cordova.myapp00d753.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;



import androidx.annotation.ColorInt;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kyanogen.signatureview.SignatureView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.SKF.SKF_AttendanceRegularizationActivity;
import io.cordova.myapp00d753.adapter.MenuItemAdapter;
import io.cordova.myapp00d753.adapter.NotiAdapter;
import io.cordova.myapp00d753.adapter.PFDocumentAdapter;
import io.cordova.myapp00d753.module.MenuItemModel;
import io.cordova.myapp00d753.module.PFDocumentModule;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.Util;

public class  EmployeeDashBoardActivity extends AppCompatActivity {
    private static final String TAG = "EmployeeDashBoardActivi";
    Pref pref;
    TextView  tvLoginDateTime;
    NetworkConnectionCheck connectionCheck;

    TextView tvEmpName,tvGreeting;
    boolean mslideState;


    @ColorInt
    public static final int BLUE = 0xFF0FB8B3;

    @ColorInt
    public static final int GREEN = 0xFF5BBD76;

    LinearLayout llGeoFence,llDailyLog;
    String responseCode,DocLink,PFLink;
    AlertDialog alertDialog;
    String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/GENIUSSURVEY/";
    String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    android.app.AlertDialog alert3;
    File f;
    private static final String IMAGE_DIRECTORY = "/signdemo";

    RecyclerView rvItem,rvPFDocument,rvNotification;
    ArrayList<MenuItemModel>itemList=new ArrayList<>();
    ImageView imglogout;
    String menuName;
    AlertDialog feedbackpopupDialog,aknowledgePopUp,pfImageDialog;
    FloatingActionButton fbSpoke;
    JSONArray spokepersonArray;
    AlertDialog al1,al2;
    boolean survey;
    String phoneNumber="0000";

    ArrayList<String> contentList;
    int signFlag=0;
    private static final String IMAGE_DIRECTORY_CONSENT = "/signdemo";
    AlertDialog consnetdialog;
    Bitmap bitmap;
    SignatureView canvasLL;
    File consetfile;
    String ConsentFlag="1";
    String android_id;
    int leaveFlag;
    ArrayList<String>menuID=new ArrayList<>();
    TextView tvNotifcation;
    LinearLayout llNotification;
    ArrayList<PFDocumentModule>docList=new ArrayList<>();
    LinearLayout llPfDocument,llAppointment;
    int scrollCount = 0;
    NotiAdapter notiAdapter;
    android.app.AlertDialog selfresignDialog;
    AlertDialog alerDialog1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_dash_board);
        initialize();
        if (pref.getMsgAlertStatus()){
            msgAlert();
        }else {
            acceptance();
        }
        getPFURL();

        onClick();
    }

    private void initialize() {
        pref = new Pref(EmployeeDashBoardActivity.this);
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        if (android_id.equals("")) {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            android_id = telephonyManager.getDeviceId();
        }else {
            android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }
        if (getIntent().getStringExtra("ConsentFlag")!=null){
            ConsentFlag=getIntent().getStringExtra("ConsentFlag");
        }else {

        }

        fbSpoke=(FloatingActionButton)findViewById(R.id.fbSpoke);
        connectionCheck = new NetworkConnectionCheck(EmployeeDashBoardActivity.this);
        imglogout=(ImageView)findViewById(R.id.imglogout);

        tvEmpName=(TextView)findViewById(R.id.tvEmpName);

        tvEmpName.setText(pref.getEmpName());
        tvNotifcation=(TextView)findViewById(R.id.tvNotifcation);
        llNotification=(LinearLayout)findViewById(R.id.llNotification);


        tvGreeting = (TextView) findViewById(R.id.tvGreeting);
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

       /* if (timeOfDay >= 0 && timeOfDay < 12) {
            tvGreeting.setText("Hi! Good Morning");
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            tvGreeting.setText("Hi! Good Afternoon");
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            tvGreeting.setText("Hi! Good Evening");
        } else if (timeOfDay >= 21 && timeOfDay < 24) {

            tvGreeting.setText("Hi! Good Evening");
        }*/
        tvLoginDateTime = (TextView) findViewById(R.id.tvDate);
        tvLoginDateTime.setText(pref.getloginTime());

        rvItem=(RecyclerView)findViewById(R.id.rvItem);
        rvItem.setLayoutManager(new GridLayoutManager(this, 3));
        new NotiAdapter(EmployeeDashBoardActivity.this, contentList);
        rvNotification = (RecyclerView) findViewById(R.id.rvNotification);
        //rvNotification.setLayoutManager(new LinearLayoutManager(EmployeeDashBoardActivity.this));
        LinearLayoutManager layoutManager = new LinearLayoutManager(EmployeeDashBoardActivity.this) {

            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(EmployeeDashBoardActivity.this) {
                    private static final float SPEED = 3500f;// Change this value (default=25f)

                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return SPEED / displayMetrics.densityDpi;
                    }
                };
                smoothScroller.setTargetPosition(position);
                startSmoothScroll(smoothScroller);
            }
        };
        //  LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvNotification.setLayoutManager(layoutManager);
        rvNotification.setHasFixedSize(true);
        rvNotification.setItemViewCacheSize(1000);
        rvNotification.setDrawingCacheEnabled(true);
        rvNotification.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rvPFDocument=(RecyclerView) findViewById(R.id.rvPFDocument);
        rvPFDocument.setLayoutManager(new LinearLayoutManager(EmployeeDashBoardActivity.this));
        llPfDocument=(LinearLayout)findViewById(R.id.llPfDocument);
        llAppointment=(LinearLayout)findViewById(R.id.llAppointment);
        Date cd = Calendar.getInstance().getTime();
        System.out.println("Current time => " + cd);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = df.format(cd);
        Log.d("formattedDate",formattedDate);
        if (!pref.getPFNotificationURL().equalsIgnoreCase("")){
            shoePFImage();
        }else {

        }


        //
    }




    private void onClick() {

        imglogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeeDashBoardActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        llAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(DocLink));
                startActivity(browserIntent);
            }
        });

        fbSpoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmployeeDashBoardActivity.this, ViewSpokePersonActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("spokepersonarray", spokepersonArray.toString());
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

    public void pfopenBrowser() {

    }





    public void loginFunction() {
        byte[] data = new byte[0];
        try {
            data = pref.getPassword().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String base64 = Base64.encodeToString(data, Base64.DEFAULT).replaceAll("\\s+", "");;


        String surl = AppData.url+"get_GCLAuthenticateWithEncryption?MasterID=" + pref.getMasterId() + "&Password=" + base64 + "&IMEI="+phoneNumber+"&Version=1.0&SecurityCode=" + pref.getSecurityCode() + "&DeviceID=azzzzzz&DeviceType=A";
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
        Log.d("inputLogin", surl);//

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
                                    DocLink = obj.optString("DocLink");
                                }

                                if (responseCode.equals("1")){
                                    llAppointment.setVisibility(View.VISIBLE);
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

                            //getMenutem();

                            JSONObject objMenu=new JSONObject();
                            try {
                                objMenu.put("ConsultantID", pref.getEmpConId());
                                objMenu.put("ClientID",pref.getEmpClintId());
                                objMenu.put("ClientOfficeID",pref.getEmpClintOffId());
                                objMenu.put("AEMEmployeeID",pref.getEmpId());
                                objMenu.put("SecurityCode",pref.getSecurityCode());
                                getMenu(objMenu);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

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

    public void getMenu(JSONObject jsonObject) {
        AndroidNetworking.post(AppData.MENU)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "GET_MENU: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(Response_Data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String MenuID=obj.optString("MenuID");
                                    String MenuItem=obj.optString("MenuItem");
                                    menuID.add(MenuID);
                                    if (menuID.contains("12")){
                                        leaveFlag=1;
                                    }else {
                                        leaveFlag=0;
                                    }
                                    MenuItemModel itemModel=new MenuItemModel(MenuItem,MenuID);
                                    itemList.add(itemModel);
                                }

                                itemList.add(new MenuItemModel("Self Resignation","160"));

                                if (pref.getEmpClintId().equals("AEMCLI1910000054") || pref.getEmpClintId().equals("AEMCLI2010000067") ||pref.getEmpClintId().equals("SECCLI2110000011") ||pref.getEmpClintId().equals("SECCLI2110000012") ){
                                    itemList.add(new MenuItemModel("Survey","200"));

                                }/*else if (pref.getEmpClintId().equals("AEMCLI0910000315")){
                                    itemList.add(new MenuItemModel("Interview","300"));
                                }*/
                                else if (pref.getEmpClintId().equals("AEMCLI2110001671")){
                                    itemList.add(new MenuItemModel("PMS","201"));
                                }else if (pref.getEmpClintId().equals("AEMCLI2310001780")){
                                    itemList.add(new MenuItemModel("Sales Management","4"));
                                }else {
                                    //itemList.add(new MenuItemModel("Leave Management","12"));
                                }

                                MenuItemAdapter menuItemAdapter=new MenuItemAdapter(itemList,getApplicationContext(),PFLink,leaveFlag,EmployeeDashBoardActivity.this);
                                rvItem.setAdapter(menuItemAdapter);

                                getFeedbackChecking();
                                JSONObject object=new JSONObject();
                                //object.put("Id","1");
                                //object.put("SecurityCode","0000");

                                object.put("MasterID",pref.getMasterId());
                                object.put("SecurityCode",pref.getSecurityCode());
                                getNotification(object);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "GET_MENU_error: "+anError.getErrorBody());
                        if (anError.getErrorCode()==401){
                            Toast.makeText(EmployeeDashBoardActivity.this, "Session expired, please login", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EmployeeDashBoardActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
    }


    public void getNotification(JSONObject jsonObject) {
        AndroidNetworking.post(AppData.GetPFNotificationAPI)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "GET_PF_NOTIFICATION: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            if (Response_Code.equals("101")) {
                                JSONObject Response_Data = job1.optJSONObject("Response_Data");
                                JSONArray Content=Response_Data.optJSONArray("Content");
                                JSONObject contentobj=Content.optJSONObject(0);
                                String sContent=contentobj.optString("Content");
                                //tvNotifcation.setText("* "+sContent);
                                contentList = new ArrayList<>();
                                if (Content.length() > 0){
                                    for (int i = 0; i < Content.length(); i++) {
                                        JSONObject conOBJ=Content.optJSONObject(i);
                                        contentList.add(conOBJ.optString("Content"));
                                    }
                                    tvNotifcation.setText(contentList.toString().replace("[","").replace("]","").replaceAll(",","\n\n"));
                                }


                                JSONArray Document=Response_Data.optJSONArray("Document");
                                if (Document.length()>0){
                                    llPfDocument.setVisibility(View.VISIBLE);
                                    for (int i=0;i<Document.length();i++){
                                        JSONObject docOBJ=Document.optJSONObject(i);
                                        String Doc_Info=docOBJ.optString("Doc_Info");
                                        String Doc_Url=docOBJ.optString("Doc_Url");
                                        PFDocumentModule pfmodule=new PFDocumentModule();
                                        pfmodule.setDoc_Info(Doc_Info);
                                        pfmodule.setDoc_Url(Doc_Url);
                                        docList.add(pfmodule);
                                    }

                                    PFDocumentAdapter docAdapter=new PFDocumentAdapter(docList,EmployeeDashBoardActivity.this);
                                    rvPFDocument.setAdapter(docAdapter);

                                     notiAdapter = new NotiAdapter(EmployeeDashBoardActivity.this, contentList);
                                    rvNotification.setAdapter(notiAdapter);
                                }else {
                                    llPfDocument.setVisibility(View.GONE);
                                }

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "GET_MENU_error: "+anError.getErrorBody());
                        if (anError.getErrorCode()==401){
                            Toast.makeText(EmployeeDashBoardActivity.this, "Session expired, please login", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EmployeeDashBoardActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
    }



    public void getFeedbackChecking() {

        String surl = AppData.url+"gel_EmployeeFeedbackStatus?MasterID="+pref.getMasterId()+"&Operation=1&SecurityCode="+pref.getSecurityCode();
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
                        getSpokePersonList();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            responseCode=job1.optString("responseCode");

                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                               // shoeFeedbackPopupDialog();

                            }else {
                                 shoeFeedbackPopupDialog();
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


    public void getSurveyChecking() {

        String surl = AppData.url+"EmployeeSurveyJLL?AEMEmployeeID="+pref.getEmpId()+"SecurityCode="+pref.getSecurityCode();
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
                                survey=true;

                            }else {
                                survey=false;
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

    private void noticeAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EmployeeDashBoardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.popup_dialog, null);
        dialogBuilder.setView(dialogView);





        LinearLayout lnCancel = (LinearLayout) dialogView.findViewById(R.id.lnCancel);

        lnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                al2.dismiss();
            }
        });

        al2 = dialogBuilder.create();
        al2.setCancelable(false);
        Window window = al2.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        al2.show();
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

    private void shoeFeedbackPopupDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EmployeeDashBoardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.feedback_popup, null);
        dialogBuilder.setView(dialogView);
        LinearLayout lnFeedback=(LinearLayout)dialogView.findViewById(R.id.lnFeedback);
        lnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EmployeeDashBoardActivity.this,FeedBackRatingActivity.class);
                startActivity(intent);
                finish();
            }
        });
        LinearLayout lnSkip=(LinearLayout)dialogView.findViewById(R.id.lnSkip);
        lnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedbackpopupDialog.dismiss();
            }
        });


        feedbackpopupDialog = dialogBuilder.create();
        feedbackpopupDialog.setCancelable(true);
        Window window = feedbackpopupDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        feedbackpopupDialog.show();


    }

    private void shoePFAknowledge() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EmployeeDashBoardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.pf_aklowdegement, null);
        dialogBuilder.setView(dialogView);
        TextView tvAcknowledge=(TextView)dialogView.findViewById(R.id.tvAcknowledge);
        tvAcknowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postPFAknowledge();
            }
        });


        aknowledgePopUp = dialogBuilder.create();
        aknowledgePopUp.setCancelable(true);
        Window window = aknowledgePopUp.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        aknowledgePopUp.show();


    }


    private void shoePFImage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EmployeeDashBoardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_pf_image, null);
        dialogBuilder.setView(dialogView);
        ImageView imgCancel=(ImageView)dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pfImageDialog.dismiss();
            }
        });

        ImageView imgPF=(ImageView)dialogView.findViewById(R.id.imgPF);
        try {
            Picasso.with(EmployeeDashBoardActivity.this)

                    .load(pref.getPFNotificationURL())

                    .placeholder(R.drawable.load)
                    .skipMemoryCache()// optional
                    .error(R.drawable.load)
                   // optional
                    .into(imgPF);

        } catch (Exception e) {
            e.printStackTrace();
        }
        pfImageDialog = dialogBuilder.create();
        pfImageDialog.setCancelable(true);
        Window window = pfImageDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        pfImageDialog.show();


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

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void getSpokePersonList(){
        ProgressDialog pd=new ProgressDialog(EmployeeDashBoardActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        String surl = AppData.url+"gcl_GeniusSpocList?ID="+pref.getEmpId()+"&SecurityCode="+pref.getSecurityCode();
        Log.d("input",surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        pd.dismiss();
                        spokepersonArray=new JSONArray();


                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText=job1.optString("responseText");

                            boolean responseStatus=job1.optBoolean("responseStatus");

                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                            JSONArray responseData=job1.optJSONArray("responseData");
                            spokepersonArray=responseData;
                            if (responseData.length()>0) {
                                fbSpoke.setVisibility(View.VISIBLE);
                            }else {
                                fbSpoke.setVisibility(View.GONE);
                            }

                            getPFAknowledgementcheck();




                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();

                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert",error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
    }


    private void getPFAknowledgementcheck(){
        ProgressDialog pd=new ProgressDialog(EmployeeDashBoardActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        String surl = AppData.url+"get_EmployeePFTrustAck?EmployeeID="+pref.getEmpId()+"&Operation=1&SecurityCode="+pref.getSecurityCode();
        Log.d("input",surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        pd.dismiss();



                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText=job1.optString("responseText");

                            boolean responseStatus=job1.optBoolean("responseStatus");
                            if (responseStatus){

                            }else {
                                shoePFAknowledge();
                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();

                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert",error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
    }


    private void postPFAknowledge(){
        ProgressDialog pd=new ProgressDialog(EmployeeDashBoardActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        String surl = AppData.url+"get_EmployeePFTrustAck?EmployeeID="+pref.getEmpId()+"&Operation=2&SecurityCode="+pref.getSecurityCode();
        Log.d("input",surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        pd.dismiss();



                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText=job1.optString("responseText");

                            boolean responseStatus=job1.optBoolean("responseStatus");
                            if (responseStatus){
                                aknowledgePopUp.dismiss();
                            }else {

                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();

                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert",error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
    }


    private void consnetLetter() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EmployeeDashBoardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.consent_signtaure, null);
        dialogBuilder.setView(dialogView);

        TextView tvConsent=(TextView)dialogView.findViewById(R.id.tvConsent);
        tvConsent.setText("I,"+pref.getEmpName()+", hereby submit my Aadhaar and/or my personal documents, information in this portal/site at my own will for employment vis-à-vis   verification purpose \n" +
                "I expressly disclaim any claims arising from representations, whether express or implied, or reliance upon any representations made regarding my documents and/or information supplied to you\n");

        canvasLL = (SignatureView) dialogView.findViewById(R.id.canvasLL);
        Button btnAcknlowedge=(Button)dialogView.findViewById(R.id.btnAcknlowedge);


        btnAcknlowedge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*bitmap = canvasLL.getSignatureBitmap();
                String path = saveImage(bitmap);*/
                consetfile = saveBitMap(EmployeeDashBoardActivity.this, canvasLL);    //which view you want to pass that view as parameter


                if (consetfile != null) {
                    Log.i("TAG", "Drawing saved to the gallery!");


                    //postAcceptance(file);
                    signUpload(tvConsent.getText().toString());
                } else {
                    Log.i("TAG", "Oops! Image could not be saved.");
                    bitmap = canvasLL.getSignatureBitmap();
                    String path = consnetsaveImage(bitmap);
                    signUpload(tvConsent.getText().toString());
                    // Toast.makeText(FeedBackRatingActivity.this, "Oops! Image could not be saved.", Toast.LENGTH_LONG).show();
                }

                consnetdialog.dismiss();



            }
        });


        consnetdialog = dialogBuilder.create();
        consnetdialog.setCancelable(false);
        Window window = consnetdialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        consnetdialog.show();
    }


    public String consnetsaveImage(Bitmap myBitmap) {
        signFlag = 1;
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
            consetfile = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");


            try {
                consetfile.createNewFile();
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
            Log.d("TAG", "File Saved::--->" + consetfile.getAbsolutePath());

            return consetfile.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";

    }


    private File saveBitMap(Context context, View drawView) {
        File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Handcare");
        if (!pictureFileDir.exists()) {
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if (!isDirectoryCreated)
                Log.i("ATG", "Can't create directory to save the image");
            return null;
        }
        String filename = pictureFileDir.getPath() + File.separator + System.currentTimeMillis() + ".jpg";
        File pictureFile = new File(filename);
        Bitmap bitmap = getBitmapFromView(drawView);
        try {
            pictureFile.createNewFile();
            FileOutputStream oStream = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream);
            oStream.flush();
            oStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue saving the image.");
        }
        scanGallery(context, pictureFile.getAbsolutePath());
        return pictureFile;
    }

    //create bitmap from view and returns it
    private Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    // used for scanning gallery
    private void scanGallery(Context cntx, String path) {
        try {
            MediaScannerConnection.scanFile(cntx, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void signUpload(String textdetils) {
        ProgressDialog progressDialog=new ProgressDialog(EmployeeDashBoardActivity.this);
        progressDialog.setMessage("Uploading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.show();
        String masterID=pref.getMasterId();

        //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        AndroidNetworking.upload(AppData.newv2url+"FileUpload/PostConsentLetter")
                .addMultipartParameter("MasterID",masterID)
                .addMultipartParameter("ConsentDetails",textdetils)
                .addMultipartParameter("DeviceID",android_id)
                .addMultipartParameter("IP","00000")
                .addMultipartParameter("DeviceType","A")
                .addMultipartParameter("Operation","1")
                .addMultipartFile("file", consetfile)
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setPercentageThresholdForCancelling(60)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        progressDialog.show();


                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();


                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);


                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101 || Response_Code==100) {
                            consnetdialog.dismiss();

                            Toast.makeText(EmployeeDashBoardActivity.this,"Consent letter has been saved successfully",Toast.LENGTH_LONG).show();


                        }else {
                            Toast.makeText(EmployeeDashBoardActivity.this,"Error Occured Please contact with Administration",Toast.LENGTH_LONG).show();

                        }


                        // boolean _status = job1.getBoolean("status");



                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e("errt", String.valueOf(error));
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Something went wrong,Please try again", Toast.LENGTH_LONG).show();
                    }
                });

    }
    public void resignationReportAlert(String lastWorkingDate,String reason,String approvalStatus) {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(EmployeeDashBoardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_self_resignation_report, null);
        dialogBuilder.setView(dialogView);
        TextView tvLastDate=(TextView)dialogView.findViewById(R.id.tvLastDate);
        TextView tvApprovalStatus=(TextView)dialogView.findViewById(R.id.tvApprovalStatus);
        TextView etReason=(TextView)dialogView.findViewById(R.id.etReason);
        LinearLayout llCancel=(LinearLayout)dialogView.findViewById(R.id.llCancel);
        llCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selfresignDialog.dismiss();
            }
        });
        tvApprovalStatus.setText(approvalStatus);
        tvLastDate.setText(lastWorkingDate);
        etReason.setText(reason);






        selfresignDialog = dialogBuilder.create();
        selfresignDialog.setCancelable(false);
        Window window = selfresignDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        selfresignDialog.show();
    }

    public void resignationAlert() {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(EmployeeDashBoardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_self_resignation, null);
        dialogBuilder.setView(dialogView);
        TextView tvLastDate=(TextView)dialogView.findViewById(R.id.tvLastDate);
        TextView tvNoticePeriod=(TextView)dialogView.findViewById(R.id.tvNoticePeriod);
        TextView tvSubmit=(TextView)dialogView.findViewById(R.id.tvSubmit);
        EditText etReason=(EditText)dialogView.findViewById(R.id.etReason);
        LinearLayout llCancel=(LinearLayout)dialogView.findViewById(R.id.llCancel);
        llCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selfresignDialog.dismiss();
            }
        });

        tvLastDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(tvLastDate,tvNoticePeriod);
            }
        });
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvLastDate.getText().toString().length()>0){
                    if (etReason.getText().toString().length()>0){
                        resignationSubmit(etReason.getText().toString(),Util.changeAnyDateFormat(tvLastDate.getText().toString(),"dd MMM,yyyy","yyyy-MM-dd"));


                    }else {
                        Toast.makeText(EmployeeDashBoardActivity.this,"Please Enter Remarks",Toast.LENGTH_LONG).show();
                    }

                }else {
                    Toast.makeText(EmployeeDashBoardActivity.this,"Please Select Your Last Date",Toast.LENGTH_LONG).show();
                }

            }
        });


        selfresignDialog = dialogBuilder.create();
        selfresignDialog.setCancelable(false);
        Window window = selfresignDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        selfresignDialog.show();
    }


    private void showDatePicker(TextView tv,TextView tvNoticePeriod) {
        // Get current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = (selectedMonth + 1) + "/" + selectedDay + "/" + selectedYear;
                    tv.setText(Util.changeAnyDateFormat(selectedDate,"MM/dd/yyyy","dd MMM,yyyy"));
                    int days=main(selectedDate);
                    tvNoticePeriod.setText(days+" Days");

                },
                year, month, day
        );

        // Disable past dates
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

        // Show dialog
        datePickerDialog.show();
    }


    public static long getDaysDifference(String selectedDateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date currentDate = new Date();
            Date selectedDate = sdf.parse(selectedDateStr);

            long diffInMillis = selectedDate.getTime() - currentDate.getTime();
            return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int main(String selectedDate) {
        int dayscalculation;
        // Example selected date
        long daysBetween = getDaysDifference(selectedDate);
        System.out.println("Days between: " + daysBetween);
        dayscalculation= Math.toIntExact(daysBetween);
        return  dayscalculation+1;

    }


    private void resignationSubmit(String remarks,String lastDate) {
        ProgressDialog progressDialog=new ProgressDialog(EmployeeDashBoardActivity.this);
        progressDialog.setMessage("Uploading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.show();
        String masterID=pref.getMasterId();

        //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        AndroidNetworking.post(AppData.newv2url+"EmployeeExit/SelfResignation")
                .addBodyParameter("MasterID",masterID)
                .addBodyParameter("DBOperation","3")
                .addBodyParameter("LastWorkingDate",lastDate)
                .addBodyParameter("EmpRemarks",remarks)
                .addBodyParameter("SecurityCode", pref.getSecurityCode())
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        progressDialog.show();


                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        selfresignDialog.dismiss();


                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);



                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101 || Response_Code==100) {
                            successAlert("Resignation letter has been submitted successfully");


                            Toast.makeText(EmployeeDashBoardActivity.this,"Resignation letter has been submitted successfully",Toast.LENGTH_LONG).show();


                        }else {
                            Toast.makeText(EmployeeDashBoardActivity.this,"Error Occured Please contact with Administration",Toast.LENGTH_LONG).show();

                        }


                        // boolean _status = job1.getBoolean("status");



                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e("errt", String.valueOf(error));
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Something went wrong,Please try again", Toast.LENGTH_LONG).show();
                    }
                });

    }

    public void resignationget() {
        ProgressDialog progressDialog=new ProgressDialog(EmployeeDashBoardActivity.this);
        progressDialog.setMessage("Uploading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.show();
        String masterID=pref.getMasterId();

        //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        AndroidNetworking.post(AppData.newv2url+"EmployeeExit/SelfResignation")
                .addBodyParameter("MasterID",masterID)
                .addBodyParameter("DBOperation","1")
                .addBodyParameter("SecurityCode", pref.getSecurityCode())
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        progressDialog.show();


                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();



                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);



                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101 || Response_Code==100) {
                            JSONArray Response_Data=job1.optJSONArray("Response_Data");
                            JSONObject dataobj=Response_Data.optJSONObject(0);
                            String LastWorkingDay=dataobj.optString("LastWorkingDay");
                            String EmpRemarks=dataobj.optString("EmpRemarks");
                            String ApprovalStatus=dataobj.optString("ApprovalStatus");

                            resignationReportAlert(LastWorkingDay,EmpRemarks,ApprovalStatus);






                        }else {
                            resignationAlert();

                        }


                        // boolean _status = job1.getBoolean("status");



                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e("errt", String.valueOf(error));
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Something went wrong,Please try again", Toast.LENGTH_LONG).show();
                    }
                });

    }
    private void successAlert(String text) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EmployeeDashBoardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvSuccess = (TextView) dialogView.findViewById(R.id.tvSuccess);

        tvSuccess.setText(text);

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




