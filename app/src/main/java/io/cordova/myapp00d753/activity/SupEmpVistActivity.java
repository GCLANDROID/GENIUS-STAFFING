package io.cordova.myapp00d753.activity;

import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.EmpSaleAdapter;
import io.cordova.myapp00d753.adapter.EmpVisitAdapter;
import io.cordova.myapp00d753.module.EmpSaleModel;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

public class SupEmpVistActivity extends AppCompatActivity {
    RecyclerView rvItem;
    LinearLayout llWLLoader,llMain,llNodata,llAgain;
    ArrayList<EmpSaleModel>itemList=new ArrayList<>();
    Pref pref;
    EditText etSearch;
    EmpVisitAdapter empAdapter;
    int y;
    String cuyear,month,financialYear;
    ImageView imgBack,imgHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sup_emp_sale);
        initView();
        getItemList();
        onClick();
    }

    private void initView(){
        pref=new Pref(getApplicationContext());
        rvItem=(RecyclerView)findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);

        llWLLoader=(LinearLayout)findViewById(R.id.llWLLoader);
        llMain=(LinearLayout)findViewById(R.id.llMain);
        llNodata=(LinearLayout)findViewById(R.id.llNodata);
        llAgain=(LinearLayout)findViewById(R.id.llAgain);
        etSearch=(EditText)findViewById(R.id.etSearch);

        y = Calendar.getInstance().get(Calendar.YEAR);
        cuyear = String.valueOf(y);
        Log.d("year", cuyear);

        int m = Calendar.getInstance().get(Calendar.MONTH) + 1;
        Log.d("month", String.valueOf(m));
        month=pref.getMonth();

        financialYear = pref.getFinacialYear();
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);

    }

    private void onClick(){
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SupEmpVistActivity.this,SuperVisiorDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getItemList(){
        Log.d("Arpan","arpan");
        llWLLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);
        llAgain.setVisibility(View.GONE);
        String surl = AppData.url+"get_EmployeeSalesTargetReport?ClientID="+pref.getEmpClintId()+"&UserID="+pref.getEmpId()+"&FinancialYear="+financialYear+"&Month="+month+"&RType=YTD&Operation=2&SecurityCode="+pref.getSecurityCode();
        Log.d("input",surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);

                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText=job1.optString("responseText");

                            boolean responseStatus=job1.optBoolean("responseStatus");
                            if (responseStatus){
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData=job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++){
                                    JSONObject obj=responseData.getJSONObject(i);
                                    String Name=obj.optString("Name");
                                    String AEMEmployeeID=obj.optString("AEMEmployeeID");
                                    String AchvPer=obj.optString("AchvPer");
                                    EmpSaleModel empModel=new EmpSaleModel(Name,AEMEmployeeID,AchvPer);
                                    itemList.add(empModel);
                                }

                                llWLLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.GONE);
                                llAgain.setVisibility(View.GONE);
                                setAdapter();

                            }

                            else {

                                llWLLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.VISIBLE);
                                llAgain.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llWLLoader.setVisibility(View.GONE);
                llMain.setVisibility(View.GONE);
                llNodata.setVisibility(View.GONE);
                llAgain.setVisibility(View.VISIBLE);

                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert",error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
    }

    private  void setAdapter(){
         empAdapter =new EmpVisitAdapter(itemList,getApplicationContext());
        rvItem.setAdapter(empAdapter);
    }

    void filter(String text) {
        ArrayList<EmpSaleModel> temp = new ArrayList();
        for (EmpSaleModel d : itemList) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.getEmpName().toLowerCase() .contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        //update recyclerview
        empAdapter.updateList(temp);
    }


}