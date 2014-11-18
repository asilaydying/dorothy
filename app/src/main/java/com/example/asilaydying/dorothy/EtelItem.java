package com.example.asilaydying.dorothy;

import android.graphics.Bitmap;

import java.util.UUID;

/**
 * Created by supergep on 2014.11.11..
 */
public class EtelItem {
    private UUID id;
    private String name;
    private String price;
    private String desc;
    private String link;

    public String getFilesize() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    private String filesize;
    private Bitmap EtelKep;
    private boolean needAdditional;

    public boolean isNeedAdditional() {
        return needAdditional;
    }

    public void setNeedAdditional(boolean needAdditional) {
        this.needAdditional = needAdditional;
    }

    private String additionalKaja;

    public String getAdditionalKaja() {
        return additionalKaja;
    }

    public void setAdditionalKaja(String additionalKaja) {
        this.additionalKaja = additionalKaja;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Bitmap getEtelKep() {
        return EtelKep;
    }

    public void setEtelKep(Bitmap etelKep) {
        EtelKep = etelKep;
    }
}
