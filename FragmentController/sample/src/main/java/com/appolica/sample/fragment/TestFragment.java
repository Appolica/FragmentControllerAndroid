package com.appolica.sample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.appolica.fragmentcontroller.FragmentController;
import com.appolica.sample.R;


public class TestFragment extends Fragment {

    private static final String TAG = "TestFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.test_fr_anim, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        final Animation animation = AnimationUtils.loadAnimation(getContext(), nextAnim);



        return animation;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private FragmentController getFragmentController() {
        return (FragmentController) getParentFragment();
    }

    public void onViewFullyAppeared() {
        Log.d(TAG, "onViewFullyAppeared: ");
        getFragmentController().pop(true);
    }
}
