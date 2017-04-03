package com.appolica.sample.fragment;

import android.support.v4.app.Fragment;

import com.appolica.fragmentcontroller.fragment.ControllerFragmentType;


public enum FragmentsType implements ControllerFragmentType {
    ONE(FragmentOne.class, "FragmentOne"),
    TWO(FragmentTwo.class, "FragmentTwo"),
    TEST(TestFragment.class, "FragmentTest");

    private Class<? extends Fragment> fragmentClass;
    private String tag;

    FragmentsType(Class<? extends Fragment> fragmentClass, String tag) {
        this.fragmentClass = fragmentClass;
        this.tag = tag;
    }

    @Override
    public Fragment getInstance() {
        try {
            return fragmentClass.newInstance();
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
