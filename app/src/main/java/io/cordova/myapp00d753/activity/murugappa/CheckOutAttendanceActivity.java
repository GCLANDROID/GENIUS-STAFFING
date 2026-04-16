package io.cordova.myapp00d753.activity.murugappa;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.CheckInAttendanceAdapter;
import io.cordova.myapp00d753.adapter.CheckOutAttendanceAdapter;
import io.cordova.myapp00d753.databinding.ActivityCheckInAttendanceBinding;
import io.cordova.myapp00d753.databinding.ActivityCheckOutAttendanceBinding;
import io.cordova.myapp00d753.module.CheckInOutAttendanceModel;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.Util;

public class CheckOutAttendanceActivity extends AppCompatActivity {
    ActivityCheckOutAttendanceBinding binding;
    LinearLayoutManager layoutManager;
    Pref pref;
    ArrayList<CheckInOutAttendanceModel>itemList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_check_out_attendance);
        initView();
    }

    private void initView(){
        pref=new Pref(CheckOutAttendanceActivity.this);
        layoutManager = new LinearLayoutManager(CheckOutAttendanceActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvItem.setLayoutManager(layoutManager);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        binding.tvCurrentDate.setText(currentDate);

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("ConsultantID",pref.getEmpConId());
            jsonObject.put("ClientID",pref.getEmpClintId());
            jsonObject.put("ApproverID",pref.getMasterId());
            jsonObject.put("AttDate", Util.changeAnyDateFormat(currentDate,"dd-MMM-yyyy","MM/dd/yyyy"));
            jsonObject.put("PunchFlag","O");
            jsonObject.put("SecurityCode",pref.getSecurityCode());
            getEmpList(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        binding.tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createSubmitJson();
            }
        });
    }

    private void getEmpList(JSONObject jsonObject) {
       ProgressDialog pd=new ProgressDialog(CheckOutAttendanceActivity.this);
        pd.setMessage("Loading...");
        pd.show();
        pd.setCancelable(false);
        AndroidNetworking.post(AppData.CHECK_IN_EMPLIST)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                        pd.dismiss();
                        JSONObject job1 = response;
                        String Response_Code = job1.optString("Response_Code");
                        String Response_Message = job1.optString("Response_Message");
                        if (Response_Code.equals("1")) {
                            JSONObject Response_Data= job1.optJSONObject("Response_Data");
                            JSONArray Table= Response_Data.optJSONArray("Table");
                            for (int i=0;i<Table.length();i++  ){
                                JSONObject jobj= Table.optJSONObject(i);
                                String EmpID=jobj.optString("AEMEmployeeID");
                                String EmpName=jobj.optString("EmpName");
                                String InTime=jobj.optString("Intime");
                                String OutTime=jobj.optString("Outtime");
                                CheckInOutAttendanceModel model=new CheckInOutAttendanceModel();
                                model.setAEMEmployeeID(EmpID);
                                model.setEmpName(EmpName);
                                model.setInTime(InTime);
                                model.setOuttime(OutTime);
                                itemList.add(model);


                            }
                            CheckOutAttendanceAdapter adapter=new CheckOutAttendanceAdapter(itemList, CheckOutAttendanceActivity.this);
                            binding.rvItem.setAdapter(adapter);


                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        pd.dismiss();

                    }
                });
    }


    private void createSubmitJson() {
        try {
            JSONObject mainJson = new JSONObject();

            mainJson.put("ConsultantID", pref.getEmpConId());
            mainJson.put("ClientID", pref.getEmpClintId());
            mainJson.put("ApproverID", pref.getMasterId());

            String currentDate = binding.tvCurrentDate.getText().toString();
            mainJson.put("AttDate",
                    Util.changeAnyDateFormat(currentDate, "dd-MMM-yyyy", "MM/dd/yyyy"));

            mainJson.put("PunchFlag", "O");

            JSONArray listArray = new JSONArray();

            for (int i = 0; i < itemList.size(); i++) {
                CheckInOutAttendanceModel model = itemList.get(i);
                if (model.isSelected()) {
                    JSONObject empObj = new JSONObject();
                    empObj.put("EmpName", model.getEmpName());
                    empObj.put("EmpID", model.getAEMEmployeeID());

                    // Example shift time (static or dynamic)
                    empObj.put("ShiftTime", convertTo12HourFormat(model.getOuttime()));

                    // Selected check-in time from time picker
                    empObj.put("CheckInTime", model.getInTime());

                    listArray.put(empObj);
                }
            }

            if (listArray.length() == 0) {
                Toast.makeText(this,
                        "Please select at least one employee",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            mainJson.put("list", listArray);
            mainJson.put("SecurityCode", pref.getSecurityCode());

            Log.d("SUBMIT_JSON", mainJson.toString(4));

            postAttendance(mainJson);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String convertTo12HourFormat(String time24) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

            Date date = inputFormat.parse(time24);
            return outputFormat.format(date);

        } catch (Exception e) {
            e.printStackTrace();
            return time24;
        }
    }


    private void postAttendance(JSONObject jsonObject) {
        ProgressDialog pd=new ProgressDialog(CheckOutAttendanceActivity.this);
        pd.setMessage("Loading...");
        pd.show();
        pd.setCancelable(false);
        AndroidNetworking.post(AppData.POST_MURUGUPAA_ATTENDANCE)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                        pd.dismiss();
                        JSONObject job1 = response;
                        String Response_Code = job1.optString("Response_Code");
                        String Response_Message = job1.optString("Response_Message");
                        if (Response_Code.equals("101")) {

                            showProductStatusDialog(CheckOutAttendanceActivity.this, response);




                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        pd.dismiss();

                    }
                });
    }


    public void showProductStatusDialog(Context context, JSONObject response) {
        try {
            JSONObject Response_Data= response.optJSONObject("Response_Data");
            JSONArray dataArray = Response_Data.optJSONArray("Table");

            if (dataArray == null || dataArray.length() == 0) {
                Toast.makeText(context, "No data available", Toast.LENGTH_SHORT).show();
                return;
            }

            StringBuilder messageBuilder = new StringBuilder();

            int successCount = 0;
            int failCount = 0;

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject item = dataArray.getJSONObject(i);

                String EmpName = item.optString("EmpName", "N/A");
                String Remarks = item.optString("Remarks", "Unknown status");

                // Simplify backend messages




                messageBuilder.append("• ")
                        .append(EmpName)
                        .append(" → ")
                        .append(Remarks)
                        .append("\n\n");
            }

            // Decide title



            // Add summary on top


            // Create TextView for scrollable content
            TextView textView = new TextView(context);
            textView.setText(messageBuilder.toString());
            textView.setTextSize(14f);
            textView.setTextColor(Color.parseColor("#000000"));
            textView.setPadding(40, 40, 40, 40);

            // Enable scrolling
            ScrollView scrollView = new ScrollView(context);
            scrollView.addView(textView);

            // Show dialog
            new AlertDialog.Builder(context)
                    .setTitle("Attendance Submission Status")
                    .setView(scrollView)
                    .setPositiveButton("OK", null)
                    .show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error parsing response", Toast.LENGTH_SHORT).show();
        }
    }

}