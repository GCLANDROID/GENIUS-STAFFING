package io.cordova.myapp00d753.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.PfAdapter;
import io.cordova.myapp00d753.module.PfModule;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.RecyclerItemClickListener;

public class PFActivity extends AppCompatActivity implements RecyclerItemClickListener.OnItemClickListener {
    RecyclerView rvPf;
    ArrayList<PfModule>pfList=new ArrayList<>();
    ImageView imgBack,imgHome;
    Pref pref;
    LinearLayout llMain,llLoader;
    String finalYear,pfUrl;
    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;
    NetworkConnectionCheck networkConnectionCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pf);
        initialize();
        if (networkConnectionCheck.isNetworkAvailable()) {

            getpfList();

        }else {
            networkConnectionCheck.getNetworkActiveAlert().show();
        }


        onClick();
    }

    private  void  initialize(){
        pref=new Pref(getApplicationContext());
        networkConnectionCheck=new NetworkConnectionCheck(this);
        rvPf=(RecyclerView)findViewById(R.id.rvPf);
        rvPf.addOnItemTouchListener(new RecyclerItemClickListener(PFActivity.this, PFActivity.this));

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(PFActivity.this, LinearLayoutManager.VERTICAL, false);
        rvPf.setLayoutManager(layoutManager);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        llMain=(LinearLayout)findViewById(R.id.llMain);
        llLoader=(LinearLayout)findViewById(R.id.llLoader);
    }

    private void getpfList(){
        String surl ="http://111.93.182.174/GeniusiOSApi/api/get_PfFinYear?AEMEmployeeID="+pref.getEmpId()+"&SecurityCode="+pref.getSecurityCode();
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        Log.d("ctcinput",surl);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText=job1.optString("responseText");
                            boolean responseStatus=job1.optBoolean("responseStatus");
                            if (responseStatus){
                                Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData=job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++){
                                    JSONObject obj=responseData.getJSONObject(i);
                                    String FinYear=obj.optString("FinYear");
                                    String  url=obj.optString("url");
                                    Log.d("url",url);
                                    PfModule pfModule=new PfModule(FinYear,url);
                                    pfList.add(pfModule);


                                }
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                setAdapter();
                            }
                            else {
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            llLoader.setVisibility(View.VISIBLE);
                            llMain.setVisibility(View.GONE);
                            Toast.makeText(PFActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);

                Toast.makeText(PFActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert",error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
    }

    private void setAdapter(){
        PfAdapter pfAdapter=new PfAdapter(pfList);
        rvPf.setAdapter(pfAdapter);
    }

    private void onClick(){
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PFActivity.this,DashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    @Override
    public void onItemClick(View childView, int position) {
        finalYear=pfList.get(position).getPfYear();
        Log.d("finalyear",finalYear);
        pfUrl=pfList.get(position).getPfUrl();
        Log.d("pfurl",pfUrl);

            operBrowser();

    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }

    private void operBrowser(){
            Uri uri = Uri.parse(pfUrl); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!pfUrl.equals("")) {
                startActivity(intent);
            }else {

            }

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                // Output stream
                OutputStream output = new FileOutputStream(Environment
                        .getExternalStorageDirectory().toString()
                        + "/2011.kml");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type);

        }

    }
}
