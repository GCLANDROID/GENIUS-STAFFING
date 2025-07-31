package io.cordova.myapp00d753.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kiit on 05-01-2018.
 */

public class Pref {
    private SharedPreferences _pref;
    private static final String PREF_FILE = "com.deus";
    private SharedPreferences.Editor _editorPref;

    @SuppressLint("CommitPrefEdits")
    public Pref(Context context) {
        _pref = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        _editorPref = _pref.edit();
    }


    public void saveFlag(String flag){
        _editorPref.putString("flag", flag);
        _editorPref.commit();
    }

    public String getFlag(){
        return _pref.getString("flag","");
    }

    public void saveEmpName(String name){
        _editorPref.putString("name", name);
        _editorPref.commit();
    }

    public String getEmpName(){
        return _pref.getString("name","");
    }

       public void saveCtime(String ctime){
        _editorPref.putString("ctime", ctime);
        _editorPref.commit();
    }

    public String getCtime(){
        return _pref.getString("ctime","");
    }

    public void saveloginTime(String ltime){
        _editorPref.putString("ltime", ltime);
        _editorPref.commit();
    }

    public String getloginTime(){
        return _pref.getString("ltime","");
    }


    public void saveEmpId(String empid){
        _editorPref.putString("empid", empid);
        _editorPref.commit();
    }

    public String getEmpId(){
        return _pref.getString("empid","");
    }

    public void saveMenu(String menu){
        _editorPref.putString("menu", menu);
        _editorPref.commit();
    }

    public String getMenu(){
        return _pref.getString("menu","");
    }

    public void saveEmpConId(String emconid){
        _editorPref.putString("emconid", emconid);
        _editorPref.commit();
    }

    public String getEmpConId(){
        return _pref.getString("emconid","");
    }


    public void saveEmpClintId(String emcclid){
        _editorPref.putString("emcclid", emcclid);
        _editorPref.commit();
    }

    public String getEmpClintId(){
        return _pref.getString("emcclid","");
    }

    public void saveEmpClintOffId(String emccloid){
        _editorPref.putString("emccloid", emccloid);
        _editorPref.commit();
    }

    public String getEmpClintOffId(){
        return _pref.getString("emccloid","");
    }

    public void saveMasterId(String Master){
        _editorPref.putString("Master", Master);
        _editorPref.commit();
    }

    public String getMasterId(){
        return _pref.getString("Master","");
    }

    public void saveUserType(String UserType){
        _editorPref.putString("UserType", UserType);
        _editorPref.commit();
    }

    public String getUserType(){
        return _pref.getString("UserType","");
    }


    public void saveCTCURL(String CTCURL){
        _editorPref.putString("CTCURL", CTCURL);
        _editorPref.commit();
    }

    public String getCTCURL(){
        return _pref.getString("CTCURL","");
    }


    public void saveManageId(String ManageId){
        _editorPref.putString("ManageId", ManageId);
        _editorPref.commit();
    }

    public String getManageId(){
        return _pref.getString("Weeklyoff","");
    }

    public void saveWeeklyoff(String Weeklyoff){
        _editorPref.putString("Weeklyoff", Weeklyoff);
        _editorPref.commit();
    }

    public String getWeeklyoff(){
        return _pref.getString("Weeklyoff","");
    }

    public void saveOnLeave(String OnLeave){
        _editorPref.putString("OnLeave", OnLeave);
        _editorPref.commit();
    }

    public String getOnLeave(){
        return _pref.getString("OnLeave","");
    }

    public void saveLeaveUrl(String LeaveUrl){
        _editorPref.putString("LeaveUrl", LeaveUrl);
        _editorPref.commit();
    }

    public String getLeaveUrl(){
        return _pref.getString("LeaveUrl","");
    }


    public void saveSecurityCode(String SecurityCode){
        _editorPref.putString("SecurityCode", SecurityCode);
        _editorPref.commit();
    }

