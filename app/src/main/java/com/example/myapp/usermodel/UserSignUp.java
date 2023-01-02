package com.example.myapp.usermodel;

import com.example.myapp.menumodel.Bill;
import com.example.myapp.menumodel.Menu;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserSignUp implements Serializable {
    private String userName,email,avatar,gioiTinh,soDT,ngaySinh;
    private List<Menu> listoder,listDaMua;
    private List<Bill> listBill;
    public Map<String, Boolean> stars = new HashMap<>();
    public UserSignUp(){

    }
    public UserSignUp(String userName,String email,String avatar,List<Menu>listoder,List<Menu> listDaMua,String gioiTinh,String soDT,String ngaySinh,List<Bill> listBill) {
        this.userName=userName;
        this.email = email;
        this.avatar=avatar;
        this.listoder=listoder;
        this.listDaMua=listDaMua;
        this.gioiTinh=gioiTinh;
        this.ngaySinh=ngaySinh;
        this.soDT=soDT;
        this.listBill=listBill;
    }

    public List<Bill> getListBill() {
        return listBill;
    }

    public void setListBill(List<Bill> listBill) {
        this.listBill = listBill;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getSoDT() {
        return soDT;
    }

    public void setSoDT(String soDT) {
        this.soDT = soDT;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Menu> getListoder() {
        return listoder;
    }

    public void setListoder(List<Menu> listoder) {
        this.listoder = listoder;
    }

    public List<Menu> getListDaMua() {
        return listDaMua;
    }

    public void setListDaMua(List<Menu> listDaMua) {
        this.listDaMua = listDaMua;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userName",userName );
        result.put("email", email);
        result.put("avatar", avatar);
        result.put("listoder", listoder);
        result.put("listBill",listBill);
        result.put("listDaMua",listDaMua);
        result.put("gioiTinh",gioiTinh);
        result.put("ngaySinh'",ngaySinh);
        result.put("soDT",soDT);
        result.put("stars", stars);

        return result;
    }
}
