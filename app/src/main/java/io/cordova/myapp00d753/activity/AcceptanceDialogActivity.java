package io.cordova.myapp00d753.activity;

import android.content.Intent;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;


public class AcceptanceDialogActivity extends AppCompatActivity {
    TextView tvClick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.acceptancedialog);
        this.setFinishOnTouchOutside(false);
        initialize();

    }

    private void initialize() {
        tvClick=(TextView)findViewById(R.id.tvClick);

    }


}
