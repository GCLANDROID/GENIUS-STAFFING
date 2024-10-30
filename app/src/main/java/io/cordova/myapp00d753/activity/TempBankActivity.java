package io.cordova.myapp00d753.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.appcompat.widget.SearchView;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import id.zelory.compressor.Compressor;
import io.cordova.myapp00d753.AndroidXCamera.AndroidXCameraActivity;
import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.TempCommonFilterAdapter;
import io.cordova.myapp00d753.module.AttendanceService;
import io.cordova.myapp00d753.module.MainDocModule;
import io.cordova.myapp00d753.module.UploadObject;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.Util;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TempBankActivity extends AppCompatActivity {
    private static final String TAG = "TempBankActivity";
    LinearLayout llSubmit;
    Spinner spBankName, spDocType;
    ArrayList<MainDocModule> mainBankName = new ArrayList<>();
    ArrayList<String> bankName = new ArrayList<>();

    ArrayList<MainDocModule> mainDocType = new ArrayList<>();
    ArrayList<String> doctype = new ArrayList<>();
    Pref pref;
    LinearLayout llLoader, llMain;
    String bankname = "";
    String bankdocid = "";
    EditText etAccNumber, etIFSC, etFName, etLName;

    ImageView imgCamera, imgDoc;
    private String encodedImage;
    private Uri imageUri;
    private static final int CAMERA_REQUEST = 1;
    ProgressDialog progressDialog;
    File file,compressedImageFile;
    private static final int REQUEST_GALLERY_CODE = 200;
    private static final int READ_REQUEST_CODE = 300;
    private Uri uri;
    private static final String SERVER_PATH = AppData.url;
    private AttendanceService uploadService;
    int flag;
    String security;
    TextView tvSkip,txtBankName;
    String pan_pattern;
    String getbankname;
    ImageView imgHome,imgBack;
    LinearLayout llBankVALBtn,llBankVAL;
    Button btnBankVal;
    int bankflag=1;
    Dialog searchHolidayDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_bank);
        initialize();
        onClick();
    }

    private void initialize() {
        pref = new Pref(getApplicationContext());
        llSubmit = (LinearLayout) findViewById(R.id.llSubmit);
        llBankVALBtn = (LinearLayout) findViewById(R.id.llBankVALBtn);
        llBankVAL = (LinearLayout) findViewById(R.id.llBankVAL);

        spBankName = (Spinner) findViewById(R.id.spBankName);
        spDocType = (Spinner) findViewById(R.id.spDocType);

        llLoader = (LinearLayout) findViewById(R.id.llLoader);
        llMain = (LinearLayout) findViewById(R.id.llMain);

        btnBankVal=(Button)findViewById(R.id.btnBankVal);
        txtBankName=(TextView) findViewById(R.id.txtBankName);
        setBank();

        etAccNumber = (EditText) findViewById(R.id.etAccNumber);
        etAccNumber.setText(pref.getAccNumber());
        etIFSC = (EditText) findViewById(R.id.etIFSC);
        etIFSC.setText(pref.getIFSC());
        etFName = (EditText) findViewById(R.id.etFName);
        // etFName.setText(pref.getBFName());
        etLName = (EditText) findViewById(R.id.etLName);
        // etLName.setText(pref.getBLName());

        imgCamera = (ImageView) findViewById(R.id.imgCamera);
        imgDoc = (ImageView) findViewById(R.id.imgDoc);

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
        security = pref.getSecurityCode();

        tvSkip=(TextView)findViewById(R.id.tvSkip);
        pan_pattern = "(([A-Za-z]{4})([0-9]{7}))";
        if (!pref.getBankName().equals("")){
            getbankname=pref.getBankName();
        }else {



        }
        imgHome=(ImageView)findViewById(R.id.imgHome);
        imgBack=(ImageView)findViewById(R.id.imgBack);

        txtBankName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSearchBankDialog();
            }
        });
    }

    private void onClick() {
        llSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bankname.isEmpty()){
                    if (etAccNumber.getText().toString().length()>0){
                        if (flag==1){
                            if (etIFSC.getText().toString().length()==11){
                                if (etFName.getText().toString().length()>0){

                                    BankDetailsSubmit();

                                } else {
                                    Toast.makeText(getApplicationContext(),"Please enter First Name as per Bank ",Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(),"Please enter 11 digits IFSC code",Toast.LENGTH_LONG).show();
                            }

                        }else {
                            Toast.makeText(getApplicationContext(),"Please upload Bank Document",Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(),"Please enter Account Number",Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"Please Select Bank Name",Toast.LENGTH_LONG).show();
                }
            }
        });
        spBankName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bankname = mainBankName.get(position).getDocID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spDocType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bankdocid = mainDocType.get(position).getDocID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraIntent();
            }
        });

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TempBankActivity.this,TempOtherDocumentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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
                Intent intent=new Intent(TempBankActivity.this,TempDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnBankVal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etAccNumber.getText().toString().length()>0){
                    if (etIFSC.getText().toString().length()>0){

                        JSONObject jsonObject=new JSONObject();
                        try {
                            jsonObject.put("Id",etAccNumber.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        checkBankDetails(jsonObject);
                    }else {
                        Toast.makeText(TempBankActivity.this,"Please Enter IFSC Code",Toast.LENGTH_LONG).show();
                    }

                }else {
                    Toast.makeText(TempBankActivity.this,"Please enter Bank Account Number",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setBank() {

        String surl = AppData.url+"gcl_CommonDDL?ddltype=5&id1=" + pref.getEmpConId() + "&id2=0&id3=0&SecurityCode=" + pref.getSecurityCode();
        Log.d("bankurl", surl);
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        llLoader.setVisibility(View.VISIBLE);
                        llMain.setVisibility(View.GONE);
                        bankName.clear();
                        mainBankName.clear();
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
                                    bankName.add(qualivalue);
                                    MainDocModule mainDocModule = new MainDocModule(qualiid, qualivalue);
                                    mainBankName.add(mainDocModule);

                                }
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (TempBankActivity.this, android.R.layout.simple_spinner_item,
                                                bankName); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spBankName.setAdapter(spinnerArrayAdapter);
                                int index = bankName.indexOf(getbankname);
                                Log.d("indexr", String.valueOf(index));
                                spBankName.setSelection(index);
                                setBankDocType();

                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TempBankActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
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

    private void setBankDocType() {

        String surl = AppData.url+"gcl_CommonDDL?ddltype=11&id1=" + pref.getEmpConId() + "&id2=0&id3=0&SecurityCode=" + pref.getSecurityCode();
        Log.d("bankdocurl", surl);
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        llLoader.setVisibility(View.GONE);
                        llMain.setVisibility(View.VISIBLE);
                        doctype.clear();
                        mainDocType.clear();
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
                                    doctype.add(qualivalue);
                                    MainDocModule mainDocModule = new MainDocModule(qualiid, qualivalue);
                                    mainDocType.add(mainDocModule);

                                }
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (TempBankActivity.this, android.R.layout.simple_spinner_item,
                                                doctype); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spDocType.setAdapter(spinnerArrayAdapter);

                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TempBankActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
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

    private void cameraIntent() {
        AndroidXCameraActivity.launch(TempBankActivity.this, 1001);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2000 && resultCode == 1001){
            Log.e("TAG", "onActivityResult: "+data.getExtras().get("picture"));
            Log.e("TAG", "onActivityResult: "+data.getExtras().get(AndroidXCameraActivity.IMAGE_PATH_KEY));
            uri =  Uri.parse(String.valueOf(data.getExtras().get("picture")));
            //image_uri = (Uri) data.getExtras().get(AndroidXCameraActivity.IMAGE_PATH_KEY);
            compressedImageFile = new File(String.valueOf(data.getExtras().get("picture")));

            if (uri != null){
                imgDoc.setImageURI(uri);
                flag=1;

            }
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


    private void BankDetailsSubmit() {
        progressDialog.show();

        //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody mFile = RequestBody.create(MediaType.parse(".jpg"), compressedImageFile);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", compressedImageFile.getName(), mFile);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), compressedImageFile.getName());
        String accnumbet = etAccNumber.getText().toString();
        String fname = etFName.getText().toString();
        String lname = etLName.getText().toString();
        String ifsc = etIFSC.getText().toString();
        String masterid = pref.getMasterId();
        String AEMEmployeeID = pref.getEmpId();
        Log.e(TAG, "BankDetailsSubmit: \nfile: IMAGE"
                +"\nAEMEmployeeID: "+masterid
                +"\nFirstNameAsperBank: "+fname
                +"\nLastNameAsperBank: "
                +"\nBankName: "+bankname
                +"\nAccountNumber: "+accnumbet
                +"\nIFSCode: "+ifsc
                +"\nSecurityCode: "+security
                +"\nDocumentID: "+bankdocid);
        Call<UploadObject> fileUpload = uploadService.uploadbankdetails(fileToUpload, AEMEmployeeID, fname, lname, bankname, accnumbet, ifsc, security, bankdocid);
        fileUpload.enqueue(new Callback<UploadObject>() {
            @Override
            public void onResponse(Call<UploadObject> call, retrofit2.Response<UploadObject> response) {

                UploadObject extraWorkingDayModel = response.body();
                if (extraWorkingDayModel.isResponseStatus()) {
                    //  Toast.makeText(getApplicationContext(), extraWorkingDayModel.getResponseText(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(TempBankActivity.this, TempOtherDocumentActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                    JSONObject jsonObject=new JSONObject();
                    try {
                        jsonObject.put("AEMEMPLOYEEID",pref.getEmpId());
                        jsonObject.put("Type",3);
                        jsonObject.put("Status",1);
                        bankvalidFlag(jsonObject,progressDialog);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    progressDialog.dismiss();
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

    private boolean regex_matcher(Pattern pattern, String string) {
        Matcher m = pattern.matcher(string);
        return m.find() && (m.group(0) != null);
    }


    public String getReadableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }


    private void validateBank(JSONObject jsonObject) {
        ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.show();
        pd.setCancelable(false);
        AndroidNetworking.post("https://ind-verify.hyperverge.co/api/checkBankAccount")
                .addJSONObjectBody(jsonObject)
                .addHeaders("appId", AppData.APPID)
                .addHeaders("appKey", AppData.APPKEY)
                .addHeaders("transactionId", pref.getMasterId())
                .addHeaders("content-type", "application/json")
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        pd.dismiss();

                        int statusCode = job1.optInt("statusCode");
                        if (statusCode == 200) {
                            JSONObject result=job1.optJSONObject("result");
                            String accountName= result.optString("accountName");
                            etFName.setText(accountName);

                            llBankVALBtn.setVisibility(View.GONE);
                            llBankVAL.setVisibility(View.VISIBLE);

                            bankflag=1;
                            etAccNumber.setEnabled(false);
                            etIFSC.setEnabled(false);
                            JSONObject cardnoobj=new JSONObject();
                            JSONObject mainobj=new JSONObject();
                            try {
                                cardnoobj.put("cardno",etAccNumber.getText().toString());
                                JSONArray jar=new JSONArray();
                                jar.put(job1);
                                jar.put(cardnoobj);
                                mainobj.put("details",jar);
                                mainobj.put("Document","Bank");
                                Log.d("mainobj", String.valueOf(mainobj));
                                uploadBankDetails(mainobj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }





                        }else {

                            bankflag=0;
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        bankflag=0;
                        pd.dismiss();
                        Toast.makeText(TempBankActivity.this,"Sorry Bank Account details not found",Toast.LENGTH_LONG).show();


                    }
                });
    }


    private void bankvalidFlag(JSONObject jsonObject,ProgressDialog pd) {
        pd.show();
        AndroidNetworking.post(AppData.newv2url+"KYC/UpdateVerifyManage")
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



                    }

                    @Override
                    public void onError(ANError error) {

                        pd.dismiss();
                        Intent intent = new Intent(TempBankActivity.this, TempOtherDocumentActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                });
    }


    private void uploadBankDetails(JSONObject jsonObject) {
        ProgressDialog pd=new ProgressDialog(TempBankActivity.this);
        pd.setMessage("Loading");
        pd.show();
        pd.setCancelable(false);
        AndroidNetworking.post(AppData.newv2url + "Profile/SaveEmployeeBankDetails")
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
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


                        } else {

                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();


                    }
                });
    }


    private void checkBankDetails(JSONObject jsonObject) {
        ProgressDialog pd=new ProgressDialog(TempBankActivity.this);
        pd.setMessage("Loading");
        pd.show();
        pd.setCancelable(false);
        AndroidNetworking.post(AppData.newv2url + "Profile/GetEmployeeAllDetails")
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
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
                        JSONObject Response_Data=job1.optJSONObject("Response_Data");
                        if (Response_Data!=null){
                            JSONObject details=Response_Data.optJSONObject("details");
                            JSONObject result=details.optJSONObject("result");
                            String accountName= result.optString("accountName");
                            etFName.setText(accountName);

                            llBankVALBtn.setVisibility(View.GONE);
                            llBankVAL.setVisibility(View.VISIBLE);

                            bankflag=1;
                            etAccNumber.setEnabled(false);
                            etIFSC.setEnabled(false);

                        }else {
                            JSONObject jsonObject=new JSONObject();
                            try {
                                jsonObject.put("accountNumber",etAccNumber.getText().toString());
                                jsonObject.put("ifsc",etIFSC.getText().toString());
                                validateBank(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onError(ANError error) {

                        pd.dismiss();
                        JSONObject jsonObject=new JSONObject();
                        try {
                            jsonObject.put("accountNumber",etAccNumber.getText().toString());
                            jsonObject.put("ifsc",etIFSC.getText().toString());
                            validateBank(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void openSearchBankDialog() {
        searchHolidayDialog = new Dialog(TempBankActivity.this);
        searchHolidayDialog.setContentView(R.layout.wbs_code_search_layout);
        searchHolidayDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        searchHolidayDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        searchHolidayDialog.setCancelable(true);

        TextView txtPopupHeadline = searchHolidayDialog.findViewById(R.id.txtPopupHeadline);
        SearchView wbsCodeSearchView = (SearchView) searchHolidayDialog.findViewById(R.id.wbsCodeSearchView);
        ImageView imgCancel = searchHolidayDialog.findViewById(R.id.imgCancel);
        RecyclerView rvWbsCode = searchHolidayDialog.findViewById(R.id.rvWbsCode);
        rvWbsCode.setLayoutManager(new LinearLayoutManager(TempBankActivity.this));
        TempCommonFilterAdapter tempCommonFilterAdapter;

        wbsCodeSearchView.setQueryHint("Search Bank Name");
        txtPopupHeadline.setText("Select Bank Name");

        ArrayList<MainDocModule>  mainBankListCopy = (ArrayList<MainDocModule>) mainBankName.clone();
        tempCommonFilterAdapter = new TempCommonFilterAdapter(TempBankActivity.this,mainBankListCopy,"bank_name");
        rvWbsCode.setAdapter(tempCommonFilterAdapter);



        wbsCodeSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                tempCommonFilterAdapter.getFilter().filter(s);
                return false;
            }
        });


        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchHolidayDialog.dismiss();
            }
        });
        searchHolidayDialog.show();
    }

    public void setText(String bankId,String selectedItem, String selectFor){
        txtBankName.setText(selectedItem);
        bankname = bankId;
        Log.e(TAG, "bankname: "+bankname);
        searchHolidayDialog.dismiss();
    }
}
