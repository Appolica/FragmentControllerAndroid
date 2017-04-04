package com.appolica.sample.fragment.two;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appolica.fragmentcontroller.FragmentController;
import com.appolica.fragmentcontroller.fragment.DisabledAnimationFragment;
import com.appolica.sample.R;
import com.appolica.sample.databinding.FragmentTwoBinding;
import com.appolica.sample.fragment.FragmentsType;

public class FragmentTwo extends DisabledAnimationFragment implements FragmentTwoClickListener {
    private static final String TAG = "FragmentTwo";

    private FragmentTwoBinding binding;
    private boolean animateTransitions = true;

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

    }

    private FragmentController getFragmentController() {
        return (FragmentController) getParentFragment();
    }

    @Override
    public void disableNextAnimation() {
        animateTransitions = false;
    }

    @Override
    public void onPrevClick() {
        getFragmentController().pop(true);
    }

    @Override
    public void onNextClick() {
        getFragmentController().pushBody()
                .addToBackStack(true)
                .withAnimation(true)
                .fragment(FragmentsType.ONE)
                .push();
    }

    @Override
    public void onPopToClick() {
        getFragmentController().popTo(FragmentsType.TWO, true, false);
    }
}
