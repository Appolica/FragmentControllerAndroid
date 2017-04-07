package com.appolica.sample.fragment.two;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appolica.fragmentcontroller.OnBackPressedListener;
import com.appolica.fragmentcontroller.fragment.DisabledAnimationFragment;
import com.appolica.fragmentcontroller.util.FragmentUtil;
import com.appolica.sample.R;
import com.appolica.sample.databinding.FragmentTwoBinding;
import com.appolica.sample.fragment.FragmentsType;

public class FragmentTwo extends DisabledAnimationFragment implements FragmentTwoClickListener, OnBackPressedListener {
    private static final String TAG = "FragmentTwo";

    private FragmentTwoBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_two, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.setClickListener(this);

        binding.buttonFail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtil.getFragmentController(FragmentTwo.this)
                        .pushBody()
                        .addToBackStack(true)
                        .withAnimation(true)
                        .fragment(FragmentsType.TEST)
                        .push();
            }
        });
    }

    @Override
    public void onPrevClick() {
        FragmentUtil.getFragmentController(this).pop(true);
    }

    @Override
    public void onNextClick() {
        FragmentUtil.getFragmentController(this)
                .pushBody()
                .addToBackStack(true)
                .withAnimation(true)
                .fragment(FragmentsType.ONE)
                .push();
    }

    @Override
    public void onPopToClick() {
        FragmentUtil.getFragmentController(this).popTo(FragmentsType.ONE, false, true);
    }

    @Override
    public boolean onBackPressed() {
        return FragmentUtil.getFragmentController(this).pop(true);
    }
}
