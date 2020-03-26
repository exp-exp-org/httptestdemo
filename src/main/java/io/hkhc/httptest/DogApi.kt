package io.hkhc.httptest

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DogApi {
    @GET("/api/breed/{dogType}/images")
    fun listDogsByType(@Path("dogType") dogType: String): Single<Response<String>>
}


