package io.cordova.myapp00d753.activity.murugappa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
import io.cordova.myapp00d753.activity.attendance.AttendanceReportActivity;
import io.cordova.myapp00d753.adapter.AttendanceAdapter;
import io.cordova.myapp00d753.adapter.CheckInAttendanceAdapter;
import io.cordova.myapp00d753.databinding.ActivityCheckInAttendanceBinding;
import io.cordova.myapp00d753.module.AttendanceModule;
import io.cordova.myapp00d753.module.CheckInOutAttendanceModel;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.Util;

public class CheckInAttendanceActivity extends AppCompatActivity {
    ActivityCheckInAttendanceBinding binding;
    LinearLayoutManager layoutManager;
    Pref pref;
    ArrayList<CheckInOutAttendanceModel>itemList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_check_in_attendance);
        initView();
    }

    private void initView(){
        pref=new Pref(CheckInAttendanceActivity.this);
        layoutManager = new LinearLayoutManager(CheckInAttendanceActivity.this, LinearLayoutManager.VERTICAL, false);
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
            jsonObject.put("PunchFlag","I");
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
       ProgressDialog pd=new ProgressDialog(CheckInAttendanceActivity.this);
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
                                String InTime=jobj.optString("InTime");
                                String OutTime=jobj.optString("OutTime");
                                CheckInOutAttendanceModel model=new CheckInOutAttendanceModel();
                                model.setAEMEmployeeID(EmpID);
                                model.setEmpName(EmpName);
                                model.setInTime(InTime);
                                model.setOuttime(OutTime);
                                itemList.add(model);


                            }
                            CheckInAttendanceAdapter adapter=new CheckInAttendanceAdapter(itemList,CheckInAttendanceActivity.this);
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

            mainJson.put("PunchFlag", "I");

            JSONArray listArray = new JSONArray();

            for (int i = 0; i < itemList.size(); i++) {
                CheckInOutAttendanceModel model = itemList.get(i);
                if (model.isSelected()) {
                    JSONObject empObj = new JSONObject();
                    empObj.put("EmpName", model.getEmpName());
                    empObj.put("EmpID", model.getAEMEmployeeID());

                    // Example shift time (static or dynamic)
                    empObj.put("ShiftTime", convertTo12HourFormat(model.getInTime()));

                    // Selected check-in time from time picker
                    empObj.put("CheckInTime", convertTo12HourFormat(model.getInTime()));

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

}