package io.cordova.myapp00d753.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

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



import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.florent37.viewtooltip.ViewTooltip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.MenuItemAdapter;
import io.cordova.myapp00d753.module.MenuItemModel;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;
import io.cordova.myapp00d753.utility.Pref;

public class  EmployeeDashBoardActivity extends AppCompatActivity {
    Pref pref;
    TextView  tvLoginDateTime;
    NetworkConnectionCheck connectionCheck;

    TextView tvEmpName,tvGreeting;
    boolean mslideState;


    @ColorInt
    public static final int BLUE = 0xFF0FB8B3;

    @ColorInt
    public static final int GREEN = 0xFF5BBD76;

    LinearLayout llGeoFence,llDailyLog;
    String responseCode,DocLink,PFLink;
    AlertDialog alertDialog;
    String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/GENIUSSURVEY/";
    String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    android.app.AlertDialog alert3;
    File f;
    private static final String IMAGE_DIRECTORY = "/signdemo";

    RecyclerView rvItem;
    ArrayList<MenuItemModel>itemList=new ArrayList<>();
    ImageView imglogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_dash_board);
        initialize();
        loginFunction();
        onClick();
    }

    private void initialize() {
        pref = new Pref(EmployeeDashBoardActivity.this);
        connectionCheck = new NetworkConnectionCheck(EmployeeDashBoardActivity.this);
        imglogout=(ImageView)findViewById(R.id.imglogout);

        tvEmpName=(TextView)findViewById(R.id.tvEmpName);

        tvEmpName.setText("Welcome "+pref.getEmpName());



        tvGreeting = (TextView) findViewById(R.id.tvGreeting);
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            tvGreeting.setText("Hi! Good Morning");
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            tvGreeting.setText("Hi! Good Afternoon");
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            tvGreeting.setText("Hi! Good Evening");
        } else if (timeOfDay >= 21 && timeOfDay < 24) {

            tvGreeting.setText("Hi! Good Evening");
        }
        tvLoginDateTime = (TextView) findViewById(R.id.tvDate);
        tvLoginDateTime.setText(pref.getloginTime());

        rvItem=(RecyclerView)findViewById(R.id.rvItem);
        rvItem.setLayoutManager(new GridLayoutManager(this, 3));




    }

    private void onClick() {
     imglogout.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent intent=new Intent(EmployeeDashBoardActivity.this,LoginActivity.class);
             startActivity(intent);
             finish();
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

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public void pfopenBrowser() {

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
        Log.d("inputLogin", surl);

        final ProgressDialog pd=new ProgressDialog(EmployeeDashBoardActivity.this);
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
                                    String AEMClientID=obj.optString("AEMClientID");
                                    pref.saveEmpClintId(AEMClientID);

                                    pref.saveMsgAlertStatus(AppRenameFlag);
                                    pref.saveMsg(AppRenameText);

                                    String PFConsolidateURL=obj.optString("PFConsolidateURL");
                                    pref.savePFURL(PFConsolidateURL);




                                }

                                if (pref.getMsgAlertStatus()){
                                    msgAlert();
                                }else {
                                    acceptance();
                                }
                            }else {
                                Intent intent=new Intent(EmployeeDashBoardActivity.this,LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EmployeeDashBoardActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                //  Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                showAlert();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");

    }

    public void acceptance() {

        String surl = AppData.url+"gcl_EmployeeAppointLetterAcceptance?AEMConsultantId=" + pref.getEmpConId() + "&MasterId=" + pref.getMasterId() + "&SecurityCode=" + pref.getSecurityCode();
        Log.d("inputLogin", surl);

        final ProgressDialog pd=new ProgressDialog(EmployeeDashBoardActivity.this);
        pd.setMessage("Loading.....");
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        pd.dismiss();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            responseCode=job1.optString("responseCode");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();


                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    DocLink = obj.optString("DocLink");




                                }

                                if (responseCode.equals("1")){
                                    shoeDialog(DocLink);
                                }


                            }


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EmployeeDashBoardActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                //  Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                showAlert();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");

    }

    public void getPFURL() {

        String surl = AppData.url+"get_PFManagementTripleA?MasterID="+pref.getMasterId()+"&SecurityCode="+pref.getSecurityCode();
        Log.d("inputLogin", surl);

        final ProgressDialog pd=new ProgressDialog(EmployeeDashBoardActivity.this);
        pd.setMessage("Loading.....");
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        pd.dismiss();
                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            responseCode=job1.optString("responseCode");
                            getMenutem();
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();


                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    PFLink = obj.optString("url");





                                }




                            }


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EmployeeDashBoardActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                //  Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                showAlert();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");

    }

    public void getMenutem() {

        String surl = AppData.url+"gel_MobileAppMenuList?ConsultantID="+pref.getEmpConId()+"&ClientID="+pref.getEmpClintId()+"&SecurityCode="+pref.getSecurityCode();
        Log.d("inputLogin", surl);

        final ProgressDialog pd=new ProgressDialog(EmployeeDashBoardActivity.this);
        pd.setMessage("Loading.....");
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        pd.dismiss();
                        itemList.clear();
                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            responseCode=job1.optString("responseCode");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                                 itemList.add(new MenuItemModel("Voice Assistant","0"));
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String MenuID=obj.optString("MenuID");
                                    String MenuItem=obj.optString("MenuItem");
                                    MenuItemModel itemModel=new MenuItemModel(MenuItem,MenuID);
                                    itemList.add(itemModel);






                                }

                                MenuItemAdapter menuItemAdapter=new MenuItemAdapter(itemList,getApplicationContext(),PFLink);
                                rvItem.setAdapter(menuItemAdapter);




                            }


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EmployeeDashBoardActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                //  Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                showAlert();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");

    }

    private void showAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("something went wrong");
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                    }
                });


    }


    private void shoeDialog(final String docLink) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EmployeeDashBoardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.acceptance_dialog, null);
        dialogBuilder.setView(dialogView);
        TextView tvClick=(TextView)dialogView.findViewById(R.id.tvClick);
        tvClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(docLink));
                startActivity(browserIntent);
            }
        });
        ImageView imgCancel=(ImageView)dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(true);
        Window window = alertDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog.show();


    }




    public String saveImage(Bitmap myBitmap) {

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


    private void msgAlert() {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(EmployeeDashBoardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_message_alert, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);
        tvInvalidDate.setText(pref.getMsg());



        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert3.dismiss();
                acceptance();

            }
        });

        alert3 = dialogBuilder.create();
        alert3.setCancelable(true);
        Window window = alert3.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alert3.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPFURL();
    }
}




