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
import android.view.animation.Animation;

import com.appolica.fragmentcontroller.fragment.animation.TransitionAnimationManager;

/**
 * This fragment implements {@link TransitionAnimationManager} and disables the transaction
 * animation by returning an Animation object with 0 duration in
 * {@link Fragment#onCreateAnimation(int, boolean, int)}. This dummy animation will be returned only
 * if {@link TransitionAnimationManager} gets called.
 */
public class DisabledAnimationFragment extends Fragment implements TransitionAnimationManager {

    private boolean animateTransitions = true;

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (!animateTransitions) {
            animateTransitions = true;
            Animation a = new Animation() { /* hack */ };
            a.setDuration(0);
            return a;
        }

        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void disableNextAnimation() {
        animateTransitions = false;
    }
}
