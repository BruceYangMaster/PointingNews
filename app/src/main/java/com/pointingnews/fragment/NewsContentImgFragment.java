package com.pointingnews.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pointingnews.R;
import com.pointingnews.model.ImageBean;
import com.pointingnews.model.NewsBean;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsContentImgFragment extends Fragment implements ViewPager.OnPageChangeListener{

    private NewsBean newsBean;
    private ViewPager mViewPager;
    private TextView mTitleTextView, mCountTextView;
    private ArrayList<ImageBean> data = new ArrayList<>();
    private String title;

    private static final float MIN_SCALE = 0.85f;
    private static final float MIN_ALPHA = 0.5f;

    public static NewsContentImgFragment newInstance(NewsBean newsBean) {
        Bundle args = new Bundle();
        args.putSerializable("NEWSBEAN", newsBean);
        NewsContentImgFragment fragment = new NewsContentImgFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.newsBean = (NewsBean) bundle.getSerializable("NEWSBEAN");
            this.title = newsBean.getTitle();
            String imgSrc = newsBean.getImgsrc();
            String alt = newsBean.getTitle();

            ImageBean imageBean = new ImageBean();
            imageBean.setAlt(alt);
            imageBean.setImgsrc(imgSrc);
            data.add(imageBean);
            ArrayList<ImageBean> imgextra = newsBean.getImgextra();
            if(imgextra != null){
               data.addAll(imgextra);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_news_content_img_layout, container, false);
        mViewPager = (ViewPager) v.findViewById(R.id.id_fragment_news_content_img_viewpager);
        mTitleTextView = (TextView) v.findViewById(R.id.id_fragment_news_content_img_title_textview);
        mCountTextView = (TextView) v.findViewById(R.id.id_fragment_news_content_img_count_textview);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(title != null && !"".equals(title)){
            mTitleTextView.setText(title);
        }
        mCountTextView.setText("1/"+data.size());

        ViewPagerAdapter adapter = new ViewPagerAdapter();
        mViewPager.setAdapter(adapter);

        adapter.setData(data);

        mViewPager.setOnPageChangeListener(this);
        //为ViewPager设置动画效果
        mViewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View view, float position) {
                int pageWidth = view.getWidth();
                int pageHeight = view.getHeight();

                if (position < -1)
                { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    view.setAlpha(0);
                } else if (position <= 1) //a页滑动至b页 ； a页从 0.0 -1 ；b页从1 ~ 0.0
                { // [-1,1]
                    // Modify the default slide transition to shrink the page as well
                    float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                    float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                    float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                    if (position < 0)
                    {
                        view.setTranslationX(horzMargin - vertMargin / 2);
                    } else
                    {
                        view.setTranslationX(-horzMargin + vertMargin / 2);
                    }

                    // Scale the page down (between MIN_SCALE and 1)
                    view.setScaleX(scaleFactor);
                    view.setScaleY(scaleFactor);

                    // Fade the page relative to its size.
                    view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE)
                            / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
                } else
                { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    view.setAlpha(0);
                }
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mCountTextView.setText((position+1)+"/"+data.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * ViewPager适配器
     */
    class ViewPagerAdapter extends PagerAdapter {

        private ArrayList<ImageBean> imgeBeans = new ArrayList<>();

        public void setData(ArrayList<ImageBean> imgeBeans){
            this.imgeBeans = imgeBeans;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return imgeBeans.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imgView = new ImageView(getActivity());
            Picasso.with(getActivity()).load(imgeBeans.get(position).getImgsrc()).into(imgView);
            mTitleTextView.setText(imgeBeans.get(position).getAlt());
            container.addView(imgView);
            return imgView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }

}
