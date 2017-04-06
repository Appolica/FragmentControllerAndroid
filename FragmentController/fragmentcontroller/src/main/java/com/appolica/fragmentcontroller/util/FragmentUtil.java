package com.appolica.fragmentcontroller.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

public class FragmentUtil {

    /**
     *
     * @param fragmentManager
     * @return
     */
    public static List<Fragment> getFragments(FragmentManager fragmentManager) {
        final List<Fragment> fragments = new ArrayList<>();

        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment != null) {
                fragments.add(fragment);
            }
        }

        return fragments;
    }

}
