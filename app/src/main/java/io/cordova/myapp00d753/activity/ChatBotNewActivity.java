package io.cordova.myapp00d753.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.ChatbotMessageAdapter;
import io.cordova.myapp00d753.module.BotMessageModule;
import io.cordova.myapp00d753.module.SalaryModule;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

public class ChatBotNewActivity extends AppCompatActivity {
    private RecyclerView recyclerChat;
    private EditText etMessage;
    private ImageView btnSend;
    private ChatbotMessageAdapter adapter;
    private List<BotMessageModule> messages = new ArrayList<>();
    private enum MenuState { MAIN, PAYROLL, PF, DOCUMENT }
    private MenuState currentMenu = MenuState.MAIN;
    Pref pref;
    int y;
    String year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot_new);
        pref=new Pref(ChatBotNewActivity.this);
        y = Calendar.getInstance().get(Calendar.YEAR);
        year=String.valueOf(y);
        recyclerChat = findViewById(R.id.recyclerChat);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);

        adapter = new ChatbotMessageAdapter(messages);
        recyclerChat.setAdapter(adapter);
        recyclerChat.setLayoutManager(new LinearLayoutManager(this));

        addBotMessage("Hi! I'm Genius Genie🤖\nPlease select one option from below \n\n🅰️ Payroll Query\n🅱️ PF Query");

        btnSend.setOnClickListener(v -> {
            String msg = etMessage.getText().toString().trim();
            if (msg.isEmpty()) return;

            addUserMessage(msg);
            etMessage.setText("");
            handleUserInput(msg.toLowerCase());
        });
    }



    private void handleUserInput(String rawMsg) {
        if (rawMsg == null) return;
        String msg = rawMsg.trim().toLowerCase();

        // 🔹 Global commands
        if (msg.equals("restart")) {
            currentMenu = MenuState.MAIN;
            addBotMessage("Please select one option from below 🔄\n\n🅰️ Payroll Related Query\n🅱️ PF Related Query");
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
                    case "a":
                    case "payroll":
                        currentMenu = MenuState.PAYROLL;
                        addBotMessage("Please choose any option:\n\n1️⃣ Last Month Salary Slip\n2️⃣ CTC Information\n3️⃣ Primary Options\n4️⃣ Exit Chat");
                        break;

                    case "b":
                    case "pf":
                        currentMenu = MenuState.PF;
                        addBotMessage("Please choose any option:\n1️⃣ UAN Number\n2️⃣ PF Passbook\n3️⃣ Any other PF Related Query\n4️⃣ Primary Options\n5️⃣ Exit Chat");
                        break;

                    case "c":
                    case "document":
                        currentMenu = MenuState.DOCUMENT;
                        addBotMessage("You chose Document Service 📄\nPlease upload or request your document here:\nhttps://example.com/document\nType 'primary' to go back.");
                        break;

                    default:
                        // 🔸 Default handler for MAIN
                        addBotMessage("Please choose a valid option 🙂\n🅰️ Payroll \n🅱️ PF");
                }
                break;

            // 💼 PAYROLL MENU
            case PAYROLL:
                switch (msg) {
                    case "1":
                        JSONObject obj=new JSONObject();
                        try {

                            obj.put("AEMEmployeeID",pref.getEmpId());
                            obj.put("SalYear",year);
                            obj.put("SecurityCode",pref.getSecurityCode());
                            getSalaryList(obj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //addBotMessage("Here’s your Last Month Salary Slip 📑\nhttps://example.com/salaryslip");
                        break;
                    case "2":
                        String ctcUrl = pref.getCTCURL(); // your real URL
                        String maskedText = "<a href=\"" + ctcUrl + "\">Click to View</a>";
                        addBotMessage("Here is your CTC information 🔒:\n" + maskedText);
                        break;
                    case "3":
                    case "primary":
                    case "primary options":
                        currentMenu = MenuState.MAIN;
                        addBotMessage("Returning to Primary Options:\n🅰️ Payroll Service\n🅱️ PF Service");
                        break;
                    case "4":
                    case "exit chat":
                        addBotMessage("Thank you for chatting with HR Genius! 👋");
                        recyclerChat.postDelayed(this::finish, 1200);
                        break;
                    default:
                        // 🔸 Default handler for PAYROLL
                        addBotMessage("Please choose a valid Payroll option (1–4):\n1️⃣ Last Month Salary Slip\n2️⃣ CTC View\n3️⃣ Primary Options\n4️⃣ Exit Chat");
                }
                break;

            // 🧾 PF MENU
            case PF:
                switch (msg) {
                    case "1":
                        //addBotMessage("Your UAN number is: <b>UAN1234567890</b>");
                        JSONObject obj=new JSONObject();
                        try {
                            obj.put("AEMConsultantID", pref.getEmpConId());
                            obj.put("AEMClientID",pref.getEmpClintId());
                            obj.put("AEMClientOfficeID",pref.getEmpClintOffId());
                            obj.put("AEMEmployeeID",pref.getEmpId());
                            obj.put("SecurityCode",pref.getSecurityCode());
                            obj.put("WorkingStatus","1");
                            obj.put("CurrentPage","0");
                            getUANNumber(obj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "2":
                        addBotMessage("Please view your PF Passbook here 📘\nhttps://www.epfindia.gov.in");
                        break;
                    case "3":
                        addBotMessage("For any other PF-related queries \nPlease contact our toll-free helpline at \n\n7313604174 (available from 10:00 AM to 6:00 PM)");
                        break;
                    case "4":
                    case "primary":
                    case "primary options":
                        currentMenu = MenuState.MAIN;
                        addBotMessage("Returning to Primary Options:\n🅰️ Payroll Service\n🅱️ PF Service");
                        break;
                    case "5":
                    case "exit chat":
                        addBotMessage("Thank you for chatting with HR Genius! 👋");
                        recyclerChat.postDelayed(this::finish, 1200);
                        break;
                    default:
                        // 🔸 Default handler for PF
                        addBotMessage("Please choose a valid PF option (1–5):\n1️⃣ UAN Number\n2️⃣ PF Passbook\n3️⃣ Any other PF Related Query\n4️⃣ Primary Options\n5️⃣ Exit Chat");
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
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
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
                                    JSONObject obj=jsonArray.getJSONObject(i);


                                    String UanNo=obj.optString("UANNumber");
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
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
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


                    }
                });
    }
}