package io.cordova.myapp00d753.activity.NEW.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import io.cordova.myapp00d753.activity.NEW.NEW_HolidayViewActivity;
import io.cordova.myapp00d753.activity.SKF.adapter.HolidayViewAdapter;
import io.cordova.myapp00d753.module.HolidayMarkModel;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.DateCalculation;
import io.cordova.myapp00d753.utility.Pref;


public class HolidayViewFragment extends Fragment {
    private static final String TAG = "HolidayViewFragment";
    Pref pref;
    RecyclerView rvHolidayView;
    LinearLayout llNoData,llLoading;
    ArrayList<HolidayMarkModel> holidayList = new ArrayList<>();
    String holidayType,Type="";
    TextView tvHolidayType;
    public static HolidayViewFragment newInstance(String holidayType) {
        HolidayViewFragment fragment = new HolidayViewFragment();
        Bundle args = new Bundle();
        args.putString("holidayType", holidayType);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            holidayType = getArguments().getString("holidayType");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_holiday_view, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        pref = new Pref(requireActivity());
        rvHolidayView = view.findViewById(R.id.rvHolidayView);
        rvHolidayView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        llNoData = view.findViewById(R.id.llNoData);
        llLoading = view.findViewById(R.id.llLoading);
        tvHolidayType = view.findViewById(R.id.tvHolidayType);
        if (holidayType.equalsIgnoreCase("Normal")){
            Type="N";
            tvHolidayType.setText("Normal Holiday");
        } else {
            Type="R";
            tvHolidayType.setText("Optional Holiday");
        }
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ClientID",pref.getEmpClintId());
            jsonObject.put("ClientOfficeID",pref.getEmpClintOffId());
            jsonObject.put("EmployeeID",pref.getEmpId());
            //jsonObject.put("Year","2025");
            jsonObject.put("Year",currentYear);
            jsonObject.put("SecurityCode","0000");
            getHolidayList(jsonObject);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void getHolidayList(JSONObject jsonObject) {
        llNoData.setVisibility(View.GONE);
        rvHolidayView.setVisibility(View.GONE);
        tvHolidayType.setVisibility(View.INVISIBLE);
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
                            llNoData.setVisibility(View.GONE);
                            llLoading.setVisibility(View.GONE);
                            rvHolidayView.setVisibility(View.VISIBLE);
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                JSONArray responseData = new JSONArray(Response_Data);
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.optJSONObject(i);
                                    String date = obj.optString("Date");
                                    String holiday = obj.optString("Holiday Name");
                                    String HolidayType = obj.optString("Type");
                                    //String DayType = obj.optString("DayType");
                                    boolean isBefore = DateCalculation.InputDateBeforeOrAfter(date);
                                    HolidayMarkModel holidayMarkModel = new HolidayMarkModel(holiday, date, isBefore);
                                    holidayMarkModel.setHolidayType(HolidayType);
                                    if (HolidayType.equalsIgnoreCase(Type)){
                                        holidayList.add(holidayMarkModel);
                                    }
                                }
                                if (holidayList.size() > 0){
                                    llNoData.setVisibility(View.GONE);
                                    tvHolidayType.setVisibility(View.VISIBLE);
                                } else {
                                    llNoData.setVisibility(View.VISIBLE);
                                    tvHolidayType.setVisibility(View.INVISIBLE);
                                }
                                HolidayViewAdapter holidayViewAdapter = new HolidayViewAdapter(requireActivity(), holidayList);
                                rvHolidayView.setAdapter(holidayViewAdapter);
                            } else {
                                llNoData.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        llNoData.setVisibility(View.VISIBLE);
                        llLoading.setVisibility(View.GONE);
                        tvHolidayType.setVisibility(View.INVISIBLE);
                        Log.e(TAG, "HOLIDAY_LIST_error: " + anError.getErrorBody());
                    }
                });
    }
}