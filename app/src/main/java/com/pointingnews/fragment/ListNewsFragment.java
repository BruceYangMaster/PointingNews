package com.pointingnews.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.pointingnews.R;
import com.pointingnews.activity.NewsContentActivity;
import com.pointingnews.activity.NewsContentImgActivity;
import com.pointingnews.imageslider.BaseSliderView;
import com.pointingnews.imageslider.PagerIndicator;
import com.pointingnews.imageslider.SliderLayout;
import com.pointingnews.imageslider.TextSliderView;
import com.pointingnews.model.AdBean;
import com.pointingnews.model.ImageBean;
import com.pointingnews.model.NewsBean;
import com.pointingnews.model.NewsSetBean;
import com.pointingnews.model.TopicBean;
import com.pointingnews.utils.TimeUtil;
import com.pointingnews.utils.UrlUtil;
import com.pointingnews.xlistview.XListView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListNewsFragment extends Fragment implements XListView.IXListViewListener, AdapterView.OnItemClickListener {

    private TopicBean topicBean;
    private String ur;
    private XListView mListView;
    private XListViewAdapter adapter;
    private int pageNo = 0;           //当前为多少页
    private int pageSize = 20;       //listView每页显示的新闻数量
    private NewsBean headNewsBean;   //ListView   的header数据源;

    private int flag;                //定义一个int的flag标签，判断是下拉刷新还是上拉加载更多
    private final int REFRESH = 1;         //刷新为0   加载更多为1
    private final int LOADMORE = 0;

    private final int ONE_IMG = 1;  //判断ListView的item需要显示布局的标签
    private final int MORE_IMG = 2;

    private TextView footerTextView; //ListView的footer
    private View headerView;         // ListView的header
    private SliderLayout sliderLayout;
    private PagerIndicator pagerIndicator;

    public static ListNewsFragment newInstance(TopicBean topicBean) {

        Log.v("tag", "Listviewfragment   newInstance");
        Bundle args = new Bundle();
        args.putSerializable("TOPICBEAN", topicBean);
        ListNewsFragment fragment = new ListNewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.topicBean = (TopicBean) bundle.getSerializable("TOPICBEAN");
            Log.v("tag", "listviefragment>>>>topicbean" + topicBean);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_news_layout, container, false);
        mListView = (XListView) v.findViewById(R.id.id_fragment_listview);
        headerView = inflater.inflate(R.layout.item_xlistview_header_layout, null);
        sliderLayout = (SliderLayout) headerView.findViewById(R.id.my_slider);
        pagerIndicator = (PagerIndicator) headerView.findViewById(R.id.custom_indicator);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //设置ListView还在网络加载数据时显示的view
        TextView emptyView = new TextView(getActivity());
        emptyView.setText("数据加载中......");
        emptyView.setGravity(Gravity.CENTER);
        ((ViewGroup) mListView.getParent()).addView(emptyView);
        mListView.setEmptyView(emptyView);

        //为ListView设置适配器
        adapter = new XListViewAdapter(getActivity());
        mListView.setAdapter(adapter);

        //设置XListView刷新和加载更多可用
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(true);

        //XlistView注册监听
        mListView.setXListViewListener(this);
        mListView.setOnItemClickListener(this);
        //为xListView设置初始化数据
        getData(pageNo, pageSize, topicBean);
    }

    /**
     * 获取数据源
     *
     * @param topicBean
     * @return
     */
    public void getData(int pageNo, int pageSize, TopicBean topicBean) {
        if (topicBean == null) {
            return;
        }
        String ur = topicBean.getUr();
        final String news_type_id = topicBean.getNews_type_id();
        String url = UrlUtil.getUrlByUr(pageNo, pageSize, ur);
        Log.v("tag", "ListViewFragment>>>>>>url>>" + url);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(getActivity(), url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    String json = new String(bytes, "utf-8");
                    Log.v("tag", "listfragment    josn>>>>>>" + json);
                    NewsSetBean newsSetBean = paseJsonToNewsSetBean(json, news_type_id);
                    ArrayList<NewsBean> data = newsSetBean.getT1348647909107();

                    if (data != null && data.size() != 0) {

                        //得到ListView的header数据源,并为头部设置数据
                        headNewsBean = data.remove(0);
                        ArrayList<AdBean> ads = headNewsBean.getAds();
                        if (ads != null && ads.size() != 0) {
                            mListView.removeHeaderView(headerView);
                            sliderLayout.removeAllSliders();//先将所有的textSliderView移除
                            for (AdBean adBean : ads) {
                                String title = adBean.getTitle();
                                String imgSrc = adBean.getImgsrc();
                                TextSliderView textSliderView = new TextSliderView(getActivity());
                                textSliderView.description(title).image(imgSrc).setScaleType(BaseSliderView.ScaleType.Fit);

                                sliderLayout.addSlider(textSliderView);
                            }
                            mListView.addHeaderView(headerView);
                        }

                        //为listview设置数据
                        if (flag == LOADMORE) {
                            adapter.pullLoadMore(data);
                            mListView.stopLoadMore();
                        } else if (flag == REFRESH) {
                            adapter.pullRefresh(data);
                            mListView.stopRefresh();
                        }
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.v("tag", "获取数据失败onFailure");
            }
        });

    }


    /**
     * 将json数据解析为NewsSetBean对象
     *
     * @param json
     * @param news_type_id
     * @return
     */
    public NewsSetBean paseJsonToNewsSetBean(String json, String news_type_id) {
        NewsSetBean newsSetBean = null;
        try {

            JSONObject root = new JSONObject(json);
            newsSetBean = new NewsSetBean();

            JSONArray newsarray = root.getJSONArray(news_type_id);
            ArrayList<NewsBean> T1348647909107 = new ArrayList<>();

            for (int i = 0; i < newsarray.length(); i++) {
                JSONObject newsbean = newsarray.getJSONObject(i);
                NewsBean newsBean = new NewsBean();

                if (newsbean.has("replayCount")) {
                    int replayCount = newsbean.getInt("replyCount");
                    newsBean.setReplyCount(replayCount);
                }

                if (newsbean.has("votecount")) {
                    int votecount = newsbean.getInt("votecount");
                    newsBean.setVotecount(votecount);
                }

                if (newsbean.has("docid")) {
                    String docid = newsbean.getString("docid");
                    newsBean.setDocid(docid);
                }

                if (newsbean.has("title")) {
                    String title = newsbean.getString("title");
                    newsBean.setTitle(title);
                }

                if (newsbean.has("digest")) {
                    String digest = newsbean.getString("digest");
                    newsBean.setDigest(digest);
                }

                if (newsbean.has("imgextra")) {
                    ArrayList<ImageBean> imgextra = new ArrayList<ImageBean>();
                    JSONArray jimgextra = newsbean.getJSONArray("imgextra");
                    for (int j = 0; j < jimgextra.length(); j++) {
                        JSONObject jimgsrc = jimgextra.getJSONObject(j);
                        String imgsrc = jimgsrc.getString("imgsrc");
                        ImageBean imageBean = new ImageBean();
                        imageBean.setImgsrc(imgsrc);
                        imgextra.add(imageBean);
                    }
                    newsBean.setImgextra(imgextra);
                }

                if (newsbean.has("priority")) {
                    int priority = newsbean.getInt("priority");
                    newsBean.setPriority(priority);
                }

                if (newsbean.has("lmodify")) {
                    String lmodify = newsbean.getString("lmodify");
                    newsBean.setLmodify(lmodify);
                }

                if (newsbean.has("imgsrc")) {
                    String imgsrc = newsbean.getString("imgsrc");
                    newsBean.setImgsrc(imgsrc);
                }

                //当i= 0事才有ads
                if (newsbean.has("ads")) {
                    ArrayList<AdBean> ads = new ArrayList<AdBean>();
                    JSONArray jads = newsbean.getJSONArray("ads");
                    for (int k = 0; k < jads.length(); k++) {
                        JSONObject jadbean = jads.getJSONObject(k);
                        String title1 = jadbean.getString("title");
                        String imgsrc1 = jadbean.getString("imgsrc");
                        AdBean adBean = new AdBean();
                        adBean.setImgsrc(imgsrc1);
                        adBean.setTitle(title1);
                        ads.add(adBean);
                    }
                    newsBean.setAds(ads);
                }

                if (newsbean.has("ptime")) {
                    String ptime = newsbean.getString("ptime");
                    newsBean.setPtime(ptime);
                }
                if (newsbean.has("source")) {
                    String source = newsbean.getString("source");
                    newsBean.setSource(source);
                }
                if (newsbean.has("url")) {
                    String url = newsbean.getString("url");
                    newsBean.setUrl(url);
                }
                T1348647909107.add(newsBean);
            }
            newsSetBean.setT1348647909107(T1348647909107);

        } catch (
                JSONException e
                )

        {
            e.printStackTrace();
        }

        return newsSetBean;
    }

    /**
     * 以下两个函数是XlistView监听事件的回调函数
     */
    @Override
    public void onRefresh() {
        this.flag = this.REFRESH;
        pageNo++;
        getData(pageNo, pageSize, topicBean);
    }

    @Override
    public void onLoadMore() {
        this.flag = this.LOADMORE;
        pageNo++;
        getData(pageNo, pageSize, topicBean);
    }

    //XListView的item点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.v("tag", "onItemClick>>>>>>>>>>>>>>>>>>>>>>>>>   " );
        int flag = parent.getAdapter().getItemViewType(position);
        NewsBean newsBean = (NewsBean) parent.getAdapter().getItem(position);
        Log.v("tag", "onItemClick>>>>>url   " + newsBean.getUrl());
        Intent intent = null;
        switch (flag){
            case ONE_IMG:
                intent = new Intent(getActivity(), NewsContentActivity.class);

                break;
            case MORE_IMG:
                intent = new Intent(getActivity(), NewsContentImgActivity.class);
                break;
        }
        intent.putExtra("NEWSBEAN", newsBean);
        startActivity(intent);

    }


    /**
     * XLstView的適配器
     */
    class XListViewAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private ArrayList<NewsBean> data = new ArrayList<NewsBean>();

        public XListViewAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }


        public void setData(ArrayList<NewsBean> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        /**
         * 上拉加载更多数据源
         *
         * @param data
         */
        public void pullLoadMore(ArrayList<NewsBean> data) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }

        /**
         * 下拉刷新设置数据源
         *
         * @param data
         */
        public void pullRefresh(ArrayList<NewsBean> data) {
            data.addAll(this.data);
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
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            NewsBean newsBean = data.get(position);
            ArrayList<ImageBean> imgextra = newsBean.getImgextra();
            if (imgextra != null && imgextra.size() != 0) {
                return MORE_IMG;
            }
            return ONE_IMG;
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int flag = getItemViewType(position);
            View v = null;
            switch (flag) {
                case ONE_IMG:
                    v = getOneImgView(position, convertView);
                    break;
                case MORE_IMG:
                    v = getMoreImgView(position, convertView);
                    break;
            }

            return v;
        }

        /**
         * 获得只有图片的ListView item布局View
         *
         * @param position
         * @param convertView
         * @return
         */
        public View getMoreImgView(int position, View convertView) {
            View v = null;
            ViewHolderImg holderImg;
            if (convertView == null) {
                holderImg = new ViewHolderImg();
                v = inflater.inflate(R.layout.item_xlist_view_image_layout, null);
                holderImg.imageView1 = (ImageView) v.findViewById(R.id.id_item_xlistview_imageview1);
                holderImg.imageView2 = (ImageView) v.findViewById(R.id.id_item_xlistview_imageview2);
                holderImg.imageView3 = (ImageView) v.findViewById(R.id.id_item_xlistview_imageview3);
                holderImg.sourceTextVeiw = (TextView) v.findViewById(R.id.id_item_xlistview_img_source_textvew);
                holderImg.timeTextView = (TextView) v.findViewById(R.id.id_item_xlistview_img_time_textview);
                holderImg.titleTextView = (TextView) v.findViewById(R.id.id_item_xlistview_img_title_textview);
                holderImg.replayCountTextView = (TextView) v.findViewById(R.id.id_item_xlistview_img_replaycount_textview);
                v.setTag(holderImg);
            } else {
                v = convertView;
                holderImg = (ViewHolderImg) v.getTag();
            }

            NewsBean newsBean = data.get(position);
            String time = newsBean.getLmodify();
            String source = newsBean.getSource();
            String imgSrc = newsBean.getImgsrc();
            String title = newsBean.getTitle();
            int replayCount = newsBean.getReplyCount();
            ArrayList<ImageBean> imgextra = newsBean.getImgextra();

            holderImg.sourceTextVeiw.setText(source);
            holderImg.timeTextView.setText(TimeUtil.parseTime(time));
            holderImg.titleTextView.setText(title);
            holderImg.replayCountTextView.setText(replayCount + "评论");
            if (imgSrc != null && !"".equals(imgSrc)) {
                Picasso.with(getActivity()).load(imgSrc).into(holderImg.imageView1);
            }
            if (imgextra != null) {
                int size = imgextra.size();
                if (size >= 2) {
                    Picasso.with(getActivity()).load(imgextra.get(0).getImgsrc()).into(holderImg.imageView2);
                    Picasso.with(getActivity()).load(imgextra.get(1).getImgsrc()).into(holderImg.imageView3);
                }
            }

            return v;
        }

        /**
         * 当只有一张图片时返回的item布局view
         *
         * @param position
         * @param convertView
         * @return
         */
        public View getOneImgView(int position, View convertView) {
            View v;
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                v = inflater.inflate(R.layout.item_xlist_view_layout, null);

                viewHolder.titleTextView = (TextView) v.findViewById(R.id.id_item_xlistview_title_textview);
                viewHolder.timeTextView = (TextView) v.findViewById(R.id.id_item_xlistview_time_textview);
                viewHolder.replayCountTextView = (TextView) v.findViewById(R.id.id_item_xlistview_replaycount_textview);
                viewHolder.imgImageView = (ImageView) v.findViewById(R.id.id_item_xlistview_img_imageview);
                viewHolder.priorityImageView = (ImageView) v.findViewById(R.id.id_item_xlistview_priority_icon);
                viewHolder.sourceTextView = (TextView) v.findViewById(R.id.id_item_xlistview_source_textview);

                v.setTag(viewHolder);
            } else {
                v = convertView;
                viewHolder = (ViewHolder) v.getTag();
            }

            NewsBean bean = data.get(position);
            String title = bean.getTitle();
            String time = bean.getLmodify();
            int priority = bean.getPriority();
            int replayCount = bean.getReplyCount();
            String imgSrc = bean.getImgsrc();
            String source = bean.getSource();

            viewHolder.titleTextView.setText(title);
            viewHolder.timeTextView.setText(TimeUtil.parseTime(time));
            viewHolder.replayCountTextView.setText("" + replayCount);
            if (priority >= 140) {
                viewHolder.priorityImageView.setVisibility(ImageView.VISIBLE);
                viewHolder.priorityImageView.setImageResource(R.drawable.hoticon_textpage);
                viewHolder.sourceTextView.setPadding(10, 0, 0, 0);
            } else if (priority >= 120) {
                viewHolder.priorityImageView.setVisibility(ImageView.VISIBLE);
                viewHolder.priorityImageView.setImageResource(R.drawable.recommenticon_textpage);
                viewHolder.sourceTextView.setPadding(10, 0, 0, 0);
            }
            viewHolder.sourceTextView.setText(source);
            if (imgSrc != null && !"".equals(imgSrc)) {
                Picasso.with(getActivity()).load(imgSrc).into(viewHolder.imgImageView);
            }

            return v;
        }


        /**
         * 有图片有数据的item项布局viewHolder
         */
        class ViewHolder {
            TextView titleTextView, replayCountTextView, timeTextView, sourceTextView;
            ImageView imgImageView, priorityImageView;
        }

        /**
         * 只有图片的item项布局ViewHolderImg
         */
        class ViewHolderImg {
            ImageView imageView1, imageView2, imageView3;
            TextView sourceTextVeiw, timeTextView, titleTextView, replayCountTextView;
        }
    }
}
