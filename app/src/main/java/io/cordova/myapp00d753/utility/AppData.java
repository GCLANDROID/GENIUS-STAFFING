package io.cordova.myapp00d753.utility;

public class AppData {
    public static final String COMPANY_NAME = "Genius Consultants Limited";
    public static String url="https://gsppi.geniusconsultant.com/GeniusiOSApi/api/";
    public static String localurl="http://171.16.2.105/GSPPI_API_V2/api/";
    public static String newv2url="https://gsppi.geniusconsultant.com/GSPPI_API_V2/api/";
    //public static String newv2url="http://171.16.2.105/GSPPI_API_V2/api/";

    //http://gsppi.geniusconsultant.com/

    public static String GCL_KYC=newv2url+"Profile/GetKYCDetails";
    public static String MENU=newv2url+"Dashboard/GetMobileAppMenuListV2";
    public static String EMPLOYEE_DOCUMENT_MANAGE=newv2url+"Document/EmployeeDocumentManage";
    public static String DOCUMENT_UPLOAD_INFO=newv2url+"Document/GetDigitalDocumentUploadInfo";
    public static String DIGITAL_DOCUMENT_TYPE = newv2url+"Document/GetDigitalDocumentType";
    public static String SAVE_EMPLOYEE_DIGITAL_DOCUMENT = newv2url+"Document/SaveEmployeeDigitalDocument";
    public static String GET_ATTENDANCE_CALENDER = newv2url+"Attendance/GetAttendancecalender";
    public static String SAVE_WEEKLY_OFF_APPLICATION = newv2url+"Attendance/SaveWOApplication";

    public static String GET_EMPLOYEE_SALARY = newv2url+"Payroll/GetEmpSalaryV2";
    public static String PAYMENT_LINK = newv2url+"Payroll/InitializePaymentGateway";
    public static String GET_EMPLOYEE_DISBURSED_PAYOUT = newv2url+"Payroll/GetEmpDisbursedPayout";
    public static String GET_COMMON_DROP_DOWN_FILL = newv2url+"Sales/GetCommonDropDownFill";
    public static String SAVE_REIMBURSEMENT_CLAIM_BY_COMPONENT = newv2url+"Reimbursement/SaveReimbursementClaimByComponent";
    public static String GET_REIMBURSEMENT_CLAIM = newv2url+"Reimbursement/GetReimbursementClaim";
    public static String SAVE_REIMBURSEMENT_CLAIM_METSO = newv2url+"Reimbursement/SaveReimbursementClaim_METSO";
    public static String GET_REIMBURSEMENT_CLAIM_COMPONENT_RECKITT = newv2url+"Reimbursement/GetReimbursementComponent_Reckitt";

    public static String GET_LEAVE_APPLICATION_APPROVER = newv2url+"Leave/GetLeaveApplicationApprover";
    public static String GET_LEAVE_APPLICATION_DETAILS = newv2url+"Leave/GetLeaveApplicationDetails";
    public static String GET_LEAVE_MODE = newv2url+"Leave/GetLeaveMode";
    public static String CHECK_LEAVE_START_DATE_STATUS = newv2url+"Leave/CheckLeaveStartDayStatus";
    public static String GET_DAY_DETAILS = newv2url+"Leave/GetDayDetails";
    public static String LEAVE_BIND_VIEW_SUMMARY= newv2url+"Leave/BindViewSummary";
    public static String GET_APPLICANT_LEAVE_APPLICATION = newv2url+"Leave/GetApplicantLeaveApplication";
    public static String GET_APPROVER_LEAVE_APPLICATION = newv2url+"Leave/GetApproverLeaveApplication";
    public static String LEAVE_APPLICATION_APPROVAL = newv2url+"Leave/LeaveApplicationApproval";
    public static String LEAVE_DELETE_BY_APPROVER = newv2url+"Leave/LeaveDeletebyApprover";
    public static String GET_OD_AND_OT_OTHER_APPLICATION = newv2url+"Leave/GetODAndOTOtherApplication";
    public static String ATT_SAVE_EMPLOYEE_DIGITAL_DOCUMENT = newv2url+"Attendance/SaveEmployeeDigitalDocument";
    public static String CHECK_LEAVE_VIEW_SUMMARY = newv2url+"Attendance/CheckLeaveViewSummary";
    public static String GET_METSO_ATTENDANCE_DATA = newv2url+"Attendance/GetMetsoAttendanceData";
    public static String ADD_LEAVE_METSO = newv2url+"Attendance/AddLeaveMetso";
    public static String GET_SELF_ATTENDANCE_WO_LEAVE = newv2url+"Attendance/GetSelfAttendanceWoLeave";
    public static String GET_OFFLINE_DAILY_LOG_ACTIVITY = newv2url+"Attendance/GetOfflineDailyLogActivity";
    public static String POST_EMPLOYEE_DAILY_ACTIVITY = newv2url+"FileUpload/PostEmployeeDailyActivity";
    //171.16.2.105
    public static String GET_ATTENDANCE_REGULARIZATION = newv2url+"Attendance/GetAttendanceRegularization";
    public static String GET_ATTENDANCE_REGULARIZATION_LOCAL_IP = "http://171.16.2.105/GSPPI_API_V2/api/Attendance/GetAttendanceRegularization";