    public String getSecurityCode(){
        return _pref.getString("SecurityCode","");
    }


    public void saveBackAttd(String BackAttd){
        _editorPref.putString("BackAttd", BackAttd);
        _editorPref.commit();
    }

    public String getBackAttd(){
        return _pref.getString("BackAttd","");
    }

    public void saveAttdImg(String AttdImg){
        _editorPref.putString("AttdImg", AttdImg);
        _editorPref.commit();
    }

    public String getAttdImg(){
        return _pref.getString("AttdImg","");
    }


    public void saveSup(String Sup){
        _editorPref.putString("Sup", Sup);
        _editorPref.commit();
    }

    public String getSup(){
        return _pref.getString("Sup","");
    }

    public void saveAtteType(String AtteType){
        _editorPref.putString("AtteType", AtteType);
        _editorPref.commit();
    }

    public String getAtteType(){
        return _pref.getString("AtteType","");
    }


    public void saveFlagLocation(String FlagLocation){
        _editorPref.putString("FlagLocation", FlagLocation);
        _editorPref.commit();
    }

    public String getFlagLocation(){
        return _pref.getString("FlagLocation","");
    }


    public void savePassword(String Password){
        _editorPref.putString("Password", Password);
        _editorPref.commit();
    }

    public String getPassword(){
        return _pref.getString("Password","");
    }


    public void saveCheckFlag(String ckflag){
        _editorPref.putString("ckflag", ckflag);
        _editorPref.commit();
    }

    public String getCheckFlag(){
        return _pref.getString("ckflag","");
    }



    public void saveIntentFlag(String IntentFlag){
        _editorPref.putString("IntentFlag", IntentFlag);
        _editorPref.commit();
    }

    public String getIntentFlag(){
        return _pref.getString("IntentFlag","");
    }


    public void saveSkill(String Skill){
        _editorPref.putString("Skill", Skill);
        _editorPref.commit();
    }

    public String getSkill(){
        return _pref.getString("Skill","");
    }


    public void saveBankName(String BankName){
        _editorPref.putString("BankName", BankName);
        _editorPref.commit();
    }

    public String getBankName(){
        return _pref.getString("BankName","");
    }


    public void saveAccNumber(String AccNumber){
        _editorPref.putString("AccNumber", AccNumber);
        _editorPref.commit();
    }

    public String getAccNumber(){
        return _pref.getString("AccNumber","");
    }


    public void saveIFSC(String IFSC){
        _editorPref.putString("IFSC", IFSC);
        _editorPref.commit();
    }

    public String getIFSC(){
        return _pref.getString("IFSC","");
    }


    public void saveBFName(String BFName){
        _editorPref.putString("BFName", BFName);
        _editorPref.commit();
    }

    public String getBFName(){
        return _pref.getString("BFName","");
    }

    public void saveBLName(String BLName){
        _editorPref.putString("BLName", BLName);
        _editorPref.commit();
    }

    public String getBLName(){
        return _pref.getString("BLName","");
    }

    public void saveM1Name(String M1Name){
        _editorPref.putString("M1Name", M1Name);
        _editorPref.commit();
    }
    public String getM1Name() {
        return _pref.getString("M1Name", "");

    }

    public void saveM1RealationId(String M1RealationId) {
        _editorPref.putString("M1RealationId", M1RealationId);
        _editorPref.commit();
    }

    public String getM1RealationId() {
        return _pref.getString("M1RealationId", "");

    }

    public void saveM1DOB(String M1DOB) {
        _editorPref.putString("M1DOB", M1DOB);
        _editorPref.commit();
    }

    public String getM1DOB() {
        return _pref.getString("M1DOB", "");

    }

    public void saveM1Aadahar(String M1Aadahar) {
        _editorPref.putString("M1Aadahar", M1Aadahar);
        _editorPref.commit();
    }

    public String getM1Aadahar() {
        return _pref.getString("M1Aadahar", "");

    }


    //m2

