package com.example.myapp.itemadapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapp.Itemmodel.Item;
import com.example.myapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

public class TitleAdapter implements GoogleMap.InfoWindowAdapter {
    Activity context;
    Item item;
    public TitleAdapter(Activity context,Item item){
        this.context=context;
        this.item=item;
    }
    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        return null;
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        LayoutInflater inflater=this.context.getLayoutInflater();
        View titel=inflater.inflate(R.layout.title_layout,null);
        ImageView imgTitle=titel.findViewById(R.id.imgTitle);
        TextView txtTenQuanTitel=titel.findViewById(R.id.txtTenQuanTitle);
        TextView txtDiaChiTitel=titel.findViewById(R.id.txtDiaChiTitle);
        txtTenQuanTitel.setText(item.getTenQuan());
        txtDiaChiTitel.setText(item.getDiaChi());
        Picasso.with(context).load(item.getImageSourceID()).into(imgTitle);
        return titel;
    }
}
