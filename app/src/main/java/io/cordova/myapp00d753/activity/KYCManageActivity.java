package io.cordova.myapp00d753.activity;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import io.cordova.myapp00d753.module.MainDocModule;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

public class KYCManageActivity extends AppCompatActivity {
    LinearLayout llEntry;
    Spinner spClentName,spClentBranch;
    //client array
    ArrayList<AutoCompleteModule> mainClientName = new ArrayList<>();
    ArrayList<String> clientname = new ArrayList<>();

    //client office
    ArrayList<MainDocModule> mainClientBranch = new ArrayList<>();
    ArrayList<String> clientbranch = new ArrayList<>();

    Pref pref;
    AutoCompleteTextView etClientName;
    String clintName="0";
    String branchName="";
    String clintId="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kycmanage);
        initialize();
        setClientName();
        onClick();
    }

    private void initialize(){
        pref=new Pref(KYCManageActivity.this);
        llEntry=(LinearLayout)findViewById(R.id.llEntry);

        spClentName=(Spinner)findViewById(R.id.spClentName);
        spClentBranch=(Spinner)findViewById(R.id.spClentBranch);

        etClientName=(AutoCompleteTextView)findViewById(R.id.etClientName);
      //  setClientName();
    }

    private void onClick(){
        llEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(KYCManageActivity.this,KYCOfficialFormActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        etClientName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etClientName.getText().toString().length()==2){
                    clintName=etClientName.getText().toString();


                }


            }
        });

        etClientName.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {

                clintId = mainClientName.get(pos).getClientid();
                Log.d("idPatient",clintId);


            }

        });

        spClentBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                branchName=mainClientBranch.get(position).getDocID();
                Log.d("branchName",branchName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setClientName() {

        String surl = AppData.url+"gcl_CommonDDL?ddltype=1200&id1="+pref.getEmpConId()+"&id2="+pref.getMasterId()+"&id3="+clintName+"&SecurityCode=" + pref.getSecurityCode();
        Log.d("clintname",surl);
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
                        clientname.clear();
                        mainClientName.clear();


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
                                    String clintvalue = obj.optString("value");
                                    String id = obj.optString("id");
                                    mainClientName.add(new AutoCompleteModule(id, clintvalue));
                                    clientname.add(clintvalue);
                                   // clientname.add(value);


                                }
                                setClientBranch();

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                        (KYCManageActivity.this, R.layout.autocomplete_layout,R.id.autoCompleteItem, clientname);
                                //Getting the instance of AutoCompleteTextView
                                etClientName.setThreshold(2);//will start working from first character
                                etClientName.setAdapter(adapter);



                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(KYCManageActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
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

    private void setClientBranch() {

        String surl = AppData.url+"gcl_CommonDDL?ddltype=13&id1="+pref.getMasterId()+"&id2="+pref.getPassword()+"&id3=0&SecurityCode=" + pref.getSecurityCode();
        Log.d("clintbranch",surl);
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
                                    String value = obj.optString("value");
                                    String id = obj.optString("id");
                                    clientbranch.add(value);
                                    MainDocModule mainDocModule = new MainDocModule(id, value);
                                    mainClientBranch.add(mainDocModule);

                                }
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (KYCManageActivity.this, android.R.layout.simple_spinner_item,
                                                clientbranch); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spClentBranch.setAdapter(spinnerArrayAdapter);


                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(KYCManageActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
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

    private void setClientOffice() {

        String surl = AppData.url+"gcl_CommonDDL?ddltype=14&id1="+pref.getMasterId()+"&id2="+pref.getPassword()+"&id3=0&SecurityCode=" + pref.getSecurityCode();
        Log.d("clintbranch",surl);
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
                                    String value = obj.optString("value");
                                    String id = obj.optString("id");
                                    clientbranch.add(value);
                                    MainDocModule mainDocModule = new MainDocModule(id, value);
                                    mainClientBranch.add(mainDocModule);

                                }
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (KYCManageActivity.this, android.R.layout.simple_spinner_item,
                                                clientbranch); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spClentBranch.setAdapter(spinnerArrayAdapter);


                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(KYCManageActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
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
