package io.cordova.myapp00d753.activity;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

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

import io.cordova.myapp00d753.KYCFamilyModel;
import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.KYCFamilyAdapter;
import io.cordova.myapp00d753.databinding.ActivityKycfamilyBinding;
import io.cordova.myapp00d753.module.MainDocModule;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

import static java.util.Calendar.DAY_OF_MONTH;


public class KYCFamilyActivity extends AppCompatActivity {
    private static final String TAG = "KYCFamilyActivity";
    ActivityKycfamilyBinding binding;
    JSONObject nominationobject=new JSONObject();
    JSONArray nominationarray=new JSONArray();
    ArrayList<KYCFamilyModel>itemList=new ArrayList<>();
    ArrayList<KYCFamilyModel>itemListForView=new ArrayList<>();
    LinearLayoutManager layoutManager;
    String month;
    String dob="";
    Pref pref;
    ProgressDialog pd;
    ArrayList<MainDocModule> mainRealation = new ArrayList<>();
    ArrayList<String> realation = new ArrayList<>();
    String relationship="";
    String relationshipID="";
    KYCFamilyAdapter nominationAdapter;
    ArrayList<String> gender = new ArrayList<>();
    ArrayList<MainDocModule> mainGender = new ArrayList<>();
    ProgressDialog pd2;
    String sexGender="";
    String isFatherSelected = "", isMotherSelected = "", isWifeSelected = "", isHusbandSelected = "";
    boolean isEditClick = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_kycfamily);
        initialize();
        setNomineeRelation();


        onClick();
    }



    private void initialize() {
        pref=new Pref(KYCFamilyActivity.this);
        pd=new ProgressDialog(KYCFamilyActivity.this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        final SimpleTooltip tooltip = new SimpleTooltip.Builder(KYCFamilyActivity.this)
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
        layoutManager = new LinearLayoutManager(KYCFamilyActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvData.setLayoutManager(layoutManager);
        binding.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.etName.getText().toString().length()>0) {
                    //if (!getSexGender().isEmpty()) {
                        if (!dob.isEmpty()) {
                            if (!relationship.isEmpty()){
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("Name", binding.etName.getText().toString());
                                    jsonObject.put("Gender", sexGender);
                                    jsonObject.put("Relationship", relationshipID);
                                    jsonObject.put("RelationshipID", relationship);
                                    if (relationship.equalsIgnoreCase("Father")){
                                        isFatherSelected = "Father";
                                    } else if (relationship.equalsIgnoreCase("Mother")){
                                        isMotherSelected = "Mother";
                                    } else if (relationship.equalsIgnoreCase("Wife")){
                                        isWifeSelected = "Wife";
                                    } else if (relationship.equalsIgnoreCase("Husband")){
                                        isHusbandSelected = "Husband";
                                    }
                                    jsonObject.put("DOB", dob);
                                    jsonObject.put("AEMEMPLOYEEID", pref.getEmpId());
                                    nominationarray.put(jsonObject);
                                    nominationobject.put("familyDetails", nominationarray);
                                    nominationobject.put("DbOperation", "6");
                                    nominationobject.put("SecurityCode", pref.getSecurityCode());
                                    Log.d("nomination", nominationobject.toString());
                                    getItemList(nominationobject);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(KYCFamilyActivity.this, "Please Select Relationship with Family Member", Toast.LENGTH_LONG).show();
                                binding.llRelationship.setBackgroundResource(R.drawable.lldesign_error);
                            }
                        } else {
                            Toast.makeText(KYCFamilyActivity.this, "Please Select Family Member's Date of Birth", Toast.LENGTH_LONG).show();
                        }
                   /* } else {
                        Toast.makeText(KYCFamilyActivity.this, "Please Select Family Member's Gender", Toast.LENGTH_LONG).show();
                        binding.llGender.setBackgroundResource(R.drawable.lldesign_error);
                    }*/
                }else {
                    Toast.makeText(KYCFamilyActivity.this,"Please Enter Family Member's Name",Toast.LENGTH_LONG).show();
                    binding.etName.setBackgroundResource(R.drawable.lldesign_error);
                }
            }

            private String getSexGender() {
                return sexGender;
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
                Intent intent = new Intent(KYCFamilyActivity.this, TempDashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void getItemList(JSONObject object){
        binding.etName.setText("");
        binding.tvUANDOB.setText("");
        dob="";
        binding.etGender.setText("");
        binding.spRealation.setSelection(0);
        binding.llData.setVisibility(View.VISIBLE);
        itemList.clear();


        JSONArray nomination=object.optJSONArray("familyDetails");
        for (int i=0;i<nomination.length();i++) {
            JSONObject nomiobj = nomination.optJSONObject(i);
            try {
                if (!itemList.get(i).getFamilyMemberName().equalsIgnoreCase(nomiobj.optString("Name"))) {
                    String Name = nomiobj.optString("Name");
                    String Gender = nomiobj.optString("Gender");
                    String Relationship = nomiobj.optString("RelationshipID");
                    String DOB = nomiobj.optString("DOB");
                    KYCFamilyModel epSmodel = new KYCFamilyModel();
                    epSmodel.setFamilyMemberName(Name);
                    epSmodel.setGender(Gender);
                    epSmodel.setDob(DOB);
                    epSmodel.setRealationship(Relationship);
                    itemList.add(epSmodel);
                }
            } catch (IndexOutOfBoundsException e) {
                String Name = nomiobj.optString("Name");
                String Gender = nomiobj.optString("Gender");
                String Relationship = nomiobj.optString("RelationshipID");
                String DOB = nomiobj.optString("DOB");
                KYCFamilyModel epSmodel = new KYCFamilyModel();
                epSmodel.setFamilyMemberName(Name);
                epSmodel.setGender(Gender);
                epSmodel.setDob(DOB);
                epSmodel.setRealationship(Relationship);
                itemList.add(epSmodel);
            }
        }
        if (nominationAdapter == null){
            nominationAdapter=new KYCFamilyAdapter(itemList,KYCFamilyActivity.this);
            binding.rvData.setAdapter(nominationAdapter);
        } else {
            nominationAdapter.notifyDataSetChanged();
        }
    }

    private void onClick(){

        binding.spRealation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>0){
                    if (isFatherSelected.equalsIgnoreCase(mainRealation.get(i).getDocumentType())
                        || isMotherSelected.equalsIgnoreCase(mainRealation.get(i).getDocumentType())
                        || isWifeSelected.equalsIgnoreCase(mainRealation.get(i).getDocumentType())
                        || isHusbandSelected.equalsIgnoreCase(mainRealation.get(i).getDocumentType())){
                        Toast.makeText(KYCFamilyActivity.this, "You have already add "+mainRealation.get(i).getDocumentType(), Toast.LENGTH_SHORT).show();
                        binding.spRealation.setSelection(0);
                    } else {
                        relationshipID=mainRealation.get(i).getDocID();
                        relationship=mainRealation.get(i).getDocumentType();
                        binding.llRelationship.setBackgroundResource(R.drawable.lldesign9);
                    }
                }
                Log.e(TAG, "onItemSelected: relationshipID: "+relationshipID);
                Log.e(TAG, "onItemSelected: relationship: "+relationship);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.btnSaveForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemList.size()>0){
                    try {
                        if (nominationobject.length() == 0){
                            nominationobject.put("familyDetails", nominationarray);
                            nominationobject.put("DbOperation", "6");
                            nominationobject.put("SecurityCode", pref.getSecurityCode());
                            uploadfamilydetails(nominationobject);
                        } else {
                            uploadfamilydetails(nominationobject);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    Toast.makeText(KYCFamilyActivity.this,"Please Add Family Member",Toast.LENGTH_LONG).show();
                }
            }
        });
        binding.btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.llData.setVisibility(View.GONE);
                itemList.clear();

                Intent intent=new Intent(KYCFamilyActivity.this,EPSNominationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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


                final DatePickerDialog dialog = new DatePickerDialog(KYCFamilyActivity.this, new DatePickerDialog.OnDateSetListener() {
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

        binding.spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!mainGender.get(position).getDocumentType().isEmpty()){
                    sexGender = mainGender.get(position).getDocumentType();
                    binding.llGender.setBackgroundResource(R.drawable.lldesign9);
                }

                //sexGender = mainGender.get(position).getDocID();
                //Log.d("sexgender", sexGender);
                //spESICGender.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
    }


    private void uploadfamilydetails(JSONObject jsonObject) throws JSONException {
        Log.e(TAG, "uploadfamilydetails: "+jsonObject.toString(4) );
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
                            Intent intent = new Intent(KYCFamilyActivity.this, EPSNominationActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            Toast.makeText(KYCFamilyActivity.this,"Family Details has been updated Successfully",Toast.LENGTH_LONG).show();

                        }else {

                        }
                    }

                    @Override
                    public void onError(ANError error) {

                            Intent intent=new Intent(KYCFamilyActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();

                    }
                });
    }


    private void setNomineeRelation() {
        String surl = AppData.url+"gcl_CommonDDL?ddltype=7&id1=0&id2=0&id3=0&SecurityCode=" + pref.getSecurityCode();
        pd=new ProgressDialog(KYCFamilyActivity.this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
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
                                        (KYCFamilyActivity.this, android.R.layout.simple_spinner_item,
                                                realation); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                binding.spRealation.setAdapter(spinnerArrayAdapter);


                                setGender();
                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(KYCFamilyActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               pd2.dismiss();

                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());

            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
    }

    public void deleteItem(int pos){
        if (itemList.get(pos).getRealationship().equalsIgnoreCase("Father")){
            isFatherSelected = "";
        } else if (itemList.get(pos).getRealationship().equalsIgnoreCase("Mother")){
            isMotherSelected = "";
        } else if (itemList.get(pos).getRealationship().equalsIgnoreCase("Wife")){
            isWifeSelected = "";
        } else if (itemList.get(pos).getRealationship().equalsIgnoreCase("Husband")){
            isHusbandSelected = "";
        }
        itemList.remove(pos);
        nominationarray.remove(pos);
        if (itemList.size()==0){
            binding.llData.setVisibility(View.GONE);
        }
    }

    public void editItem(int pos) throws JSONException {
        if (isEditClick){
            if (binding.etName.getText().toString().length()>0
                    && !dob.isEmpty()
                    && !relationship.isEmpty()){
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


    public void editItemCode(int pos){
        if (itemList.get(pos).getRealationship().equalsIgnoreCase("Father")){
            isFatherSelected = "";
        } else if (itemList.get(pos).getRealationship().equalsIgnoreCase("Mother")){
            isMotherSelected = "";
        } else if (itemList.get(pos).getRealationship().equalsIgnoreCase("Wife")){
            isWifeSelected = "";
        } else if (itemList.get(pos).getRealationship().equalsIgnoreCase("Husband")){
            isHusbandSelected = "";
        }
        JSONObject object = nominationarray.optJSONObject(pos);
        binding.etName.setText(object.optString("Name"));
        relationshipID = object.optString("Relationship");
        relationship = object.optString("RelationshipID");
        int index = realation.indexOf(relationship);
        binding.spRealation.setSelection(index);
        dob = object.optString("DOB");
        binding.tvUANDOB.setText(dob);
        itemList.remove(pos);
        nominationarray.remove(pos);
        if (itemList.size()==0){
            binding.llData.setVisibility(View.GONE);
        }
        Log.e(TAG, "deleteItem: itemList: "+itemList.size());
        Log.e(TAG, "deleteItem: nominationarray: "+nominationarray.length());
    }

    private void setGender() {
        String surl = AppData.url + "gcl_CommonDDL?ddltype=10&id1=0&id2=0&id3=0&SecurityCode=" + pref.getSecurityCode();
        //llLoader.setVisibility(View.VISIBLE);
        //llMain.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        //llLoader.setVisibility(View.VISIBLE);
                        //llMain.setVisibility(View.GONE);

                        gender.clear();
                        mainGender.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            gender.add("Please select");
                            mainGender.add(new MainDocModule("",""));
                            if (responseStatus) {
                                //Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String value = obj.optString("value");
                                    String id = obj.optString("id");
                                    gender.add(value);
                                    MainDocModule mainDocModule = new MainDocModule(id, value);
                                    mainGender.add(mainDocModule);

                                }
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (KYCFamilyActivity.this, android.R.layout.simple_spinner_item,
                                                gender); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                binding.spGender.setAdapter(spinnerArrayAdapter);

                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("AEMConsultantID", pref.getEmpConId());
                                    jsonObject.put("AEMClientID", pref.getEmpClintId());
                                    jsonObject.put("AEMClientOfficeID", pref.getEmpClintOffId());
                                    jsonObject.put("AEMEmployeeID", pref.getEmpId());
                                    jsonObject.put("WorkingStatus", "1");
                                    jsonObject.put("Operation", "6");
                                    getFamilyDetails(jsonObject);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(KYCFamilyActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //llLoader.setVisibility(View.VISIBLE);
                //llMain.setVisibility(View.GONE);
                pd.dismiss();
                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
                //errflag = 4;
                //showInternetDialog();
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
    }

    private void getFamilyDetails(JSONObject jsonObject) {
        Log.e(TAG, "getFamilyDetails: INPUT: "+jsonObject);
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
                            Log.e(TAG, "GET_FAMILY_DETAILS: "+response.toString(4));
                            pd.dismiss();
                            JSONObject job1 = response;
                            int Response_Code = job1.optInt("Response_Code");
                            if (Response_Code == 101) {
                                String Response_Data = job1.optString("Response_Data");
                                JSONObject job2 = new JSONObject(Response_Data);
                                JSONArray jsonArray = job2.getJSONArray("FamilyDetails");
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject nomiobj=jsonArray.optJSONObject(i);
                                    JSONObject object = new JSONObject();
                                    String AEMEmployeeID = nomiobj.optString("AEMEmployeeID");
                                    String Name=nomiobj.optString("MemberName");
                                    String Gender=nomiobj.optString("Gender");
                                    String Relationship=nomiobj.optString("Relation");
                                    String RelationshipID=nomiobj.optString("RelationID");
                                    String DOB=nomiobj.optString("MemberDOB");
                                    if (Relationship.equalsIgnoreCase("Father")){
                                        isFatherSelected = "Father";
                                    } else if (Relationship.equalsIgnoreCase("Mother")){
                                        isMotherSelected = "Mother";
                                    } else if (Relationship.equalsIgnoreCase("Wife")){
                                        isWifeSelected = "Wife";
                                    } else if (Relationship.equalsIgnoreCase("Husband")){
                                        isHusbandSelected = "Husband";
                                    }

                                    KYCFamilyModel epSmodel=new KYCFamilyModel();
                                    epSmodel.setFamilyMemberName(Name);
                                    epSmodel.setGender(Gender);
                                    epSmodel.setDob(DOB);
                                    epSmodel.setRealationship(Relationship);
                                    itemList.add(epSmodel);
                                    object.put("AEMEmployeeID",AEMEmployeeID);
                                    object.put("Name",Name);
                                    object.put("RelationshipID",Relationship);
                                    object.put("Relationship",RelationshipID);
                                    object.put("DOB",DOB);
                                    nominationarray.put(object);
                                }

                                if (nominationAdapter == null){
                                    nominationAdapter=new KYCFamilyAdapter(itemList,KYCFamilyActivity.this);
                                    binding.rvData.setAdapter(nominationAdapter);
                                } else {
                                    nominationAdapter.notifyDataSetChanged();
                                }

                                if (itemList.size() > 0){
                                    binding.llData.setVisibility(View.VISIBLE);
                                } else {
                                    binding.llData.setVisibility(View.GONE);
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
                        Log.e(TAG, "GET_FAMILY_DETAILS_onError: "+anError.getErrorBody());
                        Toast.makeText(KYCFamilyActivity.this, "Something went to wrong", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
