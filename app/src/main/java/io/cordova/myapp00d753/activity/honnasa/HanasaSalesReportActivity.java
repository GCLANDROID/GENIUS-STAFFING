package io.cordova.myapp00d753.activity.honnasa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import io.cordova.myapp00d753.R;

import io.cordova.myapp00d753.activity.EmployeeDashBoardActivity;
import io.cordova.myapp00d753.activity.honnasa.adapter.HanasaSalesReportAdapter;
import io.cordova.myapp00d753.activity.honnasa.model.ReportModel;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

public class HanasaSalesReportActivity extends AppCompatActivity {
    private static final String TAG = "HanasaSalesReportActivi";
    RecyclerView rvSalesReport;
    LinearLayout llNoData,llHome,llSearchPanel,llLoading;
    Pref pref;
    ImageView imgBack;
    ArrayList<ReportModel> salesReportList;
    ArrayList<String> monthList = new ArrayList<>();
    ArrayList<String> financialYearList = new ArrayList<>();
    Spinner spYear,spMonth;
    String selectedYear,selectedMonth;
    Button btnSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);

        setContentView(R.layout.activity_hanasa_sales_report);

        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/

        initView();
        btnClick();

    }



    private void initView() {
        pref = new Pref(this);
        rvSalesReport = findViewById(R.id.rvSalesReport);
        rvSalesReport.setLayoutManager(new LinearLayoutManager(this));
        llNoData = findViewById(R.id.llNoData);
        llHome = findViewById(R.id.llHome);
        llSearchPanel = findViewById(R.id.llSearchPanel);
        llLoading = findViewById(R.id.llLoading);
        imgBack = findViewById(R.id.imgBack);
        spYear = findViewById(R.id.spYear);
        spMonth = findViewById(R.id.spMonth);
        btnSearch = findViewById(R.id.btnSearch);

        monthList.add("January");
        monthList.add("February");
        monthList.add("March");
        monthList.add("April");
        monthList.add("May");
        monthList.add("June");
        monthList.add("July");
        monthList.add("August");
        monthList.add("September");
        monthList.add("October");
        monthList.add("November");
        monthList.add("December");

        financialYearList.add("2024-2025");

        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH); // January is 0, December is 11

        ArrayAdapter arrayAdapter = new ArrayAdapter(HanasaSalesReportActivity.this, android.R.layout.simple_spinner_item, financialYearList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spYear.setAdapter(arrayAdapter);

        ArrayAdapter arrayAdapter1 = new ArrayAdapter(HanasaSalesReportActivity.this, android.R.layout.simple_spinner_item, monthList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMonth.setAdapter(arrayAdapter1);

        spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedYear = financialYearList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spMonth.setSelection(month);
        spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedMonth = monthList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void btnClick() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        llHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HanasaSalesReportActivity.this, EmployeeDashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("DDL_Type", pref.getMasterId());
                    jsonObject.put("Operation", "2");
                    getReport(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getReport(JSONObject jsonObject) {
        llSearchPanel.setVisibility(View.GONE);
        llLoading.setVisibility(View.VISIBLE);
        Log.e(TAG, "getReport: "+selectedYear+" "+selectedMonth);
        AndroidNetworking.post(AppData.newv2url+"Honasa/ReportSales")
                .addJSONObjectBody(jsonObject)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "REPORT_SALES: "+response.toString());
                        llLoading.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = response;
                            String Response_Message = jsonObject.optString("Response_Message");
                            String Response_Code = jsonObject.optString("Response_Code");

                            if (Response_Code.equals("101")) {
                                salesReportList = new ArrayList<>();
                                String responseData = jsonObject.optString("Response_Data");
                                llNoData.setVisibility(View.GONE);
                                rvSalesReport.setVisibility(View.VISIBLE);
                                JSONArray jsonArray = new JSONArray(responseData);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    Log.e(TAG, "onResponse: "+i);
                                    if (selectedYear.equals(obj.optString("FinancialYear")) && selectedMonth.equals(obj.optString("Month"))){
                                        salesReportList.add(new ReportModel(
                                                obj.optString("AEMEmployeeID"),
                                                obj.optInt("AEMStoreID"),
                                                obj.optInt("AEMProductID"),
                                                obj.optString("Name"),
                                                obj.optInt("instock"),
                                                obj.optInt("closingstock"),
                                                obj.optString("ReceiveDate"),
                                                obj.optString("FinancialYear"),
                                                obj.optString("Month"),
                                                obj.optInt("ReceiveStock"),
                                                obj.optInt("SalesStock"),
                                                obj.optDouble("StockValue"),
                                                obj.optInt("TransID")
                                        ));
                                    }
                                }
                                HanasaSalesReportAdapter hanasaSalesReportAdapter = new HanasaSalesReportAdapter(HanasaSalesReportActivity.this,salesReportList);
                                rvSalesReport.setAdapter(hanasaSalesReportAdapter);

                                if (salesReportList.size() > 0){
                                    llSearchPanel.setVisibility(View.GONE);
                                    llLoading.setVisibility(View.GONE);
                                    llNoData.setVisibility(View.GONE);
                                    rvSalesReport.setVisibility(View.VISIBLE);
                                } else {
                                    llSearchPanel.setVisibility(View.GONE);
                                    llLoading.setVisibility(View.GONE);
                                    llNoData.setVisibility(View.VISIBLE);
                                    rvSalesReport.setVisibility(View.GONE);
                                }


                            } else {
                                llNoData.setVisibility(View.VISIBLE);
                                rvSalesReport.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "REPORT_SALES_onError: "+anError);
                        llSearchPanel.setVisibility(View.GONE);
                        llLoading.setVisibility(View.GONE);
                        llNoData.setVisibility(View.VISIBLE);
                        rvSalesReport.setVisibility(View.GONE);
                    }
                });
    }


}