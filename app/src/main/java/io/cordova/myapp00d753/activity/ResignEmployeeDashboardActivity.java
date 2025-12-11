package io.cordova.myapp00d753.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.MenuItemAdapter;
import io.cordova.myapp00d753.adapter.NotiAdapter;
import io.cordova.myapp00d753.databinding.ActivityResignEmployeeDashBoardBinding;
import io.cordova.myapp00d753.module.MenuItemModel;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.ClientID;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;
import io.cordova.myapp00d753.utility.Pref;

public class ResignEmployeeDashboardActivity extends AppCompatActivity {
   ActivityResignEmployeeDashBoardBinding binding;
   Pref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_resign_employee_dash_board);
        initialize();

    }

    private void initialize() {
        pref=new Pref(ResignEmployeeDashboardActivity.this);
        binding.tvEmpName.setText(pref.getEmpName());
        binding.llPayroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ResignEmployeeDashboardActivity.this,ResignEmpSalaryActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });



    }


}
