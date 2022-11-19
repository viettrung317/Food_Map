package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapp.menumodel.Menu;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SoLuongMainActivity extends AppCompatActivity {
    private EditText txtAddSoLuong;
    private Button btnAddSoLuong;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference ref = db.getReference("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_so_luong_main);
        txtAddSoLuong = (EditText) findViewById(R.id.txtAddSoLuong);
        btnAddSoLuong = (Button) findViewById(R.id.btnAddSoLuong);
        Intent intent = getIntent();
        Menu menu = (Menu) intent.getSerializableExtra("Menu");
        String user = intent.getStringExtra("User");

        btnAddSoLuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String j = txtAddSoLuong.getText().toString();
                if (TextUtils.isEmpty(j)) {
                    txtAddSoLuong.setError("Yêu cầu nhập số lượng");
                    txtAddSoLuong.requestFocus();
                } else {
                    int i = Integer.parseInt(j);
                    menu.setSoLuong(i);
                    ref.child(user).child("listoder").child(menu.getTenMonAn()).updateChildren(menu.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            startActivity(new Intent(SoLuongMainActivity.this, OderMainActivity.class));
                            finish();
                        }
                    });

                }
            }
        });
    }
}
