package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.itemadapter.BillMenuAdapter;
import com.example.myapp.menumodel.Bill;
import com.example.myapp.menumodel.Menu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class BillMainActivity extends AppCompatActivity {
    private TextView txtMaDonBill,txtTenBill,txtSdtBill,txtAddressBill,txtTongBillMenu,txtTimeBillMenu;
    private Button btnXacnhan;
    private ListView lvMenuBill;
    private ProgressBar progressBar;
    private BillMenuAdapter billMenuAdapter;
    private Bill bill;
    private List<Menu> menuList;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseDatabase db;
    private DatabaseReference ref;
    private String code,sdt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_main);
        addControls();
        getData();
        addEvents();

    }
    private String getUser(){
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
    //Nhập số shipper
    private void openDialogInput(int gravity) {
        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sdt_shipper);
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
        EditText txtEditSdt=dialog.findViewById(R.id.txtSdtsp);
        Button btnCancelSdt=dialog.findViewById(R.id.btnCancelSdt);
        Button btnUpdateSdt=dialog.findViewById(R.id.btnAccept);
        btnCancelSdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnUpdateSdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sdt="+84"+txtEditSdt.getText().toString();
                dialog.dismiss();
                getOTP(sdt);
            }
        });
        dialog.show();
    }
    ///
    private void getOTP(String sdt) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(sdt)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                code = phoneAuthCredential.getSmsCode();
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(BillMainActivity.this,"onVerificationFailed",Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verificationId, forceResendingToken);
                                openDialog(Gravity.CENTER,verificationId);


                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }



    private void openDialog(int gravity,String verificationId) {
        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_otp);
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
        EditText txtMaOTP=dialog.findViewById(R.id.txtMaOTP);
        TextView txtSendAgainOTP=dialog.findViewById(R.id.txtSendAgainOTP);
        Button btnCancelOTP=dialog.findViewById(R.id.btnCancelOTP);
        Button btnLoginOTPdialog=dialog.findViewById(R.id.btnLoginOTPdialog);
        txtSendAgainOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOTP(sdt);
            }
        });
        btnCancelOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnLoginOTPdialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp=txtMaOTP.getText().toString().trim();
                if(TextUtils.isEmpty(sdt)){
                    txtMaOTP.setError("Vui lòng nhập mã OTP !");
                    txtMaOTP.requestFocus();
                }
                else{
                    if(otp.equals(code)){
                        String user=getUser();
                        bill.setTrangthaidonhang(true);
                        bill.setTrangthaiThanhToan(true);
                        ref.child("User").child(user).child("listBill").child(bill.getMaDonHang()).updateChildren(bill.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                dialog.dismiss();
                                progressBar.setVisibility(View.GONE);
                                btnXacnhan.setVisibility(View.GONE);
                                startActivity(new Intent(BillMainActivity.this,CartMainActivity.class));
                                finish();
                            }
                        });
                    }
                    else{
                        Toast.makeText(BillMainActivity.this,"Mã otp sai !!!",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        dialog.show();
    }

    private void addEvents() {
        btnXacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                openDialogInput(Gravity.CENTER);
            }
        });
    }

    private void getData() {
        menuList=bill.getMenuList();
        billMenuAdapter=new BillMenuAdapter(this,R.layout.itembillmenu,menuList);
        lvMenuBill.setAdapter(billMenuAdapter);
        billMenuAdapter.notifyDataSetChanged();
    }

    private void addControls() {
        txtMaDonBill=(TextView) findViewById(R.id.txtMaDonBill);
        txtTenBill=(TextView) findViewById(R.id.txtTenBill);
        txtSdtBill=(TextView) findViewById(R.id.txtSdtBill);
        txtAddressBill=(TextView) findViewById(R.id.txtAddressBill);
        txtTongBillMenu=(TextView) findViewById(R.id.txtTongBillMenu);
        txtTimeBillMenu=(TextView) findViewById(R.id.txtTimeBillMenu);
        lvMenuBill=(ListView) findViewById(R.id.lvMenuBill);
        btnXacnhan=(Button) findViewById(R.id.btnXacnhan);
        progressBar=(ProgressBar) findViewById(R.id.progressBar);
        Intent intent=getIntent();
        bill= (Bill) intent.getSerializableExtra("bill");
        txtMaDonBill.setText("Mã đơn hàng :"+bill.getMaDonHang());
        txtTenBill.setText(bill.getTenKhacHang());
        txtSdtBill.setText(bill.getSodt());
        txtAddressBill.setText(bill.getDiaChi());
        txtTimeBillMenu.setText(bill.getTime());
        txtTongBillMenu.setText(bill.getTongtien());
        if(bill.getTrangthaidonhang()==false){
            btnXacnhan.setVisibility(View.VISIBLE);
        }
        db=FirebaseDatabase.getInstance();
        ref=db.getReference();
    }
}