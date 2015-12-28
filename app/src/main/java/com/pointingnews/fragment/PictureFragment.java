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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.pointingnews.R;
import com.pointingnews.model.PictureTopicBean;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PictureFragment extends Fragment implements ViewPager.OnPageChangeListener,RadioGroup.OnCheckedChangeListener{
    private String address = "http://api.sina.cn/sinago/list.json";
    private String ur = "&adid=4ad30dabe134695c3b7c3a65977d7e72&wm=b207&from=6042095012" +
            "&chwm=12050_0001&oldchwm=&imei=867064013906290&uid=802909da86d9f5fc&p=";
    private String[] channels = {"channel=hdpic_toutiao", "channel=hdpic_funny", "channel=hdpic_pretty", "channel=hdpic_story"};
    private String topics[] = {"头条","趣图","美图","故事"};

    private HorizontalScrollView mScrollView; //實現title的scrollview
    private RadioGroup mRadioGroup;           //title內的子控件，裡面每一個話題都是一個RadioButton
    private ViewPager mViewPager;             //用viewPager實現的內容滑屏效果


    public static PictureFragment newInstance() {
        Bundle args = new Bundle();

        PictureFragment fragment = new PictureFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_picture_layout, container, false);
        //实例化控件
        mScrollView = (HorizontalScrollView) v.findViewById(R.id.id_fragment_picture_scrollview);
        mRadioGroup = (RadioGroup) v.findViewById(R.id.id_fragment_picture_title_radiogroup);
        mViewPager = (ViewPager) v.findViewById(R.id.id_fragment_picture_viewpager);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //获取数据源并为ViewPager设置适配器
        ArrayList<PictureTopicBean> data = getData("");
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.setData(data);

        //将Topic添加到RadioButton，再将RadioButton添加到RadioGroup
        RadioButton button;
        PictureTopicBean bean;
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
    public RadioButton createRadioButton(PictureTopicBean bean) {
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
     * 获取数据源
     * @param url
     * @return
     */
    public ArrayList<PictureTopicBean> getData(String url){
        ArrayList<PictureTopicBean> data = new ArrayList<>();
        PictureTopicBean bean;
        for (int i = 0;i < topics.length;i++){
            bean = new PictureTopicBean();

            String urlTemp = this.address + "?" + this.channels[i] + this.ur;
            String topic = topics[i];

            bean.setId(i);
            bean.setTopic(topic);
            bean.setUrl(urlTemp);

            data.add(bean);
        }

        return data;
    }


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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        mViewPager.setCurrentItem(checkedId); //设置ViewPager当前显示第checkerId项
    }

    /**
     * ViewPager的适配器  ViewPager的每一项又是一个fragment
     */
    class ViewPagerAdapter extends FragmentStatePagerAdapter {

        ArrayList<PictureTopicBean> data = new ArrayList<>();

        public void setData(ArrayList<PictureTopicBean> data) {
            this.data = data;
            notifyDataSetChanged();
        }


        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            PictureTopicBean bean = data.get(position);
            return ListPictureFragment.newInstance(bean);
        }

        @Override
        public int getCount() {
            return data.size();
        }
    }

}
