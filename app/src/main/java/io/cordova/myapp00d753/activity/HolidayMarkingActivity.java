package io.cordova.myapp00d753.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.HolidayCustomAdapter;
import io.cordova.myapp00d753.databinding.ActivityHolidayMarkingBinding;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.Util;

public class HolidayMarkingActivity extends AppCompatActivity {
    private static final String TAG = "HolidayMarkingActivity";
    ActivityHolidayMarkingBinding binding;
    Pref pref;
    Spinner spHoliday;
    LinearLayout llSubmit;
    TextView tvDate;
    ArrayList<String> holidayList;
    HolidayCustomAdapter holidayCustomAdapter;
    String holidayDate="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_holiday_marking);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_holiday_marking);

        initView();
    }

    private void initView() {
        pref = new Pref(HolidayMarkingActivity.this);
        spHoliday = findViewById(R.id.spHoliday);
        llSubmit = findViewById(R.id.llSubmit);
        tvDate = findViewById(R.id.tvDate);
        JSONObject obj=new JSONObject();
        try {
            obj.put("ClientID" , pref.getEmpClintId() );
            obj.put("EmployeeID","0");
            obj.put("Type","1");
            obj.put("Year" , "");
            getHolidayData(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        llSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!holidayDate.equals("")) {
                    //openShiftAndLocationPopup();
                    binding.tvMan.setVisibility(View.GONE);
                } else {
                    Toast.makeText(HolidayMarkingActivity.this, "please select date", Toast.LENGTH_LONG).show();
                }
            }
        });

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spHoliday.performClick();
            }
        });
    }

    private void getHolidayData(JSONObject jsonObject) {
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
                                holidayList = new ArrayList<>();
                                String Response_Data = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(Response_Data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String holiDay = obj.optString("HOLIDAY");

                                    holidayList.add(holiDay);
                                }

                               /* ArrayAdapter arrayAdapter = new ArrayAdapter(HolidayMarkingActivity.this,
                                        android.R.layout.simple_spinner_item, holidayList);
                                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spHoliday.setAdapter(arrayAdapter);*/

                                holidayCustomAdapter = new HolidayCustomAdapter(HolidayMarkingActivity.this, holidayList);
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
                                });
                            }
                        } catch (JSONException e) {
                            Toast.makeText(HolidayMarkingActivity.this, "Something went to wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "SKF_HOLIDAY_LIST_error: "+anError.getErrorBody());
                    }
                });
    }

    private void dateFormat(String selectedDate) {
        String[] date = selectedDate.split("-",2);
        //holidayDate = date[0];
        holidayDate = Util.changeAnyDateFormat(date[0], "dd MMM yyyy", "MM/dd/yyyy");
        Log.e(TAG, "dateFormat: "+holidayDate);

    }
}