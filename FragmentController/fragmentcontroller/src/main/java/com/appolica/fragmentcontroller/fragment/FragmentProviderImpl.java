/*
 * Copyright (c) 2017 Appolica Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.
 *
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.appolica.fragmentcontroller.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * An implementation of {@link FragmentProvider}.
 */
public class FragmentProviderImpl implements FragmentProvider {

    private final Class<? extends Fragment> frClass;
    private String tag;
    private Bundle args;

    /**
     * Create a {@link FragmentProvider} for your fragment, by giving only its class object.
     * The class name will be used as a tag.
     *
     * @param frClass Your fragment's class object.
     */
    public FragmentProviderImpl(Class<? extends Fragment> frClass) {
        this(frClass, frClass.getName());
    }

    /**
     * Create a {@link FragmentProvider} for your fragment, by giving its class object and a tag.
     *
     * @param frClass Your fragment's class object.
     * @param tag The tag that will be returned from {@link FragmentProvider#getTag()}.
     */
    public FragmentProviderImpl(Class<? extends Fragment> frClass, String tag) {
        this(frClass, tag, null);
    }

    /**
     * Create a {@link FragmentProvider} for your fragment, by giving its class object and a tag and
     * arguments for the fragment.
     *
     * @param frClass Your fragment's class object.
     * @param tag The tag that will be returned from {@link FragmentProvider#getTag()}.
     */
    public FragmentProviderImpl(Class<? extends Fragment> frClass, String tag, Bundle args) {
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
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String getTag() {
        return tag;
    }
}
