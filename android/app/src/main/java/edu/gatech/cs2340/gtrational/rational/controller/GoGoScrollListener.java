package edu.gatech.cs2340.gtrational.rational.controller;

import android.widget.AbsListView;

/**
 * Created by james on 10/17/2017.
 * Scroll listener for rat data
 */

abstract class GoGoScrollListener implements AbsListView.OnScrollListener {
    private int previousTotalItemCount = 0;

    public GoGoScrollListener() {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount < previousTotalItemCount) {
            this.previousTotalItemCount = totalItemCount;
        }

        if ((totalItemCount > previousTotalItemCount)) {
            previousTotalItemCount = totalItemCount;
        }

        int visibleThreshold = 5;
        if ((firstVisibleItem + visibleItemCount + visibleThreshold) >= totalItemCount) {
            onLoadMore();
        }
    }

    public abstract void onLoadMore();
}
