package com.appolica.fragmentcontroller.fragment;

import android.support.v4.app.Fragment;

/**
 *
 */
public interface FragmentProvider {

    /**
     *
     * @return
     */
    Fragment getInstance();

    /**
     *
     * @return
     */
    String getTag();
}
