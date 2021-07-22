package io.cordova.myapp00d753.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Locale;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.AttendanceManageModule;
import io.cordova.myapp00d753.module.AttendanceService;
import io.cordova.myapp00d753.utility.ApiClient;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.GPSTracker;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.TouchImageView;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PreviewActivity extends AppCompatActivity {

    String imageFileName;

    TouchImageView imgLongPreview;


    ImageView imgClose, imgPreview;
    GPSTracker gps;
    double latitude,longitude;
    String address;


    private static final String SERVER_PATH = AppData.url;
    private AttendanceService uploadService;
    Pref pref;
    String addr;
    AlertDialog alerDialog1;



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        TextView textView = (TextView) findViewById(R.id.info);
         pref=new Pref(PreviewActivity.this);

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


        byte[] byteArray = getIntent().getByteArrayExtra("image");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        String msg = getIntent().getStringExtra("info");
        textView.setText(msg);


        address=getIntent().getStringExtra("address");
        Log.d("address",address);
        String latt= String.valueOf(latitude);
        String longtt= String.valueOf(longitude);

        if (msg.equals(pref.getMasterId())){
            attendanceGivenfunction(longtt,latt);

        }else {
           Toast.makeText(PreviewActivity.this,"Sorry!No Recognized",Toast.LENGTH_LONG).show();
        }


        imgPreview=(ImageView) findViewById(R.id.imgPreview);
        imgPreview.setImageBitmap(bitmap);

        imgClose = (ImageView) findViewById(R.id.imgClose);
        imgLongPreview = (TouchImageView) findViewById(R.id.imgPreview);


        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PreviewActivity.this, FRDashboard.class);
                startActivity(intent);
                finish();
            }
        });







//        if (getIntent().getExtras() != null && getIntent().hasExtra("imageName")) {
//            imageFileName = getIntent().getExtras().getString("imageName");
//
//
//            Bitmap d = BitmapFactory.decodeFile(imageFileName);
//            int newHeight = (int) (d.getHeight() * (512.0 / d.getWidth()));
//            Bitmap putImage = Bitmap.createScaledBitmap(d, 512, newHeight, true);
//            imgLongPreview.setImageBitmap(putImage);
////        } else {
////
////            if (getIntent().getExtras() == null && getIntent().hasExtra("imageName"));
////
////            {
////                Toast.makeText(getApplicationContext(),"No face detected or Unknown person", Toast.LENGTH_SHORT).show();
////
////            }
////
////
////        }
//        }

    }



    private int count = 0;
    @Override
    public void onBackPressed() {
        count++;
        if (count >=1) {
        /* If count is greater than 1 ,you can either move to the next
        activity or just quit. */
            Intent intent = new Intent(PreviewActivity.this, FRDashboard.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Press back again to Leave!", Toast.LENGTH_SHORT).show();

            // resetting the counter in 2s
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    count = 0;
                }
            }, 2000);
        }
        super.onBackPressed();
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


    private void attendanceGivenfunction(String longt,String lat ) {




        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("uploading...");
        progressBar.show();
        Call<AttendanceManageModule> datumCall = ApiClient.getService().getDatas(pref.getEmpId(), address, longt, lat, pref.getSecurityCode());
        datumCall.enqueue(new Callback<AttendanceManageModule>() {
            @Override
            public void onResponse(Call<AttendanceManageModule> call, Response<AttendanceManageModule> response) {
                progressBar.dismiss();
                AttendanceManageModule extraWorkingDayModel = response.body();
                if (extraWorkingDayModel.isResponseStatus()) {
                    String responseText=extraWorkingDayModel.getResponseText();
                    successAlert(responseText);
                    Log.d("riku", "withoutcamera");
                } else {
                     Toast.makeText(getApplicationContext(), extraWorkingDayModel.getResponseText(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AttendanceManageModule> call, Throwable t) {

            }


        });
    }


    private void successAlert(String t) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PreviewActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);


            tvInvalidDate.setText(t);

        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                Intent intent=new Intent(PreviewActivity.this,AttendanceReportActivity.class);
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


}
