package com.example.myapp;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.myapp.Itemmodel.Item;
import com.example.myapp.datamap.FetchURL;
import com.example.myapp.datamap.TaskLoadedCallback;
import com.example.myapp.itemadapter.TitleAdapter;
import com.example.myapp.usermodel.UserSignUp;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.AsyncTaskLoader;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private long backPressedTime;
    private Toast mToast;
    ImageButton ibtnMenu,ibtnUser,ibtnHome,ibtnOrder,ibtnHistory,ibtnBill;
    FirebaseAuth mAuth;
    Button btnSeach;
    private static final int REQUEST_CODE_GPS_PERMISSION = 100;
    private GoogleMap mMap;
    private Polyline newPolyline;
    private LatLngBounds latlngBounds;
    private FirebaseDatabase db=FirebaseDatabase.getInstance();
    private DatabaseReference ref=db.getReference();
    private LatLng pl1;
    private Double Latitude,Longitude;
    private Double lat=0.0,log=0.0;
    private String name;
    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.foodMap);
        mapFragment.getMapAsync(this);
        addControl();
        addEvents();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Toast.makeText(this, "đang đăng nhập", Toast.LENGTH_SHORT).show();
        } else {
            // No user is signed in
        }

    }


    //Xử lý hàm trở lại để thoát
    public void onBackPressed(){
        if(TextUtils.isEmpty(name)){
            if(backPressedTime+2000>System.currentTimeMillis()){
                mToast.cancel();//khi thoát ứng dụng sẽ ko hiện thông báo toast
                super.onBackPressed();
                return;
            }else {
                mToast=Toast.makeText(this, "Nhấn lần nữa để thoát!", Toast.LENGTH_SHORT);
                mToast.show();
            }
            backPressedTime=System.currentTimeMillis();
        }
        else{
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startActivity(startMain);

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(MainActivity.this, Login.class));
        }
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        if(lat!=0.0 && log!=00) {
            pl1 = new LatLng(lat, log);
            Marker marker=mMap.addMarker(new MarkerOptions().position(pl1).title(name));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pl1, 17));
            mMap.setInfoWindowAdapter(new TitleAdapter(MainActivity.this,item));
            marker.showInfoWindow();
        }else{
            getCurrentLocation();
        }
        mMap.setMyLocationEnabled(true);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //TODO: Get current location
            //getCurrentLocation();
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_GPS_PERMISSION);
        }

    }

    private void getCurrentLocation() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            //Hiển thị vị trí hiện tại
            @Override
            public void onSuccess(Location location) {
                if (location == null) {
                    Toast.makeText(MainActivity.this, "ko tìm thấy vị trí", Toast.LENGTH_SHORT).show();
                    return;
                }
                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                Latitude=location.getLatitude();
                Longitude=location.getLongitude();
                //mMap.addMarker(new MarkerOptions().position(currentLocation).title("Vị trí hiện tại của bạn"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,17));



//                pl1=new LatLng(10.882885848145596, 106.78245011085099);
//                pl2=new LatLng(10.876488866301308, 106.79904111085007);
//                place1 = new MarkerOptions().position(pl1).title("Location 1");
//                place2 = new MarkerOptions().position(pl2).title("Location 2");
//                String url=getUrl(place1.getPosition(),place2.getPosition(),"driving");
//                new FetchURL(MainActivity.this).execute(url,"driving");

            }
        });


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_GPS_PERMISSION:
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        //TODO: Action when permission denied
                    }
                }
                break;
        }

    }
//    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
//        // Origin of route
//        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
//        // Destination of route
//        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
//        // Mode
//        String mode = "mode=" + directionMode;
//        // Building the parameters to the web service
//        String parameters = str_origin + "&" + str_dest + "&" + mode;
//        // Output format
//        String output = "json";
//        // Building the url to the web service
//        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_api_key);
//        return url;
//    }
//    @Override
//    public void onTaskDone(Object... values) {
//        if(newPolyline!=null){
//            newPolyline.remove();
//        }
//        newPolyline=mMap.addPolyline((PolylineOptions) values[0]);
//    }

    private void addEvents() {
        ibtnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,User.class));
            }
        });
        ibtnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref.child("Source").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        startActivity(new Intent(MainActivity.this,HomeMainActivity.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        ibtnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,AdMainActivity.class);
                intent.putExtra("Latitude",Latitude);
                intent.putExtra("Longitude",Longitude);
                startActivity(intent);
            }
        });
        ibtnOrder.setOnClickListener(new View.OnClickListener() {
            int i=0;
            @Override
            public void onClick(View view) {
                ref.child("User").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(i==0) {
                            Intent intent=new Intent(MainActivity.this,OderMainActivity.class);
                            intent.putExtra("tttc","");
                            startActivity(intent);
                        }
                        i=1;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                if(i==1){
                    Intent intent=new Intent(MainActivity.this,OderMainActivity.class);
                    intent.putExtra("tttc","");
                    startActivity(intent);
                }
            }


        });
        ibtnHistory.setOnClickListener(new View.OnClickListener() {
            int j=0;
            @Override
            public void onClick(View view) {
                ref.child("User").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(j==0) {
                            UserSignUp userSignUp=snapshot.getValue(UserSignUp.class);
                            startActivity(new Intent(MainActivity.this, HistoryMainActivity.class));
                        }
                        j=1;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                if(j==1){
                    startActivity(new Intent(MainActivity.this,HistoryMainActivity.class));
                }
            }

        });
        btnSeach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref.child("Source").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Intent inten =new Intent(MainActivity.this,HomeMainActivity.class);
                        inten.putExtra("Seach",1);
                        startActivity(inten);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        ibtnBill.setOnClickListener(new View.OnClickListener() {
            int i=0;
            @Override
            public void onClick(View view) {
                ref.child("User").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(i==0) {
                            Intent intent=new Intent(MainActivity.this,CartMainActivity.class);

                            startActivity(intent);
                        }
                        i=1;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                if(i==1){
                    Intent intent=new Intent(MainActivity.this,CartMainActivity.class);
                    startActivity(intent);
                }
            }


        });
    }


    private void addControl() {
        ibtnMenu=(ImageButton)findViewById(R.id.ibtnMenu);
        ibtnUser=(ImageButton)findViewById(R.id.ibtnUser);
        ibtnHome=(ImageButton)findViewById(R.id.ibtnHome);
        ibtnOrder=(ImageButton)findViewById(R.id.ibtnOrder);
        ibtnBill=(ImageButton) findViewById(R.id.ibtnBill);
        ibtnHistory=findViewById(R.id.ibtnHistory);
        btnSeach=findViewById(R.id.btnSeach);
        mAuth = FirebaseAuth.getInstance();
        Intent intent=getIntent();
        item=(Item) intent.getSerializableExtra("Item");
        lat= intent.getDoubleExtra("Lat",0.0);
        log=intent.getDoubleExtra("Long",0.0);
        name=intent.getStringExtra("name");
    }
}