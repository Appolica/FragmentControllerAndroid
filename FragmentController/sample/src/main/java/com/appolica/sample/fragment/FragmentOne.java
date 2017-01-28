package com.appolica.sample.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appolica.fragmentcontroller.FragmentController;
import com.appolica.sample.R;
import com.appolica.sample.databinding.FragmentOneBinding;
import com.appolica.sample.model.TestModel;

import java.lang.ref.SoftReference;

import static com.appolica.sample.R.id.buttonNext;
import static com.appolica.sample.R.id.buttonPrev;


public class FragmentOne extends Fragment {
    private static final String MODEL_STATE_KEY = "model_key";

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
        }

        binding.get().setModel(model);

        return binding.get().getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((TextView) view.findViewById(R.id.middleText)).setText("Test text");

        binding.get().getRoot().setBackgroundResource(android.R.color.holo_orange_light);

        view.findViewById(buttonNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentsType fragmentsType = FragmentsType.TWO;

                ((FragmentController) getParentFragment()).push(new FragmentController.PushBuilder()
                        .addToBackStack(true)
                        .fragment(fragmentsType, fragmentsType.getTag())
                        .build()
                );
            }
        });

        view.findViewById(buttonPrev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentController) getParentFragment()).pop();
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(MODEL_STATE_KEY, model);
    }
}
