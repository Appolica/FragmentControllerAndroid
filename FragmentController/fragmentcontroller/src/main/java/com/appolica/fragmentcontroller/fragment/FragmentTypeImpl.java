package com.appolica.fragmentcontroller.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class FragmentTypeImpl implements ControllerFragmentType {

    private final Class<? extends Fragment> frClass;
    private String tag;
    private Bundle args;

    public FragmentTypeImpl(Class<? extends Fragment> frClass) {
        this(frClass, frClass.getName());
    }

    public FragmentTypeImpl(Class<? extends Fragment> frClass, String tag) {
        this(frClass, tag, null);
    }

    public FragmentTypeImpl(Class<? extends Fragment> frClass, String tag, Bundle args) {
        this.frClass = frClass;
        this.tag = tag;
        this.args = args;
    }

    @Override
    public Fragment getInstance() {
        try {
            Fragment fragment = frClass.newInstance();
            fragment.setArguments(args);
            return fragment;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String getTag() {
        return tag;
    }
}
