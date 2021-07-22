package io.cordova.myapp00d753.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;



import io.cordova.myapp00d753.activity.OffAttenDanceActivity;
import io.cordova.myapp00d753.helper.DatabaseHelper;
import io.cordova.myapp00d753.module.UploadObject;
import io.cordova.myapp00d753.utility.ApiClient;
import io.cordova.myapp00d753.utility.Pref;
import retrofit2.Call;
import retrofit2.Callback;

public class NetworkStateChecker extends BroadcastReceiver {
    private Context context;
    private DatabaseHelper db;
    Pref pref;


    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        db = new DatabaseHelper(context);
        pref=new Pref(context);
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        //if there is a network
        if (activeNetwork != null) {
            //if connected to wifi or mobile data plan
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                //getting all the unsynced names
                final Cursor cursor = db.getUnsyncedNames();
                if (cursor.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced name to MySQL



                                saveName(
                                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)),
                                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ADDRESS)),
                                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE)),
                                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_LAT)),
                                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_LONG))



                                );



                    } while (cursor.moveToNext());
                }
            }
        }
    }

    private void saveName(final int id, final String adress,String date,String lat,String longt) {
        Log.d("saikat","arpan");
        String empId = pref.getEmpId();
        final String adresst = adress;
        final String cdate = date;
        final String clat = lat;
        final String clong = longt;
        final String csecurtiycode = pref.getSecurityCode();


        Call<UploadObject> datumCall = ApiClient.getService().offlineAttn( empId, adresst, clong, clat, csecurtiycode,cdate);
        datumCall.enqueue(new Callback<UploadObject>() {
            @Override
            public void onResponse(Call<UploadObject> call, retrofit2.Response<UploadObject> response) {

                UploadObject extraWorkingDayModel=response.body();
                if (extraWorkingDayModel.isResponseStatus()) {

                    db.updateNameStatus(id, OffAttenDanceActivity.NAME_SYNCED_WITH_SERVER);
                    Toast.makeText(context,extraWorkingDayModel.responseText,Toast.LENGTH_LONG).show();

                    //sending the broadcast to refresh the list
                    context.sendBroadcast(new Intent(OffAttenDanceActivity.DATA_SAVED_BROADCAST));
                    Log.d("saikat","1");
                }
                else {
                    Toast.makeText(context,"broadcast not  hit",Toast.LENGTH_LONG).show();
                    Log.d("saikat","2");
                }
            }

            @Override
            public void onFailure(Call<UploadObject> call, Throwable t) {

                Log.d("saikat","3");
            }




        });
    }

    }

