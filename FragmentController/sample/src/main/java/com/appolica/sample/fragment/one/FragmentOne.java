/*
 * Copyright (c) 2017 Appolica Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.
 *
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.appolica.sample.fragment.one;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appolica.fragmentcontroller.FragmentController;
import com.appolica.fragmentcontroller.OnBackPressedListener;
import com.appolica.fragmentcontroller.fragment.DisabledAnimationFragment;
import com.appolica.sample.R;
import com.appolica.sample.databinding.FragmentOneBinding;
import com.appolica.sample.fragment.FragmentsType;
import com.appolica.sample.model.TestModel;


public class FragmentOne extends DisabledAnimationFragment implements FragmentOneClickListener, OnBackPressedListener {
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

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
