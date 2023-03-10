package io.cordova.myapp00d753.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.naishadhparmar.zcustomcalendar.CustomCalendar;
import org.naishadhparmar.zcustomcalendar.OnNavigationButtonClickedListener;
import org.naishadhparmar.zcustomcalendar.Property;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import io.cordova.myapp00d753.R;


public class CustomCalendarActivity extends AppCompatActivity  implements OnNavigationButtonClickedListener {
    CustomCalendar customCalendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_calendar);

        customCalendar=findViewById(R.id.custom_calendar);

        // Initialize description hashmap
        HashMap<Object, Property> descHashMap=new HashMap<>();

        // Initialize default property
        Property defaultProperty=new Property();

        // Initialize default resource
        defaultProperty.layoutResource=R.layout.default_view;

        // Initialize and assign variable
        defaultProperty.dateTextViewResource=R.id.text_view;

        // Put object and property
        descHashMap.put("default",defaultProperty);

        // for current date
        Property currentProperty=new Property();
        currentProperty.layoutResource=R.layout.current_view;
        currentProperty.dateTextViewResource=R.id.text_view;
        descHashMap.put("holiday",currentProperty);

        // for present date
        Property presentProperty=new Property();
        presentProperty.layoutResource=R.layout.present_view;
        presentProperty.dateTextViewResource=R.id.text_view;
        descHashMap.put("present",presentProperty);

        // For absent
        Property absentProperty =new Property();
        absentProperty.layoutResource=R.layout.absent_view;
        absentProperty.dateTextViewResource=R.id.text_view;
        descHashMap.put("absent",absentProperty);

        // set desc hashmap on custom calendar
        customCalendar.setMapDescToProp(descHashMap);

        // Initialize date hashmap

        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.PREVIOUS, this);
        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.NEXT, this);

        HashMap<Integer,Object> dateHashmap=new HashMap<>();

        // initialize calendar
        Calendar calendar=  Calendar.getInstance();

        // Put values

        JSONObject oneobj=new JSONObject();
        try {
            oneobj.put("Date",1);
            oneobj.put("status","present");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject twoobj=new JSONObject();
        try {
            twoobj.put("Date",2);
            twoobj.put("status","absent");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray jsonArray=new JSONArray();
        jsonArray.put(oneobj);
        jsonArray.put(twoobj);
        for (int i=0;i<jsonArray.length();i++){
            JSONObject obj=jsonArray.optJSONObject(i);
            int date=obj.optInt("Date");
            String Status=obj.optString("status");
            dateHashmap.put(date,Status);

        }


        // set date
        customCalendar.setDate(calendar,dateHashmap);




    }


    @Override
    public Map<Integer, Object>[] onNavigationButtonClicked(int whichButton, Calendar newMonth) {
        Map<Integer, Object>[] arr = new Map[2];
        arr[0] = new HashMap<>();
        if (whichButton==1){
            HashMap<Integer,Object> dateHashmap=new HashMap<>();

            // initialize calendar
            Calendar calendar=  Calendar.getInstance();

            // Put values

            JSONObject oneobj=new JSONObject();
            try {
                oneobj.put("Date",1);
                oneobj.put("status","present");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject twoobj=new JSONObject();
            try {
                twoobj.put("Date",2);
                twoobj.put("status","absent");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject threeobj=new JSONObject();
            try {
                threeobj.put("Date",12);
                threeobj.put("status","holiday");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONArray jsonArray=new JSONArray();
            jsonArray.put(oneobj);
            jsonArray.put(twoobj);
            jsonArray.put(threeobj);
            for (int i=0;i<jsonArray.length();i++){
                JSONObject obj=jsonArray.optJSONObject(i);
                int date=obj.optInt("Date");
                String Status=obj.optString("status");
                arr[0].put(date,Status);

            }


            // set date



        }

        return arr;
    }
}