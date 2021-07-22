package io.cordova.myapp00d753.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.developers.imagezipper.ImageZipper;
import com.google.android.cameraview.LongImageCameraActivity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


import id.zelory.compressor.Compressor;
import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.AttendanceService;
import io.cordova.myapp00d753.module.MainDocModule;
import io.cordova.myapp00d753.module.SubDocumentModule;
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

public class DocumentManageActivity extends AppCompatActivity {
    ImageView imgDoc;
    String userChoosenTask = "";
    private String encodedImage;
    private Uri imageUri;
    private static final int CAMERA_REQUEST = 1;
    private static final int GALLERY_REQUEST = 2;
    ImageView imgBack, imgHome;
    Spinner spDocType, spDoc;

    int flag;


    ProgressDialog progressDialog;
    File file, compressedImageFile;
    private static final int REQUEST_GALLERY_CODE = 200;
    private static final int READ_REQUEST_CODE = 300;
    private Uri uri;
    LinearLayout llUpload;

    int flag1;
    ArrayList<MainDocModule> mainDocType = new ArrayList<>();
    ArrayList<String> doctype = new ArrayList<>();
    String mainDocument;
    ArrayList<SubDocumentModule> subDocType = new ArrayList<>();
    ArrayList<String> subDoc = new ArrayList<>();
    LinearLayout llSp;
    private static final String SERVER_PATH = AppData.url;
    private AttendanceService uploadService;
    Pref pref;
    String subDocumentID;
    EditText etReferenceNumber;
    LinearLayout llSave;
    AlertDialog alerDialog1, alertDialog;
    String securityCode;

