package io.cordova.myapp00d753.utility;

import io.cordova.myapp00d753.module.AttendanceManageModule;
import io.cordova.myapp00d753.module.UploadObject;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


/**
 * Created by LENOVO on 8/21/2018.
 */

public class ApiClient {
    public  static  String baseUrl="http://111.93.182.174/GeniusiOSApi/api/";
    public static GetDataWorkingDay getDataWorkingDay = null;

    public static GetDataWorkingDay getService()
    {
        if(getDataWorkingDay==null)
        {
            Retrofit retrofit=new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            getDataWorkingDay=retrofit.create(GetDataWorkingDay.class);
        }
        return getDataWorkingDay;
    }
    public interface GetDataWorkingDay {
        @Multipart
        @POST("gcl_post_attedance")
        //Call<AttendanceManageModule> getDatas(@Part("AEMEmployeeID") String AEMEmployeeID, @Part("Address") String Address, @Part("Longitude") String Longitude, @Part("Latitude")String Latitude, @Part("SecurityCode") String SecurityCode, @Part("Image1")String Image1 );
        Call<AttendanceManageModule> getDatas(@Part("AEMEmployeeID") String AEMEmployeeID, @Part("Address") String Address, @Part("Longitude") String Longitude, @Part("Latitude") String Latitude, @Part("SecurityCode") String SecurityCode);
        @Multipart
        @POST("gcl_post_OfflineAttedance")
        Call<UploadObject> offlineAttn(@Part("AEMEmployeeID") String AEMEmployeeID, @Part("Address") String Address, @Part("Longitude") String Longitude, @Part("Latitude") String Latitude, @Part("SecurityCode") String SecurityCode, @Part("Attendance") String Attendance);

    }



   
}
