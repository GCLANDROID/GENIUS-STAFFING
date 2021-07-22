package io.cordova.myapp00d753.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import io.cordova.myapp00d753.adapter.SaleApprovalAdapter;
import io.cordova.myapp00d753.adapter.TotalSaleAdapter;
import io.cordova.myapp00d753.module.SaleApprovalModule;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;
import io.cordova.myapp00d753.utility.Pref;

public class TotalSaleActivity extends AppCompatActivity {
    RecyclerView rvAttenApproval;
    ArrayList<SaleApprovalModule> itemList = new ArrayList<>();

    ImageView imgBack, imgHome;
    LinearLayout llSearch;
    private AlertDialog alertDialog, alertDialog1, alertDialog2;

    ProgressBar progressBar;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    public static int mPageCount = 0;
    boolean mIsEndReached = false;
    private boolean loading = false;
    LinearLayoutManager layoutManager;
    LinearLayout llLoder;
    TotalSaleAdapter saleapprovalAdapter;
    String finacialyear ="2020-2021";
    int y;
    TextView tvYear;
    LinearLayout llMain;
    String month;
    TextView tvMonth;
    String AttendanceID;
    Pref pref;
    ArrayList<String> item = new ArrayList<>();
    ArrayList<String> item1 = new ArrayList<>();
    Button btnApprove, btnReject;
    String attId;
    NetworkConnectionCheck connectionCheck;
    LinearLayout llReply;
    LinearLayout llNodata;
    LinearLayout llAgain;
    ImageView imgAgain;
    int flag = 0;
    ImageView imgSearch;
    AlertDialog alerDialog1,alerDialog2;
    int btnFlag;
    EditText etSearch;
    LinearLayout llClick,llSelect;
    int allclick=0;
    String zoneId,branchId;
    TextView tvToolBar;
    int m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_sale);
        initialize();
        getAttendanceList();
    }

    private void initialize() {
        pref = new Pref(TotalSaleActivity.this);
        connectionCheck = new NetworkConnectionCheck(TotalSaleActivity.this);
        rvAttenApproval = (RecyclerView) findViewById(R.id.rvAttendanceApprove);
        layoutManager
                = new LinearLayoutManager(TotalSaleActivity.this, LinearLayoutManager.VERTICAL, false);
        rvAttenApproval.setLayoutManager(layoutManager);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        llSearch = (LinearLayout) findViewById(R.id.llSearch);
        llLoder = (LinearLayout) findViewById(R.id.llWLLoader);
        llMain = (LinearLayout) findViewById(R.id.llMain);
        llNodata = (LinearLayout) findViewById(R.id.llNodata);

        progressBar = (ProgressBar) findViewById(R.id.WLpagination_loader);

        y = Calendar.getInstance().get(Calendar.YEAR);

        Log.d("year", finacialyear);

        m = Calendar.getInstance().get(Calendar.MONTH) + 1;
        Log.d("month", String.valueOf(m));


        btnApprove = (Button) findViewById(R.id.btnApprove);
        btnReject = (Button) findViewById(R.id.btnReject);
        llReply = (LinearLayout) findViewById(R.id.llReply);
        llAgain = (LinearLayout) findViewById(R.id.llAgain);
        imgAgain = (ImageView) findViewById(R.id.imgAgain);
        imgAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAttendanceList();
            }
        });
        imgSearch = (ImageView) findViewById(R.id.imgSearch);
        int fu=y+1;
        String y1= String.valueOf(y);

        etSearch=(EditText)findViewById(R.id.etSearch);
        llSelect=(LinearLayout)findViewById(R.id.llSelect);
        llClick=(LinearLayout)findViewById(R.id.llClick);
        zoneId=getIntent().getStringExtra("zoneId");
        branchId=getIntent().getStringExtra("branchId");
        String zoneName=getIntent().getStringExtra("zoneName");
        tvToolBar=(TextView)findViewById(R.id.tvToolBar);
        tvToolBar.setText(zoneName);
        finacialyear="0";
        month="0";


    }

    private void getAttendanceList() {
        Log.d("Arpan", "arpan");
        llLoder.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);
        llAgain.setVisibility(View.GONE);

        String surl = AppData.url + "get_EmployeeSalesReport?ClientID="+pref.getEmpClintId()+"&UserID="+pref.getEmpId()+"&FinancialYear="+finacialyear+"&Month="+month+"&RType=0&Operation=1&SecurityCode="+pref.getSecurityCode()+"&ZoneID="+zoneId+"&BranchID="+branchId;
        Log.d("input", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //   attendabceInfiList.clear();

                        itemList.clear();
                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String _SalesDate = obj.optString("_SalesDate");
                                    String Name = obj.optString("Name");
                                    String TokenNo = obj.optString("TokenNo");
                                    String CustomerName = obj.optString("CustomerName");
                                    String ModelName = obj.optString("ModelName");
                                    String Zonename = obj.optString("Zonename");
                                    String BranchName = obj.optString("BranchName");
                                    item1.add(TokenNo);

                                    SaleApprovalModule obj2 = new SaleApprovalModule(_SalesDate,Name,TokenNo,CustomerName,ModelName);
                                    obj2.setBranch(BranchName);
                                    obj2.setZone(Zonename);
                                    itemList.add(obj2);


                                }

                                llLoder.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNodata.setVisibility(View.GONE);
                                llAgain.setVisibility(View.GONE);
                                setAdapter();
                                Log.d("token",item.toString());


                            } else {

                                llLoder.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNodata.setVisibility(View.VISIBLE);
                                llAgain.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);


                                //Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TotalSaleActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoder.setVisibility(View.GONE);
                llMain.setVisibility(View.GONE);
                llNodata.setVisibility(View.GONE);
                llAgain.setVisibility(View.VISIBLE);

                // Toast.makeText(AttenApprovalActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
    }

    private void setAdapter() {
        saleapprovalAdapter = new TotalSaleAdapter(itemList, TotalSaleActivity.this);
        rvAttenApproval.setAdapter(saleapprovalAdapter);
    }
}