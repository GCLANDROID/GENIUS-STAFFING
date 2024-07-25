package io.cordova.myapp00d753.activity.metso;

import static android.os.Build.VERSION.SDK_INT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.SearchView;

import android.widget.Spinner;
import android.widget.TextView;
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
import com.developer.filepicker.controller.DialogSelectionListener;
import com.developer.filepicker.model.DialogConfigs;
import com.developer.filepicker.model.DialogProperties;
import com.developer.filepicker.view.FilePickerDialog;
import com.developers.imagezipper.ImageZipper;
import com.google.android.cameraview.LongImageCameraActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import id.zelory.compressor.Compressor;
import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.Retrofit.RetrofitClient;
import io.cordova.myapp00d753.activity.EmployeeDashBoardActivity;
import io.cordova.myapp00d753.activity.metso.adapter.ApproverAutoCompleteAdapter;
import io.cordova.myapp00d753.activity.metso.adapter.ComponentSpinnerAdapter;
import io.cordova.myapp00d753.activity.metso.adapter.ComponentrFilterAdapter;
import io.cordova.myapp00d753.activity.metso.adapter.CostCenterFilterAdapter;
import io.cordova.myapp00d753.activity.metso.adapter.MonthAdapter;
import io.cordova.myapp00d753.activity.metso.adapter.SiteFilterAdapter;
import io.cordova.myapp00d753.activity.metso.adapter.SupervisorFilterAdapter;
import io.cordova.myapp00d753.activity.metso.adapter.WbsCodeFilterAdapter;
import io.cordova.myapp00d753.activity.metso.model.ApproverModel;
import io.cordova.myapp00d753.module.AttendanceService;
import io.cordova.myapp00d753.module.SpineerItemModel;
import io.cordova.myapp00d753.module.UploadObject;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.FindDocumentInformation;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.RealPathUtil;
import io.cordova.myapp00d753.utility.Util;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Part;

public class MetsoNewReimbursementClaimActivity extends AppCompatActivity {
    private static final String TAG = "Metso_New_Reimbursement";
    private static final int DEFAULT_BUFFER_SIZE = 2048;
    ImageView imgBack, imgHome;
    Spinner spComponent;
    ArrayList<SpineerItemModel> moduleComponentList = new ArrayList<>();
    ArrayList<SpineerItemModel> costCenterComponentList = new ArrayList<>();
    ArrayList<SpineerItemModel> wbsCodeList = new ArrayList<>();
    ArrayList<SpineerItemModel> siteMasterList = new ArrayList<>();
    ArrayList<SpineerItemModel> supervisorList = new ArrayList<>();
    ArrayList<String> componentList = new ArrayList<>();
    //ArrayList<String> costCenterList = new ArrayList<>();
    Pref pref;
    TextView tvDate;
    String nosaledate;
    ImageView imgDate;
    String monthname;
    private String encodedImage;
    private Uri imageUri;
    String pdfFilePath, pdfFileName;
    private static final int CAMERA_REQUEST = 1;
    private static final int CAMERA_REQUEST1 = 2;
    private static final int CAMERA_REQUEST2 = 3;
    private static final int CAMERA_REQUEST3 = 4;
    private static final int CAMERA_REQUEST4 = 5;
    private static final int REQUEST_SELECT_PDF = 600;

    ComponentSpinnerAdapter componentSpinnerAdapter;


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
    File file1, file2, file3, file4, compressedImageFile, compressedImageFile1, compressedImageFile2, compressedImageFile3, compressedImageFile4;
    int flag = 0, onlyImageSelectionFlag = 0;
    AlertDialog alerDialog1, alertDialog2, alertDialog1, alert1;
    TextView tvMonth, tvYear;
    String comeid = "";
    LinearLayout llAttach, llBrowse;
    ImageView imgAttach, imgPDF;
    private static final int PICK_PDF_REQUEST = 10;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;
    private Uri filePath;
    Button btnSubmit;
    public static final String UPLOAD_URL = AppData.url + "post_reimbursementClaimByComponentMETSO_V1";
    int pdfflag;
    LinearLayout llCameraD, llCamera;
    AlertDialog alerDialog3;
    ImageView imgMultiPle;
    ImageView imgMultiPleImg;
    File pdfFile;
    LinearLayout lnGalleryOne, lnGalleryTwo;
    ImageView imgGalleryOne, imgGalleryTwo, imgUnselectPdf, imgUnselectGalleryOne, imgUnselectGalleryTwo;

    private static final int REQUEST_GALLERY_CODE_ONE = 200;
    private static final int REQUEST_GALLERY_CODE_TWO = 500;
    private Uri uri;

