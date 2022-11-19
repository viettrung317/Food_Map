package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.Itemmodel.Item;
import com.example.myapp.itemadapter.ItemAdapter;
import com.example.myapp.menuadapter.Menuadapter;
import com.example.myapp.menumodel.Menu;
import com.example.myapp.usermodel.UserSignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class OderMainActivity extends AppCompatActivity {
    private TextView txtTongTien;
    private Button btnThanhToan;
    private ListView lvOder;
    private List<Menu> listOder,listBuy;
    private Menuadapter oderAdapter;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference ref=database.getReference("User");
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oder_main);
        addControls();
        getUser();
        getListBuy(getUser());
        getData();
        addEvent();
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
    private void addEvent() {
        lvOder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Menu menu = listOder.get(position);
                new AlertDialog.Builder(OderMainActivity.this)
                        .setTitle(R.string.app_name)
                        .setMessage("Bạn muốn ?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //oderAdapter.remove(oderAdapter.getItem(position));
                                removedata(menu);
                                oderAdapter.remove(oderAdapter.getItem(position));
                                Toast.makeText(OderMainActivity.this, "Đã xóa "+menu.getTenMonAn()+" khỏi danh sách !", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Sửa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String user=getUser();
                                Intent intent=new Intent(OderMainActivity.this,SoLuongMainActivity.class);
                                intent.putExtra("Menu",menu);
                                intent.putExtra("User",user);
                                startActivity(intent);
                                finish();
                                oderAdapter.notifyDataSetChanged();

                            }
                        })
                        .show();
            }
        });


        }



    private void removedata(Menu menu) {
        String user = getUser();
        ref.child(user).child("listoder").child(menu.getTenMonAn()).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
            }

        });
        listOder=new ArrayList<>();
        getData();
    }

    private void getData() {
        String userName=getUser();
        ref.child(userName).child("listoder").addValueEventListener(new ValueEventListener() {
            int bill=0;
            @Override
            public void onDataChange(@NonNull DataSnapshot data) {
                for(DataSnapshot dataSnapshot:data.getChildren()){
                    Menu menu=dataSnapshot.getValue(Menu.class);
                    listOder.add(menu);
                    int i=menu.getGia().indexOf(".");
                    int j=menu.getGia().indexOf("đ");
                    String gia=menu.getGia().substring(0,i) + menu.getGia().substring(i+1,j);
                    bill+=(Integer.parseInt(gia)*menu.getSoLuong());

                }
                String tong=String.valueOf(bill);
                txtTongTien.setText(tong);
                btnThanhToan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        List<Menu> listTemp=new ArrayList<>();
                        for(Menu menu:listOder){
                            if(listBuy.toString().equals(listTemp.toString())){
                                ref.child(userName).child("DaMua").child(menu.getTenMonAn()).setValue(menu).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(OderMainActivity.this, "Đã mua thành công !", Toast.LENGTH_LONG).show();
                                            txtTongTien.setText("0 đ");
                                            listOder = new ArrayList<>();
                                            oderAdapter=new Menuadapter(OderMainActivity.this,R.layout.item_menu,listOder);
                                            ref.child(userName).child("listoder").setValue(listOder);
                                            lvOder.setAdapter(oderAdapter);
                                            oderAdapter.notifyDataSetChanged();

                                        } else {
                                            Toast.makeText(OderMainActivity.this, "Registered error :" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }

                                });
                            }
                            else{
                                for (Menu menu1:listBuy){
                                    if(menu.getTenMonAn().equals(menu1.getTenMonAn())){
                                        menu.setSoLuong(menu.getSoLuong()+menu1.getSoLuong());
                                        ref.child(userName).child("DaMua").child(menu.getTenMonAn()).setValue(menu).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(OderMainActivity.this, "Đã mua thành công !", Toast.LENGTH_LONG).show();
                                                    txtTongTien.setText("0 đ");
                                                    listOder = new ArrayList<>();
                                                    oderAdapter=new Menuadapter(OderMainActivity.this,R.layout.item_menu,listOder);
                                                    ref.child(userName).child("listoder").setValue(listOder);
                                                    lvOder.setAdapter(oderAdapter);
                                                    oderAdapter.notifyDataSetChanged();

                                                } else {
                                                    Toast.makeText(OderMainActivity.this, "Registered error :" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            }

                                        });
                                        break;
                                    }
                                    else{
                                        ref.child(userName).child("DaMua").child(menu.getTenMonAn()).setValue(menu).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(OderMainActivity.this, "Đã mua thành công !", Toast.LENGTH_LONG).show();
                                                    txtTongTien.setText("0 đ");
                                                    listOder = new ArrayList<>();
                                                    oderAdapter=new Menuadapter(OderMainActivity.this,R.layout.item_menu,listOder);
                                                    ref.child(userName).child("listoder").setValue(listOder);
                                                    lvOder.setAdapter(oderAdapter);
                                                    oderAdapter.notifyDataSetChanged();

                                                } else {
                                                    Toast.makeText(OderMainActivity.this, "Registered error :" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            }

                                        });
                                    }
                                }
                            }

                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        lvOder.setAdapter(oderAdapter);
        oderAdapter.notifyDataSetChanged();


    }

    private void getListBuy(String userName) {
        ref.child(getUser()).child("DaMua").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data:snapshot.getChildren()){
                    Menu menu1=data.getValue(Menu.class);
                    listBuy.add(menu1);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }


    private void addControls() {
        txtTongTien=(TextView) findViewById(R.id.txtTongTien);
        btnThanhToan=(Button) findViewById(R.id.btnThanhToan);
        lvOder=(ListView) findViewById(R.id.lvOder);
        listOder=new ArrayList<>();
        listBuy=new ArrayList<>();
        oderAdapter=new Menuadapter(OderMainActivity.this,R.layout.item_menu,listOder);


    }
}