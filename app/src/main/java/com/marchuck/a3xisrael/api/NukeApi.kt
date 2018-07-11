package com.marchuck.a3xisrael.api

import com.google.android.gms.maps.model.LatLng
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NukeApi {

    @GET("getNukes")
    fun getNukes(): Observable<ArrayList<LatLng>>

    @POST("moveNuke")
    fun moveNuke(@Body moveNukeRequest: MoveNukeRequest): Observable<EmptyResponse>
}