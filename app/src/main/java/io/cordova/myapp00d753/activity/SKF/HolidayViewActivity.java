package io.cordova.myapp00d753.activity.SKF;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.EmployeeDashBoardActivity;
import io.cordova.myapp00d753.activity.SKF.adapter.HolidayViewAdapter;
import io.cordova.myapp00d753.module.HolidayMarkModel;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.DateCalculation;
import io.cordova.myapp00d753.utility.Pref;

public class HolidayViewActivity extends AppCompatActivity {
    private static final String TAG = "HolidayViewActivity";
    RecyclerView rvHolidayView;
    Pref pref;
    LinearLayout llNoData;
    ImageView imgBack,imgHome;
    ArrayList<HolidayMarkModel> holidayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday_view);
        initView();
        btnClick();
    }



    private void initView() {
        pref = new Pref(HolidayViewActivity.this);
        rvHolidayView = findViewById(R.id.rvHolidayView);
        rvHolidayView.setLayoutManager(new LinearLayoutManager(HolidayViewActivity.this));
        llNoData = findViewById(R.id.llNoData);
        imgBack = findViewById(R.id.imgBack);
        imgHome = findViewById(R.id.imgHome);
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ClientID",pref.getEmpClintId());
            jsonObject.put("ClientOfficeID",pref.getEmpClintOffId());
            jsonObject.put("EmployeeID",pref.getEmpId());
            jsonObject.put("Year",currentYear);
            jsonObject.put("SecurityCode","0000");
            getHolidayList(jsonObject);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void btnClick() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HolidayViewActivity.this, EmployeeDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void getHolidayList(JSONObject jsonObject) {
        ProgressDialog pd = new ProgressDialog(HolidayViewActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(AppData.SKF_GET_HOLIDAY_LIST)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "HOLIDAY_VIEW_LIST: " + response.toString(4));
                            pd.dismiss();
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                JSONArray responseData = new JSONArray(Response_Data);
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.optJSONObject(i);
                                    String date = obj.getString("Date");
                                    String holiday = obj.getString("Holiday");
                                    boolean isBefore = DateCalculation.InputDateBeforeOrAfter(date);
                                    holidayList.add(new HolidayMarkModel(holiday, date, isBefore));
                                }
                                HolidayViewAdapter holidayViewAdapter = new HolidayViewAdapter(HolidayViewActivity.this, holidayList);
                                rvHolidayView.setAdapter(holidayViewAdapter);
                            } else {
                                llNoData.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        pd.dismiss();
                        llNoData.setVisibility(View.VISIBLE);
                        Log.e(TAG, "HOLIDAY_LIST_error: " + anError.getErrorBody());
                    }
                });
    }

}