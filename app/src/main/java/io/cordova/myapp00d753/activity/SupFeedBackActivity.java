package io.cordova.myapp00d753.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import io.cordova.myapp00d753.adapter.SupFeedBackAdapter;
import io.cordova.myapp00d753.module.SupFeedBackModule;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.RecyclerItemClickListener;

public class SupFeedBackActivity extends AppCompatActivity  {
    RecyclerView rvFeedBack;
    ArrayList<SupFeedBackModule>feedbackList=new ArrayList<>();
    SupFeedBackAdapter supAdapter;
    ProgressBar progressBar;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    public static int mPageCount = 0;
    boolean mIsEndReached = false;
    private boolean loading = false;
    LinearLayoutManager layoutManager;
    LinearLayout llLoder,llMain;
    ImageView imgBack,imgHome;
    NetworkConnectionCheck connectionCheck;
    Pref pref;
    AlertDialog alertDialog,alerDialog1;
    EditText etFeedBack;
     LinearLayout llReply;
     Button btnReply;
    String feedId;
    ArrayList<String>item=new ArrayList<>();
    LinearLayout llNodata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sup_feed_back);
        mPageCount=1;
        initialize();
        if (connectionCheck.isNetworkAvailable()) {
            getFeedbackList();

        }else {
            connectionCheck.getNetworkActiveAlert().show();
        }

        onClick();
    }

    private void initialize(){
        pref=new Pref(SupFeedBackActivity.this);
        connectionCheck=new NetworkConnectionCheck(SupFeedBackActivity.this);
        rvFeedBack = (RecyclerView) findViewById(R.id.rvFeedBack);

        layoutManager
                = new LinearLayoutManager(SupFeedBackActivity.this, LinearLayoutManager.VERTICAL, false);
        rvFeedBack.setLayoutManager(layoutManager);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        llLoder = (LinearLayout) findViewById(R.id.llWLLoader);
        llMain = (LinearLayout) findViewById(R.id.llMain);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        llNodata=(LinearLayout)findViewById(R.id.llNodata);

        progressBar = (ProgressBar) findViewById(R.id.WLpagination_loader);
        rvFeedBack.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                                mPageCount = mPageCount + 1;
                                if (connectionCheck.isNetworkAvailable()) {
                                    getFeedbackList();
                                }else {
                                    connectionCheck.getNetworkActiveAlert().show();
                                }


                            }

                        }
                    }
                }
            }
        });
        setAdapter();
        btnReply=(Button)findViewById(R.id.btnReply);
        llReply=(LinearLayout)findViewById(R.id.llReply);

    }

    private void setAdapter(){
        supAdapter=new SupFeedBackAdapter(feedbackList,SupFeedBackActivity.this);
        rvFeedBack.setAdapter(supAdapter);

    }
    private void getFeedbackList(){
        llLoder.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);

        String surl = "http://111.93.182.174/GeniusiOSApi/api/gcl_Feedback?FeedBackID=0&AEMClientID="+pref.getEmpClintId()+"&AEMEmployeeID="+pref.getEmpId()+"&Query=null&RepliedDetails=null&RepliedBy=null&ReplyStatus=0&IssueID=0&WorkingStatus=1&CurrentPage="+mPageCount+"&Operation=1&SecurityCode="+pref.getSecurityCode();
        Log.d("input", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        loading = false;
                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String AEMEmployeeID=obj.optString("AEMEmployeeID");
                                    String Name=obj.optString("Name");
                                    String SubmitedOn=obj.optString("SubmitedOn");
                                    String IssueName=obj.optString("IssueName");
                                    String Query=obj.optString("Query");
                                    int FeedBackID=obj.optInt("FeedBackID");
                                    String feedbackid= String.valueOf(FeedBackID);
                                    SupFeedBackModule sModule=new SupFeedBackModule(AEMEmployeeID,Name,SubmitedOn,IssueName,Query,feedbackid);
                                    feedbackList.add(sModule);



                                }
                                supAdapter.notifyDataSetChanged();
                                llLoder.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.GONE);


                            } else {
                                supAdapter.notifyDataSetChanged();
                                llLoder.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.VISIBLE);

                                Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SupFeedBackActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(SupFeedBackActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
    }




    private void dialogFeedback(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SupFeedBackActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_supfeedback, null);
        dialogBuilder.setView(dialogView);
        ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        etFeedBack = (EditText) dialogView.findViewById(R.id.etFeedBAck);
        Button btnSubmit = (Button) dialogView.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                postReply();



            }
        });


        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(true);
        Window window = alertDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog.show();
    }

    private void postReply(){
        String surl ="http://111.93.182.174/GeniusiOSApi/api/gcl_Feedback?AEMClientID="+pref.getEmpClintId()+"&FeedBackID=0&AEMEmployeeID="+pref.getEmpId()+"&Query=null&RepliedDetails="+etFeedBack.getText().toString().replaceAll("\\s+","")+"&RepliedBy="+pref.getEmpId()+"&ReplyStatus=1&IssueID=0&WorkingStatus=1&CurrentPage=1&Operation=4&SecurityCode="+pref.getSecurityCode()+"&FeedBackIDs="+feedId;
        Log.d("postreply",surl);
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Submating...");
        progressBar.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLeave", response);
                        progressBar.dismiss();
                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText=job1.optString("responseText");
                            boolean responseStatus=job1.optBoolean("responseStatus");
                            if (responseStatus){
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                successAlert();


                            }


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SupFeedBackActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                Toast.makeText(SupFeedBackActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert",error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");

    }

    private void successAlert(){
        android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(SupFeedBackActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate=(TextView)dialogView.findViewById(R.id.tvSuccess);
          tvInvalidDate.setText("Reply sent");

        Button btnOk=(Button)dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPageCount=1;
                feedbackList.clear();
                getFeedbackList();
                alerDialog1.dismiss();

            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }

    private void onClick(){
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SupFeedBackActivity.this,DashBoardActivity.class);
                startActivity(intent);
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogFeedback();
            }
        });
    }

    public void updateAttendanceStatus(int position, boolean status) {
        feedbackList.get(position).setSelected(status);
        item.add(feedbackList.get(position).getFeedBackid());
        Log.d("arpan", item.toString());
        String i = item.toString();
        String d = i.replace("[", "").replace("]", "");
        feedId = d.replaceAll("\\s+", "");
        Log.d("commas", feedId);
        if (item.size()>0){
            llReply.setVisibility(View.VISIBLE);
        }else {
            llReply.setVisibility(View.GONE);
        }


        supAdapter.notifyDataSetChanged();
    }
}
