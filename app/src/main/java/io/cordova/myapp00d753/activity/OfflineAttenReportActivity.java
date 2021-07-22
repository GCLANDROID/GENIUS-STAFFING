package io.cordova.myapp00d753.activity;

import android.database.Cursor;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.OfflineAttenAdapter;
import io.cordova.myapp00d753.helper.DatabaseHelper;
import io.cordova.myapp00d753.module.OfflineSaleModel;

public class OfflineAttenReportActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private List<OfflineSaleModel> saleList=new ArrayList<>();
    private ListView listViewNames;
    OfflineAttenAdapter offlineSaleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_sale_report);
        db = new DatabaseHelper(this);
        listViewNames = (ListView) findViewById(R.id.listViewNames);
        loadNames();
        ImageView imgBack=(ImageView)findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void loadNames() {
        //names.clear();
        Cursor cursor = db.getNames();
        if (cursor.moveToFirst()) {
            do {
                OfflineSaleModel name = new OfflineSaleModel(
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ADDRESS)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_STATUS))
                );
                saleList.add(name);
                Log.d("names",saleList.toString());
            } while (cursor.moveToNext());
        }

        offlineSaleAdapter = new OfflineAttenAdapter(this, R.layout.raw, saleList);
        listViewNames.setAdapter(offlineSaleAdapter);
    }


}
