package io.cordova.myapp00d753.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.LeaveBalanceReportAdapter;
import io.cordova.myapp00d753.module.LeaveBalanceDetailsModel;
import io.cordova.myapp00d753.module.SpinnerModel;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

public class LeaveBalanceReportFragment extends Fragment {
    private static final String TAG = "LeaveBalanceReport";
    RecyclerView rvRecyclerView;
    LeaveBalanceReportAdapter leaveBalanceReportAdapter;
    ArrayList<LeaveBalanceDetailsModel> itemList = new ArrayList<>();
    Pref pref;
    View v;
    LinearLayout llNoData;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_leave_balance_report, container, false);
        initView(v);
        return v;
    }

    private void initView(View v) {
        pref = new Pref(getContext());
        rvRecyclerView = v.findViewById(R.id.rvRecyclerView);
        rvRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        llNoData = v.findViewById(R.id.llNoData);
        getLeaveAllDetails();
    }


    private void getLeaveAllDetails() {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading...");
        pd.setCancelable(true);
        pd.show();
        //names.clear();
        String surl = AppData.url +"Leave/LeaveApplicationDetails?CompanyID=" + pref.getEmpClintId() + "&EmployeeID=" + pref.getEmpId() + "&ApproverID=" + pref.getEmpId() + "&SecurityCode=" + pref.getSecurityCode();
        Log.d("printurlrequest", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        Log.d("responseAttendance", response);

                        // attendabceInfiList.clear();

                        //leaveTypeList.add("Please select");
                        //mLeaveTypeList.add(new SpinnerModel("0", "0"));
                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e(TAG, "LEAVE_BALANCE: " + job1.toString(4));
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                //for request,reject,etc details
                               /* JSONArray leaveRequestDetails = responseData.optJSONArray(0);
                                for (int i = 0; i < leaveRequestDetails.length(); i++) {
                                    JSONObject requestObject = leaveRequestDetails.optJSONObject(i);
                                    String Request = requestObject.optString("Request");
                                    tvRequested.setText(Request);
                                    String Approve = requestObject.optString("Approve");
                                    String Reject = requestObject.optString("Reject");
                                    String Pending = requestObject.optString("Pending");
                                    tvApporved.setText(Approve);
                                    tvRejected.setText(Reject);
                                    tvPending.setText(Pending);
                                }*/

                                //for  balance details
                                JSONArray leaveBalanceArray = responseData.optJSONArray(1);
                                if(leaveBalanceArray.length() > 0){
                                    for (int i = 0; i < leaveBalanceArray.length(); i++) {
                                        JSONObject balanceObject = leaveBalanceArray.optJSONObject(i);
                                        final String Code = balanceObject.optString("Code");
                                        final String Opening = balanceObject.optString("Opening");
                                        final String LeaveAvailed = balanceObject.optString("LeaveAvailed");
                                        final String Avaliable = balanceObject.optString("Avaliable");
                                        String LeaveTypeID = balanceObject.optString("LeaveTypeID");
                                        //typeAvaild.add(LeaveTypeID + "_" + Avaliable);

                                        LeaveBalanceDetailsModel model = new LeaveBalanceDetailsModel(Code, Opening, LeaveAvailed, Avaliable);
                                        itemList.add(model);
                                    }
                                    leaveBalanceReportAdapter = new LeaveBalanceReportAdapter(getActivity(),itemList);
                                    rvRecyclerView.setAdapter(leaveBalanceReportAdapter);

                                    llNoData.setVisibility(View.GONE);
                                } else {
                                    llNoData.setVisibility(View.VISIBLE);
                                }
                            } else {
                                llNoData.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                llNoData.setVisibility(View.VISIBLE);
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}