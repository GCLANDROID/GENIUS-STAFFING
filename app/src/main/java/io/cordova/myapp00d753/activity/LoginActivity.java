package io.cordova.myapp00d753.activity;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;
import io.cordova.myapp00d753.utility.Pref;


public class LoginActivity extends AppCompatActivity {
    TextView tvSignIn;
    EditText etUserId, etPassword;
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
    String phoneNumber="0000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();

        onClick();
    }

    private void initialize() {
        llSignIn = (LinearLayout) findViewById(R.id.llSignIn);

        etUserId = (EditText) findViewById(R.id.etUserId);
        etPassword = (EditText) findViewById(R.id.etPassword);
        connectionCheck = new NetworkConnectionCheck(this);
        pref = new Pref(LoginActivity.this);
        refreshedToken = "1123444";
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

        if (pref.getCheckFlag().equals("1")) {
            ckRemember.setChecked(true);
            etUserId.setText(pref.getMasterId());
            etPassword.setText(pref.getPassword());

        }

        if (pref.getCheckFlag().equals("2")) {
            ckRemember.setChecked(false);
            etUserId.setText("");
            etPassword.setText("");
        }

        imgVisible = (ImageView) findViewById(R.id.imgVisible);
        imginVisible = (ImageView) findViewById(R.id.imginVisible);
        llLoader = (LinearLayout) findViewById(R.id.llLoader);
        llForgotPassword = (TextView) findViewById(R.id.llForgotPassword);

       /* refreshButton= (ImageView) findViewById(R.id.regen);
        etCaptcha= (EditText) findViewById(R.id.etCaptcha);
        captchaImageView= (CaptchaImageView) findViewById(R.id.captchaimage);
        captchaImageView.setCaptchaType(CaptchaImageView.CaptchaGenerator.BOTH);
*/






        }

    private void onClick() {
       /* refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captchaImageView.regenerate();
            }
        });*/
        etUserId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etUserId.getText().toString().contains("TEMP") || etUserId.getText().toString().contains("temp")) {
                    etPassword.setText("password");
                } else {
                    etPassword.setText("");
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
                                loginFunction();
                                Date d = new Date();
                                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                                String currentDateTimeString = sdf.format(d);
                                Log.d("ctime", currentDateTimeString);
                                pref.saveCtime(currentDateTimeString);
                          /*  }else{
                                Toast.makeText(LoginActivity.this, "Not Matching", Toast.LENGTH_SHORT).show();
                            }*/
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
        });

    }

    public void loginFunction() {
        byte[] data = new byte[0];
        try {
            data = etPassword.getText().toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String base64 = Base64.encodeToString(data, Base64.DEFAULT).replaceAll("\\s+", "");;


        String surl = AppData.url+"get_GCLAuthenticateWithEncryption?MasterID=" + etUserId.getText().toString() + "&Password=" + base64 + "&IMEI="+phoneNumber+"&Version=" + version + "&SecurityCode=" + etSecurityCode.getText().toString() + "&DeviceID=" + refreshedToken + "&DeviceType=A";
        Log.d("inputLogin", surl);

        final ProgressDialog progressDialog=new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        progressDialog.dismiss();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    AEMEmployeeID = obj.optString("AEMEmployeeID");
                                    pref.saveEmpId(AEMEmployeeID);
                                    Log.d("aemp", pref.getEmpId());
                                    String Name = obj.optString("Name");
                                    pref.saveEmpName(Name);
                                    String LoginDateTime = obj.optString("LoginDateTime");
                                    pref.saveloginTime(LoginDateTime);
                                    String FlagMenu = obj.optString("FlagMenu");
                                    pref.saveMenu(FlagMenu);
                                    Log.d("menud", pref.getMenu());
                                    String AEMConsultantID = obj.optString("AEMConsultantID");
                                    pref.saveEmpConId(AEMConsultantID);
                                    String AEMClientID = obj.optString("AEMClientID");
                                    pref.saveEmpClintId(AEMClientID);
                                    String AEMClientOfficeID = obj.optString("AEMClientOfficeID");
                                    pref.saveEmpClintOffId(AEMClientOfficeID);
                                    String MasterID = obj.optString("MasterID");
                                    pref.saveMasterId(etUserId.getText().toString());
                                    Log.d("Master", MasterID);
                                    UserType = obj.optString("UserType");
                                    pref.saveUserType(UserType);
                                    String CTCUrl = obj.optString("CTCUrl");
                                    pref.saveCTCURL(CTCUrl);
                                    String WeeklyOff = obj.optString("WeeklyOff");
                                    pref.saveWeeklyoff(WeeklyOff);
                                    String LeaveApply = obj.optString("LeaveApply");
                                    pref.saveOnLeave(LeaveApply);
                                    String LeaveUrl = obj.optString("LeaveUrl");
                                    pref.saveLeaveUrl(LeaveUrl);
                                    String AttdImage = obj.optString("AttdImage");
                                    pref.saveAttdImg(AttdImage);
                                    String BackAttd = obj.optString("BackAttd");
                                    pref.saveBackAttd(BackAttd);
                                    String IsSupervisor = obj.optString("IsSupervisor");
                                    pref.saveSup(IsSupervisor);
                                    String CompanyName = obj.optString("CompanyName");
                                    pref.saveSecurityCode(CompanyName);
                                    String FlagAddr = obj.optString("FlagAddr");
                                    pref.saveFlagLocation(FlagAddr);
                                    String Password = obj.optString("Password");
                                    pref.savePassword(etPassword.getText().toString());
                                    String OffAttFlag=obj.optString("OffAttFlag");
                                    pref.saveOffAttnFlag(OffAttFlag);
                                    if (pref.getCheckFlag().equals("1")){
                                        pref.saveIntentFlag("1");

                                    }

                                    String  DemoFlag=obj.optString("DemoFlag");
                                    pref.saveDemoFlag(DemoFlag);
                                    String GeoConfFlag=obj.optString("GeoConfFlag");
                                    pref.saveFenceConfigFlag(GeoConfFlag);
                                    String GeoFenceMenuFlag=obj.optString("GeoFenceMenuFlag");
                                    pref.saveFenceMenuFlag(GeoFenceMenuFlag);
                                    String GeoFenceAttFlag=obj.optString("GeoFenceAttFlag");
                                    pref.saveFenceAttnFlag(GeoFenceAttFlag);
                                    boolean AppRenameFlag=obj.optBoolean("AppRenameFlag");
                                    String AppRenameText=obj.optString("AppRenameText");

                                    pref.saveMsgAlertStatus(AppRenameFlag);
                                    pref.saveMsg(AppRenameText);
                                    String PFConsolidateURL=obj.optString("PFConsolidateURL");
                                    pref.savePFURL(PFConsolidateURL);
                                    String Leave=obj.optString("Leave");
                                    pref.saveShiftFlag(Leave);
                                    pref.saveEmpClintId(AEMClientID);
                                    pref.saveMsgAlertStatus(AppRenameFlag);
                                    pref.saveMsg(AppRenameText);
                                    pref.savePFURL(PFConsolidateURL);




                                }
                                if (UserType.equals("1")) {
                                    if (etPassword.getText().toString().equalsIgnoreCase("password")){
                                        Intent intent = new Intent(LoginActivity.this, ChangePasswordActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        Intent intent = new Intent(LoginActivity.this, EmployeeDashBoardActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }


                                } else if (UserType.equals("2")) {
                                    Intent intent = new Intent(LoginActivity.this, SuperVisiorDashBoardActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                } else if (UserType.equals("4")) {
                                    if (AEMEmployeeID.equals("0")) {

                                        Intent intent = new Intent(LoginActivity.this, TempDashBoardActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        //Toast.makeText(LoginActivity.this,"your actual id generated",Toast.LENGTH_LONG).show();
                                        showEmpDialog();

                                    }
                                } else if (UserType.equals("3")) {
                                    Intent intent = new Intent(LoginActivity.this, HRMSDashBoardActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }


                            } else {
                                shoeDialog();


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               progressDialog.dismiss();
              //  Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                showInternetDialog();

            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                300000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void shoeDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this, R.style.CustomDialogNew);
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
        alertDialog.setCancelable(true);
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this, R.style.CustomDialogNew);
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this, R.style.CustomDialogNew);
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
        TextView tvInvalidDialog=(TextView)dialogView.findViewById(R.id.tvInvalidDialog);
        tvInvalidDialog.setText("Something went wrong.Please try again");
        alert1 = dialogBuilder.create();
        alert1.setCancelable(true);
        Window window = alert1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alert1.show();


    }


    private void forgotpassworddialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this, R.style.CustomDialogNew);
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.fotgotpassworddialog, null);
        dialogBuilder.setView(dialogView);
        ImageView imgClose=(ImageView)dialogView.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp.dismiss();
            }
        });
        etUserId = (EditText) dialogView.findViewById(R.id.etUserId);
        final EditText etSecurityCode = (EditText) dialogView.findViewById(R.id.etSecurityCode);
        final Button btnSubmit = (Button) dialogView.findViewById(R.id.btnSubmit);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etUserId.getText().toString().length() > 0  ) {
                    if(etSecurityCode.getText().toString().length() > 0) {
                        security_code = etSecurityCode.getText().toString();
                    }
                    changePassword();

                    popUp.dismiss();
                }else {
                    Toast.makeText(LoginActivity.this, "Please enter userid and security code", Toast.LENGTH_LONG).show();
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

        String surl = AppData.url+"GCl_ForgotPassword?MasterID="+etUserId.getText().toString()+"&SecurityCode="+ security_code;
        Log.d("inputLogin", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseChangePassword", response);

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                                JSONArray responseData = job1.optJSONArray("responseData");


                                successAlert(responseText);

                            } else {

                                Toast.makeText(LoginActivity.this, responseText, Toast.LENGTH_LONG).show();

                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                //showAlert();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void successAlert(String msg) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this, R.style.CustomDialogNew);
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





}
