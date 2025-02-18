package io.cordova.myapp00d753.activity;

import static android.os.Build.VERSION.SDK_INT;
import static java.util.Calendar.DAY_OF_MONTH;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.cordova.myapp00d753.AndroidXCamera.AndroidXCameraActivity;
import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.databinding.ActivityTempExperinceBinding;
import io.cordova.myapp00d753.module.MainDocModule;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.FindDocumentInformation;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.RealPathUtil;

public class TempExperinceActivity extends AppCompatActivity {
    private static final String TAG = "TempExperinceActivity";
    ActivityTempExperinceBinding binding;
    String month;
    String doj="";
    String strtdate;
    String doe="";
    int flag=0;
    AlertDialog attachAlert;
    Uri image_uri;
    File compressedImageFile;
    String pdfFilePath, pdfFileName;
    private static final int DEFAULT_BUFFER_SIZE = 2048;
    int imageflag=0;
    Pref pref;
    String uanDOB="";
    ArrayList<MainDocModule> mainRealation = new ArrayList<>();
    ArrayList<String> realation = new ArrayList<>();
    ArrayList<String> uanPercentage = new ArrayList<>();
    String pfPercantage="";
    String uanRltionshp="";
    int pfflag=0;
    boolean is_PF_Account_Exist = false, is_Experience_Letter_Selected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_temp_experince);
        initView();


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("AEMConsultantID", pref.getEmpConId());
            jsonObject.put("AEMClientID", pref.getEmpClintId());
            jsonObject.put("AEMClientOfficeID", pref.getEmpClintOffId());
            jsonObject.put("AEMEmployeeID", pref.getEmpId());
            jsonObject.put("WorkingStatus", "1");
            jsonObject.put("Operation", "0");
            checkFresherOrExperienceByOfficeDetails(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView(){
        pref=new Pref(TempExperinceActivity.this);
        setNomineeRelation();
        uanPercentage.add("Please Select");
        uanPercentage.add("100");
        uanPercentage.add("75");
        uanPercentage.add("50");
        uanPercentage.add("25");
        binding.etUAN.setText(pref.getSUAN());
        if (AppData.COMPANYNAME.contains("SBI")){
            binding.llNonSBIEXP.setVisibility(View.VISIBLE);
            binding.llSBIEXP.setVisibility(View.VISIBLE);
        }else {
            binding.llNonSBIEXP.setVisibility(View.GONE);
            binding.llSBIEXP.setVisibility(View.GONE);
        }
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        ArrayAdapter<String> uanspinnerArrayAdapter = new ArrayAdapter<String>
                (TempExperinceActivity.this, android.R.layout.simple_spinner_item,
                        uanPercentage); //selected item will look like a spinner set from XML
        uanspinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spTotalAmt.setAdapter(uanspinnerArrayAdapter);
        binding.llFreshers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.imgFreshersTick.getVisibility()==View.GONE){
                    binding.imgFreshersTick.setVisibility(View.VISIBLE);
                    binding.imgExperience.setVisibility(View.GONE);
                    binding.llExpericedForm.setVisibility(View.GONE);
                    flag=1;
                }else {
                    binding.imgExperience.setVisibility(View.GONE);
                    binding.imgFreshersTick.setVisibility(View.GONE);
                    binding.llExpericedForm.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.llExperienced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.imgExperience.getVisibility()==View.GONE){
                    binding.imgFreshersTick.setVisibility(View.GONE);
                    binding.imgExperience.setVisibility(View.VISIBLE);
                    binding.llExpericedForm.setVisibility(View.VISIBLE);
                    flag=2;
                }else {
                    binding.imgFreshersTick.setVisibility(View.GONE);
                    binding.imgExperience.setVisibility(View.GONE);
                    binding.llExpericedForm.setVisibility(View.GONE);
                }
            }
        });


        binding.llYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.imgYes.getVisibility()==View.GONE){
                    binding.imgNo.setVisibility(View.GONE);
                    binding.imgYes.setVisibility(View.VISIBLE);
                    binding.llUAN.setVisibility(View.VISIBLE);
                    pfflag=1;
                }else {
                    binding.imgNo.setVisibility(View.GONE);
                    binding.imgYes.setVisibility(View.GONE);
                    binding.llUAN.setVisibility(View.GONE);
                }
                is_PF_Account_Exist = true;
            }
        });


        binding.llNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.imgNo.getVisibility()==View.GONE){
                    binding.imgNo.setVisibility(View.VISIBLE);
                    binding.imgYes.setVisibility(View.GONE);
                    binding.llUAN.setVisibility(View.GONE);
                    pfflag=0;
                }else {
                    binding.imgNo.setVisibility(View.GONE);
                    binding.imgYes.setVisibility(View.GONE);
                    binding.llUAN.setVisibility(View.GONE);
                }
                is_PF_Account_Exist = true;
            }
        });

        binding.btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TempExperinceActivity.this,TempPanActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        binding.imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TempExperinceActivity.this, TempDashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        binding.imgAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attechmentAlert(300,3001,2);
            }
        });
        binding.etDOJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                System.out.println("Current date : " + (now.get(Calendar.MONTH) + 1) + "-"
                        + now.get(Calendar.DATE) + "-" + now.get(Calendar.YEAR));

                now = Calendar.getInstance();
                now.add(Calendar.YEAR, -18);
                int dyear = now.get(Calendar.YEAR);
                final int dmonth = now.get(Calendar.MONTH);
                int dday = now.get(DAY_OF_MONTH);
                Calendar c1 = Calendar.getInstance();
                /*final int syear = year - 18;

                final int month1 = c1.get(Calendar.MONTH);
                final int sday1 = c1.get(DAY_OF_MONTH);*/


                final DatePickerDialog dialog = new DatePickerDialog(TempExperinceActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {

                        int mm = (m + 1);
                        int s = (m + 1) + d + y;
                        if (mm == 1) {
                            month = "January";
                        } else if (mm == 2) {
                            month = "February";
                        } else if (mm == 3) {
                            month = "March";
                        } else if (mm == 4) {
                            month = "April";
                        } else if (mm == 5) {
                            month = "May";
                        } else if (mm == 6) {
                            month = "June";
                        } else if (mm == 7) {
                            month = "July";
                        } else if (mm == 8) {
                            month = "August";
                        } else if (mm == 9) {
                            month = "September";
                        } else if (mm == 10) {
                            month = "October";
                        } else if (mm == 11) {
                            month = "November";
                        } else if (mm == 12) {
                            month = "December";
                        }
                        doj = d + " " + month + " " + y;
                        strtdate=d + "/" + mm + "/" + y;

                        binding.etDOJ.setText(doj);

                        binding.etDOJ.setBackgroundResource(R.drawable.lldesign9);

                    }
                }, dyear, dmonth, dday);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();
            }
        });

        binding.etDOE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                System.out.println("Current date : " + (now.get(Calendar.MONTH) + 1) + "-"
                        + now.get(Calendar.DATE) + "-" + now.get(Calendar.YEAR));

                now = Calendar.getInstance();
                now.add(Calendar.YEAR, -18);
                int dyear = now.get(Calendar.YEAR);
                final int dmonth = now.get(Calendar.MONTH);
                int dday = now.get(DAY_OF_MONTH);
                Calendar c1 = Calendar.getInstance();
                /*final int syear = year - 18;

                final int month1 = c1.get(Calendar.MONTH);
                final int sday1 = c1.get(DAY_OF_MONTH);*/


                final DatePickerDialog dialog = new DatePickerDialog(TempExperinceActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {

                        int mm = (m + 1);
                        int s = (m + 1) + d + y;
                        if (mm == 1) {
                            month = "January";
                        } else if (mm == 2) {
                            month = "February";
                        } else if (mm == 3) {
                            month = "March";
                        } else if (mm == 4) {
                            month = "April";
                        } else if (mm == 5) {
                            month = "May";
                        } else if (mm == 6) {
                            month = "June";
                        } else if (mm == 7) {
                            month = "July";
                        } else if (mm == 8) {
                            month = "August";
                        } else if (mm == 9) {
                            month = "September";
                        } else if (mm == 10) {
                            month = "October";
                        } else if (mm == 11) {
                            month = "November";
                        } else if (mm == 12) {
                            month = "December";
                        }
                        doe = d + " " + month + " " + y;
                        String enddate=d + "/" + mm + "/" + y;


                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date strDate = null;
                        try {
                            strDate = sdf.parse(strtdate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                        Date striDate = null;
                        try {
                            striDate = df.parse(enddate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (striDate.getTime() > strDate.getTime() ||striDate.getTime() == strDate.getTime()) {

                            binding.etDOE.setText(doe);
                            binding.etDOE.setBackgroundResource(R.drawable.lldesign9);
                        }else {
                            Toast.makeText(TempExperinceActivity.this,"DOE Can Not be before than DOJ",Toast.LENGTH_LONG).show();
                        }
                    }
                }, dyear, dmonth, dday);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();
            }
        });


        binding.imgUANCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                System.out.println("Current date : " + (now.get(Calendar.MONTH) + 1) + "-"
                        + now.get(Calendar.DATE) + "-" + now.get(Calendar.YEAR));

                now = Calendar.getInstance();
                now.add(Calendar.YEAR, -18);
                int dyear = now.get(Calendar.YEAR);
                final int dmonth = now.get(Calendar.MONTH);
                int dday = now.get(DAY_OF_MONTH);
                Calendar c1 = Calendar.getInstance();
                /*final int syear = year - 18;

                final int month1 = c1.get(Calendar.MONTH);
                final int sday1 = c1.get(DAY_OF_MONTH);*/


                final DatePickerDialog dialog = new DatePickerDialog(TempExperinceActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {

                        int mm = (m + 1);
                        int s = (m + 1) + d + y;
                        if (mm == 1) {
                            month = "January";
                        } else if (mm == 2) {
                            month = "February";
                        } else if (mm == 3) {
                            month = "March";
                        } else if (mm == 4) {
                            month = "April";
                        } else if (mm == 5) {
                            month = "May";
                        } else if (mm == 6) {
                            month = "June";
                        } else if (mm == 7) {
                            month = "July";
                        } else if (mm == 8) {
                            month = "August";
                        } else if (mm == 9) {
                            month = "September";
                        } else if (mm == 10) {
                            month = "October";
                        } else if (mm == 11) {
                            month = "November";
                        } else if (mm == 12) {
                            month = "December";
                        }
                        uanDOB = d + " " + month + " " + y;

                        binding.tvUANDOB.setText(uanDOB);
                    }
                }, dyear, dmonth, dday);
                dialog.getDatePicker().setMaxDate((long) (System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 365.25 * 18)));
                dialog.getDatePicker().setMinDate((long) (System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 365.25 * 90)));
                dialog.show();

            }
        });
        binding.spUANRealation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    uanRltionshp = mainRealation.get(position).getDocID();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.spTotalAmt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    pfPercantage = uanPercentage.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.etcompany.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0){
                    binding.etcompany.setBackgroundResource(R.drawable.lldesign9);
                }
            }
        });
        binding.etDesignation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0){
                    binding.etDesignation.setBackgroundResource(R.drawable.lldesign9);
                }
            }
        });
        binding.etManagerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0){
                    binding.etManagerName.setBackgroundResource(R.drawable.lldesign9);
                }
            }
        });
        binding.etManagerDesignation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0){
                    binding.etManagerDesignation.setBackgroundResource(R.drawable.lldesign9);
                }
            }
        });
        binding.etManagerContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0){
                    binding.etManagerContact.setBackgroundResource(R.drawable.lldesign9);
                }
            }
        });
        binding.etUAN.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0){
                    binding.etUAN.setBackgroundResource(R.drawable.lldesign9);
                }
            }
        });
        binding.etPFNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0){
                    binding.etPFNumber.setBackgroundResource(R.drawable.lldesign9);
                }
            }
        });
        binding.btnSaveForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag!=0) {
                    if (flag==1){
                        experiencesubmit("0");
                    }else {
                        if (binding.etcompany.getText().toString().length()>0){
                            if (binding.etDesignation.getText().toString().length()>0){
                                if (binding.etManagerName.getText().toString().length()>0){
                                    if (binding.etManagerDesignation.getText().toString().length()>0){
                                        if (binding.etManagerContact.getText().toString().length()==10){
                                            if (!doj.equals("")){
                                                if (!doe.equals("")){
                                                    if (is_Experience_Letter_Selected){
                                                        if (is_PF_Account_Exist){
                                                            if (pfflag==1){
                                                                UANValidation();
                                                            }else {
                                                                experiencesubmit("1");
                                                            }
                                                        } else {
                                                            Toast.makeText(TempExperinceActivity.this,"Please Select PF Account Existing status",Toast.LENGTH_LONG).show();
                                                        }
                                                    } else {
                                                        Toast.makeText(TempExperinceActivity.this,"Please Select Your Experience Letter",Toast.LENGTH_LONG).show();
                                                    }
                                                }else {
                                                    Toast.makeText(TempExperinceActivity.this,"Please Enter Previous Company’s DOE",Toast.LENGTH_LONG).show();
                                                    binding.etDOE.setBackgroundResource(R.drawable.lldesign_error);
                                                }
                                            }else {
                                                Toast.makeText(TempExperinceActivity.this,"Please Enter Previous Company’s DOJ",Toast.LENGTH_LONG).show();
                                                binding.etDOJ.setBackgroundResource(R.drawable.lldesign_error);
                                            }
                                        }else {
                                            Toast.makeText(TempExperinceActivity.this,"Please Enter Previous Reporting Manager’s Contact Details",Toast.LENGTH_LONG).show();
                                            binding.etManagerContact.setBackgroundResource(R.drawable.lldesign_error);
                                        }
                                    }else {
                                        Toast.makeText(TempExperinceActivity.this,"Please Enter Previous Reporting Manager’s Designation",Toast.LENGTH_LONG).show();
                                        binding.etManagerDesignation.setBackgroundResource(R.drawable.lldesign_error);
                                    }
                                }else {
                                    Toast.makeText(TempExperinceActivity.this,"Please Enter Previous Reporting Manager’s Name",Toast.LENGTH_LONG).show();
                                    binding.etManagerName.setBackgroundResource(R.drawable.lldesign_error);
                                }
                            }else {
                                Toast.makeText(TempExperinceActivity.this,"Please Enter Previous Employee’s Designation",Toast.LENGTH_LONG).show();
                                binding.etDesignation.setBackgroundResource(R.drawable.lldesign_error);
                            }
                        }else {
                            Toast.makeText(TempExperinceActivity.this,"Please Enter Previous Company's Name",Toast.LENGTH_LONG).show();
                            binding.etcompany.setBackgroundResource(R.drawable.lldesign_error);
                        }
                    }
                }else {
                    Toast.makeText(TempExperinceActivity.this,"Please select Freshers/Experienced option",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void attechmentAlert(int gallerycode,int cameracode,int intentflag) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TempExperinceActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_attachment, null);
        dialogBuilder.setView(dialogView);
        LinearLayout lnCamera=(LinearLayout)dialogView.findViewById(R.id.lnCamera);
        LinearLayout lnGallery=(LinearLayout)dialogView.findViewById(R.id.lnGallery);
        ImageView imgCancel=(ImageView)dialogView.findViewById(R.id.imgCancel);
        lnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndroidXCameraActivity.launch(TempExperinceActivity.this, cameracode);
                attachAlert.dismiss();
            }
        });

        if (gallerycode==100){
            lnGallery.setVisibility(View.GONE);
        }


        lnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (intentflag==1){
                    galleryIntent(gallerycode);
                }else {
                    pdfChoose(gallerycode);
                }

                attachAlert.dismiss();
            }
        });


        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attachAlert.dismiss();
            }
        });

        attachAlert = dialogBuilder.create();
        attachAlert.setCancelable(false);
        Window window = attachAlert.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        attachAlert.show();
    }
    private void galleryIntent(int gallerycode) {
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
        openGalleryIntent.setType("image/*");
        startActivityForResult(openGalleryIntent, gallerycode);
    }
    private void pdfChoose(int code){
        Intent uploadIntent = new Intent(Intent.ACTION_GET_CONTENT);
        uploadIntent.setType("application/pdf");
        startActivityForResult(uploadIntent, code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if (requestCode == 2000 && resultCode == 3001){
            Log.e("TAG", "onActivityResult: "+data.getExtras().get("picture"));
            Log.e("TAG", "onActivityResult: "+data.getExtras().get(AndroidXCameraActivity.IMAGE_PATH_KEY));
            image_uri =  Uri.parse(String.valueOf(data.getExtras().get("picture")));
            //image_uri = (Uri) data.getExtras().get(AndroidXCameraActivity.IMAGE_PATH_KEY);
            compressedImageFile = new File(String.valueOf(data.getExtras().get("picture")));

            if (image_uri != null){
                binding.imgAttachImage.setImageURI(image_uri);
                imageflag=1;
                is_Experience_Letter_Selected = true;
            }
        }else  if ((requestCode == 300 )){
            Uri uri = data.getData();
            Log.e("TAG", "onActivityResult: "+uri.getPath());
            String imagePath = uri.getPath();
            if (imagePath.contains("all_external")){
                pdfFileName = FindDocumentInformation.FileNameFromURL(imagePath);
                compressedImageFile = convertInputStreamToFile(uri,pdfFileName);

            } else {
                try {
                    pdfFilePath = getRealPath(TempExperinceActivity.this,uri);
                    pdfFileName = FindDocumentInformation.FileNameFromURL(pdfFilePath);
                } catch (IllegalArgumentException e){
                    //Todo: from WPS office document select
                    pdfFileName = FindDocumentInformation.FileNameFromURL(imagePath);
                    compressedImageFile = convertInputStreamToFile(uri,pdfFileName);

                }
                compressedImageFile = convertInputStreamToFile(uri,pdfFileName);

            }
            binding.imgAttachImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_pdf));
            imageflag = 1;
            is_Experience_Letter_Selected = true;

        }
    }

    public static String getRealPath(Context context, Uri fileUri) {
        String realPath;
        Log.e("SDK_INT", "= " + SDK_INT);
        // SDK < API11
        if (SDK_INT < 11) {
            realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(context, fileUri);
        }
        // SDK >= 11 && SDK < 19
        else if (SDK_INT < 19) {
            realPath = RealPathUtil.getRealPathFromURI_API11to18(context, fileUri);
        }
        // SDK > 19 (Android 4.4) and up
        else {
            realPath = RealPathUtil.getRealPathFromURI_API19(context, fileUri);
        }
        return realPath;
    }

    private File convertInputStreamToFile(Uri uri, String fileNme) {
        InputStream inputStream;
        try {
            inputStream = TempExperinceActivity.this.getContentResolver().openInputStream(uri);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        File file = new File(TempExperinceActivity.this.getExternalFilesDir("/").getAbsolutePath(), fileNme);

        try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
            int read;
            byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    private void experiencesubmit(String isexperince) {
        ProgressDialog progressDialog=new ProgressDialog(TempExperinceActivity.this);
        progressDialog.setMessage("Uploading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        //progressDialog.show();

        //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        AndroidNetworking.upload(AppData.SaveExperienceDetails)
                .addMultipartParameter("AEMEmployeeID",pref.getEmpId())
                .addMultipartParameter("isExperience", isexperince)
                .addMultipartParameter("SBIExperienceMonth", binding.etSBIExp.getText().toString())
                .addMultipartParameter("NonSBIExperienceMonth", binding.etNonSBIExp.getText().toString())
                .addMultipartParameter("PreviousCompanyName", binding.etcompany.getText().toString())
                .addMultipartParameter("PreviousEmployeeDesignation", binding.etDesignation.getText().toString())
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())
                .addMultipartParameter("DocumentID", "0070")
                .addMultipartParameter("PreviousReportingManagerName", binding.etManagerName.getText().toString())
                .addMultipartParameter("PreviousReportingManagerDesignation", binding.etManagerDesignation.getText().toString())
                .addMultipartParameter("PreviousReportingManagerContactDetails", binding.etManagerContact.getText().toString())
                .addMultipartParameter("PreviousCompanyDOJ", doj)
                .addMultipartParameter("PreviousCompanyDOE", doe)
                .addMultipartFile("SingleFile", compressedImageFile)
                .setPercentageThresholdForCancelling(60)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        progressDialog.show();

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);

                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            if (pfflag==1){
                                progressDialog.show();
                                JSONObject mainobject = new JSONObject();
                                try {
                                    mainobject.put("DbOperation", "4");
                                    mainobject.put("SecurityCode", pref.getSecurityCode());
                                    JSONObject innerobj = new JSONObject();
                                    innerobj.put("AEMEMPLOYEEID", pref.getEmpId());
                                    innerobj.put("OldUANNo", binding.etUAN.getText().toString());
                                    innerobj.put("MemberName", binding.etUANNominee.getText().toString());
                                    innerobj.put("RelationID", uanRltionshp);
                                    innerobj.put("MemberDateOfBirth", uanDOB);
                                    innerobj.put("Sex", "MALE");
                                    innerobj.put("PFPercentage", pfPercantage);
                                    innerobj.put("PFNumber", binding.etPFNumber.getText().toString());
                                    innerobj.put("NomineeAddress", binding.etUANNomineeAddress.getText().toString());
                                    innerobj.put("PrevEmprName", binding.etcompany.getText().toString());
                                    mainobject.put("OLDUANDetails", innerobj);
                                    uploaduanDetails(mainobject,progressDialog);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }else {
                                progressDialog.dismiss();
                                Intent intent = new Intent(TempExperinceActivity.this, TempPanActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                                Toast.makeText(TempExperinceActivity.this,"Experience Details has been updated Successfully",Toast.LENGTH_LONG).show();

                            }

                        }else {


                        }

                        // boolean _status = job1.getBoolean("status");



                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e("errt", String.valueOf(error));

                        Toast.makeText(getApplicationContext(), "Something went wrong,Please try again", Toast.LENGTH_LONG).show();
                    }
                });

    }
    private void setNomineeRelation() {

        String surl = AppData.url + "gcl_CommonDDL?ddltype=7&id1=0&id2=0&id3=0&SecurityCode=" + pref.getSecurityCode();
        ProgressDialog pd=new ProgressDialog(TempExperinceActivity.this);
        pd.setMessage("Laoding");
        pd.show();
        pd.setCancelable(false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        pd.dismiss();
                        realation.clear();
                        mainRealation.clear();
                        realation.add("Please Select");
                        mainRealation.add(new MainDocModule("0", ""));

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                //Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String value = obj.optString("value");
                                    String id = obj.optString("id");
                                    realation.add(value);
                                    MainDocModule mainDocModule = new MainDocModule(id, value);
                                    mainRealation.add(mainDocModule);

                                }
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (TempExperinceActivity.this, android.R.layout.simple_spinner_item,
                                                realation); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                binding.spUANRealation.setAdapter(spinnerArrayAdapter);



                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TempExperinceActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               pd.dismiss();

                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());

            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");


    }


    private void uploaduanDetails(JSONObject jsonObject,ProgressDialog pd) {
        pd.show();
        AndroidNetworking.post(AppData.newv2url + "KYC/UpdateKYCDetails")
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
                            Intent intent = new Intent(TempExperinceActivity.this, TempPanActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            Toast.makeText(TempExperinceActivity.this,"Experience Details has been updated Successfully",Toast.LENGTH_LONG).show();

                        } else {

                        }
                    }

                    @Override
                    public void onError(ANError error) {

                        Intent intent = new Intent(TempExperinceActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });
    }

    private void UANValidation(){
        if (binding.etUAN.getText().toString().length()==12){
            if (binding.etPFNumber.getText().toString().length()>5){
                experiencesubmit("1");
            }else {
                Toast.makeText(TempExperinceActivity.this,"Please Enter your PF number",Toast.LENGTH_LONG).show();
                binding.etPFNumber.setBackgroundResource(R.drawable.lldesign_error);
            }
        } else {
            Toast.makeText(TempExperinceActivity.this,"Please Enter your UAN number",Toast.LENGTH_LONG).show();
            binding.etUAN.setBackgroundResource(R.drawable.lldesign_error);
        }
    }

    void checkFresherOrExperienceByOfficeDetails(JSONObject jsonObject){
        ProgressDialog pd=new ProgressDialog(TempExperinceActivity.this);
        pd.setMessage("Loading...");
        pd.show();
        pd.setCancelable(false);
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
                            Log.e(TAG, "CHECK_EXPERIENCE: " + response.toString(4));
                            JSONObject job1 = response;
                            int Response_Code = job1.optInt("Response_Code");
                            if (Response_Code == 101) {
                                JSONObject Response_Data =  job1.optJSONObject("Response_Data");
                                JSONArray OfficeDetailsArray = Response_Data.optJSONArray("OfficeDetails");
                                for (int i = 0; i < OfficeDetailsArray.length(); i++) {
                                    JSONObject OfficeDetailsObj = OfficeDetailsArray.optJSONObject(i);
                                    String IsExperience = OfficeDetailsObj.optString("IsExperience");
                                    if (IsExperience.equals("1")){
                                        //TODO: Experience
                                        JSONObject jsonObject = new JSONObject();
                                        try {
                                            jsonObject.put("AEMConsultantID", pref.getEmpConId());
                                            jsonObject.put("AEMClientID", pref.getEmpClintId());
                                            jsonObject.put("AEMClientOfficeID", pref.getEmpClintOffId());
                                            jsonObject.put("AEMEmployeeID", pref.getEmpId());
                                            jsonObject.put("WorkingStatus", "1");
                                            jsonObject.put("Operation", "10");
                                            getExperienceDetails(jsonObject);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        //TODO: Fresher
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        pd.dismiss();
                        Log.e(TAG, "CHECK_EXPERIENCE_anError: "+anError.getErrorBody());
                    }
                });
    }

    void getExperienceDetails(JSONObject jsonObject){
        Log.e(TAG, "getExperienceDetails: INPUT: "+jsonObject);
        ProgressDialog pd=new ProgressDialog(TempExperinceActivity.this);
        pd.setMessage("Loading...");
        pd.show();
        pd.setCancelable(false);
        AndroidNetworking.post(AppData.KYC_GET_DETAILS)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "GET_EXPERIENCE_DETAILS: "+response.toString(4));
                            pd.dismiss();
                            JSONObject job1 = response;
                            int Response_Code = job1.optInt("Response_Code");
                            if (Response_Code == 101){
                                String Response_Data = job1.optString("Response_Data");
                                if (Response_Data != null){
                                    JSONObject job2 = new JSONObject(Response_Data);
                                    JSONArray jsonArray = job2.getJSONArray("ExperienceDetails");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject experienceObj = jsonArray.getJSONObject(i);
                                        binding.etcompany.setText(experienceObj.optString("CompanyName"));
                                        binding.etDesignation.setText(experienceObj.optString("PreviousEmployeeDesignation"));
                                        binding.etManagerName.setText(experienceObj.optString("PreviousReportingManagerName"));
                                        binding.etManagerDesignation.setText(experienceObj.optString("PreviousReportingManagerDesignation"));
                                        binding.etManagerContact.setText(experienceObj.optString("PreviousReportingManagerContactDetails"));
                                        String DOJ = experienceObj.optString("PrevDOJ");
                                        String DOE = experienceObj.optString("PrevDOE");
                                        doj = DOJ;
                                        doe = DOE;
                                        binding.etDOJ.setText(DOJ);
                                        binding.etDOE.setText(DOE);
                                        //imgAttachImage
                                        if (binding.imgExperience.getVisibility()==View.GONE){
                                            binding.imgFreshersTick.setVisibility(View.GONE);
                                            binding.imgExperience.setVisibility(View.VISIBLE);
                                            binding.llExpericedForm.setVisibility(View.VISIBLE);
                                            flag=2;
                                        } else {
                                            binding.imgFreshersTick.setVisibility(View.GONE);
                                            binding.imgExperience.setVisibility(View.GONE);
                                            binding.llExpericedForm.setVisibility(View.GONE);
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        pd.dismiss();
                        Log.e(TAG, "GET_EXPERIENCE_DETAILS_anError: "+anError.getErrorBody());
                    }
                });
    }
}