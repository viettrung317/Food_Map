package com.example.myapp.menumodel;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bill implements Serializable {
    private String Time, maDonHang,Tongtien, tenKhacHang, diaChi, Sodt;
    private List<Menu> menuList;
    private Boolean trangthaiThanhToan,trangthaidonhang;
    public Map<String, Boolean> stars = new HashMap<>();

    public Bill() {
    }

    public Bill(String tongtien, String time, String maDonHang, String tenKhacHang, String diaChi, String sodt, List<Menu> menuList, boolean trangthaiThanhToan,boolean trangthaidoihang) {
        this.Tongtien = tongtien;
        this.Time = time;
        this.maDonHang = maDonHang;
        this.tenKhacHang = tenKhacHang;
        this.diaChi = diaChi;
        this.Sodt = sodt;
        this.menuList = menuList;
        this.trangthaiThanhToan = trangthaiThanhToan;
        this.trangthaidonhang=trangthaidoihang;
    }

    public Boolean getTrangthaidonhang() {
        return trangthaidonhang;
    }

    public void setTrangthaidonhang(Boolean trangthaidonhang) {
        this.trangthaidonhang = trangthaidonhang;
    }

    public String getTongtien() {
        return Tongtien;
    }

    public void setTongtien(String tongtien) {
        Tongtien = tongtien;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(String maDonHang) {
        this.maDonHang = maDonHang;
    }

    public String getTenKhacHang() {
        return tenKhacHang;
    }

    public void setTenKhacHang(String tenKhacHang) {
        this.tenKhacHang = tenKhacHang;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getSodt() {
        return Sodt;
    }

    public void setSodt(String sodt) {
        Sodt = sodt;
    }

    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }

    public Boolean getTrangthaiThanhToan() {
        return trangthaiThanhToan;
    }

    public void setTrangthaiThanhToan(Boolean trangthaiThanhToan) {
        this.trangthaiThanhToan = trangthaiThanhToan;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("maDonHang", maDonHang);
        result.put("tenKhacHang", tenKhacHang);
        result.put("Sodt", Sodt);
        result.put("diaChi", diaChi);
        result.put("Tongtien",Tongtien);
        result.put("menuList", menuList);
        result.put("Time", Time);
        result.put("trangthaiThanhToan", trangthaiThanhToan);
        result.put("trangthaidonhang",trangthaidonhang);
        result.put("stars", stars);

        return result;
    }
}


