package com.pointingnews.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pointingnews.R;
import com.pointingnews.activity.MainActivity;
import com.pointingnews.xlistview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class WelcomTouTiaoFragment extends Fragment {

    private CircleImageView mHeadImageView;

    public static WelcomTouTiaoFragment newInstance() {
        
        Bundle args = new Bundle();
        
        WelcomTouTiaoFragment fragment = new WelcomTouTiaoFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcom_tou_tiao_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mHeadImageView = (CircleImageView) getView().findViewById(R.id.id_fragment_welcom_head_imageview);

        new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if(bitmap != null){
                    mHeadImageView.setImageBitmap(bitmap);
                }

                startActivity(new Intent(getActivity(),MainActivity.class));
                getActivity().finish();
            }
        }.execute();
    }
}
