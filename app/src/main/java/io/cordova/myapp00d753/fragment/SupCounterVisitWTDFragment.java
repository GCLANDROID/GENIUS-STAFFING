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


public class SupCounterVisitWTDFragment extends Fragment {

    View view;
    LinearLayout llNormal,llGraphical,llNormalD,llGraphicalD;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_counter_visit_y_t_d, container, false);
        initView();
        loadGraphicalYTDFragment();
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
                loadNormalYTDFragment();
            }
        });

        llGraphical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadGraphicalYTDFragment();
            }
        });
    }

    public void loadNormalYTDFragment() {

        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SupCounterVisitNormalWTDFragment pfragment=new SupCounterVisitNormalWTDFragment();
        transaction.replace(R.id.frameLayout1, pfragment);
        transaction.commit();

        llNormalD.setVisibility(View.VISIBLE);
        llGraphicalD.setVisibility(View.GONE);




    }


    public void loadGraphicalYTDFragment() {

        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        CounterVisitGraphicalWTDFragment pfragment=new CounterVisitGraphicalWTDFragment();
        transaction.replace(R.id.frameLayout1, pfragment);
        transaction.commit();

        llNormalD.setVisibility(View.GONE);
        llGraphicalD.setVisibility(View.VISIBLE);




    }
}