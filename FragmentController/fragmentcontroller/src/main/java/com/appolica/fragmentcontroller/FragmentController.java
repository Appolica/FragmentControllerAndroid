/*
 * Copyright (c) 2017 Appolica Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.
 *
 *  You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

import java.io.Serializable;
import java.util.List;

/**
 * The core of the library. This is a fragment, that encapsulates all fragments you're going to
 * push/pop, using its own fragment manager, obtained with {@link Fragment#getChildFragmentManager()}.
 */
public class FragmentController extends Fragment implements PushBody.PushBodyConsumer, OnBackPressedListener {
    public static final String ARG_ROOT_FRAGMENT = FragmentController.class.getName() + ":ArgRootFragment";
    public static final String ARG_ROOT_TAG = FragmentController.class.getName() + ":ArgRootTag";

    /**
     * Instantiate the {@link FragmentController} by giving it a {@link FragmentProvider} that
     * will be used as a root fragment. The root fragment is the first one shown inside the
     * controller.
     *
     * @param provider {@link FragmentProvider} for the root fragment.
     * @return A new instance of {@link FragmentController}.
     */
    public static FragmentController instance(FragmentProvider provider) {
        final FragmentController controller = new FragmentController();

        final Bundle args = new Bundle();

        args.putSerializable(ARG_ROOT_FRAGMENT, provider.getInstance().getClass());
        args.putString(ARG_ROOT_TAG, provider.getTag());

        controller.setArguments(args);

        return controller;
    }

