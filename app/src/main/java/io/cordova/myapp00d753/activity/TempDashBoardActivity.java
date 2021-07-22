package io.cordova.myapp00d753.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;
import io.cordova.myapp00d753.utility.Pref;

public class TempDashBoardActivity extends AppCompatActivity {
    LinearLayout llProfile, llDocument, llLogout;
    Pref pref;
    TextView tvEmployeeName, tvGreeting, tvLoginDateTime;
    NetworkConnectionCheck connectionCheck;
    String s1,s2,s3,s4,s5,s6,s7,s8,s9;
    LinearLayout llCall,llHelp;
    android.app.AlertDialog alerDialog1;


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
                                    pref.saveContactPer(Contact);
                                    String DocumentUpload=obj.optString("DocumentUpload");
                                    pref.saveDocPer(DocumentUpload);
                                    String FamilyMember=obj.optString("MemberUpload");
                                    pref.saveFamilyPer(FamilyMember);
                                    String Miscellanneous=obj.optString("Miscellanneous");
                                    pref.saveMisPer(Miscellanneous);
                                    String Necessary=obj.optString("Necessary");
                                    pref.saveNesPer(Necessary);


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



}
