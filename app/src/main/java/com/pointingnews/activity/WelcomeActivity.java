package com.pointingnews.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.pointingnews.R;
import com.pointingnews.fragment.WelcomTouTiaoFragment;

public class WelcomeActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_layout);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.id_activity_welcom_fragment_container_layout, WelcomTouTiaoFragment.newInstance())
                .commit();
    }
}
