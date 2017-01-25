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
import com.appolica.sample.databinding.FragmentTwoBinding;

public class FragmentTwo extends Fragment {
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

        ((TextView) view.findViewById(R.id.fragmentLabel)).setText("Test text");

        view.findViewById(R.id.buttonNext2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentsType fragmentsType = FragmentsType.ONE;

                ((FragmentController) getParentFragment()).push(new FragmentController.PushBuilder()
                        .addToBackStack(true)
                        .fragment(fragmentsType, fragmentsType.getTag())
                );
            }
        });

        view.findViewById(R.id.buttonPrev2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentController) getParentFragment()).pop();
            }
        });
    }
}
