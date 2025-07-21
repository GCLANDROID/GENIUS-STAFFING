package io.cordova.myapp00d753.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

import java.io.UnsupportedEncodingException;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.ShowDialog;


public class ChangePasswordActivity extends AppCompatActivity {
    private static final String TAG = "ChangePasswordActivity";
    TextView tvNewPassword, tvConfirmPassword;
    EditText etNewPassword, etConfirmPassword;
    Button btnUpdate,btnCancel;
    String isModiFied, empId, securityCode;
    private static String SERVER_PATH = "";

    ProgressDialog progressDialog;
    Pref pref;
    String newPassword,confirmPassowrd;
    String ipAddress;
    ImageView imgHome,imgBack;
    String status;
    TextView tvOldPassword;
    EditText etOldPassword;
    AlertDialog alerDialog1;
    TextView tvTickCross1,tvTickCross2,tvTickCross3,tvTickCross4,tvTickCross5,tvPasswordLength,tvUpperCase,tvLowerCase,tvCheckNumber,tvSpecialCharacter;
    int GreenColor,RedColor;
    boolean passwordPolicyStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initView();
        onClick();
    }

    private void initView() {
        pref=new Pref(getApplicationContext());
        tvNewPassword = (TextView) findViewById(R.id.tvNewPassword);
        tvConfirmPassword = (TextView) findViewById(R.id.tvConfirmPassword);
        tvOldPassword = (TextView) findViewById(R.id.tvOldPassword);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        status=getIntent().getStringExtra("goingstatus");
        tvTickCross1 = findViewById(R.id.tvTickCross1);
        tvTickCross2 = findViewById(R.id.tvTickCross2);
        tvTickCross3 = findViewById(R.id.tvTickCross3);
        tvTickCross4 = findViewById(R.id.tvTickCross4);
        tvTickCross5 = findViewById(R.id.tvTickCross5);
        tvPasswordLength = findViewById(R.id.tvPasswordLength);
        tvUpperCase = findViewById(R.id.tvUpperCase);
        tvLowerCase = findViewById(R.id.tvLowerCase);
        tvCheckNumber = findViewById(R.id.tvCheckNumber);
        tvSpecialCharacter = findViewById(R.id.tvSpecialCharacter);
        GreenColor = ContextCompat.getColor(this, R.color.designcolor);
        RedColor = ContextCompat.getColor(this, R.color.misscolor);
        String color = "<font color='#EE0000'>*</font>";

        newPassword="New Password:";

        tvNewPassword.setText(Html.fromHtml(newPassword + color));

        confirmPassowrd = "Confirm Password";


        tvConfirmPassword.setText(Html.fromHtml(confirmPassowrd + color));

        String oldPassword = "Old Password";


        tvOldPassword.setText(Html.fromHtml(oldPassword + color));

        etNewPassword = (EditText) findViewById(R.id.etNewPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        etOldPassword = (EditText) findViewById(R.id.etOldPassword);

        isModiFied = getIntent().getStringExtra("ismodiFied");
        empId =pref.getEmpId() ;
        securityCode=pref.getSecurityCode();
        btnCancel=(Button)findViewById(R.id.btnCancel);



        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(true);

        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);



    }

    private void onClick() {

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etNewPassword.getText().toString().length() > 0) {
                    if (passwordPolicyStatus){
                        if (etConfirmPassword.getText().toString().length() > 0) {
                            if (etNewPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                                if (etOldPassword.getText().toString().length()>0) {


                                    changePassword();
                                }else {
                                    etOldPassword.setError("PLease enter old password");
                                    etOldPassword.requestFocus();
                                }

                            } else {
                                etConfirmPassword.setError("Confirm password should be same with new password");
                                etConfirmPassword.requestFocus();
                            }

                        } else {
                            etConfirmPassword.setError("Please enter confirm Password");
                            etConfirmPassword.requestFocus();
                        }
                    } else {
                        etNewPassword.setError("Enter a valid password as per the password policy.");
                        etNewPassword.requestFocus();
                    }
                } else {
                    etNewPassword.setError("Please enter new Password");
                    etNewPassword.requestFocus();

                }

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etConfirmPassword.setText("");
                etNewPassword.setText("");
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),EmployeeDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        etNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                passwordPolicyStatus = validatePassword();
            }
        });
    }

    private boolean validatePassword() {
        boolean lengthCheck,upperCheck,lowerCheck,numberCheck,specialCharCheck;
        if (isLengthValid(etNewPassword.getText().toString().trim())){
            tvTickCross1.setText(R.string.tick_text);
            tvTickCross1.setTextColor(GreenColor);
            tvPasswordLength.setTextColor(GreenColor);
            lengthCheck = true;
        } else {
            tvTickCross1.setText(R.string.cross_text);
            tvTickCross1.setTextColor(RedColor);
            tvPasswordLength.setTextColor(RedColor);
            lengthCheck = false;
        }
        if(hasUpperCase(etNewPassword.getText().toString().trim())){
            tvTickCross2.setText(R.string.tick_text);
            tvTickCross2.setTextColor(GreenColor);
            tvUpperCase.setTextColor(GreenColor);
            upperCheck = true;
        } else {
            tvTickCross2.setText(R.string.cross_text);
            tvTickCross2.setTextColor(RedColor);
            tvUpperCase.setTextColor(RedColor);
            upperCheck = false;
        }
        if(hasLowerCase(etNewPassword.getText().toString().trim())){
            tvTickCross3.setText(R.string.tick_text);
            tvTickCross3.setTextColor(GreenColor);
            tvLowerCase.setTextColor(GreenColor);
            lowerCheck = true;
        } else {
            tvTickCross3.setText(R.string.cross_text);
            tvTickCross3.setTextColor(RedColor);
            tvLowerCase.setTextColor(RedColor);
            lowerCheck = false;
        }
        if (hasDigit(etNewPassword.getText().toString().trim())){
            tvTickCross4.setText(R.string.tick_text);
            tvTickCross4.setTextColor(GreenColor);
            tvCheckNumber.setTextColor(GreenColor);
            numberCheck = true;
        } else {
            tvTickCross4.setText(R.string.cross_text);
            tvTickCross4.setTextColor(RedColor);
            tvCheckNumber.setTextColor(RedColor);
            numberCheck = false;
        }
        if(hasSpecialChar(etNewPassword.getText().toString().trim())){
            tvTickCross5.setText(R.string.tick_text);
            tvTickCross5.setTextColor(GreenColor);
            tvSpecialCharacter.setTextColor(GreenColor);
            specialCharCheck = true;
        } else {
            tvTickCross5.setText(R.string.cross_text);
            tvTickCross5.setTextColor(RedColor);
            tvSpecialCharacter.setTextColor(RedColor);
            specialCharCheck = false;
        }
        return lengthCheck && upperCheck && lowerCheck && numberCheck && specialCharCheck;
    }


    private void changePassword() {

        byte[] oldpassworddata = new byte[0];
        byte[] newpassworddata = new byte[0];
        try {
            Log.e(TAG, "changePassword: Old Password: "+etOldPassword.getText().toString());
            Log.e(TAG, "changePassword: New Password: "+etNewPassword.getText().toString());
            oldpassworddata = etOldPassword.getText().toString().getBytes("UTF-8");
            newpassworddata = etNewPassword.getText().toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String oldpasswordbase64 = Base64.encodeToString(oldpassworddata, Base64.DEFAULT).replaceAll("\\s+", "");;
        String newpasswordbase64 = Base64.encodeToString(newpassworddata, Base64.DEFAULT).replaceAll("\\s+", "");;
        Log.e(TAG, "changePassword: "+oldpasswordbase64);
        Log.e(TAG, "changePassword: "+newpasswordbase64);

        String surl = AppData.url+"gcl_EmployeePasswordChange?MasterID=" + pref.getMasterId() + "&OldPassword=" + oldpasswordbase64 + "&Password=" + newpasswordbase64 + "&SecurityCode=" + pref.getSecurityCode() ;
        Log.d("inputLogin", surl);

        final ProgressDialog progressDialog=new ProgressDialog(ChangePasswordActivity.this);
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
                                successAlert("Password has been changed successfully");
                            } else {
                                ShowDialog.showErrorDialog(ChangePasswordActivity.this,responseText);
                            }

                            // boolean _status = job1.getBoolean("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ChangePasswordActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                  Toast.makeText(ChangePasswordActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();


            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");

    }

    private void successAlert(String text) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ChangePasswordActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);


        tvInvalidDate.setText(text);


        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }
    public static boolean isLengthValid(String password) {
        return password.length() >= 6 && password.length() <= 12;
    }

    public static boolean hasUpperCase(String password) {
        return password.matches(".*[A-Z].*");
    }

    public static boolean hasLowerCase(String password) {
        return password.matches(".*[a-z].*");
    }

    public static boolean hasDigit(String password) {
        return password.matches(".*\\d.*");
    }

    public static boolean hasSpecialChar(String password) {
        return password.matches(".*[^a-zA-Z0-9].*");
    }
}
