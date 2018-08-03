package com.forradical.binzee.collectionforlisab.base.litepal;

import android.net.Uri;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

public class ImageBean extends LitePalSupport {

    private static final String TYPE_DEFAULT = "type_default";

    @Column(unique = true)
    private int id;
    private String title;   // 图片名称
    private String path;    // 图片路径
    private String netUrl;    // 网络路径
    private List<String> typeList = new ArrayList<>();    // 图片标签列表
    private long createTime;    // 完成时间
    private String comment;

    public ImageBean(String path) {
        this.path = path;
        typeList.add(TYPE_DEFAULT);
    }

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
     * @param type  自定义类别
     */
    public void addType(String type){
        typeList.add(type);
    }

    /**
     * 移除类别
     * @param type  自定义类别
     */
    public void removeType(String type){
        typeList.remove(type);
    }

    /**
     * 判断类别
     * @param type  自定义类别
     * @return  是否符合该类别
     */
    public boolean isMatchType(String type){
        return typeList.contains(type);
    }

    public void setId(int id){
        if (this.id >= 0)
            this.id = id;
    }
}
