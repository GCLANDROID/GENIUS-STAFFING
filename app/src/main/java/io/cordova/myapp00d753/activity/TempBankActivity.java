package io.cordova.myapp00d753.activity;

import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class TempBankActivity extends AppCompatActivity {
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
    TextView tvSkip;
    String pan_pattern;
    String getbankname;
    ImageView imgHome,imgBack;


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

        spBankName = (Spinner) findViewById(R.id.spBankName);
        spDocType = (Spinner) findViewById(R.id.spDocType);

        llLoader = (LinearLayout) findViewById(R.id.llLoader);
        llMain = (LinearLayout) findViewById(R.id.llMain);

        setBank();

        etAccNumber = (EditText) findViewById(R.id.etAccNumber);
        etAccNumber.setText(pref.getAccNumber());
        etIFSC = (EditText) findViewById(R.id.etIFSC);
        etIFSC.setText(pref.getIFSC());
        etFName = (EditText) findViewById(R.id.etFName);
        etFName.setText(pref.getBFName());
        etLName = (EditText) findViewById(R.id.etLName);
        etLName.setText(pref.getBLName());

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





    }

    private void onClick() {
        llSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etAccNumber.getText().toString().length()>0){
                    if (flag==1){
                        if (etIFSC.getText().toString().length()>10){
                            Pattern r = Pattern.compile(pan_pattern);
                            if (regex_matcher(r, etIFSC.getText().toString())) {
                                if (etFName.getText().toString().length()>0){
                                    if (etLName.getText().toString().length()>0){
                                        BankDetailsSubmit();

                                    }else {
                                        Toast.makeText(getApplicationContext(),"Please enter Last Name as per Bank ",Toast.LENGTH_LONG).show();
                                    }

                                }else {
                                    Toast.makeText(getApplicationContext(),"Please enter First Name as per Bank ",Toast.LENGTH_LONG).show();
                                }

                            }else {
                                Toast.makeText(getApplicationContext(),"Please enter valid IFSC code",Toast.LENGTH_LONG).show();
                            }

                        }else {
                            Toast.makeText(getApplicationContext(),"Please enter IFSC code",Toast.LENGTH_LONG).show();
                        }

                    }else {
                        Toast.makeText(getApplicationContext(),"Please upload Bank Document",Toast.LENGTH_LONG).show();
                    }

                }else {
                    Toast.makeText(getApplicationContext(),"Please enter Account Number",Toast.LENGTH_LONG).show();
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
                Intent intent=new Intent(TempBankActivity.this,KYCFamilyActivity.class);
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
                            imgDoc.setImageBitmap(bm);
                            Log.d("images", encodedImage);
                            flag = 1;


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
        Call<UploadObject> fileUpload = uploadService.uploadbankdetails(fileToUpload, masterid, fname, lname, bankname, accnumbet, ifsc, security, bankdocid);
        fileUpload.enqueue(new Callback<UploadObject>() {
            @Override
            public void onResponse(Call<UploadObject> call, retrofit2.Response<UploadObject> response) {
                progressDialog.dismiss();
                UploadObject extraWorkingDayModel = response.body();
                if (extraWorkingDayModel.isResponseStatus()) {
                    //  Toast.makeText(getApplicationContext(), extraWorkingDayModel.getResponseText(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TempBankActivity.this, KYCFamilyActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

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


}
