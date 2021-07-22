package io.cordova.myapp00d753.activity;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.AutoCompleteModule;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

public class KYCOfficialFormActivity extends AppCompatActivity {
    LinearLayout llNext;
    AutoCompleteTextView etDept;
    ArrayList<AutoCompleteModule>mainDept=new ArrayList<>();
    ArrayList<String>depart=new ArrayList<>();
    String departMentEditText="";
    Pref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kyc_official_form);
        initialize();

        onClick();
    }

    private void initialize(){
        pref=new Pref(KYCOfficialFormActivity.this);
        llNext=(LinearLayout)findViewById(R.id.llNext);
        etDept=(AutoCompleteTextView)findViewById(R.id.etDept);

    }

    private void onClick(){
        llNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(KYCOfficialFormActivity.this,KYCPersonalActivity.class);
                startActivity(intent);
            }
        });

        etDept.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etDept.getText().toString().length()==1){
                    departMentEditText=etDept.getText().toString();
                    setDeptName();

                }

            }
        });
    }

    private void setDeptName() {

        String surl = AppData.url+"gcl_CommonDDL?ddltype=100&id1="+pref.getEmpConId()+"&id2="+departMentEditText+"&id3=0&SecurityCode=" + pref.getSecurityCode();
        Log.d("deptname",surl);
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        progressBar.dismiss();
                        depart.clear();
                        mainDept.clear();


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                //Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String deptvalue = obj.optString("value");
                                    String id = obj.optString("id");
                                    mainDept.add(new AutoCompleteModule(id, deptvalue));
                                    depart.add(deptvalue);
                                    // clientname.add(value);


                                }


                                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                        (KYCOfficialFormActivity.this, R.layout.autocomplete_layout,R.id.autoCompleteItem, depart);
                                //Getting the instance of AutoCompleteTextView
                                etDept.setThreshold(2);//will start working from first character
                                etDept.setAdapter(adapter);



                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(KYCOfficialFormActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();

                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");


    }



}
