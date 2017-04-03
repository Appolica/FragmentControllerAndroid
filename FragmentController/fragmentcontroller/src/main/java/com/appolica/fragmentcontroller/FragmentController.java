package com.appolica.fragmentcontroller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appolica.fragmentcontroller.fragment.ControllerFragmentType;
import com.appolica.fragmentcontroller.fragment.animation.TransitionAnimationManager;

import org.jetbrains.annotations.Contract;

public class FragmentController extends Fragment implements PushBody.PushBodyConsumer {
    public static final String FRAGMENT_TYPE_ARGUMENT = "fragmentTypeArgument";
    private static final String ROOT_FRAGMENT_TAG = "root";

    public FragmentController() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final ControllerFragmentType fragmentType = getFragmentType();

        restore(savedInstanceState, fragmentType);

        return inflater.inflate(R.layout.fragment_container, container, false);
    }

    private ControllerFragmentType getFragmentType() {
        ControllerFragmentType fragmentType;
        if (getArguments() == null || getArguments().getSerializable(FRAGMENT_TYPE_ARGUMENT) == null) {
            throw new IllegalStateException("Root fragment is not defined!");
        } else {
            fragmentType = (ControllerFragmentType) getArguments().getSerializable(FRAGMENT_TYPE_ARGUMENT);
        }
        return fragmentType;
    }

    private void restore(Bundle savedInstanceState, ControllerFragmentType fragmentType) {
        if (savedInstanceState == null) {
            pushBody()
                    .addToBackStack(true)
                    .fragment(fragmentType, ROOT_FRAGMENT_TAG)
                    .push();
        }
    }

    public PushBody.Builder pushBody() {
        return PushBody.Builder.instance(this);
    }

    @Override
    public void push(PushBody body) {
        final FragmentManager fragmentManager = getChildFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (body.addToBackStack()) {
            fragmentTransaction.addToBackStack(body.getTag());
        }

        final PushBody.Builder.TransitionAnimationBody animations = body.getTransitionAnimations();
        if (animations != null) {
            fragmentTransaction.setCustomAnimations(
                    animations.getEnter(),
                    animations.getExit(),
                    animations.getPopEnter(),
                    animations.getPopExit());
        }

        fragmentTransaction
                .replace(R.id.fragmentPlace, body.getFragment(), body.getTag())
                .commit();

        if (body.immediate()) {
            fragmentManager.executePendingTransactions();
        }
    }

    public boolean pop(boolean withAnimation) {
        final FragmentManager fragmentManager = getChildFragmentManager();

        if (!withAnimation) {
            disableNextAnimation(fragmentManager);
        }

        return fragmentManager.getBackStackEntryCount() != 1 &&
                fragmentManager.popBackStackImmediate();
    }

    public void popAsync(boolean withAnimation) {
        final FragmentManager fragmentManager = getChildFragmentManager();

        if (!withAnimation) {
            disableNextAnimation(fragmentManager);
        }

        if (fragmentManager.getBackStackEntryCount() != 1) {
            fragmentManager.popBackStack();
        }
    }

    public boolean popTo(ControllerFragmentType fragmentType, boolean inclusive, boolean withAnimation) {
        final FragmentManager fragmentManager = getChildFragmentManager();

        if (!withAnimation) {
            disableNextAnimation(fragmentManager);
        }

        int flag = getFlagInclusive(inclusive);

        return fragmentManager.popBackStackImmediate(fragmentType.getTag(), flag);
    }

    public void popToAsync(ControllerFragmentType fragmentType, boolean inclusive, boolean withAnimation) {
        final FragmentManager fragmentManager = getChildFragmentManager();

        if (!withAnimation) {
            disableNextAnimation(fragmentManager);
        }

        int flag = getFlagInclusive(inclusive);

        fragmentManager.popBackStack(fragmentType.getTag(), flag);
    }

    @Contract(pure = true)
    private int getFlagInclusive(boolean inclusive) {
        int flag = 0;

        if (inclusive) {
            flag = FragmentManager.POP_BACK_STACK_INCLUSIVE;
        }

        return flag;
    }

    private void disableNextAnimation(FragmentManager fragmentManager) {
        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment instanceof TransitionAnimationManager) {
                ((TransitionAnimationManager) fragment).disableNextAnimation();
            }
        }
    }

    public boolean popToRoot() {
        return getChildFragmentManager().popBackStackImmediate(ROOT_FRAGMENT_TAG, 0);
    }

    public void popToRootAsync() {
        getChildFragmentManager().popBackStack(ROOT_FRAGMENT_TAG, 0);
    }

}

