package io.cordova.myapp00d753.activity.attendance;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.cordova.myapp00d753.R;

import io.cordova.myapp00d753.activity.EmployeeDashBoardActivity;
import io.cordova.myapp00d753.activity.bosch.BoschAndroidXCameraActivity;
import io.cordova.myapp00d753.activity.bosch.BoschAttendanceReportActivity;
import io.cordova.myapp00d753.activity.bosch.adapter.PunchTypeAdapter;
import io.cordova.myapp00d753.activity.bosch.model.BoschPunchTypeModel;
import io.cordova.myapp00d753.activity.metso.adapter.LocationSpinnerAdapter;
import io.cordova.myapp00d753.activity.metso.model.LocationSpinnerModel;
import io.cordova.myapp00d753.activity.metso.model.MetsoLocationModel;
import io.cordova.myapp00d753.activity.metso.model.MetsoShiftModel;
import io.cordova.myapp00d753.activity.metso.model.ShiftSpinnerModel;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.GPSTracker;
import io.cordova.myapp00d753.utility.Pref;

public class BoschAttendanceActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = "BoschAttendanceActivity";
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
    PunchTypeAdapter punchTypeAdapter;
    LocationSpinnerAdapter locationSpinnerAdapter;
    ArrayList<ShiftSpinnerModel> shiftList;
    ArrayList<MetsoShiftModel> metsoShiftList;
    ArrayList<LocationSpinnerModel> locationList;
    ArrayAdapter<String> shiftListAdapter;
    ArrayList<String> stringsShiftList;
    String latitude = "", longitude = "", currentAddresses = "", ClientID = "", logintype = "", Siteid = "", MasterID = "";
    TextView txtCurrentLocation;
    ImageView imgBack,imgHome;
    double currentLatitude, currentLongitude;
    Pref pref;
    LatLng latLng;

    private GoogleMap mMap;
    ArrayList<MetsoLocationModel> metsoLocationArrayList;
    ProgressDialog progressDialog;
    //LocationManager locationManager;
    private LocationCallback locationCallback;
    GPSTracker gps;
    LocationListener locationListener;
    String address1;
    String address = "N/A";
    LinearLayout llLoading,llSubmit;
    ImageButton btnOpenCamera;
    ActivityResultLauncher<Intent> mCaptureCamera;
    File compressedImageFile;
    ImageView showImageView;
    boolean isImageSelected=false;
    ArrayList<BoschPunchTypeModel> boschPunchTypeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bosch_attendance);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        intiView();
        btnOnClick();
        settingToGetLocation();
        getPunchType();
        //getPunchType2();
    }



    private void getPunchType() {
        String surl = AppData.url+"Leave/PunchType?CompanyID="+pref.getEmpClintId()+"&Clientofficeid="+pref.getEmpClintOffId()+"&SecurityCode="+pref.getSecurityCode();
        Log.d("inputLogin", surl);

        final ProgressDialog progressDialog=new ProgressDialog(BoschAttendanceActivity.this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        progressDialog.dismiss();
                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e(TAG, "BOSCH_PUNCH_TYPE: " + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                //BoschPunchTypeModel
                                Log.e(TAG, "onResponse: CALLED");
                                boschPunchTypeList =  new ArrayList<>();
                                boschPunchTypeList.add(new BoschPunchTypeModel(0,"Select From"));
                                JSONArray responseData = job1.optJSONArray("responseData");
                                Log.e(TAG, "onResponse: "+responseData.toString());
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.optJSONObject(i);
                                    BoschPunchTypeModel boschPunchTypeModel = new BoschPunchTypeModel(
                                            obj.getInt("Punchtypeid"),
                                            obj.getString("Punchtypename")
                                    );
                                    boschPunchTypeList.add(boschPunchTypeModel);
                                }
                                Log.e(TAG, "onResponse: "+boschPunchTypeList.size());
                                punchTypeAdapter = new PunchTypeAdapter(BoschAttendanceActivity.this,boschPunchTypeList);
                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(BoschAttendanceActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                //Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(BoschAttendanceActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                300000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void btnOnClick() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BoschAttendanceActivity.this, EmployeeDashBoardActivity.class);
                startActivity(intent);
            }
        });
        btnOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BoschAttendanceActivity.this, BoschAndroidXCameraActivity.class);
                startActivityForResult(intent, BoschAndroidXCameraActivity.LONG_IMAGE_RESULT_CODE);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isImageSelected){
                    openShiftAndLocationPopup();
                } else {
                    Toast.makeText(BoschAttendanceActivity.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void intiView() {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
        //locationListener = (LocationListener) new MyLocationListener();
        /*LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // Handle location updates
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
            }

            // Other methods of LocationListener
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BETWEEN_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);*/
        llLoading = findViewById(R.id.llLoading);
        llSubmit = findViewById(R.id.llSubmit);
        btnSubmit = findViewById(R.id.btnSubmit);
        txtCurrentLocation = findViewById(R.id.txtCurrentLocation);
        imgBack = findViewById(R.id.imgBack);
        imgHome = findViewById(R.id.imgHome);
        btnOpenCamera = findViewById(R.id.btnOpenCamera);
        showImageView = findViewById(R.id.showImageView);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //smf = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fmGoogleMaps);
        //smf.getMapAsync(this);

        pref = new Pref(BoschAttendanceActivity.this);
        ClientID = pref.getEmpClintId();
        MasterID = pref.getMasterId();

        Log.e(TAG, "intiView: ClientID: "+ClientID);
        Log.e(TAG, "intiView: MasterID: "+MasterID);
        Log.e(TAG, "intiView: Clint Id: "+pref.getEmpClintId());

        progressDialog = new ProgressDialog(BoschAttendanceActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //AndroidXCameraActivity.LONG_IMAGE_RESULT_CODE
        switch (requestCode) {
            case BoschAndroidXCameraActivity.LONG_IMAGE_RESULT_CODE:


                if ( requestCode == BoschAndroidXCameraActivity.LONG_IMAGE_RESULT_CODE) {
                    Log.e(TAG, "onActivityResult: "+data.getExtras().get("image"));
                    File file = new File(String.valueOf(data.getExtras().get("image")));
                    try {
                        compressedImageFile = new ImageZipper(BoschAttendanceActivity.this)
                                .setQuality(80)
                                .compressToFile(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap =  BitmapFactory.decodeFile(compressedImageFile.getAbsolutePath());
                    showImageView.setImageBitmap(bitmap);
                    showImageView.setVisibility(View.VISIBLE);
                    isImageSelected = true;
                }
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(BoschAttendanceActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            @SuppressLint("MissingPermission") Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleClient);
            if (location == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleClient, mLocationRequest,  this);
            } else {
                handleNewLocation(location);
            }
        }
    }

    private void handleNewLocation(Location location) {
        Log.e(TAG,"Imporent: "+location.toString());
        Log.e(TAG,"currentLongitude: Latitude: "+location.getLatitude()+" Longitude: "+location.getLongitude());
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

        if (llLoading.getVisibility() == View.VISIBLE){
            llLoading.setVisibility(View.GONE);
            llSubmit.setVisibility(View.VISIBLE);
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

    @Override
    public void onConnectionSuspended(int i) {
        if (ContextCompat.checkSelfPermission(BoschAttendanceActivity.this,
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
                connectionResult.startResolutionForResult(BoschAttendanceActivity.this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
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
            if (ContextCompat.checkSelfPermission(BoschAttendanceActivity.this,
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(BoschAttendanceActivity.this,
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
                    Toast.makeText(BoschAttendanceActivity.this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleClient = new GoogleApiClient.Builder(BoschAttendanceActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        // mapView.onPause();
        if (mGoogleClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleClient, this);
            mGoogleClient.disconnect();
        }
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

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(BoschAttendanceActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(BoschAttendanceActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(BoschAttendanceActivity.this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(BoschAttendanceActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(BoschAttendanceActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }


    private void openShiftAndLocationPopup() {
        shiftAndLocationDialog = new Dialog(BoschAttendanceActivity.this);
        shiftAndLocationDialog.setContentView(R.layout.bosch_location_dialog_layout);
        shiftAndLocationDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        shiftAndLocationDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        LinearLayout lnCancel = shiftAndLocationDialog.findViewById(R.id.lnCancel);
        TextView txtSelectFrom = shiftAndLocationDialog.findViewById(R.id.txtSelectFrom);
        TextView txtSelectLocation = shiftAndLocationDialog.findViewById(R.id.txtSelectLocation);
        TextView txtErrorShift = shiftAndLocationDialog.findViewById(R.id.txtErrorShift);
        TextView txtErrorLocation = shiftAndLocationDialog.findViewById(R.id.txtErrorLocation);
        Spinner spFrom = shiftAndLocationDialog.findViewById(R.id.spFrom);
        Spinner spLocation = shiftAndLocationDialog.findViewById(R.id.spLocation);
        AppCompatButton btnMarkedYourAttendance = shiftAndLocationDialog.findViewById(R.id.btnMarkedYourAttendance);

        spFrom.setAdapter(punchTypeAdapter);
        spFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                BoschPunchTypeModel clickedItem = (BoschPunchTypeModel) adapterView.getItemAtPosition(i);
                if (!clickedItem.getPunchtypename().equals("Select From")) {
                    txtSelectFrom.setText(clickedItem.getPunchtypename());
                    logintype = String.valueOf(clickedItem.getPunchtypeid());
                    txtErrorShift.setVisibility(View.GONE);
                } else {
                    txtSelectFrom.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /*spLocation.setAdapter(locationSpinnerAdapter);
        spLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MetsoLocationModel clickedItem = (MetsoLocationModel) adapterView.getItemAtPosition(i);
                if (!clickedItem.getSiteName().equals("Select Location")) {
                    txtSelectLocation.setText(clickedItem.getSiteName());
                    Siteid = String.valueOf(clickedItem.getSiteid());
                    txtErrorLocation.setVisibility(View.GONE);
                } else {
                    txtSelectLocation.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

        txtSelectFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spFrom.performClick();

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
                if (txtSelectFrom.getText().toString().trim().isEmpty()) {
                    txtErrorShift.setVisibility(View.VISIBLE);
                } else  {
                    txtErrorLocation.setVisibility(View.GONE);
                    submitAttendance();
                }
            }
        });

        Window window = shiftAndLocationDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.NO_GRAVITY;
        shiftAndLocationDialog.setCancelable(false);
        shiftAndLocationDialog.show();
    }

    private void submitAttendance() {
        Log.e(TAG, "submitAttendance: "+pref.getEmpId());
        progressDialog.show();
        Log.e(TAG, "submitAttendance: AEMEmployeeID: "+pref.getEmpId()
                +"\nAddress: "+address+"\nlogintype: "+logintype+"\nLongitude: "+longitude+"\nLatitude: "+latitude+"\nimage: "+"\nSecurityCode: 0000" );

        AndroidNetworking.upload("http://gsppi.geniusconsultant.com/GeniusiOSApi/api/Bosch_post_attendance")
                .addMultipartParameter("AEMEmployeeID", pref.getEmpId())
                //.addMultipartParameter("Address", currentAddresses)
                .addMultipartParameter("Address", address)
                .addMultipartParameter("logintype", logintype)
                .addMultipartParameter("Longitude", longitude)
                .addMultipartParameter("Latitude", latitude)
                .addMultipartFile("image", compressedImageFile)
                .addMultipartParameter("SecurityCode", "0000")
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
                            if (object.getBoolean("responseStatus") == true) {
                                shiftAndLocationDialog.cancel();
                                successAlert(object.getString("responseText"));
                            } else {
                                progressDialog.cancel();
                                Log.e(TAG, "FAILED: "+object.getString("responseText"));
                                Toast.makeText(BoschAttendanceActivity.this, object.getString("responseText"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e(TAG, "onResponse: onError: " + error);
                        progressDialog.cancel();
                    }
                });
    }

    private void successAlert(String message) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(BoschAttendanceActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);
        /*if (addflag == 1) {
            tvInvalidDate.setText("Your attendnace has been saved successfully");
        } else {
            tvInvalidDate.setText("Your attendnace has been saved successfully");
        }*/
        tvInvalidDate.setText(message);
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                //Intent intent = new Intent(MetsoAttendanceActivity.this, AttendanceReportActivity.class);
                Intent intent = new Intent(BoschAttendanceActivity.this, BoschAttendanceReportActivity.class);
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


}