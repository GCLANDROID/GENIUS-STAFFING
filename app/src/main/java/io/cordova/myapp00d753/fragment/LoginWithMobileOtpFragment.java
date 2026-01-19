package io.cordova.myapp00d753.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import io.cordova.myapp00d753.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginWithMobileOtpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginWithMobileOtpFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginWithMobileOtpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginWithMobileOtpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginWithMobileOtpFragment newInstance(String param1, String param2) {
        LoginWithMobileOtpFragment fragment = new LoginWithMobileOtpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    ImageView imgMobileImage;
    Button btnSendOTP;
    LinearLayout llOtpInput,llMobileNumberInput;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login_with_mobile_otp, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        btnClick();
    }

    private void initView(View view) {
        imgMobileImage = view.findViewById(R.id.imgMobileImage);
        btnSendOTP = view.findViewById(R.id.btnSendOTP);
        llOtpInput = view.findViewById(R.id.llOtpInput);
        llMobileNumberInput = view.findViewById(R.id.llMobileNumberInput);
        Glide.with(requireActivity())
                .asGif()
                .load(R.drawable.password)
                .into( imgMobileImage);
    }

    private void btnClick() {
        btnSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llOtpInput.setVisibility(View.VISIBLE);
                llMobileNumberInput.setVisibility(View.GONE);
            }
        });
    }
}