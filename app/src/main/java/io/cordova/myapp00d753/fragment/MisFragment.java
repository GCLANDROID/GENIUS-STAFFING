package io.cordova.myapp00d753.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.utility.MaskingUtility;
import io.cordova.myapp00d753.utility.Pref;


public class MisFragment extends Fragment {

   View view;
   TextView tvPfNumber,tvEsiNumber,tvBankName,tvAcNumber,tvAddharNumber,tvUanNumber;
   Pref pref;
   ImageView imgPFEye,imgESIEye,imgACEye,imgAadhaarEye,imgUANEye;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //view= inflater.inflate(R.layout.fragment_mis, container, false);
        view= inflater.inflate(R.layout.fragment_miscellaneous, container, false);
        initView();
        return  view;
    }

    private void initView(){
        pref=new Pref(getContext());
        tvPfNumber=(TextView)view.findViewById(R.id.tvPfNumber);
        String maskedPF= MaskingUtility.maskPFNumber(pref.getSPF());
        tvPfNumber.setText(maskedPF);
        tvEsiNumber=(TextView)view.findViewById(R.id.tvEsiNumber);
        String maskedESINumber= MaskingUtility.maskESINumber( pref.getSESI());
        tvEsiNumber.setText(maskedESINumber);
        tvBankName=(TextView)view.findViewById(R.id.tvBankName);
        tvBankName.setText(pref.getSBank());
        tvAcNumber=(TextView)view.findViewById(R.id.tvAcNumber);
        String makedAcNo= MaskingUtility.maskBankAccount(pref.getSAcc());
        tvAcNumber.setText(makedAcNo);
        tvAddharNumber=(TextView)view.findViewById(R.id.tvAddharNumber);
        String maskedAdahr= MaskingUtility.maskAadhaar(pref.getSAadhar());
        tvAddharNumber.setText(maskedAdahr);
        tvUanNumber=(TextView)view.findViewById(R.id.tvUanNumber);
        String makedUAN= MaskingUtility.maskUAN(pref.getSUAN());
        tvUanNumber.setText(makedUAN);

        imgPFEye=view.findViewById(R.id.imgPFEye);
        imgESIEye=view.findViewById(R.id.imgESIEye);
        imgACEye=view.findViewById(R.id.imgACEye);
        imgAadhaarEye=view.findViewById(R.id.imgAadhaarEye);
        imgUANEye=view.findViewById(R.id.imgUANEye);

        imgPFEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaskingUtility.showUnmaskedDialog(getContext(),pref.getSPF(),"PF Number");


            }
        });

        imgESIEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaskingUtility.showUnmaskedDialog(getContext(),pref.getSESI(),"ESIC Number");


            }
        });

        imgACEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaskingUtility.showUnmaskedDialog(getContext(),pref.getSAcc(),"Bank A/C Number");


            }
        });


        imgAadhaarEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaskingUtility.showUnmaskedDialog(getContext(),pref.getSAadhar(),"Aadhaar Number");


            }
        });

        imgUANEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaskingUtility.showUnmaskedDialog(getContext(),pref.getSUAN(),"UAN Number");


            }
        });


    }


}
