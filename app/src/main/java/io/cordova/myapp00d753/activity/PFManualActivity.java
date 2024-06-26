package io.cordova.myapp00d753.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import io.cordova.myapp00d753.adapter.PFManualAdapter;
import io.cordova.myapp00d753.module.MenuItemModel;
import io.cordova.myapp00d753.module.PFManualModel;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

public class PFManualActivity extends AppCompatActivity {
    RecyclerView rvItem;
    Pref pref;
    ArrayList<PFManualModel>itemList=new ArrayList<>();
    JSONObject jsonObject;
    ImageView imgBack,imgHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pfmanual);
        initView();
    }

    private void initView(){
        pref=new Pref(PFManualActivity.this);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);

        rvItem=(RecyclerView)findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(PFManualActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);
        jsonObject=new JSONObject();
        try {
            jsonObject.put("Opreateion",0);
            pfManual(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PFManualActivity.this,EmployeeDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }


    private void pfManual(JSONObject jsonObject) {
        ProgressDialog pd=new ProgressDialog(PFManualActivity.this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(AppData.newv2url+"PFSettlement/GetPFSettlementManual")
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        pd.dismiss();

                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                           String Response_Data=job1.optString("Response_Data");
                            try {
                                JSONArray response_data=new JSONArray(Response_Data);
                                for (int i=0;i<response_data.length();i++){
                                    JSONObject obj=response_data.optJSONObject(i);
                                    String Caption=obj.optString("Caption");
                                    String Doc_URL=obj.optString("Doc_URL");
                                    String Doc_Type=obj.optString("Doc_Type");
                                    PFManualModel model=new PFManualModel();
                                    model.setCaption(Caption);
                                    model.setDoc_Type(Doc_Type);
                                    model.setDoc_URL(Doc_URL);
                                    itemList.add(model);


                                }

                                PFManualAdapter manualAdapter=new PFManualAdapter(itemList,PFManualActivity.this);
                                rvItem.setAdapter(manualAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {

                        }
                    }

                    @Override
                    public void onError(ANError error) {

                        Intent intent=new Intent(PFManualActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });
    }
}