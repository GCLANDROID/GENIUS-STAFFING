package io.cordova.myapp00d753.activity;

import static android.os.Build.VERSION.SDK_INT;
import static java.util.Calendar.DAY_OF_MONTH;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import io.cordova.myapp00d753.AndroidXCamera.AndroidXCameraActivity;
import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.databinding.ActivityTempExperinceBinding;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.FindDocumentInformation;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.RealPathUtil;

public class TempExperinceActivity extends AppCompatActivity {
    ActivityTempExperinceBinding binding;
    String month;
    String doj="";
    String doe="";
    int flag=0;
    AlertDialog attachAlert;
    Uri image_uri;
    File compressedImageFile;
    String pdfFilePath, pdfFileName;
    private static final int DEFAULT_BUFFER_SIZE = 2048;
    int imageflag=0;
    Pref pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_temp_experince);
        initView();
    }

    private void initView(){
        pref=new Pref(TempExperinceActivity.this);
        binding.llFreshers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.imgFreshersTick.getVisibility()==View.GONE){
                    binding.imgFreshersTick.setVisibility(View.VISIBLE);
                    binding.imgExperience.setVisibility(View.GONE);
                    binding.llExpericedForm.setVisibility(View.GONE);
                    flag=1;
                }else {
                    binding.imgExperience.setVisibility(View.GONE);
                    binding.imgFreshersTick.setVisibility(View.GONE);
                    binding.llExpericedForm.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.llExperienced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.imgExperience.getVisibility()==View.GONE){
                    binding.imgFreshersTick.setVisibility(View.GONE);
                    binding.imgExperience.setVisibility(View.VISIBLE);
                    binding.llExpericedForm.setVisibility(View.VISIBLE);
                    flag=2;
                }else {
                    binding.imgFreshersTick.setVisibility(View.GONE);
                    binding.imgExperience.setVisibility(View.GONE);
                    binding.llExpericedForm.setVisibility(View.GONE);
                }
            }
        });

        binding.btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TempExperinceActivity.this,TempPanActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        binding.imgAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attechmentAlert(300,3001,2);
            }
        });
        binding.etDOJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                System.out.println("Current date : " + (now.get(Calendar.MONTH) + 1) + "-"
                        + now.get(Calendar.DATE) + "-" + now.get(Calendar.YEAR));

                now = Calendar.getInstance();
                now.add(Calendar.YEAR, -18);
                int dyear = now.get(Calendar.YEAR);
                final int dmonth = now.get(Calendar.MONTH);
                int dday = now.get(DAY_OF_MONTH);
                Calendar c1 = Calendar.getInstance();
                /*final int syear = year - 18;

                final int month1 = c1.get(Calendar.MONTH);
                final int sday1 = c1.get(DAY_OF_MONTH);*/


                final DatePickerDialog dialog = new DatePickerDialog(TempExperinceActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {

                        int mm = (m + 1);
                        int s = (m + 1) + d + y;
                        if (mm == 1) {
                            month = "January";
                        } else if (mm == 2) {
                            month = "February";
                        } else if (mm == 3) {
                            month = "March";
                        } else if (mm == 4) {
                            month = "April";
                        } else if (mm == 5) {
                            month = "May";
                        } else if (mm == 6) {
                            month = "June";
                        } else if (mm == 7) {
                            month = "July";
                        } else if (mm == 8) {
                            month = "August";
                        } else if (mm == 9) {
                            month = "September";
                        } else if (mm == 10) {
                            month = "October";
                        } else if (mm == 11) {
                            month = "November";
                        } else if (mm == 12) {
                            month = "December";
                        }
                        doj = d + " " + month + " " + y;

                        binding.etDOJ.setText(doj);



                    }
                }, dyear, dmonth, dday);
                dialog.getDatePicker();
                dialog.getDatePicker();
                dialog.show();
            }
        });

        binding.etDOE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                System.out.println("Current date : " + (now.get(Calendar.MONTH) + 1) + "-"
                        + now.get(Calendar.DATE) + "-" + now.get(Calendar.YEAR));

                now = Calendar.getInstance();
                now.add(Calendar.YEAR, -18);
                int dyear = now.get(Calendar.YEAR);
                final int dmonth = now.get(Calendar.MONTH);
                int dday = now.get(DAY_OF_MONTH);
                Calendar c1 = Calendar.getInstance();
                /*final int syear = year - 18;

                final int month1 = c1.get(Calendar.MONTH);
                final int sday1 = c1.get(DAY_OF_MONTH);*/


                final DatePickerDialog dialog = new DatePickerDialog(TempExperinceActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {

                        int mm = (m + 1);
                        int s = (m + 1) + d + y;
                        if (mm == 1) {
                            month = "January";
                        } else if (mm == 2) {
                            month = "February";
                        } else if (mm == 3) {
                            month = "March";
                        } else if (mm == 4) {
                            month = "April";
                        } else if (mm == 5) {
                            month = "May";
                        } else if (mm == 6) {
                            month = "June";
                        } else if (mm == 7) {
                            month = "July";
                        } else if (mm == 8) {
                            month = "August";
                        } else if (mm == 9) {
                            month = "September";
                        } else if (mm == 10) {
                            month = "October";
                        } else if (mm == 11) {
                            month = "November";
                        } else if (mm == 12) {
                            month = "December";
                        }
                        doe = d + " " + month + " " + y;

                        binding.etDOE.setText(doe);


                    }
                }, dyear, dmonth, dday);
                dialog.getDatePicker();
                dialog.getDatePicker();
                dialog.show();
            }
        });
        binding.btnSaveForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag!=0) {
                    if (flag==1){
                        experiencesubmit("0");
                    }else {
                        if (binding.etcompany.getText().toString().length()>0){
                            if (binding.etDesignation.getText().toString().length()>0){
                                if (binding.etManagerName.getText().toString().length()>0){
                                    if (binding.etManagerDesignation.getText().toString().length()>0){
                                        if (binding.etManagerContact.getText().toString().length()==10){
                                            if (!doj.equals("")){
                                                if (!doe.equals("")){
                                                    experiencesubmit("1");

                                                }else {
                                                    Toast.makeText(TempExperinceActivity.this,"Please Enter Previous Company’s DOE",Toast.LENGTH_LONG).show();

                                                }

                                            }else {
                                                Toast.makeText(TempExperinceActivity.this,"Please Enter Previous Company’s DOJ",Toast.LENGTH_LONG).show();

                                            }

                                        }else {
                                            Toast.makeText(TempExperinceActivity.this,"Please Enter Previous Reporting Manager’s Contact Details",Toast.LENGTH_LONG).show();

                                        }

                                    }else {
                                        Toast.makeText(TempExperinceActivity.this,"Please Enter Previous Reporting Manager’s Designation",Toast.LENGTH_LONG).show();

                                    }

                                }else {
                                    Toast.makeText(TempExperinceActivity.this,"Please Enter Previous Reporting Manager’s Name",Toast.LENGTH_LONG).show();

                                }

                            }else {
                                Toast.makeText(TempExperinceActivity.this,"Please Enter Previous Employee’s Designation",Toast.LENGTH_LONG).show();

                            }


                        }else {
                            Toast.makeText(TempExperinceActivity.this,"Please Enter Previous Company's Name",Toast.LENGTH_LONG).show();
                        }

                    }
                }else {
                    Toast.makeText(TempExperinceActivity.this,"Please select Freshers/Experienced option",Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    private void attechmentAlert(int gallerycode,int cameracode,int intentflag) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TempExperinceActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_attachment, null);
        dialogBuilder.setView(dialogView);
        LinearLayout lnCamera=(LinearLayout)dialogView.findViewById(R.id.lnCamera);
        LinearLayout lnGallery=(LinearLayout)dialogView.findViewById(R.id.lnGallery);
        lnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndroidXCameraActivity.launch(TempExperinceActivity.this, cameracode);
                attachAlert.dismiss();
            }
        });

        if (gallerycode==100){
            lnGallery.setVisibility(View.GONE);
        }


        lnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (intentflag==1){
                    galleryIntent(gallerycode);
                }else {
                    pdfChoose(gallerycode);
                }

                attachAlert.dismiss();
            }
        });




        attachAlert = dialogBuilder.create();
        attachAlert.setCancelable(false);
        Window window = attachAlert.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        attachAlert.show();
    }
    private void galleryIntent(int gallerycode) {
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
        openGalleryIntent.setType("image/*");
        startActivityForResult(openGalleryIntent, gallerycode);
    }
    private void pdfChoose(int code){
        Intent uploadIntent = new Intent(Intent.ACTION_GET_CONTENT);
        uploadIntent.setType("application/pdf");
        startActivityForResult(uploadIntent, code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if (requestCode == 2000 && resultCode == 3001){
            Log.e("TAG", "onActivityResult: "+data.getExtras().get("picture"));
            Log.e("TAG", "onActivityResult: "+data.getExtras().get(AndroidXCameraActivity.IMAGE_PATH_KEY));
            image_uri =  Uri.parse(String.valueOf(data.getExtras().get("picture")));
            //image_uri = (Uri) data.getExtras().get(AndroidXCameraActivity.IMAGE_PATH_KEY);
            compressedImageFile = new File(String.valueOf(data.getExtras().get("picture")));

            if (image_uri != null){
                binding.imgAttachImage.setImageURI(image_uri);
                imageflag=1;

            }
        }else  if ((requestCode == 300 )){
            Uri uri = data.getData();
            Log.e("TAG", "onActivityResult: "+uri.getPath());
            String imagePath = uri.getPath();
            if (imagePath.contains("all_external")){
                pdfFileName = FindDocumentInformation.FileNameFromURL(imagePath);
                compressedImageFile = convertInputStreamToFile(uri,pdfFileName);

            } else {
                try {
                    pdfFilePath = getRealPath(TempExperinceActivity.this,uri);
                    pdfFileName = FindDocumentInformation.FileNameFromURL(pdfFilePath);
                } catch (IllegalArgumentException e){
                    //Todo: from WPS office document select
                    pdfFileName = FindDocumentInformation.FileNameFromURL(imagePath);
                    compressedImageFile = convertInputStreamToFile(uri,pdfFileName);

                }
                compressedImageFile = convertInputStreamToFile(uri,pdfFileName);

            }
            binding.imgAttachImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_pdf));
             imageflag = 1;
            //flag++;
        }





    }

    public static String getRealPath(Context context, Uri fileUri) {
        String realPath;
        Log.e("SDK_INT", "= " + SDK_INT);
        // SDK < API11
        if (SDK_INT < 11) {
            realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(context, fileUri);
        }
        // SDK >= 11 && SDK < 19
        else if (SDK_INT < 19) {
            realPath = RealPathUtil.getRealPathFromURI_API11to18(context, fileUri);
        }
        // SDK > 19 (Android 4.4) and up
        else {
            realPath = RealPathUtil.getRealPathFromURI_API19(context, fileUri);
        }
        return realPath;
    }

    private File convertInputStreamToFile(Uri uri, String fileNme) {
        InputStream inputStream;
        try {
            inputStream = TempExperinceActivity.this.getContentResolver().openInputStream(uri);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        File file = new File(TempExperinceActivity.this.getExternalFilesDir("/").getAbsolutePath(), fileNme);

        try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
            int read;
            byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    private void experiencesubmit(String isexperince) {
        ProgressDialog progressDialog=new ProgressDialog(TempExperinceActivity.this);
        progressDialog.setMessage("Uploading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.show();

        //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        AndroidNetworking.upload(AppData.SaveExperienceDetails)
                .addMultipartParameter("AEMEmployeeID",pref.getEmpId())
                .addMultipartParameter("isExperience", isexperince)
                .addMultipartParameter("SBIExperienceMonth", binding.etSBIExp.getText().toString())
                .addMultipartParameter("NonSBIExperienceMonth", binding.etNonSBIExp.getText().toString())
                .addMultipartParameter("PreviousCompanyName", binding.etcompany.getText().toString())
                .addMultipartParameter("PreviousEmployeeDesignation", binding.etDesignation.getText().toString())
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())
                .addMultipartParameter("DocumentID", "0070")
                .addMultipartParameter("PreviousReportingManagerName", binding.etManagerName.getText().toString())
                .addMultipartParameter("PreviousReportingManagerDesignation", binding.etManagerDesignation.getText().toString())
                .addMultipartParameter("PreviousReportingManagerContactDetails", binding.etManagerContact.getText().toString())
                .addMultipartParameter("PreviousCompanyDOJ", doj)
                .addMultipartParameter("PreviousCompanyDOE", doe)
                .addMultipartFile("SingleFile", compressedImageFile)
                .setPercentageThresholdForCancelling(60)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        progressDialog.show();


                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();


                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);

                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            Intent intent = new Intent(TempExperinceActivity.this, TempPanActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            Toast.makeText(TempExperinceActivity.this,"Experience Details has been updated Successfully",Toast.LENGTH_LONG).show();

                        }else {


                        }

                        // boolean _status = job1.getBoolean("status");



                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e("errt", String.valueOf(error));

                        Toast.makeText(getApplicationContext(), "Something went wrong,Please try again", Toast.LENGTH_LONG).show();
                    }
                });

    }

}