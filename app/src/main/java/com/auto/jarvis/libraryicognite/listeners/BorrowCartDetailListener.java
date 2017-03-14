package com.auto.jarvis.libraryicognite.listeners;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by HaVH on 3/14/17.
 */

public class BorrowCartDetailListener implements RecyclerView.OnItemTouchListener {

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener _listener;

    private GestureDetector _GestureDetector;


    public BorrowCartDetailListener(Context context, final RecyclerView recyclerView, OnItemClickListener onItemClickListener) {
        _listener = onItemClickListener;


        _GestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View chilView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (chilView != null && _listener != null) {
                    _listener.onItemLongClick(chilView, recyclerView.getChildAdapterPosition(chilView));
                }
            }
        });
    }



    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View childView = rv.findChildViewUnder(e.getX(), e.getY());

        if (childView != null && _listener != null && _GestureDetector.onTouchEvent(e)) {
            _listener.onItemClick(childView, rv.getChildAdapterPosition(childView));
        }

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