    public static String SAVE_ATTENDANCE_REGULARIZATION = newv2url+"Attendance/SaveAttendanceRegularization";
    public static String GET_SHIFT = newv2url+"Attendance/GetShift";
    public static String SaveHolidayleave = newv2url+"SKF/SaveHolidayleave";
    public static String APPID = "umanjv";
    public static String APPKEY = "qf3mcm6156t1e0z006q2";

   /* public static String APPID = "89gnhe";
    public static String APPKEY = "x1xndaf7txgkxvl4vr1g";*/


    public static String AADAHARNUMBER = "";
    public static String ADHARIMAGE = "";
    public static String ADHARDOB = "";
    public static String PERMANENTADDRESS = "";
    public static String COMPANYNAME = "";

    public static String SAVE_EMPLOYEE_SALES_WITHOUT_INVOICE = newv2url+"Sales/SaveEmpSalesWithoutInvoice";
    public static String GET_EMPLOYEE_SALES_REPORT = newv2url+"Sales/GetEmpSalesReport";
    public static String GET_EMPLOYEE_DAILY_VISIT_MANAGE = newv2url+"Sales/GetEmployeeDailyVisitManage";


    //public static String SKF_GET_HOLIDAY_DETAILS = "http://171.16.2.67/GSPPI_API_V2/api/SKF/GetHolidayDetails";
    public static String SKF_GET_HOLIDAY_DETAILS = newv2url+"SKF/GetHolidayDetails";
    public static String SKF_SAVE_ATTENDANCE_REGULARIZATION = newv2url+"SKF/SaveAttendanceRegularization";
    public static String SKF_SAVE_ATTENDANCE_REGULARIZATION_LOCAL_IP = "http://171.16.2.105/GSPPI_API_V2/api/SKF/SaveAttendanceRegularization";
    public static String SAVE_ATTENDANCE_REGULARIZATION_NEW= newv2url+"Attendance/SaveAttendanceRegularizationNew";
    public static String SAVE_ATTENDANCE_REGULARIZATION_NEW_LOCAL_IP = "http://171.16.2.105/GSPPI_API_V2/api/Attendance/SaveAttendanceRegularizationNew";
    public static String SKF_DAY_TYPE_LIST = newv2url+"SKF/GetWorkingDayTypeSKF";
    public static String SKF_GET_HOLIDAY_LIST = newv2url+"SKF/GetHolidayList";

    //public static String SKF_DAY_TYPE_LIST= "http://171.16.2.105/GSPPI_API_V2/api/SKF/GetWorkingDayTypeSKF";
    public static String SaveExperienceDetails = newv2url+"Document/SaveExperienceDetails";
    public static String PROTACTORGAMBLEID = "AEMCLI1310000776";
    //public static String GetPFNotificationAPI=newv2url+"PFNotification/GetPFNotification";
    public static String GetPFNotificationAPI=newv2url+"PFNotification/GetPFNotification_V1";
    public static String GetEmpConsentPendingListAPI=newv2url+"KYC/GetEmpConsentPendingList";

    //KYC
    public static String KYC_GET_DETAILS = newv2url+"Profile/GetTempEmpKYCDetails";
    public static String CheckAadhar = newv2url+"KYC/CheckAadhar";

    public static String COMMON_DDL = newv2url+"General/CommonDDL";
    public static String SAVE_EMP_DIGITAL_DOCUMENT = newv2url+"Document/SaveEmpDigitalDocument";
    public static String SAVE_DUMMY_EMP_BANK_DOCUMENT = newv2url+"Document/SaveDummyEmpBankDetails";
    public static String Post_AttedanceGeofence_WFH = newv2url+"Attendance/Post_AttedanceGeofence_WFH";
    public static String IMAGE_PATH_URL = "https://gsppi.geniusconsultant.com/FSS/DigitalDocument/";
    public static String Open_Leave_Balance_Details = newv2url+"Leave/OpenLeaveBalanceDetails";
    public static String LEAVE_BALANCE_VIEW_FLAG = "0";
}
