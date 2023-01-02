package com.example.myapp.itemadapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.myapp.R;
import com.example.myapp.menumodel.Menu;

import java.util.List;

public class BillMenuAdapter extends ArrayAdapter<Menu> {
    private Activity context;
    private int resource;
    private List<Menu> objects;
    public BillMenuAdapter(@NonNull Activity context, int resource, @NonNull List<Menu> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater=this.context.getLayoutInflater();
        View row=layoutInflater.inflate(this.resource,null);
        TextView txtTenmonItemBill=row.findViewById(R.id.txtTenmonItemBill);
        TextView txtSLItemBill=row.findViewById(R.id.txtSLItemBill);
        TextView txtDGItemBill=row.findViewById(R.id.txtDGItemBill);
        TextView txtTTItemBill=row.findViewById(R.id.txtTTItemBill);
        final Menu menu=this.objects.get(position);
        txtTenmonItemBill.setText(menu.getTenMonAn());
        String txt= String.valueOf(menu.getSoLuong());
        txtSLItemBill.setText(txt);
        txtDGItemBill.setText(menu.getGia());
        int i=menu.getGia().indexOf(".");
        int j=menu.getGia().indexOf("đ");
        String gia=menu.getGia().substring(0,i) + menu.getGia().substring(i+1,j);
        int g= Integer.parseInt(gia);
        double tt=(double) (menu.getSoLuong()*g);
        String txt1= String.valueOf(tt);
        txtTTItemBill.setText(txt1);
        return row;

    }
}
