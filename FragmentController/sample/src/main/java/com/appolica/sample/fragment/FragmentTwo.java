package com.appolica.sample.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appolica.fragmentcontroller.FragmentController;
import com.appolica.fragmentcontroller.fragment.DisabledAnimationFragment;
import com.appolica.sample.R;
import com.appolica.sample.databinding.FragmentTwoBinding;

public class FragmentTwo extends DisabledAnimationFragment {
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

        ((TextView) view.findViewById(R.id.fragmentLabel)).setText("Test text");

        view.findViewById(R.id.buttonNext2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentsType fragmentsType = FragmentsType.ONE;

                getFragmentController().pushBody()
                        .addToBackStack(true)
                        .withAnimation(true)
                        .fragment(fragmentsType, fragmentsType.getTag())
                        .push();
            }
        });

        view.findViewById(R.id.buttonPopTo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentController().popTo(FragmentsType.TWO, true, false);
            }
        });

        view.findViewById(R.id.buttonPrev2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentController().popToRoot();

                getFragmentController().pushBody()
                        .fragment(FragmentsType.TWO, FragmentsType.TWO.getTag())
                        .withAnimation(false)
                        .addToBackStack(true)
                        .immediate(true)
                        .push();

//                getFragmentManager().executePendingTransactions();
            }
        });
    }

    private FragmentController getFragmentController() {
        return (FragmentController) getParentFragment();
    }

    @Override
    public void disableNextAnimation() {
        animateTransitions = false;
    }
}
