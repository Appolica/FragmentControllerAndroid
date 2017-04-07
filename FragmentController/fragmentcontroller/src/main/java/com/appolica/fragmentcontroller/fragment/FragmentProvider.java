package com.appolica.fragmentcontroller.fragment;

import android.support.v4.app.Fragment;

/**
 * Each fragment you want to show, using the
 * {@link com.appolica.fragmentcontroller.FragmentController}, must be represented by implementation
 * of this interface.
 */
public interface FragmentProvider {

    /**
     * Called by {@link com.appolica.fragmentcontroller.FragmentController} to get an instance
     * of the fragment you want to show, when it is adding it to the
     * {@link android.support.v4.app.FragmentTransaction}.
     *
     * @return The instance of tha fragment that you want to show.
     */
    Fragment getInstance();

    /**
     * Get the tag for your fragment, which will be used for tagging both the fragment when adding
     * it to the {@link android.support.v4.app.FragmentManager} and for tagging
     * the back stack entry.
     *
     * @return The tag that will correspond to your fragment.
     */
    String getTag();
}
