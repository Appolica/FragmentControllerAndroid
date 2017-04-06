package com.appolica.fragmentcontroller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appolica.fragmentcontroller.fragment.FragmentProvider;
import com.appolica.fragmentcontroller.fragment.FragmentProviderImpl;
import com.appolica.fragmentcontroller.fragment.animation.TransitionAnimationManager;
import com.appolica.fragmentcontroller.util.FragmentUtil;

import org.jetbrains.annotations.Contract;

import java.io.Serializable;
import java.util.List;

public class FragmentController extends Fragment implements PushBody.PushBodyConsumer, OnBackPressedListener {
    public static final String FRAGMENT_TYPE_ARGUMENT = "fragmentTypeArgument";
    private static final String ROOT_FRAGMENT_TAG = "root";

    /**
     *
     * @param rootClass
     * @return
     */
    public static FragmentController instance(Class<? extends Fragment> rootClass) {
        final FragmentController controller = new FragmentController();

        final Bundle args = new Bundle();
        args.putSerializable(FragmentController.FRAGMENT_TYPE_ARGUMENT, rootClass);

        controller.setArguments(args);

        return controller;
    }

    /**
     *
     */
    public FragmentController() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final FragmentProvider rootType = getRootFromArgs();

        addRoot(savedInstanceState, rootType);

        return inflater.inflate(R.layout.fragment_container, container, false);
    }

    private FragmentProvider getRootFromArgs() {
        final Bundle arguments = getArguments();

        final FragmentProvider fragmentType;
        final Serializable serializedClass = arguments.getSerializable(FRAGMENT_TYPE_ARGUMENT);

        if (arguments == null || serializedClass == null) {

            throw new IllegalStateException("Root fragment is not defined!");

        } else {

            if (!(serializedClass instanceof Class)) {
                throw new IllegalStateException("You must provide provide root fragment of type Class<? extends Fragment>.");
            }

            final Class<? extends Fragment> rootClass = (Class<? extends Fragment>) serializedClass;
            fragmentType = new FragmentProviderImpl(rootClass);
        }

        return fragmentType;
    }

    private void addRoot(Bundle savedInstanceState, FragmentProvider fragmentType) {
        if (savedInstanceState == null) {
            pushBody()
                    .addToBackStack(true)
                    .fragment(fragmentType, ROOT_FRAGMENT_TAG)
                    .push();
        }
    }

    /**
     *
     * @return
     */
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

    /**
     *
     * @param withAnimation
     * @return
     */
    public boolean pop(boolean withAnimation) {
        final FragmentManager fragmentManager = getChildFragmentManager();

        disableLastEntryAnimation(withAnimation, fragmentManager);

        return fragmentManager.getBackStackEntryCount() != 1 &&
                fragmentManager.popBackStackImmediate();
    }

    /**
     *
     * @param withAnimation
     */
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

    /**
     *
     * @param fragmentType
     * @param inclusive
     * @param withAnimation
     * @return
     */
    public boolean popTo(FragmentProvider fragmentType, boolean inclusive, boolean withAnimation) {
        final FragmentManager fragmentManager = getChildFragmentManager();

        if (!withAnimation) {
            disableNextAnimationTo(fragmentManager, fragmentType.getTag());
        }

        int flag = getFlagInclusive(inclusive);

        return fragmentManager.popBackStackImmediate(fragmentType.getTag(), flag);
    }

    /**
     *
     * @param fragmentType
     * @param inclusive
     * @param withAnimation
     */
    public void popToAsync(FragmentProvider fragmentType, boolean inclusive, boolean withAnimation) {
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

    /**
     *
     * @return
     */
    public boolean popToRoot() {
        return getChildFragmentManager().popBackStackImmediate(ROOT_FRAGMENT_TAG, 0);
    }

    /**
     *
     */
    public void popToRootAsync() {
        getChildFragmentManager().popBackStack(ROOT_FRAGMENT_TAG, 0);
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
