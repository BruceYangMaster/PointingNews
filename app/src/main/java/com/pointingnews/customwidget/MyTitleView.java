package com.pointingnews.customwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pointingnews.R;

/**
 * Created by Administrator on 2015-12-16.
 */
public class MyTitleView extends RelativeLayout implements View.OnClickListener{

    private ImageView mBackImageView,mMoreImageView;
    private TextView mTitleTextView;
    private String title = "";

    private OnClickListener mOnClickListener;

    @Override
    public void setOnClickListener(OnClickListener l) {
        this.mOnClickListener = l;
    }

    public MyTitleView(Context context){
        super(context);
    }

    public MyTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //初始化控件
        LayoutInflater inflater = LayoutInflater.from(context);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyTitleView,0,0);
        this.title = a.getString(R.styleable.MyTitleView_title_text);

        initView(inflater);

    }

    /**
     * 初始化控件并添加到MyTitleView
     * @param inflater
     */
    public void initView(LayoutInflater inflater){
        View v = inflater.inflate(R.layout.my_title_view_layout,this,true);

        mBackImageView = (ImageView) v.findViewById(R.id.id_my_title_view_back_imageview);
        mMoreImageView = (ImageView) v.findViewById(R.id.id_my_title_view_more_imageview);
        mTitleTextView = (TextView) v.findViewById(R.id.id_my_title_view_title_textview);

        mTitleTextView.setText(title);

        mBackImageView.setOnClickListener(this);
        mMoreImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(mOnClickListener != null){
            mOnClickListener.onClick(v);
        }
    }
}
