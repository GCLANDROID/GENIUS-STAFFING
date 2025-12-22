package io.cordova.myapp00d753.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.FormSixteenAdapter;
import io.cordova.myapp00d753.module.FormSixteenModule;
import io.cordova.myapp00d753.module.SalaryModule;
import io.cordova.myapp00d753.utility.AppData;

public class FormSixteenActivity extends AppCompatActivity {

    LinearLayout llLoader, llMain, llNodata;
    RecyclerView rvItemList;
    ArrayList<FormSixteenModule> itemList = new ArrayList<>();
    FormSixteenAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_sixteen);
        initView();
    }

    private void initView() {
        llLoader = (LinearLayout) findViewById(R.id.llLoader);
        llMain = (LinearLayout) findViewById(R.id.llMain);
        llNodata = (LinearLayout) findViewById(R.id.llNodata);

        rvItemList = (RecyclerView) findViewById(R.id.rvItemList);



    }

    private void getItemList(JSONObject jsonObject) {
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNodata.setVisibility(View.GONE);
        AndroidNetworking.post(AppData.FORM_16_VIEW)
                .addJSONObjectBody(jsonObject)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(Response_Data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String FinancialYear = obj.optString("FinancialYear");
                                    String PartA = obj.optString("PartA");
                                    String PartB = obj.optString("PartB");
                                    int IsPaidService=obj.optInt("IsPaidService");
                                    String Charges = obj.optString("Charges");
                                    FormSixteenModule sixteenModule=new FormSixteenModule();
                                    sixteenModule.setFinancialYear(FinancialYear);
                                    sixteenModule.setPartA(PartA);
                                    sixteenModule.setPartB(PartB);
                                    sixteenModule.setIsPaidService(IsPaidService);
                                    sixteenModule.setCharges(Charges);
                                    itemList.add(sixteenModule);


                                }

                                if (itemList.size() > 0) {
                                    llLoader.setVisibility(View.GONE);
                                    llMain.setVisibility(View.VISIBLE);
                                    llNodata.setVisibility(View.GONE);

                                    setAdapter();
                                } else {
                                    llLoader.setVisibility(View.GONE);
                                    llMain.setVisibility(View.GONE);
                                    llNodata.setVisibility(View.GONE);

                                }
                            } else {
                                //Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNodata.setVisibility(View.VISIBLE);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(FormSixteenActivity.this, "Something went to wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        llLoader.setVisibility(View.GONE);
                        llMain.setVisibility(View.GONE);
                        llNodata.setVisibility(View.VISIBLE);
                        Toast.makeText(FormSixteenActivity.this, "Something went to wrong", Toast.LENGTH_SHORT).show();


                    }
                });
    }

    private void setAdapter() {
        adapter = new FormSixteenAdapter( itemList,FormSixteenActivity.this);
        rvItemList.setAdapter(adapter);
    }


}