package io.cordova.myapp00d753.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;
import io.cordova.myapp00d753.utility.Pref;

public class LoginActivity extends AppCompatActivity {
    TextView tvSignIn;
    EditText etUserId,etPassword;
    String userId,password;
    LinearLayout llLogin;
    NetworkConnectionCheck connectionCheck;
    AlertDialog alertDialog;
    Pref pref;
    String UserType;
    String refreshedToken;
    EditText etSecurityCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();

        onClick();
    }

    private void  initialize(){
        llLogin=(LinearLayout)findViewById(R.id.llLogin);
        tvSignIn=(TextView) findViewById(R.id.tvSignIn);
        etUserId=(EditText)findViewById(R.id.etUserId);
        etPassword=(EditText)findViewById(R.id.etPassword);
        connectionCheck = new NetworkConnectionCheck(this);
        pref=new Pref(LoginActivity.this);
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("token",refreshedToken);
        etSecurityCode=(EditText)findViewById(R.id.etSecuritycode);

    }

    private void onClick(){
        llLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etUserId.getText().toString().length()>0){
                    if (etPassword.getText().toString().length()>0){
                        if (connectionCheck.isNetworkAvailable()) {

                            loginFunction();
                            /*Intent intent=new Intent(LoginActivity.this,EmployeeDashBoardActivity.class);
                            startActivity(intent);*/
                            Date d=new Date();
                            SimpleDateFormat sdf=new SimpleDateFormat("hh:mm a");
                            String currentDateTimeString = sdf.format(d);
                            Log.d("ctime",currentDateTimeString);
                            pref.saveCtime(currentDateTimeString);
                        }else {
                            connectionCheck.getNetworkActiveAlert().show();
                        }


                    }else {
                        etPassword.setError("please enter your password");
                        etPassword.requestFocus();
                    }

                }else {
                    etUserId.setError("please enter your user id");
                    etUserId.requestFocus();
                }



            }
        });
    }

    public void loginFunction() {
        String surl ="http://111.93.182.174/GeniusiOSApi/api/get_GCLAuthenticate?MasterID="+etUserId.getText().toString()+"&Password="+etPassword.getText().toString()+"&IMEI=0000&Version=V.7&SecurityCode="+etSecurityCode.getText().toString()+"&DeviceID="+refreshedToken+"&DeviceType=A";
        Log.d("inputLogin",surl);
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Authenticating...");
        progressBar.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        progressBar.dismiss();
                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText=job1.optString("responseText");
                            boolean responseStatus=job1.optBoolean("responseStatus");
                            if (responseStatus){
                                Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                                JSONArray responseData=job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++){
                                    JSONObject obj=responseData.getJSONObject(i);
                                    String AEMEmployeeID=obj.optString("AEMEmployeeID");
                                    pref.saveEmpId(AEMEmployeeID);
                                    String Name=obj.optString("Name");
                                    pref.saveEmpName(Name);
                                    String LoginDateTime=obj.optString("LoginDateTime");
                                    pref.saveloginTime(LoginDateTime);
                                    String FlagMenu=obj.optString("FlagMenu");
                                    pref.saveMenu(FlagMenu);
                                    String AEMConsultantID=obj.optString("AEMConsultantID");
                                    pref.saveEmpConId(AEMConsultantID);
                                    String AEMClientID=obj.optString("AEMClientID");
                                    pref.saveEmpClintId(AEMClientID);
                                    String AEMClientOfficeID=obj.optString("AEMClientOfficeID");
                                    pref.saveEmpClintOffId(AEMClientOfficeID);
                                    String MasterID=obj.optString("MasterID");
                                    pref.saveMasterId(MasterID);
                                    Log.d("Master",MasterID);
                                    UserType=obj.optString("UserType");
                                    pref.saveUserType(UserType);
                                    String CTCUrl=obj.optString("CTCUrl");
                                    pref.saveCTCURL(CTCUrl);
                                    String WeeklyOff=obj.optString("WeeklyOff");
                                    pref.saveWeeklyoff(WeeklyOff);
                                    String Leave=obj.optString("LeaveApply");
                                    pref.saveOnLeave(Leave);
                                    String LeaveUrl=obj.optString("LeaveUrl");
                                    pref.saveLeaveUrl(LeaveUrl);
                                    String AttdImage=obj.optString("AttdImage");
                                    pref.saveAttdImg(AttdImage);
                                    String BackAttd=obj.optString("BackAttd");
                                    pref.saveBackAttd(BackAttd);
                                    String IsSupervisor=obj.optString("IsSupervisor");
                                    pref.saveSup(IsSupervisor);
                                    String CompanyName=obj.optString("CompanyName");
                                    pref.saveSecurityCode(CompanyName);



                                }
                                if (UserType.equals("1")) {

                                    Intent intent = new Intent(LoginActivity.this, EmployeeDashBoardActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }else if (UserType.equals("2")){
                                    Intent intent = new Intent(LoginActivity.this, SuperVisiorDashBoardActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                            else {
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
                progressBar.dismiss();
                Toast.makeText(LoginActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                showAlert();
                Log.e("ert",error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");

    }

    private void shoeDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_invalidcredential, null);
        dialogBuilder.setView(dialogView);
        Button btnOk=(Button)dialogView.findViewById(R.id.btnOk) ;
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

    private void showAlert(){
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
}
