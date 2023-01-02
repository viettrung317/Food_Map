package com.example.myapp.menumodel;

import java.io.Serializable;
import java.util.List;

public class Bill implements Serializable {
    private String Time, maDonHang,Tongtien, tenKhacHang, diaChi, Sodt;
    private List<Menu> menuList;
    private Boolean trangthaiThanhToan;

    public Bill() {
    }

    public Bill(String tongtien, String time, String maDonHang, String tenKhacHang, String diaChi, String sodt, List<Menu> menuList, Boolean trangthaiThanhToan) {
        this.Tongtien = tongtien;
        this.Time = time;
        this.maDonHang = maDonHang;
        this.tenKhacHang = tenKhacHang;
        this.diaChi = diaChi;
        this.Sodt = sodt;
        this.menuList = menuList;
        this.trangthaiThanhToan = trangthaiThanhToan;
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
}


