package io.cordova.myapp00d753.activity.metso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.EmployeeDashBoardActivity;
import io.cordova.myapp00d753.facereogntion.InterviewFormActivity;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

public class MetsoPMSReportActivity extends AppCompatActivity {
    io.cordova.myapp00d753.databinding.ActivityMetsoPmsreportBinding binding;
    int y,ly;
    String cuyear,lastyear;
    Pref pref;
    String year;
    ArrayList<String>yearList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_metso_pmsreport);
        initView();
    }


    private void initView(){
        pref=new Pref(MetsoPMSReportActivity.this);


        y= Calendar.getInstance().get(Calendar.YEAR);
        cuyear=String.valueOf(y);
        ly=y-1;
        lastyear=String.valueOf(ly);
        yearList.add(cuyear);
        yearList.add(lastyear);
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>
                (MetsoPMSReportActivity.this, android.R.layout.simple_spinner_item,
                        yearList);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spYear.setAdapter(yearAdapter);


        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MetsoPMSReportActivity.this, EmployeeDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                year=yearList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPMSReport();
                binding.scView.setVisibility(View.VISIBLE);
            }
        });

    }


    private void getPMSReport() {
        String surl = AppData.url + "gcl_EmployeePMSReport_Metso?MasterID=" + pref.getMasterId() + "&Year="+year+"&Operation=2&SecurityCode=" + pref.getSecurityCode();
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
                            binding.tvSelfTarget.setText(E_Targt_Remarks);
                            String A_Targt_ApprovedBy = jsonObject.optString("A_Targt_ApprovedBy");
                            binding.tvTargetApproverName.setText("Target set by Approver "+A_Targt_ApprovedBy+": ");
                            String A_Targt_Remarks = jsonObject.optString("A_Targt_Remarks");
                            binding.tvApproverTarget.setText(A_Targt_Remarks);
                            String E_Achv_Remarks = jsonObject.optString("E_Achv_Remarks");
                            binding.tvSelfAcheivement.setText(E_Achv_Remarks);
                            String E_Achv_Rating = jsonObject.optString("E_Achv_Rating");
                            binding.tvSelfRating.setText(E_Achv_Rating);
                            String A_Achv_ApprovedBy = jsonObject.optString("A_Achv_ApprovedBy");
                            binding.tvAchievementApproverName.setText("Acheivement set by Approver "+A_Achv_ApprovedBy+":");
                            String A_Achv_Remarks = jsonObject.optString("A_Achv_Remarks");
                            binding.tvApproverAchievement.setText(A_Achv_Remarks);
                            String A_Achv_Rating = jsonObject.optString("A_Achv_Rating");
                            binding.tvApproverRating.setText(A_Achv_Rating);




                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                Toast.makeText(MetsoPMSReportActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");

    }


}