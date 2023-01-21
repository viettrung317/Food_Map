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
import com.example.myapp.menumodel.Bill;

import java.util.List;

public class BillAdapter extends ArrayAdapter<Bill> {
    private Activity context;
    private int resource;
    private List<Bill> objects;
    public BillAdapter(Activity context, int resource,List<Bill> objects) {
        super(context,resource,objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater=this.context.getLayoutInflater();
        View row=layoutInflater.inflate(this.resource,null);
        TextView txtBillID=row.findViewById(R.id.txtBillID);
        TextView txtTongBill=row.findViewById(R.id.txtTongBill);
        TextView txtTttBill=row.findViewById(R.id.txtTttBill);
        TextView txtTimeBill=row.findViewById(R.id.txtTimeBill);
        TextView txtTtdh=row.findViewById(R.id.txtTtdh);
        final Bill bill=this.objects.get(position);
        txtBillID.setText(bill.getMaDonHang());
        txtTongBill.setText(bill.getTongtien());
        if(bill.getTrangthaiThanhToan()==true){
            txtTttBill.setText("Đã thanh toán");
        }
        else txtTttBill.setText("Chưa thanh toán");
        txtTimeBill.setText(bill.getTime());
        if(bill.getTrangthaidonhang()==true){
            txtTtdh.setText("Đã nhận hàng");
        }
        else{
            txtTtdh.setText("Chờ nhận hàng");
        }
        return row;
    }
}
