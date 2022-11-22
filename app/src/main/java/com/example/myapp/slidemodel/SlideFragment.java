package com.example.myapp.slidemodel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapp.R;

public class SlideFragment extends Fragment {
    private View mView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.slide_show,container,false);

        Bundle bundle=getArguments();
        Slide slide= (Slide) bundle.get("slide");
        ImageView imgSlide=mView.findViewById(R.id.imgSlide);
        imgSlide.setImageResource(slide.getResourceID());
        return mView;
    }
}
