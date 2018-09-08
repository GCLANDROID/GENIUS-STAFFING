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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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
import io.cordova.myapp00d753.adapter.FeedBackAdapter;
import io.cordova.myapp00d753.module.FeedBackModule;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;
import io.cordova.myapp00d753.utility.Pref;

public class FeedBackActivity extends AppCompatActivity {
    RecyclerView rvFeedBack;
    ArrayList<FeedBackModule> feedbackList = new ArrayList<>();
    FeedBackAdapter feedBackAdapter;
    ImageView imgBack, imgHome;
    LinearLayout llAdd;
    AlertDialog alertDialog, alerDialog1;
    String[] spIssueList = {"Choose Issue", "Store Issue"};
    EditText etFeedBack;
    String[] feedback = {"All", "Replied", "Pending"};
    int flag;
    Spinner spFeedback;
    ProgressBar progressBar;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    public static int mPageCount = 0;
    boolean mIsEndReached = false;
    private boolean loading = false;
    LinearLayoutManager layoutManager;
    LinearLayout llLoader, llMain;
    Pref pref;
    NetworkConnectionCheck connectionCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        flag = 4;
        mPageCount = 1;
        initialize();
        setSpinner();
        if (connectionCheck.isNetworkAvailable()) {
            getFeedBackList();
        }else {
            connectionCheck.getNetworkActiveAlert().show();
        }
        onClick();
    }

    private void initialize() {
        pref=new Pref(getApplicationContext());
        connectionCheck=new NetworkConnectionCheck(FeedBackActivity.this);
        rvFeedBack = (RecyclerView) findViewById(R.id.rvFeedBack);
        layoutManager
                = new LinearLayoutManager(FeedBackActivity.this, LinearLayoutManager.VERTICAL, false);
        rvFeedBack.setLayoutManager(layoutManager);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        llAdd = (LinearLayout) findViewById(R.id.llAdd);
        spFeedback = (Spinner) findViewById(R.id.spFeedback);
        llLoader = (LinearLayout) findViewById(R.id.llLoader);
        llMain = (LinearLayout) findViewById(R.id.llMain);
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
                                getFeedBackList();
                            }

                        }
                    }
                }
            }
        });
        setAdapter();
    }

    private void getFeedBackList() {
        Log.d("Arpan", "arpan");
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);

        String surl = "http://111.93.182.174/GeniusiOSApi/api/gcl_Feedback?AEMClientID="+pref.getEmpClintId()+"&FeedBackID=0&AEMEmployeeID="+pref.getEmpId()+"&Query=0&RepliedDetails=0&RepliedBy=0&ReplyStatus=" + flag + "&IssueID=0&WorkingStatus=1&CurrentPage=" + mPageCount + "&Operation=1&SecurityCode="+pref.getSecurityCode();
        Log.d("input", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        feedbackList.clear();

                        Log.d("responseAttendance", response);
                        loading = false;
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
                                    String Query = obj.optString("Query");
                                    String SubmitedOn = obj.optString("SubmitedOn");
                                    String RepliedDetails = obj.optString("RepliedDetails");
                                    FeedBackModule feedBackModule = new FeedBackModule(SubmitedOn, Query, RepliedDetails);
                                    feedbackList.add(feedBackModule);

                                }
                                feedBackAdapter.notifyDataSetChanged();
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);


                            } else {

                                Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(FeedBackActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(FeedBackActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");

    }

    private void setAdapter() {
        feedBackAdapter = new FeedBackAdapter(feedbackList);
        rvFeedBack.setAdapter(feedBackAdapter);
    }

    private void onClick() {
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FeedBackActivity.this, DashBoardActivity.class);
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

        llAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FeedBackActivity.this, R.style.CustomDialogNew);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.dialog_feedback, null);
                dialogBuilder.setView(dialogView);
                Spinner spIssue = (Spinner) dialogView.findViewById(R.id.spIssue);
                SpinnerAdapter spinnerAdapter = new io.cordova.myapp00d753.adapter.SpinnerAdapter(FeedBackActivity.this, spIssueList);
                spIssue.setAdapter(spinnerAdapter);

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

                        postFeedBack();

                        alertDialog.dismiss();

                    }
                });


                alertDialog = dialogBuilder.create();
                alertDialog.setCancelable(true);
                Window window = alertDialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER);
                alertDialog.show();
            }
        });

        spFeedback.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    flag = 4;
                    mPageCount = 1;
                    feedbackList.clear();
                    getFeedBackList();
                } else if (i == 1) {
                    flag = 1;
                    mPageCount = 1;
                    feedbackList.clear();
                    getFeedBackList();

                } else if (i == 2) {
                    flag = 0;
                    mPageCount = 1;
                    feedbackList.clear();
                    getFeedBackList();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void postFeedBack() {
        String surl = "http://111.93.182.174/GeniusiOSApi/api/gcl_Feedback?AEMClientID="+pref.getEmpClintId()+"&FeedBackID=0&AEMEmployeeID="+pref.getEmpId()+"&Query=" + etFeedBack.getText().toString().replaceAll("\\s+", "") + "&RepliedDetails=0&RepliedBy=0&ReplyStatus=4&IssueID=0&WorkingStatus=1&CurrentPage=1&Operation=3&SecurityCode="+pref.getSecurityCode()+"&FeedBackIDs=0";
        Log.d("feedinput",surl);
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
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();
                                successAlert();

                            }else {
                                Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();
                            }


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(FeedBackActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                Toast.makeText(FeedBackActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");


    }

    private void successAlert() {
        android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(FeedBackActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);
        tvInvalidDate.setText("Data saved successfully");
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                flag = 0;
                getFeedBackList();
            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }

    private void setSpinner() {
        io.cordova.myapp00d753.adapter.SpinnerAdapter spinnerAdapter = new io.cordova.myapp00d753.adapter.SpinnerAdapter(FeedBackActivity.this, feedback);
        spFeedback.setAdapter(spinnerAdapter);
    }
}
