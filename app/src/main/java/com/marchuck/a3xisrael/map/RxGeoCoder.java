package com.marchuck.a3xisrael.map;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class RxGeoCoder {

    Geocoder geocoder;

    public RxGeoCoder(Context context) {
        this.geocoder = new Geocoder(context.getApplicationContext());
    }

    public Observable<String> getLocationName(final double lat, final double lon) {

        return Observable.fromCallable(new Callable<List<Address>>() {
            @Override
            public List<Address> call() throws Exception {
                try {
                    return geocoder.getFromLocation(lat, lon, 1);
                } catch (IOException x) {
                    throw new LocationNotFoundException();
                }
            }
        }).map(new Function<List<Address>, String>() {
            @Override
            public String apply(List<Address> addresses) throws Exception {

                if (addresses.isEmpty()) return "";
                Address firstAddress = addresses.get(0);
                return formattedWellAddress(firstAddress);
            }
        }).onErrorReturn(new Function<Throwable, String>() {
                    @Override
                    public String apply(Throwable throwable) throws Exception {
                        return "";
                    }
                });
    }

    String formattedWellAddress(Address address) throws LocationNotFoundException {
        int size = address.getMaxAddressLineIndex();

        if (size >= 3) {
            return address.getAddressLine(1) + " " + address.getAddressLine(0);
        }
        switch (size) {
            case 1:
                return address.getAddressLine(0);
            case 2:
                return address.getAddressLine(1) + " " + address.getAddressLine(0);
            case 0:
            default:
                throw new LocationNotFoundException();
        }
    }

}
