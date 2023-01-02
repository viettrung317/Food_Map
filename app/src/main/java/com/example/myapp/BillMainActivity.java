package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapp.itemadapter.BillMenuAdapter;
import com.example.myapp.menumodel.Bill;
import com.example.myapp.menumodel.Menu;
import java.util.List;

public class BillMainActivity extends AppCompatActivity {
    private TextView txtMaDonBill,txtTenBill,txtSdtBill,txtAddressBill,txtTongBillMenu,txtTimeBillMenu;
    private ListView lvMenuBill;
    private BillMenuAdapter billMenuAdapter;
    private Bill bill;
    private List<Menu> menuList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_main);
        addControls();
        getData();
    }

    private void getData() {
        menuList=bill.getMenuList();
        billMenuAdapter=new BillMenuAdapter(this,R.layout.itembillmenu,menuList);
        lvMenuBill.setAdapter(billMenuAdapter);
        billMenuAdapter.notifyDataSetChanged();
    }

    private void addControls() {
        txtMaDonBill=(TextView) findViewById(R.id.txtMaDonBill);
        txtTenBill=(TextView) findViewById(R.id.txtTenBill);
        txtSdtBill=(TextView) findViewById(R.id.txtSdtBill);
        txtAddressBill=(TextView) findViewById(R.id.txtAddressBill);
        txtTongBillMenu=(TextView) findViewById(R.id.txtTongBillMenu);
        txtTimeBillMenu=(TextView) findViewById(R.id.txtTimeBillMenu);
        lvMenuBill=(ListView) findViewById(R.id.lvMenuBill);
        Intent intent=getIntent();
        bill= (Bill) intent.getSerializableExtra("bill");
        txtMaDonBill.setText("Mã đơn hàng :"+bill.getMaDonHang());
        txtTenBill.setText(bill.getTenKhacHang());
        txtSdtBill.setText(bill.getSodt());
        txtAddressBill.setText(bill.getDiaChi());
        txtTimeBillMenu.setText(bill.getTime());
        txtTongBillMenu.setText(bill.getTongtien());
    }
}