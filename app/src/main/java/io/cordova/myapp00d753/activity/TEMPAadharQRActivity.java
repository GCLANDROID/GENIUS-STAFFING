package io.cordova.myapp00d753.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.databinding.ActivityTempaadharQractivityBinding;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.ShowDialog;
import io.cordova.myapp00d753.utility.Util;

public class TEMPAadharQRActivity extends AppCompatActivity {
    private static final String TAG = "TEMPAadharQRActivity";
    ActivityTempaadharQractivityBinding binding;
    Pref pref;
    String sessionId;
    AlertDialog al1;
    androidx.appcompat.app.AlertDialog alerDialog1;
    boolean aadharflag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_tempaadhar_qractivity);
        initView();
    }

    private void initView(){
        pref=new Pref(TEMPAadharQRActivity.this);
        binding.imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TEMPAadharQRActivity.this,TempDashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        binding.etAadhar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (binding.etAadhar.getText().toString().length()==12){
                    hideKeyboard();
                    /*JSONObject jsonObject=new JSONObject();
                    try {
                        jsonObject.put("Id",binding.etAadhar.getText().toString());
                        checkAddahrDetails(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/
                }

            }
        });

        binding.btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.etAadhar.getText().toString().length()==12){
                    if (binding.etCapctha.getText().toString().length()>0){
                        JSONObject jsonObject=new JSONObject();
                        try {
                            jsonObject.put("sessionId",sessionId);
                            jsonObject.put("aadhaar",binding.etAadhar.getText().toString());
                            jsonObject.put("securityCode",binding.etCapctha.getText().toString());
                            Log.e("jsonObject", "jsonObject: "+jsonObject);
                            validateCaptcha(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(TEMPAadharQRActivity.this,"Please Enter Captcha",Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(TEMPAadharQRActivity.this,"Please Enter Valid Aadhaar Number",Toast.LENGTH_LONG).show();
                }
            }
        });

        binding.btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.etAadhar.getText().toString().length()==12){

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("AEMConsultantID", pref.getEmpConId());
                        jsonObject.put("AEMClientID", pref.getEmpClintId());
                        jsonObject.put("AEMClientOfficeID", pref.getEmpClintOffId());
                        jsonObject.put("AEMEmployeeID", pref.getMasterId());
                        jsonObject.put("WorkingStatus", "1");
                        jsonObject.put("Operation", "12");
                        checkAadhaarNumber(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(TEMPAadharQRActivity.this, "Please Enter Valid Aadhaar Number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnOTPValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.etOTP.getText().toString().length()>0){

                        JSONObject jsonObject=new JSONObject();
                        try {
                            jsonObject.put("sessionId",sessionId);
                            jsonObject.put("otp",binding.etOTP.getText().toString());
                            jsonObject.put("shareCode",GetPassword(4));
                            jsonObject.put("fileUrl",true);
                            validateOTP(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                } else {
                    Toast.makeText(TEMPAadharQRActivity.this,"Please Enter OTP",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void checkAadhaarNumber(JSONObject jsonObject) {
        ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.show();
        pd.setCancelable(false);
        Log.e(TAG, "checkAadhaarNumber: INPUT: "+jsonObject);
        AndroidNetworking.post(AppData.KYC_GET_DETAILS)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pd.dismiss();
                        try {
                            Log.e(TAG, "CHECK_AADHAAR_NUMBER: "+response.toString(4));
                            JSONObject job1 = response;
                            int Response_Code = job1.optInt("Response_Code");
                            String Response_Data = job1.optString("Response_Data");
                            //Log.e(TAG, "Response_Data: "+Response_Data);
                            if (Response_Code == 101) {
                                if (Response_Data != null) {
                                    JSONObject Response_Data_obj = new JSONObject(Response_Data);
                                    JSONArray jsonArray = Response_Data_obj.getJSONArray("AadharDetails");
                                    JSONObject job2 = jsonArray.getJSONObject(0);
                                    Log.e(TAG, "onResponse: "+job2.getString("ReferenceNo") );
                                    String AadhaarNumber = job2.getString("ReferenceNo");
                                    if (AadhaarNumber.equals(binding.etAadhar.getText().toString().trim())){
                                        try {
                                            jsonObject.put("Id",binding.etAadhar.getText().toString().trim());
                                            checkAddahrDetails(jsonObject);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        ShowDialog.showErrorDialog(TEMPAadharQRActivity.this,
                                                "Another Aadhaar number is linked to this ID. Kindly provide the accurate Aadhaar number.");
                                    }
                                } else {
                                    JSONObject jsonObject=new JSONObject();
                                    try {
                                        jsonObject.put("Id",binding.etAadhar.getText().toString().trim());
                                        checkAddahrDetails(jsonObject);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                //TODO: No Data Found
                                JSONObject jsonObject=new JSONObject();
                                try {
                                    jsonObject.put("Id",binding.etAadhar.getText().toString().trim());
                                    checkAddahrDetails(jsonObject);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        pd.dismiss();
                        Log.e(TAG, "CHECK_AADHAAR_NUMBER_onError: "+anError.getErrorBody() );
                    }
                });
    }


    private void captchagebneration() {
        ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.show();
        pd.setCancelable(false);
        AndroidNetworking.get("https://vcip-aadhaarxml.hyperverge.co/api/v1/captcha")
                .addHeaders("appId", AppData.APPID)
                .addHeaders("appKey", AppData.APPKEY)
                .addHeaders("transactionId", pref.getMasterId())
                .addHeaders("content-type", "application/json")
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        pd.dismiss();

                        int statusCode = job1.optInt("statusCode");
                        if (statusCode == 200) {

                            binding.llCaptcha.setVisibility(View.VISIBLE);
                            sessionId=job1.optString("sessionId");
                            String captchaImage=job1.optString("captchaImage");
                            byte[] decodedString = Base64.decode(captchaImage, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            binding.imgCaptcha.setImageBitmap(decodedByte);

                        } else {


                        }
                    }

                    @Override
                    public void onError(ANError error) {

                        showErrorDialog("Technical issue with UIDAI, please try after some time");

                    }
                });
    }

    private void validateCaptcha(JSONObject jsonObject) {
        ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.show();
        pd.setCancelable(false);
        AndroidNetworking.post("https://vcip-aadhaarxml.hyperverge.co/api/v1/captcha")
                .addJSONObjectBody(jsonObject)
                .addHeaders("appId", AppData.APPID)
                .addHeaders("appKey", AppData.APPKEY)
                .addHeaders("transactionId", pref.getMasterId())
                .addHeaders("content-type", "application/json")
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        pd.dismiss();

                        int statusCode = job1.optInt("statusCode");
                        if (statusCode == 200) {
                            String message=job1.optString("message");
                            binding.tvOTPText.setText(message);

                            binding.llOTp.setVisibility(View.VISIBLE);
                            binding.btnValidate.setVisibility(View.GONE);
                            binding.etAadhar.setEnabled(false);
                            binding.etCapctha.setEnabled(false);

                        } else {


                        }
                    }

                    @Override
                    public void onError(ANError error) {

                        showErrorDialog("Technical issue with UIDAI, please try after some time");

                    }
                });
    }

    private void validateOTP(JSONObject jsonObject) {
        ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.show();
        pd.setCancelable(false);
        AndroidNetworking.post("https://vcip-aadhaarxml.hyperverge.co/api/v1/otp")
                .addJSONObjectBody(jsonObject)
                .addHeaders("appId", AppData.APPID)
                .addHeaders("appKey", AppData.APPKEY)
                .addHeaders("transactionId", pref.getMasterId())
                .addHeaders("content-type", "application/json")
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        pd.dismiss();

                        int statusCode = job1.optInt("statusCode");
                        if (statusCode == 200) {
                            aadharflag=true;
                            JSONObject details=job1.optJSONObject("details");
                            //name
                            JSONObject name=details.optJSONObject("name");
                            String namevalue=name.optString("value");

                            //dob

                            JSONObject dob=details.optJSONObject("dob");
                            String dobvalue=dob.optString("value");
                            AppData.ADHARDOB=dobvalue;


                            //gender

                            JSONObject gender=details.optJSONObject("gender");
                            String gendervalue=gender.optString("value");


                            //address

                            JSONObject address=details.optJSONObject("address");
                            String careof=address.optString("careof").replace("S/O:","").trim();
                            String state=address.optString("state");
                            String pin=address.optString("pin");
                            String street=address.optString("street");
                            String locality=address.optString("locality");
                            String house=address.optString("house");
                            String postoffice=address.optString("postoffice");
                            String subDistrict=address.optString("subDistrict");
                            String district=address.optString("district");
                            String vtc=address.optString("vtc");
                            String landmark=address.optString("landmark");


                            //image

                            JSONObject photo=details.optJSONObject("photo");
                            String photoval=photo.optString("value").trim();
                            AppData.ADHARIMAGE=photoval;
                            AppData.AADAHARNUMBER=binding.etAadhar.getText().toString();

                            JSONObject cardnoobj=new JSONObject();
                            JSONObject mainobj=new JSONObject();
                            try {
                                cardnoobj.put("Aadhar",binding.etAadhar.getText().toString());
                                JSONArray jar=new JSONArray();
                                jar.put(job1);
                                jar.put(cardnoobj);


                                mainobj.put("aadhardetails",jar);
                                Log.d("mainobj", String.valueOf(mainobj));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            adharAlert(namevalue,dobvalue,AppData.AADAHARNUMBER,gendervalue,careof,state,pin,street,locality,house,postoffice,subDistrict,district,vtc,landmark,mainobj,0);



                        }else {


                        }
                    }

                    @Override
                    public void onError(ANError error) {

                       showErrorDialog("Technical issue with UIDAI, please try after some time");

                    }
                });
    }

    public String GetPassword(int length){
        char[] chars = "0123456789".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();

        Random rand = new Random();

        for(int i = 0; i < length; i++){
            char c = chars[rand.nextInt(chars.length)];
            stringBuilder.append(c);
        }

        return stringBuilder.toString();
    }


    private void showErrorDialog(String text) {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(TEMPAadharQRActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.error_ayput, null);
        dialogBuilder.setView(dialogView);
        TextView tvError = (TextView) dialogView.findViewById(R.id.tvError);
        tvError.setText(text);
        ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                al1.dismiss();
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        al1 = dialogBuilder.create();
        al1.setCancelable(false);
        Window window = al1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        al1.show();
    }

    private void adharAlert(String name,String dob,String aadhar,String gendervalue,String careof,String state,String pin,String street,String locality,String house,String postoffice,String subDistrict,String district,String vtc,String landmark,JSONObject mainobj,int flag) {
        androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(TEMPAadharQRActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_kyc, null);
        dialogBuilder.setView(dialogView);
        TextView tvName = (TextView) dialogView.findViewById(R.id.tvName);

        tvName.setText(name);

        TextView tvDOB = (TextView) dialogView.findViewById(R.id.tvDOB);
        tvDOB.setText(Util.changeAnyDateFormat(dob,"dd-MM-yyyy","dd MMM yyyy"));

        TextView tvAadhar = (TextView) dialogView.findViewById(R.id.tvAadhar);
        tvAadhar.setText(aadhar);

        ImageView imgAdhar=(ImageView)dialogView.findViewById(R.id.imgAdhar);

        if (!AppData.ADHARIMAGE.equals("")){
            /*byte[] decodedString = Base64.decode(AppData.ADHARIMAGE, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imgAdhar.setImageBitmap(decodedByte);*/
            imgAdhar.setImageDrawable(getResources().getDrawable(R.drawable.man));
        }else {
            imgAdhar.setImageDrawable(getResources().getDrawable(R.drawable.man));
        }


        LinearLayout llKYC = (LinearLayout) dialogView.findViewById(R.id.llKYC);
        llKYC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                if (flag==0) {
                    uploadAddharDetails(mainobj, name, dob, gendervalue, careof, state, pin, street, locality, house, postoffice, subDistrict, district, vtc, landmark);
                }else {
                    Intent intent=new Intent(TEMPAadharQRActivity.this,TempProfileActivity.class);
                    intent.putExtra("namevalue",name);
                    intent.putExtra("dobvalue",dob);
                    intent.putExtra("gendervalue",gendervalue);
                    intent.putExtra("careof",careof);
                    intent.putExtra("state",state);
                    intent.putExtra("pin",pin);
                    intent.putExtra("street",street);
                    intent.putExtra("locality",locality);
                    intent.putExtra("house",house);
                    intent.putExtra("postoffice",postoffice);
                    intent.putExtra("subDistrict",subDistrict);
                    intent.putExtra("district",district);
                    intent.putExtra("vtc",vtc);
                    intent.putExtra("landmark",landmark);
                    intent.putExtra("aadhaarflag",aadharflag);
                    startActivity(intent);
                    finish();
                }
            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(false);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }


    private void uploadAddharDetails(JSONObject jsonObject,String name,String dob,String gendervalue,String careof,String state,String pin,String street,String locality,String house,String postoffice,String subDistrict,String district,String vtc,String landmark) {
        ProgressDialog pd=new ProgressDialog(TEMPAadharQRActivity.this);
        pd.setMessage("Loading");
        pd.show();
        pd.setCancelable(false);
        AndroidNetworking.post(AppData.newv2url + "Profile/SaveEmployeeAadharDetails")
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        pd.dismiss();

                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            Intent intent=new Intent(TEMPAadharQRActivity.this,TempProfileActivity.class);
                            intent.putExtra("namevalue",name);
                            intent.putExtra("dobvalue",dob);
                            intent.putExtra("gendervalue",gendervalue);
                            intent.putExtra("careof",careof);
                            intent.putExtra("state",state);
                            intent.putExtra("pin",pin);
                            intent.putExtra("street",street);
                            intent.putExtra("locality",locality);
                            intent.putExtra("house",house);
                            intent.putExtra("postoffice",postoffice);
                            intent.putExtra("subDistrict",subDistrict);
                            intent.putExtra("district",district);
                            intent.putExtra("vtc",vtc);
                            intent.putExtra("landmark",landmark);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent=new Intent(TEMPAadharQRActivity.this,TempProfileActivity.class);
                            intent.putExtra("namevalue",name);
                            intent.putExtra("dobvalue",dob);
                            intent.putExtra("gendervalue",gendervalue);
                            intent.putExtra("careof",careof);
                            intent.putExtra("state",state);
                            intent.putExtra("pin",pin);
                            intent.putExtra("street",street);
                            intent.putExtra("locality",locality);
                            intent.putExtra("house",house);
                            intent.putExtra("postoffice",postoffice);
                            intent.putExtra("subDistrict",subDistrict);
                            intent.putExtra("district",district);
                            intent.putExtra("vtc",vtc);
                            intent.putExtra("landmark",landmark);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onError(ANError error) {

                        Intent intent=new Intent(TEMPAadharQRActivity.this,TempProfileActivity.class);
                        intent.putExtra("namevalue",name);
                        intent.putExtra("dobvalue",dob);
                        intent.putExtra("gendervalue",gendervalue);
                        intent.putExtra("careof",careof);
                        intent.putExtra("state",state);
                        intent.putExtra("pin",pin);
                        intent.putExtra("street",street);
                        intent.putExtra("locality",locality);
                        intent.putExtra("house",house);
                        intent.putExtra("postoffice",postoffice);
                        intent.putExtra("subDistrict",subDistrict);
                        intent.putExtra("district",district);
                        intent.putExtra("vtc",vtc);
                        intent.putExtra("landmark",landmark);
                        startActivity(intent);
                        finish();
                    }
                });
    }


    private void checkAddahrDetails(JSONObject jsonObject) {
        ProgressDialog pd=new ProgressDialog(TEMPAadharQRActivity.this);
        pd.setMessage("Loading");
        pd.show();
        pd.setCancelable(false);
        AndroidNetworking.post(AppData.newv2url + "Profile/GetEmployeeAllDetails")
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        pd.dismiss();

                        int Response_Code = job1.optInt("Response_Code");
                        JSONObject Response_Data=job1.optJSONObject("Response_Data");
                        if (Response_Data!=null){
                            aadharflag=false;
                            JSONObject details=Response_Data.optJSONObject("details");
                            JSONObject name=details.optJSONObject("name");
                            String namevalue=name.optString("value");

                            //dob

                            JSONObject dob=details.optJSONObject("dob");
                            String dobvalue=dob.optString("value");
                            AppData.ADHARDOB=dobvalue;


                            //gender

                            JSONObject gender=details.optJSONObject("gender");
                            String gendervalue=gender.optString("value");


                            //address

                            JSONObject address=details.optJSONObject("address");
                            String careof=address.optString("careof").replace("S/O:","").trim();
                            String state=address.optString("state");
                            String pin=address.optString("pin");
                            String street=address.optString("street");
                            String locality=address.optString("locality");
                            String house=address.optString("house");
                            String postoffice=address.optString("postoffice");
                            String subDistrict=address.optString("subDistrict");
                            String district=address.optString("district");
                            String vtc=address.optString("vtc");
                            String landmark=address.optString("landmark");


                            //image

                            AppData.AADAHARNUMBER=binding.etAadhar.getText().toString();

                            adharAlert(namevalue,dobvalue,AppData.AADAHARNUMBER,gendervalue,careof,state,pin,street,locality,house,postoffice,subDistrict,district,vtc,landmark,null,1);

                        }else {

                            captchagebneration();

                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                        captchagebneration();
                    }
                });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}