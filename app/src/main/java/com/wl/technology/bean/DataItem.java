package com.wl.technology.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wanglin  on 2017/6/5 09:34.
 */
@Entity
public class DataItem implements Serializable {

    @Transient
    private static final long serialVersionUID = 0x34L;
    @Id(autoincrement = true)
    private Long id;
    @Property(nameInDb = "DATA_ID")
    @NotNull
    private String _id;
    private String desc;
    //将images集合解析成String后映射到该字段
    private String _images;
    @Transient
    private List<String> images;
    private String publishedAt;
    private String source;
    //存储的数据类型
    private String type;

    private String url;

    private String who;
    private boolean used;

    @Generated(hash = 1148939214)
    public DataItem(Long id, @NotNull String _id, String desc, String _images,
            String publishedAt, String source, String type, String url, String who,
            boolean used) {
        this.id = id;
        this._id = _id;
        this.desc = desc;
        this._images = _images;
        this.publishedAt = publishedAt;
        this.source = source;
        this.type = type;
        this.url = url;
        this.who = who;
        this.used = used;
    }

    @Generated(hash = 1750509646)
    public DataItem() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String get_images() {
        return this._images;
    }

    public void set_images(String _images) {
        this._images = _images;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public boolean getUsed() {
        return this.used;
    }


    @Override
    public String toString() {
        return "DataItem{" +
                "id=" + id +
                ", _id='" + _id + '\'' +
                ", desc='" + desc + '\'' +
                ", _images='" + _images + '\'' +
                ", images=" + images +
                ", publishedAt='" + publishedAt + '\'' +
                ", source='" + source + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", who='" + who + '\'' +
                ", used=" + used +
                '}';
    }



}
