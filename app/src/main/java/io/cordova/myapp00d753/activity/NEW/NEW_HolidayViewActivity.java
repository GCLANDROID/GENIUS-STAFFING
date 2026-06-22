package io.cordova.myapp00d753.activity.NEW;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.EmployeeDashBoardActivity;
import io.cordova.myapp00d753.activity.NEW.fragment.HolidayViewFragment;
import io.cordova.myapp00d753.activity.SKF.adapter.HolidayViewAdapter;
import io.cordova.myapp00d753.fragment.ApplicationFragment;
import io.cordova.myapp00d753.module.HolidayMarkModel;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.DateCalculation;
import io.cordova.myapp00d753.utility.Pref;

public class NEW_HolidayViewActivity extends AppCompatActivity {
    private static final String TAG = "HolidayViewActivity";
    RecyclerView rvHolidayView;
    Pref pref;
    LinearLayout llNoData,llNormalHoliday,llOptionalHoliday;
    ImageView imgBack,imgHome;
    TextView tvOptionalHoliday,tvNormalHoliday;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_holiday_view);
        initView();
        btnClick();
        loadNormalHolidayView();
    }

    private void initView() {
        pref = new Pref(this);
        imgBack = findViewById(R.id.imgBack);
        imgHome = findViewById(R.id.imgHome);
        llNormalHoliday = findViewById(R.id.llNormalHoliday);
        llOptionalHoliday = findViewById(R.id.llOptionalHoliday);
        llNoData = findViewById(R.id.llNoData);
        tvOptionalHoliday = findViewById(R.id.tvOptionalHoliday);
        tvNormalHoliday = findViewById(R.id.tvNormalHoliday);
    }

    private void btnClick() {
        llNormalHoliday = findViewById(R.id.llNormalHoliday);
        llOptionalHoliday = findViewById(R.id.llOptionalHoliday);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        llNormalHoliday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadNormalHolidayView();
            }
        });
        llOptionalHoliday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadOptionalHolidayView();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NEW_HolidayViewActivity.this, EmployeeDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadNormalHolidayView() {
        llNormalHoliday.setBackgroundResource(R.drawable.background_7);
        llOptionalHoliday.setBackgroundResource(0);
        tvOptionalHoliday.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
        tvNormalHoliday.setTextColor(ContextCompat.getColor(this, R.color.white));
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        HolidayViewFragment pfragment = new HolidayViewFragment().newInstance("Normal");
        transaction.replace(R.id.fragmentContainer, pfragment);
        transaction.commit();
    }

    @SuppressLint("ResourceAsColor")
    private void loadOptionalHolidayView() {
        llOptionalHoliday.setBackgroundResource(R.drawable.background_7);
        llOptionalHoliday.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e96d70")));
        llNormalHoliday.setBackgroundResource(0);
        tvNormalHoliday.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
        tvOptionalHoliday.setTextColor(ContextCompat.getColor(this, R.color.white));
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        HolidayViewFragment pfragment = new HolidayViewFragment().newInstance("Optional");
        transaction.replace(R.id.fragmentContainer, pfragment);
        transaction.commit();
    }


}