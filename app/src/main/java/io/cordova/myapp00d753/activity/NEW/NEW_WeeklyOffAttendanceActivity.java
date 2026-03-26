package io.cordova.myapp00d753.activity.NEW;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.EmployeeDashBoardActivity;
import io.cordova.myapp00d753.activity.metso.MetsoNewReimbursementClaimActivity;
import io.cordova.myapp00d753.activity.metso.adapter.SupervisorFilterAdapter;
import io.cordova.myapp00d753.module.SpineerItemModel;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.ClientID;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.RequiredListClass;
import io.cordova.myapp00d753.utility.ShowDialog;

public class NEW_WeeklyOffAttendanceActivity extends AppCompatActivity {
    private static final String TAG = "WeeklyOffAttendanceActi";
    LinearLayout llDate;
    TextView tvDate;
    ImageView imgBack, imgHome;
    LinearLayout llSubmit;
    Pref pref;
    String newdate;
    private AlertDialog alertDialog, alerDialog1, al2;
    String responseText;
    NetworkConnectionCheck connectionCheck;
    TextView tvMan,tvApprover;
    String ApproverStatus;
    LinearLayout llTick,llMainApprover;
    String WOMarkingWithHierarchySelection="";
    Dialog searchWbsCodeDialog;
    String SupervisorID="";
    ArrayList<SpineerItemModel> supervisorList = new ArrayList<>();
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_weeklyoff);
        initialize();
        onclick();
    }

    private void initialize() {
        WOMarkingWithHierarchySelection = getIntent().getStringExtra("WOMarkingWithHierarchySelection");
        String[] WOMarkingWithHierarchySelectionArr = WOMarkingWithHierarchySelection.split("_");
        String isHierarchySelectionRequired = WOMarkingWithHierarchySelectionArr[0];
        progressDialog = new ProgressDialog(NEW_WeeklyOffAttendanceActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        pref = new Pref(getApplicationContext());
        searchWbsCodeDialog = new Dialog(NEW_WeeklyOffAttendanceActivity.this, R.style.CustomDialogNew2);
        connectionCheck = new NetworkConnectionCheck(this);
        llDate = (LinearLayout) findViewById(R.id.llDate);
        llMainApprover = (LinearLayout) findViewById(R.id.llMainApprover);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvApprover = (TextView) findViewById(R.id.tvApprover);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        llSubmit = (LinearLayout) findViewById(R.id.llSubmit);
        tvMan = (TextView) findViewById(R.id.tvMan);
        if (isHierarchySelectionRequired.equals("1")){
            llMainApprover.setVisibility(View.VISIBLE);
        } else {
            llMainApprover.setVisibility(View.GONE);
        }
        getRequiredList();
    }

    private void getRequiredList() {
        progressDialog.show();
        try {
            supervisorList = RequiredListClass.getApproverData(pref.getApproverList());
        } catch (Exception e){
            e.printStackTrace();
        }
        progressDialog.dismiss();
    }

    private void onclick() {
        llDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                if (!pref.getEmpClintId().equals(ClientID.HONASA)){
                    c.add(Calendar.DAY_OF_MONTH, -7);
                }

                final DatePickerDialog dialog = new DatePickerDialog(NEW_WeeklyOffAttendanceActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {


                        newdate = y + "-" + (m + 1) + "-" + d;

                        tvDate.setVisibility(View.VISIBLE);
                        tvDate.setText(newdate);

                    }
                }, year, month, day);
                if (pref.getEmpClintId().equals(ClientID.HONASA)){
                    dialog.getDatePicker().setMinDate(c.getTimeInMillis());
                } else {
                    dialog.getDatePicker();
                }
                dialog.show();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NEW_WeeklyOffAttendanceActivity.this, EmployeeDashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //finish();
            }
        });
        tvApprover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSupervisorPopUp();
            }
        });

        llSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newdate != null) {
                    if (llMainApprover.getVisibility() == View.VISIBLE && SupervisorID.isEmpty()){
                        Toast.makeText(NEW_WeeklyOffAttendanceActivity.this, "Please select approver", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (connectionCheck.isNetworkAvailable()) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("ConsultantID", pref.getEmpConId());
                            jsonObject.put("ClientID", pref.getEmpClintId());
                            jsonObject.put("EmployeeID", pref.getEmpId());
                            jsonObject.put("Type", "WO");
                            jsonObject.put("StartDate", newdate);
                            jsonObject.put("Reason", "");
                            jsonObject.put("DbOperation", "2");
                            jsonObject.put("Shiftid", "");
                            jsonObject.put("SiteId", "");
                            jsonObject.put("ApproverID", SupervisorID);
                            jsonObject.put("SecurityCode", pref.getSecurityCode());
                            saveWeeklyOffMarking(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        tvMan.setVisibility(View.GONE);
                    } else {
                        connectionCheck.getNetworkActiveAlert().show();
                    }

                } else {
                    Toast.makeText(NEW_WeeklyOffAttendanceActivity.this, "please select date", Toast.LENGTH_LONG).show();
                }


            }
        });


    }

    private void saveWeeklyOffMarking(JSONObject jsonObject) {
        ProgressDialog progressDialog = new ProgressDialog(NEW_WeeklyOffAttendanceActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        AndroidNetworking.post(AppData.LAMS_SaveHolidayWO)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(String.valueOf(response));
                            String Response_Message = object.optString("Response_Message");
                            String Response_Code = object.optString("Response_Code");
                            if (Response_Code.equals("101")) {
                                successAlert(Response_Message);
                            } else {
                                progressDialog.cancel();
                                ShowDialog.showErrorDialog(NEW_WeeklyOffAttendanceActivity.this,Response_Message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                    }
                });
    }

    private void showAlertforFalse(String text) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NEW_WeeklyOffAttendanceActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_invaliddate, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvInvalidDialog);
        if (responseText.equals("")) {
            tvInvalidDate.setText(text);
        } else {
            tvInvalidDate.setText(text);
        }
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
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


    private void successAlert(String text) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NEW_WeeklyOffAttendanceActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvSuccess = (TextView) dialogView.findViewById(R.id.tvSuccess);
        tvSuccess.setText(text);
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                Intent intent=new Intent(NEW_WeeklyOffAttendanceActivity.this,EmployeeDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }





    private void showAlertforFalse1(String text) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NEW_WeeklyOffAttendanceActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_invaliddate, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvInvalidDialog);
        tvInvalidDate.setText(text);

        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                al2.dismiss();
                llSubmit.setVisibility(View.GONE);
            }
        });

        al2 = dialogBuilder.create();
        al2.setCancelable(true);
        Window window = al2.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        al2.show();

    }

    private void openSupervisorPopUp() {
        searchWbsCodeDialog = new Dialog(NEW_WeeklyOffAttendanceActivity.this, R.style.CustomDialogNew2);
        searchWbsCodeDialog.setContentView(R.layout.wbs_code_search_layout);
        searchWbsCodeDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        searchWbsCodeDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        searchWbsCodeDialog.setCancelable(true);

        TextView txtPopupHeadline = searchWbsCodeDialog.findViewById(R.id.txtPopupHeadline);
        SearchView wbsCodeSearchView = (SearchView) searchWbsCodeDialog.findViewById(R.id.wbsCodeSearchView);
        ImageView imgCancel = searchWbsCodeDialog.findViewById(R.id.imgCancel);
        RecyclerView rvWbsCode = searchWbsCodeDialog.findViewById(R.id.rvWbsCode);

        wbsCodeSearchView.setQueryHint("Search Approver");
        txtPopupHeadline.setText("Select Approver");
        rvWbsCode.setLayoutManager(new LinearLayoutManager(NEW_WeeklyOffAttendanceActivity.this));
        ArrayList<SpineerItemModel> supervisorListCopy = new ArrayList<>();
        supervisorListCopy = (ArrayList<SpineerItemModel>) supervisorList.clone();
        SupervisorFilterAdapter supervisorFilterAdapter = new SupervisorFilterAdapter(NEW_WeeklyOffAttendanceActivity.this, supervisorListCopy);
        rvWbsCode.setAdapter(supervisorFilterAdapter);

        wbsCodeSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                supervisorFilterAdapter.getFilter().filter(s);
                return false;
            }
        });

        supervisorFilterAdapter.setSupervisorSelectListener(new MetsoNewReimbursementClaimActivity.SupervisorSelectListener() {
            @Override
            public void onClick(String supervisor_id, String supervisor) {
                SupervisorID = supervisor_id;
                tvApprover.setText(supervisor);
                searchWbsCodeDialog.dismiss();
            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchWbsCodeDialog.dismiss();
            }
        });
        searchWbsCodeDialog.show();
    }

}
