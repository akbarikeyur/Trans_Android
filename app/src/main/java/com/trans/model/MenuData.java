package com.trans.model;

/**
 * Created by Amisha on 21-Feb-18.
 */

public class MenuData {
    String title;
    int image, total = 0;
    String url;
    boolean selecetd, divider = false;


    public MenuData(String title, int image, int total) {
        this.title = title;
        this.image = image;
        this.total = total;
    }

    public MenuData(String title, int image, String url, boolean selecetd, boolean divider) {
        this.title = title;
        this.image = image;
        this.url = url;
        this.selecetd = selecetd;
        this.divider = divider;
    }


    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public boolean isSelecetd() {
        return selecetd;
    }

    public void setSelecetd(boolean selecetd) {
        this.selecetd = selecetd;
    }

    public boolean isDivider() {
        return divider;
    }

    public void setDivider(boolean divider) {
        this.divider = divider;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