    public void saveM2Name(String M2Name) {
        _editorPref.putString("M2Name", M2Name);
        _editorPref.commit();
    }

    public String getM2Name() {
        return _pref.getString("M2Name", "");

    }

    public void saveM2RealationId(String M2RealationId) {
        _editorPref.putString("M2RealationId", M2RealationId);
        _editorPref.commit();
    }

    public String getM2RealationId() {
        return _pref.getString("M2RealationId", "");

    }

    public void saveM2DOB(String M2DOB) {
        _editorPref.putString("M2DOB", M2DOB);
        _editorPref.commit();
    }

    public String getM2DOB() {
        return _pref.getString("M2DOB", "");

    }

    public void saveM2Aadahar(String M2Aadahar) {
        _editorPref.putString("M2Aadahar", M2Aadahar);
        _editorPref.commit();
    }

    public String getM2Aadahar() {
        return _pref.getString("M2Aadahar", "");

    }

    //m3

    public void saveM3Name(String M3Name) {
        _editorPref.putString("M3Name", M3Name);
        _editorPref.commit();
    }

    public String getM3Name() {
        return _pref.getString("M3Name", "");

    }

    public void saveM3RealationId(String M3RealationId) {
        _editorPref.putString("M2RealationId", M3RealationId);
        _editorPref.commit();
    }

    public String getM3RealationId() {
        return _pref.getString("M3RealationId", "");

    }

    public void saveM3DOB(String M3DOB) {
        _editorPref.putString("M3DOB", M3DOB);
        _editorPref.commit();
    }

    public String getM3DOB() {
        return _pref.getString("M3DOB", "");

    }

    public void saveM3Aadahar(String M3Aadahar) {
        _editorPref.putString("M3Aadahar", M3Aadahar);
        _editorPref.commit();
    }

    public String getM3Aadahar() {
        return _pref.getString("M3Aadahar", "");

    }
    public void saveNesPer(String NesPer) {
        _editorPref.putString("NesPer", NesPer);
        _editorPref.commit();
    }

    public String getNesPer() {
        return _pref.getString("NesPer", "");

    }

    public void saveContactPer(String ContactPer) {
        _editorPref.putString("ContactPer", ContactPer);
        _editorPref.commit();
    }

    public String getContactPer() {
        return _pref.getString("ContactPer", "");

    }


    public void saveDocPer(String DocPer) {
        _editorPref.putString("DocPer", DocPer);
        _editorPref.commit();
    }

    public String getDocPer() {
        return _pref.getString("DocPer", "");

    }

    public void saveFamilyPer(String FamilyPer) {
        _editorPref.putString("FamilyPer", FamilyPer);
        _editorPref.commit();
    }

    public String getFamilyPer() {
        return _pref.getString("FamilyPer", "");

    }

    public void saveMisPer(String MisPer) {
        _editorPref.putString("MisPer", MisPer);
        _editorPref.commit();
    }

    public String getMisPer() {
        return _pref.getString("MisPer", "");

    }



    public void saveOffAttnFlag(String OffAttnFlag) {
        _editorPref.putString("OffAttnFlag", OffAttnFlag);
        _editorPref.commit();
    }

    public String getOffAttnFlag() {
        return _pref.getString("OffAttnFlag", "");

    }


    public void saveSEmpID(String SEmpID) {
        _editorPref.putString("SEmpID", SEmpID);
        _editorPref.commit();
    }

    public String getSEmpID() {
        return _pref.getString("SEmpID", "");

    }


    public void saveSEmpCode(String SEmpCode) {
        _editorPref.putString("SEmpCode", SEmpCode);
        _editorPref.commit();
    }

    public String getSEmpCode() {
        return _pref.getString("SEmpCode", "");

    }


    public void saveSEmpName(String SEmpName) {
        _editorPref.putString("SEmpName", SEmpName);
        _editorPref.commit();
    }

    public String getSEmpName() {
        return _pref.getString("SEmpName", "");

    }


