package com.example.myapp.Itemmodel;


import com.example.myapp.menumodel.Menu;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Item implements Serializable {
    private String tenQuan,tenMon,diaChi,imageSourceID;
    private List<Menu> menuList;
    public Map<String, Boolean> stars = new HashMap<>();
    public Item() {
    }

    public Item(String tenQuan, String tenMon, String diaChi, String imageSourceID,List<Menu> menuList) {
        this.menuList=menuList;
        this.tenQuan = tenQuan;
        this.tenMon = tenMon;
        this.diaChi = diaChi;
        this.imageSourceID = imageSourceID;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("tenQuan", tenQuan);
        result.put("teMon", tenMon);
        result.put("diaChi", diaChi);
        result.put("imageSourceID", imageSourceID);
        result.put("menuList", menuList);
        result.put("stars", stars);

        return result;
    }

    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }

    public String getTenQuan() {
        return tenQuan;
    }

    public void setTenQuan(String tenQuan) {
        this.tenQuan = tenQuan;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getImageSourceID() {
        return imageSourceID;
    }

    public void setImageSourceID(String imageSourceID) {
        this.imageSourceID = imageSourceID;
    }
}
