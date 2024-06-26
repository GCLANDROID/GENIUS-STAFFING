package io.cordova.myapp00d753.FirebaseNotification;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessagingServ";
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {

        if (message.getNotification() != null) {
            // Handle notification message
            String title = message.getNotification().getTitle();
            String body = message.getNotification().getBody();
            Log.e(TAG, "onMessageReceived: title: "+title+" body: "+body);
            // Display the notification
            //sendNotification(title, body);
        }

        // Handle data payload if needed
        if (message.getData().size() > 0) {
            // Handle data payload
             Map<String, String> data = message.getData();
            // Process data
        }
    }

    private void sendNotification(String title, String body) {
        // Implement notification creation and display here
    }
}
