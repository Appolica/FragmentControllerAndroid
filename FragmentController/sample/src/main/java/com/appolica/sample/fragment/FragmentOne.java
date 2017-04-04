package com.appolica.sample.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appolica.fragmentcontroller.FragmentController;
import com.appolica.fragmentcontroller.fragment.DisabledAnimationFragment;
import com.appolica.sample.R;
import com.appolica.sample.databinding.FragmentOneBinding;
import com.appolica.sample.model.TestModel;

import java.lang.ref.SoftReference;


public class FragmentOne extends DisabledAnimationFragment {
    private static final String MODEL_STATE_KEY = "model_key";
    private static final String TAG = "FragmentOne";
    private TestModel model;
    private SoftReference<FragmentOneBinding> binding;

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

        binding.get().middleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentController().popToRoot();
            }
        });

        binding.get().getRoot().setBackgroundResource(android.R.color.holo_orange_light);

        binding.get().buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentsType fragmentsType = FragmentsType.TWO;

                getFragmentController().pushBody()
                        .addToBackStack(true)
                        .withAnimation(true)
                        .fragment(fragmentsType, fragmentsType.getTag())
                        .push();
            }
        });

        binding.get().buttonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (getFragmentController()).pop(true);
            }
        });

        binding.get().modelEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setNumber(model.getNumber() + 1);
            }
        });
    }

    private FragmentController getFragmentController() {
        return (FragmentController) getParentFragment();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(MODEL_STATE_KEY, model);
    }
}
