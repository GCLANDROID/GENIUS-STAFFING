package io.cordova.myapp00d753.adapter;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.MobilePayslipActivity;
import io.cordova.myapp00d753.module.BotMessageModule;

public class ChatbotMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_USER = 0;
    private static final int TYPE_BOT = 1;
    private static final int VIEW_TYPE_TYPING = 2;
    private List<BotMessageModule> messages;



    public ChatbotMessageAdapter(List<BotMessageModule> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        BotMessageModule message = messages.get(position);
        switch (message.getType()) {
            case USER:
                return 0;
            case BOT:
                return 1;
            case TYPING:
                return 2;
            default:
                return 1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_USER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_user_message, parent, false);
            return new UserViewHolder(view);
        } else if (viewType == VIEW_TYPE_TYPING) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_bot_typing, parent, false);
            return new TypingViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_bot_message, parent, false);
            return new BotViewHolder(view);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BotMessageModule message = messages.get(position);

        if (holder instanceof UserViewHolder) {
            ((UserViewHolder) holder).tvUserMessage.setText(message.getMessage());
        }
        else if (holder instanceof BotViewHolder) {

            if (holder instanceof BotViewHolder) {
                String botMsg = message.getMessage();
                TextView tv = ((BotViewHolder) holder).tvBotMessage;

                // Convert to HTML for styled messages


                // Detect if the message contains a URL
                Pattern urlPattern;
                urlPattern = Pattern.compile("(https?://[^\\s\"']+)");
                Matcher matcher = urlPattern.matcher(botMsg);

                if (matcher.find()) {
                    // URL found → make it clickable
                    ((BotViewHolder) holder).tvBotMessage.setText(
                            Html.fromHtml(message.getMessage(), Html.FROM_HTML_MODE_LEGACY)
                    );

                    // Enable link clicks (opens browser automatically)
                    ((BotViewHolder) holder).tvBotMessage.setMovementMethod(LinkMovementMethod.getInstance());
                    String url = matcher.group(1);
                    if (url.endsWith("\"")) {
                        url = url.substring(0, url.length() - 1);
                    }

                    String finalUrl = url;
                    tv.setOnClickListener(v -> {
                        if (message.getMessage().contains("ESI Card") || message.getMessage().contains("Medical Card")){
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(finalUrl));
                            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            v.getContext().startActivity(browserIntent);
                        }else {
                            try {
                                Intent intent = new Intent(v.getContext(), MobilePayslipActivity.class);
                                intent.putExtra("finalUrl", finalUrl);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                v.getContext().startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    });
                } else {
                    ((BotViewHolder) holder).tvBotMessage.setText(message.getMessage());
                    // No URL → disable click
                    tv.setOnClickListener(null);
                }
            }
        }
        else if (holder instanceof TypingViewHolder) {
            // Nothing to bind — the GIF is static in XML
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserMessage;
        UserViewHolder(View itemView) {
            super(itemView);
            tvUserMessage = itemView.findViewById(R.id.tvUserMessage);
        }
    }

    static class BotViewHolder extends RecyclerView.ViewHolder {
        TextView tvBotMessage;
        BotViewHolder(View itemView) {
            super(itemView);
            tvBotMessage = itemView.findViewById(R.id.tvBotMessage);
        }
    }

    public static class TypingViewHolder extends RecyclerView.ViewHolder {
        public TypingViewHolder(View itemView) {
            super(itemView);
        }
    }
}
