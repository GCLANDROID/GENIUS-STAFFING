package io.cordova.myapp00d753.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;

import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.cameraview.LongImageCameraActivity;
import com.intrusoft.scatter.ChartData;
import com.intrusoft.scatter.PieChart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import id.zelory.compressor.Compressor;
import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.databinding.ActivityTempProfileBinding;
import io.cordova.myapp00d753.module.AttendanceService;
import io.cordova.myapp00d753.module.MainDocModule;
import io.cordova.myapp00d753.module.UploadObject;
import io.cordova.myapp00d753.utility.ApiClient;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.ValidUtils;
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

public class TempProfileActivity extends AppCompatActivity {
    private static final String TAG = "TempProfileActivity";
    TextView tvEmplId, tvEmpCode, tvEmpName, tvDOJ, tvDepartment, tvDesignation, tvLocation, tvGender, tvEmpCodeDOB, tvRealtionShip, tvQualification, tvMarital, tvBloodGroup, tvParAddr, tvDOB, tvComName;
    Spinner spGender, spRealation, spQualification, spMartial, spBloodGrp;
    EditText etPhnNumber, etMobNumber, etEmailId, etGurdianName;
    LinearLayout llLoader, llMain;
    Pref pref;
    ArrayList<MainDocModule> mainQualification = new ArrayList<>();
    ArrayList<String> qualification = new ArrayList<>();
    ArrayList<MainDocModule> mainMartial = new ArrayList<>();
    ArrayList<String> martial = new ArrayList<>();
    ArrayList<MainDocModule> mainGender = new ArrayList<>();
    ArrayList<String> gender = new ArrayList<>();
    ArrayList<MainDocModule> mainRealation = new ArrayList<>();
    ArrayList<String> realation = new ArrayList<>();
    ArrayList<MainDocModule> mainBlood = new ArrayList<>();
    ArrayList<String> blood = new ArrayList<>();
    String qualivalue, qualiid;
    String Qualification;
    String MaritalStatus;
    String Sex;
    String RelationShip;
    String BloodGroup;
    EditText etESI, etUAN;
    TextView tvCity;
    String PresentCity, AEMEmployeeID, Code, Name, DateOfJoining, Department, Designation, Location, DateOfBirth, GuardianName, PermanentAddress, Mobile, EmailID, Phone, AEMClientName;
    private static final String SERVER_PATH = AppData.url;
    private AttendanceService uploadService;
    ProgressDialog progressDialog;
    String sexGender, realationship, education, martialstatus, bloodgrp;
    Button btnUpdate;
    ImageView imgBack, imgHome;
    AlertDialog alerDialog1;
    TextView tvGenderTitle, tvGurdianTitle, tvRealationTitle, tvQualificationtitle, tvMartialTitle, tvBloodTitle, tvMobTitle, tvEmailTitle;
    String color, PresentAddress, FirstNameAsperBank, LastNameAsperBank, PermanentState, PermanentCity, PresentState;
    TextView tvPreAddr, tvPrePin, tvDOBTitle, tvPerAddr, tvPerPin, tvPreState, tvPreCity, tvPerState, tvPerCity, tvAddaharNo, tvAddaharImg;
    EditText etPreAddr, etPrePinCode, etBankFirstName, etLastBank, etAddaharNo, etPerAddr, etPerPinCode;
    ArrayList<String> percity = new ArrayList<>();
    ArrayList<MainDocModule> mainPerCity = new ArrayList<>();
    ArrayList<String> precity = new ArrayList<>();
    ArrayList<MainDocModule> mainPreCity = new ArrayList<>();
    Spinner spPresentState, spPresentCity, spPermanentState, spPermanentCity;

    ArrayList<String> perstate = new ArrayList<>();
    ArrayList<MainDocModule> mainPerState = new ArrayList<>();
    ArrayList<String> prestate = new ArrayList<>();
    ArrayList<MainDocModule> mainPreState = new ArrayList<>();
    ImageView imgCal, imgCamera;
    ImageView imgDocument;

    private String encodedImage;
    private Uri imageUri;
    private static final int CAMERA_REQUEST = 1;
    private static final int GALLERY_REQUEST = 2;
    File file, compressedImageFile;
    String permanentcity = "";
    String permanentstate = "";
    String presentstate = "";
    String presentcity = "";
    String aadharno = "";
    String preaddr = "";
    String peraddr = "";
    String prepin = "";
    String perpin = "";
    EditText etSkill;
    String month;
    int flag;
    TextView tvView, tvSubmit, tvSkip;
    String phnnumber, esi, uan;
    ImageView imgForward;
    int nessecary, Contact, FamilyMember, DocumentUpload, Miscellanneous;
    List<ChartData> data, data1;
    List<ChartData> conatactdata;
    List<ChartData> familydata;
    List<ChartData> docdata;
    AlertDialog alerDialog3;
    AlertDialog alert1;
    int errflag = 0;
    TextView tv1,tv2;
    ActivityTempProfileBinding binding;

