package io.cordova.myapp00d753.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.cameraview.LongImageCameraActivity;


import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


import id.zelory.compressor.Compressor;
import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.AttendanceService;
import io.cordova.myapp00d753.module.SpineerItemModel;
import io.cordova.myapp00d753.module.UploadObject;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.FilePath;
import io.cordova.myapp00d753.utility.Pref;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecktitRemActivity extends AppCompatActivity {
    private static final String TAG = "Recktit_Rem_Activity";
    ImageView imgBack, imgHome;
    Spinner spComponent,spLocation;
    ArrayList<SpineerItemModel> modelLocationList = new ArrayList<>();
    ArrayList<String> locationList = new ArrayList<>();

    ArrayList<SpineerItemModel> modelComponentList = new ArrayList<>();
    ArrayList<String> componentList = new ArrayList<>();
    Pref pref;
    TextView tvDate;
    String nosaledate;
    ImageView imgDate;
    String monthname;
    private String encodedImage;
    private Uri imageUri;
    private static final int CAMERA_REQUEST = 1;
    private static final int CAMERA_REQUEST1 = 2;
    private static final int CAMERA_REQUEST2 = 3;
    private static final int CAMERA_REQUEST3 = 4;
    private static final int CAMERA_REQUEST4 = 5;

    ImageView imgCamera1, imgDoc1, imgCamera2, imgDoc2, imgCamera3, imgDoc3, imgCamera4, imgDoc4, imgCamera5, imgDoc5;
    File file;
    EditText etDescription, etAmount;
    String componentId = "";
    String description = "";
    String amount = "";
    int y;
    String year, month;
    String aempid, securitycode;
    private static final String SERVER_PATH = AppData.url;
    private AttendanceService uploadService;
    ProgressDialog progressDialog;
    LinearLayout llSubmit;
    File file1, file2, file3, file4,compressedImageFile,compressedImageFile1,compressedImageFile2,compressedImageFile3,compressedImageFile4;
    int flag = 0;
    AlertDialog alerDialog1, alertDialog2, alertDialog1,alert1;
    TextView tvMonth, tvYear;
    String comeid = "";
    LinearLayout llAttach, llBrowse;
    ImageView imgAttach, imgPDF;
    private static final int PICK_PDF_REQUEST = 10;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;
    private Uri filePath;
    Button btnSubmit;
    public static final String UPLOAD_URL = AppData.url+"post_reimbursementClaimByComponent";
    int pdfflag;
    LinearLayout llCameraD,llCamera;
    String loactionId;
    ImageView imgMultiPle;
    ImageView imgMultiPleImg;
    String newdate;
    Spinner spMonth,spYear;
    String spmonth="",spyear="";
    ArrayList<String>yearList=new ArrayList<>();
    ArrayList<String>monthList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recktit_rem);
        initialize();
        setLocationItem();
        JSONObject obj = new JSONObject();
        try {
            obj.put("ddltype", "505");
            obj.put("id1", pref.getEmpConId());
            obj.put("id2", pref.getEmpClintId());
            obj.put("id3", "0");
            obj.put("SecurityCode", pref.getSecurityCode());
            setLocationItem(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        onClick();
    }

    private void initialize() {
        pref = new Pref(getApplicationContext());
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        spComponent = (Spinner) findViewById(R.id.spComponent);
        tvDate = (TextView) findViewById(R.id.tvDate);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        nosaledate = df.format(c);

        imgDate = (ImageView) findViewById(R.id.imgDate);
        imgCamera1 = (ImageView) findViewById(R.id.imgCamera1);
        imgDoc1 = (ImageView) findViewById(R.id.imgDoc1);
        etDescription = (EditText) findViewById(R.id.etDescription);
        etAmount = (EditText) findViewById(R.id.etAmount);
        y = Calendar.getInstance().get(Calendar.YEAR);
        year = String.valueOf(y);
        Log.d("year", year);

        int m = Calendar.getInstance().get(Calendar.MONTH) + 1;
        Log.d("month", String.valueOf(m));
        if (m == 1) {
            month = "January";
        } else if (m == 2) {
            month = "February";
        } else if (m == 3) {
            month = "March";
        } else if (m == 4) {
            month = "April";
        } else if (m == 5) {
            month = "May";
        } else if (m == 6) {
            month = "June";
        } else if (m == 7) {
            month = "July";
        } else if (m == 8) {
            month = "August";
        } else if (m == 9) {
            month = "September";
        } else if (m == 10) {
            month = "October";
        } else if (m == 11) {
            month = "November";
        } else if (m == 12) {
            month = "December";
        }

        aempid = pref.getEmpId();
        securitycode = pref.getSecurityCode();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(660, TimeUnit.SECONDS)
                .connectTimeout(660, TimeUnit.SECONDS)
                .build();

        // Change base URL to your upload server URL.
        uploadService = (AttendanceService) new Retrofit.Builder()
                .baseUrl(SERVER_PATH)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AttendanceService.class);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        llSubmit = (LinearLayout) findViewById(R.id.llSubmit);
        imgCamera2 = (ImageView) findViewById(R.id.imgCamera2);
        imgDoc2 = (ImageView) findViewById(R.id.imgDoc2);
        imgCamera3 = (ImageView) findViewById(R.id.imgCamera3);
        imgDoc3 = (ImageView) findViewById(R.id.imgDoc3);
        imgCamera4 = (ImageView) findViewById(R.id.imgCamera4);
        imgDoc4 = (ImageView) findViewById(R.id.imgDoc4);
        imgCamera5 = (ImageView) findViewById(R.id.imgCamera5);
        imgDoc5 = (ImageView) findViewById(R.id.imgDoc5);
        tvMonth = (TextView) findViewById(R.id.tvMonth);
        tvYear = (TextView) findViewById(R.id.tvYear);
        tvMonth = (TextView) findViewById(R.id.tvMonth);
        tvYear.setText(year);

        Date d = Calendar.getInstance().getTime();
        System.out.println("Current time => " + d);

        SimpleDateFormat dfe = new SimpleDateFormat("dd-MMM-yyyy");
        newdate = dfe.format(d);
        tvMonth.setText(newdate);
        llBrowse = (LinearLayout) findViewById(R.id.llBrowse);
        llAttach = (LinearLayout) findViewById(R.id.llAttach);
        imgAttach = (ImageView) findViewById(R.id.imgAttach);
        imgPDF = (ImageView) findViewById(R.id.imgPDF);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        llCamera=(LinearLayout)findViewById(R.id.llCamera);
        llCameraD=(LinearLayout)findViewById(R.id.llCameraD);
        spLocation=(Spinner)findViewById(R.id.spLocation);

        imgMultiPleImg=(ImageView) findViewById(R.id.imgMultiPleImg);
        imgMultiPle=(ImageView)findViewById(R.id.imgMultiPle);
        yearList.add("Please Select");
        yearList.add("2021");
        yearList.add("2022");
        yearList.add("2023");
        yearList.add("2024");
        yearList.add("2025");

        monthList.add("Please Select");
        monthList.add("January");
        monthList.add("February");
        monthList.add("March");
        monthList.add("April");
        monthList.add("May");
        monthList.add("June");
        monthList.add("July");
        monthList.add("August");
        monthList.add("September");
        monthList.add("October");
        monthList.add("November");
        monthList.add("December");

        spYear=(Spinner)findViewById(R.id.spYear);
        spMonth=(Spinner)findViewById(R.id.spMonth);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (RecktitRemActivity.this, android.R.layout.simple_spinner_item,
                        yearList); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        int pos=yearList.indexOf(year);
        spYear.setSelection(pos);
        spYear.setAdapter(spinnerArrayAdapter);


        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>
                (RecktitRemActivity.this, android.R.layout.simple_spinner_item,
                        monthList); //selected item will look like a spinner set from XML
        spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        int pos1=yearList.indexOf(month);
        spMonth.setSelection(pos1);
        spMonth.setAdapter(spinnerArrayAdapter1);



    }

    private void onClick() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecktitRemActivity.this, EmployeeDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        imgDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                c.add(Calendar.DAY_OF_MONTH, -7);


                final DatePickerDialog dialog = new DatePickerDialog(RecktitRemActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {

                        newdate = (m + 1) + "/" + d + "/" + y;


                        tvMonth.setText(newdate);

                    }
                }, year, month, day);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();

            }
        });
        imgCamera1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraIntent();
            }
        });
        spComponent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                componentId = modelComponentList.get(position).getItemId();
                Log.d("componentId", componentId);
                String compName=modelComponentList.get(position).getItemName();
                if (componentId.equals("SAEMCM1110000524")) {

                    //setAmount(loactionId, componentId);
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("LocationType", loactionId);
                        obj.put("Component", componentId);
                        obj.put("Year", year);
                        obj.put("Month", month);
                        obj.put("AEMEmployeeID", pref.getEmpId());
                        obj.put("SecurityCode", pref.getSecurityCode());
                        //"get_ReimbursementCompValidation?LocationType="+loactionId+"&Component="+compid+"&Year="+year+"&Month="+month+"&AEMEmployeeID="+pref.getEmpId()+"&SecurityCode="+pref.getSecurityCode();
                        setAmount(obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    etAmount.setText("");
                    etAmount.setEnabled(true);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        etDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etDescription.getText().toString().length() > 0) {
                    description = etDescription.getText().toString();
                }
            }
        });
        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etAmount.getText().toString().length() > 0) {
                    amount = etAmount.getText().toString();
                }
            }
        });
        tvYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showYearDialog();
            }
        });
        tvMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMonthDialog();
            }
        });

        imgCamera2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraIntent1();
            }
        });
        imgCamera3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraIntent2();
            }
        });
        imgCamera4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraIntent3();
            }
        });
        imgPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        imgCamera5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraIntent4();
            }
        });
        llBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llAttach.getVisibility() == View.GONE) {
                    llAttach.setVisibility(View.VISIBLE);
                    llCameraD.setVisibility(View.GONE);
                    llCamera.setVisibility(View.GONE);
                } else {
                    llAttach.setVisibility(View.GONE);
                    llCameraD.setVisibility(View.VISIBLE);
                    llCamera.setVisibility(View.VISIBLE);
                }
            }
        });

        imgMultiPle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LongImageCameraActivity.launch(RecktitRemActivity.this);
            }
        });
        llCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llCameraD.getVisibility()==View.GONE){
                    llCameraD.setVisibility(View.VISIBLE);
                    llAttach.setVisibility(View.GONE);
                    llBrowse.setVisibility(View.GONE);
                }else {
                    llCameraD.setVisibility(View.GONE);
                    llAttach.setVisibility(View.VISIBLE);
                    llBrowse.setVisibility(View.VISIBLE);
                }
            }
        });

        spLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loactionId=modelLocationList.get(position).getItemId();
                Log.d("loactionid",loactionId);
                //setCompItem(loactionId);

                JSONObject obj = new JSONObject();
                try {
                    obj.put("ddltype", "506");
                    obj.put("id1", loactionId);
                    obj.put("id2", pref.getEmpClintId());
                    obj.put("id3", "0");
                    setCompItem(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>0) {
                    spyear = yearList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>0) {
                    spmonth = monthList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (componentId != "") {
                    if (etDescription.getText().toString().length() > 0) {
                        if (etAmount.getText().toString().length() > 0) {
                            if (pdfflag == 1) {
                                uploadMultipart();
                            } else {
                                Toast.makeText(getApplicationContext(), "Please attach PDF file", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Please enter Claim Amount", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter Decription", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please Select Component", Toast.LENGTH_LONG).show();
                }
            }
        });

        imgAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });



        llSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag != 0) {
                    if (componentId != "") {
                        if (etDescription.getText().toString().length() > 0) {
                            if (etAmount.getText().toString().length() > 0) {
                                if (!spyear.equals("")){
                                    if (!spmonth.equals("")){
                                        if (flag == 1) {
                                            //postoneimage();
                                            postOneImage();
                                        } else if (flag == 2) {
                                            //posttwoimage();
                                            postTwoImage();
                                        } else if (flag == 3) {
                                            //postthreeimage();
                                            postThreeImage();
                                        } else if (flag == 4) {
                                            //postfourimage();
                                            postFourImage();
                                        } else if (flag == 5) {
                                            //postfiveimage();
                                            postFiveImage();
                                        }

                                    }else {
                                        Toast.makeText(getApplicationContext(),"Please Select Month",Toast.LENGTH_LONG).show();
                                    }

                                }else {
                                    Toast.makeText(getApplicationContext(), "Please Select Year", Toast.LENGTH_LONG).show();
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), "Please enter Claim Amount", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Please enter Decription", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Select Component", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please Upload Image", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void postFiveImage() {
        progressDialog.show();
        AndroidNetworking.upload(AppData.SAVE_REIMBURSEMENT_CLAIM_BY_COMPONENT)
                .addMultipartParameter("AEMEmployeeID", aempid)
                .addMultipartParameter("AEMComponentID", comeid)
                .addMultipartParameter("Description", description)
                .addMultipartParameter("ReimbursementAmount",amount)
                .addMultipartParameter("Year",year)
                .addMultipartParameter("Month",month)
                .addMultipartParameter("SecurityCode",securitycode)
                .addMultipartParameter("ConveyanceTypeId",componentId)
                .addMultipartParameter("LocationTypeID","0")
                .addMultipartParameter("ReimbursementDate","0")
                .addMultipartFile("file",compressedImageFile)
                .addMultipartFile("fil1",compressedImageFile1)
                .addMultipartFile("fil2",compressedImageFile2)
                .addMultipartFile("fil3",compressedImageFile3)
                .addMultipartFile("fil4",compressedImageFile4)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.show();
                            Log.e(TAG, "RECKTIT_POST_THREE_IMAGE: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                successAlert();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.show();
                        Log.e(TAG, "RECKTIT_POST_THREE_IMAGE_error: "+anError.getErrorBody());
                    }
                });
    }

    private void postFourImage() {
        progressDialog.show();
        AndroidNetworking.upload(AppData.SAVE_REIMBURSEMENT_CLAIM_BY_COMPONENT)
                .addMultipartParameter("AEMEmployeeID", aempid)
                .addMultipartParameter("AEMComponentID", comeid)
                .addMultipartParameter("Description", description)
                .addMultipartParameter("ReimbursementAmount",amount)
                .addMultipartParameter("Year",year)
                .addMultipartParameter("Month",month)
                .addMultipartParameter("SecurityCode",securitycode)
                .addMultipartParameter("ConveyanceTypeId",componentId)
                .addMultipartParameter("LocationTypeID","0")
                .addMultipartParameter("ReimbursementDate","0")
                .addMultipartFile("file",compressedImageFile)
                .addMultipartFile("fil1",compressedImageFile1)
                .addMultipartFile("fil2",compressedImageFile2)
                .addMultipartFile("fil3",compressedImageFile3)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.show();
                            Log.e(TAG, "RECKTIT_POST_THREE_IMAGE: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                successAlert();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.show();
                        Log.e(TAG, "RECKTIT_POST_THREE_IMAGE_error: "+anError.getErrorBody());
                    }
                });
    }

    private void postThreeImage() {
        progressDialog.show();
        AndroidNetworking.upload(AppData.SAVE_REIMBURSEMENT_CLAIM_BY_COMPONENT)
                .addMultipartParameter("AEMEmployeeID", aempid)
                .addMultipartParameter("AEMComponentID", comeid)
                .addMultipartParameter("Description", description)
                .addMultipartParameter("ReimbursementAmount",amount)
                .addMultipartParameter("Year",year)
                .addMultipartParameter("Month",month)
                .addMultipartParameter("SecurityCode",securitycode)
                .addMultipartParameter("ConveyanceTypeId",componentId)
                .addMultipartParameter("LocationTypeID","0")
                .addMultipartParameter("ReimbursementDate","0")
                .addMultipartFile("file",compressedImageFile)
                .addMultipartFile("fil1",compressedImageFile1)
                .addMultipartFile("fil2",compressedImageFile2)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.show();
                            Log.e(TAG, "RECKTIT_POST_THREE_IMAGE: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                successAlert();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.show();
                        Log.e(TAG, "RECKTIT_POST_THREE_IMAGE_error: "+anError.getErrorBody());
                    }
                });
    }

    private void postOneImage() {
        progressDialog.show();
        AndroidNetworking.upload(AppData.SAVE_REIMBURSEMENT_CLAIM_BY_COMPONENT)
                .addMultipartParameter("AEMEmployeeID", aempid)
                .addMultipartParameter("AEMComponentID", comeid)
                .addMultipartParameter("Description", description)
                .addMultipartParameter("ReimbursementAmount",amount)
                .addMultipartParameter("Year",year)
                .addMultipartParameter("Month",month)
                .addMultipartParameter("SecurityCode",securitycode)
                .addMultipartParameter("ConveyanceTypeId",componentId)
                .addMultipartParameter("LocationTypeID","0")
                .addMultipartParameter("ReimbursementDate","0")
                .addMultipartFile("file",compressedImageFile)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.show();
                            Log.e(TAG, "RECKTIT_POST_ONE_IMAGE: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                successAlert();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.show();
                        Log.e(TAG, "RECKTIT_POST_ONE_IMAGE: "+anError.getErrorBody());
                    }
                });
    }

    private void postTwoImage() {
        progressDialog.show();
        AndroidNetworking.upload(AppData.SAVE_REIMBURSEMENT_CLAIM_BY_COMPONENT)
                .addMultipartParameter("AEMEmployeeID", aempid)
                .addMultipartParameter("AEMComponentID", comeid)
                .addMultipartParameter("Description", description)
                .addMultipartParameter("ReimbursementAmount",amount)
                .addMultipartParameter("Year",year)
                .addMultipartParameter("Month",month)
                .addMultipartParameter("SecurityCode",securitycode)
                .addMultipartParameter("ConveyanceTypeId",componentId)
                .addMultipartParameter("LocationTypeID","0")
                .addMultipartParameter("ReimbursementDate","0")
                .addMultipartFile("file",compressedImageFile)
                .addMultipartFile("fil1",compressedImageFile1)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.show();
                            Log.e(TAG, "RECKTIT_POST_TWO_IMAGES: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                successAlert();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.show();
                        Log.e(TAG, "RECKTIT_POST_TWO_IMAGES_error: "+anError.getErrorBody());
                    }
                });
    }


    private void cameraIntent() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Profile Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private void cameraIntent1() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Profile Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST1);
    }

    private void cameraIntent2() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Profile Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST2);
    }

    private void cameraIntent3() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Profile Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST3);
    }

    private void cameraIntent4() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Profile Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST4);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_REQUEST:

                if (resultCode == Activity.RESULT_OK) {
                    try {
                        try {
                            String imageurl = /*"file://" +*/ getRealPathFromURI(imageUri);
                            file = new File(imageurl);
                            compressedImageFile = new Compressor(this).compressToFile(file);
                            //Log.d("imageSixw", String.valueOf(getReadableFileSize(compressedImageFile.length())));

                            BitmapFactory.Options o = new BitmapFactory.Options();
                            o.inSampleSize = 2;
                            Bitmap bm = cropToSquare(BitmapFactory.decodeFile(imageurl, o));
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.PNG, 10, baos); //bm is the bitmap object
                            byte[] b = baos.toByteArray();
                            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                            imgDoc1.setImageBitmap(bm);
                            Log.d("images", encodedImage);
                            flag = 1;



                            // _pref.saveImage(encodedImage);
                            //saveImage(encodedImage);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }

                }
                break;
            case CAMERA_REQUEST1:

                if (resultCode == Activity.RESULT_OK) {
                    try {
                        try {
                            String imageurl = /*"file://" +*/ getRealPathFromURI(imageUri);
                            file1 = new File(imageurl);
                            compressedImageFile1 = new Compressor(this).compressToFile(file1);
                            //Log.d("imageSixw", String.valueOf(getReadableFileSize(compressedImageFile1.length())));

                            BitmapFactory.Options o = new BitmapFactory.Options();
                            o.inSampleSize = 2;
                            Bitmap bm = cropToSquare(BitmapFactory.decodeFile(imageurl, o));
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.PNG, 10, baos); //bm is the bitmap object
                            byte[] b = baos.toByteArray();
                            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                            imgDoc2.setImageBitmap(bm);
                            Log.d("images", encodedImage);
                            flag = 2;
                            // _pref.saveImage(encodedImage);
                            //saveImage(encodedImage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }

                }
                break;
            case CAMERA_REQUEST2:

                if (resultCode == Activity.RESULT_OK) {
                    try {
                        try {
                            String imageurl = /*"file://" +*/ getRealPathFromURI(imageUri);
                            file2 = new File(imageurl);
                            compressedImageFile2 = new Compressor(this).compressToFile(file2);
                            BitmapFactory.Options o = new BitmapFactory.Options();
                            o.inSampleSize = 2;
                            Bitmap bm = cropToSquare(BitmapFactory.decodeFile(imageurl, o));
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.PNG, 10, baos); //bm is the bitmap object
                            byte[] b = baos.toByteArray();
                            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                            imgDoc3.setImageBitmap(bm);
                            Log.d("images", encodedImage);
                            flag = 3;


                            // _pref.saveImage(encodedImage);
                            //saveImage(encodedImage);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }

                }
                break;
            case CAMERA_REQUEST3:

                if (resultCode == Activity.RESULT_OK) {
                    try {
                        try {
                            String imageurl = /*"file://" +*/ getRealPathFromURI(imageUri);
                            file3 = new File(imageurl);
                            compressedImageFile3 = new Compressor(this).compressToFile(file3);
                            BitmapFactory.Options o = new BitmapFactory.Options();
                            o.inSampleSize = 2;
                            Bitmap bm = cropToSquare(BitmapFactory.decodeFile(imageurl, o));
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.PNG, 10, baos); //bm is the bitmap object
                            byte[] b = baos.toByteArray();
                            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                            imgDoc4.setImageBitmap(bm);
                            Log.d("images", encodedImage);
                            flag = 4;


                            // _pref.saveImage(encodedImage);
                            //saveImage(encodedImage);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }

                }
                break;
            case CAMERA_REQUEST4:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        try {
                            String imageurl = /*"file://" +*/ getRealPathFromURI(imageUri);
                            file4 = new File(imageurl);
                            compressedImageFile4 = new Compressor(this).compressToFile(file4);
                            BitmapFactory.Options o = new BitmapFactory.Options();
                            o.inSampleSize = 2;
                            Bitmap bm = cropToSquare(BitmapFactory.decodeFile(imageurl, o));
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.PNG, 10, baos); //bm is the bitmap object
                            byte[] b = baos.toByteArray();
                            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                            imgDoc5.setImageBitmap(bm);
                            Log.d("images", encodedImage);
                            flag = 5;


                            // _pref.saveImage(encodedImage);
                            //saveImage(encodedImage);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }

                }
                break;
            case PICK_PDF_REQUEST:
                if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                    filePath = data.getData();
                    imgPDF.setVisibility(View.VISIBLE);
                    pdfflag = 1;

                }


                break;

            case LongImageCameraActivity.LONG_IMAGE_RESULT_CODE:


                if (resultCode == RESULT_OK && requestCode == LongImageCameraActivity.LONG_IMAGE_RESULT_CODE) {
                    String imageFileName = data.getStringExtra(LongImageCameraActivity.IMAGE_PATH_KEY);
                    Log.d("imageFileName", imageFileName);
                    Bitmap d = BitmapFactory.decodeFile(imageFileName);
                    int newHeight = (int) (d.getHeight() * (512.0 / d.getWidth()));
                    Bitmap putImage = Bitmap.createScaledBitmap(d, 512, newHeight, true);
                    imgMultiPleImg.setImageBitmap(putImage);
                    flag = 1;
                    File pictureFile = (File)data.getExtras().get("picture");
                    Log.d("fjjgk",pictureFile.toString());
                    try {
                        compressedImageFile = new Compressor(this).compressToFile(pictureFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }


    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static Bitmap cropToSquare(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = (height > width) ? width : height;
        int newHeight = (height > width) ? height - (height - width) : height;
        int cropW = (width - height) / 2;
        cropW = (cropW < 0) ? 0 : cropW;
        int cropH = (height - width) / 2;
        cropH = (cropH < 0) ? 0 : cropH;
        Bitmap cropImg = Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);

        return cropImg;
    }


    private void showYearDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RecktitRemActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_year, null);
        dialogBuilder.setView(dialogView);
        final TextView tvYear1 = (TextView) dialogView.findViewById(R.id.tvYear1);
        final TextView tvYear2 = (TextView) dialogView.findViewById(R.id.tvYear2);
        final TextView tvYear3 = (TextView) dialogView.findViewById(R.id.tvYear3);
        LinearLayout llY1 = (LinearLayout) dialogView.findViewById(R.id.llY1);
        LinearLayout llY2 = (LinearLayout) dialogView.findViewById(R.id.llY2);
        LinearLayout llY3 = (LinearLayout) dialogView.findViewById(R.id.llY3);

        int pastx1 = y - 2;
        String pasty1 = String.valueOf(pastx1);
        tvYear1.setText(pasty1);

        int pastx2 = y - 1;
        String pasty2 = String.valueOf(pastx2);
        tvYear2.setText(pasty2);

        String pastx3 = String.valueOf(y);
        tvYear3.setText(pastx3);

        ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog1.dismiss();


            }
        });


        llY3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = tvYear3.getText().toString();
                Log.d("yrtrr", year);
                tvYear.setText(year);
                alertDialog1.dismiss();

            }
        });

        llY2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = tvYear2.getText().toString();
                alertDialog1.dismiss();
                tvYear.setText(year);
                Log.d("ttt", year);
            }
        });

        llY1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = tvYear1.getText().toString();
                alertDialog1.dismiss();
                tvYear.setText(year);
                Log.d("ttt", year);
            }
        });

        alertDialog1 = dialogBuilder.create();
        alertDialog1.setCancelable(true);
        Window window = alertDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog1.show();
    }

    private void showMonthDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RecktitRemActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_month, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llM1 = (LinearLayout) dialogView.findViewById(R.id.llM1);
        LinearLayout llM2 = (LinearLayout) dialogView.findViewById(R.id.llM2);
        LinearLayout llM3 = (LinearLayout) dialogView.findViewById(R.id.llM3);
        LinearLayout llM4 = (LinearLayout) dialogView.findViewById(R.id.llM4);
        LinearLayout llM5 = (LinearLayout) dialogView.findViewById(R.id.llM5);
        LinearLayout llM6 = (LinearLayout) dialogView.findViewById(R.id.llM6);
        LinearLayout llM7 = (LinearLayout) dialogView.findViewById(R.id.llM7);
        LinearLayout llM8 = (LinearLayout) dialogView.findViewById(R.id.llM8);
        LinearLayout llM9 = (LinearLayout) dialogView.findViewById(R.id.llM9);
        LinearLayout llM10 = (LinearLayout) dialogView.findViewById(R.id.llM10);
        LinearLayout llM11 = (LinearLayout) dialogView.findViewById(R.id.llM111);
        LinearLayout llM112 = (LinearLayout) dialogView.findViewById(R.id.llM12);

        final TextView tvJan = (TextView) dialogView.findViewById(R.id.tvJan);
        tvJan.setText("January");
        final TextView tvFeb = (TextView) dialogView.findViewById(R.id.tvFeb);
        final TextView tvMarch = (TextView) dialogView.findViewById(R.id.tvMarch);
        final TextView tvApril = (TextView) dialogView.findViewById(R.id.tvApril);
        final TextView tvMay = (TextView) dialogView.findViewById(R.id.tvMay);
        final TextView tvJune = (TextView) dialogView.findViewById(R.id.tvJune);
        final TextView tvJuly = (TextView) dialogView.findViewById(R.id.tvJuly);
        final TextView tvAugust = (TextView) dialogView.findViewById(R.id.tvAugust);
        final TextView tvSept = (TextView) dialogView.findViewById(R.id.tvSeptember);
        final TextView tvOct = (TextView) dialogView.findViewById(R.id.tvOct);
        final TextView tvNov = (TextView) dialogView.findViewById(R.id.tvNovember);
        final TextView tvDec = (TextView) dialogView.findViewById(R.id.tvDecember);

        llM1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvJan.getText().toString();
                Log.d("monnn", month);
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvFeb.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });

        llM3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvMarch.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvApril.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvMay.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });

        llM6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvJune.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvJuly.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvAugust.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvSept.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvOct.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvNov.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        llM112.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month = tvDec.getText().toString();
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });
        ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog2.dismiss();
            }
        });


        alertDialog2 = dialogBuilder.create();
        alertDialog2.setCancelable(true);
        Window window = alertDialog2.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog2.show();

    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);
    }

    private void setLocationItem(JSONObject jsonObject) {
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();
        AndroidNetworking.post(AppData.GET_COMMON_DROP_DOWN_FILL)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressBar.dismiss();
                            Log.e(TAG, "Recktit_Location_List: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(Response_Data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String comid = obj.optString("id");
                                    Log.d("comid", comid);
                                    String value = obj.optString("value");
                                    locationList.add(value);
                                    SpineerItemModel mainDocModule = new SpineerItemModel(value, comid);
                                    modelLocationList.add(mainDocModule);
                                }
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (RecktitRemActivity.this, android.R.layout.simple_spinner_item,
                                                locationList); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spLocation.setSelection(0);
                                spLocation.setAdapter(spinnerArrayAdapter);
                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RecktitRemActivity.this, "Something want to wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressBar.dismiss();
                        Log.e(TAG, "Recktit_Location_List: "+anError.getErrorBody());
                    }
                });
    }

    private void setLocationItem() {
        String surl = AppData.url+"gcl_CommonDDL?ddltype=505&id1=" + pref.getEmpConId() + "&id2=" + pref.getEmpClintId() + "&id3=0&SecurityCode=" + pref.getSecurityCode();
        Log.d("compurl", surl);
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);

                        progressBar.dismiss();
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
                                    String comid = obj.optString("id");
                                    Log.d("comid", comid);
                                    String value = obj.optString("value");
                                    locationList.add(value);
                                    SpineerItemModel mainDocModule = new SpineerItemModel(value, comid);
                                    modelLocationList.add(mainDocModule);
                                }
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (RecktitRemActivity.this, android.R.layout.simple_spinner_item,
                                                locationList); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spLocation.setSelection(0);
                                spLocation.setAdapter(spinnerArrayAdapter);
                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RecktitRemActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");


    }

    private void setCompItem(JSONObject jsonObject) {
        Log.e(TAG, "setCompItem: "+jsonObject);
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();
        AndroidNetworking.post(AppData.GET_COMMON_DROP_DOWN_FILL)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "COMP_ITEM: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(Response_Data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String comid = obj.optString("id");
                                    Log.d("comid", comid);
                                    String value = obj.optString("value");
                                    componentList.add(value);
                                    SpineerItemModel mainDocModule = new SpineerItemModel(value, comid);
                                    modelComponentList.add(mainDocModule);
                                }
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (RecktitRemActivity.this, android.R.layout.simple_spinner_item,
                                                componentList); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spComponent.setSelection(0);
                                spComponent.setAdapter(spinnerArrayAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RecktitRemActivity.this, "Something want to wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressBar.dismiss();
                        Log.e(TAG, "COMP_ITEM_error: "+anError.getErrorBody());
                    }
                });
    }


    private void setCompItem(String loactionId) {
        String surl = AppData.url+"gcl_CommonDDL?ddltype=506&id1=" + loactionId + "&id2=" + pref.getEmpClintId() + "&id3=0&SecurityCode=" + pref.getSecurityCode();
        Log.d("compurl", surl);
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        progressBar.dismiss();
                        modelComponentList.clear();
                        componentList.clear();
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
                                    String comid = obj.optString("id");
                                    Log.d("comid", comid);
                                    String value = obj.optString("value");
                                    componentList.add(value);
                                    SpineerItemModel mainDocModule = new SpineerItemModel(value, comid);
                                    modelComponentList.add(mainDocModule);

                                }
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (RecktitRemActivity.this, android.R.layout.simple_spinner_item,
                                                componentList); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spComponent.setSelection(0);
                                spComponent.setAdapter(spinnerArrayAdapter);
                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RecktitRemActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");


    }

    private void setAmount(JSONObject jsonObject) {
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();
        AndroidNetworking.post(AppData.GET_REIMBURSEMENT_CLAIM_COMPONENT_RECKITT)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressBar.dismiss();
                            Log.e(TAG, "REAM_COMPONENT_RECKITT: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(Response_Data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String Amount=obj.optString("Amount");
                                    etAmount.setText(Amount);
                                    etAmount.setEnabled(false);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RecktitRemActivity.this, "Something want to wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressBar.dismiss();
                        Log.e(TAG, "REAM_COMPONENT_RECKITT_error: "+anError.getErrorBody());
                    }
                });
    }

    private void setAmount(String loactionId,String compid) {
        String surl = AppData.url+"get_ReimbursementCompValidation?LocationType="+loactionId+"&Component="+compid+"&Year="+year+"&Month="+month+"&AEMEmployeeID="+pref.getEmpId()+"&SecurityCode="+pref.getSecurityCode();
        Log.d("amounturl", surl);
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);

                        progressBar.dismiss();

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
                                    String Amount=obj.optString("Amount");
                                    etAmount.setText(Amount);
                                    etAmount.setEnabled(false);
                                }
                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RecktitRemActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
    }



    private void postoneimage() {
        Log.d("rem", "1");
        progressDialog.show();

        //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody mFile = RequestBody.create(MediaType.parse(".PNG"), compressedImageFile);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", compressedImageFile.getName(), mFile);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), compressedImageFile.getName());

        Call<UploadObject> fileUpload = uploadService.postreimburstmentwithimage1(aempid, comeid, etDescription.getText().toString(), amount, spyear, spmonth, securitycode, fileToUpload, componentId,loactionId,"0");
        fileUpload.enqueue(new Callback<UploadObject>() {
            @Override
            public void onResponse(Call<UploadObject> call, retrofit2.Response<UploadObject> response) {
                progressDialog.dismiss();
                UploadObject extraWorkingDayModel = response.body();
                if (extraWorkingDayModel.isResponseStatus()) {
                    //  Toast.makeText(getApplicationContext(), extraWorkingDayModel.getResponseText(), Toast.LENGTH_SHORT).show();
                    successAlert();


                } else {
                    Toast.makeText(getApplicationContext(), extraWorkingDayModel.getResponseText(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UploadObject> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("error", "Error " + t.getMessage());
                Toast.makeText(getApplicationContext(), "Something went wrong!Please try again", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void posttwoimage() {
        progressDialog.show();
        Log.d("rem", "2");

        //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody mFile = RequestBody.create(MediaType.parse(".PNG"), compressedImageFile);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", compressedImageFile.getName(), mFile);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), compressedImageFile.getName());

        RequestBody mFile1 = RequestBody.create(MediaType.parse(".PNG"), compressedImageFile1);
        MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("file", compressedImageFile1.getName(), mFile);
        RequestBody filename1 = RequestBody.create(MediaType.parse("text/plain"), compressedImageFile1.getName());

        Call<UploadObject> fileUpload = uploadService.postreimburstmentwithimage2(aempid, comeid, etDescription.getText().toString(), amount, spyear, spmonth, securitycode, fileToUpload, fileToUpload1, componentId,loactionId,"0");
        fileUpload.enqueue(new Callback<UploadObject>() {
            @Override
            public void onResponse(Call<UploadObject> call, retrofit2.Response<UploadObject> response) {
                progressDialog.dismiss();
                UploadObject extraWorkingDayModel = response.body();
                if (extraWorkingDayModel.isResponseStatus()) {
                    //  Toast.makeText(getApplicationContext(), extraWorkingDayModel.getResponseText(), Toast.LENGTH_SHORT).show();
                    successAlert();

                } else {
                    Toast.makeText(getApplicationContext(), extraWorkingDayModel.getResponseText(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UploadObject> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("error", "Error " + t.getMessage());
                Toast.makeText(getApplicationContext(), "Something went wrong!Please try again", Toast.LENGTH_LONG).show();

            }

        });

    }

    private void postthreeimage() {
        Log.d("rem", "3");
        progressDialog.show();

        //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody mFile = RequestBody.create(MediaType.parse(".PNG"), compressedImageFile);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", compressedImageFile.getName(), mFile);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), compressedImageFile.getName());

        RequestBody mFile1 = RequestBody.create(MediaType.parse(".PNG"), compressedImageFile1);
        MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("file", compressedImageFile1.getName(), mFile);
        RequestBody filename1 = RequestBody.create(MediaType.parse("text/plain"), compressedImageFile1.getName());

        RequestBody mFile2 = RequestBody.create(MediaType.parse(".PNG"), compressedImageFile2);
        MultipartBody.Part fileToUpload2 = MultipartBody.Part.createFormData("file", compressedImageFile2.getName(), mFile);
        RequestBody filename2 = RequestBody.create(MediaType.parse("text/plain"), compressedImageFile2.getName());

        Call<UploadObject> fileUpload = uploadService.postreimburstmentwithimage3(aempid, comeid, etDescription.getText().toString(), amount, spyear, spmonth, securitycode, fileToUpload, fileToUpload1, fileToUpload2, componentId,loactionId,"0");
        fileUpload.enqueue(new Callback<UploadObject>() {
            @Override
            public void onResponse(Call<UploadObject> call, retrofit2.Response<UploadObject> response) {
                progressDialog.dismiss();
                UploadObject extraWorkingDayModel = response.body();
                if (extraWorkingDayModel.isResponseStatus()) {
                    //  Toast.makeText(getApplicationContext(), extraWorkingDayModel.getResponseText(), Toast.LENGTH_SHORT).show();
                    successAlert();


                } else {
                    Toast.makeText(getApplicationContext(), extraWorkingDayModel.getResponseText(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UploadObject> call, Throwable t) {
                progressDialog.dismiss();

                Log.e("error", "Error " + t.getMessage());
                Toast.makeText(getApplicationContext(), "Something went wrong!Please try again", Toast.LENGTH_LONG).show();

            }

        });

    }

    private void postfourimage() {
        Log.d("rem", "4");
        progressDialog.show();

        //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody mFile = RequestBody.create(MediaType.parse(".PNG"), compressedImageFile);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", compressedImageFile.getName(), mFile);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), compressedImageFile.getName());

        RequestBody mFile1 = RequestBody.create(MediaType.parse(".PNG"), compressedImageFile1);
        MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("file", compressedImageFile1.getName(), mFile);
        RequestBody filename1 = RequestBody.create(MediaType.parse("text/plain"), compressedImageFile1.getName());

        RequestBody mFile2 = RequestBody.create(MediaType.parse(".PNG"), compressedImageFile2);
        MultipartBody.Part fileToUpload2 = MultipartBody.Part.createFormData("file", compressedImageFile2.getName(), mFile);
        RequestBody filename2 = RequestBody.create(MediaType.parse("text/plain"), compressedImageFile2.getName());

        RequestBody mFile3 = RequestBody.create(MediaType.parse(".PNG"), compressedImageFile3);
        MultipartBody.Part fileToUpload3 = MultipartBody.Part.createFormData("file", compressedImageFile3.getName(), mFile);
        RequestBody filename3 = RequestBody.create(MediaType.parse("text/plain"), compressedImageFile3.getName());

        Call<UploadObject> fileUpload = uploadService.postreimburstmentwithimage4(aempid, comeid, etDescription.getText().toString(), amount, spyear, spmonth, securitycode, fileToUpload, fileToUpload1, fileToUpload2, fileToUpload3, componentId,loactionId,"0");
        fileUpload.enqueue(new Callback<UploadObject>() {
            @Override
            public void onResponse(Call<UploadObject> call, retrofit2.Response<UploadObject> response) {
                progressDialog.dismiss();
                UploadObject extraWorkingDayModel = response.body();
                if (extraWorkingDayModel.isResponseStatus()) {
                    //  Toast.makeText(getApplicationContext(), extraWorkingDayModel.getResponseText(), Toast.LENGTH_SHORT).show();
                    successAlert();


                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong!Please try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UploadObject> call, Throwable t) {
                progressDialog.dismiss();

                Log.e("error", "Error " + t.getMessage());
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();

            }

        });

    }

    private void postfiveimage() {
        Log.d("rem", "5");
        progressDialog.show();

        //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody mFile = RequestBody.create(MediaType.parse(".PNG"), compressedImageFile);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", compressedImageFile.getName(), mFile);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), compressedImageFile.getName());

        RequestBody mFile1 = RequestBody.create(MediaType.parse(".PNG"), compressedImageFile1);
        MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("file", compressedImageFile1.getName(), mFile);
        RequestBody filename1 = RequestBody.create(MediaType.parse("text/plain"), compressedImageFile1.getName());

        RequestBody mFile2 = RequestBody.create(MediaType.parse(".PNG"), compressedImageFile2);
        MultipartBody.Part fileToUpload2 = MultipartBody.Part.createFormData("file", compressedImageFile2.getName(), mFile);
        RequestBody filename2 = RequestBody.create(MediaType.parse("text/plain"), compressedImageFile2.getName());

        RequestBody mFile3 = RequestBody.create(MediaType.parse(".PNG"), compressedImageFile3);
        MultipartBody.Part fileToUpload3 = MultipartBody.Part.createFormData("file", compressedImageFile3.getName(), mFile);
        RequestBody filename3 = RequestBody.create(MediaType.parse("text/plain"), compressedImageFile3.getName());

        RequestBody mFile4 = RequestBody.create(MediaType.parse(".PNG"), compressedImageFile4);
        MultipartBody.Part fileToUpload4 = MultipartBody.Part.createFormData("file", compressedImageFile4.getName(), mFile);
        RequestBody filename4 = RequestBody.create(MediaType.parse("text/plain"), compressedImageFile4.getName());


        Call<UploadObject> fileUpload = uploadService.postreimburstmentwithimage5(aempid, comeid, etDescription.getText().toString(), amount, spyear, spmonth, securitycode, fileToUpload, fileToUpload1, fileToUpload2, fileToUpload3, fileToUpload4, componentId,loactionId,"0");
        fileUpload.enqueue(new Callback<UploadObject>() {
            @Override
            public void onResponse(Call<UploadObject> call, retrofit2.Response<UploadObject> response) {
                progressDialog.dismiss();
                UploadObject extraWorkingDayModel = response.body();
                if (extraWorkingDayModel.isResponseStatus()) {
                    //  Toast.makeText(getApplicationContext(), extraWorkingDayModel.getResponseText(), Toast.LENGTH_SHORT).show();
                    successAlert();

                } else {
                    Toast.makeText(getApplicationContext(), extraWorkingDayModel.getResponseText(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UploadObject> call, Throwable t) {
                progressDialog.dismiss();

                Log.e("error", "Error " + t.getMessage());
                Toast.makeText(getApplicationContext(), "Something went wrong!Please try again", Toast.LENGTH_LONG).show();

            }

        });

    }

    private void successAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RecktitRemActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);
        tvInvalidDate.setText("Claim submitted successfully");
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                Intent intent = new Intent(RecktitRemActivity.this, ClaimReportActivity.class);
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


    public void uploadMultipart() {
        //getting name for the pdf

        //getting the actual path of the pdf
        String path = FilePath.getPath(this, filePath);

        if (path == null) {

            Toast.makeText(this, "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
        } else {
            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request
                /*new MultipartUploadRequest(this, UPLOAD_URL)
                        .addFileToUpload(path, "PDF") //Adding file
                        .addParameter("AEMEmployeeID", aempid)
                        .addParameter("AEMComponentID", componentId)
                        .addParameter("Description", description)
                        .addParameter("ReimbursementAmount", amount)
                        .addParameter("Year", year)
                        .addParameter("Month", month)
                        .addParameter("SecurityCode", securitycode)
                        .addParameter("ConveyanceTypeId", componentId)
                        .addParameter("LocationTypeID", loactionId)//Adding text parameter to the request
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload();
                successAlert();//Starting the upload*/

                UploadRequest(path);

            } catch (Exception exc) {
                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void UploadRequest(String path) {
        //SAVE_REIMBURSEMENT_CLAIM_BY_COMPONENT
        progressDialog.show();
        AndroidNetworking.upload(AppData.SAVE_REIMBURSEMENT_CLAIM_BY_COMPONENT)
                .addMultipartParameter("AEMEmployeeID", aempid)
                .addMultipartParameter("AEMComponentID", comeid)
                .addMultipartParameter("Description", description)
                .addMultipartParameter("ReimbursementAmount",amount)
                .addMultipartParameter("Year",year)
                .addMultipartParameter("Month",month)
                .addMultipartParameter("SecurityCode",securitycode)
                .addMultipartParameter("ConveyanceTypeId",componentId)
                .addMultipartParameter("LocationTypeID",loactionId)
                .addMultipartParameter("ReimbursementDate","0")
                .addMultipartFile("file", new File(path))
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "REIMBURSEMENT_CLAIM: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                successAlert();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RecktitRemActivity.this, "Something want to wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "REIMBURSEMENT_CLAIM_error: "+anError.getErrorBody());
                    }
                });
    }

}
