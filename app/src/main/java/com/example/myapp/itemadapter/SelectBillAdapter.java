package com.example.myapp.itemadapter;

import android.content.Context;
import android.icu.util.ULocale;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapp.Itemmodel.SelectBill;
import com.example.myapp.R;

import java.util.List;


public class SelectBillAdapter extends ArrayAdapter<SelectBill> {
    public SelectBillAdapter(@NonNull Context context, int resource, @NonNull List<SelectBill> objects) {
        super(context, resource, objects);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected,parent,false);
        TextView txtselected=(TextView) convertView.findViewById(R.id.txtselected);
        ImageView imgdropdown=(ImageView)convertView.findViewById(R.id.imgdropdown);
        SelectBill selectBill=this.getItem(position);
        if(selectBill!=null){
            txtselected.setText(selectBill.getSelect());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_bill_layout,parent,false);
        TextView txt_select=(TextView) convertView.findViewById(R.id.txt_select);
        SelectBill selectBill=this.getItem(position);
        if(selectBill!=null){
            txt_select.setText(selectBill.getSelect());
        }
        return convertView;
    }
}

