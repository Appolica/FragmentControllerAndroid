package com.appolica.fragmentcontroller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentController extends Fragment {
    public static final String FRAGMENT_TYPE_ARGUMENT = "fragmentTypeArgument";
    private static final String ROOT_FRAGMENT_TAG = "root";

    //// TODO: 28.01.17 should root fragment tag be exceptional and private in the controller
    public FragmentController() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ControllerFragmentType fragmentType;

        if (getArguments() == null || getArguments().getSerializable(FRAGMENT_TYPE_ARGUMENT) == null) {
            throw new IllegalStateException("Root fragment is not defined!");
        } else {
            fragmentType = (ControllerFragmentType) getArguments().getSerializable(FRAGMENT_TYPE_ARGUMENT);
        }

        if (savedInstanceState == null) {
            push(new FragmentController.PushBuilder()
                    .addToBackStack(true)
                    .fragment(fragmentType, ROOT_FRAGMENT_TAG)
                    .build()
            );
        }
        return inflater.inflate(R.layout.fragment_container, container, false);
    }

    public void push(PushBody body) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();

        if (body.isToBackStack()) {
            fragmentTransaction.addToBackStack(body.getTag());
        }

        fragmentTransaction
                .replace(R.id.fragmentPlace, body.getFragment(), body.getTag())
                .commit();
    }

    public boolean pop() {
        return getChildFragmentManager().getBackStackEntryCount() != 1 &&
                getChildFragmentManager().popBackStackImmediate();
    }

    public boolean popTo(ControllerFragmentType fragmentType) {
        return getChildFragmentManager().popBackStackImmediate(fragmentType.getTag(), 0);
    }

    public static class PushBuilder {
        private ControllerFragmentType fragmentType;
        private String tag;
        private boolean toBackStack;

        public PushBuilder addToBackStack(boolean toBackStack) {
            this.toBackStack = toBackStack;
            return this;
        }

        public PushBuilder fragment(ControllerFragmentType fragmentType, String tag) {
            this.fragmentType = fragmentType;
            this.tag = tag;
            return this;
        }

        public PushBody build() {
            if (fragmentType == null) {
                throw new IllegalStateException("FragmentType must not be null");
            } else if (tag == null) {
                throw new IllegalStateException("Tag must not be null");
            }

            return new PushBody(fragmentType, tag, toBackStack);
        }
    }

    private static class PushBody {
        private ControllerFragmentType fragmentType;
        private String tag;
        private boolean toBackStack;

        public PushBody(ControllerFragmentType fragmentType, String tag, boolean toBackStack) {
            this.fragmentType = fragmentType;
            this.tag = tag;
            this.toBackStack = toBackStack;
        }

        public Fragment getFragment() {
            return fragmentType.getInstance();
        }

        public String getTag() {
            return tag;
        }

        public boolean isToBackStack() {
            return toBackStack;
        }
    }
}

