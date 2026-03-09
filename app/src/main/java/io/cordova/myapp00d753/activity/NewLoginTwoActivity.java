package io.cordova.myapp00d753.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.fragment.ApplicationFragment;
import io.cordova.myapp00d753.fragment.LoginCredentialsFragment;
import io.cordova.myapp00d753.fragment.LoginWithEPINFragment;
import io.cordova.myapp00d753.fragment.LoginWithMobileOtpFragment;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.Util;

public class NewLoginTwoActivity extends AppCompatActivity {

    TextView tvSignIn;
    EditText etUserId, etPassword, etForgotUserId;
    String userId, password;
    LinearLayout llSignIn;
    NetworkConnectionCheck connectionCheck;
    AlertDialog alertDialog, al1, al2;
    Pref pref;
    String UserType;
    String refreshedToken;
    EditText etSecurityCode;
    String AEMEmployeeID;
    String version;
    CheckBox ckRemember;
    ImageView imgVisible, imginVisible;
    LinearLayout llLoader;
    TextView tvForgotPass;
    AlertDialog alert1, popUp, alerDialog1;
    String security_code = "0000";
    TextView llForgotPassword;

    ImageView refreshButton;
    EditText etCaptcha;
    Button submitButton;
    // CaptchaImageView captchaImageView;
    String phoneNumber = "0000";
    LinearLayout llWorkForce, llSupervisior, llSecurityCode, llTEMP;
    ImageView imgWorkForce, imgSupTick, imgTEMP;
    public static String SECRET_KEY = "74074750353890398886017484399862";
    String ConsentFlag;
    int WorkingStatus;
    String ip, sessionId;
    LinearLayout llEmp,llMobile,llEmpLogin,llMainOption,llLC,llLM,llEP,llLoginCredentials;
    ImageView imgEmpCheck,imgMobileCheck,LCTick,LMTick,EPTick,iconSelfService,iconPaperlessOnboarding;
    TextView txtSelfService,txtPaperlessOnboarding;
    LinearLayout clSelfService,clPaperlessOnboarding;
    FrameLayout fragmentContainer;
    String FROM="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_login_three);

