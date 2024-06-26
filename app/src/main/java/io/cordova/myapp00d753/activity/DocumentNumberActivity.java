package io.cordova.myapp00d753.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.DocNumberReportAdapter;
import io.cordova.myapp00d753.adapter.DocumentAdapter;
import io.cordova.myapp00d753.module.DocumentManageModule;
import io.cordova.myapp00d753.module.DocumentNumberRawModel;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

public class DocumentNumberActivity extends AppCompatActivity {
    private static final String TAG = "DocumentNumberActivity";
    FloatingActionButton fbButton;
    ImageView imgBack,imgHome;
    RecyclerView rvItem;
    TextView tvTotalDoc;
    Pref pref;
    ProgressDialog pg;
    ArrayList<DocumentManageModule>docNumList=new ArrayList<>();
    ArrayList<DocumentNumberRawModel>docList=new ArrayList<>();
    ArrayList<DocumentNumberRawModel>tempDocList=new ArrayList<>();
    LinearLayout llTotalDoc;
    String from="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_number);
        initView();
        if (from.equals(TempDashBoardActivity.TEMP_DASHBOARD)){
            Log.e(TAG, "for: "+TempDashBoardActivity.TEMP_DASHBOARD);
            getNumberListForTempDashboard();
        } else {
            Log.e(TAG, "for: common use");
            getNumberList();
        }
        onClick();
    }



    private void initView(){
        if (getIntent().getExtras() != null){
            if (getIntent().getExtras().getString("from").equalsIgnoreCase(TempDashBoardActivity.TEMP_DASHBOARD)){
                from = getIntent().getExtras().getString("from");
                Log.e(TAG, "from: "+from);
            }
        } else {
            Log.e(TAG, "from: Null");
        }
        pref=new Pref(getApplicationContext());

        fbButton=(FloatingActionButton)findViewById(R.id.fbButton);

        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);

        tvTotalDoc=(TextView)findViewById(R.id.tvTotalDoc);

        rvItem=(RecyclerView)findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(DocumentNumberActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);
        pg=new ProgressDialog(DocumentNumberActivity.this);
        pg.setMessage("Loading....");
        pg.setCancelable(false);
        llTotalDoc=(LinearLayout)findViewById(R.id.llTotalDoc);
    }

    private void onClick(){
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),EmployeeDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });


        fbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),DocumentManageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        llTotalDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), DocumentReportActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void getNumberListForTempDashboard() {
        String surl = AppData.url+ "gcl_DigitalDocument?AEMEmployeeID=" + pref.getMasterId() + "&FileName=null&FileType=0&DocumentID=0&ReferenceNo=0&DbOperation=1&SecurityCode=" + pref.getSecurityCode();
        Log.d("manageinput",surl);
        pg.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("NumberListForTemp_1", response);

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("NumberListForTemp_2", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            int approved = 0, pending = 0, reject = 0;
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
                                    if (obj.optString("ApprovalRemarks").equalsIgnoreCase("NA")){
                                        pending++;
                                    } else if (obj.optString("ApprovalRemarks").equalsIgnoreCase("approved")){
                                        approved++;
                                    } else if (obj.optString("ApprovalRemarks").equalsIgnoreCase("reject")){
                                        reject++;
                                    }
                                    String DocLink = obj.optString("DocLink");
                                    DocumentManageModule dmodule = new DocumentManageModule(DocumentName, DocumentType, ApprovalRemarks, CreatedOn, AEMStatusName, DocLink);
                                    docNumList.add(dmodule);
                                    String size= String.valueOf(docNumList.size());
                                    tvTotalDoc.setText(size);
                                }
                                if (approved > 0){
                                    tempDocList.add(new DocumentNumberRawModel("Approved",String.valueOf(approved)));
                                }
                                if (pending > 0){
                                    tempDocList.add(new DocumentNumberRawModel("Pending",String.valueOf(pending)));
                                }
                                if (reject > 0){
                                    tempDocList.add(new DocumentNumberRawModel("Reject",String.valueOf(reject)));
                                }
                                pg.dismiss();
                                DocNumberReportAdapter dAdapter=new DocNumberReportAdapter(tempDocList,DocumentNumberActivity.this);
                                rvItem.setAdapter(dAdapter);
                            } else {
                                pg.dismiss();
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
                pg.dismiss();
                //  Toast.makeText(DocumentReportActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
    }

    private void getNumberList() {
        String surl = AppData.url+"gcl_DigitalDocument?AEMEmployeeID=" + pref.getEmpId() + "&FileName=null&FileType=0&DocumentID=0&ReferenceNo=0&DbOperation=1&SecurityCode=" + pref.getSecurityCode();
        Log.d("manageinput",surl);
        pg.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        pg.show();


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
                                    docNumList.add(dmodule);
                                    String size= String.valueOf(docNumList.size());
                                    tvTotalDoc.setText(size);


                                }
                                getDocInfoList();

                            } else {
                                pg.dismiss();

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
                pg.dismiss();
                //  Toast.makeText(DocumentReportActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");


    }

    private void getDocInfoList() {
        String surl = AppData.url+"gcl_EmployeeDigitalDocumentUploadInfo?AEMEmployeeID="+pref.getEmpId()+"&SecurityCode="+pref.getSecurityCode();
        Log.d("manageinput",surl);
        pg.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseDocInfoList", response);
                        pg.dismiss();

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
                                    String StatusName = obj.optString("StatusName");
                                    String TotalDocument = obj.optString("TotalDocument");
                                    DocumentNumberRawModel dmodule = new DocumentNumberRawModel(StatusName,TotalDocument);
                                    docList.add(dmodule);
                                }

                                pg.dismiss();
                                DocNumberReportAdapter dAdapter=new DocNumberReportAdapter(docList,DocumentNumberActivity.this);
                                rvItem.setAdapter(dAdapter);
                            } else {
                                pg.dismiss();
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
                pg.dismiss();
                //  Toast.makeText(DocumentReportActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
    }
}
