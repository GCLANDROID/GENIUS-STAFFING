package io.cordova.myapp00d753.fragment;

import static io.cordova.myapp00d753.activity.EmployeeDashBoardActivity.getDaysDifference;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.ClaimDeletActivity;
import io.cordova.myapp00d753.activity.ClaimReportActivity;
import io.cordova.myapp00d753.activity.DocumentActivity;
import io.cordova.myapp00d753.activity.DocumentNumberActivity;
import io.cordova.myapp00d753.activity.DocumentReportActivity;
import io.cordova.myapp00d753.activity.EmployeeDashBoardActivity;
import io.cordova.myapp00d753.activity.FMSNewClaimActivity;
import io.cordova.myapp00d753.activity.FormSixteenActivity;
import io.cordova.myapp00d753.activity.ITViewActivity;
import io.cordova.myapp00d753.activity.IncomeTaxDashboardActivity;
import io.cordova.myapp00d753.activity.LoginActivity;
import io.cordova.myapp00d753.activity.NewClaimActivity;
import io.cordova.myapp00d753.activity.OthersPayoutActivity;
import io.cordova.myapp00d753.activity.PFDashBoardActivity;
import io.cordova.myapp00d753.activity.PFManualActivity;
import io.cordova.myapp00d753.activity.PayrollActivity;
import io.cordova.myapp00d753.activity.RecktitRemActivity;
import io.cordova.myapp00d753.activity.RemDashBoardActivity;
import io.cordova.myapp00d753.activity.RemManageDashBoardActivity;
import io.cordova.myapp00d753.activity.SalaryActivity;
import io.cordova.myapp00d753.activity.TempDashBoardActivity;
import io.cordova.myapp00d753.activity.metso.MetsoNewReimbursementClaimActivity;
import io.cordova.myapp00d753.activity.metso.MetsoReimbursementDeleteActivity;
import io.cordova.myapp00d753.activity.metso.MetsoReimbursementReportActivity;
import io.cordova.myapp00d753.adapter.MenuItemAdapter;
import io.cordova.myapp00d753.adapter.NewMenuAdapter;
import io.cordova.myapp00d753.adapter.NotiAdapter;
import io.cordova.myapp00d753.adapter.NotificationModel;
import io.cordova.myapp00d753.adapter.PFDocumentAdapter;
import io.cordova.myapp00d753.module.MenuItemModel;
import io.cordova.myapp00d753.module.PFDocumentModule;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.ClientID;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.ShowDialog;
import io.cordova.myapp00d753.utility.Util;


