package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;

public class User extends AppCompatActivity {
    FirebaseAuth mAuth;
    private Button btnLogout,btnDelete,btnResetPassWord;
    private ImageView imgAvatarUser;
    FirebaseDatabase db=FirebaseDatabase.getInstance();
    DatabaseReference ref=db.getReference("User");
    private TextView txtAdressEmail,txtUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        addControls();
        addEvents();
    }
    private String getUserName(){
        String userName="";
        FirebaseUser user1 = mAuth.getCurrentUser();
        if (user1 != null) {
            // Name, email address, and profile photo Url
            String name = user1.getDisplayName();
            String email = user1.getEmail();
            int index = email.indexOf("@");
            userName = email.substring(0, index);

        }
        return userName;
    }

    private void addEvents() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(User.this,Login.class));
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userName=getUserName();
                ref.child(userName).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(User.this, userName, Toast.LENGTH_SHORT).show();
                    }
                });
                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(User.this, "Đã xóa tài khoản !", Toast.LENGTH_SHORT).show();
                        }
                    }
                    });
                startActivity(new Intent(User.this,Login.class));

            }
        });
        btnResetPassWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(User.this,ResetPassword.class));
            }
        });

    }

    private void addControls() {
        btnLogout=(Button) findViewById(R.id.btnLogout);
        btnDelete=(Button)findViewById(R.id.btnDelete) ;
        btnResetPassWord=(Button)findViewById(R.id.btnResetPassWord);
        txtAdressEmail=(TextView)findViewById(R.id.txtAdressEmail) ;
        txtUserName=(TextView)findViewById(R.id.txtUserName);
        imgAvatarUser=(ImageView) findViewById(R.id.imgAvatarUser);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            txtAdressEmail.setText(email);
            int index = email.indexOf("@");
            txtUserName.setText(email.substring(0, index));
            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }
        String userName=getUserName();
        ref.child(userName).child("avatar").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String url=snapshot.getValue(String.class);
                Picasso.with(User.this).load(url).into(imgAvatarUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        ref.child(userName).child("avatar").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                UserSignUp userSignUp = snapshot.getValue(UserSignUp.class);
//                Picasso.with(User.this).load(userSignUp.getAvatar()).into(imgAvatarUser);
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }
}