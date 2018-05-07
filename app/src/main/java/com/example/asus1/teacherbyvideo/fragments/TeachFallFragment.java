package com.example.asus1.teacherbyvideo.fragments;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asus1.teacherbyvideo.R;
import com.example.asus1.teacherbyvideo.adapters.TeachFallAdapter;

/**
 * Created by asus1 on 2018/5/2.
 */

public class TeachFallFragment extends Fragment {


    private RecyclerView mRecycler;
    private TeachFallAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teachfall,container,false);
        mRecycler = (RecyclerView)view.findViewById(R.id.recycler_view);
        mAdapter = new TeachFallAdapter(getContext());
        mRecycler.setLayoutManager(new GridLayoutManager(getContext(),2,
                GridLayoutManager.VERTICAL,false));
        mRecycler.setAdapter(mAdapter);
        return view;
    }
}
