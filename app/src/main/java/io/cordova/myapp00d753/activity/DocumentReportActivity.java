package io.cordova.myapp00d753.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.DocumentAdapter;
import io.cordova.myapp00d753.module.DocumentManageModule;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.RecyclerItemClickListener;

public class DocumentReportActivity extends AppCompatActivity  {
    private static final String TAG = "DocumentReportActivity";
    RecyclerView rvDocument;
    ArrayList<DocumentManageModule> documentList = new ArrayList<>();
    DocumentAdapter documentAdapter;
    LinearLayout llLoader, llMain;
    ImageView imgBack, imgHome;
    Pref pref;
    String dLink;
    NetworkConnectionCheck connectionCheck;
    LinearLayout llNoadata;
    LinearLayout llAgain;
    ImageView imgAgain;
    String aempEmployeeid;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_report);
        initialize();
        if (connectionCheck.isNetworkAvailable()) {

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
        llLoader = (LinearLayout) findViewById(R.id.llLoader);
        llMain = (LinearLayout) findViewById(R.id.llMain);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        llNoadata = (LinearLayout) findViewById(R.id.llNodata);
        llAgain=(LinearLayout)findViewById(R.id.llAgain);
        imgAgain=(ImageView)findViewById(R.id.imgAgain);
        if (pref.getUserType().equals("1")){
            aempEmployeeid=pref.getEmpId();
        }else if (pref.getUserType().equals("4")){
            aempEmployeeid=pref.getMasterId();
            Log.d("aempEmployeeid",aempEmployeeid);
        }
        status=getIntent().getStringExtra("status");
        if (status.equals("Approval Pending")){
            getDocListForPending();
            JSONObject obj=new JSONObject();
            try {
                obj.put("AEMEmployeeID", pref.getEmpId());
                obj.put("FileName",JSONObject.NULL);
                obj.put("FileType","0");
                obj.put("DocumentID","0");
                obj.put("ReferenceNo","0");
                obj.put("DbOperation","1");
                obj.put("SecurityCode",pref.getSecurityCode());
                getDocListForPending(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (status.equals("Approved")){
            getDocListForApproval();
        }else {
            getDocList();
        }

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
                    Intent intent = new Intent(DocumentReportActivity.this, EmployeeDashBoardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else if (pref.getUserType().equals("4")){
                    Intent intent = new Intent(DocumentReportActivity.this, TempDashBoardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
               // finish();
            }
        });

        imgAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDocList();
            }
        });
    }

    private void getDocListForPending(JSONObject jsonObject) {
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoadata.setVisibility(View.GONE);
        llAgain.setVisibility(View.GONE);
        AndroidNetworking.post(AppData.EMPLOYEE_DOCUMENT_MANAGE)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "PENDING_DOC_LIST: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(Response_Data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String DocumentName = obj.optString("DocumentName");
                                    String DocumentType = obj.optString("DocumentType");
                                    String AEMStatusName = obj.optString("AEMStatusName");
                                    String CreatedOn = obj.optString("CreatedOn");
                                    String ApprovalRemarks = obj.optString("ApprovalRemarks");
                                    String DocLink = obj.optString("DocLink");
                                    if (AEMStatusName.equals("Pending")) {
                                        DocumentManageModule dmodule = new DocumentManageModule(DocumentName, DocumentType, ApprovalRemarks, CreatedOn, AEMStatusName, DocLink);
                                        documentList.add(dmodule);
                                    }
                                }

                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNoadata.setVisibility(View.GONE);
                                llAgain.setVisibility(View.GONE);
                                documentAdapter = new DocumentAdapter(documentList,DocumentReportActivity.this);
                                rvDocument.setAdapter(documentAdapter);
                            } else {
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNoadata.setVisibility(View.VISIBLE);
                                llAgain.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        llLoader.setVisibility(View.GONE);
                        llMain.setVisibility(View.GONE);
                        llNoadata.setVisibility(View.GONE);
                        llAgain.setVisibility(View.VISIBLE);
                        Log.e(TAG, "PENDING_DOC_LIST_error: "+anError.getErrorBody());
                    }
                });
    }

    private void getDocListForPending() {
        String surl = AppData.url+"gcl_DigitalDocument?AEMEmployeeID=" + aempEmployeeid + "&FileName=null&FileType=0&DocumentID=0&ReferenceNo=0&DbOperation=1&SecurityCode=" + pref.getSecurityCode();
        Log.d("manageinput",surl);
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoadata.setVisibility(View.GONE);
        llAgain.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("responsedocumentreport", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                            //    Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();
                                 JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String DocumentName = obj.optString("DocumentName");
                                    String DocumentType = obj.optString("DocumentType");
                                    String AEMStatusName = obj.optString("AEMStatusName");
                                    String CreatedOn = obj.optString("CreatedOn");
                                    String ApprovalRemarks = obj.optString("ApprovalRemarks");
                                    String DocLink = obj.optString("DocLink");

                                    if (AEMStatusName.equals("Pending")) {
                                        DocumentManageModule dmodule = new DocumentManageModule(DocumentName, DocumentType, ApprovalRemarks, CreatedOn, AEMStatusName, DocLink);
                                        documentList.add(dmodule);
                                    }


                                }


                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNoadata.setVisibility(View.GONE);
                                llAgain.setVisibility(View.GONE);
                                documentAdapter = new DocumentAdapter(documentList,DocumentReportActivity.this);
                                rvDocument.setAdapter(documentAdapter);


                            } else {
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNoadata.setVisibility(View.VISIBLE);
                                llAgain.setVisibility(View.GONE);

                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();

                           // Toast.makeText(DocumentReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.GONE);
                llMain.setVisibility(View.GONE);
                llNoadata.setVisibility(View.GONE);
                llAgain.setVisibility(View.VISIBLE);

              //  Toast.makeText(DocumentReportActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");


    }
    private void getDocListForApproval() {
        String surl = AppData.url+"gcl_DigitalDocument?AEMEmployeeID=" + aempEmployeeid + "&FileName=null&FileType=0&DocumentID=0&ReferenceNo=0&DbOperation=1&SecurityCode=" + pref.getSecurityCode();
        Log.d("manageinput",surl);
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoadata.setVisibility(View.GONE);
        llAgain.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("responsedocumentreport", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                //    Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String DocumentName = obj.optString("DocumentName");
                                    String DocumentType = obj.optString("DocumentType");
                                    String AEMStatusName = obj.optString("AEMStatusName");
                                    String CreatedOn = obj.optString("CreatedOn");
                                    String ApprovalRemarks = obj.optString("ApprovalRemarks");
                                    String DocLink = obj.optString("DocLink");
                                    if (AEMStatusName.equals("Approved")) {
                                        DocumentManageModule dmodule = new DocumentManageModule(DocumentName, DocumentType, ApprovalRemarks, CreatedOn, AEMStatusName, DocLink);
                                        documentList.add(dmodule);
                                    }


                                }


                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNoadata.setVisibility(View.GONE);
                                llAgain.setVisibility(View.GONE);
                                documentAdapter = new DocumentAdapter(documentList,DocumentReportActivity.this);
                                rvDocument.setAdapter(documentAdapter);


                            } else {
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNoadata.setVisibility(View.VISIBLE);
                                llAgain.setVisibility(View.GONE);

                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();

                            // Toast.makeText(DocumentReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.GONE);
                llMain.setVisibility(View.GONE);
                llNoadata.setVisibility(View.GONE);
                llAgain.setVisibility(View.VISIBLE);

                //  Toast.makeText(DocumentReportActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");


    }
    private void getDocList() {
        String surl = AppData.url+"gcl_DigitalDocument?AEMEmployeeID=" + aempEmployeeid + "&FileName=null&FileType=0&DocumentID=0&ReferenceNo=0&DbOperation=1&SecurityCode=" + pref.getSecurityCode();
        Log.d("manageinput",surl);
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoadata.setVisibility(View.GONE);
        llAgain.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("responsedocumentreport", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                //    Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();
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
                                llAgain.setVisibility(View.GONE);
                                documentAdapter = new DocumentAdapter(documentList,DocumentReportActivity.this);
                                rvDocument.setAdapter(documentAdapter);


                            } else {
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNoadata.setVisibility(View.VISIBLE);
                                llAgain.setVisibility(View.GONE);

                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();

                            // Toast.makeText(DocumentReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.GONE);
                llMain.setVisibility(View.GONE);
                llNoadata.setVisibility(View.GONE);
                llAgain.setVisibility(View.VISIBLE);

                //  Toast.makeText(DocumentReportActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");


    }


}
