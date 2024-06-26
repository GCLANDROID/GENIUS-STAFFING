package io.cordova.myapp00d753.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.fragment.ContactFragment;
import io.cordova.myapp00d753.fragment.MisFragment;
import io.cordova.myapp00d753.fragment.OfficalFragment;
import io.cordova.myapp00d753.fragment.PersonalFragment;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.Util;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
      ImageView imgHome,imgBack;
      ImageView imgOfficial,imgOfficial1,imgContact,imgContact1,imgPersonal,imgPersonal1,imgMis,imgMis1;
      TextView tvOffical,tvOffical1,tvContact,tvContact1,tvPersonal,tvPersonal1,tvMis,tvMis1;
      Pref pref;
      LinearLayout llOfficial,llContact,llPersonal,llMis;
      TextView tvToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initialize();

        JSONObject obj=new JSONObject();
        try {
            obj.put("AEMConsultantID", pref.getEmpConId());
            obj.put("AEMClientID",pref.getEmpClintId());
            obj.put("AEMClientOfficeID",pref.getEmpClintOffId());
            obj.put("AEMEmployeeID",pref.getEmpId());
            obj.put("SecurityCode",pref.getSecurityCode());
            obj.put("WorkingStatus","1");
            obj.put("CurrentPage","0");
            profileFunction(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //profileFunction();
        onclick();
        //AEMConsultantID="+pref.getEmpConId()+"&AEMClientID="+pref.getEmpClintId()+"&AEMClientOfficeID="+pref.getEmpClintOffId()+"&AEMEmployeeID="+pref.getEmpId() +"&SecurityCode="+pref.getSecurityCode()+"&WorkingStatus=1&CurrentPage=0";
    }



    private void  initialize(){
        pref=new Pref(ProfileActivity.this);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        imgBack=(ImageView)findViewById(R.id.imgBack);

        imgOfficial=(ImageView)findViewById(R.id.imgOfficial);
        imgOfficial1=(ImageView)findViewById(R.id.imgOfficial1);


        imgContact=(ImageView)findViewById(R.id.imgContact);
        imgContact1=(ImageView)findViewById(R.id.imgContact1);


        imgPersonal=(ImageView)findViewById(R.id.imgPersonal);
        imgPersonal1=(ImageView)findViewById(R.id.imgPersonal1);


        imgMis=(ImageView)findViewById(R.id.imgMis);
        imgMis1=(ImageView)findViewById(R.id.imgMis1);

        tvOffical=(TextView)findViewById(R.id.tvOffical);
        tvOffical1=(TextView)findViewById(R.id.tvOffical1);

        tvContact=(TextView)findViewById(R.id.tvContact);
        tvContact1=(TextView)findViewById(R.id.tvContact1);


        tvPersonal=(TextView)findViewById(R.id.tvPersonal);
        tvPersonal1=(TextView)findViewById(R.id.tvPersonal1);


        tvMis=(TextView)findViewById(R.id.tvMis);
        tvMis1=(TextView)findViewById(R.id.tvMis1);
        llOfficial=(LinearLayout)findViewById(R.id.llOfficial);
        llContact=(LinearLayout)findViewById(R.id.llContact);
        llPersonal=(LinearLayout)findViewById(R.id.llPersonal);
        llMis=(LinearLayout)findViewById(R.id.llMis);
        tvToolBar=(TextView)findViewById(R.id.tvToolBar);


    }


    public void loadOfficialFragment() {

        FragmentManager manager = getSupportFragmentManager();
       FragmentTransaction transaction = manager.beginTransaction();
        OfficalFragment fragment=new OfficalFragment();
        transaction.replace(R.id.flMain, fragment);
        transaction.commit();

        //official
        imgOfficial.setVisibility(View.VISIBLE);
        imgOfficial1.setVisibility(View.GONE);
        tvOffical.setVisibility(View.VISIBLE);
        tvOffical1.setVisibility(View.GONE);

        //contact
        imgContact.setVisibility(View.GONE);
        imgContact1.setVisibility(View.VISIBLE);
        tvContact.setVisibility(View.GONE);
        tvContact1.setVisibility(View.VISIBLE);


        //personal
        imgPersonal.setVisibility(View.GONE);
        imgPersonal1.setVisibility(View.VISIBLE);
        tvPersonal.setVisibility(View.GONE);
        tvPersonal1.setVisibility(View.VISIBLE);

        //mis
        imgMis.setVisibility(View.GONE);
        imgMis1.setVisibility(View.VISIBLE);
        tvMis.setVisibility(View.GONE);
        tvMis1.setVisibility(View.VISIBLE);
        tvToolBar.setText("Profile - Official");




        //tvHeader.setText("Official");


    }

    public void loadPersonalFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        PersonalFragment pfragment=new PersonalFragment();
        transaction.replace(R.id.flMain, pfragment);
        transaction.commit();

        //official
        imgOfficial.setVisibility(View.GONE);
        imgOfficial1.setVisibility(View.VISIBLE);
        tvOffical.setVisibility(View.GONE);
        tvOffical1.setVisibility(View.VISIBLE);

        //contact
        imgContact.setVisibility(View.GONE);
        imgContact1.setVisibility(View.VISIBLE);
        tvContact.setVisibility(View.GONE);
        tvContact1.setVisibility(View.VISIBLE);


        //personal
        imgPersonal.setVisibility(View.VISIBLE);
        imgPersonal1.setVisibility(View.GONE);
        tvPersonal.setVisibility(View.VISIBLE);
        tvPersonal1.setVisibility(View.GONE);

        //mis
        imgMis.setVisibility(View.GONE);
        imgMis1.setVisibility(View.VISIBLE);
        tvMis.setVisibility(View.GONE);
        tvMis1.setVisibility(View.VISIBLE);

        tvToolBar.setText("Profile - Personal");








    }

    public void loadContactFragment() {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        ContactFragment cfragment=new ContactFragment();
        transaction.replace(R.id.flMain, cfragment);
        transaction.commit();


        //official
        imgOfficial.setVisibility(View.GONE);
        imgOfficial1.setVisibility(View.VISIBLE);
        tvOffical.setVisibility(View.GONE);
        tvOffical1.setVisibility(View.VISIBLE);

        //contact
        imgContact.setVisibility(View.VISIBLE);
        imgContact1.setVisibility(View.GONE);
        tvContact.setVisibility(View.VISIBLE);
        tvContact1.setVisibility(View.GONE);


        //personal
        imgPersonal.setVisibility(View.GONE);
        imgPersonal1.setVisibility(View.VISIBLE);
        tvPersonal.setVisibility(View.GONE);
        tvPersonal1.setVisibility(View.VISIBLE);

        //mis
        imgMis.setVisibility(View.GONE);
        imgMis1.setVisibility(View.VISIBLE);
        tvMis.setVisibility(View.GONE);
        tvMis1.setVisibility(View.VISIBLE);

        tvToolBar.setText("Profile - Contact");




    }

    public void loadMisFragment() {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        MisFragment ffragment=new MisFragment();
        transaction.replace(R.id.flMain, ffragment);
        transaction.commit();


        //official
        imgOfficial.setVisibility(View.GONE);
        imgOfficial1.setVisibility(View.VISIBLE);
        tvOffical.setVisibility(View.GONE);
        tvOffical1.setVisibility(View.VISIBLE);

        //contact
        imgContact.setVisibility(View.GONE);
        imgContact1.setVisibility(View.VISIBLE);
        tvContact.setVisibility(View.GONE);
        tvContact1.setVisibility(View.VISIBLE);


        //personal
        imgPersonal.setVisibility(View.GONE);
        imgPersonal1.setVisibility(View.VISIBLE);
        tvPersonal.setVisibility(View.GONE);
        tvPersonal1.setVisibility(View.VISIBLE);

        //mis
        imgMis.setVisibility(View.VISIBLE);
        imgMis1.setVisibility(View.GONE);
        tvMis.setVisibility(View.VISIBLE);
        tvMis1.setVisibility(View.GONE);

        tvToolBar.setText("Profile - Miscellaneous");



    }



    private void  onclick(){

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProfileActivity.this,EmployeeDashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
               // finish();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        llOfficial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadOfficialFragment();
            }
        });

        llContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadContactFragment();
            }
        });

        llPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPersonalFragment();
            }
        });
        llMis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMisFragment();
            }
        });
    }

    private void profileFunction(JSONObject jsonObject) {
        Log.e(TAG, "profileFunction: "+jsonObject);
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();
        AndroidNetworking.post(AppData.GCL_KYC)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.dismiss();
                        try {
                            Log.e(TAG, "PROFILE: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");

                                JSONArray jsonArray = new JSONArray(Response_Data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj=jsonArray.getJSONObject(i);
                                    String AEMEmployeeID=obj.optString("AEMEmployeeID");
                                    pref.saveSEmpID(AEMEmployeeID);

                                    String Code=obj.optString("Code");
                                    pref.saveSEmpCode(Code);

                                    String Name=obj.optString("Name");
                                    pref.saveSEmpName(Name);

                                    String DateOfJoining=obj.optString("DOJ");
                                    pref.saveSDOJ(DateOfJoining);

                                    String Department=obj.optString("Department");
                                    pref.saveSDept(Department);

                                    String Designation=obj.optString("Designation");
                                    pref.saveSDes(Designation);

                                    String Location=obj.optString("Location");
                                    pref.saveSLocation(Location);

                                    String Sex=obj.optString("Sex");
                                    if (!Sex.equals("")) {
                                        pref.saveSGender(Sex);
                                    }else {
                                        pref.saveSGender("N/A");
                                    }

                                    String DateOfBirth=obj.optString("DateOfBirth");
                                    if (!DateOfBirth.equals("")) {
                                        pref.saveSDOB(DateOfBirth);
                                    }else {
                                        pref.saveSDOB("N/A");
                                    }

                                    String GuardianName=obj.optString("GuardianName");
                                    if (!GuardianName.equals("")) {
                                        pref.saveSGurdian(GuardianName);
                                    }else {
                                        pref.saveSGurdian("N/A");
                                    }

                                    String RelationShip=obj.optString("RelationShip");
                                    if (!RelationShip.equals("")) {
                                        pref.saveSRelation(RelationShip);
                                    }else {
                                        pref.saveSRelation("N/A");
                                    }

                                    String Qualification=obj.optString("Qualification");
                                    if (!Qualification.equals("")) {
                                        pref.saveSQualification(Qualification);
                                    }else {
                                        pref.saveSQualification("N/A");
                                    }

                                    String MaritalStatus=obj.optString("MaritalStatus");

                                    if (!MaritalStatus.equals("")){
                                        pref.saveSMartial(MaritalStatus);
                                    }else {
                                        pref.saveSMartial("N/A");
                                    }

                                    String BloodGroup=obj.optString("BloodGroup");
                                    if (!BloodGroup.equals("")) {
                                        pref.saveSBlood(BloodGroup);
                                    }else {
                                        pref.saveSBlood("N/A");
                                    }

                                    String PermanentAddress=obj.optString("PermanentAddress");
                                    if (!PermanentAddress.equals("")){
                                        pref.saveSParAdd(PermanentAddress);
                                    }else {
                                        pref.saveSParAdd("N/A");
                                    }

                                    String PresentAddress=obj.optString("PresentAddress");
                                    if (!PresentAddress.equals("")){
                                        pref.saveSPerAdd(PresentAddress);
                                    }else {
                                        pref.saveSPerAdd("N/A");
                                    }

                                    String Mobile=obj.optString("Mobile");
                                    if (!Mobile.equals("")){
                                        pref.saveSPhnNo(Mobile);
                                    }else {
                                        pref.saveSPhnNo("N/A");
                                    }

                                    String EmailID=obj.optString("EmailID");
                                    if (!EmailID.equals("")){
                                        pref.saveSEmail(EmailID);
                                    }else {
                                        pref.saveSEmail("N/A");
                                    }

                                    String PFNumber=obj.optString("PFNumber");
                                    if (!PFNumber.equals("")){
                                        pref.saveSPF(PFNumber);
                                    }else {
                                        pref.saveSPF("N/A");
                                    }

                                    String ESINumber=obj.optString("ESINumber");
                                    if (!ESINumber.equals("")){
                                        pref.saveSESI(ESINumber);
                                    }


                                    String BankName=obj.optString("BankName");
                                    if (!BankName.equals("")){
                                        pref.saveSBank(BankName);
                                    }


                                    String AccountNumber=obj.optString("AccountNumber");
                                    if (!AccountNumber.equals("")){
                                        pref.saveSAcc(AccountNumber);
                                    }else {
                                        pref.saveSAcc("N/A");
                                    }

                                    String AadharCard=obj.optString("AadharNo");
                                    if (!AadharCard.equals("")){
                                        pref.saveSAadhar(AadharCard);
                                    }else {
                                        pref.saveSAadhar("N/A");
                                    }

                                    String UanNo=obj.optString("UANNumber");
                                    if (!UanNo.equals("")){
                                        pref.saveSUAN(UanNo);
                                    }else {
                                        pref.saveSUAN("N/A");
                                    }
                                }

                                loadOfficialFragment();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressBar.dismiss();
                        Log.e(TAG, "PROFILE_error: "+anError.getErrorCode());
                    }
                });
    }

    public void profileFunction() {
        String surl = AppData.url+"gcl_KYC?AEMConsultantID="+pref.getEmpConId()+"&AEMClientID="+pref.getEmpClintId()+"&AEMClientOfficeID="+pref.getEmpClintOffId()+"&AEMEmployeeID="+pref.getEmpId() +"&SecurityCode="+pref.getSecurityCode()+"&WorkingStatus=1&CurrentPage=0";
        Log.d("kyc",surl);
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);//you can cancel it by pressing back button
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
                            String responseText=job1.optString("responseText");
                            boolean responseStatus=job1.optBoolean("responseStatus");
                            if (responseStatus){
                             //   Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData=job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++){
                                    JSONObject obj=responseData.getJSONObject(i);
                                    String AEMEmployeeID=obj.optString("AEMEmployeeID");
                                    pref.saveSEmpID(AEMEmployeeID);

                                    String Code=obj.optString("Code");
                                    pref.saveSEmpCode(Code);

                                    String Name=obj.optString("Name");
                                    pref.saveSEmpName(Name);

                                    String DateOfJoining=obj.optString("DateOfJoining");
                                    pref.saveSDOJ(DateOfJoining);

                                    String Department=obj.optString("Department");
                                    pref.saveSDept(Department);

                                    String Designation=obj.optString("Designation");
                                    pref.saveSDes(Designation);

                                    String Location=obj.optString("Location");
                                    pref.saveSLocation(Location);

                                    String Sex=obj.optString("Sex");
                                    if (!Sex.equals("")) {
                                        pref.saveSGender(Sex);
                                    }else {
                                        pref.saveSGender("N/A");
                                    }

                                    String DateOfBirth=obj.optString("DateOfBirth");
                                    if (!DateOfBirth.equals("")) {
                                        pref.saveSDOB(DateOfBirth);
                                    }else {
                                        pref.saveSDOB("N/A");
                                    }

                                    String GuardianName=obj.optString("GuardianName");
                                    if (!GuardianName.equals("")) {
                                        pref.saveSGurdian(GuardianName);
                                    }else {
                                        pref.saveSGurdian("N/A");
                                    }

                                    String RelationShip=obj.optString("RelationShip");
                                    if (!RelationShip.equals("")) {
                                        pref.saveSRelation(RelationShip);

                                    }else {
                                        pref.saveSRelation("N/A");
                                    }

                                    String Qualification=obj.optString("Qualification");
                                    if (!Qualification.equals("")) {
                                        pref.saveSQualification(Qualification);
                                    }else {
                                        pref.saveSQualification("N/A");
                                    }

                                    String MaritalStatus=obj.optString("MaritalStatus");

                                    if (!MaritalStatus.equals("")){
                                        pref.saveSMartial(MaritalStatus);
                                    }else {
                                        pref.saveSMartial("N/A");
                                    }

                                    String BloodGroup=obj.optString("BloodGroup");
                                    if (!BloodGroup.equals("")) {
                                        pref.saveSBlood(BloodGroup);
                                    }else {
                                        pref.saveSBlood("N/A");
                                    }

                                    String PermanentAddress=obj.optString("PermanentAddress");
                                    if (!PermanentAddress.equals("")){
                                        pref.saveSParAdd(PermanentAddress);
                                    }else {
                                        pref.saveSParAdd("N/A");
                                    }

                                    String PresentAddress=obj.optString("PresentAddress");
                                    if (!PresentAddress.equals("")){
                                        pref.saveSPerAdd(PresentAddress);
                                    }else {
                                        pref.saveSPerAdd("N/A");
                                    }

                                    String Mobile=obj.optString("Mobile");
                                    if (!Mobile.equals("")){
                                        pref.saveSPhnNo(Mobile);
                                    }else {
                                        pref.saveSPhnNo("N/A");
                                    }

                                    String EmailID=obj.optString("EmailID");
                                    if (!EmailID.equals("")){
                                        pref.saveSEmail(EmailID);
                                    }else {
                                        pref.saveSEmail("N/A");
                                    }

                                    String PFNumber=obj.optString("PFNumber");
                                    if (!PFNumber.equals("")){
                                        pref.saveSPF(PFNumber);
                                    }else {
                                        pref.saveSPF("N/A");
                                    }

                                    String ESINumber=obj.optString("ESINumber");
                                    if (!ESINumber.equals("")){
                                        pref.saveSESI(ESINumber);
                                    }


                                    String BankName=obj.optString("BankName");
                                    if (!BankName.equals("")){
                                        pref.saveSBank(BankName);
                                    }


                                    String AccountNumber=obj.optString("AccountNumber");
                                    if (!AccountNumber.equals("")){
                                        pref.saveSAcc(AccountNumber);
                                    }else {
                                        pref.saveSAcc("N/A");
                                    }

                                    String AadharCard=obj.optString("AadharCard");
                                    if (!AadharCard.equals("")){
                                        pref.saveSAadhar(AadharCard);
                                    }else {
                                        pref.saveSAadhar("N/A");
                                    }

                                    String UanNo=obj.optString("UanNo");
                                    if (!UanNo.equals("")){
                                        pref.saveSUAN(UanNo);
                                    }else {
                                        pref.saveSUAN("N/A");
                                    }
                                }

                                loadOfficialFragment();
                            }
                            else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ProfileActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
               // Toast.makeText(ProfileActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert",error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");

    }



}
