package com.appolica.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.appolica.fragmentcontroller.FragmentController;
import com.appolica.sample.fragment.FragmentsType;


public class MainActivity extends AppCompatActivity {
    private static final String CONTROLLER_TAG = "FRAGMENT_CONTAINER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, FragmentController.instance(FragmentsType.ONE), CONTROLLER_TAG)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        final FragmentController fragmentController =
                (FragmentController) getSupportFragmentManager().findFragmentByTag(CONTROLLER_TAG);

        if (!fragmentController.onBackPressed()) {
            super.onBackPressed();
        }
    }
}
