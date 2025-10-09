package io.cordova.myapp00d753.activity.attendance;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.Retrofit.RetrofitClient;
import io.cordova.myapp00d753.activity.EmployeeDashBoardActivity;
import io.cordova.myapp00d753.activity.metso.adapter.ComponentSpinnerAdapter;
import io.cordova.myapp00d753.activity.metso.adapter.LocationSpinnerAdapter;
import io.cordova.myapp00d753.activity.metso.adapter.ShiftSpinnerAdapter;
import io.cordova.myapp00d753.activity.metso.model.MetsoLocationModel;
import io.cordova.myapp00d753.activity.metso.model.MetsoShiftModel;
import io.cordova.myapp00d753.module.SpineerItemModel;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.ShowDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeoFenceAttendanceWithShiftActivity extends AppCompatActivity  implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = "GeoFenceAttendanceWithS";
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    GoogleApiClient mGoogleClient;
    SupportMapFragment smf;
    private GoogleMap mMap;
    private LocationRequest mLocationRequest;
    LinearLayout llLoading, llSubmit;
    TextView tvTimer,txtCurrentLocation;
    double currentLatitude, currentLongitude;
    String latitude = "", longitude = "", currentAddresses = "", ClientID = "", Shiftid = "", Siteid = "", MasterID = "";
    LatLng latLng;
    String address = "N/A";
    String address1;
    String shiftStatus,shiftArray;
    JSONArray shiftJSON;
    ArrayList<SpineerItemModel> spshiftLIST=new ArrayList<>();
    private Dialog shiftAndLocationDialog;
    ComponentSpinnerAdapter componentSpinnerAdapter;
    AppCompatButton btnSubmit;
    ProgressDialog progressDialog;
    Pref pref;
    ImageView imgBack;
    LinearLayout llHome;
    ArrayList<MetsoShiftModel> shiftList;
    ArrayList<MetsoLocationModel> locationList;
    ShiftSpinnerAdapter shiftSpinnerAdapter;
    LocationSpinnerAdapter locationSpinnerAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metso_attendance);
        initView();
        settingToGetLocation();
        btnClick();
        getShift();
    }

    private void initView() {
        /*shiftStatus=getIntent().getStringExtra("shiftStatus");
        shiftArray=getIntent().getStringExtra("shiftArray");
        try {
            shiftJSON=new JSONArray(shiftArray);
            for ( int i=0;i<shiftJSON.length();i++){
                JSONObject obj=shiftJSON.optJSONObject(i);
                String WorkingShiftID=obj.optString("WorkingShiftID");
                String shiftname=obj.optString("shiftname");
                //shiftLIST.add(shiftname);
                SpineerItemModel itemModel=new SpineerItemModel(shiftname,WorkingShiftID);
                spshiftLIST.add(itemModel);
            }
            componentSpinnerAdapter = new ComponentSpinnerAdapter(GeoFenceAttendanceWithShiftActivity.this, spshiftLIST);
            //Log.d("shiftLIST",shiftLIST.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        pref = new Pref(GeoFenceAttendanceWithShiftActivity.this);
        ClientID = pref.getEmpClintId();
        llLoading = findViewById(R.id.llLoading);
        llSubmit = findViewById(R.id.llSubmit);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvTimer = findViewById(R.id.tvTimer);
        imgBack = findViewById(R.id.imgBack);
        llHome = findViewById(R.id.llLogout);
        txtCurrentLocation = findViewById(R.id.txtCurrentLocation);
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000);
        progressDialog = new ProgressDialog(GeoFenceAttendanceWithShiftActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
    }

    private void btnClick() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openShiftAndLocationPopup();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        llHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(GeoFenceAttendanceWithShiftActivity.this, EmployeeDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getShift() {
        progressDialog.show();
        Call<JsonObject> call = RetrofitClient
                .getInstance()
                .getApi()
                .GetMetsoAttendanceData("2", ClientID, pref.getSecurityCode());

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                try {
                    JSONObject object = new JSONObject(String.valueOf(response.body()));
                    if (object.getBoolean("responseStatus") == true) {
                        JSONArray jsonArray = object.getJSONArray("responseData");
                        shiftList = new ArrayList<>();
                        shiftList.add(new MetsoShiftModel(0, "Select Shift"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject objectResponse = jsonArray.getJSONObject(i);
                            MetsoShiftModel metsoLocationModel = new MetsoShiftModel(objectResponse.getLong("WorkingShiftID"),
                                    objectResponse.getString("Column1"));
                            shiftList.add(metsoLocationModel);
                        }
                        Log.e(TAG, "shiftList: "+shiftList.size());
                        shiftSpinnerAdapter = new ShiftSpinnerAdapter(GeoFenceAttendanceWithShiftActivity.this, shiftList);
                        getLocationData();
                    } else {
                        progressDialog.cancel();
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

    private void getLocationData() {
        Log.e(TAG, "ClientID: " + ClientID);
        Call<JsonObject> call = RetrofitClient
                .getInstance()
                .getApi()
                .GetMetsoAttendanceData("1", ClientID, pref.getSecurityCode());

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "onResponse: Location: " + new Gson().toJson(response.body()));
                try {
                    JSONObject object = new JSONObject(String.valueOf(response.body()));
                    if (object.getBoolean("responseStatus") == true) {
                        JSONArray jsonArray = object.getJSONArray("responseData");
                        locationList = new ArrayList<>();
                        locationList.add(new MetsoLocationModel(0, "Select Location"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject objectResponse = jsonArray.getJSONObject(i);
                            MetsoLocationModel metsoLocationModel = new MetsoLocationModel(objectResponse.getInt("Siteid"),
                                    (String) objectResponse.getString("SiteName"));
                            locationList.add(metsoLocationModel);
                        }
                        Log.e(TAG, "onResponse: SIZE: " + locationList.size());
                        locationSpinnerAdapter = new LocationSpinnerAdapter(GeoFenceAttendanceWithShiftActivity.this, locationList);

                        progressDialog.cancel();
                    } else {
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

    private void openShiftAndLocationPopup() {
        shiftAndLocationDialog = new Dialog(GeoFenceAttendanceWithShiftActivity.this);
        shiftAndLocationDialog.setContentView(R.layout.shift_location_popup);
        shiftAndLocationDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        shiftAndLocationDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        LinearLayout lnCancel = shiftAndLocationDialog.findViewById(R.id.lnCancel);
        LinearLayout llLocation = shiftAndLocationDialog.findViewById(R.id.llLocation);
        TextView txtSelectShift = shiftAndLocationDialog.findViewById(R.id.txtSelectShift);
        TextView txtSelectLocation = shiftAndLocationDialog.findViewById(R.id.txtSelectLocation);
        TextView txtErrorShift = shiftAndLocationDialog.findViewById(R.id.txtErrorShift);
        TextView txtErrorLocation = shiftAndLocationDialog.findViewById(R.id.txtErrorLocation);
        TextView txtPopUpHeader = shiftAndLocationDialog.findViewById(R.id.txtPopUpHeader);
        Spinner spShift = shiftAndLocationDialog.findViewById(R.id.spShift);
        Spinner spLocation = shiftAndLocationDialog.findViewById(R.id.spLocation);
        txtPopUpHeader.setText("Select shift");
        AppCompatButton btnMarkedYourAttendance = shiftAndLocationDialog.findViewById(R.id.btnMarkedYourAttendance);
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

        lnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shiftAndLocationDialog.cancel();
            }
        });

        btnMarkedYourAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtSelectShift.getText().toString().trim().isEmpty()) {
                    txtErrorShift.setVisibility(View.VISIBLE);
                } else if (txtSelectLocation.getText().toString().trim().isEmpty()) {
                    txtErrorLocation.setVisibility(View.VISIBLE);
                } else {
                    txtErrorShift.setVisibility(View.GONE);
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
        progressDialog.show();
        Log.e(TAG, "submitAttendance: \nAEMEmployeeID:"+pref.getEmpId()
                + "\nAddress:"+address
                + "\nShiftid: " + Shiftid
                + "\nSiteid: " + Siteid
                + "\nlongitude: " + longitude
                + "\nlatitude: " + latitude
                +"\nSecurityCode:"+pref.getSecurityCode());


        AndroidNetworking.upload(AppData.url + "gcl_post_attedanceGeofenceMetso")
                .addMultipartParameter("AEMEmployeeID", pref.getEmpId())
                .addMultipartParameter("Address", address)
                .addMultipartParameter("Shiftid", Shiftid)
                .addMultipartParameter("Siteid", Siteid)
                .addMultipartParameter("Longitude", longitude)
                .addMultipartParameter("Latitude", latitude)
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
                            if (object.getBoolean("responseStatus") == true) {
                                shiftAndLocationDialog.cancel();
                                ShowDialog.showSuccessDialog(GeoFenceAttendanceWithShiftActivity.this, object.getString("responseText"), new ShowDialog.ResultListener() {
                                    @Override
                                    public void onSuccess() {
                                        Intent intent = new Intent(GeoFenceAttendanceWithShiftActivity.this, MetsoAttendanceReportActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            } else {
                                progressDialog.cancel();
                                Toast.makeText(GeoFenceAttendanceWithShiftActivity.this, object.getString("responseText"), Toast.LENGTH_SHORT).show();
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
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(GeoFenceAttendanceWithShiftActivity.this,
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

    @Override
    public void onConnectionSuspended(int i) {
        if (ContextCompat.checkSelfPermission(GeoFenceAttendanceWithShiftActivity.this,
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
                connectionResult.startResolutionForResult(GeoFenceAttendanceWithShiftActivity.this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
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
            if (ContextCompat.checkSelfPermission(GeoFenceAttendanceWithShiftActivity.this,
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

    protected synchronized void buildGoogleApiClient() {
        mGoogleClient = new GoogleApiClient.Builder(GeoFenceAttendanceWithShiftActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleClient.connect();
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(GeoFenceAttendanceWithShiftActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(GeoFenceAttendanceWithShiftActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(GeoFenceAttendanceWithShiftActivity.this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(GeoFenceAttendanceWithShiftActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(GeoFenceAttendanceWithShiftActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
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
}
