package io.cordova.myapp00d753.utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.cordova.myapp00d753.activity.NEW.model.LocationModel;
import io.cordova.myapp00d753.activity.metso.model.MetsoShiftModel;
import io.cordova.myapp00d753.module.HolidayMarkModel;
import io.cordova.myapp00d753.module.SpineerItemModel;

public class RequiredListClass {
    public static ArrayList<MetsoShiftModel> getShiftData(String ShiftList) throws JSONException {
        ArrayList<MetsoShiftModel> mShiftList = new ArrayList<>();
        JSONArray ShiftListArray = new JSONArray(ShiftList);
        mShiftList.add(new MetsoShiftModel(0,"Select Shift"));
        if (ShiftListArray.length()>0){
            for (int i = 0; i < ShiftListArray.length(); i++) {
                JSONObject obj = ShiftListArray.optJSONObject(i);
                long WorkingShiftID = obj.optLong("WorkingShiftID");
                String WorkingShiftName = obj.optString("WorkingShiftName");
                mShiftList.add(new MetsoShiftModel(WorkingShiftID,WorkingShiftName));
            }
        }
        return mShiftList;
    }

    public static ArrayList<LocationModel> getLocationData(String LocationList) throws JSONException {
        ArrayList<LocationModel> mLocationArrayList = new ArrayList<>();
        JSONArray LocationjsonArray = new JSONArray(LocationList);
        mLocationArrayList.add(new LocationModel("0","Select Location"));
       /* mLocationArrayList.add(new MetsoLocationModel(2,"Location 1"));
        mLocationArrayList.add(new MetsoLocationModel(1,"Location 2"));*/
        if(LocationjsonArray.length()>0){
            if (LocationjsonArray.length() > 0){
                for (int i = 0; i < LocationjsonArray.length(); i++) {
                    JSONObject obj = LocationjsonArray.optJSONObject(i);
                    String SiteId = obj.getString("WorkPlaceID");
                    String SiteName = obj.getString("WorkPlaceName");
                    mLocationArrayList.add(new LocationModel(SiteId,SiteName));
                }
            }
        }
        return mLocationArrayList;
    }

    public static ArrayList<SpineerItemModel> getApproverData(String ApproverList) throws JSONException {
        ArrayList<SpineerItemModel> mSupervisorList = new ArrayList<>();
        JSONArray ApproverjsonArray = new JSONArray(ApproverList);
        if (ApproverjsonArray.length()>0){
            for (int i = 0; i < ApproverjsonArray.length(); i++) {
                JSONObject obj = ApproverjsonArray.optJSONObject(i);
                String Code = obj.optString("Code");
                String UserName = obj.optString("UserName");
                mSupervisorList.add(new SpineerItemModel(UserName,Code));
            }
        }
        return mSupervisorList;
    }

    public static ArrayList<HolidayMarkModel> getNormalHolidayList(String NormalHolidayList) throws JSONException {
        ArrayList<HolidayMarkModel> mNormalHolidayList= new ArrayList<>();
        JSONArray ApproverjsonArray = new JSONArray(NormalHolidayList);
        if (ApproverjsonArray.length()>0){
            for (int i = 0; i < ApproverjsonArray.length(); i++) {
                JSONObject obj = ApproverjsonArray.optJSONObject(i);
                String NormalHolidayName = obj.optString("NormalHolidayName");
                String NormalHolidayDate = obj.optString("NormalHolidayDate");
                mNormalHolidayList.add(new HolidayMarkModel(NormalHolidayName,NormalHolidayDate));
            }
        }
        return mNormalHolidayList;
    }

    public static ArrayList<HolidayMarkModel> getOptionalHolidayList(String OptionalHolidayList) throws JSONException {
        ArrayList<HolidayMarkModel> mOptionalHolidayList= new ArrayList<>();
        JSONArray ApproverjsonArray = new JSONArray(OptionalHolidayList);
        if (ApproverjsonArray.length()>0){
            for (int i = 0; i < ApproverjsonArray.length(); i++) {
                JSONObject obj = ApproverjsonArray.optJSONObject(i);
                String OptionalHolidayName = obj.optString("OptionalHolidayName");
                String OptionalHolidayDate = obj.optString("OptionalHolidayDate");
                mOptionalHolidayList.add(new HolidayMarkModel(OptionalHolidayName,OptionalHolidayDate));
            }
        }
        return mOptionalHolidayList;
    }
    public static ArrayList<LocationModel> getOtherLocationData(String OtherLocationList) throws JSONException {
        ArrayList<LocationModel> mLocationArrayList = new ArrayList<>();
        JSONArray LocationjsonArray = new JSONArray(OtherLocationList);
        mLocationArrayList.add(new LocationModel("0","Select Location"));
        if(LocationjsonArray.length()>0){
            if (LocationjsonArray.length() > 0){
                for (int i = 0; i < LocationjsonArray.length(); i++) {
                    JSONObject obj = LocationjsonArray.optJSONObject(i);
                    String SiteId = obj.getString("OtherLocationID");
                    String SiteName = obj.getString("OtherLocationValue");
                    mLocationArrayList.add(new LocationModel(SiteId,SiteName));
                }
            }
        }
        return mLocationArrayList;
    }
}
