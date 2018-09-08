package io.cordova.myapp00d753.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import io.cordova.myapp00d753.adapter.DocumentAdapter;
import io.cordova.myapp00d753.module.DocumentManageModule;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.RecyclerItemClickListener;

public class DocumentReportActivity extends AppCompatActivity implements RecyclerItemClickListener.OnItemClickListener {
    RecyclerView rvDocument;
    ArrayList<DocumentManageModule> documentList = new ArrayList<>();
    DocumentAdapter documentAdapter;
    LinearLayout llLoader, llMain;
    ImageView imgBack, imgHome;
    Pref pref;
    String dLink;
    NetworkConnectionCheck connectionCheck;
    LinearLayout llNoadata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_report);
        initialize();
        if (connectionCheck.isNetworkAvailable()) {
            getDocList();
        } else {
            connectionCheck.getNetworkActiveAlert().show();
        }
        onClick();

    }

    private void initialize() {
        connectionCheck = new NetworkConnectionCheck(DocumentReportActivity.this);
        pref = new Pref(this);
        rvDocument = (RecyclerView) findViewById(R.id.rvDocument);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(DocumentReportActivity.this, LinearLayoutManager.VERTICAL, false);
        rvDocument.setLayoutManager(layoutManager);
        rvDocument.addOnItemTouchListener(new RecyclerItemClickListener(DocumentReportActivity.this, DocumentReportActivity.this));
        llLoader = (LinearLayout) findViewById(R.id.llLoader);
        llMain = (LinearLayout) findViewById(R.id.llMain);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        llNoadata = (LinearLayout) findViewById(R.id.llNodata);

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
                Intent intent = new Intent(DocumentReportActivity.this, DashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getDocList() {
        String surl = "http://111.93.182.174/GeniusiOSApi/api/gcl_DigitalDocument?AEMEmployeeID=" + pref.getEmpId() + "&FileName=null&FileType=0&DocumentID=0&ReferenceNo=0&DbOperation=1&SecurityCode=" + pref.getSecurityCode();
        Log.d("manageinput",surl);
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoadata.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);


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
                                    String DocumentName = obj.optString("DocumentName");
                                    String DocumentType = obj.optString("DocumentType");
                                    String AEMStatusName = obj.optString("AEMStatusName");
                                    String CreatedOn = obj.optString("CreatedOn");
                                    String ApprovalRemarks = obj.optString("ApprovalRemarks");
                                    String DocLink = obj.optString("DocLink");
                                    DocumentManageModule dmodule = new DocumentManageModule(DocumentName, DocumentType, ApprovalRemarks, CreatedOn, AEMStatusName, DocLink);
                                    documentList.add(dmodule);


                                }


                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNoadata.setVisibility(View.GONE);
                                documentAdapter = new DocumentAdapter(documentList);
                                rvDocument.setAdapter(documentAdapter);


                            } else {
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNoadata.setVisibility(View.VISIBLE);

                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            llLoader.setVisibility(View.VISIBLE);
                            llMain.setVisibility(View.GONE);
                            Toast.makeText(DocumentReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);

                Toast.makeText(DocumentReportActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");


    }

    @Override
    public void onItemClick(View childView, int position) {
        dLink = documentList.get(position).getDocLink();
        operBrowser();

    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }

    private void operBrowser() {
        Uri uri = Uri.parse(dLink); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (!dLink.equals("") && dLink != null) {
            startActivity(intent);
        } else {

        }
    }
}
