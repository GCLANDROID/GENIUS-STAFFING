package io.cordova.myapp00d753.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.databinding.ActivityIncomeTaxDashboardBinding;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

public class IncomeTaxDashboardActivity extends AppCompatActivity {
    ActivityIncomeTaxDashboardBinding binding;
    Pref pref;
    String domain;
    String financialYear, year, month;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_income_tax_dashboard);
        initView();
    }

    private void initView(){
        pref=new Pref(IncomeTaxDashboardActivity.this);
        if (pref.getSecurityCode().equals("0000")) {
            domain = "FSS";
        } else if (pref.getSecurityCode().equals("222")) {
            domain = "FMS";
        } else if (pref.getSecurityCode().equals("888")) {

            domain = "ITS";
        } else if (pref.getSecurityCode().equals("333")) {

            domain = "SEC";
        } else if (pref.getSecurityCode().equals("444")) {

            domain = "NAPS";
        }  else if (pref.getSecurityCode().equals("666")) {

            domain = "MSP";
        }

        int y = Calendar.getInstance().get(Calendar.YEAR);
        year = String.valueOf(y);
        Log.d("year", year);


        int m = Calendar.getInstance().get(Calendar.MONTH) + 1;
        Log.d("month", String.valueOf(m));
        if (m == 1) {
            month = "January";
        } else if (m == 2) {
            month = "February";
        } else if (m == 3) {
            month = "March";
        } else if (m == 4) {
            month = "April";
        } else if (m == 5) {
            month = "May";
        } else if (m == 6) {
            month = "June";
        } else if (m == 7) {
            month = "July";
        } else if (m == 8) {
            month = "August";
        } else if (m == 9) {
            month = "September";
        } else if (m == 10) {
            month = "October";
        } else if (m == 11) {
            month = "November";
        } else if (m == 12) {
            month = "December";
        }
        if (month.equals("January")) {
            int futureyear = y - 1;
            financialYear = futureyear + "-" + year;
        } else if (month.equals("February")) {
            int futureyear = y - 1;
            financialYear = futureyear + "-" + year;
        } else if (month.equals("March")) {
            int futureyear = y - 1;
            financialYear = futureyear + "-" + year;
        } else {
            int futureyear = y + 1;
            financialYear = year + "-" + futureyear;
        }

        String itDeclarationUrl="https://gsppi.geniusconsultant.com/TAXnxt/Views/CDR/?domain="+domain+"&consultantid="+pref.getEmpConId()+"&employeeid="+pref.getMasterId()+"&fiscalyear="+financialYear+"&requestfor=ITDeclaration&userid="+pref.getMasterId();

        String itviewUrl="https://gsppi.geniusconsultant.com/TAXnxt/Views/CDR/?domain="+domain+"&consultantid="+pref.getEmpConId()+"&employeeid="+pref.getMasterId()+"&fiscalyear="+financialYear+"&requestfor=ITview&userid="+pref.getMasterId();

        binding.llFormSixteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(IncomeTaxDashboardActivity.this,FormSixteenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        binding.llITDeclaration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(IncomeTaxDashboardActivity.this,ITViewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("url",itDeclarationUrl);
                startActivity(intent);
            }
        });

        binding.llITView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(IncomeTaxDashboardActivity.this,ITViewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("url",itviewUrl);
                startActivity(intent);
            }
        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.llITForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTDSForm();
            }
        });
    }


    public void getTDSForm() {

        String surl = AppData.IT_FORM_DOWNLAOD;
        Log.d("inputLogin", surl);
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        progressBar.dismiss();
                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");

                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                                JSONObject responseData = job1.optJSONObject("Response_Data");
                                String FileUrl= responseData.optString("FileUrl");
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(FileUrl));
                                startActivity(intent);




                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(LoginActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();

                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(IncomeTaxDashboardActivity.this);
        requestQueue.add(stringRequest);

    }
}