package com.marchuck.a3xisrael.map;

import android.location.Location;
import android.util.Log;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.SerialDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class MapAroundPresenter {
    public static final String TAG = MapAroundPresenter.class.getSimpleName();
    public MapAroundView view;

    SerialDisposable disposable = new SerialDisposable();

    public MapAroundPresenter() {

    }

    public void fetchLocation(final Location fetchedLocation, RxGeoCoder wrapper) {
        if (fetchedLocation != null) {
            double dLatitude = fetchedLocation.getLatitude();
            double dLongitude = fetchedLocation.getLongitude();

            disposable.set(wrapper.getLocationName(dLatitude, dLongitude)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorReturn(new Function<Throwable, String>() {
                        @Override
                        public String apply(Throwable throwable) throws Exception {
                            return "You";
                        }
                    })
                    .map(new Function<String, Location>() {
                        @Override
                        public Location apply(String value) throws Exception {
                            Location location = new Location(value);
                            location.setLatitude(fetchedLocation.getLatitude());
                            location.setLongitude(fetchedLocation.getLongitude());
                            return location;
                        }
                    })
                    .subscribe(new Consumer<Location>() {
                        @Override
                        public void accept(Location value) {
                            view.onReceivedLocation(value);
                        }
                    }, new Consumer<Throwable>() {

                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            fetchedLocation.setProvider("You");
                            view.onReceivedLocation(fetchedLocation);
                            view.onPlaceReceiveError();
                        }
                    }));
        } else {
            view.onPlaceReceiveError();
        }
    }
}