        getMaintanceBreak();
    }


    private void initialize() {
        if (getIntent() != null){
            FROM = getIntent().getStringExtra("from");
        }
        llSignIn = (LinearLayout) findViewById(R.id.llSignIn);
        etUserId = (EditText) findViewById(R.id.etUserId);
        etPassword = (EditText) findViewById(R.id.etPassword);
        connectionCheck = new NetworkConnectionCheck(this);
        pref = new Pref(NewLoginTwoActivity.this);
        refreshedToken = getAndroidID(NewLoginTwoActivity.this);
        iconSelfService = findViewById(R.id.iconSelfService);
        clPaperlessOnboarding = findViewById(R.id.clPaperlessOnboarding);
        clSelfService = findViewById(R.id.clSelfService);
        txtSelfService = findViewById(R.id.txtSelfService);
        txtPaperlessOnboarding = findViewById(R.id.txtPaperlessOnboarding);
        //iconSelfService = findViewById(R.id.iconSelfService);
        iconPaperlessOnboarding = findViewById(R.id.iconPaperlessOnboarding);
        llMainOption = findViewById(R.id.llMainOption);
        llLoginCredentials = findViewById(R.id.llLoginCredentials);
        fragmentContainer = findViewById(R.id.fragmentContainer);
//        Log.d("token",refreshedToken);
        etSecurityCode = (EditText) findViewById(R.id.etSecuritycode);
        try {
            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
            int verCode = pInfo.versionCode;
            Log.d("sddk", version);
            Log.d("sdkl", String.valueOf(verCode));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        ckRemember = (CheckBox) findViewById(R.id.ckRemember);

        /*if (pref.getCheckFlag().equals("1")) {
            ckRemember.setChecked(true);
            etUserId.setText(pref.getUserLoginId());
            etPassword.setText(pref.getPassword());
        }

        if (pref.getCheckFlag().equals("2")) {
            ckRemember.setChecked(false);
            etUserId.setText("");
            etPassword.setText("");
        }*/

        imgVisible = (ImageView) findViewById(R.id.imgVisible);
        imginVisible = (ImageView) findViewById(R.id.imginVisible);
        llLoader = (LinearLayout) findViewById(R.id.llLoader);
        llForgotPassword = (TextView) findViewById(R.id.llForgotPassword);

        llWorkForce = (LinearLayout) findViewById(R.id.llWorkForce);
        llSupervisior = (LinearLayout) findViewById(R.id.llSupervisior);
        llSecurityCode = (LinearLayout) findViewById(R.id.llSecurityCode);
        llTEMP = (LinearLayout) findViewById(R.id.llTEMP);
        llLC = (LinearLayout) findViewById(R.id.llLC);
        llLM = (LinearLayout) findViewById(R.id.llLM);
        llEP = (LinearLayout) findViewById(R.id.llEP);

        imgSupTick = (ImageView) findViewById(R.id.imgSupTick);
        imgWorkForce = (ImageView) findViewById(R.id.imgWorkForce);
        imgTEMP = (ImageView) findViewById(R.id.imgTEMP);
        LCTick = (ImageView) findViewById(R.id.LCTick);
        LMTick = (ImageView) findViewById(R.id.LMTick);
        EPTick = (ImageView) findViewById(R.id.EPTick);

        ip = getIPAddress(true);
        sessionId = generateSessionID("Staffing_Mobile");

        llEmp=(LinearLayout)findViewById(R.id.llEmp);
        llMobile=(LinearLayout)findViewById(R.id.llMobile);
        llEmpLogin=(LinearLayout)findViewById(R.id.llEmpLogin);

        imgEmpCheck=findViewById(R.id.imgEmpCheck);
        imgMobileCheck=findViewById(R.id.imgMobileCheck);

       /* refreshButton= (ImageView) findViewById(R.id.regen);
        etCaptcha= (EditText) findViewById(R.id.etCaptcha);
        captchaImageView= (CaptchaImageView) findViewById(R.id.captchaimage);
        captchaImageView.setCaptchaType(CaptchaImageView.CaptchaGenerator.BOTH);
*/
        if (FROM != null && FROM.equals("MPIN_setup")){
            loadLoginWithEPIN();
        } else {
            loadLoginWithCredentials();
        }

        if (pref.getLoginType().equalsIgnoreCase("cred")){
            loadLoginWithCredentials();
        }else {
            loadLoginWithEPIN();
        }
        onClick();

    }

    private void onClick() {
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_zoom_out);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        //clSelfService.setBackgroundResource(R.drawable.transparent_background);
       // clPaperlessOnboarding.setBackgroundResource(R.drawable.background_6);


        clSelfService.setOnClickListener(view -> {
            txtSelfService.setTextColor(getApplicationContext().getResources().getColorStateList(R.color.white));
            txtPaperlessOnboarding.setTextColor(getApplicationContext().getResources().getColorStateList(R.color.grey));
            iconSelfService.setImageResource(R.drawable.self_service_white_icon);
            iconPaperlessOnboarding.setImageResource(R.drawable.paperless_1);
            clSelfService.setBackgroundResource(R.drawable.button_background_2);
            clPaperlessOnboarding.setBackgroundResource(0);
            llEP.setVisibility(View.VISIBLE);
            llLM.setVisibility(View.GONE);
        });
        clPaperlessOnboarding.setOnClickListener(view -> {
            txtSelfService.setTextColor(getApplicationContext().getResources().getColorStateList(R.color.grey));
            txtPaperlessOnboarding.setTextColor(getApplicationContext().getResources().getColorStateList(R.color.white));
            iconSelfService.setImageResource(R.drawable.self_service);
            iconPaperlessOnboarding.setImageResource(R.drawable.paperless_whicte_icon);
            clPaperlessOnboarding.setBackgroundResource(R.drawable.button_background_2);
            clSelfService.setBackgroundResource(0);
            llEP.setVisibility(View.GONE);
            llLM.setVisibility(View.VISIBLE);
            loadLoginWithCredentials();
        });

        llLC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadLoginWithCredentials();
                //llLoginCredentials.setVisibility(View.VISIBLE);
            }
        });

        llLM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadLoginWithMobileOTP();
            }
        });

        llEP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadLoginWithEPIN();
            }
        });

        /*Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        imgSelfServiceIcon.startAnimation(fadeOut);
        imgPaperlessOnboardingIcon.startAnimation(fadeOut);
        imgSelfServiceIcon.setVisibility(View.GONE);
        imgPaperlessOnboardingIcon.setVisibility(View.GONE);*/
       /* refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captchaImageView.regenerate();
            }
        });*/

        /*llEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llEmpLogin.getVisibility()==View.GONE){
                    llEmpLogin.setVisibility(View.VISIBLE);
                    imgEmpCheck.setVisibility(View.VISIBLE);
                    imgMobileCheck.setVisibility(View.GONE);
                }else {
                    llEmpLogin.setVisibility(View.GONE);
                    imgEmpCheck.setVisibility(View.GONE);
                    imgMobileCheck.setVisibility(View.GONE);
                }
            }
        });

        llMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgMobileCheck.getVisibility()==View.GONE){
                    llEmpLogin.setVisibility(View.GONE);
                    imgEmpCheck.setVisibility(View.GONE);
                    imgMobileCheck.setVisibility(View.VISIBLE);
                }else {
                    llEmpLogin.setVisibility(View.GONE);
                    imgEmpCheck.setVisibility(View.GONE);
                    imgMobileCheck.setVisibility(View.GONE);
                }
            }
        });

        llWorkForce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgWorkForce.setVisibility(View.VISIBLE);
                imgSupTick.setVisibility(View.GONE);
                imgTEMP.setVisibility(View.GONE);
                llSecurityCode.setVisibility(View.GONE);
            }
        });


        llSupervisior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgWorkForce.setVisibility(View.GONE);
                imgSupTick.setVisibility(View.VISIBLE);
                imgTEMP.setVisibility(View.GONE);
                llSecurityCode.setVisibility(View.VISIBLE);
            }
        });


        llTEMP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgWorkForce.setVisibility(View.GONE);
                imgSupTick.setVisibility(View.GONE);
                imgTEMP.setVisibility(View.VISIBLE);
                llSecurityCode.setVisibility(View.VISIBLE);
            }
        });*/
       /* etUserId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etUserId.getText().toString().contains("TEMP") || etUserId.getText().toString().contains("temp") || etUserId.getText().toString().contains("GCL") || etUserId.getText().toString().contains("gcl")) {
                    etPassword.setText("password");
                } else {
                    etPassword.setText("");
                }
                if (etUserId.getText().toString().contains("AEM") || etUserId.getText().toString().contains("FMS") || etUserId.getText().toString().contains("ITS") || etUserId.getText().toString().contains("SEC") || etUserId.getText().toString().contains("NAPS") || etUserId.getText().toString().contains("GMSP") || etUserId.getText().toString().contains("FSS") || etUserId.getText().toString().contains("NPS")) {
                    llSecurityCode.setVisibility(View.GONE);
                } else if (etUserId.getText().toString().isEmpty()) {
                    llSecurityCode.setVisibility(View.GONE);
                } else {
                    llSecurityCode.setVisibility(View.VISIBLE);
                }
            }
        });
        llSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etUserId.getText().toString().length() > 0) {
                    if (etPassword.getText().toString().length() > 0) {
                        if (connectionCheck.isNetworkAvailable()) {
                            //  if(etCaptcha.getText().toString().equals(captchaImageView.getCaptchaCode())){
                            //loginFunction();
                            if (etUserId.getText().toString().contains("AEM")) {
                                security_code = "0000";
                            } else if (etUserId.getText().toString().contains("FMS")) {
                                security_code = "222";
                            } else if (etUserId.getText().toString().contains("ITS")) {
                                security_code = "888";
                            } else if (etUserId.getText().toString().contains("SEC")) {
                                security_code = "333";
                            } else if (etUserId.getText().toString().contains("NAPS")) {
                                security_code = "444";
                            } else if (etUserId.getText().toString().contains("NPS")) {
                                security_code = "444";
                            } else if (etUserId.getText().toString().contains("GMSP")) {
                                security_code = "666";
                            } else if (etUserId.getText().toString().contains("MSP")) {
                                security_code = "666";
                            } else if (etUserId.getText().toString().contains("FSS")) {
                                security_code = "0000";
                            } else {
                                if (etSecurityCode.getText().toString().length() > 0) {
                                    security_code = etSecurityCode.getText().toString();
                                } else {
                                    security_code = "0000";
                                }
                            }
                            JSONObject obj = new JSONObject();
                            try {
                                obj.put("MasterID", Util.encrypt(etUserId.getText().toString(), SECRET_KEY));
                                obj.put("Password", Util.encrypt(etPassword.getText().toString(), SECRET_KEY));
                                obj.put("IPAddress", ip);
                                obj.put("AppSessionID", sessionId);
                                obj.put("UUID", refreshedToken);
                                obj.put("GUID", refreshedToken);
                                obj.put("MachineDetails", "GeniusStaffing_Android");
                                obj.put("SecurityCode", security_code);
                                loginv2(obj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Date d = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                            String currentDateTimeString = sdf.format(d);
                            Log.d("ctime", currentDateTimeString);
                            pref.saveCtime(currentDateTimeString);
                          *//*  }else{
                                Toast.makeText(LoginActivity.this, "Not Matching", Toast.LENGTH_SHORT).show();
                            }*//*
//

                        } else {
                            connectionCheck.getNetworkActiveAlert().show();
                        }


                    } else {
                        etPassword.setError("Please enter your Password");
                        etPassword.requestFocus();
                    }

                } else {
                    etUserId.setError("Please enter your User ID");
                    etUserId.requestFocus();
                }
            }
        });

        ckRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    pref.saveCheckFlag("1");
                } else {
                    pref.saveCheckFlag("2");
                }
            }
        });

        imgVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imginVisible.setVisibility(View.VISIBLE);
                imgVisible.setVisibility(View.GONE);
                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

            }
        });

        imginVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgVisible.setVisibility(View.VISIBLE);
                imginVisible.setVisibility(View.GONE);
                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        });
        llForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showForgotPasswordDialouge();
            }
        });*/
    }


    private void shoeDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NewLoginTwoActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_invalidcredential, null);
        dialogBuilder.setView(dialogView);
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        Window window = alertDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog.show();


    }

    private void shoeDialogDynamicMsg(String msg) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NewLoginTwoActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_invalidcredential, null);
        dialogBuilder.setView(dialogView);
        TextView tvTitle=dialogView.findViewById(R.id.tvTitle);
        tvTitle.setText(msg);
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        Window window = alertDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog.show();


    }

    private void showAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("somthing went wrong");
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                    }
                });
    }

    private void showEmpDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NewLoginTwoActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_empid, null);
        dialogBuilder.setView(dialogView);
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                al1.dismiss();
            }
        });
        al1 = dialogBuilder.create();
        al1.setCancelable(true);
        Window window = al1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        al1.show();
    }

    private void showInternetDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NewLoginTwoActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_invaliddate, null);
        dialogBuilder.setView(dialogView);
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert1.dismiss();
            }
        });
        TextView tvInvalidDialog = (TextView) dialogView.findViewById(R.id.tvInvalidDialog);
        tvInvalidDialog.setText("Something went wrong.Please try again");
        alert1 = dialogBuilder.create();
        alert1.setCancelable(true);
        Window window = alert1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alert1.show();
    }


    private void forgotpassworddialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NewLoginTwoActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_forgot_password, null);
        dialogBuilder.setView(dialogView);
        EditText etUserId = (EditText) dialogView.findViewById(R.id.etUserId);
        EditText etSecurityCode = (EditText) dialogView.findViewById(R.id.etSecuritycode);
        LinearLayout llSubmit = (LinearLayout) dialogView.findViewById(R.id.llSubmit);
        llSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                al2.dismiss();
            }
        });

        al2 = dialogBuilder.create();
        al2.setCancelable(true);
        Window window = al2.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        al2.show();


    }

    private void showForgotPasswordDialouge() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NewLoginTwoActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.fotgotpassworddialog, null);
        dialogBuilder.setView(dialogView);
        ImageView imgClose = (ImageView) dialogView.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp.dismiss();
            }
        });
        etForgotUserId = (EditText) dialogView.findViewById(R.id.etForgotUserId);
        final EditText etSecurityCode = (EditText) dialogView.findViewById(R.id.etSecurityCode);
        final Button btnSubmit = (Button) dialogView.findViewById(R.id.btnSubmit);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etForgotUserId.getText().toString().length() > 0) {

                    changePassword();

                    popUp.dismiss();
                } else {
                    Toast.makeText(NewLoginTwoActivity.this, "Please enter userid ", Toast.LENGTH_LONG).show();
                }

            }
        });
        popUp = dialogBuilder.create();
        popUp.setCancelable(false);
        Window window = popUp.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        popUp.show();
    }

    private void changePassword() {
        ProgressDialog pd = new ProgressDialog(NewLoginTwoActivity.this);
        pd.setMessage("Loading");
        pd.show();
        pd.setCancelable(false);
        String surl = " https://gsppi.geniusconsultant.com/GENESS/Account/RetrievePassword?UserID=" + etForgotUserId.getText().toString();
        Log.d("inputLogin", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseChangePassword", response);
                        pd.dismiss();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            int isSuccess = job1.optInt("isSuccess");
                            if (isSuccess == 1) {
                                successAlert("Password sent to your registered email id");
                            } else {

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(NewLoginTwoActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(NewLoginTwoActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                //showAlert();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(NewLoginTwoActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void successAlert(String msg) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NewLoginTwoActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);

        tvInvalidDate.setText(msg);

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

    private void getMaintanceBreak() {
        final ProgressDialog pd = new ProgressDialog(NewLoginTwoActivity.this);
        pd.setMessage("Loading.....");
        pd.show();
        AndroidNetworking.get(AppData.MAINTAINCEBREAK)
                .addHeaders("SecurityKey", "gStbCQYjYBDCQ4fkGoQSUj7LYe8uVdZ1")
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        pd.dismiss();
                        JSONObject job = response;
                        boolean IsEnabled = job.optBoolean("IsEnabled");
                        String Message = job.optString("Message");
                        if (IsEnabled) {
                            Intent intent=new Intent(NewLoginTwoActivity.this,MaintainceBreakActivity.class);
                            intent.putExtra("breakText",Message);
                            startActivity(intent);
                            finish();


                        } else {
                            initialize();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        pd.dismiss();


                    }
                });
    }

    private String getAndroidID(Context context) {
        return Settings.Secure.getString(
                context.getContentResolver(),
                Settings.Secure.ANDROID_ID
        );
    }

    private void loginv2(JSONObject jsonObject) {
        Log.e("LOGIN", "login: " + jsonObject.toString());
        final ProgressDialog pd = new ProgressDialog(NewLoginTwoActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(AppData.newv2url + "Login/EssLogin")
                .addJSONObjectBody(jsonObject)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject job1 = response;
                        Log.e("LOGIN", "@@@@@@" + job1);
                        pd.dismiss();

                        String Response_Code = job1.optString("Response_Code");
                        String Response_Message = job1.optString("Response_Message");

                        if (Response_Code.equals("1")) {
                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                            pref.saveUserLoginID(etUserId.getText().toString().trim());
                            Log.e("UserLoginId", "UserLoginId: " + pref.getUserLoginId());

                            JSONArray responseData = job1.optJSONArray("Response_Data");
                            try {

                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    AEMEmployeeID = obj.optString("UserID");


                                    String Name = obj.optString("UserName");
                                    pref.saveEmpName(Name);
                                    String AEMConsultantID = obj.optString("AEMConsultantID");
                                    pref.saveEmpConId(AEMConsultantID);

                                    String AEMClientID = obj.optString("ClientID");
                                    pref.saveEmpClintId(AEMClientID);


                                    UserType = obj.optString("UserType");
                                    pref.saveUserType(UserType);

                                    String Password = obj.optString("Password");
                                    pref.savePassword(etPassword.getText().toString());

                                    WorkingStatus = obj.optInt("WorkingStatus");

                                    String Genius_Access_Token = obj.optString("Genius_Access_Token").trim();
                                    pref.saveAccessToken(Genius_Access_Token);

                                    if (WorkingStatus == 1) {
                                        if (UserType.equals("1")  || UserType.equals("3")){
                                            JSONObject obj1 = new JSONObject();
                                            try {
                                                obj1.put("MasterID", etUserId.getText().toString());
                                                obj1.put("SecurityCode", security_code);
                                                logindetails(obj1);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }else {
                                            Intent intent = new Intent(NewLoginTwoActivity.this, TempDashBoardActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.putExtra("ConsentFlag", ConsentFlag);
                                            startActivity(intent);
                                            finish();
                                        }


                                    } else {
                                        //re direct to resign page
                                        Intent intent = new Intent(NewLoginTwoActivity.this, ResignEmployeeDashboardActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }


                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        } else {
                            shoeDialogDynamicMsg(Response_Message);


                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e("LOGIN", "onError: " + error);
                        pd.dismiss();


                    }
                });
    }


    private void logindetails(JSONObject jsonObject) {
        Log.e("LOGIN", "login: " + jsonObject.toString());
        final ProgressDialog pd = new ProgressDialog(NewLoginTwoActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(AppData.newv2url + "Login/UseMenuDetails")
                .addJSONObjectBody(jsonObject)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject job1 = response;
                        Log.e("LOGIN", "@@@@@@" + job1);
                        pd.dismiss();

                        String Response_Code = job1.optString("Response_Code");
                        String Response_Message = job1.optString("Response_Message");

                        if (Response_Code.equals("1")) {
                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                            pref.saveUserLoginID(etUserId.getText().toString().trim());
                            Log.e("UserLoginId", "UserLoginId: " + pref.getUserLoginId());

                            JSONArray responseData = job1.optJSONArray("Response_Data");
                            try {

                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);


                                    String LoginDateTime = obj.optString("LoginDateTime");
                                    pref.saveloginTime(LoginDateTime);

                                    String FlagMenu = obj.optString("FlagMenu");
                                    pref.saveMenu(FlagMenu);
                                    String AEMClientOfficeID = obj.optString("AEMClientOfficeID");
                                    pref.saveEmpClintOffId(AEMClientOfficeID);
                                    String MasterID = obj.optString("MasterID");
                                    pref.saveMasterId(MasterID);
                                    String CTCUrl = obj.optString("CTCUrl");
                                    pref.saveCTCURL(CTCUrl);
                                    String LeaveApply = obj.optString("LeaveApply");
                                    pref.saveOnLeave(LeaveApply);
                                    String AttdImage = obj.optString("AttdImage");
                                    pref.saveAttdImg(AttdImage);

                                    String BackAttd = obj.optString("BackDateAttendance");
                                    pref.saveBackAttd(BackAttd);
                                    pref.saveSecurityCode(security_code);

                                    String FlagAddr = obj.optString("FlagAddr");
                                    pref.saveFlagLocation(FlagAddr);
                                    boolean AppRenameFlag = obj.optBoolean("AppRenameFlag");
                                    pref.saveMsgAlertStatus(AppRenameFlag);
                                    pref.saveMsgAlertStatus(AppRenameFlag);
                                    String AppRenameText = obj.optString("AppRenameText");
                                    pref.saveMsg(AppRenameText);
                                    pref.saveMsg(AppRenameText);
                                    String Leave = obj.optString("Leave");
                                    pref.saveShiftFlag(Leave);
                                    String PF_Notify_URL = obj.optString("PF_Notify_URL");
                                    pref.savePFNotificationURL(PF_Notify_URL);
                                    ConsentFlag = obj.optString("ConsentFlag");
                                    String UAN_Active = obj.optString("UAN_Active");
                                    pref.saveUAN_Active(UAN_Active);
                                    String UAN_Mandatory = obj.optString("UAN_Mandatory");
                                    pref.saveUAN_Mandatory(UAN_Mandatory);
                                    String Adjustment_Status = obj.optString("Adjustment");
                                    pref.saveAdjustmentStatus(Adjustment_Status);

                                    String EmployeeID= obj.optString("EmployeeID");
                                    pref.saveEmpId(EmployeeID);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (UserType.equals("1")) {
                                if (etPassword.getText().toString().equalsIgnoreCase("password")) {
                                    Intent intent = new Intent(NewLoginTwoActivity.this, ChangePasswordActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {

                                    Intent intent = new Intent(NewLoginTwoActivity.this, EmployeeDashBoardActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("ConsentFlag", ConsentFlag);
                                    startActivity(intent);
                                    finish();

                                }
                            } else if (UserType.equals("3")) {
                                Intent intent = new Intent(NewLoginTwoActivity.this, SuperVisiorDashBoardActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else if (UserType.equals("2")) {


                                Intent intent = new Intent(NewLoginTwoActivity.this, TempDashBoardActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("ConsentFlag", ConsentFlag);
                                startActivity(intent);
                                finish();

                            }


                        } else {
                            shoeDialogDynamicMsg(Response_Message);


                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e("LOGIN", "onError: " + error);
                        pd.dismiss();


                    }
                });
    }

    public String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int index = sAddr.indexOf('%');
                                return index < 0 ? sAddr.toUpperCase() : sAddr.substring(0, index).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
        }
        return "";
    }

    public static String generateSessionID(String userId) {
        return userId + "_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString();
    }

    public void loadLoginWithCredentials(){
        LCTick.setImageResource(R.drawable.check_mark_2);
        llLC.setBackgroundResource(R.drawable.border_background_2);
        LMTick.setImageResource(R.drawable.radio_button);
        llLM.setBackgroundResource(R.drawable.border_background_3);
        EPTick.setImageResource(R.drawable.radio_button);
        llEP.setBackgroundResource(R.drawable.border_background_3);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        LoginCredentialsFragment pfragment = new LoginCredentialsFragment(); // for Metso Only
        transaction.replace(R.id.fragmentContainer, pfragment);
        transaction.commit();
        fragmentContainer.setVisibility(View.VISIBLE);
    }

    public void loadLoginWithMobileOTP(){
        LMTick.setImageResource(R.drawable.check_mark_2);
        llLM.setBackgroundResource(R.drawable.border_background_2);
        LCTick.setImageResource(R.drawable.radio_button);
        llLC.setBackgroundResource(R.drawable.border_background_3);
        EPTick.setImageResource(R.drawable.radio_button);
        llEP.setBackgroundResource(R.drawable.border_background_3);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        LoginWithMobileOtpFragment pfragment = new LoginWithMobileOtpFragment(); // for Metso Only
        transaction.replace(R.id.fragmentContainer, pfragment);
        transaction.commit();
        fragmentContainer.setVisibility(View.VISIBLE);
    }

    public void loadLoginWithEPIN(){
        EPTick.setImageResource(R.drawable.check_mark_2);
        llEP.setBackgroundResource(R.drawable.border_background_2);
        LMTick.setImageResource(R.drawable.radio_button);
        llLM.setBackgroundResource(R.drawable.border_background_3);
        LCTick.setImageResource(R.drawable.radio_button);
        llLC.setBackgroundResource(R.drawable.border_background_3);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        LoginWithEPINFragment pfragment = new LoginWithEPINFragment();
        transaction.replace(R.id.fragmentContainer, pfragment);
        transaction.commit();
        fragmentContainer.setVisibility(View.VISIBLE);
    }
}