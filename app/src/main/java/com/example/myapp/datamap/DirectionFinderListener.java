package com.example.myapp.datamap;



import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import com.example.myapp.datamap.Route;

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}