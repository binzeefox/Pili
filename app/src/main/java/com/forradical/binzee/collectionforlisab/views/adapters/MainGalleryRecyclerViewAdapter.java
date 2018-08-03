package com.forradical.binzee.collectionforlisab.views.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.forradical.binzee.collectionforlisab.R;
import com.forradical.binzee.collectionforlisab.base.litepal.ImageBean;

import java.util.List;

public class MainGalleryRecyclerViewAdapter extends RecyclerView.Adapter<MainGalleryRecyclerViewAdapter.ViewHolder> {
    private List<ImageBean> dataList;
    private Context mContext;

    public MainGalleryRecyclerViewAdapter(Context context, List<ImageBean> datas){
        dataList = datas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.main_gallery_cards_layout, parent);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageBean bean = dataList.get(position);
        String title = bean.getTitle();
        String url = bean.getPath();
        holder.titleField.setText(title);
        Glide.with(mContext).load(url).into(holder.imageField);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageField;
        TextView titleField;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageField = itemView.findViewById(R.id.picture_field);
            titleField = itemView.findViewById(R.id.title_field);
        }
    }
}
