package io.cordova.myapp00d753.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.ChangePasswordActivity;
import io.cordova.myapp00d753.activity.DailyDashBoardActivity;
import io.cordova.myapp00d753.activity.DocumentActivity;
import io.cordova.myapp00d753.activity.EmployeeDashBoardActivity;
import io.cordova.myapp00d753.activity.FeedBackRatingActivity;
import io.cordova.myapp00d753.activity.FeedBackRatingForJLLActivity;
import io.cordova.myapp00d753.activity.GeoFenceManageDashBoardActivity;
import io.cordova.myapp00d753.activity.ITViewActivity;
import io.cordova.myapp00d753.activity.IncomeTaxDashboardActivity;
import io.cordova.myapp00d753.activity.InsuranceActivity;
import io.cordova.myapp00d753.activity.LeaveApplicationActivity;
import io.cordova.myapp00d753.activity.NewLeaveApplicationActivity;
import io.cordova.myapp00d753.activity.NewUserDashboardActivity;
import io.cordova.myapp00d753.activity.PFDashBoardActivity;
import io.cordova.myapp00d753.activity.PayrollActivity;
import io.cordova.myapp00d753.activity.ProfileActivity;
import io.cordova.myapp00d753.activity.ProfileNewActivity;
import io.cordova.myapp00d753.activity.RemDashBoardActivity;
import io.cordova.myapp00d753.activity.VoiceAssistantActivity;
import io.cordova.myapp00d753.activity.attendance.AttenDanceDashboardActivity;
import io.cordova.myapp00d753.activity.honnasa.HonasaSalesDashboardActivity;
import io.cordova.myapp00d753.activity.metso.MetsoPMSTargetAchivementActivity;
import io.cordova.myapp00d753.bluedart.BlueDartAttenDanceDashboardActivity;
import io.cordova.myapp00d753.facereogntion.LoginDashboardActivity;
import io.cordova.myapp00d753.fragment.NewMenuFragment;
import io.cordova.myapp00d753.module.MenuItemModel;
import io.cordova.myapp00d753.utility.Pref;