    public void saveSDOJ(String SDOJ) {
        _editorPref.putString("SDOJ", SDOJ);
        _editorPref.commit();
    }

    public String getSDOJ() {
        return _pref.getString("SDOJ", "");

    }


    public void saveSDept(String SDept) {
        _editorPref.putString("SDept", SDept);
        _editorPref.commit();
    }

    public String getSDept() {
        return _pref.getString("SDept", "");

    }



    public void saveSDes(String SDes) {
        _editorPref.putString("SDes", SDes);
        _editorPref.commit();
    }

    public String getSDes() {
        return _pref.getString("SDes", "");

    }


    public void saveSLocation(String SLocation) {
        _editorPref.putString("SLocation", SLocation);
        _editorPref.commit();
    }

    public String getSLocation() {
        return _pref.getString("SLocation", "");

    }



    public void saveSGender(String SGender) {
        _editorPref.putString("SGender", SGender);
        _editorPref.commit();
    }

    public String getSGender() {
        return _pref.getString("SGender", "");

    }


    public void saveSDOB(String SDOB) {
        _editorPref.putString("SDOB", SDOB);
        _editorPref.commit();
    }

    public String getSDOB() {
        return _pref.getString("SDOB", "");

    }


    public void saveSGurdian(String SGurdian) {
        _editorPref.putString("SGurdian", SGurdian);
        _editorPref.commit();
    }

    public String getSGurdian() {
        return _pref.getString("SGurdian", "");

    }


    public void saveSRelation(String SRelation) {
        _editorPref.putString("SRelation", SRelation);
        _editorPref.commit();
    }

    public String getSRelation() {
        return _pref.getString("SRelation", "");

    }


    public void saveSQualification(String SQualification) {
        _editorPref.putString("SQualification", SQualification);
        _editorPref.commit();
    }

    public String getSQualification() {
        return _pref.getString("SQualification", "");

    }


    public void saveSMartial(String SMartial) {
        _editorPref.putString("SMartial", SMartial);
        _editorPref.commit();
    }

    public String getSMartial() {
        return _pref.getString("SMartial", "");

    }


    public void saveSBlood(String SBlood) {
        _editorPref.putString("SBlood", SBlood);
        _editorPref.commit();
    }

    public String getSBlood() {
        return _pref.getString("SBlood", "");

    }


    public void saveSParAdd(String SParAdd) {
        _editorPref.putString("SParAdd", SParAdd);
        _editorPref.commit();
    }

    public String getSParAdd() {
        return _pref.getString("SParAdd", "");

    }


    public void saveSPerAdd(String SPerAdd) {
        _editorPref.putString("SPerAdd", SPerAdd);
        _editorPref.commit();
    }

    public String getSPerAdd() {
        return _pref.getString("SPerAdd", "");

    }


    public void saveSPhnNo(String SPhnNo) {
        _editorPref.putString("SPhnNo", SPhnNo);
        _editorPref.commit();
    }

    public String getSPhnNo() {
        return _pref.getString("SPhnNo", "");

    }


    public void saveSEmail(String SEmail) {
        _editorPref.putString("SEmail", SEmail);
        _editorPref.commit();
    }

    public String getSEmail() {
        return _pref.getString("SEmail", "");

    }


    public void saveSPF(String SPF) {
        _editorPref.putString("SPF", SPF);
        _editorPref.commit();
    }

    public String getSPF() {
        return _pref.getString("SPF", "");

    }

    public void saveSESI(String SESI) {
        _editorPref.putString("SESI", SESI);
        _editorPref.commit();
    }

    public String getSESI() {
        return _pref.getString("SESI", "");

    }

    public void saveSBank(String SBank) {
        _editorPref.putString("SBank", SBank);
        _editorPref.commit();
    }

    public String getSBank() {
        return _pref.getString("SBank", "");

    }



    public void saveSAcc(String SAcc) {
        _editorPref.putString("SAcc", SAcc);
        _editorPref.commit();
    }

