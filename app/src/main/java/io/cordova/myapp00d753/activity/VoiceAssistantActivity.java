package io.cordova.myapp00d753.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.DocumentAdapter;
import io.cordova.myapp00d753.adapter.SalaryAdapter;
import io.cordova.myapp00d753.module.DocumentManageModule;
import io.cordova.myapp00d753.module.SalaryModule;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

public class VoiceAssistantActivity extends AppCompatActivity {
    ImageView imgMic;
    EditText etText;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    LinearLayout llVoice,llMain,llNodata;
    RecyclerView rvSalary;

    ImageView imgMicSearch;
    TextToSpeech t1;
    String mId,yID;
    String apiYID;
    Pref pref;

    TextView tvSearch;

    String cuyear;
    int y;
    String year;
    ImageView imgBack,imgHome;
    ArrayList<SalaryModule>salaryList=new ArrayList<>();
    ArrayList<DocumentManageModule> documentList = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_assistant);
        initView();
        onCLick();
    }

    private void initView(){
        pref=new Pref(VoiceAssistantActivity.this);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        tvSearch=(TextView)findViewById(R.id.tvSearch);
        llMain=(LinearLayout)findViewById(R.id.llMain);
        llVoice=(LinearLayout)findViewById(R.id.llVoice);
        llNodata=(LinearLayout)findViewById(R.id.llNodata);
        rvSalary = (RecyclerView) findViewById(R.id.rvSalary);
       /* LinearLayoutManager layoutManager
                = new LinearLayoutManager(VoiceAssistantActivity.this, LinearLayoutManager.VERTICAL, false);
        rvSalary.setLayoutManager(layoutManager);*/
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });

        imgMic=(ImageView)findViewById(R.id.imgMic);
        imgMicSearch=(ImageView)findViewById(R.id.imgMicSearch);
        etText=(EditText)findViewById(R.id.etText);
        etText.setEnabled(false);
        y= Calendar.getInstance().get(Calendar.YEAR);
        cuyear=String.valueOf(y);

        imgMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent
                        = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                        Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

                try {
                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
                }
                catch (Exception e) {
                    Toast
                            .makeText(VoiceAssistantActivity.this, " " + e.getMessage(),
                                    Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                etText.setText(
                        Objects.requireNonNull(result).get(0));
                String text=etText.getText().toString().toLowerCase();

                if (text.contains("salary") ||text.contains("payslip") ||text.contains("pay slip")){
                    tvSearch.setText("Your Salary List is Here:");




                   if (text.contains("2020")){
                       year="2020";
                   }else if (text.contains("2021")){
                       year="2021";
                   }else if (text.contains("2022")){
                       year="2022";
                   }else if (text.contains("2023")){
                       year="2023";
                   }else if (text.contains("2023")){
                       year="2023";
                   }else if (text.contains("2024")){
                       year="2024";
                   }else if (text.contains("2025")){
                       year="2025";
                   }else if (text.contains("2026")){
                       year="2026";
                   }else if (text.contains("2027")){
                       year="2027";
                   }else if (text.contains("2028")){
                       year="2028";
                   }else if (text.contains("2029")){
                       year="2029";
                   }else if (text.contains("2030")){
                       year="2030";
                   }
                   else if (text.contains("2019")){
                       year="2019";
                   }else {
                       year="0";
                   }
                    //Salary

                    getSalaryList(year);


                }

                else if (text.contains("name") || text.contains("hi")||text.contains("hello")){

                    t1.speak("Hi I am Your Genius Voice Assistant.You may ask me about your PF Balance,Salary and CTC Details", TextToSpeech.QUEUE_FLUSH, null);
                }else if (text.contains("ctc")){

                    t1.speak("Here is your CTC details ", TextToSpeech.QUEUE_FLUSH, null);
                    openBrowser();
                    //CTC
                    //CTC
                }else if (text.contains("pf") || text.contains("pf balance")){
                    t1.speak("Here is your PF balance", TextToSpeech.QUEUE_FLUSH, null);
                    getPFURL();
                }else if (text.contains("document") || text.contains("kyc")){
                    t1.speak("Here is your submitted document", TextToSpeech.QUEUE_FLUSH, null);
                    getDocList();
                }
                else {
                    t1.speak("Sorry! I don't have any training regarding this", TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        }
    }


    private void onCLick(){
        imgMicSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llVoice.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                llNodata.setVisibility(View.GONE);
                etText.setText("");
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(VoiceAssistantActivity.this,EmployeeDashBoardActivity.class);
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
    }



    /*private void getPFLInk(){
        llVoice.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);

        final ProgressDialog progressDialog=new ProgressDialog(VoiceAssistantActivity.this);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        progressDialog.setCancelable(false);
        String surl = APi.sUrl+"Utility/GetPFManagementURL?EmployeeId=" + pref.getEmpId();
        Log.d("inputholiday",surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);

                        // attendabceInfiList.clear();
                        progressDialog.dismiss();
                        holidayitemList.clear();
                        leaveList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                           boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                String url = job1.optString("responseData");
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(browserIntent);


                            } else {
                                llVoice.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNodata.setVisibility(View.VISIBLE);

                            }


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(LoginActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }





                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                llVoice.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                llNodata.setVisibility(View.GONE);
                Toast.makeText(VoiceAssistantActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert",error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(VoiceAssistantActivity.this);
        requestQueue.add(stringRequest);
    }*/






    private void getSalaryList(String year) {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(VoiceAssistantActivity.this, LinearLayoutManager.VERTICAL, false);
        rvSalary.setLayoutManager(layoutManager);

        ProgressDialog pd=new ProgressDialog(VoiceAssistantActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        llVoice.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);
        String surl =  AppData.url+"get_Salary?AEMConsultantID=0&AEMClientID=null&MasterID=" + pref.getMasterId() + "&AEMEmployeeID=" + pref.getEmpId() + "&SalYear=" + year + "&SalMonth=jan&WorkingStatus=3&CurrentPage=1&SecurityCode="+pref.getSecurityCode();
        Log.d("reporturl", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseLeave", response);
                        salaryList.clear();
                        pd.dismiss();

                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {


                                t1.speak("Here is your salary list", TextToSpeech.QUEUE_FLUSH, null);

                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String SalMonth = obj.optString("SalMonth");
                                    String SalYear = obj.optString("SalYear");
                                    String MonthlyNet = obj.optString("MonthlyNet");
                                    String url = obj.optString("url");
                                    SalaryModule salaryModule = new SalaryModule(SalYear, SalMonth,"Rs. "+ MonthlyNet, url);
                                    salaryList.add(salaryModule);


                                }
                                llVoice.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.GONE);

                                SalaryAdapter  salaryAdapter = new SalaryAdapter(salaryList,VoiceAssistantActivity.this);
                                rvSalary.setAdapter(salaryAdapter);


                            } else {
                                llVoice.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNodata.setVisibility(View.VISIBLE);
                                t1.speak("Sorry No Details found", TextToSpeech.QUEUE_FLUSH, null);


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();

                            //Toast.makeText(SalaryActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                llVoice.setVisibility(View.GONE);
                llMain.setVisibility(View.GONE);
                llNodata.setVisibility(View.VISIBLE);
                // Toast.makeText(SalaryActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(VoiceAssistantActivity.this);
        requestQueue.add(stringRequest);
    }

    private void openBrowser(){
        Uri uri = Uri.parse(pref.getCTCURL()); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void getPFURL() {

        String surl = AppData.url+"get_PFManagementTripleA?MasterID="+pref.getMasterId()+"&SecurityCode="+pref.getSecurityCode();
        Log.d("inputLogin", surl);

        final ProgressDialog pd=new ProgressDialog(VoiceAssistantActivity.this);
        pd.setMessage("Loading.....");
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        pd.dismiss();
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
                                    String PFLink = obj.optString("url");

                                    openBrowserForPF(PFLink);



                                }




                            }


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(VoiceAssistantActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                //  Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");

    }

    private void openBrowserForPF(String url){
        Uri uri = Uri.parse(url); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    private void getDocList() {
        String surl = AppData.url+"gcl_DigitalDocument?AEMEmployeeID=" + pref.getEmpId() + "&FileName=null&FileType=0&DocumentID=0&ReferenceNo=0&DbOperation=1&SecurityCode=" + pref.getSecurityCode();
        Log.d("manageinput",surl);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(VoiceAssistantActivity.this, LinearLayoutManager.VERTICAL, false);
        rvSalary.setLayoutManager(layoutManager);

        ProgressDialog pd=new ProgressDialog(VoiceAssistantActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        llVoice.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        pd.dismiss();


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


                                llVoice.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.GONE);
                                DocumentAdapter  documentAdapter = new DocumentAdapter(documentList,VoiceAssistantActivity.this);
                                rvSalary.setAdapter(documentAdapter);


                            } else {
                                llVoice.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNodata.setVisibility(View.VISIBLE);

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
                pd.dismiss();
                llVoice.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                llNodata.setVisibility(View.GONE);

                //  Toast.makeText(DocumentReportActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");


    }




}