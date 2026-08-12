package io.cordova.myapp00d753.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.dhims.timerview.TimerTextView;
import com.otpview.OTPListener;
import com.otpview.OTPTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.TempDashBoardActivity;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.Util;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginWithMobileOtpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginWithMobileOtpFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginWithMobileOtpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginWithMobileOtpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginWithMobileOtpFragment newInstance(String param1, String param2) {
        LoginWithMobileOtpFragment fragment = new LoginWithMobileOtpFragment();
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

    ImageView imgMobileImage;
    Button btnSendOTP;
    LinearLayout llOtpInput, llMobileNumberInput;
    OTPTextView otp_view;
    String OTP = "";
    EditText etMob;
    private static final String ALPHA_NUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();
    String loginCode = "";
    AlertDialog alertDialog;
    String androidID,ip;
    TextView tvOTPText;
    String domain,sessionId;
    String EmployeeID,UserType,AEMEmployeeID;
    TextView tvResendOTP;
    TimerTextView timerText;
    private final static int INTERVAL = 1000 * 60 * 3;
    Button btnLogin;
    public static String SECRET_KEY = "74074750353890398886017484399862";
    String security_code;
    Pref pref;
    int WorkingStatus;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login_with_mobile_otp, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        btnClick();
    }

    private void initView(View view) {
        pref=new Pref(getContext());
        imgMobileImage = view.findViewById(R.id.imgMobileImage);
        btnSendOTP = view.findViewById(R.id.btnSendOTP);
        llOtpInput = view.findViewById(R.id.llOtpInput);
        llMobileNumberInput = view.findViewById(R.id.llMobileNumberInput);
        Glide.with(requireActivity())
                .asGif()
                .load(R.drawable.password)
                .into(imgMobileImage);
        otp_view = view.findViewById(R.id.otp_view);
        etMob = view.findViewById(R.id.etMob);
        androidID = getAndroidID(getContext());
        if (androidID.equals("0")) {
            TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            androidID = telephonyManager.getDeviceId();
        } else {
            androidID = getAndroidID(getContext());
        }
        ip=getIPAddress(true);
        tvOTPText=view.findViewById(R.id.tvOTPText);
        timerText=view.findViewById(R.id.tvTimerText);
        tvResendOTP=view.findViewById(R.id.tvResendOTP);
        btnLogin=view.findViewById(R.id.btnLogin);
        sessionId = generateSessionID("Staffing_Mobile");
    }

    private void btnClick() {
        btnSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etMob.getText().toString().isEmpty()) {
                    shoeDialogDynamicMsg("Please enter mobile number");
                    return;
                }
                if (etMob.getText().toString().length() < 10) {
                    shoeDialogDynamicMsg("Please enter valid mobile number");
                    return;
                }
                loginCode = generateCode(6);
                Log.e("LOGIN", "Generated OTP: " + loginCode);
                JSONObject obj = new JSONObject();
                try {
                    obj.put("ComType", "Mobile");
                    obj.put("ComValue", etMob.getText().toString());
                    obj.put("LoginRequestID", loginCode);
                    obj.put("DeviceID", androidID);
                    obj.put("IPAddress", ip);
                    obj.put("MachineDetails", "GeniusStaffing_Android");
                    obj.put("AppName", "GeniusStaffing_Android");
                    generateOTP(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        tvResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginCode = generateCode(6);
                Log.e("LOGIN", "Generated OTP: " + loginCode);
                JSONObject obj = new JSONObject();
                try {
                    obj.put("ComType", "Mobile");
                    obj.put("ComValue", etMob.getText().toString());
                    obj.put("LoginRequestID", loginCode);
                    obj.put("DeviceID", androidID);
                    obj.put("IPAddress", ip);
                    obj.put("MachineDetails", "GeniusStaffing_Android");
                    obj.put("AppName", "GeniusStaffing_Android");
                    generateOTP(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        otp_view.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {

            }

            @Override
            public void onOTPComplete(@NonNull String complete_otp) {

                OTP = complete_otp;

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (OTP.isEmpty() ||  OTP.length() < 6) {
                    shoeDialogDynamicMsg("Please enter OTP");
                    return;
                }

                JSONObject obj = new JSONObject();
                try {
                    obj.put("MobileNo", Util.encrypt(etMob.getText().toString().trim(), SECRET_KEY));
                    obj.put("OTP", Util.encrypt(OTP, SECRET_KEY));
                    obj.put("IPAddress", ip);
                    obj.put("AppSessionID", sessionId);
                    obj.put("UUID", androidID);
                    obj.put("GUID", loginCode);
                    obj.put("MachineDetails", "GeniusStaffing_Android");
                    obj.put("LoginType", "2");
                    obj.put("SecurityCode", domain);
                    loginv2(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public static String generateCode(int length) {
        StringBuilder code = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(ALPHA_NUMERIC.length());
            code.append(ALPHA_NUMERIC.charAt(index));
        }

        return code.toString();
    }


    private void generateOTP(JSONObject jsonObject) {
        Log.e("LOGIN", "login: " + jsonObject.toString());
        tvResendOTP.setEnabled(false);
        tvResendOTP.setText("Resend OTP after ");
        timerText.setVisibility(View.VISIBLE);

        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(AppData.newv2url + "Login/GenerateLoginOTP")
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
                            Toast.makeText(getContext(), "OTP has been sent to "+etMob.getText().toString(), Toast.LENGTH_SHORT).show();
                            llOtpInput.setVisibility(View.VISIBLE);
                            llMobileNumberInput.setVisibility(View.GONE);
                            tvOTPText.setText("OTP has been sent to " + etMob.getText().toString());
                            JSONArray Response_Data= job1.optJSONArray("Response_Data");
                            try {
                                JSONObject frstOBJ=Response_Data.getJSONObject(0);
                                domain=frstOBJ.optString("Domain");
                                loginCode=frstOBJ.optString("LoginRequestID");
                                EmployeeID=frstOBJ.optString("EmployeeID");
                                long futureTimestamp = System.currentTimeMillis() + (3 * 60 * 1000);
                                timerText.setEndTime(futureTimestamp);

                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        tvResendOTP.setEnabled(true);
                                        tvResendOTP.setText("Resend OTP");
                                        timerText.setVisibility(View.GONE);
                                    }
                                }, INTERVAL);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();


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

    private void shoeDialogDynamicMsg(String msg) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_invalidcredential, null);
        dialogBuilder.setView(dialogView);
        TextView tvTitle = dialogView.findViewById(R.id.tvTitle);
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

    private String getAndroidID(Context context) {
        return Settings.Secure.getString(
                context.getContentResolver(),
                Settings.Secure.ANDROID_ID
        );
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


    private void loginv2(JSONObject jsonObject) {
        Log.e("LOGIN", "login: " + jsonObject.toString());
        final ProgressDialog pd = new ProgressDialog(getContext());
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




                            JSONArray responseData = job1.optJSONArray("Response_Data");
                            try {

                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String UserID=obj.optString("UserID");
                                    pref.saveMasterId(UserID);
                                    String Domain=obj.optString("Domain");

                                    AEMEmployeeID = obj.optString("UserID");
                                    pref.saveMasterId(AEMEmployeeID);


                                    String Name = obj.optString("UserName");
                                    pref.saveEmpName(Name);
                                    String AEMConsultantID = obj.optString("AEMConsultantID");
                                    pref.saveEmpConId(AEMConsultantID);

                                    String AEMClientID = obj.optString("ClientID");
                                    pref.saveEmpClintId(AEMClientID);


                                    UserType = obj.optString("UserType");
                                    pref.saveUserType(UserType);
                                    pref.saveSecurityCode(security_code);

                                    String Password = obj.optString("Password");
                                    pref.savePassword("");

                                    WorkingStatus = obj.optInt("WorkingStatus");

                                    String Genius_Access_Token = obj.optString("Genius_Access_Token").trim();
                                    pref.saveAccessToken(Genius_Access_Token);

                                    if (Domain.equals("FSS")) {
                                        security_code = "0000";
                                    } else if (Domain.equals("FMS")) {
                                        security_code = "222";
                                    } else if (Domain.equals("ITS")) {
                                        security_code = "888";
                                    } else if (Domain.equals("SEC")) {
                                        security_code = "333";
                                    } else if (Domain.equals("NAPS")) {
                                        security_code = "444";
                                    }  else if (Domain.equals("MSP")) {
                                        security_code = "666";
                                    }

                                    pref.saveSecurityCode(security_code);

                                    Intent intent = new Intent(getContext(), TempDashBoardActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);





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

    public static String generateSessionID(String userId) {
        return userId + "_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString();
    }
}