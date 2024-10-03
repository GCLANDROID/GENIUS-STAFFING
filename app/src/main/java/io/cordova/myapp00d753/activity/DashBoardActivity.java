package io.cordova.myapp00d753.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

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


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;

import io.cordova.myapp00d753.adapter.DashboardItemAdapter;
import io.cordova.myapp00d753.module.DashboardItemModel;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;
import io.cordova.myapp00d753.utility.Pref;

public class DashBoardActivity extends AppCompatActivity {

    Pref pref;

    NetworkConnectionCheck connectionCheck;

    String playversion;
    String version;
    AlertDialog alertDialog,al1,alert1,alert2;
    String responseStatus;
    RecyclerView rvItem;
    ArrayList<DashboardItemModel> itemList = new ArrayList<>();
    LinearLayout llLogin;
    private ReviewManager reviewManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);


        initialize();
        //PostCovidalerts();
        onClick();
    }

    private void initialize() {
        reviewManager = ReviewManagerFactory.create(this);
        llLogin=(LinearLayout)findViewById(R.id.llLogin);
        rvItem=(RecyclerView)findViewById(R.id.rvItem);
        rvItem.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        String security = "0000";
        String d = security.replace("\"", "");
        Log.d("de", security);
        pref = new Pref(DashBoardActivity.this);


        connectionCheck = new NetworkConnectionCheck(DashBoardActivity.this);

        try {
            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
            int verCode = pInfo.versionCode;
            Log.d("sddk", version);
            Log.d("sdkl", String.valueOf(verCode));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        checkBersion();
        setItem();

        SharedPreferences prefs = getSharedPreferences("io.cordova.myapp00d753", MODE_PRIVATE);

        int launch_count = prefs.getInt("launch_count", 0);

        if(launch_count>=5){
            // third time launch
           // Toast.makeText(DashBoardActivity.this,"3 time",Toast.LENGTH_LONG).show();
            RateApp(DashBoardActivity.this);

        } else {
            prefs.edit()
                    .putInt("launch_count", launch_count+1)
                    .apply();
        }

    }

    private void onClick() {

       llLogin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent intent = new Intent(DashBoardActivity.this, LoginActivity.class);
               startActivity(intent);
               finish();

           }
       });

    }

    private void checkBersion() {
        String surl = AppData.url+"gcl_apkVersionChecking?SecurityCode=0000";
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLeave", response);
                        progressBar.dismiss();
                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);

                            JSONArray job=job1.getJSONArray("responseData");
                            JSONObject onj=job.optJSONObject(0);
                            playversion = onj.optString("Version");
                            responseStatus = job1.optString("Mandatory");
                            if (playversion.equals(version)){

                            }else {

                                    upDateAlert();

                            }

                            // boolean _status = job1.getBoolean("status")

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DashBoardActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                Toast.makeText(DashBoardActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(DashBoardActivity.this);
        requestQueue.add(stringRequest);

    }


    private void upDateAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DashBoardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_attendate, null);
        dialogBuilder.setView(dialogView);
        TextView tvAttenDate = (TextView) dialogView.findViewById(R.id.tvAttenDate);
        tvAttenDate.setText("You are using lower version of app.Please update your app");

        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
                }



            }
        });

        Button btnSkip = (Button) dialogView.findViewById(R.id.btnSkip);
        if (responseStatus.equals("Y")) {

            btnSkip.setVisibility(View.GONE);
        }else {
            btnSkip.setVisibility(View.VISIBLE);
        }

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        Window window = alertDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog.show();
    }

    private void intentFunction() {
        if (pref.getIntentFlag().equals("1")) {
            if (pref.getUserType().equals("1")) {
                startActivity(new Intent(DashBoardActivity.this, EmployeeDashBoardActivity.class));
                finish();
            } else if (pref.getUserType().equals("2")) {
                Intent intent = new Intent(DashBoardActivity.this, SuperVisiorDashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else if (pref.getUserType().equals("4") && pref.getEmpId().equals("0")) {
                Intent intent = new Intent(DashBoardActivity.this, TempDashBoardActivity.class);
                startActivity(intent);
                finish();
            } else {

            }
        } else {
            Intent intent = new Intent(DashBoardActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void operBrowser() {
        Uri uri = Uri.parse("https://www.geniusconsultant.com/PDF-doc/GCL_BROCHURE.pdf"); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }



    private void msgAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DashBoardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.msg_dialog, null);
        dialogBuilder.setView(dialogView);

        ImageView imgCancel=(ImageView)dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert1.dismiss();
            }
        });


        alert1 = dialogBuilder.create();
        alert1.setCancelable(true);
        Window window = alert1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.TOP );
        alert1.show();
        PostCovidalerts();
    }

    public static void watchYoutubeVideo(Context context, String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

    private void PostCovidalerts() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DashBoardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.popup_postcovid, null);
        dialogBuilder.setView(dialogView);
        ImageView imgCancel=(ImageView)dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert2.dismiss();
            }
        });

        alert2 = dialogBuilder.create();
        alert2.setCancelable(false);
        Window window = alert2.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.TOP);
        alert2.show();
    }

    private void setItem(){
        itemList.add(new DashboardItemModel("Services",R.mipmap.services));
        itemList.add(new DashboardItemModel("About Us",R.mipmap.aboutus));
        itemList.add(new DashboardItemModel("Contact Us",R.mipmap.contactus));
        itemList.add(new DashboardItemModel("Brochure",R.mipmap.brochure));


        DashboardItemAdapter dashboardItemAdapter=new DashboardItemAdapter(itemList,DashBoardActivity.this);
        rvItem.setAdapter(dashboardItemAdapter);

    }

    public void RateApp(final Context mContext) {
        try {
            final ReviewManager manager = ReviewManagerFactory.create(mContext);
            manager.requestReviewFlow().addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
                @Override
                public void onComplete(@NonNull Task<ReviewInfo> task) {
                    if(task.isSuccessful()){
                        ReviewInfo reviewInfo = task.getResult();
                        manager.launchReviewFlow((Activity) mContext, reviewInfo).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                               // Toast.makeText(mContext, "Rating Failed", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                               // Toast.makeText(mContext, "Review Completed, Thank You!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                  //  Toast.makeText(mContext, "In-App Request Failed", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }




}
