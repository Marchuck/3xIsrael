package com.marchuck.a3xisrael.api

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NukeApiImpl(endpoint: String) : NukeApi {

    val api = Retrofit.Builder()
            .baseUrl(endpoint)
            .client(buildClient(15))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NukeApi::class.java)

    private fun buildClient(timeoutInSeconds: Long): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val builder = OkHttpClient.Builder()
                .writeTimeout(timeoutInSeconds, TimeUnit.SECONDS)
                .readTimeout(timeoutInSeconds, TimeUnit.SECONDS)
                .connectTimeout(timeoutInSeconds, TimeUnit.SECONDS)

        builder.addNetworkInterceptor(loggingInterceptor)

        return builder.build()
    }

    override fun getNukes() = api.getNukes()

    override fun moveNuke(moveNukeRequest: MoveNukeRequest) = api.moveNuke(moveNukeRequest)

}