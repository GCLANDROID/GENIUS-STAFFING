package io.cordova.myapp00d753.activity.attendance;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
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
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.textfield.TextInputEditText;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.cordova.myapp00d753.AndroidXCamera.AndroidXCameraActivity;
import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.databinding.ActivityBlueDartAttendanceManageBinding;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.GPSTracker;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;
import io.cordova.myapp00d753.utility.Pref;


public class GeoFenceAttendanceWithOutLocActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static final String TAG = GeoFenceAttendanceWithOutLocActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    //  private MapView mapView;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    ActivityBlueDartAttendanceManageBinding binding;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    NetworkConnectionCheck connectionCheck;
    LatLng latLng;
    private static final String SERVER_PATH = AppData.url;
    GoogleApiClient googleApiClient;
    GPSTracker gps;
    double latitude, longitude;
    TextView tvToolBar;
    Pref pref;
    double currentLatitude, currentLongitude;
    String lat, longt, address, address1;
    TextView tvAddress;

    //Permission Code
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_CAMERA_CODE = 2001;
    String cameraPermission[];
    String storagePermission[];

    Uri image_uri;
    Dialog dialog;
    ImageView odometerImage, dialogClosedImage;
    TextInputEditText odometerEdittext;
    String odometerValue = "";
    Button dialogSubmitBtn;
    AlertDialog alerDialog1;
    LinearLayout lnMark;
    String encodeToString="";

    Integer id=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_blue_dart_attendance_manage);
        // locationalerts();
        initialize();
        setUpMapIfNeeded();
        // attendanceCheck();
        onClick();
    }

    private void initialize() {
        pref = new Pref(getApplicationContext());
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        connectionCheck = new NetworkConnectionCheck(getApplicationContext());
        lnMark = (LinearLayout) findViewById(R.id.lnMark);

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds


        connectionCheck = new NetworkConnectionCheck(this);

        //  mapView = findViewById(R.id.map);

        //camera permission
        cameraPermission = new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        //storage permission
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        gps = new GPSTracker(GeoFenceAttendanceWithOutLocActivity.this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            Log.d("saikatdas", String.valueOf(latitude));
            longitude = gps.getLongitude();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings

        }


        odometerTextChange();
        onDialogSubBtnClicked();
        onDialogClosedSubBtnClicked();
        binding.lnJrStart.setVisibility(View.GONE);
        binding.lnJrEnd.setVisibility(View.GONE);

    }

    private void onClick() {
        binding.lnJrStart.setOnClickListener(v -> {
            id = 1;
            pref.saveJrFlag("1");
            //permission allowed, take picture
            //pickCamera();
            AndroidXCameraActivity.launch(GeoFenceAttendanceWithOutLocActivity.this, 1001);
        });
        binding.lnJrEnd.setOnClickListener(v -> {
            pref.saveJrFlag("2");
            //permission allowed, take picture
            //pickCamera();
            AndroidXCameraActivity.launch(GeoFenceAttendanceWithOutLocActivity.this, 1001);
        });
        lnMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attendance();
            }
        });

    }



    private void onDialogSubBtnClicked() {

    }

    private void onDialogClosedSubBtnClicked() {

    }

    private void odometerTextChange() {



    }

    private void pickCamera() {
        //intent to take image from camera, it will also be save to storage to get high quality image
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "NewPick"); //title of the picture
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image To Text"); //title of the picture
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Log.i("HAHAAH ", "HAHAH " + image_uri);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2000 && resultCode == 1001){
            Log.e(TAG, "onActivityResult: "+data.getExtras().get("picture"));
            Log.e(TAG, "onActivityResult: "+data.getExtras().get(AndroidXCameraActivity.IMAGE_PATH_KEY));
            image_uri =  Uri.parse(String.valueOf(data.getExtras().get("picture")));
            //image_uri = (Uri) data.getExtras().get(AndroidXCameraActivity.IMAGE_PATH_KEY);
            File imageFile = new File(String.valueOf(data.getExtras().get("picture")));
            Log.e(TAG, "image_uri: "+image_uri);
            if (image_uri != null){
                CropImage.activity(Uri.fromFile(imageFile.getAbsoluteFile()))
                        .setGuidelines(CropImageView.Guidelines.ON) //enable image guid lines
                        .setCropMenuCropButtonIcon(R.drawable.checked)
                        .setCropMenuCropButtonTitle("Crop")
                        .start(GeoFenceAttendanceWithOutLocActivity.this);
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri(); //get image uri
                Log.i("HHSSHSH ", "RRRRRRR " + resultUri.toString());

                    if (pref.getJrFlag().equals("1")){
                        Log.d("JRFlag",pref.getJrFlag());
                        startJourney();
                    }else {
                        endJourney();
                        Log.d("JRFlag",pref.getJrFlag());
                    }

                //  set image to image view
                odometerImage.setImageURI(resultUri);
                //  get drawable bitmap for text recognition
                BitmapDrawable bitmapDrawable = (BitmapDrawable) odometerImage.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();


                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                encodeToString= Base64.encodeToString(byteArray, Base64.DEFAULT);


                TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();

                if (!recognizer.isOperational()) {

                } else {
                    //set text to edit text
                    try {
                        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                        SparseArray<TextBlock> items = recognizer.detect(frame);
                        StringBuilder sb = new StringBuilder();
                        //get text from sb until there is no text
                        for (int i = 0; i < items.size(); i++) {
                            TextBlock myItem = items.valueAt(i);
                            sb.append(myItem.getValue());
                        }
                        odometerValue = getOdometerRegexValue(sb.toString());
                        odometerEdittext.setText(odometerValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                //if there is any error show it
                Exception error = result.getError();
                Log.e(TAG, "onActivityResult: "+error);
                Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }

        /*if (requestCode == IMAGE_PICK_CAMERA_CODE) {
            //got image from camera now crop it
            CropImage.activity(image_uri)
                    .setGuidelines(CropImageView.Guidelines.ON) //enable image guid lines
                    .setCropMenuCropButtonIcon(R.drawable.checked)
                    .setCropMenuCropButtonTitle("Crop")
                    .start(BlueDartAttendanceManageActivity.this);
        }
        //get cropped image
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri(); //get image uri
                Log.i("HHSSHSH ", "RRRRRRR " + resultUri.toString());

                if (pref.getJrFlag().equals("1")){
                    Log.d("JRFlag",pref.getJrFlag());
                    startJourney();
                }else {
                    endJourney();
                    Log.d("JRFlag",pref.getJrFlag());
                }

                //  set image to image view
                odometerImage.setImageURI(resultUri);
                //  get drawable bitmap for text recognition
                BitmapDrawable bitmapDrawable = (BitmapDrawable) odometerImage.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();


                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                encodeToString= Base64.encodeToString(byteArray, Base64.DEFAULT);


                TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();

                if (!recognizer.isOperational()) {

                } else {
                    //set text to edit text
                    try {
                        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                        SparseArray<TextBlock> items = recognizer.detect(frame);
                        StringBuilder sb = new StringBuilder();
                        //get text from sb until there is no text
                        for (int i = 0; i < items.size(); i++) {
                            TextBlock myItem = items.valueAt(i);
                            sb.append(myItem.getValue());
                        }
                        odometerValue = getOdometerRegexValue(sb.toString());
                        odometerEdittext.setText(odometerValue);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }




                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                //if there is any error show it
                Exception error = result.getError();
                Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }*/
    }

    public static Bitmap uriToBitmap(Context context, Uri uri) {
        try {
            // Open an input stream from the Uri
            InputStream inputStream = context.getContentResolver().openInputStream(uri);

            // Decode the input stream into a Bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Close the input stream
            inputStream.close();

            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addImage:
                //showImageImportDialog();
                break;

            case R.id.about:
                // dialogAbout();
                break;
        }
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();

        // mapView.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }


    private void setUpMapIfNeeded() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //  mapView.getMapAsync(this);

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
            if (ContextCompat.checkSelfPermission(GeoFenceAttendanceWithOutLocActivity.this,
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
        // Creates a CameraPosition from the builde
    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());

        currentLatitude = location.getLatitude();
        lat = String.valueOf(currentLatitude);
        currentLongitude = location.getLongitude();

        longt = String.valueOf(currentLongitude);
        Log.d("currentLongitude", longt);

        latLng = new LatLng(currentLatitude, currentLongitude);
        address = getCompleteAddressString(currentLatitude, currentLongitude);
        address1 = address.replaceAll("\\s+", "");
        tvAddress.setText("Address :- " + address);

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(address)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mapmarker));


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


    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(Bundle bundle) {

        if (ContextCompat.checkSelfPermission(GeoFenceAttendanceWithOutLocActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            @SuppressLint("MissingPermission") Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (location == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            } else {
                handleNewLocation(location);
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(GeoFenceAttendanceWithOutLocActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (ContextCompat.checkSelfPermission(GeoFenceAttendanceWithOutLocActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (location == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            } else {
                handleNewLocation(location);
            }
        }


    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(GeoFenceAttendanceWithOutLocActivity.this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
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


    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(GeoFenceAttendanceWithOutLocActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(GeoFenceAttendanceWithOutLocActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(GeoFenceAttendanceWithOutLocActivity.this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(GeoFenceAttendanceWithOutLocActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(GeoFenceAttendanceWithOutLocActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
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

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private String getOdometerRegexValue(String value) {
        // String result = "";

        // Regular expression pattern for Aadhar card numbers
        Pattern p = Pattern.compile("[0-9]+");
        Matcher m = p.matcher(value);
        String s = "";
        if (m.find()) {
            s = m.group(0);
        }

        return s.replaceAll("\\s+", "");
    }


    private void attendance() {
        ProgressDialog pd = new ProgressDialog(GeoFenceAttendanceWithOutLocActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.upload(AppData.url + "gcl_post_attedanceGeofence")
                .addMultipartParameter("AEMEmployeeID", pref.getEmpId())
                .addMultipartParameter("Address", address)
                .addMultipartParameter("Longitude", longt)
                .addMultipartParameter("Latitude", lat)
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

                        pd.dismiss();
                        JSONObject job = response;
                        String responseText = job.optString("responseText");
                        boolean responseStatus = job.optBoolean("responseStatus");
                        if (responseStatus) {
                            successAlert("Your attendance has been saved successfully",1);
                        } else {
                            Toast.makeText(GeoFenceAttendanceWithOutLocActivity.this, responseText, Toast.LENGTH_LONG).show();
                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        pd.dismiss();
                        Toast.makeText(GeoFenceAttendanceWithOutLocActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();

                    }
                });
    }

    private void journeyStart(String reading) {
        ProgressDialog pd = new ProgressDialog(GeoFenceAttendanceWithOutLocActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.upload(AppData.url + "gcl_post_odometer/OdometerSaveWithApproval")
                .addMultipartParameter("AEMEmployeeID", pref.getEmpId())
                .addMultipartParameter("Address", reading)
                .addMultipartParameter("Longitude", longt)
                .addMultipartParameter("Latitude", lat)
                .addMultipartParameter("ImgData", encodeToString)
                .addMultipartParameter("Remarks", "1")
                .addMultipartParameter("ApprovalStatus", "1")
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        pd.show();

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        pd.dismiss();
                        JSONObject job = response;
                        String responseText = job.optString("responseText");
                        boolean responseStatus = job.optBoolean("responseStatus");
                        if (responseStatus) {
                            successAlert("Your KM reading has been saved successfully",2);
                        } else {
                            Toast.makeText(GeoFenceAttendanceWithOutLocActivity.this, responseText, Toast.LENGTH_LONG).show();
                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        pd.dismiss();
                        Toast.makeText(GeoFenceAttendanceWithOutLocActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();

                    }
                });
    }

    private void journeyEnd(String reading) {
        ProgressDialog pd = new ProgressDialog(GeoFenceAttendanceWithOutLocActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.upload(AppData.url + "gcl_post_odometer/OdometerSaveWithApproval")
                .addMultipartParameter("AEMEmployeeID", pref.getEmpId())
                .addMultipartParameter("Address", reading)
                .addMultipartParameter("Longitude", longt)
                .addMultipartParameter("Latitude", lat)
                .addMultipartParameter("ImgData", encodeToString)
                .addMultipartParameter("Remarks", "2")
                .addMultipartParameter("ApprovalStatus", "1")
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        pd.show();
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        pd.dismiss();
                        JSONObject job = response;
                        String responseText = job.optString("responseText");
                        boolean responseStatus = job.optBoolean("responseStatus");
                        if (responseStatus) {
                            successAlert("Your KM reading has been saved successfully",2);
                        } else {
                            Toast.makeText(GeoFenceAttendanceWithOutLocActivity.this, responseText, Toast.LENGTH_LONG).show();
                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        pd.dismiss();
                        Toast.makeText(GeoFenceAttendanceWithOutLocActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();

                    }
                });
    }

    private void successAlert(String text,int flag) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GeoFenceAttendanceWithOutLocActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);

        tvInvalidDate.setText(text);

        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                if (flag==1){
                    Intent intent = new Intent(GeoFenceAttendanceWithOutLocActivity.this, AttendanceReportActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    onBackPressed();
                    pref.saveJrFlag("0");
                }

            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }


    private void startJourney() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GeoFenceAttendanceWithOutLocActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.journey_dialog, null);
        dialogBuilder.setView(dialogView);
        odometerImage = dialogView.findViewById(R.id.imageView);
        odometerEdittext = dialogView.findViewById(R.id.odometer_text);
        dialogSubmitBtn = dialogView.findViewById(R.id.submit_button);
        dialogClosedImage = dialogView.findViewById(R.id.closed);
        TextView tvToolBar=(TextView)dialogView.findViewById(R.id.tvToolBar);
        tvToolBar.setText("Start Journey");


        dialogSubmitBtn.setOnClickListener(v -> {
            if (TextUtils.isEmpty(odometerEdittext.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Reading Mandatory", Toast.LENGTH_SHORT).show();
            } else {
                journeyStart(odometerEdittext.getText().toString());
                dialog.dismiss();

            }

        });

        dialogClosedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog = dialogBuilder.create();
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        dialog.show();
    }


    private void endJourney() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GeoFenceAttendanceWithOutLocActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.journey_dialog, null);
        dialogBuilder.setView(dialogView);
        odometerImage = dialogView.findViewById(R.id.imageView);
        odometerEdittext = dialogView.findViewById(R.id.odometer_text);
        dialogSubmitBtn = dialogView.findViewById(R.id.submit_button);
        dialogClosedImage = dialogView.findViewById(R.id.closed);
        TextView tvToolBar=(TextView)dialogView.findViewById(R.id.tvToolBar);
        tvToolBar.setText("End Journey");



        dialogSubmitBtn.setOnClickListener(v -> {
            if (TextUtils.isEmpty(odometerEdittext.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Reading Mandatory", Toast.LENGTH_SHORT).show();
            } else {
                journeyEnd(odometerEdittext.getText().toString());
                dialog.dismiss();

            }


        });

        dialogClosedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog = dialogBuilder.create();
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        dialog.show();
    }
}
