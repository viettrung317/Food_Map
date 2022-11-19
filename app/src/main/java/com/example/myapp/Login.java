package com.example.myapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.myapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


public class Login extends AppCompatActivity {
    EditText txtEmail,txtPassword;
    Button btnLogin;
    TextView txtSignUp;
    FirebaseAuth mAuth;
    ProgressBar progress;
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addControl();
        addEvents();
    }

    //thoát ứng dụng bằng nút trở lại
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);

        // Tao su kien ket thuc app
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startActivity(startMain);
        finish();
    }
    private void addEvents() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();
            }
        });
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,SignUp.class));
            }
        });

    }
    private void LoginUser() {
        i++;
        if(i==1){
            String password,email;
            password=txtPassword.getText().toString();
            email=txtEmail.getText().toString();

            if(TextUtils.isEmpty(email)){
                txtEmail.setError("Email can't empty");
                txtEmail.requestFocus();
            }
            else if(TextUtils.isEmpty(password)){
                txtPassword.setError("Password can't empty");
                txtPassword.requestFocus();
            }
            else{
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progress.setVisibility(View.VISIBLE);
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this,"User Logged in successfully !",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(Login.this,MainActivity.class));
                        }
                        else{
                            Toast.makeText(Login.this,"Login error :"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
    }


    private void addControl() {
        txtEmail=findViewById(R.id.txtEmail);
        txtPassword=findViewById(R.id.txtPassword);
        btnLogin=findViewById(R.id.btnLogin);
        txtSignUp=findViewById(R.id.txtSignUp);
        progress=findViewById(R.id.progress);
        mAuth=FirebaseAuth.getInstance();
    }
}