package io.cordova.myapp00d753.activity;

import static io.cordova.myapp00d753.activity.EmployeeDashBoardActivity.getDaysDifference;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.attendance.AttenDanceDashboardActivity;
import io.cordova.myapp00d753.adapter.NeedToActAdapter;
import io.cordova.myapp00d753.bluedart.BlueDartAttenDanceDashboardActivity;
import io.cordova.myapp00d753.fragment.ApplicationFragment;
import io.cordova.myapp00d753.fragment.NewHomeFragment;
import io.cordova.myapp00d753.fragment.NewMenuFragment;
import io.cordova.myapp00d753.module.NeedToActModel;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.Util;

public class NewUserDashboardActivity extends AppCompatActivity {
    private static final String TAG = "NewUserDashboardActivit";
    ImageView imgHome,imgMenu,imgLogout;
    LinearLayout llDashboard,llMenu;
    TextView tvEmployeeName;
    Pref pref;
    AlertDialog feedbackpopupDialog,aknowledgePopUp,pfImageDialog;
    JSONArray spokepersonArray;
    String responseCode,DocLink,PFLink;
    android.app.AlertDialog alert3;
    ArrayList<NeedToActModel> needToActModelList = new ArrayList<>();
    LinearLayout chatFabContainer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_new_user_dashboard);
        initView();

        getPFURL();
        getFeedbackChecking();
        if (pref.getUAN_Active().equals("0")){
            Open_UAN_Activation_Popup();
        }
    }

    private void initView() {
        pref = new Pref(this);
        imgHome = findViewById(R.id.imgHome);
        imgMenu = findViewById(R.id.imgMenu);
        imgLogout = findViewById(R.id.imgLogout);
        llDashboard = findViewById(R.id.llDashboard);
        llMenu = findViewById(R.id.llMenu);
        chatFabContainer = findViewById(R.id.chatFabContainer);
        tvEmployeeName = findViewById(R.id.tvEmployeeName);
        tvEmployeeName.setText("Welcome "+pref.getEmpName());
        //loadHomeFragment();
        loadMenuFragment();
        llDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadHomeFragment();
            }
        });
        llMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMenuFragment();
            }
        });
        chatFabContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewUserDashboardActivity.this, ChatBotNewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewUserDashboardActivity.this, NewLoginTwoActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void loadHomeFragment() {
        imgHome.setBackgroundResource(R.drawable.ovaldesign);
        imgHome.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E95154")));
        imgMenu.setBackgroundResource(0);
        imgMenu.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        NewHomeFragment pfragment = new NewHomeFragment();
        transaction.replace(R.id.frameLayout, pfragment);
        transaction.commit();
    }

    public void loadMenuFragment() {
//        imgHome.setBackgroundResource(0);
//        imgHome.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
//        imgMenu.setBackgroundResource(R.drawable.ovaldesign);
//        imgMenu.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E95154")));
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        NewMenuFragment pfragment = new NewMenuFragment();
        transaction.replace(R.id.frameLayout, pfragment);
        transaction.commit();
    }

    public void getFeedbackChecking() {
        String surl = AppData.url+"gel_EmployeeFeedbackStatus?MasterID="+pref.getMasterId()+"&Operation=1&SecurityCode="+pref.getSecurityCode();
        Log.d("inputLogin", surl);
        final ProgressDialog pd=new ProgressDialog(NewUserDashboardActivity.this);
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
                            Toast.makeText(NewUserDashboardActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
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

    private void shoeFeedbackPopupDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NewUserDashboardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.feedback_popup, null);
        dialogBuilder.setView(dialogView);

        LinearLayout lnFeedback=(LinearLayout)dialogView.findViewById(R.id.lnFeedback);
        lnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(NewUserDashboardActivity.this,FeedBackRatingActivity.class);
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
        feedbackpopupDialog.setCancelable(false);
        Window window = feedbackpopupDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        if (!isFinishing() && !isDestroyed()) {
            feedbackpopupDialog.show();
        }
    }

    private void getSpokePersonList(){
        ProgressDialog pd=new ProgressDialog(NewUserDashboardActivity.this);
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
                                //fbSpoke.setVisibility(View.GONE);
                            }else {
                                //fbSpoke.setVisibility(View.GONE);
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
        ProgressDialog pd=new ProgressDialog(NewUserDashboardActivity.this);
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

    private void shoePFAknowledge() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NewUserDashboardActivity.this, R.style.CustomDialogNew);
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

    private void postPFAknowledge(){
        ProgressDialog pd=new ProgressDialog(NewUserDashboardActivity.this);
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
    private void Open_UAN_Activation_Popup() {
        Dialog UAN_Activation_Popup = new Dialog(NewUserDashboardActivity.this,R.style.CustomDialogNew2);
        UAN_Activation_Popup.setContentView(R.layout.uan_activation_dialog);
        UAN_Activation_Popup.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        UAN_Activation_Popup.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ImageView imgCancel = UAN_Activation_Popup.findViewById(R.id.imgCancel);
        Button btnUanActivation = UAN_Activation_Popup.findViewById(R.id.btnUanActivation);
        TextView txtUanBen = UAN_Activation_Popup.findViewById(R.id.txtUanBen);
        Log.e(TAG, "Open_UAN_Activation_Popup: "+pref.getUAN_Mandatory());
        if (pref.getUAN_Mandatory().equals("1")){
            // TODO: 1 - Mandatory, User unable to close popup
            imgCancel.setVisibility(View.GONE);
        } else {
            // TODO: 0 - Not-mandatory, User able to close popup
            imgCancel.setVisibility(View.VISIBLE);
        }
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UAN_Activation_Popup.dismiss();
            }
        });

        btnUanActivation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://unifiedportal-mem.epfindia.gov.in/memberinterface/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        txtUanBen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_uan_activation_benefits_popup();
            }
        });
        UAN_Activation_Popup.show();
    }

    private void open_uan_activation_benefits_popup() {
        Dialog UAN_Activation_Popup = new Dialog(NewUserDashboardActivity.this,R.style.CustomDialogNew2);
        UAN_Activation_Popup.setContentView(R.layout.uan_activation_dialog);
        UAN_Activation_Popup.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        UAN_Activation_Popup.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ImageView imgCancel = UAN_Activation_Popup.findViewById(R.id.imgCancel);
        LinearLayout llButtonLayout = UAN_Activation_Popup.findViewById(R.id.llButtonLayout);
        ImageView imgModal = UAN_Activation_Popup.findViewById(R.id.imgModal);
        imgModal.setImageResource(R.mipmap.post_activation_benefits_2);
        llButtonLayout.setVisibility(View.GONE);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UAN_Activation_Popup.dismiss();
            }
        });

        UAN_Activation_Popup.show();
    }

    public void getPFURL() {
        String surl = AppData.url+"get_PFManagementTripleA?MasterID="+pref.getMasterId()+"&SecurityCode="+pref.getSecurityCode();
        Log.d("inputLogin", surl);

        final ProgressDialog pd=new ProgressDialog(NewUserDashboardActivity.this);
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
                            Toast.makeText(NewUserDashboardActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
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
}
