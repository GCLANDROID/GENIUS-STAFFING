package io.cordova.myapp00d753.facereogntion;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.android.volley.DefaultRetryPolicy;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.databinding.ActivityCandidateLoginBinding;

public class CandidateLoginActivity extends AppCompatActivity {
    ActivityCandidateLoginBinding binding;
    ArrayList<String> idList = new ArrayList<>();
    String idProof = "";
    int LoginFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_candidate_login);
        initView();
    }

    private void initView() {
        LoginFlag=getIntent().getIntExtra("LoginFlag",0);
        Log.d("LoginFlag", String.valueOf(LoginFlag));
        idList.add("Please Select");
        idList.add("Aadhaar Card");
        idList.add("PAN Card");


        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>
                (CandidateLoginActivity.this, android.R.layout.simple_spinner_item,
                        idList); //selected item will look like a spinner set from XML
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spID.setAdapter(typeAdapter);

        binding.spID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    idProof = idList.get(i);
                    binding.tvRef.setText(idProof + " Number :");
                    binding.lnRef.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.etRefNo.getText().toString().length() > 0) {
                    if (idProof.contains("Aadhaar")) {
                        AddharValidation();
                    } else {
                        PANValidation();
                    }
                } else {
                    Toast.makeText(CandidateLoginActivity.this, "Please Enter " + idProof + "'s Number", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    private void AddharValidation() {
        if (isValidAadhaarNumber(binding.etRefNo.getText().toString()) || isValidAadhaarNumberr(binding.etRefNo.getText().toString())) {
            login();
        } else {
            Toast.makeText(CandidateLoginActivity.this, "Please Enter Valid Aadhaar Number", Toast.LENGTH_LONG).show();
        }
    }

    private void PANValidation() {
        if (isValidPANNumber(binding.etRefNo.getText().toString())) {
            login();

        } else {
            Toast.makeText(CandidateLoginActivity.this, "Please Enter Valid PAN Number", Toast.LENGTH_LONG).show();
        }
    }

    public boolean isValidAadhaarNumber(String text) {
        // Regex to check valid Aadhaar number.
        String regex
                = "^[2-9]{1}[0-9]{3}\\s[0-9]{4}\\s[0-9]{4}$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the string is empty
        // return false
        if (text == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given string
        // and regular expression.
        Matcher m = p.matcher(text);

        // Return if the string
        // matched the ReGex
        return m.matches();
    }

    public boolean isValidAadhaarNumberr(String text) {
        // Regex to check valid Aadhaar number.
        String regex
                = "^[2-9]{1}[0-9]{3}[0-9]{4}[0-9]{4}$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the string is empty
        // return false
        if (text == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given string
        // and regular expression.
        Matcher m = p.matcher(text);

        // Return if the string
        // matched the ReGex
        return m.matches();
    }


    public static boolean isValidPANNumber(String str) {
        // Regex to check valid EPIC Number
        String regex = "[A-Z]{5}[0-9]{4}[A-Z]{1}";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the str is empty return false
        if (str == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given
        // EPIC Number using regular expression.
        Matcher m = p.matcher(str);

        // Return if the str
        // matched the ReGex
        return m.matches();
    }


    public void login() {

        String surl = "http://gsppi.geniusconsultant.com/GeniusiOSApi/api/get_SBICardCandidate?CandidateID=0&ReferenceNo=" + binding.etRefNo.getText().toString() + "&Operation=1";
        Log.d("inputLogin", surl);

        final ProgressDialog progressDialog = new ProgressDialog(CandidateLoginActivity.this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        progressDialog.dismiss();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                                JSONArray responseData = job1.optJSONArray("responseData");

                                JSONObject obj = responseData.getJSONObject(0);
                                String CandidateID = obj.optString("CandidateID");
                                String EmployeeName = obj.optString("EmployeeName");
                                String MobileNumber = obj.optString("MobileNumber");
                                String Fathername = obj.optString("Fathername");
                                String DocumentName = obj.optString("DocumentName");
                                String DocumentID = obj.optString("DocumentID");
                                String ReferenceNo = obj.optString("ReferenceNo");
                                String DOB=obj.optString("DOB");
                                 if (LoginFlag==1) {
                                     Intent intent = new Intent(CandidateLoginActivity.this, InterviewFormActivity.class);
                                     intent.putExtra("CandidateID", CandidateID);
                                     intent.putExtra("EmployeeName", EmployeeName);
                                     intent.putExtra("MobileNumber", MobileNumber);
                                     intent.putExtra("Fathername", Fathername);
                                     intent.putExtra("DocumentName", DocumentName);
                                     intent.putExtra("DocumentID", DocumentID);
                                     intent.putExtra("ReferenceNo", ReferenceNo);
                                     intent.putExtra("DOB", DOB);
                                     startActivity(intent);
                                     finish();
                                 }else {
                                     Intent intent = new Intent(CandidateLoginActivity.this, EmpDashboardActivity.class);
                                     intent.putExtra("CandidateID", CandidateID);
                                     intent.putExtra("EmployeeName", EmployeeName);
                                     intent.putExtra("MobileNumber", MobileNumber);
                                     intent.putExtra("Fathername", Fathername);
                                     intent.putExtra("DocumentName", DocumentName);
                                     intent.putExtra("DocumentID", DocumentID);
                                     intent.putExtra("ReferenceNo", ReferenceNo);
                                     intent.putExtra("DOB", DOB);
                                     startActivity(intent);
                                     finish();
                                 }


                            } else {

                                Toast.makeText(CandidateLoginActivity.this, "Sorry! Invalid Reference Number", Toast.LENGTH_LONG).show();

                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CandidateLoginActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                //  Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();


            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(CandidateLoginActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                300000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


}