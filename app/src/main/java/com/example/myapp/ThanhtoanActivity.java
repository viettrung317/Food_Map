package com.example.myapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.menumodel.Bill;
import com.example.myapp.menumodel.Menu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.internal.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PrimitiveIterator;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import vn.momo.momo_partner.AppMoMoLib;

public class ThanhtoanActivity extends AppCompatActivity {
    private EditText txtTnnThanhToan,txtSDTNH,txtDiaChiNH;
    private Button btnHoanTatThanhToan;
    private RadioButton rdoOn,rdoOff;
    private ProgressBar progressBar2;
    private Bill bill=new Bill();
    private List<Menu> menuList=new ArrayList<>();
    private String tong;
    private String sdt;
    private String code,tttc="";
    //database
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseDatabase db;
    private DatabaseReference ref;
    //noi dung thanh toan
    private String amount;
    private String fee = "0";
    int environment = 0;//developer default
    private String merchantName = "HoangNgoc";
    private String merchantCode = "MOMOC2IC20220510";
    private String merchantNameLabel = "HoangNgoc";
    private String description = "Foodmap";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanhtoan);
        //
        Intent intent=getIntent();
        tong=intent.getStringExtra("tong");
        amount=tong;
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT); // AppMoMoLib.ENVIRONMENT.PRODUCTION
        addContols();
        addEvents();
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
                                Toast.makeText(ThanhtoanActivity.this,"onVerificationFailed",Toast.LENGTH_LONG).show();
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
                        tttc="tttc";
                        if (rdoOff.isChecked()) {
                            String user=getUser();
                            ref.child("Bill").child(bill.getMaDonHang()).setValue(bill);
                            ref.child("User").child(user).child("listBill").child(bill.getMaDonHang()).setValue(bill);
                            Intent intent = new Intent(ThanhtoanActivity.this, OderMainActivity.class);
                            intent.putExtra("tttc", tttc);
                            startActivity(intent);
                            finish();
                        } else if (rdoOn.isChecked()) {
                            requestPayment(bill.getMaDonHang());
                        }
                        else{
                            Toast.makeText(ThanhtoanActivity.this, "Bạn chưa chon hình thức thanh toán !!!", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                    else{
                        Toast.makeText(ThanhtoanActivity.this,"Mã otp sai !!!",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        dialog.show();
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    //Get token through MoMo app
    private void requestPayment(String iddonHang) {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);
//        if (edAmount.getText().toString() != null && edAmount.getText().toString().trim().length() != 0)
//            amount = edAmount.getText().toString().trim();

        Map<String, Object> eventValue = new HashMap<>();
        //client Required
        eventValue.put("merchantname", merchantName); //Tên đối tác. được đăng ký tại https://business.momo.vn. VD: Google, Apple, Tiki , CGV Cinemas
        eventValue.put("merchantcode", merchantCode); //Mã đối tác, được cung cấp bởi MoMo tại https://business.momo.vn
        eventValue.put("amount", amount); //Kiểu integer
        eventValue.put("orderId", iddonHang); //uniqueue id cho Bill order, giá trị duy nhất cho mỗi đơn hàng
        eventValue.put("orderLabel", iddonHang); //gán nhãn

        //client Optional - bill info
        eventValue.put("merchantnamelabel", "Dịch vụ");//gán nhãn
        eventValue.put("fee", fee); //Kiểu integer
        eventValue.put("description", description); //mô tả đơn hàng - short description

        //client extra data
        eventValue.put("requestId",  merchantCode+"merchant_billId_"+System.currentTimeMillis());
        eventValue.put("partnerCode", merchantCode);
        //Example extra data
        JSONObject objExtraData = new JSONObject();
        try {
            objExtraData.put("site_code", "008");
            objExtraData.put("site_name", "CGV Cresent Mall");
            objExtraData.put("screen_code", 0);
            objExtraData.put("screen_name", "Special");
            objExtraData.put("movie_name", "Kẻ Trộm Mặt Trăng 3");
            objExtraData.put("movie_format", "2D");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        eventValue.put("extraData", objExtraData.toString());

        eventValue.put("extra", "");
        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue);


    }
    //Get token callback from MoMo app an submit to server side
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            if(data != null) {
                if(data.getIntExtra("status", -1) == 0) {
                    //TOKEN IS AVAILABLE
                    //tvMessage.setText("message: " + "Get token " + data.getStringExtra("message"));
                    String user=getUser();
                    bill.setTrangthaiThanhToan(true);
                    ref.child("Bill").child(bill.getMaDonHang()).setValue(bill);
                    ref.child("User").child(user).child("listBill").child(bill.getMaDonHang()).setValue(bill);
                    Intent intent = new Intent(ThanhtoanActivity.this, OderMainActivity.class);
                    intent.putExtra("tttc", tttc);
                    startActivity(intent);
                    finish();
                    Log.d("Thành công",data.getStringExtra("message"));
                    String token = data.getStringExtra("data"); //Token response
                    String phoneNumber = data.getStringExtra("phonenumber");
                    String env = data.getStringExtra("env");
                    if(env == null){
                        env = "app";
                    }

                    if(token != null && !token.equals("")) {
                        // TODO: send phoneNumber & token to your server side to process payment with MoMo server
                        // IF Momo topup success, continue to process your order
                    } else {
                        //tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
                        Log.d("Thành công","Không thành công");
                    }
                } else if(data.getIntExtra("status", -1) == 1) {
                    //TOKEN FAIL
                    String message = data.getStringExtra("message") != null?data.getStringExtra("message"):"Thất bại";
                    //tvMessage.setText("message: " + message);
                    Log.d("Thành công","Không thành công");
                } else if(data.getIntExtra("status", -1) == 2) {
                    //TOKEN FAIL
                    //tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
                    Log.d("Thành công","Không thành công");
                } else {
                    //TOKEN FAIL
                    //tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
                    Log.d("Thành công","Không thành công");
                }
            } else {
                //tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
                Log.d("Thành công","Không thành công");
            }
        } else {
            //tvMessage.setText("message: " + this.getString(R.string.not_receive_info_err));
            Log.d("Thành công","Không thành công");
        }
    }

    private void addEvents() {
        btnHoanTatThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar2.setVisibility(View.VISIBLE);
                String ten,diachi;
                ten=txtTnnThanhToan.getText().toString();
                sdt="+84"+txtSDTNH.getText().toString();
                diachi=txtDiaChiNH.getText().toString();
                Calendar calendar=Calendar.getInstance();
                String time= String.valueOf(calendar.getTime());
                if(TextUtils.isEmpty(ten)){
                    txtTnnThanhToan.setError("Bạn chưa nhập tên người nhận");
                    txtTnnThanhToan.requestFocus();
                }
                else if(TextUtils.isEmpty(sdt)){
                    txtSDTNH.setError("Bạn chưa nhập số điện thoại");
                    txtSDTNH.requestFocus();
                }
                else if(TextUtils.isEmpty(diachi)){
                    txtDiaChiNH.setError("Bạn chưa nhập địa chỉ");
                    txtDiaChiNH.requestFocus();
                }
                else {
                    String user=getUser();
                    ref.child("User").child(user).child("listoder").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot data : snapshot.getChildren()) {
                                Menu menu = data.getValue(Menu.class);
                                menuList.add(menu);
                            }
                            bill.setTenKhacHang(ten);
                            bill.setSodt(sdt);
                            bill.setDiaChi(diachi);
                            bill.setMenuList(menuList);
                            bill.setTongtien(tong + " đ");
                            bill.setTime(time);
                            bill.setMaDonHang(String.valueOf(calendar.getTimeInMillis()));
                            bill.setTrangthaiThanhToan(false);
                            bill.setTrangthaidonhang(false);
                            ///


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    getOTP(sdt);

                }
            }
        });

    }

    private void addContols() {
        txtTnnThanhToan=(EditText) findViewById(R.id.txtTnnThanhToan);
        txtSDTNH=(EditText) findViewById(R.id.txtSDTNH);
        txtDiaChiNH=(EditText) findViewById(R.id.txtDiaChiNH);
        btnHoanTatThanhToan=(Button) findViewById(R.id.btnHoanTatThanhToan);
        rdoOn=(RadioButton) findViewById(R.id.rdoOn);
        rdoOff=(RadioButton) findViewById(R.id.rdoOff);
        progressBar2=(ProgressBar) findViewById(R.id.progressBar2);
        //database
        db=FirebaseDatabase.getInstance();
        ref=db.getReference();
    }
}