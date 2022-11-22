package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapp.Itemmodel.Item;
import com.example.myapp.convert.DataConvert;
import com.example.myapp.menumodel.Menu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class AdMainActivity extends AppCompatActivity {
    private static final int REQUESTCODE_CAMERA = 777;
    private static final int REQUESTCODE_FOLDER = 999;
    private EditText txtTenQuanAd,txtTenMonAd,txtDiaChiAd,txtTenMonAdmenu,txtGiaAd;
    private Button btnUpDateAd,btnAddMenu;
    private ImageButton ibtnAddCamenraQuan,ibtnAddCamenraMon,ibtnAddFolderQuan,ibtnAddFolderMon;
    private ImageView imgAddQuan,imgAddMon;
    private List<Menu> menuList;
    private Menu menu;
    private FirebaseDatabase db;
    private DatabaseReference ref;
    Bitmap bitmapImagesQuan=null;
    Bitmap bitmapImagesMon=null;
    int temp=0;
    private Double Latitude,Longitude;
    FirebaseStorage storage=FirebaseStorage.getInstance();
    //StorageReference mountainImagesRef = storageRef.child("images/mountains.jpg");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_main);
        addcontrols();
        addEvent();


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUESTCODE_CAMERA && resultCode == Activity.RESULT_OK && data != null) ;
        {
            try {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                if(temp==1) {
                imgAddQuan.setImageBitmap(bitmap);
                bitmapImagesQuan = bitmap;
                }
                else if(temp==2) {
                    imgAddMon.setImageBitmap(bitmap);
                    bitmapImagesMon = bitmap;
                }

            } catch (Exception e) {
                Log.e("erro", "" + e);
            }
            Toast.makeText(this, "Thêm ảnh thành công !", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == REQUESTCODE_FOLDER && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = this.getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                if(temp==1){
                    bitmapImagesQuan = bitmap;
                    imgAddQuan.setImageURI(data.getData());
                }
                else if(temp==2){
                    bitmapImagesMon = bitmap;
                    imgAddMon.setImageURI(data.getData());
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Toast.makeText(this, "Thêm ảnh thành công !", Toast.LENGTH_SHORT).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void addEvent() {
        final StorageReference storageRef = storage.getReferenceFromUrl("gs://myapp-8af87.appspot.com");
        ibtnAddCamenraQuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, REQUESTCODE_CAMERA);
                temp = 1;

            }
        });
        ibtnAddFolderQuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUESTCODE_FOLDER);
                temp = 1;

            }
        });
        ibtnAddCamenraMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, REQUESTCODE_CAMERA);
                temp = 2;

            }
        });
        ibtnAddFolderMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUESTCODE_FOLDER);
                temp = 2;

            }
        });
        btnAddMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp = 2;
                Calendar calendar = Calendar.getInstance();
                StorageReference mountainsRef = storageRef.child("imageMenu" + calendar.getTimeInMillis() + ".png");
                // Get the data from an ImageView as bytes
                imgAddMon.setDrawingCacheEnabled(true);
                imgAddMon.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) imgAddMon.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = mountainsRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(AdMainActivity.this, "False !!!", Toast.LENGTH_LONG).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        // ...
                        mountainsRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Uri downloadUrl = uri;
                                String tenmonan, gia;
                                tenmonan = txtTenMonAdmenu.getText().toString();
                                gia = txtGiaAd.getText().toString();
                                if (TextUtils.isEmpty(tenmonan)) {
                                    txtTenMonAdmenu.setError(" can't empty");
                                    txtTenMonAdmenu.requestFocus();
                                } else if (TextUtils.isEmpty(gia)) {
                                    txtGiaAd.setError(" can't empty");
                                    txtGiaAd.requestFocus();
                                } else {
                                    menu = new Menu();
                                    menu.setTenMonAn(tenmonan);
                                    menu.setGia(gia);
                                    menu.setIdImg(downloadUrl.toString());
                                    menuList.add(menu);
                                }
                                Toast.makeText(AdMainActivity.this, "Success !!!", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        });
        btnUpDateAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp = 1;
                Calendar calendar = Calendar.getInstance();
                StorageReference mountainsRef = storageRef.child("imageQuan" + calendar.getTimeInMillis() + ".png");
                // Get the data from an ImageView as bytes
                imgAddQuan.setDrawingCacheEnabled(true);
                imgAddQuan.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) imgAddQuan.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = mountainsRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(AdMainActivity.this, "False !!!", Toast.LENGTH_LONG).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        // ...
                        mountainsRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final Uri downloadUrlQuan = uri;

                                    String tenquan, tenmon, diachi;
                                    tenquan = txtTenQuanAd.getText().toString();
                                    tenmon = txtTenMonAd.getText().toString();
                                    diachi = txtDiaChiAd.getText().toString();
                                    if (TextUtils.isEmpty(tenquan)) {
                                        txtTenQuanAd.setError(" can't empty");
                                        txtTenQuanAd.requestFocus();
                                    } else if (TextUtils.isEmpty(tenmon)) {
                                        txtTenMonAd.setError(" can't empty");
                                        txtTenMonAd.requestFocus();
                                    } else if (TextUtils.isEmpty(diachi)) {
                                        txtDiaChiAd.setError(" can't empty");
                                        txtDiaChiAd.requestFocus();
                                    } else {
                                        Item item = new Item();
                                        item.setTenQuan(tenquan);
                                        item.setTenMon(tenmon);
                                        item.setDiaChi(diachi);
                                        item.setImageSourceID(downloadUrlQuan.toString());
                                        item.setMenuList(menuList);
                                        item.setLatitude(Latitude);
                                        item.setLongitude(Longitude);
                                        ref.child("Source").child(tenquan).setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(AdMainActivity.this, "User registered successfully !", Toast.LENGTH_LONG).show();
                                                    menuList = new ArrayList<>();

                                                } else {
                                                    Toast.makeText(AdMainActivity.this, "Registered error :" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            }

                                        });
                                    }
                            }
                        });
                    }

                });
            }
        });
    }



    private void addcontrols() {
        txtTenQuanAd=(EditText) findViewById(R.id.txtTenQuanAd);
        txtTenMonAd=(EditText) findViewById(R.id.txtTenMonAd);
        txtDiaChiAd=(EditText)findViewById(R.id.txtDiaChiAd) ;
        imgAddQuan=(ImageView) findViewById(R.id.imgAddQuan);
        ibtnAddCamenraQuan=(ImageButton)findViewById(R.id.ibtnAddCameraQuan) ;
        ibtnAddCamenraMon=(ImageButton)findViewById(R.id.ibtnAddCameraMon) ;
        ibtnAddFolderQuan=(ImageButton)findViewById(R.id.ibtnAddFolderQuan) ;
        ibtnAddFolderMon=(ImageButton)findViewById(R.id.ibtnAddFolderMon) ;
        btnUpDateAd=(Button) findViewById(R.id.btnUpdateAd);

        txtGiaAd=(EditText) findViewById(R.id.txtGiaAd);
        imgAddMon=(ImageView) findViewById(R.id.imgAddMon);
        txtTenMonAdmenu=(EditText) findViewById(R.id.txtTenMonAdMenu);
        btnAddMenu=(Button) findViewById(R.id.btnAddMenu);
        db=FirebaseDatabase.getInstance();
        ref=db.getReference();
        menuList=new ArrayList<>();
        Intent intent=getIntent();
        Latitude=intent.getDoubleExtra("Latitude",0.0);
        Longitude=intent.getDoubleExtra("Longitude",0.0);
    }
}
