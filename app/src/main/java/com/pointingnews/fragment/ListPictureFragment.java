package com.pointingnews.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.pointingnews.R;
import com.pointingnews.model.PictureBean;
import com.pointingnews.model.PictureCommetsInfoBean;
import com.pointingnews.model.PictureDataBean;
import com.pointingnews.model.PictureRootBean;
import com.pointingnews.model.PictureTopicBean;
import com.pointingnews.xlistview.XListView;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListPictureFragment extends Fragment {

    private PictureTopicBean bean;
    private XListView mListView;
    private ListViewAdapter adapter;

    public static ListPictureFragment newInstance(PictureTopicBean bean) {
        Bundle args = new Bundle();
        args.putSerializable("TOPICBEAN",bean);
        ListPictureFragment fragment = new ListPictureFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if(bundle != null){
            this.bean = (PictureTopicBean) bundle.getSerializable("TOPICBEAN");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list_picture_layout, container, false);
        mListView = (XListView) v.findViewById(R.id.id_fragment_list_picture_listview);

        //设置ListView还在网络加载数据时显示的view
        ProgressBar emptyView = new ProgressBar(getActivity());
        ((ViewGroup) mListView.getParent()).addView(emptyView);
        mListView.setEmptyView(emptyView);

        adapter = new ListViewAdapter(getActivity());
        mListView.setAdapter(adapter);

        return v;
    }

    /**
     * 获取并设置数据源
     * @param bean
     * @return
     */
    public void setData(PictureTopicBean bean){
        String url = bean.getUrl();
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    String json = new String(bytes,"UTF-8");
                    PictureRootBean rootBean = paseJsonToObject(json);
                    if(rootBean != null){
                        PictureDataBean dataBean = rootBean.getData();
                        if(dataBean != null){
                            ArrayList<PictureBean> data = dataBean.getList();
                            adapter.setData(data);
                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.v("tag","网络取数据失败");
            }
        });
    }

    /**
     * 解析json数据未PicRootBean对象
     * @param json
     * @return
     */
    public PictureRootBean paseJsonToObject(String json){
        Gson gson = new Gson();
        return gson.fromJson(json,PictureRootBean.class);
    }

    private class ListViewAdapter extends BaseAdapter{
        private ArrayList<PictureBean> data = new ArrayList<>();
        private LayoutInflater inflater;
        public ListViewAdapter(Context context){
            inflater = LayoutInflater.from(context);
        }

        public void setData(ArrayList<PictureBean> data){
            this.data = data;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return data.get(position).hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v;
            ViewHolder viewHolder;
            if(convertView == null){
                viewHolder = new ViewHolder();
                v = inflater.inflate(R.layout.item_xlist_view_picture_layout,null);
                viewHolder.contentImageView = (ImageView) v.findViewById(R.id.id_item_xlistview_picture_content_imageview);
                viewHolder.titleTextView = (TextView) v.findViewById(R.id.id_item_xlistview_picture_title_textview);
                viewHolder.sourceTextView = (TextView) v.findViewById(R.id.id_item_xlistview_picture_source_textview);
                viewHolder.replayCountTextView = (TextView) v.findViewById(R.id.id_item_xlistview_picture_replay_textview);
                viewHolder.praiseCountTextView = (TextView) v.findViewById(R.id.id_item_xlistview_picture_praise_textview);
                viewHolder.disPraiseCountTextView = (TextView) v.findViewById(R.id.id_item_xlistview_picture_dispraise_textview);

                v.setTag(viewHolder);
            }else {
                v = convertView;
                viewHolder = (ViewHolder) v.getTag();
            }

            PictureBean bean = data.get(position);
            String pic = bean.getPic();
            String title = bean.getTitle();
            String source = bean.getSource();
            PictureCommetsInfoBean info = bean.getComment_count_info();
            int replayCount = info.getQreply();
            int praiseCount = info.getPraise();
            int disPraiseCount = info.getDispraise();

            if(pic != null && !"".equals(pic)){
                Picasso.with(getActivity()).load(pic).into(viewHolder.contentImageView);
            }
            viewHolder.titleTextView.setText(title);
            viewHolder.sourceTextView.setText(source);
            viewHolder.replayCountTextView.setText(replayCount+"");
            viewHolder.praiseCountTextView.setText(praiseCount+"");
            viewHolder.disPraiseCountTextView.setText(disPraiseCount+"");

            return v;
        }

        class ViewHolder{
            ImageView contentImageView;
            TextView titleTextView,sourceTextView, replayCountTextView,praiseCountTextView,disPraiseCountTextView;
        }
    }

}
