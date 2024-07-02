package io.cordova.myapp00d753.utility;

public class AppData {
    public static String url="https://gsppi.geniusconsultant.com/GeniusiOSApi/api/";
    public static String localurl="http://171.16.2.67/GSPPI_API_V2/api/";
    public static String newv2url="https://gsppi.geniusconsultant.com/GSPPI_API_V2/api/";

    //http://gsppi.geniusconsultant.com/

    public static String GCL_KYC=newv2url+"Profile/GetKYCDetails";
    public static String MENU=newv2url+"Dashboard/GetMobileAppMenuList";
    public static String EMPLOYEE_DOCUMENT_MANAGE=newv2url+"Document/EmployeeDocumentManage";
    public static String DOCUMENT_UPLOAD_INFO=newv2url+"Document/GetDigitalDocumentUploadInfo";
    public static String DIGITAL_DOCUMENT_TYPE = newv2url+"Document/GetDigitalDocumentType";
    public static String SAVE_EMPLOYEE_DIGITAL_DOCUMENT = newv2url+"Document/SaveEmployeeDigitalDocument";
    public static String GET_ATTENDANCE_CALENDER = newv2url+"Attendance/GetAttendancecalender";
    public static String SAVE_WEEKLY_OFF_APPLICATION = newv2url+"Attendance/SaveWOApplication";

    public static String GET_EMPLOYEE_SALARY = newv2url+"Payroll/GetEmpSalary";
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
    public static String GET_METSO_ATTENDANCE_DATA = newv2url+"Attendance/GetMetsoAttendanceData";
}
