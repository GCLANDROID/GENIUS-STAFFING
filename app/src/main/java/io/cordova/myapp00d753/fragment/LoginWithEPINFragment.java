package io.cordova.myapp00d753.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.otpview.OTPListener;
import com.otpview.OTPTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.ChangePasswordActivity;
import io.cordova.myapp00d753.activity.E_Pin_SetupLoginActivity;
import io.cordova.myapp00d753.activity.EmployeeDashBoardActivity;
import io.cordova.myapp00d753.activity.LoginActivity;
import io.cordova.myapp00d753.activity.MPIN_SetupActivity;
import io.cordova.myapp00d753.activity.ResignEmployeeDashboardActivity;
import io.cordova.myapp00d753.activity.SuperVisiorDashBoardActivity;
import io.cordova.myapp00d753.activity.TempDashBoardActivity;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.ShowDialog;
import io.cordova.myapp00d753.utility.Util;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginWithEPINFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginWithEPINFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginWithEPINFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginWithEPINFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginWithEPINFragment newInstance(String param1, String param2) {
        LoginWithEPINFragment fragment = new LoginWithEPINFragment();
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
    TextView txtSetUpEpin,txtForgotEpin;
    OTPTextView otp_view;
    String MPIN = "";
    Button btnLogin;
    String androidID;
    String AEMEmployeeID;
    Pref pref;
    String UserType;
    int WorkingStatus;
    AlertDialog alertDialog;
    String ConsentFlag,security_code,ip,sessionId;
    public static String SECRET_KEY = "74074750353890398886017484399862";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login_with_e_p_i_n, container, false);
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
        btnLogin = view.findViewById(R.id.btnLogin);
        otp_view = view.findViewById(R.id.otp_view);
        imgMobileImage = view.findViewById(R.id.imgMobileImage);
        txtSetUpEpin = view.findViewById(R.id.txtSetUpEpin);
        txtForgotEpin=view.findViewById(R.id.txtForgotEpin);
        Glide.with(requireActivity())
                .asGif()
                .load(R.drawable.location_15591430)
                .into(imgMobileImage);

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

        ip = getIPAddress(true);
        sessionId = generateSessionID("Staffing_Mobile");

    }

    private void btnClick() {


        otp_view.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {

            }

            @Override
            public void onOTPComplete(@NonNull String complete_otp) {

                MPIN = complete_otp;

            }
        });
        txtSetUpEpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), E_Pin_SetupLoginActivity.class);
                startActivity(intent);
            }
        });

        txtForgotEpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), E_Pin_SetupLoginActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MPIN.isEmpty() || MPIN.length() < 4) {
                    ShowDialog.showErrorDialog(getContext(), "Please enter 4 digit MPIN");
                    return;
                }

                JSONObject obj = new JSONObject();
                try {
                    obj.put("DeviceID", Util.encrypt(androidID, SECRET_KEY));
                    obj.put("mPin", Util.encrypt(MPIN, SECRET_KEY));
                    obj.put("IPAddress", ip);
                    obj.put("AppSessionID", sessionId);
                    obj.put("UUID", androidID);
                    obj.put("GUID", androidID);
                    obj.put("MachineDetails", "GeniusStaffing_Android");
                    obj.put("LoginType", "1");
                    loginv2(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


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


                                    WorkingStatus = obj.optInt("WorkingStatus");
                                    String Domain = obj.optString("Domain").toUpperCase();

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

                                    String Genius_Access_Token = obj.optString("Genius_Access_Token").trim();
                                    pref.saveAccessToken(Genius_Access_Token);

                                    if (WorkingStatus == 1) {
                                        if (UserType.equals("1")  || UserType.equals("3")){
                                            JSONObject obj1 = new JSONObject();
                                            try {
                                                obj1.put("MasterID", AEMEmployeeID);
                                                obj1.put("SecurityCode", security_code);
                                                logindetails(obj1);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }


                                    } else {
                                        //re direct to resign page

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

//                                    String BackAttd = obj.optString("BackDateAttendance");
//                                    pref.saveBackAttd(BackAttd);
//

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


                                    Intent intent = new Intent(getContext(), EmployeeDashBoardActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("ConsentFlag", ConsentFlag);
                                    intent.putExtra("from","MPIN_setup");
                                    startActivity(intent);
                                    pref.saveLoginType("MPIN");



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
}