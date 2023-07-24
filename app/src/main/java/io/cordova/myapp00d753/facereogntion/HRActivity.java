package io.cordova.myapp00d753.facereogntion;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.databinding.ActivityHractivityBinding;

public class HRActivity extends AppCompatActivity {
    ActivityHractivityBinding binding;
    ArrayList<String>idList=new ArrayList<>();
    String dob="";
    String idProof="";
    String idProofId="";
    AlertDialog alerDialog1;
    String next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_hractivity);
        initView();
    }

    private void initView(){
        idList.add("Please Select");
        idList.add("Aadhaar Card");
        idList.add("PAN Card");
        next = "<font color='#EE0000'>*</font>";
        binding.tvFullNameTitle.setText(Html.fromHtml("Full Name: "+next));
        binding.tvMobTitle.setText(Html.fromHtml("Mobile Number: "+next));
        binding.tvIDPrrofTitle.setText(Html.fromHtml("ID Proof: "+next));




        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>
                (HRActivity.this, android.R.layout.simple_spinner_item,
                        idList); //selected item will look like a spinner set from XML
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spID.setAdapter(typeAdapter);


        binding.imgDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStrtDatePicker();
            }
        });


        binding.spID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>0){
                    idProof=idList.get(i);
                    binding.tvRef.setText(Html.fromHtml(idProof+" Number: "+next));
                    if (idProof.equals("Aadhaar Card")){
                        idProofId="002";
                    }else {
                        idProofId="003";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.etCanName.getText().toString().length()>0){
                    if (binding.etCanMob.getText().toString().length()>9){
                        if (!idProof.equals("")){
                            if (binding.etRefNo.getText().toString().length()>0){
                                if (idProof.contains("Aadhaar")){
                                    AddharValidation();
                                }else {
                                    PANValidation();
                                }
                            }else {
                                Toast.makeText(HRActivity.this,"Please Enter "+idProof+"'s Number",Toast.LENGTH_LONG).show();

                            }

                        }else {
                            Toast.makeText(HRActivity.this,"Please Select Candidate's ID Proof",Toast.LENGTH_LONG).show();

                        }

                    }else {
                        Toast.makeText(HRActivity.this,"Please Enter Candidate's Mobile Number",Toast.LENGTH_LONG).show();
                    }

                }else {
                    Toast.makeText(HRActivity.this,"Please Enter Candidate's Name",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void showStrtDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(HRActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {



                        int month = (monthOfYear + 1);
                        dob = dayOfMonth + "/" + month + "/" + year;
                        binding.tvDOB.setText(dob);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();

    }


    private void AddharValidation(){
        if (isValidAadhaarNumber(binding.etRefNo.getText().toString()) || isValidAadhaarNumberr(binding.etRefNo.getText().toString())){
            postData();
        }else {
            Toast.makeText(HRActivity.this,"Please Enter Valid Aadhaar Number",Toast.LENGTH_LONG).show();
        }
    }

    private void PANValidation(){
        if (isValidPANNumber(binding.etRefNo.getText().toString())){
            postData();

        }else {
            Toast.makeText(HRActivity.this,"Please Enter Valid PAN Number",Toast.LENGTH_LONG).show();
        }
    }

    public boolean isValidAadhaarNumber (String text){
        // Regex to check valid Aadhaar number.
        String regex
                = "^[2-9]{1}[0-9]{3}\\s[0-9]{4}\\s[0-9]{4}$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the string is empty
        // return false
        if (text == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given string
        // and regular expression.
        Matcher m = p.matcher(text);

        // Return if the string
        // matched the ReGex
        return m.matches();
    }

    public boolean isValidAadhaarNumberr (String text){
        // Regex to check valid Aadhaar number.
        String regex
                = "^[2-9]{1}[0-9]{3}[0-9]{4}[0-9]{4}$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the string is empty
        // return false
        if (text == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given string
        // and regular expression.
        Matcher m = p.matcher(text);

        // Return if the string
        // matched the ReGex
        return m.matches();
    }


    public static boolean isValidPANNumber(String str)
    {
        // Regex to check valid EPIC Number
        String regex = "[A-Z]{5}[0-9]{4}[A-Z]{1}";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the str is empty return false
        if (str == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given
        // EPIC Number using regular expression.
        Matcher m = p.matcher(str);

        // Return if the str
        // matched the ReGex
        return m.matches();
    }


    private void successAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(HRActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);

        tvInvalidDate.setText("Candidate form has been saved successfully");




        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                Intent intent=new Intent(HRActivity.this,LoginDashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }


    private void postData() {

        final ProgressDialog pd = new ProgressDialog(HRActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.dismiss();

        AndroidNetworking.upload("http://gsppi.geniusconsultant.com/GeniusiOSApi/api/post_SBICardCandidate")
                .addMultipartParameter("CandidateID", "0")
                .addMultipartParameter("EmployeeName", binding.etCanName.getText().toString())
                .addMultipartParameter("MobileNumber", binding.etCanMob.getText().toString())
                .addMultipartParameter("Fathername", binding.etCanFatherName.getText().toString())
                .addMultipartParameter("DOB", dob)
                .addMultipartParameter("DocumentID", idProofId)
                .addMultipartParameter("ReferenceNo", binding.etRefNo.getText().toString())
                .addMultipartParameter("DeviceID", "0")
                .addMultipartParameter("DeviceName", "A")
                .addMultipartParameter("Longitude", "0.00")
                .addMultipartParameter("Latitude", "0.00")
                .addMultipartParameter("Geo_Address", "00")
                .addMultipartParameter("DocumentFname", "")
                .addMultipartParameter("PhotoFname1", "")
                .addMultipartParameter("PhotoFname2", "")
                .addMultipartParameter("PhotoFname3", "")
                .addMultipartParameter("UserID", "SBIHR")
                .addMultipartParameter("Operation", "1")
                .addMultipartParameter("SecurityCode", "0000")

                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        pd.show();

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pd.dismiss();


                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        String responseText = job1.optString("responseText");
                        Log.d("responseText", responseText);
                        boolean responseStatus = job1.optBoolean("responseStatus");
                        if (responseStatus) {
                            successAlert();
                            pd.dismiss();

                        } else {
                            pd.dismiss();
                            Toast.makeText(HRActivity.this, responseText, Toast.LENGTH_LONG).show();

                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG);
                    }
                });
    }

}