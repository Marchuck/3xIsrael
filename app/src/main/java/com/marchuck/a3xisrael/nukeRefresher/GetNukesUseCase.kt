package com.marchuck.a3xisrael.nukeRefresher

import com.marchuck.a3xisrael.NukeApp
import com.marchuck.a3xisrael.api.MoveNukeRequest
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

object GetNukesUseCase {

    fun refreshNukesEachTwoSeconds() = Observable.interval(1, TimeUnit.SECONDS)
            .switchMap { getApi().getNukes() }
            .doOnEach { println("received nukes ### $it ###") }


    fun moveNuke(moveNukeRequest: MoveNukeRequest) = getApi().moveNuke(moveNukeRequest)

    fun getApi() = NukeApp.get().apiClient

}