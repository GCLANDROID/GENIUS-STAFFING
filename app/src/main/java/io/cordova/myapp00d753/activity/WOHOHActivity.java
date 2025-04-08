package io.cordova.myapp00d753.activity;

import static io.cordova.myapp00d753.activity.attendance.ProtectorGambleAttendanceActivity.SKF_PUNE_CLIENT_OFFICE_ID;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.Retrofit.RetrofitClient;
import io.cordova.myapp00d753.activity.attendance.AttenDanceDashboardActivity;
import io.cordova.myapp00d753.activity.metso.adapter.LocationSpinnerAdapter;
import io.cordova.myapp00d753.activity.metso.adapter.ShiftSpinnerAdapter;
import io.cordova.myapp00d753.activity.metso.model.MetsoLocationModel;
import io.cordova.myapp00d753.databinding.ActivityWohohactivityBinding;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WOHOHActivity extends AppCompatActivity {
    private static final String TAG = "WOHOHActivity";
    ActivityWohohactivityBinding binding;
    String newdate = "";
    Pref pref;
    ArrayList<MetsoLocationModel> metsoLocationArrayList;
    LocationSpinnerAdapter locationSpinnerAdapter;
    private Dialog shiftAndLocationDialog;
    ShiftSpinnerAdapter shiftSpinnerAdapter;
    String Siteid = "";
    AlertDialog alerDialog1;
    android.app.AlertDialog al1;
    int leaveFlag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wohohactivity);
        initView();
    }

    private void initView() {
        pref = new Pref(WOHOHActivity.this);
        shiftAndLocationDialog = new Dialog(WOHOHActivity.this);
        leaveFlag=getIntent().getIntExtra("leaveFlag",1);
        getLocationData();
        binding.llDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                c.add(Calendar.DAY_OF_MONTH, -7);

                final DatePickerDialog dialog = new DatePickerDialog(WOHOHActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                        newdate = (m + 1) + "/" + d + "/" + y;
                        Log.e( "onDateSet: ", newdate);
                        binding.tvDate.setVisibility(View.VISIBLE);
                        binding.tvDate.setText(Util.changeAnyDateFormat(newdate, "MM/dd/yyyy", "dd MMM yyyy"));
                    }
                }, year, month, day);
                dialog.getDatePicker();
                dialog.show();
            }
        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WOHOHActivity.this, EmployeeDashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //finish();
            }
        });

        binding.llSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!newdate.equals("")) {
                    if (SKF_PUNE_CLIENT_OFFICE_ID.equals(pref.getEmpClintOffId())){
                        openShiftAndLocationPopup();
                    } else {

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("ClientID", pref.getEmpClintId());
                            jsonObject.put("EmployeeID", pref.getEmpId());
                            jsonObject.put("Type", "3");
                            jsonObject.put("StartDate", newdate);
                            jsonObject.put("DbOperation", "3");
                            jsonObject.put("Shiftid", "");
                            jsonObject.put("SiteId", "");
                            jsonObject.put("SecurityCode", pref.getSecurityCode());
                            attendance(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    binding.tvMan.setVisibility(View.GONE);
                } else {
                    Toast.makeText(WOHOHActivity.this, "please select date", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void attendance(JSONObject jsonObject) {
        Log.e(TAG, "attendance: "+jsonObject);
        ProgressDialog progressDialog = new ProgressDialog(WOHOHActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        AndroidNetworking.post(AppData.SaveHolidayleave)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {}
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        shiftAndLocationDialog.dismiss();
                        JSONObject job = response;
                        String Response_Message = job.optString("Response_Message");
                        String Response_Code = job.optString("Response_Code");
                        if (Response_Code.equals("101")) {
                            successAlert(Response_Message);
                        } else {
                            showErrorDialog(Response_Message);
                        }
                        // boolean _status = job1.getBoolean("status");
                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        progressDialog.dismiss();
                        shiftAndLocationDialog.dismiss();
                        Toast.makeText(WOHOHActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();

                    }
                });
    }


    private void getLocationData() {
        ProgressDialog progressDialog = new ProgressDialog(WOHOHActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        progressDialog.setCancelable(false);
        Call<JsonObject> call = RetrofitClient
                .getInstance()
                .getApi()
                .GetMetsoAttendanceData("1", pref.getEmpClintId(), "0000");

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(String.valueOf(response.body()));
                    if (object.getBoolean("responseStatus") == true) {
                        JSONArray jsonArray = object.getJSONArray("responseData");
                        metsoLocationArrayList = new ArrayList<>();
                        metsoLocationArrayList.add(new MetsoLocationModel(0, "Select Location"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject objectResponse = jsonArray.getJSONObject(i);
                            MetsoLocationModel metsoLocationModel = new MetsoLocationModel(objectResponse.getInt("Siteid"),
                                    (String) objectResponse.getString("SiteName"));
                            metsoLocationArrayList.add(metsoLocationModel);
                        }

                        locationSpinnerAdapter = new LocationSpinnerAdapter(WOHOHActivity.this, metsoLocationArrayList);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }


    private void openShiftAndLocationPopup() {
        shiftAndLocationDialog.setContentView(R.layout.shift_location_popup);
        shiftAndLocationDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        shiftAndLocationDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        LinearLayout llShift = shiftAndLocationDialog.findViewById(R.id.llShift);
        llShift.setVisibility(View.GONE);
        LinearLayout lnCancel = shiftAndLocationDialog.findViewById(R.id.lnCancel);
        TextView txtSelectShift = shiftAndLocationDialog.findViewById(R.id.txtSelectShift);
        TextView txtSelectLocation = shiftAndLocationDialog.findViewById(R.id.txtSelectLocation);
        TextView txtErrorShift = shiftAndLocationDialog.findViewById(R.id.txtErrorShift);
        TextView txtErrorLocation = shiftAndLocationDialog.findViewById(R.id.txtErrorLocation);
        Spinner spShift = shiftAndLocationDialog.findViewById(R.id.spShift);
        Spinner spLocation = shiftAndLocationDialog.findViewById(R.id.spLocation);
        AppCompatButton btnMarkedYourAttendance = shiftAndLocationDialog.findViewById(R.id.btnMarkedYourAttendance);
        TextView textView = shiftAndLocationDialog.findViewById(R.id.textView);
        textView.setText("Select location");

        spLocation.setAdapter(locationSpinnerAdapter);
        spLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MetsoLocationModel clickedItem = (MetsoLocationModel) adapterView.getItemAtPosition(i);
                if (!clickedItem.getSiteName().equals("Select Location")) {
                    txtSelectLocation.setText(clickedItem.getSiteName());
                    Siteid = String.valueOf(clickedItem.getSiteid());
                    txtErrorLocation.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        txtSelectShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spShift.performClick();
            }
        });

        txtSelectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spLocation.performClick();
            }
        });

        lnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shiftAndLocationDialog.cancel();
            }
        });

        btnMarkedYourAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtSelectLocation.getText().toString().trim().isEmpty()) {
                    txtErrorLocation.setVisibility(View.VISIBLE);
                } else {
                    txtErrorLocation.setVisibility(View.GONE);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("ClientID", pref.getEmpClintId());
                        jsonObject.put("EmployeeID", pref.getEmpId());
                        jsonObject.put("Type", "3");
                        jsonObject.put("StartDate", newdate);
                        jsonObject.put("DbOperation", "3");
                        jsonObject.put("Shiftid", "");
                        jsonObject.put("SiteId", Siteid);
                        jsonObject.put("SecurityCode", "0000");
                        attendance(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Window window = shiftAndLocationDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.NO_GRAVITY;
        shiftAndLocationDialog.setCancelable(false);
        shiftAndLocationDialog.show();
    }


    private void successAlert(String msg) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(WOHOHActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);

        tvInvalidDate.setText(msg);

        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                Intent intent = new Intent(WOHOHActivity.this, AttenDanceDashboardActivity.class);
                intent.putExtra("leaveFlag",leaveFlag);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }


    private void showErrorDialog(String text) {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(WOHOHActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.error_ayput, null);
        dialogBuilder.setView(dialogView);
        TextView tvError = (TextView) dialogView.findViewById(R.id.tvError);
        tvError.setText(text);
        ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                al1.dismiss();
            }
        });

        al1 = dialogBuilder.create();
        al1.setCancelable(false);
        Window window = al1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        al1.show();
    }
}