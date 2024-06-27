package io.cordova.myapp00d753.utility;

public class AppData {
    public static String url="https://gsppi.geniusconsultant.com/GeniusiOSApi/api/";
    public static String localurl="http://171.16.2.67/GSPPI_API_V2/api/";
    public static String newv2url="https://gsppi.geniusconsultant.com/GSPPI_API_V2/api/";

    //http://gsppi.geniusconsultant.com/

    //gcl_KYC
    public static String GCL_KYC=newv2url+"Profile/GetKYCDetails";
    public static String MENU=newv2url+"Dashboard/GetMobileAppMenuList";
    public static String EMPLOYEE_DOCUMENT_MANAGE=newv2url+"Document/EmployeeDocumentManage";
    public static String DOCUMENT_UPLOAD_INFO=newv2url+"Document/GetDigitalDocumentUploadInfo";
    public static String DIGITAL_DOCUMENT_TYPE = newv2url+"Document/GetDigitalDocumentType";
    public static String GET_ATTENDANCE_CALENDER = newv2url+"Attendance/GetAttendancecalender";
    public static String SAVE_WO_APPLICATION = newv2url+"Attendance/SaveWOApplication";
}
