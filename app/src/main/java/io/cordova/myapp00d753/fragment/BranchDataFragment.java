package io.cordova.myapp00d753.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import io.cordova.myapp00d753.R;

public class BranchDataFragment extends Fragment {
    View view;
    LinearLayout llProduct,llProductD,llRevenue,llRevenueD;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_data, container, false);
        initView();
        loadProductWiseFragment();
        onClick();
        return view;
    }

    private void initView(){
        llProduct=(LinearLayout)view.findViewById(R.id.llProduct);
        llProductD=(LinearLayout)view.findViewById(R.id.llProductD);
        llRevenue=(LinearLayout)view.findViewById(R.id.llRevenue);
        llRevenueD=(LinearLayout)view.findViewById(R.id.llRevenueD);
    }

    public void loadProductWiseFragment() {

        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        BranchProductWiseFragment pfragment=new BranchProductWiseFragment();
        transaction.replace(R.id.frameLayout2, pfragment);
        transaction.commit();

        llProductD.setVisibility(View.VISIBLE);
        llRevenueD.setVisibility(View.GONE);




    }


    public void loadRevenueWiseWiseFragment() {

        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        BranchRevenueWiseFragment pfragment=new BranchRevenueWiseFragment();
        transaction.replace(R.id.frameLayout2, pfragment);
        transaction.commit();

        llProductD.setVisibility(View.GONE);
        llRevenueD.setVisibility(View.VISIBLE);




    }

    private void onClick(){
        llRevenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRevenueWiseWiseFragment();
            }
        });

        llProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadProductWiseFragment();
            }
        });
    }
}