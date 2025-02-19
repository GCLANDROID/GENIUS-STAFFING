package io.cordova.myapp00d753.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.List;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.EPSNominationAdapter;
import io.cordova.myapp00d753.adapter.EducationAdapter;
import io.cordova.myapp00d753.adapter.GratuityNominationAdapter;
import io.cordova.myapp00d753.databinding.ActivityEpsnominationBinding;
import io.cordova.myapp00d753.databinding.ActivityTempEducationaBinding;
import io.cordova.myapp00d753.module.EPSmodel;
import io.cordova.myapp00d753.module.EducationalModel;
import io.cordova.myapp00d753.module.GratuityModel;
import io.cordova.myapp00d753.module.MainDocModule;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class TempEducationaActivity extends AppCompatActivity {
    private static final String TAG = "TempEducationaActivity";
    ActivityTempEducationaBinding binding;
    JSONObject educationobj=new JSONObject();
    JSONArray educationarray=new JSONArray();
    ArrayList<EducationalModel> itemList=new ArrayList<>();
    LinearLayoutManager layoutManager;
    ArrayList<String>qualificationlist=new ArrayList<>();
    String qualificationid="";
    String qualification="";
    Pref pref;
    ArrayList<MainDocModule>mainQualification=new ArrayList<>();
    ProgressDialog pd;
    ArrayList<String>yearlist=new ArrayList<>();
    String passingyear="";
    EducationAdapter educationAdapter;
    boolean isEditClick = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_temp_educationa);
        initView();
        //setQualification();

        JSONObject obj=new JSONObject();
        try {
            obj.put("ddltype", 6);
            obj.put("SecurityCode",pref.getSecurityCode());
            setQualification(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView(){
        pd=new ProgressDialog(TempEducationaActivity.this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pref=new Pref(TempEducationaActivity.this);

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        // Create a list of years from 1947 to the current year
        yearlist.add("Please Select");

        for (int year = 1974; year <= currentYear; year++) {
            yearlist.add(String.valueOf(year));
        }

        ArrayAdapter yearAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, yearlist);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spYear.setAdapter(yearAdapter);



        layoutManager = new LinearLayoutManager(TempEducationaActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvData.setLayoutManager(layoutManager);


        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (TempEducationaActivity.this, android.R.layout.simple_spinner_item,
                        qualificationlist); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spExam.setAdapter(spinnerArrayAdapter);

        binding.spExam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>0) {
                    qualificationid = mainQualification.get(i).getDocID();
                    qualification=mainQualification.get(i).getDocumentType();
                    Log.e(TAG, "onItemSelected: "+qualificationid);
                    Log.e(TAG, "onItemSelected: "+qualification);
                    binding.llQualification.setBackgroundResource(R.drawable.lldesign9);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
                Intent intent = new Intent(TempEducationaActivity.this, TempDashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        binding.spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>0){
                    passingyear=yearlist.get(i);
                    binding.llPassingyear.setBackgroundResource(R.drawable.lldesign9);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });

        final SimpleTooltip tooltip = new SimpleTooltip.Builder(TempEducationaActivity.this)
                .anchorView(binding.imgAdd)
                .text("Fill in the fields, then press the \"+\" icon to add your data to the list.")
                .gravity(Gravity.BOTTOM)
                .dismissOnOutsideTouch(true)
                .dismissOnInsideTouch(true)
                .modal(true)
                .animated(true)
                .animationDuration(2000)
                .contentView(R.layout.custom_tooltip, R.id.tv_text)
                .focusable(true)
                .build();

        tooltip.findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                if (tooltip.isShowing())
                    tooltip.dismiss();
            }
        });

        tooltip.show();


        binding.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!qualification.equals("")) {
                    if (binding.etUniversity.getText().toString().length() > 0) {
                        if (!passingyear.equals("")) {
                            if (binding.etPercentage.getText().length() > 0) {
                                if (Integer.parseInt(binding.etPercentage.getText().toString()) > 0 && Integer.parseInt(binding.etPercentage.getText().toString()) < 101) {
                                    JSONObject jsonObject = new JSONObject();
                                    try {
                                        jsonObject.put("Qualification", qualification);
                                        jsonObject.put("AEMEMPLOYEEID", pref.getEmpId());
                                        jsonObject.put("qualificationid", qualificationid);
                                        jsonObject.put("Board", binding.etUniversity.getText().toString());
                                        jsonObject.put("Percentage", binding.etPercentage.getText().toString());
                                        jsonObject.put("PassingYear", passingyear);
                                        educationarray.put(jsonObject);
                                        educationobj.put("qualificationDetails", educationarray);
                                        educationobj.put("DbOperation", "5");
                                        educationobj.put("SecurityCode", pref.getSecurityCode());
                                        Log.d("educationobj", educationobj.toString());
                                        getItemList(educationobj);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Toast.makeText(TempEducationaActivity.this, "Please Enter valid Percentage", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(TempEducationaActivity.this, "Please Enter Percentage", Toast.LENGTH_LONG).show();
                                binding.etPercentage.setBackgroundResource(R.drawable.lldesign_error);
                            }
                        } else {
                            Toast.makeText(TempEducationaActivity.this, "Please Select Passing Year", Toast.LENGTH_LONG).show();
                            binding.llPassingyear.setBackgroundResource(R.drawable.lldesign_error);
                        }
                    } else {
                        Toast.makeText(TempEducationaActivity.this, "Please Enter Your Name of university or Board", Toast.LENGTH_LONG).show();
                        binding.etUniversity.setBackgroundResource(R.drawable.lldesign_error);
                    }
                } else {
                    Toast.makeText(TempEducationaActivity.this, "Please Select Education Details", Toast.LENGTH_LONG).show();
                    binding.llQualification.setBackgroundResource(R.drawable.lldesign_error);
                }
            }
        });

        binding.btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.llData.setVisibility(View.GONE);
                itemList.clear();
                Intent intent=new Intent(TempEducationaActivity.this,TempExperinceActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        binding.btnSaveForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemList.size()>0){
                    try {
                        if (educationobj.length() == 0){
                            educationobj.put("qualificationDetails", educationarray);
                            educationobj.put("DbOperation", "5");
                            educationobj.put("SecurityCode", pref.getSecurityCode());
                            uploadfamilydetails(educationobj);
                        } else {
                            uploadfamilydetails(educationobj);
                        }

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    Toast.makeText(TempEducationaActivity.this,"Please Add Education Details",Toast.LENGTH_LONG).show();
                }
            }
        });

        binding.etUniversity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0){
                    binding.etUniversity.setBackgroundResource(R.drawable.lldesign9);
                }
            }
        });

        binding.etPercentage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0){
                    binding.etPercentage.setBackgroundResource(R.drawable.lldesign9);
                }
            }
        });
    }

    private void getItemList(JSONObject object){
        binding.spExam.setSelection(0);
        binding.etUniversity.setText("");
        binding.etPercentage.setText("");
        binding.etPassingYear.setText("");
        binding.llData.setVisibility(View.VISIBLE);
        binding.spYear.setSelection(0);
        itemList.clear();


        JSONArray education=object.optJSONArray("qualificationDetails");
        for (int i=0;i<education.length();i++){
            JSONObject educationobj=education.optJSONObject(i);
            String Qualification=educationobj.optString("Qualification");
            String Board=educationobj.optString("Board");
            String Percentage=educationobj.optString("Percentage");
            String PassingYear=educationobj.optString("PassingYear");

            EducationalModel educationalModel=new EducationalModel();
            educationalModel.setQualification(Qualification);
            educationalModel.setPassingyear(PassingYear);
            educationalModel.setBoard(Board);
            educationalModel.setPercentage(Percentage);
            itemList.add(educationalModel);
        }



        if (educationAdapter == null){
            educationAdapter=new EducationAdapter(TempEducationaActivity.this,itemList);
            binding.rvData.setAdapter(educationAdapter);
        } else {
            educationAdapter.notifyDataSetChanged();
        }


    }

    private void setQualification(JSONObject jsonObject) {
        pd=new ProgressDialog(TempEducationaActivity.this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(AppData.COMMON_DDL)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "Qualification_DropDown: "+response.toString(4));
                            pd.dismiss();
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            qualificationlist.add("Please Select");
                            mainQualification.add(new MainDocModule("0",""));
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(Response_Data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String qualivalue = obj.optString("value");
                                    String qualiid = obj.optString("id");
                                    //Log.e(TAG, "onResponse: "+qualivalue);
                                    qualificationlist.add(qualivalue);
                                    MainDocModule mainDocModule = new MainDocModule(qualiid, qualivalue);
                                    mainQualification.add(mainDocModule);
                                }

                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (TempEducationaActivity.this, android.R.layout.simple_spinner_item,
                                                qualificationlist); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                binding.spExam.setAdapter(spinnerArrayAdapter);

                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("AEMConsultantID", pref.getEmpConId());
                                    jsonObject.put("AEMClientID", pref.getEmpClintId());
                                    jsonObject.put("AEMClientOfficeID", pref.getEmpClintOffId());
                                    jsonObject.put("AEMEmployeeID", pref.getEmpId());
                                    jsonObject.put("WorkingStatus", "1");
                                    jsonObject.put("Operation", "5");
                                    getEducationDetails(jsonObject);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "Qualification_DropDown_error: "+anError.getErrorBody());
                        pd.dismiss();
                    }
                });
    }

    private void setQualification() {
        String surl = AppData.url+"gcl_CommonDDL?ddltype=6&id1=0&id2=0&id3=0&SecurityCode=" + pref.getSecurityCode();
        ProgressDialog pd=new ProgressDialog(TempEducationaActivity.this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        pd.dismiss();
                        qualificationlist.add("Please Select");
                        mainQualification.add(new MainDocModule("0",""));
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
                                    String qualivalue = obj.optString("value");
                                    String qualiid = obj.optString("id");
                                    Log.e(TAG, "onResponse: "+qualivalue);
                                    qualificationlist.add(qualivalue);
                                    MainDocModule mainDocModule = new MainDocModule(qualiid, qualivalue);
                                    mainQualification.add(mainDocModule);
                                }

                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (TempEducationaActivity.this, android.R.layout.simple_spinner_item,
                                                qualificationlist); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                binding.spExam.setAdapter(spinnerArrayAdapter);

                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("AEMConsultantID", pref.getEmpConId());
                                    jsonObject.put("AEMClientID", pref.getEmpClintId());
                                    jsonObject.put("AEMClientOfficeID", pref.getEmpClintOffId());
                                    jsonObject.put("AEMEmployeeID", pref.getEmpId());
                                    jsonObject.put("WorkingStatus", "1");
                                    jsonObject.put("Operation", "5");
                                    getEducationDetails(jsonObject);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {

                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TempEducationaActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
    }

    private void uploadfamilydetails(JSONObject jsonObject) throws JSONException {
        Log.e("EDU",jsonObject.toString(4));
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
                            Intent intent = new Intent(TempEducationaActivity.this, TempExperinceActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            Toast.makeText(TempEducationaActivity.this,"Education Details has been updated Successfully",Toast.LENGTH_LONG).show();

                        } else {

                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                        Intent intent=new Intent(TempEducationaActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }


    public void deleteItem(int pos){
        itemList.remove(pos);
        educationarray.remove(pos);
        if (itemList.size()==0){
            binding.llData.setVisibility(View.GONE);
        }
    }

    public void editItem(int pos) throws JSONException {
        if (isEditClick){
            if (!qualification.equals("")
                    && binding.etUniversity.getText().toString().length() > 0
                    && !passingyear.equals("")
                    && binding.etPercentage.getText().length() > 0
                    && Integer.parseInt(binding.etPercentage.getText().toString()) > 0 && Integer.parseInt(binding.etPercentage.getText().toString()) < 101){

                binding.imgAdd.performClick();
                editItemCode(pos);
            } else {
                editItemCode(pos);
            }
        } else {
            isEditClick = true;
            editItemCode(pos);
        }
    }

    public void editItemCode(int pos) throws JSONException {
        JSONObject jsonObject = educationarray.getJSONObject(pos);
        Log.e(TAG, "editItemCode: "+jsonObject.optString("Qualification"));
        int indexQualification = qualificationlist.indexOf(jsonObject.optString("Qualification"));
        binding.spExam.setSelection(indexQualification);
        int indexYear = yearlist.indexOf(jsonObject.optString("PassingYear"));
        binding.spYear.setSelection(indexYear);
        binding.etUniversity.setText(jsonObject.optString("Board"));
        binding.etPercentage.setText(jsonObject.optString("Percentage"));
        itemList.remove(pos);
        educationarray.remove(pos);
        if (itemList.size()==0){
            binding.llData.setVisibility(View.GONE);
        }
    }

    private void getEducationDetails(JSONObject jsonObject) {
        Log.e(TAG, "getEducationDetails: INPUT: "+jsonObject);
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
                            Log.e(TAG, "GET_EDUCATION_DETAILS: "+response.toString(4));
                            pd.dismiss();
                            JSONObject job1 = response;
                            int Response_Code = job1.optInt("Response_Code");
                            if (Response_Code == 101) {
                                String Response_Data = job1.optString("Response_Data");
                                JSONObject job2 = new JSONObject(Response_Data);
                                JSONArray jsonArray = job2.getJSONArray("EducationDetails");
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject educationobj=jsonArray.optJSONObject(i);
                                    String Qualification=educationobj.optString("QualificationName");
                                    String QualificationID=educationobj.optString("QualificationID");
                                    String Board=educationobj.optString("Institute");
                                    String Percentage=educationobj.optString("Marks");
                                    String PassingYear=educationobj.optString("YearOfPass");

                                    EducationalModel educationalModel=new EducationalModel();
                                    educationalModel.setQualification(Qualification);
                                    educationalModel.setPassingyear(PassingYear);
                                    educationalModel.setBoard(Board);
                                    educationalModel.setPercentage(Percentage);
                                    itemList.add(educationalModel);
                                    JSONObject object = new JSONObject();
                                    object.put("Qualification", Qualification);
                                    object.put("AEMEMPLOYEEID", pref.getEmpId());
                                    object.put("qualificationid", QualificationID);
                                    object.put("Board", Board);
                                    object.put("Percentage", Percentage);
                                    object.put("PassingYear", PassingYear);
                                    educationarray.put(object);
                                }

                                if (itemList.size() > 0){
                                    binding.llData.setVisibility(View.VISIBLE);
                                } else {
                                    binding.llData.setVisibility(View.GONE);
                                }

                                if (educationAdapter == null){
                                    educationAdapter=new EducationAdapter(TempEducationaActivity.this,itemList);
                                    binding.rvData.setAdapter(educationAdapter);
                                } else {
                                    educationAdapter.notifyDataSetChanged();
                                }
                            } else {

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        pd.dismiss();
                        Log.e(TAG, "GET_EDUCATION_DETAILS_onError: "+anError.getErrorBody());
                        Toast.makeText(TempEducationaActivity.this, "Something went to wrong", Toast.LENGTH_LONG).show();
                    }
                });
    }
}