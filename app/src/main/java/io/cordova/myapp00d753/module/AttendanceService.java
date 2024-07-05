package io.cordova.myapp00d753.module;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

/**
 * Created by Robert
 */

 public  interface AttendanceService {
    /*@Multipart
    @POST("/upload_multi_files/MultiUpload.php")
    Call<ResponseBody> uploadFile(@Part MultipartBody.Part file, @Part("name") RequestBody name);*/
    @Multipart
    @POST("post_attedance")
    Call<UploadObject> uploadSingleFile(@Part MultipartBody.Part file, @Part("AEMEmployeeID") String AEMEmployeeID, @Part("Address") String Address, @Part("Longitude") String Longitude, @Part("Latitude") String Latitude, @Part("SecurityCode") String SecurityCode);

    @Multipart
    //@POST("/upload_multi_files/MultiUpload.php")
    @POST("/upload_multi_files/MultiPartUpload.php")
    Call<UploadObject> uploadMultiFile(@Part MultipartBody.Part file1, @Part MultipartBody.Part file2, @Part MultipartBody.Part file3);

    //@Multipart
    //@FormUrlEncoded
    //@POST("/upload_multi_files/MultiUpload.php")
    @POST("/upload_multi_files/MultiPartUpload.php")
    Call<ResponseBody> uploadMultiFile(@Body RequestBody file);

    @FormUrlEncoded
    @PUT("/api/register")
    Call<ResponseBody> getStatus(
            @Field("username") String username,
            @Field("email") String email,
            @Field("password") String password,
            @Field("fbID") String fbID,
            @Field("gmailID") String gmailID,
            @Field("twitID") String twitID,
            @Field("gender") String gender,
            @Field("birthDate") String birthDate,
            @Field("location") String location,
            @Field("longitude") String longitude,
            @Field("latitude") String latitude,
            @Field("profileImage") String profileImage);
    //@Field parameters can only be used with form encoding. (parameter #2)

   @Multipart
   @POST("post_empdigitaldocument")
   Call<UploadObject> uploadDocument(@Part MultipartBody.Part file,
                                     @Part("AEMEmployeeID") String AEMEmployeeID,
                                     @Part("DocumentID") String DocumentID,
                                     @Part("ReferenceNo") String ReferenceNo,
                                     @Part("SecurityCode") String SecurityCode);


    @Multipart
    @POST("post_KycUpdate")
    Call<UploadObject> updatekyc(@Part("AEMEmployeeID") String AEMEmployeeID, @Part("Code") String Code, @Part("DOJ") String DOJ, @Part("Department") String Department, @Part("Designation") String Designation, @Part("City") String PlaceOfPostingCity, @Part("OLDESINO") String OLDESINO, @Part("OLDUANNO") String OLDUANNO, @Part("Sex") String Sex, @Part("GuardianName") String GuardianName, @Part("RelationShip") String RelationShip, @Part("DateOfBirth") String DateOfBirth, @Part("Qualification") String Qualification, @Part("MaritalStatus") String MaritalStatus, @Part("BloodGroup") String BloodGroup, @Part("PermanentAddress") String PermanentAddress, @Part("PermanentCity") String PermanentCity, @Part("PermanentPinCode") String PermanentPinCode, @Part("PresentAddress") String PresentAddress, @Part("PresentCity") String PresentCity, @Part("PresentPincode") String PresentPincode, @Part("Phone") String Phone, @Part("Mobile") String Mobile, @Part("EmailID") String EmailID, @Part("CreatedBy") String CreatedBy, @Part("SecurityCode") String SecurityCode, @Part("AadhaarNo") String AadhaarNo, @Part("DocumentID") String DocumentID, @Part MultipartBody.Part file);
    @Multipart
    @POST("post_KycUpdatedByEmployee")
    Call<UploadObject> kycupdate(@Part("AEMEmployeeID") String AEMEmployeeID, @Part("GuardianName") String GuardianName, @Part("RelationShip") String RelationShip, @Part("Qualification") String Qualification, @Part("MaritalStatus") String MaritalStatus, @Part("BloodGroup") String BloodGroup, @Part("PermanentAddress") String PermanentAddress, @Part("PermanentCityID") String PermanentCityID, @Part("PermanentPinCode") String PermanentPinCode, @Part("PresentAddress") String PresentAddress, @Part("PresentCityID") String PresentCityID, @Part("PresentPincode") String PresentPincode, @Part("Phone") String Phone, @Part("Mobile") String Mobile, @Part("EmailID") String EmailID, @Part("UserName") String UserName, @Part("SecurityCode") String SecurityCode, @Part("Operation") String Operation);


    @Multipart
    @POST("post_reimbursementClaimByComponent")
    Call<UploadObject> postreimburstmentwithimage1(@Part("AEMEmployeeID") String AEMEmployeeID,
                                                   @Part("AEMComponentID") String AEMComponentID,
                                                   @Part("Description") String Description,
                                                   @Part("ReimbursementAmount") String ReimbursementAmount,
                                                   @Part("Year") String Year,
                                                   @Part("Month") String Month,
                                                   @Part("SecurityCode") String SecurityCode,
                                                   @Part MultipartBody.Part file,
                                                   @Part("ConveyanceTypeId") String ConveyanceTypeId,
                                                   @Part("LocationTypeID") String LocationTypeID,
                                                   @Part("ReimbursementDate") String ReimbursementDate);

    @Multipart
    @POST("post_reimbursementClaimByComponent")
    Call<UploadObject> postreimburstmentwithimage2(@Part("AEMEmployeeID") String AEMEmployeeID,
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
                                                   @Part("ReimbursementDate") String ReimbursementDate);
    @Multipart
    @POST("post_reimbursementClaimByComponent")
    Call<UploadObject> postreimburstmentwithimage3(@Part("AEMEmployeeID") String AEMEmployeeID, @Part("AEMComponentID") String AEMComponentID, @Part("Description") String Description, @Part("ReimbursementAmount") String ReimbursementAmount, @Part("Year") String Year, @Part("Month") String Month, @Part("SecurityCode") String SecurityCode, @Part MultipartBody.Part file, @Part MultipartBody.Part fil1, @Part MultipartBody.Part fil2, @Part("ConveyanceTypeId") String ConveyanceTypeId, @Part("LocationTypeID") String LocationTypeID, @Part("ReimbursementDate") String ReimbursementDate);
    @Multipart
    @POST("post_reimbursementClaimByComponent")
    Call<UploadObject> postreimburstmentwithimage4(@Part("AEMEmployeeID") String AEMEmployeeID,
                                                   @Part("AEMComponentID") String AEMComponentID,
                                                   @Part("Description") String Description,
                                                   @Part("ReimbursementAmount") String ReimbursementAmount,
                                                   @Part("Year") String Year, @Part("Month") String Month,
                                                   @Part("SecurityCode") String SecurityCode,
                                                   @Part MultipartBody.Part file,
                                                   @Part MultipartBody.Part fil1,
                                                   @Part MultipartBody.Part fil2,
                                                   @Part MultipartBody.Part fil3,
                                                   @Part("ConveyanceTypeId") String ConveyanceTypeId, @Part("LocationTypeID") String LocationTypeID, @Part("ReimbursementDate") String ReimbursementDate);
    @Multipart
    @POST("post_reimbursementClaimByComponent")
    Call<UploadObject> postreimburstmentwithimage5(@Part("AEMEmployeeID") String AEMEmployeeID, @Part("AEMComponentID") String AEMComponentID, @Part("Description") String Description, @Part("ReimbursementAmount") String ReimbursementAmount, @Part("Year") String Year, @Part("Month") String Month, @Part("SecurityCode") String SecurityCode, @Part MultipartBody.Part file, @Part MultipartBody.Part fil1, @Part MultipartBody.Part fil2, @Part MultipartBody.Part fil3, @Part MultipartBody.Part fil4, @Part("ConveyanceTypeId") String ConveyanceTypeId, @Part("LocationTypeID") String LocationTypeID, @Part("ReimbursementDate") String ReimbursementDate);


    @Multipart
    @POST("post_DummyEmployeeBank")
    Call<UploadObject> uploadbankdetails(@Part MultipartBody.Part file, @Part("AEMEmployeeID") String AEMEmployeeID, @Part("FirstNameAsperBank") String FirstNameAsperBank, @Part("LastNameAsperBank") String LastNameAsperBank, @Part("BankName") String BankName, @Part("AccountNumber") String AccountNumber, @Part("IFSCode") String IFSCode, @Part("SecurityCode") String SecurityCode, @Part("DocumentID") String DocumentID);


    @Multipart
    @POST("post_DummyEmployeeNominee")
    Call<UploadObject> uploadnominne(@Part("AEMEmployeeID") String AEMEmployeeID, @Part("MemberName1") String MemberName1, @Part("MemberRelationship1") String MemberRelationship1, @Part("MemberDOB1") String MemberDOB1, @Part("MemberAadhar1") String MemberAadhar1, @Part("MemType1") String MemType1, @Part("NomStatus1") String NomStatus1, @Part("MemberName2") String MemberName2, @Part("MemberRelationship2") String MemberRelationship2, @Part("MemberDOB2") String MemberDOB2, @Part("MemberAadhar2") String MemberAadhar2, @Part("MemType2") String MemType2, @Part("NomStatus2") String NomStatus2, @Part("MemberName3") String MemberName3, @Part("MemberRelationship3") String MemberRelationship3, @Part("MemberDOB3") String MemberDOB3, @Part("MemberAadhar3") String MemberAadhar3, @Part("MemType3") String MemType3, @Part("NomStatus3") String NomStatus3, @Part("SecurityCode") String SecurityCode);


    @Multipart
    @POST("post_reimbursementClaimByComponent")
    Call<UploadObject> postreimburstmentwithimage1Recktt(@Part("AEMEmployeeID") String AEMEmployeeID, @Part("AEMComponentID") String AEMComponentID, @Part("Description") String Description, @Part("ReimbursementAmount") String ReimbursementAmount, @Part("Year") String Year, @Part("Month") String Month, @Part("SecurityCode") String SecurityCode, @Part MultipartBody.Part file, @Part("ConveyanceTypeId") String ConveyanceTypeId, @Part("LocationTypeID") String LocationTypeID);

    @Multipart
    @POST("post_reimbursementClaimByComponent")
    Call<UploadObject> postreimburstmentwithimage2Recktt(@Part("AEMEmployeeID") String AEMEmployeeID, @Part("AEMComponentID") String AEMComponentID, @Part("Description") String Description, @Part("ReimbursementAmount") String ReimbursementAmount, @Part("Year") String Year, @Part("Month") String Month, @Part("SecurityCode") String SecurityCode, @Part MultipartBody.Part file, @Part MultipartBody.Part fil1, @Part("ConveyanceTypeId") String ConveyanceTypeId, @Part("LocationTypeID") String LocationTypeID);
    @Multipart
    @POST("post_reimbursementClaimByComponent")
    Call<UploadObject> postreimburstmentwithimage3Recktt(@Part("AEMEmployeeID") String AEMEmployeeID, @Part("AEMComponentID") String AEMComponentID, @Part("Description") String Description, @Part("ReimbursementAmount") String ReimbursementAmount, @Part("Year") String Year, @Part("Month") String Month, @Part("SecurityCode") String SecurityCode, @Part MultipartBody.Part file, @Part MultipartBody.Part fil1, @Part MultipartBody.Part fil2, @Part("ConveyanceTypeId") String ConveyanceTypeId, @Part("LocationTypeID") String LocationTypeID);
    @Multipart
    @POST("post_reimbursementClaimByComponent")
    Call<UploadObject> postreimburstmentwithimage4Recktt(@Part("AEMEmployeeID") String AEMEmployeeID, @Part("AEMComponentID") String AEMComponentID, @Part("Description") String Description, @Part("ReimbursementAmount") String ReimbursementAmount, @Part("Year") String Year, @Part("Month") String Month, @Part("SecurityCode") String SecurityCode, @Part MultipartBody.Part file, @Part MultipartBody.Part fil1, @Part MultipartBody.Part fil2, @Part MultipartBody.Part fil3, @Part("ConveyanceTypeId") String ConveyanceTypeId, @Part("LocationTypeID") String LocationTypeID);
    @Multipart
    @POST("post_reimbursementClaimByComponent")
    Call<UploadObject> postreimburstmentwithimage5Recktt(@Part("AEMEmployeeID") String AEMEmployeeID, @Part("AEMComponentID") String AEMComponentID, @Part("Description") String Description, @Part("ReimbursementAmount") String ReimbursementAmount, @Part("Year") String Year, @Part("Month") String Month, @Part("SecurityCode") String SecurityCode, @Part MultipartBody.Part file, @Part MultipartBody.Part fil1, @Part MultipartBody.Part fil2, @Part MultipartBody.Part fil3, @Part MultipartBody.Part fil4, @Part("ConveyanceTypeId") String ConveyanceTypeId, @Part("LocationTypeID") String LocationTypeID);

    @Multipart
    @POST("post_EmployeeDailyActivity")
    Call<UploadObject> dailyactivityTATAGY(@Part MultipartBody.Part file,
                                           @Part("AEMEmployeeID") String AEMEmployeeID,
                                           @Part("ApprovalStatus") String ApprovalStatus,
                                           @Part("Remarks") String Remarks,
                                           @Part("Longitude") String Longitude,
                                           @Part("Latitude") String Latitude,
                                           @Part("Address") String Address,
                                           @Part("Year") String Year,
                                           @Part("Month") String Month,
                                           @Part("SecurityCode") String SecurityCode,
                                           @Part("FName") String FName);

    @Multipart
    @POST("post_reimbursementClaimFMS_SEC")
    Call<UploadObject> FMSpostreimburstmentwithimage1(@Part("AEMEmployeeID") String AEMEmployeeID, @Part("AEMComponentID") String AEMComponentID, @Part("Description") String Description, @Part("ReimbursementAmount") String ReimbursementAmount, @Part("Year") String Year, @Part("Month") String Month, @Part("SecurityCode") String SecurityCode, @Part MultipartBody.Part file, @Part("ConveyanceTypeId") String ConveyanceTypeId, @Part("LocationTypeID") String LocationTypeID, @Part("ReimbursementDate") String ReimbursementDate, @Part("SupervisorID") String SupervisorID);


    @Multipart
    @POST("post_reimbursementClaimFMS_SEC")
    Call<UploadObject> FMSpostreimburstmentwithimage2(@Part("AEMEmployeeID") String AEMEmployeeID, @Part("AEMComponentID") String AEMComponentID, @Part("Description") String Description, @Part("ReimbursementAmount") String ReimbursementAmount, @Part("Year") String Year, @Part("Month") String Month, @Part("SecurityCode") String SecurityCode, @Part MultipartBody.Part file, @Part MultipartBody.Part fil1, @Part("ConveyanceTypeId") String ConveyanceTypeId, @Part("LocationTypeID") String LocationTypeID, @Part("ReimbursementDate") String ReimbursementDate,@Part("SupervisorID") String SupervisorID);


    @Multipart
    @POST("post_reimbursementClaimFMS_SEC")
    Call<UploadObject> FMSpostreimburstmentwithimage3(@Part("AEMEmployeeID") String AEMEmployeeID, @Part("AEMComponentID") String AEMComponentID, @Part("Description") String Description, @Part("ReimbursementAmount") String ReimbursementAmount, @Part("Year") String Year, @Part("Month") String Month, @Part("SecurityCode") String SecurityCode, @Part MultipartBody.Part file, @Part MultipartBody.Part fil1, @Part MultipartBody.Part fil2, @Part("ConveyanceTypeId") String ConveyanceTypeId, @Part("LocationTypeID") String LocationTypeID, @Part("ReimbursementDate") String ReimbursementDate,@Part("SupervisorID") String SupervisorID);

    @Multipart
    @POST("post_reimbursementClaimFMS_SEC")
    Call<UploadObject> FMSpostreimburstmentwithimage4(@Part("AEMEmployeeID") String AEMEmployeeID, @Part("AEMComponentID") String AEMComponentID, @Part("Description") String Description, @Part("ReimbursementAmount") String ReimbursementAmount, @Part("Year") String Year, @Part("Month") String Month, @Part("SecurityCode") String SecurityCode, @Part MultipartBody.Part file, @Part MultipartBody.Part fil1, @Part MultipartBody.Part fil2, @Part MultipartBody.Part fil3, @Part("ConveyanceTypeId") String ConveyanceTypeId, @Part("LocationTypeID") String LocationTypeID, @Part("ReimbursementDate") String ReimbursementDate,@Part("SupervisorID") String SupervisorID);

    @Multipart
    @POST("post_reimbursementClaimFMS_SEC")
    Call<UploadObject> FMSpostreimburstmentwithimage5(@Part("AEMEmployeeID") String AEMEmployeeID, @Part("AEMComponentID") String AEMComponentID, @Part("Description") String Description, @Part("ReimbursementAmount") String ReimbursementAmount, @Part("Year") String Year, @Part("Month") String Month, @Part("SecurityCode") String SecurityCode, @Part MultipartBody.Part file, @Part MultipartBody.Part fil1, @Part MultipartBody.Part fil2, @Part MultipartBody.Part fil3, @Part MultipartBody.Part fil4, @Part("ConveyanceTypeId") String ConveyanceTypeId, @Part("LocationTypeID") String LocationTypeID, @Part("ReimbursementDate") String ReimbursementDate,@Part("SupervisorID") String SupervisorID);

}