    //String galleryflagone = "", galleryflagtwo="";
    int galleryFlag = 0;
    Spinner spCostCenter, spSite, spSupervisor;
    AutoCompleteTextView autoComSupervisor;
    ProgressDialog progressBar;
    String SupervisorID = "", Siteid = "", WbsId = "", CostCentreId = "", ClientID = "";
    TextView txtWbsCode, txtBrowseFile, txtCamaraPicture, txtCostCenter, txtSupervisor, txtSite, txtComponent;
    Dialog searchWbsCodeDialog;
    ArrayList<ApproverModel> approverList;
    ApproverAutoCompleteAdapter approverAutoCompleteAdapter;
    ImageView imgUnselect1, imgUnselect2, imgUnselect3, imgUnselect4, imgUnselect5;
    ArrayList<String> monthList = new ArrayList<>();
    LinearLayout llDate, lnStartDate, lnEndDate;
    String startDate = "", endDate = "";
    TextView tvStartDate, tvEndDate;
    String component;
    static int dayscount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metso_new_reimbursement_claim);
        initialize();
        //setHideItem();

        JSONObject obj1=new JSONObject();
        try {
            obj1.put("DDL_Type", "16");
            obj1.put("ID1",pref.getEmpConId());
            obj1.put("ID2",pref.getEmpClintId());
            obj1.put("ID3","0");
            obj1.put("SecurityCode",pref.getSecurityCode());
            setHideItem(obj1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        onClick();
    }

    private void initialize() {
        progressBar = new ProgressDialog(this);
        pref = new Pref(getApplicationContext());
        ClientID = pref.getEmpClintId();
        llDate = (LinearLayout) findViewById(R.id.llDate);
        lnStartDate = (LinearLayout) findViewById(R.id.lnStartDate);
        lnEndDate = (LinearLayout) findViewById(R.id.lnEndDate);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        imgDate = (ImageView) findViewById(R.id.imgDate);
        imgUnselectPdf = (ImageView) findViewById(R.id.imgUnselectPdf);
        imgUnselectGalleryOne = (ImageView) findViewById(R.id.imgUnselectGalleryOne);
        imgUnselectGalleryTwo = (ImageView) findViewById(R.id.imgUnselectGalleryTwo);
        imgUnselect1 = (ImageView) findViewById(R.id.imgUnselect1);
        imgUnselect2 = (ImageView) findViewById(R.id.imgUnselect2);
        imgUnselect3 = (ImageView) findViewById(R.id.imgUnselect3);
        imgUnselect4 = (ImageView) findViewById(R.id.imgUnselect4);
        imgUnselect5 = (ImageView) findViewById(R.id.imgUnselect5);
        spComponent = (Spinner) findViewById(R.id.spComponent);
        spSite = (Spinner) findViewById(R.id.spSite);
        spSupervisor = (Spinner) findViewById(R.id.spSupervisor);
        autoComSupervisor = findViewById(R.id.autoComSupervisor);
        tvDate = (TextView) findViewById(R.id.tvDate);
        txtWbsCode = (TextView) findViewById(R.id.txtWbsCode);
        txtCostCenter = (TextView) findViewById(R.id.txtCostCenter);
        txtComponent = (TextView) findViewById(R.id.txtComponent);
        txtSupervisor = (TextView) findViewById(R.id.txtSupervisor);
        txtSite = (TextView) findViewById(R.id.txtSite);
        txtBrowseFile = (TextView) findViewById(R.id.txtBrowseFile);
        txtCamaraPicture = (TextView) findViewById(R.id.txtCamaraPicture);
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
            monthList.add("November");
            monthList.add("December");
            monthList.add("January");
        } else if (m == 2) {
            month = "February";
            monthList.add("December");
            monthList.add("January");
            monthList.add("February");
        } else if (m == 3) {
            month = "March";
            monthList.add("January");
            monthList.add("February");
            monthList.add("March");
        } else if (m == 4) {
            month = "April";
            monthList.add("February");
            monthList.add("March");
            monthList.add("April");
        } else if (m == 5) {
            month = "May";
            monthList.add("March");
            monthList.add("April");
            monthList.add("May");
        } else if (m == 6) {
            month = "June";
            monthList.add("April");
            monthList.add("May");
            monthList.add("June");
        } else if (m == 7) {
            month = "July";
            monthList.add("May");
            monthList.add("June");
            monthList.add("July");
        } else if (m == 8) {
            month = "August";
            monthList.add("June");
            monthList.add("July");
            monthList.add("August");
        } else if (m == 9) {
            month = "September";
            monthList.add("July");
            monthList.add("August");
            monthList.add("September");
        } else if (m == 10) {
            month = "October";
            monthList.add("August");
            monthList.add("September");
            monthList.add("October");
        } else if (m == 11) {
            month = "November";
            monthList.add("September");
            monthList.add("October");
            monthList.add("November");
        } else if (m == 12) {
            month = "December";
            monthList.add("October");
            monthList.add("November");
            monthList.add("December");
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
        tvMonth.setText(month);
        llBrowse = (LinearLayout) findViewById(R.id.llBrowse);
        llAttach = (LinearLayout) findViewById(R.id.llAttach);
        imgAttach = (ImageView) findViewById(R.id.imgAttach);
        imgPDF = (ImageView) findViewById(R.id.imgPDF);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        llCamera = (LinearLayout) findViewById(R.id.llCamera);
        llCameraD = (LinearLayout) findViewById(R.id.llCameraD);
        imgMultiPleImg = (ImageView) findViewById(R.id.imgMultiPleImg);
        imgMultiPle = (ImageView) findViewById(R.id.imgMultiPle);

        lnGalleryOne = (LinearLayout) findViewById(R.id.lnGalleryOne);
        lnGalleryTwo = (LinearLayout) findViewById(R.id.lnGalleryTwo);

        imgGalleryOne = (ImageView) findViewById(R.id.imgGalleryOne);
        imgGalleryTwo = (ImageView) findViewById(R.id.imgGalleryTwo);
        spCostCenter = findViewById(R.id.spCostCenter);

        tvEndDate = (TextView) findViewById(R.id.tvEndDate);
        tvStartDate = (TextView) findViewById(R.id.tvStartDate);
    }

    private void onClick() {
        lnStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStartDatePicker();
            }
        });

        lnEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEndDatePicker();
            }
        });
        lnGalleryOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
                openGalleryIntent.setType("image/*");
                startActivityForResult(openGalleryIntent, REQUEST_GALLERY_CODE_ONE);
            }
        });

        lnGalleryTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
                openGalleryIntent.setType("image/*");
                startActivityForResult(openGalleryIntent, REQUEST_GALLERY_CODE_TWO);
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MetsoNewReimbursementClaimActivity.this, EmployeeDashBoardActivity.class);
                startActivity(intent);
                finish();
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
                if (position > 0) {
                    componentId = moduleComponentList.get(position).getItemId();
                    Log.d("componentId", componentId);
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

        imgDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                Log.e(TAG, "onClick: imgCamera5");
                cameraIntent4();
            }
        });

        // llBrowse change to txtBrowseFile
        txtBrowseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llAttach.getVisibility() == View.GONE) {
                    llAttach.setVisibility(View.VISIBLE);
                    llCameraD.setVisibility(View.GONE);
                    llCamera.setVisibility(View.GONE);
                } else {
                    llAttach.setVisibility(View.GONE);
                    llCameraD.setVisibility(View.GONE);
                    llCamera.setVisibility(View.VISIBLE);
                }
            }
        });

        // llCamera change to txtCamaraPicture
        txtCamaraPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llCameraD.getVisibility() == View.GONE) {
                    llCameraD.setVisibility(View.VISIBLE);
                    llAttach.setVisibility(View.GONE);
                    llBrowse.setVisibility(View.GONE);
                } else {
                    llCameraD.setVisibility(View.GONE);
                    llAttach.setVisibility(View.GONE);
                    llBrowse.setVisibility(View.VISIBLE);
                }
            }
        });

        imgMultiPle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LongImageCameraActivity.launch(MetsoNewReimbursementClaimActivity.this);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: called");
                if (pdfflag != 0 || galleryFlag != 0 || (pdfflag != 0 && galleryFlag != 0)) {
                    if (componentId != "") {
                        if (!CostCentreId.isEmpty()) {
                            if (!WbsId.isEmpty()) {
                                if (!SupervisorID.isEmpty()) {
                                    if (!Siteid.isEmpty()) {
                                        if (etDescription.getText().toString().length() > 0) {
                                            if (etAmount.getText().toString().length() > 0) {
                                                attachFileAPI();
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Please enter Claim Amount", Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Please enter Description", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(MetsoNewReimbursementClaimActivity.this, "Please select Site", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Please select Supervisor", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Please select WBS Code or Coset Center", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Please select WBS Code or Coset Center", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Select Reimbursement Type", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please Upload Image", Toast.LENGTH_LONG).show();
                }
            }
        });

        imgAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent uploadIntent = new Intent(Intent.ACTION_GET_CONTENT);
                uploadIntent.setType("application/pdf");
                startActivityForResult(uploadIntent, REQUEST_SELECT_PDF);
                //showPDFPickerdialog();
            }
        });
        llSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SupervisorID="",Siteid="",WbsId="",CostCentreId="",ClientID=""
                if (flag != 0) {
                    if (componentId != "") {
                        if (!CostCentreId.isEmpty()) {
                            if (!WbsId.isEmpty()) {
                                if (!SupervisorID.isEmpty()) {
                                    if (!Siteid.isEmpty()) {
                                        if (etDescription.getText().toString().length() > 0) {
                                            if (etAmount.getText().toString().length() > 0) {
                                                if (flag == 1) {
                                                    postoneimage();
                                                    //TODO: new api
                                                    //postOneImage();
                                                } else if (flag == 2) {
                                                    posttwoimage();
                                                    //TODO: new api
                                                    //postTwoImage();
                                                } else if (flag == 3) {
                                                    postthreeimage();
                                                    //TODO: new api
                                                    //postThreeImage();
                                                } else if (flag == 4) {
                                                    postfourimage();
                                                    //TODO: new api
                                                    //postFourImage();
                                                } else if (flag == 5) {
                                                    postfiveimage();
                                                    //TODO: new api
                                                    //postFiveImage();
                                                }
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Please enter Claim Amount", Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Please enter Description", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(MetsoNewReimbursementClaimActivity.this, "Please select Site", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Please select Supervisor", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Please select WBS Code or Coset Center", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Please select WBS Code or Coset Center", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Select Reimbursement Type", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please Upload Image", Toast.LENGTH_LONG).show();
                }
            }
        });

        txtWbsCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWbsCodePopUp();
            }
        });

        txtCostCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCostCenterPopUp();
            }
        });

        txtComponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openComponentPopUp();
            }
        });

        txtSupervisor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSupervisorPopUp();
            }
        });

        txtSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSitePopUp();
            }
        });

        imgUnselectPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pdfFile = null;
                pdfflag = 0;
                imgUnselectPdf.setVisibility(View.GONE);
                imgPDF.setVisibility(View.GONE);
            }
        });

        imgUnselectGalleryOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compressedImageFile = null;
                flag--;
                galleryFlag--;
                imgUnselectGalleryOne.setVisibility(View.GONE);
                imgGalleryOne.setImageResource(0);

            }
        });

        imgUnselectGalleryTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compressedImageFile1 = null;
                flag--;
                galleryFlag--;
                imgUnselectGalleryTwo.setVisibility(View.GONE);
                imgGalleryTwo.setImageResource(0);
            }
        });

        imgUnselect1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag--;
                imgUnselect1.setVisibility(View.GONE);
                imgDoc1.setImageResource(0);
            }
        });

        imgUnselect2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag--;
                imgUnselect2.setVisibility(View.GONE);
                imgDoc2.setImageResource(0);
            }
        });

        imgUnselect3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag--;
                imgUnselect3.setVisibility(View.GONE);
                imgDoc3.setImageResource(0);
            }
        });

        imgUnselect4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag--;
                imgUnselect4.setVisibility(View.GONE);
                imgDoc4.setImageResource(0);
            }
        });

        imgUnselect5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag--;
                imgUnselect5.setVisibility(View.GONE);
                imgDoc5.setImageResource(0);
            }
        });
    }

    private void openSitePopUp() {
        searchWbsCodeDialog = new Dialog(MetsoNewReimbursementClaimActivity.this, R.style.CustomDialogNew2);
        searchWbsCodeDialog.setContentView(R.layout.wbs_code_search_layout);
        searchWbsCodeDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        searchWbsCodeDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        searchWbsCodeDialog.setCancelable(true);

        TextView txtPopupHeadline = searchWbsCodeDialog.findViewById(R.id.txtPopupHeadline);
        SearchView wbsCodeSearchView = (SearchView) searchWbsCodeDialog.findViewById(R.id.wbsCodeSearchView);
        ImageView imgCancel = searchWbsCodeDialog.findViewById(R.id.imgCancel);
        RecyclerView rvWbsCode = searchWbsCodeDialog.findViewById(R.id.rvWbsCode);

        wbsCodeSearchView.setQueryHint("Search Site");
        txtPopupHeadline.setText("Select Site");
        rvWbsCode.setLayoutManager(new LinearLayoutManager(MetsoNewReimbursementClaimActivity.this));
        ArrayList<SpineerItemModel> siteListCopy = new ArrayList<>();
        siteListCopy = (ArrayList<SpineerItemModel>) siteMasterList.clone();
        SiteFilterAdapter siteFilterAdapter = new SiteFilterAdapter(MetsoNewReimbursementClaimActivity.this, siteListCopy);
        rvWbsCode.setAdapter(siteFilterAdapter);

        wbsCodeSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                siteFilterAdapter.getFilter().filter(s);
                return false;
            }
        });

        siteFilterAdapter.setSiteSelectListener(new SiteSelectListener() {
            @Override
            public void onClick(String site_id, String site) {
                Siteid = site_id;
                txtSite.setText(site);
                searchWbsCodeDialog.dismiss();
            }
        });


        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchWbsCodeDialog.dismiss();
            }
        });
        searchWbsCodeDialog.show();
    }

    private void openSupervisorPopUp() {
        searchWbsCodeDialog = new Dialog(MetsoNewReimbursementClaimActivity.this, R.style.CustomDialogNew2);
        searchWbsCodeDialog.setContentView(R.layout.wbs_code_search_layout);
        searchWbsCodeDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        searchWbsCodeDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        searchWbsCodeDialog.setCancelable(true);

        TextView txtPopupHeadline = searchWbsCodeDialog.findViewById(R.id.txtPopupHeadline);
        SearchView wbsCodeSearchView = (SearchView) searchWbsCodeDialog.findViewById(R.id.wbsCodeSearchView);
        ImageView imgCancel = searchWbsCodeDialog.findViewById(R.id.imgCancel);
        RecyclerView rvWbsCode = searchWbsCodeDialog.findViewById(R.id.rvWbsCode);

        wbsCodeSearchView.setQueryHint("Search Supervisor");
        txtPopupHeadline.setText("Select Supervisor");
        rvWbsCode.setLayoutManager(new LinearLayoutManager(MetsoNewReimbursementClaimActivity.this));
        ArrayList<SpineerItemModel> supervisorListCopy = new ArrayList<>();
        supervisorListCopy = (ArrayList<SpineerItemModel>) supervisorList.clone();
        SupervisorFilterAdapter supervisorFilterAdapter = new SupervisorFilterAdapter(MetsoNewReimbursementClaimActivity.this, supervisorListCopy);
        rvWbsCode.setAdapter(supervisorFilterAdapter);

        wbsCodeSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                supervisorFilterAdapter.getFilter().filter(s);
                return false;
            }
        });

        supervisorFilterAdapter.setSupervisorSelectListener(new SupervisorSelectListener() {
            @Override
            public void onClick(String supervisor_id, String supervisor) {
                SupervisorID = supervisor_id;
                txtSupervisor.setText(supervisor);
                searchWbsCodeDialog.dismiss();
            }
        });


        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchWbsCodeDialog.dismiss();
            }
        });
        searchWbsCodeDialog.show();
    }

    private void openComponentPopUp() {
        searchWbsCodeDialog = new Dialog(MetsoNewReimbursementClaimActivity.this, R.style.CustomDialogNew2);
        searchWbsCodeDialog.setContentView(R.layout.wbs_code_search_layout);
        searchWbsCodeDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        searchWbsCodeDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        searchWbsCodeDialog.setCancelable(true);

        TextView txtPopupHeadline = searchWbsCodeDialog.findViewById(R.id.txtPopupHeadline);
        SearchView wbsCodeSearchView = (SearchView) searchWbsCodeDialog.findViewById(R.id.wbsCodeSearchView);
        ImageView imgCancel = searchWbsCodeDialog.findViewById(R.id.imgCancel);
        RecyclerView rvWbsCode = searchWbsCodeDialog.findViewById(R.id.rvWbsCode);

        wbsCodeSearchView.setQueryHint("Search Component");
        txtPopupHeadline.setText("Select Component");
        rvWbsCode.setLayoutManager(new LinearLayoutManager(MetsoNewReimbursementClaimActivity.this));
        ArrayList<SpineerItemModel> costCenterComponentListCopy = new ArrayList<>();
        costCenterComponentListCopy = (ArrayList<SpineerItemModel>) moduleComponentList.clone();
        ComponentrFilterAdapter componentrFilterAdapter = new ComponentrFilterAdapter(MetsoNewReimbursementClaimActivity.this, costCenterComponentListCopy);
        rvWbsCode.setAdapter(componentrFilterAdapter);

        wbsCodeSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                componentrFilterAdapter.getFilter().filter(s);
                return false;
            }
        });

        componentrFilterAdapter.setCostCenterSelectListener(new CostCenterSelectListener() {
            @Override
            public void onClick(String id, String text) {
                componentId = id;

                txtComponent.setText(text);
                searchWbsCodeDialog.dismiss();
                component = text;
                if (text.equalsIgnoreCase("DAILY EXP.") || text.equalsIgnoreCase("DAILY TRAVEL REIMB.")) {
                    etAmount.setEnabled(false);
                    llDate.setVisibility(View.VISIBLE);
                }

            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchWbsCodeDialog.dismiss();
            }
        });
        searchWbsCodeDialog.show();
    }

    private void openCostCenterPopUp() {
        searchWbsCodeDialog = new Dialog(MetsoNewReimbursementClaimActivity.this, R.style.CustomDialogNew2);
        searchWbsCodeDialog.setContentView(R.layout.wbs_code_search_layout);
        searchWbsCodeDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        searchWbsCodeDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        searchWbsCodeDialog.setCancelable(true);

        TextView txtPopupHeadline = searchWbsCodeDialog.findViewById(R.id.txtPopupHeadline);
        SearchView wbsCodeSearchView = (SearchView) searchWbsCodeDialog.findViewById(R.id.wbsCodeSearchView);
        ImageView imgCancel = searchWbsCodeDialog.findViewById(R.id.imgCancel);
        RecyclerView rvWbsCode = searchWbsCodeDialog.findViewById(R.id.rvWbsCode);

        wbsCodeSearchView.setQueryHint("Search Cost Center");
        txtPopupHeadline.setText("Select Cost Center");
        rvWbsCode.setLayoutManager(new LinearLayoutManager(MetsoNewReimbursementClaimActivity.this));
        ArrayList<SpineerItemModel> costCenterComponentListCopy = new ArrayList<>();
        costCenterComponentListCopy = (ArrayList<SpineerItemModel>) costCenterComponentList.clone();
        CostCenterFilterAdapter costCenterFilterAdapter = new CostCenterFilterAdapter(MetsoNewReimbursementClaimActivity.this, costCenterComponentListCopy);
        rvWbsCode.setAdapter(costCenterFilterAdapter);

        wbsCodeSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                costCenterFilterAdapter.getFilter().filter(s);
                return false;
            }
        });

        costCenterFilterAdapter.setCostCenterSelectListener(new CostCenterSelectListener() {
            @Override
            public void onClick(String cost_center_id, String cost_center) {
                CostCentreId = cost_center_id;
                WbsId = "0";
                txtCostCenter.setText(cost_center);
                searchWbsCodeDialog.dismiss();
                txtWbsCode.setEnabled(false);
                txtWbsCode.setTextColor(Color.parseColor("#F2CAC9C9"));
            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchWbsCodeDialog.dismiss();
            }
        });
        searchWbsCodeDialog.show();
    }

    private void openWbsCodePopUp() {
        searchWbsCodeDialog = new Dialog(MetsoNewReimbursementClaimActivity.this, R.style.CustomDialogNew2);
        searchWbsCodeDialog.setContentView(R.layout.wbs_code_search_layout);
        searchWbsCodeDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        searchWbsCodeDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        searchWbsCodeDialog.setCancelable(true);

        SearchView wbsCodeSearchView = (SearchView) searchWbsCodeDialog.findViewById(R.id.wbsCodeSearchView);
        ImageView imgCancel = searchWbsCodeDialog.findViewById(R.id.imgCancel);
        RecyclerView rvWbsCode = searchWbsCodeDialog.findViewById(R.id.rvWbsCode);
        rvWbsCode.setLayoutManager(new LinearLayoutManager(MetsoNewReimbursementClaimActivity.this));

        ArrayList<SpineerItemModel> wbsCodeListCopy = new ArrayList<>();
        wbsCodeListCopy = (ArrayList<SpineerItemModel>) wbsCodeList.clone();

        WbsCodeFilterAdapter wbsCodeFilterAdapter = new WbsCodeFilterAdapter(MetsoNewReimbursementClaimActivity.this, wbsCodeListCopy);
        rvWbsCode.setAdapter(wbsCodeFilterAdapter);
        wbsCodeSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                wbsCodeFilterAdapter.getFilter().filter(s);
                return false;
            }
        });

        wbsCodeFilterAdapter.setWbsCodeSelectListener(new WbsCodeSelectListener() {
            @Override
            public void onClick(String wbs_id, String wbs_code) {
                WbsId = wbs_id;
                CostCentreId = "0";
                txtWbsCode.setText(wbs_code);
                searchWbsCodeDialog.dismiss();
                txtCostCenter.setEnabled(false);
                txtCostCenter.setTextColor(Color.parseColor("#F2CAC9C9"));
            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchWbsCodeDialog.dismiss();
            }
        });
        searchWbsCodeDialog.show();
    }

    private void setHideItem(JSONObject jsonObject) {
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
                            Log.e(TAG, "GET_COMMON_DROP_DOWN_FILL: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(Response_Data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    comeid = obj.optString("ID");
                                    Log.d("comeeid", comeid);
                                    String value = obj.optString("VALUE");
                                    Log.d("comvalue", value);
                                }

                                JSONObject obj1=new JSONObject();
                                try {
                                    obj1.put("DDL_Type", "160");
                                    obj1.put("ID1",pref.getEmpConId());
                                    obj1.put("ID2",pref.getEmpClintId());
                                    obj1.put("ID3",0);
                                    obj1.put("SecurityCode",pref.getSecurityCode());
                                    setComponenetItem(obj1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                hideAlert();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressBar.dismiss();
                        Log.e(TAG, "GET_COMMON_DROP_DOWN_FILL_error: "+anError.getErrorBody());
                    }
                });
    }


    private void setHideItem() {
        String surl = AppData.url + "gcl_CommonDDL?ddltype=16&id1=" + pref.getEmpConId() + "&id2=" + pref.getEmpClintId() + "&id3=0&SecurityCode=" + pref.getSecurityCode();
        Log.d(TAG,"comp_url_1: "+surl);
        //final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);

                        //progressBar.dismiss();
                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                //Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i == 0; i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    comeid = obj.optString("id");
                                    Log.d("comeeid", comeid);
                                    String value = obj.optString("value");
                                    Log.d("comvalue", value);
                                }
                                setComponenetItem();
                            } else {
                                hideAlert();
                            }
                            // boolean _status = job1.getBoolean("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MetsoNewReimbursementClaimActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
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

    private void setComponenetItem(JSONObject jsonObject) {
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
                            Log.e(TAG, "COMPONENT_ITEM: "+response.toString(4) );
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(Response_Data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String comid = obj.optString("ID");
                                    //Log.e(TAG,"comid: "+comid);
                                    String value = obj.optString("VALUE");
                                    componentList.add(value);
                                    SpineerItemModel mainDocModule = new SpineerItemModel(value, comid);
                                    moduleComponentList.add(mainDocModule);
                                }
                                componentSpinnerAdapter = new ComponentSpinnerAdapter(MetsoNewReimbursementClaimActivity.this, moduleComponentList);
                                spComponent.setAdapter(componentSpinnerAdapter);
                                spComponent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        SpineerItemModel clickedItem = (SpineerItemModel) adapterView.getItemAtPosition(i);
                                        if (!clickedItem.getItemName().equals("Please Select Cost Center")) {
                                            comeid = clickedItem.getItemId();
                                            Log.e(TAG, "CostCentreId: " + CostCentreId);
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });

                                JSONObject obj1=new JSONObject();
                                try {
                                    obj1.put("DDL_Type", "CLCOSTC");
                                    obj1.put("ID1","0");
                                    obj1.put("ID2",pref.getEmpClintId());
                                    obj1.put("ID3",0);
                                    obj1.put("SecurityCode",pref.getSecurityCode());
                                    getCostCenterList(obj1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MetsoNewReimbursementClaimActivity.this, "Something went to wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "COMPONENT_ITEM_error: "+anError.getErrorBody());
                    }
                });
    }

    private void setComponenetItem() {
        String surl = AppData.url + "gcl_CommonDDL?ddltype=160&id1=" + pref.getEmpConId() + "&id2=" + pref.getEmpClintId() + "&id3=0&SecurityCode=" + pref.getSecurityCode();
        Log.d(TAG,"comp_url_2: "+surl);
        //final ProgressDialog progressBar = new ProgressDialog(this);
       /* progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();*/
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        //progressBar.dismiss();
                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                //Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                componentList.add("Please Select Reimbursement Type");
                                moduleComponentList.add(new SpineerItemModel("Please Select Reimbursement Type", "0"));
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String comid = obj.optString("id");
                                    Log.d("comid", comid);
                                    String value = obj.optString("value");
                                    componentList.add(value);
                                    SpineerItemModel mainDocModule = new SpineerItemModel(value, comid);
                                    moduleComponentList.add(mainDocModule);

                                }
                                componentSpinnerAdapter = new ComponentSpinnerAdapter(MetsoNewReimbursementClaimActivity.this, moduleComponentList);
                                spComponent.setAdapter(componentSpinnerAdapter);
                                spComponent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        SpineerItemModel clickedItem = (SpineerItemModel) adapterView.getItemAtPosition(i);
                                        if (!clickedItem.getItemName().equals("Please Select Cost Center")) {
                                            comeid = clickedItem.getItemId();
                                            Log.e(TAG, "CostCentreId: " + CostCentreId);
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });


                                getCostCenterList();
                            } else {

                            }
                            // boolean _status = job1.getBoolean("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MetsoNewReimbursementClaimActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                //Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
    }

    private void getCostCenterList(JSONObject jsonObject) {
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
                            Log.e(TAG, "COST_CENTER: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(Response_Data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String comid = obj.optString("ID");
                                    Log.d("comid", comid);
                                    String value = obj.optString("VALUE");
                                    SpineerItemModel mainDocModule = new SpineerItemModel(value, comid);
                                    costCenterComponentList.add(mainDocModule);
                                }

                                JSONObject obj1=new JSONObject();
                                try {
                                    obj1.put("DDL_Type", "CLWBSM");
                                    obj1.put("ID1","0");
                                    obj1.put("ID2",pref.getEmpClintId());
                                    obj1.put("ID3",0);
                                    obj1.put("SecurityCode",pref.getSecurityCode());
                                    getWbsCode(obj1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MetsoNewReimbursementClaimActivity.this, "Something went to wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressBar.dismiss();
                        Log.e(TAG, "COST_CENTER_error: "+anError.getErrorBody());
                    }
                });
    }

    private void getCostCenterList() {
        String surl = AppData.url + "gcl_CommonDDL?ddltype=CLCOSTC&id1=0&id2=" + pref.getEmpClintId() + "&id3=0&SecurityCode=" + pref.getSecurityCode();
        Log.d(TAG,"costCenterUrl: "+surl);
        //final ProgressDialog progressBar = new ProgressDialog(this);
       /* progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();*/
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response_cost_center", response);
                        //progressBar.dismiss();
                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                costCenterComponentList.add(new SpineerItemModel("Please Select Cost Center", "0"));
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String comid = obj.optString("id");
                                    Log.d("comid", comid);
                                    String value = obj.optString("value");
                                    SpineerItemModel mainDocModule = new SpineerItemModel(value, comid);
                                    costCenterComponentList.add(mainDocModule);
                                }
                                componentSpinnerAdapter = new ComponentSpinnerAdapter(MetsoNewReimbursementClaimActivity.this, costCenterComponentList);
                                spCostCenter.setAdapter(componentSpinnerAdapter);
                                spCostCenter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        SpineerItemModel clickedItem = (SpineerItemModel) adapterView.getItemAtPosition(i);
                                        if (!clickedItem.getItemName().equals("Please Select Cost Center")) {
                                            CostCentreId = clickedItem.getItemId();
                                            Log.e(TAG, "CostCentreId: " + CostCentreId);
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });
                                getWbsCode();
                            } else {

                            }
                            // boolean _status = job1.getBoolean("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MetsoNewReimbursementClaimActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                ;
                Log.e("ert", error.toString());
            }
        }) {
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
    }

    private void getWbsCode(JSONObject jsonObject) {
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
                            Log.e(TAG, "WBS_CODE: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(Response_Data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String comid = obj.optString("ID");
                                    Log.d("comid", comid);
                                    String value = obj.optString("VALUE");
                                    SpineerItemModel mainDocModule = new SpineerItemModel(value, comid);
                                    wbsCodeList.add(mainDocModule);
                                }

                                JSONObject obj1=new JSONObject();
                                try {
                                    obj1.put("DDL_Type", "CLSPM");
                                    obj1.put("ID1","0");
                                    obj1.put("ID2",pref.getEmpClintId());
                                    obj1.put("ID3",0);
                                    obj1.put("SecurityCode",pref.getSecurityCode());
                                    getSupervisorList(obj1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MetsoNewReimbursementClaimActivity.this, "Something went to wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "WBS_CODE_error: "+anError.getErrorBody());
                    }
                });
    }

    private void getWbsCode() {
        String surl = AppData.url + "gcl_CommonDDL?ddltype=CLWBSM&id1=0&id2=" + pref.getEmpClintId() + "&id3=0&SecurityCode=" + pref.getSecurityCode();
        Log.d(TAG,"WbsCodeUrl: "+surl);
        //final ProgressDialog progressBar = new ProgressDialog(this);
        /*progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();*/
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response_wbs_code", response);
                        //progressBar.dismiss();
                        try {
                            JSONObject job1 = new JSONObject(response);
                            //Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                wbsCodeList.add(new SpineerItemModel("Please Select WBS code", "0"));
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String comid = obj.optString("id");
                                    Log.d("comid", comid);
                                    String value = obj.optString("value");
                                    SpineerItemModel mainDocModule = new SpineerItemModel(value, comid);
                                    wbsCodeList.add(mainDocModule);
                                }
                               /*componentSpinnerAdapter = new ComponentSpinnerAdapter(MetsoNewReimbursementClaimActivity.this,costCenterComponentList);
                                spCostCenter.setAdapter(componentSpinnerAdapter);
                                spCostCenter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        SpineerItemModel clickedItem = (SpineerItemModel) adapterView.getItemAtPosition(i);
                                      *//* if (!clickedItem.getItemName().equals("Please Select Cost Center")){
                                            txtSelectShift.setText(clickedItem.getColumn1());
                                            Shiftid = String.valueOf(clickedItem.getWorkingShiftID());
                                            txtErrorShift.setVisibility(View.GONE);
                                        }*//*

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });*/
                                getSupervisorList();
                                //getApproverList();
                            } else {

                            }
                            // boolean _status = job1.getBoolean("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MetsoNewReimbursementClaimActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                Log.e("WbsCodeUrl_error", error.toString());
            }
        }) {
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
    }


    private void getApproverList() {
        AndroidNetworking.get(AppData.url + "Leave/Get_MetsoAttendanceData")
                .addQueryParameter("Mode", "3")
                .addQueryParameter("CompanyID", ClientID)
                .addQueryParameter("SecurityCode", "0000")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject object = new JSONObject(String.valueOf(response));
                            if (object.getBoolean("responseStatus") == true) {
                                JSONArray jsonArray = object.getJSONArray("responseData");
                                approverList = new ArrayList<>();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject objectResponse = jsonArray.getJSONObject(i);
                                    approverList.add(new ApproverModel(objectResponse.getInt("UserId"),
                                            objectResponse.getString("UserName")));
                                }
                                approverAutoCompleteAdapter = new ApproverAutoCompleteAdapter(MetsoNewReimbursementClaimActivity.this, approverList);
                                autoComSupervisor.setAdapter(approverAutoCompleteAdapter);
                                autoComSupervisor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        ApproverModel selectedItem = (ApproverModel) adapterView.getItemAtPosition(i);
                                        SupervisorID = String.valueOf(selectedItem.approverId);
                                    }
                                });
                                getSiteMasterList();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        //Handle the error response
                        Log.e(TAG, "SHIFT_RESPONSE_error: " + error);
                        Toast.makeText(MetsoNewReimbursementClaimActivity.this, "Getting Some Error", Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                    }
                });
    }

    private void getSupervisorList(JSONObject jsonObject) {
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
                            Log.e(TAG, "SUPERVISOR_LIST: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(Response_Data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String comid = obj.optString("ID");
                                    Log.d("comid", comid);
                                    String value = obj.optString("VALUE");
                                    SpineerItemModel mainDocModule = new SpineerItemModel(value, comid);
                                    supervisorList.add(mainDocModule);
                                }

                                componentSpinnerAdapter = new ComponentSpinnerAdapter(MetsoNewReimbursementClaimActivity.this, supervisorList);
                                spSupervisor.setAdapter(componentSpinnerAdapter);
                                spSupervisor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        SpineerItemModel clickedItem = (SpineerItemModel) adapterView.getItemAtPosition(i);
                                        if (!clickedItem.getItemName().equals("Please Select Supervisor")) {
                                            SupervisorID = clickedItem.getItemId();
                                            Log.e(TAG, "SupervisorID: " + SupervisorID);
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });

                                JSONObject obj1=new JSONObject();
                                try {
                                    obj1.put("DDL_Type", "CLSITEM");
                                    obj1.put("ID1","0");
                                    obj1.put("ID2",pref.getEmpClintId());
                                    obj1.put("ID3",0);
                                    obj1.put("SecurityCode",pref.getSecurityCode());
                                    getSiteMasterList(obj1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MetsoNewReimbursementClaimActivity.this, "Something went to wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "SUPERVISOR_LIST_error: "+anError.getErrorBody());
                    }
                });
    }

    private void getSupervisorList() {
        String surl = AppData.url + "gcl_CommonDDL?ddltype=CLSPM&id1=0&id2=" + pref.getEmpClintId() + "&id3=0&SecurityCode=" + pref.getSecurityCode();
        Log.d(TAG,"SupervisorUrl: "+surl);
        //final ProgressDialog progressBar = new ProgressDialog(this);
        /*progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();*/
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response_supervisor", response);
                        //progressBar.dismiss();
                        try {
                            JSONObject job1 = new JSONObject(response);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                supervisorList.add(new SpineerItemModel("Please Select Supervisor", "0"));
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String comid = obj.optString("id");
                                    Log.d("comid", comid);
                                    String value = obj.optString("value");
                                    SpineerItemModel mainDocModule = new SpineerItemModel(value, comid);
                                    supervisorList.add(mainDocModule);
                                }
                                componentSpinnerAdapter = new ComponentSpinnerAdapter(MetsoNewReimbursementClaimActivity.this, supervisorList);
                                spSupervisor.setAdapter(componentSpinnerAdapter);
                                spSupervisor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        SpineerItemModel clickedItem = (SpineerItemModel) adapterView.getItemAtPosition(i);
                                        if (!clickedItem.getItemName().equals("Please Select Supervisor")) {
                                            SupervisorID = clickedItem.getItemId();
                                            Log.e(TAG, "SupervisorID: " + SupervisorID);
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });
                                getSiteMasterList();
                            } else {

                            }
                            // boolean _status = job1.getBoolean("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MetsoNewReimbursementClaimActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                Log.e("Supervisor_error", error.toString());
            }
        }) {
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
    }

    private void getSiteMasterList(JSONObject jsonObject) {
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
                            Log.e(TAG, "SITE_MASTER_LIST: "+response.toString(4));
                            progressBar.dismiss();
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(Response_Data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String comid = obj.optString("ID");
                                    Log.d("comid", comid);
                                    String value = obj.optString("VALUE");
                                    SpineerItemModel mainDocModule = new SpineerItemModel(value, comid);
                                    siteMasterList.add(mainDocModule);
                                }
                                componentSpinnerAdapter = new ComponentSpinnerAdapter(MetsoNewReimbursementClaimActivity.this, siteMasterList);
                                spSite.setAdapter(componentSpinnerAdapter);
                                spSite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        SpineerItemModel clickedItem = (SpineerItemModel) adapterView.getItemAtPosition(i);
                                        if (!clickedItem.getItemName().equals("Please Select Site")) {
                                            Siteid = clickedItem.getItemId();
                                            Log.e(TAG, "Siteid: " + Siteid);
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressBar.dismiss();
                        Log.e(TAG, "SITE_MASTER_LIST_error: "+anError.getErrorBody());
                    }
                });
    }

    private void getSiteMasterList() {
        String surl = AppData.url + "gcl_CommonDDL?ddltype=CLSITEM&id1=0&id2=" + pref.getEmpClintId() + "&id3=0&SecurityCode=" + pref.getSecurityCode();
        Log.d(TAG,"SiteMasterUrl"+surl);
        //final ProgressDialog progressBar = new ProgressDialog(this);
        /*progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();*/
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response_site_master", response);
                        progressBar.dismiss();
                        try {
                            JSONObject job1 = new JSONObject(response);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                siteMasterList.add(new SpineerItemModel("Please Select Site", "0"));
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String comid = obj.optString("id");
                                    Log.d("comid", comid);
                                    String value = obj.optString("value");
                                    SpineerItemModel mainDocModule = new SpineerItemModel(value, comid);
                                    siteMasterList.add(mainDocModule);
                                }
                                componentSpinnerAdapter = new ComponentSpinnerAdapter(MetsoNewReimbursementClaimActivity.this, siteMasterList);
                                spSite.setAdapter(componentSpinnerAdapter);
                                spSite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        SpineerItemModel clickedItem = (SpineerItemModel) adapterView.getItemAtPosition(i);
                                        if (!clickedItem.getItemName().equals("Please Select Site")) {
                                            Siteid = clickedItem.getItemId();
                                            Log.e(TAG, "Siteid: " + Siteid);
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });
                            } else {

                            }
                            // boolean _status = job1.getBoolean("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MetsoNewReimbursementClaimActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                Log.e("site_master_error", error.toString());
            }
        }) {
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
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
                            if (flag == 0) {
                                compressedImageFile = new ImageZipper(MetsoNewReimbursementClaimActivity.this)
                                        .setQuality(100)
                                        .setMaxWidth(300)
                                        .setMaxHeight(300)
                                        .compressToFile(file);
                                flag++;
                            } else if (flag == 1) {
                                compressedImageFile1 = new ImageZipper(MetsoNewReimbursementClaimActivity.this)
                                        .setQuality(100)
                                        .setMaxWidth(300)
                                        .setMaxHeight(300)
                                        .compressToFile(file);
                                flag++;
                            } else if (flag == 2) {
                                compressedImageFile2 = new ImageZipper(MetsoNewReimbursementClaimActivity.this)
                                        .setQuality(100)
                                        .setMaxWidth(300)
                                        .setMaxHeight(300)
                                        .compressToFile(file);
                                flag++;
                            } else if (flag == 3) {
                                compressedImageFile3 = new ImageZipper(MetsoNewReimbursementClaimActivity.this)
                                        .setQuality(100)
                                        .setMaxWidth(300)
                                        .setMaxHeight(300)
                                        .compressToFile(file);
                                flag++;
                            } else if (flag == 4) {
                                compressedImageFile4 = new ImageZipper(MetsoNewReimbursementClaimActivity.this)
                                        .setQuality(100)
                                        .setMaxWidth(300)
                                        .setMaxHeight(300)
                                        .compressToFile(file);
                            }
                           /* compressedImageFile = new ImageZipper(MetsoNewReimbursementClaimActivity.this)
                                    .setQuality(100)
                                    .setMaxWidth(300)
                                    .setMaxHeight(300)
                                    .compressToFile(file);*/
                            //Log.d("imageSixw", String.valueOf(getReadableFileSize(compressedImageFile.length())));

                            BitmapFactory.Options o = new BitmapFactory.Options();
                            o.inSampleSize = 2;
                            Bitmap bm = cropToSquare(BitmapFactory.decodeFile(imageurl, o));
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.PNG, 10, baos); //bm is the bitmap object
                            byte[] b = baos.toByteArray();
                            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                            imgDoc1.setImageBitmap(bm);
                            imgUnselect1.setVisibility(View.VISIBLE);
                            Log.d("images", encodedImage);
                            //flag = 1;
                            //flag++;


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

                            if (flag == 0) {
                                compressedImageFile = new ImageZipper(MetsoNewReimbursementClaimActivity.this)
                                        .setQuality(100)
                                        .setMaxWidth(300)
                                        .setMaxHeight(300)
                                        .compressToFile(file1);
                                flag++;
                            } else if (flag == 1) {
                                compressedImageFile1 = new ImageZipper(MetsoNewReimbursementClaimActivity.this)
                                        .setQuality(100)
                                        .setMaxWidth(300)
                                        .setMaxHeight(300)
                                        .compressToFile(file1);
                                flag++;
                            } else if (flag == 2) {
                                compressedImageFile2 = new ImageZipper(MetsoNewReimbursementClaimActivity.this)
                                        .setQuality(100)
                                        .setMaxWidth(300)
                                        .setMaxHeight(300)
                                        .compressToFile(file1);
                                flag++;
                            } else if (flag == 3) {
                                compressedImageFile3 = new ImageZipper(MetsoNewReimbursementClaimActivity.this)
                                        .setQuality(100)
                                        .setMaxWidth(300)
                                        .setMaxHeight(300)
                                        .compressToFile(file1);
                                flag++;
                            } else if (flag == 4) {
                                compressedImageFile4 = new ImageZipper(MetsoNewReimbursementClaimActivity.this)
                                        .setQuality(100)
                                        .setMaxWidth(300)
                                        .setMaxHeight(300)
                                        .compressToFile(file1);
                            }


                            /*compressedImageFile1 =new ImageZipper(MetsoNewReimbursementClaimActivity.this)
                                    .setQuality(100)
                                    .setMaxWidth(300)
                                    .setMaxHeight(300)
                                    .compressToFile(file1);*/
                            //Log.d("imageSixw", String.valueOf(getReadableFileSize(compressedImageFile1.length())));

                            BitmapFactory.Options o = new BitmapFactory.Options();
                            o.inSampleSize = 2;
                            Bitmap bm = cropToSquare(BitmapFactory.decodeFile(imageurl, o));
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.PNG, 10, baos); //bm is the bitmap object
                            byte[] b = baos.toByteArray();
                            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                            imgDoc2.setImageBitmap(bm);
                            imgUnselect2.setVisibility(View.VISIBLE);
                            Log.d("images", encodedImage);
                            //flag = 2;
                            //flag++;


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

                            if (flag == 0) {
                                compressedImageFile = new ImageZipper(MetsoNewReimbursementClaimActivity.this)
                                        .setQuality(100)
                                        .setMaxWidth(300)
                                        .setMaxHeight(300)
                                        .compressToFile(file2);
                                flag++;
                            } else if (flag == 1) {
                                compressedImageFile1 = new ImageZipper(MetsoNewReimbursementClaimActivity.this)
                                        .setQuality(100)
                                        .setMaxWidth(300)
                                        .setMaxHeight(300)
                                        .compressToFile(file2);
                                flag++;
                            } else if (flag == 2) {
                                compressedImageFile2 = new ImageZipper(MetsoNewReimbursementClaimActivity.this)
                                        .setQuality(100)
                                        .setMaxWidth(300)
                                        .setMaxHeight(300)
                                        .compressToFile(file2);
                                flag++;
                            } else if (flag == 3) {
                                compressedImageFile3 = new ImageZipper(MetsoNewReimbursementClaimActivity.this)
                                        .setQuality(100)
                                        .setMaxWidth(300)
                                        .setMaxHeight(300)
                                        .compressToFile(file2);
                                flag++;
                            } else if (flag == 4) {
                                compressedImageFile4 = new ImageZipper(MetsoNewReimbursementClaimActivity.this)
                                        .setQuality(100)
                                        .setMaxWidth(300)
                                        .setMaxHeight(300)
                                        .compressToFile(file2);
                                //flag++;
                            }


                            /*compressedImageFile2 = new ImageZipper(MetsoNewReimbursementClaimActivity.this)
                                    .setQuality(100)
                                    .setMaxWidth(300)
                                    .setMaxHeight(300)
                                    .compressToFile(file2);*/
                            BitmapFactory.Options o = new BitmapFactory.Options();
                            o.inSampleSize = 2;
                            Bitmap bm = cropToSquare(BitmapFactory.decodeFile(imageurl, o));
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.PNG, 10, baos); //bm is the bitmap object
                            byte[] b = baos.toByteArray();
                            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                            imgDoc3.setImageBitmap(bm);
                            imgUnselect3.setVisibility(View.VISIBLE);
                            Log.d("images", encodedImage);
                            //flag = 3;
                            //flag++;


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

                            if (flag == 0) {
                                compressedImageFile = new ImageZipper(MetsoNewReimbursementClaimActivity.this)
                                        .setQuality(100)
                                        .setMaxWidth(300)
                                        .setMaxHeight(300)
                                        .compressToFile(file3);
                                flag++;
                            } else if (flag == 1) {
                                compressedImageFile1 = new ImageZipper(MetsoNewReimbursementClaimActivity.this)
                                        .setQuality(100)
                                        .setMaxWidth(300)
                                        .setMaxHeight(300)
                                        .compressToFile(file3);
                                flag++;
                            } else if (flag == 2) {
                                compressedImageFile2 = new ImageZipper(MetsoNewReimbursementClaimActivity.this)
                                        .setQuality(100)
                                        .setMaxWidth(300)
                                        .setMaxHeight(300)
                                        .compressToFile(file3);
                                flag++;
                            } else if (flag == 3) {
                                compressedImageFile3 = new ImageZipper(MetsoNewReimbursementClaimActivity.this)
                                        .setQuality(100)
                                        .setMaxWidth(300)
                                        .setMaxHeight(300)
                                        .compressToFile(file3);
                                flag++;
                            } else if (flag == 4) {
                                compressedImageFile4 = new ImageZipper(MetsoNewReimbursementClaimActivity.this)
                                        .setQuality(100)
                                        .setMaxWidth(300)
                                        .setMaxHeight(300)
                                        .compressToFile(file3);
                            }

                          /*  compressedImageFile3 = new ImageZipper(MetsoNewReimbursementClaimActivity.this)
                                    .setQuality(100)
                                    .setMaxWidth(300)
                                    .setMaxHeight(300)
                                    .compressToFile(file3);*/
                            BitmapFactory.Options o = new BitmapFactory.Options();
                            o.inSampleSize = 2;
                            Bitmap bm = cropToSquare(BitmapFactory.decodeFile(imageurl, o));
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.PNG, 10, baos); //bm is the bitmap object
                            byte[] b = baos.toByteArray();
                            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                            imgDoc4.setImageBitmap(bm);
                            imgUnselect4.setVisibility(View.VISIBLE);
                            Log.d("images", encodedImage);
                            //flag = 4;
                            //flag++;


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
                            //file4 = new File(imageurl);

                            if (flag == 0) {
                                compressedImageFile = new ImageZipper(MetsoNewReimbursementClaimActivity.this)
                                        .setQuality(100)
                                        .setMaxWidth(300)
                                        .setMaxHeight(300)
                                        .compressToFile(file4);
                                flag++;
                            } else if (flag == 1) {
                                compressedImageFile1 = new ImageZipper(MetsoNewReimbursementClaimActivity.this)
                                        .setQuality(100)
                                        .setMaxWidth(300)
                                        .setMaxHeight(300)
                                        .compressToFile(file4);
                                flag++;
                            } else if (flag == 2) {
                                compressedImageFile2 = new ImageZipper(MetsoNewReimbursementClaimActivity.this)
                                        .setQuality(100)
                                        .setMaxWidth(300)
                                        .setMaxHeight(300)
                                        .compressToFile(file4);
                                flag++;
                            } else if (flag == 3) {
                                compressedImageFile3 = new ImageZipper(MetsoNewReimbursementClaimActivity.this)
                                        .setQuality(100)
                                        .setMaxWidth(300)
                                        .setMaxHeight(300)
                                        .compressToFile(file4);
                                flag++;
                            } else if (flag == 4) {
                                compressedImageFile4 = new ImageZipper(MetsoNewReimbursementClaimActivity.this)
                                        .setQuality(100)
                                        .setMaxWidth(300)
                                        .setMaxHeight(300)
                                        .compressToFile(file4);
                            }





                          /*  compressedImageFile4 = new ImageZipper(MetsoNewReimbursementClaimActivity.this)
                                    .setQuality(100)
                                    .setMaxWidth(300)
                                    .setMaxHeight(300)
                                    .compressToFile(file4);*/
                            BitmapFactory.Options o = new BitmapFactory.Options();
                            o.inSampleSize = 2;
                            Bitmap bm = cropToSquare(BitmapFactory.decodeFile(imageurl, o));
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.PNG, 10, baos); //bm is the bitmap object
                            byte[] b = baos.toByteArray();
                            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                            imgDoc5.setImageBitmap(bm);
                            imgUnselect5.setVisibility(View.VISIBLE);
                            Log.d("images", encodedImage);
                            //flag = 5;
                            //flag++;


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
                    File pictureFile = (File) data.getExtras().get("picture");
                    Log.d("fjjgk", pictureFile.toString());
                    try {
                        compressedImageFile = new Compressor(this).compressToFile(pictureFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d("imageSixw", String.valueOf(getReadableFileSize(pictureFile.length())));
                    //alerDialog3.dismiss();
                }
                break;

            case REQUEST_GALLERY_CODE_ONE:
                if (resultCode == Activity.RESULT_OK) {
                    InputStream imageStream = null;
                    try {
                        try {
                            uri = data.getData();
                            String filePath = getRealPathFromURIPath(uri, MetsoNewReimbursementClaimActivity.this);
                            file = new File(filePath);
                            if (galleryFlag == 0) {
                                compressedImageFile = new ImageZipper(MetsoNewReimbursementClaimActivity.this)
                                        .setQuality(100)
                                        .setMaxWidth(300)
                                        .setMaxHeight(300)
                                        .compressToFile(file);
                            } else if (galleryFlag == 1) {
                                compressedImageFile1 = new ImageZipper(MetsoNewReimbursementClaimActivity.this)
                                        .setQuality(100)
                                        .setMaxWidth(300)
                                        .setMaxHeight(300)
                                        .compressToFile(file);
                            }
                            Log.d(TAG, "compressedImageFile =" + compressedImageFile);
                            imageStream = getContentResolver().openInputStream(uri);
                            Bitmap bm = cropToSquare(BitmapFactory.decodeStream(imageStream));
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.PNG, 10, baos); //bm is the bitmap object
                            byte[] b = baos.toByteArray();
                            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                            imgGalleryOne.setImageBitmap(bm);
                            imgUnselectGalleryOne.setVisibility(View.VISIBLE);
                            galleryFlag++;
                            //flag++;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }

                }
                break;
            case REQUEST_GALLERY_CODE_TWO:
                if (resultCode == Activity.RESULT_OK) {
                    InputStream imageStream = null;
                    try {
                        try {
                            uri = data.getData();
                            String filePath = getRealPathFromURIPath(uri, MetsoNewReimbursementClaimActivity.this);
                            file = new File(filePath);
                            if (galleryFlag == 0) {
                                compressedImageFile = new ImageZipper(MetsoNewReimbursementClaimActivity.this)
                                        .setQuality(100)
                                        .setMaxWidth(300)
                                        .setMaxHeight(300)
                                        .compressToFile(file);
                            } else if (galleryFlag == 1) {
                                compressedImageFile1 = new ImageZipper(MetsoNewReimbursementClaimActivity.this)
                                        .setQuality(100)
                                        .setMaxWidth(300)
                                        .setMaxHeight(300)
                                        .compressToFile(file);
                            }

                            //  Log.d(TAG, "filePath=" + filePath);
                            imageStream = getContentResolver().openInputStream(uri);
                            Bitmap bm = cropToSquare(BitmapFactory.decodeStream(imageStream));
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.PNG, 10, baos); //bm is the bitmap object
                            byte[] b = baos.toByteArray();
                            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                            imgGalleryTwo.setImageBitmap(bm);
                            imgUnselectGalleryTwo.setVisibility(View.VISIBLE);
                            galleryFlag++;
                            //flag++;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }

                }
                break;
            case REQUEST_SELECT_PDF:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getData();
                    Log.e(TAG, "onActivityResult: "+uri.getPath());
                    String imagePath = uri.getPath();
                    if (imagePath.contains("all_external")){
                        pdfFileName = FindDocumentInformation.FileNameFromURL(imagePath);
                        pdfFile = convertInputStreamToFile(uri,pdfFileName);
                        Log.e(TAG, "onActivityResult: "+pdfFile.getAbsolutePath());
                    } else {
                        try {
                            pdfFilePath = getRealPath(MetsoNewReimbursementClaimActivity.this,uri);
                            Log.e(TAG, "onActivityResult: ================== "+pdfFilePath);
                            pdfFileName = FindDocumentInformation.FileNameFromURL(pdfFilePath);
                        } catch (IllegalArgumentException e){
                            //Todo: from WPS office document select
                            pdfFileName = FindDocumentInformation.FileNameFromURL(imagePath);
                            pdfFile = convertInputStreamToFile(uri,pdfFileName);
                            Log.e(TAG, "onActivityResult: "+pdfFile.getAbsolutePath());
                        }
                        pdfFile = convertInputStreamToFile(uri,pdfFileName);
                        Log.e(TAG, "onActivityResult: Real Path: "+pdfFilePath);
                        Log.e(TAG, "onActivityResult: PDF name "+pdfFileName);
                        Log.e(TAG, "onActivityResult: Final PDF path"+pdfFile);
                    }
                    imgPDF.setVisibility(View.VISIBLE);
                    imgUnselectPdf.setVisibility(View.VISIBLE);
                    pdfflag = 1;
                    //flag++;
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

    private void postoneimage() {
        Log.d("rem", "1");
        progressDialog.show();

        //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody mFile = RequestBody.create(MediaType.parse(".PNG"), compressedImageFile);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", compressedImageFile.getName(), mFile);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), compressedImageFile.getName());

        Log.e(TAG, "postoneimage: CostCentreId: " + CostCentreId + " WbsId: " + WbsId + " Siteid: " + Siteid + " SupervisorID: " + SupervisorID);

        Call<UploadObject> fileUpload = RetrofitClient
                .getInstance()
                .getApi()
                .postMetsoReimbursementWithImage1(aempid, comeid, description, amount, year, month, securitycode, fileToUpload, componentId, "0", "0", CostCentreId, WbsId, Siteid, SupervisorID,startDate,endDate);
        fileUpload.enqueue(new Callback<UploadObject>() {
            @Override
            public void onResponse(Call<UploadObject> call, retrofit2.Response<UploadObject> response) {
                progressDialog.dismiss();
                UploadObject extraWorkingDayModel = response.body();
                if (extraWorkingDayModel.isResponseStatus()) {
                    //  Toast.makeText(getApplicationContext(), extraWorkingDayModel.getResponseText(), Toast.LENGTH_SHORT).show();
                    successAlert(extraWorkingDayModel.getResponseText());
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
        MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("file", compressedImageFile1.getName(), mFile1);
        RequestBody filename1 = RequestBody.create(MediaType.parse("text/plain"), compressedImageFile1.getName());

        Call<UploadObject> fileUpload = RetrofitClient
                .getInstance()
                .getApi()
                .postMetsoReimbursementWithImage2(aempid, comeid, description, amount, year, month, securitycode, fileToUpload, fileToUpload1, componentId, "0", "0", CostCentreId, WbsId, Siteid, SupervisorID,startDate,endDate);
        fileUpload.enqueue(new Callback<UploadObject>() {
            @Override
            public void onResponse(Call<UploadObject> call, retrofit2.Response<UploadObject> response) {
                progressDialog.dismiss();
                UploadObject extraWorkingDayModel = response.body();
                if (extraWorkingDayModel.isResponseStatus()) {
                    //  Toast.makeText(getApplicationContext(), extraWorkingDayModel.getResponseText(), Toast.LENGTH_SHORT).show();
                    successAlert(extraWorkingDayModel.getResponseText());

                } else {
                    //  Toast.makeText(getApplicationContext(), extraWorkingDayModel.getResponseText(), Toast.LENGTH_SHORT).show();
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

        Call<UploadObject> fileUpload = RetrofitClient
                .getInstance()
                .getApi()
                .postMetsoReimbursementWithImage3(aempid, comeid, description, amount, year, month, securitycode, fileToUpload, fileToUpload1, fileToUpload2, componentId, "0", "0", CostCentreId, WbsId, Siteid, SupervisorID,startDate,endDate);
        fileUpload.enqueue(new Callback<UploadObject>() {
            @Override
            public void onResponse(Call<UploadObject> call, retrofit2.Response<UploadObject> response) {
                progressDialog.dismiss();
                UploadObject extraWorkingDayModel = response.body();
                if (extraWorkingDayModel.isResponseStatus()) {
                    //  Toast.makeText(getApplicationContext(), extraWorkingDayModel.getResponseText(), Toast.LENGTH_SHORT).show();
                    successAlert(extraWorkingDayModel.getResponseText());


                } else {
                    //  Toast.makeText(getApplicationContext(), extraWorkingDayModel.getResponseText(), Toast.LENGTH_SHORT).show();
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

        Call<UploadObject> fileUpload = RetrofitClient
                .getInstance()
                .getApi()
                .postMetsoReimbursementWithImage4(aempid, comeid, description, amount, year, month, securitycode, fileToUpload, fileToUpload1, fileToUpload2, fileToUpload3, componentId, "0", "0", CostCentreId, WbsId, Siteid, SupervisorID,startDate,endDate);
        fileUpload.enqueue(new Callback<UploadObject>() {
            @Override
            public void onResponse(Call<UploadObject> call, retrofit2.Response<UploadObject> response) {
                progressDialog.dismiss();
                UploadObject extraWorkingDayModel = response.body();
                if (extraWorkingDayModel.isResponseStatus()) {
                    //  Toast.makeText(getApplicationContext(), extraWorkingDayModel.getResponseText(), Toast.LENGTH_SHORT).show();
                    successAlert(extraWorkingDayModel.getResponseText());


                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong!Please try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UploadObject> call, Throwable t) {
                progressDialog.dismiss();

                Log.e("error", "Error " + t.getMessage());
                // Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();

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


        Call<UploadObject> fileUpload = RetrofitClient
                .getInstance()
                .getApi()
                .postMetsoReimbursementWithImage5(aempid, comeid, description, amount, year, month, securitycode, fileToUpload, fileToUpload1, fileToUpload2, fileToUpload3, fileToUpload4, componentId, "0", "0", CostCentreId, WbsId, Siteid, SupervisorID,startDate,endDate);
        fileUpload.enqueue(new Callback<UploadObject>() {
            @Override
            public void onResponse(Call<UploadObject> call, retrofit2.Response<UploadObject> response) {
                progressDialog.dismiss();
                UploadObject extraWorkingDayModel = response.body();
                if (extraWorkingDayModel.isResponseStatus()) {
                    //  Toast.makeText(getApplicationContext(), extraWorkingDayModel.getResponseText(), Toast.LENGTH_SHORT).show();
                    successAlert(extraWorkingDayModel.getResponseText());

                } else {
                    // Toast.makeText(getApplicationContext(), extraWorkingDayModel.getResponseText(), Toast.LENGTH_SHORT).show();
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

    private void successAlert(String text) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MetsoNewReimbursementClaimActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);
        tvInvalidDate.setText(text);
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                Intent intent = new Intent(MetsoNewReimbursementClaimActivity.this, MetsoReimbursementReportActivity.class);
                startActivity(intent);
                finish();
            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(false);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }

    private void hideAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MetsoNewReimbursementClaimActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_hide, null);
        dialogBuilder.setView(dialogView);
        Button btnOk = (Button) dialogView.findViewById(R.id.btnSubmit);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert1.dismiss();
            }
        });

        alert1 = dialogBuilder.create();
        alert1.setCancelable(false);
        Window window = alert1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alert1.show();
    }

    private void showYearDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MetsoNewReimbursementClaimActivity.this, R.style.CustomDialogNew);
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MetsoNewReimbursementClaimActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_metso_month, null);
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


        RecyclerView rvMonth = dialogView.findViewById(R.id.rvMonth);
        rvMonth.setLayoutManager(new LinearLayoutManager(this));
        MonthAdapter monthAdapter = new MonthAdapter(MetsoNewReimbursementClaimActivity.this, monthList);
        rvMonth.setAdapter(monthAdapter);
        monthAdapter.setSelectMonthListener(new SelectMonthListener() {
            @Override
            public void onClick(String selected_month) {
                month = selected_month;
                tvMonth.setText(month);
                alertDialog2.dismiss();
            }
        });

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


    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


    public void uploadMultipart() {
        //getting name for the pdf
        Log.e(TAG, "uploadMultipart: \nAEMEmployeeID"+ pref.getEmpId()
                +"\nAEMComponentID"+ comeid
                +"\nDescription"+ description
                +"\nReimbursementAmount"+ amount
                +"\nYear"+ year
                +"\nMonth"+ month
                +"\nSecurityCode"+ securitycode
                +"\nConveyanceTypeId"+ componentId
                +"\nLocationTypeID"+ "0"
                +"\nReimbursementDate"+ "0"
                +"\nCostCentreId"+ CostCentreId
                +"\nWbsId"+ WbsId
                +"\nSiteid"+ Siteid
                +"\nSupervisorID"+ SupervisorID
                +"\nStartDate"+ startDate
                +"\nEndDate"+ endDate);

        //getting the actual path of the pdf
        //TODO: new api
       /* AndroidNetworking.upload(AppData.SAVE_REIMBURSEMENT_CLAIM_METSO)
                .addMultipartParameter("AEMEmployeeID", pref.getEmpId())
                .addMultipartParameter("AEMComponentID", comeid)
                .addMultipartParameter("Description", description)
                .addMultipartParameter("ReimbursementAmount", amount)
                .addMultipartParameter("Year", year)
                .addMultipartParameter("Month", month)
                .addMultipartParameter("SecurityCode", securitycode)
                .addMultipartParameter("ConveyanceTypeId", componentId)
                .addMultipartParameter("LocationTypeID", "0")
                .addMultipartParameter("ReimbursementDate", "0")
                .addMultipartParameter("CostCentreId", CostCentreId) // CostCentreId,WbsId,Siteid,SupervisorID
                .addMultipartParameter("WbsId", WbsId)
                .addMultipartParameter("Siteid", Siteid)
                .addMultipartParameter("SupervisorID", SupervisorID)
                .addMultipartParameter("StartDate", startDate)
                .addMultipartParameter("EndDate", endDate)
                .addMultipartFile("SingleFile", pdfFile)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
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
                        progressDialog.dismiss();

                        try {
                            Log.e(TAG, "MULTIPART_FILE_METSO: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                successAlert(Response_Message);
                            } else {
                                Toast.makeText(getApplicationContext(), Response_Message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        *//*JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        String responseText = job1.optString("responseText");
                        boolean responseStatus = job1.optBoolean("responseStatus");

                        if (responseStatus) {

                            successAlert(responseText);

                        } else {

                            Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();

                        }*//*


                        // boolean _status = job1.getBoolean("status");


                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e("errt", String.valueOf(error));
                        Log.e("errt", error.getErrorBody());
                        Toast.makeText(getApplicationContext(), "Something went wrong,Please try again", Toast.LENGTH_LONG).show();
                    }
                });*/

        AndroidNetworking.upload(UPLOAD_URL)
                .addMultipartParameter("AEMEmployeeID", pref.getEmpId())
                .addMultipartParameter("AEMComponentID", comeid)
                .addMultipartParameter("Description", description)
                .addMultipartParameter("ReimbursementAmount", amount)
                .addMultipartParameter("Year", year)
                .addMultipartParameter("Month", month)
                .addMultipartParameter("SecurityCode", securitycode)
                .addMultipartParameter("ConveyanceTypeId", componentId)
                .addMultipartParameter("LocationTypeID", "0")
                .addMultipartParameter("ReimbursementDate", "0")
                .addMultipartParameter("CostCentreId", CostCentreId) // CostCentreId,WbsId,Siteid,SupervisorID
                .addMultipartParameter("WbsId", WbsId)
                .addMultipartParameter("Siteid", Siteid)
                .addMultipartParameter("SupervisorID", SupervisorID)
                .addMultipartParameter("StartDate", startDate)
                .addMultipartParameter("EndDate", endDate)
                .addMultipartFile("SingleFile", pdfFile)
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
                        progressDialog.dismiss();


                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        String responseText = job1.optString("responseText");
                        boolean responseStatus = job1.optBoolean("responseStatus");

                        if (responseStatus) {

                            successAlert(responseText);

                        } else {

                            Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();

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

    public void uploadMultipartwithfile() {
        //getting name for the pdf
        //getting the actual path of the pdf
        //TODO: new api
        /*AndroidNetworking.upload(AppData.SAVE_REIMBURSEMENT_CLAIM_METSO)
                .addMultipartParameter("AEMEmployeeID", pref.getEmpId())
                .addMultipartParameter("AEMComponentID", comeid)
                .addMultipartParameter("Description", description)
                .addMultipartParameter("ReimbursementAmount", amount)
                .addMultipartParameter("Year", year)
                .addMultipartParameter("Month", month)
                .addMultipartParameter("SecurityCode", securitycode)
                .addMultipartParameter("ConveyanceTypeId", componentId)
                .addMultipartParameter("LocationTypeID", "0")
                .addMultipartParameter("ReimbursementDate", "0")
                .addMultipartParameter("CostCentreId", CostCentreId)
                .addMultipartParameter("WbsId", WbsId)
                .addMultipartParameter("Siteid", Siteid)
                .addMultipartParameter("SupervisorID", SupervisorID)
                .addMultipartParameter("StartDate", startDate)
                .addMultipartParameter("EndDate", endDate)
                .addMultipartFile("SingleFile", pdfFile)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
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
                        progressDialog.dismiss();

                        try {
                            Log.e(TAG, "MULTIPART_WITH_FILE: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                //successAlert(Response_Message);
                                postOneImage();
                            } else {
                                Toast.makeText(getApplicationContext(), Response_Message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MetsoNewReimbursementClaimActivity.this, "Something went to wrong", Toast.LENGTH_SHORT).show();
                        }


                       *//* JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        String responseText = job1.optString("responseText");
                        boolean responseStatus = job1.optBoolean("responseStatus");

                        if (responseStatus) {
                            postoneimage();
                        } else {
                            Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();
                        }*//*


                        // boolean _status = job1.getBoolean("status");
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e("errt", String.valueOf(error));
                        Toast.makeText(getApplicationContext(), "Something went wrong,Please try again", Toast.LENGTH_LONG).show();
                    }
                });*/

        AndroidNetworking.upload(UPLOAD_URL)
                .addMultipartParameter("AEMEmployeeID", pref.getEmpId())
                .addMultipartParameter("AEMComponentID", comeid)
                .addMultipartParameter("Description", description)
                .addMultipartParameter("ReimbursementAmount", amount)
                .addMultipartParameter("Year", year)
                .addMultipartParameter("Month", month)
                .addMultipartParameter("SecurityCode", securitycode)
                .addMultipartParameter("ConveyanceTypeId", componentId)
                .addMultipartParameter("LocationTypeID", "0")
                .addMultipartParameter("ReimbursementDate", "0")
                .addMultipartParameter("CostCentreId", CostCentreId)
                .addMultipartParameter("WbsId", WbsId)
                .addMultipartParameter("Siteid", Siteid)
                .addMultipartParameter("SupervisorID", SupervisorID)
                .addMultipartParameter("StartDate", startDate)
                .addMultipartParameter("EndDate", endDate)
                .addMultipartFile("SingleFile", pdfFile)
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
                        progressDialog.dismiss();


                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        String responseText = job1.optString("responseText");
                        boolean responseStatus = job1.optBoolean("responseStatus");

                        if (responseStatus) {
                            postoneimage();
                        } else {
                            Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();
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

    public void uploadMultipartwithTwofile() {
        //getting name for the pdf

        //getting the actual path of the pdf
        //TODO: new api
        /*AndroidNetworking.upload(AppData.SAVE_REIMBURSEMENT_CLAIM_METSO)
                .addMultipartParameter("AEMEmployeeID", pref.getEmpId())
                .addMultipartParameter("AEMComponentID", comeid)
                .addMultipartParameter("Description", description)
                .addMultipartParameter("ReimbursementAmount", amount)
                .addMultipartParameter("Year", year)
                .addMultipartParameter("Month", month)
                .addMultipartParameter("SecurityCode", securitycode)
                .addMultipartParameter("ConveyanceTypeId", componentId)
                .addMultipartParameter("LocationTypeID", "0")
                .addMultipartParameter("ReimbursementDate", "0")
                .addMultipartParameter("CostCentreId", CostCentreId)
                .addMultipartParameter("WbsId", WbsId)
                .addMultipartParameter("Siteid", Siteid)
                .addMultipartParameter("SupervisorID", SupervisorID)
                .addMultipartParameter("StartDate", startDate)
                .addMultipartParameter("EndDate", endDate)
                .addMultipartFile("SingleFile", pdfFile)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
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
                        progressDialog.dismiss();
                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        String responseText = job1.optString("responseText");
                        boolean responseStatus = job1.optBoolean("responseStatus");
                        if (responseStatus) {
                            //postoneimage();
                            postOneImage();
                        } else {
                            Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();
                        }
                        // boolean _status = job1.getBoolean("status");
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e("errt", String.valueOf(error));
                        Toast.makeText(getApplicationContext(), "Something went wrong,Please try again", Toast.LENGTH_LONG).show();
                    }
                });*/


        AndroidNetworking.upload(UPLOAD_URL)
                .addMultipartParameter("AEMEmployeeID", pref.getEmpId())
                .addMultipartParameter("AEMComponentID", comeid)
                .addMultipartParameter("Description", description)
                .addMultipartParameter("ReimbursementAmount", amount)
                .addMultipartParameter("Year", year)
                .addMultipartParameter("Month", month)
                .addMultipartParameter("SecurityCode", securitycode)
                .addMultipartParameter("ConveyanceTypeId", componentId)
                .addMultipartParameter("LocationTypeID", "0")
                .addMultipartParameter("ReimbursementDate", "0")

                .addMultipartParameter("CostCentreId", CostCentreId)
                .addMultipartParameter("WbsId", WbsId)
                .addMultipartParameter("Siteid", Siteid)
                .addMultipartParameter("SupervisorID", SupervisorID)
                .addMultipartParameter("StartDate", startDate)
                .addMultipartParameter("EndDate", endDate)
                .addMultipartFile("SingleFile", pdfFile)
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
                        progressDialog.dismiss();
                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        String responseText = job1.optString("responseText");
                        boolean responseStatus = job1.optBoolean("responseStatus");
                        if (responseStatus) {
                            postoneimage();
                        } else {
                            Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();
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


    public String getReadableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }


    private void openCameraDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MetsoNewReimbursementClaimActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_cameraimage, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llSingle = (LinearLayout) dialogView.findViewById(R.id.llSingle);
        llSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        LinearLayout llMultiple = (LinearLayout) dialogView.findViewById(R.id.llMultiple);
        llMultiple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LongImageCameraActivity.launch(MetsoNewReimbursementClaimActivity.this);
            }
        });


        alerDialog3 = dialogBuilder.create();
        alerDialog3.setCancelable(true);
        Window window = alerDialog3.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog3.show();
    }

    private void showPDFPickerdialog() {
        DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File(DialogConfigs.DEFAULT_DIR);
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = new String[]{"pdf", "PDF"};

        final FilePickerDialog dialog = new FilePickerDialog(this, properties);
        dialog.setTitle("Select a File");
        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                //files is the array of the paths of files selected by the Application User.
                dialog.dismiss();
                if (files.length > 0) {
                    Log.d("arpan", files[0]);
                    pdfFile = new File(files[0]);
                    pdfflag = 1;
                    imgPDF.setVisibility(View.VISIBLE);
                }
            }
        });
        dialog.show();
    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    private void attachFileAPI() {
        if (pdfflag == 1) {
            Log.e(TAG, "attachFileAPI: uploadMultipart");
            uploadMultipart();
        } else if (pdfflag == 1 && galleryFlag == 1) {
            Log.e(TAG, "attachFileAPI: uploadMultipartwithfile");
            uploadMultipartwithfile();
        } else if (pdfflag == 1 && galleryFlag == 2) {
            Log.e(TAG, "attachFileAPI: uploadMultipartwithTwofile");
            uploadMultipartwithTwofile();
        } else if (galleryFlag == 1) {
            Log.e(TAG, "attachFileAPI: postoneimage");
            postoneimage();
            //TODO: new api
            //postOneImage();
        } else if (galleryFlag == 2) {
            Log.e(TAG, "attachFileAPI: posttwoimage");
            posttwoimage();
            //TODO: new api
            //postTwoImage();
        } else {
            Toast.makeText(MetsoNewReimbursementClaimActivity.this, "Please Attach Your Reimbursement File", Toast.LENGTH_LONG).show();
        }
    }


    private void checkPermission(int i) {
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Log.e("onPermissionsGranted", "Called");
                            if (i == 0) {
                                Intent uploadIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                uploadIntent.setType("application/pdf");
                                startActivityForResult(uploadIntent, REQUEST_SELECT_PDF);
                            } else if (i == 1) {
                                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
                                openGalleryIntent.setType("image/*");
                                startActivityForResult(openGalleryIntent, REQUEST_GALLERY_CODE_ONE);
                            } else if (i == 2) {
                                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
                                openGalleryIntent.setType("image/*");
                                startActivityForResult(openGalleryIntent, REQUEST_GALLERY_CODE_TWO);
                            }
                        } else {
                            Toast.makeText(MetsoNewReimbursementClaimActivity.this, "Permissions are required to perform app functionality.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private File convertInputStreamToFile(Uri uri, String fileNme) {
        InputStream inputStream;
        try {
            inputStream = MetsoNewReimbursementClaimActivity.this.getContentResolver().openInputStream(uri);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        File file = new File(MetsoNewReimbursementClaimActivity.this.getExternalFilesDir("/").getAbsolutePath(), fileNme);

        try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
            int read;
            byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            Log.e(TAG, "convertInputStreamToFile: file: " + file.getPath());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
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


    public interface WbsCodeSelectListener {
        void onClick(String wbs_id, String wbs_code);
    }

    public interface CostCenterSelectListener {
        void onClick(String cost_center_id, String cost_center);
    }

    public interface SupervisorSelectListener {
        void onClick(String supervisor_id, String supervisor);
    }

    public interface SiteSelectListener {
        void onClick(String site_id, String site);
    }

    public interface SelectMonthListener {
        void onClick(String selected_month);
    }

    private void showStartDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(MetsoNewReimbursementClaimActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        int month = (monthOfYear + 1);
                        startDate = year + "-" + month + "-" + dayOfMonth;
                        tvStartDate.setText(Util.changeAnyDateFormat(startDate, "yyyy-MM-dd", "dd MMM yyyy"));
                        lnEndDate.setVisibility(View.VISIBLE);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate((long) (System.currentTimeMillis() - 1000));
        datePickerDialog.show();
    }


    private void showEndDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(MetsoNewReimbursementClaimActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        int enddate = dayOfMonth + monthOfYear + year;
                        int month = (monthOfYear + 1);
                        endDate = year + "-" + month + "-" + dayOfMonth;
                        tvEndDate.setText(Util.changeAnyDateFormat(endDate, "yyyy-MM-dd", "dd MMM yyyy"));


                        SimpleDateFormat sdf
                                = new SimpleDateFormat(
                                "dd/MM/yyyy");

                        // Try Block
                        try {

                            // parse method is used to parse
                            // the text from a string to
                            // produce the date
                            Date d1 = sdf.parse(Util.changeAnyDateFormat(startDate, "yyyy-MM-dd", "dd/MM/yyyy"));
                            Date d2 = sdf.parse(Util.changeAnyDateFormat(endDate, "yyyy-MM-dd", "dd/MM/yyyy"));

                            // Calculate time difference
                            // in milliseconds
                            long difference_In_Time
                                    = d2.getTime() - d1.getTime();



                            long difference_In_Days
                                    = (difference_In_Time
                                    / (1000 * 60 * 60 * 24))
                                    % 365;



                            dayscount= Math.toIntExact(difference_In_Days)+1;
                            Log.d("dayscount",""+dayscount);
                            if (component.equalsIgnoreCase("DAILY EXP.")){
                                int amount=(200*dayscount);
                                etAmount.setText(""+amount);
                            }else if (component.equalsIgnoreCase("DAILY TRAVEL REIMB.")){
                                int amount=(600*dayscount);
                                etAmount.setText(""+amount);
                            }




                        }

                        // Catch the Exception
                        catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.getDatePicker().setMaxDate((long) (System.currentTimeMillis() - 1000));
        datePickerDialog.show();
    }


    private void postOneImage() {
        Log.e(TAG, "postOneImage: \nAEMEmployeeID:"+aempid
                +"\nAEMComponentID:"+comeid
                +"\nDescription:"+description
                +"\nReimbursementAmount:"+amount
                +"\nYear:"+year
                +"\nMonth:"+month
                +"\nSecurityCode:"+securitycode
                +"\nConveyanceTypeId:"+componentId
                +"\nLocationTypeID:"+"0"
                +"\nReimbursementDate:"+"0"
                +"\nCostCentreId:"+CostCentreId
                +"\nWbsId:"+WbsId
                +"\nSiteid:"+Siteid
                +"\nSupervisorID:"+SupervisorID
                +"\nStartDate:"+startDate
                +"\nEndDate:"+endDate
                +"\nfile: ");

        progressDialog.show();
        AndroidNetworking.upload(AppData.SAVE_REIMBURSEMENT_CLAIM_METSO)
                .addMultipartParameter("AEMEmployeeID", pref.getEmpId())
                .addMultipartParameter("AEMComponentID", comeid)
                .addMultipartParameter("Description", description)
                .addMultipartParameter("ReimbursementAmount", amount)
                .addMultipartParameter("Year", year)
                .addMultipartParameter("Month", month)
                .addMultipartParameter("SecurityCode", securitycode)
                .addMultipartParameter("ConveyanceTypeId", componentId)
                .addMultipartParameter("LocationTypeID", "0")
                .addMultipartParameter("ReimbursementDate", "0")
                .addMultipartParameter("CostCentreId", CostCentreId)
                .addMultipartParameter("WbsId", WbsId)
                .addMultipartParameter("Siteid", Siteid)
                .addMultipartParameter("SupervisorID", SupervisorID)
                .addMultipartParameter("StartDate", startDate)
                .addMultipartParameter("EndDate", endDate)
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
                            Log.e(TAG, "METSO_POST_ONE_IMAGE: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                successAlert(Response_Message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.show();
                        Log.e(TAG, "MOTSO_POST_ONE_IMAGE: "+anError.getErrorBody());
                    }
                });
    }

    private void postTwoImage() {
        progressDialog.show();
        AndroidNetworking.upload(AppData.SAVE_REIMBURSEMENT_CLAIM_METSO)
                .addMultipartParameter("AEMEmployeeID", pref.getEmpId())
                .addMultipartParameter("AEMComponentID", comeid)
                .addMultipartParameter("Description", description)
                .addMultipartParameter("ReimbursementAmount", amount)
                .addMultipartParameter("Year", year)
                .addMultipartParameter("Month", month)
                .addMultipartParameter("SecurityCode", securitycode)
                .addMultipartParameter("ConveyanceTypeId", componentId)
                .addMultipartParameter("LocationTypeID", "0")
                .addMultipartParameter("ReimbursementDate", "0")
                .addMultipartParameter("CostCentreId", CostCentreId)
                .addMultipartParameter("WbsId", WbsId)
                .addMultipartParameter("Siteid", Siteid)
                .addMultipartParameter("SupervisorID", SupervisorID)
                .addMultipartParameter("StartDate", startDate)
                .addMultipartParameter("EndDate", endDate)
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
                            Log.e(TAG, "METSO_POST_TWO_IMAGES: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                successAlert(Response_Message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.show();
                        Log.e(TAG, "METSO_POST_TWO_IMAGES_error: "+anError.getErrorBody());
                    }
                });
    }

    private void postThreeImage() {
        progressDialog.show();
        AndroidNetworking.upload(AppData.SAVE_REIMBURSEMENT_CLAIM_METSO)
                .addMultipartParameter("AEMEmployeeID", pref.getEmpId())
                .addMultipartParameter("AEMComponentID", comeid)
                .addMultipartParameter("Description", description)
                .addMultipartParameter("ReimbursementAmount", amount)
                .addMultipartParameter("Year", year)
                .addMultipartParameter("Month", month)
                .addMultipartParameter("SecurityCode", securitycode)
                .addMultipartParameter("ConveyanceTypeId", componentId)
                .addMultipartParameter("LocationTypeID", "0")
                .addMultipartParameter("ReimbursementDate", "0")
                .addMultipartParameter("CostCentreId", CostCentreId)
                .addMultipartParameter("WbsId", WbsId)
                .addMultipartParameter("Siteid", Siteid)
                .addMultipartParameter("SupervisorID", SupervisorID)
                .addMultipartParameter("StartDate", startDate)
                .addMultipartParameter("EndDate", endDate)
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
                            Log.e(TAG, "METSO_POST_THREE_IMAGE: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                successAlert(Response_Message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.show();
                        Log.e(TAG, "METSO_POST_THREE_IMAGE_error: "+anError.getErrorBody());
                    }
                });
    }

    private void postFourImage() {
        progressDialog.show();
        AndroidNetworking.upload(AppData.SAVE_REIMBURSEMENT_CLAIM_METSO)
                .addMultipartParameter("AEMEmployeeID", pref.getEmpId())
                .addMultipartParameter("AEMComponentID", comeid)
                .addMultipartParameter("Description", description)
                .addMultipartParameter("ReimbursementAmount", amount)
                .addMultipartParameter("Year", year)
                .addMultipartParameter("Month", month)
                .addMultipartParameter("SecurityCode", securitycode)
                .addMultipartParameter("ConveyanceTypeId", componentId)
                .addMultipartParameter("LocationTypeID", "0")
                .addMultipartParameter("ReimbursementDate", "0")
                .addMultipartParameter("CostCentreId", CostCentreId)
                .addMultipartParameter("WbsId", WbsId)
                .addMultipartParameter("Siteid", Siteid)
                .addMultipartParameter("SupervisorID", SupervisorID)
                .addMultipartParameter("StartDate", startDate)
                .addMultipartParameter("EndDate", endDate)
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
                            Log.e(TAG, "METSO_POST_FOUR_IMAGES: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                successAlert(Response_Message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.show();
                        Log.e(TAG, "METSO_POST_FOUR_IMAGES_error: "+anError.getErrorBody());
                    }
                });
    }

    private void postFiveImage() {
        progressDialog.show();
        AndroidNetworking.upload(AppData.SAVE_REIMBURSEMENT_CLAIM_METSO)
                .addMultipartParameter("AEMEmployeeID", pref.getEmpId())
                .addMultipartParameter("AEMComponentID", comeid)
                .addMultipartParameter("Description", description)
                .addMultipartParameter("ReimbursementAmount", amount)
                .addMultipartParameter("Year", year)
                .addMultipartParameter("Month", month)
                .addMultipartParameter("SecurityCode", securitycode)
                .addMultipartParameter("ConveyanceTypeId", componentId)
                .addMultipartParameter("LocationTypeID", "0")
                .addMultipartParameter("ReimbursementDate", "0")
                .addMultipartParameter("CostCentreId", CostCentreId)
                .addMultipartParameter("WbsId", WbsId)
                .addMultipartParameter("Siteid", Siteid)
                .addMultipartParameter("SupervisorID", SupervisorID)
                .addMultipartParameter("StartDate", startDate)
                .addMultipartParameter("EndDate", endDate)
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
                            Log.e(TAG, "METSO_POST_FOUR_IMAGES: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            String Response_Message = job1.optString("Response_Message");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                successAlert(Response_Message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.show();
                        Log.e(TAG, "METSO_POST_FOUR_IMAGES_error: "+anError.getErrorBody());
                    }
                });
    }
}