package io.cordova.myapp00d753.activity;

import static io.cordova.myapp00d753.activity.protectorgamble.ProtectorGambleAttendanceActivity.SKF_PUNE_CLIENT_OFFICE_ID;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import io.cordova.myapp00d753.activity.metso.MetsoAttendanceActivity;
import io.cordova.myapp00d753.activity.metso.MetsoNewReimbursementClaimActivity;
import io.cordova.myapp00d753.activity.metso.adapter.LocationSpinnerAdapter;
import io.cordova.myapp00d753.activity.metso.adapter.SupervisorFilterAdapter;
import io.cordova.myapp00d753.activity.metso.model.MetsoLocationModel;
import io.cordova.myapp00d753.adapter.HolidayCustomAdapter;
import io.cordova.myapp00d753.adapter.HolidayFilterAdapter;
import io.cordova.myapp00d753.databinding.ActivityHolidayMarkingBinding;
import io.cordova.myapp00d753.module.HolidayMarkModel;
import io.cordova.myapp00d753.module.SpineerItemModel;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.ClientID;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HolidayMarkingActivity extends AppCompatActivity {
    private static final String TAG = "HolidayMarkingActivity";
    ActivityHolidayMarkingBinding binding;
    Pref pref;
    Spinner spHoliday;
    LinearLayout llHolidayMark,llOptionalHolidayMark;
    TextView tvDate;
    ArrayList<HolidayMarkModel> holidayList= new ArrayList<>();;
    HolidayCustomAdapter holidayCustomAdapter;
    String holidayDate="";
    private Dialog shiftAndLocationDialog;
    String Siteid = "";
    LocationSpinnerAdapter locationSpinnerAdapter;
    ArrayList<MetsoLocationModel> metsoLocationArrayList;
    AlertDialog alerDialog1;
    android.app.AlertDialog al1;
    ProgressDialog pd;
    Dialog searchHolidayDialog;
    int leaveFlag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_holiday_marking);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_holiday_marking);
        initView();
    }

    private void initView() {
        pref = new Pref(HolidayMarkingActivity.this);
        if (pref.getEmpClintId().equals(ClientID.SVF)){
            llOptionalHolidayMark.setVisibility(View.GONE);
        }
        leaveFlag=getIntent().getIntExtra("leaveFlag",1);
        searchHolidayDialog = new Dialog(HolidayMarkingActivity.this, R.style.CustomDialogNew2);
        spHoliday = findViewById(R.id.spHoliday);
        llHolidayMark = findViewById(R.id.llHolidayMark);
        llOptionalHolidayMark = findViewById(R.id.llOptionalHolidayMark);
        tvDate = findViewById(R.id.tvDate);

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        JSONObject obj=new JSONObject();
        try {
            obj.put("ClientID" , pref.getEmpClintId());
            obj.put("EmployeeID",pref.getEmpId());
            obj.put("Type","1");
            obj.put("Year" , currentYear);
            getHolidayData(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HolidayMarkingActivity.this, EmployeeDashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        llHolidayMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!holidayDate.equals("")) {
                    if (SKF_PUNE_CLIENT_OFFICE_ID.equals(pref.getEmpClintOffId())){
                        openShiftAndLocationPopup("1");
                    } else {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("ClientID", pref.getEmpClintId());
                            jsonObject.put("EmployeeID", pref.getEmpId());
                            jsonObject.put("Type", "1");
                            jsonObject.put("StartDate", holidayDate);
                            jsonObject.put("DbOperation", "3");
                            jsonObject.put("Shiftid", "");
                            jsonObject.put("SiteId", "");
                            jsonObject.put("SecurityCode", "0000");
                            attendance(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    binding.tvMan.setVisibility(View.GONE);
                } else {
                    Toast.makeText(HolidayMarkingActivity.this, "please select date", Toast.LENGTH_LONG).show();
                }
            }
        });

        llOptionalHolidayMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!holidayDate.equals("")) {
                    if (SKF_PUNE_CLIENT_OFFICE_ID.equals(pref.getEmpClintOffId())){
                        openShiftAndLocationPopup("2");
                    } else {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("ClientID", pref.getEmpClintId());
                            jsonObject.put("EmployeeID", pref.getEmpId());
                            jsonObject.put("Type", "2");
                            jsonObject.put("StartDate", holidayDate);
                            jsonObject.put("DbOperation", "3");
                            jsonObject.put("Shiftid", "");
                            jsonObject.put("SiteId", "");
                            jsonObject.put("SecurityCode", "0000");
                            attendance(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    binding.tvMan.setVisibility(View.GONE);
                } else {
                    Toast.makeText(HolidayMarkingActivity.this, "please select date", Toast.LENGTH_LONG).show();
                }
            }
        });

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //spHoliday.performClick();
                openSearchHolidayDialog();
            }
        });
    }

    private void openSearchHolidayDialog() {
        searchHolidayDialog.setContentView(R.layout.wbs_code_search_layout);
        searchHolidayDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        searchHolidayDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        searchHolidayDialog.setCancelable(true);

        TextView txtPopupHeadline = searchHolidayDialog.findViewById(R.id.txtPopupHeadline);
        SearchView wbsCodeSearchView = (SearchView) searchHolidayDialog.findViewById(R.id.wbsCodeSearchView);
        ImageView imgCancel = searchHolidayDialog.findViewById(R.id.imgCancel);
        RecyclerView rvWbsCode = searchHolidayDialog.findViewById(R.id.rvWbsCode);

        wbsCodeSearchView.setQueryHint("Search Holiday");
        txtPopupHeadline.setText("Select Holiday");
        rvWbsCode.setLayoutManager(new LinearLayoutManager(HolidayMarkingActivity.this));


        ArrayList<HolidayMarkModel> holidayListCopy = (ArrayList<HolidayMarkModel>) holidayList.clone();
        HolidayFilterAdapter holidayFilterAdapter = new HolidayFilterAdapter(HolidayMarkingActivity.this, holidayListCopy);
        rvWbsCode.setAdapter(holidayFilterAdapter);


        wbsCodeSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //supervisorFilterAdapter.getFilter().filter(s);
                holidayFilterAdapter.getFilter().filter(s);
                return false;
            }
        });


        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchHolidayDialog.dismiss();
            }
        });
        searchHolidayDialog.show();
    }

    private void getHolidayData(JSONObject jsonObject) {
        Log.e(TAG, "getHolidayData: "+jsonObject);
        pd = new ProgressDialog(HolidayMarkingActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(AppData.SKF_GET_HOLIDAY_DETAILS)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "SKF_HOLIDAY_LIST: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                holidayList.clear();
                                String Response_Data = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(Response_Data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String holiDay = obj.optString("HOLIDAY");
                                    String holidayDate = obj.optString("HolidayDate");

                                    holidayList.add(new HolidayMarkModel(holiDay,holidayDate));
                                }

                                /*holidayCustomAdapter = new HolidayCustomAdapter(HolidayMarkingActivity.this, holidayList);
                                spHoliday.setAdapter(holidayCustomAdapter);
                                spHoliday.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        tvDate.setText((CharSequence) adapterView.getSelectedItem());
                                        dateFormat(adapterView.getSelectedItem().toString());
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });*/
                            }

                            getLocationData();
                        } catch (JSONException e) {
                            Toast.makeText(HolidayMarkingActivity.this, "Something went to wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        pd.dismiss();
                        Log.e(TAG, "SKF_HOLIDAY_LIST_error: "+anError.getErrorBody());
                    }
                });
    }

    public void dateFormat(String selectedDate,String reqDate) {
        searchHolidayDialog.dismiss();
        tvDate.setText(selectedDate);
        holidayDate = Util.changeAnyDateFormat(reqDate, "yyyy-MM-dd'T'HH:mm:ss", "MM/dd/yyyy");
        Log.e(TAG, "dateFormat: "+holidayDate);
    }

    private void openShiftAndLocationPopup(String holidayType) {
        shiftAndLocationDialog = new Dialog(HolidayMarkingActivity.this);
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
                    shiftAndLocationDialog.cancel();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("ClientID", pref.getEmpClintId());
                        jsonObject.put("EmployeeID", pref.getEmpId());
                        jsonObject.put("Type", holidayType);
                        jsonObject.put("StartDate", holidayDate);
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

    private void getLocationData() {
        /*ProgressDialog progressDialog = new ProgressDialog(HolidayMarkingActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();*/
        //progressDialog.setCancelable(false);
        Call<JsonObject> call = RetrofitClient
                .getInstance()
                .getApi()
                .GetMetsoAttendanceData("1", pref.getEmpClintId(), "0000");

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                pd.dismiss();
                //progressDialog.dismiss();
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

                        locationSpinnerAdapter = new LocationSpinnerAdapter(HolidayMarkingActivity.this, metsoLocationArrayList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                pd.dismiss();
            }
        });
    }

    private void attendance(JSONObject jsonObject) {
        Log.e(TAG, "attendance: "+jsonObject);
        ProgressDialog progressDialog = new ProgressDialog(HolidayMarkingActivity.this);
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
                        Log.e(TAG, "HOLIDAY_MARKING_SAVE: "+response);
                        progressDialog.dismiss();
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
                        Toast.makeText(HolidayMarkingActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void successAlert(String msg) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(HolidayMarkingActivity.this, R.style.CustomDialogNew);
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
                Intent intent = new Intent(HolidayMarkingActivity.this, AttenDanceDashboardActivity.class);
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
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(HolidayMarkingActivity.this, R.style.CustomDialogNew);
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