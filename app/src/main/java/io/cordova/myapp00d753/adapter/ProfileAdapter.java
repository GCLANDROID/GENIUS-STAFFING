package io.cordova.myapp00d753.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.ProfileModule;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.MyViewHolder> {
    ArrayList<ProfileModule>profileList=new ArrayList<>();
    Context context;
    @NonNull
    @Override
    public ProfileAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.profile_raw,viewGroup,false);

        return new ProfileAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProfileAdapter.MyViewHolder myViewHolder, final int i) {
      //  final ProfileModule pmodel = profileList.get(i);
        if (!profileList.get(i).getEmailId().equals("")) {
            myViewHolder.tvEmpID.setText(profileList.get(i).getEmailId());
        }
        else {
            myViewHolder.tvEmpID.setText("N/A");
        }

        if (!profileList.get(i).getEmpCode().equals("")) {
            myViewHolder.tvEmpCode.setText(profileList.get(i).getEmpCode());
        }else {
            myViewHolder.tvEmpCode.setText("N/A");
        }
        if (!profileList.get(i).getEmpName().equals("")) {
            myViewHolder.tvEmpName.setText(profileList.get(i).getEmpName());
        }else {
            myViewHolder.tvEmpName.setText("N/A");
        }

        if (!profileList.get(i).getDOJ().equals("")) {
            myViewHolder.tvDOJ.setText(profileList.get(i).getDOJ());
        }else {
            myViewHolder.tvDOJ.setText("N/A");
        }
        if (profileList.get(i).getDepartment().equals("")) {
            myViewHolder.tvDep.setText(profileList.get(i).getDepartment());
        }else {
            myViewHolder.tvDep.setText("N/A");
        }
        if (!profileList.get(i).getDesignation().equals("")) {
            myViewHolder.tvDes.setText(profileList.get(i).getDesignation());
        }else {
            myViewHolder.tvDes.setText("N/A");
        }
        if (!profileList.get(i).getLocation().equals("")) {
            myViewHolder.tvLocation.setText(profileList.get(i).getLocation());

        }else {
            myViewHolder.tvLocation.setText("N/A");
        }
        if (!profileList.get(i).getGender().equals("")) {
            myViewHolder.tvGender.setText(profileList.get(i).getGender());
        }else {
            myViewHolder.tvGender.setText("N/A");
        }
        if (!profileList.get(i).getDOB().equals("")) {
            myViewHolder.tvDOB.setText(profileList.get(i).getDOB());
        }else {
            myViewHolder.tvDOB.setText("N/A");
        }
        if (!profileList.get(i).getGurdianName().equals("")) {
            myViewHolder.tvGurdianNAme.setText(profileList.get(i).getGurdianName());
        }
        else {
            myViewHolder.tvGurdianNAme.setText("N/A");
        }
        if (!profileList.get(i).getGurdianName().equals("")) {
            myViewHolder.tvRelationShip.setText(profileList.get(i).getRelationship());
        }else {
            myViewHolder.tvRelationShip.setText("N/A");
        }
        if (!profileList.get(i).getQualification().equals("")) {
            myViewHolder.tvQualification.setText(profileList.get(i).getQualification());
        }else {
            myViewHolder.tvQualification.setText("N/A");
        }
        if (!profileList.get(i).getMartialStatus().equals("")) {
            myViewHolder.tvMartialStatus.setText(profileList.get(i).getMartialStatus());
        }else {
            myViewHolder.tvMartialStatus.setText("N/A");
        }
        if (!profileList.get(i).getBloodGroup().equals("")) {
            myViewHolder.tvBloodGroup.setText(profileList.get(i).getBloodGroup());
        }
        else {
            myViewHolder.tvBloodGroup.setText("N/A");
        }
        if (!profileList.get(i).getPerAddress().equals("")) {
            myViewHolder.tvPerAdd.setText(profileList.get(i).getPerAddress());
        }else {
            myViewHolder.tvPerAdd.setText("N/A");
        }

        if (!profileList.get(i).getPreAddress().equals("")) {
            myViewHolder.tvPreAdd.setText(profileList.get(i).getPreAddress());
        }else {
            myViewHolder.tvPreAdd.setText("N/A");
        }

        if (!profileList.get(i).getPhoneNumber().equals("")) {
            myViewHolder.tvPhoneNumbver.setText(profileList.get(i).getPhoneNumber());
        }else {
            myViewHolder.tvPhoneNumbver.setText("N/A");
        }
        if (!profileList.get(i).getPhoneNumber().equals("")) {
            myViewHolder.tvMobileNumber.setText(profileList.get(i).getMobileNumber());
        }else {
            myViewHolder.tvMobileNumber.setText("N/A");
        }
        if (!profileList.get(i).getEmailId().equals("")) {
            myViewHolder.tvEmail.setText(profileList.get(i).getEmailId());
        }
        else {
            myViewHolder.tvEmail.setText("N/A");
        }
        if (!profileList.get(i).getPfNumber().equals("")) {
            myViewHolder.tvPFNumber.setText(profileList.get(i).getPfNumber());
        }else {
            myViewHolder.tvPFNumber.setText("N/A");
        }
        if (!profileList.get(i).getEsiNumber().equals("")) {
            myViewHolder.tvESINumber.setText(profileList.get(i).getEsiNumber());
        }else {
            myViewHolder.tvESINumber.setText("N/A");
        }
        if (!profileList.get(i).getBankName().equals("")) {
            myViewHolder.tvBankName.setText(profileList.get(i).getBankName());
        }else {
            myViewHolder.tvBankName.setText("N/A");
        }
        if (!profileList.get(i).getAccNumber().equals("")) {
            myViewHolder.tvAccNumber.setText(profileList.get(i).getAccNumber());
        }else {
            myViewHolder.tvAccNumber.setText("N/A");
        }
        if (!profileList.get(i).getAadharNumber().equals("")) {
            myViewHolder.tvAadharNumber.setText(profileList.get(i).getAadharNumber());
        }else {
            myViewHolder.tvAadharNumber.setText("N/A");
        }
        if (!profileList.get(i).getUanNumber().equals("")) {
            myViewHolder.tvUANNumber.setText(profileList.get(i).getUanNumber());
        }else {
            myViewHolder.tvUANNumber.setText("N/A");
        }

        myViewHolder.tvName.setText(profileList.get(i).getEmpName());
        myViewHolder.llName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myViewHolder.imgNamePlus.getVisibility()==View.VISIBLE){
                    myViewHolder.imgNamePlus.setVisibility(View.GONE);
                    myViewHolder.imgNameMinus.setVisibility(View.VISIBLE);
                    myViewHolder.llName.setVisibility(View.VISIBLE);
                    myViewHolder.llDetails.setVisibility(View.VISIBLE);
                }else {
                    myViewHolder.imgNamePlus.setVisibility(View.VISIBLE);
                    myViewHolder.imgNameMinus.setVisibility(View.GONE);
                    myViewHolder.llName.setVisibility(View.VISIBLE);
                    myViewHolder.llDetails.setVisibility(View.GONE);
                }



            }
        });


    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llName,llDetails;
        TextView tvEmpID,tvEmpCode,tvEmpName,tvName,tvDOJ,tvDep,tvDes,tvLocation,tvGender,tvDOB,tvGurdianNAme,tvRelationShip,tvQualification,tvMartialStatus,tvBloodGroup,tvPerAdd,tvPreAdd,tvPhoneNumbver,tvMobileNumber,tvEmail,tvPFNumber,tvESINumber,tvBankName,tvAccNumber,tvAadharNumber,tvUANNumber;
        ImageView imgNamePlus,imgNameMinus;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            llName=(LinearLayout)itemView.findViewById(R.id.llName);
            llDetails=(LinearLayout)itemView.findViewById(R.id.llDetails);

            tvEmpID=(TextView)itemView.findViewById(R.id.tvEmpId);
            tvEmpCode=(TextView)itemView.findViewById(R.id.tvEmpCode);
            tvEmpName=(TextView)itemView.findViewById(R.id.tvEmpName);
            tvDOJ=(TextView)itemView.findViewById(R.id.tvDOJ);
            tvDep=(TextView)itemView.findViewById(R.id.tvDep);
            tvDes=(TextView)itemView.findViewById(R.id.tvDes);
            tvLocation=(TextView)itemView.findViewById(R.id.tvLoc);
            tvGender=(TextView)itemView.findViewById(R.id.tvGender);
            tvDOB=(TextView)itemView.findViewById(R.id.tvDOB);
            tvGurdianNAme=(TextView)itemView.findViewById(R.id.tvGurdianName);
            tvRelationShip=(TextView)itemView.findViewById(R.id.tvRealtionShip);
            tvQualification=(TextView)itemView.findViewById(R.id.tvQualification);
            tvMartialStatus=(TextView)itemView.findViewById(R.id.tvMartialStatus);
            tvBloodGroup=(TextView)itemView.findViewById(R.id.tvBloodGroup);
            tvPerAdd=(TextView)itemView.findViewById(R.id.tvParAddr);
            tvPreAdd=(TextView)itemView.findViewById(R.id.tvPreAddr);
            tvPhoneNumbver=(TextView)itemView.findViewById(R.id.tvPhnNumber);
            tvMobileNumber=(TextView)itemView.findViewById(R.id.tvMobileNumber);
            tvEmail=(TextView)itemView.findViewById(R.id.tvEmail);
            tvPFNumber=(TextView)itemView.findViewById(R.id.tvPfNumber);
            tvESINumber=(TextView)itemView.findViewById(R.id.tvEsiNumber);
            tvBankName=(TextView)itemView.findViewById(R.id.tvBankName);
            tvAccNumber=(TextView)itemView.findViewById(R.id.tvAcNumber);
            tvAadharNumber=(TextView)itemView.findViewById(R.id.tvAddharNumber);
            tvUANNumber=(TextView)itemView.findViewById(R.id.tvUanNumber);
            tvName=(TextView)itemView.findViewById(R.id.tvName);

            imgNameMinus=(ImageView)itemView.findViewById(R.id.imgNameMins);
            imgNamePlus=(ImageView)itemView.findViewById(R.id.imgNamePlus);


        }
    }

    public ProfileAdapter(ArrayList<ProfileModule> profileList, Context context) {
        this.profileList = profileList;
        this.context = context;
    }
}
