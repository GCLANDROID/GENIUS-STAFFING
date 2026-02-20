package io.cordova.myapp00d753.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import io.cordova.myapp00d753.R;

public class E_Pin_SetupLoginActivity extends AppCompatActivity {
    Button btnLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_pin_setup_login);
        initView();
    }

    private void initView() {
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(E_Pin_SetupLoginActivity.this, MPIN_SetupActivity.class);
                startActivity(intent);
            }
        });
    }
}
