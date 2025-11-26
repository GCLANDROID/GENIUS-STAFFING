package io.cordova.myapp00d753.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.SelfOnboardingChatBotActivity;
import io.cordova.myapp00d753.module.BotMessageModule;

public class SelfOnboardingChatbotMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<BotMessageModule> messageList;
    private OnUploadClickListener uploadClickListener;
    Activity activity;

    public interface OnUploadClickListener {
        void onUploadClicked();
    }

    public void setOnUploadClickListener(OnUploadClickListener listener) {
        this.uploadClickListener = listener;
    }

    public SelfOnboardingChatbotMessageAdapter(List<BotMessageModule> list,Activity activity) {
        this.messageList = list;
        this.activity=activity;
    }

    @Override
    public int getItemViewType(int position) {
        return messageList.get(position).getType().ordinal();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        BotMessageModule.Type type = BotMessageModule.Type.values()[viewType];
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (type) {

            case USER:
                return new UserHolder(inflater.inflate(R.layout.item_user_message, parent, false));

            case BOT:
                return new BotHolder(inflater.inflate(R.layout.item_bot_message, parent, false));

            case TYPING:
                return new TypingHolder(inflater.inflate(R.layout.item_bot_typing, parent, false));

            case BOT_BUTTON:
                return new BotButtonHolder(inflater.inflate(R.layout.item_bot_button, parent, false));

            case IMAGE:
                return new ImageHolder(inflater.inflate(R.layout.item_image_message, parent, false));
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        BotMessageModule msg = messageList.get(position);

        switch (msg.getType()) {

            case USER:
                ((UserHolder) holder).txtUser.setText(msg.getMessage());

                break;

            case BOT:
                ((BotHolder) holder).txtBot.setText(msg.getMessage());
                if (msg.getMessage().contains("Please enter your Personal Details.")){
                    ((BotHolder) holder).llPerosnalDetails.setVisibility(View.VISIBLE);
                }else {
                    ((BotHolder) holder).llPerosnalDetails.setVisibility(View.GONE);
                }

                ((BotHolder) holder).btnPersonalUpload.setOnClickListener(v -> {
                    ((SelfOnboardingChatBotActivity)activity).personalDialog();
                });
                break;

            case TYPING:
                // no binding needed
                break;

            case BOT_BUTTON:
                BotButtonHolder btnHolder = (BotButtonHolder) holder;
                btnHolder.btnUpload.setOnClickListener(v -> {
                    if (uploadClickListener != null)
                        uploadClickListener.onUploadClicked();
                });
                break;

            case IMAGE:
                Bitmap bmp = msg.getImage();
                ((ImageHolder) holder).imgMessage.setImageBitmap(bmp);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    // ----------- HOLDERS -----------

    static class UserHolder extends RecyclerView.ViewHolder {
        TextView txtUser;
        UserHolder(@NonNull View itemView) {
            super(itemView);
            txtUser = itemView.findViewById(R.id.tvUserMessage);
        }
    }

    static class BotHolder extends RecyclerView.ViewHolder {
        TextView txtBot;
        LinearLayout llPerosnalDetails;
        Button btnPersonalUpload;
        BotHolder(@NonNull View itemView) {
            super(itemView);
            txtBot = itemView.findViewById(R.id.tvBotMessage);
            llPerosnalDetails=(LinearLayout) itemView.findViewById(R.id.llPersonalDetails);
            btnPersonalUpload=itemView.findViewById(R.id.btnPersonalUpload);
        }
    }

    static class TypingHolder extends RecyclerView.ViewHolder {
        TypingHolder(@NonNull View itemView) { super(itemView); }
    }

    static class BotButtonHolder extends RecyclerView.ViewHolder {
        Button btnUpload;
        BotButtonHolder(@NonNull View itemView) {
            super(itemView);
            btnUpload = itemView.findViewById(R.id.btnUpload);
        }
    }

    static class ImageHolder extends RecyclerView.ViewHolder {
        ImageView imgMessage;
        ImageHolder(@NonNull View itemView) {
            super(itemView);
            imgMessage = itemView.findViewById(R.id.imgMessage);
        }
    }
}
