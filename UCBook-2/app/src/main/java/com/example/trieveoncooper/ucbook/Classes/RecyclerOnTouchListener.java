package com.example.trieveoncooper.ucbook.Classes;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.example.trieveoncooper.ucbook.activities.UserList;

/**
 * Created by trieveoncooper on 12/4/17.
 */


public  class RecyclerOnTouchListener implements RecyclerView.OnItemTouchListener {


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }


        private GestureDetector gestureDetector;
        private RecyclerOnTouchListener.ClickListener clickListener;

        public RecyclerOnTouchListener(Context context, final RecyclerView recyclerView, final RecyclerOnTouchListener.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
Log.d("A","i was TOUCHED TOUCHED");

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
