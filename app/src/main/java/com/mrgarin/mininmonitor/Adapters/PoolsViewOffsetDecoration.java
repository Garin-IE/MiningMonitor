package com.mrgarin.mininmonitor.Adapters;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class PoolsViewOffsetDecoration extends RecyclerView.ItemDecoration {
    private int offset;

    public PoolsViewOffsetDecoration(int offset){
        this.offset = offset;
    }

    @Override
    public void getItemOffsets (Rect outRect, View view, RecyclerView parent,
                                RecyclerView.State state) {
        outRect.left = offset;
        outRect.right = offset;
        outRect.top = offset;
    }
}
