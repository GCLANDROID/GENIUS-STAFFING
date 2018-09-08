package io.cordova.myapp00d753.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.MenuAdapter;
import io.cordova.myapp00d753.module.MenuModule;
import io.cordova.myapp00d753.utility.AppController;

public class AboutUsActivity extends AppCompatActivity {
     ImageView imgHome,imgBack;
     LinearLayout llLoader,llMain;
     RecyclerView rvAbout;
     ArrayList<MenuModule>menuList=new ArrayList<>();
    MenuAdapter menuAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        initialize();
        getAboutInformation();
        onClick();
    }
    private void initialize(){


        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        llLoader=(LinearLayout)findViewById(R.id.llLoader);
        llMain=(LinearLayout)findViewById(R.id.llMain);
        rvAbout=(RecyclerView)findViewById(R.id.rvAbout);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(AboutUsActivity.this, LinearLayoutManager.VERTICAL, false);
        rvAbout.setLayoutManager(layoutManager);

    }
    private void onClick(){



        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AboutUsActivity.this,DashBoardActivity.class);
                startActivity(intent);
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    private void getAboutInformation(){
        String surl ="http://111.93.182.174/GeniusiOSApi/api/gcl_HomeScreen?MenuId=1&SecurityCode=0000";
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText=job1.optString("responseText");
                            boolean responseStatus=job1.optBoolean("responseStatus");
                            if (responseStatus){
                                Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                                JSONArray responseData=job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++){
                                    JSONObject obj=responseData.getJSONObject(i);
                                    String MenuName=obj.optString("MenuName");
                                    String Description=obj.optString("Description").replaceAll("<br/>","").replaceAll("<b>","").replaceAll("</b>","");
                                    MenuModule menuModule=new MenuModule(MenuName,Description,false);
                                    menuList.add(menuModule);



                                }
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                menuAdapter=new MenuAdapter(menuList,AboutUsActivity.this);
                                rvAbout.setAdapter(menuAdapter);
                            }
                            else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AboutUsActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(AboutUsActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert",error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");

    }


    public void updateAttendanceStatus(int position, boolean status) {
        menuList.get(position).setSelected(status);
        menuAdapter.notifyDataSetChanged();
    }


}
