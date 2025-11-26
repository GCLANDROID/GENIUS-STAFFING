package io.cordova.myapp00d753.activity;

import static java.util.Calendar.DAY_OF_MONTH;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.attendance.AttendanceReportActivity;
import io.cordova.myapp00d753.adapter.SelfOnboardingChatbotMessageAdapter;
import io.cordova.myapp00d753.bluedart.BlueDartAttendanceManageActivity;
import io.cordova.myapp00d753.module.BotMessageModule;
import io.cordova.myapp00d753.module.MainDocModule;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

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
    String month;

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

        addBotMessage("Hi! I'm Genie🤖\n I'm here to assist you with your self-onboarding process. \n\nPlease upload your Aadhar card image to proceed further.");
        addUploadButton();


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
            addBotMessage("Hi! I'm Genie🤖\n I'm here to assist you with your self-onboarding process. \n\nPlease upload your Aadhar card image to proceed further.");
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
                addBotMessage("Okay, please upload your Aadhaar again.");
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

    private void getUANNumber(JSONObject jsonObject) {
        showTypingIndicator();
        AndroidNetworking.post(AppData.GCL_KYC)
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
                            String Response_Code = job1.optString("Response_Code");
                            if (Response_Code.equals("101")) {
                                hideTypingIndicator();
                                String Response_Data = job1.optString("Response_Data");

                                JSONArray jsonArray = new JSONArray(Response_Data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);


                                    String UanNo = obj.optString("UANNumber");
                                    addBotMessage("Your UAN Number is: " + UanNo);

                                }


                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        hideTypingIndicator();
                        addBotMessage("Error fetching UAN details. Please try again later ❌");


                    }
                });
    }

    private void getSalaryList(JSONObject jsonObject) {
        showTypingIndicator();
        AndroidNetworking.post(AppData.GET_EMPLOYEE_SALARY)
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
                            String Response_Code = job1.optString("Response_Code");
                            if (Response_Code.equals("101")) {
                                hideTypingIndicator();
                                String Response_Data = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(Response_Data);

                                JSONObject obj = jsonArray.getJSONObject(0);

                                String url = obj.optString("url");
                                Log.d("SalarySlipURL", url);
                                String ctcUrl = url; // your real URL
                                String maskedText = "<a href=\"" + url + "\">Click to View</a>";
                                addBotMessage("Here’s your Last Month Salary Slip 📑\n" + maskedText);


                            } else {
                                hideTypingIndicator();
                                addBotMessage("No Salary details found ❌");
                                //Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            hideTypingIndicator();
                            addBotMessage("Error fetching Salary details. Please try again later ❌");

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        hideTypingIndicator();
                        addBotMessage("Error fetching Salary details. Please try again later ❌");


                    }
                });
    }

    private void getDocList(JSONObject jsonObject) {
        showTypingIndicator();
        AndroidNetworking.post(AppData.EMPLOYEE_DOCUMENT_MANAGE)
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
                            String Response_Code = job1.optString("Response_Code");
                            if (Response_Code.equals("101")) {

                                String Response_Data = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(Response_Data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String DocumentName = obj.optString("DocumentName");
                                    String DocLink = obj.optString("Mobilelink");
                                    if (DocumentName.equalsIgnoreCase("Esi Card")) {
                                        hideTypingIndicator();
                                        esiCardFound = true;
                                        String maskedText = "<a href=\"" + DocLink + "\">Click to View</a>";
                                        addBotMessage("Here’s your ESI Card 📑\n" + maskedText);
                                        break;

                                    }
                                }

                                if (!esiCardFound) {
                                    getSpokePersonList();
                                }

                            } else {
                                hideTypingIndicator();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        hideTypingIndicator();
                        addBotMessage("Error fetching ESI Card details. Please try again later ❌");

                    }
                });
    }

    private void getMedicalCard(JSONObject jsonObject) {
        showTypingIndicator();
        AndroidNetworking.post(AppData.EMPLOYEE_DOCUMENT_MANAGE)
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
                            String Response_Code = job1.optString("Response_Code");
                            if (Response_Code.equals("101")) {

                                String Response_Data = job1.optString("Response_Data");
                                JSONArray jsonArray = new JSONArray(Response_Data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String DocumentName = obj.optString("DocumentName");
                                    String DocLink = obj.optString("Mobilelink");
                                    if (DocumentName.equalsIgnoreCase("Medical Card")) {
                                        hideTypingIndicator();
                                        medicalCardFound = true;
                                        String maskedText = "<a href=\"" + DocLink + "\">Click to View</a>";
                                        addBotMessage("Here’s your Medical Card 📑\n" + maskedText);
                                        break;

                                    }
                                }

                                if (!medicalCardFound) {
                                    getSpokePersonList();
                                }

                            } else {
                                hideTypingIndicator();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        hideTypingIndicator();
                        addBotMessage("Error fetching ESI Card details. Please try again later ❌");

                    }
                });
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
        builder.setTitle("Upload Aadhar Image");
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

        Bitmap bitmap = null;

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

                            addBotMessage("Thank you for confirming your Aadhaar details. We will proceed to the next step.");
                            addBotMessage("Please enter your Personal Details. \n\nFor fill up Personal Details you will need to provide \n1.Date of Birth \n2.Father's/Husband's Name\n3.Relationship\n4.Gender\n5.Highest Qualification\n6.Martial Status\n7.Blood Group");

                            // adharAlert(namevalue, dobvalue, AppData.AADAHARNUMBER, gendervalue, careof, state, pin, street, locality, house, postoffice, subDistrict, district, vtc, landmark, null, 1);

                        } else {
                            AppData.AADAHARNUMBER = aadhaarNumber;
                            addBotMessage("Thank you for confirming your Aadhaar details. We will proceed to the next step.");
                            addBotMessage("Please enter your Personal Details. \n\nFor fill up Personal Details you will need to provide \n1.Date of Birth \n2.Father's/Husband's Name\n3.Relationship\n4.Gender\n5.Highest Qualification\n6.Martial Status\n7.Blood Group");


                            /*Intent intent = new Intent(TEMPAadharQRActivity.this, TempProfileActivity.class);
                            intent.putExtra("namevalue", "");
                            intent.putExtra("dobvalue", "");
                            intent.putExtra("gendervalue", "");
                            intent.putExtra("careof", "");
                            intent.putExtra("state", "");
                            intent.putExtra("pin", "");
                            intent.putExtra("street", "");
                            intent.putExtra("locality","");
                            intent.putExtra("house", "");
                            intent.putExtra("postoffice", "");
                            intent.putExtra("subDistrict",  "");
                            intent.putExtra("district", "");
                            intent.putExtra("vtc", "");
                            intent.putExtra("landmark", "");
                            intent.putExtra("aadhaarflag", aadharflag);
                            startActivity(intent);
                            finish();*/

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

                            JSONObject job1 = response;
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
                      pd.dismiss();
                    }
                });
    }

    private void setMarital(JSONObject jsonObject) {
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
                       pd.dismiss();
                    }
                });
    }


    private void setGender(JSONObject jsonObject) {
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
                       pd.dismiss();
                    }
                });
    }

    private void setRealation() {

        realation.add("Father");
        realation.add("Husband");
        mainRealation.add(new MainDocModule("REL00000001", "Father"));
        mainRealation.add(new MainDocModule("REL00000008", "Husband"));







        //setNomineeRelation();


    }

    private void setBlood(JSONObject jsonObject) {
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
                            pd.dismiss();

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

                       pd.dismiss();
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


                        tvEmpCodeDOB.setText(DateOfBirth);



                    }
                }, dyear, dmonth, dday);
                dialog.getDatePicker().setMaxDate((long) (System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 365.25 * 18)));
                dialog.getDatePicker().setMinDate((long) (System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 365.25 * 80)));
                dialog.show();

            }
        });


        personalDialog = dialogBuilder.create();
        personalDialog.setCancelable(true);
        Window window = personalDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        personalDialog.show();
    }









}