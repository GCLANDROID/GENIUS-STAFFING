package io.cordova.myapp00d753.activity;

import static android.os.Build.VERSION.SDK_INT;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.kyanogen.signatureview.SignatureView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import io.cordova.myapp00d753.AndroidXCamera.AndroidXCameraActivity;
import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.databinding.ActivityTempOtherDocumentBinding;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.FindDocumentInformation;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.RealPathUtil;
import okhttp3.Call;
import okhttp3.OkHttpClient;

public class TempOtherDocumentActivity extends AppCompatActivity {
    private static final String TAG = "TempOtherDocumentActivi";
    ActivityTempOtherDocumentBinding binding;
    int signFlag=0;
    private static final String IMAGE_DIRECTORY = "/signdemo";
    AlertDialog alerDialog1,attachAlert;
    Bitmap bitmap;
    SignatureView canvasLL;
    File f;
    File compressedImageFile, filePassportSizePhoto, fileFamilyPhoto, fileResume, fileExperience, fileAppointmentLetter;
    String passportSizePhotoURL="", familyPhotoURL="", resumeURL="", experienceURL="", appointmentLetterURL="";
    Pref pref;
    Uri image_uri;
    int flag,pFlag = 0, fFlag=0, rFlag=0, eFlag = 0, aFlag=0;
    String pdfFilePath, pdfFileName;
    private static final int DEFAULT_BUFFER_SIZE = 2048;
    ImageView imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_temp_other_document);
        initView();
    }

    private void initView(){
        pref=new Pref(TempOtherDocumentActivity.this);
        if (!AppData.ADHARIMAGE.equals("")){
            flag=1;
            byte[] decodedString = Base64.decode(AppData.ADHARIMAGE, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            binding.imgPassportDocument.setImageBitmap(decodedByte);
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("AEMConsultantID", pref.getEmpConId());
            jsonObject.put("AEMClientID", pref.getEmpClintId());
            jsonObject.put("AEMClientOfficeID", pref.getEmpClintOffId());
            jsonObject.put("AEMEmployeeID", pref.getEmpId());
            jsonObject.put("WorkingStatus", "1");
            jsonObject.put("Operation", "16");
            getOtherDocument(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }




        binding.btnSaveForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signatureAlert();
            }
        });

        binding.imgPassportCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attechmentAlert(100,1001,1);
            }
        });
        binding.btnPassportSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (flag==1){
                if (pFlag==1){
                    docupload("0016","Passport Size Photo",binding.btnPassportSave,filePassportSizePhoto);
                }else {
                    Toast.makeText(TempOtherDocumentActivity.this,"Please Upload Passport Size Photo",Toast.LENGTH_LONG).show();
                }
            }
        });


        binding.imgFamilyCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attechmentAlert(200,2001,1);
            }
        });


        binding.btnFamilySave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (flag==1){
                if (fFlag==1){
                    docupload("0015","Family Photo",binding.btnFamilySave,fileFamilyPhoto);
                }else {
                    Toast.makeText(TempOtherDocumentActivity.this,"Please Upload Family Photo",Toast.LENGTH_LONG).show();
                }
            }
        });


        binding.imgResumeCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attechmentAlert(300,3001,2);
            }
        });


        binding.btnResumeSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (flag==1){
                if (rFlag==1){
                    docupload("0021","Resume",binding.btnResumeSave,fileResume);
                }else {
                    Toast.makeText(TempOtherDocumentActivity.this,"Please Upload Resume",Toast.LENGTH_LONG).show();
                }
            }
        });


        binding.imgExperinceLetterCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attechmentAlert(400,4001,2);
            }
        });


        binding.btnExperinceLetterSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (flag==1){
                if (eFlag==1){
                    docupload("0070","Experience Letter",binding.btnExperinceLetterSave,fileExperience);
                }else {
                    Toast.makeText(TempOtherDocumentActivity.this,"Please Upload Experince Letter",Toast.LENGTH_LONG).show();
                }
            }
        });


        binding.imgAppointmentletterCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attechmentAlert(500,5001,2);
            }
        });


        binding.btnAppointLetterSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (flag==1){
                if (aFlag==1){
                    docupload("0068","Appointment Letter",binding.btnAppointLetterSave,fileAppointmentLetter);
                }else {
                    Toast.makeText(TempOtherDocumentActivity.this,"Please Upload Appointment Letter",Toast.LENGTH_LONG).show();
                }
            }
        });

        binding.imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TempOtherDocumentActivity.this, TempDashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.imgPassportDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!passportSizePhotoURL.isEmpty()){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(passportSizePhotoURL));
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        binding.imgFamilyDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!familyPhotoURL.isEmpty()){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(familyPhotoURL));
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        binding.imgResumeDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!resumeURL.isEmpty()){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(resumeURL));
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        binding.imgExperinceLetterDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!experienceURL.isEmpty()){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(experienceURL));
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        binding.imgAppointmentletterDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!appointmentLetterURL.isEmpty()){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(appointmentLetterURL));
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
    }


    private void signatureAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TempOtherDocumentActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.signtaure_lay, null);
        dialogBuilder.setView(dialogView);

        canvasLL = (SignatureView) dialogView.findViewById(R.id.canvasLL);
        Button btnclear = (Button) dialogView.findViewById(R.id.btnclear);
        Button btnsave = (Button) dialogView.findViewById(R.id.btnsave);
        btnclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvasLL.clearCanvas();
                alerDialog1.dismiss();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*bitmap = canvasLL.getSignatureBitmap();
                String path = saveImage(bitmap);*/
                f = saveBitMap(TempOtherDocumentActivity.this, canvasLL);    //which view you want to pass that view as parameter


                if (f != null) {
                    Log.i("TAG", "Drawing saved to the gallery!");

                    signUpload();

                    alerDialog1.dismiss();
                    //postAcceptance(file);
                } else {
                    Log.i("TAG", "Oops! Image could not be saved.");
                    bitmap = canvasLL.getSignatureBitmap();
                    String path = saveImage(bitmap);
                    f=new File(path);
                    signUpload();
                    alerDialog1.dismiss();

                    // Toast.makeText(FeedBackRatingActivity.this, "Oops! Image could not be saved.", Toast.LENGTH_LONG).show();
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


    public String saveImage(Bitmap myBitmap) {
        signFlag = 1;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY /*iDyme folder*/);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
            Log.d("hhhhh", wallpaperDirectory.toString());
        }

        try {
            f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");


            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileOutputStream fo = null;
            try {
                fo = new FileOutputStream(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getApplicationContext(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";

    }


    private File saveBitMap(Context context, View drawView) {
        File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Handcare");
        if (!pictureFileDir.exists()) {
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if (!isDirectoryCreated)
                Log.i("ATG", "Can't create directory to save the image");
            return null;
        }
        String filename = pictureFileDir.getPath() + File.separator + System.currentTimeMillis() + ".jpg";
        File pictureFile = new File(filename);
        Bitmap bitmap = getBitmapFromView(drawView);
        try {
            pictureFile.createNewFile();
            FileOutputStream oStream = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream);
            oStream.flush();
            oStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue saving the image.");
        }
        scanGallery(context, pictureFile.getAbsolutePath());
        return pictureFile;
    }

    //create bitmap from view and returns it
    private Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    // used for scanning gallery
    private void scanGallery(Context cntx, String path) {
        try {
            MediaScannerConnection.scanFile(cntx, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void docupload(String docid,String doc,Button btn,File docFile) {
        ProgressDialog progressDialog=new ProgressDialog(TempOtherDocumentActivity.this);
        progressDialog.setMessage("Uploading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        //progressDialog.show();

        AndroidNetworking.upload(AppData.SAVE_EMP_DIGITAL_DOCUMENT)
                .addMultipartParameter("AEMEmployeeID",pref.getEmpId())
                .addMultipartParameter("DocumentID", docid)
                .addMultipartParameter("ReferenceNo", "0")
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())
                //.addMultipartFile("SingleFile", compressedImageFile)
                .addMultipartFile("SingleFile", docFile)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
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
                        try {
                            Log.e(TAG, "OTHER_DOCUMENT: "+response.toString(4));
                            progressDialog.dismiss();
                            JSONObject job1 = response;
                            int Response_Code = job1.optInt("Response_Code");
                            String Response_Data = job1.optString("Response_Data");
                            if (Response_Code == 101) {
                                if (doc.equalsIgnoreCase("Passport Size Photo")){
                                    pFlag=0;
                                } else if (doc.equalsIgnoreCase("Family Photo")){
                                    fFlag = 0;
                                } else if (doc.equalsIgnoreCase("Resume")){
                                    rFlag = 0;
                                } else if (doc.equalsIgnoreCase("Experience Letter")){
                                    eFlag = 0;
                                } else if (doc.equalsIgnoreCase("Appointment Letter")){
                                    aFlag = 0;
                                }
                                flag=0;
                                btn.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), doc+" details has been updated successfully", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), Response_Data, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        progressDialog.dismiss();
                        Log.e("errt", String.valueOf(error));
                        Toast.makeText(getApplicationContext(), "Something went wrong,Please try again", Toast.LENGTH_LONG).show();
                    }
                });


        //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        /*AndroidNetworking.upload(AppData.url+"post_empdigitaldocument")
                .addMultipartParameter("AEMEmployeeID",pref.getEmpId())
                .addMultipartParameter("DocumentID", docid)
                .addMultipartParameter("ReferenceNo", "0")
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())
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
                        String responseText = job1.optString("responseText");
                        boolean responseStatus = job1.optBoolean("responseStatus");

                        if (responseStatus) {
                            flag=0;
                            btn.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), doc+" details has been updated successfully", Toast.LENGTH_LONG).show();

                        } else {

                            Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();

                        }


                        // boolean _status = job1.getBoolean("status");



                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e("errt", String.valueOf(error));

                        Toast.makeText(getApplicationContext(), "Something went wrong,Please try again", Toast.LENGTH_LONG).show();
                    }
                });*/

    }



    private void attechmentAlert(int gallerycode,int cameracode,int intentflag) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TempOtherDocumentActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_attachment, null);
        dialogBuilder.setView(dialogView);
        LinearLayout lnCamera=(LinearLayout)dialogView.findViewById(R.id.lnCamera);
        LinearLayout lnGallery=(LinearLayout)dialogView.findViewById(R.id.lnGallery);
        LinearLayout lnCancel=(LinearLayout)dialogView.findViewById(R.id.lnCancel);
        lnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndroidXCameraActivity.launch(TempOtherDocumentActivity.this, cameracode);
                attachAlert.dismiss();
            }
        });

        /*if (gallerycode==100){
            lnGallery.setVisibility(View.GONE);
        }*/


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

        lnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attachAlert.cancel();
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2000 && resultCode == 1001){
            Log.e("TAG", "onActivityResult: "+data.getExtras().get("picture"));
            Log.e("TAG", "onActivityResult: "+data.getExtras().get(AndroidXCameraActivity.IMAGE_PATH_KEY));
            image_uri =  Uri.parse(String.valueOf(data.getExtras().get("picture")));
            //image_uri = (Uri) data.getExtras().get(AndroidXCameraActivity.IMAGE_PATH_KEY);
            compressedImageFile = new File(String.valueOf(data.getExtras().get("picture")));
            filePassportSizePhoto = new File(String.valueOf(data.getExtras().get("picture")));
            if (image_uri != null){
                binding.imgPassportDocument.setImageURI(image_uri);
                flag=1;
                pFlag=1;
            }
        }else if ((requestCode == 100 )) {
            InputStream imageStream = null;
            try {
                try {
                    image_uri = data.getData();
                    String filePath = getRealPathFromURIPath(image_uri, TempOtherDocumentActivity.this);
                    compressedImageFile = new File(filePath);
                    filePassportSizePhoto = new File(filePath);
                    //  Log.d(TAG, "filePath=" + filePath);
                    imageStream = getContentResolver().openInputStream(image_uri);
                    Bitmap bm = cropToSquare(BitmapFactory.decodeStream(imageStream));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.PNG, 10, baos); //bm is the bitmap object
                    byte[] b = baos.toByteArray();
                    binding.imgPassportDocument.setImageBitmap(bm);

                    flag = 1;
                    pFlag=1;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }

        }else  if (requestCode == 2000 && resultCode == 2001){
            Log.e("TAG", "onActivityResult: "+data.getExtras().get("picture"));
            Log.e("TAG", "onActivityResult: "+data.getExtras().get(AndroidXCameraActivity.IMAGE_PATH_KEY));
            image_uri =  Uri.parse(String.valueOf(data.getExtras().get("picture")));
            //image_uri = (Uri) data.getExtras().get(AndroidXCameraActivity.IMAGE_PATH_KEY);
            compressedImageFile = new File(String.valueOf(data.getExtras().get("picture")));
            fileFamilyPhoto = new File(String.valueOf(data.getExtras().get("picture")));
            if (image_uri != null){
                binding.imgFamilyDocument.setImageURI(image_uri);
                flag=1;
                fFlag=1;
            }
        }else if ((requestCode == 200 )) {
            InputStream imageStream = null;
            try {
                try {
                    image_uri = data.getData();
                    String filePath = getRealPathFromURIPath(image_uri, TempOtherDocumentActivity.this);
                    compressedImageFile = new File(filePath);
                    fileFamilyPhoto = new File(filePath);
                    //  Log.d(TAG, "filePath=" + filePath);
                    imageStream = getContentResolver().openInputStream(image_uri);
                    Bitmap bm = cropToSquare(BitmapFactory.decodeStream(imageStream));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.PNG, 10, baos); //bm is the bitmap object
                    byte[] b = baos.toByteArray();
                    binding.imgFamilyDocument.setImageBitmap(bm);

                    flag = 1;
                    fFlag=1;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }

        }else  if (requestCode == 2000 && resultCode == 3001){
            Log.e("TAG", "onActivityResult: "+data.getExtras().get("picture"));
            Log.e("TAG", "onActivityResult: "+data.getExtras().get(AndroidXCameraActivity.IMAGE_PATH_KEY));
            image_uri =  Uri.parse(String.valueOf(data.getExtras().get("picture")));
            //image_uri = (Uri) data.getExtras().get(AndroidXCameraActivity.IMAGE_PATH_KEY);
            compressedImageFile = new File(String.valueOf(data.getExtras().get("picture")));
            fileResume = new File(String.valueOf(data.getExtras().get("picture")));
            if (image_uri != null){
                binding.imgResumeDocument.setImageURI(image_uri);
                flag=1;
                rFlag = 1;
            }
        }else  if ((requestCode == 300 )){
            if (data != null){
                Uri uri = data.getData();
                Log.e("TAG", "onActivityResult: "+uri.getPath());
                String imagePath = uri.getPath();
                if (imagePath.contains("all_external")){
                    pdfFileName = FindDocumentInformation.FileNameFromURL(imagePath);
                    compressedImageFile = convertInputStreamToFile(uri,pdfFileName);
                    fileResume = convertInputStreamToFile(uri,pdfFileName);
                } else {
                    try {
                        pdfFilePath = getRealPath(TempOtherDocumentActivity.this,uri);
                        pdfFileName = FindDocumentInformation.FileNameFromURL(pdfFilePath);
                    } catch (IllegalArgumentException e){
                        //Todo: from WPS office document select
                        pdfFileName = FindDocumentInformation.FileNameFromURL(imagePath);
                        compressedImageFile = convertInputStreamToFile(uri,pdfFileName);
                        fileResume = convertInputStreamToFile(uri,pdfFileName);
                    }
                    compressedImageFile = convertInputStreamToFile(uri,pdfFileName);
                    fileResume = convertInputStreamToFile(uri,pdfFileName);
                }
                binding.imgResumeDocument.setImageDrawable(getResources().getDrawable(R.drawable.ic_pdf));
                flag = 1;
                rFlag = 1;
            }
            //flag++;
        }else  if (requestCode == 2000 && resultCode == 4001){
            Log.e("TAG", "onActivityResult: "+data.getExtras().get("picture"));
            Log.e("TAG", "onActivityResult: "+data.getExtras().get(AndroidXCameraActivity.IMAGE_PATH_KEY));
            image_uri =  Uri.parse(String.valueOf(data.getExtras().get("picture")));
            //image_uri = (Uri) data.getExtras().get(AndroidXCameraActivity.IMAGE_PATH_KEY);
            compressedImageFile = new File(String.valueOf(data.getExtras().get("picture")));
            fileExperience = new File(String.valueOf(data.getExtras().get("picture")));
            if (image_uri != null){
                binding.imgExperinceLetterDocument.setImageURI(image_uri);
                flag=1;
                eFlag =1;
            }
        }else  if ((requestCode == 400 )){
            Uri uri = data.getData();
            Log.e("TAG", "onActivityResult: "+uri.getPath());
            String imagePath = uri.getPath();
            if (imagePath.contains("all_external")){
                pdfFileName = FindDocumentInformation.FileNameFromURL(imagePath);
                compressedImageFile = convertInputStreamToFile(uri,pdfFileName);
                fileExperience = convertInputStreamToFile(uri,pdfFileName);
            } else {
                try {
                    pdfFilePath = getRealPath(TempOtherDocumentActivity.this,uri);
                    pdfFileName = FindDocumentInformation.FileNameFromURL(pdfFilePath);
                } catch (IllegalArgumentException e){
                    //Todo: from WPS office document select
                    pdfFileName = FindDocumentInformation.FileNameFromURL(imagePath);
                    compressedImageFile = convertInputStreamToFile(uri,pdfFileName);
                    fileExperience = convertInputStreamToFile(uri,pdfFileName);
                }
                compressedImageFile = convertInputStreamToFile(uri,pdfFileName);
                fileExperience = convertInputStreamToFile(uri,pdfFileName);
            }
            binding.imgExperinceLetterDocument.setImageDrawable(getResources().getDrawable(R.drawable.ic_pdf));
            flag = 1;
            eFlag =1;
            //flag++;
        }else  if (requestCode == 2000 && resultCode == 5001){
            Log.e("TAG", "onActivityResult: "+data.getExtras().get("picture"));
            Log.e("TAG", "onActivityResult: "+data.getExtras().get(AndroidXCameraActivity.IMAGE_PATH_KEY));
            image_uri =  Uri.parse(String.valueOf(data.getExtras().get("picture")));
            //image_uri = (Uri) data.getExtras().get(AndroidXCameraActivity.IMAGE_PATH_KEY);
            compressedImageFile = new File(String.valueOf(data.getExtras().get("picture")));
            fileAppointmentLetter = new File(String.valueOf(data.getExtras().get("picture")));
            if (image_uri != null){
                binding.imgAppointmentletterDocument.setImageURI(image_uri);
                flag=1;
                aFlag = 1;
            }
        }else  if ((requestCode == 500 )){
            Uri uri = data.getData();
            Log.e("TAG", "onActivityResult: "+uri.getPath());
            String imagePath = uri.getPath();
            if (imagePath.contains("all_external")){
                pdfFileName = FindDocumentInformation.FileNameFromURL(imagePath);
                compressedImageFile = convertInputStreamToFile(uri,pdfFileName);
                fileAppointmentLetter = convertInputStreamToFile(uri,pdfFileName);
            } else {
                try {
                    pdfFilePath = getRealPath(TempOtherDocumentActivity.this,uri);
                    pdfFileName = FindDocumentInformation.FileNameFromURL(pdfFilePath);
                } catch (IllegalArgumentException e){
                    //Todo: from WPS office document select
                    pdfFileName = FindDocumentInformation.FileNameFromURL(imagePath);
                    compressedImageFile = convertInputStreamToFile(uri,pdfFileName);
                    fileAppointmentLetter = convertInputStreamToFile(uri,pdfFileName);
                }
                compressedImageFile = convertInputStreamToFile(uri,pdfFileName);
                fileAppointmentLetter = convertInputStreamToFile(uri,pdfFileName);
            }
            binding.imgAppointmentletterDocument.setImageDrawable(getResources().getDrawable(R.drawable.ic_pdf));
            flag = 1;
            aFlag = 1;
            //flag++;
        }
    }

    private File convertInputStreamToFile(Uri uri, String fileNme) {
        InputStream inputStream;
        try {
            inputStream = TempOtherDocumentActivity.this.getContentResolver().openInputStream(uri);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        File file = new File(TempOtherDocumentActivity.this.getExternalFilesDir("/").getAbsolutePath(), fileNme);

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


    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    public static Bitmap cropToSquare(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = (height > width) ? width : height;
        int newHeight = (height > width) ? height - (height - width) : height;
        int cropW = (width - height) / 2;
        cropW = (cropW < 0) ? 0 : cropW;
        int cropH = (height - width) / 2;
        cropH = (cropH < 0) ? 0 : cropH;
        Bitmap cropImg = Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);

        return cropImg;
    }

    private void pdfChoose(int code){
        Intent uploadIntent = new Intent(Intent.ACTION_GET_CONTENT);
        uploadIntent.setType("application/pdf");
        startActivityForResult(uploadIntent, code);
    }


    private void signUpload() {
        ProgressDialog progressDialog=new ProgressDialog(TempOtherDocumentActivity.this);
        progressDialog.setMessage("Uploading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.show();

        //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        AndroidNetworking.upload(AppData.newv2url+"FileUpload/PostDigitalSignFile")
                .addMultipartParameter("p_AEMEmployeeID",pref.getEmpId())
                .addMultipartFile("file", f)
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
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
                        if (Response_Code == 101 ) {
                            successAlert("Your Form has been submitted successfully");



                        }else {
                            Toast.makeText(TempOtherDocumentActivity.this,"Error Occured Please contact with Administration",Toast.LENGTH_LONG).show();

                        }


                        // boolean _status = job1.getBoolean("status");



                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e("errt", String.valueOf(error));
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Something went wrong,Please try again", Toast.LENGTH_LONG).show();
                    }
                });

    }


    private void successAlert(String message) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TempOtherDocumentActivity.this, R.style.CustomDialogNew);
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
                Intent intent=new Intent(TempOtherDocumentActivity.this,TempDashBoardActivity.class);
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

    void getOtherDocument(JSONObject jsonObject){
        ProgressDialog pd=new ProgressDialog(TempOtherDocumentActivity.this);
        pd.setMessage("Loading...");
        pd.show();
        pd.setCancelable(false);
        AndroidNetworking.post(AppData.KYC_GET_DETAILS)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "GET_OTHER_DOCUMENT: "+response.toString(4));
                            pd.dismiss();
                            JSONObject job1 = response;
                            int Response_Code = job1.optInt("Response_Code");
                            if (Response_Code == 101){
                                String Response_Data = job1.optString("Response_Data");
                                JSONObject job2 = new JSONObject(Response_Data);
                                JSONArray jsonArray = job2.getJSONArray("OtherDocDetails");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject otherDocObj = jsonArray.getJSONObject(i);
                                    if (otherDocObj.optString("DocumentType").equalsIgnoreCase("Self Photo(PP)")){
                                        String DocUrl = otherDocObj.optString("DocUrl");
                                        passportSizePhotoURL = otherDocObj.optString("DocUrl");
                                        String DocumentID = otherDocObj.optString("DocumentID");
                                        String FileName = otherDocObj.optString("FileName");
                                        getPassportPhoto(DocUrl,FileName);
                                    } else if(otherDocObj.optString("DocumentType").equalsIgnoreCase("Family Photo")){
                                        String DocUrl = otherDocObj.optString("DocUrl");
                                        familyPhotoURL = otherDocObj.optString("DocUrl");
                                        String DocumentID = otherDocObj.optString("DocumentID");
                                        String FileName = otherDocObj.optString("FileName");
                                        getFamilyPhoto(DocUrl,FileName);
                                    } else if(otherDocObj.optString("DocumentType").equalsIgnoreCase("Resume")){
                                        String DocUrl = otherDocObj.optString("DocUrl");
                                        resumeURL = otherDocObj.optString("DocUrl");
                                        String DocumentID = otherDocObj.optString("DocumentID");
                                        String FileName = otherDocObj.optString("FileName");
                                        getResume(DocUrl,FileName);
                                    } else if(otherDocObj.optString("DocumentType").equalsIgnoreCase("Experience Letter")){
                                        String DocUrl = otherDocObj.optString("DocUrl");
                                        experienceURL = otherDocObj.optString("DocUrl");
                                        String DocumentID = otherDocObj.optString("DocumentID");
                                        String FileName = otherDocObj.optString("FileName");
                                        getExperienceLetter(DocUrl,FileName);
                                    } else if(otherDocObj.optString("DocumentType").equalsIgnoreCase("Appointment Letter")){
                                        String DocUrl = otherDocObj.optString("DocUrl");
                                        appointmentLetterURL = otherDocObj.optString("DocUrl");
                                        String DocumentID = otherDocObj.optString("DocumentID");
                                        String FileName = otherDocObj.optString("FileName");
                                        getAppointmentLetter(DocUrl,FileName);
                                    }
                                }
                            } else {

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "GET_OTHER_DOCUMENT_error: "+anError.getErrorBody());
                        pd.dismiss();
                    }
                });
    }

    private void getAppointmentLetter(String docUrl, String fileName) {
        String exe = docUrl.substring(docUrl.lastIndexOf("."));
        Picasso.with(TempOtherDocumentActivity.this)
                .load(R.drawable.loading)        // Load the image from the URL
                .placeholder(R.drawable.loading)
                .skipMemoryCache()
                .error(R.drawable.warning)
                .into(binding.imgAppointmentletterDocument);
        new Thread(new Runnable() {
            @Override
            public void run() {
                fileAppointmentLetter = downloadResume_PDF(docUrl,fileName);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (exe.equalsIgnoreCase(".pdf")){
                            Picasso.with(TempOtherDocumentActivity.this)
                                    .load(R.drawable.pdff)        // Load the image from the URL
                                    .placeholder(R.drawable.loading)
                                    .skipMemoryCache()
                                    .error(R.drawable.warning)
                                    .into(binding.imgAppointmentletterDocument);
                        } else {
                            Picasso.with(TempOtherDocumentActivity.this)
                                    .load(docUrl)
                                    .placeholder(R.drawable.loading)
                                    .skipMemoryCache()// optional
                                    .error(R.drawable.warning)
                                    .into(binding.imgAppointmentletterDocument);
                        }
                    }
                });
            }
        }).start();

    }

    private void getExperienceLetter(String docUrl, String fileName) {
        String exe = docUrl.substring(docUrl.lastIndexOf("."));
        Picasso.with(TempOtherDocumentActivity.this)
                .load(R.drawable.loading)        // Load the image from the URL
                .placeholder(R.drawable.loading)
                .skipMemoryCache()
                .error(R.drawable.warning)
                .into(binding.imgExperinceLetterDocument);
        new Thread(new Runnable() {
            @Override
            public void run() {

                fileExperience = downloadResume_PDF(docUrl, fileName);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (exe.equalsIgnoreCase(".pdf")){
                            Picasso.with(TempOtherDocumentActivity.this)
                                    .load(R.drawable.pdff)        // Load the image from the URL
                                    .placeholder(R.drawable.loading)
                                    .skipMemoryCache()
                                    .error(R.drawable.warning)
                                    .into(binding.imgExperinceLetterDocument);
                        } else {
                            Picasso.with(TempOtherDocumentActivity.this)
                                    .load(docUrl)
                                    .placeholder(R.drawable.loading)
                                    .skipMemoryCache()// optional
                                    .error(R.drawable.warning)
                                    .into(binding.imgExperinceLetterDocument);
                        }

                    }
                });
            }
        }).start();
    }

    private void getResume(String docUrl, String fileName) {
        String exe = docUrl.substring(docUrl.lastIndexOf("."));
        Picasso.with(TempOtherDocumentActivity.this)
                .load(R.drawable.loading)
                .placeholder(R.drawable.loading)
                .skipMemoryCache()// optional
                .error(R.drawable.warning)
                .into(binding.imgResumeDocument);
        new Thread(new Runnable() {
            @Override
            public void run() {
                fileResume = downloadResume_PDF(docUrl, fileName);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (exe.equalsIgnoreCase(".pdf")){
                            Picasso.with(TempOtherDocumentActivity.this)
                                    .load(R.drawable.pdff)        // Load the image from the URL
                                    .placeholder(R.drawable.loading)
                                    .skipMemoryCache()
                                    .error(R.drawable.warning)
                                    .into(binding.imgResumeDocument);
                        } else {
                            Picasso.with(TempOtherDocumentActivity.this)
                                    .load(docUrl)
                                    .placeholder(R.drawable.loading)
                                    .skipMemoryCache()// optional
                                    .error(R.drawable.warning)
                                    .into(binding.imgResumeDocument);
                        }
                    }
                });
            }
        }).start();
    }

    private void getFamilyPhoto(String docUrl, String fileName) {
        String exe = docUrl.substring(docUrl.lastIndexOf("."));
        Picasso.with(TempOtherDocumentActivity.this)
                .load(docUrl)
                .placeholder(R.drawable.loading)
                .skipMemoryCache()
                .error(R.drawable.warning)
                .into(binding.imgFamilyDocument);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "Family_Photo: "+downloadFamilyPhoto(docUrl, fileName));
                fileFamilyPhoto = downloadFamilyPhoto(docUrl, fileName);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Picasso.with(TempOtherDocumentActivity.this)
                                .load(docUrl)
                                .placeholder(R.drawable.loading)
                                .skipMemoryCache()
                                .error(R.drawable.warning)
                                .into(binding.imgFamilyDocument);
                    }
                });
            }
        }).start();
    }

    private void getPassportPhoto(String docUrl, String fileName) {
        String exe = docUrl.substring(docUrl.lastIndexOf("."));
        Picasso.with(TempOtherDocumentActivity.this)
                .load(docUrl)
                .placeholder(R.drawable.loading)
                .skipMemoryCache()
                .error(R.drawable.warning)
                .into(binding.imgPassportDocument);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "Passport_Photo: "+downloadFamilyPhoto(docUrl, fileName));
                filePassportSizePhoto = downloadFamilyPhoto(docUrl, fileName);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Picasso.with(TempOtherDocumentActivity.this)
                                .load(docUrl)
                                .placeholder(R.drawable.loading)
                                .skipMemoryCache()
                                .error(R.drawable.warning)
                                .into(binding.imgPassportDocument);
                    }
                });
            }
        }).start();
    }


    private File downloadPassportPhoto(String url, String fileName) {
        // Create OkHttp client to handle the download

        OkHttpClient client = new OkHttpClient();

        // Create the request to download the PDF
        okhttp3.Request request = new okhttp3.Request.Builder().url(url).build();

        // Keep the Call object reference to be able to cancel it later
        Call downloadCall = client.newCall(request);

        try {
            // Execute the request
            okhttp3.Response response = downloadCall.execute();

            if (response.isSuccessful()) {
                // Define the file where the PDF will be saved
                File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
                //File pdfFile = new File(Environment.getExternalStorageDirectory(), fileName);

                // Create the output stream to save the file
                try (InputStream inputStream = response.body().byteStream();
                     OutputStream outputStream = new FileOutputStream(pdfFile)) {

                    byte[] buffer = new byte[4096];
                    int bytesRead;

                    // Read from input stream and write to output stream
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }

                    // Return the saved file
                    return pdfFile;
                } catch (IOException e) {
                    Log.e(TAG, "Error saving PDF", e);
                    return null;
                }
            } else {
                Log.e(TAG, "Failed to download PDF. Response code: " + response.code());
                return null;
            }
        } catch (IOException e) {
            Log.e(TAG, "Error downloading PDF", e);
            return null;
        }
    }


    private File downloadFamilyPhoto(String url, String fileName) {
        // Create OkHttp client to handle the download

        OkHttpClient client = new OkHttpClient();

        // Create the request to download the PDF
        okhttp3.Request request = new okhttp3.Request.Builder().url(url).build();

        // Keep the Call object reference to be able to cancel it later
        Call downloadCall = client.newCall(request);

        try {
            // Execute the request
            okhttp3.Response response = downloadCall.execute();

            if (response.isSuccessful()) {
                // Define the file where the PDF will be saved
                File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
                //File pdfFile = new File(Environment.getExternalStorageDirectory(), fileName);

                // Create the output stream to save the file
                try (InputStream inputStream = response.body().byteStream();
                     OutputStream outputStream = new FileOutputStream(pdfFile)) {

                    byte[] buffer = new byte[4096];
                    int bytesRead;

                    // Read from input stream and write to output stream
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }

                    // Return the saved file
                    return pdfFile;
                } catch (IOException e) {
                    Log.e(TAG, "Error saving PDF", e);
                    return null;
                }
            } else {
                Log.e(TAG, "Failed to download PDF. Response code: " + response.code());
                return null;
            }
        } catch (IOException e) {
            Log.e(TAG, "Error downloading PDF", e);
            return null;
        }
    }

    private File downloadAppointmentLetter_PDF(String url, String fileName) {
        // Create OkHttp client to handle the download

        OkHttpClient client = new OkHttpClient();

        // Create the request to download the PDF
        okhttp3.Request request = new okhttp3.Request.Builder().url(url).build();

        // Keep the Call object reference to be able to cancel it later
        Call downloadCall = client.newCall(request);

        try {
            // Execute the request
            okhttp3.Response response = downloadCall.execute();

            if (response.isSuccessful()) {
                // Define the file where the PDF will be saved
                File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
                //File pdfFile = new File(Environment.getExternalStorageDirectory(), fileName);

                // Create the output stream to save the file
                try (InputStream inputStream = response.body().byteStream();
                     OutputStream outputStream = new FileOutputStream(pdfFile)) {

                    byte[] buffer = new byte[4096];
                    int bytesRead;

                    // Read from input stream and write to output stream
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }

                    // Return the saved file
                    return pdfFile;
                } catch (IOException e) {
                    Log.e(TAG, "Error saving PDF", e);
                    return null;
                }
            } else {
                Log.e(TAG, "Failed to download PDF. Response code: " + response.code());
                return null;
            }
        } catch (IOException e) {
            Log.e(TAG, "Error downloading PDF", e);
            return null;
        }
    }

    private File downloadExperienceLetter_PDF(String url, String fileName) {
        // Create OkHttp client to handle the download

        OkHttpClient client = new OkHttpClient();

        // Create the request to download the PDF
        okhttp3.Request request = new okhttp3.Request.Builder().url(url).build();

        // Keep the Call object reference to be able to cancel it later
        Call downloadCall = client.newCall(request);

        try {
            // Execute the request
            okhttp3.Response response = downloadCall.execute();

            if (response.isSuccessful()) {
                // Define the file where the PDF will be saved
                File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
                //File pdfFile = new File(Environment.getExternalStorageDirectory(), fileName);

                // Create the output stream to save the file
                try (InputStream inputStream = response.body().byteStream();
                     OutputStream outputStream = new FileOutputStream(pdfFile)) {

                    byte[] buffer = new byte[4096];
                    int bytesRead;

                    // Read from input stream and write to output stream
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }

                    // Return the saved file
                    return pdfFile;
                } catch (IOException e) {
                    Log.e(TAG, "Error saving PDF", e);
                    return null;
                }
            } else {
                Log.e(TAG, "Failed to download PDF. Response code: " + response.code());
                return null;
            }
        } catch (IOException e) {
            Log.e(TAG, "Error downloading PDF", e);
            return null;
        }
    }

    private File downloadResume_PDF(String url, String fileName) {
        // Create OkHttp client to handle the download

        OkHttpClient client = new OkHttpClient();

        // Create the request to download the PDF
        okhttp3.Request request = new okhttp3.Request.Builder().url(url).build();

        // Keep the Call object reference to be able to cancel it later
        Call downloadCall = client.newCall(request);

        try {
            // Execute the request
            okhttp3.Response response = downloadCall.execute();

            if (response.isSuccessful()) {
                // Define the file where the PDF will be saved
                File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
                //File pdfFile = new File(Environment.getExternalStorageDirectory(), fileName);

                // Create the output stream to save the file
                try (InputStream inputStream = response.body().byteStream();
                     OutputStream outputStream = new FileOutputStream(pdfFile)) {

                    byte[] buffer = new byte[4096];
                    int bytesRead;

                    // Read from input stream and write to output stream
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }

                    // Return the saved file
                    return pdfFile;
                } catch (IOException e) {
                    Log.e(TAG, "Error saving PDF", e);
                    return null;
                }
            } else {
                Log.e(TAG, "Failed to download PDF. Response code: " + response.code());
                return null;
            }
        } catch (IOException e) {
            Log.e(TAG, "Error downloading PDF", e);
            return null;
        }
    }
}