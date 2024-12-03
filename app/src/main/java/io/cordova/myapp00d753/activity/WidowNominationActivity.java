package io.cordova.myapp00d753.activity;

import static java.util.Calendar.DAY_OF_MONTH;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.databinding.ActivityWidowNominationBinding;
import io.cordova.myapp00d753.module.EPSmodel;
import io.cordova.myapp00d753.module.MainDocModule;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.Util;

public class WidowNominationActivity extends AppCompatActivity {
    private static final String TAG = "WidowNominationActivity";
    ActivityWidowNominationBinding binding;
    Pref pref;
    ProgressDialog pd;
    ArrayList<MainDocModule> mainRealation = new ArrayList<>();
    ArrayList<String> realation = new ArrayList<>();
    String relationship="";
    String relationshipID="";
    String month;
    String dob="";
    JSONObject nominationobject=new JSONObject();
    JSONArray nominationarray=new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_widow_nomination);
        onClick();
        setNomineeRelation();
    }

    private void onClick(){
        pref=new Pref(WidowNominationActivity.this);
        pd=new ProgressDialog(WidowNominationActivity.this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        binding.btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(WidowNominationActivity.this,GratuityNominationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                binding.etName.setText("");
                binding.etAadharNominee.setText("");
                binding.etAddress.setText("");
                binding.imgTick.setVisibility(View.GONE);
                binding.spRealation.setSelection(0);

            }
        });
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WidowNominationActivity.this, TempDashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        binding.spRealation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //if (i>0){
                    relationshipID=mainRealation.get(i).getDocID();
                    relationship=mainRealation.get(i).getDocumentType();
                    binding.llRelationship.setBackgroundResource(R.drawable.lldesign9);
                //}
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.llTick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.imgTick.getVisibility()==View.GONE){
                    binding.imgTick.setVisibility(View.VISIBLE);
                    binding.etAddress.setText(AppData.PERMANENTADDRESS);
                    binding.etAddress.setBackgroundResource(R.drawable.lldesign9);
                }else {
                    binding.imgTick.setVisibility(View.GONE);
                    binding.etAddress.setText("");
                }
            }
        });
        binding.imgUANCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                System.out.println("Current date : " + (now.get(Calendar.MONTH) + 1) + "-"
                        + now.get(Calendar.DATE) + "-" + now.get(Calendar.YEAR));

                now = Calendar.getInstance();
                now.add(Calendar.YEAR, -18);
                int dyear = now.get(Calendar.YEAR);
                final int dmonth = now.get(Calendar.MONTH);
                int dday = now.get(DAY_OF_MONTH);
                Calendar c1 = Calendar.getInstance();
                /*final int syear = year - 18;

                final int month1 = c1.get(Calendar.MONTH);
                final int sday1 = c1.get(DAY_OF_MONTH);*/


                final DatePickerDialog dialog = new DatePickerDialog(WidowNominationActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {

                        int mm = (m + 1);
                        int s = (m + 1) + d + y;
                        if (mm == 1) {
                            month = "January";
                        } else if (mm == 2) {
                            month = "February";
                        } else if (mm == 3) {
                            month = "March";
                        } else if (mm == 4) {
                            month = "April";
                        } else if (mm == 5) {
                            month = "May";
                        } else if (mm == 6) {
                            month = "June";
                        } else if (mm == 7) {
                            month = "July";
                        } else if (mm == 8) {
                            month = "August";
                        } else if (mm == 9) {
                            month = "September";
                        } else if (mm == 10) {
                            month = "October";
                        } else if (mm == 11) {
                            month = "November";
                        } else if (mm == 12) {
                            month = "December";
                        }

                        dob = d + " " + month + " " + y;
                        binding.tvUANDOB.setText(Util.changeAnyDateFormat(dob,"dd MMMM yyyy","dd MMM yy"));
                    }
                }, dyear, dmonth, dday);
                dialog.getDatePicker().setMaxDate((long) (System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 365.25 * 18)));
                dialog.getDatePicker().setMinDate((long) (System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 365.25 * 90)));
                dialog.show();
            }
        });
        binding.btnSaveForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.etName.getText().toString().length()>0){
                    if (binding.etAddress.getText().toString().length()>0){
                        if (binding.etAadharNominee.getText().toString().length()>0){
                            if (!dob.isEmpty()){
                                if(!relationshipID.isEmpty()){
                                    JSONObject jsonObject=new JSONObject();
                                    try {
                                        jsonObject.put("Name",binding.etName.getText().toString());
                                        jsonObject.put("Address",binding.etAddress.getText().toString());
                                        jsonObject.put("Aadhar",binding.etAadharNominee.getText().toString());
                                        jsonObject.put("Relationship",relationshipID);
                                        jsonObject.put("DOB",dob);
                                        jsonObject.put("AEMEMPLOYEEID",pref.getEmpId());
                                        nominationarray.put(jsonObject);
                                        nominationobject.put("widowDetails",nominationarray);
                                        nominationobject.put("DbOperation","8");
                                        nominationobject.put("SecurityCode",pref.getSecurityCode());
                                        uploadfamilydetails(nominationobject);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Toast.makeText(WidowNominationActivity.this, "Please Enter Relationship type with Nominee", Toast.LENGTH_LONG).show();
                                    binding.llRelationship.setBackgroundResource(R.drawable.lldesign_error);
                                }
                            } else {
                                Toast.makeText(WidowNominationActivity.this, "Please Select Date of Birth of Nominee", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(WidowNominationActivity.this, "Please Enter Nominee's Aadhar Card No.", Toast.LENGTH_LONG).show();
                            binding.etAadharNominee.setBackgroundResource(R.drawable.lldesign_error);
                        }
                    } else {
                        Toast.makeText(WidowNominationActivity.this, "Please Enter Address of the Nominee", Toast.LENGTH_LONG).show();
                        binding.etAddress.setBackgroundResource(R.drawable.lldesign_error);
                    }
                }else {
                    Toast.makeText(WidowNominationActivity.this,"Please Enter Family Member's Name",Toast.LENGTH_LONG).show();
                    binding.etName.setBackgroundResource(R.drawable.lldesign_error);
                }
            }
        });

        binding.etAadharNominee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (binding.etAadharNominee.getText().length()==12){
                    JSONObject jsonObject=new JSONObject();
                    try {
                        jsonObject.put("aadhaarNumber",binding.etAadharNominee.getText().toString());
                        jsonObject.put("consent","Y");
                        //validateAadhar(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        });

        binding.etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0){
                    binding.etName.setBackgroundResource(R.drawable.lldesign9);
                }
            }
        });

        binding.etAadharNominee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0){
                    binding.etAadharNominee.setBackgroundResource(R.drawable.lldesign9);
                }
            }
        });

        binding.etAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0){
                    binding.etAddress.setBackgroundResource(R.drawable.lldesign_error);
                }
            }
        });
    }


    private void uploadfamilydetails(JSONObject jsonObject) {
        pd.show();
        AndroidNetworking.post(AppData.newv2url+"KYC/UpdateKYCDetails")
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        pd.dismiss();

                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            Intent intent = new Intent(WidowNominationActivity.this, GratuityNominationActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            Toast.makeText(WidowNominationActivity.this,"Widow Pension Details has been updated Successfully",Toast.LENGTH_LONG).show();

                        }else {

                        }
                    }

                    @Override
                    public void onError(ANError error) {

                            Intent intent=new Intent(WidowNominationActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();

                    }
                });
    }

    private void setNomineeRelation() {
        String surl = AppData.url+"gcl_CommonDDL?ddltype=7&id1=0&id2=0&id3=0&SecurityCode=" + pref.getSecurityCode();
        ProgressDialog pd=new ProgressDialog(WidowNominationActivity.this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        pd.dismiss();
                        realation.clear();
                        mainRealation.clear();
                        //realation.add("Please Select");
                        //mainRealation.add(new MainDocModule("0",""));

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
                                    if (obj.optString("value").equalsIgnoreCase("Wife")){
                                        String value = obj.optString("value");
                                        String id = obj.optString("id");
                                        realation.add(value);
                                        MainDocModule mainDocModule = new MainDocModule(id, value);
                                        mainRealation.add(mainDocModule);
                                    }
                                }
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (WidowNominationActivity.this, android.R.layout.simple_spinner_item,
                                                realation); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                binding.spRealation.setAdapter(spinnerArrayAdapter);
                                //binding.spRealation.setSelection(0);

                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("AEMConsultantID", pref.getEmpConId());
                                    jsonObject.put("AEMClientID", pref.getEmpClintId());
                                    jsonObject.put("AEMClientOfficeID", pref.getEmpClintOffId());
                                    jsonObject.put("AEMEmployeeID", pref.getEmpId());
                                    jsonObject.put("WorkingStatus", "1");
                                    jsonObject.put("Operation", "8");
                                    getWidowPensionDetails(jsonObject);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(WidowNominationActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();

                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());

            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");


    }

    private void validateAadhar(JSONObject jsonObject) {
        ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.show();
        pd.setCancelable(false);
        AndroidNetworking.post("https://ind.thomas.hyperverge.co/v1/verifyAadhaar")
                .addJSONObjectBody(jsonObject)
                .addHeaders("appId", AppData.APPID)
                .addHeaders("appKey", AppData.APPKEY)
                .addHeaders("transactionId", pref.getMasterId())
                .addHeaders("content-type", "application/json")
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        pd.dismiss();

                        int statusCode = job1.optInt("statusCode");
                        if (statusCode == 200) {

                            binding.llAadharVerified.setVisibility(View.VISIBLE);
                            binding.etAadharNominee.setEnabled(false);
                        }else {

                            binding.etAadharNominee.setText("");
                            Toast.makeText(WidowNominationActivity.this,"Sorry!Invalid Aadhar Number",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();

                        Toast.makeText(WidowNominationActivity.this,"Sorry!Invalid Aadhar Number",Toast.LENGTH_LONG).show();
                        binding.etAadharNominee.setText("");



                    }
                });
    }

    private void getWidowPensionDetails(JSONObject jsonObject) {
        Log.e(TAG, "getWidowPensionDetails: INPUT: "+jsonObject);
        pd.show();
        AndroidNetworking.post(AppData.KYC_GET_DETAILS)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "GET_WIDOW_NOMINATION_DETAILS: "+response.toString(4));
                            pd.dismiss();
                            JSONObject job1 = response;
                            int Response_Code = job1.optInt("Response_Code");
                            if (Response_Code == 101) {
                                String Response_Data = job1.optString("Response_Data");
                                JSONObject job2 = new JSONObject(Response_Data);
                                JSONArray jsonArray = job2.getJSONArray("WIDOWDetails");
                                JSONObject job3 = jsonArray.getJSONObject(0);
                                binding.etName.setText(job3.optString("MemberName"));
                                binding.etAddress.setText(job3.optString("NomineeAddress"));
                                binding.etAddress.setBackgroundResource(R.drawable.lldesign9);
                                binding.etAadharNominee.setText(job3.optString("MemberAadhar"));
                                dob = job3.optString("MemberDOB");
                                binding.tvUANDOB.setText(dob);
                                int index = realation.indexOf(job3.optString("Relation"));
                                binding.spRealation.setSelection(index);
                            } else {

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        pd.dismiss();
                        Log.e(TAG, "GET_WIDOW_NOMINATION_DETAILS_onError: "+anError.getErrorBody());
                        Toast.makeText(WidowNominationActivity.this, "Something went to wrong", Toast.LENGTH_LONG).show();
                    }
                });
    }

}