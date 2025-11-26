package io.cordova.myapp00d753.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import io.cordova.myapp00d753.R;

public class MaintainceBreakActivity extends AppCompatActivity {
    TextView tvText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintaince_break);
        tvText=(TextView) findViewById(R.id.tvText);
        String breakText=getIntent().getStringExtra("breakText");
        tvText.setText(breakText);
    }
}