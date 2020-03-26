package io.hkhc.httptest

import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class DogApiFactory(
    private val clientBuilder: OkHttpClient.Builder,
    private val baseUrl: String
) {

    fun createApi(): DogApi {

        val okhttpClient = clientBuilder.build()

        // Creeate API that map the Kotlin interface to proper HTTP request
        val retrofit = Retrofit.Builder()
            .client(okhttpClient)
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        return retrofit.create(DogApi::class.java)

    }

}