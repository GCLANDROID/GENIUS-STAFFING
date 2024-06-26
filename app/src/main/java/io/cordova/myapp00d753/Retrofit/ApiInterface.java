package io.cordova.myapp00d753.Retrofit;

import com.google.gson.JsonObject;

import io.cordova.myapp00d753.module.UploadObject;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("Leave/Get_MetsoAttendanceData")
    Call<JsonObject> GetMetsoAttendanceData(
            @Query("Mode") String Mode,
            @Query("CompanyID") String CompanyID,
            @Query("SecurityCode") String SecurityCode
    );

    @Multipart
    @POST("gcl_post_attedanceGeofenceMetso")
    Call<JsonObject> MetsoAttendancePunch(
            @Part("AEMEmployeeID") RequestBody AEMEmployeeID,
            @Part("Address") RequestBody Address,
            @Part("Shiftid") RequestBody Shiftid,
            @Part("Siteid") RequestBody Siteid,
            @Part("Longitude") RequestBody Longitude,
            @Part("Latitude") RequestBody Latitude,
            @Part("SecurityCode") RequestBody SecurityCode
    );

    @GET("gcl_AttendanceRegularization")
    Call<JsonObject> GetAttendanceRegularizationData(
            @Query("DbOperation") String DbOperation,
            @Query("empid") String empid,
            @Query("clientid") String clientid,
            @Query("fromdate") String fromdate,
            @Query("todate") String todate,
            @Query("SecurityCode") String SecurityCode
    );


    @Multipart
    @POST("post_reimbursementClaimByComponentMETSO_V1")
    Call<UploadObject> postMetsoReimbursementWithImage1(
            @Part("AEMEmployeeID") String AEMEmployeeID,
            @Part("AEMComponentID") String AEMComponentID,
            @Part("Description") String Description,
            @Part("ReimbursementAmount") String ReimbursementAmount,
            @Part("Year") String Year,
            @Part("Month") String Month,
            @Part("SecurityCode") String SecurityCode,
            @Part MultipartBody.Part file,
            @Part("ConveyanceTypeId") String ConveyanceTypeId,
            @Part("LocationTypeID") String LocationTypeID,
            @Part("ReimbursementDate") String ReimbursementDate,
            @Part("CostCentreId") String CostCentreId,
            @Part("WbsId") String WbsId,
            @Part("Siteid") String Siteid,
            @Part("SupervisorID") String SupervisorID,
            @Part("StartDate") String StartDate,
            @Part("EndDate") String EndDate
    );

    @Multipart
    @POST("post_reimbursementClaimByComponentMETSO_V1")
    Call<UploadObject> postMetsoReimbursementWithImage2(
            @Part("AEMEmployeeID") String AEMEmployeeID,
            @Part("AEMComponentID") String AEMComponentID,
            @Part("Description") String Description,
            @Part("ReimbursementAmount") String ReimbursementAmount,
            @Part("Year") String Year,
            @Part("Month") String Month,
            @Part("SecurityCode") String SecurityCode,
            @Part MultipartBody.Part file,
            @Part MultipartBody.Part fil1,
            @Part("ConveyanceTypeId") String ConveyanceTypeId,
            @Part("LocationTypeID") String LocationTypeID,
            @Part("ReimbursementDate") String ReimbursementDate,
            @Part("CostCentreId") String CostCentreId,
            @Part("WbsId") String WbsId,
            @Part("Siteid") String Siteid,
            @Part("SupervisorID") String SupervisorID,
            @Part("StartDate") String StartDate,
            @Part("EndDate") String EndDate
    );


    @Multipart
    @POST("post_reimbursementClaimByComponentMETSO_V1")
    Call<UploadObject> postMetsoReimbursementWithImage3(
            @Part("AEMEmployeeID") String AEMEmployeeID,
            @Part("AEMComponentID") String AEMComponentID,
            @Part("Description") String Description,
            @Part("ReimbursementAmount") String ReimbursementAmount,
            @Part("Year") String Year,
            @Part("Month") String Month,
            @Part("SecurityCode") String SecurityCode,
            @Part MultipartBody.Part file,
            @Part MultipartBody.Part fil1,
            @Part MultipartBody.Part fil2,
            @Part("ConveyanceTypeId") String ConveyanceTypeId,
            @Part("LocationTypeID") String LocationTypeID,
            @Part("ReimbursementDate") String ReimbursementDate,
            @Part("CostCentreId") String CostCentreId,
            @Part("WbsId") String WbsId,
            @Part("Siteid") String Siteid,
            @Part("SupervisorID") String SupervisorID,
            @Part("StartDate") String StartDate,
            @Part("EndDate") String EndDate
    );

    @Multipart
    @POST("post_reimbursementClaimByComponentMETSO_V1")
    Call<UploadObject> postMetsoReimbursementWithImage4(
            @Part("AEMEmployeeID") String AEMEmployeeID,
            @Part("AEMComponentID") String AEMComponentID,
            @Part("Description") String Description,
            @Part("ReimbursementAmount") String ReimbursementAmount,
            @Part("Year") String Year,
            @Part("Month") String Month,
            @Part("SecurityCode") String SecurityCode,
            @Part MultipartBody.Part file,
            @Part MultipartBody.Part fil1,
            @Part MultipartBody.Part fil2,
            @Part MultipartBody.Part fil3,
            @Part("ConveyanceTypeId") String ConveyanceTypeId,
            @Part("LocationTypeID") String LocationTypeID,
            @Part("ReimbursementDate") String ReimbursementDate,
            @Part("CostCentreId") String CostCentreId,
            @Part("WbsId") String WbsId,
            @Part("Siteid") String Siteid,
            @Part("SupervisorID") String SupervisorID,
            @Part("StartDate") String StartDate,
            @Part("EndDate") String EndDate
    );

    @Multipart
    @POST("post_reimbursementClaimByComponentMETSO_V1")
    Call<UploadObject> postMetsoReimbursementWithImage5(
            @Part("AEMEmployeeID") String AEMEmployeeID,
            @Part("AEMComponentID") String AEMComponentID,
            @Part("Description") String Description,
            @Part("ReimbursementAmount") String ReimbursementAmount,
            @Part("Year") String Year,
            @Part("Month") String Month,
            @Part("SecurityCode") String SecurityCode,
            @Part MultipartBody.Part file,
            @Part MultipartBody.Part fil1,
            @Part MultipartBody.Part fil2,
            @Part MultipartBody.Part fil3,
            @Part MultipartBody.Part fil4,
            @Part("ConveyanceTypeId") String ConveyanceTypeId,
            @Part("LocationTypeID") String LocationTypeID,
            @Part("ReimbursementDate") String ReimbursementDate,
            @Part("CostCentreId") String CostCentreId,
            @Part("WbsId") String WbsId,
            @Part("Siteid") String Siteid,
            @Part("SupervisorID") String SupervisorID,
            @Part("StartDate") String StartDate,
            @Part("EndDate") String EndDate
    );
}
