package io.cordova.myapp00d753.activity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.ProfileAdapter;
import io.cordova.myapp00d753.module.ProfileModule;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;
import io.cordova.myapp00d753.utility.Pref;

public class SupProfileActivity extends AppCompatActivity {
    private static final String TAG = "SupProfileActivity";
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
    LinearLayout llMain;
    LinearLayout llAgain;
    ImageView imgAgain;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sup_profile);
        mPageCount=1;
        initialize();
        if (connectionCheck.isNetworkAvailable()) {
            JSONObject obj=new JSONObject();
            try {
                obj.put("AEMConsultantID", pref.getEmpConId());
                obj.put("AEMClientID",pref.getEmpClintId());
                obj.put("AEMClientOfficeID",pref.getEmpClintOffId());
                obj.put("AEMEmployeeID",pref.getEmpId());
                obj.put("SecurityCode",pref.getSecurityCode());
                obj.put("WorkingStatus","1");
                obj.put("CurrentPage",mPageCount);
                getProfile(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //getProfile();
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
                //getProfile();
                JSONObject obj=new JSONObject();
                try {
                    obj.put("AEMConsultantID", pref.getEmpConId());
                    obj.put("AEMClientID",pref.getEmpClintId());
                    obj.put("AEMClientOfficeID",pref.getEmpClintOffId());
                    obj.put("AEMEmployeeID",pref.getEmpId());
                    obj.put("SecurityCode",pref.getSecurityCode());
                    obj.put("WorkingStatus","1");
                    obj.put("CurrentPage",mPageCount);
                    getProfile(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        setAdapter();

        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        llMain=(LinearLayout)findViewById(R.id.llMain);
        llAgain=(LinearLayout)findViewById(R.id.llAgain);
        imgAgain=(ImageView)findViewById(R.id.imgAgain);
        imgAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getProfile();

                JSONObject obj=new JSONObject();
                try {
                    obj.put("AEMConsultantID", pref.getEmpConId());
                    obj.put("AEMClientID",pref.getEmpClintId());
                    obj.put("AEMClientOfficeID",pref.getEmpClintOffId());
                    obj.put("AEMEmployeeID",pref.getEmpId());
                    obj.put("SecurityCode",pref.getSecurityCode());
                    obj.put("WorkingStatus","1");
                    obj.put("CurrentPage",mPageCount);
                    getProfile(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setAdapter(){
        padapter=new ProfileAdapter(profileList,SupProfileActivity.this);
        rvProfile.setAdapter(padapter);
    }

    private void getProfile(JSONObject jsonObject){
        AndroidNetworking.post(AppData.GCL_KYC)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
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
                                    ProfileModule profileModule=new ProfileModule(AEMEmployeeID,Code,Name,DateOfJoining,Department,Designation,Location,Sex,DateOfBirth,GuardianName,RelationShip,Qualification,MaritalStatus,BloodGroup,PermanentAddress,PresentAddress,Phone,Mobile,EmailID,PFNumber,ESINumber,BankName,AccountNumber,AadharCard,UanNo,false);
                                    profileList.add(profileModule);
                                }
                                padapter.notifyDataSetChanged();
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llAgain.setVisibility(View.GONE);
                            } else {
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llAgain.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(SupProfileActivity.this, "Something want to wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "onError: "+anError);
                        llLoader.setVisibility(View.GONE);
                        llMain.setVisibility(View.GONE);
                        llAgain.setVisibility(View.VISIBLE);
                    }
                });
    }




    private void getProfile(){
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llAgain.setVisibility(View.GONE);

        String surl = AppData.url+"gcl_KYC?AEMConsultantID="+pref.getEmpConId()+"&AEMClientID="+pref.getEmpClintId()+"&AEMClientOfficeID="+pref.getEmpClintOffId()+"&AEMEmployeeID="+pref.getEmpId()+"&SecurityCode="+pref.getSecurityCode()+"&WorkingStatus=1&CurrentPage="+mPageCount;
        Log.d("inputpr",surl);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus=job1.optBoolean("responseStatus");
                            if (responseStatus){
                        //        Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
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
                                    ProfileModule profileModule=new ProfileModule(AEMEmployeeID,Code,Name,DateOfJoining,Department,Designation,Location,Sex,DateOfBirth,GuardianName,RelationShip,Qualification,MaritalStatus,BloodGroup,PermanentAddress,PresentAddress,Phone,Mobile,EmailID,PFNumber,ESINumber,BankName,AccountNumber,AadharCard,UanNo,false);
                                    profileList.add(profileModule);
                                }
                                padapter.notifyDataSetChanged();
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llAgain.setVisibility(View.GONE);
                               ;
                            }
                            else {
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llAgain.setVisibility(View.GONE);

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
                llLoader.setVisibility(View.GONE);
                llMain.setVisibility(View.GONE);
                llAgain.setVisibility(View.VISIBLE);

               // Toast.makeText(SupProfileActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert",error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");


    }

    public void updateStatus(int position,boolean status)
    {
        for (int i =0 ;i<profileList.size();i++)
        {
            if (i==position)
            {
                profileList.get(i).setExpanded(status);
            }
            else
            {
                profileList.get(i).setExpanded(false);
            }
        }
        padapter.notifyDataSetChanged();
    }

    private void onClick(){
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SupProfileActivity.this,SuperVisiorDashBoardActivity.class);
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
    }

}
