package com.marchuck.a3xisrael.map;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.marchuck.a3xisrael.api.EmptyResponse;
import com.marchuck.a3xisrael.api.MoveNukeRequest;
import com.marchuck.a3xisrael.api.NukeDirection;
import com.marchuck.a3xisrael.nukeRefresher.GetNukesUseCase;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.EOFException;
import java.util.ArrayList;
import java.util.Arrays;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.SerialDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MapAroundPresenter {
    public static final String TAG = MapAroundPresenter.class.getSimpleName();
    public MapAroundView view;


    SerialDisposable placeNukeDisposable = new SerialDisposable();
    SerialDisposable refreshNukesDisposable = new SerialDisposable();

    public MapAroundPresenter() {

    }

    public void singleShot() {
        Disposable d = GetNukesUseCase.INSTANCE.getApi().getNukes().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(latLngs -> {
                    System.err.println("accept bounds " + Arrays.toString(latLngs.toArray()));
                    if (view != null) {
                        view.onReceivedNukes(latLngs);
                    }
                }, throwable -> {
                    System.err.println("error " + throwable);

                    if (view != null) {
                        view.onPlaceReceiveError();
                    }
                });
    }

    public void startNukesInterval() {
        refreshNukesDisposable.set(GetNukesUseCase.INSTANCE.refreshNukesEachTwoSeconds()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(latLngs -> {

                    System.err.println("accept bounds " + Arrays.toString(latLngs.toArray()));
                    if (view != null) {
                        view.onReceivedNukes(latLngs);
                    }
                }, throwable -> {
                    System.err.println("error " + throwable);

                    if (view != null) {
                        view.onPlaceReceiveError();
                    }
                }));
    }

    public void stopNukesInterval() {
        refreshNukesDisposable.dispose();
        placeNukeDisposable.dispose();
        refreshNukesDisposable = new SerialDisposable();
        placeNukeDisposable = new SerialDisposable();
    }

    public void requestMoveNuke(Integer index) {
        view.showMoveNukeDialog(index);
    }

    public void moveNuke(@Nullable Integer lastIndexInUse, @NotNull NukeDirection direction) {
        Log.d(TAG, "moveNuke() called with: lastIndexInUse = [" + lastIndexInUse + "], direction = [" + direction + "]");
        if (lastIndexInUse == null) {
            view.onPlaceNukeError();
        } else {
            placeNukeDisposable.set(GetNukesUseCase.INSTANCE.moveNuke(new MoveNukeRequest(lastIndexInUse, direction))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorResumeNext(throwable -> {
                        if (throwable instanceof EOFException) {
                            return Observable.just(new EmptyResponse());
                        } else {
                            return Observable.error(throwable);
                        }
                    })
                    .subscribe(emptyResponse -> {
                        System.err.println("nuke placed!");
                        singleShot();
                    }, throwable -> {
                        System.err.println("nuke error: " + throwable);
                        if (view != null) {
                            view.onPlaceNukeError();
                        }
                    }));
        }
    }
}
