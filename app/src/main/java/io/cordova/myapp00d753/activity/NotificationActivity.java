package io.cordova.myapp00d753.activity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
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


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.NotificationAdapter;
import io.cordova.myapp00d753.module.NotificationModule;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;
import io.cordova.myapp00d753.utility.Pref;

public class NotificationActivity extends AppCompatActivity {
    RecyclerView rvNotification;
    ArrayList<NotificationModule> nottificationList = new ArrayList<>();
    ImageView imgBack, imgHome;
    ProgressBar progressBar;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    public static int mPageCount = 0;
    boolean mIsEndReached = false;
    private boolean loading = false;
    LinearLayoutManager layoutManager;
    LinearLayout llLoder;
    NotificationAdapter notificationAdapter;
    LinearLayout llMain;
    Pref pref;
    NetworkConnectionCheck connectionCheck;
    LinearLayout llNodata;
    LinearLayout llAgain;
    ImageView imgAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        mPageCount = 1;
        initialize();
        if (connectionCheck.isNetworkAvailable()) {
            getNotificationList();
        } else {
            connectionCheck.getNetworkActiveAlert().show();
        }


        onClick();
    }

    private void initialize() {
        pref = new Pref(NotificationActivity.this);
        connectionCheck = new NetworkConnectionCheck(NotificationActivity.this);
        rvNotification = (RecyclerView) findViewById(R.id.rvNotification);
        layoutManager
                = new LinearLayoutManager(NotificationActivity.this, LinearLayoutManager.VERTICAL, false);
        rvNotification.setLayoutManager(layoutManager);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        llLoder = (LinearLayout) findViewById(R.id.llWLLoader);
        llMain = (LinearLayout) findViewById(R.id.llMain);
        llNodata=(LinearLayout)findViewById(R.id.llNodata);
        progressBar = (ProgressBar) findViewById(R.id.WLpagination_loader);
        rvNotification.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                                getNotificationList();
                            }

                        }
                    }
                }
            }
        });
        setAdapter();
        llAgain=(LinearLayout)findViewById(R.id.llAgain);
        imgAgain=(ImageView)findViewById(R.id.imgAgain);
        imgAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNotificationList();
            }
        });
    }

    private void getNotificationList() {
        Log.d("Arpan", "arpan");
        llLoder.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);
        llAgain.setVisibility(View.GONE);

        String surl = AppData.url+"gcl_Notification?MsgMasterId=0&AEMClientID=" + pref.getEmpClintId() + "&AEMEmployeeID=" + pref.getEmpId() + "&StartDate=null&EndDate=null&Tagline=null&Description=null&CurrentPage=" + mPageCount + "&ApprovalStatus=0&Operation=1&SecurityCode=" + pref.getSecurityCode();
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
                              //  Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String StartDate = obj.optString("StartDate");
                                    String Tagline = obj.optString("Tagline");
                                    String Description = obj.optString("Description");

                                    NotificationModule nmodule = new NotificationModule(Tagline, Description, StartDate);
                                    nottificationList.add(nmodule);


                                }
                                notificationAdapter.notifyDataSetChanged();
                                llLoder.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.GONE);
                                llAgain.setVisibility(View.GONE);


                            } else {
                                notificationAdapter.notifyDataSetChanged();
                                llLoder.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.VISIBLE);
                                llAgain.setVisibility(View.GONE);

                                Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            notificationAdapter.notifyDataSetChanged();

                          //  Toast.makeText(NotificationActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoder.setVisibility(View.GONE);
                llMain.setVisibility(View.GONE);
                llNodata.setVisibility(View.GONE);
                llAgain.setVisibility(View.VISIBLE);
                //Toast.makeText(NotificationActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");

    }

    private void setAdapter() {
        notificationAdapter = new NotificationAdapter(nottificationList);
        rvNotification.setAdapter(notificationAdapter);
    }

    private void onClick() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pref.getUserType().equals("1")) {
                    Intent intent = new Intent(NotificationActivity.this, EmployeeDashBoardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    //finish();
                }else if (pref.getUserType().equals("2")){
                    Intent intent = new Intent(NotificationActivity.this, SuperVisiorDashBoardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    //finish();
                }
            }
        });
    }
}
