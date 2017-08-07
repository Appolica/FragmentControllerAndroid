package com.appolica.fragmentcontroller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appolica.fragmentcontroller.fragment.ControllerFragmentType;
import com.appolica.fragmentcontroller.fragment.FragmentTypeImpl;
import com.appolica.fragmentcontroller.fragment.animation.TransitionAnimationManager;
import com.appolica.fragmentcontroller.util.FragmentUtil;

import org.jetbrains.annotations.Contract;

import java.io.Serializable;
import java.util.List;

public class FragmentController extends Fragment implements PushBody.PushBodyConsumer, OnBackPressedListener {
    public static final String ARG_ROOT_FRAGMENT = FragmentController.class.getName() + ":ArgRootFragment";
    public static final String ARG_ROOT_TAG = FragmentController.class.getName() + ":ArgRootTAG";
    public static final String ARG_ROOT_BUNDLE = FragmentController.class.getName() + ":ArgRootBundle";

    public static FragmentController instance(ControllerFragmentType fragmentType) {
        return instance(fragmentType, fragmentType.getInstance().getArguments());
    }

    public static FragmentController instance(ControllerFragmentType fragmentType, Bundle rootArgs) {
        final FragmentController controller = new FragmentController();

        final Bundle args = new Bundle();

        args.putSerializable(ARG_ROOT_FRAGMENT, fragmentType.getInstance().getClass());
        args.putString(ARG_ROOT_TAG, fragmentType.getTag());
        args.putBundle(ARG_ROOT_BUNDLE, rootArgs);

        controller.setArguments(args);

        return controller;
    }

    public FragmentController() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final ControllerFragmentType rootType = getRootFromArgs();

        addRoot(savedInstanceState, rootType);

        return inflater.inflate(R.layout.fragment_container, container, false);
    }

    private ControllerFragmentType getRootFromArgs() {
        final Bundle arguments = getArguments();

        final ControllerFragmentType fragmentType;

        if (arguments == null
                || arguments.getSerializable(ARG_ROOT_FRAGMENT) == null
                || arguments.getString(ARG_ROOT_TAG) == null) {

            throw new IllegalStateException("Root fragment is not defined or tag not provided!");

        } else {

            final Serializable serializedClass = arguments.getSerializable(ARG_ROOT_FRAGMENT);
            final String tag = arguments.getString(ARG_ROOT_TAG);
            final Bundle rootArgs = arguments.getBundle(ARG_ROOT_BUNDLE);

            if (!(serializedClass instanceof Class)) {
                throw new IllegalStateException("You must provide provide root fragment of type Class<? extends Fragment>.");
            }

            final Class<? extends Fragment> rootClass = (Class<? extends Fragment>) serializedClass;
            fragmentType = new FragmentTypeImpl(rootClass, tag, rootArgs);
        }

        return fragmentType;
    }

    private void addRoot(Bundle savedInstanceState, ControllerFragmentType fragmentType) {
        if (savedInstanceState == null) {
            pushBody()
                    .addToBackStack(true)
                    .fragment(fragmentType)
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

        disableLastEntryAnimation(withAnimation, fragmentManager);

        return fragmentManager.getBackStackEntryCount() != 1 &&
                fragmentManager.popBackStackImmediate();
    }

    public void popAsync(boolean withAnimation) {
        final FragmentManager fragmentManager = getChildFragmentManager();

        disableLastEntryAnimation(withAnimation, fragmentManager);

        if (fragmentManager.getBackStackEntryCount() != 1) {
            fragmentManager.popBackStack();
        }
    }

    private void disableLastEntryAnimation(boolean withAnimation, FragmentManager fragmentManager) {
        if (!withAnimation) {
            final int lastEntry = fragmentManager.getBackStackEntryCount() - 1;
            final String lastTag = getTagFromEntry(fragmentManager, lastEntry);
            disableNextAnimationTo(fragmentManager, lastTag);
        }
    }

    public boolean popTo(ControllerFragmentType fragmentType, boolean inclusive, boolean withAnimation) {
        final FragmentManager fragmentManager = getChildFragmentManager();

        if (!withAnimation) {
            disableNextAnimationTo(fragmentManager, fragmentType.getTag());
        }

        int flag = getFlagInclusive(inclusive);

        return fragmentManager.popBackStackImmediate(fragmentType.getTag(), flag);
    }

    public void popToAsync(ControllerFragmentType fragmentType, boolean inclusive, boolean withAnimation) {
        final FragmentManager fragmentManager = getChildFragmentManager();

        if (!withAnimation) {
            disableNextAnimationTo(fragmentManager, fragmentType.getTag());
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

    private String getTagFromEntry(FragmentManager fragmentManager, int entry) {
        return fragmentManager.getBackStackEntryAt(entry).getName();
    }

    private void disableNextAnimationTo(FragmentManager fragmentManager, String tag) {
        final int entryCount = fragmentManager.getBackStackEntryCount();
        final int lastEntry = entryCount - 1;

        String entryTag = null;
        int entry = lastEntry;
        while (!tag.equals(entryTag) || entry > 0) {
            entryTag = getTagFromEntry(fragmentManager, entry);

            final Fragment fragment = fragmentManager.findFragmentByTag(entryTag);
            if (fragment instanceof TransitionAnimationManager) {
                ((TransitionAnimationManager) fragment).disableNextAnimation();
            }

            entry--;
        }
    }

    public boolean popToRoot() {
        final FragmentManager fragmentManager = getChildFragmentManager();
        final int entryCount = fragmentManager.getBackStackEntryCount();
        final int lastEntry = entryCount == 0 ? 0 : entryCount - 1;

        boolean popped = false;
        for (int index = lastEntry; index > 0; index--) {
            fragmentManager.popBackStack();
        }

        fragmentManager.executePendingTransactions();

        return lastEntry > 0;
    }

    public void popToRootAsync() {
        final FragmentManager fragmentManager = getChildFragmentManager();
        final int entryCount = fragmentManager.getBackStackEntryCount();
        final int lastEntry = entryCount == 0 ? 0 : entryCount - 1;

        for (int index = lastEntry; index > 0; index--) {
            fragmentManager.popBackStack();
        }
    }

    @Override
    public boolean onBackPressed() {
        final Fragment topFragment = getTopFragment();

        boolean handled = false;
        if (topFragment != null) {
            if (topFragment instanceof OnBackPressedListener) {
                handled = ((OnBackPressedListener) topFragment).onBackPressed();
            }
        }

        if (!handled) {
            handled = pop(true);
        }

        return handled;
    }

    @Nullable
    private Fragment getTopFragment() {
        final List<Fragment> fragments = FragmentUtil.getFragments(getChildFragmentManager());
        final int size = fragments.size();
        if (size > 0) {
            return fragments.get(size - 1);
        }

        return null;
    }

}
