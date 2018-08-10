package com.forradical.binzee.collectionforlisab.base.litepal;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

public class ImageBean extends LitePalSupport implements Parcelable {

    private static final String TYPE_DEFAULT = "默认分组";

    @Column(unique = true)
    private int id;
    private String title;   // 图片名称
    private String path;    // 图片路径
    private String netUrl;    // 网络路径
    private List<String> typeList = new ArrayList<>();    // 图片标签列表
    private long createTime = -1;    // 完成时间
    private String comment;

    public ImageBean() {

    }

    public ImageBean(String path) {
        this.path = path;
        if (!typeList.contains(TYPE_DEFAULT))
            typeList.add(TYPE_DEFAULT);
    }

    protected ImageBean(Parcel in) {
        id = in.readInt();
        title = in.readString();
        path = in.readString();
        netUrl = in.readString();
        typeList = in.createStringArrayList();
        createTime = in.readLong();
        comment = in.readString();
    }

    public static final Creator<ImageBean> CREATOR = new Creator<ImageBean>() {
        @Override
        public ImageBean createFromParcel(Parcel in) {
            return new ImageBean(in);
        }

        @Override
        public ImageBean[] newArray(int size) {
            return new ImageBean[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public String getNetUrl() {
        return netUrl;
    }

    public void setNetUrl(String netUrl) {
        this.netUrl = netUrl;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<String> typeList) {
        this.typeList = typeList;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    //    ******↓公共方法

    /**
     * 添加类别
     *
     * @param type 自定义类别
     */
    public void addType(String type) {
        if (!typeList.contains(type))
            typeList.add(type);
    }

    /**
     * 移除类别
     *
     * @param type 自定义类别
     */
    public void removeType(String type) {
        typeList.remove(type);
    }

    /**
     * 判断类别
     *
     * @param type 自定义类别
     * @return 是否符合该类别
     */
    public boolean isMatchType(String type) {
        return typeList.contains(type);
    }

    public void setId(int id) {
        if (this.id >= 0)
            this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(path);
        parcel.writeString(netUrl);
        parcel.writeStringList(typeList);
        parcel.writeLong(createTime);
        parcel.writeString(comment);
    }
}
