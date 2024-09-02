package io.cordova.myapp00d753.activity.honnasa;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;

import io.cordova.myapp00d753.activity.EmployeeDashBoardActivity;
import io.cordova.myapp00d753.activity.honnasa.adapter.HanasaSalesAdapter;
import io.cordova.myapp00d753.activity.honnasa.model.MerchandiserModel;
import io.cordova.myapp00d753.activity.honnasa.model.StoreModel;
import io.cordova.myapp00d753.activity.honnasa.model.ViewSalesModel;
import io.cordova.myapp00d753.activity.metso.MetsoAttendanceActivity;
import io.cordova.myapp00d753.activity.metso.MetsoAttendanceReportActivity;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

public class HonasaSalesActivity extends AppCompatActivity {
    private static final String TAG = "Honasa_Sales_Activity";
    RecyclerView rvSales;
    Pref pref;
    ArrayList<String> storeStringList;
    ArrayList<StoreModel> storeArrayList;
    ArrayList<String> merchandiseList;
    ArrayList<MerchandiserModel> merchandiseList2;
    ArrayList<MerchandiserModel> categoryArrayList;
    ArrayList<String> categoryList;
    Spinner spStore,spMerchandiser,spCategory;
    String merchandiseValue,merchandiseID;
    String storeValue,storeID,categoryID;
    Button btnSearch,btnSave;
    LinearLayout llMerchandiser,llCategory;
    ArrayList<ViewSalesModel> viewSalesList;
    ConstraintLayout clSearchViewPanel;
    LinearLayout llSearchPanel,llLoading,llNoData,llHome;

