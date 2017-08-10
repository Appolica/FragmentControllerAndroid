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
import android.support.v4.app.FragmentManager;

import com.appolica.fragmentcontroller.fragment.FragmentProvider;

import org.jetbrains.annotations.Contract;

/**
 * This class is a wrapper of a single {@link android.support.v4.app.FragmentTransaction}. It holds
 * all the information, needed for your fragment to be shown. You construct this object by using
 * {@link PushBody.Builder}. A new instance of the builder can be obtained by calling
 * {@link FragmentController#pushBody()}.
 *
 * @see PushBody.Builder
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
     *  Set all properties for the {@link android.support.v4.app.FragmentTransaction} by using this
     *  builder.
     */
    public static class Builder {
        private String tag;

        private FragmentProvider fragmentType;

        private boolean addToBackStack = false;
        private boolean immediate = false;

        private PushBodyConsumer bodyConsumer;
        private TransitionAnimationBody transitionAnimation = null;

        @Contract("_ -> !null")
        static Builder instance(@NonNull PushBodyConsumer bodyConsumer) {
            return new Builder(bodyConsumer);
        }

        private Builder(PushBodyConsumer bodyConsumer) {
            this.bodyConsumer = bodyConsumer;
        }

        /**
         * Pass true if you want the transaction for showing your fragment to be added to
         * {@link android.support.v4.app.FragmentManager}'s back stack, false otherwise.
         *
         * @param toBackStack Pass true if you want the transaction to be added to the back stack.
         * @return The same object of the builder.
         */
        public Builder addToBackStack(boolean toBackStack) {
            this.addToBackStack = toBackStack;
            return this;
        }

        /**
         * Tell which fragment you want to show, by passing a corresponding
         * {@link FragmentProvider} implementation. The tag for this fragment will be obtained
         * from the {@link FragmentProvider}.
         *
         * @param fragmentType An implementation of {@link FragmentProvider} corresponding
         *                     to your fragment.
         * @return The same object of the builder.
         *
         * @see FragmentProvider
         */
        public Builder fragment(FragmentProvider fragmentType) {
            fragment(fragmentType, fragmentType.getTag());
            return this;
        }

        /**
         * Same as {@link PushBody.Builder#fragment(FragmentProvider)}, but you have pass the
         * fragment's tag explicitly.
         *
         * @param fragmentType An implementation of {@link FragmentProvider} corresponding
         *                     to your fragment.
         * @param tag The tag, used for your fragment when executing the transaction.
         *
         * @return The same object of the builder.
         */
        public Builder fragment(FragmentProvider fragmentType, String tag) {
            this.fragmentType = fragmentType;
            this.tag = tag;
            return this;
        }

        /**
         * Pass true to this method if you like to show your fragment using default animations.
         * There is no animation by default.

         * @param withAnimation true to show your fragment with default animations, false otherwise.
         *
         * @return The same object of the builder.
         *
         * @see TransitionAnimationBody
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
         * If you want to set a custom animation for showing your fragment, this is what you have
         * to use.This method will return a {@link TransitionAnimationBody} object, to which
         * you have to set your animations.
         *
         * @return {@link TransitionAnimationBody} object for setting animations.
         *
         * @see TransitionAnimationBody
         */
        public TransitionAnimationBody customAnimation() {
            return new TransitionAnimationBody();
        }

        /**
         * Pass true if you want the operation for adding your fragment to be committed immediately.
         * If you pass true to this method, there will be no need of calling
         * {@link FragmentManager#executePendingTransactions()} afterwards.
         *
         * @param immediate true to commit the operation immediately, false otherwise. false is
         *                  by default.
         * @return The same object of the builder.
         */
        public Builder immediate(boolean immediate) {
            this.immediate = immediate;
            return this;
        }

        /**
         * Create the {@link PushBody} object for your fragment. This object can be passed then to
         * {@link FragmentController#push(PushBody)}.
         *
         * @return The created {@link PushBody} object for showing your fragment.
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
         * Directly push the fragment which's {@link PushBody} your were building for.
         * This method calls {@link Builder#build()} internally.
         */
        public void push() {
            final PushBody pushBody = build();
            bodyConsumer.push(pushBody);
        }

        private void setTransitionAnimation(TransitionAnimationBody transitionAnimation) {
            this.transitionAnimation = transitionAnimation;
        }

        /**
         * This class holds the four animation ids for animating the fragments when executing the
         * transaction for showing you fragment. The default animations in this class are:<br>
         *     enter - R.anim.slide_in_right
         *     exit - R.anim.slide_out_left
         *     popEnter - android.R.anim.slide_in_left
         *     popExit - android.R.anim.slide_out_right
         *
         * @see android.support.v4.app.FragmentTransaction#setCustomAnimations(int, int, int, int)
         */
        public class TransitionAnimationBody {
            private int enter = R.anim.slide_in_right;
            private int exit = R.anim.slide_out_left;
            private int popEnter = android.R.anim.slide_in_left;
            private int popExit = android.R.anim.slide_out_right;

            /**
             * Set the resource id for your enter animation.
             * @param enter resource id of enter animation.
             * @return The same {@link TransitionAnimationBody} object.
             */
            public TransitionAnimationBody enter(int enter) {
                this.enter = enter;
                return this;
            }

            /**
             * Set the resource id for your exit animation.
             * @param exit resource id of exit animation.
             * @return The same {@link TransitionAnimationBody} object.
             */
            public TransitionAnimationBody exit(int exit) {
                this.exit = exit;
                return this;
            }

            /**
             * Set the resource id for your popEnter animation.
             * @param popEnter resource id of popEnter animation.
             * @return The same {@link TransitionAnimationBody} object.
             */
            public TransitionAnimationBody popEnter(int popEnter) {
                this.popEnter = popEnter;
                return this;
            }

            /**
             * Set the resource id for your popExit animation.
             * @param popExit resource id of enter animation.
             * @return The same {@link TransitionAnimationBody} object.
             */
            public TransitionAnimationBody popExit(int popExit) {
                this.popEnter = popExit;
                return this;
            }

            /**
             * Return back to building your {@link PushBody}
             *
             * @return The {@link PushBody.Builder} object you were using before calling
             * {@link Builder#customAnimation()}.
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
