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

/**
 * A simple {@link Fragment} subclass.
 */
public class SupMTDSaleFragment extends Fragment {


    View view;
    LinearLayout llNormal,llGraphical,llNormalD,llGraphicalD;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_sale_mtd, container, false);
        initView();
        loadGraphicalMTDFragment();
        onClick();
        return view;
    }

    private void initView(){
        llNormal=(LinearLayout)view.findViewById(R.id.llNormal);
        llGraphical=(LinearLayout)view.findViewById(R.id.llGraphical);

        llNormalD=(LinearLayout)view.findViewById(R.id.llNormalD);
        llGraphicalD=(LinearLayout)view.findViewById(R.id.llGraphicalD);

    }

    private void onClick(){
        llNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadNormalMTDFragment();
            }
        });

        llGraphical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadGraphicalMTDFragment();
            }
        });
    }

    public void loadNormalMTDFragment() {

        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SupNormalMTDFragment pfragment=new SupNormalMTDFragment();
        transaction.replace(R.id.frameLayout1, pfragment);
        transaction.commit();

        llNormalD.setVisibility(View.VISIBLE);
        llGraphicalD.setVisibility(View.GONE);




    }


    public void loadGraphicalMTDFragment() {

        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        GraphicalMTDFragment pfragment=new GraphicalMTDFragment();
        transaction.replace(R.id.frameLayout1, pfragment);
        transaction.commit();

        llNormalD.setVisibility(View.GONE);
        llGraphicalD.setVisibility(View.VISIBLE);





    }

}
