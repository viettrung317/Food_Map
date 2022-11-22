package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

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

import me.relex.circleindicator.CircleIndicator3;

public class HomeMainActivity extends AppCompatActivity {
    private List<Slide> slideList;
    private ViewPager2 rcvSlide;
    private SlideAdapter slideAdapter;
    private CircleIndicator3 circleIndicator3;

    //Tự động chạy slide
    private Handler handler=new Handler(Looper.getMainLooper());
    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            int position=rcvSlide.getCurrentItem();
            if(position==slideList.size()-1){//nếu như đến slide cuối thì quay lại slide đầu tiên
                rcvSlide.setCurrentItem(0);
            }else{
                rcvSlide.setCurrentItem(position+1);//tự động chuyển đến slide tiếp theo
            }
        }
    };

    private List<Item> itemList;
    private ListView lvItem;
    private ItemAdapter itemAdapter;
    private EditText txtSeachFood;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference ref=database.getReference();
    private int seach;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_main);
        addControl();
        sLide();
        iTem();
        filterList();
        addEvents();
    }

    private void addControl() {
        txtSeachFood=(EditText) findViewById(R.id.txtSeachFood) ;
        Intent intent=getIntent();
        seach=intent.getIntExtra("Seach",0);
        if(seach==1){
            txtSeachFood.requestFocus();
        }
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
        rcvSlide= (ViewPager2) findViewById(R.id.vpSlide);
        circleIndicator3=(CircleIndicator3)findViewById(R.id.circleIndicator3);

        //Setting viewpager2
        rcvSlide.setOffscreenPageLimit(3);
        rcvSlide.setClipToPadding(false);
        rcvSlide.setClipChildren(false);

        CompositePageTransformer compositePageTransformer=new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r=1-Math.abs(position);
                page.setScaleY(0.85f+r*0.15f);
            }
        });
        rcvSlide.setPageTransformer(compositePageTransformer);
        rcvSlide.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.removeCallbacks(runnable);//vì runnable đang chạy nên khi mình chuyển page thì phải xóa runnable cũ đi.
                handler.postDelayed(runnable,3000);//cấu hình lại runnable
            }
        });
        slideList = getList();
        slideAdapter=new SlideAdapter(this,slideList);
        rcvSlide.setAdapter(slideAdapter);
        circleIndicator3.setViewPager(rcvSlide);

    }

    private List<Slide> getList() {
        List<Slide> List=new ArrayList<>();
        List.add(new Slide(R.drawable.baemingiaodo,""));
        List.add(new Slide(R.drawable.beamindicho,""));
        List.add(new Slide(R.drawable.nhatrangbeamin,""));
        return List;
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


    //thoát khỏi home khi trở lại thì hiển thị đúng trang slide lúc vừa thoát
    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable,3000);//Cấu hình runnable set time chuyển page
    }
}