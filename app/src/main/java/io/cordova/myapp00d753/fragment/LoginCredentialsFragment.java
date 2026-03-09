package io.cordova.myapp00d753.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import io.cordova.myapp00d753.activity.ChangePasswordActivity;
import io.cordova.myapp00d753.activity.EmployeeDashBoardActivity;
import io.cordova.myapp00d753.activity.HRMSDashBoardActivity;
import io.cordova.myapp00d753.activity.LoginActivity;
import io.cordova.myapp00d753.activity.MaintainceBreakActivity;
import io.cordova.myapp00d753.activity.ResignEmployeeDashboardActivity;
import io.cordova.myapp00d753.activity.SuperVisiorDashBoardActivity;
import io.cordova.myapp00d753.activity.TempDashBoardActivity;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.Util;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginCredentialsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginCredentialsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginCredentialsFragment() {
        // Required empty public constructor
    }


    public static LoginCredentialsFragment newInstance(String param1, String param2) {
        LoginCredentialsFragment fragment = new LoginCredentialsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    NetworkConnectionCheck connectionCheck;
    String refreshedToken,ip,sessionId;
    EditText etUserId,etPassword,etSecurityCode;
    Pref pref;
    CheckBox ckRemember;
    ImageView imgVisible,imginVisible;
    TextView llForgotPassword;
    boolean tempFlag;
    LinearLayout llSecurityCode;
    Button llSignIn;
    String security_code,AEMEmployeeID,UserType,ConsentFlag;
    AlertDialog alertDialog,al1,popUp,alerDialog1;
    EditText etForgotUserId;
    int WorkingStatus;
    public static String SECRET_KEY = "74074750353890398886017484399862";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login_credentials, container, false);
        return v;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(view);
        onClick();
    }

    private void initialize(View view) {


        etUserId = (EditText) view.findViewById(R.id.etUserId);
        etPassword = (EditText) view.findViewById(R.id.etPassword);
        connectionCheck = new NetworkConnectionCheck(getContext());
        pref = new Pref(getContext());
        refreshedToken = getAndroidID(getContext());


        if (refreshedToken.equals("0")) {
            TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            refreshedToken = telephonyManager.getDeviceId();
        }else {
            refreshedToken = getAndroidID(getContext());
        }

//        Log.d("token",refreshedToken);
        etSecurityCode = (EditText) view.findViewById(R.id.etSecuritycode);
        llSecurityCode=view.findViewById(R.id.llSecurityCode);


        ckRemember = (CheckBox) view.findViewById(R.id.ckRemember);
        llSignIn=view.findViewById(R.id.llSignIn);

        if (pref.getCheckFlag().equals("1")) {
            ckRemember.setChecked(true);
            etUserId.setText(pref.getUserLoginId());
            etPassword.setText(pref.getPassword());
        }

        if (pref.getCheckFlag().equals("2")) {
            ckRemember.setChecked(false);
            etUserId.setText("");
            etPassword.setText("");
        }

        imgVisible = (ImageView) view.findViewById(R.id.imgVisible);
        imginVisible = (ImageView) view.findViewById(R.id.imginVisible);

        llForgotPassword = (TextView) view.findViewById(R.id.llForgotPassword);


        ip = getIPAddress(true);
        sessionId = generateSessionID("Staffing_Mobile");






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
                if (etUserId.getText().toString().contains("TEMP") || etUserId.getText().toString().contains("temp") || etUserId.getText().toString().contains("GCL") || etUserId.getText().toString().contains("gcl")) {
                    etPassword.setText("password");
                    tempFlag=true;
                } else {
                    etPassword.setText("");
                    tempFlag=false;
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

                            if (!tempFlag){
                                JSONObject obj = new JSONObject();
                                try {
                                    obj.put("MasterID", Util.encrypt(etUserId.getText().toString().trim(), SECRET_KEY));
                                    obj.put("Password", Util.encrypt(etPassword.getText().toString().trim(), SECRET_KEY));
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
                            }else {
                                JSONObject obj = new JSONObject();
                                try {
                                    obj.put("MasterID", Util.encrypt(etUserId.getText().toString(), SECRET_KEY));
                                    obj.put("Password", Util.encrypt(etPassword.getText().toString(), SECRET_KEY));
                                    obj.put("IMEI", refreshedToken);
                                    obj.put("DeviceID", refreshedToken);
                                    obj.put("DeviceType", "A");
                                    obj.put("SecurityCode", security_code);
                                    login(obj);
                                }catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

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


    private void login(JSONObject jsonObject) {
        Log.e("LOGIN", "login: " + jsonObject.toString());
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(AppData.newv2url + "Login/UserLogin")
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
                        if (Response_Code.equals("101")) {
                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                            pref.saveUserLoginID(etUserId.getText().toString().trim());
                            Log.e("UserLoginId", "UserLoginId: " + pref.getUserLoginId());

                            String responseData = job1.optString("Response_Data");
                            try {
                                JSONArray jarr = new JSONArray(responseData);
                                for (int i = 0; i < jarr.length(); i++) {
                                    JSONObject obj = jarr.getJSONObject(i);
                                    AEMEmployeeID = obj.optString("EmployeeID");
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
                                    pref.saveMasterId(MasterID);
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
                                    String BackAttd = obj.optString("BackDateAttendance");
                                    pref.saveBackAttd(BackAttd);
                                    String IsSupervisor = obj.optString("IsSupervisor");
                                    pref.saveSup(IsSupervisor);
                                    String CompanyName = obj.optString("CompanyName");
                                    Log.e("Log", "CompanyName: " + CompanyName);
                                    pref.saveCompanyName(CompanyName);
                                    pref.saveSecurityCode(security_code);
                                    String FlagAddr = obj.optString("FlagAddr");
                                    pref.saveFlagLocation(FlagAddr);
                                    String Password = obj.optString("Password");
                                    pref.savePassword(etPassword.getText().toString());
                                    String OffAttFlag = obj.optString("OffAttFlag");
                                    pref.saveOffAttnFlag(OffAttFlag);
                                    if (pref.getCheckFlag().equals("1")) {
                                        pref.saveIntentFlag("1");
                                    }

                                    String DemoFlag = obj.optString("DemoFlag");
                                    pref.saveDemoFlag(DemoFlag);
                                    String GeoConfFlag = obj.optString("GeoConfFlag");
                                    pref.saveFenceConfigFlag(GeoConfFlag);
                                    String GeoFenceMenuFlag = obj.optString("GeoFenceMenuFlag");
                                    pref.saveFenceMenuFlag(GeoFenceMenuFlag);
                                    String GeoFenceAttFlag = obj.optString("GeoFenceAttFlag");
                                    pref.saveFenceAttnFlag(GeoFenceAttFlag);
                                    boolean AppRenameFlag = obj.optBoolean("AppRenameFlag");
                                    String AppRenameText = obj.optString("AppRenameText");

                                    pref.saveMsgAlertStatus(AppRenameFlag);
                                    pref.saveMsg(AppRenameText);
                                    String PFConsolidateURL = obj.optString("PFConsolidateURL");
                                    pref.savePFURL(PFConsolidateURL);
                                    String Leave = obj.optString("Leave");
                                    pref.saveShiftFlag(Leave);
                                    pref.saveEmpClintId(AEMClientID);
                                    pref.saveMsgAlertStatus(AppRenameFlag);
                                    pref.saveMsg(AppRenameText);
                                    pref.savePFURL(PFConsolidateURL);
                                    String PF_Notify_URL = obj.optString("PF_Notify_URL");
                                    pref.savePFNotificationURL(PF_Notify_URL);
                                    String Genius_Access_Token = obj.optString("Genius_Access_Token").trim();
                                    pref.saveAccessToken(Genius_Access_Token);
                                    ConsentFlag = obj.optString("ConsentFlag");
                                    String LeaveBalanceView = obj.optString("LeaveBalanceView");
                                    AppData.LEAVE_BALANCE_VIEW_FLAG = LeaveBalanceView;
                                    String UAN_Active = obj.optString("UAN_Active");
                                    pref.saveUAN_Active(UAN_Active);
                                    String UAN_Mandatory = obj.optString("UAN_Mandatory");
                                    pref.saveUAN_Mandatory(UAN_Mandatory);
                                    String Adjustment_Status = obj.optString("Adjustment");
                                    pref.saveAdjustmentStatus(Adjustment_Status);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (UserType.equals("1")) {
                                if (etPassword.getText().toString().equalsIgnoreCase("password")) {
                                    Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);

                                } else {
                                    if (ConsentFlag.equals("1")) {
                                        Intent intent = new Intent(getContext(), EmployeeDashBoardActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra("ConsentFlag", ConsentFlag);
                                        intent.putExtra("from","Login_setup");
                                        startActivity(intent);

                                    } else {
                                        Intent intent = new Intent(getContext(), EmployeeDashBoardActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra("ConsentFlag", ConsentFlag);
                                        intent.putExtra("from","Login_setup");
                                        startActivity(intent);

                                    }
                                }
                            } else if (UserType.equals("2")) {
                                Intent intent = new Intent(getContext(), SuperVisiorDashBoardActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            } else if (UserType.equals("4")) {
                                if (AEMEmployeeID.equals("0")) {

                                    Intent intent = new Intent(getContext(), TempDashBoardActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("ConsentFlag", ConsentFlag);
                                    startActivity(intent);

                                } else {
                                    //Toast.makeText(LoginActivity.this,"your actual id generated",Toast.LENGTH_LONG).show();
                                    showEmpDialog();

                                }
                            } else if (UserType.equals("3")) {
                                Intent intent = new Intent(getContext(), HRMSDashBoardActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            }
                        } else {
                            shoeDialog();


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

    private void shoeDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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



    private void showEmpDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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






    private void showForgotPasswordDialouge() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                    Toast.makeText(getContext(), "Please enter userid ", Toast.LENGTH_LONG).show();
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
        ProgressDialog pd = new ProgressDialog(getContext());
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
                            Toast.makeText(getActivity(), "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(getActivity(), "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                //showAlert();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void successAlert(String msg) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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



    private String getAndroidID(Context context) {
        return Settings.Secure.getString(
                context.getContentResolver(),
                Settings.Secure.ANDROID_ID
        );
    }

    private void loginv2(JSONObject jsonObject) {
        Log.e("LOGIN", "login: " + jsonObject.toString());
        final ProgressDialog pd = new ProgressDialog(getActivity());
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
                                            Intent intent = new Intent(getContext(), TempDashBoardActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.putExtra("ConsentFlag", ConsentFlag);
                                            pref.saveEmpId(AEMEmployeeID);
                                            startActivity(intent);


                                        }


                                    } else {
                                        //re direct to resign page
                                        pref.saveEmpId(AEMEmployeeID);
                                        pref.saveSecurityCode(security_code);
                                        Intent intent = new Intent(getContext(), ResignEmployeeDashboardActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);

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
        final ProgressDialog pd = new ProgressDialog(getContext());
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

                            pref.saveLoginType("Cred");

                            if (UserType.equals("1")) {
                                if (etPassword.getText().toString().equalsIgnoreCase("password")) {
                                    Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);


                                } else {

                                    Intent intent = new Intent(getContext(), EmployeeDashBoardActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("ConsentFlag", ConsentFlag);
                                    intent.putExtra("from","Login_setup");
                                    startActivity(intent);


                                }
                            } else if (UserType.equals("3")) {
                                Intent intent = new Intent(getContext(), SuperVisiorDashBoardActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            } else if (UserType.equals("2")) {


                                Intent intent = new Intent(getContext(), TempDashBoardActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("ConsentFlag", ConsentFlag);
                                startActivity(intent);


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



}