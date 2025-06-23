package io.cordova.myapp00d753.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.LeaveBalanceReportAdapter;
import io.cordova.myapp00d753.adapter.ViewLeaveBalanceAdapter;
import io.cordova.myapp00d753.module.LeaveBalanceDetailsModel;
import io.cordova.myapp00d753.module.ViewLeaveBalanceModel;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

public class ViewLeaveBalanceFragment extends Fragment {
    private static final String TAG = "LeaveBalanceReport";
    RecyclerView rvRecyclerView;
    ViewLeaveBalanceAdapter viewLeaveBalanceAdapter;
    ArrayList<ViewLeaveBalanceModel> itemList = new ArrayList<>();
    Pref pref;
    View v;
    LinearLayout llNoData,llLoading;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_view_leave_balance, container, false);
        initView(v);
        return v;
    }

    private void initView(View v) {
        pref = new Pref(getContext());
        rvRecyclerView = v.findViewById(R.id.rvRecyclerView);
        rvRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        llNoData = v.findViewById(R.id.llNoData);
        llLoading = v.findViewById(R.id.llLoading);
        JSONObject obj1=new JSONObject();
        try {
            obj1.put("MasterID", pref.getMasterId());
            obj1.put("SecurityCode",pref.getSecurityCode());
            getOpenLeaveBalance(obj1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getOpenLeaveBalance(JSONObject jsonObject) {
        llLoading.setVisibility(View.VISIBLE);
        AndroidNetworking.post(AppData.Open_Leave_Balance_Details)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            llLoading.setVisibility(View.GONE);
                            Log.e(TAG, "OPEN_LEAVE_BALANCE: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            if (Response_Code.equals("101")){
                                JSONArray jsonArray = job1.optJSONArray("Response_Data");
                                if(jsonArray.length() > 0){
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        String LeaveYear = obj.getString("LeaveYear");
                                        String LeaveTypeID = obj.getString("LeaveTypeID");
                                        String LeaveTypeName = obj.getString("LeaveTypeName");
                                        String Opening = obj.getString("Opening");
                                        String Increment = obj.getString("Increment");
                                        String Availed = obj.getString("Availed");
                                        String Adjusted = obj.getString("Adjusted");
                                        String Encasement = obj.getString("Encasement");
                                        String Closing = obj.getString("Closing");
                                        itemList.add(new ViewLeaveBalanceModel(LeaveYear,LeaveTypeID,LeaveTypeName,Opening,Increment,Availed,Adjusted,Encasement,Closing));
                                    }
                                    viewLeaveBalanceAdapter = new ViewLeaveBalanceAdapter(getActivity(),itemList);
                                    rvRecyclerView.setAdapter(viewLeaveBalanceAdapter);

                                    llNoData.setVisibility(View.GONE);
                                } else {
                                    llNoData.setVisibility(View.VISIBLE);
                                }
                            } else {
                                llNoData.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        llLoading.setVisibility(View.GONE);
                        llNoData.setVisibility(View.VISIBLE);
                        Log.e(TAG, "OPEN_LEAVE_BALANCE_error: "+anError.getErrorBody());
                    }
                });
    }
}