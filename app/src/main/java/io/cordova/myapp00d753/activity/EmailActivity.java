package io.cordova.myapp00d753.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;

import org.json.JSONObject;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

public class EmailActivity extends AppCompatActivity {
    TextView tvTo;
    EditText etSubject,etBody;
    Button btnSendMail;
    String to;
    Pref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        initView();
    }

    private void initView(){
        pref=new Pref(EmailActivity.this);
        to=getIntent().getStringExtra("to");
        btnSendMail=(Button) findViewById(R.id.btnSendMail);

        tvTo=(TextView) findViewById(R.id.tvTo);
        tvTo.setText(to);

        etSubject=(EditText) findViewById(R.id.etSubject);
        etBody=(EditText) findViewById(R.id.etBody);

        btnSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etSubject.getText().toString().length()>0){
                    if (etBody.getText().toString().length()>0){
                        postEmail();

                    }else {
                        Toast.makeText(EmailActivity.this,"Please Enter Email Body",Toast.LENGTH_LONG).show();

                    }

                }else {
                    Toast.makeText(EmailActivity.this,"Please Enter Email Subject",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void postEmail() {
        ProgressDialog pd=new ProgressDialog(EmailActivity.this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.upload(AppData.url + "post_GeniusSpocEmail")
                .addMultipartParameter("AEMEmployeeID", pref.getEmpId())
                .addMultipartParameter("MailBody", etBody.getText().toString())
                .addMultipartParameter("MailSubject", etSubject.getText().toString()+" - "+pref.getEmpId()+" ("+pref.getEmpName()+")")
                .addMultipartParameter("MailID", to )
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pd.dismiss();
                        Toast.makeText(EmailActivity.this,"Email has been sent successfully",Toast.LENGTH_LONG).show();
                        onBackPressed();







                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                      pd.dismiss();

                    }
                });
    }
}