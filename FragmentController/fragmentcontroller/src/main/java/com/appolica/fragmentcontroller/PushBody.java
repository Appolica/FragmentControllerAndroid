package com.appolica.fragmentcontroller;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.appolica.fragmentcontroller.fragment.ControllerFragmentType;

import org.jetbrains.annotations.Contract;

class PushBody {
    private String tag;

    private ControllerFragmentType fragmentType;

    private boolean addToBackStack;

    private boolean immediate;
    private Builder.TransitionAnimationBody transitionAnimations;

    public PushBody() {

    }

    public Fragment getFragment() {
        return fragmentType.getInstance();
    }

    public ControllerFragmentType getFragmentType() {
        return fragmentType;
    }

    public void setFragmentType(ControllerFragmentType fragmentType) {
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

    public static class Builder {
        private String tag;

        private ControllerFragmentType fragmentType;

        private boolean addToBackStack = false;
        private boolean immediate = false;

        private PushBodyConsumer bodyConsumer;
        private TransitionAnimationBody transitionAnimation = null;

        @Contract("_ -> !null")
        public static Builder instance(@NonNull PushBodyConsumer bodyConsumer) {
            return new Builder(bodyConsumer);
        }

        public Builder(PushBodyConsumer bodyConsumer) {
            this.bodyConsumer = bodyConsumer;
        }

        public Builder addToBackStack(boolean toBackStack) {
            this.addToBackStack = toBackStack;
            return this;
        }

        public Builder fragment(ControllerFragmentType fragmentType, String tag) {
            this.fragmentType = fragmentType;
            this.tag = tag;
            return this;
        }

        public Builder withAnimation(boolean withAnimation) {
            if (withAnimation) {
                transitionAnimation = new TransitionAnimationBody();
            } else {
                transitionAnimation = null;
            }

            return this;
        }

        public TransitionAnimationBody customAnimation() {
            return new TransitionAnimationBody();
        }

        public Builder immediate(boolean immediate) {
            this.immediate = immediate;
            return this;
        }

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

        public void push() {
            final PushBody pushBody = build();
            bodyConsumer.push(pushBody);
        }

        private void setTransitionAnimation(TransitionAnimationBody transitionAnimation) {
            this.transitionAnimation = transitionAnimation;
        }

        public class TransitionAnimationBody {
            private int enter = R.anim.slide_in_right;
            private int exit = R.anim.slide_out_left;
            private int popEnter = android.R.anim.slide_in_left;
            private int popExit = android.R.anim.slide_out_right;

            public TransitionAnimationBody enter(int enter) {
                this.enter = enter;
                return this;
            }

            public TransitionAnimationBody exit(int exit) {
                this.exit = exit;
                return this;
            }

            public TransitionAnimationBody popEnter(int popEnter) {
                this.popEnter = popEnter;
                return this;
            }

            public TransitionAnimationBody popExit(int popExit) {
                this.popEnter = popExit;
                return this;
            }

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

    public interface PushBodyConsumer {
        void push(PushBody body);
    }
}
