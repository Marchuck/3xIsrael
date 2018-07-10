package com.marchuck.a3xisrael.bomb;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.Random;

public class BombsPlacement {

    private static final LatLng zero = new LatLng(0, 0);

    private static final int size = 3;

    public final LatLng[] locations;

    public BombsPlacement(final LatLng... locations) {
        if (locations == null || locations.length != size) {
            this.locations = new LatLng[]{zero, zero, zero};
        } else {
            this.locations = locations;
        }
    }

    public BombsPlacement(BombsPlacement current, LatLng newOne) {
        this(newOne, current.locations[0], current.locations[1]);
    }

    public BombsPlacement push(LatLng newOne) {
        return new BombsPlacement(this, newOne);
    }

    public BombsPlacement pushRandom(Random random) {
        LatLng randomLatLng = new LatLng(40 + 20 * random.nextDouble(), 15 + 20 * random.nextDouble());
        return push(randomLatLng);
    }

    public LatLngBounds getBounds() {
        double minLat = zero.latitude,
                minLon = zero.longitude,
                maxLat = zero.latitude,
                maxLon = zero.longitude;

        double lat, lon;
        for (LatLng latLng : locations) {
            lat = latLng.latitude;
            lon = latLng.longitude;
            if (lat < minLat) minLat = lat;
            if (lat > maxLat) maxLat = lat;
            if (lon > maxLon) maxLon = lon;
            if (lon < minLon) minLon = lon;
        }
        return new LatLngBounds(new LatLng(minLat, minLon), new LatLng(maxLat, maxLon));
    }
}



