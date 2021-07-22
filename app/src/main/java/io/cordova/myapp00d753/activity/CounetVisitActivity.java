package io.cordova.myapp00d753.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.provider.MediaStore;

import android.os.Bundle;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import com.androidnetworking.interfaces.UploadProgressListener;
import com.developers.imagezipper.ImageZipper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.SpinnerItemModule;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.GPSTracker;
import io.cordova.myapp00d753.utility.Pref;

public class CounetVisitActivity extends AppCompatActivity {
    TextView tvAddress;
    Spinner spCounter;
    ImageView imgCamera,imgPhoto;
    Button btnSubmit;
    GPSTracker gps;
    double latitude,longitude;
    String address;

    private String encodedImage;
    private Uri imageUri;
    private static final int CAMERA_REQUEST = 1;
    File file, compressedImageFile;
    Pref prefManager;
    ArrayList<SpinnerItemModule>mcounterList=new ArrayList<>();
    ArrayList<String>counterList=new ArrayList<>();
    String latt,longt;
    EditText etRemark;
    String salePointId="";
    String capFlag="";
    AlertDialog alerDialog1;
    ImageView imgBack,imgHome;
    TextView tvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counet_visit);
        initView();
        setCounter();

        onClick();
    }

    private void initView(){
        prefManager=new Pref(CounetVisitActivity.this);
        tvAddress=(TextView)findViewById(R.id.tvAddress);
        spCounter=(Spinner)findViewById(R.id.spCounter);
        imgCamera=(ImageView)findViewById(R.id.imgCamera);
        imgPhoto=(ImageView)findViewById(R.id.imgPhoto);

        btnSubmit=(Button) findViewById(R.id.btnSubmit);


        gps = new GPSTracker(CounetVisitActivity.this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            Log.d("saikatdas", String.valueOf(latitude));
            longitude = gps.getLongitude();
        } else {
// can't get location
// GPS or Network is not enabled
// Ask user to enable GPS/network in settings

        }
        address = getCompleteAddressString(latitude, longitude);
        tvAddress.setText(address);
        latt= String.valueOf(latitude);
        longt= String.valueOf(longitude);

        etRemark=(EditText)findViewById(R.id.etRemark);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        tvTime=(TextView)findViewById(R.id.tvTime);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy 'at' HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        tvTime.setText("Time: "+currentDateandTime);
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

    private void onClick(){
        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               cameraIntent();
            }
        });

        spCounter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>0){
                    salePointId=mcounterList.get(i).getItemId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!salePointId.equals("")){
                    if (capFlag.equals("1")){
                        postCounterVisit();

                    }else {
                        Toast.makeText(CounetVisitActivity.this,"Please capture counter Image",Toast.LENGTH_LONG).show();
                    }

                }else {
                    Toast.makeText(getApplicationContext(),"Please select Counter",Toast.LENGTH_LONG).show();
                }
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CounetVisitActivity.this,EmployeeDashBoardActivity.class);
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
                            compressedImageFile = new ImageZipper(CounetVisitActivity.this)
                                    .setQuality(75)
                                    .setMaxWidth(300)
                                    .setMaxHeight(300)
                                    .compressToFile(file);


                            BitmapFactory.Options o = new BitmapFactory.Options();
                            o.inSampleSize = 6;
                            Bitmap bm = cropToSquare(BitmapFactory.decodeFile(imageurl, o));
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
                            byte[] b = baos.toByteArray();
                            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                            imgPhoto.setImageBitmap(bm);
                            capFlag="1";


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

    private void setCounter() {
        Log.d("hitr", "1");

        String surl = AppData.url+ "gcl_CommonDDL?ddltype=9200&ID=0&ID1=0&ID2=0&ID3=0&SecurityCode=" + prefManager.getSecurityCode();
        Log.d("ctegoryinput", surl);
        final ProgressDialog pd=new ProgressDialog(CounetVisitActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseIFBCategory", response);
                        pd.dismiss();
                        counterList.clear();
                        mcounterList.clear();
                        counterList.add("Please select");
                        mcounterList.add(new SpinnerItemModule("0", "0"));

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
                                    String value = obj.optString("value");
                                    String id = obj.optString("id");
                                    counterList.add(value);
                                    SpinnerItemModule itemModule = new SpinnerItemModule(value, id);
                                    mcounterList.add(itemModule);

                                }




                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (CounetVisitActivity.this, android.R.layout.simple_spinner_item,
                                                counterList); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spCounter.setAdapter(spinnerArrayAdapter);


                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CounetVisitActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.d("errort", "category");
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(CounetVisitActivity.this);
        requestQueue.add(stringRequest);

    }

    private void postCounterVisit() {

        final ProgressDialog pd = new ProgressDialog(CounetVisitActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();

        AndroidNetworking.upload(AppData.url+"post_EmployeeVisitActivity")
                .addMultipartParameter("AEMEmployeeID", prefManager.getEmpId())
                .addMultipartParameter("SalesPointID", salePointId)
                .addMultipartParameter("Remarks", etRemark.getText().toString())
                .addMultipartParameter("Longitude", longt)
                .addMultipartParameter("Latitude", latt)
                .addMultipartParameter("Address", address)
                .addMultipartFile("Image", compressedImageFile)
                .addMultipartParameter("SecurityCode", prefManager.getSecurityCode())

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



                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        String responseText = job1.optString("responseText");
                        Log.d("responseText", responseText);
                        boolean responseStatus=job1.optBoolean("responseStatus");
                        if (responseStatus) {
                            successAlert(responseText);
                            pd.dismiss();

                        } else {
                            pd.dismiss();
                            Toast.makeText(CounetVisitActivity.this, responseText, Toast.LENGTH_LONG).show();

                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void successAlert(String text) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CounetVisitActivity.this, R.style.CustomDialogNew);
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
                Intent intent = new Intent(CounetVisitActivity.this, CounterVisitEntryActivity.class);
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