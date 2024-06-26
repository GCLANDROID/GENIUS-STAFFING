package io.cordova.myapp00d753.facereogntion;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

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
import com.androidnetworking.interfaces.UploadProgressListener;

import com.google.android.cameraview.LongImageBackCameraActivity;
import com.google.android.cameraview.LongImageCameraActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.databinding.ActivityInterViewerPanelBinding;
import io.cordova.myapp00d753.utility.AppData;

public class InterViewerPanelActivity extends AppCompatActivity {
    ActivityInterViewerPanelBinding binding;
    int selfieFlag=0;
    AlertDialog alerDialog1;
    File  file;
    String CandidateID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_inter_viewer_panel);

        onClick();
    }

    public void onClick(){

        String next = "<font color='#EE0000'>*</font>";
        binding.tvCanFace.setText(Html.fromHtml("Upload Candidate's \nFace Image: "+next));
        binding.btnGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.etRefNo.getText().toString().length()>0){
                    getCanData();

                }else {
                    Toast.makeText(InterViewerPanelActivity.this,"Please Enter ID Proof Number",Toast.LENGTH_LONG).show();
                }
            }
        });
        binding.lnSelfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LongImageBackCameraActivity.launch(InterViewerPanelActivity.this);
            }
        });

        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selfieFlag==1){
                    //successAlert();
                    faceVerification();
                }else {
                    Toast.makeText(InterViewerPanelActivity.this,"Please Capture Candidate's Face Image",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case LongImageBackCameraActivity.LONG_IMAGE_RESULT_CODE_2:


                if (resultCode == RESULT_OK && requestCode == LongImageBackCameraActivity.LONG_IMAGE_RESULT_CODE_2) {
                     file = (File) data.getExtras().get("picture");

                    String imageFileName = data.getStringExtra(LongImageCameraActivity.IMAGE_PATH_KEY);
                    Log.d("imageFileName", imageFileName);
                    Bitmap d = BitmapFactory.decodeFile(imageFileName);
                    int newHeight = (int) (d.getHeight() * (512.0 / d.getWidth()));
                    Bitmap putImage = Bitmap.createScaledBitmap(d, 512, newHeight, true);
                    binding.imgSelfie.setImageBitmap(putImage);
                    selfieFlag = 1;

                }
                break;


        }
    }



    private void successAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(InterViewerPanelActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);

        tvInvalidDate.setText("Face has been matched.");



        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

    private void errorAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(InterViewerPanelActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_alerts, null);
        dialogBuilder.setView(dialogView);



        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

    public void getCanData() {

        String surl = AppData.url+ "get_SBICardCandidate?CandidateID=0&ReferenceNo=" + binding.etRefNo.getText().toString() + "&Operation=1";
        Log.d("inputLogin", surl);

        final ProgressDialog progressDialog = new ProgressDialog(InterViewerPanelActivity.this);
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
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                                JSONArray responseData = job1.optJSONArray("responseData");

                                JSONObject obj = responseData.getJSONObject(0);
                                 CandidateID = obj.optString("CandidateID");
                                String EmployeeName = obj.optString("EmployeeName");
                                binding.etFirstName.setText(EmployeeName);
                                String MobileNumber = obj.optString("MobileNumber");
                                String Fathername = obj.optString("Fathername");
                                binding.etFatherName.setText(Fathername);
                                String DocumentName = obj.optString("DocumentName");
                                binding.etIDProof.setText(DocumentName);
                                String DocumentID = obj.optString("DocumentID");
                                String ReferenceNo = obj.optString("ReferenceNo");
                                binding.etRefNo.setText(ReferenceNo);
                                String DOB=obj.optString("DOB");
                                binding.etDOB.setText(DOB);
                                binding.lnData.setVisibility(View.VISIBLE);

                                String Doc_ByteData=obj.optString("Doc_ByteData");
                                byte[] decodedString = Base64.decode(Doc_ByteData, Base64.DEFAULT);
                                Bitmap docImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                binding.imgID.setImageBitmap(docImage);

                                getCanDataSelfieData();


                            } else {
                                binding.lnData.setVisibility(View.GONE);
                                Toast.makeText(InterViewerPanelActivity.this, "Sorry! Invalid Reference Number", Toast.LENGTH_LONG).show();

                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(InterViewerPanelActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                //  Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();


            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(InterViewerPanelActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                300000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void getCanDataSelfieData() {

        String surl = AppData.url+"get_SBICardCandidate?CandidateID=0&ReferenceNo=" + binding.etRefNo.getText().toString() + "&Operation=2";
        Log.d("inputLogin", surl);

        final ProgressDialog progressDialog = new ProgressDialog(InterViewerPanelActivity.this);
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
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                                JSONArray responseData = job1.optJSONArray("responseData");

                                JSONObject obj = responseData.getJSONObject(0);

                                String ByteData=obj.optString("ByteData");
                                byte[] decodedString = Base64.decode(ByteData, Base64.DEFAULT);
                                Bitmap selfieImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                binding.imgCanImage.setImageBitmap(selfieImage);


                            } else {

                                Toast.makeText(InterViewerPanelActivity.this, "Sorry! Invalid Reference Number", Toast.LENGTH_LONG).show();

                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(InterViewerPanelActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                //  Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();


            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(InterViewerPanelActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                300000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    private void faceVerification() {

        final ProgressDialog pd = new ProgressDialog(InterViewerPanelActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();

        AndroidNetworking.upload("https://geniusconsultant.pythonanywhere.com/api/facematch")
                .addMultipartFile("photo", file)
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

                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        String Person_Name = job1.optString("Person Name");
                        if (Person_Name.equals(CandidateID)){
                            successAlert();
                        }else {

                            errorAlert();
                        }



                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG);
                    }
                });
    }
}