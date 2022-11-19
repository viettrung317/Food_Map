package com.example.myapp.menumodel;

import com.example.myapp.Itemmodel.Item;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Menu implements Serializable{
    private String gia,tenMonAn,idImg;
    public int soLuong = 1;
    public Map<String, Boolean> stars = new HashMap<>();

    public Menu() {
    }

    public Menu(String gia,String tenMonAn,String idImg,int soLuong) {
        this.idImg=idImg;
        this.tenMonAn=tenMonAn;
        this.gia = gia;
        this.soLuong=soLuong;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }

    public String getTenMonAn() {
        return tenMonAn;
    }

    public void setTenMonAn(String tenMonAn) {
        this.tenMonAn = tenMonAn;
    }

    public String getIdImg() {
        return idImg;
    }

    public void setIdImg(String idImg) {
        this.idImg = idImg;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("tenMonAn", tenMonAn);
        result.put("gia", gia);
        result.put("idImg", idImg);
        result.put("soLuong",soLuong);
        result.put("stars", stars);

        return result;
    }
}
