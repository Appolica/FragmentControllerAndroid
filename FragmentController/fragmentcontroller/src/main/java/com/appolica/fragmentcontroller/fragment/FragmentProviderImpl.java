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

import android.support.v4.app.Fragment;

/**
 *
 */
public class FragmentProviderImpl implements FragmentProvider {

    private final Class<? extends Fragment> frClass;
    private String tag;

    public FragmentProviderImpl(Class<? extends Fragment> frClass) {
        this(frClass, frClass.getName());
    }

    public FragmentProviderImpl(Class<? extends Fragment> frClass, String tag) {
        this.frClass = frClass;
        this.tag = tag;
    }

    @Override
    public Fragment getInstance() {
        try {
            return frClass.newInstance();
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
