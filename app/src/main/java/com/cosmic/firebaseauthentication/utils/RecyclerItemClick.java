package com.cosmic.firebaseauthentication.utils;

import android.content.Context;
import android.gesture.GestureUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerItemClick implements RecyclerView.OnItemTouchListener {

    private final OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
    GestureDetector gestureDetector;

    public RecyclerItemClick(Context context, OnItemClickListener listener){
        onItemClickListener = listener;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }


    /**
     * required implemented methods for RecyclerView.OnItemTouchListener
     * key method here is onInterceptedTouchEvent()
     * This will intercept the child view touched or tapped in the main activity and get the position of the child view
     * in the recycler view
     * By getting the position we can retrieve the Enemy object, the position in the list or the key in the database, whichever we need
     * We implement this so that on touch of the child view, we can transition to a ViewEnemyActivity and see a specific record from the databse displayed
     */
    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        //collects the X and Y coordinates of the child view selected
        View childView = rv.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && gestureDetector != null && gestureDetector.onTouchEvent(e)){
            onItemClickListener.onItemClick(childView, rv.getChildAdapterPosition(childView));
        }
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }




}
