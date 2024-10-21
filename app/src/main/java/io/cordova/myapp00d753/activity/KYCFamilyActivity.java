package io.cordova.myapp00d753.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.intrusoft.scatter.ChartData;
import com.intrusoft.scatter.PieChart;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.cordova.myapp00d753.KYCFamilyModel;
import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.EPSNominationAdapter;
import io.cordova.myapp00d753.adapter.KYCFamilyAdapter;
import io.cordova.myapp00d753.databinding.ActivityEpsnominationBinding;
import io.cordova.myapp00d753.databinding.ActivityKycfamilyBinding;
import io.cordova.myapp00d753.module.AttendanceService;
import io.cordova.myapp00d753.module.EPSmodel;
import io.cordova.myapp00d753.module.MainDocModule;
import io.cordova.myapp00d753.module.UploadObject;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.util.Calendar.DAY_OF_MONTH;


public class KYCFamilyActivity extends AppCompatActivity {
    ActivityKycfamilyBinding binding;
    JSONObject nominationobject=new JSONObject();
    JSONArray nominationarray=new JSONArray();
    ArrayList<KYCFamilyModel>itemList=new ArrayList<>();
    LinearLayoutManager layoutManager;
    String month;
    String dob="";
    Pref pref;
    ProgressDialog pd;
    ArrayList<MainDocModule> mainRealation = new ArrayList<>();
    ArrayList<String> realation = new ArrayList<>();
    String relationship="";
    String relationshipID="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_kycfamily);
        initialize();
        setNomineeRelation();
        onClick();


    }

    private void initialize() {
        pref=new Pref(KYCFamilyActivity.this);
        pd=new ProgressDialog(KYCFamilyActivity.this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        final SimpleTooltip tooltip = new SimpleTooltip.Builder(KYCFamilyActivity.this)
                .anchorView(binding.imgAdd)
                .text("Fill in the fields, then press the \"+\" icon to add your data to the list.")
                .gravity(Gravity.BOTTOM)
                .dismissOnOutsideTouch(true)
                .dismissOnInsideTouch(true)
                .modal(true)
                .animated(true)
                .animationDuration(2000)
                .contentView(R.layout.custom_tooltip, R.id.tv_text)
                .focusable(true)
                .build();

        tooltip.findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                if (tooltip.isShowing())
                    tooltip.dismiss();
            }
        });

        tooltip.show();
        layoutManager
                = new LinearLayoutManager(KYCFamilyActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvData.setLayoutManager(layoutManager);
        binding.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.etName.getText().toString().length()>0){
                    JSONObject jsonObject=new JSONObject();
                    try {
                        jsonObject.put("Name",binding.etName.getText().toString());
                        jsonObject.put("Gender",binding.etGender.getText().toString());
                        jsonObject.put("Relationship",relationshipID);
                        jsonObject.put("RelationshipID",relationship);
                        jsonObject.put("DOB",dob);
                        jsonObject.put("AEMEMPLOYEEID",pref.getEmpId());
                        nominationarray.put(jsonObject);
                        nominationobject.put("familyDetails",nominationarray);
                        nominationobject.put("DbOperation","6");
                        nominationobject.put("SecurityCode",pref.getSecurityCode());
                        Log.d("nomination",nominationobject.toString());
                        getItemList(nominationobject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(KYCFamilyActivity.this,"Please Enter Family Member's Name",Toast.LENGTH_LONG).show();
                }

            }
        });
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void getItemList(JSONObject object){
        binding.etName.setText("");
        binding.tvUANDOB.setText("");
        dob="";
        binding.etGender.setText("");
        binding.spRealation.setSelection(0);
        binding.llData.setVisibility(View.VISIBLE);
        itemList.clear();


        JSONArray nomination=object.optJSONArray("familyDetails");
        for (int i=0;i<nomination.length();i++){
            JSONObject nomiobj=nomination.optJSONObject(i);
            String Name=nomiobj.optString("Name");
            String Gender=nomiobj.optString("Gender");
            String Relationship=nomiobj.optString("RelationshipID");
            String DOB=nomiobj.optString("DOB");
            KYCFamilyModel epSmodel=new KYCFamilyModel();
            epSmodel.setFamilyMemberName(Name);
            epSmodel.setGender(Gender);
            epSmodel.setDob(DOB);
            epSmodel.setRealationship(Relationship);
            itemList.add(epSmodel);
        }
        KYCFamilyAdapter nominationAdapter=new KYCFamilyAdapter(itemList);
        binding.rvData.setAdapter(nominationAdapter);




    }
    private void onClick(){

        binding.spRealation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>0){
                    relationshipID=mainRealation.get(i).getDocID();
                    relationship=mainRealation.get(i).getDocumentType();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.btnSaveForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemList.size()>0){
                    uploadfamilydetails(nominationobject);
                }else {
                    Toast.makeText(KYCFamilyActivity.this,"Please Add Family Member",Toast.LENGTH_LONG).show();
                }
            }
        });
        binding.btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.llData.setVisibility(View.GONE);
                itemList.clear();

                Intent intent=new Intent(KYCFamilyActivity.this,EPSNominationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        binding.imgUANCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                System.out.println("Current date : " + (now.get(Calendar.MONTH) + 1) + "-"
                        + now.get(Calendar.DATE) + "-" + now.get(Calendar.YEAR));

                now = Calendar.getInstance();
                now.add(Calendar.YEAR, -18);
                int dyear = now.get(Calendar.YEAR);
                final int dmonth = now.get(Calendar.MONTH);
                int dday = now.get(DAY_OF_MONTH);
                Calendar c1 = Calendar.getInstance();
                /*final int syear = year - 18;

                final int month1 = c1.get(Calendar.MONTH);
                final int sday1 = c1.get(DAY_OF_MONTH);*/


                final DatePickerDialog dialog = new DatePickerDialog(KYCFamilyActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {

                        int mm = (m + 1);
                        int s = (m + 1) + d + y;
                        if (mm == 1) {
                            month = "January";
                        } else if (mm == 2) {
                            month = "February";
                        } else if (mm == 3) {
                            month = "March";
                        } else if (mm == 4) {
                            month = "April";
                        } else if (mm == 5) {
                            month = "May";
                        } else if (mm == 6) {
                            month = "June";
                        } else if (mm == 7) {
                            month = "July";
                        } else if (mm == 8) {
                            month = "August";
                        } else if (mm == 9) {
                            month = "September";
                        } else if (mm == 10) {
                            month = "October";
                        } else if (mm == 11) {
                            month = "November";
                        } else if (mm == 12) {
                            month = "December";
                        }
                        dob = d + " " + month + " " + y;

                        binding.tvUANDOB.setText(dob);


                    }
                }, dyear, dmonth, dday);
                dialog.getDatePicker().setMaxDate((long) (System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 365.25 * 18)));
                dialog.getDatePicker().setMinDate((long) (System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 365.25 * 90)));
                dialog.show();

            }
        });
    }


    private void uploadfamilydetails(JSONObject jsonObject) {
        pd.show();
        AndroidNetworking.post(AppData.newv2url+"KYC/UpdateKYCDetails")
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        pd.dismiss();

                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            Intent intent = new Intent(KYCFamilyActivity.this, EPSNominationActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            Toast.makeText(KYCFamilyActivity.this,"Family Details has been updated Successfully",Toast.LENGTH_LONG).show();

                        }else {

                        }
                    }

                    @Override
                    public void onError(ANError error) {

                            Intent intent=new Intent(KYCFamilyActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();

                    }
                });
    }


    private void setNomineeRelation() {

        String surl = AppData.url+"gcl_CommonDDL?ddltype=7&id1=0&id2=0&id3=0&SecurityCode=" + pref.getSecurityCode();
        ProgressDialog pd=new ProgressDialog(KYCFamilyActivity.this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                       pd.dismiss();
                        realation.clear();
                        mainRealation.clear();
                        realation.add("Please Select");
                        mainRealation.add(new MainDocModule("0",""));

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                //Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String value = obj.optString("value");
                                    String id = obj.optString("id");
                                    realation.add(value);
                                    MainDocModule mainDocModule = new MainDocModule(id, value);
                                    mainRealation.add(mainDocModule);

                                }
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (KYCFamilyActivity.this, android.R.layout.simple_spinner_item,
                                                realation); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                binding.spRealation.setAdapter(spinnerArrayAdapter);



                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(KYCFamilyActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               pd.dismiss();

                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());

            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");


    }





}
