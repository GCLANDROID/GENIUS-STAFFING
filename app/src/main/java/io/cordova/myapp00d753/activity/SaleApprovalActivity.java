package io.cordova.myapp00d753.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.SaleApprovalAdapter;
import io.cordova.myapp00d753.module.SaleApprovalModule;
import io.cordova.myapp00d753.module.SaleApprovalReportModel;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;
import io.cordova.myapp00d753.utility.Pref;

public class SaleApprovalActivity extends AppCompatActivity {

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
    SaleApprovalAdapter saleapprovalAdapter;
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
        setContentView(R.layout.activity_sale_approval);
        mPageCount = 1;
        initialize();
        if (connectionCheck.isNetworkAvailable()) {
            getAttendanceList();
        } else {
            connectionCheck.getNetworkActiveAlert().show();
        }
        onClick();
    }

    private void initialize() {
        pref = new Pref(SaleApprovalActivity.this);
        connectionCheck = new NetworkConnectionCheck(SaleApprovalActivity.this);
        rvAttenApproval = (RecyclerView) findViewById(R.id.rvAttendanceApprove);
        layoutManager
                = new LinearLayoutManager(SaleApprovalActivity.this, LinearLayoutManager.VERTICAL, false);
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

        String surl = AppData.url + "get_EmployeeSalesApproval?ClientID="+pref.getEmpClintId()+"&UserID="+pref.getEmpId()+"&FinancialYear="+ finacialyear +"&Month="+month+"&TokenNo=0&Status=0&Remarks=0&Operation=1&SecurityCode="+pref.getSecurityCode()+"&ZoneID="+zoneId+"&BranchID="+branchId;
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
                            Toast.makeText(SaleApprovalActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
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
        saleapprovalAdapter = new SaleApprovalAdapter(itemList, SaleApprovalActivity.this);
        rvAttenApproval.setAdapter(saleapprovalAdapter);
    }

    public void updateAttendanceStatus(int position, boolean status) {
        item1.clear();
        itemList.get(position).setSelected(status);
        if (itemList.get(position).isSelected() == true) {
            item.add(itemList.get(position).getToken());
        } else {
            item.clear();
        }


        Log.d("arpan", item.toString());
        String i = item.toString();
        String d = i.replace("[", "").replace("]", "");


        if (item.size() > 0) {

        } else {

        }


        saleapprovalAdapter.notifyDataSetChanged();
    }


    private void onClick() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SaleApprovalActivity.this, SuperVisiorDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                finacialyear="2020-2021";
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SaleApprovalActivity.this, R.style.CustomDialogNew);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.attendancereportsearch, null);
                dialogBuilder.setView(dialogView);
                LinearLayout llYear = (LinearLayout) dialogView.findViewById(R.id.llYear);
                tvYear = (TextView) dialogView.findViewById(R.id.tvYear);
                tvMonth = (TextView) dialogView.findViewById(R.id.tvMonth);
                ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);

                llYear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showYearDialog();
                    }
                });

                tvYear.setText(finacialyear);
                LinearLayout llMonth = (LinearLayout) dialogView.findViewById(R.id.llMonth);
                llMonth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showMonthDialog();

                    }
                });
                tvMonth.setText(month);

                Button btnSubmit = (Button) dialogView.findViewById(R.id.btnSubmit);
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPageCount = 1;
                        itemList.clear();
                        flag = 0;
                        llLoder.setVisibility(View.VISIBLE);
                        llMain.setVisibility(View.GONE);
                        getAttendanceList();
                        alertDialog.dismiss();
                    }
                });
                imgCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog = dialogBuilder.create();
                alertDialog.setCancelable(true);
                Window window = alertDialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER);
                alertDialog.show();

            }
        });

        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionCheck.isNetworkAvailable()) {
                    if (item.size() > 0 ||item1.size()>0) {
                        btnFlag=1;
                        remsrksAlert();




                    } else {
                        Toast.makeText(SaleApprovalActivity.this, "Please select atleast one value", Toast.LENGTH_LONG).show();
                    }
                } else {
                    connectionCheck.getNetworkActiveAlert().show();
                }


            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.size() > 0 ||item1.size()>0) {
                   btnFlag=2;
                    remsrksAlert();



                } else {
                    Toast.makeText(SaleApprovalActivity.this, "Please select atleast one value", Toast.LENGTH_LONG).show();

                }


            }
        });

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

        llClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llSelect.getVisibility()==View.GONE){
                    llSelect.setVisibility(View.VISIBLE);
                    allclick=1;
                    saleapprovalAdapter.selectAll();
                }else {
                    llSelect.setVisibility(View.GONE);
                    saleapprovalAdapter.unselectall();
                    allclick=0;
                    item1.clear();
                }
            }
        });


    }


    private void showYearDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SaleApprovalActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_year, null);
        dialogBuilder.setView(dialogView);
        final TextView tvYear1 = (TextView) dialogView.findViewById(R.id.tvYear1);
        final TextView tvYear2 = (TextView) dialogView.findViewById(R.id.tvYear2);
        final TextView tvYear3 = (TextView) dialogView.findViewById(R.id.tvYear3);
        LinearLayout llY1 = (LinearLayout) dialogView.findViewById(R.id.llY1);
        LinearLayout llY2 = (LinearLayout) dialogView.findViewById(R.id.llY2);
        LinearLayout llY3 = (LinearLayout) dialogView.findViewById(R.id.llY3);


        tvYear1.setText("2020-2021");


        tvYear2.setText("2021-2022");


        tvYear3.setText("2022-2023");

        ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog1.dismiss();


            }
        });


        llY3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finacialyear = tvYear3.getText().toString();
                Log.d("yrtrr", finacialyear);
                tvYear.setText(finacialyear);
                alertDialog1.dismiss();

            }
        });

        llY2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finacialyear = tvYear2.getText().toString();
                alertDialog1.dismiss();
                tvYear.setText(finacialyear);
                Log.d("ttt", finacialyear);
            }
        });

        llY1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finacialyear = tvYear1.getText().toString();
                alertDialog1.dismiss();
                tvYear.setText(finacialyear);
                Log.d("ttt", finacialyear);
            }
        });

        alertDialog1 = dialogBuilder.create();
        alertDialog1.setCancelable(true);
        Window window = alertDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog1.show();
    }

    private void showMonthDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SaleApprovalActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_month, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llM1 = (LinearLayout) dialogView.findViewById(R.id.llM1);
        LinearLayout llM2 = (LinearLayout) dialogView.findViewById(R.id.llM2);
        LinearLayout llM3 = (LinearLayout) dialogView.findViewById(R.id.llM3);
        LinearLayout llM4 = (LinearLayout) dialogView.findViewById(R.id.llM4);
        LinearLayout llM5 = (LinearLayout) dialogView.findViewById(R.id.llM5);
        LinearLayout llM6 = (LinearLayout) dialogView.findViewById(R.id.llM6);
        LinearLayout llM7 = (LinearLayout) dialogView.findViewById(R.id.llM7);
        LinearLayout llM8 = (LinearLayout) dialogView.findViewById(R.id.llM8);
        LinearLayout llM9 = (LinearLayout) dialogView.findViewById(R.id.llM9);
        LinearLayout llM10 = (LinearLayout) dialogView.findViewById(R.id.llM10);
        LinearLayout llM11 = (LinearLayout) dialogView.findViewById(R.id.llM111);
        LinearLayout llM112 = (LinearLayout) dialogView.findViewById(R.id.llM12);

        final TextView tvJan = (TextView) dialogView.findViewById(R.id.tvJan);
        tvJan.setText("January");
        final TextView tvFeb = (TextView) dialogView.findViewById(R.id.tvFeb);
        final TextView tvMarch = (TextView) dialogView.findViewById(R.id.tvMarch);
        final TextView tvApril = (TextView) dialogView.findViewById(R.id.tvApril);
        final TextView tvMay = (TextView) dialogView.findViewById(R.id.tvMay);
        final TextView tvJune = (TextView) dialogView.findViewById(R.id.tvJune);
        final TextView tvJuly = (TextView) dialogView.findViewById(R.id.tvJuly);
        final TextView tvAugust = (TextView) dialogView.findViewById(R.id.tvAugust);
        final TextView tvSept = (TextView) dialogView.findViewById(R.id.tvSeptember);
        final TextView tvOct = (TextView) dialogView.findViewById(R.id.tvOct);
        final TextView tvNov = (TextView) dialogView.findViewById(R.id.tvNovember);
        final TextView tvDec = (TextView) dialogView.findViewById(R.id.tvDecember);

        llM1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvJan.getText().toString();
                Log.d("monnn", month);
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvFeb.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });

        llM3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvMarch.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvApril.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvMay.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });

        llM6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvJune.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvJuly.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvAugust.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvSept.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvOct.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvNov.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM112.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvDec.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog2.dismiss();
            }
        });


        alertDialog2 = dialogBuilder.create();
        alertDialog2.setCancelable(true);
        Window window = alertDialog2.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog2.show();

    }

    private void acceptAttendance(String remarks) {
        if (allclick==1){
            attId=item1.toString().replace("[","").replace("]","");
        }else {
            attId=item.toString().replace("[","").replace("]","");
        }
        final ProgressDialog pd = new ProgressDialog(SaleApprovalActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();

        AndroidNetworking.upload(AppData.url+"post_EmployeeSalesApprove")
                .addMultipartParameter("ClientID", pref.getEmpClintId())
                .addMultipartParameter("UserID", pref.getEmpId())
                .addMultipartParameter("FinancialYear", "2020-2021")
                .addMultipartParameter("Month", month)
                .addMultipartParameter("TokenNo", attId)
                .addMultipartParameter("Status", "IFBSM100008")
                .addMultipartParameter("Remarks", remarks)
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())


                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        pd.show();

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {



                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        String responseText = job1.optString("responseText");
                        Log.d("responseText", responseText);
                        boolean responseStatus=job1.optBoolean("responseStatus");
                        if (responseStatus) {
                            successAlert(responseText);
                            pd.dismiss();

                        } else {
                            pd.dismiss();
                            Toast.makeText(SaleApprovalActivity.this, responseText, Toast.LENGTH_LONG).show();

                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                });


    }


    private void rejectAttendance(String remarks) {
        if (allclick==1){
            attId=item1.toString().replace("[","").replace("]","");
        }else {
            attId=item.toString().replace("[","").replace("]","");
        }
        final ProgressDialog pd = new ProgressDialog(SaleApprovalActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();

        AndroidNetworking.upload(AppData.url+"post_EmployeeSalesApprove")
                .addMultipartParameter("ClientID", pref.getEmpClintId())
                .addMultipartParameter("UserID", pref.getEmpId())
                .addMultipartParameter("FinancialYear", "2020-2021")
                .addMultipartParameter("Month", month)
                .addMultipartParameter("TokenNo", attId)
                .addMultipartParameter("Status", "IFBSM100007")
                .addMultipartParameter("Remarks", remarks)
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())


                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        pd.show();

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {



                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        String responseText = job1.optString("responseText");
                        Log.d("responseText", responseText);
                        boolean responseStatus=job1.optBoolean("responseStatus");
                        if (responseStatus) {
                            successAlert(responseText);
                            pd.dismiss();

                        } else {
                            pd.dismiss();
                            Toast.makeText(SaleApprovalActivity.this, responseText, Toast.LENGTH_LONG).show();

                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                });


    }

    private void successAlert(String text) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SaleApprovalActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);
        tvInvalidDate.setText(text);

        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                itemList.clear();
                getAttendanceList();
                llSelect.setVisibility(View.GONE);
                item.clear();
                item1.clear();
            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(false);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }

    private void remsrksAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SaleApprovalActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_remarks, null);
        dialogBuilder.setView(dialogView);
        final EditText etRemark = (EditText) dialogView.findViewById(R.id.etRemark);


        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog2.dismiss();
                if (btnFlag==1){
                    acceptAttendance(etRemark.getText().toString());
                }else {
                    rejectAttendance(etRemark.getText().toString());
                }

            }
        });

        alerDialog2 = dialogBuilder.create();
        alerDialog2.setCancelable(false);
        Window window = alerDialog2.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog2.show();
    }

    void filter(String text) {
        ArrayList<SaleApprovalModule> temp = new ArrayList();
        for (SaleApprovalModule d : itemList) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.getEmpName().toLowerCase() .contains(text.toLowerCase())||d.getBranch().toLowerCase() .contains(text.toLowerCase()))   {
                temp.add(d);
            }
        }
        //update recyclerview
        saleapprovalAdapter.updateList(temp);
    }




}
