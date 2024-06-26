package io.cordova.myapp00d753.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.provider.Settings;
import android.telephony.TelephonyManager;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;
import io.cordova.myapp00d753.utility.Pref;

public class TempDashBoardActivity extends AppCompatActivity {
    public static String TEMP_DASHBOARD="temp_dashboard";
    LinearLayout llProfile, llDocument, llLogout;
    Pref pref;
    TextView tvEmployeeName, tvGreeting, tvLoginDateTime;
    NetworkConnectionCheck connectionCheck;
    String s1,s2,s3,s4,s5,s6,s7,s8,s9;
    LinearLayout llCall,llHelp;
    android.app.AlertDialog alerDialog1;
    String ConsentFlag="1";
    int signFlag=0;
    private static final String IMAGE_DIRECTORY_CONSENT = "/signdemo";
    AlertDialog consnetdialog;
    Bitmap bitmap;
    SignatureView canvasLL;
    File consetfile;
    private static final String IMAGE_DIRECTORY = "/signdemo";
    String android_id="1234556";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_dash_board);
        initialize();
        profileFunction();
        onClick();
    }
    private  void initialize(){
        pref = new Pref(TempDashBoardActivity.this);
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
        if (getIntent().getStringExtra("ConsentFlag")!=null){
            ConsentFlag=getIntent().getStringExtra("ConsentFlag");
        }else {

        }

        connectionCheck=new NetworkConnectionCheck(TempDashBoardActivity.this);
        llProfile = (LinearLayout) findViewById(R.id.llProfile);
        llDocument = (LinearLayout) findViewById(R.id.llDocument);
        llLogout=(LinearLayout)findViewById(R.id.llLogout);

        tvEmployeeName = (TextView) findViewById(R.id.tvEmployeeName);
        tvEmployeeName.setText(pref.getEmpName());

        tvGreeting = (TextView) findViewById(R.id.tvGreeting);
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            tvGreeting.setText("Good Morning");
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            tvGreeting.setText("Good Afternoon");
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            tvGreeting.setText("Good Evening");
        } else if (timeOfDay >= 21 && timeOfDay < 24) {

            tvGreeting.setText("Good Evening");
        }
        tvLoginDateTime = (TextView) findViewById(R.id.tvLoginDateTime);
        tvLoginDateTime.setText(pref.getloginTime());
        String menu = pref.getMenu();

        Log.d("menuu", menu);
        String[] separated = menu.split(",");
        llCall=(LinearLayout)findViewById(R.id.llCall);
        llHelp=(LinearLayout)findViewById(R.id.llHelp);
        if (ConsentFlag.equals("0")){
            consnetLetter();
        }

    }
    private void onClick(){
        llProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TempDashBoardActivity.this,TempProfileActivity.class);
                startActivity(intent);
            }
        });

        llDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TempDashBoardActivity.this,DocumentActivity.class);
                intent.putExtra("from",TEMP_DASHBOARD);
                startActivity(intent);
            }
        });

        llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TempDashBoardActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        llCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:18008333555"));
                startActivity(intent);
            }
        });
        llHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),PdfViewActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are  you want to exit ,"
        );
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void profileFunction() {
        String surl = AppData.url+"gcl_DummyKYCCompletion?AEMEmployeeID=" + pref.getMasterId() + "&SecurityCode=" + pref.getSecurityCode();
        Log.d("kyccomplete", surl);
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        progressBar.dismiss();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                //   Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String Contact=obj.optString("Contact");


                                    if (!Contact.equals("")){
                                        pref.saveContactPer(Contact);
                                    }else {
                                        pref.saveContactPer("0");
                                    }
                                    String DocumentUpload=obj.optString("DocumentUpload");

                                    if (!DocumentUpload.equals("")){
                                        pref.saveDocPer(DocumentUpload);
                                    }else {
                                        pref.saveDocPer("0");
                                    }

                                    String FamilyMember=obj.optString("MemberUpload");

                                    if (!FamilyMember.equals("")){
                                        pref.saveFamilyPer(FamilyMember);
                                    }else {
                                        pref.saveFamilyPer("0");
                                    }
                                    String Miscellanneous=obj.optString("Miscellanneous");


                                    if (!Miscellanneous.equals("")){
                                        pref.saveMisPer(Miscellanneous);
                                    }else {
                                        pref.saveMisPer("0");
                                    }

                                    String Necessary=obj.optString("Necessary");
                                    if (!Necessary.equals("")){
                                        pref.saveNesPer(Necessary);
                                    }else {
                                        pref.saveNesPer("0");
                                    }



                                }
                                if (pref.getIntentFlag().equals("1")){
                                    loginFunction();
                                }else {

                                }



                            } else {
                                progressBar.dismiss();

                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TempDashBoardActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                pref.saveNesPer("0");
                pref.saveMisPer("0");
                pref.saveFamilyPer("0");
                pref.saveDocPer("0");
                pref.saveContactPer("0");
                // Toast.makeText(TempProfileActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");

    }


    public void loginFunction() {

        byte[] data = new byte[0];
        try {
            data = pref.getPassword().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String base64 = Base64.encodeToString(data, Base64.DEFAULT).replaceAll("\\s+", "");;


        String surl = AppData.url+"get_GCLAuthenticateWithEncryption?MasterID=" + pref.getMasterId() + "&Password=" + base64 + "&IMEI=0000&Version=1.0&SecurityCode=" + pref.getSecurityCode() + "&DeviceID=azzzzzz&DeviceType=A";
        final ProgressDialog pd=new ProgressDialog(TempDashBoardActivity.this);
        pd.setMessage("Loading.....");
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                pd.dismiss();

                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);




                                    String LoginDateTime = obj.optString("LoginDateTime");
                                    pref.saveloginTime(LoginDateTime);
                                    boolean AppRenameFlag=obj.optBoolean("AppRenameFlag");
                                    String AppRenameText=obj.optString("AppRenameText");

                                    pref.saveMsgAlertStatus(AppRenameFlag);
                                    pref.saveMsg(AppRenameText);





                                }

                                if (pref.getMsgAlertStatus()){
                                    msgAlert();
                                }else {

                                }




                            }else {
                                Intent intent=new Intent(TempDashBoardActivity.this,LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TempDashBoardActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Intent intent=new Intent(TempDashBoardActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                //  Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");

    }

    private void operBrowser() {
        String surl=pref.getLeaveUrl();
        Uri uri = Uri.parse(surl);
        if (!pref.getLeaveUrl().equals("")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }else {

        }
    }


    private void msgAlert() {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(TempDashBoardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_message_alert, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);
        tvInvalidDate.setText(pref.getMsg());



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


    private void consnetLetter() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TempDashBoardActivity.this, R.style.CustomDialogNew);
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
                consetfile = saveBitMap(TempDashBoardActivity.this, canvasLL);    //which view you want to pass that view as parameter


                if (consetfile != null) {
                    Log.i("TAG", "Drawing saved to the gallery!");

                    signUpload(tvConsent.getText().toString());
                    //postAcceptance(file);
                } else {
                    Log.i("TAG", "Oops! Image could not be saved.");
                    bitmap = canvasLL.getSignatureBitmap();
                    String path = consnetsaveImage(bitmap);
                    signUpload(tvConsent.getText().toString());

                    // Toast.makeText(FeedBackRatingActivity.this, "Oops! Image could not be saved.", Toast.LENGTH_LONG).show();
                }





            }
        });


        consnetdialog = dialogBuilder.create();
        consnetdialog.setCancelable(true);
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
                fo = new FileOutputStream(consetfile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getApplicationContext(),
                    new String[]{consetfile.getPath()},
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
        ProgressDialog progressDialog=new ProgressDialog(TempDashBoardActivity.this);
        progressDialog.setMessage("Uploading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.show();
        String masterID=pref.getMasterId();

        //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        AndroidNetworking.upload(AppData.newv2url+"FileUpload/PostConsentLetter")
                .addMultipartParameter("MasterID",masterID)
                .addMultipartParameter("ConsentDetails",textdetils)
                .addMultipartParameter("DeviceID",android_id)
                .addMultipartParameter("IP","00000")
                .addMultipartParameter("DeviceType","A")
                .addMultipartParameter("Operation","1")
                .addMultipartFile("file", consetfile)
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

                            Toast.makeText(TempDashBoardActivity.this,"Consent letter has been saved successfully",Toast.LENGTH_LONG).show();


                        }else {
                            Toast.makeText(TempDashBoardActivity.this,"Error Occured Please contact with Administration",Toast.LENGTH_LONG).show();

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



}