public class NewMenuFragment extends Fragment {
    private static final String TAG = "NewMenuFragment";
    View v;
    RecyclerView rvMenu;
    ArrayList<MenuItemModel> itemList=new ArrayList<>();
    Pref pref;
    ArrayList<String>menuID=new ArrayList<>();
    int leaveFlag;
    String responseCode,PFLink;
    String isLiveStatus_LeaveApplication="";
    NotiAdapter notiAdapter;
    ArrayList<NotificationModel> contentList;
    RecyclerView rvNotification;
    String domain;
    String financialYear, year, month;
    String itviewUrl;
    String itDeclarationUrl;
    android.app.AlertDialog selfresignDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_new_menu, container, false);
        initView();
        return v;
    }

    private void initView() {
        pref = new Pref(getActivity());
        rvMenu = v.findViewById(R.id.rvMenu);
        rvMenu.setLayoutManager(new GridLayoutManager(getActivity(),3));
        rvNotification = v.findViewById(R.id.rvNotification);
        rvNotification.setLayoutManager(new LinearLayoutManager(getActivity()));

        /*itemList.add(new MenuItemModel("Profile","1"));
        itemList.add(new MenuItemModel("KYC Document","2"));
        itemList.add(new MenuItemModel("Attendance","3"));
        itemList.add(new MenuItemModel("Leave Management","4"));
        itemList.add(new MenuItemModel("Income Tax","5"));
        itemList.add(new MenuItemModel("Payroll","6"));
        itemList.add(new MenuItemModel("Reimbursement","7"));
        itemList.add(new MenuItemModel("PF","8"));
        itemList.add(new MenuItemModel("Feedback","9"));
        itemList.add(new MenuItemModel("Change Password","10"));
        itemList.add(new MenuItemModel("Insurance","11"));
        itemList.add(new MenuItemModel("Self Resignation","12"));
        itemList.add(new MenuItemModel("PMS","13"));
        NewMenuAdapter newMenuAdapter = new NewMenuAdapter(getActivity(),itemList);
        rvMenu.setAdapter(newMenuAdapter);*/
        getPFURL();
        JSONObject objSubmenu=new JSONObject();
        try {
            objSubmenu.put("ConsultantID", pref.getEmpConId());
            objSubmenu.put("ClientID", pref.getEmpClintId());
            objSubmenu.put("EmployeeID", pref.getEmpId());
            objSubmenu.put("ModuleName", "Service Menu");
            objSubmenu.put("PunchDate", "");
            objSubmenu.put("SecurityCode", pref.getSecurityCode());
            Log.e(TAG, "objSubmenu: "+objSubmenu.toString(4));
            getSubmenu(objSubmenu);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try{
            JSONObject object=new JSONObject();
            object.put("MasterID",pref.getMasterId());
            object.put("SecurityCode",pref.getSecurityCode());
            getNotification(object);
        }catch (Exception e){
            e.printStackTrace();
        }

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

        itDeclarationUrl="https://gsppi.geniusconsultant.com/TAXnxt/Views/CDR/?domain="+domain+"&consultantid="+pref.getEmpConId()+"&employeeid="+pref.getMasterId()+"&fiscalyear="+financialYear+"&requestfor=ITDeclaration&userid="+pref.getMasterId();

        itviewUrl="https://gsppi.geniusconsultant.com/TAXnxt/Views/CDR/?domain="+domain+"&consultantid="+pref.getEmpConId()+"&employeeid="+pref.getMasterId()+"&fiscalyear="+financialYear+"&requestfor=ITview&userid="+pref.getMasterId();


    }

    public void getMenu(JSONObject jsonObject) {
        AndroidNetworking.post(AppData.MENU)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "GET_MENU: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(Response_Data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String MenuID=obj.optString("MenuID");
                                    String MenuItem=obj.optString("MenuItem");
                                    if (MenuID.equals("9") || MenuID.equals("10") || MenuID.equals("14") || MenuID.equals("15")
                                            || MenuID.equals("16") || MenuID.equals("18") || MenuID.equals("19")){
                                        continue;
                                    }
                                    menuID.add(MenuID);
                                    if (menuID.contains("12")){
                                        leaveFlag=1;
                                    }else {
                                        leaveFlag=0;
                                    }
                                    MenuItemModel itemModel=new MenuItemModel(MenuItem,MenuID);
                                    itemList.add(itemModel);
                                }
                                itemList.add(4,new MenuItemModel("Income Tax","2100"));



                                if (pref.getEmpClintId().equals("AEMCLI1910000054") || pref.getEmpClintId().equals("AEMCLI2010000067") ||pref.getEmpClintId().equals("SECCLI2110000011") ||pref.getEmpClintId().equals("SECCLI2110000012") ){
                                    itemList.add(new MenuItemModel("Survey","200"));

                                }/*else if (pref.getEmpClintId().equals("AEMCLI0910000315")){
                                    itemList.add(new MenuItemModel("Interview","300"));
                                }*/
                                else if (pref.getEmpClintId().equals(ClientID.METSO)){
                                    itemList.add(new MenuItemModel("PMS","201"));
                                }else if (pref.getEmpClintId().equals(ClientID.LTFOOD) ){
                                    itemList.add(new MenuItemModel("Sales Management","4"));
                                }else {
                                    //itemList.add(new MenuItemModel("Leave Management","12"));
                                }
                                NewMenuAdapter newMenuAdapter = new NewMenuAdapter(getActivity(),itemList,leaveFlag,PFLink,isLiveStatus_LeaveApplication,NewMenuFragment.this,NewMenuFragment.this);
                                rvMenu.setAdapter(newMenuAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "GET_MENU_error: "+anError.getErrorBody());
                        if (anError.getErrorCode()==401){
                            Toast.makeText(getActivity(), "Session expired, please login", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
    }

    public void getPFURL() {
        String surl = AppData.url+"get_PFManagementTripleA?MasterID="+pref.getMasterId()+"&SecurityCode="+pref.getSecurityCode();
        Log.d("inputLogin", surl);

        final ProgressDialog pd=new ProgressDialog(getActivity());
        pd.setMessage("Loading.....");
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        pd.dismiss();
                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            responseCode=job1.optString("responseCode");

                            //getMenutem();

                            JSONObject objMenu=new JSONObject();
                            try {
                                objMenu.put("ConsultantID", pref.getEmpConId());
                                objMenu.put("ClientID",pref.getEmpClintId());
                                objMenu.put("ClientOfficeID",pref.getEmpClintOffId());
                                objMenu.put("AEMEmployeeID",pref.getEmpId());
                                objMenu.put("SecurityCode",pref.getSecurityCode());
                                getMenu(objMenu);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();


                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    PFLink = obj.optString("url");

                                }
                            }
                            // boolean _status = job1.getBoolean("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                //  Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                showAlert();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
    }

    private void showAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("something went wrong");
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                    }
                });


    }
    void getSubmenu(JSONObject objSubmenu){
        final ProgressDialog pd=new ProgressDialog(getActivity());
        pd.setMessage("Loading.....");
        pd.show();
        Log.e(TAG, "getSubmenu: "+objSubmenu);
        //AndroidNetworking.post("https://gsppi.geniusconsultant.com/GSPPI_API_V2/api/LAMS/GetSubServiceMenu")
        AndroidNetworking.post(AppData.LAMS_GetSubServiceMenu)
                .addJSONObjectBody(objSubmenu)
                //.addHeaders("SecurityKey", "gStbCQYjYBDCQ4fkGoQSUj7LYe8uVdZ1")
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            pd.dismiss();
                            Log.e(TAG, "SUB_MENU: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            JSONObject Response_Data = job1.optJSONObject("Response_Data");
                            if(Response_Code.equals("101")){
                                Log.e(TAG, "Response_Data: "+Response_Data);
                                Log.e(TAG, "ServiceMenuAccessDetails_Array: "+Response_Data.optJSONArray("ServiceMenuAccessDetails"));
                                JSONArray ServiceMenuAccessDetails_Array = Response_Data.optJSONArray("ServiceMenuAccessDetails");
                                JSONObject ServiceMenuAccessDetails_OBJ = ServiceMenuAccessDetails_Array.getJSONObject(0);

                                if (ServiceMenuAccessDetails_OBJ.has("LeaveApplicationAccessFromMobile")){
                                    String LeaveApplicationAccessFromMobile = ServiceMenuAccessDetails_OBJ.optString("LeaveApplicationAccessFromMobile");
                                    if (!LeaveApplicationAccessFromMobile.isEmpty() || !LeaveApplicationAccessFromMobile.equals("null")){
                                        String[] LeaveApplicationAccessArray = LeaveApplicationAccessFromMobile.split("_");
                                        String isRequired_LeaveApplicationAccess = LeaveApplicationAccessArray[0];
                                        isLiveStatus_LeaveApplication = LeaveApplicationAccessArray[1];
                                        Log.e(TAG, "isLiveStatus_LeaveApplication: "+isLiveStatus_LeaveApplication);
                                    }
                                } else {

                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        pd.dismiss();
                        Log.e(TAG, "SUB_MENU_error: "+anError.getErrorBody());
                    }
                });
    }

    public void getNotification(JSONObject jsonObject) {
        AndroidNetworking.post(AppData.GetPFNotificationAPI)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "GET_PF_NOTIFICATION: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            if (Response_Code.equals("101")) {
                                JSONObject Response_Data = job1.optJSONObject("Response_Data");
                                JSONArray Content=Response_Data.optJSONArray("Content");
                                JSONObject contentobj=Content.optJSONObject(0);
                                String sContent=contentobj.optString("Content");
                                //tvNotifcation.setText("* "+sContent);
                                contentList = new ArrayList<>();
                                if (Content.length() > 0){
                                    for (int i = 0; i < Content.length(); i++) {
                                        JSONObject conOBJ=Content.optJSONObject(i);
                                        //contentList.add(conOBJ.optString("Content"));
                                        contentList.add(new NotificationModel(conOBJ.optString("Content"),conOBJ.optString("C_Url")));
                                    }
                                    //tvNotifcation.setText(contentList.toString().replace("[","").replace("]","").replaceAll(",","\n\n"));
                                }


                                JSONArray Document=Response_Data.optJSONArray("Document");
                                /*if (Document.length()>0){
                                    llPfDocument.setVisibility(View.VISIBLE);
                                    for (int i=0;i<Document.length();i++){
                                        JSONObject docOBJ=Document.optJSONObject(i);
                                        String Doc_Info=docOBJ.optString("Doc_Info");
                                        String Doc_Url=docOBJ.optString("Doc_Url");
                                        PFDocumentModule pfmodule=new PFDocumentModule();
                                        pfmodule.setDoc_Info(Doc_Info);
                                        pfmodule.setDoc_Url(Doc_Url);
                                        docList.add(pfmodule);
                                    }

                                    PFDocumentAdapter docAdapter=new PFDocumentAdapter(docList,EmployeeDashBoardActivity.this);
                                    rvPFDocument.setAdapter(docAdapter);

                                    notiAdapter = new NotiAdapter(EmployeeDashBoardActivity.this, contentList);
                                    rvNotification.setAdapter(notiAdapter);
                                }else {
                                    llPfDocument.setVisibility(View.GONE);
                                }*/
                                notiAdapter = new NotiAdapter(getActivity(), contentList);
                                rvNotification.setAdapter(notiAdapter);

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "GET_MENU_error: "+anError.getErrorBody());
                        if (anError.getErrorCode()==401){
                            Toast.makeText(getActivity(), "Session expired, please login", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
    }

    public void openKYCDocumentPopUp(){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(),R.style.TransparentDialog);
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_pop_up_layout, null);
        bottomSheetDialog.setContentView(view);
        TextView txtHeader = bottomSheetDialog.findViewById(R.id.txtPopUpHeader);
        txtHeader.setText("KYC Document");
        bottomSheetDialog.show();
        LinearLayout llIncomeTax = bottomSheetDialog.findViewById(R.id.llIncomeTax);
        LinearLayout llReimbursement = bottomSheetDialog.findViewById(R.id.llReimbursement);
        LinearLayout llKYC_Doc = bottomSheetDialog.findViewById(R.id.llKYC_Doc);
        ConstraintLayout clDocumentManage = bottomSheetDialog.findViewById(R.id.clDocumentManage);
        ConstraintLayout clDocumentReport = bottomSheetDialog.findViewById(R.id.clDocumentReport);
        ImageView imgClose = bottomSheetDialog.findViewById(R.id.imgClose);
        llIncomeTax.setVisibility(View.GONE);
        llReimbursement.setVisibility(View.GONE);
        llKYC_Doc.setVisibility(View.VISIBLE);
        clDocumentManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                Intent intent = new Intent(getActivity(), DocumentNumberActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        clDocumentReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                Intent intent = new Intent(getActivity(), DocumentReportActivity.class);
                intent.putExtra("status","All");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
    }

   public void openIncomeTaxPopUp(){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(),R.style.TransparentDialog);
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_pop_up_layout, null);
        bottomSheetDialog.setContentView(view);
        TextView txtHeader = bottomSheetDialog.findViewById(R.id.txtPopUpHeader);
        txtHeader.setText("Income Tax");
        bottomSheetDialog.show();
        LinearLayout llIncomeTax = bottomSheetDialog.findViewById(R.id.llIncomeTax);
        LinearLayout llReimbursement = bottomSheetDialog.findViewById(R.id.llReimbursement);
        LinearLayout llKYC_Doc = bottomSheetDialog.findViewById(R.id.llKYC_Doc);
        ConstraintLayout clFrom16 = bottomSheetDialog.findViewById(R.id.clFrom16);
        ConstraintLayout clIT_View = bottomSheetDialog.findViewById(R.id.clIT_View);
        ConstraintLayout clIT_Submit = bottomSheetDialog.findViewById(R.id.clIT_Submit);
        ConstraintLayout clIT_From = bottomSheetDialog.findViewById(R.id.clIT_From);
       ImageView imgClose = bottomSheetDialog.findViewById(R.id.imgClose);
        llIncomeTax.setVisibility(View.VISIBLE);
        llReimbursement.setVisibility(View.GONE);
        llKYC_Doc.setVisibility(View.GONE);
        clFrom16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                Intent intent=new Intent(getActivity(), FormSixteenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        clIT_View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                Intent intent=new Intent(getActivity(),ITViewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("url",itviewUrl);
                startActivity(intent);
            }
        });

        clIT_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),ITViewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("url",itDeclarationUrl);
                startActivity(intent);
            }
        });

        clIT_From.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTDSForm();
            }
        });
       imgClose.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               bottomSheetDialog.dismiss();
           }
       });
    }

    public void openReimbursementPopUp(){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(),R.style.TransparentDialog);
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_pop_up_layout, null);
        bottomSheetDialog.setContentView(view);
        TextView txtHeader = bottomSheetDialog.findViewById(R.id.txtPopUpHeader);
        txtHeader.setText("Reimbursement");
        bottomSheetDialog.show();
        LinearLayout llIncomeTax = bottomSheetDialog.findViewById(R.id.llIncomeTax);
        LinearLayout llReimbursement = bottomSheetDialog.findViewById(R.id.llReimbursement);
        LinearLayout llKYC_Doc = bottomSheetDialog.findViewById(R.id.llKYC_Doc);
        ConstraintLayout clReimApp = bottomSheetDialog.findViewById(R.id.clReimApp);
        ConstraintLayout clReimDelete = bottomSheetDialog.findViewById(R.id.clReimDelete);
        ConstraintLayout clReimReport = bottomSheetDialog.findViewById(R.id.clReimReport);
        ImageView imgClose = bottomSheetDialog.findViewById(R.id.imgClose);
        llIncomeTax.setVisibility(View.GONE);
        llReimbursement.setVisibility(View.VISIBLE);
        llKYC_Doc.setVisibility(View.GONE);
        clReimApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pref.getEmpClintId().equals("AEMCLI1110000501")) {
                    Intent intent = new Intent(getActivity(), RecktitRemActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (pref.getEmpClintId().equals(ClientID.METSO)) {
                    Intent intent = new Intent(getActivity(), MetsoNewReimbursementClaimActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (pref.getSecurityCode().equals("222")) {
                    Intent intent = new Intent(getActivity(), FMSNewClaimActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), NewClaimActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
        clReimDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pref.getEmpClintId().equals(ClientID.METSO)) {
                    Intent intent = new Intent(getActivity(), MetsoReimbursementDeleteActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), ClaimDeletActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
        clReimReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pref.getEmpClintId().equals(ClientID.METSO)){
                    Intent intent=new Intent(getActivity(), MetsoReimbursementReportActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Intent intent=new Intent(getActivity(), ClaimReportActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
    }

    public void openPayrollPopUp(){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(),R.style.TransparentDialog);
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_pop_up_layout, null);
        bottomSheetDialog.setContentView(view);
        TextView txtHeader = bottomSheetDialog.findViewById(R.id.txtPopUpHeader);
        txtHeader.setText("Payroll");
        bottomSheetDialog.show();
        LinearLayout llIncomeTax = bottomSheetDialog.findViewById(R.id.llIncomeTax);
        LinearLayout llReimbursement = bottomSheetDialog.findViewById(R.id.llReimbursement);
        LinearLayout llKYC_Doc = bottomSheetDialog.findViewById(R.id.llKYC_Doc);
        LinearLayout llPayroll = bottomSheetDialog.findViewById(R.id.llPayroll);
        ConstraintLayout clMonthlySalary = bottomSheetDialog.findViewById(R.id.clMonthlySalary);
        ConstraintLayout clCTC = bottomSheetDialog.findViewById(R.id.clCTC);
        ConstraintLayout clOtherPayout = bottomSheetDialog.findViewById(R.id.clOtherPayout);
        ImageView imgClose = bottomSheetDialog.findViewById(R.id.imgClose);
        llIncomeTax.setVisibility(View.GONE);
        llReimbursement.setVisibility(View.GONE);
        llKYC_Doc.setVisibility(View.GONE);
        llPayroll.setVisibility(View.VISIBLE);
        clMonthlySalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), SalaryActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        clCTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBrowser();
            }
        });
        clOtherPayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), OthersPayoutActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
    }

    public void openPF_Popup(){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(),R.style.TransparentDialog);
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_pop_up_layout, null);
        bottomSheetDialog.setContentView(view);
        TextView txtHeader = bottomSheetDialog.findViewById(R.id.txtPopUpHeader);
        txtHeader.setText("PF");
        bottomSheetDialog.show();
        LinearLayout llIncomeTax = bottomSheetDialog.findViewById(R.id.llIncomeTax);
        LinearLayout llReimbursement = bottomSheetDialog.findViewById(R.id.llReimbursement);
        LinearLayout llKYC_Doc = bottomSheetDialog.findViewById(R.id.llKYC_Doc);
        LinearLayout llPayroll = bottomSheetDialog.findViewById(R.id.llPayroll);
        LinearLayout llPF = bottomSheetDialog.findViewById(R.id.llPF);
        ConstraintLayout clPFClaimManual = bottomSheetDialog.findViewById(R.id.clPFClaimManual);
        ImageView imgClose = bottomSheetDialog.findViewById(R.id.imgClose);
        llIncomeTax.setVisibility(View.GONE);
        llReimbursement.setVisibility(View.GONE);
        llKYC_Doc.setVisibility(View.GONE);
        llPayroll.setVisibility(View.GONE);
        llPF.setVisibility(View.VISIBLE);
        clPFClaimManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                Intent intent=new Intent(getActivity(), PFManualActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
    }

    private void openBrowser(){
        Uri uri = Uri.parse(pref.getCTCURL()); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void getTDSForm() {
        String surl = AppData.IT_FORM_DOWNLAOD;
        Log.d("inputLogin", surl);
        final ProgressDialog progressBar = new ProgressDialog(getActivity());
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }
    private void resignationSubmit(String remarks,String lastDate) {
        ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Uploading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.show();
        String masterID=pref.getMasterId();

        //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        AndroidNetworking.post(AppData.newv2url+"EmployeeExit/SelfResignation")
                .addBodyParameter("MasterID",masterID)
                .addBodyParameter("DBOperation","3")
                .addBodyParameter("LastWorkingDate",lastDate)
                .addBodyParameter("EmpRemarks",remarks)
                .addBodyParameter("SecurityCode", pref.getSecurityCode())
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        progressDialog.show();


                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        selfresignDialog.dismiss();


                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);



                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101 || Response_Code==100) {
                            ShowDialog.showSuccessDialog(getActivity(), "Resignation letter has been submitted successfully", new ShowDialog.ResultListener() {
                                @Override
                                public void onSuccess() {

                                }
                            });


                            Toast.makeText(getActivity(),"Resignation letter has been submitted successfully",Toast.LENGTH_LONG).show();


                        }else {
                            Toast.makeText(getActivity(),"Error Occured Please contact with Administration",Toast.LENGTH_LONG).show();

                        }


                        // boolean _status = job1.getBoolean("status");



                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e("errt", String.valueOf(error));
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Something went wrong,Please try again", Toast.LENGTH_LONG).show();
                    }
                });

    }

    public void resignationget() {
        ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Uploading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.show();
        String masterID=pref.getMasterId();

        //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        AndroidNetworking.post(AppData.newv2url+"EmployeeExit/SelfResignation")
                .addBodyParameter("MasterID",masterID)
                .addBodyParameter("DBOperation","1")
                .addBodyParameter("SecurityCode", pref.getSecurityCode())
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        progressDialog.show();


                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();



                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);



                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101 || Response_Code==100) {
                            JSONArray Response_Data=job1.optJSONArray("Response_Data");
                            JSONObject dataobj=Response_Data.optJSONObject(0);
                            String LastWorkingDay=dataobj.optString("LastWorkingDay");
                            String EmpRemarks=dataobj.optString("EmpRemarks");
                            String ApprovalStatus=dataobj.optString("ApprovalStatus");

                            resignationReportAlert(LastWorkingDay,EmpRemarks,ApprovalStatus);
                        }else {
                            resignationAlert();
                        }


                        // boolean _status = job1.getBoolean("status");



                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e("errt", String.valueOf(error));
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Something went wrong,Please try again", Toast.LENGTH_LONG).show();
                    }
                });

    }

    public void resignationReportAlert(String lastWorkingDate,String reason,String approvalStatus) {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(getActivity(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_self_resignation_report, null);
        dialogBuilder.setView(dialogView);
        TextView tvLastDate=(TextView)dialogView.findViewById(R.id.tvLastDate);
        TextView tvApprovalStatus=(TextView)dialogView.findViewById(R.id.tvApprovalStatus);
        TextView etReason=(TextView)dialogView.findViewById(R.id.etReason);
        LinearLayout llCancel=(LinearLayout)dialogView.findViewById(R.id.llCancel);
        llCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selfresignDialog.dismiss();
            }
        });
        tvApprovalStatus.setText(approvalStatus);
        tvLastDate.setText(lastWorkingDate);
        etReason.setText(reason);




        selfresignDialog = dialogBuilder.create();
        selfresignDialog.setCancelable(false);
        Window window = selfresignDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        selfresignDialog.show();
    }

    public void resignationAlert() {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(getActivity(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_self_resignation, null);
        dialogBuilder.setView(dialogView);
        TextView tvLastDate=(TextView)dialogView.findViewById(R.id.tvLastDate);
        TextView tvNoticePeriod=(TextView)dialogView.findViewById(R.id.tvNoticePeriod);
        TextView tvSubmit=(TextView)dialogView.findViewById(R.id.tvSubmit);
        EditText etReason=(EditText)dialogView.findViewById(R.id.etReason);
        LinearLayout llCancel=(LinearLayout)dialogView.findViewById(R.id.llCancel);
        llCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selfresignDialog.dismiss();
            }
        });

        tvLastDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(tvLastDate,tvNoticePeriod);
            }
        });
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvLastDate.getText().toString().length()>0){
                    if (etReason.getText().toString().length()>0){
                        resignationSubmit(etReason.getText().toString(), Util.changeAnyDateFormat(tvLastDate.getText().toString(),"dd MMM,yyyy","yyyy-MM-dd"));


                    }else {
                        Toast.makeText(getActivity(),"Please Enter Remarks",Toast.LENGTH_LONG).show();
                    }

                }else {
                    Toast.makeText(getActivity(),"Please Select Your Last Date",Toast.LENGTH_LONG).show();
                }

            }
        });


        selfresignDialog = dialogBuilder.create();
        selfresignDialog.setCancelable(false);
        Window window = selfresignDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        selfresignDialog.show();
    }

    private void showDatePicker(TextView tv,TextView tvNoticePeriod) {
        // Get current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = (selectedMonth + 1) + "/" + selectedDay + "/" + selectedYear;
                    tv.setText(Util.changeAnyDateFormat(selectedDate,"MM/dd/yyyy","dd MMM,yyyy"));
                    int days=main(selectedDate);
                    tvNoticePeriod.setText(days+" Days");

                },
                year, month, day
        );

        // Disable past dates
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

        // Show dialog
        datePickerDialog.show();
    }

    public static int main(String selectedDate) {
        int dayscalculation;
        // Example selected date
        long daysBetween = getDaysDifference(selectedDate);
        System.out.println("Days between: " + daysBetween);
        dayscalculation= Math.toIntExact(daysBetween);
        return  dayscalculation+1;

    }


}