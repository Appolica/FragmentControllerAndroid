package com.appolica.fragmentcontroller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Alexander Iliev on 25.01.17.
 * Copyright © 2017 Appolica. All rights reserved.
 */
public class FragmentController extends Fragment {
    public static final String FRAGMENT_TYPE_ARGUMENT = "fragmentTypeArgument";
    private static final String ROOT_FRAGMENT_TAG = "root";

    public FragmentController() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ControllerFragmentType fragmentType;

        if (getArguments() == null || getArguments().getSerializable(FRAGMENT_TYPE_ARGUMENT) == null) {
            throw new IllegalStateException("Root fragment is not defined!");
        } else {
            fragmentType = (ControllerFragmentType) getArguments().getSerializable(FRAGMENT_TYPE_ARGUMENT);
        }

        if (savedInstanceState == null) {
            push(new FragmentController.PushBuilder()
                    .addToBackStack(true)
                    .fragment(fragmentType, ROOT_FRAGMENT_TAG)
                    .build()
            );
        }
        return inflater.inflate(R.layout.fragment_container, container, false);
    }

    public void push(PushBody body) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();

        if (body.isToBackStack()) {
            fragmentTransaction.addToBackStack(body.getTag());
        }

        if (body.isWithAnimation()) {
            fragmentTransaction.setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right);
        }

        fragmentTransaction
                .replace(R.id.fragmentPlace, body.getFragment(), body.getTag())
                .commit();
    }

    public boolean pop(boolean withAnimation) {
        FragmentManager fragmentManager = getChildFragmentManager();

        if (!withAnimation) {
            for (Fragment fragment : fragmentManager.getFragments()) {
                if (fragment instanceof TransitionAnimationManager) {
                    ((TransitionAnimationManager) fragment).disableNextAnimation();
                }
            }
        }

        return fragmentManager.getBackStackEntryCount() != 1 &&
                fragmentManager.popBackStackImmediate();
    }

    public boolean popTo(ControllerFragmentType fragmentType, boolean inclusive, boolean withAnimation) {
        FragmentManager fragmentManager = getChildFragmentManager();

        if (!withAnimation) {
            for (Fragment fragment : fragmentManager.getFragments()) {
                if (fragment instanceof TransitionAnimationManager) {
                    ((TransitionAnimationManager) fragment).disableNextAnimation();
                }
            }
        }

        int flag = 0;

        if (inclusive) {
            flag = FragmentManager.POP_BACK_STACK_INCLUSIVE;
        }

        return fragmentManager.popBackStackImmediate(fragmentType.getTag(), flag);
    }

    public boolean popToRoot() {
        return getChildFragmentManager().popBackStackImmediate(ROOT_FRAGMENT_TAG, 0);
    }

    public static class PushBuilder {
        private ControllerFragmentType fragmentType;
        private String tag;
        private boolean toBackStack;
        private boolean withAnimation = false;

        public PushBuilder addToBackStack(boolean toBackStack) {
            this.toBackStack = toBackStack;
            return this;
        }

        public PushBuilder fragment(ControllerFragmentType fragmentType, String tag) {
            this.fragmentType = fragmentType;
            this.tag = tag;
            return this;
        }

        public PushBuilder withAnimation(boolean withAnimation) {
            this.withAnimation = withAnimation;
            return this;
        }

        public PushBody build() {
            if (fragmentType == null) {
                throw new IllegalStateException("FragmentType must not be null");
            } else if (tag == null) {
                throw new IllegalStateException("Tag must not be null");
            }

            return new PushBody(fragmentType, tag, toBackStack, withAnimation);
        }
    }

    private static class PushBody {
        private ControllerFragmentType fragmentType;
        private String tag;
        private boolean toBackStack;
        private boolean withAnimation;

        public PushBody(ControllerFragmentType fragmentType, String tag, boolean toBackStack, boolean withAnimation) {
            this.fragmentType = fragmentType;
            this.tag = tag;
            this.toBackStack = toBackStack;
            this.withAnimation = withAnimation;
        }

        public boolean isWithAnimation() {
            return withAnimation;
        }

        public Fragment getFragment() {
            return fragmentType.getInstance();
        }

        public String getTag() {
            return tag;
        }

        public boolean isToBackStack() {
            return toBackStack;
        }
    }
}