    String aempEmployeeid;
    String document;
    private static final int PDF_REQUEST = 9;
    File pdffile;
    String imageFileName;
    File pictureFile;
    AlertDialog alerDialog3;
    public int PIC_CODE=0;;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_manage);
        initialize();

        onClick();
    }

    private void initialize() {
        pref = new Pref(DocumentManageActivity.this);

        imgDoc = (ImageView) findViewById(R.id.imgDoc);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);

        spDocType = (Spinner) findViewById(R.id.spDocType);
        spDoc = (Spinner) findViewById(R.id.spDoc);
        llUpload=(LinearLayout)findViewById(R.id.llUpload);


        llSp = (LinearLayout) findViewById(R.id.llSp);
        etReferenceNumber = (EditText) findViewById(R.id.etReferenceNumber);
        llSave = (LinearLayout) findViewById(R.id.llSave);
        setMainDoc();
        securityCode = pref.getSecurityCode();


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(360, TimeUnit.SECONDS)
                .connectTimeout(360, TimeUnit.SECONDS)
                .build();

        // Change base URL to your upload server URL.
        uploadService = (AttendanceService) new Retrofit.Builder()
                .baseUrl(SERVER_PATH)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AttendanceService.class);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        if (pref.getUserType().equals("1")) {
            aempEmployeeid = pref.getEmpId();
        } else if (pref.getUserType().equals("4")) {
            aempEmployeeid = pref.getMasterId();
            Log.d("aempEmployeeid", aempEmployeeid);
        }


    }

    private void onClick() {


        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pref.getUserType().equals("1")) {
                    Intent intent = new Intent(DocumentManageActivity.this, EmployeeDashBoardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (pref.getUserType().equals("4")) {
                    Intent intent = new Intent(DocumentManageActivity.this, TempDashBoardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                // finish();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        spDocType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                mainDocument = mainDocType.get(i).getDocID();

                setSubDocType();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spDoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subDocumentID = subDocType.get(i).getDocumentID();
                document = subDocType.get(i).getDocument();
                Log.d("document", subDocumentID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        llUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCameraDialog();
            }
        });



        llSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag1 == 1) {
                    if (document.equals("Aadhar Card")) {
                        if (etReferenceNumber.getText().toString().length() == 12) {
                            cameraImageDoc();

                        } else {
                            Toast.makeText(DocumentManageActivity.this, "Please enter valid Aadhaar Number", Toast.LENGTH_LONG).show();
                        }
                    } else if (document.equals("PAN Card")) {
                        if (etReferenceNumber.getText().toString().length() == 12) {
                            cameraImageDoc();

                        } else {
                            Toast.makeText(DocumentManageActivity.this, "Please enter valid PAN number", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (etReferenceNumber.getText().toString().length() > 0) {
                            cameraImageDoc();
                        } else {
                            Toast.makeText(DocumentManageActivity.this, "Please enter reference  number", Toast.LENGTH_LONG).show();

                        }

                    }
                } else if (flag1 == 2) {
                    if (etReferenceNumber.getText().toString().length() > 0) {
                        saveAttachDoc();
                    } else {
                        Toast.makeText(DocumentManageActivity.this, "please enter refernce number", Toast.LENGTH_LONG).show();

                    }
                } else if (flag1 == 3) {
                    if (etReferenceNumber.getText().toString().length() > 0) {
                        pdfFileSend();
                    } else {
                        Toast.makeText(DocumentManageActivity.this, "please enter refernce number", Toast.LENGTH_LONG).show();

                    }
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


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_REQUEST:

                if (resultCode == Activity.RESULT_OK) {
                    try {
                        try {

                            String imageurl = "file://" + getRealPathFromURI(imageUri);
                            file = new File(imageurl);
                            compressedImageFile =new ImageZipper(DocumentManageActivity.this)
                                    .setQuality(100)
                                    .setMaxWidth(300)
                                    .setMaxHeight(300)
                                    .compressToFile(file);
                            Log.d("imageSixw", String.valueOf(getReadableFileSize(compressedImageFile.length())));
                            BitmapFactory.Options o = new BitmapFactory.Options();
                            o.inSampleSize = 2;
                            Bitmap bm = cropToSquare(BitmapFactory.decodeFile(imageurl, o));
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.PNG, 10, baos); //bm is the bitmap object
                            byte[] b = baos.toByteArray();
                            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                            Log.d("images", encodedImage);

                            flag1 = 1;
                            imgDoc.setImageBitmap(bm);
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
            case REQUEST_GALLERY_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    InputStream imageStream = null;
                    try {
                        try {
                            uri = data.getData();
                            String filePath = getRealPathFromURIPath(uri, DocumentManageActivity.this);
                            file = new File(filePath);
                            //  Log.d(TAG, "filePath=" + filePath);
                            imageStream = getContentResolver().openInputStream(uri);
                            Bitmap bm = cropToSquare(BitmapFactory.decodeStream(imageStream));
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.PNG, 10, baos); //bm is the bitmap object
                            byte[] b = baos.toByteArray();
                            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                            alerDialog3.dismiss();
                            imgDoc.setImageBitmap(bm);

                            flag1 = 2;


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }

                }
                break;

            case PDF_REQUEST:
                if (requestCode == PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

                    Uri selectedFileURI = data.getData();
                    pdffile = new File(getRealPDFPathFromURI(selectedFileURI));

                    flag1 = 3;
                    alerDialog1.dismiss();


                }


                break;

            case LongImageCameraActivity.LONG_IMAGE_RESULT_CODE:


                if (resultCode == RESULT_OK && requestCode == LongImageCameraActivity.LONG_IMAGE_RESULT_CODE) {
                    imageFileName = data.getStringExtra(LongImageCameraActivity.IMAGE_PATH_KEY);
                    Log.d("imageFileName", imageFileName);
                    Bitmap d = BitmapFactory.decodeFile(imageFileName);
                    int newHeight = (int) (d.getHeight() * (512.0 / d.getWidth()));
                    Bitmap putImage = Bitmap.createScaledBitmap(d, 512, newHeight, true);
                    imgDoc.setImageBitmap(putImage);
                    flag1 = 1;
                     pictureFile = (File)data.getExtras().get("picture");
                    Log.d("fjjgk",pictureFile.toString());
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


    private void setMainDoc() {

        String surl = AppData.url+"gcl_DigitalDocumentType?DocID=0&DocumentID=0&DbOperation=1&SecurityCode=" + pref.getSecurityCode();
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
                                    String DocID = obj.optString("DocID");
                                    String DocumentType = obj.optString("DocumentType");
                                    doctype.add(DocumentType);
                                    MainDocModule mainDocModule = new MainDocModule(DocID, DocumentType);
                                    mainDocType.add(mainDocModule);

                                }
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (DocumentManageActivity.this, android.R.layout.simple_spinner_item,
                                                doctype); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spDocType.setAdapter(spinnerArrayAdapter);
                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DocumentManageActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
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


    private void setSubDocType() {
        llSp.setVisibility(View.VISIBLE);
        String surl = AppData.url+"gcl_DigitalDocumentType?DocID=1&DocumentID=" + mainDocument + "&DbOperation=2&SecurityCode=" + pref.getSecurityCode();
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        subDoc.clear();
                        subDocType.clear();


                        Log.d("responseLogin", response);
                        progressBar.dismiss();
                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                //  Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String DocumentID = obj.optString("DocumentID");
                                    String Document = obj.optString("Document");
                                    subDoc.add(Document);
                                    SubDocumentModule mainDocModule = new SubDocumentModule(DocumentID, Document);
                                    subDocType.add(mainDocModule);

                                }


                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (DocumentManageActivity.this, android.R.layout.simple_spinner_item,
                                                subDoc); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spDoc.setAdapter(spinnerArrayAdapter);
                            } else {
                                llSp.setVisibility(View.GONE);

                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DocumentManageActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                //  Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");


    }

    private void cameraImageDoc() {
        progressDialog.show();

        //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody mFile = RequestBody.create(MediaType.parse(".png"),   compressedImageFile);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", compressedImageFile.getName(), mFile);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), compressedImageFile.getName());

        Call<UploadObject> fileUpload = uploadService.uploadDocument(fileToUpload, aempEmployeeid, subDocumentID, etReferenceNumber.getText().toString(), securityCode);
        fileUpload.enqueue(new Callback<UploadObject>() {
            @Override
            public void onResponse(Call<UploadObject> call, retrofit2.Response<UploadObject> response) {
                progressDialog.dismiss();
                UploadObject extraWorkingDayModel = response.body();
                if (extraWorkingDayModel.isResponseStatus()) {
                    //  Toast.makeText(getApplicationContext(), extraWorkingDayModel.getResponseText(), Toast.LENGTH_SHORT).show();
                    successAlert(extraWorkingDayModel.getResponseText());
                    alerDialog3.dismiss();

                } else {
                     Toast.makeText(getApplicationContext(), extraWorkingDayModel.getResponseText(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UploadObject> call, Throwable t) {
                progressDialog.dismiss();

                Log.e("error", "Error " + t.getMessage());
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                messageAlert();
            }

        });

    }

    private void pdfFileSend() {
        Log.d("apihit", "pdf");
        progressDialog.show();

        //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        Call<UploadObject> call = null;
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), pdffile);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", pdffile.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), pdffile.getName());

        Call<UploadObject> fileUpload = uploadService.uploadDocument(fileToUpload, aempEmployeeid, subDocumentID, etReferenceNumber.getText().toString(), securityCode);
        fileUpload.enqueue(new Callback<UploadObject>() {
            @Override
            public void onResponse(Call<UploadObject> call, retrofit2.Response<UploadObject> response) {
                progressDialog.dismiss();
                UploadObject extraWorkingDayModel = response.body();
                if (extraWorkingDayModel.isResponseStatus()) {
                    //  Toast.makeText(getApplicationContext(), extraWorkingDayModel.getResponseText(), Toast.LENGTH_SHORT).show();
                    successAlert(extraWorkingDayModel.getResponseText());

                } else {
                    // Toast.makeText(getApplicationContext(), extraWorkingDayModel.getResponseText(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UploadObject> call, Throwable t) {
                progressDialog.dismiss();

                Log.e("error", "Error " + t.getMessage());
                //  Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                messageAlert();
            }

        });

    }


    private void successAlert(String text) {
       AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DocumentManageActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);
        tvInvalidDate.setText(text);
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DocumentManageActivity.this, DocumentReportActivity.class);
                startActivity(intent);
                finish();
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


    private void messageAlert() {
       AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DocumentManageActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_message, null);
        dialogBuilder.setView(dialogView);
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                if (flag1 == 1) {
                    cameraImageDoc();
                } else if (flag1 == 2) {
                    saveAttachDoc();
                }


            }
        });

        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(true);
        Window window = alertDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog.show();
    }

    private void saveAttachDoc() {
        progressDialog.show();
        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

        Call<UploadObject> fileUpload = uploadService.uploadDocument(fileToUpload, aempEmployeeid, subDocumentID, etReferenceNumber.getText().toString(), securityCode);
        fileUpload.enqueue(new Callback<UploadObject>() {
            @Override
            public void onResponse(Call<UploadObject> call, retrofit2.Response<UploadObject> response) {
                progressDialog.dismiss();
                UploadObject extraWorkingDayModel = response.body();
                if (extraWorkingDayModel.isResponseStatus()) {
                    // Toast.makeText(getApplicationContext(), extraWorkingDayModel.getResponseText(), Toast.LENGTH_SHORT).show();
                    successAlert(extraWorkingDayModel.getResponseText());

                } else {
                    //  Toast.makeText(getApplicationContext(), extraWorkingDayModel.getResponseText(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UploadObject> call, Throwable t) {
                progressDialog.dismiss();

                Log.e("error", "Error " + t.getMessage());
                // Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                messageAlert();
            }

        });

    }

    public String getReadableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }


    private void openAttachDialog() {
       AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DocumentManageActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_cvoption2, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llPdf = (LinearLayout) dialogView.findViewById(R.id.llPdf);
        llPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPDFChooser();

            }
        });

        LinearLayout llGallery = (LinearLayout) dialogView.findViewById(R.id.llGallery);
        llGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
                openGalleryIntent.setType("image/*");
                startActivityForResult(openGalleryIntent, REQUEST_GALLERY_CODE);

            }
        });


        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }


    private void openCameraDialog() {
       AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DocumentManageActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_cameraimage, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llSingle = (LinearLayout) dialogView.findViewById(R.id.llSingle);
        llSingle.setVisibility(View.GONE);
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
                LongImageCameraActivity.launch(DocumentManageActivity.this);

            }
        });
        LinearLayout llGallery=(LinearLayout)dialogView.findViewById(R.id.llGallery);
        llGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
                openGalleryIntent.setType("image/*");
                startActivityForResult(openGalleryIntent, REQUEST_GALLERY_CODE);
            }
        });


        alerDialog3 = dialogBuilder.create();
        alerDialog3.setCancelable(true);
        Window window = alerDialog3.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog3.show();
    }

    private void showPDFChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PDF_REQUEST);


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String getRealPDFPathFromURI(Uri contentURI) {
        final String id = DocumentsContract.getDocumentId(contentURI);
        final Uri contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);

    }


}
