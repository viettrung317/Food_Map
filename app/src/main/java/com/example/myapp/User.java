package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.usermodel.UserSignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class User extends AppCompatActivity {
    FirebaseAuth mAuth;
    private Button btnLogout,btnDelete,btnResetPassWord;
    private ImageButton ibtnEditAvatar,ibtnEditGioiTinh,ibtnEditNgaySinh,ibtnEditSodt;
    private ImageView imgAvatarUser;
    FirebaseDatabase db=FirebaseDatabase.getInstance();
    DatabaseReference ref=db.getReference("User");
    private TextView txtAdressEmail,txtUserName,txtGioiTinh,txtNgaySinh,txtSdt;
    private UserSignUp userSignUp;
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
        ibtnEditSodt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(Gravity.CENTER);
            }

        });
        ibtnEditNgaySinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chonNgay();
            }
        });
        ibtnEditGioiTinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chonGT(Gravity.CENTER);
            }
        });
        ibtnEditAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(User.this,EditAvatarMainActivity.class));
            }
        });
    }




    private void chonGT(int gravity) {
        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit_gt);
        Window window= dialog.getWindow();
        if(window==null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes=window.getAttributes();
        windowAttributes.gravity=gravity;
        window.setAttributes(windowAttributes);

        if(Gravity.BOTTOM==gravity){
            dialog.setCancelable(true);
        }
        else{
            dialog.setCancelable(false);
        }
        RadioGroup radioGt=dialog.findViewById(R.id.radioGt);
        RadioButton radioNam=dialog.findViewById(R.id.radioNam);
        RadioButton radioNu=dialog.findViewById(R.id.radioNu);
        Button btnCancelgt=dialog.findViewById(R.id.btnCancelgt);
        Button btnUpdategt=dialog.findViewById(R.id.btnUpdategt);
        btnCancelgt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnUpdategt.setOnClickListener(new View.OnClickListener() {
            String gt="";
            @Override
            public void onClick(View view) {
                if(radioNam.isChecked()){
                    gt="Nam";
                }
                else if(radioNu.isChecked()){
                    gt="Nữ";
                }
                else{
                    Toast.makeText(User.this, "Bạn chưa chọn giới tính!", Toast.LENGTH_SHORT).show();
                }
                txtGioiTinh.setText(gt);
                ref.child(getUserName()).child("gioiTinh").setValue(gt).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
    }

    private void chonNgay(){
        Calendar calendar=Calendar.getInstance();
        int ngay=calendar.get(Calendar.DATE);
        int thang=calendar.get(Calendar.MONTH);
        int nam=calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(i,i1,i2);
                String ngay=String.valueOf(i2);
                if(i2<10){
                    ngay="0"+ngay;
                }
                String thang=String.valueOf(i1+1);
                if(i1<10){
                    thang="0"+thang;
                }
                String nam=String.valueOf(i);
                String nSinh=ngay+"/"+thang+"/"+nam;
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
                ref.child(getUserName()).child("ngaySinh").setValue(nSinh).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        txtNgaySinh.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                });
            }
        },nam,thang,ngay);
        datePickerDialog.show();
    }

    private void openDialog(int gravity) {
        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit_sdt);
        Window window= dialog.getWindow();
        if(window==null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes=window.getAttributes();
        windowAttributes.gravity=gravity;
        window.setAttributes(windowAttributes);

        if(Gravity.BOTTOM==gravity){
            dialog.setCancelable(true);
        }
        else{
            dialog.setCancelable(false);
        }
        EditText txtEditSdt=dialog.findViewById(R.id.txtEditSdt);
        Button btnCancelSdt=dialog.findViewById(R.id.btnCancelAvatar);
        Button btnUpdateSdt=dialog.findViewById(R.id.btnUpdateAvatar);
        btnCancelSdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnUpdateSdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sdt=txtEditSdt.getText().toString();
                if(TextUtils.isEmpty(sdt)){
                    txtEditSdt.setError("Vui lòng nhập số điện thoại !");
                    txtEditSdt.requestFocus();
                }
                else{
                    txtSdt.setText(sdt);
                    ref.child(getUserName()).child("soDT").setValue(sdt).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            dialog.dismiss();
                        }
                    });

                }
            }
        });
        dialog.show();
    }

    private void addControls() {
        btnLogout=(Button) findViewById(R.id.btnLogout);
        btnDelete=(Button)findViewById(R.id.btnDelete) ;
        btnResetPassWord=(Button)findViewById(R.id.btnResetPassWord);
        txtAdressEmail=(TextView)findViewById(R.id.txtAdressEmail) ;
        txtUserName=(TextView)findViewById(R.id.txtUserName);
        txtGioiTinh=(TextView)findViewById(R.id.txtGioiTinh);
        txtNgaySinh=(TextView)findViewById(R.id.txtNgaySinh);
        txtSdt=(TextView)findViewById(R.id.txtSdt);
        ibtnEditAvatar=(ImageButton) findViewById(R.id.ibtnEditAvatar);
        ibtnEditGioiTinh=(ImageButton) findViewById(R.id.ibtnEditGT);
        ibtnEditNgaySinh=(ImageButton) findViewById(R.id.ibtnEditNgaySinh);
        ibtnEditSodt=(ImageButton) findViewById(R.id.ibtnEditSdt);
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
        ref.child(userName).child("soDT").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String sdt=snapshot.getValue(String.class);
                if(TextUtils.isEmpty(sdt)){
                    txtSdt.setText("-Chọn-");
                }else{
                    txtSdt.setText(sdt);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ref.child(userName).child("gioiTinh").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String gt=snapshot.getValue(String.class);
                if(TextUtils.isEmpty(gt)){
                    txtGioiTinh.setText("-Chọn-");
                }else{
                    txtGioiTinh.setText(gt);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ref.child(userName).child("ngaySinh").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String ngaysinh=snapshot.getValue(String.class);
                if(TextUtils.isEmpty(ngaysinh)){
                    txtNgaySinh.setText("-Chọn-");
                }else{
                    txtNgaySinh.setText(ngaysinh);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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