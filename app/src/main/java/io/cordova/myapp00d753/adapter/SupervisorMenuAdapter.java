package io.cordova.myapp00d753.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.GeoFenceApprovalActivity;
import io.cordova.myapp00d753.activity.ITViewActivity;
import io.cordova.myapp00d753.activity.SupAttenManageActivity;
import io.cordova.myapp00d753.activity.SupAttendanceActivity;
import io.cordova.myapp00d753.activity.SupProfileActivity;
import io.cordova.myapp00d753.activity.SuperVisiorDashBoardActivity;
import io.cordova.myapp00d753.activity.murugappa.AttendanceDashboardActivity;
import io.cordova.myapp00d753.bluedart.ODOmeterApprvalActivity;
import io.cordova.myapp00d753.utility.Pref;

public class SupervisorMenuAdapter extends RecyclerView.Adapter<SupervisorMenuAdapter.ViewHolder> {
    Context context;
    ArrayList<HashMap<String, String>> menuList;

    public SupervisorMenuAdapter(Context context, ArrayList<HashMap<String, String>> menuList) {
        this.context = context;
        this.menuList = menuList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.supervisior_menu_raw, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pref pref=new Pref(context);

        HashMap<String, String> item = menuList.get(position);

        String menuId = item.get("id");
        String menuName = item.get("name");

        holder.txtMenuName.setText(menuName);

        holder.itemView.setOnClickListener(v -> {

            switch (menuId) {

                case "NewLMSAccess":


                case "AttendanceAccess":
                    context.startActivity(new Intent(context, SupAttendanceActivity.class));
                    break;

                case "LeaveAccess":


                case "AttnMarkAccess":
                    if (pref.getEmpClintId().equalsIgnoreCase("AEMCLI2410001867") || pref.getEmpClintId().equalsIgnoreCase("AEMCLI1310000782")){
                        Intent AttnMarkintent=new Intent(context, AttendanceDashboardActivity.class);
                        context.startActivity(AttnMarkintent);
                    }else {
                        Intent Attnintent=new Intent(context,SupAttenManageActivity.class);
                        context.startActivity(Attnintent);
                    }
                    break;

                case "LeaveApprovalAccess":
                    Intent intent=new Intent(context, ITViewActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("url",pref.getLeaveApprovalURL());
                    context.startActivity(intent);
                   // context.startActivity(new Intent(context, LeaveApprovalActivity.class));
                    break;

                case "AttnRegApprovalAccess":
                    Intent RegApprovalintent=new Intent(context, ITViewActivity.class);
                    RegApprovalintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    RegApprovalintent.putExtra("url",pref.getRegApprovalURL());
                    context.startActivity(RegApprovalintent);
                   // context.startActivity(new Intent(context, AttendanceRegularizationApprovalActivity.class));
                    break;

                case "AdjApprovalAccess":
                    Intent AdjApprovalintent=new Intent(context, ITViewActivity.class);
                    AdjApprovalintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    AdjApprovalintent.putExtra("url",pref.getAdjApprovalURL());
                    context.startActivity(AdjApprovalintent);
                    //context.startActivity(new Intent(context, AdjustmentApprovalActivity.class));
                    break;

                case "DailyAttnReportAccess":
                    Intent dailyAttReport=new Intent(context, ITViewActivity.class);
                    dailyAttReport.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    dailyAttReport.putExtra("url",pref.getSupDailyAttenReportUrl());
                    context.startActivity(dailyAttReport);
                    break;

                case "LeaveApplicationReportAccess":
                   // context.startActivity(new Intent(context, LeaveApplicationReportActivity.class));
                    break;

                case "LeaveBalanceReportAccess":
                    Intent leaveBalanceReportIntent=new Intent(context, ITViewActivity.class);
                    leaveBalanceReportIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    leaveBalanceReportIntent.putExtra("url",pref.getSupLeaveBalanceYTDReportUrl());
                    context.startActivity(leaveBalanceReportIntent);
                    //context.startActivity(new Intent(context, LeaveBalanceReportActivity.class));
                    break;

                case "AttnAdjustmentReportAccess":
                    Intent AdjReportintent=new Intent(context, ITViewActivity.class);
                    AdjReportintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    AdjReportintent.putExtra("url",pref.getSupAdjAndRegReportUrl());
                    context.startActivity(AdjReportintent);
                    //context.startActivity(new Intent(context, AttendanceAdjustmentReportActivity.class));
                    break;

                case "AttnRegReportAccess":
                    //context.startActivity(new Intent(context, AttendanceRegularizationReportActivity.class));
                    break;

                case "GeoFenchApprovalAccess":
                    context.startActivity(new Intent(context, GeoFenceApprovalActivity.class));
                    break;

                case "TeamProfile":
                    context.startActivity(new Intent(context, SupProfileActivity.class));
                    break;

                case "ODOMeter":
                    context.startActivity(new Intent(context, ODOmeterApprvalActivity.class));
                    break;

                default:
                    Toast.makeText(context, "Screen not available", Toast.LENGTH_SHORT).show();
                    break;
            }
        });



        switch (menuId) {

                case "NewLMSAccess":

                    break;

                case "AttendanceAccess":
                    holder.imgMenu.setImageResource(R.drawable.attendance);
                    break;

                case "LeaveAccess":

                    break;

                case "AttnMarkAccess":
                    holder.imgMenu.setImageResource(R.drawable.manage);
                    break;

                case "LeaveApprovalAccess":
                    holder.imgMenu.setImageResource(R.drawable.approval);
                    break;

                case "AttnRegApprovalAccess":
                    holder.imgMenu.setImageResource(R.drawable.approval);
                    break;

                case "AdjApprovalAccess":
                    holder.imgMenu.setImageResource(R.drawable.approval);
                    break;

                case "DailyAttnReportAccess":
                    holder.imgMenu.setImageResource(R.drawable.report);
                    break;

                case "LeaveApplicationReportAccess":

                    break;

                case "LeaveBalanceReportAccess":
                    holder.imgMenu.setImageResource(R.drawable.report);
                    break;

                case "AttnAdjustmentReportAccess":
                    holder.imgMenu.setImageResource(R.drawable.report);
                    break;

                case "AttnRegReportAccess":

                    break;

                case "GeoFenchApprovalAccess":
                    holder.imgMenu.setImageResource(R.drawable.geo_fencing);
                    break;

            case "TeamProfile":
                    holder.imgMenu.setImageResource(R.drawable.profile);
                    break;

            case "ODOMeter":
                holder.imgMenu.setImageResource(R.drawable.speedometer);
                break;



                default:

                    break;
            }
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtMenuName;
        ImageView imgMenu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMenu=itemView.findViewById(R.id.imgMenu);
            txtMenuName = itemView.findViewById(R.id.txtMenuName);
        }
    }

}