    ArrayList<String> residing = new ArrayList<>();
    ArrayList<String> uanPercentage = new ArrayList<>();
    String esicDOB="",uanDOB="",esicGender="",esicRltionshp="",uanRltionshp="",residingIP="",pfPercantage="";
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_temp_profile);
        initialize();
       // completedialog();
        onClick();
    }

    private void initialize() {
        pref = new Pref(TempProfileActivity.this);
        pd=new ProgressDialog(TempProfileActivity.this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        tvEmplId = (TextView) findViewById(R.id.tvEmplId);
        tvEmpCode = (TextView) findViewById(R.id.tvEmpCode);
        tvEmpName = (TextView) findViewById(R.id.tvEmpName);
        tvDOJ = (TextView) findViewById(R.id.tvDOJ);
        tvDepartment = (TextView) findViewById(R.id.tvDepartment);
        tvDesignation = (TextView) findViewById(R.id.tvDesignation);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        tvGender = (TextView) findViewById(R.id.tvGender);
        tvEmpCodeDOB = (TextView) findViewById(R.id.tvEmpCodeDOB);
        tvRealtionShip = (TextView) findViewById(R.id.tvRealtionShip);
        tvQualification = (TextView) findViewById(R.id.tvQualification);
        tvMarital = (TextView) findViewById(R.id.tvMarital);
        tvBloodGroup = (TextView) findViewById(R.id.tvBloodGroup);
        //tvParAddr = (TextView) findViewById(R.id.tvParAddr);
        tvComName = (TextView) findViewById(R.id.tvComName);


        spGender = (Spinner) findViewById(R.id.spGender);
        spRealation = (Spinner) findViewById(R.id.spRealation);
        spQualification = (Spinner) findViewById(R.id.spQualification);
        spMartial = (Spinner) findViewById(R.id.spMartial);
        spBloodGrp = (Spinner) findViewById(R.id.spBloodGrp);

        etPhnNumber = (EditText) findViewById(R.id.etPhnNumber);
        etMobNumber = (EditText) findViewById(R.id.etMobNumber);
        etEmailId = (EditText) findViewById(R.id.etEmail);
        etGurdianName = (EditText) findViewById(R.id.etGurdianName);
        etESI = (EditText) findViewById(R.id.etESI);
        etUAN = (EditText) findViewById(R.id.etUAN);

        llLoader = (LinearLayout) findViewById(R.id.llLoader);

        llMain = (LinearLayout) findViewById(R.id.llMain);

        /*JSONObject obj=new JSONObject();
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
        }*/

        profileFunction();
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
        progressDialog.setMessage("Loading...");

        imgHome = (ImageView) findViewById(R.id.imgHome);
        imgBack = (ImageView) findViewById(R.id.imgBack);

        tvGenderTitle = (TextView) findViewById(R.id.tvGenderTitle);
        color = "<font color='#EE0000'>*</font>";
        String gender = "Gender";
        tvGenderTitle.setText(Html.fromHtml(gender + color));

        tvGurdianTitle = (TextView) findViewById(R.id.tvGurdianTitle);
        String gurdian = "Father's/Husband's Name";
        tvGurdianTitle.setText(Html.fromHtml(gurdian + color));


        tvRealationTitle = (TextView) findViewById(R.id.tvRealationTitle);
        String realation = "Relationship ";
        tvRealationTitle.setText(Html.fromHtml(realation + color));


        tvQualificationtitle = (TextView) findViewById(R.id.tvQualificationtitle);
        String qualification = "Qualification";
        tvQualificationtitle.setText(Html.fromHtml(qualification + color));


        tvMartialTitle = (TextView) findViewById(R.id.tvMartialTitle);
        String matial = "Marital status";
        tvMartialTitle.setText(Html.fromHtml(matial + color));

        tvBloodTitle = (TextView) findViewById(R.id.tvBloodTitle);
        String blood = "Blood group";
        tvBloodTitle.setText(Html.fromHtml(blood + color));

        tvMobTitle = (TextView) findViewById(R.id.tvMobTitle);
        String mobile = "Mobile number";
        tvMobTitle.setText(Html.fromHtml(mobile + color));

        tvEmailTitle = (TextView) findViewById(R.id.tvEmailTitle);
        String email = "Email Id";
        tvEmailTitle.setText(email);

        tvPreAddr = (TextView) findViewById(R.id.tvPreAddr);
        String preaddr = "Present Address";
        tvPreAddr.setText(Html.fromHtml(preaddr + color));

        tvPrePin = (TextView) findViewById(R.id.tvPrePin);
        String prepin = "Present Pincode";
        tvPrePin.setText(Html.fromHtml(prepin + color));

        tvDOBTitle = (TextView) findViewById(R.id.tvDOBTitle);
        String dob = "Date of Birth";
        tvDOBTitle.setText(Html.fromHtml(dob + color));

        tvPerAddr = (TextView) findViewById(R.id.tvPerAddr);
        String peradddr = "Permanent Address";
        tvPerAddr.setText(Html.fromHtml(peradddr + color));

        tvPerPin = (TextView) findViewById(R.id.tvPerPin);
        String perpin = "Permanent Pincode";
        tvPerPin.setText(Html.fromHtml(perpin + color));

        tvPreState = (TextView) findViewById(R.id.tvPreState);
        String prestate = "Present State";
        tvPreState.setText(Html.fromHtml(prestate + color));

        tvPreCity = (TextView) findViewById(R.id.tvPreCity);
        String precity = "Present City";
        tvPreCity.setText(Html.fromHtml(precity + color));


        tvPerState = (TextView) findViewById(R.id.tvPerState);
        String perstaete = "Permanent State";
        tvPerState.setText(Html.fromHtml(perstaete + color));


        tvPerCity = (TextView) findViewById(R.id.tvPerCity);
        String percity = "Permanent City";
        tvPerCity.setText(Html.fromHtml(percity + color));


        tvAddaharNo = (TextView) findViewById(R.id.tvAddaharNo);
        String aadaharno = "Aadhar Number";
        tvAddaharNo.setText(Html.fromHtml(aadaharno + color));

        tvAddaharImg = (TextView) findViewById(R.id.tvAddaharImg);
        String aadharimg = "Aadhar Document";
        tvAddaharImg.setText(Html.fromHtml(aadharimg + color));

        etPreAddr = (EditText) findViewById(R.id.etPreAddr);
        etPrePinCode = (EditText) findViewById(R.id.etPrePinCode);
        etBankFirstName = (EditText) findViewById(R.id.etBankFirstName);
        etLastBank = (EditText) findViewById(R.id.etLastBank);
        etAddaharNo = (EditText) findViewById(R.id.etAddaharNo);
        etPerAddr = (EditText) findViewById(R.id.etPerAddr);
        etPerPinCode = (EditText) findViewById(R.id.etPerPinCode);

        spPresentState = (Spinner) findViewById(R.id.spPresentState);
        spPresentCity = (Spinner) findViewById(R.id.spPresentCity);
        spPermanentState = (Spinner) findViewById(R.id.spPermanentState);
        spPermanentCity = (Spinner) findViewById(R.id.spPermanentCity);

        imgCal = (ImageView) findViewById(R.id.imgCal);
        imgCamera = (ImageView) findViewById(R.id.imgCamera);
        imgDocument = (ImageView) findViewById(R.id.imgDocument);

        etSkill = (EditText) findViewById(R.id.etSkill);
        if (!pref.getSkill().equals("")) {
            etSkill.setText(pref.getSkill());
        } else {
            etSkill.setText("--");
        }

        tvView = (TextView) findViewById(R.id.tvView);
        tvSubmit = (TextView) findViewById(R.id.tvSubmit);

        imgForward = (ImageView) findViewById(R.id.imgForward);
        tvSkip = (TextView) findViewById(R.id.tvSkip);
        nessecary = Integer.parseInt(pref.getNesPer());
        Log.d("nessecary", String.valueOf(nessecary));
        Contact = Integer.parseInt(pref.getContactPer());
        FamilyMember = Integer.parseInt(pref.getFamilyPer());
        DocumentUpload = Integer.parseInt(pref.getDocPer());
        Miscellanneous = Integer.parseInt(pref.getMisPer());
        tv1=(TextView)findViewById(R.id.tv1);
        tv2=(TextView)findViewById(R.id.tv2);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                tv1.setVisibility(View.GONE);
                tv2.setVisibility(View.VISIBLE);

            }
        }, 50000);



    }

    public void profileFunction(JSONObject jsonObject) {
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
                                    tvEmplId.setText(AEMEmployeeID);
                                    pref.saveEmpId(AEMEmployeeID);

                                    Code = obj.optString("Code");
                                    tvEmpCode.setText(Code);

                                    Name = obj.optString("Name");
                                    tvEmpName.setText(Name);

                                    DateOfJoining = obj.optString("DOJ");
                                    tvDOJ.setText(DateOfJoining);

                                    Department = obj.optString("Department");
                                    tvDepartment.setText(Department);

                                    Designation = obj.optString("Designation");
                                    tvDesignation.setText(Designation);

                                    Location = obj.optString("Location");
                                    tvLocation.setText(Location);

                                    Sex = obj.optString("Sex").toUpperCase();
                                    Log.d("sexid", Sex);
                                    if (!Sex.equals("")) {
                                        tvGender.setText(Sex);
                                    } else {
                                        tvGender.setText("");
                                    }

                                    DateOfBirth = obj.optString("DateOfBirth");
                                    if (!DateOfBirth.equals("")) {
                                        tvEmpCodeDOB.setText(DateOfBirth);
                                    } else {
                                        tvEmpCodeDOB.setText("");
                                    }

                                    GuardianName = obj.optString("GuardianName");
                                    if (!GuardianName.equals("")) {
                                        etGurdianName.setText(GuardianName);
                                    } else {
                                        etGurdianName.setText("");
                                    }

                                    RelationShip = obj.optString("RelationShip").toUpperCase();
                                    if (!RelationShip.equals("")) {
                                        tvRealtionShip.setText(RelationShip);

                                    } else {
                                        tvRealtionShip.setText("");
                                    }

                                    Qualification = obj.optString("Qualification");
                                    if (!Qualification.equals("")) {
                                        tvQualification.setTag(Qualification);
                                    } else {
                                        tvQualification.setText("");
                                    }

                                    MaritalStatus = obj.optString("MaritalStatus");

                                    if (!MaritalStatus.equals("")) {
                                        tvMarital.setText(MaritalStatus);
                                    } else {
                                        tvMarital.setText("");
                                    }

                                    BloodGroup = obj.optString("BloodGroup");
                                    if (!BloodGroup.equals("")) {
                                        tvBloodGroup.setText(BloodGroup);
                                    } else {
                                        tvBloodGroup.setText("");
                                    }

                                    PermanentAddress = obj.optString("PresentAddress");
                                   /* if (!PermanentAddress.equals("")) {
                                        tvParAddr.setText(PermanentAddress);
                                    } else {
                                        tvParAddr.setText("");
                                    }
*/

                                    Mobile = obj.optString("Mobile");
                                    if (!Mobile.equals("")) {
                                        etMobNumber.setText(Mobile);
                                    } else {
                                        etMobNumber.setText("");
                                    }

                                    EmailID = obj.optString("EmailID");
                                    if (!EmailID.equals("")) {
                                        etEmailId.setText(EmailID);
                                    } else {
                                        etEmailId.setText("");
                                    }

                                    Phone = obj.optString("Phone");
                                    if (!Phone.equals("")) {
                                        etPhnNumber.setText(Phone);
                                    } else {
                                        etPhnNumber.setText("");
                                    }
                                    AEMClientName = obj.optString("AEMClientName");
                                    tvComName.setText(AEMClientName);
                                    String ESINumber = obj.optString("ESINumber");
                                    if (!ESINumber.equals("")) {
                                        etESI.setText(ESINumber);
                                    } else {
                                        etESI.setText("");
                                    }
                                    String UanNo = obj.optString("UANNumber");
                                    if (!UanNo.equals("")) {
                                        etUAN.setText(UanNo);
                                    } else {
                                        etUAN.setText("");
                                    }
                                    PresentCity = obj.optString("PresentCity");

                                    String PresentPincode = obj.optString("PresentPincode");
                                    Log.d("rikusaga", PresentPincode);
                                    etPrePinCode.setText(PresentPincode);


                                    PresentAddress = obj.optString("PresentAddress");
                                    etPreAddr.setText(PresentAddress);

                                    FirstNameAsperBank = obj.optString("FirstNameAsperBank");
                                    if (FirstNameAsperBank.equals("null")) {
                                        etBankFirstName.setText("");
                                    } else {
                                        etBankFirstName.setText(FirstNameAsperBank);
                                    }

                                    LastNameAsperBank = obj.optString("LastNameAsperBank");
                                    if (LastNameAsperBank.equals("null")) {
                                        etLastBank.setText("");
                                    } else {
                                        etLastBank.setText(LastNameAsperBank);
                                    }

                                    String AadharCard = obj.optString("AadharNo");
                                    etAddaharNo.setText(AadharCard);

                                    String PermanentAddress = obj.optString("PermanentAddress");
                                    etPerAddr.setText(PermanentAddress);

                                    String PermanentPinCode = obj.optString("PermanentPinCode");
                                    if (PermanentPinCode.equals("null")) {
                                        etPerPinCode.setText("");
                                    } else {
                                        etPerPinCode.setText(PermanentPinCode);
                                    }

                                    PermanentState = obj.optString("PermanentState");
                                    PermanentCity = obj.optString("PermanentCity");
                                    PresentState = obj.optString("PresentState");

                                    String PAN = obj.optString("PAN");
                                    Log.d("pan", PAN);
                                    String IFSCode = obj.optString("IFSCode");
                                    Log.d("ifsccode", IFSCode);
                                    pref.saveIFSC(IFSCode);
                                    String FirstNameAsperBank = obj.optString("FirstNameAsperBank");
                                    Log.d("fname", FirstNameAsperBank);
                                    pref.saveBFName(FirstNameAsperBank);
                                    String LastNameAsperBank = obj.optString("LastNameAsperBank");
                                    pref.saveBLName(LastNameAsperBank);
                                    Log.d("lname", LastNameAsperBank);
                                    String MemberName1 = obj.optString("MemberName1");
                                    Log.d("m1name", MemberName1);
                                    pref.saveM1Name(MemberName1);
                                    String MemberRelationship1 = obj.optString("MemberRelationship1");
                                    Log.d("MemberRelationship1", MemberRelationship1);
                                    String MemberAadhar1 = obj.optString("MemberAadhar1");
                                    Log.d("MemberAadhar1", MemberAadhar1);
                                    pref.saveM1Aadahar(MemberAadhar1);
                                    String MemberDOB1 = obj.optString("MemberDOB1");
                                    pref.saveM1DOB(MemberDOB1);
                                    Log.d("MemberDOB1", MemberDOB1);
                                    String MemberName2 = obj.optString("MemberName2");
                                    Log.d("MemberName2", MemberName2);
                                    pref.saveM2Name(MemberName2);
                                    String MemberRelationship2 = obj.optString("MemberRelationship2");
                                    Log.d("MemberRelationship2", MemberRelationship2);
                                    String MemberAadhar2 = obj.optString("MemberAadhar2");
                                    Log.d("MemberAadhar2", MemberAadhar2);
                                    pref.saveM2Aadahar(MemberAadhar2);
                                    String MemberDOB2 = obj.optString("MemberDOB2");
                                    Log.d("MemberDOB2", MemberDOB2);
                                    pref.saveM2DOB(MemberDOB2);
                                    String MemberName3 = obj.optString("MemberName3");
                                    Log.d("MemberName3", MemberName3);
                                    pref.saveM3Name(MemberName3);
                                    String MemberRelationship3 = obj.optString("MemberRelationship3");
                                    Log.d("MemberRelationship3", MemberRelationship3);
                                    String MemberAadhar3 = obj.optString("MemberAadhar3");
                                    Log.d("MemberAadhar3", MemberAadhar3);
                                    pref.saveM3Aadahar(MemberAadhar3);
                                    String MemberDOB3 = obj.optString("MemberDOB3");
                                    Log.d("MemberDOB3", MemberDOB3);
                                    pref.saveM3DOB(MemberDOB3);
                                    String BankName = obj.optString("BankName");
                                    pref.saveBankName(BankName);
                                    String AccountNumber = obj.optString("AccountNumber");
                                    pref.saveAccNumber(AccountNumber);
                                }
                                llMain.setVisibility(View.GONE);
                                llLoader.setVisibility(View.VISIBLE);
                                setQualification();
                            } else {
                                
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TempProfileActivity.this, "Something went to wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        errflag = 1;
                        showInternetDialog();
                    }
                });
    }

    public void profileFunction() {
        String surl = AppData.url+"gcl_KYC?AEMConsultantID=" + pref.getEmpConId() + "&AEMClientID=" + pref.getEmpClintId() + "&AEMClientOfficeID=" + pref.getEmpClintOffId() + "&AEMEmployeeID=" + pref.getMasterId() + "&SecurityCode=" + pref.getSecurityCode() + "&WorkingStatus=1&CurrentPage=0";
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
                                    tvEmplId.setText(AEMEmployeeID);
                                    pref.saveEmpId(AEMEmployeeID);

                                    Code = obj.optString("Code");
                                    tvEmpCode.setText(Code);

                                    Name = obj.optString("Name");
                                    tvEmpName.setText(Name);

                                    DateOfJoining = obj.optString("DateOfJoining");
                                    tvDOJ.setText(DateOfJoining);

                                    Department = obj.optString("Department");
                                    tvDepartment.setText(Department);

                                    Designation = obj.optString("Designation");
                                    tvDesignation.setText(Designation);

                                    Location = obj.optString("Location");
                                    tvLocation.setText(Location);

                                    Sex = obj.optString("Sex").toUpperCase();
                                    Log.d("sexid", Sex);
                                    if (!Sex.equals("")) {
                                        tvGender.setText(Sex);
                                    } else {
                                        tvGender.setText("");
                                    }

                                    DateOfBirth = obj.optString("DateOfBirth");
                                    if (!DateOfBirth.equals("")) {
                                        tvEmpCodeDOB.setText(DateOfBirth);
                                    } else {
                                        tvEmpCodeDOB.setText("");
                                    }

                                    GuardianName = obj.optString("GuardianName");
                                    if (!GuardianName.equals("")) {
                                        etGurdianName.setText(GuardianName);
                                    } else {
                                        etGurdianName.setText("");
                                    }

                                    RelationShip = obj.optString("RelationShip").toUpperCase();
                                    if (!RelationShip.equals("")) {
                                        tvRealtionShip.setText(RelationShip);

                                    } else {
                                        tvRealtionShip.setText("");
                                    }

                                    Qualification = obj.optString("Qualification");
                                    if (!Qualification.equals("")) {
                                        tvQualification.setTag(Qualification);
                                    } else {
                                        tvQualification.setText("");
                                    }

                                    MaritalStatus = obj.optString("MaritalStatus");

                                    if (!MaritalStatus.equals("")) {
                                        tvMarital.setText(MaritalStatus);
                                    } else {
                                        tvMarital.setText("");
                                    }

                                    BloodGroup = obj.optString("BloodGroup");
                                    if (!BloodGroup.equals("")) {
                                        tvBloodGroup.setText(BloodGroup);
                                    } else {
                                        tvBloodGroup.setText("");
                                    }

                                    PermanentAddress = obj.optString("PresentAddress");
                                   /* if (!PermanentAddress.equals("")) {
                                        tvParAddr.setText(PermanentAddress);
                                    } else {
                                        tvParAddr.setText("");
                                    }
*/

                                    Mobile = obj.optString("Mobile");
                                    if (!Mobile.equals("")) {
                                        etMobNumber.setText(Mobile);
                                    } else {
                                        etMobNumber.setText("");
                                    }

                                    EmailID = obj.optString("EmailID");
                                    if (!EmailID.equals("")) {
                                        etEmailId.setText(EmailID);
                                    } else {
                                        etEmailId.setText("");
                                    }

                                    Phone = obj.optString("Phone");
                                    if (!Phone.equals("")) {
                                        etPhnNumber.setText(Phone);
                                    } else {
                                        etPhnNumber.setText("");
                                    }
                                    AEMClientName = obj.optString("AEMClientName");
                                    tvComName.setText(AEMClientName);
                                    String ESINumber = obj.optString("ESINumber");
                                    if (!ESINumber.equals("")) {
                                        etESI.setText(ESINumber);
                                    } else {
                                        etESI.setText("");
                                    }
                                    String UanNo = obj.optString("UANNumber");
                                    if (!UanNo.equals("")) {
                                        etUAN.setText(UanNo);
                                    } else {
                                        etUAN.setText("");
                                    }
                                    PresentCity = obj.optString("PresentCity");

                                    String PresentPincode = obj.optString("PresentPincode");
                                    Log.d("rikusaga", PresentPincode);
                                    etPrePinCode.setText(PresentPincode);


                                    PresentAddress = obj.optString("PresentAddress");
                                    etPreAddr.setText(PresentAddress);

                                    FirstNameAsperBank = obj.optString("FirstNameAsperBank");
                                    if (FirstNameAsperBank.equals("null")) {
                                        etBankFirstName.setText("");
                                    } else {
                                        etBankFirstName.setText(FirstNameAsperBank);
                                    }

                                    LastNameAsperBank = obj.optString("LastNameAsperBank");
                                    if (LastNameAsperBank.equals("null")) {
                                        etLastBank.setText("");
                                    } else {
                                        etLastBank.setText(LastNameAsperBank);
                                    }

                                    String AadharCard = obj.optString("AadharCard");
                                    etAddaharNo.setText(AadharCard);

                                    String PermanentAddress = obj.optString("PermanentAddress");
                                    etPerAddr.setText(PermanentAddress);

                                    String PermanentPinCode = obj.optString("PermanentPinCode");
                                    if (PermanentPinCode.equals("null")) {
                                        etPerPinCode.setText("");
                                    } else {
                                        etPerPinCode.setText(PermanentPinCode);
                                    }

                                    PermanentState = obj.optString("PermanentState");
                                    PermanentCity = obj.optString("PermanentCity");
                                    PresentState = obj.optString("PresentState");

                                    String PAN = obj.optString("PAN");
                                    Log.d("pan", PAN);
                                    String IFSCode = obj.optString("IFSCode");
                                    Log.d("ifsccode", IFSCode);
                                    pref.saveIFSC(IFSCode);
                                    String FirstNameAsperBank = obj.optString("FirstNameAsperBank");
                                    Log.d("fname", FirstNameAsperBank);
                                    pref.saveBFName(FirstNameAsperBank);
                                    String LastNameAsperBank = obj.optString("LastNameAsperBank");
                                    pref.saveBLName(LastNameAsperBank);
                                    Log.d("lname", LastNameAsperBank);
                                    String MemberName1 = obj.optString("MemberName1");
                                    Log.d("m1name", MemberName1);
                                    pref.saveM1Name(MemberName1);
                                    String MemberRelationship1 = obj.optString("MemberRelationship1");
                                    Log.d("MemberRelationship1", MemberRelationship1);
                                    String MemberAadhar1 = obj.optString("MemberAadhar1");
                                    Log.d("MemberAadhar1", MemberAadhar1);
                                    pref.saveM1Aadahar(MemberAadhar1);
                                    String MemberDOB1 = obj.optString("MemberDOB1");
                                    pref.saveM1DOB(MemberDOB1);
                                    Log.d("MemberDOB1", MemberDOB1);
                                    String MemberName2 = obj.optString("MemberName2");
                                    Log.d("MemberName2", MemberName2);
                                    pref.saveM2Name(MemberName2);
                                    String MemberRelationship2 = obj.optString("MemberRelationship2");
                                    Log.d("MemberRelationship2", MemberRelationship2);
                                    String MemberAadhar2 = obj.optString("MemberAadhar2");
                                    Log.d("MemberAadhar2", MemberAadhar2);
                                    pref.saveM2Aadahar(MemberAadhar2);
                                    String MemberDOB2 = obj.optString("MemberDOB2");
                                    Log.d("MemberDOB2", MemberDOB2);
                                    pref.saveM2DOB(MemberDOB2);
                                    String MemberName3 = obj.optString("MemberName3");
                                    Log.d("MemberName3", MemberName3);
                                    pref.saveM3Name(MemberName3);
                                    String MemberRelationship3 = obj.optString("MemberRelationship3");
                                    Log.d("MemberRelationship3", MemberRelationship3);
                                    String MemberAadhar3 = obj.optString("MemberAadhar3");
                                    Log.d("MemberAadhar3", MemberAadhar3);
                                    pref.saveM3Aadahar(MemberAadhar3);
                                    String MemberDOB3 = obj.optString("MemberDOB3");
                                    Log.d("MemberDOB3", MemberDOB3);
                                    pref.saveM3DOB(MemberDOB3);
                                    String BankName = obj.optString("BankName");
                                    pref.saveBankName(BankName);
                                    String AccountNumber = obj.optString("AccountNumber");
                                    pref.saveAccNumber(AccountNumber);


                                }
                                llMain.setVisibility(View.GONE);
                                llLoader.setVisibility(View.VISIBLE);
                                setQualification();
                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TempProfileActivity.this, "Something went to wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Toast.makeText(TempProfileActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
                errflag = 1;
                showInternetDialog();
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");

    }

    private void setQualification() {

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
                                    qualivalue = obj.optString("value");
                                    qualiid = obj.optString("id");
                                    qualification.add(qualivalue);
                                    MainDocModule mainDocModule = new MainDocModule(qualiid, qualivalue);
                                    mainQualification.add(mainDocModule);

                                }
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (TempProfileActivity.this, android.R.layout.simple_spinner_item,
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
                            Toast.makeText(TempProfileActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
                errflag=2;
                showInternetDialog();
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");


    }

    private void setMartial() {
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
                                    qualivalue = obj.optString("value");
                                    qualiid = obj.optString("id");
                                    martial.add(qualivalue);
                                    MainDocModule mainDocModule = new MainDocModule(qualiid, qualivalue);
                                    mainMartial.add(mainDocModule);

                                }
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (TempProfileActivity.this, android.R.layout.simple_spinner_item,
                                                martial); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spMartial.setAdapter(spinnerArrayAdapter);
                                int index = martial.indexOf(MaritalStatus);
                                Log.d("indexr", String.valueOf(index));
                                spMartial.setSelection(index);
                                setGender();
                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TempProfileActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);

                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
                errflag=3;
                showInternetDialog();
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");


    }

    private void setGender() {

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
                                        (TempProfileActivity.this, android.R.layout.simple_spinner_item,
                                                gender); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spGender.setAdapter(spinnerArrayAdapter);
                                binding.spESICGender.setAdapter(spinnerArrayAdapter);
                                int index = gender.indexOf(Sex);
                                Log.d("indexr", String.valueOf(index));
                                spGender.setSelection(index);
                                setRealation();

                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TempProfileActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);

                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
                errflag=4;
                showInternetDialog();
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");


    }

    private void setRealation() {
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        realation.add("Father");
        realation.add("Husband");
        mainRealation.add(new MainDocModule("REL00000001", "Father"));
        mainRealation.add(new MainDocModule("REL00000008", "Husband"));

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (TempProfileActivity.this, android.R.layout.simple_spinner_item,
                        realation); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRealation.setAdapter(spinnerArrayAdapter);
        int index = realation.indexOf(RelationShip);
        Log.d("indexr", String.valueOf(index));
        spRealation.setSelection(1);


        residing.add("Please Select");
        residing.add("Yes");
        residing.add("No");


        ArrayAdapter<String> residingspinnerArrayAdapter = new ArrayAdapter<String>
                (TempProfileActivity.this, android.R.layout.simple_spinner_item,
                        residing); //selected item will look like a spinner set from XML
        residingspinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spResiding.setAdapter(residingspinnerArrayAdapter);


        uanPercentage.add("Please Select");
        uanPercentage.add("100");
        uanPercentage.add("75");
        uanPercentage.add("50");
        uanPercentage.add("25");



        ArrayAdapter<String> uanspinnerArrayAdapter = new ArrayAdapter<String>
                (TempProfileActivity.this, android.R.layout.simple_spinner_item,
                        uanPercentage); //selected item will look like a spinner set from XML
        uanspinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spTotalAmt.setAdapter(uanspinnerArrayAdapter);


        setNomineeRelation();


    }


    private void setNomineeRelation() {

        String surl = AppData.url+"gcl_CommonDDL?ddltype=7&id1=0&id2=0&id3=0&SecurityCode=" + pref.getSecurityCode();
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        llLoader.setVisibility(View.VISIBLE);
                        llMain.setVisibility(View.GONE);
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
                                        (TempProfileActivity.this, android.R.layout.simple_spinner_item,
                                                realation); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                binding.spESICRealation.setAdapter(spinnerArrayAdapter);
                                binding.spUANRealation.setAdapter(spinnerArrayAdapter);

                                setBlood();

                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TempProfileActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);

                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
                errflag=5;
                showInternetDialog();
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");


    }

    private void setBlood() {

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
                                        (TempProfileActivity.this, android.R.layout.simple_spinner_item,
                                                blood); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spBloodGrp.setAdapter(spinnerArrayAdapter);
                                int index = blood.indexOf(BloodGroup);
                                Log.d("indexr", String.valueOf(index));
                                spBloodGrp.setSelection(index);
                                setPerCity();

                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TempProfileActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);

                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
                errflag=5;
                showInternetDialog();
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");


    }

    private void onClick() {

        tvView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TempProfileActivity.this, DocumentReportActivity.class);
                startActivity(intent);
                finish();
            }
        });
        spQualification.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String qualification = mainQualification.get(position).getDocumentType();
                tvQualification.setText(qualification);

                education = mainQualification.get(position).getDocumentType();
                Log.d("qualification", education);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spPermanentCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                permanentcity = mainPerCity.get(position).getDocID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spPermanentState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                permanentstate = mainPerState.get(position).getDocID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spPresentState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presentstate = mainPerState.get(position).getDocID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spResiding.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>0){
                    residingIP = residing.get(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spTotalAmt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>0){
                    pfPercantage = uanPercentage.get(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TempProfileActivity.this, KYCFamilyActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        spMartial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                martialstatus = mainMartial.get(position).getDocID();
                Log.d("martial", martialstatus);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sexGender = mainGender.get(position).getDocID();
                Log.d("sexgender", sexGender);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.spESICGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                esicGender = mainGender.get(position).getDocID();
                Log.d("sexgender", sexGender);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.spESICRealation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>0){
                    esicRltionshp = mainRealation.get(position).getDocID();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.spUANRealation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>0){
                    uanRltionshp = mainRealation.get(position).getDocID();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        etPerAddr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etPerAddr.getText().toString().length() > 0) {
                    peraddr = etPerAddr.getText().toString();
                }

            }
        });

        etPreAddr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etPreAddr.getText().toString().length() > 0) {
                    preaddr = etPreAddr.getText().toString();
                }

            }
        });
        etPerPinCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etPerPinCode.getText().toString().length() > 0) {
                    perpin = etPerPinCode.getText().toString();
                }

            }
        });
        etPrePinCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etPrePinCode.getText().toString().length() > 0) {
                    prepin = etPrePinCode.getText().toString();
                }

            }
        });

        spRealation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                realationship = mainRealation.get(position).getDocumentType();
                Log.d("realation", realationship);
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        etAddaharNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etAddaharNo.getText().toString().length() > 0) {
                    aadharno = etAddaharNo.getText().toString();
                }

            }
        });
        etSkill.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etSkill.getText().toString().length() > 0) {
                    pref.saveSkill(etSkill.getText().toString());
                }

            }
        });

        etUAN.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etUAN.getText().toString().length() > 0) {
                    uan = etUAN.getText().toString();
                }

            }
        });

        etPhnNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etPhnNumber.getText().toString().length() > 0) {
                    phnnumber = etPhnNumber.getText().toString();
                }

            }
        });


        spBloodGrp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                bloodgrp = mainBlood.get(position).getDocID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spPresentCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presentcity = mainPreCity.get(position).getDocID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCameraDialog();
            }
        });

        imgCal.setOnClickListener(new View.OnClickListener() {
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


                final DatePickerDialog dialog = new DatePickerDialog(TempProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                        DateOfBirth = d + " " + month + " " + y;

                        tvEmpCodeDOB.setText(DateOfBirth);


                    }
                }, dyear, dmonth, dday);
                dialog.getDatePicker().setMaxDate((long) (System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 365.25 * 18)));
                dialog.getDatePicker().setMinDate((long) (System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 365.25 * 80)));
                dialog.show();

            }
        });


        binding.imgESICCal.setOnClickListener(new View.OnClickListener() {
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


                final DatePickerDialog dialog = new DatePickerDialog(TempProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                        esicDOB = d + " " + month + " " + y;
                        uanDOB=d + " " + month + " " + y;

                        binding.tvESICDOB.setText(esicDOB);
                        binding.tvUANDOB.setText(uanDOB);


                    }
                }, dyear, dmonth, dday);
                dialog.getDatePicker().setMaxDate((long) (System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 365.25 * 18)));
                dialog.getDatePicker().setMinDate((long) (System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 365.25 * 80)));
                dialog.show();

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


                final DatePickerDialog dialog = new DatePickerDialog(TempProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                        uanDOB = d + " " + month + " " + y;

                        binding.tvUANDOB.setText(uanDOB);


                    }
                }, dyear, dmonth, dday);
                dialog.getDatePicker().setMaxDate((long) (System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 365.25 * 18)));
                dialog.getDatePicker().setMinDate((long) (System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 365.25 * 90)));
                dialog.show();

            }
        });


        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                esi = etESI.getText().toString();
                if (!preaddr.equals("")) {
                    if (!prepin.equals("")) {
                        if (etGurdianName.getText().toString().length() > 0) {


                                    if (etPerAddr.getText().toString().length() > 0) {
                                        if (etPrePinCode.getText().toString().length() > 0) {
                                            if (etESI.getText().toString().equals("") || etESI.getText().toString().length() > 9) {
                                                if (etUAN.getText().toString().equals("") || etUAN.getText().toString().length() > 11) {
                                                    if (etPhnNumber.getText().toString().equals("") || etPhnNumber.getText().toString().length() > 9) {

                                                        JSONObject mainobject=new JSONObject();
                                                        try {
                                                            mainobject.put("DbOperation","1");
                                                            mainobject.put("SecurityCode",pref.getSecurityCode());
                                                            JSONObject personalOBJ=new JSONObject();
                                                            personalOBJ.put("AEMEMPLOYEEID",AEMEmployeeID);
                                                            personalOBJ.put("Sex",sexGender);
                                                            personalOBJ.put("GuardianName",etGurdianName.getText().toString());
                                                            personalOBJ.put("RelationShip",realationship);
                                                            personalOBJ.put("BloodGroup",bloodgrp);
                                                            personalOBJ.put("DateOfBirth",DateOfBirth);
                                                            personalOBJ.put("Qualification",education);
                                                            personalOBJ.put("MaritalStatus",martialstatus);
                                                            mainobject.put("PersonalDetails",personalOBJ);
                                                            uploadOfficalDetails(mainobject);
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "Please enter valid Phone number", Toast.LENGTH_LONG).show();
                                                    }
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Please enter valid UAN number", Toast.LENGTH_LONG).show();

                                                }
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Please enter valid ESI number", Toast.LENGTH_LONG).show();

                                            }


                                        } else {
                                            Toast.makeText(getApplicationContext(), "Please update your permanent pincode", Toast.LENGTH_LONG).show();

                                        }

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Please update your permanent address", Toast.LENGTH_LONG).show();

                                    }





                        } else {
                            Toast.makeText(getApplicationContext(), "Please update your fathers or husband name", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Please update your present pincode", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Please update your present address", Toast.LENGTH_LONG).show();
                }

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
                Intent intent = new Intent(TempProfileActivity.this, TempDashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }


    private void updateProfile() {
        if (etESI.getText().toString().equals("")) {
            esi = "";
        } else {
            esi = etESI.getText().toString();
        }

        if (etUAN.getText().toString().equals("")) {
            uan = "";
        } else {
            uan = etUAN.getText().toString();
        }

        if (etPhnNumber.getText().equals("")) {
            phnnumber = "";
        } else {
            phnnumber = etPhnNumber.getText().toString();
        }
        progressDialog.show();
        String doucmentid = "002";
        // file = new File(imageUri.getPath());
        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), compressedImageFile);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", compressedImageFile.getName(), mFile);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), compressedImageFile.getName());
        //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        Call<UploadObject> fileUpload = uploadService.updatekyc(AEMEmployeeID, Code, DateOfJoining, Department, Designation, Location, esi, uan, sexGender, etGurdianName.getText().toString(), realationship, tvEmpCodeDOB.getText().toString(), education, martialstatus, bloodgrp, peraddr, permanentcity, perpin, preaddr, presentcity, prepin, phnnumber, etMobNumber.getText().toString(), etEmailId.getText().toString(), AEMEmployeeID, pref.getSecurityCode(), aadharno, doucmentid, fileToUpload);
        fileUpload.enqueue(new Callback<UploadObject>() {
            @Override
            public void onResponse(Call<UploadObject> call, retrofit2.Response<UploadObject> response) {
                progressDialog.dismiss();
                UploadObject extraWorkingDayModel = response.body();
                if (extraWorkingDayModel.isResponseStatus()) {
                    //  Toast.makeText(getApplicationContext(), extraWorkingDayModel.getResponseText(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TempProfileActivity.this, TempPanActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);


                } else {
                    Toast.makeText(getApplicationContext(), extraWorkingDayModel.getResponseText(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UploadObject> call, Throwable t) {
                progressDialog.dismiss();

                Log.e("reterror", "Error " + t.getMessage());
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();

            }

        });

    }


    private void completedialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TempProfileActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_prflecomp, null);
        dialogBuilder.setView(dialogView);

        PieChart pieChart = (PieChart) dialogView.findViewById(R.id.pie_chart);
        data = new ArrayList<>();
        data.add(new ChartData(nessecary + "%", nessecary));
        pieChart.setChartData(data);
        pieChart.partitionWithPercent(true);


        PieChart pieChartdoc = (PieChart) dialogView.findViewById(R.id.pie_chart_Doc);
        docdata = new ArrayList<>();
        docdata.add(new ChartData(DocumentUpload + "%", DocumentUpload));
        pieChartdoc.setChartData(docdata);
        pieChartdoc.partitionWithPercent(true);


        PieChart pieChartfamily = (PieChart) dialogView.findViewById(R.id.pie_chart_nominee);
        familydata = new ArrayList<>();
        familydata.add(new ChartData(FamilyMember + "%", FamilyMember));
        pieChartfamily.setChartData(familydata);
        pieChartfamily.partitionWithPercent(true);


        PieChart pieChartcontact = (PieChart) dialogView.findViewById(R.id.pie_chart_contact);
        conatactdata = new ArrayList<>();
        conatactdata.add(new ChartData(Contact + "%", Contact));
        pieChartcontact.setChartData(conatactdata);
        pieChartcontact.partitionWithPercent(true);


        LinearLayout llOk = (LinearLayout) dialogView.findViewById(R.id.llOk);
        llOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerDialog1.dismiss();
            }
        });


        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }

    private void check() {
        if (etESI.getText().toString().length() > 11) {
            updateProfile();
        } else {
            Toast.makeText(getApplicationContext(), "Please enter valid ESI number", Toast.LENGTH_LONG).show();
        }
    }

    private void setPerCity() {

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
                                        (TempProfileActivity.this, android.R.layout.simple_spinner_item,
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
                            Toast.makeText(TempProfileActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                errflag=6;
                showInternetDialog();

                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");


    }

    private void serPreCity() {

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
                                        (TempProfileActivity.this, android.R.layout.simple_spinner_item,
                                                precity); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spPresentCity.setAdapter(spinnerArrayAdapter);
                                int index = percity.indexOf(PresentCity);
                                Log.d("indexr", String.valueOf(index));
                                spPresentCity.setSelection(index);
                                setprestate();

                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TempProfileActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                errflag=7;
                showInternetDialog();

                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");


    }

    private void setprestate() {

        String surl = AppData.url+"gcl_CommonDDL?ddltype=3&id1=0&id2=0&id3=0&SecurityCode=" + pref.getSecurityCode();
        Log.d("deptname", surl);
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        llLoader.setVisibility(View.VISIBLE);
                        llMain.setVisibility(View.GONE);
                        mainPreState.clear();
                        prestate.clear();


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
                                    String deptvalue = obj.optString("value");
                                    String id = obj.optString("id");
                                    prestate.add(deptvalue);

                                    MainDocModule mainDocModule = new MainDocModule(id, deptvalue);
                                    mainPreState.add(mainDocModule);
                                    // clientname.add(value);
                                }
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (TempProfileActivity.this, android.R.layout.simple_spinner_item,
                                                prestate); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spPresentState.setAdapter(spinnerArrayAdapter);
                                int index = prestate.indexOf(PresentState);
                                Log.d("indexr", String.valueOf(index));
                                spPresentState.setSelection(index);
                                setperstate();


                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TempProfileActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                errflag=8;
                showInternetDialog();

                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");


    }

    private void setperstate() {

        String surl = AppData.url+"gcl_CommonDDL?ddltype=3&id1=0&id2=0&id3=0&SecurityCode=" + pref.getSecurityCode();
        Log.d("deptname", surl);
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        llLoader.setVisibility(View.GONE);
                        llMain.setVisibility(View.VISIBLE);
                        mainPerState.clear();
                        perstate.clear();


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
                                    String deptvalue = obj.optString("value");
                                    String id = obj.optString("id");
                                    perstate.add(deptvalue);
                                    MainDocModule mainDocModule = new MainDocModule(id, deptvalue);
                                    mainPerState.add(mainDocModule);
                                    // clientname.add(value);
                                }
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (TempProfileActivity.this, android.R.layout.simple_spinner_item,
                                                perstate); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spPermanentState.setAdapter(spinnerArrayAdapter);
                                int index = perstate.indexOf(PermanentState);
                                Log.d("indexr", String.valueOf(index));
                                spPermanentState.setSelection(index);


                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TempProfileActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                errflag=9;
                showInternetDialog();

                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");


    }

    private void cameraIntent() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Profile Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_REQUEST:

                if (resultCode == Activity.RESULT_OK) {
                    try {
                        try {
                            String imageurl = /*"file://" +*/ getRealPathFromURI(imageUri);
                            file = new File(imageurl);
                            compressedImageFile = new Compressor(this).compressToFile(file);
                            Log.d("imageSixw", String.valueOf(getReadableFileSize(compressedImageFile.length())));
                            BitmapFactory.Options o = new BitmapFactory.Options();
                            o.inSampleSize = 2;
                            Bitmap bm = cropToSquare(BitmapFactory.decodeFile(imageurl, o));
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.PNG, 10, baos); //bm is the bitmap object
                            byte[] b = baos.toByteArray();
                            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                            imgDocument.setImageBitmap(bm);
                            Log.d("images", encodedImage);
                            flag = 1;
                            alerDialog3.dismiss();


                            // _pref.saveImage(encodedImage);
                            //saveImage(encodedImage);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }

                }
                break;

            case LongImageCameraActivity.LONG_IMAGE_RESULT_CODE:


                if (resultCode == RESULT_OK && requestCode == LongImageCameraActivity.LONG_IMAGE_RESULT_CODE) {
                    String imageFileName = data.getStringExtra(LongImageCameraActivity.IMAGE_PATH_KEY);
                    Log.d("imageFileName", imageFileName);
                    Bitmap d = BitmapFactory.decodeFile(imageFileName);
                    int newHeight = (int) (d.getHeight() * (512.0 / d.getWidth()));
                    Bitmap putImage = Bitmap.createScaledBitmap(d, 512, newHeight, true);
                    imgDocument.setImageBitmap(putImage);
                    flag = 1;
                    File pictureFile = (File) data.getExtras().get("picture");
                    Log.d("fjjgk", pictureFile.toString());
                    try {
                        compressedImageFile = new Compressor(this).compressToFile(pictureFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d("imageSixw", String.valueOf(getReadableFileSize(pictureFile.length())));

                    alerDialog3.dismiss();


                }
                break;


        }

    }


    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    public static Bitmap cropToSquare(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = (height > width) ? width : height;
        int newHeight = (height > width) ? height - (height - width) : height;
        int cropW = (width - height) / 2;
        cropW = (cropW < 0) ? 0 : cropW;
        int cropH = (height - width) / 2;
        cropH = (cropH < 0) ? 0 : cropH;
        Bitmap cropImg = Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);

        return cropImg;
    }

    public String getReadableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }


    private void openCameraDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TempProfileActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_cameraimage, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llSingle = (LinearLayout) dialogView.findViewById(R.id.llSingle);
        llSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraIntent();

            }
        });

        LinearLayout llMultiple = (LinearLayout) dialogView.findViewById(R.id.llMultiple);
        llMultiple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LongImageCameraActivity.launch(TempProfileActivity.this);

            }
        });


        alerDialog3 = dialogBuilder.create();
        alerDialog3.setCancelable(true);
        Window window = alerDialog3.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog3.show();
    }

    private void showInternetDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TempProfileActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_invaliddate, null);
        dialogBuilder.setView(dialogView);
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (errflag == 1) {
                    profileFunction();
                    alert1.dismiss();
                }else if (errflag==2){
                    setQualification();
                    alert1.dismiss();
                }
                else if (errflag==3){
                    setMartial();
                    alert1.dismiss();
                }
                else if (errflag==4){
                    setGender();
                    alert1.dismiss();
                }
                else if (errflag==5){
                    setBlood();
                    alert1.dismiss();
                }
                else if (errflag==6){
                    setPerCity();
                    alert1.dismiss();
                }

                else if (errflag==7){
                    serPreCity();
                    alert1.dismiss();
                }else if (errflag==8){
                    setprestate();
                    alert1.dismiss();
                }else if(errflag==9){
                    setperstate();
                    alert1.dismiss();
                }
            }
        });
        TextView tvInvalidDialog = (TextView) dialogView.findViewById(R.id.tvInvalidDialog);
        tvInvalidDialog.setText("Something went wrong.Please try again");
        alert1 = dialogBuilder.create();
        alert1.setCancelable(true);
        Window window = alert1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alert1.show();


    }


    private void uploadOfficalDetails(JSONObject jsonObject) {
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


                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            pd.show();
                            JSONObject mainobject=new JSONObject();
                            try {
                                mainobject.put("DbOperation","2");
                                mainobject.put("SecurityCode",pref.getSecurityCode());
                                JSONObject innerobj=new JSONObject();
                                innerobj.put("AEMEMPLOYEEID",AEMEmployeeID);
                                innerobj.put("PermanentAddress",etPerAddr.getText().toString());
                                innerobj.put("PermanentStateID",permanentstate);
                                innerobj.put("PermanentCityID",permanentcity);
                                innerobj.put("PermanentPinCode",etPerPinCode.getText().toString());
                                innerobj.put("PresentAddress",etPreAddr.getText().toString());
                                innerobj.put("PresentStateID",presentstate);
                                innerobj.put("PresentCityID",presentcity);
                                innerobj.put("PresentPincode",etPrePinCode.getText().toString());
                                innerobj.put("Phone",binding.etPhnNumber.getText().toString());
                                innerobj.put("Mobile",etMobNumber.getText().toString());
                                innerobj.put("EmailID",etEmailId.getText().toString());
                                innerobj.put("RefContact",binding.etRefNumber.getText().toString());
                                mainobject.put("ContactDetails",innerobj);
                                uploadContactDetails(mainobject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(TempProfileActivity.this,"Personal Details has been updated Successfully",Toast.LENGTH_LONG).show();

                        }else {
                            pd.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError error) {

                            Intent intent=new Intent(TempProfileActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();

                    }
                });
    }


    private void uploadContactDetails(JSONObject jsonObject) {
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


                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            pd.show();
                            JSONObject mainobject=new JSONObject();
                            try {
                                mainobject.put("DbOperation","3");
                                mainobject.put("SecurityCode",pref.getSecurityCode());
                                JSONObject innerobj=new JSONObject();
                                innerobj.put("AEMEMPLOYEEID",AEMEmployeeID);
                                innerobj.put("OldESINo",etESI.getText().toString());
                                innerobj.put("NomineeName",binding.etESINominee.getText().toString());
                                innerobj.put("NomineeDOB",esicDOB);
                                innerobj.put("NomineeGender",esicGender);
                                innerobj.put("NomineeRelation",esicRltionshp);
                                innerobj.put("ResidingIP",residingIP);
                                mainobject.put("OLDESICDetails",innerobj);
                                uploadesicDetails(mainobject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(TempProfileActivity.this,"Contact Details has been updated Successfully",Toast.LENGTH_LONG).show();

                        }else {
                            pd.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError error) {

                            Intent intent=new Intent(TempProfileActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();

                    }
                });
    }

    private void uploadesicDetails(JSONObject jsonObject) {
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


                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            pd.show();

                            JSONObject mainobject=new JSONObject();
                            try {
                                mainobject.put("DbOperation","4");
                                mainobject.put("SecurityCode",pref.getSecurityCode());
                                JSONObject innerobj=new JSONObject();
                                innerobj.put("AEMEMPLOYEEID",AEMEmployeeID);
                                innerobj.put("OldUANNo",etUAN.getText().toString());
                                innerobj.put("MemberName",binding.etUANNominee.getText().toString());
                                innerobj.put("RelationID",uanRltionshp);
                                innerobj.put("MemberDateOfBirth",uanDOB);
                                innerobj.put("Sex",esicGender);
                                innerobj.put("PFPercentage",pfPercantage);
                                innerobj.put("PFNumber",binding.etPFNumber.getText().toString());
                                innerobj.put("NomineeAddress",binding.etUANNomineeAddress.getText().toString());
                                innerobj.put("PrevEmprName",binding.etPreviousEmployer.getText().toString());
                                mainobject.put("OLDUANDetails",innerobj);
                                uploaduanDetails(mainobject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(TempProfileActivity.this,"ESIC Details has been updated Successfully",Toast.LENGTH_LONG).show();

                        }else {
                            pd.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError error) {

                            Intent intent=new Intent(TempProfileActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();

                    }
                });
    }

    private void uploaduanDetails(JSONObject jsonObject) {
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
                            Intent intent = new Intent(TempProfileActivity.this, KYCFamilyActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            Toast.makeText(TempProfileActivity.this,"UAN Details has been updated Successfully",Toast.LENGTH_LONG).show();

                        }else {

                        }
                    }

                    @Override
                    public void onError(ANError error) {

                            Intent intent=new Intent(TempProfileActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();

                    }
                });
    }
}
