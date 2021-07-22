package io.cordova.myapp00d753.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.EmployeeMappinglAdapter;
import io.cordova.myapp00d753.module.EmployeeMapiingModule;
import io.cordova.myapp00d753.module.SpinnerModel;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

public class EmpMappingActivity extends AppCompatActivity {
    ArrayList<EmployeeMapiingModule> empList = new ArrayList<>();
    RecyclerView rvItem;
    LinearLayout llLoader, llMain, llNoData;
    Pref pref;
    ImageView imgBack, imgHome;
    ArrayList<String> itemList = new ArrayList<>();
    EmployeeMappinglAdapter cAdapter;
    ArrayList<SpinnerModel> mLocationList = new ArrayList<>();
    ArrayList<String> locationList = new ArrayList<>();
    Button btnMap;
    Spinner spLocation;
    AlertDialog alerDialog1;
    String empId;
    String loactionId;
    AlertDialog alerDialog4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_mapping);
        initView();
        getEmpList();
        onClick();
    }

    private void initView() {
        pref = new Pref(getApplicationContext());
        llLoader = (LinearLayout) findViewById(R.id.llLoader);
        llMain = (LinearLayout) findViewById(R.id.llMain);
        llNoData = (LinearLayout) findViewById(R.id.llNoData);
        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(EmpMappingActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        btnMap=(Button)findViewById(R.id.btnMap);
    }

    private void onClick(){
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationAlert();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),EmployeeDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getEmpList() {

        String surl = AppData.url+"get_EmployeeGeofenceConfigure?AEMConsultantID="+pref.getEmpConId()+"&AEMClientID="+pref.getEmpClintId()+"&AEMConsultantOffID=ACONOF0910000018&EmployeeId=0&GeoFenceId=0&Operation=1&SecurityCode="+pref.getSecurityCode();
        Log.d("configurl", surl);
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseconfig", response);

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("responseconfig", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {

                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String EmpName = obj.optString("Name");
                                    String EmployeeID = obj.optString("AEMEmployeeID");
                                    String LocationName=obj.optString("LocationName");

                                    EmployeeMapiingModule mModel = new EmployeeMapiingModule(EmpName, EmployeeID,LocationName);
                                    empList.add(mModel);
                                    cAdapter = new EmployeeMappinglAdapter(empList, EmpMappingActivity.this);
                                    rvItem.setAdapter(cAdapter);

                                }

                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNoData.setVisibility(View.GONE);



                            } else {
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNoData.setVisibility(View.VISIBLE);
                            }


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            //  Toast.makeText(EmployeeDashBoardActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                 Toast.makeText(EmpMappingActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());

            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(EmpMappingActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    private void locationGet() {
        String surl = AppData.url+"get_GeofenceConfiguration?SLongitude=0&SLatitude=0&SAddress=0&ELongitude=0&ELatitude=0&EAddress=0&EndPoint=0&LocationName=0&Operation=1&SecurityCode="+pref.getSecurityCode();
        Log.d("locationgeturl", surl);
        final ProgressDialog progressDialog=new ProgressDialog(EmpMappingActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseconfig", response);
                        progressDialog.dismiss();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("responseconfig", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {

                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String LocationName = obj.optString("LocationName");
                                    String GeoFenceId=obj.optString("GeoFenceId");
                                    SpinnerModel spModel=new SpinnerModel(LocationName,GeoFenceId);
                                    locationList.add(LocationName);
                                    mLocationList.add(spModel);

                                }

                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (EmpMappingActivity.this, android.R.layout.simple_spinner_item,
                                                locationList); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spLocation.setAdapter(spinnerArrayAdapter);




                            } else {
                                progressDialog.show();
                            }


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            //  Toast.makeText(EmployeeDashBoardActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                 Toast.makeText(EmpMappingActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());

            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(EmpMappingActivity.this);
        requestQueue.add(stringRequest);


    }

    public void updateAttendanceStatus(int position, boolean status) {
        empList.get(position).setSelected(status);
        if (empList.get(position).isSelected() == true) {
            itemList.add(empList.get(position).getEmpId());
        } else {
            itemList.clear();
        }


        Log.d("arpan", itemList.toString());
        String i = itemList.toString();
        String d = i.replace("[", "").replace("]", "");
        empId=d.replaceAll("\\s+", "");


        cAdapter.notifyDataSetChanged();
    }


    private void locationAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EmpMappingActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_spinner, null);
        dialogBuilder.setView(dialogView);
        locationGet();
        spLocation=(Spinner)dialogView.findViewById(R.id.spLocation);
        spLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loactionId=mLocationList.get(position).getItemId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button btnOk=(Button)dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                empMapping(loactionId);
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


    private void empMapping(String loactionId) {
        String surl = AppData.url+"get_EmployeeGeofenceConfigure?AEMConsultantID=0&AEMClientID=0&AEMConsultantOffID=0&EmployeeId="+empId+"&GeoFenceId="+loactionId+"&Operation=3&SecurityCode="+pref.getSecurityCode();
        Log.d("mappingurl", surl);
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseconfig", response);
                        progressDialog.dismiss();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("responseconfig", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {


                               successAlert(responseText);



                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"IN PROBLEM",Toast.LENGTH_LONG).show();
                            }


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            //  Toast.makeText(EmployeeDashBoardActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Toast.makeText(EmployeeDashBoardActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                Log.e("ert", error.toString());
                showAlert();

            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(EmpMappingActivity.this);
        requestQueue.add(stringRequest);


    }


    private void successAlert(String responseText) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EmpMappingActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);

        tvInvalidDate.setText(responseText);

        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog4.dismiss();

                    Intent intent = new Intent(EmpMappingActivity.this, FencingDashBoardActivity.class);
                    startActivity(intent);
                    finish();

            }
        });

        alerDialog4 = dialogBuilder.create();
        alerDialog4.setCancelable(true);
        Window window = alerDialog4.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog4.show();
    }

    private void showAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Something went wrong");
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();

                    }
                });
        alertDialogBuilder.show();




    }



}
