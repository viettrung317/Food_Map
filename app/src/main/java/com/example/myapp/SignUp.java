package com.example.myapp;

import android.app.Activity;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapp.menumodel.Bill;
import com.example.myapp.menumodel.Menu;
import com.example.myapp.usermodel.UserSignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class SignUp extends AppCompatActivity {
    private static final int REQUESTCODE_CAMERA = 777;
    private static final int REQUESTCODE_FOLDER = 999;
    EditText txtEmail,txtPasswordSU;
    Button btnSignUp;
    TextView txtLogin;
    ImageView imgAvatarli;
    ImageButton ibtnAvatarCamera,getIbtnAvatarFolder;
    ProgressBar progress;
    FirebaseAuth mAuth;
    private List<Menu> oderlist,listDaMua;
    private List<Bill> listBill;
    private Menu menu;
    private FirebaseDatabase db;
    private DatabaseReference ref;
    FirebaseStorage storage=FirebaseStorage.getInstance();
    Bitmap bitmapImagesAvatar=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        addControl();
        addEvents();


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUESTCODE_CAMERA && resultCode == Activity.RESULT_OK && data != null) ;
        {
            try {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgAvatarli.setImageBitmap(bitmap);
                bitmapImagesAvatar = bitmap;


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
                bitmapImagesAvatar = bitmap;
                imgAvatarli.setImageURI(data.getData());


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Toast.makeText(this, "Thêm ảnh thành công !", Toast.LENGTH_SHORT).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void addEvents() {
        ibtnAvatarCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, REQUESTCODE_CAMERA);
            }
        });
        getIbtnAvatarFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUESTCODE_FOLDER);
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }


        });
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this,Login.class));
            }
        });
    }

    private void createUser() {
        final StorageReference storageRef = storage.getReferenceFromUrl("gs://myapp-8af87.appspot.com");
        String password,email;
        email=txtEmail.getText().toString();
        password=txtPasswordSU.getText().toString();
        if(TextUtils.isEmpty(email)){
            txtEmail.setError("Email can't empty");
            txtEmail.requestFocus();
        }
        else if(TextUtils.isEmpty(password)){
            txtPasswordSU.setError("Password can't empty");
            txtPasswordSU.requestFocus();
        }
        else{
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progress.setVisibility(View.GONE);
                    if(task.isSuccessful()){
                        Toast.makeText(SignUp.this,"User registered successfully !",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(SignUp.this,Login.class));
///////////////////////////////////////////////////////
                        Calendar calendar = Calendar.getInstance();
                        StorageReference mountainsRef = storageRef.child("imageAvatar" + calendar.getTimeInMillis() + ".png");
                        // Get the data from an ImageView as bytes
                        imgAvatarli.setDrawingCacheEnabled(true);
                        imgAvatarli.buildDrawingCache();
                        Bitmap bitmap = ((BitmapDrawable) imgAvatarli.getDrawable()).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] data = baos.toByteArray();

                        UploadTask uploadTask = mountainsRef.putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                Toast.makeText(SignUp.this, "False !!!", Toast.LENGTH_LONG).show();
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
                                        String user,email;
                                        email = txtEmail.getText().toString();
                                        int index=email.indexOf("@");
                                        user=email.substring(0,index);
                                        UserSignUp userSignUp= new UserSignUp();
                                        userSignUp.setEmail(email);
                                        userSignUp.setUserName(user);
                                        userSignUp.setAvatar(downloadUrl.toString());
                                        listBill=new ArrayList<>();
                                        oderlist = new ArrayList<>();
                                        listDaMua=new ArrayList<>();
                                        userSignUp.setListBill(listBill);
                                        userSignUp.setListoder(oderlist);
                                        userSignUp.setListDaMua(listDaMua);
                                        ref.child("User").child(user).setValue(userSignUp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    listBill=new ArrayList<>();
                                                    oderlist = new ArrayList<>();
                                                    listDaMua=new ArrayList<>();
                                                }
                                            }

                                        });

                                    }
                                });
                            }
                        });
                    }
                    else{
                        Toast.makeText(SignUp.this,"Registered error :"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void addControl() {
        txtEmail=findViewById(R.id.txtEmail);
        txtPasswordSU=findViewById(R.id.txtPasswordSU);
        btnSignUp=findViewById(R.id.btnSignUp);
        txtLogin=findViewById(R.id.txtLogin);
        imgAvatarli=findViewById(R.id.imgAvatarli);
        ibtnAvatarCamera=findViewById(R.id.ibtnAvatarcamera);
        getIbtnAvatarFolder=findViewById(R.id.ibtnAvatarFolder);
        progress=findViewById(R.id.progress);
        mAuth=FirebaseAuth.getInstance();
        db=FirebaseDatabase.getInstance();
        ref=db.getReference();
        oderlist=new ArrayList<>();
        listDaMua=new ArrayList<>();




    }
    //Starting Write and Read data with URL
    //Creating array for parameters


}