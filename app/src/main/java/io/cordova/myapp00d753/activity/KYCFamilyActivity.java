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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.intrusoft.scatter.ChartData;
import com.intrusoft.scatter.PieChart;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.AttendanceService;
import io.cordova.myapp00d753.module.MainDocModule;
import io.cordova.myapp00d753.module.UploadObject;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;
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
    LinearLayout llPrevious, llNext;
    Spinner spRealation1, spRealation2, spRealation3;
    Pref pref;
    LinearLayout llM1, llM2, llM3;

    //relation1
    ArrayList<MainDocModule> mainRealation1 = new ArrayList<>();
    ArrayList<String> realation1 = new ArrayList<>();

    //relation2
    ArrayList<MainDocModule> mainRealation2 = new ArrayList<>();
    ArrayList<String> realation2 = new ArrayList<>();

    //relation3
    ArrayList<MainDocModule> mainRealation3 = new ArrayList<>();
    ArrayList<String> realation3 = new ArrayList<>();
    LinearLayout llM1DOB, llM2DOB, llM3DOB;

    TextView tvM1DOB, tvM2DOB, tvM3DOB;
    EditText etM1Name, etM1Aadahar, etM2Name, etM2Aadahar, etM3Name, etM3Aadahar;
    String m1Name = "";
    String m1RealationId = "";
    String m1Aadahar = "";
    String m1DOB = "";

    String m2Name = "";
    String m2RealationId = "";
    String m2DOB = "";
    String m2Aadahar = "";

    String m3Name = "";
    String m3RealationId = "";
    String m3DOB = "";
    String m3Aadahar = "";

    String m1RealationName = "";
    String m2RealationName = "";
    String m3RealationName = "";
    TextView tvM3Realation, tvM1Realation, tvM2Realation;
    int flag = 0;
    ImageView imgBack;
    ProgressDialog progressDialog;
    private static final String SERVER_PATH = AppData.url;
    private AttendanceService uploadService;
    TextView tvSubmit, tvSkip;
    AlertDialog alerDialog1, alerDialog2;
    String Contact,DocumentUpload,FamilyMember,Miscellanneous,Necessary;
    ImageView imgHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kycfamily);
        initialize();
        setRealation1();
        onClick();
    }

    private void initialize() {
        pref = new Pref(KYCFamilyActivity.this);


        spRealation1 = (Spinner) findViewById(R.id.spRealation1);
        spRealation2 = (Spinner) findViewById(R.id.spRealation2);
        spRealation3 = (Spinner) findViewById(R.id.spRealation3);


        llM1DOB = (LinearLayout) findViewById(R.id.llM1DOB);
        llM2DOB = (LinearLayout) findViewById(R.id.llM2DOB);
        llM3DOB = (LinearLayout) findViewById(R.id.llM3DOB);

        tvM1DOB = (TextView) findViewById(R.id.tvM1DOB);
        tvM1DOB.setText(pref.getM1DOB());
        tvM2DOB = (TextView) findViewById(R.id.tvM2DOB);
        tvM2DOB.setText(pref.getM2DOB());
        tvM3DOB = (TextView) findViewById(R.id.tvM3DOB);
        tvM3DOB.setText(pref.getM3DOB());

        tvM3Realation = (TextView) findViewById(R.id.tvM3Realation);
        tvM1Realation = (TextView) findViewById(R.id.tvM1Realation);
        tvM2Realation = (TextView) findViewById(R.id.tvM2Realation);

        etM1Name = (EditText) findViewById(R.id.etM1Name);
        etM1Name.setText(pref.getM1Name());
        etM2Name = (EditText) findViewById(R.id.etM2Name);
        etM2Name.setText(pref.getM2Name());
        etM3Name = (EditText) findViewById(R.id.etM3Name);
        etM3Name.setText(pref.getM3Name());
        etM1Aadahar = (EditText) findViewById(R.id.etM1Aadahar);
        etM1Aadahar.setText(pref.getM1Aadahar());
        etM2Aadahar = (EditText) findViewById(R.id.etM2Aadahar);
        etM2Aadahar.setText(pref.getM2Aadahar());
        etM3Aadahar = (EditText) findViewById(R.id.etM3Aadahar);
        etM3Aadahar.setText(pref.getM3Aadahar());

        imgBack = (ImageView) findViewById(R.id.imgBack);

        llM1 = (LinearLayout) findViewById(R.id.llM1);
        llM2 = (LinearLayout) findViewById(R.id.llM2);
        llM3 = (LinearLayout) findViewById(R.id.llM3);


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        // Change base URL to your upload server URL.
        uploadService = (AttendanceService) new Retrofit.Builder()
                .baseUrl(SERVER_PATH)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AttendanceService.class);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");

        tvSubmit = (TextView) findViewById(R.id.tvSubmit);
        tvSkip = (TextView) findViewById(R.id.tvSkip);
        imgHome=(ImageView)findViewById(R.id.imgHome);

    }

    private void onClick() {

        etM1Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etM1Name.getText().toString().length() > 0) {
                    m1Name = etM1Name.getText().toString();

                    spRealation1.setVisibility(View.VISIBLE);
                    tvM1Realation.setVisibility(View.GONE);
                    llM1.setVisibility(View.VISIBLE);
                } else {
                    spRealation1.setVisibility(View.GONE);
                    tvM1Realation.setVisibility(View.VISIBLE);
                    llM1.setVisibility(View.GONE);
                }

            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        spRealation1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {

                    m1RealationId = mainRealation1.get(position).getDocID();

                    m1RealationName = mainRealation1.get(position).getDocumentType();
                    Log.d("m1RealationName", m1RealationName);
                }
                if (position == 1) {
                    m2adapterpos1();

                } else if (position == 2) {
                    m2adapterpos2();

                } else if (position == 3) {
                    m2adapterpos3();
                    // m2adapterpos31();


                } else if (position == 4) {
                    m2adapterpos4();
                } else if (position == 5) {
                    m2adapterpos5();

                } else if (position == 6) {
                    m2adapterpos6();

                } else if (position == 7) {
                    m2adapterpos7();

                } else if (position == 8) {
                    m2adapterpos8();

                } else if (position == 9) {
                    m2adapterpos9();

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileFunction();
            }
        });

        etM1Aadahar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etM1Aadahar.getText().toString().length() > 0) {
                    m1Aadahar = etM1Aadahar.getText().toString();

                }

            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(KYCFamilyActivity.this,TempDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });


        etM2Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etM2Name.getText().toString().length() > 0) {
                    m2Name = etM2Name.getText().toString();

                    spRealation2.setVisibility(View.VISIBLE);
                    tvM2Realation.setVisibility(View.GONE);
                    llM2.setVisibility(View.VISIBLE);
                } else {
                    spRealation2.setVisibility(View.GONE);
                    tvM2Realation.setVisibility(View.VISIBLE);
                    llM2.setVisibility(View.GONE);
                }

            }
        });

        etM3Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etM3Name.getText().toString().length() > 0) {
                    m3Name = etM3Name.getText().toString();

                    spRealation3.setVisibility(View.VISIBLE);
                    tvM3Realation.setVisibility(View.GONE);
                    llM3.setVisibility(View.VISIBLE);
                } else {
                    spRealation3.setVisibility(View.GONE);
                    tvM3Realation.setVisibility(View.VISIBLE);
                    llM3.setVisibility(View.GONE);
                }

            }
        });

        etM3Aadahar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etM3Aadahar.getText().toString().length() > 0) {
                    m3Aadahar = etM3Aadahar.getText().toString();

                }

            }
        });

        spRealation2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {

                    m2RealationId = mainRealation2.get(position).getDocID();

                    m2RealationName = mainRealation2.get(position).getDocumentType();
                    Log.d("m2realtion", m2RealationName);
                    if (position == 1) {
                        m3adapterpos1();
                    } else if (position == 2) {
                        m3adapterpos1();
                    } else if (position == 2) {
                        m3adapterpos1();
                    } else if (position == 3) {
                        m3adapterpos1();
                    } else if (position == 4) {
                        m3adapterpos1();
                    } else if (position == 5) {
                        m3adapterpos1();
                    } else if (position == 6) {
                        m3adapterpos1();
                    } else if (position == 7) {
                        m3adapterpos1();
                    } else if (position == 8) {
                        m3adapterpos1();
                    } else if (position == 9) {
                        m3adapterpos1();
                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        etM2Aadahar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etM2Aadahar.getText().toString().length() > 0) {
                    m2Aadahar = etM2Aadahar.getText().toString();


                } else {

                }

            }
        });

        spRealation3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    m3RealationId = mainRealation3.get(position).getDocID();
                    m3RealationName = mainRealation3.get(position).getDocumentType();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etM1Name.getText().toString().length() > 0) {
                    if (etM2Name.getText().toString().length() > 0) {
                        if (etM3Name.getText().toString().length() > 0) {
                            member3validation();
                        } else {
                            member2validation();
                        }

                    } else {
                        memeber1validation();
                    }


                } else if (etM2Name.getText().toString().length() > 0) {
                    if (etM3Name.getText().toString().length() > 0) {

                        member3validation();

                    } else {
                        member2validation();
                    }


                } else if (etM3Name.getText().toString().length() > 0) {
                    if (etM1Name.getText().toString().length() > 0) {
                        if (etM2Name.getText().toString().length() > 0) {
                            member2validation();
                        } else {
                            memeber1validation();
                        }

                    } else {
                        member3validation();
                    }


                } else {

                }
            }
        });

      /*  llPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/
        llM1DOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                final int year = c.get(Calendar.YEAR);
                final int month = c.get(Calendar.MONTH);
                final int sday = c.get(DAY_OF_MONTH);


                final DatePickerDialog dialog = new DatePickerDialog(KYCFamilyActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {

                        m1DOB = (m + 1) + "/" + d + "/" + y;
                        int s = (m + 1) + d + y;

                        tvM1DOB.setText(m1DOB);


                    }
                }, year, month, sday);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                dialog.show();
            }
        });

        llM2DOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                final int year = c.get(Calendar.YEAR);
                final int month = c.get(Calendar.MONTH);
                final int sday = c.get(DAY_OF_MONTH);


                final DatePickerDialog dialog = new DatePickerDialog(KYCFamilyActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {

                        m2DOB = (m + 1) + "/" + d + "/" + y;
                        int s = (m + 1) + d + y;

                        tvM2DOB.setText(m2DOB);


                    }
                }, year, month, sday);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                dialog.show();
            }
        });

        llM3DOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                final int year = c.get(Calendar.YEAR);
                final int month = c.get(Calendar.MONTH);
                final int sday = c.get(DAY_OF_MONTH);


                final DatePickerDialog dialog = new DatePickerDialog(KYCFamilyActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {

                        m3DOB = (m + 1) + "/" + d + "/" + y;
                        int s = (m + 1) + d + y;

                        tvM3DOB.setText(m3DOB);


                    }
                }, year, month, sday);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                dialog.show();
            }
        });
    }

    private void setRealation1() {

        String surl = AppData.url+"gcl_CommonDDL?ddltype=7&id1=0&id2=0&id3=0&SecurityCode=" + pref.getSecurityCode();
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        progressBar.dismiss();
                        realation1.clear();
                        mainRealation1.clear();


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                mainRealation1.add(new MainDocModule("0", "0"));
                                realation1.add("Please select");


                                //Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String value = obj.optString("value");
                                    String id = obj.optString("id");
                                    realation1.add(value);
                                    MainDocModule mainDocModule = new MainDocModule(id, value);
                                    mainRealation1.add(mainDocModule);

                                }
                                setRealation2();
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (KYCFamilyActivity.this, android.R.layout.simple_spinner_item,
                                                realation1); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spRealation1.setAdapter(spinnerArrayAdapter);


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
                progressBar.dismiss();

                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");


    }

    private void setRealation2() {


        String surl = AppData.url+"gcl_CommonDDL?ddltype=7&id1=0&id2=0&id3=0&SecurityCode=" + pref.getSecurityCode();
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        progressBar.dismiss();
                        realation2.clear();
                        mainRealation2.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                mainRealation2.add(new MainDocModule("0", "0"));
                                realation2.add("Please select");

                                //Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String value = obj.optString("value");
                                    String id = obj.optString("id");
                                    realation2.add(value);
                                    MainDocModule mainDocModule = new MainDocModule(id, value);
                                    mainRealation2.add(mainDocModule);

                                }
                                setRealation3();

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
                progressBar.dismiss();

                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");


    }

    private void setRealation3() {

        String surl = AppData.url+"gcl_CommonDDL?ddltype=7&id1=0&id2=0&id3=0&SecurityCode=" + pref.getSecurityCode();
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        progressBar.dismiss();
                        realation3.clear();
                        mainRealation3.clear();
                        mainRealation3.add(new MainDocModule("0", "0"));
                        realation3.add("Please select");

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {

                                //Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 3; i < responseData.length() - 2; i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String value = obj.optString("value");
                                    String id = obj.optString("id");
                                    realation3.add(value);
                                    MainDocModule mainDocModule = new MainDocModule(id, value);
                                    mainRealation3.add(mainDocModule);

                                }


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
                progressBar.dismiss();

                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");


    }


    private void m2adapterpos1() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (KYCFamilyActivity.this, android.R.layout.simple_spinner_item,
                        realation2) {

            @Override
            public boolean isEnabled(int position) {
                if (position == 1) {
                    // Disable the second item from Spinner
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 1) {
                    // Set the disable item text color
                    tv.setTextColor(Color.LTGRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRealation2.setAdapter(spinnerArrayAdapter);

    }

    private void m2adapterpos2() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (KYCFamilyActivity.this, android.R.layout.simple_spinner_item,
                        realation2) {

            @Override
            public boolean isEnabled(int position) {
                if (position == 2) {
                    // Disable the second item from Spinner
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 2) {
                    // Set the disable item text color
                    tv.setTextColor(Color.LTGRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRealation2.setAdapter(spinnerArrayAdapter);

    }

    private void m2adapterpos3() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (KYCFamilyActivity.this, android.R.layout.simple_spinner_item,
                        realation2) {

            @Override
            public boolean isEnabled(int position) {
                if (position == 3) {
                    // Disable the second item from Spinner
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 3) {
                    // Set the disable item text color
                    tv.setTextColor(Color.LTGRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRealation2.setAdapter(spinnerArrayAdapter);

    }

    private void m2adapterpos31() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (KYCFamilyActivity.this, android.R.layout.simple_spinner_item,
                        realation2) {

            @Override
            public boolean isEnabled(int position) {
                if (position == 7) {
                    // Disable the second item from Spinner
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 7) {
                    // Set the disable item text color
                    tv.setTextColor(Color.LTGRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRealation2.setAdapter(spinnerArrayAdapter);

    }

    private void m2adapterpos4() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (KYCFamilyActivity.this, android.R.layout.simple_spinner_item,
                        realation2);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRealation2.setAdapter(spinnerArrayAdapter);

    }

    private void m2adapterpos5() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (KYCFamilyActivity.this, android.R.layout.simple_spinner_item,
                        realation2);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRealation2.setAdapter(spinnerArrayAdapter);
    }

    private void m2adapterpos6() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (KYCFamilyActivity.this, android.R.layout.simple_spinner_item,
                        realation2);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRealation2.setAdapter(spinnerArrayAdapter);

    }

    private void m2adapterpos7() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (KYCFamilyActivity.this, android.R.layout.simple_spinner_item,
                        realation2);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRealation2.setAdapter(spinnerArrayAdapter);

    }

    private void m2adapterpos8() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (KYCFamilyActivity.this, android.R.layout.simple_spinner_item,
                        realation2) {

            @Override
            public boolean isEnabled(int position) {
                if (position == 8) {
                    // Disable the second item from Spinner
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 8) {
                    // Set the disable item text color
                    tv.setTextColor(Color.LTGRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRealation2.setAdapter(spinnerArrayAdapter);

    }

    private void m2adapterpos9() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (KYCFamilyActivity.this, android.R.layout.simple_spinner_item,
                        realation2) {

            @Override
            public boolean isEnabled(int position) {
                if (position == 9) {
                    // Disable the second item from Spinner
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 9) {
                    // Set the disable item text color
                    tv.setTextColor(Color.LTGRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRealation2.setAdapter(spinnerArrayAdapter);

    }


    private void m3adapterpos1() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (KYCFamilyActivity.this, android.R.layout.simple_spinner_item,
                        realation3);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRealation3.setAdapter(spinnerArrayAdapter);

    }

    private void m3adapterpos2() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (KYCFamilyActivity.this, android.R.layout.simple_spinner_item,
                        realation3) {

            @Override
            public boolean isEnabled(int position) {
                if (position == 2) {
                    // Disable the second item from Spinner
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 2) {
                    // Set the disable item text color
                    tv.setTextColor(Color.LTGRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRealation3.setAdapter(spinnerArrayAdapter);

    }

    private void m3adapterpos3() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (KYCFamilyActivity.this, android.R.layout.simple_spinner_item,
                        realation3) {

            @Override
            public boolean isEnabled(int position) {
                if (position == 2) {
                    // Disable the second item from Spinner
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 2) {
                    // Set the disable item text color
                    tv.setTextColor(Color.LTGRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRealation3.setAdapter(spinnerArrayAdapter);

    }

    private void m3adapterpos4() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (KYCFamilyActivity.this, android.R.layout.simple_spinner_item,
                        realation3);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRealation3.setAdapter(spinnerArrayAdapter);

    }

    private void m3adapterpos5() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (KYCFamilyActivity.this, android.R.layout.simple_spinner_item,
                        realation3);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRealation3.setAdapter(spinnerArrayAdapter);
    }

    private void m3adapterpos6() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (KYCFamilyActivity.this, android.R.layout.simple_spinner_item,
                        realation3);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRealation3.setAdapter(spinnerArrayAdapter);

    }

    private void m3adapterpos7() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (KYCFamilyActivity.this, android.R.layout.simple_spinner_item,
                        realation3);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRealation3.setAdapter(spinnerArrayAdapter);

    }

    private void m3adapterpos8() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (KYCFamilyActivity.this, android.R.layout.simple_spinner_item,
                        realation3) {

            @Override
            public boolean isEnabled(int position) {
                if (position == 8) {
                    // Disable the second item from Spinner
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 8) {
                    // Set the disable item text color
                    tv.setTextColor(Color.LTGRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRealation3.setAdapter(spinnerArrayAdapter);
    }

    private void m3adapterpos9() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (KYCFamilyActivity.this, android.R.layout.simple_spinner_item,
                        realation3) {

            @Override
            public boolean isEnabled(int position) {
                if (position == 9) {
                    // Disable the second item from Spinner
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 9) {
                    // Set the disable item text color
                    tv.setTextColor(Color.LTGRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRealation3.setAdapter(spinnerArrayAdapter);

    }

    private void memeber1validation() {
        if (!m1RealationName.equals("")) {
            if (!m1DOB.equals("")) {
                if (etM1Aadahar.getText().toString().length() > 11) {
                    nomineedetailssubmit();


                } else {
                    Toast.makeText(getApplicationContext(), "Please enter Aadhar", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Please choose Date of Birth", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), "Please choose realationship", Toast.LENGTH_LONG).show();
        }

    }

    private void member2validation() {
        if (!m2RealationName.equals("")) {
            if (!m2DOB.equals("")) {
                if (etM2Aadahar.getText().toString().length() > 11) {

                    nomineedetailssubmit();


                } else {
                    Toast.makeText(getApplicationContext(), "Please enter Aadhar of member2", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(getApplicationContext(), "please choose Date of Birth of member2", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), "Please choose Realationship of member2", Toast.LENGTH_LONG).show();
        }

    }

    private void member3validation() {
        if (!m3RealationName.equals("")) {
            if (!m3DOB.equals("")) {
                if (etM3Aadahar.getText().toString().length() > 11) {
                    nomineedetailssubmit();

                } else {
                    Toast.makeText(getApplicationContext(), "Please enter Aadhar of member3", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Please choose Date of Birth of member3", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), "Please choose realationship of member3", Toast.LENGTH_LONG).show();
        }

    }

    private void nomineedetailssubmit() {
        progressDialog.show();

        //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);


        Call<UploadObject> fileUpload = uploadService.uploadnominne(pref.getMasterId(), etM1Name.getText().toString(), m1RealationId, tvM1DOB.getText().toString(), etM1Aadahar.getText().toString(), "0", "0", etM2Name.getText().toString(), m2RealationId, tvM2DOB.getText().toString(), etM2Aadahar.getText().toString(), "0", "0", etM3Name.getText().toString(), m3RealationId, tvM3DOB.getText().toString(), etM3Aadahar.getText().toString(), "0", "0", pref.getSecurityCode());
        fileUpload.enqueue(new Callback<UploadObject>() {
            @Override
            public void onResponse(Call<UploadObject> call, retrofit2.Response<UploadObject> response) {
                progressDialog.dismiss();
                UploadObject extraWorkingDayModel = response.body();
                if (extraWorkingDayModel.isResponseStatus()) {
                    //  Toast.makeText(getApplicationContext(), extraWorkingDayModel.getResponseText(), Toast.LENGTH_SHORT).show();
                    successAlert();

                } else {
                    Toast.makeText(getApplicationContext(), extraWorkingDayModel.getResponseText(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UploadObject> call, Throwable t) {
                progressDialog.dismiss();

                Log.e("error", "Error " + t.getMessage());
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();

            }

        });

    }

    public void profileFunction() {
        String surl = AppData.url+"gcl_DummyKYCCompletion?AEMEmployeeID=" + pref.getMasterId() + "&SecurityCode=" + pref.getSecurityCode();
        Log.d("kyccomplete", surl);
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        progressBar.dismiss();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                //   Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                     Contact=obj.optString("Contact");
                                     DocumentUpload=obj.optString("DocumentUpload");
                                     FamilyMember=obj.optString("MemberUpload");
                                     Miscellanneous=obj.optString("Miscellanneous");
                                     Necessary=obj.optString("Necessary");



                                }
                                completedialog();

                            } else {
                                progressBar.dismiss();

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
                progressBar.dismiss();

                // Toast.makeText(TempProfileActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");

    }



    private void successAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(KYCFamilyActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);
        tvInvalidDate.setText("Thank you for Submitting KYC details");

        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog2.dismiss();
                profileFunction();
            }
        });

        alerDialog2 = dialogBuilder.create();
        alerDialog2.setCancelable(true);
        Window window = alerDialog2.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog2.show();
    }

    private void completedialog() {
        int nessecary= Integer.parseInt(Necessary);
        int docupload= Integer.parseInt(DocumentUpload);
        int family= Integer.parseInt(FamilyMember);
        int contact= Integer.parseInt(Contact);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(KYCFamilyActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_prflecomp, null);
        dialogBuilder.setView(dialogView);

        PieChart pieChart = (PieChart) dialogView.findViewById(R.id.pie_chart);
        List data = new ArrayList<>();
        data.add(new ChartData(nessecary + "%", nessecary));
        pieChart.setChartData(data);
        pieChart.partitionWithPercent(true);


        PieChart pieChartdoc = (PieChart) dialogView.findViewById(R.id.pie_chart_Doc);
        List docdata = new ArrayList<>();
        docdata.add(new ChartData(docupload + "%", docupload));
        pieChartdoc.setChartData(docdata);
        pieChartdoc.partitionWithPercent(true);


        PieChart pieChartfamily = (PieChart) dialogView.findViewById(R.id.pie_chart_nominee);
        List familydata = new ArrayList<>();
        familydata.add(new ChartData(family + "%", family));
        pieChartfamily.setChartData(familydata);
        pieChartfamily.partitionWithPercent(true);


        PieChart pieChartcontact = (PieChart) dialogView.findViewById(R.id.pie_chart_contact);
        List conatactdata = new ArrayList<>();
        conatactdata.add(new ChartData(contact + "%", contact));
        pieChartcontact.setChartData(conatactdata);
        pieChartcontact.partitionWithPercent(true);


        LinearLayout llOk = (LinearLayout) dialogView.findViewById(R.id.llOk);
        llOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerDialog1.dismiss();
                Intent intent=new Intent(KYCFamilyActivity.this,TempDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });


        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }


}
