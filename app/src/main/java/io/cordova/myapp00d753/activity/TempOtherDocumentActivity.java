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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.kyanogen.signatureview.SignatureView;

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
import io.cordova.myapp00d753.activity.metso.MetsoNewReimbursementClaimActivity;
import io.cordova.myapp00d753.activity.metso.MetsoPMSTargetAchivementActivity;
import io.cordova.myapp00d753.databinding.ActivityTempOtherDocumentBinding;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.FindDocumentInformation;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.RealPathUtil;

public class TempOtherDocumentActivity extends AppCompatActivity {
    ActivityTempOtherDocumentBinding binding;
    int signFlag=0;
    private static final String IMAGE_DIRECTORY = "/signdemo";
    AlertDialog alerDialog1,attachAlert;
    Bitmap bitmap;
    SignatureView canvasLL;
    File f;
    File compressedImageFile;
    Pref pref;
    Uri image_uri;
    int flag;
    String pdfFilePath, pdfFileName;
    private static final int DEFAULT_BUFFER_SIZE = 2048;
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
                if (flag==1){
                    docupload("0016","Passport Size Photo",binding.btnPassportSave);
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
                if (flag==1){
                    docupload("0015","Family Photo",binding.btnFamilySave);
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
                if (flag==1){
                    docupload("0021","Resume",binding.btnResumeSave);
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
                if (flag==1){
                    docupload("0070","Experince Letter",binding.btnExperinceLetterSave);
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
                if (flag==1){
                    docupload("0068","Appointment Letter",binding.btnAppointLetterSave);
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


    private void docupload(String docid,String doc,Button btn) {
        ProgressDialog progressDialog=new ProgressDialog(TempOtherDocumentActivity.this);
        progressDialog.setMessage("Uploading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.show();

        //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        AndroidNetworking.upload(AppData.url+"post_empdigitaldocument")
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
                });

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

            if (image_uri != null){
                binding.imgPassportDocument.setImageURI(image_uri);
                flag=1;

            }
        }else if ((requestCode == 100 )) {
            InputStream imageStream = null;
            try {
                try {
                    image_uri = data.getData();
                    String filePath = getRealPathFromURIPath(image_uri, TempOtherDocumentActivity.this);
                    compressedImageFile = new File(filePath);
                    //  Log.d(TAG, "filePath=" + filePath);
                    imageStream = getContentResolver().openInputStream(image_uri);
                    Bitmap bm = cropToSquare(BitmapFactory.decodeStream(imageStream));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.PNG, 10, baos); //bm is the bitmap object
                    byte[] b = baos.toByteArray();
                    binding.imgPassportDocument.setImageBitmap(bm);

                    flag = 1;


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

            if (image_uri != null){
                binding.imgFamilyDocument.setImageURI(image_uri);
                flag=1;

            }
        }else if ((requestCode == 200 )) {
            InputStream imageStream = null;
            try {
                try {
                    image_uri = data.getData();
                    String filePath = getRealPathFromURIPath(image_uri, TempOtherDocumentActivity.this);
                    compressedImageFile = new File(filePath);
                    //  Log.d(TAG, "filePath=" + filePath);
                    imageStream = getContentResolver().openInputStream(image_uri);
                    Bitmap bm = cropToSquare(BitmapFactory.decodeStream(imageStream));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.PNG, 10, baos); //bm is the bitmap object
                    byte[] b = baos.toByteArray();
                    binding.imgFamilyDocument.setImageBitmap(bm);

                    flag = 1;


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

            if (image_uri != null){
                binding.imgResumeDocument.setImageURI(image_uri);
                flag=1;

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
                    pdfFilePath = getRealPath(TempOtherDocumentActivity.this,uri);
                    pdfFileName = FindDocumentInformation.FileNameFromURL(pdfFilePath);
                } catch (IllegalArgumentException e){
                    //Todo: from WPS office document select
                    pdfFileName = FindDocumentInformation.FileNameFromURL(imagePath);
                    compressedImageFile = convertInputStreamToFile(uri,pdfFileName);

                }
                compressedImageFile = convertInputStreamToFile(uri,pdfFileName);

            }
            binding.imgResumeDocument.setImageDrawable(getResources().getDrawable(R.drawable.ic_pdf));
            flag = 1;
            //flag++;
        }else  if (requestCode == 2000 && resultCode == 4001){
            Log.e("TAG", "onActivityResult: "+data.getExtras().get("picture"));
            Log.e("TAG", "onActivityResult: "+data.getExtras().get(AndroidXCameraActivity.IMAGE_PATH_KEY));
            image_uri =  Uri.parse(String.valueOf(data.getExtras().get("picture")));
            //image_uri = (Uri) data.getExtras().get(AndroidXCameraActivity.IMAGE_PATH_KEY);
            compressedImageFile = new File(String.valueOf(data.getExtras().get("picture")));

            if (image_uri != null){
                binding.imgExperinceLetterDocument.setImageURI(image_uri);
                flag=1;

            }
        }else  if ((requestCode == 400 )){
            Uri uri = data.getData();
            Log.e("TAG", "onActivityResult: "+uri.getPath());
            String imagePath = uri.getPath();
            if (imagePath.contains("all_external")){
                pdfFileName = FindDocumentInformation.FileNameFromURL(imagePath);
                compressedImageFile = convertInputStreamToFile(uri,pdfFileName);

            } else {
                try {
                    pdfFilePath = getRealPath(TempOtherDocumentActivity.this,uri);
                    pdfFileName = FindDocumentInformation.FileNameFromURL(pdfFilePath);
                } catch (IllegalArgumentException e){
                    //Todo: from WPS office document select
                    pdfFileName = FindDocumentInformation.FileNameFromURL(imagePath);
                    compressedImageFile = convertInputStreamToFile(uri,pdfFileName);

                }
                compressedImageFile = convertInputStreamToFile(uri,pdfFileName);

            }
            binding.imgExperinceLetterDocument.setImageDrawable(getResources().getDrawable(R.drawable.ic_pdf));
            flag = 1;
            //flag++;
        }else  if (requestCode == 2000 && resultCode == 5001){
            Log.e("TAG", "onActivityResult: "+data.getExtras().get("picture"));
            Log.e("TAG", "onActivityResult: "+data.getExtras().get(AndroidXCameraActivity.IMAGE_PATH_KEY));
            image_uri =  Uri.parse(String.valueOf(data.getExtras().get("picture")));
            //image_uri = (Uri) data.getExtras().get(AndroidXCameraActivity.IMAGE_PATH_KEY);
            compressedImageFile = new File(String.valueOf(data.getExtras().get("picture")));

            if (image_uri != null){
                binding.imgAppointmentletterDocument.setImageURI(image_uri);
                flag=1;

            }
        }else  if ((requestCode == 500 )){
            Uri uri = data.getData();
            Log.e("TAG", "onActivityResult: "+uri.getPath());
            String imagePath = uri.getPath();
            if (imagePath.contains("all_external")){
                pdfFileName = FindDocumentInformation.FileNameFromURL(imagePath);
                compressedImageFile = convertInputStreamToFile(uri,pdfFileName);

            } else {
                try {
                    pdfFilePath = getRealPath(TempOtherDocumentActivity.this,uri);
                    pdfFileName = FindDocumentInformation.FileNameFromURL(pdfFilePath);
                } catch (IllegalArgumentException e){
                    //Todo: from WPS office document select
                    pdfFileName = FindDocumentInformation.FileNameFromURL(imagePath);
                    compressedImageFile = convertInputStreamToFile(uri,pdfFileName);

                }
                compressedImageFile = convertInputStreamToFile(uri,pdfFileName);

            }
            binding.imgAppointmentletterDocument.setImageDrawable(getResources().getDrawable(R.drawable.ic_pdf));
            flag = 1;
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
}