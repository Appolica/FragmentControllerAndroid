package com.appolica.fragmentcontroller.fragment;

import android.support.v4.app.Fragment;
import android.view.animation.Animation;

import com.appolica.fragmentcontroller.fragment.animation.TransitionAnimationManager;


public class DisabledAnimationFragment extends Fragment implements TransitionAnimationManager {

    private boolean animateTransitions = true;

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (!animateTransitions) {
            animateTransitions = true;
            Animation a = new Animation() { /* hack */ };
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
