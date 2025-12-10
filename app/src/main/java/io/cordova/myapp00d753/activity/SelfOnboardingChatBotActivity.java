package io.cordova.myapp00d753.activity;

import static java.util.Calendar.DAY_OF_MONTH;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.SelfOnboardingChatbotMessageAdapter;
import io.cordova.myapp00d753.adapter.TempCommonFilterForSelfOnboardingAdapter;
import io.cordova.myapp00d753.module.BotMessageModule;
import io.cordova.myapp00d753.module.MainDocModule;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SelfOnboardingChatBotActivity extends AppCompatActivity {
    private RecyclerView recyclerChat;
    private EditText etMessage;
    private ImageView btnSend;
    private SelfOnboardingChatbotMessageAdapter adapter;
    private List<BotMessageModule> messages = new ArrayList<>();

    private enum MenuState {MAIN, PAYROLL, PF, DOCUMENT}

    private MenuState currentMenu = MenuState.MAIN;
    Pref pref;
    int y;
    String year;
    boolean esiCardFound, medicalCardFound;
    ImageView imgMic;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    private static final int REQ_CAMERA = 101;
    private static final int REQ_GALLERY = 102;
    private Bitmap capturedBitmap;
    boolean awaitingAadharConfirmation;
    boolean awaitingPANConfirmation;
    boolean aadharflag;
    private String extractedAadhar = "";
    ProgressDialog pd;
    ArrayList<MainDocModule> mainQualification = new ArrayList<>();
    ArrayList<String> qualification = new ArrayList<>();

    ArrayList<MainDocModule> mainMartial = new ArrayList<>();
    ArrayList<String> martial = new ArrayList<>();
    ArrayList<MainDocModule> mainGender = new ArrayList<>();
    ArrayList<String> gender = new ArrayList<>();
    ArrayList<MainDocModule> mainRealation = new ArrayList<>();
    ArrayList<String> realation = new ArrayList<>();
    ArrayList<MainDocModule> mainBlood = new ArrayList<>();
    ArrayList<String> blood = new ArrayList<>();
    androidx.appcompat.app.AlertDialog personalDialog;
    androidx.appcompat.app.AlertDialog contactDialog;
    androidx.appcompat.app.AlertDialog bankDialog;
    String month;
    String realationship="",realationshipID,dob="";
    String sexGender="",sexGenderID;
    String education="",educationID;
    String martialstatus="",martialstatusID;
    String bloodgrp="",bloodgrpID;
    String frontID, backID;
    File imageFile;
    File backAadhaarFile;
    File bankFile;
    File panFile;
    Bitmap bitmap = null;
    Bitmap bitmapforAadharBack = null;
    Bitmap bitmapforBank = null;
    private static final int REQ_CAMERA_AADHAAR_BACK = 103;
    private static final int REQ_GALLERY_AADHAAR_BACK = 104;

    private static final int REQ_CAMERA_BANK_DOC = 105;
    private static final int REQ_GALLERY_BANK_DOC = 106;

    Bitmap bitmapPAN = null;
    private static final int REQ_CAMERA_PAN = 108;
    private static final int REQ_GALLERY_PAN = 109;

    ArrayList<String> percity = new ArrayList<>();
    ArrayList<MainDocModule> mainPerCity = new ArrayList<>();
    ArrayList<String> precity = new ArrayList<>();
    ArrayList<MainDocModule> mainPreCity = new ArrayList<>();

    ArrayList<String> state = new ArrayList<>();
    ArrayList<MainDocModule> mainState = new ArrayList<>();
    String presentstate,presentstateID;

    String permanentState,permanentStateID="";
    Dialog searchHolidayDialog;
    TextView txtPresentCity,txtPermanentCity;
    String presentcity,permanentcity,presentcityID="",permanentcityID="";
    ArrayList<MainDocModule> mainBankName = new ArrayList<>();
    ArrayList<String> bankName = new ArrayList<>();

    ArrayList<MainDocModule> mainDocType = new ArrayList<>();
    ArrayList<String> doctype = new ArrayList<>();
    String bankdocid="",bankdoc,bankname,banknameID="";
    ImageView imgDoc;
    String panID;
    String PANnumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot_new);
        pd=new ProgressDialog(SelfOnboardingChatBotActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pref = new Pref(SelfOnboardingChatBotActivity.this);
        y = Calendar.getInstance().get(Calendar.YEAR);
        year = String.valueOf(y);
        recyclerChat = findViewById(R.id.recyclerChat);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        imgMic = findViewById(R.id.imgMic);
        imgMic.setVisibility(View.GONE);

        adapter = new SelfOnboardingChatbotMessageAdapter(messages,SelfOnboardingChatBotActivity.this);
        adapter.setOnUploadClickListener(() -> showImagePicker());
        recyclerChat.setAdapter(adapter);
        recyclerChat.setLayoutManager(new LinearLayoutManager(this));
        JSONObject obj=new JSONObject();
        try {
            obj.put("ddltype", 6);
            obj.put("SecurityCode",pref.getSecurityCode());
            setQualification(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }




        btnSend.setOnClickListener(v -> {
            String msg = etMessage.getText().toString().trim();
            if (msg.isEmpty()) return;

            addUserMessage(msg);
            etMessage.setText("");
            handleUserInput(msg.toLowerCase());
        });

        imgMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent
                        = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                        Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

                try {
                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
                } catch (Exception e) {
                    Toast
                            .makeText(SelfOnboardingChatBotActivity.this, " " + e.getMessage(),
                                    Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }


    private void handleUserInput(String rawMsg) {
        if (rawMsg == null) return;
        String msg = rawMsg.trim().toLowerCase();

        // 🔹 Global commands
        if (msg.equals("restart")) {
            currentMenu = MenuState.MAIN;
            addBotMessage("Hi! I'm Genie🤖\n I'm here to guide you through your self-onboarding process.\n\nTo complete your onboarding, I’ll need the following information:\n\n1.Personal Information.\n2.Contact Details\n3.Bank Details\n4.Aadhaar Card Image (Front and Back) \n5.PAN Card Image. \n\nPlease upload your Aadhaar Card Front Image to continue.");
            addUploadButton();

            return;
        }

        if (msg.equals("exit") || msg.equals("exit chat")) {
            addBotMessage("Thank you for chatting with HR Genius! 👋");
            recyclerChat.postDelayed(this::finish, 1200);
            return;
        }

        if (awaitingAadharConfirmation) {



            if (msg.equals("1")) {
                awaitingAadharConfirmation = false;
                //hitAadhaarAPI(extractedAadhar);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("AEMConsultantID", pref.getEmpConId());
                    jsonObject.put("AEMClientID", pref.getEmpClintId());
                    jsonObject.put("AEMClientOfficeID", pref.getEmpClintOffId());
                    jsonObject.put("AEMEmployeeID", pref.getMasterId());
                    jsonObject.put("WorkingStatus", "1");
                    jsonObject.put("Operation", "12");
                    checkAadhaarNumber(jsonObject,extractedAadhar);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return;
            }
            else if (msg.equals("2")) {
                awaitingAadharConfirmation = false;
                addBotMessage("Please upload your Aadhaar card again so we can proceed further.");
                addUploadButton();  // Camera/Gallery
                return;
            }
            else {
                addBotMessage("Please press 1 for YES or 2 for NO.");
                return;
            }
        }


        if (awaitingPANConfirmation) {



            if (msg.equals("1")) {
                awaitingPANConfirmation = false;
                //hitAadhaarAPI(extractedAadhar);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("AEMConsultantID", pref.getEmpConId());
                    jsonObject.put("AEMClientID", pref.getEmpClintId());
                    jsonObject.put("AEMClientOfficeID", pref.getEmpClintOffId());
                    jsonObject.put("AEMEmployeeID", pref.getMasterId());
                    jsonObject.put("WorkingStatus", "1");
                    jsonObject.put("Operation", "12");
                    panUpload();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return;
            }
            else if (msg.equals("2")) {
                awaitingPANConfirmation = false;
                addBotMessage("Please upload your PAN card again so we can proceed further.");
                addUploadButton();  // Camera/Gallery
                return;
            }
            else {
                addBotMessage("Please press 1 for YES or 2 for NO.");
                return;
            }
        }


    }

    private void addUserMessage(String message) {
        messages.add(new BotMessageModule(message, BotMessageModule.Type.USER));
        adapter.notifyItemInserted(messages.size() - 1);
        recyclerChat.scrollToPosition(messages.size() - 1);
    }

    private void addBotMessage(String message) {
        messages.add(new BotMessageModule(message, BotMessageModule.Type.BOT));
        adapter.notifyItemInserted(messages.size() - 1);
        recyclerChat.scrollToPosition(messages.size() - 1);
    }

    private void showTypingIndicator() {
        messages.add(new BotMessageModule("typing", BotMessageModule.Type.TYPING));
        adapter.notifyItemInserted(messages.size() - 1);
        recyclerChat.scrollToPosition(messages.size() - 1);
    }

    private void hideTypingIndicator() {
        int index = -1;
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getType() == BotMessageModule.Type.TYPING) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            messages.remove(index);
            adapter.notifyItemRemoved(index);
        }
    }


    private void getSpokePersonList() {
        showTypingIndicator();
        String surl = AppData.url + "gcl_GeniusSpocList?ID=" + pref.getEmpId() + "&SecurityCode=" + pref.getSecurityCode();
        Log.d("input", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        hideTypingIndicator();


                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");


                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                            JSONArray responseData = job1.optJSONArray("responseData");
                            if (responseData.length() > 0) {
                                hideTypingIndicator();
                                JSONObject obj = responseData.optJSONObject(0);
                                String Name = obj.optString("Name");
                                String Mobile = obj.optString("Mobile");
                                addBotMessage("No details found \n\nFor further query please contact with: " + Name + "\nContact Number: " + Mobile);

                            } else {
                                hideTypingIndicator();
                                addBotMessage("Please contact our toll-free helpline at \n\n7313604174 (available from 10:00 AM to 6:00 PM)");
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideTypingIndicator();

                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
    }



    private void addUploadButton() {
        messages.add(new BotMessageModule("UPLOAD_AADHAR", BotMessageModule.Type.BOT_BUTTON));
        adapter.notifyItemInserted(messages.size() - 1);
        recyclerChat.scrollToPosition(messages.size() - 1);
    }

    private void showImagePicker() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Upload Image");
        builder.setItems(new String[]{"Camera", "Gallery"}, (dialog, which) -> {
            if (which == 0) openCamera();
            else openGallery();
        });
        builder.show();
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQ_CAMERA);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQ_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK || data == null) return;



        if (requestCode == REQ_CAMERA) {
            bitmap = (Bitmap) data.getExtras().get("data");
        }

        if (requestCode == REQ_GALLERY) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (Exception e) {}
        }

        if (bitmap != null) {
            addImageBubble(bitmap);  // show inside chat
            runVisionOCR(bitmap);

            // extract data


        }


        if (requestCode == REQ_CAMERA_AADHAAR_BACK) {
            bitmapforAadharBack = (Bitmap) data.getExtras().get("data");
        }

        if (requestCode == REQ_GALLERY_AADHAAR_BACK) {
            Uri uri = data.getData();
            try {
                bitmapforAadharBack = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (Exception e) {}
        }

        if (bitmapforAadharBack != null) {
            addImageBubble(bitmapforAadharBack);
            aadharBackUpload();// show inside chat
            }


        if (requestCode == REQ_CAMERA_BANK_DOC) {
            bitmapforBank = (Bitmap) data.getExtras().get("data");
        }

        if (requestCode == REQ_GALLERY_BANK_DOC) {
            Uri uri = data.getData();
            try {
                bitmapforBank = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (Exception e) {}
        }

        if (bitmapforBank != null) {
            imgDoc.setImageBitmap(bitmapforBank);
        }


        //PAN

        if (requestCode == REQ_CAMERA_PAN) {
            bitmapPAN = (Bitmap) data.getExtras().get("data");
        }

        if (requestCode == REQ_GALLERY_PAN) {
            Uri uri = data.getData();
            try {
                bitmapPAN = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (Exception e) {}
        }

        if (bitmapPAN != null) {
            addImageBubble(bitmapPAN);  // show inside chat
            runVisionOCRForPAN(bitmapPAN);

            // extract data


        }



    }

    private void addImageBubble(Bitmap bitmap) {
        messages.add(new BotMessageModule(bitmap));
        adapter.notifyItemInserted(messages.size() - 1);
        recyclerChat.scrollToPosition(messages.size() - 1);
    }

    private void runVisionOCR(Bitmap bitmap) {

        TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if (!recognizer.isOperational()) {
            addBotMessage("OCR not supported on this device.");

            return;
        }

        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<TextBlock> items = recognizer.detect(frame);

        StringBuilder fullText = new StringBuilder();

        for (int i = 0; i < items.size(); i++) {
            TextBlock block = items.valueAt(i);
            fullText.append(block.getValue());
            fullText.append("\n");
        }

        extractAadhaarData(fullText.toString());
    }

    private void extractAadhaarData(String text) {

        String aadhar = extractAadhaarNumber(text);
        String name = extractNameFromAadhaar(text);
        String dob = extractDOB(text);
        String gender = extractGender(text);
        String address = extractAddressFromAadhaar(text);

        // ----------------------------
        //    AADHAAR VALIDATION
        // ----------------------------
        if (aadhar == null || aadhar.trim().length() < 12) {
            addBotMessage("❌ Not able to extract Aadhaar details.\nPlease upload a clearer image.");
            addUploadButton();
            return;
        }

        // If extracted, show nicely
        String result =

                        "🔢 Aadhaar: " + aadhar ;

        showAadhaarConfirmation(aadhar);
    }

    private String extractNameFromAadhaar(String text) {

        String[] lines = text.split("\n");

        for (String line : lines) {
            line = line.trim();

            // Must contain only letters & spaces
            if (!line.matches("^[A-Za-z ]+$")) continue;

            // Should NOT contain gender or DOB
            if (line.toLowerCase().contains("male") || line.toLowerCase().contains("female")) continue;
            if (line.matches(".*\\d.*")) continue;

            // Must be between 5 and 28 chars
            if (line.length() < 5 || line.length() > 28) continue;

            // Should have at least 2 words
            if (line.trim().split("\\s+").length < 2) continue;

            return line;
        }

        return "";
    }

    private String extractAddressFromAadhaar(String text) {

        String lower = text.toLowerCase();
        int index = lower.indexOf("address");

        if (index == -1) return "";

        // Grab text after the word "Address"
        String addr = text.substring(index + 7).trim();

        // Break into lines
        String[] parts = addr.split("\n");

        StringBuilder cleaned = new StringBuilder();

        for (String line : parts) {

            line = line.replace(":", "").trim();

            // Skip blank lines
            if (line.length() < 3) continue;

            // Skip gender/DOB lines
            if (line.toLowerCase().contains("male") || line.toLowerCase().contains("female")) continue;
            if (line.matches(".*\\d{2}/\\d{2}/\\d{4}.*")) continue;

            cleaned.append(line).append(", ");
        }

        // Remove last comma
        if (cleaned.length() > 2)
            cleaned.setLength(cleaned.length() - 2);

        return cleaned.toString();
    }

    private String extractAadhaarNumber(String text) {
        Pattern p = Pattern.compile("(\\d{4}[\\s-]?\\d{4}[\\s-]?\\d{4})");
        Matcher m = p.matcher(text.replace("\n", " "));
        return m.find() ? m.group(1).replace("-", " ") : "";
    }


    private String extractDOB(String text) {
        Pattern p = Pattern.compile("(\\d{2}[/-]\\d{2}[/-]\\d{4})");
        Matcher m = p.matcher(text);
        return m.find() ? m.group(1) : "";
    }

    private String extractGender(String text) {
        if (text.toLowerCase().contains("female")) return "Female";
        if (text.toLowerCase().contains("male")) return "Male";
        return "";
    }



    private void runVisionOCRForPAN(Bitmap bitmap) {

        TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if (!recognizer.isOperational()) {
            addBotMessage("OCR not supported on this device.");

            return;
        }

        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<TextBlock> items = recognizer.detect(frame);

        StringBuilder fullText = new StringBuilder();

        for (int i = 0; i < items.size(); i++) {
            TextBlock block = items.valueAt(i);
            fullText.append(block.getValue());
            fullText.append("\n");
        }

        extractPANData(fullText.toString());
    }

    private void extractPANData(String text) {

        String pan = extractPANNumber(text);

        // ----------------------------
        //    PAN VALIDATION
        // ----------------------------
        if (pan == null || pan.trim().length() != 10) {
            addBotMessage("❌ Unable to extract PAN details.\nPlease upload a clearer PAN card image.");
            return;
        }

        // Show result
        String result = "🪪 PAN: " + pan;

        showPANConfirmation(pan);
    }

    private void showPANConfirmation(String aadhar) {

        addBotMessage(
                "Your PAN Number: " + aadhar +
                        "\n\nIs this correct?" +
                        "\nPress 1️⃣ for YES" +
                        "\nPress 2️⃣ for NO"
        );

        // extractedAadhar = aadhar.replaceAll(" ","");
        PANnumber =aadhar ;

        awaitingPANConfirmation = true;
    }

    private String extractPANNumber(String text) {
        if (text == null) return null;

        // Normalize text
        String cleanText = text.toUpperCase().replaceAll("[^A-Z0-9]", "");

        // PAN format: 5 letters + 4 digits + 1 letter
        Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]");
        Matcher matcher = pattern.matcher(cleanText);

        if (matcher.find()) {
            return matcher.group(0);   // Extracted PAN number
        }
        return null;
    }

    private void showAadhaarConfirmation(String aadhar) {

        addBotMessage(
                "Your Aadhaar Number: " + aadhar +
                        "\n\nIs this correct?" +
                        "\nPress 1️⃣ for YES" +
                        "\nPress 2️⃣ for NO"
        );

       // extractedAadhar = aadhar.replaceAll(" ","");
        extractedAadhar ="901254115664" ;

        awaitingAadharConfirmation = true;
    }


    private void checkAadhaarNumber(JSONObject jsonObject,String aadhaarNumber) {
       showTypingIndicator();
       hideKeyboard();

        AndroidNetworking.post(AppData.KYC_GET_DETAILS)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideTypingIndicator();
                        try {

                            JSONObject job1 = response;
                            int Response_Code = job1.optInt("Response_Code");
                            String Response_Data = job1.optString("Response_Data");
                            //Log.e(TAG, "Response_Data: "+Response_Data);
                            if (Response_Code == 101) {
                                if (Response_Data != null) {
                                    JSONObject Response_Data_obj = new JSONObject(Response_Data);
                                    JSONArray jsonArray = Response_Data_obj.getJSONArray("AadharDetails");
                                    JSONObject job2 = jsonArray.getJSONObject(0);

                                    String AadhaarNumber = job2.getString("ReferenceNo");
                                    if (AadhaarNumber.equals(aadhaarNumber.trim())) {
                                        try {
                                            jsonObject.put("Id", aadhaarNumber.trim());
                                            checkAddahrDetails(jsonObject,aadhaarNumber);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        addBotMessage("Another Aadhaar number is linked to this ID. Kindly provide the accurate Aadhaar number.");
                                        addUploadButton();

                                    }
                                } else {
                                    JSONObject jsonObject = new JSONObject();
                                    try {
                                        jsonObject.put("AadharNo", aadhaarNumber);
                                        jsonObject.put("SecurityCode", pref.getSecurityCode());
                                        checkAadhaarNumberGeniusDB(jsonObject,aadhaarNumber);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            } else {
                                //TODO: No Data Found


                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("AadharNo", aadhaarNumber);
                                    jsonObject.put("SecurityCode", pref.getSecurityCode());
                                    checkAadhaarNumberGeniusDB(jsonObject,aadhaarNumber);
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
                        hideTypingIndicator();
                        addBotMessage("Unable very the details.Please try again");
                        addUploadButton();

                    }
                });
    }


    private void checkAddahrDetails(JSONObject jsonObject,String aadhaarNumber) {
       showTypingIndicator();
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
                       hideTypingIndicator();

                        int Response_Code = job1.optInt("Response_Code");
                        JSONObject Response_Data = job1.optJSONObject("Response_Data");
                        if (Response_Data != null) {
                            aadharflag = false;
                            JSONObject details = Response_Data.optJSONObject("details");
                            JSONObject name = details.optJSONObject("name");
                            String namevalue = name.optString("value");

                            //dob

                            JSONObject dob = details.optJSONObject("dob");
                            String dobvalue = dob.optString("value");
                            AppData.ADHARDOB = dobvalue;


                            //gender

                            JSONObject gender = details.optJSONObject("gender");
                            String gendervalue = gender.optString("value");


                            //address

                            JSONObject address = details.optJSONObject("address");
                            String careof = address.optString("careof").replace("S/O:", "").trim();
                            String state = address.optString("state");
                            String pin = address.optString("pin");
                            String street = address.optString("street");
                            String locality = address.optString("locality");
                            String house = address.optString("house");
                            String postoffice = address.optString("postoffice");
                            String subDistrict = address.optString("subDistrict");
                            String district = address.optString("district");
                            String vtc = address.optString("vtc");
                            String landmark = address.optString("landmark");


                            //image

                            AppData.AADAHARNUMBER = aadhaarNumber;
                            aadharFrontUpload();

                          // call Addahr front upload
                        } else {

                            aadharFrontUpload();


                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        hideTypingIndicator();
                        AppData.AADAHARNUMBER = aadhaarNumber;
                        addBotMessage("Unable very the details.Please try again");
                        addUploadButton();

                    }
                });
    }

    private void checkAadhaarNumberGeniusDB(JSONObject jsonObject,String aadhar) {
       showTypingIndicator();

        AndroidNetworking.post(AppData.CheckAadhar)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                       hideTypingIndicator();

                        JSONObject job1 = response;
                        int Response_Code = job1.optInt("Response_Code");
                        JSONArray Response_Data = job1.optJSONArray("Response_Data");
                        //Log.e(TAG, "Response_Data: "+Response_Data);
                        if (Response_Code == 101) {
                            if (Response_Data.length()>0) {
                                JSONObject obj=Response_Data.optJSONObject(0);
                                String EmployeeID=obj.optString("EmployeeID");
                                if (EmployeeID.equalsIgnoreCase(pref.getMasterId())){
                                    JSONObject jsonObject = new JSONObject();
                                    try {
                                        jsonObject.put("Id", aadhar.trim());
                                        checkAddahrDetails(jsonObject,aadhar);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }else {
                                    addBotMessage("The provided Aadhaar number is already linked to another ID. Kindly share the correct Aadhaar number.");
                                    addUploadButton();

                                }


                            } else {
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("Id", aadhar.trim());
                                    checkAddahrDetails(jsonObject,aadhar);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("Id", aadhar.trim());
                                checkAddahrDetails(jsonObject,aadhar);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        hideTypingIndicator();
                        addBotMessage("Unable to verify.Please try again");
                        addUploadButton();

                    }
                });
    }


    private void setQualification(JSONObject jsonObject) {
       showTypingIndicator();
        AndroidNetworking.post(AppData.COMMON_DDL)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject job1 = response;
                            hideTypingIndicator();
                            String Response_Code = job1.optString("Response_Code");
                            qualification.add("Please select");
                            mainQualification.add(new MainDocModule("",""));
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(Response_Data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.optJSONObject(i);
                                    String qualivalue = obj.optString("value");
                                    String qualiid = obj.optString("id");
                                    qualification.add(qualivalue);
                                    MainDocModule mainDocModule = new MainDocModule(qualiid, qualivalue);
                                    mainQualification.add(mainDocModule);
                                }


                                //setMartial();

                                JSONObject obj=new JSONObject();
                                try {
                                    obj.put("ddltype", 8);
                                    obj.put("SecurityCode",pref.getSecurityCode());
                                    setMarital(obj);
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
                        hideTypingIndicator();
                    }
                });
    }

    private void setMarital(JSONObject jsonObject) {
        showTypingIndicator();
        AndroidNetworking.post(AppData.COMMON_DDL)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            hideTypingIndicator();
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            martial.add("Please Select");
                            mainMartial.add(new MainDocModule("",""));
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(Response_Data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String qualivalue = obj.optString("value");
                                    String qualiid = obj.optString("id");
                                    martial.add(qualivalue);
                                    MainDocModule mainDocModule = new MainDocModule(qualiid, qualivalue);
                                    mainMartial.add(mainDocModule);
                                }

                                //setGender();

                                JSONObject obj=new JSONObject();
                                try {
                                    obj.put("ddltype", 10);
                                    obj.put("SecurityCode",pref.getSecurityCode());
                                    setGender(obj);
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
                        hideTypingIndicator();
                    }
                });
    }


    private void setGender(JSONObject jsonObject) {
        showTypingIndicator();

        AndroidNetworking.post(AppData.COMMON_DDL)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            hideTypingIndicator();
                            gender.clear();
                            mainGender.clear();
                            gender.add("Please select");
                            mainGender.add(new MainDocModule("",""));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(Response_Data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String value = obj.optString("value");
                                    String id = obj.optString("id");
                                    gender.add(value);
                                    MainDocModule mainDocModule = new MainDocModule(id, value);
                                    mainGender.add(mainDocModule);

                                }



                                JSONObject obj=new JSONObject();
                                try {
                                    obj.put("ddltype", 9);
                                    obj.put("SecurityCode",pref.getSecurityCode());
                                    setBlood(obj);
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
                        hideTypingIndicator();
                    }
                });
    }

    private void setRealation() {

        realation.add("Father");
        realation.add("Husband");
        mainRealation.add(new MainDocModule("REL00000001", "Father"));
        mainRealation.add(new MainDocModule("REL00000008", "Husband"));

        JSONObject obj=new JSONObject();
        try {
            obj.put("ddltype", "Doc_Aadhar");
            obj.put("SecurityCode",pref.getSecurityCode());
            getAddharFrontID(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }





        //setNomineeRelation();


    }

    private void setBlood(JSONObject jsonObject) {
        showTypingIndicator();
        AndroidNetworking.post(AppData.COMMON_DDL)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            hideTypingIndicator();
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            blood.add("Please select");
                            mainBlood.add(new MainDocModule("",""));
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(Response_Data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String value = obj.optString("value");
                                    String id = obj.optString("id");
                                    blood.add(value);
                                    MainDocModule mainDocModule = new MainDocModule(id, value);
                                    mainBlood.add(mainDocModule);

                                }

                                //setPerCity();

                                setRealation();

                                /*JSONObject obj=new JSONObject();
                                try {
                                    obj.put("ddltype", 4);
                                    obj.put("id1",presentstate);;
                                    obj.put("SecurityCode",pref.getSecurityCode());
                                    setPerCity(obj);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }*/
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        hideTypingIndicator();
                    }
                });
    }


    public void personalDialog() {
        androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(SelfOnboardingChatBotActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.personaldetails_popup, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llDOB = (LinearLayout) dialogView.findViewById(R.id.llDOB);
        TextView tvEmpCodeDOB=(TextView)dialogView.findViewById(R.id.tvEmpCodeDOB);
        EditText etGurdianName=dialogView.findViewById(R.id.etGurdianName);

        Spinner spRealation=(Spinner)dialogView.findViewById(R.id.spRealation);
        ArrayAdapter<String> RealationAdapter = new ArrayAdapter<String>
                (SelfOnboardingChatBotActivity.this, android.R.layout.simple_spinner_item,
                        realation); //selected item will look like a spinner set from XML
        RealationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRealation.setAdapter(RealationAdapter);


        Spinner spGender=dialogView.findViewById(R.id.spGender);
        ArrayAdapter<String> GenderAdapter = new ArrayAdapter<String>
                (SelfOnboardingChatBotActivity.this, android.R.layout.simple_spinner_item,
                        gender); //selected item will look like a spinner set from XML
        GenderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(GenderAdapter);


        Spinner spQualification=dialogView.findViewById(R.id.spQualification);
        ArrayAdapter<String> QualificationAdapter = new ArrayAdapter<String>
                (SelfOnboardingChatBotActivity.this, android.R.layout.simple_spinner_item,
                        qualification); //selected item will look like a spinner set from XML
        QualificationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spQualification.setAdapter(QualificationAdapter);


        Spinner spMartial=dialogView.findViewById(R.id.spMartial);
        ArrayAdapter<String> MartialAdapter = new ArrayAdapter<String>
                (SelfOnboardingChatBotActivity.this, android.R.layout.simple_spinner_item,
                        martial); //selected item will look like a spinner set from XML
        MartialAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMartial.setAdapter(MartialAdapter);


        Spinner spBloodGrp=dialogView.findViewById(R.id.spBloodGrp);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (SelfOnboardingChatBotActivity.this, android.R.layout.simple_spinner_item,
                        blood); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBloodGrp.setAdapter(spinnerArrayAdapter);

        llDOB.setOnClickListener(new View.OnClickListener() {
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


                final DatePickerDialog dialog = new DatePickerDialog(SelfOnboardingChatBotActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                        String DateOfBirth = d + " " + month + " " + y;
                        dob=DateOfBirth;


                        tvEmpCodeDOB.setText(DateOfBirth);



                    }
                }, dyear, dmonth, dday);
                dialog.getDatePicker().setMaxDate((long) (System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 365.25 * 18)));
                dialog.getDatePicker().setMinDate((long) (System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 365.25 * 80)));
                dialog.show();

            }
        });

        spRealation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                realationshipID = mainRealation.get(position).getDocumentType();
                realationship = realation.get(position);

                Log.d("realation", realationship);
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sexGenderID = mainGender.get(position).getDocID();
                sexGender = gender.get(position);
                Log.d("sexgender", sexGender);
                //spESICGender.setSelection(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spQualification.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                educationID = mainQualification.get(position).getDocumentType();
                education=qualification.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spMartial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!mainMartial.get(position).getDocID().isEmpty()){
                    martialstatusID = mainMartial.get(position).getDocID();
                    martialstatus=martial.get(position);
                    Log.d("martial", martialstatus);

                } else {
                    martialstatus = "";
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spBloodGrp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //bloodgrp = mainBlood.get(position).getDocID();
                bloodgrpID = mainBlood.get(position).getDocID();
                bloodgrp = blood.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Button btnSave=dialogView.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!dob.equals("")){
                    if (etGurdianName.getText().toString().length()>0){
                        if (!realationship.equals("")){
                            if (!sexGender.equals("")){
                                if (!education.equals("")){
                                    if (!martialstatus.equals("")){
                                        if (!bloodgrp.equals("")){
                                            personalDialog.dismiss();
                                            SpannableStringBuilder sb = new SpannableStringBuilder();

                                            int color = ContextCompat.getColor(SelfOnboardingChatBotActivity.this, R.color.misscolor);  // change to your color

// 1. Guardian Name
                                            sb.append("Father's/Husband's Name: ");
                                            sb.append("\n");
                                            int start = sb.length();

                                            sb.append(etGurdianName.getText().toString());
                                            sb.setSpan(new ForegroundColorSpan(color), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            sb.setSpan(new StyleSpan(Typeface.BOLD), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            sb.append("\n\n");

// 2. Relationship
                                            sb.append("Relationship with Guardian: ");
                                            sb.append("\n");
                                            start = sb.length();

                                            sb.append(realationship);
                                            sb.setSpan(new ForegroundColorSpan(color), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            sb.setSpan(new StyleSpan(Typeface.BOLD), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            sb.append("\n\n");

// 3. Gender
                                            sb.append("Gender: ");
                                            sb.append("\n");
                                            start = sb.length();

                                            sb.append(sexGender);
                                            sb.setSpan(new ForegroundColorSpan(color), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            sb.setSpan(new StyleSpan(Typeface.BOLD), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            sb.append("\n\n");

// 4. DOB
                                            sb.append("Date of Birth: ");
                                            sb.append("\n");
                                            start = sb.length();

                                            sb.append(dob);
                                            sb.setSpan(new ForegroundColorSpan(color), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            sb.setSpan(new StyleSpan(Typeface.BOLD), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            sb.append("\n\n");

// 5. Qualification
                                            sb.append("Highest Qualification: ");
                                            sb.append("\n");
                                            start = sb.length();

                                            sb.append(education);
                                            sb.setSpan(new ForegroundColorSpan(color), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            sb.setSpan(new StyleSpan(Typeface.BOLD), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            sb.append("\n\n");

// 6. Marital Status
                                            sb.append("Marital Status: ");
                                            sb.append("\n");
                                            start = sb.length();

                                            sb.append(martialstatus);
                                            sb.setSpan(new ForegroundColorSpan(color), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            sb.setSpan(new StyleSpan(Typeface.BOLD), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            sb.append("\n\n");

// 7. Blood Group
                                            sb.append("Blood Group: ");
                                            sb.append("\n");
                                            start = sb.length();

                                            sb.append(bloodgrp);
                                            sb.setSpan(new ForegroundColorSpan(color), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            sb.setSpan(new StyleSpan(Typeface.BOLD), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

// finally
                                            addUserMessage(String.valueOf(sb));

                                            JSONObject mainobject = new JSONObject();
                                            try {
                                                mainobject.put("DbOperation", "1");
                                                mainobject.put("SecurityCode", pref.getSecurityCode());
                                                JSONObject personalOBJ = new JSONObject();
                                                personalOBJ.put("AEMEMPLOYEEID", pref.getEmpId());
                                                personalOBJ.put("Sex", sexGenderID);
                                                personalOBJ.put("GuardianName", etGurdianName.getText().toString());
                                                personalOBJ.put("RelationShip", realationshipID);
                                                personalOBJ.put("BloodGroup", bloodgrpID);
                                                personalOBJ.put("DateOfBirth", dob);
                                                personalOBJ.put("Qualification", educationID);
                                                personalOBJ.put("UpdatedFrom", "ANDR");
                                                personalOBJ.put("MaritalStatus", martialstatusID);
                                                personalOBJ.put("EmployeeName", pref.getEmpName());
                                                mainobject.put("PersonalDetails", personalOBJ);

                                                uploadOfficalDetails(mainobject);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }




                                        }else {

                                            Toast.makeText(SelfOnboardingChatBotActivity.this, "Please select Blood Group", Toast.LENGTH_SHORT).show();


                                        }

                                    }else {
                                        Toast.makeText(SelfOnboardingChatBotActivity.this, "Please select Your Martial Status", Toast.LENGTH_SHORT).show();
                                    }

                                }else {
                                    Toast.makeText(SelfOnboardingChatBotActivity.this, "Please select Your Highest Qualification", Toast.LENGTH_SHORT).show();
                                }

                            }else {
                                Toast.makeText(SelfOnboardingChatBotActivity.this, "Please select Gender", Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            Toast.makeText(SelfOnboardingChatBotActivity.this, "Please select realationship with guardian", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        Toast.makeText(SelfOnboardingChatBotActivity.this, "Please enter your Father's/Husband's Name", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(SelfOnboardingChatBotActivity.this, "Please enter your Date of Birth", Toast.LENGTH_SHORT).show();
                }


            }
        });


        personalDialog = dialogBuilder.create();
        personalDialog.setCancelable(true);
        Window window = personalDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        personalDialog.show();
    }


    private void uploadOfficalDetails(JSONObject jsonObject) {
        showTypingIndicator();


        //AndroidNetworking.post("http://171.16.2.105/GSPPI_API_V2/api/KYC/UpdateKYCDetails")
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

                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {

                               hideTypingIndicator();
                               addBotMessage("Thank you! Your personal details have been updated successfully.");
                               addBotMessage("Step 2: To continue, please provide your contact details.");
                            //
                        } else {
                            hideTypingIndicator();

                        }
                    }

                    @Override
                    public void onError(ANError error) {

                        Intent intent = new Intent(SelfOnboardingChatBotActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });
    }

    private void getAddharFrontID(JSONObject jsonObject) {
        showTypingIndicator();
        AndroidNetworking.post(AppData.COMMON_DDL)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        hideTypingIndicator();
                        JSONObject job1 = response;
                        String Response_Code=job1.optString("Response_Code");
                        if (Response_Code.equals("101")){
                            JSONArray Response_Data=job1.optJSONArray("Response_Data");
                            for (int i=0;i<Response_Data.length();i++){
                                JSONObject obj=Response_Data.optJSONObject(i);
                                String id=obj.optString("id");
                                String value=obj.optString("value");
                                if (value.equals("Aadhaar Card")){
                                    frontID=id;
                                }
                                if (value.equals("Aadhar Card-Back")){
                                    backID=id;
                                }
                            }

                            JSONObject obj=new JSONObject();
                            try {
                                obj.put("ddltype", 3);
                                obj.put("SecurityCode",pref.getSecurityCode());
                                getState(obj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            /*JSONObject obj=new JSONObject();
                            try {
                                obj.put("ddltype", "Doc_Pan");
                                obj.put("SecurityCode",pref.getSecurityCode());
                                getPANID(obj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }*/
                        }


                    }

                    @Override
                    public void onError(ANError anError) {

                        hideTypingIndicator();
                    }
                });
    }


    private void aadharFrontUpload() {
        showTypingIndicator();
        saveBitmapAsync(bitmap, "myImage.jpg", new SaveCallback() {
            @Override
            public void onSuccess(File file) {
                imageFile=file;

            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
        AndroidNetworking.upload(AppData.SAVE_EMP_DIGITAL_DOCUMENT)
                .addMultipartParameter("AEMEmployeeID",pref.getEmpId())
                .addMultipartParameter("DocumentID", frontID)
                .addMultipartParameter("ReferenceNo", extractedAadhar)
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())
                .addMultipartFile("SingleFile", imageFile)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setPercentageThresholdForCancelling(60)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONObject job1 = response;
                        int Response_Code = job1.optInt("Response_Code");
                        String Response_Data = job1.optString("Response_Data");
                        if (Response_Code == 101) {
                            bitmap=null;
                            hideTypingIndicator();
                            addBotMessage("Great! I’ve received the front side of your Aadhaar card.");
                            addBotMessage("Kindly upload the Aadhaar back side as well.");
//                            addBotMessage("Thank you for confirming your Aadhaar details. We will proceed to the next step.");
//                            addBotMessage("Please fill up Personal Details you will need to provide \n1.Date of Birth \n2.Father's/Husband's Name\n3.Relationship\n4.Gender\n5.Highest Qualification\n6.Martial Status\n7.Blood Group");



                        } else {
                            hideTypingIndicator();
                            addBotMessage(Response_Data);

                        }
                    }

                    @Override
                    public void onError(ANError error) {
                       hideTypingIndicator();
                        Toast.makeText(getApplicationContext(), "Something went wrong,Please try again", Toast.LENGTH_LONG).show();
                    }
                });


    }


    private void saveBitmapAsync(Bitmap bitmap, String fileName, SaveCallback callback) {

        new Thread(() -> {
            File file = new File(getCacheDir(), fileName);

            try (FileOutputStream out = new FileOutputStream(file)) {

                // Compress in background — heavy task
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out);
                out.flush();

                runOnUiThread(() -> callback.onSuccess(file));

            } catch (Exception e) {
                runOnUiThread(() -> callback.onError(e));
            }

        }).start();
    }

    interface SaveCallback {
        void onSuccess(File file);
        void onError(Exception e);
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public void showImagePickerForAadharBack() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Upload Image");
        builder.setItems(new String[]{"Camera", "Gallery"}, (dialog, which) -> {
            if (which == 0) openCameraForAadharBack();
            else openGalleryForAadharBack();
        });
        builder.show();
    }

    private void openCameraForAadharBack() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQ_CAMERA_AADHAAR_BACK);
    }

    private void openGalleryForAadharBack() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQ_GALLERY_AADHAAR_BACK);
    }


    private void aadharBackUpload() {
        showTypingIndicator();
        saveBitmapAsync(bitmapforAadharBack, "myBackAadhaarImage.jpg", new SaveCallback() {
            @Override
            public void onSuccess(File file) {
                backAadhaarFile=file;

            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
        AndroidNetworking.upload(AppData.SAVE_EMP_DIGITAL_DOCUMENT)
                .addMultipartParameter("AEMEmployeeID",pref.getEmpId())
                .addMultipartParameter("DocumentID", backID)
                .addMultipartParameter("ReferenceNo", extractedAadhar)
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())
                .addMultipartFile("SingleFile", backAadhaarFile)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setPercentageThresholdForCancelling(60)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideTypingIndicator();
                        JSONObject job1 = response;
                        int Response_Code = job1.optInt("Response_Code");
                        String Response_Data = job1.optString("Response_Data");
                        if (Response_Code == 101) {
                            bitmapforAadharBack=null;
                            hideTypingIndicator();
                            addBotMessage("Thank you for confirming your Aadhaar details. We will proceed to the next step.");
                            addBotMessage("To continue, please enter your Personal Details: \n1.Date of Birth \n2.Father's/Husband's Name\n3.Relationship\n4.Gender\n5.Highest Qualification\n6.Martial Status\n7.Blood Group");
                        } else {
                            hideTypingIndicator();
                            addBotMessage(Response_Data);

                        }
                    }

                    @Override
                    public void onError(ANError error) {

                       hideTypingIndicator();
                    }
                });


        //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        /*AndroidNetworking.upload(AppData.url+"post_empdigitaldocument")
                .addMultipartParameter("AEMEmployeeID",pref.getEmpId())
                .addMultipartParameter("DocumentID", "00233")
                .addMultipartParameter("ReferenceNo", etAddaharNo.getText().toString())
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())
                .addMultipartFile("SingleFile", file)
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
                        String responseText = job1.optString("responseText");
                        boolean responseStatus = job1.optBoolean("responseStatus");

                        if (responseStatus) {
                            JSONObject jsonObject=new JSONObject();
                            try {
                                jsonObject.put("AEMEMPLOYEEID",pref.getEmpId());
                                jsonObject.put("Type",1);
                                jsonObject.put("Status",1);
                                panAadharvalidFlag(jsonObject,progressDialog);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            btnAadharSave.setVisibility(View.GONE);
                            responseflag=1;
                            Toast.makeText(getApplicationContext(), "Your Aadhaar details has been updated Successfully", Toast.LENGTH_LONG).show();

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();

                        }


                        // boolean _status = job1.getBoolean("status");



                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e("errt", String.valueOf(error));
                        progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(), "Something went wrong,Please try again", Toast.LENGTH_LONG).show();
                    }
                });*/

    }


    public void contactDialog() {
        androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(SelfOnboardingChatBotActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.contactdetails_popup, null);
        dialogBuilder.setView(dialogView);
        Spinner spPresentState = (Spinner) dialogView.findViewById(R.id.spPresentState);
        Spinner spPresentCity = (Spinner)  dialogView.findViewById(R.id.spPresentCity);
        Spinner spPermanentState = (Spinner)  dialogView.findViewById(R.id.spPermanentState);
        Spinner spPermanentCity = (Spinner)  dialogView.findViewById(R.id.spPermanentCity);

        EditText etPhnNumber = (EditText)  dialogView.findViewById(R.id.etPhnNumber);
        EditText etMobNumber = (EditText)  dialogView.findViewById(R.id.etMobNumber);
        EditText etEmailId = (EditText)  dialogView.findViewById(R.id.etEmail);
        EditText etWhatssappNumber = (EditText)  dialogView.findViewById(R.id.etWhatssappNumber);
        EditText etRefNumber = (EditText)  dialogView.findViewById(R.id.etRefNumber);
        etRefNumber.setVisibility(View.GONE);
        EditText etPrePinCode = (EditText)  dialogView.findViewById(R.id.etPrePinCode);
        EditText etPreAddr = (EditText) dialogView.findViewById(R.id.etPreAddr);
        EditText etPerPinCode = (EditText) dialogView.findViewById(R.id.etPerPinCode);
        EditText etPerAddr = (EditText) dialogView.findViewById(R.id.etPerAddr);


         txtPresentCity = (TextView)dialogView. findViewById(R.id.txtPresentCity);
         txtPermanentCity = (TextView) dialogView.findViewById(R.id.txtPermanentCity);


        txtPresentCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSearchCityDialog("present_city");
            }
        });

        txtPermanentCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSearchCityDialog("permanent_city");
            }
        });


        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (SelfOnboardingChatBotActivity.this, android.R.layout.simple_spinner_item,
                        state); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPresentState.setAdapter(spinnerArrayAdapter);
        spPermanentState.setAdapter(spinnerArrayAdapter);

        spPresentState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presentstateID = mainState.get(position).getDocID();
                presentstate=state.get(position);


                if (position > 0){
                    JSONObject obj=new JSONObject();
                    try {
                        obj.put("ddltype", 4);
                        obj.put("id1",presentstateID);
                        obj.put("SecurityCode",pref.getSecurityCode());
                        setPreCity(obj,spPresentCity);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spPermanentState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                permanentStateID = mainState.get(position).getDocID();
                permanentState=state.get(position);


                if (position > 0){
                    JSONObject obj=new JSONObject();
                    try {
                        obj.put("ddltype", 4);
                        obj.put("id1",permanentStateID);
                        obj.put("SecurityCode",pref.getSecurityCode());
                        setPerCity(obj,spPermanentCity);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spPermanentCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                permanentcityID = mainPerCity.get(position).getDocID();
                permanentcity=percity.get(position);




            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spPresentCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presentcityID = mainPreCity.get(position).getDocID();
                presentcity=precity.get(position);




            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button btnSave=(Button) dialogView.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etMobNumber.getText().toString().length()==10){
                    if (etWhatssappNumber.getText().toString().length()==10){
                        if (etPhnNumber.getText().toString().length()==10){
                            if (etEmailId.getText().toString().length()>0){
                                if (etPrePinCode.getText().toString().length()==6){
                                    if (!presentstateID.equals("")){
                                        if (!presentcityID.equals("")){
                                            if (etPreAddr.getText().toString().length()>3){
                                                if (!permanentStateID.equals("")){
                                                    if (!permanentcityID.equals("") ){
                                                        if (etPerAddr.getText().toString().length()>3 ){
                                                            contactDialog.dismiss();
                                                            SpannableStringBuilder sb = new SpannableStringBuilder();

                                                            int color = ContextCompat.getColor(SelfOnboardingChatBotActivity.this, R.color.misscolor);  // change to your color

// 1. Guardian Name
                                                            sb.append("Mobile Number: ");
                                                            sb.append("\n");
                                                            int start = sb.length();

                                                            sb.append(etMobNumber.getText().toString());
                                                            sb.setSpan(new ForegroundColorSpan(color), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                            sb.setSpan(new StyleSpan(Typeface.BOLD), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                            sb.append("\n\n");

// 2. Relationship
                                                            sb.append("Whatsapp Number: ");
                                                            sb.append("\n");
                                                            start = sb.length();

                                                            sb.append(etWhatssappNumber.getText().toString());
                                                            sb.setSpan(new ForegroundColorSpan(color), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                            sb.setSpan(new StyleSpan(Typeface.BOLD), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                            sb.append("\n\n");

// 3. Gender
                                                            sb.append("Emergency Number: ");
                                                            sb.append("\n");
                                                            start = sb.length();

                                                            sb.append(etPhnNumber.getText().toString());
                                                            sb.setSpan(new ForegroundColorSpan(color), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                            sb.setSpan(new StyleSpan(Typeface.BOLD), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                            sb.append("\n\n");

// 4. DOB
                                                            sb.append("Email ID: ");
                                                            sb.append("\n");
                                                            start = sb.length();

                                                            sb.append(etEmailId.getText().toString());
                                                            sb.setSpan(new ForegroundColorSpan(color), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                            sb.setSpan(new StyleSpan(Typeface.BOLD), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                            sb.append("\n\n");

// 5. Qualification
                                                            sb.append("Present PIN Code: ");
                                                            sb.append("\n");
                                                            start = sb.length();

                                                            sb.append(etPrePinCode.getText().toString());
                                                            sb.setSpan(new ForegroundColorSpan(color), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                            sb.setSpan(new StyleSpan(Typeface.BOLD), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                            sb.append("\n\n");

// 6. Marital Status
                                                            sb.append("Present State: ");
                                                            sb.append("\n");
                                                            start = sb.length();

                                                            sb.append(presentstate);
                                                            sb.setSpan(new ForegroundColorSpan(color), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                            sb.setSpan(new StyleSpan(Typeface.BOLD), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                            sb.append("\n\n");

// 7. Blood Group
                                                            sb.append("Present City: ");
                                                            sb.append("\n");
                                                            start = sb.length();

                                                            sb.append(presentcity);
                                                            sb.setSpan(new ForegroundColorSpan(color), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                            sb.setSpan(new StyleSpan(Typeface.BOLD), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                            sb.append("\n\n");


                                                            sb.append("Present Address: ");
                                                            sb.append("\n");
                                                            start = sb.length();

                                                            sb.append(etPreAddr.getText().toString());
                                                            sb.setSpan(new ForegroundColorSpan(color), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                            sb.setSpan(new StyleSpan(Typeface.BOLD), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                            sb.append("\n\n");


                                                            sb.append("Permanent PIN Code: ");
                                                            sb.append("\n");
                                                            start = sb.length();

                                                            sb.append(etPerPinCode.getText().toString());
                                                            sb.setSpan(new ForegroundColorSpan(color), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                            sb.setSpan(new StyleSpan(Typeface.BOLD), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                            sb.append("\n\n");

// 6. Marital Status
                                                            sb.append("Permanent State: ");
                                                            sb.append("\n");
                                                            start = sb.length();

                                                            sb.append(permanentState);
                                                            sb.setSpan(new ForegroundColorSpan(color), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                            sb.setSpan(new StyleSpan(Typeface.BOLD), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                            sb.append("\n\n");

// 7. Blood Group
                                                            sb.append("Permanent City: ");
                                                            sb.append("\n");
                                                            start = sb.length();

                                                            sb.append(permanentcity);
                                                            sb.setSpan(new ForegroundColorSpan(color), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                            sb.setSpan(new StyleSpan(Typeface.BOLD), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                            sb.append("\n\n");


                                                            sb.append("Permanent Address: ");
                                                            sb.append("\n");
                                                            start = sb.length();

                                                            sb.append(etPerAddr.getText().toString());
                                                            sb.setSpan(new ForegroundColorSpan(color), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                            sb.setSpan(new StyleSpan(Typeface.BOLD), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                            sb.append("\n\n");
// finally
                                                            addUserMessage(String.valueOf(sb));



                                                            //API CALL FOR CONTACT
                                                            JSONObject mainobject = new JSONObject();
                                                            try {
                                                                mainobject.put("DbOperation", "2");
                                                                mainobject.put("SecurityCode", pref.getSecurityCode());
                                                                JSONObject innerobj = new JSONObject();
                                                                innerobj.put("AEMEMPLOYEEID", pref.getEmpId());
                                                                innerobj.put("PermanentAddress", etPerAddr.getText().toString());
                                                                innerobj.put("PermanentStateID", permanentStateID);
                                                                innerobj.put("PermanentCityID", permanentcityID);
                                                                innerobj.put("PermanentPinCode", etPerPinCode.getText().toString());
                                                                innerobj.put("PresentAddress", etPreAddr.getText().toString());
                                                                innerobj.put("PresentStateID", presentstateID);
                                                                innerobj.put("PresentCityID", presentcityID);
                                                                innerobj.put("PresentPincode", etPrePinCode.getText().toString());
                                                                innerobj.put("Phone", etWhatssappNumber.getText().toString());
                                                                innerobj.put("Mobile", etMobNumber.getText().toString());
                                                                innerobj.put("EmergencyContact", etPhnNumber.getText().toString());
                                                                innerobj.put("EmailID", etEmailId.getText().toString());
                                                                innerobj.put("RefContact", "9090909090");
                                                                mainobject.put("ContactDetails", innerobj);
                                                                uploadContactDetails(mainobject);
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }

                                                        }else {
                                                            Toast.makeText(SelfOnboardingChatBotActivity.this, "Please enter permanent address", Toast.LENGTH_SHORT).show();
                                                        }

                                                    }else {
                                                        Toast.makeText(SelfOnboardingChatBotActivity.this, "Please select permanent city", Toast.LENGTH_SHORT).show();
                                                    }

                                                }else {
                                                    Toast.makeText(SelfOnboardingChatBotActivity.this, "Please select permanent state", Toast.LENGTH_SHORT).show();
                                                }

                                            }else {
                                                Toast.makeText(SelfOnboardingChatBotActivity.this, "Please enter present address", Toast.LENGTH_SHORT).show();
                                            }

                                        }else {
                                            Toast.makeText(SelfOnboardingChatBotActivity.this, "Please select present city", Toast.LENGTH_SHORT).show();

                                        }

                                    }else {
                                        Toast.makeText(SelfOnboardingChatBotActivity.this, "Please select present state", Toast.LENGTH_SHORT).show();

                                    }

                                }else {
                                    Toast.makeText(SelfOnboardingChatBotActivity.this, "Please enter valid present address pincode", Toast.LENGTH_SHORT).show();

                                }


                            }else {
                                Toast.makeText(SelfOnboardingChatBotActivity.this, "Please enter email id", Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            Toast.makeText(SelfOnboardingChatBotActivity.this, "Please enter emergency number", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        Toast.makeText(SelfOnboardingChatBotActivity.this, "Please enter whatsapp number", Toast.LENGTH_SHORT).show();
                    }

                }else {
                        Toast.makeText(SelfOnboardingChatBotActivity.this, "Please enter valid mobile number", Toast.LENGTH_SHORT).show();
                }

            }
        });

        CheckBox ckMobileSame=(CheckBox)dialogView.findViewById(R.id.ckMobileSame);
        ckMobileSame.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    etWhatssappNumber.setText(etMobNumber.getText().toString());
                }else{
                    etWhatssappNumber.setText("");
                }
            }
        });

        CheckBox ckAddressSame=(CheckBox)dialogView.findViewById(R.id.ckAddressSame);
        ckAddressSame.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    etPerAddr.setText(etPreAddr.getText().toString());
                    etPerPinCode.setText(etPrePinCode.getText().toString());
                    int indexState=state.indexOf(presentstate);
                    spPermanentState.setSelection(indexState);
                    int indexCity=state.indexOf(presentcity);
                    spPermanentCity.setSelection(indexCity);
                }
            }
        });






        contactDialog = dialogBuilder.create();
        contactDialog.setCancelable(true);
        Window window = contactDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        contactDialog.show();
    }


    private void getState(JSONObject jsonObject) {
        showTypingIndicator();
        AndroidNetworking.post(AppData.COMMON_DDL)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject bankobj=new JSONObject();
                            try {
                                bankobj.put("ddltype", 5);
                                bankobj.put("id1",pref.getEmpConId());;
                                bankobj.put("SecurityCode",pref.getSecurityCode());
                                setBank(bankobj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            hideTypingIndicator();
                            mainState.clear();
                            state.clear();
                            state.add("Please Select");
                            mainState.add(new MainDocModule("",""));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(Response_Data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String deptvalue = obj.optString("value");
                                    String id = obj.optString("id");
                                    state.add(deptvalue);
                                    MainDocModule mainDocModule = new MainDocModule(id, deptvalue);
                                    mainState.add(mainDocModule);
                                    // clientname.add(value);
                                }

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        hideTypingIndicator();
                    }
                });
    }

    private void setPerCity(JSONObject jsonObject,Spinner sp) {

        ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(AppData.COMMON_DDL)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            //llLoader.setVisibility(View.GONE);
                            //llMain.setVisibility(View.VISIBLE);
                            pd.dismiss();
                            percity.clear();
                            mainPerCity.clear();
                            percity.add("Please Select");
                            mainPerCity.add(new MainDocModule("", ""));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(Response_Data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String qualivalue = obj.optString("value");
                                    String qualiid = obj.optString("id");
                                    percity.add(qualivalue);
                                    MainDocModule mainDocModule = new MainDocModule(qualiid, qualivalue);
                                    mainPerCity.add(mainDocModule);
                                }

                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (SelfOnboardingChatBotActivity.this, android.R.layout.simple_spinner_item,
                                                percity); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                sp.setAdapter(spinnerArrayAdapter);






                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        pd.dismiss();

                    }
                });
    }

    private void setPreCity(JSONObject jsonObject,Spinner sp) {

        ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(AppData.COMMON_DDL)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            //llLoader.setVisibility(View.GONE);
                            //llMain.setVisibility(View.VISIBLE);
                            pd.dismiss();
                            precity.clear();
                            mainPreCity.clear();
                            precity.add("Please Select");
                            mainPreCity.add(new MainDocModule("", ""));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(Response_Data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String qualivalue = obj.optString("value");
                                    String qualiid = obj.optString("id");
                                    precity.add(qualivalue);
                                    MainDocModule mainDocModule = new MainDocModule(qualiid, qualivalue);
                                    mainPreCity.add(mainDocModule);
                                }

                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (SelfOnboardingChatBotActivity.this, android.R.layout.simple_spinner_item,
                                                precity); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                sp.setAdapter(spinnerArrayAdapter);





                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        pd.dismiss();

                    }
                });
    }


    private void openSearchCityDialog(String from) {
        searchHolidayDialog.setContentView(R.layout.wbs_code_search_layout);
        searchHolidayDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        searchHolidayDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        searchHolidayDialog.setCancelable(true);

        TextView txtPopupHeadline = searchHolidayDialog.findViewById(R.id.txtPopupHeadline);
        SearchView wbsCodeSearchView = (SearchView) searchHolidayDialog.findViewById(R.id.wbsCodeSearchView);
        ImageView imgCancel = searchHolidayDialog.findViewById(R.id.imgCancel);
        RecyclerView rvWbsCode = searchHolidayDialog.findViewById(R.id.rvWbsCode);
        rvWbsCode.setLayoutManager(new LinearLayoutManager(SelfOnboardingChatBotActivity.this));
        TempCommonFilterForSelfOnboardingAdapter tempCommonFilterAdapter;
        if (from.equals("present_city")){
            wbsCodeSearchView.setQueryHint("Search Present city");
            txtPopupHeadline.setText("Select Present City");

            ArrayList<MainDocModule>  mainPreCityCopy = (ArrayList<MainDocModule>) mainPreCity.clone();
            tempCommonFilterAdapter = new TempCommonFilterForSelfOnboardingAdapter(SelfOnboardingChatBotActivity.this,mainPreCityCopy,from);
            rvWbsCode.setAdapter(tempCommonFilterAdapter);
        } else {
            wbsCodeSearchView.setQueryHint("Search Permanent city");
            txtPopupHeadline.setText("Select Permanent City");

            ArrayList<MainDocModule>  mainPerCityCopy = (ArrayList<MainDocModule>) mainPerCity.clone();
            tempCommonFilterAdapter = new TempCommonFilterForSelfOnboardingAdapter(SelfOnboardingChatBotActivity.this,mainPerCityCopy,from);
            rvWbsCode.setAdapter(tempCommonFilterAdapter);
        }


        wbsCodeSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                tempCommonFilterAdapter.getFilter().filter(s);
                return false;
            }
        });


        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchHolidayDialog.dismiss();
            }
        });
        searchHolidayDialog.show();
    }

    public void setText(String cityID,String selectedItem, String selectFor){
        if (selectFor.equals("present_city")){

            txtPresentCity.setText(selectedItem);
            presentcity = cityID;
        } else  {

            txtPermanentCity.setText(selectedItem);
            permanentcity = cityID;
        }

        searchHolidayDialog.dismiss();
    }

    private void uploadContactDetails(JSONObject jsonObject) {
       showTypingIndicator();
        //AndroidNetworking.post("http://171.16.2.105/GSPPI_API_V2/api/KYC/UpdateKYCDetails")
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


                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            hideTypingIndicator();
                            addBotMessage("Thank you! Your contact details have been updated successfully.");
                            addBotMessage("Step 3: To continue, please provide your bank details.");

                            // Toast.makeText(TempProfileActivity.this, "Contact Details has been updated Successfully", Toast.LENGTH_LONG).show();

                        } else {
                            hideTypingIndicator();
                        }
                    }

                    @Override
                    public void onError(ANError error) {

                        hideTypingIndicator();

                    }
                });
    }


    private void setBank(JSONObject jsonObject) {
        showTypingIndicator();
        AndroidNetworking.post(AppData.COMMON_DDL)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            hideTypingIndicator();
                            bankName.clear();
                            mainBankName.clear();
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(Response_Data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String qualivalue = obj.optString("value");
                                    String qualiid = obj.optString("id");
                                    bankName.add(qualivalue);
                                    MainDocModule mainDocModule = new MainDocModule(qualiid, qualivalue);
                                    mainBankName.add(mainDocModule);

                                }

                                //setBankDocType();
                                JSONObject obj=new JSONObject();
                                try {
                                    obj.put("ddltype", 11);
                                    obj.put("id1",pref.getEmpConId());;
                                    obj.put("SecurityCode",pref.getSecurityCode());
                                    setBankDocType(obj);
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
                        hideTypingIndicator();
                    }
                });
    }

    private void setBankDocType(JSONObject jsonObject) {
        showTypingIndicator();
        AndroidNetworking.post(AppData.COMMON_DDL)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject PANobj=new JSONObject();
                            try {
                                PANobj.put("ddltype", "Doc_Pan");
                                PANobj.put("SecurityCode",pref.getSecurityCode());
                                getPANID(PANobj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            hideTypingIndicator();
                            doctype.clear();
                            mainDocType.clear();
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            if (Response_Code.equals("101")) {
                                String Response_Data = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(Response_Data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String qualivalue = obj.optString("value");
                                    String qualiid = obj.optString("id");
                                    doctype.add(qualivalue);
                                    MainDocModule mainDocModule = new MainDocModule(qualiid, qualivalue);
                                    mainDocType.add(mainDocModule);

                                }

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        hideTypingIndicator();
                    }
                });
    }


    public void bankDialog() {
        androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(SelfOnboardingChatBotActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.bankdetails_popup, null);
        dialogBuilder.setView(dialogView);
        Spinner spBankName = (Spinner) dialogView.findViewById(R.id.spBankName);
        Spinner spDocType = (Spinner) dialogView.findViewById(R.id.spDocType);
        EditText etAccNumber = (EditText) dialogView.findViewById(R.id.etAccNumber);
        EditText etIFSC = (EditText) dialogView.findViewById(R.id.etIFSC);
        EditText etFName = (EditText)dialogView. findViewById(R.id.etFName);
        EditText etLName = (EditText)dialogView. findViewById(R.id.etLName);
        ImageView imgCamera = (ImageView)dialogView. findViewById(R.id.imgCamera);
        imgDoc = (ImageView) dialogView.findViewById(R.id.imgDoc);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (SelfOnboardingChatBotActivity.this, android.R.layout.simple_spinner_item,
                        bankName); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBankName.setAdapter(spinnerArrayAdapter);

        ArrayAdapter<String> spinnerDocArrayAdapter = new ArrayAdapter<String>
                (SelfOnboardingChatBotActivity.this, android.R.layout.simple_spinner_item, doctype); //selected item will look like a spinner set from XML
        spinnerDocArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDocType.setAdapter(spinnerDocArrayAdapter);


        spBankName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                banknameID = mainBankName.get(position).getDocID();
                bankname = bankName.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spDocType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bankdocid = mainDocType.get(position).getDocID();
                bankdoc= doctype.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePickerForBankDoc();
            }
        });
        Button btnSave=dialogView.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!bankname.isEmpty()) {
                    if (etAccNumber.getText().toString().length() > 0) {
                        if (bitmapforBank != null) {
                            if (etIFSC.getText().toString().length() == 11) {
                                if (etFName.getText().toString().length() > 0) {
                                    if (etLName.getText().toString().length() > 0) {
                                        bankDialog.dismiss();
                                        SpannableStringBuilder sb = new SpannableStringBuilder();

                                        int color = ContextCompat.getColor(SelfOnboardingChatBotActivity.this, R.color.misscolor);  // change to your color

// 1. Guardian Name
                                        sb.append("Bank Name: ");
                                        sb.append("\n");
                                        int start = sb.length();

                                        sb.append(bankname);
                                        sb.setSpan(new ForegroundColorSpan(color), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        sb.setSpan(new StyleSpan(Typeface.BOLD), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        sb.append("\n\n");

// 2. Relationship
                                        sb.append("Account Number: ");
                                        sb.append("\n");
                                        start = sb.length();

                                        sb.append(etAccNumber.getText().toString());
                                        sb.setSpan(new ForegroundColorSpan(color), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        sb.setSpan(new StyleSpan(Typeface.BOLD), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        sb.append("\n\n");

// 3. Gender
                                        sb.append("Bank Document: ");
                                        sb.append("\n");
                                        start = sb.length();

                                        sb.append(bankdoc);
                                        sb.setSpan(new ForegroundColorSpan(color), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        sb.setSpan(new StyleSpan(Typeface.BOLD), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        sb.append("\n\n");

// 4. DOB
                                        sb.append("IFSC Code: ");
                                        sb.append("\n");
                                        start = sb.length();

                                        sb.append(etIFSC.getText().toString());
                                        sb.setSpan(new ForegroundColorSpan(color), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        sb.setSpan(new StyleSpan(Typeface.BOLD), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        sb.append("\n\n");

// 5. Qualification
                                        sb.append("First Name as Per Bank: ");
                                        sb.append("\n");
                                        start = sb.length();

                                        sb.append(etFName.getText().toString());
                                        sb.setSpan(new ForegroundColorSpan(color), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        sb.setSpan(new StyleSpan(Typeface.BOLD), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        sb.append("\n\n");

// 6. Marital Status
                                        sb.append("Last Name as Per Bank: ");
                                        sb.append("\n");
                                        start = sb.length();

                                        sb.append(etLName.getText().toString());
                                        sb.setSpan(new ForegroundColorSpan(color), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        sb.setSpan(new StyleSpan(Typeface.BOLD), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        sb.append("\n\n");


                                        addUserMessage(String.valueOf(sb));
                                        BankDetailsSubmit(etAccNumber.getText().toString(),etFName.getText().toString(),etLName.getText().toString(),etIFSC.getText().toString());



                                    } else {
                                        Toast.makeText(getApplicationContext(), "Please enter Last Name as per Bank ", Toast.LENGTH_LONG).show();

                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Please enter First Name as per Bank", Toast.LENGTH_LONG).show();

                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Please enter 11 digits IFSC code", Toast.LENGTH_LONG).show();

                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Please upload Bank Document", Toast.LENGTH_LONG).show();
                            //llDocumentType.setBackgroundResource(R.drawable.lldesign_error);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter Account Number", Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please Select Bank Name", Toast.LENGTH_LONG).show();

                }
            }
        });


        bankDialog = dialogBuilder.create();
        bankDialog.setCancelable(true);
        Window window = bankDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        bankDialog.show();
    }

    public void showImagePickerForBankDoc() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Upload Image");
        builder.setItems(new String[]{"Camera", "Gallery"}, (dialog, which) -> {
            if (which == 0) openCameraBankDoc();
            else openGalleryBankDoc();
        });
        builder.show();
    }

    private void openCameraBankDoc() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQ_CAMERA_BANK_DOC);
    }

    private void openGalleryBankDoc() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQ_GALLERY_BANK_DOC);
    }


    private void BankDetailsSubmit(String accNumber,String fName,String lName,String Ifsc ) {
        showTypingIndicator();
        saveBitmapAsync(bitmapforBank, "bankDocImage.jpg", new SaveCallback() {
            @Override
            public void onSuccess(File file) {
                bankFile=file;

            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });


        String accnumbet = accNumber;
        String fname = fName;
        String lname = lName;
        String ifsc = Ifsc;
        String masterid = pref.getMasterId();
        String AEMEmployeeID = pref.getEmpId();

        AndroidNetworking.upload(AppData.SAVE_DUMMY_EMP_BANK_DOCUMENT)
                .addMultipartParameter("AEMEmployeeID",pref.getEmpId())
                .addMultipartParameter("FirstNameAsperBank",fname)
                .addMultipartParameter("LastNameAsperBank",lname)
                .addMultipartParameter("BankName",banknameID)
                .addMultipartParameter("AccountNumber",accnumbet)
                .addMultipartParameter("IFSCode",ifsc)
                .addMultipartParameter("SecurityCode",pref.getSecurityCode())
                .addMultipartParameter("DocumentID",bankdocid)
                .addMultipartFile("SingleFile", bankFile)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setPercentageThresholdForCancelling(60)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONObject job1 = response;
                        int Response_Code = job1.optInt("Response_Code");
                        String Response_Data = job1.optString("Response_Data");
                        if (Response_Code == 101) {
                            bitmapforBank = null;
                            hideTypingIndicator();
                            addBotMessage("Awesome! Your bank details are updated.");
                            addBotMessage("Step 4: Please upload your PAN card image.");

                        } else {
                            hideTypingIndicator();
                            addBotMessage(Response_Data);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        hideTypingIndicator();
                    }
                });



    }


    public void showImagePickerForPAN() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Upload Image");
        builder.setItems(new String[]{"Camera", "Gallery"}, (dialog, which) -> {
            if (which == 0) openCameraPAN();
            else openGalleryPAN();
        });
        builder.show();
    }

    private void openCameraPAN() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQ_CAMERA_PAN);
    }

    private void openGalleryPAN() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQ_GALLERY_PAN);
    }


    private void getPANID(JSONObject jsonObject) {
        showTypingIndicator();
        AndroidNetworking.post(AppData.COMMON_DDL)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideTypingIndicator();
                        JSONObject job1 = response;
                        addBotMessage("Hi! I'm Genie🤖\n I'm here to guide you through your self-onboarding process.\n\nTo complete your onboarding, I’ll need the following information:\n\n1.Personal Information.\n2.Contact Details\n3.Bank Details\n4.Aadhaar Card Image (Front and Back) \n5.PAN Card Image. \n\nPlease upload your Aadhaar Card Front Image to continue.");
                        addUploadButton();
                        String Response_Code=job1.optString("Response_Code");
                        if (Response_Code.equals("101")){
                            JSONArray Response_Data=job1.optJSONArray("Response_Data");
                            for (int i=0;i<Response_Data.length();i++){
                                JSONObject obj=Response_Data.optJSONObject(i);
                                panID=obj.optString("id");


                            }


                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        hideTypingIndicator();
                    }
                });
    }

    private void panUpload() {
        showTypingIndicator();
        saveBitmapAsync(bitmapPAN, "panDocImage.jpg", new SaveCallback() {
            @Override
            public void onSuccess(File file) {
                panFile=file;

            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
        AndroidNetworking.upload(AppData.SAVE_EMP_DIGITAL_DOCUMENT)
                .addMultipartParameter("AEMEmployeeID",pref.getEmpId())
                .addMultipartParameter("DocumentID", panID)
                .addMultipartParameter("ReferenceNo", PANnumber)
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())
                .addMultipartFile("SingleFile", panFile)
                .addHeaders("Authorization", "Bearer " + pref.getAccessToken())
                .setPercentageThresholdForCancelling(60)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONObject job1 = response;
                        int Response_Code = job1.optInt("Response_Code");
                        String Response_Data = job1.optString("Response_Data");
                        if (Response_Code == 101) {
                            bitmapPAN=null;
                            hideTypingIndicator();

                            addBotMessage("Great! Your PAN details have been verified and updated.");
                            //give Success message of self onboarding
                            addBotMessage("Congratulations! You have successfully completed the self-onboarding process.");
                            addBotMessage("Type “exit” to leave the chat.");


                        } else {
                            hideTypingIndicator();
                            Toast.makeText(getApplicationContext(), Response_Data, Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e("errt", String.valueOf(error));
                        hideTypingIndicator();
                        Toast.makeText(getApplicationContext(), "Something went wrong,Please try again", Toast.LENGTH_LONG).show();
                    }
                });


    }







}