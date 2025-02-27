package io.cordova.myapp00d753.activity;

import static io.cordova.myapp00d753.utility.RealPathUtil.getRealPath;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.squareup.picasso.Picasso;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.cordova.myapp00d753.AndroidXCamera.AndroidXCameraActivity;
import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.AttendanceService;
import io.cordova.myapp00d753.module.MainDocModule;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.FindDocumentInformation;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.Util;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TempPanActivity extends AppCompatActivity {
    private static final String TAG = "TempPanActivity";
    LinearLayout llSubmit;
    ImageView imgDoc,imgCamera;
    String userChoosenTask = "";
    private String encodedImage;
    private Uri imageUri;
    private static final int CAMERA_REQUEST = 1;
    ProgressDialog progressDialog;
    File file,compressedImageFile,compressedImageFilePan;
    private static final int REQUEST_GALLERY_CODE = 200;
    private static final int READ_REQUEST_CODE = 300;
    private Uri uri;
    private static final String SERVER_PATH = AppData.url;
    private AttendanceService uploadService;
    Pref pref;
    String security;
    EditText etPanNumber;
    int flag;
    int backflag;
    int frontflag;
    TextView tvSubmit,tvSkip;
    ImageView imgBackDown,imgForward;
    String pan_pattern,aadharPatern="^[2-9]{1}[0-9]{3}[0-9]{4}[0-9]{4}$";
    ImageView imgHome,imgBack;
    String color;
    TextView tvAddaharNo,tvAddaharImg,tvAddaharBackImg;
    AlertDialog alerDialog1;
    Uri image_uri;
    String encodeToString;
    Button btnPanSave;
    ImageView imgAadharCamera,imgAadharBackCamera,imgAadharBackDocument,imgAadharDocument;
    Button btnAadharSave;
    EditText etAddaharNo;
    int responseflag=0;
    int panvalflag=0;
    LinearLayout llPANVAL,llPanDoc,llAadharFront,llAadharBack;
    String aadhaarImage="",aadhaarBackPage = "", imagePanURL ="";
    //boolean is_Pan_Document_selected = false;
    boolean panFlag=false;
    String pdfFilePath, pdfFileName;
    private static final int DEFAULT_BUFFER_SIZE = 2048;
    String frontID,backID,panID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_pan);
        initialize();
        onClick();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("AEMConsultantID", pref.getEmpConId());
            jsonObject.put("AEMClientID", pref.getEmpClintId());
            jsonObject.put("AEMClientOfficeID", pref.getEmpClintOffId());
            jsonObject.put("AEMEmployeeID", pref.getEmpId());
            jsonObject.put("WorkingStatus", "1");
            jsonObject.put("Operation", "12");
            getAadhaarDetails(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initialize(){
        pref = new Pref(TempPanActivity.this);
        llPANVAL=(LinearLayout)findViewById(R.id.llPANVAL);
        llPanDoc=(LinearLayout)findViewById(R.id.llPanDoc);
        etAddaharNo=(EditText)findViewById(R.id.etAddaharNo);
        btnPanSave=(Button)findViewById(R.id.btnPanSave);

        color = "<font color='#EE0000'>*</font>";
        tvAddaharNo = (TextView) findViewById(R.id.tvAddaharNo);
        String aadaharno = "Aadhaar Number";
        tvAddaharNo.setText(Html.fromHtml(aadaharno + color));

        tvAddaharImg = (TextView) findViewById(R.id.tvAddaharImg);
        String aadharimg = "Aadhaar Front Image";
        tvAddaharImg.setText(Html.fromHtml(aadharimg + color));


        tvAddaharBackImg = (TextView) findViewById(R.id.tvAddaharBackImg);
        String aadharbackimg = "Aadhaar Back Image";
        tvAddaharBackImg.setText(Html.fromHtml(aadharbackimg + color));


        llSubmit=(LinearLayout)findViewById(R.id.llSubmit);
        llAadharFront=(LinearLayout)findViewById(R.id.llAadharFront);
        llAadharBack=(LinearLayout)findViewById(R.id.llAadharBack);
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
        security=pref.getSecurityCode();
        imgCamera=(ImageView)findViewById(R.id.imgCamera);
        imgDoc=(ImageView)findViewById(R.id.imgDocument);
        etPanNumber=(EditText)findViewById(R.id.etPanNumber);
        tvSubmit=(TextView)findViewById(R.id.tvSubmit);
        imgBackDown=(ImageView)findViewById(R.id.imgBackDown);
        imgForward=(ImageView)findViewById(R.id.imgForward);
        tvSkip=(TextView)findViewById(R.id.tvSkip);
        pan_pattern = "(([A-Za-z]{5})([0-9]{4})([a-zA-Z]))";
        imgHome=(ImageView)findViewById(R.id.imgHome);
        imgBack=(ImageView)findViewById(R.id.imgBack);

        imgAadharCamera=(ImageView)findViewById(R.id.imgAadharCamera);
        imgAadharBackCamera=(ImageView)findViewById(R.id.imgAadharBackCamera);
        imgAadharBackDocument=(ImageView)findViewById(R.id.imgAadharBackDocument);
        imgAadharDocument=(ImageView)findViewById(R.id.imgAadharDocument);
        btnAadharSave=(Button) findViewById(R.id.btnAadharSave);

        etAddaharNo.setText(AppData.AADAHARNUMBER);
        etAddaharNo.setEnabled(false);
        JSONObject obj=new JSONObject();
        try {
            obj.put("ddltype", "Doc_Aadhar");
            obj.put("SecurityCode",pref.getSecurityCode());
            getAddharFrontID(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void onClick(){
        btnAadharSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etAddaharNo.getText().toString().length()>0){

                        if (frontflag==1 && backflag==1){

                            Pattern r = Pattern.compile(aadharPatern);
                            if (regex_matcher(r, etAddaharNo.getText().toString())) {
                                aadharFrontUpload();
                            }else {
                                Toast.makeText(getApplicationContext(),"Invalid Aadhaar number",Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(getApplicationContext(),"Please upload Aadhaar Front and Back Image",Toast.LENGTH_LONG).show();
                            llAadharFront.setBackgroundResource(R.drawable.lldesign_error);
                            llAadharBack.setBackgroundResource(R.drawable.lldesign_error);
                        }


                }else {
                    Toast.makeText(getApplicationContext(),"Please enter valid Aadhaar number",Toast.LENGTH_LONG).show();

                }
            }
        });


        btnPanSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etPanNumber.getText().toString().length()>0){
                    if (etPanNumber.getText().toString().length()>9){
                        if (flag==1){
                            if (panvalflag==1) {
                                panUpload();
                            }else {
                                Toast.makeText(getApplicationContext(),"Please Enter Valid PAN Number",Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(getApplicationContext(),"Please upload PAN document",Toast.LENGTH_LONG).show();
                            llPanDoc.setBackgroundResource(R.drawable.lldesign_error);
                        }
                    }else {
                        Toast.makeText(getApplicationContext(),"Please enter valid PAN number",Toast.LENGTH_LONG).show();
                        etPanNumber.setBackgroundResource(R.drawable.lldesign_error);
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"Please enter valid PAN number",Toast.LENGTH_LONG).show();
                    etPanNumber.setBackgroundResource(R.drawable.lldesign_error);
                }
            }
        });
        etPanNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etPanNumber.getText().toString().length()==10){
                    JSONObject jsonObject=new JSONObject();
                    try {
                        jsonObject.put("Id",etPanNumber.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    checkPANDetails(jsonObject);
                }

            }
        });
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (responseflag==1) {
                    Intent intent = new Intent(TempPanActivity.this, TempBankActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    Toast.makeText(TempPanActivity.this,"Please Upload Aadhaar Details",Toast.LENGTH_LONG).show();
                }
            }
        });

        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attechmentAlert(200,1001, "no");
            }
        });


        imgAadharCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attechmentAlert(300,2001,"no");
            }
        });


        imgAadharBackCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attechmentAlert(400,3001,"no");
            }
        });


        imgBackDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TempPanActivity.this,TempProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TempPanActivity.this,TempBankActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TempPanActivity.this,TempDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //aadhaarImage="",aadhaarBackPage = "", imagePanURL ="";
        imgAadharDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!aadhaarImage.isEmpty()){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(aadhaarImage));
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);                }
            }
        });

        imgAadharBackDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!aadhaarBackPage.isEmpty()){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(aadhaarBackPage));
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        imgDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!imagePanURL.isEmpty()){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(imagePanURL));
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2000 && resultCode == 1001){
            Log.e(TAG, "onActivityResult: 2000  1001");
            Log.e("TAG", "onActivityResult: "+data.getExtras().get("picture"));
            Log.e("TAG", "onActivityResult: "+data.getExtras().get(AndroidXCameraActivity.IMAGE_PATH_KEY));
            image_uri =  Uri.parse(String.valueOf(data.getExtras().get("picture")));
            //image_uri = (Uri) data.getExtras().get(AndroidXCameraActivity.IMAGE_PATH_KEY);
            compressedImageFilePan = new File(String.valueOf(data.getExtras().get("picture")));

            if (image_uri != null){
                imgDoc.setImageURI(image_uri);
                flag=1;

                llPanDoc.setBackgroundResource(R.drawable.lldesign9);
            }
        }else if ((requestCode == 200 )) {
            InputStream imageStream = null;
            try {
                try {
                    uri = data.getData();
                    String filePath = getRealPathFromURIPath(uri, TempPanActivity.this);
                    compressedImageFilePan = new File(filePath);
                    //  Log.d(TAG, "filePath=" + filePath);
                    imageStream = getContentResolver().openInputStream(uri);
                    Bitmap bm = cropToSquare(BitmapFactory.decodeStream(imageStream));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.PNG, 10, baos); //bm is the bitmap object
                    byte[] b = baos.toByteArray();
                    encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

                    imgDoc.setImageBitmap(bm);

                    flag = 1;

                    llPanDoc.setBackgroundResource(R.drawable.lldesign9);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }

        }else if (requestCode == 1002){
            Log.e(TAG, "onActivityResult: PDF: "+data.getData());
            if (data != null){
                Uri uri = data.getData();
                Log.e("TAG", "onActivityResult: "+uri.getPath());
                String imagePath = uri.getPath();
                if (imagePath.contains("all_external")){
                    pdfFileName = FindDocumentInformation.FileNameFromURL(imagePath);
                    compressedImageFilePan = convertInputStreamToFile(uri,pdfFileName);

                } else {
                    try {
                        pdfFilePath = getRealPath(TempPanActivity.this,uri);
                        pdfFileName = FindDocumentInformation.FileNameFromURL(pdfFilePath);
                    } catch (IllegalArgumentException e){
                        //Todo: from WPS office document select
                        pdfFileName = FindDocumentInformation.FileNameFromURL(imagePath);
                        compressedImageFilePan = convertInputStreamToFile(uri,pdfFileName);

                    }
                    compressedImageFilePan = convertInputStreamToFile(uri,pdfFileName);
                    Log.e(TAG, "onActivityResult: "+compressedImageFile.getAbsolutePath());
                    imgDoc.setImageResource(R.drawable.pdff);
                }
                flag = 1;
            }
        }else if (requestCode == 2000 && resultCode == 2001){
            Log.e("TAG", "onActivityResult: "+data.getExtras().get("picture"));
            Log.e("TAG", "onActivityResult: "+data.getExtras().get(AndroidXCameraActivity.IMAGE_PATH_KEY));
            image_uri =  Uri.parse(String.valueOf(data.getExtras().get("picture")));
            //image_uri = (Uri) data.getExtras().get(AndroidXCameraActivity.IMAGE_PATH_KEY);
            compressedImageFile = new File(String.valueOf(data.getExtras().get("picture")));

            if (image_uri != null){
                imgAadharDocument.setImageURI(image_uri);
                frontflag=1;
                llAadharFront.setBackgroundResource(R.drawable.lldesign9);

            }
        }else if ((requestCode == 300 )) {
            InputStream imageStream = null;
            try {
                try {
                    uri = data.getData();
                    String filePath = getRealPathFromURIPath(uri, TempPanActivity.this);
                    compressedImageFile = new File(filePath);
                    //  Log.d(TAG, "filePath=" + filePath);
                    imageStream = getContentResolver().openInputStream(uri);
                    Bitmap bm = cropToSquare(BitmapFactory.decodeStream(imageStream));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.PNG, 10, baos); //bm is the bitmap object
                    byte[] b = baos.toByteArray();
                    encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

                    imgAadharDocument.setImageBitmap(bm);

                    frontflag=1;
                    llAadharFront.setBackgroundResource(R.drawable.lldesign9);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }

        } else if (requestCode == 1003){
            Log.e(TAG, "onActivityResult: PDF: "+data.getData());
            if (data != null){
                Uri uri = data.getData();
                Log.e("TAG", "onActivityResult: "+uri.getPath());
                String imagePath = uri.getPath();
                if (imagePath.contains("all_external")){
                    pdfFileName = FindDocumentInformation.FileNameFromURL(imagePath);
                    compressedImageFile = convertInputStreamToFile(uri,pdfFileName);

                } else {
                    try {
                        pdfFilePath = getRealPath(TempPanActivity.this,uri);
                        pdfFileName = FindDocumentInformation.FileNameFromURL(pdfFilePath);
                    } catch (IllegalArgumentException e){
                        //Todo: from WPS office document select
                        pdfFileName = FindDocumentInformation.FileNameFromURL(imagePath);
                        compressedImageFile = convertInputStreamToFile(uri,pdfFileName);

                    }
                    compressedImageFile = convertInputStreamToFile(uri,pdfFileName);
                    Log.e(TAG, "onActivityResult: "+compressedImageFile.getAbsolutePath());
                    imgAadharDocument.setImageResource(R.drawable.pdff);
                }
                frontflag=1;
            }
        }else if (requestCode == 2000 && resultCode == 3001) {
            Log.e("TAG", "onActivityResult: "+data.getExtras().get("picture"));
            Log.e("TAG", "onActivityResult: "+data.getExtras().get(AndroidXCameraActivity.IMAGE_PATH_KEY));
            image_uri =  Uri.parse(String.valueOf(data.getExtras().get("picture")));
            //image_uri = (Uri) data.getExtras().get(AndroidXCameraActivity.IMAGE_PATH_KEY);
            file = new File(String.valueOf(data.getExtras().get("picture")));

            if (image_uri != null){
                imgAadharBackDocument.setImageURI(image_uri);
                backflag=1;
                llAadharBack.setBackgroundResource(R.drawable.lldesign9);
            }
        }else if ((requestCode == 400 )) {
            InputStream imageStream = null;
            try {
                try {
                    uri = data.getData();
                    String filePath = getRealPathFromURIPath(uri, TempPanActivity.this);
                    file = new File(filePath);
                    //  Log.d(TAG, "filePath=" + filePath);
                    imageStream = getContentResolver().openInputStream(uri);
                    Bitmap bm = cropToSquare(BitmapFactory.decodeStream(imageStream));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.PNG, 10, baos); //bm is the bitmap object
                    byte[] b = baos.toByteArray();
                    encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

                    imgAadharBackDocument.setImageBitmap(bm);

                    backflag=1;

                    llAadharBack.setBackgroundResource(R.drawable.lldesign9);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        } else if (requestCode == 1004){
            Log.e(TAG, "onActivityResult: PDF: "+data.getData());
            if (data != null){
                Uri uri = data.getData();
                Log.e("TAG", "onActivityResult: "+uri.getPath());
                String imagePath = uri.getPath();
                if (imagePath.contains("all_external")){
                    pdfFileName = FindDocumentInformation.FileNameFromURL(imagePath);
                    file = convertInputStreamToFile(uri,pdfFileName);

                } else {
                    try {
                        pdfFilePath = getRealPath(TempPanActivity.this,uri);
                        pdfFileName = FindDocumentInformation.FileNameFromURL(pdfFilePath);
                    } catch (IllegalArgumentException e){
                        //Todo: from WPS office document select
                        pdfFileName = FindDocumentInformation.FileNameFromURL(imagePath);
                        file = convertInputStreamToFile(uri,pdfFileName);

                    }
                    file = convertInputStreamToFile(uri,pdfFileName);
                    Log.e(TAG, "onActivityResult: "+compressedImageFile.getAbsolutePath());
                    imgAadharBackDocument.setImageResource(R.drawable.pdff);
                }
                backflag=1;
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


    private void panUpload() {
        progressDialog.show();
        AndroidNetworking.upload(AppData.SAVE_EMP_DIGITAL_DOCUMENT)
                .addMultipartParameter("AEMEmployeeID",pref.getEmpId())
                .addMultipartParameter("DocumentID", panID)
                .addMultipartParameter("ReferenceNo", etPanNumber.getText().toString())
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())
                .addMultipartFile("SingleFile", compressedImageFilePan)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setPercentageThresholdForCancelling(60)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        progressDialog.show();
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "SAVE_PAN_DOC: "+response.toString(4));
                            JSONObject job1 = response;
                            int Response_Code = job1.optInt("Response_Code");
                            String Response_Data = job1.optString("Response_Data");
                            if (Response_Code == 101) {
                                JSONObject jsonObject=new JSONObject();
                                try {
                                    jsonObject.put("AEMEMPLOYEEID",pref.getEmpId());
                                    jsonObject.put("Type",2);
                                    jsonObject.put("Status",1);
                                    panAadharvalidFlag(jsonObject,progressDialog);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                btnPanSave.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Your PAN details has been updated Successfully", Toast.LENGTH_LONG).show();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), Response_Data, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e("errt", String.valueOf(error));
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Something went wrong,Please try again", Toast.LENGTH_LONG).show();
                    }
                });

        //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        /*AndroidNetworking.upload(AppData.url+"post_empdigitaldocument")
                .addMultipartParameter("AEMEmployeeID",pref.getEmpId())
                .addMultipartParameter("DocumentID", "003")
                .addMultipartParameter("ReferenceNo", etPanNumber.getText().toString())
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())
                .addMultipartFile("SingleFile", compressedImageFilePan)
                .setPercentageThresholdForCancelling(60)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        progressDialog.show();


                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {



                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        String responseText = job1.optString("responseText");
                        boolean responseStatus = job1.optBoolean("responseStatus");

                        if (responseStatus) {
                            JSONObject jsonObject=new JSONObject();
                            try {
                                jsonObject.put("AEMEMPLOYEEID",pref.getEmpId());
                                jsonObject.put("Type",2);
                                jsonObject.put("Status",1);
                                panAadharvalidFlag(jsonObject,progressDialog);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            btnPanSave.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Your PAN details has been updated Successfully", Toast.LENGTH_LONG).show();

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();
                        }
                        // boolean _status = job1.getBoolean("status");
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e("errt", String.valueOf(error));
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Something went wrong,Please try again", Toast.LENGTH_LONG).show();
                    }
                });*/

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


    private void attechmentAlert(int gallerycode,int cameracode, String pdf_option) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TempPanActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_attachment, null);
        dialogBuilder.setView(dialogView);
        LinearLayout lnCamera=(LinearLayout)dialogView.findViewById(R.id.lnCamera);
        LinearLayout lnGallery=(LinearLayout)dialogView.findViewById(R.id.lnGallery);
        LinearLayout lnCancel=(LinearLayout)dialogView.findViewById(R.id.lnCancel);
        LinearLayout lnPDF=(LinearLayout)dialogView.findViewById(R.id.lnPDF);
        if (pdf_option.equalsIgnoreCase("yes")){
            lnPDF.setVisibility(View.VISIBLE);
        } else {
            lnPDF.setVisibility(View.GONE);
        }
        lnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndroidXCameraActivity.launch(TempPanActivity.this, cameracode);
                alerDialog1.dismiss();
            }
        });


        lnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryIntent(gallerycode);
                alerDialog1.dismiss();
            }
        });
        lnPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "gallerycode: "+gallerycode);
                if (gallerycode == 200){
                    pdfIntent(1002);
                } else if(gallerycode == 300){
                    pdfIntent(1003);
                } else if (gallerycode == 400){
                    pdfIntent(1004);
                }
                alerDialog1.dismiss();
            }
        });
        lnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.cancel();
            }
        });



        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(false);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }


    private void galleryIntent(int gallerycode) {
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
        openGalleryIntent.setType("image/*");
        startActivityForResult(openGalleryIntent, gallerycode);
    }

    private void pdfIntent(int gallerycode) {
        Intent openGalleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        openGalleryIntent.setType("application/pdf");
        startActivityForResult(openGalleryIntent, gallerycode);
    }


    private void aadharFrontUpload() {
        progressDialog.show();
        AndroidNetworking.upload(AppData.SAVE_EMP_DIGITAL_DOCUMENT)
                .addMultipartParameter("AEMEmployeeID",pref.getEmpId())
                .addMultipartParameter("DocumentID", frontID)
                .addMultipartParameter("ReferenceNo", etAddaharNo.getText().toString())
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())
                .addMultipartFile("SingleFile", compressedImageFile)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setPercentageThresholdForCancelling(60)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        progressDialog.show();
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "SAVE_AADHAAR_FRONT: "+response.toString(4));
                            JSONObject job1 = response;
                            int Response_Code = job1.optInt("Response_Code");
                            String Response_Data = job1.optString("Response_Data");
                            if (Response_Code == 101) {
                                aadharBackUpload();
                            } else {
                                Toast.makeText(getApplicationContext(), Response_Data, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e(TAG, "SAVE_AADHAAR_FRONT: "+error.getErrorBody());
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Something went wrong,Please try again", Toast.LENGTH_LONG).show();
                    }
                });

        //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        /*AndroidNetworking.upload(AppData.url+"post_empdigitaldocument")
                .addMultipartParameter("AEMEmployeeID",pref.getEmpId())
                .addMultipartParameter("DocumentID", "002")
                .addMultipartParameter("ReferenceNo", etAddaharNo.getText().toString())
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())
                .addMultipartFile("SingleFile", compressedImageFile)
                .setPercentageThresholdForCancelling(60)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        progressDialog.show();


                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.show();


                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        String responseText = job1.optString("responseText");
                        boolean responseStatus = job1.optBoolean("responseStatus");

                        if (responseStatus) {
                           aadharBackUpload();
                        } else {
                           Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();
                        }


                        // boolean _status = job1.getBoolean("status");



                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e("errt", String.valueOf(error));
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Something went wrong,Please try again", Toast.LENGTH_LONG).show();
                    }
                });*/

    }


    private void aadharBackUpload() {
        progressDialog.show();
        AndroidNetworking.upload(AppData.SAVE_EMP_DIGITAL_DOCUMENT)
                .addMultipartParameter("AEMEmployeeID",pref.getEmpId())
                .addMultipartParameter("DocumentID", backID)
                .addMultipartParameter("ReferenceNo", etAddaharNo.getText().toString())
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())
                .addMultipartFile("SingleFile", file)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setPercentageThresholdForCancelling(60)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        progressDialog.show();
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "SAVE_AADHAAR_BACK: "+response.toString(4));
                            JSONObject job1 = response;
                            int Response_Code = job1.optInt("Response_Code");
                            String Response_Data = job1.optString("Response_Data");
                            if (Response_Code == 101) {
                                JSONObject jsonObject=new JSONObject();
                                try {
                                    jsonObject.put("AEMEMPLOYEEID",pref.getEmpId());
                                    jsonObject.put("Type",1);
                                    jsonObject.put("Status",1);
                                    panAadharvalidFlag(jsonObject,progressDialog);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                btnAadharSave.setVisibility(View.GONE);
                                responseflag=1;
                                Toast.makeText(getApplicationContext(), "Your Aadhaar details has been updated Successfully", Toast.LENGTH_LONG).show();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), Response_Data, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e(TAG, "SAVE_AADHAAR_BACK_error: "+error.getErrorBody());
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Something went wrong,Please try again", Toast.LENGTH_LONG).show();
                    }
                });


        //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        /*AndroidNetworking.upload(AppData.url+"post_empdigitaldocument")
                .addMultipartParameter("AEMEmployeeID",pref.getEmpId())
                .addMultipartParameter("DocumentID", "00233")
                .addMultipartParameter("ReferenceNo", etAddaharNo.getText().toString())
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())
                .addMultipartFile("SingleFile", file)
                .setPercentageThresholdForCancelling(60)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        progressDialog.show();


                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {



                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        String responseText = job1.optString("responseText");
                        boolean responseStatus = job1.optBoolean("responseStatus");

                        if (responseStatus) {
                            JSONObject jsonObject=new JSONObject();
                            try {
                                jsonObject.put("AEMEMPLOYEEID",pref.getEmpId());
                                jsonObject.put("Type",1);
                                jsonObject.put("Status",1);
                                panAadharvalidFlag(jsonObject,progressDialog);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            btnAadharSave.setVisibility(View.GONE);
                            responseflag=1;
                            Toast.makeText(getApplicationContext(), "Your Aadhaar details has been updated Successfully", Toast.LENGTH_LONG).show();

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();

                        }


                        // boolean _status = job1.getBoolean("status");



                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e("errt", String.valueOf(error));
                        progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(), "Something went wrong,Please try again", Toast.LENGTH_LONG).show();
                    }
                });*/

    }

    private void validatePAN(JSONObject jsonObject) {
        ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.show();
        pd.setCancelable(false);
        AndroidNetworking.post("https://ind-thomas.hyperverge.co/v1/PANDetailedFetchWithoutPhoneNumber")
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
                            JSONObject data=result.optJSONObject("data");
                            JSONObject panData=data.optJSONObject("panData");
                            String dateOfBirth= Util.changeAnyDateFormat(panData.optString("dateOfBirth"),"yyyy-MM-dd","dd-MM-yyyy");
                            if (!dateOfBirth.equals("")){
                                if (dateOfBirth.equals(AppData.ADHARDOB)) {
                                    panvalflag=1;
                                    panFlag=true;
                                    etPanNumber.setEnabled(false);
                                    llPANVAL.setVisibility(View.VISIBLE);
                                }else {
                                    panvalflag=0;
                                    Toast.makeText(TempPanActivity.this,"Sorry PAN validation failed",Toast.LENGTH_LONG).show();
                                    etPanNumber.setText("");
                                }
                            }else {
                                panvalflag=1;
                                panFlag=true;
                                etPanNumber.setEnabled(false);
                            }


                            JSONObject cardnoobj=new JSONObject();
                            JSONObject mainobj=new JSONObject();
                            try {
                                cardnoobj.put("cardno",etPanNumber.getText().toString());
                                JSONArray jar=new JSONArray();
                                jar.put(job1);
                                jar.put(cardnoobj);
                                mainobj.put("details",jar);
                                mainobj.put("Document","PAN");
                                Log.d("mainobj", String.valueOf(mainobj));
                                uploadPANDetails(mainobj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }







                        }else {


                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                        panvalflag=0;
                        Toast.makeText(TempPanActivity.this,"Sorry PAN validation failed",Toast.LENGTH_LONG).show();
                        etPanNumber.setText("");



                    }
                });
    }


    private void panAadharvalidFlag(JSONObject jsonObject,ProgressDialog pd) {
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
                        if (panFlag==true){
                            pd.show();
                            JSONObject obj=new JSONObject();
                            try {
                                obj.put("Operation",2);
                                obj.put("Id",pref.getEmpId());
                                Log.d("aadhartrackobj",obj.toString());
                                aadharTrack(obj,pd);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            pd.dismiss();
                        }



                    }

                    @Override
                    public void onError(ANError error) {

                        pd.dismiss();

                    }
                });
    }

    private void aadharTrack(JSONObject jsonObject,ProgressDialog pd) {

        pd.show();
        AndroidNetworking.post(AppData.newv2url + "Profile/UpdateEmployeeKYCApiStatus")
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                        JSONObject job1 = response;
                        Log.e("aadhartarck", "@@@@@@" + job1);


                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            pd.dismiss();



                            //Toast.makeText(TempProfileActivity.this, "ESIC Details has been updated Successfully", Toast.LENGTH_LONG).show();

                        } else {
                            pd.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError error) {

                        Intent intent = new Intent(TempPanActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });
    }


    private void uploadPANDetails(JSONObject jsonObject) {
        ProgressDialog pd=new ProgressDialog(TempPanActivity.this);
        pd.setMessage("Loading");
        pd.show();
        pd.setCancelable(false);
        AndroidNetworking.post(AppData.newv2url + "Profile/SaveEmployeePanDetails")
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


    private void checkPANDetails(JSONObject jsonObject) {
        ProgressDialog pd=new ProgressDialog(TempPanActivity.this);
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
                            JSONObject data=result.optJSONObject("data");
                            JSONObject panData=data.optJSONObject("panData");
                            String dateOfBirth= Util.changeAnyDateFormat(panData.optString("dateOfBirth"),"yyyy-MM-dd","dd-MM-yyyy");
                            if (!dateOfBirth.equals("")){
                                panFlag=true;
                                if (dateOfBirth.equals(AppData.ADHARDOB)) {
                                    panvalflag=1;
                                    etPanNumber.setEnabled(false);
                                    llPANVAL.setVisibility(View.VISIBLE);
                                    etPanNumber.setBackgroundResource(R.drawable.lldesign9);
                                }else {
                                    panvalflag=0;
                                    Toast.makeText(TempPanActivity.this,"Sorry PAN validation failed",Toast.LENGTH_LONG).show();
                                    etPanNumber.setText("");
                                    etPanNumber.setBackgroundResource(R.drawable.lldesign_error);
                                }
                            }else {
                                panFlag=true;
                                panvalflag=1;
                                etPanNumber.setEnabled(false);
                            }

                        }else {
                            JSONObject jsonObject=new JSONObject();
                            try {
                                jsonObject.put("pan",etPanNumber.getText().toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            validatePAN(jsonObject);
                        }

                    }

                    @Override
                    public void onError(ANError error) {

                        pd.dismiss();
                        JSONObject jsonObject=new JSONObject();
                        try {
                            jsonObject.put("pan",etPanNumber.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        validatePAN(jsonObject);
                    }
                });
    }

    private void getAadhaarDetails(JSONObject jsonObject){
        ProgressDialog pd=new ProgressDialog(TempPanActivity.this);
        pd.setMessage("Loading...");
        pd.show();
        pd.setCancelable(false);
        Log.e(TAG, "getAadhaarDetails: "+jsonObject);
        AndroidNetworking.post(AppData.KYC_GET_DETAILS)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "GET_AADHAAR_DETAILS: "+ response.toString(4));
                            JSONObject job1 = response;
                            int Response_Code = job1.optInt("Response_Code");
                            if (Response_Code == 101) {
                                responseflag=1;
                                String Response_Data = job1.optString("Response_Data");
                                JSONObject job2 = new JSONObject(Response_Data);
                                JSONArray jsonArray = job2.getJSONArray("AadharDetails");
                                JSONObject aadhaarFront = null;
                                JSONObject aadhaarBack = null;
                                if (jsonArray.length()>0){
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.optJSONObject(i);
                                        if (object.optString("Document").equalsIgnoreCase("Aadhaar Card")){
                                            aadhaarFront = object;
                                        } else {
                                            aadhaarBack = object;
                                        }
                                    }

                                    if (aadhaarFront != null){
                                        aadhaarImage = AppData.IMAGE_PATH_URL+aadhaarFront.optString("FileName");

                                       /* Picasso.with(TempPanActivity.this)
                                                .load(AppData.IMAGE_PATH_URL+aadhaarFront.optString("FileName"))
                                                .placeholder(R.drawable.load)
                                                .skipMemoryCache()// optional
                                                .error(R.drawable.warning)
                                                // optional
                                                .into(imgAadharDocument);
                                        ImageDownloader.downloadImageAndSaveToFile(getApplication(), aadhaarImage, aadhaarFront.optString("FileName"), new ImageDownloader.SaveFileListener() {
                                            @Override
                                            public void onSaveFile(File file) {
                                                pd.dismiss();
                                                compressedImageFile = file;
                                                frontflag=1;
                                                responseflag = 1;
                                                Log.e(TAG, "compressedImageFile: "+compressedImageFile.getPath());
                                            }

                                            @Override
                                            public void onFileSaveFailure(String error) {
                                                pd.dismiss();
                                                Log.e(TAG, "onFileSaveFailure: "+error);
                                            }
                                        });*/
                                        frontflag=1;
                                        getAadhaarFront(aadhaarImage, aadhaarFront.optString("FileName"),pd);
                                    }

                                    if (aadhaarBack != null){
                                        aadhaarBackPage = AppData.IMAGE_PATH_URL+aadhaarBack.optString("FileName");
                                        /*Picasso.with(TempPanActivity.this)
                                                .load(AppData.IMAGE_PATH_URL+aadhaarBack.optString("FileName"))
                                                .placeholder(R.drawable.load)
                                                .skipMemoryCache()// optional
                                                .error(R.drawable.warning)
                                                // optional
                                                .into(imgAadharBackDocument);
                                        ImageDownloader.downloadImageAndSaveToFile(getApplication(), aadhaarBackPage, aadhaarBack.optString("FileName"), new ImageDownloader.SaveFileListener() {
                                            @Override
                                            public void onSaveFile(File file1) {
                                                pd.dismiss();
                                                file = file1;
                                                backflag=1;
                                                responseflag = 1;
                                                Log.e(TAG, "compressedImageFile: "+compressedImageFile.getPath());
                                            }

                                            @Override
                                            public void onFileSaveFailure(String error) {
                                                pd.dismiss();
                                                Log.e(TAG, "onFileSaveFailure: "+error);
                                            }
                                        });*/
                                        backflag=1;
                                        getAadhaarBack(aadhaarBackPage, aadhaarBack.optString("FileName"),pd);
                                    }
                                }


                            } else {
                                pd.dismiss();
                            }

                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("AEMConsultantID", pref.getEmpConId());
                                jsonObject.put("AEMClientID", pref.getEmpClintId());
                                jsonObject.put("AEMClientOfficeID", pref.getEmpClintOffId());
                                jsonObject.put("AEMEmployeeID", pref.getEmpId());
                                jsonObject.put("WorkingStatus", "1");
                                jsonObject.put("Operation", "13");
                                getPanDetails(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        pd.dismiss();
                        Log.e(TAG, "GET_AADHAAR_DETAILS_anError: "+ anError.getErrorBody()  );
                    }
                });
    }

    private void getPanDetails(JSONObject jsonObject){
        ProgressDialog pd=new ProgressDialog(TempPanActivity.this);
        pd.setMessage("Loading...");
        pd.show();
        pd.setCancelable(false);
        Log.e(TAG, "getAadhaarDetails: "+jsonObject);
        AndroidNetworking.post(AppData.KYC_GET_DETAILS)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            pd.dismiss();
                            Log.e(TAG, "GET_PAN_DETAILS: "+ response.toString(4));
                            JSONObject job1 = response;
                            int Response_Code = job1.optInt("Response_Code");
                            if (Response_Code == 101) {
                                String Response_Data = job1.optString("Response_Data");
                                JSONObject job2 = new JSONObject(Response_Data);
                                JSONArray jsonArray = job2.getJSONArray("PANDetails");

                                if (jsonArray.length() > 0){
                                    JSONObject panObj = null;
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.optJSONObject(i);
                                        if (object.optString("Document").equalsIgnoreCase("PAN Card")){
                                            panObj = object;
                                        }
                                    }

                                    if (panObj != null){
                                        etPanNumber.setText(panObj.optString("ReferenceNo"));
                                        imagePanURL = AppData.IMAGE_PATH_URL+panObj.optString("FileName");
                                       /* Picasso.with(TempPanActivity.this)
                                                .load(AppData.IMAGE_PATH_URL+panObj.optString("FileName"))
                                                .placeholder(R.drawable.load)
                                                .skipMemoryCache()// optional
                                                .error(R.drawable.warning)
                                                // optional
                                                .into(imgDoc);

                                        ImageDownloader.downloadImageAndSaveToFile(getApplication(), imagePanURL, panObj.optString("FileName"), new ImageDownloader.SaveFileListener() {
                                            @Override
                                            public void onSaveFile(File file1) {
                                                pd.dismiss();
                                                compressedImageFilePan = file1;
                                                flag = 1;
                                                panvalflag = 1;
                                                Log.e(TAG, "compressedImageFile: "+compressedImageFilePan.getPath());
                                            }

                                            @Override
                                            public void onFileSaveFailure(String error) {
                                                pd.dismiss();
                                                Log.e(TAG, "onFileSaveFailure: "+error);
                                            }
                                        });*/
                                        flag = 1;
                                        getLoadPAN(imagePanURL, panObj.optString("FileName"),pd);
                                    }
                                }
                            } else {
                                pd.dismiss();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        pd.dismiss();
                        Log.e(TAG, "GET_PAN_DETAILS_anError: "+ anError.getErrorBody());
                    }
                });
    }

    private File convertInputStreamToFile(Uri uri, String fileNme) {
        InputStream inputStream;
        try {
            inputStream = TempPanActivity.this.getContentResolver().openInputStream(uri);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        File file = new File(TempPanActivity.this.getExternalFilesDir("/").getAbsolutePath(), fileNme);

        try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
            int read;
            byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    private void getAadhaarFront(String docUrl, String fileName, ProgressDialog pd) {
        String exe = docUrl.substring(docUrl.lastIndexOf("."));
        Picasso.with(TempPanActivity.this)
                .load(R.drawable.loading)        // Load the image from the URL
                .placeholder(R.drawable.loading)
                .skipMemoryCache()
                .error(R.drawable.warning)
                .into(imgAadharDocument);
        new Thread(new Runnable() {
            @Override
            public void run() {
                compressedImageFile = downloadDocument(docUrl,fileName);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        if (exe.equalsIgnoreCase(".pdf")){
                            Picasso.with(TempPanActivity.this)
                                    .load(R.drawable.pdff)        // Load the image from the URL
                                    .placeholder(R.drawable.loading)
                                    .skipMemoryCache()
                                    .error(R.drawable.warning)
                                    .into(imgAadharDocument);
                        } else {
                            Picasso.with(TempPanActivity.this)
                                    .load(docUrl)
                                    .placeholder(R.drawable.loading)
                                    .skipMemoryCache()// optional
                                    .error(R.drawable.warning)
                                    .into(imgAadharDocument);
                        }

                    }
                });
            }
        }).start();

    }

    private void getAadhaarBack(String docUrl, String fileName, ProgressDialog pd) {
        String exe = docUrl.substring(docUrl.lastIndexOf("."));
        Picasso.with(TempPanActivity.this)
                .load(R.drawable.loading)        // Load the image from the URL
                .placeholder(R.drawable.loading)
                .skipMemoryCache()
                .error(R.drawable.warning)
                .into(imgAadharBackDocument);
        new Thread(new Runnable() {
            @Override
            public void run() {
                file = downloadDocument(docUrl,fileName);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        if (exe.equalsIgnoreCase(".pdf")){
                            Picasso.with(TempPanActivity.this)
                                    .load(R.drawable.pdff)        // Load the image from the URL
                                    .placeholder(R.drawable.loading)
                                    .skipMemoryCache()
                                    .error(R.drawable.warning)
                                    .into(imgAadharBackDocument);
                        } else {
                            Picasso.with(TempPanActivity.this)
                                    .load(docUrl)
                                    .placeholder(R.drawable.loading)
                                    .skipMemoryCache()// optional
                                    .error(R.drawable.warning)
                                    .into(imgAadharBackDocument);
                        }
                    }
                });
            }
        }).start();

    }

    private void getLoadPAN(String docUrl, String fileName, ProgressDialog pd) {
        String exe = docUrl.substring(docUrl.lastIndexOf("."));
        Picasso.with(TempPanActivity.this)
                .load(R.drawable.loading)        // Load the image from the URL
                .placeholder(R.drawable.loading)
                .skipMemoryCache()
                .error(R.drawable.warning)
                .into(imgDoc);
        new Thread(new Runnable() {
            @Override
            public void run() {
                compressedImageFilePan = downloadDocument(docUrl,fileName);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        if (exe.equalsIgnoreCase(".pdf")){
                            Picasso.with(TempPanActivity.this)
                                    .load(R.drawable.pdff)        // Load the image from the URL
                                    .placeholder(R.drawable.loading)
                                    .skipMemoryCache()
                                    .error(R.drawable.warning)
                                    .into(imgDoc);
                        } else {
                            Picasso.with(TempPanActivity.this)
                                    .load(docUrl)
                                    .placeholder(R.drawable.loading)
                                    .skipMemoryCache()// optional
                                    .error(R.drawable.warning)
                                    .into(imgDoc);
                        }
                    }
                });
            }
        }).start();

    }

    private File downloadDocument(String url, String fileName) {
        // Create OkHttp client to handle the download

        OkHttpClient client = new OkHttpClient();

        // Create the request to download the PDF
        okhttp3.Request request = new okhttp3.Request.Builder().url(url).build();

        // Keep the Call object reference to be able to cancel it later
        Call downloadCall = client.newCall(request);

        try {
            // Execute the request
            okhttp3.Response response = downloadCall.execute();

            if (response.isSuccessful()) {
                // Define the file where the PDF will be saved
                File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
                //File pdfFile = new File(Environment.getExternalStorageDirectory(), fileName);

                // Create the output stream to save the file
                try (InputStream inputStream = response.body().byteStream();
                     OutputStream outputStream = new FileOutputStream(pdfFile)) {

                    byte[] buffer = new byte[4096];
                    int bytesRead;

                    // Read from input stream and write to output stream
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }

                    // Return the saved file
                    return pdfFile;
                } catch (IOException e) {
                    Log.e(TAG, "Error saving PDF", e);
                    return null;
                }
            } else {
                Log.e(TAG, "Failed to download PDF. Response code: " + response.code());
                return null;
            }
        } catch (IOException e) {
            Log.e(TAG, "Error downloading PDF", e);
            return null;
        }
    }


    private void getAddharFrontID(JSONObject jsonObject) {
        ProgressDialog progressDialog=new ProgressDialog(TempPanActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        progressDialog.setCancelable(false);
        AndroidNetworking.post(AppData.COMMON_DDL)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            Log.e(TAG, "BLOOD_DROPDOWN: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code=job1.optString("Response_Code");
                            if (Response_Code.equals("101")){
                                JSONArray Response_Data=job1.optJSONArray("Response_Data");
                                for (int i=0;i<Response_Data.length();i++){
                                    JSONObject obj=Response_Data.optJSONObject(i);
                                    String id=obj.optString("id");
                                    String value=obj.optString("value");
                                    if (value.equals("Aadhaar Card")){
                                        frontID=id;
                                    }
                                    if (value.equals("Aadhar Card-Back")){
                                        backID=id;
                                    }
                                }

                                JSONObject obj=new JSONObject();
                                try {
                                    obj.put("ddltype", "Doc_Pan");
                                    obj.put("SecurityCode",pref.getSecurityCode());
                                    getPANID(obj);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "BLOOD_DROPDOWN_error: "+anError.getErrorBody());
                       progressDialog.dismiss();
                    }
                });
    }


    private void getPANID(JSONObject jsonObject) {
        ProgressDialog progressDialog=new ProgressDialog(TempPanActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        progressDialog.setCancelable(false);
        AndroidNetworking.post(AppData.COMMON_DDL)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            Log.e(TAG, "BLOOD_DROPDOWN: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code=job1.optString("Response_Code");
                            if (Response_Code.equals("101")){
                                JSONArray Response_Data=job1.optJSONArray("Response_Data");
                                for (int i=0;i<Response_Data.length();i++){
                                    JSONObject obj=Response_Data.optJSONObject(i);
                                    panID=obj.optString("id");


                                }


                            }


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "BLOOD_DROPDOWN_error: "+anError.getErrorBody());
                        progressDialog.dismiss();
                    }
                });
    }

}
