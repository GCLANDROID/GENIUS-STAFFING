package io.cordova.myapp00d753.activity.honnasa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.databinding.ActivityHonasaSalesDashboardBinding;

public class HonasaSalesDashboardActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityHonasaSalesDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_honasa_sales_dashboard);
        initView();
    }
    
    private void initView(){
        binding.llSalesManage.setOnClickListener(this);
        binding.llSalesReport.setOnClickListener(this);

        binding.imgBack.setOnClickListener(this);
        binding.imhHome.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view==binding.llSalesManage){
            Intent intent=new Intent(HonasaSalesDashboardActivity.this,HonasaSalesActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if (view==binding.llSalesReport){
            Intent intent=new Intent(HonasaSalesDashboardActivity.this,HanasaSalesReportActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if (view==binding.imgBack){
           onBackPressed();
        }else if (view==binding.imhHome){
            onBackPressed();
        }
        
    }
}