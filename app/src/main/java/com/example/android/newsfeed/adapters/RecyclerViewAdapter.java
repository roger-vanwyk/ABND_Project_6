package com.example.android.newsfeed.adapters;

// Created on 24/01/2020 by Roger van Wyk.

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.android.newsfeed.R;
import com.example.android.newsfeed.model.Articles;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<ArticlesViewHolder> {

    private ArrayList<Articles> items;
    private Activity activity;
    private OnItemClickListener mListener;

    public RecyclerViewAdapter(Activity activity, ArrayList<Articles> articlesList, OnItemClickListener listener) {
        this.activity = activity;
        this.items = articlesList;
        this.mListener = listener;
    }

    @Override
    public ArticlesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from (parent.getContext ( )).inflate (R.layout.list_item, parent, false);
        return new ArticlesViewHolder (view);
    }

    @Override
    public void onBindViewHolder(ArticlesViewHolder holder, final int position) {

        Articles articles = items.get (position);
        holder.articlesCategory.setText (articles.getCategory ( ));
        holder.articlesTitle.setText (articles.getTitle ( ));
        holder.articlesType.setText (activity.getResources ( ).getString (R.string.type, articles.getType ( )));
        holder.articlesDate.setText (activity.getResources ( ).getString (R.string.date, articles.getDate ( )));
        holder.bind (items.get (position), mListener);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return items.size ( );
    }

    // Updates the adapter item list with a new list of data
    public void updateAdapterData(ArrayList<Articles> newData) {
        this.items = newData;
        notifyDataSetChanged ( );
    }

    // Returns adapter data list
    public ArrayList<Articles> getAdapterData() {
        return this.items;
    }

    public interface OnItemClickListener {
        void onItemClick(Articles articles);
    }
}