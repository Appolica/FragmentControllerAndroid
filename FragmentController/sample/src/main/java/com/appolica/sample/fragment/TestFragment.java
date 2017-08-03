package com.appolica.sample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.appolica.fragmentcontroller.FragmentController;
import com.appolica.fragmentcontroller.OnBackPressedListener;
import com.appolica.sample.R;


public class TestFragment extends Fragment implements OnBackPressedListener {

    private static final String TAG = "TestFragment";
    private FragmentController childController;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.test_fr_anim, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        childController = FragmentController.instance(FragmentsType.ONE);

        getChildFragmentManager().beginTransaction()
                .replace(R.id.container, childController, "FrController")
                .commitNow();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
//        final Animation animation = AnimationUtils.loadAnimation(getContext(), nextAnim);

        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    private FragmentController getParentFrController() {
        return (FragmentController) getParentFragment();
    }

    public void onViewFullyAppeared() {
        Log.d(TAG, "onViewFullyAppeared: ");
        getParentFrController().pop(true);
    }

    @Override
    public boolean onBackPressed() {
        return childController.onBackPressed();
    }
}
