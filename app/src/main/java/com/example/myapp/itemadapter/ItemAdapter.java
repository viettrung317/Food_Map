package com.example.myapp.itemadapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapp.HomeMainActivity;
import com.example.myapp.Itemmodel.Item;

import com.example.myapp.R;
import com.example.myapp.convert.DataConvert;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item> {
    private TextView txtTenQuan,txtTenMon,txtDiaChi;
    private ImageView imgAvatar;
    Activity context; int resource;List<Item> objects;
    public ItemAdapter(@NonNull Activity context, int resource, @NonNull List<Item> objects) {
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
        txtTenQuan=(TextView) row.findViewById(R.id.txtTenQuan);
        txtTenMon=(TextView) row.findViewById(R.id.txtTenMon);
        txtDiaChi=(TextView) row.findViewById(R.id.txtDiaChi);
        imgAvatar=(ImageView) row.findViewById(R.id.imgAvatar);

        final Item item =this.objects.get(position);
        txtTenQuan.setText(item.getTenQuan());
        txtTenMon.setText(item.getTenMon());
        txtDiaChi.setText(item.getDiaChi());
        Picasso.with(context).load(item.getImageSourceID()).into(imgAvatar);
        return  row;
    }
}
