package io.cordova.myapp00d753.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.ProfileAdapter;
import io.cordova.myapp00d753.module.ProfileModule;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;
import io.cordova.myapp00d753.utility.Pref;

public class SupProfileActivity extends AppCompatActivity {
    LinearLayout llLoader;
    RecyclerView rvProfile;
    ProgressBar progressBar;
    ArrayList<ProfileModule>profileList=new ArrayList<>();
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    public static int mPageCount = 0;
    boolean mIsEndReached = false;
    private boolean loading = false;
    LinearLayoutManager layoutManager;
    Pref pref;
    ProfileAdapter padapter;
    Button btnNext;
    ImageView imgBack;
    ImageView imgHome;
    NetworkConnectionCheck connectionCheck;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sup_profile);
        mPageCount=1;
        initialize();
        if (connectionCheck.isNetworkAvailable()) {
            getProfile();
        }else {
            connectionCheck.getNetworkActiveAlert().show();
        }
        onClick();
    }

    private void initialize(){
        pref=new Pref(getApplicationContext());
        connectionCheck=new NetworkConnectionCheck(SupProfileActivity.this);
        btnNext=(Button) findViewById(R.id.btnNext);
        progressBar=(ProgressBar)findViewById(R.id.WLpagination_loader);
        llLoader=(LinearLayout)findViewById(R.id.llLoader);
        rvProfile=(RecyclerView)findViewById(R.id.rvProfile);
         layoutManager
                = new LinearLayoutManager(SupProfileActivity.this, LinearLayoutManager.VERTICAL, false);
        rvProfile.setLayoutManager(layoutManager);
/*        rvProfile.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                    if (!loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = true;

                            progressBar.setVisibility(View.VISIBLE);
                            if (!mIsEndReached) {
                                mPageCount=mPageCount+1;
                               getProfile();
                            }

                        }
                    }
                }
            }
        });*/

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading = true;
                mPageCount=mPageCount+1;
                getProfile();
            }
        });
        setAdapter();

        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
    }

    private void setAdapter(){
        padapter=new ProfileAdapter(profileList,SupProfileActivity.this);

        rvProfile.setAdapter(padapter);

    }




    private void getProfile(){
        llLoader.setVisibility(View.VISIBLE);
        rvProfile.setVisibility(View.GONE);

        String surl ="http://111.93.182.174/GeniusiOSApi/api/gcl_KYC?AEMConsultantID="+pref.getEmpConId()+"&AEMClientID="+pref.getEmpClintId()+"&AEMClientOfficeID="+pref.getEmpClintOffId()+"&AEMEmployeeID="+pref.getEmpId()+"&SecurityCode="+pref.getSecurityCode()+"&WorkingStatus=1&CurrentPage="+mPageCount;
        Log.d("inputpr",surl);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);

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
                                    String Code=obj.optString("Code");
                                    String Name=obj.optString("Name");
                                    String DateOfJoining=obj.optString("DateOfJoining");
                                    String Department=obj.optString("Department");
                                    String Designation=obj.optString("Designation");
                                    String Location=obj.optString("Location");
                                    String Sex=obj.optString("Sex");
                                    String DateOfBirth=obj.optString("DateOfBirth");
                                    String GuardianName=obj.optString("GuardianName");
                                    String RelationShip=obj.optString("RelationShip");
                                    String Qualification=obj.optString("Qualification");
                                    String MaritalStatus=obj.optString("MaritalStatus");
                                    String BloodGroup=obj.optString("BloodGroup");
                                    String PermanentAddress=obj.optString("PermanentAddress");
                                    String PresentAddress=obj.optString("PresentAddress");
                                    String Mobile=obj.optString("Mobile");
                                    String EmailID=obj.optString("EmailID");
                                    String PFNumber=obj.optString("PFNumber");
                                    String ESINumber=obj.optString("ESINumber");
                                    String BankName=obj.optString("BankName");
                                    String AccountNumber=obj.optString("AccountNumber");
                                    String AadharCard=obj.optString("AadharCard");
                                    String UanNo=obj.optString("UanNo");
                                    String Phone=obj.optString("Phone");
                                    ProfileModule profileModule=new ProfileModule(AEMEmployeeID,Code,Name,DateOfJoining,Department,Designation,Location,Sex,DateOfBirth,GuardianName,RelationShip,Qualification,MaritalStatus,BloodGroup,PermanentAddress,PresentAddress,Phone,Mobile,EmailID,PFNumber,ESINumber,BankName,AccountNumber,AadharCard,UanNo);
                                    profileList.add(profileModule);
                                }
                                padapter.notifyDataSetChanged();
                                llLoader.setVisibility(View.GONE);
                                rvProfile.setVisibility(View.VISIBLE);
                               ;
                            }
                            else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SupProfileActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(SupProfileActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert",error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");


    }

    public void updateStudentAttendanceStatus(int position,boolean status)
    {
        profileList.get(position).setSelected(status);
        padapter.notifyDataSetChanged();
    }

    private void onClick(){
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SupProfileActivity.this,DashBoardActivity.class);
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

}
