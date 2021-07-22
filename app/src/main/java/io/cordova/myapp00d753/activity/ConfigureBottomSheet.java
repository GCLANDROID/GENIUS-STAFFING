package io.cordova.myapp00d753.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.concurrent.TimeUnit;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.UploadObject;
import io.cordova.myapp00d753.utility.Pref;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConfigureBottomSheet extends BottomSheetDialogFragment {
    View v;
    String locationPoint;
    TextView tvPoint,tvClose;
    String fenceid;
    private static final String SERVER_PATH = "https://www.cloud.geniusconsultant.com/GHRMSApi/api/";
   // private AttendanceService uploadService;
    ProgressDialog progressDialog;
    Pref pref;
    EditText etLocation;
    Button btnSubmit;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.configure_bottom_sheet, container, false);
        initView();
        onClick();
        return v;
    }

    private void initView(){
        pref=new Pref(getContext());
        Bundle test = getArguments();
        locationPoint = test.getString("locationPoint");
        fenceid=test.getString("fenceid").replace("[","").replace("]","");
        tvPoint=(TextView)v.findViewById(R.id.tvPoint);
        tvPoint.setText(locationPoint);
        tvClose=(TextView)v.findViewById(R.id.tvClose);

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .build();

        // Change base URL to your upload server URL.
      /*  uploadService = (AttendanceService) new Retrofit.Builder()
                .baseUrl(SERVER_PATH)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AttendanceService.class);*/

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Uploading...");
        etLocation=(EditText)v.findViewById(R.id.etLocation);
        btnSubmit=(Button) v.findViewById(R.id.btnSubmit);


    }

    private void onClick(){
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  fenceCreate();
            }
        });
    }


   /* private void fenceCreate() {

        String security = pref.getSecurityCode();

        progressDialog.show();

        Call<UploadObject> fileUpload = uploadService.createGeoFence(fenceid, etLocation.getText().toString(), security);
        fileUpload.enqueue(new Callback<UploadObject>() {
            @Override
            public void onResponse(Call<UploadObject> call, retrofit2.Response<UploadObject> response) {
                progressDialog.dismiss();
                UploadObject extraWorkingDayModel = response.body();
                if (extraWorkingDayModel.isResponseStatus()) {
                    showAlert();


                    Log.d("riku", "withocamera");
                } else {
                    Toast.makeText(getContext(),extraWorkingDayModel.responseText,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UploadObject> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("errot", t.getMessage());


                //   Toast.makeText(AttendanceManageActivity.this,"attendance saved without image",Toast.LENGTH_LONG).show();
            }

        });

    }*/

    private void showAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage("Geo Fence Config Suceesfully");
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                        Intent intent = new Intent(getContext(), EmployeeDashBoardActivity.class);
                        startActivity(intent);

                    }
                });
        alertDialogBuilder.show();


    }
}
