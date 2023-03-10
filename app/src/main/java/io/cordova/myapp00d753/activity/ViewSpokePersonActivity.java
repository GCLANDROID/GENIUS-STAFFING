package io.cordova.myapp00d753.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.SpokePersonAdapter;
import io.cordova.myapp00d753.module.AttendanceModule;
import io.cordova.myapp00d753.module.SpokePersonModel;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

public class ViewSpokePersonActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView rvItem;
    ArrayList<SpokePersonModel> itemList = new ArrayList<>();
    Pref pref;
    String spokepersonarray;
    JSONArray itemArray;
    ImageView imgBack,imgHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_spoke_person);
        initView();
    }

    private void initView() {
        pref = new Pref(ViewSpokePersonActivity.this);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        spokepersonarray = getIntent().getStringExtra("spokepersonarray");
        try {
            itemArray = new JSONArray(spokepersonarray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(ViewSpokePersonActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);
        getItemList();
        imgBack.setOnClickListener(this);
        imgHome.setOnClickListener(this);
    }

    private void getItemList() {

        if (itemArray.length() > 0) {
            for (int i = 0; i < itemArray.length(); i++) {
                JSONObject obj = itemArray.optJSONObject(i);
                String Name = obj.optString("Name");
                String Branch = obj.optString("Branch");
                String Mobile = obj.optString("Mobile");
                String Email = obj.optString("Email");

                SpokePersonModel obj2 = new SpokePersonModel(Name, Mobile, Email, Branch);
                itemList.add(obj2);


            }

            SpokePersonAdapter spokePersonAdapter = new SpokePersonAdapter(itemList, ViewSpokePersonActivity.this);
            rvItem.setAdapter(spokePersonAdapter);

        }
    }

    @Override
    public void onClick(View view) {
        if (view==imgBack){
            onBackPressed();
        }else if (view==imgHome){
            onBackPressed();
        }
    }
}