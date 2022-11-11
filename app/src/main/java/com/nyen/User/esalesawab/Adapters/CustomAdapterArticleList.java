package com.nyen.User.esalesawab.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nyen.User.esalesawab.Database.FireBaseHelper;
import com.nyen.User.esalesawab.R;


import java.util.ArrayList;


public class CustomAdapterArticleList extends BaseAdapter {

    private final Context context;
    private final ArrayList<Articles> ArticleModelArrayList;

    private FireBaseHelper mDataBaseRef;


    public CustomAdapterArticleList(Context context, ArrayList<Articles> ArticleModelArrayList) {

        this.context = context;
        this.ArticleModelArrayList = ArticleModelArrayList;
    }



    @Override
    public int getCount() {
        return ArticleModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return ArticleModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        CustomAdapterArticleList.ViewHolder holder;
        mDataBaseRef = new FireBaseHelper(context);

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.article_model_list, null, true);

            holder.ArticleListModelTitle = (TextView) convertView.findViewById(R.id.ArticleListModelTtile);
            holder.articleListModelHadya = (TextView) convertView.findViewById(R.id.articleListModelhadya);
            holder.ArticleListModelRequest = (TextView) convertView.findViewById(R.id.ArticleListModelRequest);
            holder.ivArticleDelete = (ImageView) convertView.findViewById(R.id.ivArticleDelete);

            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (CustomAdapterArticleList.ViewHolder)convertView.getTag();
        }

        String name = String.valueOf(ArticleModelArrayList.get(position).getaName());
        holder.ArticleListModelTitle.setText(name);
        String hadya = String.valueOf(ArticleModelArrayList.get(position).getaHadya());
        holder.articleListModelHadya.setText(hadya);
        holder.ArticleListModelRequest.setOnClickListener(v -> {

            Articles mPosition = ArticleModelArrayList.get(position);
            Intent intent = new Intent(context, AddEsaleSawabRequest.class);
            intent.putExtra("id", mPosition);
            context.startActivity(intent);


        });

        holder.ivArticleDelete.setOnClickListener(v -> {
            mDataBaseRef.DeleteArticle(context,ArticleModelArrayList.get(position).getaID());
            ArticleModelArrayList.remove(position);
            notifyDataSetChanged();


        });


        return convertView;
    }

    private static class ViewHolder {

        protected TextView ArticleListModelTitle, articleListModelHadya, ArticleListModelRequest;
        protected ImageView  ivArticleDelete;

    }

}
