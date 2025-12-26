package io.cordova.myapp00d753.activity.NEW.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.NEW.NEW_AttendanceMarkingActivity;
import io.cordova.myapp00d753.activity.NEW.NEW_AttendanceRegularizationActivity;
import io.cordova.myapp00d753.activity.NEW.model.LocationModel;
import io.cordova.myapp00d753.activity.NEW.model.NEW_BackLogAttendanceModel;
import io.cordova.myapp00d753.activity.SKF.SKF_AttendanceRegularizationActivity;
import io.cordova.myapp00d753.activity.SKF.adapter.SKF_DayTypeAdapter;
import io.cordova.myapp00d753.activity.metso.MetsoNewReimbursementClaimActivity;
import io.cordova.myapp00d753.activity.metso.adapter.LocationSpinnerAdapter;
import io.cordova.myapp00d753.activity.metso.adapter.MetsoAttendanceAdapter;
import io.cordova.myapp00d753.activity.metso.adapter.ShiftSpinnerAdapter;
import io.cordova.myapp00d753.activity.metso.adapter.SupervisorFilterAdapter;
import io.cordova.myapp00d753.activity.metso.model.MetsoLocationModel;
import io.cordova.myapp00d753.activity.metso.model.MetsoShiftModel;
import io.cordova.myapp00d753.module.BackLogAttendanceModel;
import io.cordova.myapp00d753.module.SpineerItemModel;
import io.cordova.myapp00d753.utility.TimeConversion;

public class NEW_BacklogAdapter extends RecyclerView.Adapter<NEW_BacklogAdapter.MyViewholder> {
    private static final String TAG = "NEW_BacklogAdapter";
    ArrayList<NEW_BackLogAttendanceModel> itemList;
    Context mContext;
    ArrayList<String> dayTypeArray;
    private Dialog shiftAndLocationDialog;
    Dialog searchWbsCodeDialog;
    ArrayList<LocationModel> locationArrayList;
    ArrayList<MetsoShiftModel> metsoShiftList;
    ArrayList<SpineerItemModel> supervisorList;
    String isDayTypeSelectionRequired;
    String isShiftSelectionRequired;
    String isLocationSelectionRequired;
    String isApproverRequired;
    ShiftSpinnerAdapter shiftSpinnerAdapter;
    NEW_LocationSpinnerAdapter locationSpinnerAdapter;
    public void setIsApproverRequired(String isApproverRequired) {
        this.isApproverRequired = isApproverRequired;
    }

    public void setIsLocationSelectionRequired(String isLocationSelectionRequired) {
        this.isLocationSelectionRequired = isLocationSelectionRequired;
        locationSpinnerAdapter = new NEW_LocationSpinnerAdapter(mContext, locationArrayList);
    }

    public void setIsShiftSelectionRequired(String isShiftSelectionRequired) {
        this.isShiftSelectionRequired = isShiftSelectionRequired;
        shiftSpinnerAdapter = new ShiftSpinnerAdapter(mContext, metsoShiftList);
    }

    public void setIsDayTypeSelectionRequired(String isDayTypeSelectionRequired) {
        this.isDayTypeSelectionRequired = isDayTypeSelectionRequired;
    }


