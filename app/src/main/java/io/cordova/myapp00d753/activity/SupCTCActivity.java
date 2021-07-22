package io.cordova.myapp00d753.activity;

import android.content.Intent;
import android.net.Uri;
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
import io.cordova.myapp00d753.adapter.CTCAdapter;
import io.cordova.myapp00d753.module.CTCModule;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.RecyclerItemClickListener;

public class SupCTCActivity extends AppCompatActivity implements RecyclerItemClickListener.OnItemClickListener {
    RecyclerView rvCTC;
    ArrayList<CTCModule>ctcList=new ArrayList<>();
    CTCAdapter ctcAdapter;
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
    LinearLayout llAgain;
    ImageView imgAgain;
    LinearLayout llNodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sup_ctc);
        mPageCount=1;
        initialize();
        if (connectionCheck.isNetworkAvailable()) {
            getCTCList();
        }else {
            connectionCheck.getNetworkActiveAlert().show();
        }
        onClick();
    }

    private void initialize(){
        pref=new Pref(this);
        connectionCheck=new NetworkConnectionCheck(SupCTCActivity.this);

        rvCTC=(RecyclerView)findViewById(R.id.rvCTC);
        layoutManager
                = new LinearLayoutManager(SupCTCActivity.this, LinearLayoutManager.VERTICAL, false);
        rvCTC.setLayoutManager(layoutManager);
        rvCTC.addOnItemTouchListener(new RecyclerItemClickListener(SupCTCActivity.this, SupCTCActivity.this));

        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        llLoder=(LinearLayout)findViewById(R.id.llWLLoader) ;
        llMain=(LinearLayout)findViewById(R.id.llMain);
        progressBar=(ProgressBar)findViewById(R.id.WLpagination_loader);
        rvCTC.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                               getCTCList();
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
                getCTCList();
            }
        });
        llNodata=(LinearLayout)findViewById(R.id.llNodata);
    }

    private void getCTCList(){
        llLoder.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llAgain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);

        String surl = AppData.url+"gcl_CTC?AEMConsultantID="+pref.getEmpConId()+"&AEMClientID="+pref.getEmpClintId()+"&AEMEmployeeID="+pref.getEmpId()+"&WorkingStatus=1&CurrentPage="+mPageCount+"&SecurityCode="+pref.getSecurityCode();
        Log.d("input",surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        loading=false;
                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText=job1.optString("responseText");

                            boolean responseStatus=job1.optBoolean("responseStatus");
                            if (responseStatus){
                    //             Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData=job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++){
                                    JSONObject obj=responseData.getJSONObject(i);
                                    String MonthlyNet=obj.optString("MonthlyNet");
                                    String AEMEmployeeID=obj.optString("AEMEmployeeID");
                                    String  Name=obj.optString("Name");
                                    String url=obj.optString("url");

                                    CTCModule obj2 = new CTCModule(AEMEmployeeID,Name,MonthlyNet,url);
                                    ctcList.add(obj2);


                                }
                                ctcAdapter.notifyDataSetChanged();
                                llLoder.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llAgain.setVisibility(View.GONE);
                                llNodata.setVisibility(View.GONE);


                            }

                            else {
                                ctcAdapter.notifyDataSetChanged();
                                llLoder.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llAgain.setVisibility(View.GONE);
                                llNodata.setVisibility(View.VISIBLE);

                                Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();

                            Toast.makeText(SupCTCActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoder.setVisibility(View.GONE);
                llMain.setVisibility(View.GONE);
                llAgain.setVisibility(View.VISIBLE);
                llNodata.setVisibility(View.GONE);

              //  Toast.makeText(SupCTCActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert",error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");

    }

    private void setAdapter(){
        ctcAdapter=new CTCAdapter(ctcList);
        rvCTC.setAdapter(ctcAdapter);
    }
    private void onClick(){
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SupCTCActivity.this,SuperVisiorDashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //finish();
            }
        });


    }

    @Override
    public void onItemClick(View childView, int position) {
        String ctcUrl=ctcList.get(position).getCtcUrl();
        openBrowser(ctcUrl);

    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }

    private void openBrowser(String url){
        Uri uri = Uri.parse(url); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
