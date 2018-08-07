package com.forradical.binzee.collectionforlisab.views.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.forradical.binzee.collectionforlisab.R;
import com.forradical.binzee.collectionforlisab.base.litepal.ImageBean;

import java.util.List;

public class MainGalleryRecyclerViewAdapter extends RecyclerView.Adapter<MainGalleryRecyclerViewAdapter.ViewHolder>{
    private List<ImageBean> dataList;
    private Context mContext;
    private OnItemClickListener mListener;

    public MainGalleryRecyclerViewAdapter(Context context, List<ImageBean> datas){
        mContext = context;
        dataList = datas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.main_gallery_cards_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ImageBean bean = dataList.get(position);
        final int p = position;
        String title = bean.getTitle();
        //TODO 测试数据，应改为 bean.getPath();
        int url = bean.getId();
        holder.titleField.setText(title);
        Glide.with(mContext).load(url).into(holder.imageField);
        holder.imageField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener == null)
                    return;
                mListener.onImageClick(view, p);
            }
        });
        holder.detailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener == null)
                    return;
                mListener.onMoreClick(bean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    /**
     * 子项点击事件监听器
     */
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    /**
     * 子项点击事件监听器
     */
    public interface OnItemClickListener{
        void onImageClick(View target, int position);
        void onMoreClick(ImageBean bean);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageField;
        TextView titleField;
        ImageButton detailBtn;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageField = itemView.findViewById(R.id.picture_field);
            detailBtn = itemView.findViewById(R.id.detail_btn);
            titleField = itemView.findViewById(R.id.title_field);
        }
    }

}
