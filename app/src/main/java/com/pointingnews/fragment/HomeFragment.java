package com.pointingnews.fragment;


import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.pointingnews.R;
import com.pointingnews.model.TopicBean;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    private String[] topics = {"头条", "娱乐", "体育", "财经", "科技", "军事", "农业", "国际", "段子", "笑话"};
    private String[] news_type_ids = {"T1348647909107", "T1348648517839", "T1348649079062", "T1348648756099", "T1348649580692"
            , "T1348647909107", "T1348648517839", "T1348649079062", "T1348648756099", "T1348649580692"};
    private String[] urs = {"http://c.m.163.com/nc/article/headline/T1348647909107"
            , "http://c.m.163.com/nc/article/list/T1348648517839"
            , "http://c.m.163.com/nc/article/list/T1348649079062"
            , "http://c.m.163.com/nc/article/list/T1348648756099"
            , "http://c.m.163.com/nc/article/list/T1348649580692"
            , "http://c.m.163.com/nc/article/headline/T1348647909107"
            , "http://c.m.163.com/nc/article/list/T1348648517839"
            , "http://c.m.163.com/nc/article/list/T1348649079062"
            , "http://c.m.163.com/nc/article/list/T1348648756099"
            , "http://c.m.163.com/nc/article/list/T1348649580692"};

    private HorizontalScrollView mScrollView; //實現title的scrollview
    private RadioGroup mRadioGroup;           //title內的子控件，裡面每一個話題都是一個RadioButton
    private ViewPager mViewPager;             //用viewPager實現的內容滑屏效果

    public static HomeFragment newInstance() {
        Log.v("tag", "HomeFragment>>>>>>>newInstance");
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View v = getView();

        //实例化控件
        mScrollView = (HorizontalScrollView) v.findViewById(R.id.id_fragment_home_scrollview);
        mRadioGroup = (RadioGroup) v.findViewById(R.id.id_fragment_home_title_radiogroup);
        mViewPager = (ViewPager) v.findViewById(R.id.id_fragment_home_viewpager);

        //为ViewPager设置适配器
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(adapter);

        ArrayList<TopicBean> data = getData("");
        adapter.setData(data);

        //将Topic添加到RadioButton，再将RadioButton添加到RadioGroup
        RadioButton button;
        TopicBean bean;
        for (int i = 0; i < data.size(); i++) {
            bean = data.get(i);
            button = createRadioButton(bean);
            mRadioGroup.addView(button, i);
        }
        ((RadioButton) mRadioGroup.getChildAt(0)).toggle();//设置RadioGroup的第一个RadioButton被checked

        //设置RadioGroup的Checked监听事件
        mRadioGroup.setOnCheckedChangeListener(this);
        //设置ViewPager的监听事件
        mViewPager.setOnPageChangeListener(this);

    }

    /**
     * 产生相同属性的radioButton
     *
     * @return
     */
    public RadioButton createRadioButton(TopicBean bean) {
        RadioButton radioButton = new RadioButton(getActivity());
        radioButton.setId(bean.getId());

        RadioGroup.LayoutParams layoutParams =
                new RadioGroup.LayoutParams(0, RadioGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1;
        radioButton.setLayoutParams(layoutParams);
        radioButton.setGravity(Gravity.CENTER_HORIZONTAL);

        //代码舍子radioButton的button为null
        radioButton.setButtonDrawable(new BitmapDrawable((Bitmap) null));

        radioButton.setPadding(20, 10, 20, 10);
        radioButton.setTextSize(15f);
        radioButton.setText(bean.getTopic());

        //代码引用color下的selector
        ColorStateList colors = getResources()
                .getColorStateList(R.color.selector_fragment_title_radio_text_color);
        if (colors != null) {
            radioButton.setTextColor(colors);
        }
        //将每一个话题对象保存起来
        radioButton.setTag(bean);
        return radioButton;
    }

    /**
     * 获取topic数据源
     *
     * @param url
     * @return
     */
    public ArrayList<TopicBean> getData(String url) {
        ArrayList<TopicBean> data = new ArrayList<TopicBean>();
        TopicBean bean;
        for (int i = 0; i < topics.length; i++) {
            bean = new TopicBean();
            bean.setId(i);
            bean.setNews_type_id(news_type_ids[i]);
            bean.setTopic(topics[i]);
            bean.setUr(urs[i]);
            data.add(bean);
        }
        return data;
    }


    /**
     * RadioButton的监听事件回调函数
     *
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        mViewPager.setCurrentItem(checkedId); //设置ViewPager当前显示第checkerId项
    }

    /**
     * 以下为ViewPager的监听事件回调函数
     *
     * @param position
     * @param positionOffset
     * @param positionOffsetPixels
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        ((RadioButton) mRadioGroup.getChildAt(position)).toggle();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * ViewPager的适配器  ViewPager的每一项又是一个fragment
     */
    class ViewPagerAdapter extends FragmentStatePagerAdapter {

        ArrayList<TopicBean> data = new ArrayList<TopicBean>();

        public void setData(ArrayList<TopicBean> data) {
            this.data = data;
            notifyDataSetChanged();
        }


        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            TopicBean bean = data.get(position);
            Log.v("tag", "homefragment>>>>>>getitem");

            return ListNewsFragment.newInstance(bean);
        }

        @Override
        public int getCount() {
            return data.size();
        }
    }
}
