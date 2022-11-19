package com.example.myapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.myapp.Itemmodel.Item;
import com.example.myapp.menuadapter.Menuadapter;
import com.example.myapp.menumodel.Menu;
import com.example.myapp.usermodel.UserSignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MenuMainActivity extends AppCompatActivity {
    private TextView txtMenuName,txtAdressMenu;
    private ImageView imgAvtMenu;
    private List<Menu> menuList,listOrder;
    private List<UserSignUp> listUser;
    private ListView lvMenu;
    private Menuadapter menuadapter;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference ref=database.getReference("User");
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_main);
        addControls();
        addEvents();

    }

    private void addEvents() {
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
        lvMenu=(ListView) findViewById(R.id.lvMenu);
        menuList=new ArrayList<>();
        Intent intent=getIntent();
        Item item=(Item) intent.getSerializableExtra("Item");
        txtMenuName.setText(item.getTenQuan());
        txtAdressMenu.setText(item.getDiaChi());
        Picasso.with(MenuMainActivity.this).load(item.getImageSourceID()).into(imgAvtMenu);
        menuList=item.getMenuList();
        menuadapter=new Menuadapter(MenuMainActivity.this,R.layout.item_menu,menuList);
        lvMenu.setAdapter(menuadapter);
        menuadapter.notifyDataSetChanged();


    }
}