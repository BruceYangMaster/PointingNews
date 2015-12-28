package com.pointingnews.activity;

import android.os.Bundle;
import android.view.Window;
import android.widget.RadioGroup;

import com.pointingnews.R;
import com.pointingnews.fragment.HomeFragment;
import com.pointingnews.fragment.SlideMenuFragment;
import com.warmtel.slidingmenu.lib.SlidingMenu;
import com.warmtel.slidingmenu.lib.app.SlidingActivity;

public class MainActivity extends SlidingActivity implements RadioGroup.OnCheckedChangeListener{

    private RadioGroup mTabRadioGroup;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        mTabRadioGroup = (RadioGroup) findViewById(R.id.id_activity_main_radiogroup);
        mTabRadioGroup.setOnCheckedChangeListener(this);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.id_activity_main_fragment_container_layout, HomeFragment.newInstance())
                .commit();

        setBehindContentView(R.layout.slide_menu_layout);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.id_slide_menu_fragment_container_layout, SlideMenuFragment.newInstance())
                .commit();

        initSlideMenu();

    }

    /**
     * 初始化SlideMenu
     */
    public void initSlideMenu(){
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setBehindOffset(200);
        slidingMenu.setTouchModeAbove(SlidingMenu.LEFT);
        slidingMenu.setTouchModeBehind(SlidingMenu.TOUCHMODE_FULLSCREEN);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.id_activity_main__video_radiobutton:
                
                break;
        }
    }
}
