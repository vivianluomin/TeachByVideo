package com.example.asus1.teacherbyvideo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asus1.teacherbyvideo.Holders.CourseCommentHolder;
import com.example.asus1.teacherbyvideo.Holders.CourseDetailHolder;
import com.example.asus1.teacherbyvideo.Holders.CourseHeaderHolder;
import com.example.asus1.teacherbyvideo.Holders.CourseIntroduceHolder;
import com.example.asus1.teacherbyvideo.Holders.CourseItemHolder;
import com.example.asus1.teacherbyvideo.R;

import java.util.ArrayList;
import java.util.List;

public class CourseDetailAdapter extends RecyclerView.Adapter<CourseDetailHolder> {

    private static final int HEAD = 0;
    private static final int INTRODUCE = 1;
    private static final int VIDEO = 2;
    private static final int COMMENTS = 4;
    private static final int COMMENTS_ITEM = 3;

    private Context mContext;
    private List<String> mComments;
    private List<String> mVideos ;
    private VedioPremissionCallBack mCallBack;

    public CourseDetailAdapter(Context context,
                               List<String> comments,
                               List<String> videos,VedioPremissionCallBack callBack) {

        mContext = context;
        mComments = comments;
        mVideos = videos;
        mCallBack =callBack;
    }

    @Override
    public int getItemViewType(int position) {

        if(position == 0){
            return HEAD;
        }else if(position == 1){
            return INTRODUCE;
        } else if(position<mVideos.size()){
            return VIDEO;
        } else if(position == mVideos.size()){
            return  COMMENTS_ITEM;
        }
        else {
            return COMMENTS;
        }


    }

    @Override
    public CourseDetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        CourseDetailHolder holder;
        if(viewType == HEAD){
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_coursedetail_head
                    ,parent,false);
            holder = new CourseHeaderHolder(mContext,view);
        }else if(viewType == INTRODUCE){
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_coursedetail_introduce,
                    parent,false);
            holder = new CourseIntroduceHolder(view);
        }else if(viewType == COMMENTS_ITEM){
            view = LayoutInflater.from(mContext).inflate(R.layout.
                            layout_coursedetail_commemt,
                    parent,false);
            holder = new CourseIntroduceHolder(view);
        } else if(viewType == VIDEO){
            view = LayoutInflater.from(mContext).inflate(R.layout.layoyt_coursedetail_iteml,
                    parent,false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallBack.callBack();
                }
            });
            holder = new CourseItemHolder(view);
        }else {
            view = LayoutInflater.from(mContext).inflate(R.layout.
                            layout_crousedeatil_comment_item,
                    parent,false);
            holder = new CourseCommentHolder(view);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(CourseDetailHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mComments.size()+mVideos.size()+2;
    }
}
