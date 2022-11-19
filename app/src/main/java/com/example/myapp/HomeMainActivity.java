package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.myapp.Itemmodel.Item;

import com.example.myapp.itemadapter.ItemAdapter;
import com.example.myapp.menuadapter.Menuadapter;
import com.example.myapp.menumodel.Menu;
import com.example.myapp.slideadapter.SlideAdapter;
import com.example.myapp.slidemodel.Slide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeMainActivity extends AppCompatActivity {
    private List<Slide> slideList;
    private RecyclerView rcvSlide;
    private SlideAdapter slideAdapter;

    private List<Item> itemList;
    private ListView lvItem;
    private ItemAdapter itemAdapter;
    private EditText txtSeachFood;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference ref=database.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_main);
        sLide();
        iTem();
        filterList();
//        List<Menu> list=new ArrayList<>();
//        list.add(new Menu("200.000đ","gà nướng mọi","https://firebasestorage.googleapis.com/v0/b/myapp-8af87.appspot.com/o/imageQuan1668227680164.png?alt=media&token=bbf75201-dc8d-42bf-8810-5e469104ec37"));
//        list.add(new Menu("180.000đ","gà luộc","https://firebasestorage.googleapis.com/v0/b/myapp-8af87.appspot.com/o/imageQuan1668227680164.png?alt=media&token=bbf75201-dc8d-42bf-8810-5e469104ec37"));
//        Item item=new Item("gà đồi","gà kfc","bình dương","https://firebasestorage.googleapis.com/v0/b/myapp-8af87.appspot.com/o/imageQuan1668227680164.png?alt=media&token=bbf75201-dc8d-42bf-8810-5e469104ec37",list);
//        ref.child("sbxbxbdy").updateChildren(item.toMap());
        addEvents();
    }

    private void filterList() {
    }
//    private void writeNewPost(String tenquan, String tenmon, String diachi, String sourceimg,List<Menu> list) {
//        // Create new post at /user-posts/$userid/$postid and at
//        // /posts/$postid simultaneously
//        String key =ref.child("Source").push().getKey();
//        Item item = new Item(tenquan,tenmon,diachi, sourceimg,list);
//        Map<String, Object> postValues =item.toMap();
//
//        Map<String, Object> childUpdates = new HashMap<>();
//        //childUpdates.put(key, postValues);
//        ref.updateChildren(childUpdates);
//    }



    private void addEvents() {
        lvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(HomeMainActivity.this,MenuMainActivity.class);
                Item item=itemList.get(i);
                Toast.makeText(HomeMainActivity.this, item.getTenQuan(), Toast.LENGTH_SHORT).show();
                intent.putExtra("Item",item);
                startActivity(intent);
            }
        });


    }

    private void iTem() {
        txtSeachFood=(EditText) findViewById(R.id.txtSeachFood) ;
        lvItem=(ListView) findViewById(R.id.lvItem);
        itemList=new ArrayList<>();
        itemAdapter=new ItemAdapter(HomeMainActivity.this,R.layout.item,itemList);
        ref=database.getReference("Source");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot data) {
                for(DataSnapshot dataSnapshot:data.getChildren()){
                    Item item=dataSnapshot.getValue(Item.class);
                    itemList.add(item);
                }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

        });

        lvItem.setAdapter(itemAdapter);
        itemAdapter.notifyDataSetChanged();
        txtSeachFood.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ArrayList<Item> List = new ArrayList<>();
                for (Item item : itemList) {
                    if (item.getTenQuan().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        List.add(item);
                    }
                }
                itemAdapter=new ItemAdapter(HomeMainActivity.this,R.layout.item,List);
                lvItem.setAdapter(itemAdapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void sLide() {
        rcvSlide=(RecyclerView) findViewById(R.id.rcvSlide);
        slideList=new ArrayList<>();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rcvSlide.setLayoutManager(linearLayoutManager);

        SnapHelper snapHelper=new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rcvSlide);

        slideList.add(new Slide(R.drawable.baemingiaodo,""));
        slideList.add(new Slide(R.drawable.beamindicho,""));
        slideList.add(new Slide(R.drawable.nhatrangbeamin,""));
        slideAdapter=new SlideAdapter(slideList,HomeMainActivity.this);
        rcvSlide.setAdapter(slideAdapter);
        slideAdapter.notifyDataSetChanged();
    }
//    private void filter(String Text) {
//        itemList= new ArrayList<>();
//        for (Item item : itemList) {
//            if (item.getTenQuan().toLowerCase().contains(Text.toLowerCase())) {
//                itemList.add(item);
//            }
//        }
//        itemAdapter.FilterList(itemList);
//    }
}