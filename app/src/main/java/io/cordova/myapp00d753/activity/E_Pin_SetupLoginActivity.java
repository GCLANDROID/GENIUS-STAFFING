package io.cordova.myapp00d753.activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

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
import io.cordova.myapp00d753.databinding.ActivityEPinSetupLoginBinding;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;
import io.cordova.myapp00d753.utility.Util;

public class E_Pin_SetupLoginActivity extends AppCompatActivity {

    ActivityEPinSetupLoginBinding binding;
    AlertDialog alertDialog;
    NetworkConnectionCheck connectionCheck;
    String security_code;
    public static String SECRET_KEY = "74074750353890398886017484399862";
    String ip, sessionId;
    String refreshedToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_e_pin_setup_login);
        initView();
    }

    private void initView() {
        connectionCheck = new NetworkConnectionCheck(E_Pin_SetupLoginActivity.this);
        ip = getIPAddress(true);
        sessionId = generateSessionID("Staffing_Mobile");
        refreshedToken = getAndroidID(E_Pin_SetupLoginActivity.this);


        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.etUserId.getText().toString().length() > 0) {
                    if (binding.etPassword.getText().toString().length() > 0) {
                        if (connectionCheck.isNetworkAvailable()) {
                            //  if(etCaptcha.getText().toString().equals(captchaImageView.getCaptchaCode())){
                            //loginFunction();

                            if (binding.etUserId.getText().toString().contains("AEM")) {
                                security_code = "0000";
                            } else if (binding.etUserId.getText().toString().contains("FMS")) {
                                security_code = "222";
                            } else if (binding.etUserId.getText().toString().contains("ITS")) {
                                security_code = "888";
                            } else if (binding.etUserId.getText().toString().contains("SEC")) {
                                security_code = "333";
                            } else if (binding.etUserId.getText().toString().contains("NAPS")) {
                                security_code = "444";
                            } else if (binding.etUserId.getText().toString().contains("NPS")) {
                                security_code = "444";
                            } else if (binding.etUserId.getText().toString().contains("GMSP")) {
                                security_code = "666";
                            } else if (binding.etUserId.getText().toString().contains("MSP")) {
                                security_code = "666";
                            } else if (binding.etUserId.getText().toString().contains("FSS")) {
                                security_code = "0000";
                            }


                            JSONObject obj = new JSONObject();
                            try {
                                obj.put("MasterID", Util.encrypt(binding.etUserId.getText().toString().trim(), SECRET_KEY));
                                obj.put("Password", Util.encrypt(binding.etPassword.getText().toString().trim(), SECRET_KEY));
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


                        } else {
                            connectionCheck.getNetworkActiveAlert().show();
                        }


                    } else {
                        binding.etPassword.setError("Please enter your Password");
                        binding.etPassword.requestFocus();
                    }


                } else {
                    binding.etUserId.setError("Please enter your User ID");
                    binding.etUserId.requestFocus();
                }
            }
        });
    }


    private void loginv2(JSONObject jsonObject) {
        Log.e("LOGIN", "login: " + jsonObject.toString());
        final ProgressDialog pd = new ProgressDialog(E_Pin_SetupLoginActivity.this);
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

                            Intent intent = new Intent(E_Pin_SetupLoginActivity.this, MPIN_SetupActivity.class);
                            intent.putExtra("MasterID",binding.etUserId.getText().toString().trim());
                            startActivity(intent);


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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(E_Pin_SetupLoginActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    private String getAndroidID(Context context) {
        return Settings.Secure.getString(
                context.getContentResolver(),
                Settings.Secure.ANDROID_ID
        );
    }
}
