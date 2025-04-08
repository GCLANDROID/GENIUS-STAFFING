package io.cordova.myapp00d753.activity.metso;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
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

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.metso.adapter.ApproverAutoCompleteAdapter;
import io.cordova.myapp00d753.activity.metso.model.ApproverModel;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

public class MetsoPMSTargetAchivementActivity extends AppCompatActivity {
    TextView tvTargetAchievement;
    EditText etRemarks;
    LinearLayout llExceptional,llExcellent,llGood,llImprovement,llNA,llRating;
    ImageView imgExceptional,imgSelectedExceptional,imgExcellent,imgSelectedExcellent,imgGood,imgSelectedGood,imgImprovement,imgSelectedImprovement,imgNA,imgSelectedNA;
    String rating="";
    String bFlagg;
    AlertDialog alerDialog1;
    Pref pref;
    Button btnSave;
    ApproverAutoCompleteAdapter approverAutoCompleteAdapter;
    Dialog dialogLocationPopUp;
    long approverID;
    ArrayList<ApproverModel> approverList;
    TextView tvApproverName;
    ImageView imgBack,imgHome;
    TextView tvLastYearTarget;
    LinearLayout llLastYrTarget;
    Button btnReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metso_pmstarget_achivement);
        initView();
        getPMSSubmitDetails();

        onCLick();
    }

    private void initView(){
        pref=new Pref(MetsoPMSTargetAchivementActivity.this);
        btnReport=(Button)findViewById(R.id.btnReport);
        llLastYrTarget=(LinearLayout)findViewById(R.id.llLastYrTarget);
        tvLastYearTarget=(TextView)findViewById(R.id.tvLastYearTarget);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        imgBack=(ImageView)findViewById(R.id.imgBack);;

        tvApproverName=(TextView)findViewById(R.id.tvApproverName);

        tvTargetAchievement=(TextView) findViewById(R.id.tvTargetAchievement);

        etRemarks=(EditText) findViewById(R.id.etRemarks);

        llExceptional=(LinearLayout) findViewById(R.id.llExceptional);
        llExcellent=(LinearLayout) findViewById(R.id.llExcellent);
        llGood=(LinearLayout) findViewById(R.id.llGood);
        llImprovement=(LinearLayout) findViewById(R.id.llImprovement);
        llNA=(LinearLayout) findViewById(R.id.llNA);
        llRating=(LinearLayout) findViewById(R.id.llRating);

        imgExceptional=(ImageView) findViewById(R.id.imgExceptional);
        imgSelectedExceptional=(ImageView) findViewById(R.id.imgSelectedExceptional);
        imgExcellent=(ImageView) findViewById(R.id.imgExcellent);
        imgSelectedExcellent=(ImageView) findViewById(R.id.imgSelectedExcellent);
        imgGood=(ImageView) findViewById(R.id.imgGood);
        imgSelectedGood=(ImageView) findViewById(R.id.imgSelectedGood);
        imgImprovement=(ImageView) findViewById(R.id.imgImprovement);
        imgSelectedImprovement=(ImageView) findViewById(R.id.imgSelectedImprovement);
        imgNA=(ImageView) findViewById(R.id.imgNA);
        imgSelectedNA=(ImageView) findViewById(R.id.imgSelectedNA);


        btnSave=(Button) findViewById(R.id.btnSave);
    }

    private void onCLick(){
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MetsoPMSTargetAchivementActivity.this,MetsoPMSReportActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bFlagg.equals("2")){
                    if (etRemarks.getText().toString().length()>0){
                        postTargetAchevement("2");
                    }else {
                        Toast.makeText(MetsoPMSTargetAchivementActivity.this,"Please Enter Your Target Details",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    if (etRemarks.getText().toString().length()>0){
                        if (!rating.equals("")){
                            postTargetAchevement("3");

                        }else {
                            Toast.makeText(MetsoPMSTargetAchivementActivity.this,"Please Give Your Self Ratings",Toast.LENGTH_SHORT).show();

                        }

                    }else {
                        Toast.makeText(MetsoPMSTargetAchivementActivity.this,"Please Enter Your Achievement Details",Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
        llExceptional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgSelectedExceptional.getVisibility() == View.GONE){
                    imgSelectedExceptional.setVisibility(View.VISIBLE);
                    imgExceptional.setVisibility(View.GONE);

                    imgExcellent.setVisibility(View.VISIBLE);
                    imgSelectedExcellent.setVisibility(View.GONE);

                    imgGood.setVisibility(View.VISIBLE);
                    imgSelectedGood.setVisibility(View.GONE);

                    imgImprovement.setVisibility(View.VISIBLE);
                    imgSelectedImprovement.setVisibility(View.GONE);

                    imgNA.setVisibility(View.VISIBLE);
                    imgSelectedNA.setVisibility(View.GONE);

                    rating="Exceptional";
                }else {
                    imgSelectedExceptional.setVisibility(View.GONE);
                    imgExceptional.setVisibility(View.VISIBLE);

                    imgExcellent.setVisibility(View.VISIBLE);
                    imgSelectedExcellent.setVisibility(View.GONE);

                    imgGood.setVisibility(View.VISIBLE);
                    imgSelectedGood.setVisibility(View.GONE);

                    imgImprovement.setVisibility(View.VISIBLE);
                    imgSelectedImprovement.setVisibility(View.GONE);

                    imgNA.setVisibility(View.VISIBLE);
                    imgSelectedNA.setVisibility(View.GONE);

                    rating="";
                }
            }
        });


        llExcellent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgSelectedExcellent.getVisibility() == View.GONE){
                    imgSelectedExceptional.setVisibility(View.GONE);
                    imgExceptional.setVisibility(View.VISIBLE);

                    imgExcellent.setVisibility(View.GONE);
                    imgSelectedExcellent.setVisibility(View.VISIBLE);

                    imgGood.setVisibility(View.VISIBLE);
                    imgSelectedGood.setVisibility(View.GONE);

                    imgImprovement.setVisibility(View.VISIBLE);
                    imgSelectedImprovement.setVisibility(View.GONE);

                    imgNA.setVisibility(View.VISIBLE);
                    imgSelectedNA.setVisibility(View.GONE);

                    rating="Excellent";
                }else {
                    imgSelectedExceptional.setVisibility(View.GONE);
                    imgExceptional.setVisibility(View.VISIBLE);

                    imgExcellent.setVisibility(View.VISIBLE);
                    imgSelectedExcellent.setVisibility(View.GONE);

                    imgGood.setVisibility(View.VISIBLE);
                    imgSelectedGood.setVisibility(View.GONE);

                    imgImprovement.setVisibility(View.VISIBLE);
                    imgSelectedImprovement.setVisibility(View.GONE);

                    imgNA.setVisibility(View.VISIBLE);
                    imgSelectedNA.setVisibility(View.GONE);

                    rating="";
                }
            }
        });


        llGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgSelectedGood.getVisibility() == View.GONE){
                    imgSelectedExceptional.setVisibility(View.GONE);
                    imgExceptional.setVisibility(View.VISIBLE);

                    imgExcellent.setVisibility(View.VISIBLE);
                    imgSelectedExcellent.setVisibility(View.GONE);

                    imgGood.setVisibility(View.GONE);
                    imgSelectedGood.setVisibility(View.VISIBLE);

                    imgImprovement.setVisibility(View.VISIBLE);
                    imgSelectedImprovement.setVisibility(View.GONE);

                    imgNA.setVisibility(View.VISIBLE);
                    imgSelectedNA.setVisibility(View.GONE);

                    rating="Good";
                }else {
                    imgSelectedExceptional.setVisibility(View.GONE);
                    imgExceptional.setVisibility(View.VISIBLE);

                    imgExcellent.setVisibility(View.VISIBLE);
                    imgSelectedExcellent.setVisibility(View.GONE);

                    imgGood.setVisibility(View.VISIBLE);
                    imgSelectedGood.setVisibility(View.GONE);

                    imgImprovement.setVisibility(View.VISIBLE);
                    imgSelectedImprovement.setVisibility(View.GONE);

                    imgNA.setVisibility(View.VISIBLE);
                    imgSelectedNA.setVisibility(View.GONE);

                    rating="";
                }
            }
        });

        llImprovement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgSelectedImprovement.getVisibility() == View.GONE){
                    imgSelectedExceptional.setVisibility(View.GONE);
                    imgExceptional.setVisibility(View.VISIBLE);

                    imgExcellent.setVisibility(View.VISIBLE);
                    imgSelectedExcellent.setVisibility(View.GONE);

                    imgGood.setVisibility(View.VISIBLE);
                    imgSelectedGood.setVisibility(View.GONE);

                    imgImprovement.setVisibility(View.GONE);
                    imgSelectedImprovement.setVisibility(View.VISIBLE);

                    imgNA.setVisibility(View.VISIBLE);
                    imgSelectedNA.setVisibility(View.GONE);

                    rating="Need improvement";
                }else {
                    imgSelectedExceptional.setVisibility(View.GONE);
                    imgExceptional.setVisibility(View.VISIBLE);

                    imgExcellent.setVisibility(View.VISIBLE);
                    imgSelectedExcellent.setVisibility(View.GONE);

                    imgGood.setVisibility(View.VISIBLE);
                    imgSelectedGood.setVisibility(View.GONE);

                    imgImprovement.setVisibility(View.VISIBLE);
                    imgSelectedImprovement.setVisibility(View.GONE);

                    imgNA.setVisibility(View.VISIBLE);
                    imgSelectedNA.setVisibility(View.GONE);

                    rating="";
                }
            }
        });



        llNA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgSelectedNA.getVisibility() == View.GONE){
                    imgSelectedExceptional.setVisibility(View.GONE);
                    imgExceptional.setVisibility(View.VISIBLE);

                    imgExcellent.setVisibility(View.VISIBLE);
                    imgSelectedExcellent.setVisibility(View.GONE);

                    imgGood.setVisibility(View.VISIBLE);
                    imgSelectedGood.setVisibility(View.GONE);

                    imgImprovement.setVisibility(View.VISIBLE);
                    imgSelectedImprovement.setVisibility(View.GONE);

                    imgNA.setVisibility(View.GONE);
                    imgSelectedNA.setVisibility(View.VISIBLE);

                    rating="Not acceptable";
                }else {
                    imgSelectedExceptional.setVisibility(View.GONE);
                    imgExceptional.setVisibility(View.VISIBLE);

                    imgExcellent.setVisibility(View.VISIBLE);
                    imgSelectedExcellent.setVisibility(View.GONE);

                    imgGood.setVisibility(View.VISIBLE);
                    imgSelectedGood.setVisibility(View.GONE);

                    imgImprovement.setVisibility(View.VISIBLE);
                    imgSelectedImprovement.setVisibility(View.GONE);

                    imgNA.setVisibility(View.VISIBLE);
                    imgSelectedNA.setVisibility(View.GONE);

                    rating="";
                }
            }
        });
    }


    private void postTargetAchevement(String operation) {
        ProgressDialog pg=new ProgressDialog(MetsoPMSTargetAchivementActivity.this);
        pg.setMessage("Loading");
        pg.setCancelable(false);
        pg.show();



        AndroidNetworking.upload(AppData.url+"post_EmployeePMS_Metso")
                .addMultipartParameter("MasterID", pref.getMasterId())
                .addMultipartParameter("Remarks", etRemarks.getText().toString())
                .addMultipartParameter("Rating", rating)
                .addMultipartParameter("SuperID", String.valueOf(approverID))
                .addMultipartParameter("Operation", operation)
                .addMultipartParameter("SecurityCode", "0000")
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("TAG", "onResponse: SUCCESS: " + response);
                        pg.dismiss();
                        try {
                            JSONObject object = new JSONObject(String.valueOf(response));
                            if (object.getBoolean("responseStatus") == true) {

                                successAlert(object.getString("responseText"));
                            } else {

                                Toast.makeText(MetsoPMSTargetAchivementActivity.this, object.getString("responseText"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e("TAG", "onResponse: onError: " + error);
                        pg.cancel();
                    }
                });
    }

    private void successAlert(String message) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MetsoPMSTargetAchivementActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);
        /*if (addflag == 1) {
            tvInvalidDate.setText("Your attendnace has been saved successfully");
        } else {
            tvInvalidDate.setText("Your attendnace has been saved successfully");
        }*/
        tvInvalidDate.setText(message);
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                //Intent intent = new Intent(MetsoAttendanceActivity.this, AttendanceReportActivity.class);
                onBackPressed();
            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(false);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }


    private void getApproverList() {
        ProgressDialog progressDialog=new ProgressDialog(MetsoPMSTargetAchivementActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        AndroidNetworking.get(AppData.url+"Leave/Get_MetsoAttendanceData")
                .addQueryParameter("Mode", "3")
                .addQueryParameter("CompanyID", pref.getEmpClintId())
                .addQueryParameter("SecurityCode", "0000")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject object = new JSONObject(String.valueOf(response));
                            if (object.getBoolean("responseStatus") == true){
                                JSONArray jsonArray = object.getJSONArray("responseData");
                                approverList = new ArrayList<>();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject objectResponse = jsonArray.getJSONObject(i);
                                    approverList.add(new ApproverModel(objectResponse.getInt("UserId"),
                                            objectResponse.getString("UserName")));
                                }

                                approverAutoCompleteAdapter = new ApproverAutoCompleteAdapter(MetsoPMSTargetAchivementActivity.this,approverList);
                                progressDialog.cancel();
                                approverpopup();


                                //llMain.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        //Handle the error response

                        Toast.makeText(MetsoPMSTargetAchivementActivity.this, "Getting Some Error", Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                    }
                });
    }


    private void approverpopup() {
        dialogLocationPopUp = new Dialog(MetsoPMSTargetAchivementActivity.this);
        dialogLocationPopUp.setContentView(R.layout.metso_att_location_selection_dialog);
        dialogLocationPopUp.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogLocationPopUp.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ImageView imgCancel = dialogLocationPopUp.findViewById(R.id.imgCancel);
        imgCancel.setVisibility(View.GONE);
        TextView txtSelectLocation = dialogLocationPopUp.findViewById(R.id.txtSelectLocation);
        TextView tvLocationTitle=dialogLocationPopUp.findViewById(R.id.tvLocationTitle);
        tvLocationTitle.setVisibility(View.GONE);
        txtSelectLocation.setVisibility(View.GONE);
        TextView txtErrorApprover = dialogLocationPopUp.findViewById(R.id.txtErrorApprover);
        TextView txtErrorLocation = dialogLocationPopUp.findViewById(R.id.txtErrorLocation);
        txtErrorLocation.setVisibility(View.GONE);
        AutoCompleteTextView actApproverName = dialogLocationPopUp.findViewById(R.id.actApproverName);
        Spinner spLocation = dialogLocationPopUp.findViewById(R.id.spLocation);
        LinearLayout llApprover = dialogLocationPopUp.findViewById(R.id.llApprover);
        AppCompatButton btnSubmit = dialogLocationPopUp.findViewById(R.id.btnSubmit);


        actApproverName.setAdapter(approverAutoCompleteAdapter);
        actApproverName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ApproverModel selectedItem = (ApproverModel) adapterView.getItemAtPosition(i);
                actApproverName.setText(selectedItem.getApproverName());
                tvApproverName.setText("You select "+selectedItem.getApproverName()+" as an approver");
                approverID = selectedItem.approverId;
                txtErrorApprover.setVisibility(View.GONE);
            }
        });

        txtSelectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spLocation.performClick();
            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogLocationPopUp.cancel();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (actApproverName.getText().toString().trim().isEmpty()){
                    txtErrorApprover.setVisibility(View.VISIBLE);
                } else {
                    // submitOperation();
                    dialogLocationPopUp.cancel();
                }
            }
        });
        dialogLocationPopUp.show();
        dialogLocationPopUp.setCancelable(false);
    }

    private void getPMSSubmitDetails() {
        String surl = AppData.url + "gcl_EmployeePMS_Metso?MasterID=" + pref.getMasterId() + "&SecurityCode=" + pref.getSecurityCode();
        Log.d("attencinput", surl);
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responsecheck", response);
                        progressBar.dismiss();
                        try {
                            JSONArray job1 = new JSONArray(response);
                            JSONObject jsonObject = job1.optJSONObject(0);
                            String bFlag = jsonObject.optString("bFlag");
                            bFlagg=bFlag;
                            if (bFlag.equals("2")){
                                tvTargetAchievement.setText("Please set your current year target below:-");
                                llRating.setVisibility(View.GONE);
                                getApproverList();
                            }else if (bFlag.equals("1")){
                                tvTargetAchievement.setText("Please Your Last Year Achivement Details");
                                llRating.setVisibility(View.VISIBLE);
                                getApproverList();
                                getLastYearTargetDetails();
                            }else if (bFlag.equals("3")){
                                tvTargetAchievement.setText("Please Your Current Year Achivement Details");
                                llRating.setVisibility(View.VISIBLE);
                                getApproverList();
                                getLastYearTargetDetails();
                            }else {
                                tvTargetAchievement.setVisibility(View.GONE);
                                llRating.setVisibility(View.GONE);
                                tvApproverName.setVisibility(View.GONE);
                                etRemarks.setVisibility(View.GONE);
                                btnSave.setVisibility(View.GONE);
                                getLastYearTargetDetails();
                            }



                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                Toast.makeText(MetsoPMSTargetAchivementActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");

    }


    private void getLastYearTargetDetails() {
        String surl = AppData.url + "gcl_EmployeePMSReport_Metso?MasterID=" + pref.getMasterId() + "&Year=2024&Operation=1&SecurityCode=" + pref.getSecurityCode();
        Log.d("attencinput", surl);
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responsecheck", response);
                        progressBar.dismiss();
                        try {
                            JSONArray job1 = new JSONArray(response);
                            JSONObject jsonObject = job1.optJSONObject(0);
                            String E_Targt_Remarks = jsonObject.optString("E_Targt_Remarks");

                            tvLastYearTarget.setText(E_Targt_Remarks);

                            llLastYrTarget.setVisibility(View.VISIBLE);

                            if (E_Targt_Remarks.equals("")){
                                btnReport.setVisibility(View.GONE);
                            }else {
                                btnReport.setVisibility(View.VISIBLE);
                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                Toast.makeText(MetsoPMSTargetAchivementActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");

    }
}