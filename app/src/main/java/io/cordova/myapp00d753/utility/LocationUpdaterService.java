package io.cordova.myapp00d753.utility;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

import android.util.Log;


import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.cordova.myapp00d753.R;

public class LocationUpdaterService extends Service {
    public LocationManager mLocationManager;
    public LocationUpdaterListener mLocationListener;
    public Location previousBestLocation = null;
    public static final int TWO_MINUTES = 1000; // 120 seconds
    public static Boolean isRunning = false;
    Pref pref;
    Context context;
    String lat;
    String longitude;
    String location;
    String address;

    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;

    String date;
    //float radiusValue;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationUpdaterListener();
        pref = new Pref(this);


        /*int endPoint= Integer.parseInt(pref.getEndPoint());
        float c=endPoint/10;
        float de=c/10;
        radiusValue=de/10;*/





        // Toast.makeText(this,"start",Toast.LENGTH_LONG).show();
        super.onCreate();

    }

    Handler mHandler = new Handler();
    Runnable mHandlerTask = new Runnable() {
        @Override
        public void run() {
            if (!isRunning) {
                startListening();
            }
            mHandler.postDelayed(mHandlerTask, TWO_MINUTES);
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mHandlerTask.run();
        //Toast.makeText(this,"start",Toast.LENGTH_LONG).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        AlarmManager alarm = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarm.set(
                alarm.RTC_WAKEUP,
                System.currentTimeMillis() + (1000 * 60 * 60),
                PendingIntent.getService(this, 0, new Intent(this, LocationUpdaterService.class), 0)
        );
    }

    private void startListening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mLocationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) mLocationListener);

            if (mLocationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
        }
        isRunning = true;
    }

    private void stopListening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationManager.removeUpdates(mLocationListener);
        }
        isRunning = false;
    }

    public class LocationUpdaterListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            if (isBetterLocation(location, previousBestLocation)) {
                previousBestLocation = location;
                try {
                    // Script to post location data to server..

                    lat = String.valueOf(previousBestLocation.getLatitude());
                    longitude = String.valueOf(previousBestLocation.getLongitude());
                    //Toast.makeText(getApplicationContext(),lat+"lat service",Toast.LENGTH_LONG).show();
                    pref.saveEndLat(lat);
                    pref.saveEndLong(longitude);
                    Log.d("latiet", lat);
                    Log.d("longitude", longitude);
                     address = getCompleteAddressString(previousBestLocation.getLatitude(), previousBestLocation.getLongitude());
                    //Toast.makeText(getApplicationContext(),address,Toast.LENGTH_LONG).show();

                    double clat = Double.parseDouble(pref.getOwnLat());
                    Log.d("clat", String.valueOf(clat));

                    double clong = Double.parseDouble(pref.getOwnLong());


                    LatLng latLng = new LatLng(clat, clong);
                    LatLng elatLng = new LatLng(previousBestLocation.getLatitude(), previousBestLocation.getLongitude());
                    double dlat=clat-0.0000500;
                    Log.d("dlat", String.valueOf(dlat));


                    if (previousBestLocation.getLatitude() > dlat) {
                        CalculationByDistance(latLng, elatLng);
                    } else {
                       // Toast.makeText(getApplicationContext(), "out of range 1", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    stopListening();
                }
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            stopListening();
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    private void locationUpdate() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Authentication..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        Log.d("riku", "run");


    }

    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current ", strReturnedAddress.toString());
            } else {
                Log.w("My Current", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current", "Canont get Address!");
        }
        return strAdd;
    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.d("RadiusValue", valueResult + " KM " + kmInDec
                + " Meter " + meterInDec);
        String distance = String.format("%.3f", valueResult);
        double ddis = Double.parseDouble(distance);
        Log.d("chota", String.valueOf(ddis));
        Date dof = Calendar.getInstance().getTime();
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String currentDateTimeString = sdf.format(d);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(dof);
        date = formattedDate+"="+currentDateTimeString;
        if (ddis < 0.00 || ddis == 0.00) {
            //Toast.makeText(getApplicationContext(), "fencing zone", Toast.LENGTH_LONG).show();
/*            String surl = "https://www.cloud.geniusconsultant.com/GHRMSApi/api/get_EmployeewiseGeofence?EmployeeId="+pref.getEmpId()+"&Longitude="+longitude+"&Latitude="+lat+"&Address="+address.replaceAll("\\s+","")+"&FenceType=IN&Createdon="+date+"&Operation=3&SecurityCode="+pref.getSecurityCode();
            Log.d("inputatt", surl);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("responseLeave", response);

                            try {
                                JSONObject job1 = new JSONObject(response);
                                Log.e("response12", "@@@@@@" + job1);
                                String responseText = job1.optString("responseText");
                                boolean responseStatus = job1.optBoolean("responseStatus");
                                if (responseStatus) {
                                    addNotification();
                                }


                                // boolean _status = job1.getBoolean("status");


                            } catch (JSONException e) {
                                e.printStackTrace();
                              //  Toast.makeText(context, "Volly Error", Toast.LENGTH_LONG).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                   // Toast.makeText(context, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                    Log.e("ert", error.toString());
                    saveNameToLocalStorage(address.replaceAll("\\s+", ""),date,lat,longitude,"IN",NAME_NOT_SYNCED_WITH_SERVER);


                }
            }) {

            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);*/




        } else {
            /*String surl = "https://www.cloud.geniusconsultant.com/GHRMSApi/api/get_EmployeewiseGeofence?EmployeeId="+pref.getEmpId()+"&Longitude="+longitude+"&Latitude="+lat+"&Address="+address.replaceAll("\\s+","")+"&FenceType=OUT&Createdon="+date+"&Operation=3&SecurityCode="+pref.getSecurityCode();
            Log.d("inputatt333", surl);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("responseLeave", response);

                            try {
                                JSONObject job1 = new JSONObject(response);
                                Log.e("response12", "@@@@@@" + job1);
                                String responseText = job1.optString("responseText");
                                boolean responseStatus = job1.optBoolean("responseStatus");
                                if (responseStatus) {
                                    outNotification();

                                }


                                // boolean _status = job1.getBoolean("status");


                            } catch (JSONException e) {
                                e.printStackTrace();
                               // Toast.makeText(context, "Volly Error", Toast.LENGTH_LONG).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.e("ert", error.toString());
                    saveNameToLocalStorage(address.replaceAll("\\s+", ""),date,lat,longitude,"OUT",NAME_NOT_SYNCED_WITH_SERVER);
                }
            }) {

            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);*/


            //Toast.makeText(getApplicationContext(), "not fencing zone", Toast.LENGTH_LONG).show();

        }


        return Radius * c;
    }


    private void addNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.geniuslogo)
                        .setContentTitle("Notifications Example")
                        .setContentText("fence zone");


        /*Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);*/

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }


    private void outNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.geniuslogo)
                        .setContentTitle("Notifications Example")
                        .setContentText("not fence zone");


        /*Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);*/

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }











}
