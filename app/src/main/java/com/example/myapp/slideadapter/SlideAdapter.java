package com.example.myapp.slideadapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.R;
import com.example.myapp.slidemodel.Slide;

import java.util.List;


public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.SlideViewHolder>{
    private List<Slide> slideList;
    private Activity context;
    public SlideAdapter(List<Slide> slideList,Activity context){
        this.slideList=slideList;
        this.context=context;

    }
    @NonNull
    @Override
    public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_show,parent,false);
        return new SlideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlideViewHolder holder, int position) {
        Slide slide=slideList.get(position);
        if(slide==null) return;
        holder.imgSlide.setImageResource(slide.getResourceID());

    }

    @Override
    public int getItemCount() {
        if(slideList!=null){
            return slideList.size();
        }
        return 0;
    }

    public class SlideViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgSlide;
        public SlideViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSlide=(ImageView) itemView.findViewById(R.id.imgSlide);
        }
    }
}
