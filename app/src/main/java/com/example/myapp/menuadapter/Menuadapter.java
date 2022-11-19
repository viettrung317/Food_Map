package com.example.myapp.menuadapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapp.Itemmodel.Item;
import com.example.myapp.R;
import com.example.myapp.convert.DataConvert;
import com.example.myapp.menumodel.Menu;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Menuadapter extends ArrayAdapter<Menu> {
    private TextView txtTenMonMenu,txtGia,txtSoLuong;
    private ImageView imgMon;
    Activity context; int resource; List<Menu> objects;
    public Menuadapter(Activity context, int resource, List<Menu> objects) {
        super(context, resource, objects);
        this.context=context;
        this.objects=objects;
        this.resource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater=this.context.getLayoutInflater();
        View row=layoutInflater.inflate(this.resource,null);
        txtTenMonMenu=(TextView) row.findViewById(R.id.txtTenMonMenu);
        txtGia=(TextView) row.findViewById(R.id.txtGia);
        txtSoLuong=(TextView) row.findViewById(R.id.txtSoLuong);
        imgMon=(ImageView) row.findViewById(R.id.imgMon);
        final Menu menu=this.objects.get(position);
        txtTenMonMenu.setText(menu.getTenMonAn());
        txtGia.setText(menu.getGia());
        String sl=String.valueOf("Số lượng : "+menu.getSoLuong());
        txtSoLuong.setText(sl);
        Picasso.with(context).load(menu.getIdImg()).into(imgMon);
        return row;
    }
}
