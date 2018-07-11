package com.marchuck.a3xisrael.map;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


public interface MapAroundView {

    void onReceivedNukes(ArrayList<LatLng> nukes);

    void showGpsNotReady();

    void onPlaceReceiveError();

    void showMoveNukeDialog(Integer index);

    void showPlacedNuke();

    void onPlaceNukeError();
}
