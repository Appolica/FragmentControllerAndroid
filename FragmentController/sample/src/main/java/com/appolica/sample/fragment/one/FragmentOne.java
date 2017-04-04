package com.appolica.sample.fragment.one;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appolica.fragmentcontroller.FragmentController;
import com.appolica.fragmentcontroller.fragment.DisabledAnimationFragment;
import com.appolica.sample.R;
import com.appolica.sample.databinding.FragmentOneBinding;
import com.appolica.sample.fragment.FragmentsType;
import com.appolica.sample.model.TestModel;


public class FragmentOne extends DisabledAnimationFragment implements FragmentOneClickListener {
    private static final String MODEL_STATE_KEY = "model_key";
    private static final String TAG = "FragmentOne";
    private FragmentOneBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_one, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TestModel model;
        if (savedInstanceState == null) {
            model = new TestModel("incrementable num: ", 0);
        } else {
            model = (TestModel) savedInstanceState.getSerializable(MODEL_STATE_KEY);
        }

        binding.setModel(model);
        binding.setClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(MODEL_STATE_KEY, binding.getModel());
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
                .fragment(FragmentsType.TWO)
                .push();
    }

    @Override
    public void onPopToRootClick() {
        getFragmentController().popToRoot();
    }

    @Override
    public void onBtnIncrementClick() {
        final TestModel model = binding.getModel();
        model.setNumber(model.getNumber() + 1);
    }

    private FragmentController getFragmentController() {
        return (FragmentController) getParentFragment();
    }
}
