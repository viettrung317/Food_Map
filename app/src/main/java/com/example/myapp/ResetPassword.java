package com.example.myapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPassword extends AppCompatActivity {
    private Button btnSaveResetpassWord;
    private EditText txtResetPassWord,txtCheckResetPassWord;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnSaveResetpassWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String newPass;
                newPass=txtResetPassWord.getText().toString();
                boolean reset=traVeHopLe();
                if(reset==true){
                    user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ResetPassword.this, "User password updated", Toast.LENGTH_SHORT).show();
                                        mAuth.signOut();
                                        startActivity(new Intent(ResetPassword.this,Login.class));

                                    }
                                }
                            });
                }


            }
        });
        txtResetPassWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String newPass;
                newPass=txtResetPassWord.getText().toString();
                if(newPass.length()<6){
                    txtResetPassWord.setError("Độ dài tối thiểu là 6 ký tự");

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        txtCheckResetPassWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String newPass,checkNewPass;
                newPass=txtResetPassWord.getText().toString();
                checkNewPass=txtCheckResetPassWord.getText().toString();
                if(checkNewPass.equals(newPass)==true && checkNewPass.length()>=6){
                    traVeHopLe();
                }else{
                    txtCheckResetPassWord.setError("Mật khẩu không trùng khớp");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private boolean traVeHopLe() {
        return true;
    }

    private void addControls() {
        btnSaveResetpassWord=(Button) findViewById(R.id.btnSaveResetPassWord);
        txtResetPassWord=(EditText) findViewById(R.id.txtResetPassword);
        txtCheckResetPassWord=(EditText) findViewById(R.id.txtCheckResetPassword);
        mAuth=FirebaseAuth.getInstance();
    }
}