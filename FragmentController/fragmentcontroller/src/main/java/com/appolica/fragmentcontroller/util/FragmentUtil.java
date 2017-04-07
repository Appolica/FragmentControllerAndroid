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

package com.appolica.fragmentcontroller.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

public class FragmentUtil {

    /**
     * Get all fragments, returned from {@link FragmentManager#getFragments()} but without the
     * null references in the list.
     *
     * @param fragmentManager The {@link FragmentManager} which's fragments you want to get.
     *
     * @return A list of the {@link FragmentManager}'s fragments, without the null references.
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
