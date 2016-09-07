> 本文仿照新浪微博/QQ空间实现了滑动自动播放视频的功能。

>本文来自个人博客：http://www.zhangliwei.date

>如有疑问欢迎讨论,感谢您的关注。

<!-- more --> 

#### 效果图
![滑动自动播放1](http://i2.buimg.com/567571/fb49b7d9d13947bf.gif)
![滑动自动播放2](http://i2.buimg.com/567571/f5c131c21ba350e1.gif)

---

### 关键代码
#### 1.监听滚动事件
首先要给listview添加setOnScrollListener监听,注意这个监听在recyclerView上是addOnScrollListener，也就是说下面代码同时支持recyclerView。

	public int firstVisible=0,visibleCount=0, totalCount=0;
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
        
 监听里会有两个方法，**我们用onScroll方法记录 当前第一个可见Item，以及可见Item总数，用onScrollStateChanged来监听手滑动屏幕的整个过程。** 当onScrollStateChanged 中的scrollState字段值等于SCROLL_STATE_IDLE 时，代表本次滑动完毕并停止滚动🙄感兴趣的朋友可以自行百度另外两个参数的意思，没准对你的需求有帮助🙄。
 
 #### 2.处理视频逻辑
 	
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
    
 首先是根据总数循环判断 Item 是否有视频，如果有再利用Rect类获取视图在屏幕坐标中的可视区域。基本核心代码就这些是不是非常简单？
 
 ----
 
### 项目源码 [autovideoplay](https://github.com/zzzlw/autovideoplayer)
### 技术博客 [Wells'Note](http://zhangliwei.date/)

### 视频播放器依赖
   	compile 'fm.jiecao:jiecaovideoplayer:4.6.3' 
 
