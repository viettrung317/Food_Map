package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.menuadapter.Menuadapter;
import com.example.myapp.menumodel.Menu;
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

public class HistoryMainActivity extends AppCompatActivity {
    private ListView lvBuy;
    private Menuadapter menuadapter;
    private List<Menu> listBuy;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference ref=database.getReference("User");
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_main);
        addControls();
        getData();
    }
    private void getData() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            int index=email.indexOf("@");
            String userName=email.substring(0,index);
            ref.child(userName).child("DaMua").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot data) {
                    for (DataSnapshot dataSnapshot : data.getChildren()) {
                        Menu menu = dataSnapshot.getValue(Menu.class);
                        listBuy.add(menu);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            lvBuy.setAdapter(menuadapter);
            menuadapter.notifyDataSetChanged();
        }
    }


    private void addControls() {
        lvBuy=(ListView) findViewById(R.id.lvBuy);
        listBuy=new ArrayList<>();
        menuadapter=new Menuadapter(HistoryMainActivity.this,R.layout.item_menu,listBuy);
    }
}