package io.cordova.myapp00d753.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;
import io.cordova.myapp00d753.utility.Pref;

public class ProfileActivity extends AppCompatActivity {
    LinearLayout llOffical,llOffDetail,llCon,llConDetail,llPer,llPerDetail,llMis,llMisDetail;
    ImageView imgOffiPlus,imgOffiMinus,imgPerPlus,imgPerMinus,imgConPlus,imgConMinus,imgMisPlus,imgMisMinus;
    ImageView imgHome,imgBack;
    TextView tvEmplId,tvEmpCode,tvEmpName,tvDOJ,tvDepartment,tvDesignation,tvLocation,tvGender,tvEmpDOB,tvGurdianName,tvRealtionShip,tvQualification,tvMarital,tvBloodGroup;
     TextView tvParAddr,tvPreAddr,tvPhnNumber,tvEmail,tvPfNumber,tvEsiNumber,tvBankName,tvAcNumber,tvAddharNumber,tvUanNumber;
     Pref pref;
    String empConsId,empClinId,empClintOffId,empId;
    NetworkConnectionCheck connectionCheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initialize();

        if (connectionCheck.isNetworkAvailable()) {
            profileFunction();
            }
            else {
            connectionCheck.getNetworkActiveAlert().show();
        }

        onclick();
    }

    private void  initialize(){
        pref=new Pref(ProfileActivity.this);
        connectionCheck = new NetworkConnectionCheck(this);

        llOffical=(LinearLayout)findViewById(R.id.llOffical);
        llOffDetail=(LinearLayout)findViewById(R.id.llOffiDetail);

        llCon=(LinearLayout)findViewById(R.id.llContact);
        llConDetail=(LinearLayout)findViewById(R.id.llContactDetail);

        llPer=(LinearLayout)findViewById(R.id.llPersonal);
        llPerDetail=(LinearLayout)findViewById(R.id.llPersDetail);

        llMis=(LinearLayout)findViewById(R.id.llMis);
        llMisDetail=(LinearLayout)findViewById(R.id.llMisDetail);

        imgOffiPlus=(ImageView)findViewById(R.id.imgOffiPlus);
        imgOffiMinus=(ImageView)findViewById(R.id.imgOffiMius);

        imgPerPlus=(ImageView)findViewById(R.id.imgpersPlus);
        imgPerMinus=(ImageView)findViewById(R.id.imgPersMinus);

        imgConPlus=(ImageView)findViewById(R.id.imgConPlus);
        imgConMinus=(ImageView)findViewById(R.id.imgConMinus);

        imgMisPlus=(ImageView)findViewById(R.id.imgMisPlus);
        imgMisMinus=(ImageView)findViewById(R.id.imgMisMinus);

        imgHome=(ImageView)findViewById(R.id.imgHome);
        imgBack=(ImageView)findViewById(R.id.imgBack);

        tvEmplId=(TextView)findViewById(R.id.tvEmplId);
        tvEmpCode=(TextView)findViewById(R.id.tvEmpCode);
        tvEmpName=(TextView)findViewById(R.id.tvEmpName);
        tvDOJ=(TextView)findViewById(R.id.tvDOJ);
        tvDepartment=(TextView)findViewById(R.id.tvDepartment);
        tvDesignation=(TextView)findViewById(R.id.tvDesignation);
        tvLocation=(TextView)findViewById(R.id.tvLocation);


        tvGender=(TextView)findViewById(R.id.tvGender);
        tvEmpDOB=(TextView)findViewById(R.id.tvEmpCodeDOB);
        tvGurdianName=(TextView)findViewById(R.id.tvGurdianName);
        tvRealtionShip=(TextView)findViewById(R.id.tvRealtionShip);
        tvQualification=(TextView)findViewById(R.id.tvQualification);
        tvMarital=(TextView)findViewById(R.id.tvMarital);
        tvBloodGroup=(TextView)findViewById(R.id.tvBloodGroup);

        tvParAddr=(TextView)findViewById(R.id.tvParAddr);
        tvPreAddr=(TextView)findViewById(R.id.tvPreAddr);
        tvPhnNumber=(TextView)findViewById(R.id.tvPhnNumber);
        tvEmail=(TextView)findViewById(R.id.tvEmail);

        tvPfNumber=(TextView)findViewById(R.id.tvPfNumber);
        tvEsiNumber=(TextView)findViewById(R.id.tvEsiNumber);
        tvAcNumber=(TextView)findViewById(R.id.tvAcNumber);
        tvBankName=(TextView)findViewById(R.id.tvBankName);
        tvAddharNumber=(TextView)findViewById(R.id.tvAddharNumber);
        tvUanNumber=(TextView)findViewById(R.id.tvUanNumber);
        empConsId=pref.getEmpConId();
        Log.d("empConsId",empConsId);
        empClinId=pref.getEmpClintId();
        Log.d("empClinId",empClinId);
        empClintOffId=pref.getEmpClintOffId();
        Log.d("empClintOffId",empClintOffId);
        empId=pref.getEmpId();
        Log.d("empId",empId);

        int month=Calendar.getInstance().get(Calendar.MONTH);
        Log.d("month", String.valueOf(month));

    }

    private void  onclick(){
        llOffical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgOffiPlus.getVisibility()== view.VISIBLE){
                    imgOffiPlus.setVisibility(View.GONE);
                    imgOffiMinus.setVisibility(View.VISIBLE);
                    llOffDetail.setVisibility(View.VISIBLE);
                    llPerDetail.setVisibility(View.GONE);
                    imgPerPlus.setVisibility(View.VISIBLE);
                    imgConPlus.setVisibility(View.VISIBLE);
                    llConDetail.setVisibility(View.GONE);
                    llMisDetail.setVisibility(View.GONE);
                    imgMisPlus.setVisibility(View.VISIBLE);

                }
                else {
                    imgOffiPlus.setVisibility(View.VISIBLE);
                    imgOffiMinus.setVisibility(View.GONE);
                    llOffDetail.setVisibility(View.GONE);
                    llPerDetail.setVisibility(View.GONE);
                    imgPerPlus.setVisibility(View.VISIBLE);
                    imgConPlus.setVisibility(View.VISIBLE);
                    llConDetail.setVisibility(View.GONE);
                    llMisDetail.setVisibility(View.GONE);
                    imgMisPlus.setVisibility(View.VISIBLE);


                }
            }
        });

        llPer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgPerPlus.getVisibility()==view.VISIBLE){
                    imgPerPlus.setVisibility(View.GONE);
                    imgPerMinus.setVisibility(View.VISIBLE);
                    llPerDetail.setVisibility(View.VISIBLE);
                    llOffDetail.setVisibility(View.GONE);
                    imgOffiPlus.setVisibility(View.VISIBLE);
                    imgConPlus.setVisibility(View.VISIBLE);
                    llConDetail.setVisibility(View.GONE);
                    llMisDetail.setVisibility(View.GONE);
                    imgMisPlus.setVisibility(View.VISIBLE);


                }
                else {
                    imgPerPlus.setVisibility(View.VISIBLE);
                    imgPerMinus.setVisibility(View.GONE);
                    llPerDetail.setVisibility(View.GONE);
                    llOffDetail.setVisibility(View.GONE);
                    imgOffiPlus.setVisibility(View.VISIBLE);
                    imgConPlus.setVisibility(View.VISIBLE);
                    llConDetail.setVisibility(View.GONE);
                    llMisDetail.setVisibility(View.GONE);
                    imgMisPlus.setVisibility(View.VISIBLE);


                }
            }
        });

        llCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgConPlus.getVisibility()==view.VISIBLE){
                    imgConPlus.setVisibility(View.GONE);
                    imgConMinus.setVisibility(View.VISIBLE);
                    llConDetail.setVisibility(View.VISIBLE);
                    imgOffiPlus.setVisibility(View.VISIBLE);
                    llOffDetail.setVisibility(View.GONE);
                    llPerDetail.setVisibility(View.GONE);
                    imgPerPlus.setVisibility(View.VISIBLE);
                    llMisDetail.setVisibility(View.GONE);
                    imgMisPlus.setVisibility(View.VISIBLE);

                }
                else {
                    imgConPlus.setVisibility(View.VISIBLE);
                    imgConMinus.setVisibility(View.GONE);
                    llConDetail.setVisibility(View.GONE);
                    imgOffiPlus.setVisibility(View.VISIBLE);
                    llOffDetail.setVisibility(View.GONE);
                    llPerDetail.setVisibility(View.GONE);
                    imgPerPlus.setVisibility(View.VISIBLE);
                    llMisDetail.setVisibility(View.GONE);
                    imgMisPlus.setVisibility(View.VISIBLE);

                }
            }
        });

        llMis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgMisPlus.getVisibility()==View.VISIBLE){
                    imgMisPlus.setVisibility(View.GONE);
                    llMisDetail.setVisibility(View.VISIBLE);
                    imgMisMinus.setVisibility(View.VISIBLE);
                    imgOffiPlus.setVisibility(View.VISIBLE);
                    imgConPlus.setVisibility(View.VISIBLE);
                    imgPerPlus.setVisibility(View.VISIBLE);
                    llOffDetail.setVisibility(View.GONE);
                    llPerDetail.setVisibility(View.GONE);
                    llConDetail.setVisibility(View.GONE);
                }
                else {
                    imgMisPlus.setVisibility(View.VISIBLE);
                    llMisDetail.setVisibility(View.GONE);
                    imgMisMinus.setVisibility(View.GONE);
                    imgOffiPlus.setVisibility(View.VISIBLE);
                    imgConPlus.setVisibility(View.VISIBLE);
                    imgPerPlus.setVisibility(View.VISIBLE);
                    llOffDetail.setVisibility(View.GONE);
                    llPerDetail.setVisibility(View.GONE);
                    llConDetail.setVisibility(View.GONE);
                }
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProfileActivity.this,DashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    public void profileFunction() {
        String surl ="http://111.93.182.174/GeniusiOSApi/api/gcl_KYC?AEMConsultantID="+empConsId+"&AEMClientID="+empClinId+"&AEMClientOfficeID="+empClintOffId+"&AEMEmployeeID="+empId+"&SecurityCode="+pref.getSecurityCode()+"&WorkingStatus=1&CurrentPage=0";
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
                            String responseText=job1.optString("responseText");
                            boolean responseStatus=job1.optBoolean("responseStatus");
                            if (responseStatus){
                                Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData=job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++){
                                    JSONObject obj=responseData.getJSONObject(i);
                                    String AEMEmployeeID=obj.optString("AEMEmployeeID");
                                    tvEmplId.setText(AEMEmployeeID);

                                    String Code=obj.optString("Code");
                                    tvEmpCode.setText(Code);

                                    String Name=obj.optString("Name");
                                    tvEmpName.setText(Name);

                                    String DateOfJoining=obj.optString("DateOfJoining");
                                    tvDOJ.setText(DateOfJoining);

                                    String Department=obj.optString("Department");
                                    tvDepartment.setText(Department);

                                    String Designation=obj.optString("Designation");
                                    tvDesignation.setText(Designation);

                                    String Location=obj.optString("Location");
                                    tvLocation.setText(Location);

                                    String Sex=obj.optString("Sex");
                                    if (!Sex.equals("")) {
                                        tvGender.setText(Sex);
                                    }else {
                                        tvGender.setText("N/A");
                                    }

                                    String DateOfBirth=obj.optString("DateOfBirth");
                                    if (!DateOfBirth.equals("")) {
                                        tvEmpDOB.setText(DateOfBirth);
                                    }else {
                                        tvEmpDOB.setText("N/A");
                                    }

                                    String GuardianName=obj.optString("GuardianName");
                                    if (!GuardianName.equals("")) {
                                        tvGurdianName.setText(GuardianName);
                                    }else {
                                        tvGurdianName.setText("N/A");
                                    }

                                    String RelationShip=obj.optString("RelationShip");
                                    if (!RelationShip.equals("")) {
                                        tvRealtionShip.setText(RelationShip);

                                    }else {
                                        tvRealtionShip.setText("N/A");
                                    }

                                    String Qualification=obj.optString("Qualification");
                                    if (!Qualification.equals("")) {
                                        tvQualification.setTag(Qualification);
                                    }else {
                                        tvQualification.setText("N/A");
                                    }

                                    String MaritalStatus=obj.optString("MaritalStatus");

                                    if (!MaritalStatus.equals("")){
                                        tvMarital.setText(MaritalStatus);
                                    }else {
                                        tvMarital.setText("N/A");
                                    }

                                    String BloodGroup=obj.optString("BloodGroup");
                                    if (!BloodGroup.equals("")) {
                                        tvBloodGroup.setText(BloodGroup);
                                    }else {
                                        tvBloodGroup.setText("N/A");
                                    }

                                    String PermanentAddress=obj.optString("PermanentAddress");
                                    if (!PermanentAddress.equals("")){
                                        tvParAddr.setText(PermanentAddress);
                                    }else {
                                        tvParAddr.setText("N/A");
                                    }

                                    String PresentAddress=obj.optString("PresentAddress");
                                    if (!PresentAddress.equals("")){
                                        tvPreAddr.setText(PresentAddress);
                                    }else {
                                        tvPreAddr.setText("N/A");
                                    }

                                    String Mobile=obj.optString("Mobile");
                                    if (!Mobile.equals("")){
                                        tvPhnNumber.setText(Mobile);
                                    }else {
                                        tvPhnNumber.setText("N/A");
                                    }

                                    String EmailID=obj.optString("EmailID");
                                    if (!EmailID.equals("")){
                                        tvEmail.setText(EmailID);
                                    }else {
                                        tvEmail.setText("N/A");
                                    }

                                    String PFNumber=obj.optString("PFNumber");
                                    if (!PFNumber.equals("")){
                                        tvPfNumber.setText(PFNumber);
                                    }else {
                                        tvPfNumber.setText("N/A");
                                    }

                                    String ESINumber=obj.optString("ESINumber");
                                    if (!ESINumber.equals("")){
                                        tvEsiNumber.setText(ESINumber);
                                    }


                                    String BankName=obj.optString("BankName");
                                    if (!BankName.equals("")){
                                        tvBankName.setText(BankName);
                                    }


                                    String AccountNumber=obj.optString("AccountNumber");
                                    if (!AccountNumber.equals("")){
                                        tvAcNumber.setText(AccountNumber);
                                    }else {
                                        tvAcNumber.setText("N/A");
                                    }

                                    String AadharCard=obj.optString("AadharCard");
                                    if (!AadharCard.equals("")){
                                        tvAddharNumber.setText(AadharCard);
                                    }else {
                                        tvAddharNumber.setText("N/A");
                                    }

                                    String UanNo=obj.optString("UanNo");
                                    if (!UanNo.equals("")){
                                        tvUanNumber.setText(UanNo);
                                    }else {
                                        tvUanNumber.setText("N/A");
                                    }
                                }
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
                Toast.makeText(ProfileActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert",error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");

    }



}
