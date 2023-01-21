package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapp.Itemmodel.Item;
import com.example.myapp.menuadapter.Menuadapter;
import com.example.myapp.menumodel.Menu;
import com.example.myapp.usermodel.UserSignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MenuMainActivity extends AppCompatActivity {
    private TextView txtMenuName,txtAdressMenu;
    private ImageView imgAvtMenu;
    private Button btnLocTheoGia;
    private ImageButton ibtnLocation;
    private List<Menu> menuList,listOrder;
    private List<UserSignUp> listUser;
    private ListView lvMenu;
    private Menuadapter menuadapter;
    private Double Latitude,Longitude;
    private String name;
    private Item item;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference ref=database.getReference("User");
    DatabaseReference ref1=database.getReference("User");
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_main);
        addControls();
        addEvents();

    }

    private void swap(int i,int j){
        Menu m1=menuList.get(i);
        menuList.set(i,menuList.get(j));
        menuList.set(j,m1);
    }

    private void sxtd(List<Menu> lmenu) {
        for(int i=0;i<lmenu.size();i++){
            for(int j=i+1;j<lmenu.size();j++){
                if(getgia(lmenu.get(j))<getgia(lmenu.get(i))){
                    swap(j,i);
                }
            }

        }

    }

    private int getgia(Menu menu) {
        int i=menu.getGia().indexOf(".");
        int j=menu.getGia().indexOf("đ");
        String gia=menu.getGia().substring(0,i) + menu.getGia().substring(i+1,j);
        return Integer.parseInt(gia);
    }

    private void addEvents() {
        btnLocTheoGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sxtd(menuList);
                menuadapter.notifyDataSetChanged();
            }
        });
        ibtnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MenuMainActivity.this,MainActivity.class);
                intent.putExtra("Item",item);
                intent.putExtra("Lat",Latitude);
                intent.putExtra("Long",Longitude);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });
        lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    // Name, email address, and profile photo Url
                    String name = user.getDisplayName();
                    String email = user.getEmail();
                    int index=email.indexOf("@");
                    String userName=email.substring(0,index);
                    Menu menu=menuList.get(i);
                    ref.child(userName).child("listoder").child(menu.getTenMonAn()).setValue(menu).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MenuMainActivity.this, "Đã thêm vào giỏ hàng !", Toast.LENGTH_LONG).show();


                            } else {
                                Toast.makeText(MenuMainActivity.this, "Registered error :" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                    });

                }
            }
        });

    }

    private void addControls() {
        txtMenuName=(TextView) findViewById(R.id.txtMenuName);
        txtAdressMenu=(TextView) findViewById(R.id.txtAdressMenu);
        imgAvtMenu=(ImageView) findViewById(R.id.imgAvtMenu);
        ibtnLocation=(ImageButton) findViewById(R.id.ibtnLocation);
        btnLocTheoGia=(Button) findViewById(R.id.btnLocTheoGia);
        lvMenu=(ListView) findViewById(R.id.lvMenu);
        menuList=new ArrayList<>();
        Intent intent=getIntent();
        item=(Item) intent.getSerializableExtra("Item");
        txtMenuName.setText(item.getTenQuan());
        txtAdressMenu.setText(item.getDiaChi());
        Picasso.with(MenuMainActivity.this).load(item.getImageSourceID()).into(imgAvtMenu);
        menuList=item.getMenuList();
        menuadapter=new Menuadapter(MenuMainActivity.this,R.layout.item_menu,menuList);
        lvMenu.setAdapter(menuadapter);
        menuadapter.notifyDataSetChanged();
        Latitude=item.getLatitude();
        Longitude= item.getLongitude();
        name=item.getTenQuan();
    }
}