package io.cordova.myapp00d753.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import id.zelory.compressor.Compressor;
import io.cordova.myapp00d753.AndroidXCamera.AndroidXCameraActivity;
import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.metso.MetsoPMSTargetAchivementActivity;
import io.cordova.myapp00d753.bluedart.BlueDartAttendanceManageActivity;
import io.cordova.myapp00d753.module.AttendanceService;
import io.cordova.myapp00d753.module.UploadObject;
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

public class TempPanActivity extends AppCompatActivity {
    LinearLayout llSubmit;
    ImageView imgDoc,imgCamera;
    String userChoosenTask = "";
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
    LinearLayout llPANVAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_pan);
        initialize();
        onClick();
    }

    private void initialize(){
        pref = new Pref(TempPanActivity.this);
        llPANVAL=(LinearLayout)findViewById(R.id.llPANVAL);
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



    }

    private void onClick(){
        etPanNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etPanNumber.getText().toString().length()==10){

                }

            }
        });
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
                        }

                    }else {
                        Toast.makeText(getApplicationContext(),"Please enter valid PAN number",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"Please enter valid PAN number",Toast.LENGTH_LONG).show();

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
                attechmentAlert(200,1001);
            }
        });


        imgAadharCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attechmentAlert(300,2001);
            }
        });


        imgAadharBackCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attechmentAlert(400,3001);
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
            Log.e("TAG", "onActivityResult: "+data.getExtras().get("picture"));
            Log.e("TAG", "onActivityResult: "+data.getExtras().get(AndroidXCameraActivity.IMAGE_PATH_KEY));
            image_uri =  Uri.parse(String.valueOf(data.getExtras().get("picture")));
            //image_uri = (Uri) data.getExtras().get(AndroidXCameraActivity.IMAGE_PATH_KEY);
           compressedImageFile = new File(String.valueOf(data.getExtras().get("picture")));

            if (image_uri != null){
                imgDoc.setImageURI(image_uri);
                flag=1;

            }
        }else if ((requestCode == 200 )) {
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

                    imgDoc.setImageBitmap(bm);

                    flag = 1;


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
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


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }

        }else if (requestCode == 2000 && resultCode == 3001){
            Log.e("TAG", "onActivityResult: "+data.getExtras().get("picture"));
            Log.e("TAG", "onActivityResult: "+data.getExtras().get(AndroidXCameraActivity.IMAGE_PATH_KEY));
            image_uri =  Uri.parse(String.valueOf(data.getExtras().get("picture")));
            //image_uri = (Uri) data.getExtras().get(AndroidXCameraActivity.IMAGE_PATH_KEY);
            file = new File(String.valueOf(data.getExtras().get("picture")));

            if (image_uri != null){
                imgAadharBackDocument.setImageURI(image_uri);
                backflag=1;

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


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
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

        //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        AndroidNetworking.upload(AppData.url+"post_empdigitaldocument")
                .addMultipartParameter("AEMEmployeeID",pref.getEmpId())
                .addMultipartParameter("DocumentID", "003")
                .addMultipartParameter("ReferenceNo", etPanNumber.getText().toString())
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


    private void attechmentAlert(int gallerycode,int cameracode) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TempPanActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_attachment, null);
        dialogBuilder.setView(dialogView);
        LinearLayout lnCamera=(LinearLayout)dialogView.findViewById(R.id.lnCamera);
        LinearLayout lnGallery=(LinearLayout)dialogView.findViewById(R.id.lnGallery);
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


    private void aadharFrontUpload() {
        progressDialog.show();

        //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        AndroidNetworking.upload(AppData.url+"post_empdigitaldocument")
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
                });

    }


    private void aadharBackUpload() {
        progressDialog.show();

        //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        AndroidNetworking.upload(AppData.url+"post_empdigitaldocument")
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
                });

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
                                    etPanNumber.setEnabled(false);
                                    llPANVAL.setVisibility(View.VISIBLE);
                                }else {
                                    panvalflag=0;
                                    Toast.makeText(TempPanActivity.this,"Sorry PAN validation failed",Toast.LENGTH_LONG).show();
                                    etPanNumber.setText("");
                                }
                            }else {
                                panvalflag=1;
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
                        pd.dismiss();



                    }

                    @Override
                    public void onError(ANError error) {

                        pd.dismiss();

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
                                if (dateOfBirth.equals(AppData.ADHARDOB)) {
                                    panvalflag=1;
                                    etPanNumber.setEnabled(false);
                                    llPANVAL.setVisibility(View.VISIBLE);
                                }else {
                                    panvalflag=0;
                                    Toast.makeText(TempPanActivity.this,"Sorry PAN validation failed",Toast.LENGTH_LONG).show();
                                    etPanNumber.setText("");
                                }
                            }else {
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



}
