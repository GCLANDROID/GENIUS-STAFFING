package io.cordova.myapp00d753.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

public class ProfileUpdatectivity extends AppCompatActivity {
    private static final String TAG = "ProfileUpdatectivity";
    TextView tvGurdianTitle, tvRealationTitle, tvQualificationtitle, tvMartialTitle, tvPerAddTitle, tvPerCountry, tvPerPinTile, tvPreAddTitle, tvPreCountry, tvPrePinTile, tvMobTitle;
    LinearLayout llLoader, llMain;
    Pref pref;
    String AEMEmployeeID, Code, Name, DateOfJoining, Department, Designation, Location, Sex, DateOfBirth, GuardianName, RelationShip, Qualification, MaritalStatus, BloodGroup, PermanentAddress, PresentAddress, Mobile, EmailID, PFNumber, ESINumber, BankName, AccountNumber;
    ArrayList<MainDocModule> mainGender = new ArrayList<>();
    ArrayList<String> gender = new ArrayList<>();
    ArrayList<MainDocModule> mainRealation = new ArrayList<>();
    ArrayList<String> realation = new ArrayList<>();
    ArrayList<MainDocModule> mainBlood = new ArrayList<>();
    ArrayList<String> blood = new ArrayList<>();
    Spinner spSex, spRealation, spBlood, spQualification, spMartial;
    EditText etGurdianName;
    ArrayList<MainDocModule> mainQualification = new ArrayList<>();
    ArrayList<String> qualification = new ArrayList<>();
    ArrayList<MainDocModule> mainMartial = new ArrayList<>();
    ArrayList<String> martial = new ArrayList<>();
    EditText etPerAddr, etMobile, etPhn, etEmailID, etPreAddr, etPerPinCode, etPrePinCode;
    TextView tvPerLocation, tvPreLocation;
    String PermanentCity, PresentCity;
    Spinner spPermanentCity, spPresentCity;
    ArrayList<String> percity = new ArrayList<>();
    ArrayList<MainDocModule> mainPerCity = new ArrayList<>();
    ArrayList<String> precity = new ArrayList<>();
    ArrayList<MainDocModule> mainPreCity = new ArrayList<>();
    TextView tvFirstName, tvLastName, tvDOB;
    private static final String SERVER_PATH = AppData.url;
    private AttendanceService uploadService;
    ProgressDialog progressDialog;
    ProgressDialog progressDialog1;
    String operationpersonal, operationcontact;
    String aempid, securitycode, username, bloodgrpid;
    String realationship = "";
    String qualificationid = "";
    String martialid = "";
    String percityid = "";
    String preCityId = "";
    Button btnUpdate;
   AlertDialog alerDialog1;
   ImageView imgBack,imgHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_updatectivity);
        initialize();
        //profileFunction();
        JSONObject obj=new JSONObject();
        try {
            obj.put("AEMConsultantID", pref.getEmpConId());
            obj.put("AEMClientID",pref.getEmpClintId());
            obj.put("AEMClientOfficeID",pref.getEmpClintOffId());
            obj.put("AEMEmployeeID",pref.getEmpId());
            obj.put("SecurityCode",pref.getSecurityCode());
            obj.put("WorkingStatus","1");
            obj.put("CurrentPage","0");
            profileFunction(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        onClick();
    }

    private void initialize() {
        pref = new Pref(ProfileUpdatectivity.this);
        tvGurdianTitle = (TextView) findViewById(R.id.tvGurdianTitle);
        String color = "<font color='#EE0000'>*</font>";
        String gurtitle = "Guardian's Name:";
        tvGurdianTitle.setText(Html.fromHtml(gurtitle + color));

        tvRealationTitle = (TextView) findViewById(R.id.tvRealationTitle);
        String relTilt = "Relationship:";
        tvRealationTitle.setText(Html.fromHtml(relTilt + color));

        tvQualificationtitle = (TextView) findViewById(R.id.tvQualificationtitle);
        String qualtitle = "Qualification:";
        tvQualificationtitle.setText(Html.fromHtml(qualtitle + color));

        tvMartialTitle = (TextView) findViewById(R.id.tvMartialTitle);
        String martitle = "Marital Status:";
        tvMartialTitle.setText(Html.fromHtml(martitle + color));

        tvPerAddTitle = (TextView) findViewById(R.id.tvPerAddTitle);
        String peraddtitle = "Address:";
        tvPerAddTitle.setText(Html.fromHtml(peraddtitle + color));

        tvPerCountry = (TextView) findViewById(R.id.tvPerCountry);
        String percountry = "City:";
        tvPerCountry.setText(Html.fromHtml(percountry + color));


        tvPerPinTile = (TextView) findViewById(R.id.tvPerPinTile);
        String perpin = "Pincode:";
        tvPerPinTile.setText(Html.fromHtml(perpin + color));


        tvPreAddTitle = (TextView) findViewById(R.id.tvPreAddTitle);
        String preadd = "Address:";
        tvPreAddTitle.setText(Html.fromHtml(preadd + color));

        tvPreCountry = (TextView) findViewById(R.id.tvPreCountry);
        String precountry = "City:";
        tvPreCountry.setText(Html.fromHtml(precountry + color));


        tvPrePinTile = (TextView) findViewById(R.id.tvPrePinTile);
        String prepin = "Pincode:";
        tvPrePinTile.setText(Html.fromHtml(prepin + color));

        tvMobTitle = (TextView) findViewById(R.id.tvMobTitle);
        String Mobile = "Mobile:";
        tvMobTitle.setText(Html.fromHtml(Mobile + color));

        llLoader = (LinearLayout) findViewById(R.id.llLoader);
        llMain = (LinearLayout) findViewById(R.id.llMain);
        spSex = (Spinner) findViewById(R.id.spSex);
        spRealation = (Spinner) findViewById(R.id.spRealation);
        spBlood = (Spinner) findViewById(R.id.spBlood);
        spQualification = (Spinner) findViewById(R.id.spQualification);
        spMartial = (Spinner) findViewById(R.id.spMartial);
        etGurdianName = (EditText) findViewById(R.id.etGurdianName);
        etPerAddr = (EditText) findViewById(R.id.etPerAddr);
        etMobile = (EditText) findViewById(R.id.etMobile);
        etPhn = (EditText) findViewById(R.id.etPhn);
        etEmailID = (EditText) findViewById(R.id.etEmailID);
        tvPerLocation = (TextView) findViewById(R.id.tvPerLocation);
        tvPreLocation = (TextView) findViewById(R.id.tvPreLocation);
        etPreAddr = (EditText) findViewById(R.id.etPreAddr);
        spPermanentCity = (Spinner) findViewById(R.id.spPermanentCity);
        etPerPinCode = (EditText) findViewById(R.id.etPerPinCode);
        etPrePinCode = (EditText) findViewById(R.id.etPrePinCode);
        spPresentCity = (Spinner) findViewById(R.id.spPresentCity);
        tvFirstName = (TextView) findViewById(R.id.tvFirstName);
        tvLastName = (TextView) findViewById(R.id.tvLastName);
        tvDOB = (TextView) findViewById(R.id.tvDOB);
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
        progressDialog1 = new ProgressDialog(this);
        progressDialog1.setMessage("Uploading...");
        progressDialog.setMessage("Uploading...");
        aempid = pref.getEmpId();
        securitycode = pref.getSecurityCode();
        username = pref.getEmpName();
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        operationpersonal = "1";

        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);


    }

    private void onClick() {
        spRealation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                realationship = mainRealation.get(position).getDocID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spQualification.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                qualificationid = mainQualification.get(position).getDocID();
                Log.d("qualificationid", qualificationid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spMartial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                martialid = mainMartial.get(position).getDocID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spBlood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bloodgrpid = mainBlood.get(position).getDocID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spPermanentCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                percityid = mainPerCity.get(position).getDocID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spPresentCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                preCityId = mainPreCity.get(position).getDocID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileUpdatectivity.this,EmployeeDashBoardActivity.class);
                startActivity(intent);
            }
        });


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etGurdianName.getText().toString().length() > 0) {
                    if (!realationship.equals("")) {
                        if (!qualificationid.equals("")) {
                            if (!martialid.equals("")) {
                                if (etPerAddr.getText().toString().length() > 0) {
                                    if (!percityid.equals("")) {
                                        if (etPerPinCode.getText().toString().length() > 5) {
                                            if (!etPerPinCode.getText().toString().equals("null")) {
                                                if (etPreAddr.getText().toString().length() > 0) {
                                                    if (!preCityId.equals("")) {
                                                        if (etPrePinCode.getText().toString().length() == 6) {
                                                            if (etMobile.getText().toString().length() > 9) {
                                                                savepersonal();

                                                            } else {
                                                                Toast.makeText(getApplicationContext(), "please enter Mobile Number", Toast.LENGTH_LONG).show();
                                                            }
                                                        } else {
                                                            Toast.makeText(getApplicationContext(), "Please enter Present PinCode", Toast.LENGTH_LONG).show();
                                                        }

                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "Please select Present City", Toast.LENGTH_LONG).show();
                                                    }
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Please enter Present Address", Toast.LENGTH_LONG).show();
                                                }
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Please enter Pin Code", Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Please enter Pin Code", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "please select Permanent City", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Please enter Permamnent address", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Please select Martial Status", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Please  select Qualification", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please select Relationship Status", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter Gurduian Name", Toast.LENGTH_LONG).show();
                }

            }
        });

    }


    private void profileFunction(JSONObject jsonObject) {
        Log.e(TAG, "profileFunction: "+jsonObject);
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        AndroidNetworking.post(AppData.GCL_KYC)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.e(TAG, "PROFILE: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");

                                JSONArray jsonArray = new JSONArray(Response_Data);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    AEMEmployeeID = obj.optString("AEMEmployeeID");
                                    Code = obj.optString("Code");
                                    Name = obj.optString("Name");
                                    DateOfJoining = obj.optString("DOJ");
                                    Department = obj.optString("Department");
                                    Designation = obj.optString("Designation");
                                    Location = obj.optString("Location");
                                    tvPreLocation.setText(Location);
                                    tvPerLocation.setText(Location);
                                    Sex = obj.optString("Sex");
                                    DateOfBirth = obj.optString("DateOfBirth");
                                    GuardianName = obj.optString("GuardianName");
                                    etGurdianName.setText(GuardianName);
                                    RelationShip = obj.optString("RelationShip");
                                    Qualification = obj.optString("Qualification");
                                    MaritalStatus = obj.optString("MaritalStatus");
                                    BloodGroup = obj.optString("BloodGroup");
                                    PermanentAddress = obj.optString("PermanentAddress");
                                    etPerAddr.setText(PermanentAddress);
                                    PresentAddress = obj.optString("PresentAddress");
                                    etPreAddr.setText(PresentAddress);
                                    Mobile = obj.optString("Mobile");
                                    etMobile.setText(Mobile);
                                    EmailID = obj.optString("EmailID");
                                    etEmailID.setText(EmailID);
                                    PFNumber = obj.optString("PFNumber");
                                    ESINumber = obj.optString("ESINumber");
                                    BankName = obj.optString("BankName");
                                    AccountNumber = obj.optString("AccountNumber");
                                    String Phone = obj.optString("Phone");
                                    etPhn.setText(Phone);
                                    PermanentCity = obj.optString("PermanentCity");
                                    String PermanentPinCode = obj.optString("PermanentPinCode");
                                    etPerPinCode.setText(PermanentPinCode);
                                    String PresentPincode = obj.optString("PresentPincode");
                                    etPrePinCode.setText(PresentPincode);
                                    PresentCity = obj.optString("PresentCity");
                                    String FirstName = obj.optString("FirstName");
                                    tvFirstName.setText(FirstName);
                                    String LastName = obj.optString("LastName");
                                    tvLastName.setText(LastName);
                                    String DateOfBirth = obj.optString("DateOfBirth");
                                    tvDOB.setText(DateOfBirth);
                                }

                                llLoader.setVisibility(View.VISIBLE);
                                llMain.setVisibility(View.GONE);
                                setGender();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ProfileUpdatectivity.this, "Something went to wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        llLoader.setVisibility(View.VISIBLE);
                        llMain.setVisibility(View.GONE);
                        Log.e(TAG, "PROFILE_error: "+anError.getErrorCode());
                    }
                });
    }


    public void profileFunction() {
        Log.d("apihit", "1");
        String surl = AppData.url+"gcl_KYC?AEMConsultantID=" + pref.getEmpConId() + "&AEMClientID=" + pref.getEmpClintId() + "&AEMClientOfficeID=" + pref.getEmpClintOffId() + "&AEMEmployeeID=" + pref.getEmpId() + "&SecurityCode=" + pref.getSecurityCode() + "&WorkingStatus=1&CurrentPage=0";
        Log.d("kyc", surl);
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);

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
                                //   Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    AEMEmployeeID = obj.optString("AEMEmployeeID");
                                    Code = obj.optString("Code");
                                    Name = obj.optString("Name");
                                    DateOfJoining = obj.optString("DateOfJoining");
                                    Department = obj.optString("Department");
                                    Designation = obj.optString("Designation");
                                    Location = obj.optString("Location");
                                    tvPreLocation.setText(Location);
                                    tvPerLocation.setText(Location);
                                    Sex = obj.optString("Sex");
                                    DateOfBirth = obj.optString("DateOfBirth");
                                    GuardianName = obj.optString("GuardianName");
                                    etGurdianName.setText(GuardianName);
                                    RelationShip = obj.optString("RelationShip");
                                    Qualification = obj.optString("Qualification");
                                    MaritalStatus = obj.optString("MaritalStatus");
                                    BloodGroup = obj.optString("BloodGroup");
                                    PermanentAddress = obj.optString("PermanentAddress");
                                    etPerAddr.setText(PermanentAddress);
                                    PresentAddress = obj.optString("PresentAddress");
                                    etPreAddr.setText(PresentAddress);
                                    Mobile = obj.optString("Mobile");
                                    etMobile.setText(Mobile);
                                    EmailID = obj.optString("EmailID");
                                    etEmailID.setText(EmailID);
                                    PFNumber = obj.optString("PFNumber");
                                    ESINumber = obj.optString("ESINumber");
                                    BankName = obj.optString("BankName");
                                    AccountNumber = obj.optString("AccountNumber");
                                    String Phone = obj.optString("Phone");
                                    etPhn.setText(Phone);
                                    PermanentCity = obj.optString("PermanentCity");
                                    String PermanentPinCode = obj.optString("PermanentPinCode");
                                    etPerPinCode.setText(PermanentPinCode);
                                    String PresentPincode = obj.optString("PresentPincode");
                                    etPrePinCode.setText(PresentPincode);
                                    PresentCity = obj.optString("PresentCity");
                                    String FirstName = obj.optString("FirstName");
                                    tvFirstName.setText(FirstName);
                                    String LastName = obj.optString("LastName");
                                    tvLastName.setText(LastName);
                                    String DateOfBirth = obj.optString("DateOfBirth");
                                    tvDOB.setText(DateOfBirth);


                                }
                                llLoader.setVisibility(View.VISIBLE);
                                llMain.setVisibility(View.GONE);
                                setGender();
                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(ProfileUpdatectivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);

                Toast.makeText(ProfileUpdatectivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
    }


    private void setGender() {
        Log.d("apihit", "2");
        String surl = AppData.url+"gcl_CommonDDL?ddltype=10&id1=0&id2=0&id3=0&SecurityCode=" + pref.getSecurityCode();
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        llLoader.setVisibility(View.VISIBLE);
                        llMain.setVisibility(View.GONE);
                        gender.clear();
                        mainGender.clear();

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
                                    gender.add(value);
                                    MainDocModule mainDocModule = new MainDocModule(id, value);
                                    mainGender.add(mainDocModule);

                                }
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (ProfileUpdatectivity.this, android.R.layout.simple_spinner_item,
                                                gender); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spSex.setAdapter(spinnerArrayAdapter);
                                int index = gender.indexOf(Sex);
                                Log.d("indexr", String.valueOf(index));
                                spSex.setSelection(index);
                                spSex.setEnabled(false);
                                setRealation();


                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ProfileUpdatectivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);

                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
    }


    private void setRealation() {
        mainRealation.clear();
        realation.clear();
        mainRealation.add(new MainDocModule("REL00000001","FATHER"));
        mainRealation.add(new MainDocModule("REL00000008","HUSBAND"));
        realation.add("Father");
        realation.add("Husband");

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (ProfileUpdatectivity.this, android.R.layout.simple_spinner_item,
                        realation); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRealation.setAdapter(spinnerArrayAdapter);
        int index = realation.indexOf(RelationShip);
        Log.d("indexr", String.valueOf(index));
        spRealation.setSelection(index);
        setBlood();
    }

    private void setBlood() {
        Log.d("apihit", "4");
        String surl = AppData.url+"gcl_CommonDDL?ddltype=9&id1=0&id2=0&id3=0&SecurityCode=" + pref.getSecurityCode();
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        llLoader.setVisibility(View.VISIBLE);
                        llMain.setVisibility(View.GONE);
                        blood.clear();
                        mainBlood.clear();

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
                                    blood.add(value);
                                    MainDocModule mainDocModule = new MainDocModule(id, value);
                                    mainBlood.add(mainDocModule);

                                }
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (ProfileUpdatectivity.this, android.R.layout.simple_spinner_item,
                                                blood); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spBlood.setAdapter(spinnerArrayAdapter);
                                int index = blood.indexOf(BloodGroup);
                                Log.d("indexr", String.valueOf(index));
                                spBlood.setSelection(index);
                                setQualification();

                            } else {}
                            // boolean _status = job1.getBoolean("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(ProfileUpdatectivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) { };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
    }

    private void setQualification() {
        Log.d("apihit", "5");
        String surl = AppData.url+"gcl_CommonDDL?ddltype=6&id1=0&id2=0&id3=0&SecurityCode=" + pref.getSecurityCode();
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        llLoader.setVisibility(View.VISIBLE);
                        llMain.setVisibility(View.GONE);
                        qualification.clear();
                        mainQualification.clear();
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
                                    String qualivalue = obj.optString("value");
                                    String qualiid = obj.optString("id");
                                    qualification.add(qualivalue);
                                    MainDocModule mainDocModule = new MainDocModule(qualiid, qualivalue);
                                    mainQualification.add(mainDocModule);

                                }
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (ProfileUpdatectivity.this, android.R.layout.simple_spinner_item,
                                                qualification); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spQualification.setAdapter(spinnerArrayAdapter);
                                int index = qualification.indexOf(Qualification);
                                Log.d("indexr", String.valueOf(index));
                                spQualification.setSelection(index);
                                setMartial();

                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(ProfileUpdatectivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");


    }

    private void setMartial() {
        Log.d("apihit", "6");
        String surl = AppData.url+"gcl_CommonDDL?ddltype=8&id1=0&id2=0&id3=0&SecurityCode=" + pref.getSecurityCode();
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        llLoader.setVisibility(View.VISIBLE);
                        llMain.setVisibility(View.GONE);
                        martial.clear();
                        mainMartial.clear();

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
                                    String qualivalue = obj.optString("value");
                                    String qualiid = obj.optString("id");
                                    martial.add(qualivalue);
                                    MainDocModule mainDocModule = new MainDocModule(qualiid, qualivalue);
                                    mainMartial.add(mainDocModule);

                                }
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (ProfileUpdatectivity.this, android.R.layout.simple_spinner_item,
                                                martial); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spMartial.setAdapter(spinnerArrayAdapter);
                                int index = martial.indexOf(MaritalStatus);
                                Log.d("indexr", String.valueOf(index));
                                spMartial.setSelection(index);
                                setPerCity();

                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            //  Toast.makeText(ProfileUpdatectivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);

                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");


    }

    private void setPerCity() {
        Log.d("apihit", "7");
        String surl = AppData.url+"gcl_CommonDDL?ddltype=4&id1=0&id2=0&id3=0&SecurityCode=" + pref.getSecurityCode();
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        llLoader.setVisibility(View.VISIBLE);
                        llMain.setVisibility(View.GONE);
                        percity.clear();
                        mainPerCity.clear();

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
                                    String qualivalue = obj.optString("value");
                                    String qualiid = obj.optString("id");
                                    percity.add(qualivalue);
                                    MainDocModule mainDocModule = new MainDocModule(qualiid, qualivalue);
                                    mainPerCity.add(mainDocModule);

                                }
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (ProfileUpdatectivity.this, android.R.layout.simple_spinner_item,
                                                percity); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spPermanentCity.setAdapter(spinnerArrayAdapter);
                                int index = percity.indexOf(PermanentCity);
                                Log.d("indexr", String.valueOf(index));
                                spPermanentCity.setSelection(index);
                                serPreCity();

                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ProfileUpdatectivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);

                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");


    }

    private void serPreCity() {
        Log.d("apihit", "8");
        String surl = AppData.url+"gcl_CommonDDL?ddltype=4&id1=0&id2=0&id3=0&SecurityCode=" + pref.getSecurityCode();
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        llLoader.setVisibility(View.GONE);
                        llMain.setVisibility(View.VISIBLE);
                        precity.clear();
                        mainPreCity.clear();

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
                                    String qualivalue = obj.optString("value");
                                    String qualiid = obj.optString("id");
                                    precity.add(qualivalue);
                                    MainDocModule mainDocModule = new MainDocModule(qualiid, qualivalue);
                                    mainPreCity.add(mainDocModule);

                                }
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (ProfileUpdatectivity.this, android.R.layout.simple_spinner_item,
                                                precity); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spPresentCity.setAdapter(spinnerArrayAdapter);
                                int index = percity.indexOf(PresentCity);
                                Log.d("indexr", String.valueOf(index));
                                spPresentCity.setSelection(index);

                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ProfileUpdatectivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);

                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");


    }

    private void savepersonal() {
        String peraddress = etPerAddr.getText().toString();
        String perPinCode = etPerPinCode.getText().toString();
        String preaddress = etPreAddr.getText().toString();
        String prepincode = etPrePinCode.getText().toString();
        String phone = etPhn.getText().toString();
        String mobile = etMobile.getText().toString();
        String email = etEmailID.getText().toString();
        //qualificationid="0";

        String gurdianname = etGurdianName.getText().toString();
        final ProgressDialog pd = new ProgressDialog(ProfileUpdatectivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();

        AndroidNetworking.upload(AppData.url+"post_KycUpdatedByEmployee")
                .addMultipartParameter("AEMEmployeeID", aempid)
                .addMultipartParameter("GuardianName", gurdianname)
                .addMultipartParameter("RelationShip", realationship)
                .addMultipartParameter("Qualification", qualificationid)
                .addMultipartParameter("MaritalStatus", martialid)
                .addMultipartParameter("BloodGroup", bloodgrpid)
                .addMultipartParameter("PermanentAddress", peraddress)
                .addMultipartParameter("PermanentCityID", percityid)
                .addMultipartParameter("PermanentPinCode", perPinCode)
                .addMultipartParameter("PresentAddress", preaddress)
                .addMultipartParameter("PresentCityID", preCityId)
                .addMultipartParameter("PresentPincode", prepincode)
                .addMultipartParameter("Phone", phone)
                .addMultipartParameter("mobile", mobile)
                .addMultipartParameter("EmailID", email)
                .addMultipartParameter("UserName", aempid)
                .addMultipartParameter("SecurityCode", securitycode)
                .addMultipartParameter("Operation", operationpersonal)

                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        pd.show();

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        pd.dismiss();

                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        String responseText = job1.optString("responseText");
                        Log.d("responseText", responseText);
                        boolean responseStatus=job1.optBoolean("responseStatus");
                        if (responseStatus) {
                            savecontact();


                        } else {
                            pd.dismiss();
                            Toast.makeText(ProfileUpdatectivity.this, responseText, Toast.LENGTH_LONG).show();

                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void savecontact() {
        String peraddress = etPerAddr.getText().toString();
        String perPinCode = etPerPinCode.getText().toString();
        String preaddress = etPreAddr.getText().toString();
        String prepincode = etPrePinCode.getText().toString();
        String phone = etPhn.getText().toString();
        String mobile = etMobile.getText().toString();
        String email = etEmailID.getText().toString();
        operationcontact = "2";
        String gurdianname = etGurdianName.getText().toString();


        final ProgressDialog pd = new ProgressDialog(ProfileUpdatectivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();

        AndroidNetworking.upload(AppData.url+"post_KycUpdatedByEmployee")
                .addMultipartParameter("AEMEmployeeID", aempid)
                .addMultipartParameter("GuardianName", gurdianname)
                .addMultipartParameter("RelationShip", realationship)
                .addMultipartParameter("Qualification", qualificationid)
                .addMultipartParameter("MaritalStatus", martialid)
                .addMultipartParameter("BloodGroup", bloodgrpid)
                .addMultipartParameter("PermanentAddress", peraddress)
                .addMultipartParameter("PermanentCityID", percityid)
                .addMultipartParameter("PermanentPinCode", perPinCode)
                .addMultipartParameter("PresentAddress", preaddress)
                .addMultipartParameter("PresentCityID", preCityId)
                .addMultipartParameter("PresentPincode", prepincode)
                .addMultipartParameter("Phone", phone)
                .addMultipartParameter("mobile", mobile)
                .addMultipartParameter("EmailID", email)
                .addMultipartParameter("UserName", aempid)
                .addMultipartParameter("SecurityCode", securitycode)
                .addMultipartParameter("Operation", operationcontact)

                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        pd.show();

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pd.dismiss();


                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        String responseText = job1.optString("responseText");
                        Log.d("responseText", responseText);
                        boolean responseStatus=job1.optBoolean("responseStatus");
                        if (responseStatus) {
                            successAlert();


                        } else {

                            Toast.makeText(ProfileUpdatectivity.this, responseText, Toast.LENGTH_LONG).show();

                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void successAlert() {
       AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProfileUpdatectivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);
        tvInvalidDate.setText("Profile has been Updated successfully");
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                //profileFunction();
                JSONObject obj=new JSONObject();
                try {
                    obj.put("AEMConsultantID", pref.getEmpConId());
                    obj.put("AEMClientID",pref.getEmpClintId());
                    obj.put("AEMClientOfficeID",pref.getEmpClintOffId());
                    obj.put("AEMEmployeeID",pref.getEmpId());
                    obj.put("SecurityCode",pref.getSecurityCode());
                    obj.put("WorkingStatus","1");
                    obj.put("CurrentPage","0");
                    profileFunction(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
