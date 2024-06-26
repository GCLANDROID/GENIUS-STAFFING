package io.cordova.myapp00d753.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.developers.imagezipper.ImageZipper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.AttendanceManageModule;
import io.cordova.myapp00d753.module.AttendanceService;
import io.cordova.myapp00d753.utility.ApiClient;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.GPSTracker;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;
import io.cordova.myapp00d753.utility.Pref;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AttendanceManageWithoutLocActivity extends AppCompatActivity  {
    public static final String TAG = AttendanceManageWithoutLocActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    //  private MapView mapView;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    NetworkConnectionCheck connectionCheck;
    LatLng latLng;
    TextView tvAddress;
    ImageView imgCamera, imgEmp;
    String userChoosenTask = "";
    private String encodedImage;
    private Uri imageUri;
    private static final int CAMERA_REQUEST = 1;
    private static final int GALLERY_REQUEST = 2;
    ImageView imgBack, imgHome;
    AlertDialog alerDialog1, alertDialog, alertDialog2;
    Button btnSubmit;
    Pref pref;
    String address = "N/A";
    double currentLatitude, currentLongitude;
    String address1;
    String empId;
    String lat = "0";
    String longt = "0";
    String emp;
    String a1;
    String security;
    private static final String SERVER_PATH = AppData.url;
    private AttendanceService uploadService;
    ProgressDialog progressDialog;
    File file, compressedImageFile;
    private static final int REQUEST_GALLERY_CODE = 200;
    private static final int READ_REQUEST_CODE = 300;
    private Uri uri;
    int flag;
    LinearLayout llNone, llMain;
    Button btnSubmit1;
    String formattedDate;
    String toastText;
    LinearLayout llLocation, llCamera;
    String addr;
    GoogleApiClient googleApiClient;
    int addflag = 0;
    AlertDialog alert1;
    String responseText;
    TextView tvEmpName;

    double latitude, longitude;
    String intt;
    TextView tvFace;
    LinearLayout llL;
    TextView tvToolBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_manage);
        // locationalerts();
        initialize();
       // attendanceCheck();
        onClick();
    }

    private void initialize() {
        pref = new Pref(getApplicationContext());
        empId = pref.getEmpId();
        String emp = empId;
        security = pref.getSecurityCode();
        connectionCheck = new NetworkConnectionCheck(getApplicationContext());

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds


        connectionCheck = new NetworkConnectionCheck(this);

        //  mapView = findViewById(R.id.map);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        imgCamera = (ImageView) findViewById(R.id.imgCamera);
        imgEmp = (ImageView) findViewById(R.id.imgEmp);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
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
        llNone = (LinearLayout) findViewById(R.id.llNone);
        llMain = (LinearLayout) findViewById(R.id.llMain);


        btnSubmit1 = (Button) findViewById(R.id.btnSubmit1);


        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        formattedDate = df.format(c);
        llCamera = (LinearLayout) findViewById(R.id.llCamera);

        llLocation = (LinearLayout) findViewById(R.id.llLocation);

            llCamera.setVisibility(View.VISIBLE);


            llLocation.setVisibility(View.GONE);




        final Handler handler = new Handler();

        tvEmpName = (TextView) findViewById(R.id.tvEmpName);
        tvEmpName.setText("You are here:");

        tvFace = (TextView) findViewById(R.id.tvFace);
        intt = getIntent().getStringExtra("intt");
        tvToolBar=(TextView)findViewById(R.id.tvToolBar);

        if (intt.equals("2")) {
            btnSubmit.setVisibility(View.VISIBLE);
            tvFace.setText("Upload Your Image");
            tvToolBar.setText("Manage Attendance");
        } else {
            btnSubmit.setVisibility(View.GONE);
            tvFace.setVisibility(View.VISIBLE);
            tvFace.setText("Recognize My Face");
            tvToolBar.setText("Manage Attendance With Face");
        }

        llL = (LinearLayout) findViewById(R.id.llL);


    }

    private void onClick() {
        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (intt.equals("2")) {
                    cameraIntent();
                } else {
                    Intent intent = new Intent(AttendanceManageWithoutLocActivity.this, FaceRecognitation.class);
                    intent.putExtra("address", address1);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AttendanceManageWithoutLocActivity.this, EmployeeDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionCheck.isNetworkAvailable()) {
                   imgemandatoryCheck();
                } else {
                    connectionCheck.getNetworkActiveAlert().show();
                }


            }
        });
        btnSubmit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attendanceGivenfunction();
            }
        });

    }













    private void cameraIntent() {
        flag = 1;
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Picture");
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

                            //messageAlert();
                            String imageurl = /*"file://" +*/ getRealPathFromURIPath(imageUri);
                            file = new File(imageurl);
                            compressedImageFile = new ImageZipper(AttendanceManageWithoutLocActivity.this)
                                    .setQuality(75)
                                    .setMaxWidth(300)
                                    .setMaxHeight(300)
                                    .compressToFile(file);
                            Log.d("imageSixw", String.valueOf(getReadableFileSize(compressedImageFile.length())));

                            BitmapFactory.Options o = new BitmapFactory.Options();
                            o.inSampleSize = 6;
                            Bitmap bm = cropToSquare(BitmapFactory.decodeFile(imageurl, o));
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
                            byte[] b = baos.toByteArray();
                            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                            imgEmp.setImageBitmap(bm);


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


    private String getRealPathFromURIPath(Uri contentURI) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentURI, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static Bitmap cropToSquare(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = (height > width) ? width : height;
        int newHeight = (height > width) ? height - (height - width) : height;
        int cropW = (width - height) / 2;
        cropW = (cropW < 0) ? 0 : cropW;
        int cropH = (height - width) / 2;
        cropH = (cropH <  0) ? 0 : cropH;
        Bitmap cropImg = Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);

        return cropImg;
    }


    private void attendance() {
        btnSubmit.setVisibility(View.GONE);
        llL.setVisibility(View.VISIBLE);
        AndroidNetworking.upload(AppData.url + "post_attedance")
                .addMultipartFile("File", compressedImageFile)
                .addMultipartParameter("AEMEmployeeID", pref.getEmpId())
                .addMultipartParameter("Address", "")
                .addMultipartParameter("Longitude", "")
                .addMultipartParameter("Latitude", "")
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {


                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        btnSubmit.setVisibility(View.VISIBLE);
                        llL.setVisibility(View.GONE);
                        JSONObject job = response;
                        responseText=job.optString("responseText");
                        boolean responseStatus = job.optBoolean("responseStatus");

                            successAlert();



                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        btnSubmit.setVisibility(View.VISIBLE);
                        llL.setVisibility(View.GONE);
                        Toast.makeText(AttendanceManageWithoutLocActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();

                    }
                });
    }




    private void attendanceGivenfunction() {


        if (!address.equals("")) {
            addr = address;
        } else {
            addr = "default";
        }

        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("uploading...");
        progressBar.show();
        Call<AttendanceManageModule> datumCall = ApiClient.getService().getDatas(empId, "", "", "", pref.getSecurityCode());
        datumCall.enqueue(new Callback<AttendanceManageModule>() {
            @Override
            public void onResponse(Call<AttendanceManageModule> call, Response<AttendanceManageModule> response) {
                progressBar.dismiss();
                AttendanceManageModule extraWorkingDayModel = response.body();
                if (extraWorkingDayModel.isResponseStatus()) {
                    responseText = extraWorkingDayModel.getResponseText();
                    successAlert();
                    Log.d("riku", "withoutcamera");
                } else {
                    // Toast.makeText(getApplicationContext(), extraWorkingDayModel.getResponseText(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AttendanceManageModule> call, Throwable t) {

            }


        });
    }


    private void successAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AttendanceManageWithoutLocActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);
        if (addflag == 1) {
            tvInvalidDate.setText("Your attendnace has been saved successfully");
        } else {
            tvInvalidDate.setText("Your attendnace has been saved successfully");
        }
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                Intent intent=new Intent(AttendanceManageWithoutLocActivity.this,AttendanceReportActivity.class);
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


    private void messageAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AttendanceManageWithoutLocActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_message, null);
        dialogBuilder.setView(dialogView);

        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                attendance();


            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }


    private void attendanceCheck() {
        String surl = AppData.url + "gcl_FetchEmployeeAttendanceByDate?AEMEmployeeID=" + pref.getEmpId() + "&Adate=" + formattedDate + "&CurrentPage=1&Operation=1&SecurityCode=" + pref.getSecurityCode();
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLeave", response);
                        progressBar.dismiss();
                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                toastText = job1.optString("responseText");
                                //Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                atteAlert();

                            } else {

                            }


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            //   Toast.makeText(AttendanceManageActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                // Toast.makeText(AttendanceManageActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");

    }

    private void atteAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AttendanceManageWithoutLocActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_attendate, null);
        dialogBuilder.setView(dialogView);
        TextView tvAttenDate = (TextView) dialogView.findViewById(R.id.tvAttenDate);
        tvAttenDate.setText(toastText);
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(true);
        Window window = alertDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog.show();
    }


    private void locationAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AttendanceManageWithoutLocActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_location, null);
        dialogBuilder.setView(dialogView);

        Button btnOk = (Button) dialogView.findViewById(R.id.btnGPSOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog2.dismiss();
                // turnGPSOn();
                //  attendance();


            }
        });

        alertDialog2 = dialogBuilder.create();
        alertDialog2.setCancelable(true);
        Window window = alertDialog2.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog2.show();
    }







    public String getReadableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    private void imgemandatoryCheck(){
        if (pref.getEmpClintId().equals("AEMCLI1410000807")){
            if (flag == 1) {


                attendance();
            } else {
                Toast.makeText(AttendanceManageWithoutLocActivity.this,"Please Capture Selfie Image",Toast.LENGTH_LONG).show();
            }
        }else {
            if (flag == 1) {


                attendance();
            } else {
                attendanceGivenfunction();
            }
        }
    }
}
