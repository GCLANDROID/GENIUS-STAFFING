package io.cordova.myapp00d753.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.ChatbotMessageAdapter;
import io.cordova.myapp00d753.adapter.DocumentAdapter;
import io.cordova.myapp00d753.module.BotMessageModule;
import io.cordova.myapp00d753.module.DocumentManageModule;
import io.cordova.myapp00d753.module.SalaryModule;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

public class ChatBotNewActivity extends AppCompatActivity {
    private RecyclerView recyclerChat;
    private EditText etMessage;
    private ImageView btnSend;
    private ChatbotMessageAdapter adapter;
    private List<BotMessageModule> messages = new ArrayList<>();

    private enum MenuState {MAIN, PAYROLL, PF, DOCUMENT}

    private MenuState currentMenu = MenuState.MAIN;
    Pref pref;
    int y;
    String year;
    boolean esiCardFound, medicalCardFound;
    ImageView imgMic;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot_new);
        pref = new Pref(ChatBotNewActivity.this);
        y = Calendar.getInstance().get(Calendar.YEAR);
        year = String.valueOf(y);
        recyclerChat = findViewById(R.id.recyclerChat);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        imgMic = findViewById(R.id.imgMic);

        adapter = new ChatbotMessageAdapter(messages);
        recyclerChat.setAdapter(adapter);
        recyclerChat.setLayoutManager(new LinearLayoutManager(this));

        addBotMessage("Hi! I'm Genie🤖\nPlease enter your choice (1, 2, 3, 4 or 5) to continue \n\n1️⃣ Payroll Query\n2️⃣ PF Query\n3️⃣ ESIC Card \n4️⃣ Medical Card\n5️⃣ Know Your SPOC");

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
                            .makeText(ChatBotNewActivity.this, " " + e.getMessage(),
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
            addBotMessage("Hi! I'm Genie🤖\nPlease enter your choice (1, 2, 3, 4 or 5) to continue \n\n1️⃣ Payroll Query\n2️⃣ PF Query\n3️⃣ ESIC Card \n4️⃣ Medical Card\n5️⃣ Know Your SPOC");
            return;
        }

        if (msg.equals("exit") || msg.equals("exit chat")) {
            addBotMessage("Thank you for chatting with HR Genius! 👋");
            recyclerChat.postDelayed(this::finish, 1200);
            return;
        }

        switch (currentMenu) {

            // 🅰️🅱️🅲 MAIN MENU
            case MAIN:
                switch (msg) {
                    case "1":
                    case "one":
                    case "payroll":
                    case "payroll query":
                        currentMenu = MenuState.PAYROLL;
                        addBotMessage("Please enter your choice (1, 2, 3 or 4) to continue \n\n1️⃣ Last Month’s Salary Slip\n2️⃣ CTC Information\n3️⃣ Primary Options\n4️⃣ Exit Chat");
                        break;

                    case "2":
                    case "two":
                    case "pf":
                    case "pf query":
                        currentMenu = MenuState.PF;
                        addBotMessage("Please enter your choice (1, 2, 3, 4 or 5) to continue\n \n1️⃣ UAN Number\n2️⃣ PF Passbook\n3️⃣ Any Other PF-Related Query\n4️⃣ Primary Options\n5️⃣ Exit Chat");
                        break;

                    case "3":
                    case "three":
                    case "esi":
                    case "esi card":
                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("AEMEmployeeID", pref.getEmpId());
                            obj.put("FileName", JSONObject.NULL);
                            obj.put("FileType", "0");
                            obj.put("DocumentID", "0");
                            obj.put("ReferenceNo", "0");
                            obj.put("DbOperation", "1");
                            obj.put("SecurityCode", pref.getSecurityCode());
                            getDocList(obj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;
                    case "4":
                    case "four":
                    case "medical":
                    case "medical card":
                        JSONObject medobj = new JSONObject();
                        try {
                            medobj.put("AEMEmployeeID", pref.getEmpId());
                            medobj.put("FileName", JSONObject.NULL);
                            medobj.put("FileType", "0");
                            medobj.put("DocumentID", "0");
                            medobj.put("ReferenceNo", "0");
                            medobj.put("DbOperation", "1");
                            medobj.put("SecurityCode", pref.getSecurityCode());
                            getMedicalCard(medobj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;
                    case "5":
                    case "five":
                    case "spoc":
                    case "my spoc":
                    case "know my spoc":
                        getSpokePersonListOne();

                        break;

                    default:
                        // 🔸 Default handler for MAIN
                        addBotMessage("Oops! I didn’t quite get that\n\nPlease enter your choice (1, 2, 3, 4 or 5) to continue🙂\n\n1️⃣ Payroll Query\n2️⃣ PF Query\n3️⃣ ESIC Card \n4️⃣ Medical Card\n5️⃣ Know Your SPOC");
                }
                break;

            // 💼 PAYROLL MENU
            case PAYROLL:
                switch (msg) {
                    case "1":
                    case "one":
                    case "salary":
                    case "last month salary":
                    case "last month salary slip":
                    case "salary slip":
                        JSONObject obj = new JSONObject();
                        try {

                            obj.put("AEMEmployeeID", pref.getEmpId());
                            obj.put("SalYear", year);
                            obj.put("SecurityCode", pref.getSecurityCode());
                            getSalaryList(obj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //addBotMessage("Here’s your Last Month Salary Slip 📑\nhttps://example.com/salaryslip");
                        break;
                    case "2":
                    case "two":
                    case "ctc":
                    case "ctc information":
                    case "ctc view":
                        String ctcUrl = pref.getCTCURL(); // your real URL
                        String maskedText = "<a href=\"" + ctcUrl + "\">Tap to View</a>";
                        addBotMessage("Here is your CTC information 🔒:\n" + maskedText);
                        break;
                    case "3":
                    case "three":
                    case "primary":
                    case "primary option":
                        currentMenu = MenuState.MAIN;
                        addBotMessage("Please enter your choice (1, 2, 3, 4 or 5) to continue\n\n1️⃣ Payroll Query\n2️⃣ PF Query\n3️⃣ ESIC Card \n4️⃣ Medical Card\n5️⃣ Know Your SPOC");
                        break;
                    case "4":
                    case "four":
                    case "exit":
                    case "exit chat":
                        addBotMessage("Thank you for chatting with HR Genius! 👋");
                        recyclerChat.postDelayed(this::finish, 1200);
                        break;
                    default:
                        // 🔸 Default handler for PAYROLL
                        addBotMessage("Please enter your choice (1, 2, 3, 4 or 5) to continue\n\n1️⃣ Month’s Salary Slip\n2️⃣ CTC View\n3️⃣ Primary Options\n4️⃣ Exit Chat\n5️⃣ Know Your SPOC");
                }
                break;

            // 🧾 PF MENU
            case PF:
                switch (msg) {
                    case "1":
                    case "one":
                    case "uan number":
                    case "un number":
                    case "uan":
                        //addBotMessage("Your UAN number is: <b>UAN1234567890</b>");
                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("AEMConsultantID", pref.getEmpConId());
                            obj.put("AEMClientID", pref.getEmpClintId());
                            obj.put("AEMClientOfficeID", pref.getEmpClintOffId());
                            obj.put("AEMEmployeeID", pref.getEmpId());
                            obj.put("SecurityCode", pref.getSecurityCode());
                            obj.put("WorkingStatus", "1");
                            obj.put("CurrentPage", "0");
                            getUANNumber(obj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "2":
                    case "two":
                    case "pf passbook":
                    case "passbook":
                        addBotMessage("View your PF Passbook \nTap to Open 📘\nhttps://www.epfindia.gov.in");
                        break;
                    case "3":
                    case "three":
                    case "query":
                    case "pf related query":
                    case "another query":
                        addBotMessage("For any other PF-related queries \nPlease contact our toll-free helpline\n\n7313604174 (10:00 AM to 6:00 PM)");
                        break;
                    case "4":
                    case "four":
                    case "primary":
                    case "primary option":
                        currentMenu = MenuState.MAIN;
                        addBotMessage("Please enter your choice (1, 2, 3, 4 or 5) to continue \n\n1️⃣ Payroll Query\n2️⃣ PF Query\n3️⃣ ESIC Card \n4️⃣ Medical Card\n5️⃣ Know Your SPOC");
                        break;
                    case "5":
                    case "five":
                    case "exit chat":
                        addBotMessage("Thank you for chatting with HR Genius! 👋");
                        recyclerChat.postDelayed(this::finish, 1200);
                        break;
                    default:
                        // 🔸 Default handler for PF
                        addBotMessage("Please choose a valid PF option (1–5):\n1️⃣ UAN Number\n2️⃣ PF Passbook\n3️⃣ Any Other PF-Related Query\n4️⃣ Primary Options\n5️⃣ Exit Chat");
                }
                break;

            // 📄 DOCUMENT MENU
            case DOCUMENT:
                if (msg.equals("primary") || msg.equals("primary options")) {
                    currentMenu = MenuState.MAIN;
                    addBotMessage("Returning to Primary Options:\n🅰️ Payroll Service\n🅱️ PF Service");
                } else {
                    // 🔸 Default handler for DOCUMENT
                    addBotMessage("Please provide more details about your document request 📄\nOr type 'primary' to go back.");
                }
                break;
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
                                String maskedText = "<a href=\"" + url + "\">Tap to View</a>";
                                addBotMessage("Here’s your last month’s salary slip 📑\n" + maskedText);


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
                                        String maskedText = "<a href=\"" + DocLink + "\">Tap to View</a>";
                                        addBotMessage("Your ESIC Card is ready 📑\n" + maskedText);
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
                        addBotMessage("Error fetching ESIC Card details. Please try again later ❌");

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
                                        String maskedText = "<a href=\"" + DocLink + "\">Tap to View</a>";
                                        addBotMessage("Your Medical Card is ready 📑\n" + maskedText);
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
                        addBotMessage("Error fetching ESIC Card details. Please try again later ❌");

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
    private void getSpokePersonListOne() {
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
                                String Email= obj.optString("Email");
                                addBotMessage("Your designated SPOC is " + Name + "\nContact Number: " + Mobile+"\nEmail ID: "+Email);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);

                String text = Objects.requireNonNull(result).get(0);
                String msg = text;
                if (msg.isEmpty()) return;

                addUserMessage(msg);
                etMessage.setText("");
                handleUserInput(msg.toLowerCase());


            }
        }
    }
}