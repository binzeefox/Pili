package com.forradical.binzee.collectionforlisab.views.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.forradical.binzee.collectionforlisab.base.FoxActivity;
import com.forradical.binzee.collectionforlisab.base.litepal.ImageBean;
import com.forradical.binzee.collectionforlisab.utils.RxHelp;
import com.forradical.binzee.collectionforlisab.views.ResizableImageView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;

public class MainGalleryRecyclerViewAdapter extends RecyclerView.Adapter<MainGalleryRecyclerViewAdapter.ViewHolder> {
    private List<ImageBean> dataList;
    private FoxActivity mContext;
    private OnItemClickListener mListener;
    private List<Float> whRadius = new ArrayList<>();

    public MainGalleryRecyclerViewAdapter(FoxActivity context, final List<ImageBean> datas) {
        mContext = context;
        setData(datas);
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
        holder.titleField.setText(title);

        String path = bean.getPath();
        float ratio = whRadius.get(position);
        holder.imageField.setRatio(ratio);
        Glide.with(mContext).load(path).into(holder.imageField);
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
                mListener.onMoreClick(view, bean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    /**
     * 子项点击事件监听器
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    /**
     * 添加数据
     */
    public void setData(final List<ImageBean> data) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                if (data != null) {
                    for (ImageBean bean : data) {
                        String path = bean.getPath();
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(path, options);
                        float radius = ((float) options.outWidth) / ((float)options.outHeight);
                        whRadius.add(radius);
                    }
                }
                emitter.onComplete();
            }
        }).compose(RxHelp.applySchedulers())
                .subscribe(new RxHelp.CompleteObserver<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mContext.dContainer.add(d);
                    }

                    @Override
                    public void onComplete() {
                        dataList = data;
                        notifyDataSetChanged();
                    }
                });
    }

    /**
     * 子项点击事件监听器
     */
    public interface OnItemClickListener {
        void onImageClick(View target, int position);

        void onMoreClick(View target, ImageBean bean);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ResizableImageView imageField;
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
