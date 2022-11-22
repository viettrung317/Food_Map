package com.example.myapp.slideadapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapp.R;
import com.example.myapp.slidemodel.Slide;
import com.example.myapp.slidemodel.SlideFragment;

import java.util.List;


public class SlideAdapter extends FragmentStateAdapter {
    private List<Slide> slideList;
    public SlideAdapter(@NonNull FragmentActivity fragmentActivity,List<Slide> slideList) {
        super(fragmentActivity);
        this.slideList=slideList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Slide slide=slideList.get(position);
        //Chuyển oject slide sang fragment
        Bundle bundle=new Bundle();
        bundle.putSerializable("slide",slide);

        SlideFragment slideFragment=new SlideFragment();
        slideFragment.setArguments(bundle);
        return slideFragment;
    }


    @Override
    public int getItemCount() {
        if(slideList!=null){
            return slideList.size();
        }
        return 0;
    }

    public static class SlideViewHolder extends RecyclerView.ViewHolder{
        private final ImageView imgSlide;
        public SlideViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSlide=(ImageView) itemView.findViewById(R.id.imgSlide);
        }
    }
}
