package com.appolica.sample.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.appolica.fragmentcontroller.FragmentController;
import com.appolica.fragmentcontroller.TransitionAnimationManager;
import com.appolica.sample.R;
import com.appolica.sample.databinding.FragmentOneBinding;
import com.appolica.sample.model.TestModel;

import java.lang.ref.SoftReference;

import static com.appolica.sample.R.id.buttonNext;
import static com.appolica.sample.R.id.buttonPrev;


public class FragmentOne extends Fragment implements TransitionAnimationManager {
    private static final String MODEL_STATE_KEY = "model_key";
    private static final String TAG = "FragmentOne";
    private TestModel model;
    private SoftReference<FragmentOneBinding> binding;
    private boolean animateTransitions = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (binding == null) {
            binding = new SoftReference<>((FragmentOneBinding) DataBindingUtil.inflate(inflater, R.layout.fragment_one, container, false));

            if (savedInstanceState == null) {
                model = new TestModel("Test model number: ", 1);
            } else {
                model = (TestModel) savedInstanceState.getSerializable(MODEL_STATE_KEY);
            }
        } else if (binding.get() == null) {
            Log.d(TAG, "onCreateView: the binding is null.......");
            binding = new SoftReference<>((FragmentOneBinding) DataBindingUtil.inflate(inflater, R.layout.fragment_one, container, false));
        }

        binding.get().setModel(model);

        return binding.get().getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.middleText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentController) getParentFragment()).popToRoot();
            }
        });

        binding.get().getRoot().setBackgroundResource(android.R.color.holo_orange_light);

        view.findViewById(buttonNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentsType fragmentsType = FragmentsType.TWO;

                ((FragmentController) getParentFragment()).push(new FragmentController.PushBuilder()
                        .addToBackStack(true)
                        .withAnimation(true)
                        .fragment(fragmentsType, fragmentsType.getTag())
                        .build()
                );
            }
        });

        view.findViewById(buttonPrev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentController) getParentFragment()).pop(true);
            }
        });

        view.findViewById(R.id.modelEditButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setNumber(model.getNumber() + 1);
            }
        });
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(MODEL_STATE_KEY, model);
    }

    @Override
    public void disableNextAnimation() {
        animateTransitions = false;
    }
}
