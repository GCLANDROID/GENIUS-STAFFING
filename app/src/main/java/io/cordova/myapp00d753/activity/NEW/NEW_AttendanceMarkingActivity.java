package io.cordova.myapp00d753.activity.NEW;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.developers.imagezipper.ImageZipper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.cordova.myapp00d753.AndroidXCamera.AndroidXCameraActivity;
import io.cordova.myapp00d753.AndroidXCamera.FrontAndroidXCameraActivity;
import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.Retrofit.RetrofitClient;
import io.cordova.myapp00d753.activity.EmployeeDashBoardActivity;
import io.cordova.myapp00d753.activity.attendance.MetsoAttendanceActivity;
import io.cordova.myapp00d753.activity.attendance.MetsoAttendanceReportActivity;
import io.cordova.myapp00d753.activity.metso.MetsoNewReimbursementClaimActivity;
import io.cordova.myapp00d753.activity.metso.adapter.LocationSpinnerAdapter;
import io.cordova.myapp00d753.activity.metso.adapter.ShiftSpinnerAdapter;
import io.cordova.myapp00d753.activity.metso.adapter.SupervisorFilterAdapter;
import io.cordova.myapp00d753.activity.metso.model.LocationSpinnerModel;
import io.cordova.myapp00d753.activity.metso.model.MetsoLocationModel;
import io.cordova.myapp00d753.activity.metso.model.MetsoShiftModel;
import io.cordova.myapp00d753.activity.metso.model.ShiftSpinnerModel;
import io.cordova.myapp00d753.module.HolidayMarkModel;
import io.cordova.myapp00d753.module.SpineerItemModel;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.GPSTracker;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.ShowDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NEW_AttendanceMarkingActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{
    private static final String TAG = "NEW_AttendanceMarkingAc";
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    FusedLocationProviderClient fusedLocationProviderClient;
    GoogleApiClient mGoogleClient;
    private LocationRequest mLocationRequest;
    SupportMapFragment smf;
    AlertDialog shiftLocationAlertDialog;
    AlertDialog alerDialog1;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    private Dialog shiftAndLocationDialog;
    AppCompatButton btnSubmit;
    ShiftSpinnerAdapter shiftSpinnerAdapter;
    LocationSpinnerAdapter locationSpinnerAdapter;
    ArrayList<ShiftSpinnerModel> shiftList;
    ArrayList<MetsoShiftModel> metsoShiftList;
    ArrayList<LocationSpinnerModel> locationList;
    ArrayAdapter<String> shiftListAdapter;
    ArrayList<String> stringsShiftList;
    String latitude = "", longitude = "", currentAddresses = "", ClientID = "", Shiftid = "", Siteid = "", MasterID = "";
    TextView txtCurrentLocation;
    ImageView imgBack,imgAttachImage;
    double currentLatitude, currentLongitude;
    Pref pref;
    LatLng latLng;
    Dialog searchWbsCodeDialog;
    private GoogleMap mMap;
    ArrayList<MetsoLocationModel> metsoLocationArrayList;
    ProgressDialog progressDialog;
    //LocationManager locationManager;
    private LocationCallback locationCallback;
    GPSTracker gps;
    LocationListener locationListener;
    String address1;
    String address = "N/A";
    LinearLayout llLoading, llSubmit,llCamera;
    TextView tvTimer;
    AlertDialog alertDialog;
    private static final int REQUEST_PHONE_STATE = 1;
    String phoneNumber;
    ImageView imhHome,btnImageClick;
    String SupervisorID = "";
    ArrayList<SpineerItemModel> supervisorList = new ArrayList<>();
    Uri image_uri;
    File compressedImageFile;
    String isImageRequired="",isWorkPlaceRequired="",isShiftRequired="",isApproverRequired="";
    int imageSelectionFlag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_attendance_marking);
        intiView();
        settingToGetLocation();
        //buildGoogleApiClient();
        //getLocations();

        //getMetsoShift();
        btnClick();

    }

    private void intiView() {

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
        llLoading = findViewById(R.id.llLoading);
        llSubmit = findViewById(R.id.llSubmit);
        llCamera = findViewById(R.id.llCamera);
        tvTimer = findViewById(R.id.tvTimer);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnImageClick = findViewById(R.id.btnImageClick);
        txtCurrentLocation = findViewById(R.id.txtCurrentLocation);
        imgBack = findViewById(R.id.imgBack);
        imgAttachImage = findViewById(R.id.imgAttachImage);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        imhHome=(ImageView)findViewById(R.id.imhHome);
        //smf = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fmGoogleMaps);
        //smf.getMapAsync(this);

        pref = new Pref(NEW_AttendanceMarkingActivity.this);
        ClientID = pref.getEmpClintId();
        MasterID = pref.getMasterId();
        shiftAndLocationDialog = new Dialog(NEW_AttendanceMarkingActivity.this);
        getRequiredStatus();
        progressDialog = new ProgressDialog(NEW_AttendanceMarkingActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        phoneNumber=getAndroidID(NEW_AttendanceMarkingActivity.this);

        JSONObject obj = new JSONObject();
        try {
            obj.put("CompanyID", pref.getEmpClintId());
            obj.put("EmployeeID", pref.getEmpId());
            obj.put("IMEINo", phoneNumber);
            obj.put("Action", 1);
            obj.put("Username", "A");
            obj.put("SecurityCode", pref.getSecurityCode());
            savedeviceID(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        imhHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(NEW_AttendanceMarkingActivity.this, EmployeeDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        getRequiredList();
    }

    private void getRequiredStatus() {
        String MarkAttnFromMobileWithImage = getIntent().getStringExtra("MarkAttnFromMobileWithImage");
        String[] MarkAttnFromMobileWithImageArr = MarkAttnFromMobileWithImage.split("_");
        isImageRequired = MarkAttnFromMobileWithImageArr[0];
        //isImageRequired = "0";
        llCamera.setVisibility(isImageRequired.equals("1")?View.VISIBLE:View.GONE);
        String MarkAttnFromMobileWithWorkPlaceSelection = getIntent().getStringExtra("MarkAttnFromMobileWithWorkPlaceSelection");
        String[] MarkAttnFromMobileWithWorkPlaceSelectionArr = MarkAttnFromMobileWithWorkPlaceSelection.split("_");
        isWorkPlaceRequired = MarkAttnFromMobileWithWorkPlaceSelectionArr[0];
        //isWorkPlaceRequired = "1";
        String MarkAttnFromMobileWithShiftSelection =getIntent().getStringExtra("MarkAttnFromMobileWithShiftSelection");
        String[] MarkAttnFromMobileWithShiftSelectionArr = MarkAttnFromMobileWithShiftSelection.split("_");
        isShiftRequired = MarkAttnFromMobileWithShiftSelectionArr[0];
        //isShiftRequired = "1";
        String MarkAttnFromMobileWithHierarchySelection = getIntent().getStringExtra("MarkAttnFromMobileWithHierarchySelection");
        String[] MarkAttnFromMobileWithHierarchySelectionArr = MarkAttnFromMobileWithHierarchySelection.split("_");
        isApproverRequired = MarkAttnFromMobileWithHierarchySelectionArr[0];
        //isApproverRequired = "1";
        Log.e(TAG, "intiView: \nisImageRequired: "+isImageRequired
                +"\nisWorkPlaceRequired: "+isWorkPlaceRequired
                +"\nisShiftRequired: "+isShiftRequired
                +"\nisApproverRequired: "+isApproverRequired);
    }


    private void getRequiredList() {
        progressDialog.show();
        try {
            metsoShiftList = getShiftData(pref.getShiftList());
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            shiftSpinnerAdapter = new ShiftSpinnerAdapter(NEW_AttendanceMarkingActivity.this, metsoShiftList);
        }

        try {
            metsoLocationArrayList = getLocationData(pref.getLocationList());
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            locationSpinnerAdapter = new LocationSpinnerAdapter(NEW_AttendanceMarkingActivity.this, metsoLocationArrayList);
        }

        try {
            supervisorList = getApproverData(pref.getApproverList());
        } catch (Exception e){
            e.printStackTrace();
        }
        progressDialog.dismiss();
    }

    private void btnClick() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isImageRequired.equals("1") && imageSelectionFlag == 0) {
                    Toast.makeText(NEW_AttendanceMarkingActivity.this,"Please capture your image",Toast.LENGTH_SHORT).show();
                }else if (isShiftRequired.equals("1") || isWorkPlaceRequired.equals("1")|| isApproverRequired.equals("1")){
                    openShiftAndLocationPopup();
                } else {
                    submitAttendance();
                }
            }
        });
        btnImageClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FrontAndroidXCameraActivity.launch(NEW_AttendanceMarkingActivity.this, 8001);
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private ArrayList<MetsoShiftModel> getShiftData(String ShiftList) throws JSONException {
        ArrayList<MetsoShiftModel> mShiftList = new ArrayList<>();
        JSONArray ShiftListArray = new JSONArray(ShiftList);
        mShiftList.add(new MetsoShiftModel(0,"Select Shift"));
        if (ShiftListArray.length()>0){
            for (int i = 0; i < ShiftListArray.length(); i++) {
                JSONObject obj = ShiftListArray.optJSONObject(i);
                long WorkingShiftID = obj.optLong("WorkingShiftID");
                String WorkingShiftName = obj.optString("WorkingShiftName");
                mShiftList.add(new MetsoShiftModel(WorkingShiftID,WorkingShiftName));
            }
        }
        return mShiftList;
    }

    private ArrayList<MetsoLocationModel> getLocationData(String LocationList) throws JSONException {
        Log.e(TAG, "getLocationData: "+LocationList );
        ArrayList<MetsoLocationModel> mLocationArrayList = new ArrayList<>();
        JSONArray LocationjsonArray = new JSONArray(LocationList);
        mLocationArrayList.add(new MetsoLocationModel(0,"Select Location"));
       /* mLocationArrayList.add(new MetsoLocationModel(2,"Location 1"));
        mLocationArrayList.add(new MetsoLocationModel(1,"Location 2"));*/
        if(LocationjsonArray.length()>0){
            if (LocationjsonArray.length() > 0){
                for (int i = 0; i < LocationjsonArray.length(); i++) {
                    JSONObject obj = LocationjsonArray.optJSONObject(i);
                    int SiteId = obj.optInt("WorkPlaceID");
                    String SiteName = obj.getString("WorkPlaceName");
                    mLocationArrayList.add(new MetsoLocationModel(SiteId,SiteName));
                }
            }
        }
        return mLocationArrayList;
    }

    private ArrayList<SpineerItemModel> getApproverData(String ApproverList) throws JSONException {
        ArrayList<SpineerItemModel> mSupervisorList = new ArrayList<>();
        JSONArray ApproverjsonArray = new JSONArray(ApproverList);
        if (ApproverjsonArray.length()>0){
            for (int i = 0; i < ApproverjsonArray.length(); i++) {
                JSONObject obj = ApproverjsonArray.optJSONObject(i);
                String Code = obj.optString("Code");
                String UserName = obj.optString("UserName");
                mSupervisorList.add(new SpineerItemModel(UserName,Code));
            }
        }
        return mSupervisorList;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 8001 && resultCode == 8001){
            Log.e("TAG", "onActivityResult: "+data.getExtras().get("picture"));
            Log.e("TAG", "onActivityResult: "+data.getExtras().get(AndroidXCameraActivity.IMAGE_PATH_KEY));
            image_uri =  Uri.parse(String.valueOf(data.getExtras().get("picture")));
            //image_uri = (Uri) data.getExtras().get(AndroidXCameraActivity.IMAGE_PATH_KEY);
            File file = new File(String.valueOf(data.getExtras().get("picture")));
            try {
                compressedImageFile = new ImageZipper(this)
                        .setQuality(100)
                        .setMaxWidth(300)
                        .setMaxHeight(300)
                        .compressToFile(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (image_uri != null){
                imgAttachImage.setImageURI(image_uri);
                imgAttachImage.setVisibility(View.VISIBLE);

            }
            imageSelectionFlag=1;
        }
    }

    private void getMetsoShift() {
        progressDialog.show();
        Call<JsonObject> call = RetrofitClient
                .getInstance()
                .getApi()
                .GetMetsoAttendanceData("2", ClientID, "0000");

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                try {
                    JSONObject object = new JSONObject(String.valueOf(response.body()));
                    if (object.getBoolean("responseStatus") == true) {
                        JSONArray jsonArray = object.getJSONArray("responseData");
                        metsoShiftList = new ArrayList<>();
                        metsoShiftList.add(new MetsoShiftModel(0, "Select Shift"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject objectResponse = jsonArray.getJSONObject(i);
                            MetsoShiftModel metsoLocationModel = new MetsoShiftModel(objectResponse.getLong("WorkingShiftID"),
                                    objectResponse.getString("Column1"));
                            metsoShiftList.add(metsoLocationModel);
                        }

                        shiftSpinnerAdapter = new ShiftSpinnerAdapter(NEW_AttendanceMarkingActivity.this, metsoShiftList);

                        getMetsoLocationData();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void getMetsoLocationData() {
        Log.e(TAG, "ClientID: " + ClientID);
        Call<JsonObject> call = RetrofitClient
                .getInstance()
                .getApi()
                .GetMetsoAttendanceData("1", ClientID, "0000");

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "onResponse: Location: " + new Gson().toJson(response.body()));
                try {
                    JSONObject object = new JSONObject(String.valueOf(response.body()));
                    if (object.getBoolean("responseStatus") == true) {
                        JSONArray jsonArray = object.getJSONArray("responseData");
                        metsoLocationArrayList = new ArrayList<>();
                        metsoLocationArrayList.add(new MetsoLocationModel(0, "Select Location"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject objectResponse = jsonArray.getJSONObject(i);
                            MetsoLocationModel metsoLocationModel = new MetsoLocationModel(objectResponse.getInt("Siteid"),
                                    (String) objectResponse.getString("SiteName"));
                            metsoLocationArrayList.add(metsoLocationModel);
                        }
                        Log.e(TAG, "onResponse: SIZE: " + metsoLocationArrayList.size());
                        locationSpinnerAdapter = new LocationSpinnerAdapter(NEW_AttendanceMarkingActivity.this, metsoLocationArrayList);

                        progressDialog.cancel();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void openShiftAndLocationPopup2() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NEW_AttendanceMarkingActivity.this, R.style.CustomDialogNew2);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.shift_location_popup, null);
        dialogBuilder.setView(dialogView);


        shiftLocationAlertDialog = dialogBuilder.create();
        shiftLocationAlertDialog.setCancelable(true);
        Window window = shiftLocationAlertDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.NO_GRAVITY);
        shiftLocationAlertDialog.show();
    }



    private void settingToGetLocation() {
        mGoogleClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fmGoogleMaps);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void openShiftAndLocationPopup() {
        shiftAndLocationDialog.setContentView(R.layout.shift_location_popup);
        shiftAndLocationDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        shiftAndLocationDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        LinearLayout lnCancel = shiftAndLocationDialog.findViewById(R.id.lnCancel);
        LinearLayout llShift = shiftAndLocationDialog.findViewById(R.id.llShift);
        LinearLayout llLocation = shiftAndLocationDialog.findViewById(R.id.llLocation);
        LinearLayout llApprover = shiftAndLocationDialog.findViewById(R.id.llApprover);
        TextView txtSelectShift = shiftAndLocationDialog.findViewById(R.id.txtSelectShift);
        TextView txtSelectLocation = shiftAndLocationDialog.findViewById(R.id.txtSelectLocation);
        TextView txtSelectApprover = shiftAndLocationDialog.findViewById(R.id.txtSelectApprover);
        TextView txtErrorShift = shiftAndLocationDialog.findViewById(R.id.txtErrorShift);
        TextView txtErrorLocation = shiftAndLocationDialog.findViewById(R.id.txtErrorLocation);
        TextView txtErrorApprover = shiftAndLocationDialog.findViewById(R.id.txtErrorApprover);
        Spinner spShift = shiftAndLocationDialog.findViewById(R.id.spShift);
        Spinner spLocation = shiftAndLocationDialog.findViewById(R.id.spLocation);
        AppCompatButton btnMarkedYourAttendance = shiftAndLocationDialog.findViewById(R.id.btnMarkedYourAttendance);
        if (isApproverRequired.equals("1")){
            llApprover.setVisibility(View.VISIBLE);
        } else {
            llApprover.setVisibility(View.GONE);
        }

        if (isShiftRequired.equals("1")){
            spShift.setAdapter(shiftSpinnerAdapter);
            spShift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    MetsoShiftModel clickedItem = (MetsoShiftModel) adapterView.getItemAtPosition(i);
                    if (!clickedItem.getColumn1().equals("Select Shift")) {
                        txtSelectShift.setText(clickedItem.getColumn1());
                        Shiftid = String.valueOf(clickedItem.getWorkingShiftID());
                        txtErrorShift.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        } else {
            llShift.setVisibility(View.GONE);
        }

        if(isWorkPlaceRequired.equals("1")){
            spLocation.setAdapter(locationSpinnerAdapter);
            spLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    MetsoLocationModel clickedItem = (MetsoLocationModel) adapterView.getItemAtPosition(i);
                    if (!clickedItem.getSiteName().equals("Select Location")) {
                        txtSelectLocation.setText(clickedItem.getSiteName());
                        Siteid = String.valueOf(clickedItem.getSiteid());
                        txtErrorLocation.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        } else {
            llLocation.setVisibility(View.GONE);
        }


        txtSelectShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spShift.performClick();
            }
        });

        txtSelectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spLocation.performClick();
            }
        });

        txtSelectApprover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSupervisorPopUp(txtSelectApprover);
            }
        });

        lnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shiftAndLocationDialog.cancel();
            }
        });

        btnMarkedYourAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isImageRequired.equals("1") && imageSelectionFlag == 0){
                    Toast.makeText(NEW_AttendanceMarkingActivity.this,"Please capture your image",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isShiftRequired.equals("1") && txtSelectShift.getText().toString().trim().isEmpty()){
                    txtErrorShift.setVisibility(View.VISIBLE);
                    return;
                }
                if (isWorkPlaceRequired.equals("1") && txtSelectLocation.getText().toString().trim().isEmpty()){
                    txtErrorLocation.setVisibility(View.VISIBLE);
                    return;
                }
                if (isApproverRequired.equals("1") && txtSelectApprover.getText().toString().trim().isEmpty()){
                    txtErrorApprover.setVisibility(View.VISIBLE);
                    return;
                } else {
                    txtErrorApprover.setVisibility(View.GONE);
                }
                submitAttendance();
            }
        });

        Window window = shiftAndLocationDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.NO_GRAVITY;
        shiftAndLocationDialog.setCancelable(false);
        shiftAndLocationDialog.show();
    }

    private void submitAttendance() {
        progressDialog.show();
        Log.e(TAG, "submitAttendance: "
                + "\nConsultantID: "+pref.getEmpConId()
                + "\nClientID: "+pref.getEmpClintId()
                + "\nAEMEmployeeID: "+pref.getEmpId()
                + "\nAddress: "+address
                + "\nShiftid: "+Shiftid
                + "\nWorkPlaceID: "+Siteid
                + "\nApproverid: "+SupervisorID
                + "\nLongitude: "+longitude
                + "\nLatitude: "+latitude
                + "\nSecurityCode: "+pref.getSecurityCode()
        );


        AndroidNetworking.upload(AppData.LAMS_SaveAttendance)
                .addMultipartParameter("ConsultantID", pref.getEmpConId())
                .addMultipartParameter("ClientID", pref.getEmpClintId())
                .addMultipartParameter("AEMEmployeeID", pref.getEmpId())
                .addMultipartParameter("Address", address)
                .addMultipartParameter("Longitude", longitude)
                .addMultipartParameter("Latitude", latitude)
                .addMultipartParameter("Shiftid", Shiftid)
                .addMultipartParameter("WorkPlaceID", Siteid)
                .addMultipartParameter("Approverid", SupervisorID)
                .addMultipartFile("Image1", compressedImageFile)
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "onResponse: SUCCESS: " + response);
                        progressDialog.cancel();
                        try {
                            JSONObject object = new JSONObject(String.valueOf(response));
                            String Response_Message = object.optString("Response_Message");
                            String Response_Code = object.optString("Response_Code");
                            if (Response_Code.equals("101")) {
                                shiftAndLocationDialog.cancel();
                                successAlert(Response_Message);
                            } else {
                                progressDialog.cancel();
                                ShowDialog.showErrorDialog(NEW_AttendanceMarkingActivity.this,Response_Message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e(TAG, "onResponse: onError: " + error.getErrorBody());
                        progressDialog.cancel();
                    }
                });
    }

    private void successAlert(String message) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NEW_AttendanceMarkingActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);
        tvInvalidDate.setText(message);
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                //Intent intent = new Intent(MetsoAttendanceActivity.this, AttendanceReportActivity.class);
                Intent intent = new Intent(NEW_AttendanceMarkingActivity.this, MetsoAttendanceReportActivity.class);
                startActivity(intent);
                finish();
            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(false);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }

    private void getLocations() {
        //progressDialog.show();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                smf.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        Location location = task.getResult();
                        if (location != null) {
                            try {
                                /**------- Code use for marker in map ------*/
                                Log.e(TAG, "onMapReady: getLatitude: " + location.getLatitude());
                                Log.e(TAG, "onMapReady: getLongitude: " + location.getLongitude());
                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("My current location!!");
                                googleMap.addMarker(markerOptions);
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));

                                /**------- Code use for getting current address and showing address -------*/
                                Geocoder geocoder = new Geocoder(NEW_AttendanceMarkingActivity.this, Locale.getDefault());
                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                latitude = String.valueOf(addresses.get(0).getLatitude());
                                longitude = String.valueOf(addresses.get(0).getLongitude());
                                Log.e(TAG, "onComplete: Latitude: " + latitude + " Longitude: " + longitude);
                                Log.e(TAG, "onComplete: Address: " + addresses.get(0).getAddressLine(0));
                                currentAddresses = addresses.get(0).getAddressLine(0);

                                txtCurrentLocation.setText(addresses.get(0).getAddressLine(0));


                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    getLocations();
                                }
                            }, 2000);
                            Log.e(TAG, "onMapReady: Error");
                        }
                    }
                });
            }
        });
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleClient = new GoogleApiClient.Builder(NEW_AttendanceMarkingActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(NEW_AttendanceMarkingActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            @SuppressLint("MissingPermission") Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleClient);
            if (location == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleClient, mLocationRequest, this);
            } else {
                handleNewLocation(location);
            }
        }
    }

    private void handleNewLocation(Location location) {
        Log.e(TAG, "Imporent: " + location.toString());
        Log.e(TAG, "currentLongitude: Latitude: " + location.getLatitude() + " Longitude: " + location.getLongitude());
        currentLatitude = location.getLatitude();
        latitude = String.valueOf(location.getLatitude());

        currentLongitude = location.getLongitude();
        longitude = String.valueOf(location.getLongitude());


        latLng = new LatLng(currentLatitude, currentLongitude);
        address = getCompleteAddressString(currentLatitude, currentLongitude);
        address1 = address.replaceAll("\\s+", "");

        //txtCurrentLocation.setText(addresses.get(0).getAddressLine(0));

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(address)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_icon_5));
        txtCurrentLocation.setText(address);

        if (llLoading.getVisibility() == View.VISIBLE) {
            llLoading.setVisibility(View.GONE);
            llSubmit.setVisibility(View.VISIBLE);
            startTimer();
        }

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)      // Sets the center of the map to location user
                .zoom(16)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        mMap.addMarker(options);
    }

    private void startTimer() {
        int timerInMillis = 120 * 1000;
        CountDownTimer countDownTimer = new CountDownTimer(timerInMillis, 1000) {
            @Override
            public void onTick(long l) {
                long seconds = l / 1000;
                long hours = seconds / 3600;
                seconds %= 3600;
                long minutes = seconds / 60;
                seconds %= 60;

                String timeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                tvTimer.setText(timeFormatted);
            }

            @Override
            public void onFinish() {
                finish();
            }
        };

        countDownTimer.start();
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (ContextCompat.checkSelfPermission(NEW_AttendanceMarkingActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleClient);
            if (location == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleClient, mLocationRequest, this);
            } else {
                handleNewLocation(location);
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(NEW_AttendanceMarkingActivity.this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        handleNewLocation(location);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(NEW_AttendanceMarkingActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(NEW_AttendanceMarkingActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(NEW_AttendanceMarkingActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(NEW_AttendanceMarkingActivity.this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(NEW_AttendanceMarkingActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(NEW_AttendanceMarkingActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            // other 'case' lines to check for other
            // permissions this app might request
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(NEW_AttendanceMarkingActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(NEW_AttendanceMarkingActivity.this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
/*
    @Override
    protected void onStart() {
        super.onStart();
        if(this.mGoogleClient != null){
            this.mGoogleClient.connect();
        }
    }*/

    @Override
    public void onPause() {
        super.onPause();
        // mapView.onPause();
        //finish();
        Log.e(TAG, "Called: onPause");
        if (mGoogleClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleClient, this);
            mGoogleClient.disconnect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "called: onRestart");
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current ", strReturnedAddress.toString());
            } else {
                Log.w("My Current", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current", "Canont get Address!");
        }
        return strAdd;
    }


    private void savedeviceID(JSONObject jsonObject) {
        ProgressDialog pd = new ProgressDialog(NEW_AttendanceMarkingActivity.this);
        pd.setCancelable(false);
        pd.setMessage("Loading");
        pd.show();
        AndroidNetworking.post(AppData.newv2url + "Attendance/SaveIMEITrackingDetails")
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
                        String Response_Message = job1.optString("Response_Message");
                        if (Response_Code == 101) {

                        } else {

                            shoeErrorDialog(Response_Message);

                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();


                    }
                });
    }


    private void shoeErrorDialog(String msg) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NEW_AttendanceMarkingActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_alerts, null);
        dialogBuilder.setView(dialogView);
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        TextView tvSuccess = (TextView) dialogView.findViewById(R.id.tvSuccess);
        tvSuccess.setText(msg);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                onBackPressed();
            }
        });
        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        Window window = alertDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog.show();
    }

    private void openSupervisorPopUp(TextView txtSupervisor) {
        searchWbsCodeDialog = new Dialog(NEW_AttendanceMarkingActivity.this, R.style.CustomDialogNew2);
        searchWbsCodeDialog.setContentView(R.layout.wbs_code_search_layout);
        searchWbsCodeDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        searchWbsCodeDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        searchWbsCodeDialog.setCancelable(true);

        TextView txtPopupHeadline = searchWbsCodeDialog.findViewById(R.id.txtPopupHeadline);
        SearchView wbsCodeSearchView = (SearchView) searchWbsCodeDialog.findViewById(R.id.wbsCodeSearchView);
        ImageView imgCancel = searchWbsCodeDialog.findViewById(R.id.imgCancel);
        RecyclerView rvWbsCode = searchWbsCodeDialog.findViewById(R.id.rvWbsCode);

        wbsCodeSearchView.setQueryHint("Search Approver");
        txtPopupHeadline.setText("Select Approver");
        rvWbsCode.setLayoutManager(new LinearLayoutManager(NEW_AttendanceMarkingActivity.this));
        ArrayList<SpineerItemModel> supervisorListCopy = new ArrayList<>();
        supervisorListCopy = (ArrayList<SpineerItemModel>) supervisorList.clone();
        SupervisorFilterAdapter supervisorFilterAdapter = new SupervisorFilterAdapter(NEW_AttendanceMarkingActivity.this, supervisorListCopy);
        rvWbsCode.setAdapter(supervisorFilterAdapter);

        wbsCodeSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                supervisorFilterAdapter.getFilter().filter(s);
                return false;
            }
        });

        supervisorFilterAdapter.setSupervisorSelectListener(new MetsoNewReimbursementClaimActivity.SupervisorSelectListener() {
            @Override
            public void onClick(String supervisor_id, String supervisor) {
                SupervisorID = supervisor_id;
                txtSupervisor.setText(supervisor);
                searchWbsCodeDialog.dismiss();
            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchWbsCodeDialog.dismiss();
            }
        });
        searchWbsCodeDialog.show();
    }

    public static String getAndroidID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
