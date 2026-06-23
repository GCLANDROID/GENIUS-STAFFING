package io.cordova.myapp00d753.activity.metso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.EmployeeDashBoardActivity;
import io.cordova.myapp00d753.activity.NewUserDashboardActivity;
import io.cordova.myapp00d753.activity.metso.adapter.ApproverAutoCompleteAdapter;
import io.cordova.myapp00d753.activity.metso.adapter.SupervisorFilterAdapter;
import io.cordova.myapp00d753.activity.metso.model.ApproverModel;
import io.cordova.myapp00d753.module.SpineerItemModel;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.ShowDialog;

public class MetsoPMSReportActivity extends AppCompatActivity {
    private static final String TAG = "MetsoPMSReportActivity";
    io.cordova.myapp00d753.databinding.ActivityMetsoPmsreportBinding binding;
    int y,ly;
    String cuyear,lastyear;
    Pref pref;
    String year;
    ArrayList<String>yearList=new ArrayList<>();
    ArrayList<ApproverModel> approverList;
    ArrayList<SpineerItemModel> approverList2;
    ApproverAutoCompleteAdapter approverAutoCompleteAdapter;
    Dialog dialogLocationPopUp;
    long approverID;
    TextView tvApproverName;
    Dialog searchApproverDialog;
    String color;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_metso_pmsreport);
        initView();
    }


    private void initView(){
        pref=new Pref(MetsoPMSReportActivity.this);
        y= Calendar.getInstance().get(Calendar.YEAR);
        color = "<font color='#EE0000'>*</font>";
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
                Intent intent=new Intent(MetsoPMSReportActivity.this, NewUserDashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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
        binding.btnChangeApprover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                approverpopup();
            }
        });
        getApproverList();
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
                            if (!E_Targt_Remarks.equals("null")){
                                binding.tvSelfTarget.setText(E_Targt_Remarks);
                                String A_Targt_ApprovedBy = jsonObject.optString("A_Targt_ApprovedBy");
                                String TargetApprovedBy = "<font color='#EE0000'>"+jsonObject.optString("TargetApprovedBy")+"</font>";
                                binding.tvTargetApproverName.setText(Html.fromHtml("Target set by Approver "+TargetApprovedBy+": "));
                                String A_Targt_Remarks = jsonObject.optString("A_Targt_Remarks");
                                binding.tvApproverTarget.setText(A_Targt_Remarks);
                                String E_Achv_Remarks = jsonObject.optString("E_Achv_Remarks");
                                binding.tvSelfAcheivement.setText(E_Achv_Remarks);
                                String E_Achv_Rating = jsonObject.optString("E_Achv_Rating");
                                binding.tvSelfRating.setText(E_Achv_Rating);
                                String A_Achv_ApprovedBy = "<font color='#EE0000'>"+jsonObject.optString("A_Achv_ApprovedBy")+"</font>";
                                binding.tvAchievementApproverName.setText(Html.fromHtml("Acheivement set by Approver "+A_Achv_ApprovedBy+":"));
                                String A_Achv_Remarks = jsonObject.optString("A_Achv_Remarks");
                                binding.tvApproverAchievement.setText(A_Achv_Remarks);
                                String A_Achv_Rating = jsonObject.optString("A_Achv_Rating");
                                binding.tvApproverRating.setText(A_Achv_Rating);
                                // boolean _status = job1.getBoolean("status");


                                if (!A_Achv_Rating.isEmpty()){
                                    binding.btnChangeApprover.setVisibility(View.GONE);
                                } else {
                                    binding.btnChangeApprover.setVisibility(View.VISIBLE);
                                }
                                binding.tvApproverName.setText(A_Targt_ApprovedBy);
                                binding.scView.setVisibility(View.VISIBLE);
                                binding.llNoDataFound.setVisibility(View.GONE);
                            } else {
                                binding.scView.setVisibility(View.GONE);
                                binding.llNoDataFound.setVisibility(View.VISIBLE);
                            }
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

    private void getApproverList() {
        ProgressDialog progressDialog=new ProgressDialog(MetsoPMSReportActivity.this);
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
                                approverList2 = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject objectResponse = jsonArray.getJSONObject(i);
                                    approverList.add(new ApproverModel(objectResponse.getInt("UserId"),
                                            objectResponse.getString("UserName")));
                                    /*approverList2.add(new SpineerItemModel(objectResponse.getString("UserName"),
                                            String.valueOf(objectResponse.getInt("UserId"))));*/
                                }

                                approverAutoCompleteAdapter = new ApproverAutoCompleteAdapter(MetsoPMSReportActivity.this,approverList);
                                progressDialog.cancel();



                                //llMain.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        //Handle the error response

                        Toast.makeText(MetsoPMSReportActivity.this, "Getting Some Error", Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                    }
                });
    }

    private void approverpopup() {
        dialogLocationPopUp = new Dialog(MetsoPMSReportActivity.this);
        dialogLocationPopUp.setContentView(R.layout.metso_att_location_selection_dialog);
        dialogLocationPopUp.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogLocationPopUp.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ImageView imgCancel = dialogLocationPopUp.findViewById(R.id.imgCancel);
        imgCancel.setVisibility(View.VISIBLE);
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
        actApproverName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.e("charSequence", "onTextChanged: "+charSequence);
                approverID = 0;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        actApproverName.setAdapter(approverAutoCompleteAdapter);
        actApproverName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ApproverModel selectedItem = (ApproverModel) adapterView.getItemAtPosition(i);
                actApproverName.setText(selectedItem.getApproverName());
                //tvApproverName.setText("You select "+selectedItem.getApproverName()+" as an approver");
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
                } else if (approverID == 0) {
                    txtErrorApprover.setVisibility(View.VISIBLE);
                    txtErrorApprover.setText("Please select an approver name from the search list");
                } else {
                    approverChange();
                    dialogLocationPopUp.cancel();
                }
            }
        });
        dialogLocationPopUp.show();
        dialogLocationPopUp.setCancelable(false);
    }

    private void approverChange() {
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.setCancelable(true);
        progressBar.show();
        AndroidNetworking.post(AppData.Metso_Change_Employee_PMS_Approver)
                .addBodyParameter("MasterID", pref.getMasterId())
                .addBodyParameter("PMSYear", year)
                .addBodyParameter("ApproverID",String.valueOf(approverID))
                .addBodyParameter("SecurityCode", pref.getSecurityCode())
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressBar.dismiss();
                            Log.e(TAG, "APPROVER_CHANGE: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                ShowDialog.showSuccessDialog(MetsoPMSReportActivity.this, Response_Message, new ShowDialog.ResultListener() {
                                    @Override
                                    public void onSuccess() {
                                        ShowDialog.onDismiss();
                                        getPMSReport();
                                    }
                                });
                            } else {
                                ShowDialog.showErrorDialog(MetsoPMSReportActivity.this, Response_Message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "APPROVER_CHANGE_error: "+anError);
                        progressBar.dismiss();
                    }
                });
    }

    private void openSupervisorPopUp() {
        searchApproverDialog = new Dialog(MetsoPMSReportActivity.this, R.style.CustomDialogNew2);
        searchApproverDialog.setContentView(R.layout.approve_search_layout);
        searchApproverDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        searchApproverDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        searchApproverDialog.setCancelable(true);

        TextView txtPopupHeadline = searchApproverDialog.findViewById(R.id.txtPopupHeadline);
        SearchView wbsCodeSearchView = (SearchView) searchApproverDialog.findViewById(R.id.wbsCodeSearchView);
        ImageView imgCancel = searchApproverDialog.findViewById(R.id.imgCancel);
        RecyclerView rvWbsCode = searchApproverDialog.findViewById(R.id.rvWbsCode);

        wbsCodeSearchView.setQueryHint("Search Approver");
        //txtPopupHeadline.setText("Select Cost Center");
        rvWbsCode.setLayoutManager(new LinearLayoutManager(MetsoPMSReportActivity.this));
        ArrayList<SpineerItemModel> approverList2Copy = new ArrayList<>();
        approverList2Copy = (ArrayList<SpineerItemModel>) approverList2.clone();
        SupervisorFilterAdapter supervisorFilterAdapter = new SupervisorFilterAdapter(MetsoPMSReportActivity.this, approverList2Copy);
        rvWbsCode.setAdapter(supervisorFilterAdapter);

        wbsCodeSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                supervisorFilterAdapter.getFilter().filter(s);
                return false;
            }
        });

        /*supervisorFilterAdapter.setCostCenterSelectListener(new MetsoNewReimbursementClaimActivity.CostCenterSelectListener() {
            @Override
            public void onClick(String cost_center_id, String cost_center) {
                CostCentreId = cost_center_id;
                WbsId = "0";
                txtCostCenter.setText(cost_center);
                searchApproverDialog.dismiss();
                txtWbsCode.setEnabled(false);
                txtWbsCode.setTextColor(Color.parseColor("#F2CAC9C9"));
            }
        });*/

        supervisorFilterAdapter.setSupervisorSelectListener(new MetsoNewReimbursementClaimActivity.SupervisorSelectListener() {
            @Override
            public void onClick(String supervisor_id, String supervisor) {

            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchApproverDialog.dismiss();
            }
        });
        searchApproverDialog.show();
    }
}