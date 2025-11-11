package io.cordova.myapp00d753.activity;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import io.cordova.myapp00d753.R;

public class ChatBotActivity extends AppCompatActivity {
    LinearLayout chatLayout;
    EditText userInput;
    LinearLayout sendBtn;

    boolean isFirstMessage = true;
    String selectedMainOption = "";
    String selectedSubOption = "";
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);

         scrollView = findViewById(R.id.chatScroll);

        chatLayout = findViewById(R.id.chatLayout);
        userInput = findViewById(R.id.userInput);
        sendBtn = findViewById(R.id.sendBtn);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = userInput.getText().toString().trim();
                if (!msg.isEmpty()) {
                    addUserMessage("You: " + msg);
                    handleUserMessage(msg);
                    userInput.setText("");
                }
            }
        });
    }

    private void handleUserMessage(String msg) {
        if (msg.equalsIgnoreCase("restart")) {
            resetChat();
            return;
        }

        if (isFirstMessage) {
            isFirstMessage = false;
            showMainOptions();
        }
        else if (selectedMainOption.isEmpty()) {
            // Main level selection
            if (msg.equalsIgnoreCase("1") || msg.toLowerCase().contains("payroll")) {
                selectedMainOption = "Payroll";
                showPayrollOptions();
            }
            else if (msg.equalsIgnoreCase("2") || msg.toLowerCase().contains("pf")) {
                selectedMainOption = "PF";
                showPFResponse();
            }
            else {
                addBotMessage("Please choose 1️⃣ Payroll Service or 2️⃣ PF Service.");
            }
        }
        else if (selectedMainOption.equals("Payroll")) {
            // Payroll options
            if (msg.equalsIgnoreCase("1") || msg.toLowerCase().contains("salary")) {
                selectedSubOption = "SalarySlip";
                addBotMessage("Here’s your last month salary slip 👇");
                addBotMessage("<a href='https://example.com/salary-slip'>View Salary Slip</a>");
            }
            else if (msg.equalsIgnoreCase("2") || msg.toLowerCase().contains("ctc")) {
                selectedSubOption = "CTC";
                addBotMessage("Here’s your CTC details 👇");
                addBotMessage("<a href='https://example.com/ctc-details'>View CTC</a>");
            }
            else if (msg.equalsIgnoreCase("3") || msg.toLowerCase().contains("primary")) {
                selectedMainOption = "";
                selectedSubOption = "";
                addBotMessage("Returning to primary options...");
                showMainOptions();
            }
            else if (msg.equalsIgnoreCase("4") || msg.toLowerCase().contains("exit")) {
                addBotMessage("Exiting chat. Have a nice day!");
                finish(); // closes chatbot window
            }
            else {
                addBotMessage("Please choose 1️⃣ Last Month Salary Slip, 2️⃣ View CTC, 3️⃣ Primary Options, or 4️⃣ Exit Chat.");
            }
        }
        else {
            addBotMessage("You have already selected an option. Type 'restart' to start again.");
        }
    }

    private void showMainOptions() {
        addBotMessage("Please choose one of the following options:");
        addBotMessage("1️⃣ Payroll");
        addBotMessage("2️⃣ PF");
    }

    private void showPayrollOptions() {
        addBotMessage("You selected Payroll Service. Please choose an option:");
        addBotMessage("1️⃣ Last Month Salary Slip");
        addBotMessage("2️⃣ View CTC");
        addBotMessage("3️⃣ Primary Options");
        addBotMessage("4️⃣ Exit Chat");
    }

    private void showPFResponse() {
        addBotMessage("You selected PF Service.");
        addBotMessage("🧾 Your UAN Number is: UAN1234567890");
        addBotMessage("🔗 Please view your passbook here:");
        addBotMessage("<a href='https://www.epfindia.gov.in/site_en/For_Employees.php'>View PF Passbook</a>");
        addBotMessage("Type 'restart' to go back or 'exit' to close the chat.");
    }

    private void resetChat() {
        selectedMainOption = "";
        selectedSubOption = "";
        isFirstMessage = true;
        addBotMessage("Chat restarted. Please send any message to begin again.");
    }

    private void addMessage(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(16);
        tv.setPadding(8, 8, 8, 8);
        chatLayout.addView(tv);
    }

    private void addBotMessage(String text) {
        LinearLayout messageRow = new LinearLayout(this);
        messageRow.setOrientation(LinearLayout.HORIZONTAL);
        messageRow.setPadding(10, 5, 10, 5);

        // Robot icon
        ImageView icon = new ImageView(this);
        icon.setImageResource(R.drawable.ic_robot);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(60, 60);
        iconParams.setMargins(5, 0, 10, 0);
        icon.setLayoutParams(iconParams);

        // Message text
        TextView tv = new TextView(this);
        tv.setText(Html.fromHtml(text));
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        tv.setTextSize(16);
        tv.setPadding(10, 10, 10, 10);
        tv.setBackgroundResource(R.drawable.bg_bot_message);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        textParams.setMargins(0, 0, 80, 0);
        tv.setLayoutParams(textParams);

        // Add both views
        messageRow.addView(icon);
        messageRow.addView(tv);
        chatLayout.addView(messageRow);
        scrollToBottom();
    }

    private void addUserMessage(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(16);
        tv.setPadding(10, 10, 10, 10);
        tv.setBackgroundResource(R.drawable.bg_user_message);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = android.view.Gravity.END;
        params.setMargins(80, 5, 10, 5);
        tv.setLayoutParams(params);
        chatLayout.addView(tv);
        scrollToBottom();
    }

    private void scrollToBottom() {
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }
}
