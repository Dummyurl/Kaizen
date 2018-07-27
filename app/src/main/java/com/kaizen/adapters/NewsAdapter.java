package com.kaizen.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kaizen.R;
import com.kaizen.models.Articles;
import com.kaizen.reterofit.APIUrls;

public class NewsAdapter extends CommonRecyclerAdapter<Articles> {

    private Context context;

    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View itemView = LayoutInflater.from(context).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(itemView);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
        NewsViewHolder newsViewHolder = (NewsViewHolder) holder;
        newsViewHolder.bindData(position);
    }

    private class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tv_title, tv_description;
        private ImageView iv_news;

        private NewsViewHolder(View view) {
            super(view);
            tv_title = view.findViewById(R.id.tv_title);
            tv_description = view.findViewById(R.id.tv_description);
            iv_news = view.findViewById(R.id.iv_news);
            view.setOnClickListener(this);
        }

        public void bindData(int position) {
            Articles articles = getItem(position);
            tv_title.setText(articles.getTitle());
            tv_description.setText(articles.getDescription());

            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_place_holder)
                    .error(R.drawable.ic_place_holder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true);

            Glide.with(context).setDefaultRequestOptions(requestOptions).load(articles.getUrlToImage()).into(iv_news);

        }

        @Override
        public void onClick(View v) {
            Articles articles = getItem(getAdapterPosition());

            //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(articles.getUrl()));
            //context.startActivity(browserIntent);
        }
    }
}
