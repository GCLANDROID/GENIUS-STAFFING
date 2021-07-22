package io.cordova.myapp00d753.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
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


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.AttandanceManageAdapter;
import io.cordova.myapp00d753.module.SupAttendenceManageModule;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;
import io.cordova.myapp00d753.utility.Pref;

public class SupAttenManageActivity extends AppCompatActivity {
    RecyclerView rvAttendanceManage;
    AttandanceManageAdapter madapter;

    ArrayList<SupAttendenceManageModule> attedanceList = new ArrayList<>();
    Pref pref;
    LinearLayout llSubmit;
    ArrayList<String> item = new ArrayList<>();
    ImageView imgBack, imgHome;
    LinearLayout llSearch;
    AlertDialog alerDialog1;
    String newdate;
    ;
    ProgressBar progressBar;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    public static int mPageCount = 0;
    boolean mIsEndReached = false;
    private boolean loading = false;
    LinearLayoutManager layoutManager;
    LinearLayout llLoder;
    LinearLayout llMain;
    String formattedDate;
    String comma = "";
    String d;
    String empId;
    NetworkConnectionCheck connectionCheck;
    String year;
    int y;
    String month;
    LinearLayout llAgain;
    ImageView imgAgain;
    ImageView imgSearch;
    LinearLayout llClick,llSelect;
    int allclick;
    ArrayList<String>item1=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sup_atten_manage);
        mPageCount = 1;
        initialize();

        onClick();


    }

    private void initialize() {
        pref = new Pref(this);
        connectionCheck = new NetworkConnectionCheck(SupAttenManageActivity.this);

        rvAttendanceManage = (RecyclerView) findViewById(R.id.rvAttenDanceManage);
        layoutManager
                = new LinearLayoutManager(SupAttenManageActivity.this, LinearLayoutManager.VERTICAL, false);
        rvAttendanceManage.setLayoutManager(layoutManager);
        llSubmit = (LinearLayout) findViewById(R.id.llSubmit);
        llSelect=(LinearLayout)findViewById(R.id.llSelect);
        llClick=(LinearLayout)findViewById(R.id.llClick);


        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);

        llSearch = (LinearLayout) findViewById(R.id.llSearch);
        llMain = (LinearLayout) findViewById(R.id.llMain);
        llLoder = (LinearLayout) findViewById(R.id.llWLLoader);
        progressBar = (ProgressBar) findViewById(R.id.WLpagination_loader);
        Date c = Calendar.getInstance().getTime();
        //System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        formattedDate = df.format(c);
        Log.d("formattedDate", formattedDate);
        if (connectionCheck.isNetworkAvailable()) {
            getAttendanceList(formattedDate);
        } else {
            connectionCheck.getNetworkActiveAlert().show();
        }
        rvAttendanceManage.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                    if (!loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = true;

                            progressBar.setVisibility(View.VISIBLE);
                            if (!mIsEndReached) {
                                mPageCount = mPageCount + 1;
                                getAttendanceList(formattedDate);
                            }

                        }
                    }
                }
            }
        });
        setAdapter();

        y = Calendar.getInstance().get(Calendar.YEAR);
        year = String.valueOf(y);
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
        imgSearch=(ImageView)findViewById(R.id.imgSearch);



    }

    private void getAttendanceList(String date) {
        llLoder.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);

        String surl = AppData.url+"gcl_AttendanceManageByAdmin?AEMConsultantID=0&AEMClientID=" + pref.getEmpClintId() + "&AEMClientOfficeID=0&AEMEmployeeID=" + pref.getEmpId() + "&UserType=" + pref.getEmpId() + "&CurrentPage=" + mPageCount + "&AttendanceID=0&AttendanceDate=" + date + "&ApproverStatus=0&Remarks=0&WorkingStatus=1&YearVal=" + year + "&MonthName=" + month + "&SecurityCode=" + pref.getSecurityCode() + "&DbOperation=1";
        Log.d("input", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        loading = false;
                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            //    Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String AttendanceDate = obj.optString("AttendanceDate");
                                    String AEMEmployeeID = obj.optString("AEMEmployeeID");
                                    String Name = obj.optString("Name");
                                    item1.add(AEMEmployeeID);
                                    String PlaceOfPostingCity = obj.optString("PlaceOfPostingCity");
                                    SupAttendenceManageModule mModule = new SupAttendenceManageModule(AEMEmployeeID, Name, PlaceOfPostingCity, AttendanceDate);
                                    attedanceList.add(mModule);

                                }
                                madapter.notifyDataSetChanged();
                                llLoder.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);



                            } else {
                                madapter.notifyDataSetChanged();
                                llLoder.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);

                                Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SupAttenManageActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoder.setVisibility(View.GONE);
                llMain.setVisibility(View.GONE);


              //  Toast.makeText(SupAttenManageActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
    }

    private void setAdapter() {
        madapter = new AttandanceManageAdapter(attedanceList, SupAttenManageActivity.this);
        rvAttendanceManage.setAdapter(madapter);
    }

    public void updateAttendanceStatus(int position, boolean status) {
        attedanceList.get(position).setSelected(status);
        if (attedanceList.get(position).isSelected()==true) {
            item.add(attedanceList.get(position).getEmoId());
        }else {
            item.clear();
        }
        Log.d("arpan", item.toString());





        madapter.notifyDataSetChanged();
    }


    private void onClick() {

        llClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llSelect.getVisibility()==View.GONE){
                    llSelect.setVisibility(View.VISIBLE);
                    allclick=1;
                    madapter.selectAll();
                }else {
                    llSelect.setVisibility(View.GONE);
                    madapter.unselectall();
                    allclick=0;
                    item1.clear();
                }
            }
        });

        llSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("manageid", pref.getManageId());
                if (item.size()>0||item1.size()>0) {
                    postAttendencereport(formattedDate);
                }else {
                    Toast.makeText(SupAttenManageActivity.this,"Please select atleast one employee",Toast.LENGTH_LONG).show();
                }
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SupAttenManageActivity.this, SuperVisiorDashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
               // finish();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchDialog();
            }
        });
    }


    private void searchDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SupAttenManageActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_attendancemanage, null);
        dialogBuilder.setView(dialogView);
        final TextView tvdate = (TextView) dialogView.findViewById(R.id.tvDate);
        tvdate.setText(formattedDate);
        LinearLayout llDate = (LinearLayout) dialogView.findViewById(R.id.llDate);
        llDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                c.add(Calendar.DAY_OF_MONTH, -7);


                final DatePickerDialog dialog = new DatePickerDialog(SupAttenManageActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {

                        newdate = (m + 1) + "/" + d + "/" + y;


                        tvdate.setText(newdate);

                    }
                }, year, month, day);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();

            }
        });
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                mPageCount = 1;
                attedanceList.clear();
                getAttendanceList(newdate);


            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }

    private void postAttendencereport(final String date) {

        if (allclick==1){
            empId=item1.toString().replace("[","").replace("]","").replaceAll("\\s+", "");
        }else {
            empId = item.toString().replace("[","").replace("]","").replaceAll("\\s+", "");
        }
        Log.d("riku", "riku");
        String surl = AppData.url+"gcl_AttendanceManageByAdmin?AEMConsultantID=0&AEMClientID=" + pref.getEmpClintId() + "&AEMClientOfficeID=0&AEMEmployeeID=" + empId + "&UserType=" + pref.getEmpId() + "&CurrentPage=1&AttendanceID=0&AttendanceDate=" + date + "&ApproverStatus=1&Remarks=0&WorkingStatus=1&YearVal=" + year + "&MonthName=" + month + "&SecurityCode=" + pref.getSecurityCode() + "&DbOperation=3 ";
        Log.d("postatten", surl);
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
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
                            if (responseStatus) {
                                Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();
                                mPageCount = 1;
                                attedanceList.clear();
                                successAlert("Attendance has been submitted successfully");


                            }


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SupAttenManageActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                Toast.makeText(SupAttenManageActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");


    }

    private void successAlert(String text) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SupAttenManageActivity.this, R.style.CustomDialogNew);
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


                Intent intent=new Intent(SupAttenManageActivity.this,SupAttendanceActivity.class);
                startActivity(intent);
                finish();
                llSelect.setVisibility(View.GONE);



            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(false);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }


}

