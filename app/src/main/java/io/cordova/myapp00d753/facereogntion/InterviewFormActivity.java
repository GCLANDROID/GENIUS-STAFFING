package io.cordova.myapp00d753.facereogntion;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;

import com.google.android.cameraview.LongImageBackCameraActivity;
import com.google.android.cameraview.LongImageCameraActivity;
import com.google.android.cameraview.LongImageFrontCameraActivity;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.databinding.ActivityInterviewFormBinding;

public class InterviewFormActivity extends AppCompatActivity {
    ActivityInterviewFormBinding binding;
    ArrayList<String>idList=new ArrayList<>();
    String idProof="";
    String dob="";
    int selfieFlag=0;
    int selfieleftFlag=0;
    int selfierightFlag=0;
    int idFlag=0;
    AlertDialog alerDialog1;
    String next;
    String CandidateID,EmployeeName,MobileNumber,Fathername,DocumentName,DocumentID,ReferenceNo,DOB;

    String docImage="";
    String frontImage="";
    String rightImage="";
    String leftImage="";
    File oneFile,twoFile,threeFile;
     ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_interview_form);
        initView();
        onClick();
    }

    private void initView(){
         pd = new ProgressDialog(InterviewFormActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);

        ReferenceNo=getIntent().getStringExtra("ReferenceNo");
        DocumentID=getIntent().getStringExtra("DocumentID");
        DocumentName=getIntent().getStringExtra("DocumentName");
        Fathername=getIntent().getStringExtra("Fathername");
        MobileNumber=getIntent().getStringExtra("MobileNumber");
        EmployeeName=getIntent().getStringExtra("EmployeeName");
        CandidateID=getIntent().getStringExtra("CandidateID");
        DOB=getIntent().getStringExtra("DOB");
        dob=DOB;

        idList.add("Please Select");
        idList.add("Aadhaar Card");
        idList.add("PAN Card");


        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>
                (InterviewFormActivity.this, android.R.layout.simple_spinner_item,
                        idList); //selected item will look like a spinner set from XML
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spID.setAdapter(typeAdapter);
        int index=idList.indexOf(DocumentName);
        binding.spID.setSelection(index);
        binding.spID.setEnabled(false);
         next = "<font color='#EE0000'>*</font>";
        binding.tvFullNameTitle.setText(Html.fromHtml("Full Name: "+next));
        binding.tvFatherNameTitle.setText(Html.fromHtml("Father's Name: "+next));
        binding.tvDOBTile.setText(Html.fromHtml("Date Of Birth:  "+next));
        binding.tvIDProofTitle.setText(Html.fromHtml("ID Proof: "+next));
        binding.tvSelfieFontFaceTitle.setText(Html.fromHtml("Image 1: "+next));
        binding.tvSelfieRightFaceUploadTitle.setText(Html.fromHtml("Image 2: "+next));
        binding.tvSelfieLeftUploadTitle.setText(Html.fromHtml("Image 3: "+next));

        binding.etRefNo.setText(ReferenceNo);
        binding.etRefNo.setEnabled(false);

        binding.etFirstName.setText(EmployeeName);
        binding.etFatherName.setText(Fathername);
        binding.tvDOB.setText(dob);

    }

    private void onClick(){
        binding.spID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>0){
                    idProof=idList.get(i);
                    binding.tvRef.setText(Html.fromHtml(idProof+" Number: "+next));
                    binding.tvIDProof.setText(Html.fromHtml(idProof+" \nImage: "+next));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.imgDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStrtDatePicker();
            }
        });

        binding.lnID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LongImageBackCameraActivity.launch(InterviewFormActivity.this);
            }
        });

        binding.lnSelfieFontFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LongImageFrontCameraActivity.launch(InterviewFormActivity.this);
            }
        });

        binding.lnSelfieLeftFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LongImageFrontCameraActivity.launchleft(InterviewFormActivity.this);
            }
        });
        binding.lnSelfieRightFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LongImageFrontCameraActivity.launchright(InterviewFormActivity.this);
            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.etFirstName.getText().toString().length()>0){

                        if (binding.etFatherName.getText().toString().length()>0){
                            if (!dob.equals("")){
                                if (!idProof.equals("")){
                                   if (binding.etRefNo.getText().toString().length()>0){
                                       if (idFlag==1){
                                           if (selfieFlag==1){
                                               if (selfierightFlag==1){
                                                   if (selfieleftFlag==1){
                                                       if (idProof.contains("Aadhaar")){
                                                           AddharValidation();
                                                       }else {
                                                           PANValidation();
                                                       }
                                                   }else {
                                                       Toast.makeText(InterviewFormActivity.this,"Please Upload Your Left Side Face Image",Toast.LENGTH_LONG).show();

                                                   }

                                               }else {
                                                   Toast.makeText(InterviewFormActivity.this,"Please Upload Your Right Side Face Image",Toast.LENGTH_LONG).show();
                                               }


                                           }else {
                                               Toast.makeText(InterviewFormActivity.this,"Please Upload Your Front Side Face Image",Toast.LENGTH_LONG).show();

                                           }

                                       }else {
                                           Toast.makeText(InterviewFormActivity.this,"Please Upload Your ID Proof Image",Toast.LENGTH_LONG).show();

                                       }
                                   }else {
                                       Toast.makeText(InterviewFormActivity.this,"Please Enter "+idProof+" Number",Toast.LENGTH_LONG).show();
                                   }

                                }else {
                                    Toast.makeText(InterviewFormActivity.this,"Please Select Your ID Proof",Toast.LENGTH_LONG).show();

                                }

                            }else {
                                Toast.makeText(InterviewFormActivity.this,"Please Your Enter Date Of Birth",Toast.LENGTH_LONG).show();
                            }

                        }else {
                            Toast.makeText(InterviewFormActivity.this,"Please Enter Your Father's Name",Toast.LENGTH_LONG).show();

                        }

                }else {
                    Toast.makeText(InterviewFormActivity.this,"Please Enter Your Full  Name",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void showStrtDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(InterviewFormActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {



                        int month = (monthOfYear + 1);
                        dob = dayOfMonth + "/" + month + "/" + year;
                        binding.tvDOB.setText(dob);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case LongImageBackCameraActivity.LONG_IMAGE_RESULT_CODE_2:


                if (resultCode == RESULT_OK && requestCode == LongImageBackCameraActivity.LONG_IMAGE_RESULT_CODE_2) {
                    File file = (File) data.getExtras().get("picture");

                    String imageFileName = data.getStringExtra(LongImageCameraActivity.IMAGE_PATH_KEY);
                    Log.d("imageFileName", imageFileName);
                    Bitmap d = BitmapFactory.decodeFile(imageFileName);
                    int newHeight = (int) (d.getHeight() * (512.0 / d.getWidth()));
                    Bitmap putImage = Bitmap.createScaledBitmap(d, 512, newHeight, true);
                    binding.imgIDPic.setImageBitmap(putImage);
                    idFlag = 1;

                    File pictureFile = (File) data.getExtras().get("picture");


                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    putImage.compress(Bitmap.CompressFormat.PNG, 10, baos); //bm is the bitmap object
                    byte[] b = baos.toByteArray();
                    String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

                    String contentType = "image/png";
                    String[] brkDown = imageFileName.split("/");
                    String name = brkDown[6];
                    docImage = name + "_" + encodedImage + "_" + contentType;

                }
                break;

            case LongImageFrontCameraActivity.LONG_IMAGE_RESULT_CODE:


                if (resultCode == RESULT_OK && requestCode == LongImageFrontCameraActivity.LONG_IMAGE_RESULT_CODE) {
                    oneFile = (File) data.getExtras().get("picture");

                    String imageFileName = data.getStringExtra(LongImageFrontCameraActivity.IMAGE_PATH_KEY);
                    Log.d("imageFileName", imageFileName);
                    Bitmap d = BitmapFactory.decodeFile(imageFileName);
                    int newHeight = (int) (d.getHeight() * (512.0 / d.getWidth()));
                    Bitmap putImage = Bitmap.createScaledBitmap(d, 512, newHeight, true);
                    binding.imgSelfieFontFacePic.setImageBitmap(putImage);
                    selfieFlag = 1;

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    putImage.compress(Bitmap.CompressFormat.PNG, 10, baos); //bm is the bitmap object
                    byte[] b = baos.toByteArray();
                    String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

                    String contentType = "image/png";
                    String[] brkDown = imageFileName.split("/");
                    String name = brkDown[6];
                    frontImage = name + "_" + encodedImage + "_" + contentType;


                }
                break;

            case LongImageFrontCameraActivity.LONG_IMAGE_RESULT_CODE_RIGHT:


                if (resultCode == RESULT_OK && requestCode == LongImageFrontCameraActivity.LONG_IMAGE_RESULT_CODE_RIGHT) {
                    twoFile = (File) data.getExtras().get("picture");

                    String imageFileName = data.getStringExtra(LongImageFrontCameraActivity.IMAGE_PATH_KEY);
                    Log.d("imageFileName", imageFileName);
                    Bitmap d = BitmapFactory.decodeFile(imageFileName);
                    int newHeight = (int) (d.getHeight() * (512.0 / d.getWidth()));
                    Bitmap putImage = Bitmap.createScaledBitmap(d, 512, newHeight, true);
                    binding.imgSelfieRightFacePic.setImageBitmap(putImage);
                    selfierightFlag = 1;

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    putImage.compress(Bitmap.CompressFormat.PNG, 10, baos); //bm is the bitmap object
                    byte[] b = baos.toByteArray();
                    String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

                    String contentType = "image/png";
                    String[] brkDown = imageFileName.split("/");
                    String name = brkDown[6];
                    rightImage = name + "_" + encodedImage + "_" + contentType;


                }
                break;

            case LongImageFrontCameraActivity.LONG_IMAGE_RESULT_CODE_LEFT:


                if (resultCode == RESULT_OK && requestCode == LongImageFrontCameraActivity.LONG_IMAGE_RESULT_CODE_LEFT) {
                    threeFile = (File) data.getExtras().get("picture");

                    String imageFileName = data.getStringExtra(LongImageFrontCameraActivity.IMAGE_PATH_KEY);
                    Log.d("imageFileName", imageFileName);
                    Bitmap d = BitmapFactory.decodeFile(imageFileName);
                    int newHeight = (int) (d.getHeight() * (512.0 / d.getWidth()));
                    Bitmap putImage = Bitmap.createScaledBitmap(d, 512, newHeight, true);
                    binding.imgSelfieLeftPic.setImageBitmap(putImage);
                    selfieleftFlag = 1;


                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    putImage.compress(Bitmap.CompressFormat.PNG, 10, baos); //bm is the bitmap object
                    byte[] b = baos.toByteArray();
                    String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

                    String contentType = "image/png";
                    String[] brkDown = imageFileName.split("/");
                    String name = brkDown[6];
                    leftImage = name + "_" + encodedImage + "_" + contentType;


                }
                break;
        }


    }

    public boolean isValidAadhaarNumber (String text){
        // Regex to check valid Aadhaar number.
        String regex
                = "^[2-9]{1}[0-9]{3}\\s[0-9]{4}\\s[0-9]{4}$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the string is empty
        // return false
        if (text == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given string
        // and regular expression.
        Matcher m = p.matcher(text);

        // Return if the string
        // matched the ReGex
        return m.matches();
    }

    public boolean isValidAadhaarNumberr (String text){
        // Regex to check valid Aadhaar number.
        String regex
                = "^[2-9]{1}[0-9]{3}[0-9]{4}[0-9]{4}$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the string is empty
        // return false
        if (text == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given string
        // and regular expression.
        Matcher m = p.matcher(text);

        // Return if the string
        // matched the ReGex
        return m.matches();
    }

    public static boolean isValidEPICNumber(String str)
    {
        // Regex to check valid EPIC Number
        String regex = "^[A-Z]{3}[0-9]{7}$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the str is empty return false
        if (str == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given
        // EPIC Number using regular expression.
        Matcher m = p.matcher(str);

        // Return if the str
        // matched the ReGex
        return m.matches();
    }


    public static boolean isValidPANNumber(String str)
    {
        // Regex to check valid EPIC Number
        String regex = "[A-Z]{5}[0-9]{4}[A-Z]{1}";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the str is empty return false
        if (str == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given
        // EPIC Number using regular expression.
        Matcher m = p.matcher(str);

        // Return if the str
        // matched the ReGex
        return m.matches();
    }

    private void AddharValidation(){
        if (isValidAadhaarNumber(binding.etRefNo.getText().toString()) || isValidAadhaarNumberr(binding.etRefNo.getText().toString())){
             postFaceData();
        }else {
            Toast.makeText(InterviewFormActivity.this,"Please Enter Valid Aadhaar Number",Toast.LENGTH_LONG).show();
        }
    }

    private void PANValidation(){
        if (isValidPANNumber(binding.etRefNo.getText().toString())){
            postFaceData();

        }else {
            Toast.makeText(InterviewFormActivity.this,"Please Enter Valid PAN Number",Toast.LENGTH_LONG).show();
        }
    }

    private void successAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(InterviewFormActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);

        tvInvalidDate.setText("Your interview form has been saved successfully");



        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                Intent intent=new Intent(InterviewFormActivity.this,LoginDashboardActivity.class);
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

    private void postFaceData() {

        pd.show();

        AndroidNetworking.upload("https://geniusconsultant.pythonanywhere.com/api/faceupload")
                .addMultipartParameter("ID", CandidateID)
                .addMultipartParameter("Name", binding.etFirstName.getText().toString())
                .addMultipartFile("img1",oneFile)
                .addMultipartFile("img2",twoFile)
                .addMultipartFile("img3",threeFile)
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
                        pd.show();


                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        String Error = job1.optString("Error");
                        boolean responseStatus = job1.optBoolean("status");
                        if (responseStatus) {
                            postData();


                        } else {
                            pd.dismiss();
                            errorAlert(Error);

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
    private void postData() {


        pd.show();

        AndroidNetworking.upload("http://gsppi.geniusconsultant.com/GeniusiOSApi/api/post_SBICardCandidate")
                .addMultipartParameter("CandidateID", CandidateID)
                .addMultipartParameter("EmployeeName", binding.etFirstName.getText().toString())
                .addMultipartParameter("MobileNumber", MobileNumber)
                .addMultipartParameter("Fathername", binding.etFatherName.getText().toString())
                .addMultipartParameter("DOB", dob)
                .addMultipartParameter("DocumentID", DocumentID)
                .addMultipartParameter("ReferenceNo", binding.etRefNo.getText().toString())
                .addMultipartParameter("DeviceID", "0")
                .addMultipartParameter("DeviceName", "A")
                .addMultipartParameter("Longitude", "0.00")
                .addMultipartParameter("Latitude", "0.00")
                .addMultipartParameter("Geo_Address", "00")
                .addMultipartParameter("DocumentFname", docImage)
                .addMultipartParameter("PhotoFname1", frontImage)
                .addMultipartParameter("PhotoFname2", rightImage)
                .addMultipartParameter("PhotoFname3", leftImage)
                .addMultipartParameter("UserID", "SBIHR")
                .addMultipartParameter("Operation", "2")
                .addMultipartParameter("SecurityCode", "0000")

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
                        String responseText = job1.optString("responseText");
                        Log.d("responseText", responseText);
                        boolean responseStatus = job1.optBoolean("responseStatus");
                        if (responseStatus) {
                            successAlert();
                            pd.dismiss();

                        } else {
                            pd.dismiss();
                            Toast.makeText(InterviewFormActivity.this, responseText, Toast.LENGTH_LONG).show();

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

    private void errorAlert(String msg) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(InterviewFormActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_alerts, null);
        dialogBuilder.setView(dialogView);
        TextView tvSuccess=(TextView)dialogView.findViewById(R.id.tvSuccess);
        tvSuccess.setText(msg);



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



}