package io.cordova.myapp00d753.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.kyanogen.signatureview.SignatureView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.ConsentDocumentAdapter;
import io.cordova.myapp00d753.adapter.PFDocumentAdapter;
import io.cordova.myapp00d753.databinding.ActivityConsentBinding;
import io.cordova.myapp00d753.module.ConsentDocModule;
import io.cordova.myapp00d753.module.PFDocumentModule;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

public class ConsentActivity extends AppCompatActivity {
    ActivityConsentBinding binding;
    Pref pref;
    ArrayList<ConsentDocModule>itemList=new ArrayList<>();
    ArrayList<String>docIDList=new ArrayList<>();
    ArrayList<String>docIndexList=new ArrayList<>();
    AlertDialog consnetdialog;
    Bitmap bitmap;
    SignatureView canvasLL;
    File consetfile;
    String ConsentFlag="1";
    int signFlag;
    private static final String IMAGE_DIRECTORY = "/signdemo";
    String android_id;
    File f;
    int frsttxtflag=0;
    private long startTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_consent);
        pref=new Pref(ConsentActivity.this);
        binding.rvItem.setLayoutManager(new LinearLayoutManager(ConsentActivity.this));
        binding.llSigned.setEnabled(false);
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        if (android_id.equals("")) {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            android_id = telephonyManager.getDeviceId();
        }else {
            android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }
        startTime = SystemClock.elapsedRealtime();






        JSONObject object=new JSONObject();
        try {
            object.put("MasterID",pref.getMasterId());
            getConsentLetter(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        binding.llSigned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.imgTick.getVisibility()==View.GONE){
                    binding.imgTick.setVisibility(View.VISIBLE);
                    consnetLetter();
                }
            }
        });
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.llSigned.setEnabled(true);
            }
        }, 6000);



    }

    public void getConsentLetter(JSONObject jsonObject) {
        AndroidNetworking.post(AppData.GetEmpConsentPendingListAPI)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject job1 = response;
                        String Response_Code = job1.optString("Response_Code");
                        if (Response_Code.equals("101")) {
                            JSONArray Response_Data = job1.optJSONArray("Response_Data");
                            for (int i=0;i<Response_Data.length();i++){
                                JSONObject obj=Response_Data.optJSONObject(i);
                                String Contents=obj.optString("Contents");
                                String[] splitconten=Contents.split(",",2);
                                String frst=splitconten[0];
                                String secnd=splitconten[1];
                                //binding.tvConsnet.setText(frst+", "+pref.getEmpName()+", "+secnd);

                                String Document=obj.optString("Document");
                                String indx= String.valueOf(obj.optInt("indx"));
                                String DocumentID=obj.optString("DocumentID");
                                docIDList.add(DocumentID);
                                docIndexList.add(indx);
                                ConsentDocModule docModule=new ConsentDocModule();
                                docModule.setDocument(Document);
                                itemList.add(docModule);

                            }
                            ConsentDocumentAdapter docAdapter=new ConsentDocumentAdapter(itemList);
                            binding.rvItem.setAdapter(docAdapter);

                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        if (anError.getErrorCode()==401){
                            Toast.makeText(ConsentActivity.this, "Session expired, please login", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ConsentActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
    }

    private void consnetLetter() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ConsentActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.consent_signtaure, null);
        dialogBuilder.setView(dialogView);

        TextView tvConsent=(TextView)dialogView.findViewById(R.id.tvConsent);
        tvConsent.setText("I,"+pref.getEmpName()+", hereby submit my Aadhaar and/or my personal documents, information in this portal/site at my own will for employment vis-à-vis   verification purpose \n" +
                "I expressly disclaim any claims arising from representations, whether express or implied, or reliance upon any representations made regarding my documents and/or information supplied to you\n");

        canvasLL = (SignatureView) dialogView.findViewById(R.id.canvasLL);
        Button btnAcknlowedge=(Button)dialogView.findViewById(R.id.btnAcknlowedge);


        btnAcknlowedge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*bitmap = canvasLL.getSignatureBitmap();
                String path = saveImage(bitmap);*/
                consetfile = saveBitMap(ConsentActivity.this, canvasLL);    //which view you want to pass that view as parameter


                if (consetfile != null) {
                    Log.i("TAG", "Drawing saved to the gallery!");

                    signUpload(binding.tvConsnet.getText().toString());
                } else {
                    Log.i("TAG", "Oops! Image could not be saved.");
                    bitmap = canvasLL.getSignatureBitmap();
                    String path = consnetsaveImage(bitmap);
                    signUpload(binding.tvConsnet.getText().toString());
                    // Toast.makeText(FeedBackRatingActivity.this, "Oops! Image could not be saved.", Toast.LENGTH_LONG).show();
                }

                consnetdialog.dismiss();



            }
        });


        consnetdialog = dialogBuilder.create();
        consnetdialog.setCancelable(false);
        Window window = consnetdialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        consnetdialog.show();
    }


    public String consnetsaveImage(Bitmap myBitmap) {
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
            consetfile = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");


            try {
                consetfile.createNewFile();
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
            Log.d("TAG", "File Saved::--->" + consetfile.getAbsolutePath());

            return consetfile.getAbsolutePath();
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


    private void signUpload(String textdetils) {
        ProgressDialog progressDialog=new ProgressDialog(ConsentActivity.this);
        progressDialog.setMessage("Uploading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.show();
        String masterID=pref.getMasterId();
        String Doc_Indx=docIndexList.toString().replaceAll(",","#").replaceAll(" ","").replace("[","").replace("]","");
        String DocID=docIDList.toString().replaceAll(",","#").replaceAll(" ","").replace("]","").replace("[","");
        //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        AndroidNetworking.upload(AppData.newv2url+"Document/SaveEmpKYCDocConsent")
                .addMultipartParameter("MasterID",masterID)
                .addMultipartParameter("Doc_Indx",Doc_Indx)
                .addMultipartParameter("DocumentID",DocID)
                .addMultipartParameter("ConsentDetails",textdetils)
                .addMultipartParameter("DeviceID",android_id)
                .addMultipartParameter("IPAddress","00000")
                .addMultipartParameter("DeviceType","A")
                .addMultipartFile("SignImage", consetfile)
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
                        if (Response_Code == 101 || Response_Code==100) {
                            consnetdialog.dismiss();
                            Intent intent=new Intent(ConsentActivity.this,EmployeeDashBoardActivity.class);
                            startActivity(intent);
                            finish();

                            Toast.makeText(ConsentActivity.this,"Consent letter has been saved successfully",Toast.LENGTH_LONG).show();


                        }else {
                            Toast.makeText(ConsentActivity.this,"Error Occured Please contact with Administration",Toast.LENGTH_LONG).show();

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


    @Override
    protected void onPause() {
        super.onPause();

    }
}