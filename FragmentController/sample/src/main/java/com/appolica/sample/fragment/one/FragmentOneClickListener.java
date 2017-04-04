package com.appolica.sample.fragment.one;

import com.appolica.sample.fragment.NavigationClickListener;


public interface FragmentOneClickListener extends NavigationClickListener {

    void onPopToRootClick();

    void onBtnIncrementClick();
}