public class NewMenuAdapter extends RecyclerView.Adapter<NewMenuAdapter.MyViewHolder>{
    Context context;
    ArrayList<MenuItemModel> itemList;
    Pref pref;
    int leaveFlag;
    String PFLink,isLiveStatus_LeaveApplication;
    Fragment fContext;
    NewMenuFragment newMenuFragment;
    public NewMenuAdapter(Context context, ArrayList<MenuItemModel> itemList,int leaveFlag,String PFLink,String isLiveStatus_LeaveApplication,Fragment fContext,NewMenuFragment newMenuFragment) {
        this.context = context;
        this.itemList = itemList;
        this.leaveFlag = leaveFlag;
        this.PFLink = PFLink;
        this.isLiveStatus_LeaveApplication = isLiveStatus_LeaveApplication;
        pref = new Pref(context);
        this.fContext = fContext;
        this.newMenuFragment = newMenuFragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.new_menu_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(itemList.get(position).getMenuId().equals("1")){
            holder.imgMenuItem.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.new_profile)
            );
        } else if(itemList.get(position).getMenuId().equals("6")){
            holder.imgMenuItem.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.new_kyc)
            );
        } else if(itemList.get(position).getMenuId().equals("2")){
            holder.imgMenuItem.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.new_attendance)
            );
        } else if(itemList.get(position).getMenuId().equals("3")){
            holder.imgMenuItem.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.new_payroll)
            );
        }  else if(itemList.get(position).getMenuId().equals("7")){
            holder.imgMenuItem.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.new_reimbursement)
            );
        } else if(itemList.get(position).getMenuId().equals("5")){
            holder.imgMenuItem.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.new_pf)
            );
        } else if(itemList.get(position).getMenuId().equals("12")){
            holder.imgMenuItem.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.new_leave)
            );
        } /*else if(itemList.get(position).getMenuId().equals("5")){
            holder.imgMenuItem.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.new_pf)
            );
        }*/  else if(itemList.get(position).getMenuId().equals("8")){
            holder.imgMenuItem.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.new_feedback)
            );
        } else if(itemList.get(position).getMenuId().equals("11")){
            holder.imgMenuItem.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.new_change_password)
            );
        } else if(itemList.get(position).getMenuId().equals("20")){
            holder.imgMenuItem.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.new_insurance)
            );
        } else if(itemList.get(position).getMenuId().equals("21")){
            holder.imgMenuItem.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.new_self_resignation)
            );
        } else if(itemList.get(position).getMenuId().equals("201")){
            holder.imgMenuItem.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.new_pms)
            );
        } else if(itemList.get(position).getMenuId().equals("4")){
            holder.imgMenuItem.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.new_sales_management)
            );
        } else if(itemList.get(position).getMenuId().equals("200")) {
            holder.imgMenuItem.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.new_survey)
            );
        } else if(itemList.get(position).getMenuId().equals("2100")){
            holder.imgMenuItem.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.new_income)
            );
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemList.get(position).getMenuId().equals("1")){
                    //Profile
                    //Intent intent=new Intent(context, ProfileActivity.class);
                    /*Intent intent=new Intent(context, ProfileNewActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);*/
                    JSONObject objNeedToAct=new JSONObject();
                    try {
                        objNeedToAct.put("MasterID", pref.getMasterId());
                        newMenuFragment.getNeedToActData(objNeedToAct,"profile");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if (itemList.get(position).getMenuId().equals("2")) {
                    //Attendnace
                    JSONObject objNeedToAct=new JSONObject();
                    try {
                        objNeedToAct.put("MasterID", pref.getMasterId());
                        newMenuFragment.getNeedToActData(objNeedToAct,"attendance");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (itemList.get(position).getMenuId().equals("3")) {
                    //Payroll

                    JSONObject objNeedToAct=new JSONObject();
                    try {
                        objNeedToAct.put("MasterID", pref.getMasterId());
                        newMenuFragment.getNeedToActData(objNeedToAct,"payroll");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    /*Intent intent=new Intent(context, PayrollActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);*/
                } else if (itemList.get(position).getMenuId().equals("4")) {
                    //Saales
                    if (pref.getEmpClintId().equalsIgnoreCase("AEMCLI2310001780")){
                        Intent intent=new Intent(context, HonasaSalesDashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }else {
                        Intent intent=new Intent(context, HonasaSalesDashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                } else if (itemList.get(position).getMenuId().equals("5")){
                    //PF
                    ((NewMenuFragment) fContext).openPF_Popup();
                    /*Intent intent=new Intent(context, PFDashBoardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("PFLink",PFLink);
                    context.startActivity(intent);*/
                } else if (itemList.get(position).getMenuId().equals("6")) {
                    //document
                    ((NewMenuFragment) fContext).openKYCDocumentPopUp();
                    /*Intent intent=new Intent(context, DocumentActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);*/
                } else if (itemList.get(position).getMenuId().equals("7")) {
                    //reimbursement
                    ((NewMenuFragment) fContext).openReimbursementPopUp();
                    /*Intent intent=new Intent(context, RemDashBoardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);*/
                } else if (itemList.get(position).getMenuId().equals("8")) {
                    //feedback
                    Intent intent = new Intent(context, FeedBackRatingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else if (itemList.get(position).getMenuId().equals("9")){
                    //geofence
                    Intent intent=new Intent(context, GeoFenceManageDashBoardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else if (itemList.get(position).getMenuId().equals("10")) {
                    //Dailylog
                    Intent intent = new Intent(context, DailyDashBoardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else if (itemList.get(position).getMenuId().equals("11")) {
                    //Chanepassword
                    Intent intent=new Intent(context, ChangePasswordActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else if (itemList.get(position).getMenuId().equals("0")){
                    //Chanepassword
                    Intent intent=new Intent(context, VoiceAssistantActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else if (itemList.get(position).getMenuId().equals("12")){
                    //TODO: LEAVE
                    JSONObject objNeedToAct=new JSONObject();
                    try {
                        objNeedToAct.put("MasterID", pref.getMasterId());
                        newMenuFragment.getNeedToActData(objNeedToAct,"leave");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (itemList.get(position).getMenuId().equals("200")){
                    //TODO: Feedback
                    Intent intent=new Intent(context, FeedBackRatingForJLLActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }else if (itemList.get(position).getMenuId().equals("20")){

                    Intent intent=new Intent(context, InsuranceActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }else if (itemList.get(position).getMenuId().equals("300")){
                    Intent intent=new Intent(context, LoginDashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }else if (itemList.get(position).getMenuId().equals("201")){
                    //TODO: PMS
                    Intent intent=new Intent(context, MetsoPMSTargetAchivementActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }else if (itemList.get(position).getMenuId().equals("2100")){
                    //TODO: Income Tax
                    ((NewMenuFragment) fContext).openIncomeTaxPopUp();
                    /*Intent intent=new Intent(context, IncomeTaxDashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);*/
                }else if (itemList.get(position).getMenuId().equals("21")){
                    //TODO: Self Resignation
                    newMenuFragment.resignationget();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imgMenuItem;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMenuItem = itemView.findViewById(R.id.imgMenuItem);
        }
    }
}
