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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import id.zelory.compressor.Compressor;
import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.AttendanceService;
import io.cordova.myapp00d753.module.UploadObject;
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
    TextView tvSubmit,tvSkip;
    ImageView imgBackDown,imgForward;
    String pan_pattern;
    ImageView imgHome,imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_pan);
        initialize();
        onClick();
    }

    private void initialize(){
        pref = new Pref(TempPanActivity.this);
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


    }

    private void onClick(){
        etPanNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if (etPanNumber.getText().toString().length()>0){
                  if (etPanNumber.getText().toString().length()>9){
                      if (flag==1){

                          Pattern r = Pattern.compile(pan_pattern);
                          if (regex_matcher(r, etPanNumber.getText().toString())) {
                              cameraImageDoc();
                          }else {
                              Toast.makeText(getApplicationContext(),"Invalid PAN number",Toast.LENGTH_LONG).show();
                          }
                      }else {
                          Toast.makeText(getApplicationContext(),"Please upload PAN document",Toast.LENGTH_LONG).show();
                      }

                  }else {
                      Toast.makeText(getApplicationContext(),"Please enter valid PAN number",Toast.LENGTH_LONG).show();
                  }
              }else {

              }
            }
        });

        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraIntent();
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
                            flag=1;


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


    private void cameraImageDoc() {
        progressDialog.show();

        //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody mFile = RequestBody.create(MediaType.parse(".jpg"), compressedImageFile);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", compressedImageFile.getName(), mFile);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), compressedImageFile.getName());

        Call<UploadObject> fileUpload = uploadService.uploadDocument(fileToUpload, pref.getMasterId(), "003", etPanNumber.getText().toString(), security);
        fileUpload.enqueue(new Callback<UploadObject>() {
            @Override
            public void onResponse(Call<UploadObject> call, retrofit2.Response<UploadObject> response) {
                progressDialog.dismiss();
                UploadObject extraWorkingDayModel = response.body();
                if (extraWorkingDayModel.isResponseStatus()) {
                    //  Toast.makeText(getApplicationContext(), extraWorkingDayModel.getResponseText(), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(TempPanActivity.this,TempBankActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
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
