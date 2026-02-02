package io.cordova.myapp00d753.adapter;

import android.content.Context;
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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;

public class NotiAdapter extends RecyclerView.Adapter<NotiAdapter.MyViewModel> {
    Context context;
    ArrayList<NotificationModel> contentList;

    public NotiAdapter(Context context, ArrayList<NotificationModel> contentList) {
        this.context = context;
        this.contentList = contentList;
    }

    @NonNull
    @Override
    public MyViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.noti_item, parent, false);
        return new MyViewModel(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewModel holder, int position) {
        holder.tvNotifcation.setText("* " + contentList.get(position).Content);
        if (!contentList.get(position).getC_Url().isEmpty()) {
            String reasonText =
                    "<a href='"+ contentList.get(position).getC_Url() +"'>" +
                            "<font color='#FF0000'><b>" + contentList.get(position).Content + "</b></font>" +
                            "</a>" +"<font color='#FF0000'><b> - View Document</b></font>";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.tvNotifcation.setText(Html.fromHtml(reasonText, Html.FROM_HTML_MODE_LEGACY));
            } else {
                holder.tvNotifcation.setText(Html.fromHtml(reasonText));
            }
        } else {
            holder.tvNotifcation.setText("* " + contentList.get(position).Content);
        }
        holder.tvNotifcation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!contentList.get(position).getC_Url().isEmpty()){
                    Uri uri = Uri.parse(contentList.get(position).getC_Url());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    class MyViewModel extends RecyclerView.ViewHolder {
        TextView tvNotifcation;

        public MyViewModel(@NonNull View itemView) {
            super(itemView);
            tvNotifcation = itemView.findViewById(R.id.tvNotifcation);
        }
    }
}