    /**
     *  This is an empty public constructor that is used by the framework. If you want to
     *  instantiate this fragment you have to use
     *  {@link FragmentController#instance(FragmentProvider)}
     *
     *  @see FragmentController#instance(FragmentProvider)
     */
    public FragmentController() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_container, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final FragmentProvider rootType = getRootFromArgs();
        addRoot(savedInstanceState, rootType);
    }

    private void addRoot(Bundle savedInstanceState, FragmentProvider fragmentType) {
        if (savedInstanceState == null) {
            pushBody()
                    .addToBackStack(true)
                    .fragment(fragmentType)
                    .push();
        }
    }

    private FragmentProvider getRootFromArgs() {
        final Bundle arguments = getArguments();

        final FragmentProvider fragmentType;

        if (arguments == null
                || arguments.getSerializable(ARG_ROOT_FRAGMENT) == null
                || arguments.getString(ARG_ROOT_TAG) == null) {

            throw new IllegalStateException("Root fragment is not defined or tag not provided!");

        } else {

            final Serializable serializedClass = arguments.getSerializable(ARG_ROOT_FRAGMENT);
            final String tag = arguments.getString(ARG_ROOT_TAG);

            if (!(serializedClass instanceof Class)) {
                throw new IllegalStateException("You must provide provide root fragment of type Class<? extends Fragment>.");
            }

            final Class<? extends Fragment> rootClass = (Class<? extends Fragment>) serializedClass;
            fragmentType = new FragmentProviderImpl(rootClass, tag);
        }

        return fragmentType;
    }

    /**
     * If you want to show a certain fragment, this is where you start from. This method returns
     * a {@link PushBody.Builder} object that will help you with adding the fragment. Once you're
     * done, the transaction will be committed by the {@link FragmentController}, without you
     * bothering about it.
     * <br><br>
     * This is basically a wrapper of a single {@link FragmentTransaction}.
     *
     * @return Builder for {@link PushBody}.
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
     * Same as {@link FragmentController#popAsync(boolean)} but performs the operation
     * immediately so there is no need of calling
     * {@link FragmentManager#executePendingTransactions()} afterwards.
     *
     * @param withAnimation Pass true if you want the changes to be animated, false otherwise.
     * @return True if there was something to pop, false otherwise.
     *
     * @see TransitionAnimationManager
     */
    public boolean pop(boolean withAnimation) {
        final FragmentManager fragmentManager = getChildFragmentManager();

        disableLastEntryAnimation(withAnimation, fragmentManager);

        return fragmentManager.getBackStackEntryCount() != 1 &&
                fragmentManager.popBackStackImmediate();
    }

    /**
     * Pop a single entry from the child {@link FragmentManager}'s back stack. If there is only
     * the root fragment in the back stack, it will be considered as there is nothing to pop.
     *
     * @param withAnimation Pass true if you want the changes to be animated, false otherwise.
     *
     * @see TransitionAnimationManager
     */
    public void popAsync(boolean withAnimation) {
        final FragmentManager fragmentManager = getChildFragmentManager();

        disableLastEntryAnimation(withAnimation, fragmentManager);

        if (fragmentManager.getBackStackEntryCount() != 1) {
            fragmentManager.popBackStack();
        }
    }

    /**
     * Same as {@link FragmentController#popToAsync(FragmentProvider, boolean, boolean)} but
     * performs the operation immediately, so there is no need of calling
     * {@link FragmentManager#executePendingTransactions()} afterwards.
     *
     * @param provider The provider of the fragment, to which you want to pop back to.
     * @param inclusive true if you want its entry to be popped too, false otherwise. Ignored
     *                  if this is the root entry.
     * @param withAnimation true if you want the changes to be animated, false otherwise.
     *
     * @return true if something was popped at all, false otherwise.
     *
     * @see FragmentController#popToAsync(FragmentProvider, boolean, boolean)
     */
    public boolean popTo(FragmentProvider provider, boolean inclusive, boolean withAnimation) {
        final FragmentManager fragmentManager = getChildFragmentManager();

        if (!withAnimation) {
            disableNextAnimationTo(fragmentManager, provider.getTag(), inclusive);
        }

        final boolean popped = fragmentManager.popBackStackImmediate(provider.getTag(), 0);

        if (popped && inclusive) {
            pop(withAnimation);
        }

        return popped;
    }

    /**
     * Pop to the first back stack entry with the same name as the tag from the given
     * {@link FragmentProvider}. Whether that entry will be popped itself, depends on what you pass
     * as a second argument. If this is the entry that corresponds to the root fragment, the
     * inclusive parameter will be ignored.
     *
     * @param provider The provider of the fragment, to which you want to pop back to.
     * @param inclusive true if you want its entry to be popped too, false otherwise. Ignored
     *                  if this is the root entry.
     * @param withAnimation true if you want the changes to be animated, false otherwise.
     */
    public void popToAsync(FragmentProvider provider, boolean inclusive, boolean withAnimation) {
        final FragmentManager fragmentManager = getChildFragmentManager();

        if (!withAnimation) {
            disableNextAnimationTo(fragmentManager, provider.getTag(), inclusive);
        }

        fragmentManager.popBackStack(provider.getTag(), 0);

        if (inclusive) {
            popAsync(withAnimation);
        }
    }

    /**
     * Same as {@link FragmentController#popToRootAsync()} but performs the operation immediately,
     * so there is no need of calling {@link FragmentManager#executePendingTransactions()} afterwards.
     *
     * @return true if there was something to pop, false otherwise.
     *
     * @see FragmentController#popToRootAsync()
     */
    public boolean popToRoot() {
        final FragmentManager fragmentManager = getChildFragmentManager();
        final int entryCount = fragmentManager.getBackStackEntryCount();
        final int lastEntry = entryCount == 0 ? 0 : entryCount - 1;

        for (int index = lastEntry; index > 0; index--) {
            fragmentManager.popBackStack();
        }

        fragmentManager.executePendingTransactions();

        return lastEntry > 0;
    }

    /**
     * Pop all back stack entries except the root one.
     */
    public void popToRootAsync() {
        final FragmentManager fragmentManager = getChildFragmentManager();
        final int entryCount = fragmentManager.getBackStackEntryCount();
        final int lastEntry = entryCount == 0 ? 0 : entryCount - 1;

        for (int index = lastEntry; index > 0; index--) {
            fragmentManager.popBackStack();
        }
    }

    private String getTagFromEntry(FragmentManager fragmentManager, int entry) {
        return fragmentManager.getBackStackEntryAt(entry).getName();
    }

    private void disableLastEntryAnimation(boolean withAnimation, FragmentManager fragmentManager) {
        if (!withAnimation) {
            final int lastEntry = fragmentManager.getBackStackEntryCount() - 1;
            final String lastTag = getTagFromEntry(fragmentManager, lastEntry);
            disableNextAnimationTo(fragmentManager, lastTag, true);
        }
    }

    private void disableNextAnimationTo(FragmentManager fragmentManager, String tag, boolean inclusive) {
        final int entryCount = fragmentManager.getBackStackEntryCount();
        final int lastEntry = entryCount - 1;

        String entryTag = null;
        int entry = lastEntry;
        while (!tag.equals(entryTag) || entry > 1) {
            entryTag = getTagFromEntry(fragmentManager, entry);

            disableAnimationForEntryTag(fragmentManager, entryTag);

            entry--;
        }

        if (inclusive && entry > 1) {
            entryTag = getTagFromEntry(fragmentManager, entry);
            disableAnimationForEntryTag(fragmentManager, entryTag);
        }
    }

    private void disableAnimationForEntryTag(FragmentManager fragmentManager, String entryTag) {
        final Fragment fragment = fragmentManager.findFragmentByTag(entryTag);
        if (fragment instanceof TransitionAnimationManager) {
            ((TransitionAnimationManager) fragment).disableNextAnimation();
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
