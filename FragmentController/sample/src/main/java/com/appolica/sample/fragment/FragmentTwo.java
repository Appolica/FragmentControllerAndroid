package com.appolica.sample.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;

import com.appolica.fragmentcontroller.FragmentController;
import com.appolica.fragmentcontroller.TransitionAnimationManager;
import com.appolica.sample.R;
import com.appolica.sample.databinding.FragmentTwoBinding;

public class FragmentTwo extends Fragment implements TransitionAnimationManager {
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
                getFragmentController().pop(true);
            }
        });
    }

    private FragmentController getFragmentController() {
        return (FragmentController) getParentFragment();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (!animateTransitions) {
            animateTransitions = true;
            Animation a = new Animation() {
            };
            a.setDuration(0);
            return a;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void disableNextAnimation() {
        animateTransitions = false;
    }
}
