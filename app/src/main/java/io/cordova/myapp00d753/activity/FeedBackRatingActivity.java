package io.cordova.myapp00d753.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.kyanogen.signatureview.SignatureView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.Question1Adapter;
import io.cordova.myapp00d753.adapter.QuestionAdapter;
import io.cordova.myapp00d753.module.Question1Model;
import io.cordova.myapp00d753.module.QuestionModel;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.GPSTracker;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;
import io.cordova.myapp00d753.utility.Pref;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FeedBackRatingActivity extends AppCompatActivity {
    ImageView imgBack;
    ArrayList<QuestionModel> itemList = new ArrayList();
    ArrayList<Question1Model> itemList1 = new ArrayList();
    QuestionAdapter qustionAdapter;
    Question1Adapter qustionAdapter1;
    RecyclerView rvItem;
    QuestionModel qModel;
    ArrayList<String> item = new ArrayList();
    ProgressDialog progressDialog;
    Pref pref;
    String msg, remarks;
    EditText etRemarks;
    LinearLayout llSubmit;
    AlertDialog alertDialog;
    ImageView imgLogout;
    String ans = "";
    String ans1 = "";
    NetworkConnectionCheck networkConnectionCheck;

    RecyclerView rvItem1;
    LinearLayout llLoader, llMain;
    ArrayList<String> item1 = new ArrayList();
    ImageView imgHome;
    String s1, s2, s3, s4, s5;
    String op1, op2, op3, op4, op5;
    ImageView imgSign, imgSignPic;
    Bitmap bitmap;
    String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/GENIUSSURVEY/";
    String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    AlertDialog alerDialog1;
    SignatureView canvasLL;
    File f;
    private static final String IMAGE_DIRECTORY = "/signdemo";
    GPSTracker gps;
    String cuulat, cuulong, cuaddress;
    String userid, domain, clintid, clintname, clintemail, clinntdes, officeid, officename, clintphn, address, answer, longt, lat, clintoffname, username;
    int signFlag;
    LinearLayout llLoad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back_rating);
        initialize();

        getItem();

        onClick();
    }

    private void initialize() {
        pref = new Pref(FeedBackRatingActivity.this);
        gps = new GPSTracker(FeedBackRatingActivity.this);
        networkConnectionCheck = new NetworkConnectionCheck(FeedBackRatingActivity.this);
        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        rvItem1 = (RecyclerView) findViewById(R.id.rvItem1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(FeedBackRatingActivity.this) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        };
        rvItem.setLayoutManager(layoutManager);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(FeedBackRatingActivity.this) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        };

        rvItem1.setLayoutManager(layoutManager1);

        imgBack = (ImageView) findViewById(R.id.imgBack);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        // Change base URL to your upload server URL.

        progressDialog = new ProgressDialog(FeedBackRatingActivity.this);
        progressDialog.setMessage("Loading...");
        etRemarks = (EditText) findViewById(R.id.etRemarks);
        llSubmit = (LinearLayout) findViewById(R.id.llSubmit);
        llLoader = (LinearLayout) findViewById(R.id.llLoader);
        llMain = (LinearLayout) findViewById(R.id.llMain);
        imgHome = (ImageView) findViewById(R.id.imgHome);

        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            cuulat = String.valueOf(latitude);
            Log.d("saikatdas", String.valueOf(latitude));
            double longitude = gps.getLongitude();
            cuulong = String.valueOf(longitude);
            cuaddress = getCompleteAddressString(latitude, longitude);
        }

        llLoad=(LinearLayout)findViewById(R.id.llLoad);


    }

    private void onClick() {


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        etRemarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etRemarks.getText().toString().length() > 0) {
                    remarks = etRemarks.getText().toString();

                }

            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeedBackRatingActivity.this, EmployeeDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        llSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.size() + item1.size() == itemList.size() + itemList1.size()) {
                    if (networkConnectionCheck.isNetworkAvailable()) {

                        signatureAlert();

                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please give your Feedback", Toast.LENGTH_LONG).show();
                }
            }
        });


    }


    public void updateStatus(int position, boolean status) {
        itemList.get(position).setSelected(status);
        if (itemList.get(position).isSelected() == true) {
            item.add(itemList.get(position).getQuestionid() + "-" + itemList.get(position).getAnswervalue());
        } else {
            item.remove(position);
        }
        Log.d("arpan", item.toString());
        String i = item.toString();
        String d = i.replace("[", "").replace("]", "");
        ans = d.replaceAll("\\s+", "");
     //   pref.saveAns(ans);
        qustionAdapter.notifyDataSetChanged();


    }


    public void updateStatus1(int position, boolean status) {
        itemList1.get(position).setSelected(status);
        if (itemList1.get(position).isSelected() == true) {
            item1.add(itemList1.get(position).getQuestionid() + "-" + itemList1.get(position).getAnswervalue());
        } else {
            item1.remove(position);
        }
        Log.d("riku34", item1.toString());
        String i = item1.toString();
        String d = i.replace("[", "").replace("]", "");
        ans1 = d.replaceAll("\\s+", "");
        //pref.saveAns1(ans1);
        qustionAdapter1.notifyDataSetChanged();


    }


    private void getItem() {
        String surl = "https://gsppi.geniusconsultant.com/GeniusiOSApi/api/get_EmployeeFeedbackQuestionList?SecurityCode="+pref.getSecurityCode()+"&QnsType=R&options=1";
        Log.d("clintname", surl);
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responserfeed", response);

                        itemList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                //Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");

                                for (int i = 0; i < 5; i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String QuestionMasterID = obj.optString("QuestionMasterID");
                                    String Question = obj.optString("Question");

                                    JSONObject joby = responseData.getJSONObject(0);
                                    String Hints = joby.optString("Hints");
                                    String[] separated = Hints.split(",");
                                    if (separated.length == 5) {
                                        Log.d("arpan", "riku");
                                        s1 = separated[0];
                                        s2 = separated[1];
                                        s3 = separated[2];
                                        s4 = separated[3];
                                        s5 = separated[4];
                                    }

                                    QuestionModel questionModel = new QuestionModel(Question, QuestionMasterID, s1, s2, s3, s4, s5);
                                    itemList.add(questionModel);


                                }
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                rvItem.setVisibility(View.VISIBLE);
                                qustionAdapter = new QuestionAdapter(itemList, FeedBackRatingActivity.this);
                                rvItem.setAdapter(qustionAdapter);
                                qustionAdapter.notifyDataSetChanged();
                                getItem1();


                            } else {
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                rvItem.setVisibility(View.GONE);
                                itemList.clear();


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(FeedBackRatingActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                // Toast.makeText(FeedBackActivity.this, "R"+error.toString(), Toast.LENGTH_LONG).show();

                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(FeedBackRatingActivity.this);
        requestQueue.add(stringRequest);


    }

    private void getItem1() {
        String surl = AppData.url+ "get_EmployeeFeedbackQuestionList?SecurityCode="+pref.getSecurityCode()+"&QnsType=R&options=1";
        Log.d("clintname", surl);
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responserfeed", response);

                        itemList1.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                //Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");

                                for (int i = 5; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String QuestionMasterID = obj.optString("QuestionMasterID");
                                    String Question = obj.optString("Question");

                                    JSONObject joby = responseData.getJSONObject(0);
                                    String Hints = joby.optString("Hints");
                                    String[] separated = Hints.split(",");
                                    if (separated.length == 5) {
                                        Log.d("arpan", "riku");
                                        op1 = separated[0];
                                        op2 = separated[1];
                                        op3 = separated[2];
                                        op4 = separated[3];
                                        op5 = separated[4];
                                    }

                                    Question1Model questionModel = new Question1Model(Question, QuestionMasterID, op1, op2, op3, op4, op5);
                                    itemList1.add(questionModel);


                                }
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                rvItem1.setVisibility(View.VISIBLE);
                                qustionAdapter1 = new Question1Adapter(itemList1, FeedBackRatingActivity.this);
                                rvItem1.setAdapter(qustionAdapter1);
                                qustionAdapter1.notifyDataSetChanged();


                            } else {
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                rvItem1.setVisibility(View.GONE);
                                itemList1.clear();


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            //  Toast.makeText(FeedBackActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                // Toast.makeText(FeedBackActivity.this, "R"+error.toString(), Toast.LENGTH_LONG).show();

                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(FeedBackRatingActivity.this);
        requestQueue.add(stringRequest);


    }







    private void successalert(String txt) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FeedBackRatingActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvSuccess = (TextView) dialogView.findViewById(R.id.tvSuccess);
        tvSuccess.setText(txt);
        Button llOk = (Button) dialogView.findViewById(R.id.btnOk);
        llOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Intent intent = new Intent(FeedBackRatingActivity.this, EmployeeDashBoardActivity.class);
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


    private void signatureAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FeedBackRatingActivity.this, R.style.CustomDialogNew);
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

                bitmap = canvasLL.getSignatureBitmap();
                String path = saveImage(bitmap);
                Log.d("path", path);
                alerDialog1.dismiss();
                submitAnswer();


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


    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current ", strReturnedAddress.toString());
            } else {
                Log.w("My Current", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current", "Canont get Address!");
        }
        return strAdd;
    }




   private void submitAnswer() {
        if (canvasLL.isBitmapEmpty()){
            Toast.makeText(getApplicationContext(),"Please sign",Toast.LENGTH_LONG).show();
        }else {

            postwithimageanswer();
        }

    }




    private void postwithimageanswer() {
        String answer = ans + "," + ans1;
        AndroidNetworking.upload(AppData.url + "post_EmployeeFeedback")
                .addMultipartParameter("Answer", answer)
                .addMultipartParameter("UserId", pref.getEmpId())
                .addMultipartParameter("AEMEmployeeID", pref.getEmpId())
                .addMultipartParameter("Remarks", etRemarks.getText().toString())
                .addMultipartParameter("Longitude", cuulong)
                .addMultipartParameter("Latitude", cuulat)
                .addMultipartParameter("Address", cuaddress)
                .addMultipartParameter("UserName", pref.getEmpName())
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())
                .addMultipartFile("image", f)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        llLoad.setVisibility(View.VISIBLE);

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        String responseText = job1.optString("responseText");
                        boolean responseStatus = job1.optBoolean("responseStatus");

                        if (responseStatus) {
                            msg = responseText;
                            Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_SHORT).show();
                            llLoad.setVisibility(View.GONE);
                            successalert(msg);

                        } else {
                            Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_SHORT).show();
                            llLoad.setVisibility(View.GONE);

                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.d("errort", error.toString());
                        llLoad.setVisibility(View.GONE);

                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG);
                    }
                });
    }
}

