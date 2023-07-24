package io.cordova.myapp00d753.facereogntion;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.databinding.ActivityInterviewrLoginBinding;
import io.cordova.myapp00d753.utility.Pref;

public class HRLoginActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityInterviewrLoginBinding binding;
    AlertDialog alertDialog;
    androidx.appcompat.app.AlertDialog alertDialog1;
    AlertDialog al1;
    String refreshedToken;
    String AEMEmployeeID;
    String version;
    int MY_SOCKET_TIMEOUT_MS = 60000;
    String LoginFlag="1";
    String IsModified;
    String responseText;
    Pref pref;
    private AppUpdateManager mAppUpdateManager;
    private static final int RC_APP_UPDATE=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_interviewr_login);
        initView();
    }

  private void initView(){
      pref=new Pref(HRLoginActivity.this);
      mAppUpdateManager= AppUpdateManagerFactory.create(this);
      mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
          @Override
          public void onSuccess(AppUpdateInfo appUpdateInfo) {
              if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)){
                  try {
                      mAppUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, HRLoginActivity.this,RC_APP_UPDATE);
                  } catch (IntentSender.SendIntentException e) {
                      e.printStackTrace();
                  }
              }
          }
      });
        binding.llSignIn.setOnClickListener(this);

        binding.imginVisible.setOnClickListener(this);
        binding.imgVisible.setOnClickListener(this);

      binding.ckRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
              if (isChecked) {
                  pref.saveCheckFlag("1");
              } else {
                  pref.saveCheckFlag("2");
              }
          }
      });
      try {
          PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
          version = pInfo.versionName;
          int verCode = pInfo.versionCode;

      } catch (PackageManager.NameNotFoundException e) {
          e.printStackTrace();
      }


  }

    @Override
    public void onClick(View view) {
        if (view==binding.llSignIn){
           if (binding.etUserId.getText().toString().length()>0){
               if (binding.etPassword.getText().toString().length()>0){
                   Intent intent=new Intent(HRLoginActivity.this,HRDashboardActivity.class);
                   startActivity(intent);
                   finish();
               }else {
                   Toast.makeText(HRLoginActivity.this,"Please enter Password",Toast.LENGTH_LONG).show();
               }

           }else {
               Toast.makeText(HRLoginActivity.this,"Please enter User ID",Toast.LENGTH_LONG).show();
           }
        }
        else if (view==binding.imgVisible){
            binding.imginVisible.setVisibility(View.VISIBLE);
            binding.imgVisible.setVisibility(View.GONE);
            binding.etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }else if (view==binding.imginVisible){
            binding.imgVisible.setVisibility(View.VISIBLE);
            binding.imginVisible.setVisibility(View.GONE);
            binding.etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        }

    }


    

    

    

   

   

}