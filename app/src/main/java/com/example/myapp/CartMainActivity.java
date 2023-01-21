package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapp.Itemmodel.SelectBill;
import com.example.myapp.itemadapter.BillAdapter;
import com.example.myapp.itemadapter.SelectBillAdapter;
import com.example.myapp.menumodel.Bill;
import com.example.myapp.menumodel.Menu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class CartMainActivity extends AppCompatActivity {
    private Spinner spn_selected;
    private SelectBillAdapter selectBillAdapter;
    private ListView lvBill;
    private BillAdapter billAdapter;
    private List<Bill> listBill=new ArrayList<>();
    //
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseDatabase db= FirebaseDatabase.getInstance();
    private DatabaseReference ref=db.getReference("User");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_main);
        spn_selected=(Spinner) findViewById(R.id.spn_selected);
        lvBill=(ListView) findViewById(R.id.lvBill);
        getlistBill();
        getLv(listBill);
        Select_Bill();
    }
    private int toInt(Bill bill){
        int j=bill.getTongtien().indexOf(" ");
        String gia=bill.getTongtien().substring(0,j);
        int i= Integer.parseInt(gia);
        return i;
    }
    private long getTime(Bill bill){
        long time= Long.parseLong(bill.getMaDonHang());
        return time;
    }
    private void swap(int i,int j){
        Bill b1=listBill.get(i);
        listBill.set(i,listBill.get(j));
        listBill.set(j,b1);
    }
    ///Sắp xếp theo thời gian;
    private int partitiontime(List<Bill> list, int low, int high)
    {
        Bill pivot = list.get(high); // pivot
        int i = (low - 1); // Index of smaller element and indicates
        // the right position of pivot found so far

        for (int j = low; j <= high - 1; j++) {
            // If current element is smaller than the pivot
            if (getTime(list.get(j)) > getTime(pivot)) {
                i++; // increment index of smaller element
                swap(i, j);
            }
        }
        swap(i+1, high);
        return (i + 1);
    }

    /* Hàm thực hiện giải thuật quick sort */
    private void quickSorttime(List<Bill> list, int low, int high)
    {
        if (low < high)
        {
        /* pi là chỉ số nơi phần tử này đã đứng đúng vị trí
         và là phần tử chia mảng làm 2 mảng con trái & phải */
            int pi = partitiontime(list, low, high);

            // Gọi đệ quy sắp xếp 2 mảng con trái và phải
            quickSorttime(list, low, pi - 1);
            quickSorttime(list, pi + 1, high);
        }
    }
    ////
    private int partition(List<Bill> list, int low, int high)
    {
        Bill pivot = list.get(high); // pivot
        int i = (low - 1); // Index of smaller element and indicates
        // the right position of pivot found so far

        for (int j = low; j <= high - 1; j++) {
            // If current element is smaller than the pivot
            if (toInt(list.get(j)) < toInt(pivot)) {
                i++; // increment index of smaller element
                swap(i, j);
            }
        }
        swap(i+1, high);
        return (i + 1);
    }

    /* Hàm thực hiện giải thuật quick sort */
    private void quickSort(List<Bill> list, int low, int high)
    {
        if (low < high)
        {
        /* pi là chỉ số nơi phần tử này đã đứng đúng vị trí
         và là phần tử chia mảng làm 2 mảng con trái & phải */
            int pi = partition(list, low, high);

            // Gọi đệ quy sắp xếp 2 mảng con trái và phải
            quickSort(list, low, pi - 1);
            quickSort(list, pi + 1, high);
        }
    }
    private int gt50(List<Bill> list,int x, int l, int r){
        if (r >= l) {
            int mid = l + (r - l) / 2;

            // If the element is present at the middle
            // itself
            if (toInt(list.get(mid)) == x)
                return mid;

            // If element is smaller than mid, then
            // it can only be present in left subarray
            if (toInt(list.get(mid)) > x)
                return gt50(list,x, l, mid - 1);

            // Else the element can only be present
            // in right subarray
            return gt50(list,x, mid + 1, r);
        }

        // We reach here when element is not
        // present in array
        return -1;

    }


    private void getLv(List<Bill> list) {
        billAdapter=new BillAdapter(this,R.layout.item_bill,list);
        lvBill.setAdapter(billAdapter);
        billAdapter.notifyDataSetChanged();
        lvBill.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bill bill=billAdapter.getItem(i);
                Intent intent=new Intent(CartMainActivity.this,BillMainActivity.class);
                intent.putExtra("bill",bill);
                startActivity(intent);
            }
        });
    }

    private void Select_Bill() {
        selectBillAdapter=new SelectBillAdapter(this,R.layout.item_selected,getlist());
        spn_selected.setAdapter(selectBillAdapter);
        spn_selected.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(selectBillAdapter.getItem(i).getSelect().equals("Sắp xếp theo giá")){
                    quickSort(listBill,0,listBill.size()-1);
                    getLv(listBill);
                    //billAdapter.notifyDataSetChanged();
                }
                else if(selectBillAdapter.getItem(i).getSelect().equals("Sắp xếp theo thời gian")){
                    quickSorttime(listBill,0,listBill.size()-1);
                    getLv(listBill);
                }
                else if(selectBillAdapter.getItem(i).getSelect().equals("Đơn trên 500.000 đ")){
                    Bill bill=new Bill();
                    bill.setTongtien("500000 đ");
                    listBill.add(bill);
                    quickSort(listBill,0,listBill.size()-1);
                    int mid=gt50(listBill,500000,0,listBill.size()-1);
                    listBill.remove(mid);
                    List<Bill> list=listBill.subList(mid,listBill.size());
                    getLv(list);
                }
                else if(selectBillAdapter.getItem(i).getSelect().equals("Đơn dưới 500.000 đ")){
                    Bill bill=new Bill();
                    bill.setTongtien("500000 đ");
                    listBill.add(bill);
                    quickSort(listBill,0,listBill.size()-1);
                    int mid=gt50(listBill,500000,0,listBill.size()-1);
                    listBill.remove(mid);
                    List<Bill> list=listBill.subList(0,mid);
                    getLv(list);
                }
                else if(selectBillAdapter.getItem(i).getSelect().equals("Đơn đã thanh toán")){
                    List<Bill> list=new ArrayList<>();
                    for (Bill bill:listBill){
                        if(bill.getTrangthaiThanhToan()==true){
                            list.add(bill);
                        }
                    }
                    quickSort(list,0,list.size()-1);
                    getLv(list);
                }
                else if(selectBillAdapter.getItem(i).getSelect().equals("Đơn chưa thanh toán")){
                    List<Bill> list=new ArrayList<>();
                    for (Bill bill:listBill){
                        if(bill.getTrangthaiThanhToan()==false){
                            list.add(bill);
                        }
                    }
                    quickSort(list,0,list.size()-1);
                    getLv(list);
                }
                else if(selectBillAdapter.getItem(i).getSelect().equals("Đơn đã nhận")){
                    List<Bill> list=new ArrayList<>();
                    for (Bill bill:listBill){
                        if(bill.getTrangthaidonhang()==true){
                            list.add(bill);
                        }
                    }
                    quickSort(list,0,list.size()-1);
                    getLv(list);
                }
                else if(selectBillAdapter.getItem(i).getSelect().equals("Đơn chưa nhận")){
                    List<Bill> list=new ArrayList<>();
                    for (Bill bill:listBill){
                        if(bill.getTrangthaidonhang()==false){
                            list.add(bill);
                        }
                    }
                    quickSort(list,0,list.size()-1);
                    getLv(list);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private String getUser() {
        String userName="";
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            int index = email.indexOf("@");
            userName= email.substring(0, index);
        }
        return userName;
    }

    private void getlistBill() {
        String user=getUser();
        ref.child(user).child("listBill").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dt:snapshot.getChildren()){
                    Bill bill=dt.getValue(Bill.class);
                    listBill.add(bill);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private List<SelectBill> getlist() {
        List<SelectBill> list=new ArrayList<>();
        list.add(new SelectBill("Sắp xếp theo thời gian"));
        list.add(new SelectBill("Sắp xếp theo giá"));
        list.add(new SelectBill("Đơn trên 500.000 đ"));
        list.add(new SelectBill("Đơn dưới 500.000 đ"));
        list.add(new SelectBill("Đơn đã thanh toán"));
        list.add(new SelectBill("Đơn chưa thanh toán"));
        list.add(new SelectBill("Đơn đã nhận"));
        list.add(new SelectBill("Đơn chưa nhận"));
        return list;
    }
}