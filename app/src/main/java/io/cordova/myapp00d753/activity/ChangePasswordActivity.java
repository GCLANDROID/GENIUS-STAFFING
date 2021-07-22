package io.cordova.myapp00d753.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.text.Html;
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

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;


import org.json.JSONObject;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.utility.Pref;


public class ChangePasswordActivity extends AppCompatActivity {

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

    }


    private void changePassword() {

        final ProgressDialog pd = new ProgressDialog(ChangePasswordActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();

        AndroidNetworking.upload("https://payhr.geniusconsultant.com/Himadri/HimadriAPI/api/Authentication/ChangePassword")
                .addMultipartParameter("EmployeeId", pref.getEmpId())
                .addMultipartParameter("NewPassword", etNewPassword.getText().toString())
                .addMultipartParameter("ExistingPassword", etOldPassword.getText().toString())
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())


                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        pd.show();

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        pd.dismiss();
                        JSONObject job = response;
                        boolean responseStatus = job.optBoolean("responseStatus");
                        String responseText = job.optString("responseText");
                        if (responseStatus) {
                            successAlert("Password has been changed successfully");
                        } else {
                            Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();
                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        pd.dismiss();
                        Toast.makeText(ChangePasswordActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();

                    }
                });
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


}
