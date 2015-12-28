package com.pointingnews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.pointingnews.R;
import com.pointingnews.fragment.NewsContentImgFragment;
import com.pointingnews.model.NewsBean;

public class NewsContentImgActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content_img_layout);

        Intent intent = getIntent();
        if(intent != null){
            NewsBean newsBean = (NewsBean) intent.getSerializableExtra("NEWSBEAN");

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.id_activity_news_content_img_fragment_container_layout, NewsContentImgFragment.newInstance(newsBean))
                    .commit();
        }


    }
}
