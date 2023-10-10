package io.cordova.myapp00d753.fragment;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.utility.Pref;

/**
 * A simple {@link Fragment} subclass.
 */
public class OfficalFragment extends Fragment {

    View view;
    TextView tvEmplId,tvEmpCode,tvEmpName,tvDOJ,tvDepartment,tvDesignation,tvLocation;
    Pref pref;
    LinearLayout lnDept,lnEmpCode;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_offical, container, false);
        iniitView();
        return  view;

    }

    private void iniitView(){
        pref=new Pref(getContext());
        tvEmplId=(TextView)view.findViewById(R.id.tvEmplId);
        tvEmplId.setText(pref.getSEmpID());
        tvEmpCode=(TextView)view.findViewById(R.id.tvEmpCode);
        tvEmpCode.setText(pref.getSEmpCode());
        tvEmpName=(TextView)view.findViewById(R.id.tvEmpName);
        tvEmpName.setText(pref.getSEmpName());
        tvDOJ=(TextView)view.findViewById(R.id.tvDOJ);
        tvDOJ.setText(pref.getSDOJ());
        tvDepartment=(TextView)view.findViewById(R.id.tvDepartment);
        tvDepartment.setText(pref.getSDept());
        tvDesignation=(TextView)view.findViewById(R.id.tvDesignation);
        tvDesignation.setText(pref.getSDes());
        tvLocation=(TextView)view.findViewById(R.id.tvLocation);
        tvLocation.setText(pref.getSLocation());
        lnEmpCode=(LinearLayout) view.findViewById(R.id.lnEmpCode);
        lnDept=(LinearLayout) view.findViewById(R.id.lnDept);

        if (pref.getEmpClintId().equals("AEMCLI2310001813")){
            lnEmpCode.setVisibility(View.GONE);
            lnDept.setVisibility(View.GONE);
        }else {
            lnEmpCode.setVisibility(View.VISIBLE);
            lnDept.setVisibility(View.VISIBLE);
        }
    }

}
