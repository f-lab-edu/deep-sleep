package com.flab.deepsleep.utils

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RecyclerItemClickListener(
    context: Context,
    recyclerView: RecyclerView,
    private val onItemClick: (View, Int) -> Unit
) : RecyclerView.OnItemTouchListener {

    private val gestureDetector =
        GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }
        })

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        val child = rv.findChildViewUnder(e.x, e.y)
        if (child != null && gestureDetector.onTouchEvent(e)) {
            onItemClick(child, rv.getChildAdapterPosition(child))
            return true
        }
        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
}