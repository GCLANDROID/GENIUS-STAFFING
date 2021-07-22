package io.cordova.myapp00d753.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.utility.Pref;


public class MisFragment extends Fragment {

   View view;
   TextView tvPfNumber,tvEsiNumber,tvBankName,tvAcNumber,tvAddharNumber,tvUanNumber;
   Pref pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_mis, container, false);
        initView();
        return  view;
    }

    private void initView(){
        pref=new Pref(getContext());
        tvPfNumber=(TextView)view.findViewById(R.id.tvPfNumber);
        tvPfNumber.setText(pref.getSPF());
        tvEsiNumber=(TextView)view.findViewById(R.id.tvEsiNumber);
        tvEsiNumber.setText(pref.getSESI());
        tvBankName=(TextView)view.findViewById(R.id.tvBankName);
        tvBankName.setText(pref.getSBank());
        tvAcNumber=(TextView)view.findViewById(R.id.tvAcNumber);
        tvAcNumber.setText(pref.getSAcc());
        tvAddharNumber=(TextView)view.findViewById(R.id.tvAddharNumber);
        tvAddharNumber.setText(pref.getSAadhar());
        tvUanNumber=(TextView)view.findViewById(R.id.tvUanNumber);
        tvUanNumber.setText(pref.getSUAN());

    }


}