    ImageView imgBack,imgHome;
    AlertDialog alerDialog1;
    ProgressDialog pdSave;
    SearchView edtSearch;
    //EditText edtSearch;
    public boolean isSalesDoneIsLessThenClosingStock = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_honasa_sales);
        initView();
        btnClick();
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
                Intent intent = new Intent(HonasaSalesActivity.this, EmployeeDashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSearch();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSalesDoneIsLessThenClosingStock){
                    try {
                        makeJsonObject();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(HonasaSalesActivity.this, "Sales Done value should be less then Closing Stock.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void initView() {
        pref = new Pref(this);
        spStore  = findViewById(R.id.spStore);
        spMerchandiser  = findViewById(R.id.spMerchandiser);
        spCategory  = findViewById(R.id.spCategory);
        btnSearch  = findViewById(R.id.btnSearch);
        btnSave  = findViewById(R.id.btnSave);
        llMerchandiser  = findViewById(R.id.llMerchandiser);
        llCategory  = findViewById(R.id.llCategory);
        clSearchViewPanel  = findViewById(R.id.clSearchViewPanel);
        llSearchPanel  = findViewById(R.id.llSearchPanel);
        llLoading  = findViewById(R.id.llLoading);
        llNoData  = findViewById(R.id.llNoData);
        imgBack  = findViewById(R.id.imgBack);
        imgHome = findViewById(R.id.imgHome);
        llHome = findViewById(R.id.llHome);
        edtSearch = findViewById(R.id.edtSearch);

        rvSales =  findViewById(R.id.rvSales);
        rvSales.setLayoutManager(new LinearLayoutManager(this));

        getStoreData();
    }

    private void getStoreData() {
        String surl = AppData.url+"gcl_CommonDDL?ddltype=STRE&ID=0&ID1="+pref.getMasterId()+"&ID2=0&ID3=0&SecurityCode=0000";
        Log.e(TAG,"STORE_LINK: "+surl);

        final ProgressDialog pd=new ProgressDialog(HonasaSalesActivity.this);
        pd.setMessage("Loading.....");
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pd.dismiss();
                        //itemList.clear();
                        try {
                            //Log.e(TAG,"GET_STORE_DATA: "+response.toString());
                            JSONObject job1 = new JSONObject(response);
                            Log.e(TAG,"GET_STORE_DATA"+ job1.toString(4));
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            String responseCode=job1.optString("responseCode");
                            if (responseStatus) {
                                storeStringList = new ArrayList<>();
                                storeArrayList = new ArrayList<>();
                                //storeStringList.add("Select Store");
                                /*storeArrayList.add(new StoreModel(
                                        "0",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        ""
                                ));*/
                                JSONArray responseData = job1.optJSONArray("responseData");

                                for (int i = 0; i < responseData.length(); i++){
                                    JSONObject obj = responseData.getJSONObject(i);
                                    Log.e(TAG, "VALUE: "+obj.optString("value") );
                                    storeStringList.add(obj.optString("value"));
                                    storeArrayList.add(new StoreModel(
                                            obj.optString("$id"),
                                            obj.optString("SecurityCode"),
                                            obj.optString("ddltype"),
                                            obj.optString("id"),
                                            obj.optString("id1"),
                                            obj.optString("id2"),
                                            obj.optString("id3"),
                                            obj.optString("value")
                                    ));
                                }

                                ArrayAdapter arrayAdapter = new ArrayAdapter(HonasaSalesActivity.this, android.R.layout.simple_spinner_item, storeStringList);
                                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spStore.setAdapter(arrayAdapter);

                                spStore.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                        storeValue = storeStringList.get(position);
                                        Log.e(TAG, "onItemSelected: "+storeArrayList.get(position).id);
                                        storeID = storeArrayList.get(position).id;
                                        Log.e(TAG, "onItemSelected: "+storeValue);
                                        Log.e(TAG, "storeID: "+storeID);
                                        getMerchandiserData();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });


                            }


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(HonasaSalesActivity.this, "GET_STORE_DATA_ERROR", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                //Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                //showAlert();
                Log.e(TAG,"ERROR: "+error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
    }

    private void getMerchandiserData() {
        String surl = AppData.url+"gcl_CommonDDL?ddltype=BRND&ID=0&ID1=0&ID2=0&ID3=0&SecurityCode=0000";
        Log.d(TAG,"inputMerchandiserLink: "+surl);

        final ProgressDialog pd=new ProgressDialog(HonasaSalesActivity.this);
        pd.setMessage("Loading.....");
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG,"GET_DATA_MERCHANDISER: "+response);
                        pd.dismiss();
                        //itemList.clear();
                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e(TAG,"response12"+ job1.toString(4));
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            String responseCode=job1.optString("responseCode");
                            if (responseStatus) {
                                llMerchandiser.setVisibility(View.VISIBLE);
                                merchandiseList = new ArrayList<>();
                                merchandiseList2 = new ArrayList<>();
                                merchandiseList.add("Select Merchandiser");
                                merchandiseList2.add(new MerchandiserModel( "0",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        ""));
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++){
                                    JSONObject obj = responseData.getJSONObject(i);
                                    Log.e(TAG, "Merchandiser_VALUE: "+obj.optString("value") );
                                    merchandiseList.add(obj.optString("value"));
                                    merchandiseList2.add(new MerchandiserModel(
                                            obj.optString("$id"),
                                            obj.optString("SecurityCode"),
                                            obj.optString("ddltype"),
                                            obj.optString("id"),
                                            obj.optString("id1"),
                                            obj.optString("id2"),
                                            obj.optString("id3"),
                                            obj.optString("value")
                                    ));
                                }

                                ArrayAdapter arrayAdapter = new ArrayAdapter(HonasaSalesActivity.this, android.R.layout.simple_spinner_item, merchandiseList);
                                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spMerchandiser.setAdapter(arrayAdapter);

                                spMerchandiser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                        merchandiseValue = merchandiseList.get(position);
                                        if (!merchandiseValue.equals("Select Merchandiser")){
                                            merchandiseID = merchandiseList2.get(position).id;
                                            Log.e(TAG, "onItemSelected: "+merchandiseID);
                                            getCategoryData();
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });


                            }
                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(HonasaSalesActivity.this, "ERROR", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                //Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                //showAlert();
                Log.e(TAG,"ERROR: "+error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
    }

    private void getCategoryData() {
        //storeID = "1";
        String surl = AppData.url+"gcl_CommonDDL?ddltype=CATGRY&ID=0&ID1="+merchandiseID+"&ID2=0&ID3=0&SecurityCode=0000";
        Log.e(TAG,"inputCategoryLink"+surl);

        final ProgressDialog pd=new ProgressDialog(HonasaSalesActivity.this);
        pd.setMessage("Loading.....");
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.e(TAG,"GET_DATA_CATEGORY: "+response);
                        pd.dismiss();
                        //itemList.clear();
                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e(TAG,"GET_DATA_CATEGORY: " + job1.toString(4));
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            String responseCode=job1.optString("responseCode");
                            if (responseStatus) {
                                llCategory.setVisibility(View.VISIBLE);
                                categoryList = new ArrayList<>();
                                categoryArrayList = new ArrayList<>();
                                categoryArrayList.add(new MerchandiserModel(
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        ""
                                ));
                                categoryList.add("Select Category");
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++){
                                    JSONObject obj = responseData.getJSONObject(i);
                                    Log.e(TAG, "GET_DATA_CATEGORY_VALUE: "+obj.optString("value") );
                                    categoryList.add(obj.optString("value"));
                                    categoryArrayList.add(new MerchandiserModel(
                                            obj.optString("$id"),
                                            obj.optString("SecurityCode"),
                                            obj.optString("ddltype"),
                                            obj.optString("id"),
                                            obj.optString("id1"),
                                            obj.optString("id2"),
                                            obj.optString("id3"),
                                            obj.optString("value")
                                    ));
                                }

                                ArrayAdapter arrayAdapter = new ArrayAdapter(HonasaSalesActivity.this, android.R.layout.simple_spinner_item, categoryList);
                                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spCategory.setAdapter(arrayAdapter);

                                spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                        String categoryValue = categoryList.get(position);
                                        Log.e(TAG, "onItemSelected: "+categoryValue);
                                        if (!categoryValue.equals("Select Category")){
                                            categoryID =categoryArrayList.get(position).id;
                                            btnSearch.setVisibility(View.VISIBLE);
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });

                            }
                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(HonasaSalesActivity.this, "ERROR", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                //Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                //showAlert();
                Log.e(TAG,"ERROR: "+error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
    }

    private void btnSearch() {
        llSearchPanel.setVisibility(View.GONE);
        llLoading.setVisibility(View.VISIBLE);
        Log.e(TAG, "btnSearch: DDL_Type: "+storeID+"\nID1: "+merchandiseID+"\nID2: "+categoryID+"\nID3: 1");
        AndroidNetworking.post(AppData.newv2url+"Honasa/ViewSales")
                .addBodyParameter("DDL_Type", storeID)
                .addBodyParameter("ID1", merchandiseID)
                .addBodyParameter("ID2", categoryID)
                .addBodyParameter("ID3", "1")
                //.addMultipartParameter("SecurityCode", "0000")
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "VIEW_SALES: SUCCESS: " + response.toString(4));
                            JSONObject object = response;
                            String Response_Message = object.optString("Response_Message");
                            String Response_Code =object.optString("Response_Code");
                            if (Response_Code.equals("101")) {

                                viewSalesList = new ArrayList();
                                String responseData = object.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(responseData);
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    viewSalesList.add(new ViewSalesModel(
                                            obj.optInt("OpenStock"),
                                            obj.optDouble("ProductValue"),
                                            obj.optInt("InStock"),
                                            obj.optInt("AEMProductID"),
                                            obj.optString("PRODUCTNAME")
                                    ));
                                }
                                Log.e(TAG, "viewSalesList.size(): "+viewSalesList.size());
                                HanasaSalesAdapter hanasaSalesAdapter = new HanasaSalesAdapter(HonasaSalesActivity.this,viewSalesList);
                                rvSales.setAdapter(hanasaSalesAdapter);

                               /* edtSearch.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                        hanasaSalesAdapter.getFilter().filter(charSequence);
                                    }

                                    @Override
                                    public void afterTextChanged(Editable editable) {

                                    }
                                });*/

                                edtSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                    @Override
                                    public boolean onQueryTextSubmit(String s) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onQueryTextChange(String s) {
                                        hanasaSalesAdapter.getFilter().filter(s);
                                        return false;
                                    }
                                });

                                llSearchPanel.setVisibility(View.GONE);
                                llLoading.setVisibility(View.GONE);
                                llNoData.setVisibility(View.GONE);
                                clSearchViewPanel.setVisibility(View.VISIBLE);
                            } else {
                                llSearchPanel.setVisibility(View.GONE);
                                llLoading.setVisibility(View.GONE);
                                llNoData.setVisibility(View.VISIBLE);
                                clSearchViewPanel.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        llNoData.setVisibility(View.VISIBLE);
                        llSearchPanel.setVisibility(View.GONE);
                        llLoading.setVisibility(View.GONE);
                        clSearchViewPanel.setVisibility(View.GONE);
                        Log.e(TAG, "onResponse: onError: " + error);
                        //progressDialog.cancel();
                    }
                });
    }

    private void makeJsonObject() throws JSONException {
        pdSave =new ProgressDialog(HonasaSalesActivity.this);
        pdSave.setMessage("Loading.....");
        pdSave.show();

        JSONArray jsonArray = new JSONArray();
        JSONObject finalJsonObject = new JSONObject();
        for (int i = 0; i < viewSalesList.size(); i++) {
            if (viewSalesList.get(i).isModified){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ProductID",viewSalesList.get(i).getAEMProductID());
                jsonObject.put("OpenStock",viewSalesList.get(i).getInStock());
                jsonObject.put("ReceiveStock",viewSalesList.get(i).getReceivingStock());
                jsonObject.put("SalesStock",viewSalesList.get(i).getSalesDone());
                jsonObject.put("StockValue",viewSalesList.get(i).getSalesValues());
                jsonObject.put("CloseStock",viewSalesList.get(i).getClosingStock());
                jsonArray.put(jsonObject);
            }
        }
        finalJsonObject.put("DDL_Type",pref.getMasterId());
        finalJsonObject.put("ID1",storeID);
        finalJsonObject.put("ListData",jsonArray);
        finalJsonObject.put("Operation","1");
        Log.e(TAG, "makeJsonObject: "+finalJsonObject.toString(4));

        saveSalesApiCall(finalJsonObject);
    }

    private void saveSalesApiCall(JSONObject finalJsonObject) {
        AndroidNetworking.post(AppData.newv2url+"Honasa/SaveSales")
                .addJSONObjectBody(finalJsonObject)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pdSave.dismiss();
                        Log.e(TAG, "SALES_API_CALL: "+response.toString());
                        JSONObject jsonObject = response;
                        String Response_Message = jsonObject.optString("Response_Message");
                        if (jsonObject.optString("Response_Code").equals("101")){
                            successAlert(Response_Message);
                        } else {
                            Toast.makeText(HonasaSalesActivity.this, Response_Message, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        pdSave.dismiss();
                        Toast.makeText(HonasaSalesActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "SALES_API_CALL_anError: "+anError);
                    }
                });
    }

    private void successAlert(String message) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(HonasaSalesActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);
        /*if (addflag == 1) {
            tvInvalidDate.setText("Your attendnace has been saved successfully");
        } else {
            tvInvalidDate.setText("Your attendnace has been saved successfully");
        }*/
        tvInvalidDate.setText(message);
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                Intent intent = new Intent(HonasaSalesActivity.this,HanasaSalesReportActivity.class);
                startActivity(intent);
                finish();
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