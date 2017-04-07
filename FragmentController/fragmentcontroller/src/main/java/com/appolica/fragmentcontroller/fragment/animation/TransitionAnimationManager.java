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

package com.appolica.fragmentcontroller.fragment.animation;

/**
 * If a {@link android.support.v4.app.Fragment} was pushed with animation but you would like to
 * pop it without one, the {@link com.appolica.fragmentcontroller.FragmentController} requires your
 * {@link android.support.v4.app.Fragment} to implement this interface.
 */
public interface TransitionAnimationManager {

    /**
     * Called by {@link com.appolica.fragmentcontroller.FragmentController} when you want to pop
     * your fragment without animation but it was previously pushed with such one. Implementation
     * of this method should disable it.
     *
     * @see com.appolica.fragmentcontroller.fragment.DisabledAnimationFragment
     */
    void disableNextAnimation();
}