    public String getSAcc() {
        return _pref.getString("SAcc", "");

    }


    public void saveSAadhar(String SAadhar) {
        _editorPref.putString("SAadhar", SAadhar);
        _editorPref.commit();
    }

    public String getSAadhar() {
        return _pref.getString("SAadhar", "");

    }

    public void saveSUAN(String SUAN) {
        _editorPref.putString("SUAN", SUAN);
        _editorPref.commit();
    }

    public String getSUAN() {
        return _pref.getString("SUAN", "");

    }


    public void saveOwnLong(String OwnLong){
        _editorPref.putString("OwnLong", OwnLong);
        _editorPref.commit();
    }

    public String getOwnLong(){
        return _pref.getString("OwnLong","");
    }

    public void saveOwnLat(String OwnLat){
        _editorPref.putString("OwnLat", OwnLat);
        _editorPref.commit();
    }

    public String getOwnLat(){
        return _pref.getString("OwnLat","");
    }

    public void saveEndLat(String EndLat){
        _editorPref.putString("EndLat", EndLat);
        _editorPref.commit();
    }

    public String getEndLat(){
        return _pref.getString("EndLat","");
    }


    public void saveEndLong(String EndLong){
        _editorPref.putString("EndLong", EndLong);
        _editorPref.commit();
    }

    public String getEndLong(){
        return _pref.getString("EndLong","");
    }


    public void saveEndPoint(String EndPoint){
        _editorPref.putString("EndPoint", EndPoint);
        _editorPref.commit();
    }

    public String getEndPoint(){
        return _pref.getString("EndPoint","");
    }



    public void saveDemoFlag(String DemoFlag){
        _editorPref.putString("DemoFlag", DemoFlag);
        _editorPref.commit();
    }

    public String getDemoFlag(){
        return _pref.getString("DemoFlag","");
    }


    public void saveFenceMenuFlag(String FenceMenuFlag){
        _editorPref.putString("FenceMenuFlag", FenceMenuFlag);
        _editorPref.commit();
    }

    public String getFenceMenuFlag(){
        return _pref.getString("FenceMenuFlag","");
    }



    public void saveFenceAttnFlag(String FenceAttnFlag){
        _editorPref.putString("FenceAttnFlag", FenceAttnFlag);
        _editorPref.commit();
    }

    public String getFenceAttnFlag(){
        return _pref.getString("FenceAttnFlag","");
    }


    public void saveFenceConfigFlag(String FenceConfigFlag){
        _editorPref.putString("FenceConfigFlag", FenceConfigFlag);
        _editorPref.commit();
    }

    public String getFenceConfigFlag(){
        return _pref.getString("FenceConfigFlag","");
    }


    public void saveMsg(String Msg){
        _editorPref.putString("Msg", Msg);
        _editorPref.commit();
    }

    public String getMsg(){
        return _pref.getString("Msg","");
    }


    public void saveMsgAlertStatus(boolean saveMsgAlertStatus){
        _editorPref.putBoolean("saveMsgAlertStatus", saveMsgAlertStatus);
        _editorPref.commit();
    }

    public boolean getMsgAlertStatus(){
        return _pref.getBoolean("saveMsgAlertStatus",false);
    }

    public void savePFURL(String PFURL){
        _editorPref.putString("PFURL", PFURL);
        _editorPref.commit();
    }

    public String getPFURL(){
        return _pref.getString("PFURL","");
    }


    public void saveID(String ID){
        _editorPref.putString("ID", ID);
        _editorPref.commit();
    }

    public String getID(){
        return _pref.getString("ID","");
    }


    public void saveMonth(String Month){
        _editorPref.putString("Month", Month);
        _editorPref.commit();
    }

    public String getMonth(){
        return _pref.getString("Month","");
    }



    public void saveFinacialYear(String FinacialYear){
        _editorPref.putString("FinacialYear", FinacialYear);
        _editorPref.commit();
    }

