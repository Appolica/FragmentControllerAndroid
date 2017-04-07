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

package com.appolica.fragmentcontroller;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.appolica.fragmentcontroller.fragment.FragmentProvider;

import org.jetbrains.annotations.Contract;

/**
 *
 */
public class PushBody {
    private String tag;

    private FragmentProvider fragmentType;

    private boolean addToBackStack;
    private boolean immediate;

    private Builder.TransitionAnimationBody transitionAnimations;

    private PushBody() {

    }

    public Fragment getFragment() {
        return fragmentType.getInstance();
    }

    public FragmentProvider getFragmentType() {
        return fragmentType;
    }

    public void setFragmentType(FragmentProvider fragmentType) {
        this.fragmentType = fragmentType;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean addToBackStack() {
        return addToBackStack;
    }

    public void setAddToBackStack(boolean addToBackStack) {
        this.addToBackStack = addToBackStack;
    }

    public void setImmediate(boolean immediate) {
        this.immediate = immediate;
    }

    public boolean immediate() {
        return immediate;
    }

    public void setTransitionAnimations(Builder.TransitionAnimationBody transitionAnimations) {
        this.transitionAnimations = transitionAnimations;
    }

    public Builder.TransitionAnimationBody getTransitionAnimations() {
        return transitionAnimations;
    }

    /**
     *
     */
    public static class Builder {
        private String tag;

        private FragmentProvider fragmentType;

        private boolean addToBackStack = false;
        private boolean immediate = false;

        private PushBodyConsumer bodyConsumer;
        private TransitionAnimationBody transitionAnimation = null;

        /**
         *
         * @param bodyConsumer
         * @return
         */
        @Contract("_ -> !null")
        public static Builder instance(@NonNull PushBodyConsumer bodyConsumer) {
            return new Builder(bodyConsumer);
        }

        /**
         *
         * @param bodyConsumer
         */
        public Builder(PushBodyConsumer bodyConsumer) {
            this.bodyConsumer = bodyConsumer;
        }

        /**
         *
         * @param toBackStack
         * @return
         */
        public Builder addToBackStack(boolean toBackStack) {
            this.addToBackStack = toBackStack;
            return this;
        }

        /**
         *
         * @param fragmentType
         * @return
         */
        public Builder fragment(FragmentProvider fragmentType) {
            fragment(fragmentType, fragmentType.getTag());
            return this;
        }

        /**
         *
         * @param fragmentType
         * @param tag
         * @return
         */
        public Builder fragment(FragmentProvider fragmentType, String tag) {
            this.fragmentType = fragmentType;
            this.tag = tag;
            return this;
        }

        /**
         *
         * @param withAnimation
         * @return
         */
        public Builder withAnimation(boolean withAnimation) {
            if (withAnimation) {
                transitionAnimation = new TransitionAnimationBody();
            } else {
                transitionAnimation = null;
            }

            return this;
        }

        /**
         *
         * @return
         */
        public TransitionAnimationBody customAnimation() {
            return new TransitionAnimationBody();
        }

        /**
         *
         * @param immediate
         * @return
         */
        public Builder immediate(boolean immediate) {
            this.immediate = immediate;
            return this;
        }

        /**
         *
         * @return
         */
        public PushBody build() {
            if (fragmentType == null) {
                throw new IllegalStateException("FragmentType must not be null");
            } else if (tag == null) {
                throw new IllegalStateException("Tag must not be null");
            }

            final PushBody pushBody = new PushBody();

            pushBody.setFragmentType(fragmentType);
            pushBody.setTag(tag);
            pushBody.setAddToBackStack(addToBackStack);
            pushBody.setTransitionAnimations(transitionAnimation);
            pushBody.setImmediate(immediate);

            return pushBody;
        }

        /**
         *
         */
        public void push() {
            final PushBody pushBody = build();
            bodyConsumer.push(pushBody);
        }

        private void setTransitionAnimation(TransitionAnimationBody transitionAnimation) {
            this.transitionAnimation = transitionAnimation;
        }

        /**
         *
         */
        public class TransitionAnimationBody {
            private int enter = R.anim.slide_in_right;
            private int exit = R.anim.slide_out_left;
            private int popEnter = android.R.anim.slide_in_left;
            private int popExit = android.R.anim.slide_out_right;

            /**
             *
             * @param enter
             * @return
             */
            public TransitionAnimationBody enter(int enter) {
                this.enter = enter;
                return this;
            }

            /**
             *
             * @param exit
             * @return
             */
            public TransitionAnimationBody exit(int exit) {
                this.exit = exit;
                return this;
            }

            /**
             *
             * @param popEnter
             * @return
             */
            public TransitionAnimationBody popEnter(int popEnter) {
                this.popEnter = popEnter;
                return this;
            }

            /**
             *
             * @param popExit
             * @return
             */
            public TransitionAnimationBody popExit(int popExit) {
                this.popEnter = popExit;
                return this;
            }

            /**
             *
             * @return
             */
            public PushBody.Builder end() {
                PushBody.Builder.this.setTransitionAnimation(this);
                return PushBody.Builder.this;
            }

            public int getEnter() {
                return enter;
            }

            public int getExit() {
                return exit;
            }

            public int getPopEnter() {
                return popEnter;
            }

            public int getPopExit() {
                return popExit;
            }
        }
    }

    interface PushBodyConsumer {
        void push(PushBody body);
    }
}
