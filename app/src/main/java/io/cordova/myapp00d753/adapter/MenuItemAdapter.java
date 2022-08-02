package io.cordova.myapp00d753.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.AttenDanceDashboardActivity;
import io.cordova.myapp00d753.activity.AttendanceActivity;
import io.cordova.myapp00d753.activity.ChangePasswordActivity;
import io.cordova.myapp00d753.activity.DailyDashBoardActivity;
import io.cordova.myapp00d753.activity.DocumentActivity;
import io.cordova.myapp00d753.activity.EmployeeDashBoardActivity;
import io.cordova.myapp00d753.activity.FeedBackRatingActivity;
import io.cordova.myapp00d753.activity.GeoFenceActivity;
import io.cordova.myapp00d753.activity.KYCDashBoardActivity;
import io.cordova.myapp00d753.activity.LeaveApplicationActivity;
import io.cordova.myapp00d753.activity.PayrollActivity;
import io.cordova.myapp00d753.activity.ProfileActivity;
import io.cordova.myapp00d753.activity.RemDashBoardActivity;
import io.cordova.myapp00d753.activity.SalesManagementDashboardActivity;
import io.cordova.myapp00d753.activity.VoiceAssistantActivity;
import io.cordova.myapp00d753.module.MenuItemModel;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MyViewHolder> {
    ArrayList<MenuItemModel> itemList=new ArrayList<>();
    Context mContex;
    String PFLink;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_item_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {


        myViewHolder.tvMenuItem.setText(itemList.get(i).getMenuName());

        if (itemList.get(i).getMenuId().equalsIgnoreCase("1")){
            myViewHolder.imgMenu.setImageResource(R.drawable.profile);
        }
        else if (itemList.get(i).getMenuId().equalsIgnoreCase("2")){
            myViewHolder.imgMenu.setImageResource(R.drawable.attendance);
        }
        else if (itemList.get(i).getMenuId().equalsIgnoreCase("3")){
            myViewHolder.imgMenu.setImageResource(R.drawable.payroll);
        }
        else if (itemList.get(i).getMenuId().equalsIgnoreCase("4")){
            myViewHolder.imgMenu.setImageResource(R.drawable.sales_management);
        }
        else if (itemList.get(i).getMenuId().equalsIgnoreCase("5")){
            myViewHolder.imgMenu.setImageResource(R.drawable.pf_trust);
        }
        else if (itemList.get(i).getMenuId().equalsIgnoreCase("6")){
            myViewHolder.imgMenu.setImageResource(R.drawable.kyc);
        }
        else if (itemList.get(i).getMenuId().equalsIgnoreCase("7")){
            myViewHolder.imgMenu.setImageResource(R.drawable.reimbursement);
        }
        else if (itemList.get(i).getMenuId().equalsIgnoreCase("8")){
            myViewHolder.imgMenu.setImageResource(R.drawable.feedback);
        }
        else if (itemList.get(i).getMenuId().equalsIgnoreCase("9")){
            myViewHolder.imgMenu.setImageResource(R.drawable.geo_fencing);
        }
        else if (itemList.get(i).getMenuId().equalsIgnoreCase("10")){
            myViewHolder.imgMenu.setImageResource(R.drawable.daily_log);
        }
        else if (itemList.get(i).getMenuId().equalsIgnoreCase("11")){
            myViewHolder.imgMenu.setImageResource(R.drawable.chage_password);
        }else if (itemList.get(i).getMenuId().equalsIgnoreCase("12")){
            myViewHolder.imgMenu.setImageResource(R.drawable.leave_management);
        }else if (itemList.get(i).getMenuId().equalsIgnoreCase("0")){
            myViewHolder.imgMenu.setImageResource(R.drawable.voice);
        }else {
            myViewHolder.itemView.setVisibility(View.GONE);
        }


        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemList.get(i).getMenuId().equalsIgnoreCase("1")){
                    //Profile
                    Intent intent=new Intent(mContex, ProfileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContex.startActivity(intent);
                }else if (itemList.get(i).getMenuId().equalsIgnoreCase("2")){
                    //Attendnace

                        Intent intent = new Intent(mContex, AttenDanceDashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContex.startActivity(intent);

                }
                else if (itemList.get(i).getMenuId().equalsIgnoreCase("3")){
                    //Payroll

                    Intent intent=new Intent(mContex, PayrollActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContex.startActivity(intent);
                }
                else if (itemList.get(i).getMenuId().equalsIgnoreCase("4")){
                    //Saales
                    Intent intent=new Intent(mContex, SalesManagementDashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContex.startActivity(intent);
                }
                else if (itemList.get(i).getMenuId().equalsIgnoreCase("5")){
                    //PF
                    Uri uri = Uri.parse(PFLink); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContex.startActivity(intent);

                }
                else if (itemList.get(i).getMenuId().equalsIgnoreCase("6")){
                    //document
                    Intent intent=new Intent(mContex, DocumentActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContex.startActivity(intent);
                }
                else if (itemList.get(i).getMenuId().equalsIgnoreCase("7")){
                    //reimbursement
                    Intent intent=new Intent(mContex, RemDashBoardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContex.startActivity(intent);
                }
                else if (itemList.get(i).getMenuId().equalsIgnoreCase("8")){
                    //feedback
                    Intent intent = new Intent(mContex, FeedBackRatingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContex.startActivity(intent);
                }
                else if (itemList.get(i).getMenuId().equalsIgnoreCase("9")){
                    //geofence
                    Intent intent=new Intent(mContex, GeoFenceActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContex.startActivity(intent);
                }
                else if (itemList.get(i).getMenuId().equalsIgnoreCase("10")){
                    //Dailylog

                        Intent intent = new Intent(mContex, DailyDashBoardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContex.startActivity(intent);

                }
                else if (itemList.get(i).getMenuId().equalsIgnoreCase("11")){
                    //Chanepassword
                    Intent intent=new Intent(mContex, ChangePasswordActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContex.startActivity(intent);
                }   else if (itemList.get(i).getMenuId().equalsIgnoreCase("0")){
                    //Chanepassword
                    Intent intent=new Intent(mContex, VoiceAssistantActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContex.startActivity(intent);
                }

                else if (itemList.get(i).getMenuId().equalsIgnoreCase("12")){
                    //leave
                    Intent intent=new Intent(mContex, LeaveApplicationActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContex.startActivity(intent);
                }

            }
        });




    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvMenuItem;
        LinearLayout lnMain,llGreen,llYellow;
        ImageView imgMenu;




        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMenuItem=(TextView)itemView.findViewById(R.id.tvMenuItem);
            imgMenu=(ImageView)itemView.findViewById(R.id.imgMenu);
            lnMain=(LinearLayout)itemView.findViewById(R.id.lnMain);




        }
    }

    public MenuItemAdapter(ArrayList<MenuItemModel> itemList, Context mContext,String pfLink) {
        this.itemList = itemList;
        this.mContex=mContext;
        this.PFLink=pfLink;
    }

    public void filterList(ArrayList<MenuItemModel> filterdNames) {
        this.itemList = filterdNames;
        notifyDataSetChanged();
    }


}
