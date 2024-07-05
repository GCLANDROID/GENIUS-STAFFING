package io.cordova.myapp00d753.fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.ApproverAdapter;
import io.cordova.myapp00d753.module.ApprovalModel;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

/**
 * A simple {@link Fragment} subclass.
 */
public class ApproverFragment extends Fragment {
    private static final String TAG = "ApproverFragment";
    LinearLayout llLoader, llMain, llNoData;
    RecyclerView rvItem;
    View view;
    Pref pref;
    ArrayList<ApprovalModel> itemList = new ArrayList<>();
    ApproverAdapter lAdaapter;
    ArrayList<String> mIdList = new ArrayList<>();
    Button btnReject, btnApprove;
    LinearLayout llShow;
    String mId;
    AlertDialog alerDialog1;
    AlertDialog.Builder builder;
    Button btnDelete;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_approver, container, false);
        initView();
        onClick();
        return view;
    }

    private void initView() {
        pref = new Pref(getContext());
        llLoader = (LinearLayout) view.findViewById(R.id.llLoader);
        llMain = (LinearLayout) view.findViewById(R.id.llMain);
        llNoData = (LinearLayout) view.findViewById(R.id.llNoData);
        rvItem = (RecyclerView) view.findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);

        llShow = (LinearLayout) view.findViewById(R.id.llShow);
        btnReject = (Button) view.findViewById(R.id.btnReject);
        btnApprove = (Button) view.findViewById(R.id.btnApprove);
        btnDelete = (Button) view.findViewById(R.id.btnDelete);
        builder = new AlertDialog.Builder(getContext());
        getItem();

        /*JSONObject obj=new JSONObject();
        try {
            obj.put("CompanyID" , pref.getEmpClintId() );
            obj.put("ApproverID" , pref.getEmpId() );
            obj.put("SecurityCode" , pref.getSecurityCode());
            getItem(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        btnReject.setText("Reject");
        btnApprove.setText("Approve");
        btnDelete.setText("Delete");

    }

    private void getItem(JSONObject jsonObject) {
        Log.e(TAG, "getItem: INPUT: "+jsonObject);
        AndroidNetworking.post(AppData.GET_APPROVER_LEAVE_APPLICATION)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "APPROVER_LEAVE_APPLICATION: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(Response_Data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String ApplicationMID = obj.optString("ApplicationMID");
                                    String Name = obj.optString("Name");
                                    String LeaveName = obj.optString("LeaveName");
                                    String LeaveSDate = obj.optString("LeaveSDate");
                                    String LeaveEDate = obj.optString("LeaveEDate");
                                    String LeaveValue = obj.optString("LeaveValue");
                                    String Reason = obj.optString("Reason");
                                    String ApprovalStatus = obj.optString("ApprovalStatus");
                                    ApprovalModel aModel = new ApprovalModel(ApplicationMID, Name, LeaveName, LeaveSDate, LeaveEDate, LeaveValue, Reason, ApprovalStatus);
                                    itemList.add(aModel);
                                }

                                lAdaapter = new ApproverAdapter(itemList, ApproverFragment.this, getContext());
                                rvItem.setAdapter(lAdaapter);
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNoData.setVisibility(View.GONE);
                            } else {
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNoData.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Something went to wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "APPROVER_LEAVE_APPLICATION_error: "+anError.getErrorBody());
                        llLoader.setVisibility(View.VISIBLE);
                        llMain.setVisibility(View.GONE);
                        llNoData.setVisibility(View.GONE);
                    }
                });
    }

    private void getItem() {
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        llShow.setVisibility(View.GONE);

        String surl = AppData.url + "Leave/ApproverLeaveApp?CompanyID=" + pref.getEmpClintId() + "&ApproverID=" + pref.getEmpId() + "&SecurityCode=" + pref.getSecurityCode();
        Log.d("input", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        itemList.clear();

                        // attendabceInfiList.clear();


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String ApplicationMID = obj.optString("ApplicationMID");
                                    String Name = obj.optString("Name");
                                    String LeaveName = obj.optString("LeaveName");
                                    String LeaveSDate = obj.optString("LeaveSDate");
                                    String LeaveEDate = obj.optString("LeaveEDate");
                                    String LeaveValue = obj.optString("LeaveValue");
                                    String Reason = obj.optString("Reason");
                                    String ApprovalStatus = obj.optString("ApprovalStatus");
                                    ApprovalModel aModel = new ApprovalModel(ApplicationMID, Name, LeaveName, LeaveSDate, LeaveEDate, LeaveValue, Reason, ApprovalStatus);
                                    itemList.add(aModel);
                                }


                                lAdaapter = new ApproverAdapter(itemList, ApproverFragment.this, getContext());
                                rvItem.setAdapter(lAdaapter);
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNoData.setVisibility(View.GONE);


                            } else {
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNoData.setVisibility(View.VISIBLE);

                                //Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                llNoData.setVisibility(View.GONE);


                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    public void updateAttendanceStatus(int position, boolean status) {
        itemList.get(position).setSelected(status);
        if (itemList.get(position).isSelected() == true) {
            mIdList.add(itemList.get(position).getmId());


        } else {
            mIdList.remove(position);
        }

        mId = mIdList.toString().replace("[", "").replace("]", "").replaceAll("\\s+", "%20");
        if (mIdList.size() > 0) {
            llShow.setVisibility(View.VISIBLE);
        } else {
            llShow.setVisibility(View.GONE);
        }
        lAdaapter.notifyDataSetChanged();
    }

    private void approveFunction(JSONObject jsonObject) {
        Log.e(TAG, "approveFunction: INPUT: "+jsonObject );
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(AppData.LEAVE_APPLICATION_APPROVAL)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "LEAVE_APPROVE: "+response.toString(4));
                            pd.dismiss();
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                approveAlert();
                            } else {
                                Toast.makeText(getContext(), Response_Message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "something went to wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "LEAVE_APPROVE: "+anError.getErrorBody());
                        pd.dismiss();
                    }
                });
    }


    private void approveFunction() {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();


        AndroidNetworking.upload(AppData.url + "Leave/ApprovedApplication")
                .addMultipartParameter("CompanyID", pref.getEmpClintId())
                .addMultipartParameter("StrAppMID", mId)
                .addMultipartParameter("ApproverID", pref.getEmpId())
                .addMultipartParameter("ApprovalStatus", "1")
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())

                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        pd.show();

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        pd.dismiss();
                        JSONObject job = response;
                        boolean responseStatus = job.optBoolean("responseStatus");
                        String responseText=job.optString("responseText");
                        if (responseStatus) {
                            approveAlert();
                        } else {
                            Toast.makeText(getContext(), responseText, Toast.LENGTH_LONG).show();
                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        pd.dismiss();
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();

                    }
                });

    }

    private void rejectFunction(JSONObject jsonObject) {
        AndroidNetworking.post(AppData.LEAVE_APPLICATION_APPROVAL)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "LEAVE_REJECT: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                rejectAlert();
                            } else {
                                Toast.makeText(getContext(), Response_Message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Something went to wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "LEAVE_REJECT_error: "+anError.getErrorBody());
                    }
                });
    }

    private void rejectFunction() {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();

        AndroidNetworking.upload(AppData.url+ "Leave/ApprovedApplication")
                .addMultipartParameter("CompanyID", pref.getEmpClintId())
                .addMultipartParameter("StrAppMID", mId)
                .addMultipartParameter("ApproverID", pref.getEmpId())
                .addMultipartParameter("ApprovalStatus", "0")
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())

                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        pd.show();

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        pd.dismiss();
                        JSONObject job = response;
                        boolean responseStatus = job.optBoolean("responseStatus");
                        String responseText=job.optString("responseText");
                        if (responseStatus) {
                            rejectAlert();
                        } else {
                            Toast.makeText(getContext(), responseText, Toast.LENGTH_LONG).show();
                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        pd.dismiss();
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();

                    }
                });

    }
    private void deleteFunction(JSONObject jsonObject) {
        Log.e(TAG, "deleteFunction: INPUT: "+jsonObject);
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(AppData.LEAVE_DELETE_BY_APPROVER)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            pd.dismiss();
                            Log.e(TAG, "DELETE_FUNCTION: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                deleteAlert();
                            }else {
                                Toast.makeText(getContext(), Response_Message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Something went to wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        pd.dismiss();
                        Log.e(TAG, "DELETE_FUNCTION_error: "+anError.getErrorBody());
                    }
                });
    }
    

    private void deleteFunction() {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();

        AndroidNetworking.upload(AppData.url+"Leave/LeaveDeletebyApprover")
                .addMultipartParameter("CompanyID", pref.getEmpClintId())
                .addMultipartParameter("ApplicationMID", mId)
                .addMultipartParameter("ApproverID", pref.getEmpId())
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())

                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        pd.show();

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        pd.dismiss();
                        JSONObject job = response;
                        boolean responseStatus = job.optBoolean("responseStatus");
                        String responseText=job.optString("responseText");
                        if (responseStatus) {
                            deleteAlert();
                        } else {
                            Toast.makeText(getContext(), responseText, Toast.LENGTH_LONG).show();
                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        pd.dismiss();
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();

                    }
                });

    }


    private void approveAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);

        tvInvalidDate.setText("Leave approved successfully");


        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                getItem();

               /* JSONObject obj = new JSONObject();
                try {
                    obj.put("CompanyID", pref.getEmpClintId());
                    obj.put("ApproverID", pref.getEmpId());
                    obj.put("SecurityCode", pref.getSecurityCode());
                    getItem(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }

    private void deleteAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);

        tvInvalidDate.setText("Leave deleted successfully");



        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                getItem();
               /* JSONObject obj=new JSONObject();
                try {
                    obj.put("CompanyID" , pref.getEmpClintId() );
                    obj.put("ApproverID" , pref.getEmpId() );
                    obj.put("SecurityCode" , pref.getSecurityCode());
                    getItem(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }


    private void rejectAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);
        tvInvalidDate.setText("Leave rejected successfully");



        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                getItem();
                /*JSONObject obj=new JSONObject();
                try {
                    obj.put("CompanyID" , pref.getEmpClintId() );
                    obj.put("ApproverID" , pref.getEmpId() );
                    obj.put("SecurityCode" , pref.getSecurityCode());
                    getItem(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }

    private void onClick() {
        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approveFunction();

               /* JSONObject obj=new JSONObject();
                try {
                    obj.put("CompanyID" , pref.getEmpClintId());
                    obj.put("StrAppMID", mId);
                    obj.put("ApproverID", pref.getEmpId());
                    obj.put("ApprovalStatus", "1");
                    obj.put("SecurityCode" , pref.getSecurityCode());
                    approveFunction(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Setting message manually and performing action on button click
                builder.setMessage("Do you want to reject ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                rejectFunction();
                               /* JSONObject obj=new JSONObject();
                                try {
                                    obj.put("CompanyID" , pref.getEmpClintId());
                                    obj.put("StrAppMID", mId);
                                    obj.put("ApproverID", pref.getEmpId());
                                    obj.put("ApprovalStatus", "0");
                                    obj.put("SecurityCode" , pref.getSecurityCode());
                                    rejectFunction(obj);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }*/
                                dialog.cancel();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();

                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Rejection Alert");
                alert.show();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Setting message manually and performing action on button click
                builder.setMessage("Do you want to delete ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteFunction();
                                dialog.cancel();
                               /* JSONObject obj=new JSONObject();
                                try {
                                    obj.put("CompanyID" , pref.getEmpClintId());
                                    obj.put("ApplicationMID", mId);
                                    obj.put("ApproverID", pref.getEmpId());
                                    obj.put("SecurityCode" , pref.getSecurityCode());
                                    deleteFunction(obj);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }*/

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();

                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Delete Alert");
                alert.show();
            }
        });
    }
}
