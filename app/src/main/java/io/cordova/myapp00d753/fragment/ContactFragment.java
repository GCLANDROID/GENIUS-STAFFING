package io.cordova.myapp00d753.fragment;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.utility.Pref;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {


   View view;
   TextView tvParAddr,tvPreAddr,tvPhnNumber,tvEmail;
   Pref pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_contact, container, false);
        initView();
        return view;
    }

    private void initView(){
        pref=new Pref(getContext());
        tvParAddr=(TextView)view.findViewById(R.id.tvParAddr);
        tvParAddr.setText(pref.getSParAdd());
        tvPreAddr=(TextView)view.findViewById(R.id.tvPreAddr);
        tvPreAddr.setText(pref.getSPerAdd());
        tvPhnNumber=(TextView)view.findViewById(R.id.tvPhnNumber);
        tvPhnNumber.setText(pref.getSPhnNo());
        tvEmail=(TextView)view.findViewById(R.id.tvEmail);
        tvEmail.setText(pref.getSEmail());
    }

}
