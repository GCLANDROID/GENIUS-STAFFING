package io.cordova.myapp00d753.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
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
import io.cordova.myapp00d753.activity.SaleApprovalActivity;
import io.cordova.myapp00d753.adapter.SaleApprovalReportAdapter;
import io.cordova.myapp00d753.adapter.SaleRejectReportAdapter;
import io.cordova.myapp00d753.module.SaleApprovalReportModel;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;


public class SaleRejectFragment extends Fragment {


    View view;
    Pref pref;
    RecyclerView rvItem;
    String year;
    String month;
    ArrayList<SaleApprovalReportModel> itemList=new ArrayList<>();
    EditText etSearch;
    SaleRejectReportAdapter saleapprovalAdapter;
    ArrayList<String>item=new ArrayList<>();
    LinearLayout llClick,llSelect;
    int allclick=0;
    ArrayList<String>item1=new ArrayList<>();
    AlertDialog alerDialog1,alerDialog2;
    String attId;
    Button btnApprove;
    LinearLayout llMain,llNoData;
    String financialYear="0";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_sale_reject, container, false);
        initialize();
        getItemList();
        onClick();
        return view;
    }

    private void initialize() {
        pref = new Pref(getContext());
        rvItem = (RecyclerView)view. findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);



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

        etSearch=(EditText)view.findViewById(R.id.etSearch);
        llSelect=(LinearLayout)view.findViewById(R.id.llSelect);
        llClick=(LinearLayout)view.findViewById(R.id.llClick);
        btnApprove=(Button)view.findViewById(R.id.btnApprove);
        llMain=(LinearLayout)view.findViewById(R.id.llMain);
        llNoData=(LinearLayout)view.findViewById(R.id.llNoData);
        month="0";



    }

    private void getItemList() {
        llMain.setVisibility(View.VISIBLE);
        llNoData.setVisibility(View.GONE);
        Log.d("Arpan", "arpan");
        final ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String surl = AppData.url + "get_EmployeeSalesApproval?ClientID="+pref.getEmpClintId()+"&UserID="+pref.getEmpId()+"&FinancialYear="+financialYear+"&Month="+month+"&TokenNo=0&Status=IFBSM100007&Remarks=0&Operation=1&SecurityCode="+pref.getSecurityCode()+"&ZoneID="+pref.getZoneId()+"&BranchID="+pref.getBranchId();
        Log.d("input", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //   attendabceInfiList.clear();

                        Log.d("responseAttendance", response);
                        progressDialog.dismiss();
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
                                    String ModelName = obj.optString("ModelName");
                                    String CustomerName = obj.optString("CustomerName");
                                    String CustomerPhNo = obj.optString("CustomerPhNo");
                                    String CustomerEmail = obj.optString("CustomerEmail");
                                    String ApproverRemarks = obj.optString("ApproverRemarks");
                                    item1.add(TokenNo);


                                    SaleApprovalReportModel obj2 = new SaleApprovalReportModel(_SalesDate,Name,TokenNo);
                                    obj2.setModel(ModelName);
                                    obj2.setCusName(CustomerName);
                                    itemList.add(obj2);

                                }


                                setAdapter();


                            } else {

                                llMain.setVisibility(View.GONE);
                                llNoData.setVisibility(View.VISIBLE);
                                //Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                // Toast.makeText(AttenApprovalActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
    }

    private void setAdapter() {
         saleapprovalAdapter = new SaleRejectReportAdapter(itemList, getContext(),SaleRejectFragment.this);
        rvItem.setAdapter(saleapprovalAdapter);
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
        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.size()>0 ||allclick==1){
                    remsrksAlert();
                }else {
                    Toast.makeText(getContext(),"Please select item(s) to re approve the sales",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    void filter(String text) {
        ArrayList<SaleApprovalReportModel> temp = new ArrayList();
        for (SaleApprovalReportModel d : itemList) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.getEmpName().toLowerCase() .contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        //update recyclerview
        saleapprovalAdapter.updateList(temp);
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





        saleapprovalAdapter.notifyDataSetChanged();
    }


    private void successAlert(String text) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                getItemList();
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_remarks, null);
        dialogBuilder.setView(dialogView);
        final EditText etRemark = (EditText) dialogView.findViewById(R.id.etRemark);


        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog2.dismiss();

                acceptAttendance(etRemark.getText().toString());


            }
        });

        alerDialog2 = dialogBuilder.create();
        alerDialog2.setCancelable(false);
        Window window = alerDialog2.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog2.show();
    }

    private void acceptAttendance(String remarks) {
        if (allclick==1){
            attId=item1.toString().replace("[","").replace("]","");
        }else {
            attId=item.toString().replace("[","").replace("]","");
        }
        final ProgressDialog pd = new ProgressDialog(getContext());
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
                            Toast.makeText(getContext(), responseText, Toast.LENGTH_LONG).show();

                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                });


    }
}