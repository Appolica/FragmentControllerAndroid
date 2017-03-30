package com.appolica.fragmentcontroller;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import org.jetbrains.annotations.Contract;

class PushBody {
    private String tag;

    private ControllerFragmentType fragmentType;

    private boolean addToBackStack;
    private boolean withAnimation;
    private boolean immediate;

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

    public boolean withAnimation() {
        return withAnimation;
    }

    public void setWithAnimation(boolean withAnimation) {
        this.withAnimation = withAnimation;
    }

    public void setImmediate(boolean immediate) {
        this.immediate = immediate;
    }

    public boolean immediate() {
        return immediate;
    }

    public static class Builder {
        private String tag;

        private ControllerFragmentType fragmentType;

        private boolean addToBackStack = false;
        private boolean withAnimation = false;
        private boolean immediate = false;

        private PushBodyConsumer bodyConsumer;

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
            this.withAnimation = withAnimation;
            return this;
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
            pushBody.setWithAnimation(withAnimation);
            pushBody.setImmediate(immediate);

            return pushBody;
        }

        public void push() {
            final PushBody pushBody = build();
            bodyConsumer.push(pushBody);
        }

    }

    public interface PushBodyConsumer {
        void push(PushBody body);
    }
}
