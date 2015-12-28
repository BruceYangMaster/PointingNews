package com.pointingnews.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.pointingnews.R;
import com.pointingnews.customwidget.MyTitleView;
import com.pointingnews.model.ImageBean;
import com.pointingnews.model.NewsBean;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsContentFragment extends Fragment implements View.OnClickListener{


    private WebView mWebView;
    private ProgressBar mProgressBar;
    private TextView mSourceTextView, mTitleTextView, mTimeTextView,mImgCountTextView;
    private ImageView mContentImageView;
    private MyTitleView mMyTitleView;
    private Intent mIntent;
    private String url;
    private long current = 0;//数据加载当前进度
    private NewsBean newsBean;
    private ArrayList<ImageBean> imageBeans = new ArrayList<>();

    public static NewsContentFragment newInstance(NewsBean newsBean) {

        Bundle args = new Bundle();
        args.putSerializable("NEWSBEAN",newsBean);
        NewsContentFragment fragment = new NewsContentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if(bundle != null){
            this.newsBean = (NewsBean) bundle.getSerializable("NEWSBEAN");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_news_content_layout, container, false);

        mWebView = (WebView) v.findViewById(R.id.id_activity_news_conent_webview);
        mProgressBar = (ProgressBar) v.findViewById(R.id.id_activity_news_content_progressbar);
        mTitleTextView = (TextView) v.findViewById(R.id.id_activity_news_content_title_textview);
        mSourceTextView = (TextView) v.findViewById(R.id.id_activity_news_content_source_textview);
        mTimeTextView = (TextView) v.findViewById(R.id.id_activity_news_content_time_textview);
        mContentImageView = (ImageView) v.findViewById(R.id.id_activity_news_conent_imageview);
        mImgCountTextView = (TextView) v.findViewById(R.id.id_activity_news_content_img_count_textview);
        mMyTitleView = (MyTitleView) v.findViewById(R.id.id_news_content_my_title_view);

        mMyTitleView.setOnClickListener(this);
        mContentImageView.setOnClickListener(this);

        WebSettings settings = mWebView.getSettings();
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        settings.setJavaScriptEnabled(true);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(this.newsBean != null){
            getData(newsBean);
        }

    }

    /**
     * 获取数据源
     *
     * @param newsBean  ListView中列表item中对应的数据
     * @return
     */
    public void getData(NewsBean newsBean) {
        final String docid = newsBean.getDocid();
        url = "http://c.m.163.com/nc/article/" + docid + "/full.html";
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(getActivity(), url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    String json = new String(bytes, "UTF-8");
                    paseJsonToView(json, docid);  //将json数据解析到相对应的控件中
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                mProgressBar.setMax((int)bytesWritten);
                mProgressBar.setProgress((int)current);
                current = bytesWritten;
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.v("tag", "联网获取数据失败onFailure");
            }
        });

    }

    /**
     * 解析json数据到控件
     *
     * @param json
     */
    public void paseJsonToView(String json, String docid) throws Exception {
        JSONObject root = new JSONObject(json);
        JSONObject content = root.getJSONObject(docid);
        String title = content.getString("title");
        String source = content.getString("source");
        String ptime = content.getString("ptime");
        String body = content.getString("body");
        body = "<html><body>" + body + "</body></html>";
        JSONArray imgs = content.getJSONArray("img");

        int lenth = imgs.length();
        if(lenth != 0){
            mImgCountTextView.setText("共"+lenth+"张");
        }
        for (int j = 0; j < lenth; j++) {
            JSONObject img = imgs.getJSONObject(j);
            String alt = img.getString("alt");
            String src = img.getString("src");
            if(j == 0 && src != null && !"".equals(src)){
                Picasso.with(getActivity()).load(src).into(mContentImageView);
            }
            ImageBean imageBean = new ImageBean();
            imageBean.setImgsrc(src);
            imageBean.setAlt(alt);
            imageBeans.add(imageBean);
        }
        mTitleTextView.setText(title);
        mSourceTextView.setText(source);
        mTimeTextView.setText(ptime);
        mWebView.loadDataWithBaseURL(null, body, "text/html", "UTF-8", null);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress == 100){
                    mProgressBar.setMax(100);
                    mProgressBar.setProgress(100);
                    mProgressBar.setVisibility(ProgressBar.GONE);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_my_title_view_back_imageview:
                getActivity().finish();
                break;
            case R.id.id_activity_news_conent_imageview:
                newsBean.setImgextra(imageBeans);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.addToBackStack(this.getTag());
                transaction.add(R.id.id_activity_news_content_fragment_container_layout,NewsContentImgFragment.newInstance(newsBean));
                transaction.commit();

        }
    }

}
