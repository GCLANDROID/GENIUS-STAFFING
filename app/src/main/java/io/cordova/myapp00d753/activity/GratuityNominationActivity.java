package io.cordova.myapp00d753.activity;

import static java.util.Calendar.DAY_OF_MONTH;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import io.cordova.myapp00d753.adapter.EPSNominationAdapter;
import io.cordova.myapp00d753.adapter.GratuityNominationAdapter;
import io.cordova.myapp00d753.databinding.ActivityGratuityNominationBinding;
import io.cordova.myapp00d753.module.EPSmodel;
import io.cordova.myapp00d753.module.GratuityModel;
import io.cordova.myapp00d753.module.MainDocModule;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

public class GratuityNominationActivity extends AppCompatActivity {
    ActivityGratuityNominationBinding binding;
    JSONObject nominationobject=new JSONObject();
    JSONArray nominationarray=new JSONArray();
    ArrayList<GratuityModel> itemList=new ArrayList<>();
    LinearLayoutManager layoutManager;
    Pref pref;
    ProgressDialog pd;
    ArrayList<MainDocModule> mainRealation = new ArrayList<>();
    ArrayList<String> realation = new ArrayList<>();
    String relationship="";
    String relationshipID="";
    String month;
    String dob="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_gratuity_nomination);
        initView();
        setNomineeRelation();


    }

    private void initView(){
        pref=new Pref(GratuityNominationActivity.this);
        pd=new ProgressDialog(GratuityNominationActivity.this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        layoutManager
                = new LinearLayoutManager(GratuityNominationActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvData.setLayoutManager(layoutManager);

        binding.llTick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.imgTick.getVisibility()==View.GONE){
                    binding.imgTick.setVisibility(View.VISIBLE);
                    binding.etAddress.setText(AppData.PERMANENTADDRESS);
                }else {
                    binding.imgTick.setVisibility(View.GONE);
                    binding.etAddress.setText("");
                }
            }
        });

        binding.spRealation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>0){
                    relationshipID=mainRealation.get(i).getDocID();
                    relationship=mainRealation.get(i).getDocumentType();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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


                final DatePickerDialog dialog = new DatePickerDialog(GratuityNominationActivity.this, new DatePickerDialog.OnDateSetListener() {
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

                        binding.tvUANDOB.setText(dob);


                    }
                }, dyear, dmonth, dday);
                dialog.getDatePicker().setMaxDate((long) (System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 365.25 * 18)));
                dialog.getDatePicker().setMinDate((long) (System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 365.25 * 90)));
                dialog.show();

            }
        });
        binding.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.etName.getText().toString().length()>0){
                    JSONObject jsonObject=new JSONObject();
                    try {
                        jsonObject.put("Name",binding.etName.getText().toString());
                        jsonObject.put("Aadhar",binding.etAadharNominee.getText().toString());
                        jsonObject.put("Address",binding.etAddress.getText().toString());
                        jsonObject.put("Relationship",relationshipID);
                        jsonObject.put("RelationshipID",relationship);
                        jsonObject.put("DOB",dob);
                        jsonObject.put("Proportion",binding.etProportion.getText().toString());
                        jsonObject.put("AEMEMPLOYEEID",pref.getEmpId());
                        nominationarray.put(jsonObject);
                        nominationobject.put("gratuityDetails",nominationarray);
                        nominationobject.put("DbOperation","9");
                        nominationobject.put("SecurityCode",pref.getSecurityCode());
                        getItemList(nominationobject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(GratuityNominationActivity.this,"Please Enter Family Member's Name",Toast.LENGTH_LONG).show();
                }

            }
        });

        binding.btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(GratuityNominationActivity.this,TempEducationaActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        binding.btnSaveForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemList.size()>0){
                    uploadfamilydetails(nominationobject);
                }else {
                    Toast.makeText(GratuityNominationActivity.this,"Please Add Family Member",Toast.LENGTH_LONG).show();
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
    }

    private void getItemList(JSONObject object){
        binding.etName.setText("");
        binding.etAddress.setText("");
        binding.imgTick.setVisibility(View.GONE);
        binding.tvUANDOB.setText("");
        dob="";
        binding.etAadharNominee.setText("");
        binding.spRealation.setSelection(0);
        binding.etProportion.setText("");
        binding.llData.setVisibility(View.VISIBLE);
        itemList.clear();


        JSONArray nomination=object.optJSONArray("gratuityDetails");
        for (int i=0;i<nomination.length();i++){
            JSONObject nomiobj=nomination.optJSONObject(i);
            String Name=nomiobj.optString("Name");
            String Address=nomiobj.optString("Address");
            String Relationship=nomiobj.optString("RelationshipID");
            String Age=nomiobj.optString("DOB");
            String Proportion=nomiobj.optString("Proportion");
            GratuityModel epSmodel=new GratuityModel();
            epSmodel.setName(Name);
            epSmodel.setAddress(Address);
            epSmodel.setAge(Age);
            epSmodel.setRelationship(Relationship);
            epSmodel.setPortion(Proportion);

            itemList.add(epSmodel);
        }
        GratuityNominationAdapter nominationAdapter=new GratuityNominationAdapter(itemList);
        binding.rvData.setAdapter(nominationAdapter);




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
                            Intent intent = new Intent(GratuityNominationActivity.this, TempEducationaActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            Toast.makeText(GratuityNominationActivity.this,"Gratuity Details has been updated Successfully",Toast.LENGTH_LONG).show();

                        }else {

                        }
                    }

                    @Override
                    public void onError(ANError error) {

                            Intent intent=new Intent(GratuityNominationActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();

                    }
                });
    }


    private void setNomineeRelation() {

        String surl = AppData.url+"gcl_CommonDDL?ddltype=7&id1=0&id2=0&id3=0&SecurityCode=" + pref.getSecurityCode();
        ProgressDialog pd=new ProgressDialog(GratuityNominationActivity.this);
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
                        realation.add("Please Select");
                        mainRealation.add(new MainDocModule("0",""));

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
                                    realation.add(value);
                                    MainDocModule mainDocModule = new MainDocModule(id, value);
                                    mainRealation.add(mainDocModule);

                                }
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (GratuityNominationActivity.this, android.R.layout.simple_spinner_item,
                                                realation); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                binding.spRealation.setAdapter(spinnerArrayAdapter);



                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(GratuityNominationActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(GratuityNominationActivity.this,"Sorry!Invalid Aadhar Number",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();

                        Toast.makeText(GratuityNominationActivity.this,"Sorry!Invalid Aadhar Number",Toast.LENGTH_LONG).show();
                        binding.etAadharNominee.setText("");


                    }
                });
    }

}