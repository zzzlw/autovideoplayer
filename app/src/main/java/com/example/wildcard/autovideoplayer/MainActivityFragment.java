package com.example.wildcard.autovideoplayer;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    @Bind(R.id.videoList)
    ListView videoList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        return view;

    }
    public int firstVisible=0,visibleCount=0, totalCount=0;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
//        recyclerView.addOnScrollListener
        videoList.setAdapter(new VideoListAdapter(getActivity()));
        videoList.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        Log.e("videoTest", "SCROLL_STATE_FLING");
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        Log.e("videoTest", "SCROLL_STATE_IDLE");
                        autoPlayVideo(view);
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        Log.e("videoTest", "SCROLL_STATE_TOUCH_SCROLL");

                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // firstVisibleItem   当前第一个可见的item
                // visibleItemCount   当前可见的item个数
                if (firstVisible == firstVisibleItem) {
                    return;
                }
                firstVisible = firstVisibleItem;
                visibleCount = visibleItemCount;
                totalCount = totalItemCount;
            }
        });
    }
    void autoPlayVideo(AbsListView view){
        Log.e("videoTest", "firstVisiblePos  =  " + firstVisible + "visibleItemCount =  " + visibleCount);
        for (int i = 0; i < visibleCount; i++) {
            if (view!=null&&view.getChildAt(i)!=null&&view.getChildAt(i).findViewById(R.id.videoplayer) != null) {
                JCVideoPlayerStandard videoPlayerStandard1 = (JCVideoPlayerStandard) view.getChildAt(i).findViewById(R.id.videoplayer);
                Rect rect = new Rect();
                videoPlayerStandard1.getLocalVisibleRect(rect);
                int videoheight3 = videoPlayerStandard1.getHeight();
                Log.e("videoTest","i="+i+"==="+"videoheight3:"+videoheight3+"==="+"rect.top:"+rect.top+"==="+"rect.bottom:"+rect.bottom);
                if (rect.top==0&&rect.bottom==videoheight3)
                {
                    if (videoPlayerStandard1.currentState == JCVideoPlayer.CURRENT_STATE_NORMAL || videoPlayerStandard1.currentState == JCVideoPlayer.CURRENT_STATE_ERROR) {
                        Log.e("videoTest", videoPlayerStandard1.currentState + "======================performClick======================");
                        videoPlayerStandard1.startButton.performClick();
                        VPApplication.instance.VideoPlaying=videoPlayerStandard1;
                    }
                    return;
                }

            }
        }
        Log.e("videoTest", "======================releaseAllVideos=====================");
        JCVideoPlayer.releaseAllVideos();
        VPApplication.instance.VideoPlaying=null;
    }
    public class VideoListAdapter extends BaseAdapter {

        int[] videoIndexs = {0,0,1,1,1,0,0,1,0,0,1,1,1,0,0};
        Context context;
        LayoutInflater mInflater;

        public VideoListAdapter(Context context) {
            this.context = context;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return videoIndexs.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //This is the point
            if (convertView != null && convertView.getTag() != null && convertView.getTag() instanceof VideoHolder) {
                ((VideoHolder) convertView.getTag()).jcVideoPlayer.release();
            }

            if (videoIndexs[position] == 1) {
                VideoHolder viewHolder;
                if (convertView != null && convertView.getTag() != null && convertView.getTag() instanceof VideoHolder) {
                    viewHolder = (VideoHolder) convertView.getTag();
                } else {
                    viewHolder = new VideoHolder();
                    convertView = mInflater.inflate(R.layout.item_videoview, null);
                    viewHolder.jcVideoPlayer = (JCVideoPlayerStandard) convertView.findViewById(R.id.videoplayer);
                    convertView.setTag(viewHolder);
                }

                boolean setUp = viewHolder.jcVideoPlayer.setUp(
                        "http://gslb.miaopai.com/stream/ed5HCfnhovu3tyIQAiv60Q__.mp4", JCVideoPlayer.SCREEN_LAYOUT_LIST,
                        "");
                if (setUp) {
                    Glide.with(getActivity()).load("http://a4.att.hudong.com/05/71/01300000057455120185716259013.jpg").into(viewHolder.jcVideoPlayer.thumbImageView);
                }
            } else {

                ImageViewHolder imageViewHolder;
                if (convertView != null && convertView.getTag() != null && convertView.getTag() instanceof ImageViewHolder) {
                    imageViewHolder = (ImageViewHolder) convertView.getTag();
                } else {
                    imageViewHolder = new ImageViewHolder();
                    LayoutInflater mInflater = LayoutInflater.from(context);
                    convertView = mInflater.inflate(R.layout.item_textview, null);
                    imageViewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_view);
                    Glide.with(getActivity()).load("http://img04.tooopen.com/images/20131019/sy_43185978222.jpg").into(imageViewHolder.imageView);
                    convertView.setTag(imageViewHolder);
                }

            }
            return convertView;
        }

        class VideoHolder {
            JCVideoPlayerStandard jcVideoPlayer;
        }

        class ImageViewHolder {
            ImageView imageView;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
