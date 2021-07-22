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
public class PersonalFragment extends Fragment {


   View view;
   TextView tvGender,tvEmpCodeDOB,tvGurdianName,tvRealtionShip,tvQualification,tvMarital,tvBloodGroup;
   Pref pref;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_personal, container, false);
        iniItView();
        return view;
    }

    private void iniItView(){
        pref=new Pref(getContext());
        tvGender=(TextView)view.findViewById(R.id.tvGender);
        tvGender.setText(pref.getSGender());
        tvEmpCodeDOB=(TextView)view.findViewById(R.id.tvEmpCodeDOB);
        tvEmpCodeDOB.setText(pref.getSDOB());
        tvGurdianName=(TextView)view.findViewById(R.id.tvGurdianName);
        tvGurdianName.setText(pref.getSGurdian());
        tvRealtionShip=(TextView)view.findViewById(R.id.tvRealtionShip);
        tvRealtionShip.setText(pref.getSRelation());
        tvQualification=(TextView)view.findViewById(R.id.tvQualification);
        tvQualification.setText(pref.getSQualification());
        tvMarital=(TextView)view.findViewById(R.id.tvMarital);
        tvMarital.setText(pref.getSMartial());
        tvBloodGroup=(TextView)view.findViewById(R.id.tvBloodGroup);
        tvBloodGroup.setText(pref.getSBlood());
    }

}