    public NEW_BacklogAdapter(ArrayList<NEW_BackLogAttendanceModel> itemList, ArrayList<String> dayTypeArray, Context mContext,ArrayList<MetsoShiftModel> metsoShiftList,
                              ArrayList<LocationModel> locationArrayList,ArrayList<SpineerItemModel> supervisorList) {
        this.itemList = itemList;
        this.mContext = mContext;
        this.dayTypeArray = dayTypeArray;
        this.metsoShiftList = metsoShiftList;
        this.locationArrayList = locationArrayList;
        this.supervisorList = supervisorList;
        shiftAndLocationDialog =  new Dialog(mContext);
    }



    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_back_attendance_row, parent, false);
        return new MyViewholder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, @SuppressLint("RecyclerView") int position) {
        if (itemList.get(position).isSelected()){
            holder.imgLike.setVisibility(View.VISIBLE);
            ((NEW_AttendanceRegularizationActivity) mContext).updateItemStatus(position, true );
        } else {
            holder.imgLike.setVisibility(View.GONE);
            ((NEW_AttendanceRegularizationActivity) mContext).updateItemStatus(position, false);
        }

        holder.tvDate.setText(itemList.get(position).getDate());
        holder.tvInTime.setText(itemList.get(position).getInTime());
        holder.tvOutTime.setText(itemList.get(position).getOutTime());
        holder.tvDayType.setText(itemList.get(position).getDayType());

        holder.llLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: called");
                if (itemList.get(position).isSelected()){
                    holder.imgLike.setVisibility(View.GONE);
                    itemList.get(position).setSelected(false);
                    ((NEW_AttendanceRegularizationActivity) mContext).updateItemStatus(position, false);
                } else {
                    holder.imgLike.setVisibility(View.VISIBLE);
                    itemList.get(position).setSelected(true);
                    ((NEW_AttendanceRegularizationActivity) mContext).updateItemStatus(position, true);
                }
            }
        });

        holder.imgInTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(mContext,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                //txtTime.setText(hourOfDay + ":" + minute);
                                String intime = TimeConversion.convert_HH_mm_To_HH_mm_ss(hourOfDay + ":" + minute);
                                itemList.get(position).setInTime(intime);
                                holder.tvInTime.setText(itemList.get(position).getInTime());
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        holder.imgOutTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(mContext,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                //txtTime.setText(hourOfDay + ":" + minute);
                                String outTime = TimeConversion.convert_HH_mm_To_HH_mm_ss(hourOfDay + ":" + minute);
                                itemList.get(position).setOutTime(outTime);
                                holder.tvOutTime.setText(itemList.get(position).getOutTime());


                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        holder.tvDayType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dayTypeArray != null){
                    openDayTypePopup(mContext,holder,position);
                }
            }
        });
        holder.edtRemarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                itemList.get(position).setRemarks2(editable.toString().trim());
            }
        });
        if (isShiftSelectionRequired.equals("1")){
            holder.tvShift.setVisibility(View.VISIBLE);
        } else {
            holder.tvShift.setVisibility(View.GONE);
        }
        if (isLocationSelectionRequired.equals("1")){
            holder.tvLocation.setVisibility(View.VISIBLE);
        } else {
            holder.tvLocation.setVisibility(View.GONE);
        }
        if(isApproverRequired.equals("1")){
            holder.tvApprover.setVisibility(View.VISIBLE);
        } else {
            holder.tvApprover.setVisibility(View.GONE);
        }
        holder.tvShift.setText(itemList.get(position).getShift());
        holder.tvShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openShiftAndLocationPopup(1,position,itemList,holder);
            }
        });
        holder.tvLocation.setText(itemList.get(position).getLocation());
        holder.tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openShiftAndLocationPopup(2,position,itemList,holder);
            }
        });
        holder.tvApprover.setText(itemList.get(position).getApprover());
        holder.tvApprover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSupervisorPopUp(holder,position,itemList);
            }
        });

        if (itemList.get(position).getRemarks().equals("C")){
            holder.txtRemarks.setVisibility(View.GONE);
        } else {
            holder.txtRemarks.setVisibility(View.VISIBLE);
            if (itemList.get(position).getRemarksCode().equals("0")){
                //holder.txtRemarks.setTextColor(Color.parseColor("#FF4CAF50"));
                if (itemList.get(position).getRemarks() == null){
                    holder.txtRemarks.setVisibility(View.GONE);
                }
            } else if (itemList.get(position).getRemarksCode().equals("1")){
                holder.txtRemarks.setTextColor(Color.parseColor("#FFD1BF20"));
                holder.txtRemarks.setText(itemList.get(position).getRemarks());
            } else if (itemList.get(position).getRemarksCode().equals("2")){
                holder.txtRemarks.setTextColor(Color.parseColor("#000000"));
                holder.txtRemarks.setText(itemList.get(position).getRemarks());
            } else if (itemList.get(position).getRemarksCode().equals("3")){
                holder.txtRemarks.setTextColor(Color.parseColor("#FFF44336"));
                holder.txtRemarks.setText(itemList.get(position).getRemarks());
            }
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class MyViewholder extends RecyclerView.ViewHolder{
        TextView tvDate, tvInTime,tvOutTime,tvDayType,txtRemarks,tvShift,tvLocation,tvApprover;
        ImageView imgLike,imgInTime,imgOutTime;
        LinearLayout llLike;
        EditText edtRemarks;
        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvInTime = itemView.findViewById(R.id.tvInTime);
            tvOutTime = itemView.findViewById(R.id.tvOutTime);
            txtRemarks = itemView.findViewById(R.id.txtRemarks);
            tvShift = itemView.findViewById(R.id.tvShift);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvApprover = itemView.findViewById(R.id.tvApprover);
            imgLike = itemView.findViewById(R.id.imgLike);
            llLike = itemView.findViewById(R.id.llLike);
            tvDayType = itemView.findViewById(R.id.tvDayType);
            imgInTime = itemView.findViewById(R.id.imgInTime);
            imgOutTime = itemView.findViewById(R.id.imgOutTime);
            edtRemarks = itemView.findViewById(R.id.edtRemarks);
        }
    }

    public void selectAll(){
        notifyDataSetChanged();
    }

    private void openDayTypePopup(Context context, MyViewholder holder, int position) {
        Dialog openGradPopup = new Dialog(context,R.style.CustomDialogNew2);
        openGradPopup.setContentView(R.layout.grad_selection_popup);
        openGradPopup.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        openGradPopup.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        RecyclerView rvGrad = openGradPopup.findViewById(R.id.rvGrad);
        LinearLayout lnCancel = openGradPopup.findViewById(R.id.lnCancel);
        rvGrad.setLayoutManager(new LinearLayoutManager(context));
        SKF_DayTypeAdapter skfDayTypeAdapter = new SKF_DayTypeAdapter(mContext,dayTypeArray);
        rvGrad.setAdapter(skfDayTypeAdapter);
        skfDayTypeAdapter.setNewSetOnGradSelect(new setOnDayTypeSelect() {
            @Override
            public void onClick(int pos, String dayType) {
                itemList.get(position).setDayType(dayType);
                holder.tvDayType.setText(dayType);
                openGradPopup.cancel();
            }
        });

        lnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGradPopup.cancel();
            }
        });


        Window window = openGradPopup.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.NO_GRAVITY;
        openGradPopup.setCancelable(false);
        openGradPopup.show();
    }

    public interface setOnDayTypeSelect{
        void onClick(int position,String dayType);
    }

    private void openShiftAndLocationPopup(int Option,int position, ArrayList<NEW_BackLogAttendanceModel> itemList,MyViewholder holder) {
        shiftAndLocationDialog.setContentView(R.layout.shift_location_popup);
        shiftAndLocationDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        shiftAndLocationDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        LinearLayout lnCancel = shiftAndLocationDialog.findViewById(R.id.lnCancel);
        LinearLayout llShift = shiftAndLocationDialog.findViewById(R.id.llShift);
        LinearLayout llLocation = shiftAndLocationDialog.findViewById(R.id.llLocation);
        LinearLayout llApprover = shiftAndLocationDialog.findViewById(R.id.llApprover);
        TextView txtSelectShift = shiftAndLocationDialog.findViewById(R.id.txtSelectShift);
        TextView txtSelectLocation = shiftAndLocationDialog.findViewById(R.id.txtSelectLocation);
        TextView txtSelectApprover = shiftAndLocationDialog.findViewById(R.id.txtSelectApprover);
        TextView txtErrorShift = shiftAndLocationDialog.findViewById(R.id.txtErrorShift);
        TextView txtErrorLocation = shiftAndLocationDialog.findViewById(R.id.txtErrorLocation);
        TextView txtErrorApprover = shiftAndLocationDialog.findViewById(R.id.txtErrorApprover);
        Spinner spShift = shiftAndLocationDialog.findViewById(R.id.spShift);
        Spinner spLocation = shiftAndLocationDialog.findViewById(R.id.spLocation);
        AppCompatButton btnMarkedYourAttendance = shiftAndLocationDialog.findViewById(R.id.btnMarkedYourAttendance);
        final String[] Siteid = {""};
        final long[] Shiftid = {0};
        llApprover.setVisibility(View.GONE);

        if (Option==1){
            spShift.setAdapter(shiftSpinnerAdapter);
            spShift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    MetsoShiftModel clickedItem = (MetsoShiftModel) adapterView.getItemAtPosition(i);
                    if (!clickedItem.getColumn1().equals("Select Shift")) {
                        txtSelectShift.setText(clickedItem.getColumn1());
                        holder.tvShift.setText(clickedItem.getColumn1());
                        itemList.get(position).setShift(clickedItem.getColumn1());
                        Shiftid[0] = (long) clickedItem.getWorkingShiftID();
                        txtErrorShift.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        } else {
            llShift.setVisibility(View.GONE);
        }

        if(Option==2){
            spLocation.setAdapter(locationSpinnerAdapter);
            spLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    LocationModel clickedItem = (LocationModel) adapterView.getItemAtPosition(i);
                    if (!clickedItem.getSiteName().equals("Select Location")) {
                        txtSelectLocation.setText(clickedItem.getSiteName());
                        holder.tvLocation.setText(clickedItem.getSiteName());
                        Siteid[0] = clickedItem.getSiteid();
                        itemList.get(position).setLocation(clickedItem.getSiteName());
                        txtErrorLocation.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        } else {
            llLocation.setVisibility(View.GONE);
        }


        txtSelectShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spShift.performClick();
            }
        });

        txtSelectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spLocation.performClick();
            }
        });

        txtSelectApprover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        lnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shiftAndLocationDialog.cancel();
            }
        });

        btnMarkedYourAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llShift.getVisibility()==View.VISIBLE && txtSelectShift.getText().toString().isEmpty()){
                    txtErrorShift.setVisibility(View.VISIBLE);
                    return;
                }
                if (llLocation.getVisibility()==View.VISIBLE && txtSelectLocation.getText().toString().isEmpty()){
                    txtErrorLocation.setVisibility(View.VISIBLE);
                    return;
                }
                if (llApprover.getVisibility()==View.VISIBLE ){
                    txtErrorApprover.setVisibility(View.VISIBLE);
                    return;
                } else {
                    txtErrorApprover.setVisibility(View.GONE);
                }
                if (llShift.getVisibility()==View.VISIBLE){
                    itemList.get(position).setShiftID(Shiftid[0]);
                }
                if (llLocation.getVisibility()==View.VISIBLE){
                    itemList.get(position).setLocationID(Siteid[0]);
                }
                if (llApprover.getVisibility()==View.VISIBLE){

                }
                shiftAndLocationDialog.cancel();
            }
        });

        Window window = shiftAndLocationDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.NO_GRAVITY;
        shiftAndLocationDialog.setCancelable(false);
        shiftAndLocationDialog.show();
    }

    private void openSupervisorPopUp(MyViewholder holder,int position,ArrayList<NEW_BackLogAttendanceModel> itemList) {
        searchWbsCodeDialog = new Dialog(mContext, R.style.CustomDialogNew2);
        searchWbsCodeDialog.setContentView(R.layout.wbs_code_search_layout);
        searchWbsCodeDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        searchWbsCodeDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        searchWbsCodeDialog.setCancelable(true);

        TextView txtPopupHeadline = searchWbsCodeDialog.findViewById(R.id.txtPopupHeadline);
        SearchView wbsCodeSearchView = (SearchView) searchWbsCodeDialog.findViewById(R.id.wbsCodeSearchView);
        ImageView imgCancel = searchWbsCodeDialog.findViewById(R.id.imgCancel);
        RecyclerView rvWbsCode = searchWbsCodeDialog.findViewById(R.id.rvWbsCode);

        wbsCodeSearchView.setQueryHint("Search Approver");
        txtPopupHeadline.setText("Select Approver");
        rvWbsCode.setLayoutManager(new LinearLayoutManager(mContext));
        ArrayList<SpineerItemModel> supervisorListCopy = new ArrayList<>();
        supervisorListCopy = (ArrayList<SpineerItemModel>) supervisorList.clone();
        SupervisorFilterAdapter supervisorFilterAdapter = new SupervisorFilterAdapter(mContext, supervisorListCopy);
        rvWbsCode.setAdapter(supervisorFilterAdapter);

        wbsCodeSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                supervisorFilterAdapter.getFilter().filter(s);
                return false;
            }
        });

        supervisorFilterAdapter.setSupervisorSelectListener(new MetsoNewReimbursementClaimActivity.SupervisorSelectListener() {
            @Override
            public void onClick(String supervisor_id, String supervisor) {
                itemList.get(position).setApprover(supervisor);
                itemList.get(position).setApproverID(supervisor_id);
                holder.tvApprover.setText(supervisor);
                searchWbsCodeDialog.dismiss();
            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchWbsCodeDialog.dismiss();
            }
        });
        searchWbsCodeDialog.show();
    }
}