    public String getFinacialYear(){
        return _pref.getString("FinacialYear","");
    }

    public void saveZoneId(String ZoneId){
        _editorPref.putString("ZoneId", ZoneId);
        _editorPref.commit();
    }

    public String getZoneId(){
        return _pref.getString("ZoneId","");
    }

    public void saveBranchId(String BranchId){
        _editorPref.putString("BranchId", BranchId);
        _editorPref.commit();
    }

    public String getBranchId(){
        return _pref.getString("BranchId","");
    }



    public void saveZoneNameForEmp(String ZoneNameForEmp){
        _editorPref.putString("ZoneNameForEmp", ZoneNameForEmp);
        _editorPref.commit();
    }

    public String getZoneNameForEmp(){
        return _pref.getString("ZoneNameForEmp","");
    }

    public void saveBranchNameForEmp(String BranchNameForEmp){
        _editorPref.putString("BranchNameForEmp", BranchNameForEmp);
        _editorPref.commit();
    }

    public String getBranchNameForEmp(){
        return _pref.getString("BranchNameForEmp","");
    }


    public void saveShowFinacialYear(String ShowFinacialYear){
        _editorPref.putString("ShowFinacialYear", ShowFinacialYear);
        _editorPref.commit();
    }

    public String getShowFinacialYear(){
        return _pref.getString("ShowFinacialYear","");
    }


    public void saveXYZ(String XYZ){
        _editorPref.putString("XYZ", XYZ);
        _editorPref.commit();
    }

    public String getXYZ(){
        return _pref.getString("XYZ","");
    }

    public void saveShiftFlag(String ShiftFlag){
        _editorPref.putString("ShiftFlag", ShiftFlag);
        _editorPref.commit();
    }

    public String getShiftFlag(){
        return _pref.getString("ShiftFlag","");
    }


    public void saveJrFlag(String JrFlag){
        _editorPref.putString("JrFlag", JrFlag);
        _editorPref.commit();
    }

    public String getJrFlag(){
        return _pref.getString("JrFlag","");
    }

    public void savePFNotificationURL(String PFNotificationURL){
        _editorPref.putString("PFNotificationURL", PFNotificationURL);
        _editorPref.commit();
    }

    public String getPFNotificationURL(){
        return _pref.getString("PFNotificationURL","");
    }

    public void saveAccessToken(String AccessToken){
        _editorPref.putString("AccessToken", AccessToken);
        _editorPref.commit();
    }

    public String getAccessToken(){
        return _pref.getString("AccessToken","");
    }

    public void saveUserLoginID(String userLoginId){
        _editorPref.putString("UserLoginId", userLoginId);
        _editorPref.commit();
    }

    public String getUserLoginId(){
        return _pref.getString("UserLoginId","");
    }

    public void saveExperience(String expStatus){
        _editorPref.putString("experience", expStatus);
        _editorPref.commit();
    }

    public String getExperience(){
        return _pref.getString("experience","");
    }

    public void saveCompanyName(String companyName){
        _editorPref.putString("company_name", companyName);
        _editorPref.commit();
    }

    public String getCompanyName(){
        return _pref.getString("company_name","");
    }

    public void saveUAN_Active(String UAN_Active){
        _editorPref.putString("UAN_Active", UAN_Active);
        _editorPref.commit();
    }

    public String getUAN_Active(){
        return _pref.getString("UAN_Active","");
    }

    public void saveUAN_Mandatory(String UAN_Mandatory){
        _editorPref.putString("UAN_Mandatory", UAN_Mandatory);
        _editorPref.commit();
    }

    public String getUAN_Mandatory(){
        return _pref.getString("UAN_Mandatory","");
    }

    public void saveAdjustmentStatus(String UAN_Mandatory){
        _editorPref.putString("Adjustment_Status", UAN_Mandatory);
        _editorPref.commit();
    }

    public String getAdjustmentStatus(){
        return _pref.getString("Adjustment_Status","");
    }
}

