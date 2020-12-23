package com.ljkj.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * 文件名：ChartAdapter
 * 作者：Turbo
 * 时间： 12/23/20 2:50 PM
 * 蚁穴虽小，溃之千里。
 */
public class ChartAdapter extends RecyclerView.Adapter<ChartAdapter.ViewHolder>{

    private List<String> newsList;

    public ChartAdapter(List<String> newsList) {
        this.newsList = newsList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String news = newsList.get(position);
        holder.newsTitle.setText(news);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView newsTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            newsTitle = itemView.findViewById(R.id.newsTitle);
        }
    }
}
