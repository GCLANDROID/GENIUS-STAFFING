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

























}

