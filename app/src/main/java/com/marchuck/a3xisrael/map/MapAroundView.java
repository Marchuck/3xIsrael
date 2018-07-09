package com.marchuck.a3xisrael.map;

import android.location.Location;

/**
 * Project "Evalu"
 * <p>
 * Created by Lukasz Marczak
 * on 23.02.2017.
 */

public interface MapAroundView {

    void onReceivedLocation(Location location);

    void showGpsNotReady();

    void onPlaceReceiveError();
}
